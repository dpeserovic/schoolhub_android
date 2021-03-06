package com.example.schoolhub_android.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.schoolhub_android.Database.DAO.NotificationDao;
import com.example.schoolhub_android.Database.Entity.Notification;

@Database(entities = {Notification.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NotificationDao notificationDao();
}
