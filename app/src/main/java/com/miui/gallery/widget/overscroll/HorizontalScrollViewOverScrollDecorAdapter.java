package com.miui.gallery.widget.overscroll;

import android.view.View;
import android.widget.HorizontalScrollView;

/* loaded from: classes2.dex */
public class HorizontalScrollViewOverScrollDecorAdapter implements IOverScrollInterface$IOverScrollDecoratorAdapter {
    public HorizontalScrollView mView;

    public HorizontalScrollViewOverScrollDecorAdapter(HorizontalScrollView horizontalScrollView) {
        this.mView = horizontalScrollView;
    }

    @Override // com.miui.gallery.widget.overscroll.IOverScrollInterface$IOverScrollDecoratorAdapter
    public View getView() {
        return this.mView;
    }

    @Override // com.miui.gallery.widget.overscroll.IOverScrollInterface$IOverScrollDecoratorAdapter
    public boolean isInAbsoluteStart() {
        return this.mView.getScrollX() == 0;
    }

    @Override // com.miui.gallery.widget.overscroll.IOverScrollInterface$IOverScrollDecoratorAdapter
    public boolean isInAbsoluteEnd() {
        return this.mView.getChildCount() == 0 || this.mView.getScrollX() == this.mView.getChildAt(0).getWidth() - this.mView.getWidth();
    }
}
