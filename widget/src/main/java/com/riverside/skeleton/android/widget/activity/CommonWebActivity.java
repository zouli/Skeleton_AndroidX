package com.riverside.skeleton.android.widget.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.riverside.skeleton.android.base.activity.BaseActivity;
import com.riverside.skeleton.android.widget.R;

/**
 * 共通Web画面  1.0
 * b_e  2018/02/16
 */
public class CommonWebActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private LinearLayout ll_web;
    private WebView wv_content;
    private ProgressBar pb_loading;

    private String url;
    String title_name = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        initView();
        setListener();
    }

    private void getData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title_name = intent.getStringExtra("title_name");
    }

    private void initView() {
        setContentView(R.layout.activity_common_web);
        ll_web = findViewById(R.id.ll_web);
        pb_loading = findViewById(R.id.pb_loading);
        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbar_title.setText(title_name);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionKey(KeyEvent.KEYCODE_BACK);
            }
        });
        toolbar.setNavigationIcon(R.drawable.web_toolbar_back);

        createWebView();

        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        wv_content.setWebViewClient(nativeWebViewClient);
        wv_content.setWebChromeClient(nativeWebChromeClient);
        wv_content.loadUrl(url);

    }

    private void setListener() {
    }

    void createWebView() {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wv_content = new WebView(getApplicationContext());
        wv_content.setLayoutParams(params);
        ll_web.addView(wv_content);

        settingWebview();
    }

    @SuppressLint("SetJavaScriptEnabled")
    void settingWebview() {
        // 启用支持javascript
        WebSettings settings = wv_content.getSettings();
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setLoadsImagesAutomatically(true);

        //调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        saveData(settings);
        newWin(settings);
    }

    /**
     * 多窗口的问题
     */
    private void newWin(WebSettings mWebSettings) {
        //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下
        //然后 复写 WebChromeClient的onCreateWindow方法
        mWebSettings.setSupportMultipleWindows(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }


    /**
     * HTML5数据存储
     */
    private void saveData(WebSettings mWebSettings) {
        //有时候网页需要自己保存一些关键数据,Android WebView 需要自己设置
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        mWebSettings.setAppCachePath(appCachePath);
    }

    void actionKey(final int keyCode) {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(keyCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wv_content.canGoBack()) {
            wv_content.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
     */
    WebViewClient nativeWebViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (TextUtils.isEmpty(title_name)) {
                toolbar_title.setText(view.getTitle());
            }
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return handleUri(view, request.getUrl());
        }

        /**
         * 多页面在同一个WebView中打开，就是不新建activity或者调用系统浏览器打开
         */
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return handleUri(view, Uri.parse(url));
        }

        private boolean handleUri(WebView view, final Uri uri) {
            String scheme = uri.getScheme();
            if (scheme.startsWith("http")) {
//                view.loadUrl(uri.toString());
                return false;
            } else if (scheme.startsWith("tel") || scheme.startsWith("mailto") || scheme.startsWith("geo")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
            return true;
        }
    };

    WebChromeClient nativeWebChromeClient = new WebChromeClient() {
        //=========HTML5定位==========================================================
        //需要先加入权限
        //<uses-permission android:name="android.permission.INTERNET"/>
        //<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
        //<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);//注意个函数，第二个参数就是是否同意定位权限，第三个是是否希望内核记住
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
        //=========HTML5定位==========================================================

        //=========多窗口的问题==========================================================
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(wv_content);
            resultMsg.sendToTarget();
            return true;
        }
        //=========多窗口的问题==========================================================

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                pb_loading.setVisibility(View.INVISIBLE);
            } else {
                pb_loading.setVisibility(View.VISIBLE);
                pb_loading.setProgress(newProgress);
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(CommonWebActivity.this)
                    .setTitle(R.string.app_name)
                    .setMessage(message)
                    .setPositiveButton("OK", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    });
            dialog.setCancelable(false);
            dialog.create();
            dialog.show();
            return true;
        }
    };

    @Override
    protected void onDestroy() {
        if (wv_content != null) {
            wv_content.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            wv_content.clearHistory();

            ((ViewGroup) wv_content.getParent()).removeView(wv_content);
            wv_content.destroy();
            wv_content = null;
        }
        System.exit(0);
        super.onDestroy();
    }
}
