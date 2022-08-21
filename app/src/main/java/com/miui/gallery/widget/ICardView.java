package com.miui.gallery.widget;

/* loaded from: classes2.dex */
public interface ICardView {
    default int getCurrentIndex() {
        return 0;
    }

    default String getCurrentLocalPath() {
        return "";
    }

    default void setLoadIndex(int i) {
    }
}
