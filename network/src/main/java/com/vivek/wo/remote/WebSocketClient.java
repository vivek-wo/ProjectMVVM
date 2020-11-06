package com.vivek.wo.remote;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketClient {
    private static final String TAG = "WebSocketClient";
    /* Used when actively closes. */
    static final int CLOSE_CLIENT_INITIACTIVE = 1000;
    /* WS 正在打开 */
    static final int WS_OPENING_STATE = 1;
    /* WS 已打开 */
    static final int WS_OPENED_STATE = 2;
    /* WS 正在通信 */
    static final int WS_ONMESSAGE_STATE = 3;
    /* WS 正在关闭 */
    static final int WS_CLOSING_STATE = 4;
    /* WS 已关闭 */
    static final int WS_CLOSED_STATE = 5;
    /* WS 错误失败 */
    static final int WS_ERROR_STATE = -1;

    private static final String HEART_RESPONSE_FILTER = "ok";//定义心跳返回内容
    private static ScheduledExecutorService singleExecutorService;
    private WebSocket mWebSocket;
    private ScheduledFuture mDaemonFuture;
    private int mConnectState;
    OkHttpClient okHttpClient;
    String wsUrl;
    int connectTimeout;
    int readTimeout;
    int writeTimeout;

    private WebSocketClient(Builder builder) {
        this.wsUrl = builder.wsUrl;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        if (builder.okHttpClient != null) {
            this.okHttpClient = builder.okHttpClient;
        } else {
            createDefaultOkHttpClient();
        }

        singleExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    private void createDefaultOkHttpClient() {
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(this.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(this.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(this.writeTimeout, TimeUnit.SECONDS)
                .build();
    }

    public void setup() {
        Request request = new Request.Builder().url(this.wsUrl).build();
        mWebSocket = this.okHttpClient.newWebSocket(request, mWebSocketListener);
        setConnectState(WS_OPENING_STATE);
        mDaemonFuture = singleExecutorService.scheduleWithFixedDelay(daemonRunnable,
                20, 10, TimeUnit.SECONDS);
    }

    public void stop() {
        if (mWebSocket == null) {
            Log.w(TAG, "ws client not setup.");
            return;
        }
        mWebSocket.close(CLOSE_CLIENT_INITIACTIVE, null);
        setConnectState(WS_CLOSING_STATE);
        if (mDaemonFuture != null) {
            mDaemonFuture.cancel(true);
        }
    }

    public boolean isConnected() {
        return true;
    }

    private WebSocketListener mWebSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            setConnectState(WS_OPENED_STATE);
            //启动定时心跳包
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            setConnectState(WS_ONMESSAGE_STATE);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            setConnectState(WS_ONMESSAGE_STATE);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            setConnectState(WS_CLOSING_STATE);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            setConnectState(WS_CLOSED_STATE);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            setConnectState(WS_ERROR_STATE);
        }
    };

    private synchronized void setConnectState(int connectState) {
        Log.d(TAG, "ws change state: " + connectState);
        mConnectState = connectState;
    }

    private void sendHeartMessage() {

    }

    private final Runnable daemonRunnable = () -> {

    };

    public static class Builder {
        OkHttpClient okHttpClient;
        String wsUrl;
        int connectTimeout;
        int readTimeout;
        int writeTimeout;

        public Builder setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public Builder setWsUrl(String wsUrl) {
            this.wsUrl = wsUrl;
            return this;
        }

        public Builder setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder setWriteTimeout(int writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public WebSocketClient build() {
            return new WebSocketClient(this);
        }
    }
}
