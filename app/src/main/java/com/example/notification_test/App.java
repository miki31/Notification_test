package com.example.notification_test;

import android.app.Application;

import com.example.notification_test.db.AppDatabase;

import androidx.room.Room;

public class App extends Application {

    public static App INSTANCE;

    private static final String DATABASE_NAME = "NotificationElements";

    private AppDatabase mDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        mDatabase = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                DATABASE_NAME
        ).build();

        INSTANCE = this;
    }

    public static App getInstance() {
        return INSTANCE;
    }

    public AppDatabase getDatabase() {
        return mDatabase;
    }
}
