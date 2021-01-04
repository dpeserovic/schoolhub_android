package com.example.schoolhub_android.Common;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Common {
    public static final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public static final DatabaseReference mSchoolsRef = mRootRef.child("schools");
    public static final DatabaseReference mNotificationsRef = mRootRef.child("notifications");

    public static final String MY_SCHOOL = "MY_SCHOOL";
    public static final String ROOM_DATABASE = "schoolhub";

    public static final String CHANNEL_1_ID = "Channel 1";
    public static final String CHANNEL_2_ID = "Channel 2";
    public static final String CHANNEL_3_ID = "Channel 3";
}