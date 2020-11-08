package com.gsg.youtubemonitor.common;

public class YMException extends Exception {

    private final YMExceptionReason reason;

    public YMException(YMExceptionReason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public YMException(String s) {
        this(YMExceptionReason.INTERNAL_SERVER_ERROR, s);
    }

    public YMExceptionReason getReason() {
        return reason;
    }
}
