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

import com.example.notification_test.dao.ElementDao;
import com.example.notification_test.model.Element;
import com.example.notification_test.model.NotifFragment;

import java.util.ArrayList;
import java.util.List;

public class NotifActivity extends AppCompatActivity {

    public static final String ARG_NOTIFICATION_ID = "notification_id";
    private static final long DEFAULT_NOTIFICATION_ID = -1;

    private List<Element> mElements;

    @BindView(R.id.notif_view_pager)
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);
        ButterKnife.bind(this);

        initDB();

        long id = getIntent().getLongExtra(ARG_NOTIFICATION_ID, DEFAULT_NOTIFICATION_ID);

        if (id != DEFAULT_NOTIFICATION_ID){
            // search in DB by id
            for (int i = 0; i < mElements.size(); i++) {
                if (id == mElements.get(i).getId()){
                    mViewPager.setCurrentItem(i);
                    break;
                }
            }
        }
    }
    private void initDB(){
        new Thread(() -> {

            mElements = App.getInstance().initDB();

            createViewPager();
        }).start();

    }

    private void createViewPager(){
        runOnUiThread(() -> {
            FragmentManager manager = getSupportFragmentManager();
            MyFragmentStateAdapter adapter =
                    new MyFragmentStateAdapter(manager,
                            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT); // ?????????Lifecycle.State.RESUMED
            mViewPager.setAdapter(adapter);
            for (int i = 0; i < mElements.size(); i++) {
                Fragment f = new NotifFragment(mElements.get(i));
                adapter.addFragment(f);
            }
        });

    }


    private class MyFragmentStateAdapter
        extends FragmentStatePagerAdapter{

        List<Fragment> mFragments;

        public MyFragmentStateAdapter(
                @NonNull FragmentManager fm,
                int behavior) {
            super(fm, behavior);
            mFragments = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Element element = mElements.get(position);
            return NotifFragment.newInstance(element);
        }

        @Override
        public int getCount() {
            return mElements.size();
        }

        public void addFragment(Fragment f){
            mFragments.add(f);

        }
    }
}
