package com.riverside.skeleton.android.widget.containers;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.riverside.skeleton.android.widget.R;

/**
 * 完全显示的Listview    1.0
 * b_e  2018/01/15
 */
public class CompleteListView extends LinearLayout {
    private BaseAdapter adapter;
    private AdapterView.OnItemClickListener itemClickListener;
    private View mEmptyView;
    private AdapterDataSetObserver mDataSetObserver;

    private Drawable mDivider;
    private int mDividerHeight;

    private Context mContext;

    public CompleteListView(Context context) {
        this(context, null);
    }

    public CompleteListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CompleteListView);

        // 取得分割线
        final Drawable d = a.getDrawable(R.styleable.CompleteListView_clv_divider);
        if (d != null) {
            // Use an implicit divider height which may be explicitly
            // overridden by android:dividerHeight further down.
            setDivider(d);
        }

        // 取得分割线高度
        final int dividerHeight = a.getDimensionPixelSize(R.styleable.CompleteListView_clv_dividerHeight, 0);
        if (dividerHeight != 0) {
            setDividerHeight(dividerHeight);
        }

        a.recycle();
    }

    public CompleteListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Returns the drawable that will be drawn between each item in the list.
     *
     * @return the current drawable drawn between list elements
     * @attr ref R.styleable#ListView_divider
     */
    @Nullable
    public Drawable getDivider() {
        return mDivider;
    }

    /**
     * Sets the drawable that will be drawn between each item in the list.
     * <p>
     * <strong>Note:</strong> If the drawable does not have an intrinsic
     * height, you should also call {@link #setDividerHeight(int)}.
     *
     * @param divider the drawable to use
     * @attr ref R.styleable#ListView_divider
     */
    public void setDivider(@Nullable Drawable divider) {
        if (divider != null) {
            mDividerHeight = divider.getIntrinsicHeight();
        } else {
            mDividerHeight = 0;
        }
        mDivider = divider;
        requestLayout();
        invalidate();
    }

    /**
     * @return Returns the height of the divider that will be drawn between each item in the list.
     */
    public int getDividerHeight() {
        return mDividerHeight;
    }

    /**
     * Sets the height of the divider that will be drawn between each item in the list. Calling
     * this will override the intrinsic height as set by {@link #setDivider(Drawable)}
     *
     * @param height The new height of the divider in pixels.
     */
    public void setDividerHeight(int height) {
        mDividerHeight = height;
        requestLayout();
        invalidate();
    }

    /**
     * 绑定显示控件
     */
    private void bindView() {
        if (adapter == null)
            return;

        // 清除现有View
        removeAllViews();

        // 添加View
        for (int i = 0; i < adapter.getCount(); i++) {
            final int position = i;
            final View view = adapter.getView(position, null, this);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(null, view, position, view.getId());
                    }
                }
            });
            addView(view);

            //添加分割线
            if (mDivider != null && mDividerHeight > 0) {
                if (adapter.getCount() > 1 && i < adapter.getCount() - 1) {
                    View dividerView = new View(mContext);
                    ViewGroup.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mDividerHeight);
                    dividerView.setLayoutParams(lp);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        dividerView.setBackground(mDivider);
                    } else {
                        dividerView.setBackgroundDrawable(mDivider);
                    }
                    addView(dividerView);
                }
            }
        }
    }

    /**
     * 设置Adapter
     *
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;

        // 注册Adapter数据监听
        mDataSetObserver = new AdapterDataSetObserver();
        this.adapter.registerDataSetObserver(mDataSetObserver);
    }

    /**
     * 设置Item点击监听
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }

    /**
     * 设置无数据时显示的View
     *
     * @param view
     */
    public void setEmptyView(View view) {
        mEmptyView = view;

        boolean empty = ((adapter == null) || adapter.isEmpty());
        updateEmptyStatus(empty);
    }

    /**
     * 取得当前无数据View
     *
     * @return
     */
    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * 更新无数据View显示状态
     *
     * @param empty
     */
    private void updateEmptyStatus(boolean empty) {
        if (empty) {
            if (mEmptyView != null) {
                mEmptyView.setVisibility(View.VISIBLE);
                setVisibility(View.GONE);
            } else {
                // If the caller just removed our empty view, make sure the list view is visible
                setVisibility(View.VISIBLE);
            }
        } else {
            if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
            setVisibility(View.VISIBLE);
        }
    }

    /**
     * Adapter数据监听类
     */
    class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            // 绑定显示控件
            bindView();
            if (mEmptyView != null) {
                updateEmptyStatus((adapter == null) || adapter.isEmpty());
            }
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            if (mEmptyView != null) {
                updateEmptyStatus((adapter == null) || adapter.isEmpty());
            }
        }
    }
}
