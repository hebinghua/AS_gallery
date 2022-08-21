package com.miui.gallery.search.navigationpage;

/* loaded from: classes2.dex */
public interface NavigationSectionContentView {
    NavigationSectionAdapter getContentAdapter();

    default void refreshResource() {
    }

    void setContentAdapter(NavigationSectionAdapter navigationSectionAdapter);
}
