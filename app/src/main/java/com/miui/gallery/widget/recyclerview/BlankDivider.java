package com.miui.gallery.widget.recyclerview;

import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.OrientationProvider;

/* loaded from: classes3.dex */
public class BlankDivider extends RecyclerView.ItemDecoration {
    public int mBottom;
    public int mEnd;
    public int mInterval;
    public OrientationProvider mOrientationProvider;
    public int mStart;
    public int mTop;

    public BlankDivider(Resources resources, int i) {
        this(i == 0 ? 0 : resources.getDimensionPixelSize(i));
    }

    public BlankDivider(int i) {
        this(i, i, i, 0, 0);
    }

    public BlankDivider(int i, int i2, int i3, int i4, int i5) {
        this.mOrientationProvider = OrientationProvider.PORTRAIT;
        this.mStart = i;
        this.mEnd = i2;
        this.mInterval = i3;
        this.mTop = i4;
        this.mBottom = i5;
    }

    public void updateItemDecorationStartEnd(int i, int i2) {
        this.mStart = i;
        this.mEnd = i2;
    }

    public void setOrientationProvider(OrientationProvider orientationProvider) {
        this.mOrientationProvider = orientationProvider;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        super.getItemOffsets(rect, view, recyclerView, state);
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
        int i7 = 0;
        if (!this.mOrientationProvider.isPortrait(recyclerView.getContext())) {
            if (childAdapterPosition == -1) {
                DefaultLogger.w("BlankDivider", "no adapter bound");
                return;
            }
            if (recyclerView.getChildAdapterPosition(view) == 0) {
                i = this.mTop;
                i2 = this.mBottom;
                i7 = this.mStart;
                i3 = this.mInterval;
            } else if (recyclerView.getChildAdapterPosition(view) == recyclerView.getAdapter().getItemCount() - 1) {
                i = this.mTop;
                i2 = this.mBottom;
                i3 = this.mEnd;
            } else {
                i = this.mTop;
                i2 = this.mBottom;
                i3 = this.mInterval;
            }
            rect.set(i, i7, i2, i3);
        } else if (childAdapterPosition == -1) {
            DefaultLogger.w("BlankDivider", "no adapter bound");
        } else {
            if (recyclerView.getChildAdapterPosition(view) == 0) {
                i7 = this.mStart;
                i4 = this.mInterval;
                i5 = this.mTop;
                i6 = this.mBottom;
            } else if (recyclerView.getChildAdapterPosition(view) == recyclerView.getAdapter().getItemCount() - 1) {
                i4 = this.mEnd;
                i5 = this.mTop;
                i6 = this.mBottom;
            } else {
                i4 = this.mInterval;
                i5 = this.mTop;
                i6 = this.mBottom;
            }
            if (recyclerView.getLayoutDirection() == 1) {
                rect.set(i4, i5, i7, i6);
            } else {
                rect.set(i7, i5, i4, i6);
            }
        }
    }
}
