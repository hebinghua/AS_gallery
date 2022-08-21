package com.nexstreaming.app.common.tracelog;

import android.content.Context;
import com.nexstreaming.app.common.task.Task;

/* loaded from: classes3.dex */
public enum ResponseCode implements Task.TaskError {
    SUCCESS(0),
    APPAUTH_FAILED(1),
    LICENSE_EXPIRED(2),
    DLLIMIT_EXCEEDED(3),
    NO_AVAILABLE_LICENSE(4),
    INVALID_APIVERSION(5),
    ERR_NORMAL(6),
    ERR_FATAL(7),
    INVALID_CODECID(8),
    INVALID_DEVICE(9),
    UPDATE_AVAILABLE(10),
    UPDATE_NOTAVAILABLE(11),
    APP_VERSION_EXISTED(12),
    NOTICE_AVAILABLE(13),
    NOTICE_NOTAVAILABLE(14),
    INVALID_PROMOCODE(15),
    EXPIRED_PROMOCODE(16),
    ALREADYINUSE_PROMOCODE(17),
    PROMOCODE_NONE(18),
    PROMOCODE_ALREADY(19),
    ALREADY_IAP_REGISTERED(20),
    APP_BLOCKDATE(21),
    INVALID_SOUNDTRACKID(22),
    NO_MATCH(23),
    SOMETHING_WRONG(505),
    UNKNOWN;
    
    private final boolean unknown;
    private final int value;

    @Override // com.nexstreaming.app.common.task.Task.TaskError
    public Exception getException() {
        return null;
    }

    ResponseCode(int i) {
        this.value = i;
        this.unknown = false;
    }

    ResponseCode() {
        this.value = 0;
        this.unknown = true;
    }

    public int value() {
        return this.value;
    }

    @Override // com.nexstreaming.app.common.task.Task.TaskError
    public String getMessage() {
        if (this.unknown) {
            return "UNKNOWN";
        }
        return name() + " [" + this.value + "]";
    }

    public static ResponseCode fromValue(int i) {
        ResponseCode[] values;
        for (ResponseCode responseCode : values()) {
            if (responseCode.value == i) {
                return responseCode;
            }
        }
        return UNKNOWN;
    }

    @Override // com.nexstreaming.app.common.task.Task.TaskError
    public String getLocalizedMessage(Context context) {
        return getMessage();
    }

    public int getIntErrorCode() {
        return value();
    }
}
