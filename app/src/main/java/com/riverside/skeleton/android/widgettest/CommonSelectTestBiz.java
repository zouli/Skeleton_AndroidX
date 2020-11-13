package com.riverside.skeleton.android.widgettest;

import android.content.Context;

import com.riverside.skeleton.android.widget.commonselect.business.BaseBiz;
import com.riverside.skeleton.android.widget.commonselect.business.BaseBizItem;
import com.riverside.skeleton.android.widget.commonselect.business.BizCallback;

import java.util.ArrayList;
import java.util.List;

public class CommonSelectTestBiz extends BaseBiz {
    public CommonSelectTestBiz(Context context) {
    }

    @Override
    public void setCriteria(String... args) {
    }

    @Override
    public void getList(BizCallback callback) {
        List<BaseBizItem> datas = new ArrayList<>();
        BaseBizItem item = new BaseBizItem();
        item.setTitle("a");
        item.setValue("1");
        datas.add(item);
        item = new BaseBizItem();
        item.setTitle("b");
        item.setValue("2");
        datas.add(item);
        item = new BaseBizItem();
        item.setTitle("c");
        item.setValue("3");
        datas.add(item);
        item = new BaseBizItem();
        item.setTitle("d");
        item.setValue("4");
        datas.add(item);
        callback.callback(datas);
    }
}
