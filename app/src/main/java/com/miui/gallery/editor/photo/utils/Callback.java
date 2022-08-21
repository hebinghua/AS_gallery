package com.miui.gallery.editor.photo.utils;

/* loaded from: classes2.dex */
public interface Callback<Result, Error> {
    void onCancel();

    void onDone(Result result);

    void onError(Error error);

    void onExecute(Result result);

    void onPrepare();
}
