package com.example.schoolhub_android.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolhub_android.Adapter.RecyclerNotificationAdapter;
import com.example.schoolhub_android.Common.Common;
import com.example.schoolhub_android.Database.DatabaseClient;
import com.example.schoolhub_android.Database.Entity.Notification;
import com.example.schoolhub_android.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerNotificationAdapter adapter;
    ImageView iv_schoolhub;
    TextView tv_no_notifications_yet;
    SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferences sharedPreferences;
    String school_key, school_name, school_address, school_city;
    boolean isNewSchool;
    Set<String> s_isRead = new HashSet<>();
    Set<String> set = new HashSet<>();
    SharedPreferences.Editor editor;

    Notification notification;
    List<Notification> l_notifications = new ArrayList<>();
    String key;
    List<String> l_keys = new ArrayList<>();

    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault());
    String currentDate = sdf.format(c.getTime());

    NotificationManagerCompat notificationManager;
    android.app.Notification androidAppNotification;

    @Override
    protected void onStart() {
        super.onStart();

        sharedPreferences = getSharedPreferences(Common.MY_SCHOOL, MODE_PRIVATE);
        school_key = sharedPreferences.getString("key", null);
        school_name = sharedPreferences.getString("name", null);
        school_address = sharedPreferences.getString("address", null);
        school_city = sharedPreferences.getString("city", null);
        isNewSchool = sharedPreferences.getBoolean("isNewSchool", true);
        s_isRead = sharedPreferences.getStringSet("s_isRead", set);
        if(isNewSchool) {
            ChannelOneNotification();
            editor = getSharedPreferences(Common.MY_SCHOOL, MODE_PRIVATE).edit();
            editor.putBoolean("isNewSchool", false);
            editor.apply();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.rv_notifications);

        iv_schoolhub = findViewById(R.id.iv_schoolhub);
        tv_no_notifications_yet = findViewById(R.id.tv_no_notifications_yet);

        notificationManager = NotificationManagerCompat.from(this);

        if(isNetworkConnected()) {
            ClearDatabase();
        }
        else {
            GetAllNotifications();
            InitRecyclerView();
            ChannelTwoNotification();
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }
        FirebaseEventListener();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isNetworkConnected()) {
                    ClearDatabase();
                    GetSharedPreferences();
                    FirebaseEventListener();
                }
                else {
                    Toast.makeText(NotificationActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void FirebaseEventListener() {
        Common.mNotificationsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                notification = dataSnapshot.getValue(Notification.class);
                key = dataSnapshot.getKey();
                assert notification != null;
                if (school_key.equals(notification.getSchool_key())) {
                    l_keys.add(key);
                    notification.setKey(key);
                    if(s_isRead.contains(key)) {
                        notification.setRead(true);
                    }
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .notificationDao()
                            .insertNotification(notification);
                }
                GetAllNotifications();
                InitRecyclerView();
                ChannelTwoNotification();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                notification = dataSnapshot.getValue(Notification.class);
                key = dataSnapshot.getKey();
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .notificationDao()
                        .updateNotification(notification.getTitle(), notification.getBody(), key);
                GetAllNotifications();
                InitRecyclerView();
                androidAppNotification = new NotificationCompat.Builder(getApplicationContext(), Common.CHANNEL_3_ID)
                        .setSmallIcon(R.mipmap.schoolhub_round)
                        .setContentTitle(getString(R.string.channel3_notification_title, notification.getTitle()))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.channel3_notification_text, notification.getBody())))
                        .build();
                notificationManager.notify((int)((new Date().getTime() / 1000L) % Integer.MAX_VALUE), androidAppNotification);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                key = dataSnapshot.getKey();
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .notificationDao()
                        .deleteNotification(key);
                GetAllNotifications();
                InitRecyclerView();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void ChannelOneNotification() {
        androidAppNotification = new NotificationCompat.Builder(this, Common.CHANNEL_1_ID)
                .setSmallIcon(R.mipmap.schoolhub_round)
                .setContentTitle(getString(R.string.channel1_notification_title, school_name))
                .setContentText(getString(R.string.channel1_notification_text, school_address, school_city))
                .build();
        notificationManager.notify(1, androidAppNotification);
    }

    public void ChannelTwoNotification() {
        for(int i=0;i<l_notifications.size();i++) {
            if(currentDate.equals(l_notifications.get(i).getDate()) && !l_notifications.get(i).isRead()) {
                androidAppNotification = new NotificationCompat.Builder(getApplicationContext(), Common.CHANNEL_2_ID)
                        .setSmallIcon(R.mipmap.schoolhub_round)
                        .setContentTitle(getString(R.string.channel2_notification_title, l_notifications.get(i).getTitle()))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.channel2_notification_text, l_notifications.get(i).getBody())))
                        .build();
                notificationManager.notify(i, androidAppNotification);
            }
        }
    }

    public void GetAllNotifications() {
        l_notifications = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                .notificationDao()
                .getAllNotifications();
    }

    public void InitRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        adapter = new RecyclerNotificationAdapter(l_notifications, this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setAdapter(adapter);

        if(l_notifications.size() == 0) {
            iv_schoolhub.setVisibility(View.VISIBLE);
            tv_no_notifications_yet.setVisibility(View.VISIBLE);
        }
        else {
            iv_schoolhub.setVisibility(View.GONE);
            tv_no_notifications_yet.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.schoolInfo:
                StartSchoolInfoActivity();
                return true;
            case R.id.markAllAsRead:
                MarkAllAsRead();
                return true;
            case R.id.markAllAsUnread:
                MarkAllAsUnread();
                return true;
            case R.id.unfollowSchool:
                UnfollowSchool();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
            invalidateOptionsMenu();
            if(l_notifications.size() == 0) {
                menu.findItem(R.id.markAllAsRead).setVisible(false);
                menu.findItem(R.id.markAllAsUnread).setVisible(false);
            }
            else {
                menu.findItem(R.id.markAllAsRead).setVisible(true);
                menu.findItem(R.id.markAllAsUnread).setVisible(true);
            }
            return super.onPrepareOptionsMenu(menu);
    }

    public void StartSchoolInfoActivity() {
        Intent i = new Intent(this, SchoolInfoActivity.class);
        startActivity(i);
    }

    public void MarkAllAsRead() {
        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                .notificationDao()
                .markAllAsRead();
        GetAllNotifications();
        InitRecyclerView();
        for(int i=0;i<l_notifications.size();i++) {
            set.add(l_notifications.get(i).getKey());
        }
        editor = getSharedPreferences(Common.MY_SCHOOL, MODE_PRIVATE).edit();
        editor.putStringSet("s_isRead", set);
        editor.apply();
    }

    public void MarkAllAsUnread() {
        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                .notificationDao()
                .markAllAsUnread();
        GetAllNotifications();
        InitRecyclerView();
        set.clear();
        editor = getSharedPreferences(Common.MY_SCHOOL, MODE_PRIVATE).edit();
        editor.putStringSet("s_isRead", set);
        editor.apply();
    }

    public void GetSharedPreferences() {
        sharedPreferences = getSharedPreferences(Common.MY_SCHOOL, MODE_PRIVATE);
        s_isRead = sharedPreferences.getStringSet("s_isRead", set);
    }

    public void UnfollowSchool() {
        ClearSharedPreferences();
        ClearArrays();
        ClearDatabase();
        StartMainActivity();
    }

    public void ClearSharedPreferences() {
        editor = getSharedPreferences(Common.MY_SCHOOL, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    public void ClearArrays() {
        l_notifications.clear();
        l_keys.clear();
    }

    public void ClearDatabase() {
        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                .notificationDao()
                .deleteAll();
    }

    public void StartMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}