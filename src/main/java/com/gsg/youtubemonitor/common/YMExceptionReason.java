package com.gsg.youtubemonitor.common;

public enum YMExceptionReason {
    BAD_REQUEST(400),
    ACCESS_DENIED(403),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    private YMExceptionReason(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
