package com.miui.gallery.ui.photoPage.bars.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes2.dex */
public class LimitRecyclerView extends RecyclerView {
    public int mMaxHeight;

    public LimitRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setMaxHeight(int i) {
        this.mMaxHeight = i;
        invalidate();
    }

    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public void onMeasure(int i, int i2) {
        int i3 = this.mMaxHeight;
        if (i3 > 0) {
            i2 = View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE);
        }
        super.onMeasure(i, i2);
    }
}
