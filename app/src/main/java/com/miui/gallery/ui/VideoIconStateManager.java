package com.miui.gallery.ui;

import android.view.View;
import android.view.ViewPropertyAnimator;
import com.baidu.platform.comapi.UIMsg;
import com.miui.gallery.ui.PhotoPageItem;
import com.miui.gallery.util.logger.DefaultLogger;
import miuix.view.animation.SineEaseInOutInterpolator;

/* loaded from: classes2.dex */
public class VideoIconStateManager {
    public PhotoPageVideoItem mHost;
    public int mIconHideFlag;
    public static AnimInfo NO_ANIM = new AnimInfo(false, 0);
    public static AnimInfo DEFAULT_ANIM = new AnimInfo(true, UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME);
    public static AnimInfo VIDEO_PLAYER_START_ANIM = new AnimInfo(true, 100);
    public static AnimInfo GESTURE_ICON_ANIM = new AnimInfo(true, 100);
    public boolean mHideIconWhenImmerse = false;
    public Runnable mGestureHideRunnable = new Runnable() { // from class: com.miui.gallery.ui.VideoIconStateManager$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            VideoIconStateManager.$r8$lambda$_i0OA8Trq1fZAEoJmBGS8dk3Zl0(VideoIconStateManager.this);
        }
    };

    public static /* synthetic */ void $r8$lambda$_i0OA8Trq1fZAEoJmBGS8dk3Zl0(VideoIconStateManager videoIconStateManager) {
        videoIconStateManager.lambda$new$0();
    }

    public VideoIconStateManager(PhotoPageVideoItem photoPageVideoItem) {
        this.mHost = photoPageVideoItem;
    }

    public void onSlipping(float f) {
        this.mHost.getVideoIcon().animate().cancel();
        if (this.mIconHideFlag == 0) {
            this.mHost.getVideoIcon().setAlpha(1.0f - f);
        }
    }

    public void onMatrixChanged() {
        if (!this.mHost.isSupportZoom()) {
            float[] fArr = {this.mHost.getWidth() >> 1, this.mHost.getHeight() >> 1};
            this.mHost.getPhotoView().getSuppMatrix().mapPoints(fArr);
            this.mHost.getVideoIcon().setTranslationX(fArr[0] - (this.mHost.getWidth() >> 1));
            this.mHost.getVideoIcon().setTranslationY(fArr[1] - (this.mHost.getHeight() >> 1));
        }
    }

    public /* synthetic */ void lambda$new$0() {
        setIconVisibleAlpha(false, 8, NO_ANIM);
    }

    public void onStartHandleTouchEvent() {
        if (this.mHost.isSupportZoom()) {
            this.mHost.getPhotoView().postDelayed(this.mGestureHideRunnable, 100L);
        }
    }

    public void onStopHandleTouchEvent() {
        if (this.mHost.isSupportZoom()) {
            this.mHost.getPhotoView().removeCallbacks(this.mGestureHideRunnable);
            setIconVisibleAlpha(true, 8, GESTURE_ICON_ANIM);
        }
    }

    public void onStartCheck() {
        setIconVisibleAlpha(false, 2, NO_ANIM);
    }

    public void onEndCheck() {
        setIconVisibleAlpha(true, 2, NO_ANIM);
    }

    public void onActionBarVisibleChanged() {
        updateImmerseState(DEFAULT_ANIM);
    }

    public void onPreviewStart() {
        setIconVisibleAlpha(false, 4, NO_ANIM);
    }

    public void onPreviewStop() {
        setIconVisibleAlpha(true, 4, NO_ANIM);
    }

    public void onPlayerStarted() {
        setHideIconWhenImmerse();
        updateImmerseState(NO_ANIM);
        setIconVisibleAlpha(false, 32, VIDEO_PLAYER_START_ANIM);
    }

    public void onPlayerReturn() {
        updateImmerseState(NO_ANIM);
        onOnlinePlayerReturn();
        setIconVisibleAlpha(true, 32, NO_ANIM);
    }

    public void onPreparePlayOnline() {
        setIconVisibleAlpha(false, 16, DEFAULT_ANIM);
    }

    public void onOnlinePlayerReturn() {
        setIconVisibleAlpha(true, 16, NO_ANIM);
    }

    public void onProgressChanged() {
        setHideIconWhenImmerse();
    }

    public void resetIconHideState() {
        this.mIconHideFlag = 0;
        this.mHideIconWhenImmerse = false;
        this.mHost.getVideoIcon().animate().cancel();
        PhotoPageItem.CheckManager checkManager = this.mHost.mCheckManager;
        if (checkManager == null || !checkManager.inAction()) {
            this.mHost.getVideoIcon().setClickable(true);
            this.mHost.getVideoIcon().setAlpha(1.0f);
            return;
        }
        this.mIconHideFlag |= 2;
    }

    public final void setHideIconWhenImmerse() {
        if (this.mHost.mPreviewManager == null || this.mHideIconWhenImmerse) {
            return;
        }
        DefaultLogger.d("VideoIconStateManager", "setHideIconWhenImmerse");
        this.mHideIconWhenImmerse = true;
    }

    public final void updateImmerseState(AnimInfo animInfo) {
        PhotoPageVideoItem photoPageVideoItem = this.mHost;
        if (photoPageVideoItem.mPreviewManager != null && this.mHideIconWhenImmerse) {
            setIconVisibleAlpha(photoPageVideoItem.isActionBarVisible(), 1, animInfo);
        }
    }

    public final void setIconVisibleAlpha(boolean z, int i, AnimInfo animInfo) {
        DefaultLogger.d("VideoIconStateManager", "setIconVisibleAlpha: visible %b, withAnim %b, flag %d", Boolean.valueOf(z), Integer.valueOf(animInfo.duration), Integer.valueOf(i));
        int i2 = this.mIconHideFlag;
        float f = 0.0f;
        if (!z) {
            this.mIconHideFlag = i | i2;
            if (i2 > 0) {
                if (animInfo.withAnim) {
                    return;
                }
                this.mHost.getVideoIcon().animate().cancel();
                this.mHost.getVideoIcon().setAlpha(0.0f);
                this.mHost.getVideoIcon().setClickable(false);
                return;
            }
        } else if (i2 == 0) {
            if (animInfo.withAnim) {
                return;
            }
            this.mHost.getVideoIcon().animate().cancel();
            this.mHost.getVideoIcon().setAlpha(1.0f);
            this.mHost.getVideoIcon().setClickable(true);
            return;
        } else {
            int i3 = (~i) & i2;
            this.mIconHideFlag = i3;
            if (i3 > 0) {
                return;
            }
        }
        this.mHost.getVideoIcon().setClickable(z);
        this.mHost.getVideoIcon().animate().cancel();
        float alpha = this.mHost.getVideoIcon().getAlpha();
        if (alpha != 1.0f || !z) {
            if (alpha == 0.0f && !z) {
                return;
            }
            if (!animInfo.withAnim) {
                View videoIcon = this.mHost.getVideoIcon();
                if (z) {
                    f = 1.0f;
                }
                videoIcon.setAlpha(f);
                return;
            }
            ViewPropertyAnimator animate = this.mHost.getVideoIcon().animate();
            if (z) {
                f = 1.0f;
            }
            animate.alpha(f).setDuration(animInfo.duration).setInterpolator(new SineEaseInOutInterpolator()).start();
        }
    }

    /* loaded from: classes2.dex */
    public static class AnimInfo {
        public int duration;
        public boolean withAnim;

        public AnimInfo(boolean z, int i) {
            this.withAnim = z;
            this.duration = i;
        }
    }
}
