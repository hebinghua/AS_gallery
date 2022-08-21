package com.miui.gallery.video.timeburst;

/* loaded from: classes2.dex */
public interface IBurstPhotoComposer {
    void cancel();

    void compose();

    void release();

    void setComposeCallback(ComposeCallback composeCallback);
}
