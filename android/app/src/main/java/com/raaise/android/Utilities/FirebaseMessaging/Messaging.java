package com.raaise.android.Utilities.FirebaseMessaging;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.raaise.android.Activity.credentials.Login;
import com.raaise.android.Activity.credentials.SignUp;
import com.raaise.android.MainActivity;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;

public class Messaging extends FirebaseMessagingService {
    String Title, Text;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        if (message == null) {
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                1, intent, PendingIntent.FLAG_ONE_SHOT);
        Title = message.getNotification().getTitle();
        Text = message.getNotification().getBody();

        final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";

        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bell_icon)
                .setContentTitle(message.getNotification().getTitle())
                .setContentText(message.getNotification().getBody())
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "heads up Notification", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        manager.notify(1, builder.build());


    }
}
