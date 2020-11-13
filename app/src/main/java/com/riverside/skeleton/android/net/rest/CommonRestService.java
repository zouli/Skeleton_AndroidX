package com.riverside.skeleton.android.net.rest;

import com.riverside.skeleton.android.net.jsonbean.JsonResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface CommonRestService {
    @GET(ConstUrls2.LOGIN)
    Flowable<JsonResponse<String>> login();

    @GET(ConstUrls2.LOGOUT)
    Flowable<JsonResponse<String>> logout();

    @GET(ConstUrls2.SESSION_TIMEOUT)
    Flowable<JsonResponse<String>> sessionTimeout();

    @GET(ConstUrls2.GET_LIST)
    Flowable<JsonResponse<List<String>>> getList(@QueryMap Map<String, String> param);
}
