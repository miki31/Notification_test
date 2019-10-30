package com.example.notification_test;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.widget.TextView;

public class NotifActivity extends AppCompatActivity {


//    @BindView(R.id.textView)
//    TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);
        ButterKnife.bind(this);
//        mTextView.setText("Hello NOTIFICATION app!!!");
    }
}
