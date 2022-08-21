package miuix.androidbasewidget.internal.view;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

/* loaded from: classes3.dex */
public class SeekBarGradientDrawable extends GradientDrawable {
    public int mHeight;
    public SeekBarGradientState mSeekBarGradientState;
    public int mWidth;

    @Override // android.graphics.drawable.GradientDrawable, android.graphics.drawable.Drawable
    public boolean isStateful() {
        return true;
    }

    public void startPressedAnim() {
    }

    public void startUnPressedAnim() {
    }

    public SeekBarGradientDrawable() {
        this.mWidth = -1;
        this.mHeight = -1;
        SeekBarGradientState newSeekBarGradientState = newSeekBarGradientState();
        this.mSeekBarGradientState = newSeekBarGradientState;
        newSeekBarGradientState.setConstantState(super.getConstantState());
    }

    public SeekBarGradientDrawable(Resources resources, Resources.Theme theme, SeekBarGradientState seekBarGradientState) {
        Drawable newDrawable;
        this.mWidth = -1;
        this.mHeight = -1;
        if (resources == null) {
            newDrawable = seekBarGradientState.mParent.newDrawable();
        } else if (theme == null) {
            newDrawable = seekBarGradientState.mParent.newDrawable(resources);
        } else {
            newDrawable = seekBarGradientState.mParent.newDrawable(resources, theme);
        }
        seekBarGradientState.mParent = newDrawable.getConstantState();
        SeekBarGradientState newSeekBarGradientState = newSeekBarGradientState();
        this.mSeekBarGradientState = newSeekBarGradientState;
        newSeekBarGradientState.setConstantState(seekBarGradientState.mParent);
        this.mWidth = newDrawable.getIntrinsicWidth();
        this.mHeight = newDrawable.getIntrinsicHeight();
        if (newDrawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) newDrawable;
            setCornerRadius(gradientDrawable.getCornerRadius());
            setShape(gradientDrawable.getShape());
            setColor(gradientDrawable.getColor());
        }
    }

    public SeekBarGradientState newSeekBarGradientState() {
        return new SeekBarGradientState();
    }

    @Override // android.graphics.drawable.GradientDrawable, android.graphics.drawable.Drawable
    public boolean onStateChange(int[] iArr) {
        boolean onStateChange = super.onStateChange(iArr);
        boolean z = false;
        for (int i : iArr) {
            if (i == 16842919) {
                z = true;
            }
        }
        if (z) {
            startPressedAnim();
        }
        if (!z) {
            startUnPressedAnim();
        }
        return onStateChange;
    }

    @Override // android.graphics.drawable.GradientDrawable, android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.mSeekBarGradientState;
    }

    /* loaded from: classes3.dex */
    public static class SeekBarGradientState extends Drawable.ConstantState {
        public Drawable.ConstantState mParent;

        public void setConstantState(Drawable.ConstantState constantState) {
            this.mParent = constantState;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            if (this.mParent == null) {
                return null;
            }
            return newSeekBarGradientDrawable(null, null, this);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources) {
            if (this.mParent == null) {
                return null;
            }
            return newSeekBarGradientDrawable(resources, null, this);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources, Resources.Theme theme) {
            if (this.mParent == null) {
                return null;
            }
            return newSeekBarGradientDrawable(resources, theme, this);
        }

        public Drawable newSeekBarGradientDrawable(Resources resources, Resources.Theme theme, SeekBarGradientState seekBarGradientState) {
            return new SeekBarGradientDrawable(resources, theme, seekBarGradientState);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.mParent.getChangingConfigurations();
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            return this.mParent.canApplyTheme();
        }
    }

    @Override // android.graphics.drawable.GradientDrawable, android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mWidth;
    }

    @Override // android.graphics.drawable.GradientDrawable, android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mHeight;
    }
}
