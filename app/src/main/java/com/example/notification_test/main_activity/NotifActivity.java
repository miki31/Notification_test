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

        initDB();





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

    private void openPageFromNoyif(){
        long id = getIntent().getLongExtra(ARG_NOTIFICATION_ID, DEFAULT_NOTIFICATION_ID);

        if (id != DEFAULT_NOTIFICATION_ID) {
            // TODO: search Element in DB by id
            for (int i = 0; i < mElements.size(); i++) {
                if (id == mElements.get(i).getId()) {
                    mViewPager.setCurrentItem(i);
                    break;
                }
            }
        }
    }

    public void updateElements(List<Element> elements) {
        runOnUiThread(() -> {
            this.mElements = elements;

            List<NotifFragment> fragments = mAdapter.getFragments();

            if (mElements.size() > fragments.size()){
                for (int i = 0; i < mElements.size(); i++) {
                    if (fragments.size() < i + 1 ||
                            mElements.get(i).getPageNumber() !=
                                    fragments.get(i).getElement().getPageNumber()
                    ){
                        NotifFragment f = NotifFragment.newInstance(mElements.get(i));
                        f.setPresenter(mPresenter);
                        mAdapter.addFragment(f, i);

                        updateViewPagerAdapter();

                        mViewPager.setCurrentItem(i);
                        break;
                    }
                }
            } else {
                for (int i = 0; i < fragments.size(); i++) {
                    if (mElements.size() < i + 1 ||
                            mElements.get(i).getPageNumber() !=
                                    fragments.get(i).getElement().getPageNumber()
                    ){
                        mAdapter.deleteFragment(i);

                        updateViewPagerAdapter();

                        if (i == 0){
                            mViewPager.setCurrentItem(0);
                        } else {
                            mViewPager.setCurrentItem(i - 1);
                        }
                        break;
                    }
                }
            }
        });
    }

    private void updateViewPagerAdapter(){
        MyFragmentStateAdapter adapter = (MyFragmentStateAdapter) mViewPager.getAdapter();
        mViewPager.setAdapter(adapter);
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


            openPageFromNoyif();
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

        public void deleteFragment(int positin){
            Fragment f = mFragments.get(positin);
            mFragments.remove(positin);
            notifyDataSetChanged();
            destroyItem(mViewPager, positin, f);
        }
    }
}
