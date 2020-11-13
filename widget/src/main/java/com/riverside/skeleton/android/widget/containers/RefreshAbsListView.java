package com.riverside.skeleton.android.widget.containers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

/**
 * 可刷新的AbsListview  1.0
 * b_e  2019/04/26
 */
public abstract class RefreshAbsListView extends LinearLayout {
    protected Context mContext;
    protected int pageNum = 1;

    private SwipyRefreshLayout srl_list;
    private AbsListView listView;
    protected BindDataListener bindDataListener;

    public RefreshAbsListView(Context context) {
        this(context, null);
    }

    public RefreshAbsListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshAbsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        getAttrs(attrs);
        initView();
        setListener();
    }

    /**
     * 取得属性
     *
     * @param attrs
     */
    abstract void getAttrs(AttributeSet attrs);

    /**
     * 初始化控件
     */
    private void initView() {
        //初始化刷新控件
        srl_list = new SwipyRefreshLayout(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        srl_list.setLayoutParams(params);
        srl_list.setDirection(SwipyRefreshLayoutDirection.getFromInt(getDirection()));
        addView(srl_list);

        listView = initList();
        srl_list.addView(listView);
    }

    /**
     * 取得刷新模式
     *
     * @return
     */
    abstract int getDirection();

    /**
     * 初始化List控件
     *
     * @return
     */
    abstract AbsListView initList();

    /**
     * 设置事件
     */
    private void setListener() {
        srl_list.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    reloadList();
                } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    getList(++pageNum);
                }
            }
        });
    }

    /**
     * 刷新数据
     */
    public void reloadList() {
        pageNum = 1;
        if (bindDataListener != null) {
            bindDataListener.clearData();
        }
        getList(pageNum);
    }

    /**
     * 读取下一页数据
     *
     * @param pageNum
     */
    private void getList(int pageNum) {
        if (bindDataListener != null) {
            bindDataListener.bindData(pageNum);
        }
    }

    public interface BindDataListener {
        void clearData();

        void bindData(int pageNum);
    }

    public void setOnBindDataListener(BindDataListener listener) {
        this.bindDataListener = listener;
    }

    public void setRefreshing(boolean refreshing) {
        srl_list.setRefreshing(refreshing);
    }

    public void setAdapter(BaseAdapter adapter) {
        listView.setAdapter(adapter);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        listView.setOnItemClickListener(listener);
    }

    public void setEmptyView(View emptyView) {
        listView.setEmptyView(emptyView);
    }
}
