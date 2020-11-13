package com.riverside.skeleton.android.widget.commonselect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.riverside.skeleton.android.base.activity.BaseActivity;
import com.riverside.skeleton.android.widget.R;
import com.riverside.skeleton.android.widget.adapter.ListViewAdapter;
import com.riverside.skeleton.android.widget.adapter.viewholders.ListViewHolder;
import com.riverside.skeleton.android.widget.commonselect.business.BaseBiz;
import com.riverside.skeleton.android.widget.commonselect.business.BaseBizItem;
import com.riverside.skeleton.android.widget.commonselect.business.BizCallback;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CommonSelectActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    public static final String EXTRA_COMMONSELECT_OPTION = "COMMONSELECT_OPTION";

    private CommonSelectOptions options;

    private Toolbar toolbar;
    private TextView toolbar_title;

    private ListView lv_list;
    private SelectListViewAdapter adapter;
    private List<BaseBizItem> mOriginalValues;
    private final Object mLock = new Object();

    private LinearLayout ll_search;
    private SearchView sv_search;
    private RelativeLayout rl_all;
    private CheckBox cb_all;
    private LinearLayout list_empty;
    private TextView tv_empty_text;
    private TextView toolbar_right_btn;

    private boolean isFirst = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        initView();
        setListener();
    }

    private void getData() {
        Intent intent = getIntent();
        options = intent.getParcelableExtra(EXTRA_COMMONSELECT_OPTION);
    }

    private void initView() {
        setContentView(R.layout.activity_common_select);
        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
        toolbar_title.setText(options.getTitleName());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        toolbar.setNavigationIcon(R.drawable.web_toolbar_back);

        lv_list = findViewById(R.id.lv_list);
        ll_search = findViewById(R.id.ll_search);
        sv_search = findViewById(R.id.sv_search);
        rl_all = findViewById(R.id.rl_all);
        cb_all = findViewById(R.id.cb_all);
        list_empty = findViewById(R.id.list_empty);
        tv_empty_text = findViewById(R.id.tv_empty_text);
        toolbar_right_btn = findViewById(R.id.toolbar_right_btn);

        if (options.getHasSearch() == CommonSelectOptions.SEARCH_FLAG.HAVE) {
            ll_search.setVisibility(View.VISIBLE);
        } else {
            ll_search.setVisibility(View.GONE);
        }

        BaseBiz listBiz = generateList(options.getListGenerator());
        if (listBiz != null) {
            listBiz.setCriteria(options.getListCriteria());
            listBiz.getList(new BizCallback() {
                @Override
                public void callback(List<BaseBizItem> datas) {
                    mOriginalValues = datas;

                    switch (options.getChoiceMode()) {
                        case ListView.CHOICE_MODE_SINGLE:
                            rl_all.setVisibility(View.GONE);
                            adapter = new SelectListViewAdapter(R.layout.common_select_list_item_radio, mOriginalValues);
                            break;
                        case ListView.CHOICE_MODE_MULTIPLE:
                        case ListView.CHOICE_MODE_MULTIPLE_MODAL:
                            rl_all.setVisibility(View.VISIBLE);
                            adapter = new SelectListViewAdapter(R.layout.common_select_list_item_checkbox, mOriginalValues);
                            break;
                    }
                    lv_list.setChoiceMode(options.getChoiceMode());
                    lv_list.setAdapter(adapter);

                    Flowable.fromIterable(mOriginalValues)
                            .zipWith(Flowable.range(0, mOriginalValues.size())
                                    , new BiFunction<BaseBizItem, Integer, Pair<Integer, BaseBizItem>>() {
                                        @Override
                                        public Pair<Integer, BaseBizItem> apply(BaseBizItem item, Integer index) throws Exception {
                                            return new Pair<>(index, item);
                                        }
                                    })
                            .subscribe(new FlowableSubscriber<Pair<Integer, BaseBizItem>>() {
                                @Override
                                public void onSubscribe(Subscription s) {
                                    s.request(Integer.MAX_VALUE);
                                }

                                @Override
                                public void onNext(Pair<Integer, BaseBizItem> item) {
                                    lv_list.setItemChecked(item.first, Arrays.asList(options.getCheckedValue()).contains(item.second.getValue()));
                                }

                                @Override
                                public void onError(Throwable t) {

                                }

                                @Override
                                public void onComplete() {
                                    adapter.notifyDataSetChanged();
                                    adapter.setSelectAll();
                                }
                            });
                }
            });
        }

        //支持查询
        lv_list.setTextFilterEnabled(false);

        if (!TextUtils.isEmpty(options.getListEmptyText())) {
            tv_empty_text.setText(options.getListEmptyText());
        }
        lv_list.setEmptyView(list_empty);

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectAll();
            }
        });

        sv_search.setOnQueryTextListener(this);
        sv_search.setSubmitButtonEnabled(false);
        sv_search.clearFocus();
    }

    private void setListener() {
        rl_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCbAllClicked(v);
            }
        });

        cb_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCbAllClicked(v);
            }
        });

        toolbar_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightBtn(v);
            }
        });
    }

    private void onCbAllClicked(View v) {
        sv_search.clearFocus();
        if (v.getId() == R.id.rl_all) {
            cb_all.toggle();
        }

        Flowable.range(0, adapter.getCount())
                .subscribe(new FlowableSubscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer index) {
                        lv_list.setItemChecked(index, cb_all.isChecked());
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void onRightBtn(View v) {
        final long[] ids = lv_list.getCheckedItemIds();

        Flowable.range(0, ids.length)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return (int) ids[integer];
                    }
                })
                .flatMap(new Function<Integer, Publisher<BaseBizItem>>() {
                    @Override
                    public Publisher<BaseBizItem> apply(Integer index) throws Exception {
                        return Flowable.just(adapter.getItem(index));
                    }
                })
                .scan(new BaseBizItem(), new BiFunction<BaseBizItem, BaseBizItem, BaseBizItem>() {
                    @Override
                    public BaseBizItem apply(BaseBizItem bizItem, BaseBizItem bizItem2) throws Exception {
                        if (TextUtils.isEmpty(bizItem.getTitle())) {
                            bizItem.setTitle(bizItem2.getTitle());
                            bizItem.setValue(bizItem2.getValue());
                            bizItem.setSubValue(bizItem2.getSubValue());
                        } else {
                            bizItem.setTitle(bizItem.getTitle() + "," + bizItem2.getTitle());
                            bizItem.setValue(bizItem.getValue() + ";" + bizItem2.getValue());
                            bizItem.setSubValue(bizItem.getSubValue() + ";" + bizItem2.getSubValue());
                        }
                        return bizItem;
                    }
                })
                .lastElement()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBizItem>() {
                    @Override
                    public void accept(BaseBizItem bizItem) throws Exception {
                        if (bizItem.getValue() == null) {
                            setResult(RESULT_CANCELED);
                            finish();
                        } else {
                            Intent data = new Intent();
                            data.putExtra(CommonSelectOptions.RESULT_DATA_TITLE, bizItem.getTitle());
                            data.putExtra(CommonSelectOptions.RESULT_DATA_VALUE, bizItem.getValue());
                            data.putExtra(CommonSelectOptions.RESULT_DATA_SUBVALUE, bizItem.getSubValue());
                            data.putExtra(CommonSelectOptions.RESULT_DATA_CRITERIA, options.getListCriteria());
                            setResult(RESULT_OK, data);
                            finish();
                        }
                    }
                });
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
//            lv_list.clearTextFilter();
            adapter.getFilter().filter(null);
        } else {
            // Sets the initial value for the text filter.
//            lv_list.setFilterText(newText.toString());
            adapter.getFilter().filter(newText.toString());
        }
        return false;
    }

    private class SelectListViewAdapter extends ListViewAdapter<BaseBizItem> implements Filterable {
        @Override
        public boolean hasStableIds() {
            return true;
        }

        public void setSelectAll() {
            cb_all.setChecked(mOriginalValues.size() == lv_list.getCheckedItemCount());
        }

        public SelectListViewAdapter(int layoutId, List<BaseBizItem> datas) {
            super(layoutId, datas);
        }

        @Override
        protected void onBindData(ListViewHolder viewHolder, final int position, final BaseBizItem item) {
            viewHolder.setText(R.id.tv_title, item.getTitle());
            if (!TextUtils.isEmpty(item.getSubTitle())) {
                viewHolder.setText(R.id.tv_subTitle, item.getSubTitle());
            }
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence prefix) {
                    // 持有过滤操作完成之后的数据。该数据包括过滤操作之后的数据的值以及数量。 count:数量 values包含过滤操作之后的数据的值
                    FilterResults results = new FilterResults();
                    if (prefix == null || prefix.length() == 0) {
                        synchronized (mLock) {
                            ArrayList<BaseBizItem> list = new ArrayList<>(mOriginalValues);
                            results.values = list;
                            results.count = list.size();
                        }
                    } else {
                        // 做正式的筛选
                        String prefixString = prefix.toString().toLowerCase();
                        // 新的集合对象
                        final ArrayList<BaseBizItem> newValues = new ArrayList<>();
                        for (BaseBizItem baseBizItem : mOriginalValues) {
                            if (baseBizItem.getTitle().indexOf(prefixString) > -1) {
                                newValues.add(baseBizItem);
                            }
                        }
                        results.values = newValues;
                        results.count = newValues.size();
                    }

                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    List<BaseBizItem> datas = (List<BaseBizItem>) results.values;
                    adapter.clear();
                    adapter.addItems(datas);
                    cb_all.setChecked(false);
                    lv_list.clearChoices();
                }
            };
        }
    }

    public BaseBiz generateList(Class<? extends BaseBiz> clazz) {
        BaseBiz baseBiz = null;
        try {
            Constructor constructor = clazz.getDeclaredConstructor(Context.class);
            constructor.setAccessible(true);
            baseBiz = (BaseBiz) constructor.newInstance(activity);
        } catch (InstantiationException ignored) {
        } catch (IllegalAccessException ignored) {
        } catch (NoSuchMethodException ignored) {
        } catch (InvocationTargetException ignored) {
        }
        return baseBiz;
    }
}
