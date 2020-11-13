package com.riverside.skeleton.android.widget.containers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

import com.riverside.skeleton.android.widget.R;

/**
 * 可刷新的Listview     1.0
 * b_e  2019/04/26
 */
public class RefreshListView extends RefreshAbsListView {
    private int direction;
    private Drawable mDivider;
    private int mDividerHeight;

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.RefreshListView);
        direction = typedArray.getInt(R.styleable.RefreshListView_rlv_direction, 2);
        mDivider = typedArray.getDrawable(R.styleable.RefreshListView_rlv_divider);
        mDividerHeight = typedArray.getDimensionPixelSize(R.styleable.RefreshListView_rlv_dividerHeight, 0);
        typedArray.recycle();
    }

    @Override
    int getDirection() {
        return direction;
    }

    @Override
    AbsListView initList() {
        //初始化ListView
        ListView lv_list = new ListView(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lv_list.setLayoutParams(params);
        lv_list.setVerticalScrollBarEnabled(false);
        lv_list.setHorizontalScrollBarEnabled(false);
        lv_list.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
        if (mDivider != null) {
            lv_list.setDivider(mDivider);
        }
        if (mDividerHeight != 0) {
            lv_list.setDividerHeight(mDividerHeight);
        }

        lv_list.post(new Runnable() {
            @Override
            public void run() {
                reloadList();
            }
        });
        return lv_list;
    }
}
