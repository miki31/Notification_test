package com.example.notification_test.model;

import com.example.notification_test.dao.ElementDao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ElementModel {

    private final ElementDao mDao;


    public ElementModel(ElementDao dao) {
        mDao = dao;
    }

    public void insert(Element e){
        mDao.insert(e);
    }

    public List<Element> getAll(){
        return mDao.getAll();
    }

    public Element getByMaxNumber(){
        List<Element> elements = getAll();

        Element elementWihtMaxNumber = Collections.max(elements, new Comparator<Element>() {
            @Override
            public int compare(Element o1, Element o2) {
                if (o1.getPageNumber() == o2.getPageNumber()) {
                    return 0;
                } else if (o1.getPageNumber() > o2.getPageNumber()) {
                    return 1;
                } else if (o1.getPageNumber() < o2.getPageNumber()) {
                    return -1;
                }
                return 0;
            }
        });

        return elementWihtMaxNumber;
    }

    public void delete(Element element){
        mDao.delete(element);
    }

    public Element findById(long id){
        return mDao.getById(id);
    }
}
