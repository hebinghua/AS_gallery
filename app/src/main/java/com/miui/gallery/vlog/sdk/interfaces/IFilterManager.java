package com.miui.gallery.vlog.sdk.interfaces;

/* loaded from: classes2.dex */
public interface IFilterManager {
    void buildTrackFilter(String str, int i, String str2, boolean z);

    void buildTrackFilter(boolean z, String str, String str2, int i, String str3);

    int getCurrentFilterStrength();

    String getFilterLabel();

    boolean isMasterFilterOpen();

    void removeTrackFilter();

    void updateTrackFilterIntensity(int i);
}
