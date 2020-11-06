package com.vivek.wo.remote;

public class RemoteApiException extends Exception {
    private int errorCode;

    public RemoteApiException(int errorCode) {
        this.errorCode = errorCode;
    }

    public RemoteApiException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public RemoteApiException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
