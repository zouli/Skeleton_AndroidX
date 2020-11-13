package com.riverside.skeleton.android.widgettest;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.riverside.skeleton.android.R;
import com.riverside.skeleton.android.base.activity.BaseActivity;
import com.riverside.skeleton.android.net.jsonbean.JsonResponse;
import com.riverside.skeleton.android.net.rest.CommonRestService;
import com.riverside.skeleton.android.net.rest.ObservableHelper2;
import com.riverside.skeleton.android.net.rest.utils.ObservableHelper;
import com.riverside.skeleton.android.net.rest.utils.RestSubscriber;
import com.riverside.skeleton.android.net.rest.utils.RetrofitBindHelper;
import com.riverside.skeleton.android.util.converter.TypeCastingUtils;
import com.riverside.skeleton.android.util.notice.ToastHelper;
import com.riverside.skeleton.android.widget.adapter.ListViewAdapter;
import com.riverside.skeleton.android.widget.adapter.viewholders.ListViewHolder;
import com.riverside.skeleton.android.widget.containers.RefreshAbsListView;
import com.riverside.skeleton.android.widget.containers.RefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_refresh_listview)
public class RefreshListViewActivity extends BaseActivity {
    @ViewById
    RefreshListView rlv;
    private ListViewAdapter<String> adapter;

    @ViewById
    TextView tv_empty;

    @AfterViews
    void initView() {
        initList();
    }

    void initList() {
        adapter = new ListViewAdapter<String>(R.layout.list_item_refresh_listview) {
            @Override
            protected void onBindData(ListViewHolder viewHolder, int position, String item) {
                viewHolder.setText(R.id.tv_title, item);
            }
        };

        rlv.setEmptyView(tv_empty);
        rlv.setAdapter(adapter);
        rlv.setOnBindDataListener(new RefreshAbsListView.BindDataListener() {
            @Override
            public void clearData() {
                adapter.clear();
            }

            @Override
            public void bindData(int pageNum) {
                Map<String, String> param = new HashMap<>();
                param.put("pageNum", TypeCastingUtils.toString(pageNum));
                RetrofitBindHelper.getInstance().doBind(CommonRestService.class).getList(param)
                        .compose(ObservableHelper.<JsonResponse<List<String>>>startReading())
                        .compose(ObservableHelper2.<List<String>>checkResult())
                        .compose(ObservableHelper.<List<String>>toMainThread())
                        .subscribe(new RestSubscriber<List<String>>() {
                            @Override
                            public void onNext(List<String> s) {
                                adapter.addItems(s);
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {
                                rlv.setRefreshing(false);
                            }
                        });
            }
        });
        rlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastHelper.getInstance(activity).showToast(adapter.getItem(position));
            }
        });
    }
}
