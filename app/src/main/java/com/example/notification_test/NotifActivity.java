package com.example.notification_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.widget.TextView;

import com.example.notification_test.model.Element;
import com.example.notification_test.model.NotifFragment;

import java.util.ArrayList;
import java.util.List;

public class NotifActivity extends AppCompatActivity {


//    @BindView(R.id.textView)
//    TextView mTextView;

    private List<Element> mElements;

    @BindView(R.id.notif_view_pager)
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);
        ButterKnife.bind(this);
//        mTextView.setText("Hello NOTIFICATION app!!!");

        // only for test
        testElements();

        createViewPager();
    }

    // only for test
    private void testElements(){
        mElements = new ArrayList<>();
        Element element;

        for (int i = 1; i <= 5; i++){
            element = new Element(i);
            mElements.add(element);
        }
    }

    private void createViewPager(){
        FragmentManager manager = getSupportFragmentManager();
        MyFragmentStateAdapter adapter =
                new MyFragmentStateAdapter(manager,
                        FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT); // ?????????Lifecycle.State.RESUMED
        mViewPager.setAdapter(adapter);
        for (int i = 0; i < mElements.size(); i++) {
            Fragment f = new NotifFragment();
        }
    }


    private class MyFragmentStateAdapter
        extends FragmentStatePagerAdapter{


        public MyFragmentStateAdapter(
                @NonNull FragmentManager fm,
                int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return new NotifFragment();
        }

        @Override
        public int getCount() {
            return mElements.size();
        }
    }
}
