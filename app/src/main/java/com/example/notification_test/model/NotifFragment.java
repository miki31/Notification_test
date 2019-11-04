package com.example.notification_test.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.notification_test.main_activity.NotifActivity;
import com.example.notification_test.R;
import com.example.notification_test.presenter.NotifyPresenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NotifFragment
        extends Fragment {

    Unbinder mUnbinder;

    private NotifyPresenter mPresenter;

    @BindString(R.string.text_notification)
    String textNotif;
    @BindString(R.string.title_notification)
    String titleNotif;


    @BindView(R.id.btnCreateNotify)
    Button mBntCreateNotify;
    @BindView(R.id.imgBntMinus)
    ImageButton mImBtnMinus;
    @BindView(R.id.imgBntPlus)
    ImageButton mImBtnPlus;
    @BindView(R.id.tvPageNum)
    TextView mTvNumberFragment;


    private Element mElement;

    public Element getElement() {
        return mElement;
    }

    private NotifFragment(Element element) {
        this.mElement = element;
    }

    public void setPresenter(NotifyPresenter presenter) {
        this.mPresenter = presenter;
    }

    public static NotifFragment newInstance(Element element) {
        Bundle args = new Bundle();
        args.putLong(NotifActivity.ARG_NOTIFICATION_ID, element.getId());

        NotifFragment fragment = new NotifFragment(element);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(
                R.layout.fragment_notif,
                container, false
        );

        mUnbinder = ButterKnife.bind(this, viewGroup);

        mTvNumberFragment.setText("" + mElement.getPageNumber());

        return viewGroup;
    }

    @OnClick(R.id.btnCreateNotify)
    void createNotification() {
        // create large Icon for Notification
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmapIcon = BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.ic_launcher,
                options
        );

        // to send an intent message (to be opened)
        Intent resultIntent = new Intent(getActivity().getApplicationContext(),
                NotifActivity.class);
        resultIntent.putExtra(NotifActivity.ARG_NOTIFICATION_ID,
                mElement.getId());
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getActivity().getApplicationContext(),
                        mElement.getPageNumber(),
                        resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
                );

        // build Notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getContext(), null)
                        .setSmallIcon(android.R.drawable.ic_dialog_email)
                        .setLargeIcon(bitmapIcon)
                        .setContentTitle(titleNotif)
                        .setContentText(textNotif + " " + mElement.getPageNumber())
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true);

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) getActivity()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mElement.getPageNumber(), notification);
    }

    @OnClick(R.id.imgBntMinus)
    void deleteFragment() {
        mPresenter.deleteElementById(mElement);

        // delete notification with this pageNumber
        NotificationManager notificationManager =
                (NotificationManager) getActivity()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(mElement.getPageNumber());
    }

    @OnClick(R.id.imgBntPlus)
    void createNewFragment() {
        mPresenter.createNewElement();
    }

    public void setBtnMinusVisible(boolean visible){
        if (visible){
            mImBtnMinus.setVisibility(View.VISIBLE);
        } else {
            mImBtnMinus.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
