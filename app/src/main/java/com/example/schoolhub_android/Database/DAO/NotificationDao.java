package com.example.schoolhub_android.Database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.schoolhub_android.Database.Entity.Notification;

import java.util.List;

@Dao
public interface NotificationDao {
    @Query("SELECT * FROM notifications")
    List<Notification> getAllNotifications();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNotification(Notification notification);

    @Query("UPDATE notifications SET read = 1 WHERE `key` = :key")
    void updateRead(String key);

    @Query("UPDATE notifications SET title = :title, body = :body WHERE `key` = :key")
    void updateNotification(String title, String body, String key);

    @Query("DELETE FROM notifications WHERE `key` = :key")
    void deleteNotification(String key);

    @Query("UPDATE notifications SET read = 1 WHERE read = 0")
    void markAllAsRead();

    @Query("UPDATE notifications SET read = 0 WHERE read = 1")
    void markAllAsUnread();

    @Query("DELETE FROM notifications")
    void deleteAll();
}