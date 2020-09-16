package com.example.bobo_hello;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class PushService extends FirebaseMessagingService {
    private final String TAG = "FirebaseTAG";
    int messageId = 0;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "Refreshed token: " + s);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title;
        if (Objects.requireNonNull(remoteMessage.getNotification()).getTitle() == null){
            title = "Push Message";
        } else {
            title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        }

        String text = remoteMessage.getNotification().getBody();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "2")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(text);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
}