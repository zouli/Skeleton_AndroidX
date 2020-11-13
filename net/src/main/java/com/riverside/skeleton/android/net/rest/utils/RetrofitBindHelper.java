package com.riverside.skeleton.android.net.rest.utils;

import android.text.TextUtils;

import com.riverside.skeleton.android.base.application.BaseApplication;
import com.riverside.skeleton.android.net.ConstUrls;
import com.riverside.skeleton.android.net.rest.handler.SessionHandlerFactory;
import com.riverside.skeleton.android.util.log.CLog;
import com.riverside.skeleton.android.util.packageinfo.PackageInfoUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 网络连接服务类 1.0
 * b_e  2017/12/25
 */
public class RetrofitBindHelper {
    private static String baseUrl;
    private static volatile ConcurrentHashMap<String, RetrofitBindHelper> mapRetrofit = new ConcurrentHashMap<>();
    protected Retrofit retrofit;

    /**
     * 创建网络对象
     */
    protected RetrofitBindHelper() {
        retrofit = new Retrofit.Builder()
                .client(genericClient())
                .baseUrl(getBaseUrl())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 设置服务器地址
     *
     * @param url
     */
    public static void setBaseUrl(String url) {
        baseUrl = url;
    }

    /**
     * 取得服务器地址
     *
     * @return
     */
    public static String getBaseUrl() {
        if (TextUtils.isEmpty(baseUrl)) {
            // 取得server_host中设置的地址
            baseUrl = ConstUrls.SERVER_ROOT;
        }
        return baseUrl;
    }

    /**
     * 生成通用客户端
     *
     * @return
     */
    private OkHttpClient genericClient() {
        //生成客户端对象
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient().newBuilder();
        okhttpBuilder.connectTimeout(ConstUrls.CONNECT_TIME_OUT, TimeUnit.SECONDS);

        //设置Header
        okhttpBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder();

                //根据http_header的设置，取得Header设置
                int httpHeaderId = PackageInfoUtils.getMetadataInt("HTTP_HEADER", -1);
                if (httpHeaderId > -1) {
                    String[] httpHeader = BaseApplication.getInstance().getResources().getStringArray(httpHeaderId);
                    for (String header : httpHeader) {
                        String[] items = header.split(":", 2);
                        String key = items[0].trim();
                        String value = items[1].trim();
                        if (value.startsWith("${") && value.endsWith("}")) {
                            value = SessionHandlerFactory.getHeaderValue(value.substring(2, value.length() - 1));
                        }
                        if (null != value) {
                            requestBuilder.addHeader(key, value);
                        }
                    }
                }

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        // 设置Log的生成级别
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (CLog.isDebug()) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        okhttpBuilder.addInterceptor(interceptor);

        // 设置Cookie的保存和读取
        okhttpBuilder.cookieJar(new CookieJar() {
            private final List<Cookie> cookieStore = new ArrayList<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                // 判断是否保存Session
                if (SessionHandlerFactory.canToSave(url.toString())) {
                    cookieStore.clear();
                    cookieStore.addAll(cookies);
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                return cookieStore;
            }
        });
        return okhttpBuilder.build();
    }

    /**
     * 取得当前对象
     *
     * @return
     */
    public static RetrofitBindHelper getInstance() {
        RetrofitBindHelper retrofitBindHelper = mapRetrofit.get(getBaseUrl());

        if (retrofitBindHelper == null) {
            synchronized (RetrofitBindHelper.class) {
                retrofitBindHelper = new RetrofitBindHelper();
                mapRetrofit.put(getBaseUrl(), retrofitBindHelper);
            }
        }
        return retrofitBindHelper;
    }

    /**
     * 绑定Rest服务
     *
     * @param restService
     * @param <T>
     * @return
     */
    public <T> T doBind(Class<T> restService) {
        return retrofit.create(restService);
    }

    /**
     * 生成文件上传对象
     *
     * @param name
     * @param file
     * @param listener
     * @return
     */
    public static RequestBody getFilePart(String name, File file, ProgressRequestBody.UploadCallbacks listener) {
        ProgressRequestBody.Builder builder = new ProgressRequestBody.Builder();
        builder.addFormDataPart(name, file.getName(), file.getAbsolutePath(), listener);
        return builder.build();
    }
}

