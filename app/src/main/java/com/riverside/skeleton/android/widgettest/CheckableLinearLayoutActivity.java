package com.riverside.skeleton.android.widgettest;

import android.widget.ListView;

import com.riverside.skeleton.android.R;
import com.riverside.skeleton.android.base.activity.BaseActivity;
import com.riverside.skeleton.android.widget.adapter.ListViewAdapter;
import com.riverside.skeleton.android.widget.adapter.viewholders.ListViewHolder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_checkable_linearlayout)
public class CheckableLinearLayoutActivity extends BaseActivity {
    @ViewById
    ListView lv_list;

    @AfterViews
    void initView() {
        initList();
    }

    void initList() {
        List<String> datas = new ArrayList<>();
        datas.add("aa");
        datas.add("bb");
        datas.add("cc");
        datas.add("dd");

        ListViewAdapter<String> adapter = new ListViewAdapter<String>(R.layout.list_item_checkable_linearlayout, datas) {
            @Override
            protected void onBindData(ListViewHolder viewHolder, int position, String item) {
                viewHolder.setText(R.id.tv_text, item);
            }
        };

        lv_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv_list.setAdapter(adapter);
    }
}
