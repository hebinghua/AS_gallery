package com.xiaomi.mediaprocess;

/* loaded from: classes3.dex */
public enum ErrorCode {
    ERROR_UNKNOWN(0),
    ERROR_NO_SUPPORT_TYPE(1),
    ERROR_NO_DISK_SPACE(2);
    
    private int nCode;

    ErrorCode(int i) {
        this.nCode = i;
    }

    @Override // java.lang.Enum
    public String toString() {
        return String.valueOf(this.nCode);
    }

    public int getCode() {
        return this.nCode;
    }

    public static ErrorCode int2enum(int i) {
        ErrorCode[] values;
        ErrorCode errorCode = ERROR_UNKNOWN;
        for (ErrorCode errorCode2 : values()) {
            if (errorCode2.ordinal() == i) {
                errorCode = errorCode2;
            }
        }
        return errorCode;
    }
}
