package com.miui.gallery.widget.tsd;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import androidx.viewpager2.widget.ViewPager2;
import com.miui.gallery.R$styleable;
import com.miui.gallery.util.ViewUtils;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: NestedScrollableHost.kt */
/* loaded from: classes3.dex */
public final class NestedScrollableHost extends FrameLayout {
    public float initialX;
    public float initialY;
    public boolean isChildHasSameDirection;
    public int touchSlop;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NestedScrollableHost(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "context");
        this.isChildHasSameDirection = true;
        this.touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.NestedScrollableHost);
        Intrinsics.checkNotNullExpressionValue(obtainStyledAttributes, "context.obtainStyledAttr…ble.NestedScrollableHost)");
        this.isChildHasSameDirection = obtainStyledAttributes.getBoolean(0, false);
        obtainStyledAttributes.recycle();
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x001b A[LOOP:0: B:6:0x000d->B:11:0x001b, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0022  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x000c A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:20:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:5:0x000c -> B:6:0x000d). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final androidx.viewpager2.widget.ViewPager2 getParentViewPager() {
        /*
            r3 = this;
            android.view.ViewParent r0 = r3.getParent()
            boolean r1 = r0 instanceof android.view.View
            r2 = 0
            if (r1 == 0) goto Lc
            android.view.View r0 = (android.view.View) r0
            goto Ld
        Lc:
            r0 = r2
        Ld:
            if (r0 == 0) goto L1e
            boolean r1 = r0 instanceof androidx.viewpager2.widget.ViewPager2
            if (r1 != 0) goto L1e
            android.view.ViewParent r0 = r0.getParent()
            boolean r1 = r0 instanceof android.view.View
            if (r1 == 0) goto Lc
            android.view.View r0 = (android.view.View) r0
            goto Ld
        L1e:
            boolean r1 = r0 instanceof androidx.viewpager2.widget.ViewPager2
            if (r1 == 0) goto L25
            r2 = r0
            androidx.viewpager2.widget.ViewPager2 r2 = (androidx.viewpager2.widget.ViewPager2) r2
        L25:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.tsd.NestedScrollableHost.getParentViewPager():androidx.viewpager2.widget.ViewPager2");
    }

    private final View getChild() {
        return ViewUtils.getChildRecyclerView(this);
    }

    public final boolean canChildScroll(int i, float f) {
        int i2 = -((int) Math.signum(f));
        if (i == 0) {
            View child = getChild();
            if (child != null) {
                return child.canScrollHorizontally(i2);
            }
            return false;
        } else if (i == 1) {
            View child2 = getChild();
            if (child2 != null) {
                return child2.canScrollVertically(i2);
            }
            return false;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent e) {
        Intrinsics.checkNotNullParameter(e, "e");
        handleInterceptTouchEvent(e);
        return super.onInterceptTouchEvent(e);
    }

    public final void handleInterceptTouchEvent(MotionEvent motionEvent) {
        ViewPager2 parentViewPager = getParentViewPager();
        Integer valueOf = parentViewPager == null ? null : Integer.valueOf(parentViewPager.getOrientation());
        if (valueOf == null) {
            return;
        }
        int intValue = valueOf.intValue();
        int i = this.isChildHasSameDirection ? intValue : intValue ^ 1;
        float f = 1.0f;
        if (!canChildScroll(i, -1.0f) && !canChildScroll(i, 1.0f)) {
            return;
        }
        if (motionEvent.getAction() == 0) {
            this.initialX = motionEvent.getX();
            this.initialY = motionEvent.getY();
            getParent().requestDisallowInterceptTouchEvent(true);
        } else if (motionEvent.getAction() != 2) {
        } else {
            float x = motionEvent.getX() - this.initialX;
            float y = motionEvent.getY() - this.initialY;
            boolean z = intValue == 0;
            float abs = Math.abs(x) * (z ? 0.5f : 1.0f);
            float abs2 = Math.abs(y);
            if (!z) {
                f = 0.5f;
            }
            float f2 = abs2 * f;
            int i2 = this.touchSlop;
            if (abs <= i2 && f2 <= i2) {
                return;
            }
            if (z == (abs > f2)) {
                if (!z) {
                    x = y;
                }
                if (canChildScroll(intValue, x)) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return;
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return;
                }
            }
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }
}
