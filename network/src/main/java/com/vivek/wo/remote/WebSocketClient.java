package com.vivek.wo.remote;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
    /* Used when server not reply. */
    static final int CLOSE_CLIENT_NOT_REPLY = 1001;
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

    private static final String DEFAULT_HEART_RESPONSE = "ok";//定义心跳返回内容

    private static ScheduledExecutorService singleExecutorService;
    private WebSocketMessageListener mMessageListener;
    private WebSocket mWebSocket;
    private ScheduledFuture mHeartFuture;
    private ScheduledFuture mResetFuture;
    private int mConnectState;
    /*心跳发送次数统计*/
    private AtomicInteger mHeartCount = new AtomicInteger(0);
    private String mHeartMessage;
    private boolean mIsActiveClose = false;
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

    public void initHeartMessage(String heartMessage) {
        mHeartMessage = heartMessage;
    }

    public synchronized void start() {
        if (mWebSocket != null) {
            Log.w(TAG, "ws client started, need call stop.");
            return;
        }
        Log.i(TAG, "ws client start.");
        startWebSocket();
    }

    private void startWebSocket() {
        mIsActiveClose = false;
        Request request = new Request.Builder().url(this.wsUrl).build();
        mWebSocket = this.okHttpClient.newWebSocket(request, mWebSocketListener);
        setConnectState(WS_OPENING_STATE);
    }

    public synchronized void stop() {
        if (mWebSocket == null) {
            Log.w(TAG, "ws client not start.");
            return;
        }
        mIsActiveClose = true;
        Log.i(TAG, "ws client stop.");
        mWebSocket.close(CLOSE_CLIENT_INITIACTIVE, null);
        setConnectState(WS_CLOSING_STATE);
        cancelResetFuture();
        cancelHeartFuture();
        mWebSocket = null;
    }

    private synchronized void setupHeartFuture() {
        if (!mIsActiveClose) {
            mHeartCount.set(0);
            mHeartFuture = singleExecutorService.scheduleAtFixedRate(() -> {
                sendHeartMessage();
            }, 0, 5, TimeUnit.SECONDS);
        }
    }

    private synchronized void resetWebSocket() {
        cancelHeartFuture();
        mResetFuture = singleExecutorService.schedule(() -> {
            synchronized (this) {
                if (!mIsActiveClose) {
                    Log.i(TAG, "ws client reset.");
                    startWebSocket();
                }
            }
        }, 5, TimeUnit.SECONDS);
    }

    public boolean isConnected() {
        return mConnectState == WS_ONMESSAGE_STATE;
    }

    public void setWebSocketMessageListener(WebSocketMessageListener listener) {
        mMessageListener = listener;
    }

    private WebSocketListener mWebSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            setConnectState(WS_OPENED_STATE);
            setupHeartFuture();
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            setConnectState(WS_ONMESSAGE_STATE);
            System.out.println("ws message: " + text);
            //判断是否是心跳回复
            if (isHeartMessageReply(text)) {
                return;
            }
            if (mMessageListener != null) {
                mMessageListener.onMessage(WebSocketClient.this, text);
            }
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
            resetWebSocket();
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            setConnectState(WS_ERROR_STATE);
            resetWebSocket();
        }
    };

    private synchronized void setConnectState(int connectState) {
        Log.d(TAG, "ws change state: " + connectState);
        mConnectState = connectState;
    }

    /**
     * 发送心跳数据
     */
    void sendHeartMessage() {
        int count = mHeartCount.getAndIncrement();
        Log.d(TAG, "Heart " + mHeartMessage + ", count: " + mHeartCount.get());
        if (count > 0) {
            mWebSocket.close(CLOSE_CLIENT_NOT_REPLY, null);
        } else {
            mWebSocket.send(mHeartMessage);
        }
    }

    /**
     * 是否心跳回复数据
     *
     * @param text
     * @return
     */
    boolean isHeartMessageReply(String text) {
        boolean isReply = DEFAULT_HEART_RESPONSE.equals(text);
        if (isReply) {
            mHeartCount.getAndSet(0);
        }
        return isReply;
    }

    private void cancelHeartFuture() {
        if (mHeartFuture != null) {
            mHeartFuture.cancel(true);
            mHeartFuture = null;
        }
    }

    private void cancelResetFuture() {
        if (mResetFuture != null) {
            mResetFuture.cancel(true);
            mResetFuture = null;
        }
    }

    public static class Builder {
        OkHttpClient okHttpClient;
        String wsUrl;
        int connectTimeout = 10;
        int readTimeout = 5;
        int writeTimeout = 5;

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
