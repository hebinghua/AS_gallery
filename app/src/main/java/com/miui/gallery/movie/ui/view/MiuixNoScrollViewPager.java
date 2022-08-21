package com.miui.gallery.movie.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import miuix.viewpager.widget.ViewPager;

/* loaded from: classes2.dex */
public class MiuixNoScrollViewPager extends ViewPager {
    @Override // miuix.viewpager.widget.ViewPager, androidx.viewpager.widget.OriginalViewPager, android.view.ViewGroup
    @SuppressLint({"RestrictedApi"})
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override // miuix.viewpager.widget.ViewPager, androidx.viewpager.widget.OriginalViewPager, android.view.View
    @SuppressLint({"RestrictedApi"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    public MiuixNoScrollViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
