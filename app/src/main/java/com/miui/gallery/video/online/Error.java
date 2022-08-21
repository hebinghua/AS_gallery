package com.miui.gallery.video.online;

/* loaded from: classes2.dex */
public enum Error {
    NO_ERROR(0),
    UNKNOWN(-1),
    FILE_NOT_FOUND(50008),
    FILE_STATUS_ILLEGAL(50050),
    DISABLED(50061),
    VIP_LEVEL_LOW(50062),
    UNSUPPORTED_REGION(50063),
    UNSUPPORTED_TYPE(50064),
    TRANSCODING_NOT_DONE(50065),
    PARAMETERS_UNSUPPORTED_RESOLUTION(50066),
    PARAMETERS_ILLEGAL(50068),
    SERVER_INTERNAL_ERROR(50069),
    SERVER_WITHOUT_WHITELIST(50070),
    TRANSCODE_ERROR(50073);
    
    public final int CODE;

    Error(int i) {
        this.CODE = i;
    }
}
