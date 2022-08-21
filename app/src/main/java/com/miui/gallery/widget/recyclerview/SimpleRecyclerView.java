package com.miui.gallery.widget.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import miuix.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public class SimpleRecyclerView extends RecyclerView {
    public boolean mAlwaysDisableSpring;
    public boolean mEnableItemClickWhileSettling;
    public float mFlingScale;
    public boolean mTryingDispatchEvent;

    public SimpleRecyclerView(Context context) {
        this(context, null);
    }

    public SimpleRecyclerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SimpleRecyclerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mFlingScale = 0.6f;
        setItemAnimator(null);
    }

    @Override // miuix.recyclerview.widget.RecyclerView, androidx.recyclerview.widget.RecyclerView
    public boolean fling(int i, int i2) {
        return super.fling((int) (i * this.mFlingScale), i2);
    }

    public void setFlingVelocityScale(float f) {
        this.mFlingScale = f;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x001f, code lost:
        if (r6.mTryingDispatchEvent != false) goto L10;
     */
    @Override // androidx.recyclerview.widget.SpringRecyclerView, androidx.recyclerview.widget.RemixRecyclerView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onInterceptTouchEvent(android.view.MotionEvent r7) {
        /*
            r6 = this;
            int r0 = r6.getScrollState()
            boolean r1 = super.onInterceptTouchEvent(r7)
            boolean r2 = r6.mEnableItemClickWhileSettling
            r3 = 0
            if (r2 == 0) goto L29
            int r2 = r7.getActionMasked()
            r4 = 2
            r5 = 1
            if (r2 == 0) goto L22
            if (r2 == r5) goto L1d
            if (r2 == r4) goto L1a
            goto L29
        L1a:
            r6.mTryingDispatchEvent = r3
            goto L29
        L1d:
            boolean r0 = r6.mTryingDispatchEvent
            if (r0 == 0) goto L29
            goto L2a
        L22:
            if (r0 != r4) goto L27
            r6.mTryingDispatchEvent = r5
            goto L2a
        L27:
            r6.mTryingDispatchEvent = r3
        L29:
            r3 = r1
        L2a:
            java.lang.String r7 = com.miui.gallery.util.BaseMiscUtil.getActionStr(r7)
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r1)
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r3)
            java.lang.String r2 = "SimpleRecyclerViewMiuix"
            java.lang.String r4 = "for event %s, super result: %s, real result: %s"
            com.miui.gallery.util.logger.DefaultLogger.d(r2, r4, r7, r0, r1)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.recyclerview.SimpleRecyclerView.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // androidx.recyclerview.widget.SpringRecyclerView, androidx.recyclerview.widget.RemixRecyclerView, androidx.recyclerview.widget.RecyclerView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mAlwaysDisableSpring) {
            setSpringEnabled(true);
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setAlwaysDisableSpring(boolean z) {
        this.mAlwaysDisableSpring = z;
        setSpringEnabled(!z);
    }

    public void setEnableItemClickWhileSettling(boolean z) {
        this.mEnableItemClickWhileSettling = z;
    }
}
