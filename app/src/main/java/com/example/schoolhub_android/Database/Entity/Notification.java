package com.example.schoolhub_android.Database.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

@Entity(tableName = "notifications")
public class Notification {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "key")
    private String key;

    @ColumnInfo(name = "school_key")
    private String school_key;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "body")
    private String body;

    @ColumnInfo(name = "expanded", defaultValue = "0")
    private boolean expanded;

    @ColumnInfo(name = "read", defaultValue = "0")
    private boolean read;

    @NonNull
    public String getKey() {
        return key;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }

    public String getSchool_key() {
        return school_key;
    }

    public void setSchool_key(String school_key) {
        this.school_key = school_key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}