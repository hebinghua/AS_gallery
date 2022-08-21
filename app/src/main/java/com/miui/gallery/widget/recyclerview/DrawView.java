package com.miui.gallery.widget.recyclerview;

import android.graphics.Canvas;

/* loaded from: classes3.dex */
public interface DrawView {
    void draw(Canvas canvas);

    int getViewHeight();

    int getViewWidth();

    void setStyle(int i);

    void setVisible();
}
