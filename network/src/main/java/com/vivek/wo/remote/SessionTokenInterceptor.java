package com.vivek.wo.remote;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class SessionTokenInterceptor implements Interceptor {
    private String sessionToken;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        addRealTimeHeader(builder);
        return chain.proceed(builder.build());
    }

    protected void addRealTimeHeader(Request.Builder builder) {
        //TODO 添加全局请求头
        builder.header("mobile_session_flag", "true");
        if (sessionToken != null) {
            builder.header("session_token", sessionToken);
        }
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
