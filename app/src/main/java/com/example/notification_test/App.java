package com.example.notification_test;

import android.app.Application;

import com.example.notification_test.dao.ElementDao;
import com.example.notification_test.db.AppDatabase;
import com.example.notification_test.model.Element;

import java.util.List;

import androidx.room.Room;

public class App extends Application {

    private static App instance;

    private static final String DATABASE_NAME = "NotificationElements";

    private AppDatabase mDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        createDB();

        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    private void createDB(){
        mDatabase = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                DATABASE_NAME
        ).build();
    }

    public void initDB() {
        ElementDao eDAO = mDatabase.mElementDao();

        List<Element> mElements = eDAO.getAll();

        if (mElements.size() < 1) {
            createFirstElement();
        }
    }

    // create Elements if application opened first time
    private void createFirstElement() {
        ElementDao eDAO = mDatabase.mElementDao();

        Element element;

        for (int i = 1; i <= 2; i++) {
            element = new Element();
            element.setPageNumber(i);
            eDAO.insert(element);
        }
    }

    public AppDatabase getDatabase() {
        return mDatabase;
    }
}
