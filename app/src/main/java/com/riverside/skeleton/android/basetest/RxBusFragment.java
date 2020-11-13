package com.riverside.skeleton.android.basetest;

import android.widget.TextView;

import com.riverside.skeleton.android.R;
import com.riverside.skeleton.android.base.fragment.BaseFragment;
import com.riverside.skeleton.android.util.log.CLog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import io.reactivex.functions.Consumer;

@EFragment(R.layout.fragment_rxbus)
public class RxBusFragment extends BaseFragment {
    @ViewById
    TextView tv_title;

    @AfterViews
    void initView() {
        registerRxBus(String.class, new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                tv_title.setText(s);
                CLog.w(s);
            }
        });
    }
}
