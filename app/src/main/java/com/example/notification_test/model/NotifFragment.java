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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NotifFragment
        extends Fragment {
    private static final String ARG_NOTIFICATION_ID = "notification_id";

    Unbinder mUnbinder;

    private NotifyPresenter mPresenter;


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
        args.putLong(ARG_NOTIFICATION_ID, element.getId());

        NotifFragment fragment = new NotifFragment(element);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mElement = new Element();
//        long id = getArguments().getLong(ARG_NOTIFICATION_ID);
//        mElement.setPageNumber((int) id);
//        mElement.setId(id);
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

        mBntCreateNotify.setText("id " + mElement.getId());
        mTvNumberFragment.setText("page " + mElement.getPageNumber());

        return viewGroup;
    }

    @OnClick(R.id.btnCreateNotify)
    void createNotification() {
        mTvNumberFragment.setText("new N : " + mElement.getPageNumber());

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmapIcon = BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.ic_launcher,
                options
        );

        Intent resultIntent = new Intent(getActivity().getApplicationContext(),
                NotifActivity.class);
        resultIntent.putExtra(NotifActivity.ARG_NOTIFICATION_ID,
                mElement.getPageNumber());
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getActivity().getApplicationContext(),
                        mElement.getPageNumber(),
                        resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getContext(), null)
                        .setSmallIcon(android.R.drawable.ic_dialog_email)
                        .setLargeIcon(bitmapIcon)
                        .setContentTitle("Chat heads active" + mElement.getPageNumber())
                        .setContentText("Notification " + mElement.getPageNumber())
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
        mTvNumberFragment.setText("delete " + mElement.getPageNumber());

        mPresenter.deleteElementById(mElement);

        // delete notification with this pageNumber
        NotificationManager notificationManager =
                (NotificationManager) getActivity()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(mElement.getPageNumber());
    }

    @OnClick(R.id.imgBntPlus)
    void createNewFragment() {
        mTvNumberFragment.setText("create new fargment " + mElement.getPageNumber());

//        getContext().get
        mPresenter.createNewElement();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
