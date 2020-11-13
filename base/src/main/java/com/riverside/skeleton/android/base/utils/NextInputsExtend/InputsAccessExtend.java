package com.riverside.skeleton.android.base.utils.NextInputsExtend;

import android.app.Activity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RadioGroup;

import com.github.yoojia.inputs.Input;
import com.github.yoojia.inputs.WidgetAccess;

/**
 * 控件访问扩展   1.1
 * b_e  2017/11/23
 * 1.1  添加取得tag的值
 */
public class InputsAccessExtend extends WidgetAccess {
    public InputsAccessExtend(Activity activity) {
        super(activity);
    }

    /**
     * 取得RadioGroup是否选择
     *
     * @param viewId
     * @return
     */
    public Input findRadioGroup(int viewId) {
        return AndroidInputsExtend.radioGroup((RadioGroup) findView(viewId));
    }

    /**
     * 取得View的tag的值
     *
     * @param viewId
     * @param tagId
     * @return
     */
    public Input getTag(int viewId, int tagId) {
        return AndroidInputsExtend.getTag((View) findView(viewId), tagId);
    }

    /**
     * 取得GridLayout已选择Child
     *
     * @param gridLayoutId
     * @param checkableId
     * @return
     */
    public Input findGridLayoutSelectable(int gridLayoutId, int checkableId) {
        return AndroidInputsExtend.gridLayoutSelectable((GridLayout) findView(gridLayoutId), checkableId);
    }

    /**
     * 取得GridLayout是否包含Child
     *
     * @param gridLayoutId
     * @param childMinCount
     * @return
     */
    public Input findGridLayoutHasChild(int gridLayoutId, int childMinCount) {
        return AndroidInputsExtend.gridLayoutHasChild((GridLayout) findView(gridLayoutId), childMinCount);
    }

    /**
     * 取得最少一项为true
     *
     * @param viewId
     * @param inputs
     * @return
     */
    public Input findOnceSatisfy(int viewId, Input... inputs) {
        StringBuilder value = new StringBuilder();
        for (Input input : inputs) {
            value.append(input.getValue().trim());
        }
        return AndroidInputsExtend.view((View) findView(viewId), value.toString());
    }
}
