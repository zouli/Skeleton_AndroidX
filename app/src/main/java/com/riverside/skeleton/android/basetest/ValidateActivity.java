package com.riverside.skeleton.android.basetest;

import android.view.View;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.RadioButton;

import com.github.yoojia.inputs.AndroidNextInputs;
import com.github.yoojia.inputs.LazyLoaders;
import com.github.yoojia.inputs.StaticScheme;
import com.riverside.skeleton.android.R;
import com.riverside.skeleton.android.base.activity.BaseActivity;
import com.riverside.skeleton.android.base.utils.NextInputsExtend.AndroidSnackbarMessageDisplay;
import com.riverside.skeleton.android.base.utils.NextInputsExtend.AndroidToastMessageDisplay;
import com.riverside.skeleton.android.base.utils.NextInputsExtend.BlankMessageDisplay;
import com.riverside.skeleton.android.base.utils.NextInputsExtend.InputsAccessExtend;
import com.riverside.skeleton.android.base.utils.NextInputsExtend.LogicScheme;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_validate)
public class ValidateActivity extends BaseActivity {
    @ViewById(R.id.rb_show_snackbar)
    RadioButton rb_show_snackbar;

    @ViewById(R.id.gl_1)
    GridLayout gl_1;

    @AfterViews
    void initView() {
        for (int i = 0; i < 6; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setId(R.id.cb_1);
            cb.setText("cb_1");
            gl_1.addView(cb);
        }
    }

    @Click(R.id.btn_do)
    void onBtnDoClick(View v) {
        validate();
    }

    @Override
    public boolean doValidate() {
        final InputsAccessExtend access = new InputsAccessExtend(this);
        AndroidSnackbarMessageDisplay androidSnackbarMessageDisplay = new AndroidSnackbarMessageDisplay();
        AndroidToastMessageDisplay androidToastMessageDisplay = new AndroidToastMessageDisplay();
        BlankMessageDisplay blankMessageDisplay = new BlankMessageDisplay();
        final LazyLoaders loader = new LazyLoaders(this);
        AndroidNextInputs inputs = new AndroidNextInputs();

        inputs.setMessageDisplay(rb_show_snackbar.isChecked() ? androidSnackbarMessageDisplay : androidToastMessageDisplay);
//        inputs.setMessageDisplay(blankMessageDisplay);

        inputs.add(access.findRadioGroup(R.id.rg_1), StaticScheme.Required().msg("请在rb_1,rb_2,rb_3中选择一个"));
        inputs.add(access.findGridLayoutSelectable(R.id.gl_1, R.id.cb_1), StaticScheme.Required().msg("请至少选择一个cb_1"));
        inputs.add(access.findOnceSatisfy(R.id.et_1, access.findEditText(R.id.et_1), access.findEditText(R.id.et_2)), StaticScheme.Required().msg("文本框至少输入一项"));
        inputs.add(access.findEditText(R.id.et_2), LogicScheme.RequiredAndPreviousRequired(loader.fromEditText(R.id.et_1)));

        return inputs.test();
    }
}
