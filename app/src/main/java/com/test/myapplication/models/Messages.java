package com.test.myapplication.models;

import org.json.JSONObject;

public class Messages {
    public String message;
    public String phoneNumber;
    public String uri = "http://192.168.1.92:80/shutdown/post_android_message.php";

    public Messages(String message, String phoneNumber) {
        this.message = message;
        this.phoneNumber = phoneNumber;
    }

    public JSONObject putsFieldsInJson() {
        JSONObject json = new JSONObject();
        {
            try {
                json.put("message", message);
                json.put("phone_number", phoneNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }


    }
}
