package com.miui.gallery.vlog.caption.widget;

import java.util.ArrayList;

/* loaded from: classes2.dex */
public interface IVideoFrameView {
    void reInit();

    void release();

    void setEndPadding(int i);

    void setPixelPerMicrosecond(double d);

    void setStartPadding(int i);

    void setThumbnailAspectRatio(float f);

    void setThumbnailImageFillMode(int i);

    void setThumbnailSequenceDescArray(ArrayList<VideoClipInfo> arrayList);
}
