package com.miui.gallery.widget.recyclerview.transition;

import com.miui.gallery.ui.pictures.PictureViewMode;

/* loaded from: classes3.dex */
public class TransitionAnchor {
    public final int fromAdapterPosition;
    public final long fromAnchorId;
    public final int fromGuideline;
    public final PictureViewMode fromViewMode;
    public final boolean marginStart;
    public final int toAdapterPosition;
    public final long toAnchorId;
    public final int toGuideline;
    public final PictureViewMode toViewMode;

    public TransitionAnchor(long j, long j2, int i, int i2, int i3, int i4, PictureViewMode pictureViewMode, PictureViewMode pictureViewMode2, boolean z) {
        this.fromAnchorId = j;
        this.toAnchorId = j2;
        this.fromAdapterPosition = i;
        this.toAdapterPosition = i2;
        this.fromGuideline = i3;
        this.toGuideline = i4;
        this.fromViewMode = pictureViewMode;
        this.toViewMode = pictureViewMode2;
        this.marginStart = z;
    }

    public boolean isValid() {
        return this.fromAnchorId > 0 && this.toAnchorId > 0;
    }

    public String toString() {
        return "TransitionAnchor{fromAnchorId=" + this.fromAnchorId + ", toAnchorId=" + this.toAnchorId + ", fromAdapterPosition=" + this.fromAdapterPosition + ", toAdapterPosition=" + this.toAdapterPosition + ", fromGuideline=" + this.fromGuideline + ", toGuideline=" + this.toGuideline + ", fromViewMode=" + this.fromViewMode + ", toViewMode=" + this.toViewMode + ", marginStart=" + this.marginStart + '}';
    }
}
