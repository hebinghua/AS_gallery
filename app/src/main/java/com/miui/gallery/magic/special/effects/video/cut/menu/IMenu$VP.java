package com.miui.gallery.magic.special.effects.video.cut.menu;

import android.util.Size;
import android.widget.ImageView;

/* loaded from: classes2.dex */
public interface IMenu$VP {
    void addImageToBody(ImageView imageView);

    void cutVideo();

    int[] getCurrentTimes(int i);

    void initFinish(Size size);

    void loadListData();

    void onStopTrackingTouch();

    void seekTo(long j, boolean z);

    void setDuration(int i);

    void setProgress(int i);
}
