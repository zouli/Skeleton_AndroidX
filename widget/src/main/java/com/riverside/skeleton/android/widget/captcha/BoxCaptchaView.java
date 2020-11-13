package com.riverside.skeleton.android.widget.captcha;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.riverside.skeleton.android.widget.R;

/**
 * 验证码输入框控件  1.0
 * b_e  2019/04/15
 */
public class BoxCaptchaView extends RelativeLayout implements CaptchaView {
    private Context mContext;
    //根容器
    private LinearLayout ll_content;
    //验证码输入框
    private EditText et_input;
    //验证码显示控件
    private TextView[] tv_shows;

    //输入框个数
    private int charNumber;
    //输入框间隔
    private int divideWidth;
    //显示框样式
    private int itemStyle;

    //输入事件
    private InputTextWatcher inputTextWatcher = new InputTextWatcher();
    //回调函数
    private InputChangedListener inputChangedListener;

    public BoxCaptchaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        getAttrs(attrs);
        initView();
    }

    void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_box_captcha, this);
        ll_content = findViewById(R.id.ll_content);
        et_input = findViewById(R.id.et_input);
    }

    void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.BoxCaptchaView);
        charNumber = typedArray.getInteger(R.styleable.BoxCaptchaView_bcv_charNumber, 6);
        divideWidth = typedArray.getDimensionPixelSize(R.styleable.BoxCaptchaView_bcv_divideWidth, 0);
        itemStyle = typedArray.getResourceId(R.styleable.BoxCaptchaView_bcv_itemStyle, R.style.BoxCaptcha_Item);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initTextViews();
        setListener();
    }

    void initTextViews() {
        tv_shows = new TextView[charNumber];

        et_input.setCursorVisible(false);
        et_input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(charNumber)});
        et_input.requestFocus();

        //生成显示控件
        for (int i = 0; i < charNumber; i++) {
            TextView tv = new TextView(new ContextThemeWrapper(mContext, itemStyle));
            tv.setFocusable(false);

            if (i > 0) {
                View divide = new View(mContext);
                ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(divideWidth, 0);
                divide.setLayoutParams(params);
                ll_content.addView(divide);
            }
            ll_content.addView(tv);
            tv_shows[i] = tv;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        et_input.setWidth(getWidth());
        et_input.setHeight(getHeight());
    }

    void setListener() {
        et_input.addTextChangedListener(inputTextWatcher);
        et_input.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onKeyDelete();
                    return true;
                }
                return false;
            }
        });
    }

    void setText(String inputString) {
        for (TextView tv : tv_shows) {
            if (TextUtils.isEmpty(tv.getText())) {
                tv.setText(inputString);

                if (inputChangedListener != null) {
                    inputChangedListener.inputChanged(getText());
                }
                break;
            }
        }
    }

    void onKeyDelete() {
        for (int i = tv_shows.length - 1; i >= 0; i--) {
            TextView tv = tv_shows[i];
            if (!TextUtils.isEmpty(tv.getText())) {
                tv.setText("");

                if (inputChangedListener != null) {
                    inputChangedListener.inputChanged(getText());
                }

                break;
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
        StringBuilder str = new StringBuilder();
        for (TextView tv : tv_shows) {
            if (!TextUtils.isEmpty(tv.getText())) {
                str.append(tv.getText().toString());
            }
        }
        return str.toString().trim();
    }

    /**
     * 设置回调接口
     *
     * @param inputChangedListener
     */
    public void setOnInputChangedListener(InputChangedListener inputChangedListener) {
        this.inputChangedListener = inputChangedListener;
    }

    private class InputTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();
            if (!TextUtils.isEmpty(str)) {
                String[] strArray = str.split("");
                for (int i = 0; i < strArray.length && i < charNumber; i++) {
                    setText(strArray[i]);
                    et_input.setText("");
                }
            }
        }
    }
}
