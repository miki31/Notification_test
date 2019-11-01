package com.example.notification_test.dao;

import com.example.notification_test.model.Element;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ElementDao {

    @Query("SELECT * FROM element")
    List<Element> getAll();

    @Query("SELECT * FROM element WHERE id = :id")
    Element getById(long id);

    @Query("SELECT * FROM element WHERE mPageNumber = :mPageNumber")
    Element getElementByPageNumber(int mPageNumber);

    @Insert
    void insert(Element element);

    @Update
    void update(Element element);

    @Delete
    void delete(Element element);

}
