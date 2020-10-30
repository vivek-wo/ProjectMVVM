package com.vivek.wo.remote;

import android.content.Context;

public class AndroidRemoteHelper {
    private static Context mContext;
    private static String mBaseApiUrl = null;
    private static String mToken = null;

    public static void attachApplicationContext(Context context) {
        mContext = context;
    }

    public static boolean checkAttachApplicationContext() {
        return mContext != null;
    }

    public static String getRealTimeToken() {
        //TODO 返回Token
        return mToken;
    }

    public static void setToken(String token) {
        mToken = token;
    }

    public static String getBaseApiUrl() {
        return mBaseApiUrl;
    }

    public static void setBaseApiUrl(String baseApiUrl) {
        mBaseApiUrl = baseApiUrl;
    }
}
