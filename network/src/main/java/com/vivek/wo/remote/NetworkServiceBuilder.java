package com.vivek.wo.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkServiceBuilder {
    public static final String BASE_URL = "";// TODO API请求地址
    public static final int TIMEOUT_CONNECT_SECONDS = 5;
    public static final int TIMEOUT_READ_SECONDS = 5;
    public static final int TIMEOUT_WRITE_SECONDS = 10;

    protected OkHttpClient.Builder mOkHttpClientBuilder;
    protected Retrofit.Builder mRetrofitBuilder;

    protected void buildOkHttpClientBuilder() {
        mOkHttpClientBuilder = new OkHttpClient.Builder();
        mOkHttpClientBuilder.connectTimeout(TIMEOUT_CONNECT_SECONDS, TimeUnit.SECONDS);
        mOkHttpClientBuilder.readTimeout(TIMEOUT_READ_SECONDS, TimeUnit.SECONDS);
        mOkHttpClientBuilder.writeTimeout(TIMEOUT_WRITE_SECONDS, TimeUnit.SECONDS);
    }

    protected void buildInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpClientBuilder.addInterceptor(loggingInterceptor);
    }

    protected void buildRetrofitBuilder() {
        mRetrofitBuilder = new Retrofit.Builder();
        mRetrofitBuilder.baseUrl(BASE_URL);
        mRetrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        mRetrofitBuilder.addConverterFactory(GsonConverterFactory.create());
    }

    protected void buildOtherChanged() {
        if (AndroidRemoteHelper.getBaseApiUrl() != null) {
            mRetrofitBuilder.baseUrl(AndroidRemoteHelper.getBaseApiUrl());
        }
        SessionTokenInterceptor interceptor = new SessionTokenInterceptor() {
            @Override
            protected void addRealTimeHeader(Request.Builder builder) {
                setSessionToken(AndroidRemoteHelper.getRealTimeToken());
                super.addRealTimeHeader(builder);
            }
        };
        mOkHttpClientBuilder.addInterceptor(interceptor);
    }

    public final <T> T create(Class<T> cls) {
        buildOkHttpClientBuilder();
        buildInterceptor();
        buildRetrofitBuilder();
        buildOtherChanged();
        return mRetrofitBuilder.client(mOkHttpClientBuilder.build()).build().create(cls);
    }
}
