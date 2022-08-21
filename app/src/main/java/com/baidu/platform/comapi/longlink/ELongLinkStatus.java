package com.baidu.platform.comapi.longlink;

/* JADX WARN: Init of enum CloudRestart can be incorrect */
/* JADX WARN: Init of enum CloudStop can be incorrect */
/* JADX WARN: Init of enum ResultConnectError can be incorrect */
/* JADX WARN: Init of enum ResultSendError can be incorrect */
/* JADX WARN: Init of enum ResultServerError can be incorrect */
/* JADX WARN: Init of enum ResultTimeout can be incorrect */
/* JADX WARN: Init of enum SendDataLenLimited can be incorrect */
/* JADX WARN: Init of enum SendFormatError can be incorrect */
/* JADX WARN: Init of enum SendInvalidReqID can be incorrect */
/* JADX WARN: Init of enum SendLimited can be incorrect */
/* JADX WARN: Init of enum SendUnRegistered can be incorrect */
/* loaded from: classes.dex */
public enum ELongLinkStatus {
    OK(0),
    SendFormatError(r0.getStatusCode() + 1),
    SendUnRegistered(r0.getStatusCode() + 2),
    SendLimited(r0.getStatusCode() + 3),
    SendDataLenLimited(r0.getStatusCode() + 4),
    SendInvalidReqID(r0.getStatusCode() + 5),
    ResultConnectError(r0.getStatusCode() + 6),
    ResultSendError(r0.getStatusCode() + 7),
    ResultTimeout(r0.getStatusCode() + 8),
    ResultServerError(r0.getStatusCode() + 9),
    CloudStop(r0.getStatusCode() + 10),
    CloudRestart(r0.getStatusCode() + 11);
    
    private int a;
    private int b;

    static {
        ELongLinkStatus eLongLinkStatus = OK;
    }

    ELongLinkStatus(int i) {
        this.a = i;
    }

    public int getRequestId() {
        return this.b;
    }

    public int getStatusCode() {
        return this.a;
    }

    public void setRequestId(int i) {
        this.b = i;
    }
}
