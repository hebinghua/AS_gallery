package com.miui.gallery.hybrid.pulltorefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import com.miui.gallery.R;
import com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase;
import com.nexstreaming.nexeditorsdk.nexClip;

@SuppressLint({"ViewConstructor"})
/* loaded from: classes2.dex */
public class FlipLoadingLayout extends LoadingLayout {
    public final Animation mResetRotateAnimation;
    public final Animation mRotateAnimation;

    @Override // com.miui.gallery.hybrid.pulltorefresh.LoadingLayout
    public int getDefaultDrawableResId() {
        return R.drawable.hybrid_default_ptr_flip;
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.LoadingLayout
    public void onPullImpl(float f) {
    }

    public FlipLoadingLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation orientation, TypedArray typedArray) {
        super(context, mode, orientation, typedArray);
        float f = mode == PullToRefreshBase.Mode.PULL_FROM_START ? -180 : nexClip.kClip_Rotate_180;
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, f, 1, 0.5f, 1, 0.5f);
        this.mRotateAnimation = rotateAnimation;
        Interpolator interpolator = LoadingLayout.ANIMATION_INTERPOLATOR;
        rotateAnimation.setInterpolator(interpolator);
        rotateAnimation.setDuration(150L);
        rotateAnimation.setFillAfter(true);
        RotateAnimation rotateAnimation2 = new RotateAnimation(f, 0.0f, 1, 0.5f, 1, 0.5f);
        this.mResetRotateAnimation = rotateAnimation2;
        rotateAnimation2.setInterpolator(interpolator);
        rotateAnimation2.setDuration(150L);
        rotateAnimation2.setFillAfter(true);
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.LoadingLayout
    public void onLoadingDrawableSet(Drawable drawable) {
        if (drawable != null) {
            int intrinsicHeight = drawable.getIntrinsicHeight();
            int intrinsicWidth = drawable.getIntrinsicWidth();
            ViewGroup.LayoutParams layoutParams = this.mHeaderImage.getLayoutParams();
            int max = Math.max(intrinsicHeight, intrinsicWidth);
            layoutParams.height = max;
            layoutParams.width = max;
            this.mHeaderImage.requestLayout();
            this.mHeaderImage.setScaleType(ImageView.ScaleType.MATRIX);
            Matrix matrix = new Matrix();
            matrix.postTranslate((layoutParams.width - intrinsicWidth) / 2.0f, (layoutParams.height - intrinsicHeight) / 2.0f);
            matrix.postRotate(getDrawableRotationAngle(), layoutParams.width / 2.0f, layoutParams.height / 2.0f);
            this.mHeaderImage.setImageMatrix(matrix);
        }
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.LoadingLayout
    public void pullToRefreshImpl() {
        if (this.mRotateAnimation == this.mHeaderImage.getAnimation()) {
            this.mHeaderImage.startAnimation(this.mResetRotateAnimation);
        }
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.LoadingLayout
    public void refreshingImpl() {
        this.mHeaderImage.clearAnimation();
        this.mHeaderImage.setVisibility(4);
        this.mHeaderProgress.setVisibility(0);
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.LoadingLayout
    public void releaseToRefreshImpl() {
        this.mHeaderImage.startAnimation(this.mRotateAnimation);
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.LoadingLayout
    public void resetImpl() {
        this.mHeaderImage.clearAnimation();
        this.mHeaderProgress.setVisibility(8);
        this.mHeaderImage.setVisibility(0);
    }

    /* renamed from: com.miui.gallery.hybrid.pulltorefresh.FlipLoadingLayout$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode;

        static {
            int[] iArr = new int[PullToRefreshBase.Mode.values().length];
            $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode = iArr;
            try {
                iArr[PullToRefreshBase.Mode.PULL_FROM_END.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode[PullToRefreshBase.Mode.PULL_FROM_START.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    private float getDrawableRotationAngle() {
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode[this.mMode.ordinal()];
        return i != 1 ? (i == 2 && this.mScrollDirection == PullToRefreshBase.Orientation.HORIZONTAL) ? 270.0f : 0.0f : this.mScrollDirection == PullToRefreshBase.Orientation.HORIZONTAL ? 90.0f : 180.0f;
    }
}
