package com.example.schoolhub_android.PushNotification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.schoolhub_android.Common.Common;

public class PushNotification extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CreateNotificationChannels();
    }

    private void CreateNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    Common.CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Channel 1");

            NotificationChannel channel2 = new NotificationChannel(
                    Common.CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel2.setDescription("Channel 2");

            NotificationChannel channel3 = new NotificationChannel(
                    Common.CHANNEL_3_ID,
                    "Channel 3",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel2.setDescription("Channel 3");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
            manager.createNotificationChannel(channel3);
        }
    }
}