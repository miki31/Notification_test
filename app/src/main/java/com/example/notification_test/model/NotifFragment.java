package com.example.notification_test.model;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.notification_test.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NotifFragment
        extends Fragment {
    private static final String ARG_NOTIFICATION_ID = "notification_id";

    Unbinder mUnbinder;

    @BindView(R.id.btnCreateNotify) Button mBntCreateNotify;
    @BindView(R.id.imgBntMinus) ImageButton mImBtnMinus;
    @BindView(R.id.imgBntPlus) ImageButton mImBtnPlus;
    @BindView(R.id.tvPageNum)TextView mTvNumberFragment;


    private Element mElement;


    public static NotifFragment newInstance(Element element) {
        Bundle args = new Bundle();
        args.putLong(ARG_NOTIFICATION_ID, element.getId());

        NotifFragment fragment = new NotifFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mElement = new Element();
        long id = getArguments().getLong(ARG_NOTIFICATION_ID);
        mElement.setPageNumber((int) id);
        mElement.setId(id);
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

        mBntCreateNotify.setText("create " + mElement.getPageNumber());
        mTvNumberFragment.setText("page " + mElement.getPageNumber());

        return viewGroup;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
