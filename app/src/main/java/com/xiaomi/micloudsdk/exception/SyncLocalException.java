package com.xiaomi.micloudsdk.exception;

/* loaded from: classes3.dex */
public class SyncLocalException extends Exception {
    private final int mErrorCode;

    public SyncLocalException(int i) {
        super("errorCode: " + i);
        this.mErrorCode = i;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }
}
