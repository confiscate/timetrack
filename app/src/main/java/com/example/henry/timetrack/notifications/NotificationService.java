package com.example.henry.timetrack.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.henry.timetrack.StorageManager;

/**
 * Created by silvio on 4/26/15.
 */
public class NotificationService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationsManager.sendNotification(this);

        // stop service so it does not stay in memory
        stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        StorageManager storageManager = new StorageManager();
        if (!storageManager.getNotificationsActive(this)) {
            return;
        }
        String hours = storageManager.getNotificationsPeriod(this);
        int minutes = (int) (Double.parseDouble(hours) * 60);
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm.set(
            alarm.RTC_WAKEUP,
            System.currentTimeMillis() + (minutes * 60 * 1000),
            PendingIntent.getService(this, 0, new Intent(this, NotificationService.class), 0)
        );
    }
}
