package com.miui.gallery.card.ui.detail;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewTreeLifecycleOwner;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miui.gallery.BaseConfig$ScreenConfig;
import com.miui.gallery.R;
import com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager;
import com.miui.gallery.card.model.BaseMedia;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.glide.BindImageHelper;
import com.miui.gallery.vlog.VlogEntranceUtils;
import com.miui.gallery.widget.ICardView;
import com.miui.gallery.widget.SlideShowView;
import java.util.List;

/* loaded from: classes.dex */
public class SlideShowHeaderView extends RelativeLayout implements ICardView, DefaultLifecycleObserver {
    public boolean mIsPlayEnable;
    public boolean mIsShowVideo;
    public int mOrigHeight;
    public int mOrigWidth;
    public final View mPlayIcon;
    public ObjectAnimator mPlayIconAlphaAnim;
    public final SlideShowController mSlideShowController;
    public final SlideShowView mSlideShowView;
    public final ImageView mVideoLogo;

    @Override // com.miui.gallery.widget.ICardView
    public /* bridge */ /* synthetic */ String getCurrentLocalPath() {
        return super.getCurrentLocalPath();
    }

    public SlideShowHeaderView(Context context) {
        this(context, null);
    }

    public SlideShowHeaderView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SlideShowHeaderView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        LayoutInflater.from(context).inflate(R.layout.slide_show_header, this);
        SlideShowView slideShowView = (SlideShowView) findViewById(R.id.slide);
        this.mSlideShowView = slideShowView;
        slideShowView.setSlideDuration(3500);
        this.mSlideShowController = new SlideShowController(slideShowView, 3500);
        View findViewById = findViewById(R.id.play_icon);
        this.mPlayIcon = findViewById;
        findViewById.setContentDescription(getResources().getString(R.string.talkback_story_album_playicon));
        FolmeUtil.setCustomTouchAnim(findViewById, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, true);
        this.mVideoLogo = (ImageView) findViewById(R.id.video_logo);
    }

    public void setIsEnter(boolean z) {
        SlideShowView slideShowView = this.mSlideShowView;
        if (slideShowView != null) {
            slideShowView.setIsEnter(z);
        }
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        super.onLayout(z, i, i2, i3, i4);
        if (this.mOrigHeight == 0) {
            this.mOrigHeight = getMeasuredHeight();
            this.mOrigWidth = getMeasuredWidth();
        }
        int i6 = this.mOrigHeight;
        if (i6 == 0 || (i5 = this.mOrigWidth) == 0) {
            return;
        }
        this.mSlideShowView.scaleBitmap(i, i2, i3, i4, i6, i5);
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mPlayIcon.setOnClickListener(onClickListener);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        LifecycleOwner lifecycleOwner = ViewTreeLifecycleOwner.get(this);
        if (lifecycleOwner != null) {
            lifecycleOwner.getLifecycle().addObserver(this);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LifecycleOwner lifecycleOwner = ViewTreeLifecycleOwner.get(this);
        if (lifecycleOwner != null) {
            lifecycleOwner.getLifecycle().removeObserver(this);
        }
        destroySliderShow();
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onStart(LifecycleOwner lifecycleOwner) {
        startSliderShow();
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onResume(LifecycleOwner lifecycleOwner) {
        setSlideShowViewCanInvalidate(true);
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onPause(LifecycleOwner lifecycleOwner) {
        setSlideShowViewCanInvalidate(false);
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onStop(LifecycleOwner lifecycleOwner) {
        stopSliderShow();
    }

    public void startSliderShow() {
        this.mSlideShowController.resume();
    }

    public void stopSliderShow() {
        this.mSlideShowController.stop();
    }

    public void destroySliderShow() {
        this.mSlideShowController.destroy();
    }

    public void setSlideShowViewCanInvalidate(boolean z) {
        SlideShowView slideShowView = this.mSlideShowView;
        if (slideShowView != null) {
            slideShowView.setCanInvalidate(z);
        }
    }

    public void updateMedias(List<? extends BaseMedia> list) {
        this.mSlideShowController.updateMedias(list, true);
    }

    @Override // com.miui.gallery.widget.ICardView
    public int getCurrentIndex() {
        SlideShowController slideShowController = this.mSlideShowController;
        if (slideShowController == null) {
            return 0;
        }
        return slideShowController.getShowIndex();
    }

    @Override // com.miui.gallery.widget.ICardView
    public void setLoadIndex(int i) {
        this.mSlideShowController.setLoadIndex(i);
    }

    public View getPlayIcon() {
        return this.mPlayIcon;
    }

    public boolean isPlayEnable() {
        return this.mIsPlayEnable;
    }

    public void setPlayVisible(boolean z) {
        this.mPlayIcon.setVisibility(z ? 0 : 8);
    }

    public void setPlayEnable(boolean z) {
        if (this.mPlayIcon.getVisibility() == 8) {
            this.mIsPlayEnable = false;
            return;
        }
        this.mIsPlayEnable = z;
        if (z && this.mPlayIcon.getAlpha() != 1.0f) {
            startPlayIconAlphaAnim(true);
        } else if (z || this.mPlayIcon.getAlpha() == 0.0f) {
        } else {
            startPlayIconAlphaAnim(false);
        }
    }

    public void setIsShowVideo(boolean z) {
        boolean z2 = z && VlogEntranceUtils.isAvailable() && AnalyticFaceAndSceneManager.isDeviceSupportVideo();
        this.mIsShowVideo = z2;
        if (z2) {
            this.mVideoLogo.setVisibility(0);
        } else {
            this.mVideoLogo.setVisibility(8);
        }
    }

    public boolean isShowVideo() {
        return this.mIsShowVideo;
    }

    public void setVideoLogo(int i) {
        this.mVideoLogo.setImageResource(i);
    }

    public final void startPlayIconAlphaAnim(boolean z) {
        ObjectAnimator objectAnimator = this.mPlayIconAlphaAnim;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            this.mPlayIconAlphaAnim.cancel();
        }
        if (z) {
            View view = this.mPlayIcon;
            this.mPlayIconAlphaAnim = ObjectAnimator.ofFloat(view, "alpha", view.getAlpha(), 1.0f);
        } else {
            View view2 = this.mPlayIcon;
            this.mPlayIconAlphaAnim = ObjectAnimator.ofFloat(view2, "alpha", view2.getAlpha(), 0.0f);
        }
        this.mPlayIconAlphaAnim.setDuration(150L);
        this.mPlayIconAlphaAnim.start();
    }

    public void setSlideRefreshedListener(SlideShowView.OnRefreshedListener onRefreshedListener) {
        this.mSlideShowView.setRefreshListener(onRefreshedListener);
    }

    public void setPlayIconAlpha(float f) {
        View view = this.mPlayIcon;
        if (view == null) {
            return;
        }
        view.setAlpha(f);
    }

    public void preUpdateShow(String str, int i) {
        BindImageHelper.bindImage(str, DownloadType.ORIGIN, this.mSlideShowView, GlideOptions.bigPhotoOf().mo970override(Math.min(BaseConfig$ScreenConfig.getScreenWidth(), BaseConfig$ScreenConfig.getScreenHeight())).mo950diskCacheStrategy(DiskCacheStrategy.NONE).mo974priority(Priority.HIGH));
        this.mSlideShowView.setEnterIndex(i);
    }

    public void setShowViewTargetHeight(int i) {
        SlideShowView slideShowView = this.mSlideShowView;
        if (slideShowView != null) {
            slideShowView.setTargetHeight(i);
        }
    }
}
