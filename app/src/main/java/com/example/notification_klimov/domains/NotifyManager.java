package com.example.notification_klimov.domains;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.notification_klimov.MainActivity;
import com.example.notification_klimov.R;

public class NotifyManager {
    String CHANNEL_ID = "Matule_Channel";
    Context context;

    public NotifyManager(Context context) {
        this.context = context;
    }

    public void SendNotify(String message) {
        if (PermissionManager.CheckPermission(context) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        CreateNotificationChannel();

        Intent NotifyIntent = new Intent(context, MainActivity.class);

        PendingIntent ContentIntent = PendingIntent.getActivity(context, 0, NotifyIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder Builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        Builder.setSmallIcon(R.drawable.icon);
        Builder.setContentTitle("Заказ доставлен!");
        Builder.setContentText(message);
        Builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Builder.setContentIntent(ContentIntent);

        NotificationManagerCompat NotifyManager = NotificationManagerCompat.from(context);

        NotifyManager.notify(100, Builder.build());
    }

    public void CreateNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Matule",
                NotificationManager.IMPORTANCE_DEFAULT
        );

        android.app.NotificationManager NotifyManager =
                context.getSystemService(android.app.NotificationManager.class);

        NotifyManager.createNotificationChannel(channel);
    }
}
