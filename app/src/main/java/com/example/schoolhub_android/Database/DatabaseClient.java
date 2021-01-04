package com.example.schoolhub_android.Database;

import android.content.Context;

import androidx.room.Room;

import com.example.schoolhub_android.Common.Common;

public class DatabaseClient {
    private Context mContext;
    private AppDatabase appDatabase;
    private static DatabaseClient mInstance;

    private DatabaseClient(Context mContext) {
        this.mContext = mContext.getApplicationContext();
        appDatabase = Room.databaseBuilder(mContext, AppDatabase.class, Common.ROOM_DATABASE).allowMainThreadQueries().build();
    }

    public static synchronized DatabaseClient getInstance(Context mContext) {
        if(mInstance == null) {
            mInstance = new DatabaseClient(mContext);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}