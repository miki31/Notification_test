package com.example.notification_test.main_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;

import com.example.notification_test.App;
import com.example.notification_test.R;
import com.example.notification_test.dao.ElementDao;
import com.example.notification_test.model.Element;
import com.example.notification_test.model.ElementModel;
import com.example.notification_test.model.NotifFragment;
import com.example.notification_test.presenter.NotifyPresenter;

import java.util.ArrayList;
import java.util.List;

public class NotifActivity extends AppCompatActivity {

    public static final String ARG_NOTIFICATION_ID = "notification_id";
    private static final long DEFAULT_NOTIFICATION_ID = -1;

    private List<Element> mElements;

    private NotifyPresenter mPresenter;

    private MyFragmentStateAdapter mAdapter;

    @BindView(R.id.notif_view_pager)
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);
        ButterKnife.bind(this);

        initDB();

        long id = getIntent().getLongExtra(ARG_NOTIFICATION_ID, DEFAULT_NOTIFICATION_ID);

        if (id != DEFAULT_NOTIFICATION_ID) {
            // search in DB by id
            for (int i = 0; i < mElements.size(); i++) {
                if (id == mElements.get(i).getId()) {
                    mViewPager.setCurrentItem(i);
                    break;
                }
            }
        }

        ElementDao eDAO = App.getInstance().getDatabase().mElementDao();
        ElementModel model = new ElementModel(eDAO);
        mPresenter = new NotifyPresenter(model);
        mPresenter.attachView(this);
    }

    private void initDB() {
        new Thread(() -> {

            mElements = App.getInstance().initDB();

            createViewPager();
        }).start();

    }

    public void updateElements(List<Element> elements) {
        runOnUiThread(() -> {
            this.mElements = elements;

            List<NotifFragment> fragments = mAdapter.getFragments();

            for (int i = 0; i < mElements.size(); i++) {
                if (fragments.size() < i + 1 ||
                        mElements.get(i).getPageNumber() !=
                                fragments.get(i).getElement().getPageNumber()){
                    NotifFragment f = NotifFragment.newInstance(mElements.get(i));
                    f.setPresenter(mPresenter);
                    mAdapter.addFragment(f, i);
                }
            }
        });
    }

    private void createViewPager() {
        runOnUiThread(() -> {
            FragmentManager manager = getSupportFragmentManager();
            mAdapter =
                    new MyFragmentStateAdapter(manager,
                            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT); // ?????????Lifecycle.State.RESUMED
            for (int i = 0; i < mElements.size(); i++) {
                NotifFragment f = NotifFragment.newInstance(mElements.get(i));
                f.setPresenter(mPresenter);
                mAdapter.addFragment(f);
            }
            mViewPager.setAdapter(mAdapter);
        });

    }


    public NotifyPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private class MyFragmentStateAdapter
            extends FragmentStatePagerAdapter {

        private List<NotifFragment> mFragments;

        public List<NotifFragment> getFragments() {
            return mFragments;
        }

        public MyFragmentStateAdapter(
                @NonNull FragmentManager fm,
                int behavior) {
            super(fm, behavior);
            mFragments = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
//            Element element = mElements.get(position);
//            return NotifFragment.newInstance(element);
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mElements.size();
        }

        public void addFragment(NotifFragment f) {
            mFragments.add(f);
            notifyDataSetChanged();
        }

        public void addFragment(NotifFragment f, int position) {
            mFragments.add(position, f);
            notifyDataSetChanged();
        }


    }
}
