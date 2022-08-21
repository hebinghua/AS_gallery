package com.miui.gallery.ui;

/* loaded from: classes2.dex */
public interface ImmersionMenuSupport {
    String getPageName();

    int getSupportedAction();

    void onActionClick(int i);

    default boolean isActionSupport(int i) {
        return (i & getSupportedAction()) != 0;
    }
}
