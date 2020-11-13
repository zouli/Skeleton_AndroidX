package com.riverside.skeleton.android.widget.captcha;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.yoojia.inputs.AndroidNextInputs;
import com.github.yoojia.inputs.StaticScheme;
import com.riverside.skeleton.android.base.utils.KeyboardHelper;
import com.riverside.skeleton.android.base.utils.NextInputsExtend.BlankMessageDisplay;
import com.riverside.skeleton.android.base.utils.NextInputsExtend.InputsAccessExtend;
import com.riverside.skeleton.android.widget.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 获得验证码控件  1.0
 * b_e  2018/02/08
 */
public class InputCaptchaView extends LinearLayout implements CaptchaView {
    public static final int ERROR_PHONE = 1;

    private Activity mContext;
    //验证码输入框
    private EditText et_vc;
    //获取验证码
    private TextView tv_send_vc;
    //回调函数
    private ResultListener onResultListener;
    //获取验证码间隔时间（秒）
    private int sleepSecond;
    //手机号码输入框ID
    private int phoneNumberId;
    //验证码输入框长度
    private int maxLength;
    //设置Hint
    private String textHint;

    public InputCaptchaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = (Activity) context;
        getAttrs(attrs);
        initView();
    }

    void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_input_captcha, this);
        et_vc = findViewById(R.id.et_vc);
        if (maxLength > 0) {
            InputFilter[] filters = {new InputFilter.LengthFilter(maxLength)};
            et_vc.setFilters(filters);
        }
        et_vc.setHint(textHint);
        tv_send_vc = findViewById(R.id.tv_send_vc);
    }

    void getAttrs(AttributeSet attrs) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.InputCaptchaView);
        phoneNumberId = obtainStyledAttributes.getResourceId(R.styleable.InputCaptchaView_icv_phoneNumberId, -1);
        sleepSecond = obtainStyledAttributes.getInt(R.styleable.InputCaptchaView_icv_sleepSecond, 60);
        maxLength = obtainStyledAttributes.getInt(R.styleable.InputCaptchaView_icv_maxLength, 0);
        textHint = obtainStyledAttributes.getString(R.styleable.InputCaptchaView_icv_textHint);
        obtainStyledAttributes.recycle();

        count = sleepSecond;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setListener();
    }

    void setListener() {
        tv_send_vc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doClick(v);
            }
        });
    }

    /**
     * 验证手机号码控件内容
     *
     * @return
     */
    boolean validate() {
        AndroidNextInputs inputs = new AndroidNextInputs();
        final InputsAccessExtend access = new InputsAccessExtend(mContext);
        BlankMessageDisplay blankMessageDisplay = new BlankMessageDisplay();
        inputs.setMessageDisplay(blankMessageDisplay);

        inputs.add(access.findEditText(phoneNumberId), StaticScheme.Required(), StaticScheme.ChineseMobile());
        return inputs.test();
    }

    /**
     * 获取验证码点击事件
     *
     * @param v
     */
    void doClick(View v) {
        KeyboardHelper.init(mContext).hideKeyboard();
        if (!validate()) {
            //调用报错回调
            if (onResultListener != null) {
                onResultListener.onError(ERROR_PHONE);
            }
        } else {
            //开始计时
            startCount();
            //调用获取验证码点击事件
            if (onResultListener != null) {
                onResultListener.onClick();
            }
        }
    }

    /**
     * 取得已输入验证码
     *
     * @return
     */
    @Override
    public String getText() {
        return et_vc.getText().toString();
    }

    private int count;
    private Timer timer;
    private TimerTask timerTask;

    /**
     * 开始计时
     */
    void startCount() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (count > 0) {
                    mContext.runOnUiThread(new Runnable() {
                        public void run() {
                            tv_send_vc.setEnabled(false);
                            tv_send_vc.setText(getResources().getString(R.string.button_view_send_verification_code2, count));
                        }
                    });
                } else {
                    resetCount();
                }
                count--;
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * 重新开始计时
     */
    public void resetCount() {
        mContext.runOnUiThread(new Runnable() {
            public void run() {
                tv_send_vc.setEnabled(true);
                tv_send_vc.setText(getResources().getString(R.string.button_view_send_verification_code));
                count = sleepSecond;
            }
        });
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * 设置回调接口
     *
     * @param onResultListener
     */
    public void setOnResultListener(ResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }
}

