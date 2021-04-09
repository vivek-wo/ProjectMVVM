package com.vivek.wo.remote;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WSClientOperation {
    private static ScheduledExecutorService singleExecutorService;
    /* ws 超时设置 */
    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 5;
    private static final int WRITE_TIMEOUT = 5;

    private WebSocketClient mWebSocketClient;

    private String wsUrl;
    private OnMessageListener onMessageListener;

    private WSClientOperation() {
        singleExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    public static WSClientOperation get() {
        return Holder.INSTANCE;
    }

    public interface OnMessageListener {
        void onMessage(WebSocketClient client, String text);
    }

    public void setWebSocketUrl(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    public void setOnMessageListener(OnMessageListener listener) {
        this.onMessageListener = listener;
    }

    public synchronized void startWebSocket(String heartMessage) {
        if (mWebSocketClient != null) {
            closeWebSocket();
        }
        mWebSocketClient = new WebSocketClient(this.wsUrl);
        mWebSocketClient.setOnMessageListener(this.onMessageListener);
        mWebSocketClient.start(heartMessage);
    }

    public synchronized void closeWebSocket() {
        mWebSocketClient.stop();
        mWebSocketClient = null;
    }

    public boolean isConnected() {
        return mWebSocketClient != null && mWebSocketClient.isConnected();
    }

    private static class Holder {
        private static final WSClientOperation INSTANCE = new WSClientOperation();
    }

    public static class WebSocketClient extends WebSocketListener {
        private static final String TAG = "WebSocketClient";
        /* Used when actively closes. */
        static final int CLOSE_CLIENT_INITIACTIVE = 1000;
        /* Used when server not reply. */
        static final int CLOSE_CLIENT_NOT_REPLY = 1001;

        /* 心跳相关设置 */
        private static final String DEFAULT_HEART_RESPONSE = "ok";//TODO 定义心跳返回内容
        private static final int DEFAULT_HEART_REPLY_TIMEOUT = 5;//心跳回复超时时间（秒）
        private static final int DEFAULT_HEART_TIMEOUT_COUNT = 2; // 定义心跳超时次数，每5秒1次

        private ScheduledFuture mHeartFuture;//心跳发送定时器
        private ScheduledFuture mResetFuture;//失败重试定时器
        private AtomicBoolean mIsActiveClose = new AtomicBoolean(false); // 是否主动关闭

        private WebSocket mWebSocket;
        private OnMessageListener mMessageListener;

        private String wsUrl; // websocket服务地址
        private OkHttpClient okHttpClient;
        private String heartMessage;// 心跳包
        private boolean isConnected; // 是否已连接

        public WebSocketClient(String wsUrl) {
            this(wsUrl, null);
        }

        public WebSocketClient(String wsUrl, OkHttpClient okHttpClient) {
            this.wsUrl = wsUrl;
            this.okHttpClient = okHttpClient;
        }

        /*心跳发送次数统计*/
        private AtomicInteger mHeartCount = new AtomicInteger(0);

        private void createDefaultOkHttpClient() {
            this.okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .build();
        }

        public void setOnMessageListener(OnMessageListener listener) {
            mMessageListener = listener;
        }

        public synchronized void start(String heartMessage) {
            if (mWebSocket != null) {
                Log.w(TAG, "ws client started.");
                return;
            }
            Log.i(TAG, "ws client start.");
            this.heartMessage = heartMessage;
            Request request = new Request.Builder().url(this.wsUrl).build();
            if (this.okHttpClient == null) {
                createDefaultOkHttpClient();
            }
            mWebSocket = this.okHttpClient.newWebSocket(request, this);
        }

        public synchronized void stop() {
            mIsActiveClose.set(true);
            cancelHeartFuture();
            cancelResetFuture();
            Log.i(TAG, "ws client stop.");
            mWebSocket.close(CLOSE_CLIENT_INITIACTIVE, null);
            mWebSocket = null;
            mMessageListener = null;
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

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            Log.i(TAG, "ws client open setup heart.");
            setupHeartFuture();
        }

        private void setupHeartFuture() {
            mHeartCount.set(0);
            mHeartFuture = singleExecutorService.scheduleAtFixedRate(() -> {
                sendHeartMessage();
            }, 0, DEFAULT_HEART_REPLY_TIMEOUT, TimeUnit.SECONDS);
        }

        /**
         * 发送心跳数据
         */
        void sendHeartMessage() {
            if (mIsActiveClose.get()) {
                log("ws client sendHeartMessage but active close on future.");
                return;
            }
            int count = mHeartCount.getAndIncrement();
            log(">> Heart " + heartMessage + ", count: " + mHeartCount.get());
            if (count > DEFAULT_HEART_TIMEOUT_COUNT) {
                if (mWebSocket != null) {
                    mWebSocket.close(CLOSE_CLIENT_NOT_REPLY, null);
                }
            } else {
                if (heartMessage != null) {
                    mWebSocket.send(heartMessage);
                }
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            isConnected = true;
            log(">> ws message: " + text);
            //判断是否是心跳回复
            if (isHeartMessageReply(text)) {
                return;
            }
            if (mMessageListener != null) {
                mMessageListener.onMessage(this, text);
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

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            isConnected = true;
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            Log.i(TAG, "ws client onClosed.");
            resetWebSocket();
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            Log.i(TAG, "ws client onFailure.");
            resetWebSocket();
        }

        private synchronized void resetWebSocket() {
            isConnected = false;
            if (mIsActiveClose.get()) {
                log("ws client reset but active close.");
                return;
            }
            cancelHeartFuture();
            mResetFuture = singleExecutorService.schedule(() -> {
                startWebSocket();
            }, 5, TimeUnit.SECONDS);
        }

        private void startWebSocket() {
            if (mIsActiveClose.get()) {
                log("ws client startWebSocket but active close.");
                return;
            }
            Log.i(TAG, "ws client restart.");
            Request request = new Request.Builder().url(this.wsUrl).build();
            mWebSocket = this.okHttpClient.newWebSocket(request, this);
        }

        private void log(String log) {
            Log.d(TAG, log);
        }

        public boolean isConnected() {
            return isConnected;
        }
    }
}
