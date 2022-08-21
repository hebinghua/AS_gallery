package com.miui.gallery.cloud;

import com.xiaomi.micloudsdk.exception.CloudServerException;

/* loaded from: classes.dex */
public class GalleryMiCloudServerException extends Exception {
    private Exception mCloudServerException;

    public GalleryMiCloudServerException(Exception exc) {
        this.mCloudServerException = exc;
    }

    public Exception getCloudServerException() {
        return this.mCloudServerException;
    }

    public int getStatusCode() {
        return ((CloudServerException) this.mCloudServerException).getStatusCode();
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return this.mCloudServerException.toString();
    }
}
