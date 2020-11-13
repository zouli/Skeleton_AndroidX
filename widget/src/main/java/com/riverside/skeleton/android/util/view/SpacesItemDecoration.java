package com.riverside.skeleton.android.util.view;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView间距装饰类    1.0
 * b_e  2017/12/28
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int leftSpace;
    private int rightSpace;
    private int topSpace;
    private int bottomSpace;

    public SpacesItemDecoration(int space) {
        this(space, space);
    }

    public SpacesItemDecoration(int leftrightSpace, int topbottmSpace) {
        this(leftrightSpace, leftrightSpace, topbottmSpace, topbottmSpace);
    }

    public SpacesItemDecoration(int leftSpace, int rightSpace, int topSpace, int bottmSpace) {
        this.leftSpace = leftSpace;
        this.rightSpace = rightSpace;
        this.topSpace = topSpace;
        this.bottomSpace = bottmSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = leftSpace;
        outRect.right = rightSpace;
        outRect.top = topSpace;
        outRect.bottom = bottomSpace;
    }
}