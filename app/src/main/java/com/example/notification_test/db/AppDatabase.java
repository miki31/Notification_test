package com.example.notification_test.db;

import com.example.notification_test.dao.ElementDao;
import com.example.notification_test.model.Element;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Element.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ElementDao mElementDao();

}
