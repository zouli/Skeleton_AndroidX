package com.riverside.skeleton.android.widget.containers;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.GridView;

import com.riverside.skeleton.android.widget.R;

/**
 * 可刷新的Gridview     1.0
 * b_e  2019/04/26
 */
public class RefreshGridView extends RefreshAbsListView {
    private int direction;
    private int numColumns;
    private int horizontalSpacing;
    private int verticalSpacing;

    public RefreshGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.RefreshGridView);
        direction = typedArray.getInt(R.styleable.RefreshGridView_rgv_direction, 2);
        numColumns = typedArray.getInt(R.styleable.RefreshGridView_rgv_numColumns, 3);
        horizontalSpacing = typedArray.getDimensionPixelSize(R.styleable.RefreshGridView_rgv_horizontalSpacing, 0);
        verticalSpacing = typedArray.getDimensionPixelSize(R.styleable.RefreshGridView_rgv_verticalSpacing, 0);
        typedArray.recycle();
    }

    @Override
    int getDirection() {
        return direction;
    }

    @Override
    AbsListView initList() {
        GridView gv_list = new GridView(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        gv_list.setLayoutParams(params);
        gv_list.setVerticalScrollBarEnabled(false);
        gv_list.setHorizontalScrollBarEnabled(false);
        gv_list.setDescendantFocusability(GridView.FOCUS_BLOCK_DESCENDANTS);
        gv_list.setNumColumns(numColumns);
        if (horizontalSpacing != 0) {
            gv_list.setHorizontalSpacing(horizontalSpacing);
        }
        if (verticalSpacing != 0) {
            gv_list.setVerticalSpacing(verticalSpacing);
        }

        gv_list.post(new Runnable() {
            @Override
            public void run() {
                reloadList();
            }
        });
        return gv_list;
    }
}
