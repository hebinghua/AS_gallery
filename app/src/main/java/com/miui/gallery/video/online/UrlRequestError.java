package com.miui.gallery.video.online;

import java.util.Locale;

/* loaded from: classes2.dex */
public class UrlRequestError extends Exception {
    private final Error mError;

    public UrlRequestError(String str, Error error) {
        super(str);
        this.mError = error;
    }

    public Error getError() {
        return this.mError;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return String.format(Locale.US, "%s, %s", this.mError, super.getMessage());
    }
}
