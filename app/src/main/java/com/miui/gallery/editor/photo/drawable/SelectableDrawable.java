package com.miui.gallery.editor.photo.drawable;

import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.StateSet;

/* loaded from: classes2.dex */
public class SelectableDrawable extends Drawable {
    public State mAnimateState;
    public Drawable.Callback mChildrenCallback;
    public long mDuration;
    public int mFromAlpha;
    public TimeInterpolator mInterpolator;
    public boolean mMutated;
    public boolean mSelected;
    public long mStart;
    public ChildrenState mState;
    public int mToAlpha;
    public static final int[] STATE_SELECTED = {16842913};
    public static final int[] STATE_ACTIVATED = {16843518};

    /* loaded from: classes2.dex */
    public enum State {
        IDLE,
        PREPARE,
        RUNNING
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return true;
    }

    public SelectableDrawable(Drawable drawable, Drawable drawable2) {
        this(new ChildrenState(drawable, drawable2));
    }

    public SelectableDrawable(ChildrenState childrenState) {
        this.mDuration = 200L;
        this.mChildrenCallback = new Drawable.Callback() { // from class: com.miui.gallery.editor.photo.drawable.SelectableDrawable.1
            @Override // android.graphics.drawable.Drawable.Callback
            public void invalidateDrawable(Drawable drawable) {
                if (SelectableDrawable.this.getCallback() != null) {
                    if (drawable != SelectableDrawable.this.mState.mNormal && drawable != SelectableDrawable.this.mState.mSelect) {
                        return;
                    }
                    SelectableDrawable.this.getCallback().invalidateDrawable(SelectableDrawable.this);
                }
            }

            @Override // android.graphics.drawable.Drawable.Callback
            public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
                if (SelectableDrawable.this.getCallback() != null) {
                    if (drawable != SelectableDrawable.this.mState.mNormal && drawable != SelectableDrawable.this.mState.mSelect) {
                        return;
                    }
                    SelectableDrawable.this.getCallback().scheduleDrawable(SelectableDrawable.this, runnable, j);
                }
            }

            @Override // android.graphics.drawable.Drawable.Callback
            public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
                if (SelectableDrawable.this.getCallback() != null) {
                    if (drawable != SelectableDrawable.this.mState.mNormal && drawable != SelectableDrawable.this.mState.mSelect) {
                        return;
                    }
                    SelectableDrawable.this.getCallback().unscheduleDrawable(SelectableDrawable.this, runnable);
                }
            }
        };
        this.mState = childrenState;
        childrenState.mNormal.setCallback(this.mChildrenCallback);
        this.mState.mSelect.setCallback(this.mChildrenCallback);
    }

    public void setDuration(long j) {
        this.mDuration = j;
    }

    public void setInterpolator(TimeInterpolator timeInterpolator) {
        this.mInterpolator = timeInterpolator;
    }

    @Override // android.graphics.drawable.Drawable
    public void setChangingConfigurations(int i) {
        this.mState.mChangingConfigurations = i;
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        return this.mState.getChangingConfigurations();
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean z) {
        this.mState.mSelect.setDither(z);
        this.mState.mNormal.setDither(z);
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean z) {
        this.mState.mSelect.setFilterBitmap(z);
        this.mState.mNormal.setFilterBitmap(z);
    }

    @Override // android.graphics.drawable.Drawable
    public void clearColorFilter() {
        this.mState.mNormal.clearColorFilter();
        this.mState.mSelect.clearColorFilter();
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return getCurrent().getAlpha();
    }

    @Override // android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        super.jumpToCurrentState();
        State state = this.mAnimateState;
        State state2 = State.IDLE;
        if (state != state2) {
            this.mAnimateState = state2;
        }
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onStateChange(int[] iArr) {
        boolean z = StateSet.stateSetMatches(STATE_SELECTED, iArr) || StateSet.stateSetMatches(STATE_ACTIVATED, iArr);
        if (this.mSelected == z) {
            return false;
        }
        this.mSelected = z;
        State state = this.mAnimateState;
        State state2 = State.PREPARE;
        if (state == state2) {
            if ((z && this.mToAlpha == 0) || (!z && this.mFromAlpha == 0)) {
                this.mAnimateState = State.IDLE;
            }
            return false;
        }
        if (z) {
            this.mFromAlpha = 0;
            this.mToAlpha = this.mState.mSelect.getAlpha();
        } else {
            this.mFromAlpha = this.mState.mSelect.getAlpha();
            this.mToAlpha = 0;
        }
        this.mAnimateState = state2;
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onLevelChange(int i) {
        return this.mState.mSelect.setLevel(i) || this.mState.mNormal.setLevel(i);
    }

    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect rect) {
        this.mState.mNormal.setBounds(rect);
        this.mState.mSelect.setBounds(rect);
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable getCurrent() {
        return this.mSelected ? this.mState.mSelect : this.mState.mNormal;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean z, boolean z2) {
        return this.mState.mSelect.setVisible(z, z2) || this.mState.mNormal.setVisible(z, z2);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAutoMirrored(boolean z) {
        this.mState.mNormal.setAutoMirrored(z);
        this.mState.mSelect.setAutoMirrored(z);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isAutoMirrored() {
        return getCurrent().isAutoMirrored();
    }

    @Override // android.graphics.drawable.Drawable
    public Region getTransparentRegion() {
        return getCurrent().getTransparentRegion();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return getCurrent().getIntrinsicWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return getCurrent().getIntrinsicHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumWidth() {
        return getCurrent().getMinimumWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumHeight() {
        return getCurrent().getMinimumHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        return getCurrent().getPadding(rect);
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        if (!this.mMutated) {
            this.mState = this.mState.mutate();
            this.mMutated = true;
        }
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.mState;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        State state = this.mAnimateState;
        State state2 = State.IDLE;
        if (state == state2) {
            getCurrent().draw(canvas);
            return;
        }
        this.mState.mNormal.draw(canvas);
        long uptimeMillis = SystemClock.uptimeMillis();
        if (this.mAnimateState == State.PREPARE) {
            this.mStart = uptimeMillis;
            this.mAnimateState = State.RUNNING;
        }
        int alpha = this.mState.mSelect.getAlpha();
        float f = ((float) (uptimeMillis - this.mStart)) / ((float) this.mDuration);
        this.mState.mSelect.setAlpha(calculateAlpha(f, this.mFromAlpha, this.mToAlpha, this.mInterpolator));
        this.mState.mSelect.draw(canvas);
        this.mState.mSelect.setAlpha(alpha);
        if (f < 1.0f) {
            invalidateSelf();
        } else {
            this.mAnimateState = state2;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.mState.mNormal.setAlpha(i);
        this.mState.mSelect.setAlpha(i);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mState.mNormal.setColorFilter(colorFilter);
        this.mState.mSelect.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return getCurrent().getOpacity();
    }

    public static int calculateAlpha(float f, int i, int i2, TimeInterpolator timeInterpolator) {
        if (f > 1.0f) {
            f = 1.0f;
        }
        if (timeInterpolator != null) {
            f = timeInterpolator.getInterpolation(f);
        }
        return (int) (i + ((i2 - i) * f));
    }

    /* loaded from: classes2.dex */
    public static class ChildrenState extends Drawable.ConstantState {
        public int mChangingConfigurations;
        public Drawable mNormal;
        public Drawable mSelect;

        public ChildrenState(Drawable drawable, Drawable drawable2) {
            this.mNormal = drawable;
            this.mSelect = drawable2;
        }

        public ChildrenState(ChildrenState childrenState) {
            this.mNormal = childrenState.mNormal;
            this.mSelect = childrenState.mSelect;
            this.mChangingConfigurations = childrenState.mChangingConfigurations;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new SelectableDrawable(this);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.mChangingConfigurations | this.mNormal.getChangingConfigurations() | this.mSelect.getChangingConfigurations();
        }

        public ChildrenState mutate() {
            ChildrenState childrenState = new ChildrenState(this);
            childrenState.mNormal.mutate();
            childrenState.mSelect.mutate();
            return childrenState;
        }
    }
}
