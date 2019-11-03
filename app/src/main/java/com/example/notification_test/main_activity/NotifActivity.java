package com.example.notification_test.main_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.view.ViewGroup;

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

        ElementDao eDAO = App.getInstance().getDatabase().mElementDao();
        ElementModel model = new ElementModel(eDAO);
        mPresenter = new NotifyPresenter(model);
        mPresenter.attachView(this);

        // init DB in own Thread (not in main)
        initDB();
    }

    private void initDB() {
        new Thread(() -> {

            App.getInstance().initDB();

            mElements = mPresenter.getAllElements();

            createViewPager();
        }).start();
    }

    private void openPageFromNoyif() {
        // get it element from Notification
        long id = getIntent().getLongExtra(ARG_NOTIFICATION_ID, DEFAULT_NOTIFICATION_ID);

        if (id != DEFAULT_NOTIFICATION_ID) {
            // find position in list by id
            for (int i = 0; i < mElements.size(); i++) {
                if (id == mElements.get(i).getId()) {
                    //show page from Notification
                    mViewPager.setCurrentItem(i);
                    break;
                }
            }
        }
    }

    // update list of fragments after click minus or plus
    public void updateElements(List<Element> elements) {
        runOnUiThread(() -> {
            this.mElements = elements;

            List<NotifFragment> fragments = mAdapter.getFragments();

            if (mElements.size() > fragments.size()) {
                // after click plus
                for (int i = 0; i < mElements.size(); i++) {
                    if (fragments.size() < i + 1 ||
                            mElements.get(i).getPageNumber() !=
                                    fragments.get(i).getElement().getPageNumber()
                    ) {
                        // create fragment with new element (and show it)
                        NotifFragment f = NotifFragment.newInstance(mElements.get(i));
                        f.setPresenter(mPresenter);
                        mAdapter.addFragment(f, i);

                        updateViewPagerAdapter();

                        mViewPager.setCurrentItem(i);
                        break;
                    }
                }
            } else {
                // after click minus
                for (int i = 0; i < fragments.size(); i++) {
                    if (mElements.size() < i + 1 ||
                            mElements.get(i).getPageNumber() !=
                                    fragments.get(i).getElement().getPageNumber()
                    ) {
                        // delete fragment from ViewPager
                        mAdapter.deleteFragment(i);

                        updateViewPagerAdapter();

                        // show previous Fragment
                        if (i == 0) {
                            mViewPager.setCurrentItem(0);
                        } else {
                            mViewPager.setCurrentItem(i - 1);
                        }
                        break;
                    }
                }

                // invisible button minus in fragment if it is last in list of fragments
                if (fragments.size() == 1) {
                    fragments.get(0).setBtnMinusVisible(false);
                }
            }
        });
    }

    // update ViewPager after click minus or plus
    private void updateViewPagerAdapter() {
        MyFragmentStateAdapter adapter = (MyFragmentStateAdapter) mViewPager.getAdapter();
        mViewPager.setAdapter(adapter);
    }

    // create ViewPager after open Program or if Notification was clicked
    private void createViewPager() {
        runOnUiThread(() -> {
            FragmentManager manager = getSupportFragmentManager();
            mAdapter =
                    new MyFragmentStateAdapter(manager,
                            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

            // create Fragments with information from Element
            for (int i = 0; i < mElements.size(); i++) {
                NotifFragment f = NotifFragment.newInstance(mElements.get(i));
                f.setPresenter(mPresenter);
                mAdapter.addFragment(f);
            }

            mViewPager.setAdapter(mAdapter);

            // if click on Notification open that page
            openPageFromNoyif();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // clean information about activity in presenter
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
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mElements.size();
        }

        // add Fragment after click plus
        public void addFragment(NotifFragment f) {
            mFragments.add(f);
            notifyDataSetChanged();
        }

        // add Fragment after click plus in special position (middle of list)
        public void addFragment(NotifFragment f, int position) {
            mFragments.add(position, f);
            notifyDataSetChanged();
        }

        // delete Fragment after click minus
        public void deleteFragment(int positin) {
            Fragment f = mFragments.get(positin);
            mFragments.remove(positin);
            notifyDataSetChanged();
            destroyItem(mViewPager, positin, f);
        }
    }
}
