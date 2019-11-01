package com.example.notification_test;

import android.app.Application;

import com.example.notification_test.dao.ElementDao;
import com.example.notification_test.db.AppDatabase;
import com.example.notification_test.model.Element;

import java.util.ArrayList;
import java.util.List;

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

    public List<Element> initDB() {

        List<Element> mElements;

        ElementDao eDAO = App.getInstance().getDatabase().mElementDao();

        mElements = eDAO.getAll();

        if (mElements.size() < 1) {
            createFirstElement();
            mElements = eDAO.getAll();
        }
        return mElements;
    }

    private void createFirstElement() {
        ElementDao eDAO = App.getInstance().getDatabase().mElementDao();

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
