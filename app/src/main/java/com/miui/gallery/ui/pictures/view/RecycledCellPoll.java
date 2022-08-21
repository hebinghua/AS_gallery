package com.miui.gallery.ui.pictures.view;

import android.view.View;

/* loaded from: classes2.dex */
public interface RecycledCellPoll {
    ImageCell obtainFor(View view);

    void release(ImageCell imageCell);
}
