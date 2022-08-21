package com.miui.gallery.view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import com.miui.gallery.util.BaseBuildUtil;

/* loaded from: classes2.dex */
public class ExpandAllGridView extends GridView {
    public boolean mIsMultiWindow;

    public ExpandAllGridView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ExpandAllGridView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIsMultiWindow = !BaseBuildUtil.isLargeHorizontalWindow();
    }

    @Override // android.widget.GridView, android.widget.AbsListView, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(536870911, Integer.MIN_VALUE));
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mIsMultiWindow = !BaseBuildUtil.isLargeHorizontalWindow();
    }
}
