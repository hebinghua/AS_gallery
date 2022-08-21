package com.miui.gallery.widget;

import android.view.View;

/* loaded from: classes2.dex */
public interface PanelItem {
    int getPriority();

    View getView();

    void setAlpha(float f);

    void setClickable(boolean z);

    void setEnable(boolean z);
}
