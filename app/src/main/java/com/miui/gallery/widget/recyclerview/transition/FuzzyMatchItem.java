package com.miui.gallery.widget.recyclerview.transition;

import java.util.Locale;

/* loaded from: classes3.dex */
public class FuzzyMatchItem {
    public final long mDate;
    public final long mId;
    public final String mLocation;
    public final int mPosition;

    public FuzzyMatchItem(int i, long j, String str, long j2) {
        this.mPosition = i;
        this.mId = j;
        this.mLocation = str;
        this.mDate = j2;
    }

    public String toString() {
        return String.format(Locale.US, "mPosition=%d; mId=%s, mLocation=%s, mDate=%s", Integer.valueOf(this.mPosition), Long.valueOf(this.mId), this.mLocation, Long.valueOf(this.mDate));
    }
}
