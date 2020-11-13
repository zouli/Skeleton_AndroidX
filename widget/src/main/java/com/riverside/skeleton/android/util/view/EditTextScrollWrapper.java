package com.riverside.skeleton.android.util.view;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.ref.WeakReference;

/**
 * 解决EditText与Scroller冲突类   1.0
 * b_e  2018/02/16
 */
public class EditTextScrollWrapper implements View.OnTouchListener {
    private WeakReference<Context> mContext;
    private EditText et_text;

    public static void setEditText(Context context, EditText editText) {
        new EditTextScrollWrapper(new WeakReference<>(context), editText);
    }

    private EditTextScrollWrapper(WeakReference<Context> context, EditText editText) {
        mContext = context;
        et_text = editText;
        et_text.setVerticalScrollBarEnabled(true);
        et_text.setMovementMethod(ScrollingMovementMethod.getInstance());
        et_text.setOnTouchListener(this);
        et_text.setTextIsSelectable(true);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        InputMethodManager imm = (InputMethodManager) mContext.get().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if ((view.getId() == et_text.getId() && canVerticalScroll(et_text))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    /**
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }
}
