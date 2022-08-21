package miuix.internal.view;

import android.content.res.Resources;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.util.Log;

/* loaded from: classes3.dex */
public class CheckWidgetAnimatedStateListDrawable extends AnimatedStateListDrawable {
    public static final String TAG = CheckWidgetAnimatedStateListDrawable.class.getName();
    public CheckWidgetConstantState mCheckWidgetConstantState;

    @Override // android.graphics.drawable.DrawableContainer, android.graphics.drawable.Drawable
    public boolean canApplyTheme() {
        return true;
    }

    public CheckWidgetAnimatedStateListDrawable() {
        this.mCheckWidgetConstantState = newCheckWidgetConstantState();
    }

    public CheckWidgetConstantState newCheckWidgetConstantState() {
        return new CheckWidgetConstantState();
    }

    public CheckWidgetAnimatedStateListDrawable(Resources resources, Resources.Theme theme, CheckWidgetConstantState checkWidgetConstantState) {
        Drawable newDrawable;
        if (checkWidgetConstantState != null) {
            if (resources == null) {
                newDrawable = checkWidgetConstantState.mParent.newDrawable();
            } else if (theme == null) {
                newDrawable = checkWidgetConstantState.mParent.newDrawable(resources);
            } else {
                newDrawable = checkWidgetConstantState.mParent.newDrawable(resources, theme);
            }
            if (newDrawable != null) {
                checkWidgetConstantState.mParent = newDrawable.getConstantState();
            }
            setConstantState((DrawableContainer.DrawableContainerState) checkWidgetConstantState.mParent);
            onStateChange(getState());
            jumpToCurrentState();
            CheckWidgetConstantState checkWidgetConstantState2 = this.mCheckWidgetConstantState;
            checkWidgetConstantState2.grayColor = checkWidgetConstantState.grayColor;
            checkWidgetConstantState2.blackColor = checkWidgetConstantState.blackColor;
            checkWidgetConstantState2.backGroundColor = checkWidgetConstantState.backGroundColor;
            checkWidgetConstantState2.touchAnimEnable = checkWidgetConstantState.touchAnimEnable;
            return;
        }
        Log.e(TAG, "checkWidgetConstantState is null ,but it can't be null", null);
    }

    @Override // android.graphics.drawable.AnimatedStateListDrawable, android.graphics.drawable.StateListDrawable, android.graphics.drawable.DrawableContainer
    public void setConstantState(DrawableContainer.DrawableContainerState drawableContainerState) {
        super.setConstantState(drawableContainerState);
        if (this.mCheckWidgetConstantState == null) {
            this.mCheckWidgetConstantState = newCheckWidgetConstantState();
        }
        this.mCheckWidgetConstantState.mParent = drawableContainerState;
    }

    @Override // android.graphics.drawable.DrawableContainer, android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.mCheckWidgetConstantState;
    }

    /* loaded from: classes3.dex */
    public static class CheckWidgetConstantState extends Drawable.ConstantState {
        public int backGroundColor;
        public int backgroundDisableAlpha;
        public int backgroundNormalAlpha;
        public int blackColor;
        public int grayColor;
        public Drawable.ConstantState mParent;
        public int strokeColor;
        public int strokeDisableAlpha;
        public int strokeNormalAlpha;
        public boolean touchAnimEnable;

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            if (this.mParent == null) {
                return null;
            }
            return newAnimatedStateListDrawable(null, null, this);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources) {
            if (this.mParent == null) {
                return null;
            }
            return newAnimatedStateListDrawable(resources, null, this);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources, Resources.Theme theme) {
            if (this.mParent == null) {
                return null;
            }
            return newAnimatedStateListDrawable(resources, theme, this);
        }

        public Drawable newAnimatedStateListDrawable(Resources resources, Resources.Theme theme, CheckWidgetConstantState checkWidgetConstantState) {
            return new CheckWidgetAnimatedStateListDrawable(resources, theme, checkWidgetConstantState);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            Drawable.ConstantState constantState = this.mParent;
            if (constantState == null) {
                return -1;
            }
            return constantState.getChangingConfigurations();
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            Drawable.ConstantState constantState = this.mParent;
            if (constantState == null) {
                return false;
            }
            return constantState.canApplyTheme();
        }
    }
}
