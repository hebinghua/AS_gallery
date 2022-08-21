package com.miui.gallery.widget.recyclerview.transition;

import android.graphics.RectF;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public class TimelineTransitionalItem implements HeaderTransitItem {
    public RectF mFrame;
    public long mId;
    public boolean mIsSticky;
    public RecyclerView.ViewHolder mViewHolder;

    public TimelineTransitionalItem(long j, RectF rectF, RecyclerView.ViewHolder viewHolder, boolean z) {
        this.mId = j;
        this.mFrame = rectF;
        this.mViewHolder = viewHolder;
        this.mIsSticky = z;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.HeaderTransitItem
    public RecyclerView.ViewHolder getViewHolder() {
        return this.mViewHolder;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitItem
    public long getTransitId() {
        return this.mId;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitItem
    public RectF getTransitFrame() {
        return this.mFrame;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.HeaderTransitItem
    public boolean isSticky() {
        return this.mIsSticky;
    }
}
