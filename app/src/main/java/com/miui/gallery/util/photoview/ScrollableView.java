package com.miui.gallery.util.photoview;

import android.view.View;

/* loaded from: classes2.dex */
public interface ScrollableView {
    void exeShowPlaceholderItem();

    void hidePlaceholderItem();

    void postShowPlaceholderItem(int i);

    void postShowPlaceholderItem(View view);

    default int translatePosition(int i) {
        return -1;
    }

    void viewToPosition(int i);
}
