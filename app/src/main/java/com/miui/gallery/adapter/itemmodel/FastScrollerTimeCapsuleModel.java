package com.miui.gallery.adapter.itemmodel;

import com.miui.gallery.widget.recyclerview.FastScrollerCapsuleContentProvider;

/* loaded from: classes.dex */
public class FastScrollerTimeCapsuleModel implements FastScrollerCapsuleContentProvider<Long> {
    public String mLocation;
    public long mSortTime;

    public void setSortTime(long j) {
        this.mSortTime = j;
    }

    public void setLocation(String str) {
        this.mLocation = str;
    }

    public String getLocation() {
        return this.mLocation;
    }

    public Long getContent() {
        return Long.valueOf(this.mSortTime);
    }
}
