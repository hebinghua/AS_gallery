package com.miui.gallery.gallerywidget.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/* loaded from: classes2.dex */
public class InterceptorLayout extends RelativeLayout {
    public boolean mIntercept;

    public InterceptorLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mIntercept || super.onInterceptTouchEvent(motionEvent);
    }

    public void setIntercept(boolean z) {
        this.mIntercept = z;
    }
}
