package com.miui.gallery.widget.recyclerview;

import android.content.Context;

/* loaded from: classes3.dex */
public abstract class FastScrollerCapsule implements DrawView {
    public int mCapsuleMarginToThumb;
    public Context mContext;

    /* loaded from: classes3.dex */
    public interface OnAnimatorListener {
        void onTimeCapsuleAnimatorFadeInEnd();

        void onTimeCapsuleAnimatorFadeInStart();

        void onTimeCapsuleAnimatorFadeOutEnd();

        void onTimeCapsuleAnimatorFadeOutStart();

        void onTimeCapsuleAnimatorUpdate();
    }

    /* loaded from: classes3.dex */
    public interface OnLocationChangedListener {
        void onTimeCapsuleHideLocation();

        void onTimeCapsuleShowLocation();
    }

    public abstract void cancelHideCapsule();

    public abstract void cancelShowCapsule();

    public abstract boolean getIsShowLocation();

    public abstract void hideCapsule();

    public abstract void hideCapsuleByAnimator(long j);

    public abstract void hideLocationByAnimation();

    public abstract void setContent(FastScrollerCapsuleContentProvider fastScrollerCapsuleContentProvider);

    public abstract void setIsInRight(boolean z);

    public abstract void setIsShowLocation(boolean z);

    public abstract void setOnAnimatorListener(OnAnimatorListener onAnimatorListener);

    public abstract void setOnLocationChangedListener(OnLocationChangedListener onLocationChangedListener);

    public abstract void showCapsuleByAnimator();

    public abstract void showLocationByAnimation();

    public FastScrollerCapsule(Context context) {
        this.mContext = context;
    }

    public int getCapsuleMarginToThumb() {
        return this.mCapsuleMarginToThumb;
    }
}
