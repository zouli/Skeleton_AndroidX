package com.riverside.skeleton.android.widget.containers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

import com.riverside.skeleton.android.widget.R;

/**
 * 可Check的LinearLayout  1.0
 * b_e  2017/12/28
 */
public class CheckableLinearLayout extends LinearLayout implements Checkable {
    private Context mContext;
    private TypedArray typedArray;
    private boolean checkedChild = false;
    private boolean mChecked;
    private static final String TAG = CheckableLinearLayout.class.getCanonicalName();
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_selected};

    public CheckableLinearLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        // 取得属性
        typedArray = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.CheckableLinearLayout, 0, 0);
        checkedChild = typedArray.getBoolean(R.styleable.CheckableLinearLayout_cll_checkedChild, false);
        typedArray.recycle();
    }

    @Override
    public void setChecked(final boolean checked) {
        // 只有变化时调用
        if (checked != mChecked) {
            if (checkedChild) {
                // Check所有可以check的对象
                for (int i = 0; i < getChildCount(); i++) {
                    View view = getChildAt(i);
                    if (view instanceof Checkable) {
                        ((Checkable) view).setChecked(checked);
                    }
                }
            }

            // 保存Check状态
            mChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    protected int[] onCreateDrawableState(final int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        // 更新控件状态
        if (isChecked())
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        // 更新控件状态
        final Drawable drawable = getBackground();
        if (drawable != null) {
            final int[] myDrawableState = getDrawableState();
            drawable.setState(myDrawableState);
            invalidate();
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean performLongClick() {
        return super.performLongClick();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        // 更新Check状态
        setChecked(!mChecked);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // 保存Check状态
        final Parcelable superState = super.onSaveInstanceState();
        final SavedState savedState = new SavedState(superState);
        savedState.checked = isChecked();
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(final Parcelable state) {
        // 回复Check状态
        final SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setChecked(savedState.checked);
        requestLayout();
    }

    /**
     * 状态保存类
     */
    private static class SavedState extends BaseSavedState {
        boolean checked;
        @SuppressWarnings("unused")
        public static final Creator<SavedState> CREATOR;

        static {
            CREATOR = new Creator<SavedState>() {
                @Override
                public SavedState createFromParcel(final Parcel in) {
                    return new SavedState(in);
                }

                @Override
                public SavedState[] newArray(final int size) {
                    return new SavedState[size];
                }
            };
        }

        SavedState(final Parcelable superState) {
            super(superState);
        }

        private SavedState(final Parcel in) {
            super(in);
            checked = (Boolean) in.readValue(null);
        }

        @Override
        public void writeToParcel(final Parcel out, final int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(checked);
        }

        @Override
        public String toString() {
            return TAG + ".SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " checked=" + checked + "}";
        }
    }
}
