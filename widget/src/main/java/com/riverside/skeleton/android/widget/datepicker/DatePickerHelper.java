package com.riverside.skeleton.android.widget.datepicker;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.riverside.skeleton.android.util.converter.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期选择器帮助类 1.1
 * b_e  2017/12/27
 * 1.1  添加是否显示时分的设置
 */
public class DatePickerHelper {
    FragmentManager supportFragmentManager;
    private DatePickerFragment.Callback mFragmentCallback = null;
    private static volatile DatePickerHelper instance;
    private Calendar startCal;
    private Calendar endCal;
    private boolean showTime = false;

    private DatePickerHelper() {
    }

    /**
     * 单例模式
     *
     * @param supportFragmentManager
     * @return
     */
    public static synchronized DatePickerHelper getInstance(FragmentManager supportFragmentManager) {
        if (instance == null) {
            instance = new DatePickerHelper();
        }
        instance.supportFragmentManager = supportFragmentManager;

        Date now = new Date();
        instance.startCal = DateUtils.getCalendar(now);
        instance.endCal = DateUtils.getCalendar(now);

        return instance;
    }

    /**
     * 设置时间
     *
     * @param calendar
     * @return
     */
    public DatePickerHelper setDate(Calendar calendar) {
        return setDate(calendar, calendar);
    }

    /**
     * 设置时间
     *
     * @param startCal
     * @param endCal
     * @return
     */
    public DatePickerHelper setDate(Calendar startCal, Calendar endCal) {
        this.startCal = startCal;
        this.endCal = endCal;
        return this;
    }

    /**
     * 设置是否显示时分
     *
     * @param showTime
     */
    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }

    /**
     * 设置回调
     *
     * @param mFragmentCallback
     * @return
     */
    public DatePickerHelper setOnCallback(DatePickerFragment.Callback mFragmentCallback) {
        this.mFragmentCallback = mFragmentCallback;
        return this;
    }

    /**
     * 显示时间选择器
     */
    public void show() {
        // DialogFragment to host SublimePicker
        DatePickerFragment pickerFrag = new DatePickerFragment();
        pickerFrag.setCallback(mFragmentCallback);

        // Options
        SublimeOptions options = getOptions();

        // Valid options
        Bundle bundle = new Bundle();
        bundle.putParcelable("SUBLIME_OPTIONS", options);
        pickerFrag.setArguments(bundle);

        pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        pickerFrag.show(supportFragmentManager, "SUBLIME_PICKER");
    }

    /**
     * 取得时间选择器设置项
     *
     * @return
     */
    SublimeOptions getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;

        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
        if (showTime) {
            displayOptions |= SublimeOptions.ACTIVATE_TIME_PICKER;
        }
        //displayOptions |= SublimeOptions.ACTIVATE_RECURRENCE_PICKER;
        //options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
        //options.setPickerToShow(SublimeOptions.Picker.TIME_PICKER);
        //options.setPickerToShow(SublimeOptions.Picker.REPEAT_OPTION_PICKER);

        options.setDisplayOptions(displayOptions);

        // Enable/disable the date range selection feature
        //options.setCanPickDateRange(cbAllowDateRangeSelection.isChecked());

        // Example for setting date range:
        // Note that you can pass a date range as the initial date params
        // even if you have date-range selection disabled. In this case,
        // the user WILL be able to change date-range using the header
        // TextViews, but not using long-press.
        options.setDateParams(startCal, endCal);
        if (showTime) {
            options.setTimeParams(startCal.get(Calendar.HOUR_OF_DAY), startCal.get(Calendar.MINUTE), true);
        }

        // If 'displayOptions' is zero, the chosen options are not valid
        return options;
    }
}

