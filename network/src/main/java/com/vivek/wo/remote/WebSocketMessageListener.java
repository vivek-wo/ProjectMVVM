package com.vivek.wo.remote;

public interface WebSocketMessageListener {

    void onMessage(WebSocketClient client, String text);
}
