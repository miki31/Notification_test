package com.example.notification_test.presenter;

import com.example.notification_test.main_activity.NotifActivity;
import com.example.notification_test.model.Element;
import com.example.notification_test.model.ElementModel;

import java.util.List;

public class NotifyPresenter {

    private NotifActivity mViewActivity;
    private final ElementModel model;

    public NotifyPresenter(ElementModel model) {
        this.model = model;
    }

    public void attachView(NotifActivity notifActivity) {
        this.mViewActivity = notifActivity;
    }

    public void detachView() {
        mViewActivity = null;
    }

    public void createNewElement(){
        new Thread(() -> {
            Element eMax = model.getByMaxNumber();

            Element e = new Element();
            e.setPageNumber(eMax.getPageNumber() + 1);
            model.insert(e);

            updateView();
        }).start();
    }

    private void updateView(){
        List<Element> elements = model.getAll();

        mViewActivity.updateElements(elements);
    }
}
