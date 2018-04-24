package com.example.administrator.phonedemo.RxJavaUtils;


public class RetrofitHttpUtil {
    public static ApiService getApiService() {
        return RetrofitHelper.getRetrofit().create(ApiService.class);
    }
}
