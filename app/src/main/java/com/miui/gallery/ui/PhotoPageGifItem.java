package com.miui.gallery.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.ui.PhotoPageItem;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.gifdecoder.NSGifDecode;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.photoview.ItemViewInfo;
import com.miui.gallery.util.uil.SpecialPhotoLoadUtil;
import com.miui.gallery.view.animation.AnimationListenerAdapter;
import com.miui.gallery.widget.CircleStrokeProgressBar;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class PhotoPageGifItem extends PhotoPageItem {
    public int mActionBarHeight;
    public Future mGifFuture;
    public NSGifDecode.GifFrameUpdateListener mGifListener;
    public Handler mHandler;
    public View mIndicator;
    public int mIndicatorMargin;
    public boolean mPlayEnable;
    public View mPlayIcon;

    static {
        try {
            System.loadLibrary("MiuiGalleryNSGIF");
        } catch (Throwable th) {
            DefaultLogger.e("PhotoPageGifItem", "load jni failed.", th);
        }
    }

    public PhotoPageGifItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mPlayEnable = true;
        this.mHandler = new Handler(ThreadManager.getMainHandler().getLooper()) { // from class: com.miui.gallery.ui.PhotoPageGifItem.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i != 1000) {
                    if (i != 1001) {
                        return;
                    }
                    BaseDataItem baseDataItem = PhotoPageGifItem.this.mDataItem;
                    if (baseDataItem != null) {
                        baseDataItem.setFilePath(null);
                    }
                    PhotoPageGifItem.this.setPlayIconVisible(true);
                    PhotoPageGifItem.this.setIndicatorVisible(true, true);
                    return;
                }
                Bitmap bitmap = (Bitmap) message.obj;
                if (bitmap == null || bitmap.isRecycled()) {
                    return;
                }
                Matrix suppMatrix = PhotoPageGifItem.this.mPhotoView.getSuppMatrix();
                PhotoPageGifItem.this.mPhotoView.setImageBitmap(bitmap, true);
                PhotoPageGifItem.this.mPhotoView.setDisplayMatrix(suppMatrix);
            }
        };
    }

    public final void initPlayIcon() {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.video_play_icon));
        imageView.setContentDescription(getResources().getString(R.string.play_gif_desc));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(13);
        addView(imageView, layoutParams);
        this.mPlayIcon = imageView;
        FolmeUtil.setDefaultTouchAnim(imageView, null, true);
        this.mPlayIcon.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PhotoPageGifItem.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (PhotoPageGifItem.this.mPlayEnable) {
                    PhotoPageGifItem.this.downloadOrigin();
                }
            }
        });
    }

    public final void initIndicator() {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.gif_indicator));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(9);
        layoutParams.addRule(10);
        imageView.setVisibility(8);
        addView(imageView, layoutParams);
        this.mIndicator = imageView;
    }

    @Override // com.miui.gallery.ui.PhotoPageItem, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
    }

    public final float calcBaseScale(String str, int i, int i2) {
        return SpecialPhotoLoadUtil.getFollowOriginalResolutionBaseScale(str, i, i2);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doOnMatrixChanged(RectF rectF) {
        super.doOnMatrixChanged(rectF);
        adjustIndicatorLocation();
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void onActionBarVisibleChanged(Boolean bool, int i) {
        super.onActionBarVisibleChanged(bool, i);
        this.mActionBarHeight = i;
        setIndicatorVisible(bool.booleanValue(), true);
    }

    private int getIndicatorMargin() {
        if (this.mIndicatorMargin == 0) {
            this.mIndicatorMargin = getResources().getDimensionPixelSize(R.dimen.gif_indicator_margin);
        }
        return this.mIndicatorMargin;
    }

    public final void adjustIndicatorLocation() {
        RectF displayRect;
        View view = this.mIndicator;
        if (view == null || view.getVisibility() != 0 || (displayRect = this.mPhotoView.getDisplayRect()) == null) {
            return;
        }
        float f = displayRect.left;
        float f2 = 0.0f;
        if (f <= 0.0f) {
            f = 0.0f;
        }
        float indicatorMargin = f + getIndicatorMargin();
        float f3 = displayRect.top;
        if (isActionBarVisible()) {
            f2 = this.mActionBarHeight;
        }
        float max = Math.max(f3, f2) + getIndicatorMargin();
        if (this.mIndicator.getTranslationX() == indicatorMargin && this.mIndicator.getTranslationY() == max) {
            return;
        }
        DefaultLogger.d("PhotoPageGifItem", "setTranslation x %s, y %s", Float.valueOf(indicatorMargin), Float.valueOf(max));
        this.mIndicator.setTranslationX(indicatorMargin);
        this.mIndicator.setTranslationY(max);
    }

    public Animation generateShowAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setInterpolator(new CubicEaseOutInterpolator());
        alphaAnimation.setDuration(300L);
        return alphaAnimation;
    }

    public Animation generateHideAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setInterpolator(new CubicEaseOutInterpolator());
        alphaAnimation.setDuration(200L);
        return alphaAnimation;
    }

    public final boolean isOriginFileExist() {
        BaseDataItem baseDataItem = this.mDataItem;
        return baseDataItem != null && !TextUtils.isEmpty(baseDataItem.getOriginalPath());
    }

    public final boolean needShowIndicator() {
        return this.mDataItem != null && !isOriginFileExist() && !isAnimExiting() && isActionBarVisible();
    }

    public final void setIndicatorVisible(boolean z, boolean z2) {
        if (z && needShowIndicator()) {
            if (this.mIndicator == null) {
                initIndicator();
            }
            if (this.mIndicator.getVisibility() == 0) {
                return;
            }
            this.mIndicator.clearAnimation();
            this.mIndicator.setVisibility(0);
            adjustIndicatorLocation();
            if (!z2) {
                return;
            }
            this.mIndicator.startAnimation(generateShowAnimation());
            return;
        }
        View view = this.mIndicator;
        if (view == null || view.getVisibility() == 8) {
            return;
        }
        this.mIndicator.clearAnimation();
        if (z2) {
            Animation generateHideAnimation = generateHideAnimation();
            generateHideAnimation.setAnimationListener(new AnimationListenerAdapter() { // from class: com.miui.gallery.ui.PhotoPageGifItem.3
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    if (PhotoPageGifItem.this.mIndicator != null) {
                        PhotoPageGifItem.this.mIndicator.setVisibility(8);
                    }
                }
            });
            this.mIndicator.startAnimation(generateHideAnimation);
            return;
        }
        this.mIndicator.setVisibility(8);
    }

    public void setPlayEnable(boolean z) {
        this.mPlayEnable = z;
    }

    public final boolean needShowPlayIcon() {
        return !isOriginFileExist() && !isAnimExiting();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPlayIconVisible(boolean z) {
        if (z && needShowPlayIcon()) {
            if (this.mPlayIcon == null) {
                initPlayIcon();
            }
            this.mPlayIcon.setVisibility(0);
            return;
        }
        View view = this.mPlayIcon;
        if (view == null) {
            return;
        }
        view.setVisibility(8);
    }

    public NSGifDecode.GifFrameUpdateListener getGifUpdateListener() {
        if (this.mGifListener == null) {
            this.mGifListener = new NSGifDecode.GifFrameUpdateListener() { // from class: com.miui.gallery.ui.PhotoPageGifItem.4
                @Override // com.miui.gallery.util.gifdecoder.NSGifDecode.GifFrameUpdateListener
                public void onUpdateGifFrame(Bitmap bitmap) {
                    PhotoPageGifItem.this.mHandler.obtainMessage(1000, bitmap).sendToTarget();
                }
            };
        }
        return this.mGifListener;
    }

    public final void cancelGifDecoder() {
        Future future = this.mGifFuture;
        if (future != null) {
            future.cancel();
            this.mGifFuture = null;
        }
        this.mHandler.removeMessages(1000);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doOnSelected(boolean z, boolean z2, boolean z3) {
        tryPlayGif();
        super.doOnSelected(z, z2, z3);
    }

    public final void tryPlayGif() {
        cancelGifDecoder();
        if (isOriginFileExist()) {
            setPlayIconVisible(false);
            setIndicatorVisible(false, false);
            this.mGifFuture = ThreadManager.getMiscPool().submit(new GifJob(this.mDataItem, this.mHandler));
            return;
        }
        setPlayIconVisible(true);
        setIndicatorVisible(true, false);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doOnUnSelected(boolean z, int i) {
        super.doOnUnSelected(z, i);
        cancelGifDecoder();
        setIndicatorVisible(false, false);
        setPlayIconVisible(false);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doRelease() {
        cancelGifDecoder();
        super.doRelease();
    }

    /* loaded from: classes2.dex */
    public class GifJob implements ThreadPool.Job {
        public NSGifDecode mDecoder;
        public Handler mHandler;
        public BaseDataItem mItem;

        public GifJob(BaseDataItem baseDataItem, Handler handler) {
            this.mItem = baseDataItem;
            this.mHandler = handler;
        }

        public final void cancel() {
            NSGifDecode nSGifDecode = this.mDecoder;
            if (nSGifDecode != null) {
                nSGifDecode.setListener(null);
                this.mDecoder.cancel();
                this.mDecoder = null;
            }
        }

        public final void start(ThreadPool.JobContext jobContext) {
            if (jobContext.isCancelled()) {
                cancel();
                return;
            }
            NSGifDecode nSGifDecode = this.mDecoder;
            if (nSGifDecode == null) {
                return;
            }
            nSGifDecode.setListener(PhotoPageGifItem.this.getGifUpdateListener());
            this.mDecoder.run();
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public Object mo1807run(ThreadPool.JobContext jobContext) {
            BaseDataItem baseDataItem = this.mItem;
            if (baseDataItem == null || !baseDataItem.checkOriginalFileExist()) {
                cancel();
                Handler handler = this.mHandler;
                if (handler != null) {
                    handler.obtainMessage(1001).sendToTarget();
                }
                return null;
            }
            jobContext.setCancelListener(new ThreadPool.CancelListener() { // from class: com.miui.gallery.ui.PhotoPageGifItem.GifJob.1
                @Override // com.miui.gallery.concurrent.ThreadPool.CancelListener
                public void onCancel() {
                    GifJob.this.cancel();
                }
            });
            BaseDataItem baseDataItem2 = this.mItem;
            if (baseDataItem2 != null) {
                this.mDecoder = baseDataItem2.createNSGifDecoder(jobContext);
                start(jobContext);
            }
            return null;
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public PhotoPageItem.DownloadManager createDownloadManager() {
        return new GifDownloadManager();
    }

    /* loaded from: classes2.dex */
    public class GifDownloadManager extends PhotoPageItem.DownloadManager {
        public ProgressBar mOriginProgressBar;

        public GifDownloadManager() {
            super();
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.DownloadManager
        public void doOnDownloaded(DownloadType downloadType, String str) {
            super.doOnDownloaded(downloadType, str);
            if (PhotoPageGifItem.this.isPagerSelected()) {
                PhotoPageGifItem.this.tryPlayGif();
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.DownloadManager
        public void doOnProgressVisibilityChanged(boolean z) {
            super.doOnProgressVisibilityChanged(z);
            if (DownloadType.isOrigin(getCurDownloadType())) {
                PhotoPageGifItem.this.setPlayIconVisible(!z);
                if (!z) {
                    return;
                }
                PhotoPageGifItem.this.mErrorDrawableManager.shutDown();
            }
        }

        public final ProgressBar initOriginProgressBar() {
            CircleStrokeProgressBar circleStrokeProgressBar = new CircleStrokeProgressBar(PhotoPageGifItem.this.getContext());
            circleStrokeProgressBar.setDrawablesForLevels(new int[]{R.drawable.photo_download_progress_bg_big}, new int[]{R.drawable.photo_download_progress_second_big}, (int[]) null);
            circleStrokeProgressBar.setMiddleStrokeColors(new int[]{PhotoPageGifItem.this.getResources().getColor(R.color.download_progress_shadow_color)}, PhotoPageGifItem.this.getResources().getDimensionPixelSize(R.dimen.download_progress_shadow_radius));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(13);
            layoutParams.addRule(13);
            circleStrokeProgressBar.setVisibility(8);
            PhotoPageGifItem.this.addView(circleStrokeProgressBar, layoutParams);
            return circleStrokeProgressBar;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.DownloadManager
        public ProgressBar getProgressBar() {
            if (DownloadType.isOrigin(getCurDownloadType())) {
                if (this.mOriginProgressBar == null) {
                    this.mOriginProgressBar = initOriginProgressBar();
                }
                return this.mOriginProgressBar;
            }
            return super.getProgressBar();
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.DownloadManager
        public void adjustProgressBarLocation(boolean z, RectF rectF, boolean z2) {
            if (DownloadType.isOrigin(getCurDownloadType())) {
                return;
            }
            super.adjustProgressBarLocation(z, rectF, z2);
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.DownloadManager
        public boolean needShowDownloadView() {
            if (DownloadType.isOrigin(getCurDownloadType())) {
                return !isDrawableDisplayInside();
            }
            return super.needShowDownloadView();
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.DownloadManager
        public CharSequence getErrorTip() {
            return DownloadType.isOrigin(getCurDownloadType()) ? PhotoPageGifItem.this.getResources().getString(R.string.download_error_format, PhotoPageGifItem.this.getResources().getString(R.string.origin_gif_name)) : super.getErrorTip();
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void animEnter(ItemViewInfo itemViewInfo, final PhotoPageItem.PhotoTransitionListener photoTransitionListener) {
        PhotoPageItem.PhotoTransitionListener photoTransitionListener2 = new PhotoPageItem.PhotoTransitionListener() { // from class: com.miui.gallery.ui.PhotoPageGifItem.5
            @Override // com.github.chrisbanes.photoview.TransitionListener
            public void onTransitEnd() {
                photoTransitionListener.onTransitEnd();
            }

            @Override // com.github.chrisbanes.photoview.TransitionListener
            public void onTransitUpdate(float f) {
                photoTransitionListener.onTransitUpdate(f);
            }
        };
        setPlayIconVisible(false);
        setIndicatorVisible(false, false);
        super.animEnter(itemViewInfo, photoTransitionListener2);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void animExit(ItemViewInfo itemViewInfo, PhotoPageItem.PhotoTransitionListener photoTransitionListener) {
        cancelGifDecoder();
        setPlayIconVisible(false);
        setIndicatorVisible(false, false);
        super.animExit(itemViewInfo, photoTransitionListener);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public boolean setResource(Bitmap bitmap, boolean z) {
        float calcBaseScale;
        if (z) {
            calcBaseScale = calcBaseScale(this.mCacheItem.getMimeType(), this.mCacheItem.getImageWidth(), this.mCacheItem.getImageHeight());
        } else {
            calcBaseScale = calcBaseScale(this.mDataItem.getMimeType(), this.mDataItem.getWidth(), this.mDataItem.getHeight());
        }
        this.mPhotoView.setBaseScale(calcBaseScale);
        return super.setResource(bitmap, z);
    }
}
