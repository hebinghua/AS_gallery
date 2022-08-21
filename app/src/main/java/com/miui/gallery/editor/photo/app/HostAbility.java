package com.miui.gallery.editor.photo.app;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.Map;

/* loaded from: classes2.dex */
public interface HostAbility {
    void addViewToExtraContainer(View view, FrameLayout.LayoutParams layoutParams);

    void clearExtraContainer();

    ViewGroup getExtraContainer();

    void hideInnerToast();

    default void sample(String str) {
    }

    default void sample(String str, Map<String, String> map) {
    }

    void showInnerToast(String str);
}
