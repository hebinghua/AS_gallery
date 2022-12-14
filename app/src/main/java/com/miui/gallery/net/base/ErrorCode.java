package com.miui.gallery.net.base;

/* loaded from: classes2.dex */
public enum ErrorCode {
    SUCCESS(0),
    PARSE_ERROR(-1),
    NET_ERROR(-2),
    HANDLE_ERROR(-3),
    BODY_EMPTY(-4),
    DATA_BIND_ERROR(-5),
    SERVER_ERROR(-6),
    NETWORK_NOT_CONNECTED(-7);
    
    public int CODE;

    ErrorCode(int i) {
        this.CODE = i;
    }
}
