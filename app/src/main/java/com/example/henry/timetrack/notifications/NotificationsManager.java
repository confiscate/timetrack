package com.example.henry.timetrack.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.henry.timetrack.MainActivity;
import com.example.henry.timetrack.R;

/**
 * Created by silvio on 4/26/15.
 */
public class NotificationsManager {
    private static int NOTIFY_ID = 1;

    public static void sendNotification(Context context) {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher_white)
                        .setContentTitle("TimeTrack")
                        .setContentText("What happened since the last reminder?")
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra(MainActivity.EDIT_LATEST, true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
    }

    public static void turnOnPeriodicNotifications(Context context) {
        context.startService(new Intent(context, NotificationService.class));
    }
}
