package com.miui.gallery.cloud.card.exception;

/* loaded from: classes.dex */
public class NoResultException extends Exception {
    @Override // java.lang.Throwable
    public String getMessage() {
        return "CommonGalleryRequest do not return target object!";
    }
}
