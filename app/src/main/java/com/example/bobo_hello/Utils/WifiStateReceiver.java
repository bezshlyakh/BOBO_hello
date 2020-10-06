package com.example.bobo_hello.Utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.bobo_hello.R;

public class WifiStateReceiver extends BroadcastReceiver {
    private int messageId = 0;
    private String channelID = "2";

    @Override
    public void onReceive(Context context, Intent intent) {

        int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                WifiManager.WIFI_STATE_UNKNOWN);
        Log.d("RECEIVER", "intent " + extraWifiState);


        if(extraWifiState == WifiManager.WIFI_STATE_ENABLED){

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                    .setSmallIcon(R.drawable.temp_icon)
                    .setContentTitle("Broadcast Receiver")
                    .setContentText("WiFi connected");
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(messageId++, builder.build());

         }
    }
}
