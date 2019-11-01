package com.example.notification_test.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Element {

    @PrimaryKey(autoGenerate = true)
//    @PrimaryKey
    private long id;
    private int mPageNumber;

    public Element() {
    }

    public Element(int number) {
        this.mPageNumber = number;
    }

    public int getPageNumber() {
        return mPageNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPageNumber(int pageNumber) {
        mPageNumber = pageNumber;
    }
}
