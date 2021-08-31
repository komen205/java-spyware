package com.test.myapplication.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SendToDBService<jstring> extends Service
{
    public JSONObject json;
    public String uri;
    private RequestQueue mRequestQueue;


    public SendToDBService(JSONObject json, String uri){
        this.json = json;
        this.uri = uri;

    }
    static {
        System.loadLibrary("myapplication");
    }
    public native String stringFromJNI(String test);



    protected SendToDBService perform(Context context){
        //Log.i("DEBUG", stringFromJNI(this.json.toString()));

        final String requestBody = stringFromJNI(this.json.toString());


        RequestQueue mRequestQueue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.uri,
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
        mRequestQueue.add(stringRequest);
        return this;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
