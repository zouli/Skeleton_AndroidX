package com.riverside.skeleton.android;

import android.content.Intent;
import androidx.appcompat.widget.ViewStubCompat;
import android.view.View;
import android.widget.ListView;

import com.riverside.skeleton.android.base.activity.BaseActivity;
import com.riverside.skeleton.android.util.log.CLog;
import com.riverside.skeleton.android.widget.activity.CommonWebActivity;
import com.riverside.skeleton.android.widget.commonselect.CommonSelectActivity;
import com.riverside.skeleton.android.widget.commonselect.CommonSelectOptions;
import com.riverside.skeleton.android.widget.datepicker.DatePickerHelper;
import com.riverside.skeleton.android.widgettest.CaptchaViewActivity_;
import com.riverside.skeleton.android.widgettest.CheckableLinearLayoutActivity_;
import com.riverside.skeleton.android.widgettest.CommonSelectTestBiz;
import com.riverside.skeleton.android.widgettest.CompleteListViewActivity_;
import com.riverside.skeleton.android.widgettest.ImageGridViewActivity_;
import com.riverside.skeleton.android.widgettest.RefreshGridViewActivity_;
import com.riverside.skeleton.android.widgettest.RefreshListViewActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;

@EActivity(R.layout.activity_widget_main)
public class WidgetMainActivity extends BaseActivity {
    private static final int COMMON_SELECT_RESULT = 100;

    ViewStubCompat viewStub;

    @Click(R.id.btn_date_picker)
    void btnDatePickerClick(View v) {
        DatePickerHelper.getInstance(getSupportFragmentManager()).show();
    }

    @Click(R.id.btn_checkable_linearlayout)
    void btnCheckableLinearLayoutClick(View v) {
        CheckableLinearLayoutActivity_.intent(activity).start();
    }

    @Click(R.id.btn_completelistview)
    void btnCompleteListviewClick(View v) {
        CompleteListViewActivity_.intent(activity).start();
    }

    @Click(R.id.btn_verificationcodeview)
    void btnVerificationCodeViewClick(View v) {
        CaptchaViewActivity_.intent(activity).start();
    }

    @Click(R.id.btn_imagegridview)
    void btnImageGridViewClick(View v) {
        ImageGridViewActivity_.intent(activity).start();
    }

    @Click(R.id.btn_web)
    void btnWebClick(View v) {
        Intent intent = new Intent(activity, CommonWebActivity.class);
        intent.putExtra("url", "http://www.baidu.com");
//        intent.putExtra("title_name", "aaa");
        startActivity(intent);
    }

    @Click(R.id.btn_common_select)
    void btnCommonSelectClick(View v) {
        CommonSelectOptions options = new CommonSelectOptions();
        options.setTitleName("Common Select Test");
        options.setCheckedValue("");
        options.setListGenerator(CommonSelectTestBiz.class);
        options.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        options.setHasSearch(CommonSelectOptions.SEARCH_FLAG.HAVE);

        Intent intent = new Intent(activity, CommonSelectActivity.class);
        intent.putExtra(CommonSelectActivity.EXTRA_COMMONSELECT_OPTION, options);
        startActivityForResult(intent, COMMON_SELECT_RESULT);
    }

    @OnActivityResult(COMMON_SELECT_RESULT)
    void onCommonSelectResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            CLog.w(data.getStringExtra(CommonSelectOptions.RESULT_DATA_TITLE));
        }
    }

    @Click(R.id.btn_refresh_listview)
    void onBtnRefreshListViewClick(View v) {
        RefreshListViewActivity_.intent(activity).start();
    }

    @Click(R.id.btn_refresh_gridview)
    void onBtnRefreshGridViewClick(View v) {
        RefreshGridViewActivity_.intent(activity).start();
    }
}
