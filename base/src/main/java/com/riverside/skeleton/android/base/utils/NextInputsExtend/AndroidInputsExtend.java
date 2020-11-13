package com.riverside.skeleton.android.base.utils.NextInputsExtend;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.github.yoojia.inputs.AndroidNextInputs;
import com.github.yoojia.inputs.ViewInput;

/**
 * 选择器扩展    1.2
 * b_e  2017/11/23
 * 1.1  修改控件类型
 * 1.2  添加取得tag的值
 */
public class AndroidInputsExtend extends AndroidNextInputs {
    /**
     * 取得RadioGroup是否选择
     *
     * @param radioGroup
     * @return true有选择的时候
     */
    public static ViewInput<RadioGroup> radioGroup(final RadioGroup radioGroup) {
        return new ViewInput<RadioGroup>(radioGroup) {
            @Override
            public String getValue() {
                return String.valueOf(radioGroup.getCheckedRadioButtonId() == -1 ? "" : "true");
            }
        };
    }

    /**
     * 取得View的值
     *
     * @param view
     * @param value
     * @return value的值
     */
    public static ViewInput<View> view(View view, final String value) {
        return new ViewInput<View>(view) {
            @Override
            public String getValue() {
                return value;
            }
        };
    }

    /**
     * 取得View的tag值
     *
     * @param view
     * @param id
     * @return
     */
    public static ViewInput<View> getTag(final View view, final int id) {
        return new ViewInput<View>(view) {
            @Override
            public String getValue() {
                if (id == 0) {
                    return String.valueOf(view.getTag() == null ? "" : TextUtils.isEmpty(view.getTag().toString()) ? "" : "true");
                } else {
                    return String.valueOf(view.getTag(id) == null ? "" : TextUtils.isEmpty(view.getTag(id).toString()) ? "" : "true");
                }
            }
        };
    }

    /**
     * 取得GirdLayout是否包含Child
     *
     * @param glView
     * @param childMinCount
     * @return true包含Child的时候
     */
    public static ViewInput<ViewGroup> gridLayoutHasChild(final ViewGroup glView, final int childMinCount) {
        return new ViewInput<ViewGroup>(glView) {
            @Override
            public String getValue() {
                return String.valueOf(glView.getChildCount() > childMinCount ? "true" : "");
            }
        };
    }

    /**
     * 取得GirdLayout是否包含已选择的Child
     *
     * @param glView
     * @param checkableId 可以被选择的Child的ID
     * @return true包含已选择的Child
     */
    public static ViewInput<ViewGroup> gridLayoutSelectable(final ViewGroup glView, final int checkableId) {
        return new ViewInput<ViewGroup>(glView) {
            @Override
            public String getValue() {
                String hasChild = gridLayoutHasChild(glView, 0).getValue();
                if (TextUtils.isEmpty(hasChild)) {
                    return hasChild;
                } else {
                    boolean flag = false;
                    for (int i = 0; i < glView.getChildCount(); i++) {
                        View view = glView.getChildAt(i);
                        if (view instanceof ViewGroup) {
                            view = view.findViewById(checkableId);
                        }
                        if (view.getId() == checkableId) {
                            flag |= ((CompoundButton) view).isChecked();
                        }
                    }
                    return String.valueOf(flag ? "true" : "");
                }
            }
        };
    }
}
