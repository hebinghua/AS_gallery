package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EdgeEffect;
import androidx.annotation.Keep;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.recyclerview.R$attr;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RemixRecyclerView;
import java.lang.reflect.Field;
import miuix.os.Build;
import miuix.spring.view.SpringHelper;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* loaded from: classes.dex */
public abstract class SpringRecyclerView extends RemixRecyclerView {
    public static final Field NESTED_SCROLL_HELPER;
    public static final RecyclerView.EdgeEffectFactory NON_EFFECT_FACTORY;
    public static final Field VIEW_FLINGER;
    public boolean mHorizontalOverScrolling;
    public int mManagedScrollState;
    public SpringFlinger mSpringFlinger;
    public SpringHelper mSpringHelper;
    public SpringNestedScrollingHelper mSpringNestedScrollingHelper;
    public boolean mVerticalOverScrolling;

    @Override // androidx.recyclerview.widget.RemixRecyclerView
    public /* bridge */ /* synthetic */ boolean getSpringEnabled() {
        return super.getSpringEnabled();
    }

    @Override // androidx.recyclerview.widget.RemixRecyclerView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
    public /* bridge */ /* synthetic */ boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // androidx.recyclerview.widget.RemixRecyclerView, androidx.recyclerview.widget.RecyclerView, android.view.View
    public /* bridge */ /* synthetic */ boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    @Override // androidx.recyclerview.widget.RemixRecyclerView, android.view.View
    public /* bridge */ /* synthetic */ void setOverScrollMode(int i) {
        super.setOverScrollMode(i);
    }

    @Override // androidx.recyclerview.widget.RemixRecyclerView
    public /* bridge */ /* synthetic */ void setSpringEnabled(boolean z) {
        super.setSpringEnabled(z);
    }

    public SpringRecyclerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.recyclerViewStyle);
    }

    public SpringRecyclerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mManagedScrollState = 0;
        this.mSpringHelper = new SpringHelper() { // from class: androidx.recyclerview.widget.SpringRecyclerView.1
            @Override // miuix.spring.view.SpringHelper
            public boolean canScrollHorizontally() {
                RecyclerView.LayoutManager layoutManager = SpringRecyclerView.this.mLayout;
                return layoutManager != null && layoutManager.canScrollHorizontally();
            }

            @Override // miuix.spring.view.SpringHelper
            public boolean canScrollVertically() {
                RecyclerView.LayoutManager layoutManager = SpringRecyclerView.this.mLayout;
                return layoutManager != null && layoutManager.canScrollVertically();
            }

            @Override // miuix.spring.view.SpringHelper
            public int getHeight() {
                return SpringRecyclerView.this.getHeight();
            }

            @Override // miuix.spring.view.SpringHelper
            public int getWidth() {
                return SpringRecyclerView.this.getWidth();
            }

            @Override // miuix.spring.view.SpringHelper
            public boolean springAvailable() {
                return SpringRecyclerView.this.springAvailable();
            }

            @Override // miuix.spring.view.SpringHelper
            @Keep
            public void vibrate() {
                HapticCompat.performHapticFeedback(SpringRecyclerView.this, HapticFeedbackConstants.MIUI_SCROLL_EDGE);
            }

            @Override // miuix.spring.view.SpringHelper
            public boolean dispatchNestedPreScroll(int i2, int i3, int[] iArr, int[] iArr2, int i4) {
                if (SpringRecyclerView.this.mHorizontalOverScrolling && getHorizontalDistance() == 0) {
                    SpringRecyclerView.this.mHorizontalOverScrolling = false;
                }
                if (SpringRecyclerView.this.mVerticalOverScrolling && getVerticalDistance() == 0) {
                    SpringRecyclerView.this.mVerticalOverScrolling = false;
                }
                return SpringRecyclerView.this.mSpringNestedScrollingHelper.super_dispatchNestedPreScroll(i2, i3, iArr, iArr2, i4);
            }

            @Override // miuix.spring.view.SpringHelper
            public void dispatchNestedScroll(int i2, int i3, int i4, int i5, int[] iArr, int i6, int[] iArr2) {
                SpringRecyclerView.this.mSpringNestedScrollingHelper.super_dispatchNestedScroll(i2, i3, i4, i5, iArr, i6, iArr2);
                if (!springAvailable() || SpringRecyclerView.this.mManagedScrollState != 2) {
                    return;
                }
                if (!SpringRecyclerView.this.mHorizontalOverScrolling && canScrollHorizontally() && i4 != 0) {
                    SpringRecyclerView.this.mSpringFlinger.notifyHorizontalEdgeReached(i4);
                }
                if (SpringRecyclerView.this.mVerticalOverScrolling || !canScrollVertically() || i5 == 0) {
                    return;
                }
                SpringRecyclerView.this.mSpringFlinger.notifyVerticalEdgeReached(i5);
            }
        };
        this.mSpringFlinger = new SpringFlinger();
        this.mSpringNestedScrollingHelper = new SpringNestedScrollingHelper(this);
        replaceViewFlinger(this.mSpringFlinger);
        replaceNestedScrollingHelper(this.mSpringNestedScrollingHelper);
        super.setEdgeEffectFactory(NON_EFFECT_FACTORY);
        if (Build.IS_INTERNATIONAL_BUILD) {
            setSpringEnabled(false);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public void draw(Canvas canvas) {
        int horizontalDistance = this.mSpringHelper.getHorizontalDistance();
        int verticalDistance = this.mSpringHelper.getVerticalDistance();
        if (horizontalDistance != 0 || verticalDistance != 0) {
            int save = canvas.save();
            canvas.translate(-horizontalDistance, -verticalDistance);
            super.draw(canvas);
            canvas.restoreToCount(save);
            return;
        }
        super.draw(canvas);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void setScrollState(int i) {
        if (this.mManagedScrollState == 1 && i == 0) {
            int horizontalDistance = this.mSpringHelper.getHorizontalDistance();
            int verticalDistance = this.mSpringHelper.getVerticalDistance();
            if (horizontalDistance != 0 || verticalDistance != 0) {
                this.mSpringFlinger.springBack(horizontalDistance, verticalDistance);
                return;
            }
        }
        super.setScrollState(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void onScrollStateChanged(int i) {
        super.onScrollStateChanged(i);
        this.mManagedScrollState = i;
        if (springAvailable() && i != 2) {
            if (!this.mHorizontalOverScrolling && !this.mVerticalOverScrolling) {
                return;
            }
            this.mSpringFlinger.stop();
            this.mHorizontalOverScrolling = false;
            this.mVerticalOverScrolling = false;
        }
    }

    public final boolean springAvailable() {
        return getOverScrollMode() != 2 && getSpringEnabled();
    }

    /* loaded from: classes.dex */
    public class SpringFlinger extends RemixRecyclerView.ViewFlinger {
        public SpringFlinger() {
            super();
        }

        @Override // androidx.recyclerview.widget.RemixRecyclerView.ViewFlinger, androidx.recyclerview.widget.RecyclerView.ViewFlinger
        public void fling(int i, int i2) {
            int horizontalDistance = SpringRecyclerView.this.mSpringHelper.getHorizontalDistance();
            int verticalDistance = SpringRecyclerView.this.mSpringHelper.getVerticalDistance();
            if (SpringRecyclerView.this.springAvailable() && (horizontalDistance != 0 || verticalDistance != 0)) {
                overFling(i, i2, horizontalDistance, verticalDistance);
            } else {
                super.fling(i, i2);
            }
        }

        public void springBack(int i, int i2) {
            if (i != 0) {
                SpringRecyclerView.this.mHorizontalOverScrolling = true;
            }
            if (i2 != 0) {
                SpringRecyclerView.this.mVerticalOverScrolling = true;
            }
            SpringRecyclerView.this.setScrollState(2);
            resetFlingPosition();
            int i3 = -i;
            int i4 = -i2;
            ((RemixRecyclerView.ViewFlinger) this).mOverScroller.springBack(0, 0, i3, i3, i4, i4);
            postOnAnimation();
        }

        public void overFling(int i, int i2, int i3, int i4) {
            int i5;
            int i6;
            int i7;
            int i8;
            boolean z = true;
            SpringRecyclerView.this.mHorizontalOverScrolling = i3 != 0;
            SpringRecyclerView springRecyclerView = SpringRecyclerView.this;
            if (i4 == 0) {
                z = false;
            }
            springRecyclerView.mVerticalOverScrolling = z;
            SpringRecyclerView.this.setScrollState(2);
            resetFlingPosition();
            int i9 = Integer.MIN_VALUE;
            int i10 = Integer.MAX_VALUE;
            if (Integer.signum(i) * i3 > 0) {
                i5 = -i3;
                i6 = i5;
            } else if (i < 0) {
                i6 = -i3;
                i5 = Integer.MIN_VALUE;
            } else {
                i5 = -i3;
                i6 = Integer.MAX_VALUE;
            }
            if (Integer.signum(i2) * i4 > 0) {
                i7 = -i4;
                i8 = i7;
            } else {
                if (i2 < 0) {
                    i10 = -i4;
                } else {
                    i9 = -i4;
                }
                i7 = i9;
                i8 = i10;
            }
            ((RemixRecyclerView.ViewFlinger) this).mOverScroller.fling(0, 0, i, i2, i5, i6, i7, i8, SpringRecyclerView.this.getWidth(), SpringRecyclerView.this.getHeight());
            postOnAnimation();
        }

        public void notifyVerticalEdgeReached(int i) {
            SpringRecyclerView.this.mVerticalOverScrolling = true;
            SpringRecyclerView.this.setScrollState(2);
            resetFlingPosition();
            ((RemixRecyclerView.ViewFlinger) this).mOverScroller.notifyVerticalEdgeReached(0, -i, SpringRecyclerView.this.getHeight());
        }

        public void notifyHorizontalEdgeReached(int i) {
            SpringRecyclerView.this.mHorizontalOverScrolling = true;
            SpringRecyclerView.this.setScrollState(2);
            resetFlingPosition();
            ((RemixRecyclerView.ViewFlinger) this).mOverScroller.notifyHorizontalEdgeReached(0, -i, SpringRecyclerView.this.getWidth());
        }
    }

    /* loaded from: classes.dex */
    public class SpringNestedScrollingHelper extends NestedScrollingChildHelper {
        public SpringNestedScrollingHelper(View view) {
            super(view);
        }

        @Override // androidx.core.view.NestedScrollingChildHelper
        public boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2, int i3) {
            return SpringRecyclerView.this.mSpringHelper.handleNestedPreScroll(i, i2, iArr, iArr2, i3);
        }

        @Override // androidx.core.view.NestedScrollingChildHelper
        public void dispatchNestedScroll(int i, int i2, int i3, int i4, int[] iArr, int i5, int[] iArr2) {
            SpringRecyclerView.this.mSpringHelper.handleNestedScroll(i, i2, i3, i4, iArr, i5, iArr2);
        }

        public boolean super_dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2, int i3) {
            if (SpringRecyclerView.this.mHorizontalOverScrolling || SpringRecyclerView.this.mVerticalOverScrolling) {
                return false;
            }
            if (i != 0 || i2 != 0) {
                return super.dispatchNestedPreScroll(i, i2, iArr, iArr2, i3);
            }
            return false;
        }

        public void super_dispatchNestedScroll(int i, int i2, int i3, int i4, int[] iArr, int i5, int[] iArr2) {
            if (SpringRecyclerView.this.mHorizontalOverScrolling || SpringRecyclerView.this.mVerticalOverScrolling) {
                return;
            }
            super.dispatchNestedScroll(i, i2, i3, i4, iArr, i5, iArr2);
        }
    }

    static {
        try {
            Field declaredField = RecyclerView.class.getDeclaredField("mViewFlinger");
            VIEW_FLINGER = declaredField;
            declaredField.setAccessible(true);
            try {
                Field declaredField2 = RecyclerView.class.getDeclaredField("mScrollingChildHelper");
                NESTED_SCROLL_HELPER = declaredField2;
                declaredField2.setAccessible(true);
                NON_EFFECT_FACTORY = new NonEdgeEffectFactory();
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        } catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2);
        }
    }

    public final void replaceNestedScrollingHelper(NestedScrollingChildHelper nestedScrollingChildHelper) {
        try {
            NESTED_SCROLL_HELPER.set(this, nestedScrollingChildHelper);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public final void replaceViewFlinger(RemixRecyclerView.ViewFlinger viewFlinger) {
        try {
            VIEW_FLINGER.set(this, viewFlinger);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /* loaded from: classes.dex */
    public static class NonEdgeEffectFactory extends RecyclerView.EdgeEffectFactory {
        public NonEdgeEffectFactory() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory
        public EdgeEffect createEdgeEffect(RecyclerView recyclerView, int i) {
            return new NonEdgeEffect(recyclerView.getContext());
        }
    }

    /* loaded from: classes.dex */
    public static class NonEdgeEffect extends EdgeEffect {
        @Override // android.widget.EdgeEffect
        public boolean draw(Canvas canvas) {
            return false;
        }

        @Override // android.widget.EdgeEffect
        public void finish() {
        }

        @Override // android.widget.EdgeEffect
        public BlendMode getBlendMode() {
            return null;
        }

        @Override // android.widget.EdgeEffect
        public int getColor() {
            return 0;
        }

        @Override // android.widget.EdgeEffect
        public int getMaxHeight() {
            return 0;
        }

        @Override // android.widget.EdgeEffect
        public boolean isFinished() {
            return true;
        }

        @Override // android.widget.EdgeEffect
        public void onAbsorb(int i) {
        }

        @Override // android.widget.EdgeEffect
        public void onPull(float f) {
        }

        @Override // android.widget.EdgeEffect
        public void onPull(float f, float f2) {
        }

        @Override // android.widget.EdgeEffect
        public void onRelease() {
        }

        @Override // android.widget.EdgeEffect
        public void setBlendMode(BlendMode blendMode) {
        }

        @Override // android.widget.EdgeEffect
        public void setColor(int i) {
        }

        @Override // android.widget.EdgeEffect
        public void setSize(int i, int i2) {
        }

        public NonEdgeEffect(Context context) {
            super(context);
        }
    }
}
