package com.test.myapplication.models;

import org.json.JSONObject;

public class Contacts {
    public String phoneName;
    public String phoneNumber;
    public String uri = "http://192.168.1.92:80/shutdown/post_android_number.php";

    public Contacts(String phoneName, String phoneNumber) {
        this.phoneName = phoneName;
        this.phoneNumber = phoneNumber;
    }

    public JSONObject putsFieldsInJson() {
        JSONObject json = new JSONObject();
        {
            try {
                json.put("phone_name", phoneName);
                json.put("phone_number", phoneNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }


    }
}
