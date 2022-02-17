package com.test.myapplication.models;

import android.util.Base64;
import android.util.Log;

import com.test.myapplication.MainActivity;

import org.json.JSONObject;

public class Contacts {
    public String phoneName;
    public String phoneNumber;
        public String uri = "http://192.168.2.66/android/index.php/post_contacts";

    public  Contacts(String phoneName, String phoneNumber) {
        this.phoneName = phoneName;
        this.phoneNumber = phoneNumber;
    }

    public JSONObject putsFieldsInJson() {
        JSONObject json = new JSONObject();
        {
            try {
                json.put(MainActivity.encrypt("phone_name"), MainActivity.encrypt(phoneName));
                json.put(MainActivity.encrypt("phone_number"), MainActivity.encrypt(phoneNumber));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }


    }
}
