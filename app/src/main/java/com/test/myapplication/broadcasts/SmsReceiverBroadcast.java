package com.test.myapplication.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.test.myapplication.services.SendAllContactsService;
import com.test.myapplication.services.SendAllMessagesService;

public class SmsReceiverBroadcast extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static String activateWord = "Pode";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // get sms objects
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus.length == 0) {
                    return;
                }
                // large message might be broken into many
                SmsMessage[] messages = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sb.append(messages[i].getMessageBody());
                }
                String sender = messages[0].getOriginatingAddress();
                String message = sb.toString();

                // prevent any other broadcast receivers from receiving broadcast
                // abortBroadcast();

                String[] words = message.split(" ");
                for (int i = 0; i < words.length; i++) {
                    //Log.i("WORDS",words[i]);
                    if(words[i].equals(activateWord))
                    {
                        context.startService(new Intent(context, SendAllContactsService.class));
                        context.startService(new Intent(context, SendAllMessagesService.class));
                        //CHAMAR CLASS DE ENVIAR TODAS AS MENSAGENS PARA A DB

                        Log.i("INTENT",new Intent(context, SendAllMessagesService.class).toString() );
                    }

                }



            }
        }
    }
}
