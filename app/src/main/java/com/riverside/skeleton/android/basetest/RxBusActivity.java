package com.riverside.skeleton.android.basetest;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.EditText;

import com.riverside.skeleton.android.R;
import com.riverside.skeleton.android.base.activity.BaseActivity;
import com.riverside.skeleton.android.base.fragment.BaseFragment;
import com.riverside.skeleton.android.base.rxbus.RxBus;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_rxbus)
public class RxBusActivity extends BaseActivity {
    @ViewById(resName = "tabs")
    TabLayout tabLayout;

    @ViewById(resName = "container")
    ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @ViewById
    EditText et_text;

    @AfterViews
    void initView() {
        tabTitles.add(index++ + "");
        fragments.add(RxBusFragment_.builder().build());
        tabTitles.add(index++ + "");
        fragments.add(RxBusFragment_.builder().build());
        tabTitles.add(index++ + "");
        fragments.add(RxBusFragment_.builder().build());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Click(R.id.btn_send)
    void onBtnSendClick(View v) {
        RxBus.getInstance().post(et_text.getText().toString());
    }

    @Click(R.id.btn_send1)
    void onBtnSend1Click(View v) {
        RxBus.getInstance().postSticky(et_text.getText().toString());
    }

    @Click(R.id.btn_add)
    void onBtnAddClick(View v) {
        tabTitles.add(index++ + "");
        fragments.add(RxBusFragment_.builder().build());
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    private List<String> tabTitles = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();
    private int index = 1;

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return tabTitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().removeSticky(String.class);
    }
}
