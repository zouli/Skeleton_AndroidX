package com.riverside.skeleton.android.widgettest;

import com.riverside.skeleton.android.R;
import com.riverside.skeleton.android.base.activity.BaseActivity;
import com.riverside.skeleton.android.widget.adapter.ListViewAdapter;
import com.riverside.skeleton.android.widget.adapter.viewholders.ListViewHolder;
import com.riverside.skeleton.android.widget.containers.CompleteListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_complete_listview)
public class CompleteListViewActivity extends BaseActivity {
    @ViewById
    CompleteListView clv_list;

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

        clv_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
