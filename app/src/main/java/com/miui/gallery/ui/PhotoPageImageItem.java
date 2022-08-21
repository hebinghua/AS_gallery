package com.miui.gallery.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.lifecycle.LifecycleOwner;
import androidx.tracing.Trace;
import ch.qos.logback.core.joran.action.ActionConst;
import com.android.internal.WindowInsetsCompat;
import com.baidu.platform.comapi.UIMsg;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.OnScaleLevelChangedListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.ScaleLevel;
import com.miui.extraphoto.sdk.ExtraPhotoSDK;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.assistant.cache.MediaFeatureCacheManager;
import com.miui.gallery.compat.view.ViewCompat;
import com.miui.gallery.editor.photo.sdk.AutoRenderer;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.photosapi.ProcessingMetadataQuery$ProcessingUI;
import com.miui.gallery.photosapi.ProcessingMetadataQuery$ProgressStatus;
import com.miui.gallery.provider.ProcessingMedia;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.PhotoPageImageBaseItem;
import com.miui.gallery.ui.PhotoPageImageItem;
import com.miui.gallery.ui.PhotoPageItem;
import com.miui.gallery.util.ImageType;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.ProcessingMediaHelper;
import com.miui.gallery.util.ReceiverUtils;
import com.miui.gallery.util.TalkBackUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.photoview.ItemViewInfo;
import com.miui.gallery.view.animation.AnimationListenerAdapter;
import com.miui.gallery.widget.BeautifyCheckBox;
import com.miui.gallery.widget.CircleStrokeProgressBar;
import com.miui.gallery.widget.GalleryVideoView;
import com.miui.gallery.widget.MiniNavMap;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;
import miuix.view.animation.CubicEaseInOutInterpolator;
import miuix.view.animation.CubicEaseOutInterpolator;
import miuix.view.animation.SineEaseInOutInterpolator;

/* loaded from: classes2.dex */
public class PhotoPageImageItem extends PhotoPageImageBaseItem {
    public static final boolean DEBUG_ENABLE = Log.isLoggable("PhotoPageImageItem", 3);
    public AutoRenderer mAutoRenderer;
    public HigherDefinitionManager mHigherDefinitionManager;
    public ImageProcessingManager mImageProcessingManager;
    public float mMaxScale;
    public MotionPhotoManager mMotionPhotoManager;

    public static /* synthetic */ void $r8$lambda$ah3etRbk6GI6VM7d6m2gAvKyhyk(PhotoPageImageItem photoPageImageItem, boolean z) {
        photoPageImageItem.lambda$doOnUnSelected$0(z);
    }

    public static /* synthetic */ void $r8$lambda$cEcVzehatatGJVgbRi3cu6LImd0(Drawable drawable, ValueAnimator valueAnimator) {
        lambda$setResource$1(drawable, valueAnimator);
    }

    public PhotoPageImageItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMaxScale = 1.0f;
    }

    @Override // com.miui.gallery.ui.PhotoPageImageBaseItem, com.miui.gallery.ui.PhotoPageItem, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mHigherDefinitionManager = new HigherDefinitionManager();
        this.mImageProcessingManager = new ImageProcessingManager();
        this.mMotionPhotoManager = new MotionPhotoManager();
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public PhotoPageItem.CheckManager createCheckManager() {
        return new ImageCheckManager();
    }

    @Override // com.miui.gallery.ui.PhotoPageImageBaseItem, com.miui.gallery.ui.PhotoPageItem
    public void doRelease() {
        this.mHigherDefinitionManager.release();
        this.mMotionPhotoManager.release();
        super.doRelease();
    }

    @Override // com.miui.gallery.ui.PhotoPageImageBaseItem, com.miui.gallery.ui.PhotoPageItem
    public void doOnSelected(boolean z, boolean z2, boolean z3) {
        super.doOnSelected(z, z2, z3);
        Trace.beginSection("imageSelected");
        this.mHigherDefinitionManager.onSelected();
        this.mImageProcessingManager.onSelected();
        this.mMotionPhotoManager.onSelected(z, z2, z3);
        Trace.endSection();
    }

    @Override // com.miui.gallery.ui.PhotoPageImageBaseItem, com.miui.gallery.ui.PhotoPageItem
    public void doOnUnSelected(final boolean z, int i) {
        super.doOnUnSelected(z, i);
        Trace.beginSection("imageUnSelected");
        Trace.beginSection("HigherDefinition");
        this.mHigherDefinitionManager.onUnSelected();
        Trace.endSection();
        Trace.beginSection("ImageProcessing");
        this.mImageProcessingManager.onUnSelected();
        Trace.endSection();
        Trace.beginSection("MotionPhoto");
        this.mMotionPhotoManager.onUnSelected();
        Trace.endSection();
        ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageImageItem$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PhotoPageImageItem.$r8$lambda$ah3etRbk6GI6VM7d6m2gAvKyhyk(PhotoPageImageItem.this, z);
            }
        });
        Trace.endSection();
    }

    public /* synthetic */ void lambda$doOnUnSelected$0(boolean z) {
        if (!z) {
            statMaxScale();
            this.mMaxScale = 1.0f;
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void onPageScrollDragging() {
        super.onPageScrollDragging();
        this.mMotionPhotoManager.onPageScrollDragging();
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void onPageScrollIdle() {
        super.onPageScrollIdle();
        this.mMotionPhotoManager.onPageScrollIdle();
    }

    public final void statMaxScale() {
        RectF baseDisplayRect;
        SamplingStatHelper.recordCountEvent("photo", "photo_item_select_count");
        if (this.mMaxScale > 1.0f) {
            HashMap hashMap = new HashMap();
            hashMap.put("max_scale", String.valueOf(this.mMaxScale));
            BaseDataItem baseDataItem = this.mDataItem;
            if (baseDataItem != null && baseDataItem.getWidth() != 0 && (baseDisplayRect = this.mPhotoView.getBaseDisplayRect()) != null) {
                hashMap.put("max_scale_to_origin", String.valueOf((this.mMaxScale * baseDisplayRect.width()) / this.mDataItem.getWidth()));
            }
            SamplingStatHelper.recordCountEvent("photo", "photo_max_scale", hashMap);
        }
    }

    public void onSpecialTypeEnterClick() {
        this.mMotionPhotoManager.stopPlayback(false);
    }

    @Override // com.miui.gallery.ui.PhotoPageImageBaseItem, com.miui.gallery.ui.PhotoPageItem
    public void onImageLoadFinish(ErrorCode errorCode) {
        super.onImageLoadFinish(errorCode);
        this.mPhotoView.setRegionDecodeEnable(needRegionDecode() && !isMediaInProcessing() && !TransitionViewHolder.needTransit(this.mPhotoView));
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public RequestOptions getCacheRequestOptions(ImageLoadParams imageLoadParams) {
        RequestOptions cacheRequestOptions = super.getCacheRequestOptions(imageLoadParams);
        if (ProcessingMediaHelper.getInstance().isMediaInProcessing(imageLoadParams.getPath())) {
            cacheRequestOptions = Config$ThumbConfig.markAsTemp(cacheRequestOptions);
        }
        return ProcessingMediaHelper.getInstance().isBlurred(imageLoadParams.getPath()) ? Config$ThumbConfig.applyProcessingOptions(cacheRequestOptions) : cacheRequestOptions;
    }

    /* loaded from: classes2.dex */
    public static class TransitionViewHolder {
        public int mPreStatus = -1;
        public int mCurStatus = -1;

        public final int getPreStatus() {
            return this.mPreStatus;
        }

        public final int getCurStatus() {
            return this.mCurStatus;
        }

        public final void setPreStatus(int i) {
            this.mPreStatus = i;
        }

        public final void setCurStatus(int i) {
            this.mCurStatus = i;
        }

        public static boolean needTransit(View view) {
            Object tag = view.getTag(R.id.photo_page_image_item_transition_status);
            if (tag != null) {
                TransitionViewHolder transitionViewHolder = (TransitionViewHolder) tag;
                return transitionViewHolder.getPreStatus() == 0 && 1 == transitionViewHolder.getCurStatus();
            }
            return false;
        }

        public static void updateStatus(View view, boolean z) {
            Object tag = view.getTag(R.id.photo_page_image_item_transition_status);
            TransitionViewHolder transitionViewHolder = tag == null ? new TransitionViewHolder() : (TransitionViewHolder) tag;
            transitionViewHolder.setPreStatus(transitionViewHolder.getCurStatus());
            transitionViewHolder.setCurStatus(!z ? 1 : 0);
            view.setTag(R.id.photo_page_image_item_transition_status, transitionViewHolder);
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doDisplayCache(String str, RequestOptions requestOptions, boolean z) {
        boolean z2 = true;
        boolean z3 = isMediaInProcessing() || ProcessingMediaHelper.getInstance().isMediaInProcessing(this.mCacheItem.getPath());
        boolean z4 = isBlurredForProcessing() || ProcessingMediaHelper.getInstance().isBlurred(this.mCacheItem.getPath());
        PhotoView photoView = this.mPhotoView;
        if (!z3 || z4) {
            z2 = false;
        }
        TransitionViewHolder.updateStatus(photoView, z2);
        if (z3) {
            this.mPhotoView.setRegionDecodeEnable(false);
        }
        super.doDisplayCache(str, requestOptions, z);
    }

    @Override // com.miui.gallery.ui.PhotoPageImageBaseItem, com.miui.gallery.ui.PhotoPageItem
    public void doDisplayImage(String str, RequestOptions requestOptions) {
        TransitionViewHolder.updateStatus(this.mPhotoView, isMediaInProcessing() && !isBlurredForProcessing());
        this.mPhotoView.setRegionDecodeEnable(false);
        this.mPhotoView.setZoomable(!isMediaInProcessing());
        super.doDisplayImage(str, requestOptions);
        if (isPagerSelected() && !this.mIsImageFirstDisplay) {
            this.mRegionManager.resetRegionDecoderIfNeeded();
        }
        this.mIsImageFirstDisplay = false;
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void setProcessingMedia(ProcessingMedia processingMedia) {
        super.setProcessingMedia(processingMedia);
        ImageProcessingManager imageProcessingManager = this.mImageProcessingManager;
        if (imageProcessingManager != null) {
            imageProcessingManager.setProcessingMedia(processingMedia);
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public ProcessingMedia getProcessingMedia() {
        ImageProcessingManager imageProcessingManager = this.mImageProcessingManager;
        if (imageProcessingManager != null) {
            return imageProcessingManager.getProcessingMedia();
        }
        return null;
    }

    @Override // com.miui.gallery.ui.PhotoPageImageBaseItem, com.miui.gallery.ui.PhotoPageItem
    public PhotoPageItem.DownloadManager createDownloadManager() {
        return new ImageDownloadManager();
    }

    /* loaded from: classes2.dex */
    public class ImageDownloadManager extends PhotoPageImageBaseItem.BaseImageDownloadManager {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ImageDownloadManager() {
            super();
            PhotoPageImageItem.this = r1;
        }

        @Override // com.miui.gallery.ui.PhotoPageImageBaseItem.BaseImageDownloadManager, com.miui.gallery.ui.PhotoPageItem.DownloadManager
        public void doOnDownloaded(DownloadType downloadType, String str) {
            super.doOnDownloaded(downloadType, str);
            if (!PhotoPageImageItem.this.isPagerSelected() || !DownloadType.isOrigin(downloadType)) {
                return;
            }
            PhotoPageImageItem.this.mHigherDefinitionManager.onSelected();
        }
    }

    /* loaded from: classes2.dex */
    public class ImageCheckManager extends PhotoPageItem.CheckManager {
        public CompoundButton.OnCheckedChangeListener mCheckStateListener;
        public AutoRenderer.Callback mRenderCallback;
        public BeautifyCheckBox mRenderCheckBox;
        public PhotoPageAdapter.ChoiceModeInterface mRenderInterface;
        public View mSimilarBestMark;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ImageCheckManager() {
            super();
            PhotoPageImageItem.this = r1;
            this.mCheckStateListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.miui.gallery.ui.PhotoPageImageItem.ImageCheckManager.1
                {
                    ImageCheckManager.this = this;
                }

                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    boolean z2 = false;
                    if (z) {
                        compoundButton.setEnabled(false);
                    } else {
                        compoundButton.setText(R.string.fast_share_auto_render);
                    }
                    PhotoPageImageItem photoPageImageItem = PhotoPageImageItem.this;
                    PhotoView photoView = photoPageImageItem.mPhotoView;
                    if (!z && !photoPageImageItem.isMediaInProcessing()) {
                        z2 = true;
                    }
                    photoView.setRegionDecodeEnable(z2);
                    PhotoPageImageItem.this.refreshItem();
                }
            };
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.CheckManager
        public void ensureInflated() {
            super.ensureInflated();
            this.mSimilarBestMark = PhotoPageImageItem.this.findViewById(R.id.similar_best_mark);
            if (PhotoPageImageItem.this.mDataItem != null && MediaFeatureCacheManager.getInstance().shouldShowSelectionStar(PhotoPageImageItem.this.mDataItem.getKey(), false, false, PhotoPageImageItem.this.mDataItem.getBurstKeys())) {
                this.mSimilarBestMark.setVisibility(0);
            } else {
                this.mSimilarBestMark.setVisibility(8);
            }
            BeautifyCheckBox beautifyCheckBox = (BeautifyCheckBox) PhotoPageImageItem.this.findViewById(R.id.check_render);
            this.mRenderCheckBox = beautifyCheckBox;
            beautifyCheckBox.setClickable(false);
            this.mRenderCheckBox.setFocusable(false);
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.CheckManager
        public void dispatchInterfaces(PhotoPageAdapter.ChoiceModeInterface... choiceModeInterfaceArr) {
            super.dispatchInterfaces(choiceModeInterfaceArr);
            this.mRenderInterface = (choiceModeInterfaceArr == null || choiceModeInterfaceArr.length <= 2) ? null : choiceModeInterfaceArr[2];
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.CheckManager
        public void startCheck(PhotoPageAdapter.ChoiceModeInterface... choiceModeInterfaceArr) {
            PhotoPageImageItem photoPageImageItem = PhotoPageImageItem.this;
            photoPageImageItem.mAutoRenderer = new AutoRenderer(photoPageImageItem.getContext());
            super.startCheck(choiceModeInterfaceArr);
            this.mCheckRenderLayout.setOnClickListener(this);
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.CheckManager
        public void endCheck() {
            super.endCheck();
            BeautifyCheckBox beautifyCheckBox = this.mRenderCheckBox;
            if (beautifyCheckBox == null) {
                return;
            }
            beautifyCheckBox.setOnCheckedChangeListener(null);
            this.mRenderCheckBox.setChecked(false);
            this.mRenderCheckBox.setText(R.string.fast_share_auto_render);
            PhotoPageImageItem.this.releaseRenderer();
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v4, types: [java.lang.String] */
        @Override // com.miui.gallery.ui.PhotoPageItem.CheckManager
        public void refreshCheck(PhotoPageAdapter.ChoiceModeInterface... choiceModeInterfaceArr) {
            BaseDataItem baseDataItem;
            BaseDataItem baseDataItem2;
            super.refreshCheck(choiceModeInterfaceArr);
            if (PhotoPageImageItem.this.mAutoRenderer != null && (baseDataItem = PhotoPageImageItem.this.mDataItem) != null && !baseDataItem.isRaw()) {
                this.mCheckRenderLayout.setOnClickListener(this);
                this.mRenderCheckBox.setOnCheckedChangeListener(this.mCheckStateListener);
                this.mRenderCheckBox.setVisibility(0);
                PhotoPageAdapter.ChoiceModeInterface choiceModeInterface = this.mRenderInterface;
                if (choiceModeInterface != null && (baseDataItem2 = PhotoPageImageItem.this.mDataItem) != null) {
                    setCheckBoxState(this.mRenderCheckBox, choiceModeInterface.isChecked(baseDataItem2.getKey()), false);
                    return;
                }
                ?? r0 = ActionConst.NULL;
                if (choiceModeInterface == null) {
                    choiceModeInterface = r0;
                }
                BaseDataItem baseDataItem3 = PhotoPageImageItem.this.mDataItem;
                BaseDataItem baseDataItem4 = r0;
                if (baseDataItem3 != null) {
                    baseDataItem4 = baseDataItem3;
                }
                DefaultLogger.d("PhotoPageImageItem", "renderInterface[%s] or data[%s] not prepared", choiceModeInterface, baseDataItem4);
                return;
            }
            this.mCheckRenderLayout.setOnClickListener(null);
            this.mRenderCheckBox.setOnCheckedChangeListener(null);
            this.mRenderCheckBox.setVisibility(8);
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.CheckManager, android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getId() == R.id.check_render_layout) {
                if (!PhotoPageImageItem.this.isRenderable() || !this.mRenderCheckBox.isEnabled()) {
                    return;
                }
                if (this.mRenderCheckBox.getWidth() > 0 && this.mRenderCheckBox.updateWidthIfNeeded()) {
                    float width = this.mRenderCheckBox.getWidth();
                    this.mRenderCheckBox.initWidth(width, (this.mRenderCheckBox.getPaint().measureText((String) PhotoPageImageItem.this.getResources().getText(R.string.fast_share_auto_render_finish)) + width) - this.mRenderCheckBox.getPaint().measureText((String) PhotoPageImageItem.this.getResources().getText(R.string.fast_share_auto_render)));
                }
                toggleCheckBox(this.mRenderCheckBox, this.mRenderInterface);
                return;
            }
            super.onClick(view);
        }

        public final boolean renderChecked() {
            BaseDataItem baseDataItem;
            PhotoPageAdapter.ChoiceModeInterface choiceModeInterface = this.mRenderInterface;
            return (choiceModeInterface == null || (baseDataItem = PhotoPageImageItem.this.mDataItem) == null || !choiceModeInterface.isChecked(baseDataItem.getKey())) ? false : true;
        }

        public AutoRenderer.Callback getRenderCallback() {
            if (this.mRenderCallback == null) {
                this.mRenderCallback = new AutoRenderer.Callback() { // from class: com.miui.gallery.ui.PhotoPageImageItem.ImageCheckManager.2
                    {
                        ImageCheckManager.this = this;
                    }

                    @Override // com.miui.gallery.editor.photo.sdk.AutoRenderer.Callback
                    public void onDone(Bitmap bitmap) {
                        PhotoPageImageItem photoPageImageItem = PhotoPageImageItem.this;
                        PhotoPageItem.CheckManager checkManager = photoPageImageItem.mCheckManager;
                        if (checkManager != null) {
                            ImageCheckManager imageCheckManager = (ImageCheckManager) checkManager;
                            if (photoPageImageItem.isAttachedToWindow()) {
                                PhotoPageImageItem.this.mPhotoView.setImageBitmap(bitmap, true);
                                ImageCheckManager.this.ensureInflated();
                                imageCheckManager.mRenderCheckBox.setText(R.string.fast_share_auto_render_finish);
                            }
                            imageCheckManager.mRenderCheckBox.setEnabled(true);
                        }
                        PhotoPageDisplayHelper photoPageDisplayHelper = PhotoPageImageItem.this.mPhotoPageDisplayHelper;
                        if (photoPageDisplayHelper != null) {
                            photoPageDisplayHelper.clearPrevious();
                        }
                    }

                    @Override // com.miui.gallery.editor.photo.sdk.AutoRenderer.Callback
                    public void onError(int i, Object obj) {
                        PhotoPageItem.CheckManager checkManager = PhotoPageImageItem.this.mCheckManager;
                        if (checkManager != null) {
                            ((ImageCheckManager) checkManager).mRenderCheckBox.setEnabled(true);
                        }
                        PhotoPageDisplayHelper photoPageDisplayHelper = PhotoPageImageItem.this.mPhotoPageDisplayHelper;
                        if (photoPageDisplayHelper != null) {
                            photoPageDisplayHelper.clearPrevious();
                        }
                    }
                };
            }
            return this.mRenderCallback;
        }
    }

    public final boolean isRenderable() {
        return this.mAutoRenderer != null && (this.mPhotoView.getDrawable() instanceof BitmapDrawable);
    }

    public final void releaseRenderer() {
        DefaultLogger.d("PhotoPageImageItem", "releasing renderer");
        AutoRenderer autoRenderer = this.mAutoRenderer;
        if (autoRenderer != null) {
            this.mAutoRenderer = null;
            autoRenderer.release();
        }
    }

    /* loaded from: classes2.dex */
    public class HigherDefinitionManager extends PhotoPageItem.AbsPhotoRectAwareManager {
        public int mCurActionBarHeight;
        public int mDefaultMarginTop;
        public TextView mDefinitionStateView;
        public int mHRPreviewTipsRes;
        public Handler mHandler;
        public boolean mHasHdViewDisplayed;
        public Runnable mHideDefinitionRunnable;
        public ImageType mImageType;
        public boolean mIsEverEnterHd;
        public boolean mIsScreenshotReceiverRegistered;
        public MiniNavMap mNavMap;
        public ObjectAnimator mNavMapAnimator;
        public int mNavMapMarginBottom;
        public int mNavMapMarginRight;
        public float mNavMapTransX;
        public float mNavMapTransY;
        public OnScaleLevelChangedListener mOnScaleLevelChangedListener;
        public PhotoViewNavMapBridge mPhotoViewNavMapBridge;
        public Runnable mSelectTipsRunnable;
        public BroadcastReceiver mTakeScreenshotReceiver;
        public int mTapForHRPreviewTipsRes;
        public int mTipsAutoHideDelayMillis;
        public int mTipsHorizontalMargin;

        /* renamed from: $r8$lambda$-JSUDCpe3vl5xUg8RR-t_Ff4gEs */
        public static /* synthetic */ void m1545$r8$lambda$JSUDCpe3vl5xUg8RRt_Ff4gEs(HigherDefinitionManager higherDefinitionManager) {
            higherDefinitionManager.hideDefinitionText();
        }

        /* renamed from: $r8$lambda$4WZgNN-Kw1BoY5COIPXlvj5hsuI */
        public static /* synthetic */ void m1546$r8$lambda$4WZgNNKw1BoY5COIPXlvj5hsuI(HigherDefinitionManager higherDefinitionManager) {
            higherDefinitionManager.registerScreenshotReceiver();
        }

        public static /* synthetic */ void $r8$lambda$OGhqVrFkXGyVMLdtoHowl7kt4Po(HigherDefinitionManager higherDefinitionManager) {
            higherDefinitionManager.lambda$showDefinitionText$0();
        }

        public static /* synthetic */ void $r8$lambda$XkXUDP1E_VZoHLKOZ6dDt15yyNE(HigherDefinitionManager higherDefinitionManager) {
            higherDefinitionManager.unregisterScreenshotReceiver();
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public HigherDefinitionManager() {
            super();
            PhotoPageImageItem.this = r1;
            this.mHandler = ThreadManager.getMainHandler();
            this.mImageType = ImageType.NORMAL;
            this.mHideDefinitionRunnable = new Runnable() { // from class: com.miui.gallery.ui.PhotoPageImageItem$HigherDefinitionManager$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageImageItem.HigherDefinitionManager.m1545$r8$lambda$JSUDCpe3vl5xUg8RRt_Ff4gEs(PhotoPageImageItem.HigherDefinitionManager.this);
                }
            };
            this.mOnScaleLevelChangedListener = new OnScaleLevelChangedListener() { // from class: com.miui.gallery.ui.PhotoPageImageItem.HigherDefinitionManager.4
                public boolean mIsInHighScale;

                {
                    HigherDefinitionManager.this = this;
                }

                @Override // com.github.chrisbanes.photoview.OnScaleLevelChangedListener
                public void onMidScaleLevel(boolean z, boolean z2) {
                    if (z) {
                        PhotoPageImageItem.this.mHigherDefinitionManager.changeToMidScale();
                    } else {
                        PhotoPageImageItem.this.mHigherDefinitionManager.exitHigherDefinition();
                    }
                }

                @Override // com.github.chrisbanes.photoview.OnScaleLevelChangedListener
                public void onHighScaleLevel(boolean z, boolean z2) {
                    if (z) {
                        PhotoPageImageItem.this.mHigherDefinitionManager.changeToHigherDefinition(z2);
                        PhotoPageImageItem photoPageImageItem = PhotoPageImageItem.this;
                        photoPageImageItem.mPhotoView.setDragInsideBoundsEnabled(!photoPageImageItem.isActionBarVisible());
                    } else {
                        PhotoPageImageItem.this.mHigherDefinitionManager.exitHigherDefinition();
                        PhotoPageImageItem.this.mPhotoView.setDragInsideBoundsEnabled(false);
                    }
                    if (this.mIsInHighScale != z) {
                        this.mIsInHighScale = z;
                        HigherDefinitionManager.this.recordEnterExitHighScaleLevel(z, z2);
                    }
                }
            };
            this.mTakeScreenshotReceiver = new BroadcastReceiver() { // from class: com.miui.gallery.ui.PhotoPageImageItem.HigherDefinitionManager.5
                {
                    HigherDefinitionManager.this = this;
                }

                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if ("miui.intent.TAKE_SCREENSHOT".equals(intent.getAction())) {
                        if (!intent.getBooleanExtra("IsFinished", true)) {
                            HigherDefinitionManager.this.hideNavMap();
                            HigherDefinitionManager.this.hideDefinitionText();
                        } else if (!HigherDefinitionManager.this.needShowMiniNavMap()) {
                        } else {
                            HigherDefinitionManager.this.showNavMap(false, false);
                        }
                    }
                }
            };
        }

        public final int getTipsAutoHideDelayMillis() {
            if (this.mTipsAutoHideDelayMillis == 0) {
                this.mTipsAutoHideDelayMillis = PhotoPageImageItem.this.getContext().getResources().getInteger(R.integer.high_resolution_tips_auto_hide_delay_millis);
            }
            return this.mTipsAutoHideDelayMillis + UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME + 350;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void onSelected() {
            int width;
            int height;
            super.onSelected();
            BaseDataItem baseDataItem = PhotoPageImageItem.this.mDataItem;
            if (baseDataItem == null) {
                width = 0;
                height = 0;
            } else {
                width = baseDataItem.getWidth();
                height = PhotoPageImageItem.this.mDataItem.getHeight();
            }
            BaseDataItem baseDataItem2 = PhotoPageImageItem.this.mDataItem;
            if (baseDataItem2 == null || TextUtils.isEmpty(baseDataItem2.getOriginalPath())) {
                this.mImageType = ImageType.NORMAL;
            } else {
                this.mImageType = ImageType.of(width, height);
            }
            ImageType imageType = this.mImageType;
            if (imageType == ImageType.HIGH_RESOLUTION_108M) {
                this.mHRPreviewTipsRes = R.string.hr_preview_108m;
                this.mTapForHRPreviewTipsRes = R.string.double_tap_for_hr_preview_108m;
            } else if (imageType.isHighResolution()) {
                this.mHRPreviewTipsRes = R.string.is_higher_definition;
                this.mTapForHRPreviewTipsRes = R.string.double_tap_to_higher_definition;
            }
            PhotoPageImageItem.this.mPhotoView.setHDState(width, height, this.mImageType.isHighResolution());
            PhotoPageImageItem.this.mPhotoView.setPointsScaleEnlargeFactor(this.mImageType.isNavMapSupported() ? 1.0f : 2.0f);
            if (this.mImageType.isHighResolution() || this.mImageType.isNavMapSupported()) {
                PhotoPageImageItem.this.mPhotoView.setOnScaleStageChangedListener(this.mOnScaleLevelChangedListener);
                if (this.mImageType.isHighResolution()) {
                    initHdTextView();
                }
                if (this.mImageType.isNavMapSupported()) {
                    initNavMap();
                    if (needShowMiniNavMap()) {
                        showNavMap(false);
                    }
                }
            } else {
                PhotoPageImageItem.this.mPhotoView.setOnScaleStageChangedListener(null);
            }
            ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageImageItem$HigherDefinitionManager$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageImageItem.HigherDefinitionManager.m1546$r8$lambda$4WZgNNKw1BoY5COIPXlvj5hsuI(PhotoPageImageItem.HigherDefinitionManager.this);
                }
            });
        }

        public final void registerScreenshotReceiver() {
            if (this.mIsScreenshotReceiverRegistered) {
                return;
            }
            this.mIsScreenshotReceiverRegistered = true;
            ReceiverUtils.registerReceiver(PhotoPageImageItem.this.getContext(), this.mTakeScreenshotReceiver, "miui.intent.TAKE_SCREENSHOT");
        }

        public final void unregisterScreenshotReceiver() {
            if (!this.mIsScreenshotReceiverRegistered) {
                return;
            }
            this.mIsScreenshotReceiverRegistered = false;
            ReceiverUtils.safeUnregisterReceiver(PhotoPageImageItem.this.getContext(), this.mTakeScreenshotReceiver);
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void onUnSelected() {
            super.onUnSelected();
            release();
        }

        public final void initHdTextView() {
            if (this.mDefinitionStateView == null) {
                LayoutInflater.from(PhotoPageImageItem.this.getContext()).inflate(R.layout.photo_higher_definition_hint_view, (ViewGroup) PhotoPageImageItem.this, true);
                TextView textView = (TextView) PhotoPageImageItem.this.findViewById(R.id.hd_text_view);
                this.mDefinitionStateView = textView;
                textView.setVisibility(8);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mDefinitionStateView.getLayoutParams();
                this.mDefaultMarginTop = PhotoPageImageItem.this.getResources().getDimensionPixelSize(R.dimen.higher_definition_state_margin_top);
                int dimensionPixelSize = PhotoPageImageItem.this.getResources().getDimensionPixelSize(R.dimen.higher_definition_state_margin_horizontal);
                this.mTipsHorizontalMargin = dimensionPixelSize;
                layoutParams.setMargins(dimensionPixelSize, this.mDefaultMarginTop, dimensionPixelSize, 0);
                layoutParams.addRule(14, -1);
                this.mDefinitionStateView.setTranslationY(this.mCurActionBarHeight);
            }
        }

        public final void initNavMap() {
            if (this.mNavMap == null) {
                this.mNavMap = (MiniNavMap) LayoutInflater.from(PhotoPageImageItem.this.getContext()).inflate(R.layout.photo_page_mini_nav_map, (ViewGroup) PhotoPageImageItem.this, false);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(11);
                layoutParams.addRule(12);
                PhotoPageImageItem.this.addView(this.mNavMap, layoutParams);
            }
            PhotoViewNavMapBridge photoViewNavMapBridge = this.mPhotoViewNavMapBridge;
            if (photoViewNavMapBridge == null) {
                this.mPhotoViewNavMapBridge = new PhotoViewNavMapBridge();
            } else {
                photoViewNavMapBridge.disconnect();
            }
            PhotoViewNavMapBridge photoViewNavMapBridge2 = this.mPhotoViewNavMapBridge;
            PhotoPageImageItem photoPageImageItem = PhotoPageImageItem.this;
            photoViewNavMapBridge2.connect(photoPageImageItem.mPhotoView, this.mNavMap, Math.max(photoPageImageItem.mDataItem.getWidth(), PhotoPageImageItem.this.mDataItem.getHeight()));
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void release() {
            super.release();
            TextView textView = this.mDefinitionStateView;
            if (textView != null) {
                textView.animate().cancel();
                this.mDefinitionStateView.clearAnimation();
                this.mDefinitionStateView.setVisibility(8);
            }
            MiniNavMap miniNavMap = this.mNavMap;
            if (miniNavMap != null) {
                miniNavMap.animate().cancel();
                this.mNavMap.setVisibility(8);
            }
            PhotoViewNavMapBridge photoViewNavMapBridge = this.mPhotoViewNavMapBridge;
            if (photoViewNavMapBridge != null) {
                photoViewNavMapBridge.disconnect();
            }
            this.mHandler.removeCallbacks(this.mHideDefinitionRunnable);
            Runnable runnable = this.mSelectTipsRunnable;
            if (runnable != null) {
                this.mHandler.removeCallbacks(runnable);
            }
            if (this.mImageType.isHighResolution()) {
                SamplingStatHelper.recordCountEvent("photo", "photo_hd_satisfy_count");
                if (this.mIsEverEnterHd) {
                    SamplingStatHelper.recordCountEvent("photo", "photo_hd_enter_count");
                }
            }
            this.mImageType = ImageType.NORMAL;
            this.mHasHdViewDisplayed = false;
            this.mIsEverEnterHd = false;
            PhotoPageImageItem.this.mPhotoView.setOnScaleStageChangedListener(null);
            ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageImageItem$HigherDefinitionManager$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageImageItem.HigherDefinitionManager.$r8$lambda$XkXUDP1E_VZoHLKOZ6dDt15yyNE(PhotoPageImageItem.HigherDefinitionManager.this);
                }
            });
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void changeVisibilityForSpecialScene() {
            if (needShowMiniNavMap() && PhotoPageImageItem.this.isPagerSelected()) {
                showNavMap(false);
            } else {
                hideNavMap();
            }
        }

        public final boolean needShowMiniNavMap() {
            return !PhotoPageImageItem.this.isAnimEntering() && !PhotoPageImageItem.this.isAnimExiting() && !isRotating() && !isDrawableDisplayInside() && PhotoPageImageItem.this.mPhotoView.getScaleLevel() == ScaleLevel.HIGH;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public int getHorizontalMargin() {
            int i;
            int displayCutoutInsetsRight;
            if (this.mNavMapMarginRight == 0) {
                this.mNavMapMarginRight = PhotoPageImageItem.this.getResources().getDimensionPixelSize(R.dimen.photo_page_nav_map_margin_right);
            }
            if (PhotoPageImageItem.this.getResources().getConfiguration().orientation == 2) {
                if (PhotoPageImageItem.this.isActionBarVisible()) {
                    i = this.mNavMapMarginRight;
                    displayCutoutInsetsRight = ViewCompat.getSystemWindowInsetRight(PhotoPageImageItem.this);
                } else {
                    i = this.mNavMapMarginRight;
                    displayCutoutInsetsRight = WindowInsetsCompat.getDisplayCutoutInsetsRight(PhotoPageImageItem.this);
                }
                return i + displayCutoutInsetsRight;
            }
            return this.mNavMapMarginRight;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public int getVerticalMargin() {
            if (PhotoPageImageItem.this.isActionBarVisible()) {
                if (this.mNavMapMarginRight == 0) {
                    this.mNavMapMarginRight = PhotoPageImageItem.this.getResources().getDimensionPixelSize(R.dimen.photo_page_nav_map_margin_right);
                }
                return this.mNavMapMarginRight;
            }
            if (this.mNavMapMarginBottom == 0) {
                this.mNavMapMarginBottom = PhotoPageImageItem.this.getResources().getDimensionPixelSize(R.dimen.photo_page_nav_map_margin_bottom);
            }
            return this.mNavMapMarginBottom;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void adjustLocation(boolean z, RectF rectF, boolean z2) {
            MiniNavMap miniNavMap = this.mNavMap;
            if (miniNavMap == null || miniNavMap.getVisibility() != 0) {
                return;
            }
            doAdjustLocation(z, z2);
        }

        public final void doAdjustLocation(boolean z, boolean z2) {
            cancelNavMapAnim();
            float min = 0.0f - Math.min(getMaxTranslationY(), (z ? PhotoPageImageItem.this.getMenuBarHeight() : 0.0f) + getVerticalMargin());
            float min2 = 0.0f - Math.min(getMaxTranslationX(), getHorizontalMargin());
            if (this.mNavMapTransY == min && this.mNavMapTransX == min2) {
                return;
            }
            this.mNavMapTransX = min2;
            this.mNavMapTransY = min;
            if (z2) {
                this.mNavMap.animate().translationX(min2).translationY(min).setDuration(getAdjustAnimDuration(z)).setInterpolator(getAdjustAnimInterpolator(z)).start();
                return;
            }
            this.mNavMap.setTranslationX(min2);
            this.mNavMap.setTranslationY(min);
        }

        public final void cancelNavMapAnim() {
            MiniNavMap miniNavMap = this.mNavMap;
            if (miniNavMap != null) {
                miniNavMap.animate().cancel();
                this.mNavMapTransX = this.mNavMap.getTranslationX();
                this.mNavMapTransY = this.mNavMap.getTranslationY();
            }
        }

        public final void changeToMidScale() {
            TextView textView = this.mDefinitionStateView;
            if (textView == null) {
                return;
            }
            textView.setText(this.mTapForHRPreviewTipsRes);
            showDefinitionText();
            hideNavMap();
        }

        public final void changeToHigherDefinition(boolean z) {
            TextView textView;
            if (!this.mHasHdViewDisplayed && (textView = this.mDefinitionStateView) != null) {
                textView.setText(this.mHRPreviewTipsRes);
                this.mHasHdViewDisplayed = true;
                this.mIsEverEnterHd = true;
                showDefinitionText();
            }
            showNavMap(z);
        }

        public final void exitHigherDefinition() {
            this.mHasHdViewDisplayed = false;
            hideDefinitionText();
            hideNavMap();
        }

        public final void showDefinitionText() {
            TextView textView = this.mDefinitionStateView;
            if (textView != null) {
                if (!TextUtils.isEmpty(textView.getText())) {
                    TextView textView2 = this.mDefinitionStateView;
                    TalkBackUtil.requestAnnouncementEvent(textView2, textView2.getText().toString());
                }
                if (!this.mDefinitionStateView.isSelected() && this.mSelectTipsRunnable == null) {
                    Runnable runnable = new Runnable() { // from class: com.miui.gallery.ui.PhotoPageImageItem$HigherDefinitionManager$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            PhotoPageImageItem.HigherDefinitionManager.$r8$lambda$OGhqVrFkXGyVMLdtoHowl7kt4Po(PhotoPageImageItem.HigherDefinitionManager.this);
                        }
                    };
                    this.mSelectTipsRunnable = runnable;
                    this.mHandler.postDelayed(runnable, 1000L);
                }
                if (this.mDefinitionStateView.getVisibility() != 0) {
                    this.mDefinitionStateView.animate().cancel();
                    this.mDefinitionStateView.setAlpha(0.0f);
                    this.mDefinitionStateView.setVisibility(0);
                    this.mDefinitionStateView.animate().alpha(1.0f).setDuration(300L).setInterpolator(new SineEaseInOutInterpolator()).setStartDelay(350L).setListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.PhotoPageImageItem.HigherDefinitionManager.1
                        {
                            HigherDefinitionManager.this = this;
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationCancel(Animator animator) {
                            animator.end();
                        }
                    }).start();
                }
                this.mHandler.removeCallbacks(this.mHideDefinitionRunnable);
                this.mHandler.postDelayed(this.mHideDefinitionRunnable, getTipsAutoHideDelayMillis());
            }
        }

        public /* synthetic */ void lambda$showDefinitionText$0() {
            TextView textView = this.mDefinitionStateView;
            if (textView != null) {
                textView.setSelected(true);
            }
        }

        public final void hideDefinitionText() {
            TextView textView = this.mDefinitionStateView;
            if (textView != null) {
                textView.setVisibility(8);
            }
        }

        public final void endNavMapShowingAnim() {
            ObjectAnimator objectAnimator = this.mNavMapAnimator;
            if (objectAnimator != null) {
                if (objectAnimator.isRunning()) {
                    this.mNavMapAnimator.end();
                }
                this.mNavMapAnimator = null;
            }
        }

        public final void showNavMap(boolean z) {
            showNavMap(z, true);
        }

        public final void showNavMap(boolean z, boolean z2) {
            MiniNavMap miniNavMap = this.mNavMap;
            if (miniNavMap != null) {
                if (miniNavMap.getVisibility() != 0) {
                    this.mNavMap.setVisibility(0);
                    endNavMapShowingAnim();
                    if (z2) {
                        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mNavMap, "Alpha", 0.0f, 1.0f);
                        this.mNavMapAnimator = ofFloat;
                        ofFloat.setAutoCancel(true);
                        this.mNavMapAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.PhotoPageImageItem.HigherDefinitionManager.2
                            {
                                HigherDefinitionManager.this = this;
                            }

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationCancel(Animator animator) {
                                animator.end();
                            }
                        });
                        if (z) {
                            this.mNavMapAnimator.setDuration(600L);
                            this.mNavMapAnimator.setInterpolator(PhotoPageItem.ZOOM_INTERPOLATOR);
                        } else {
                            this.mNavMapAnimator.setDuration(150L);
                        }
                        this.mNavMapAnimator.start();
                    }
                }
                adjustLocation(PhotoPageImageItem.this.isActionBarVisible(), PhotoPageImageItem.this.mPhotoView.getDisplayRect(), false);
            }
        }

        public final void hideNavMap() {
            MiniNavMap miniNavMap = this.mNavMap;
            if (miniNavMap == null || miniNavMap.getVisibility() != 0) {
                return;
            }
            this.mNavMap.setVisibility(8);
        }

        public final void translateByActionBarVisibility(int i) {
            TextView textView = this.mDefinitionStateView;
            if (textView == null) {
                return;
            }
            float translationY = textView.getTranslationY();
            if (this.mDefinitionStateView.getVisibility() == 0) {
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mDefinitionStateView, View.TRANSLATION_Y, translationY, translationY + i);
                ofFloat.setDuration(PhotoPageImageItem.this.getResources().getInteger(17694721));
                ofFloat.setInterpolator(new DecelerateInterpolator());
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.PhotoPageImageItem.HigherDefinitionManager.3
                    {
                        HigherDefinitionManager.this = this;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                        animator.end();
                    }
                });
                ofFloat.start();
                return;
            }
            this.mDefinitionStateView.setTranslationY(this.mCurActionBarHeight);
        }

        public void onActionBarVisibleChanged(Boolean bool, RectF rectF, int i) {
            boolean z = false;
            this.mCurActionBarHeight = bool.booleanValue() ? i : 0;
            if (bool.booleanValue()) {
                translateByActionBarVisibility(i);
                PhotoPageImageItem.this.mPhotoView.setDragInsideBoundsEnabled(false);
            } else {
                translateByActionBarVisibility(-i);
                PhotoView photoView = PhotoPageImageItem.this.mPhotoView;
                if (photoView.getScaleLevel() == ScaleLevel.HIGH) {
                    z = true;
                }
                photoView.setDragInsideBoundsEnabled(z);
            }
            adjustLocation(bool.booleanValue(), rectF, true);
        }

        public final void recordEnterExitHighScaleLevel(boolean z, boolean z2) {
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, this.mImageType.getName());
            hashMap.put("from", z2 ? "doubleTap" : "gestureScale");
            SamplingStatHelper.recordCountEvent("photo", z ? "photo_enter_HD_preview" : "photo_exit_HD_preview", hashMap);
        }
    }

    /* loaded from: classes2.dex */
    public class ImageProcessingManager extends PhotoPageItem.AbsPhotoRectAwareManager {
        public float mAdjustX;
        public float mAdjustY;
        public Animator mAnimator;
        public CircleStrokeProgressBar mDeterminateProgress;
        public boolean mHasShowProgress;
        public ProgressBar mIndeterminateProgress;
        public View mLoadingView;
        public Runnable mNextQueryRunnable;
        public ProcessingMedia mProcessingMedia;
        public QueryProgressAsyncTask mQueryTask;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ImageProcessingManager() {
            super();
            PhotoPageImageItem.this = r1;
        }

        public void ensureInflated() {
            if (this.mLoadingView == null) {
                this.mLoadingView = LayoutInflater.from(PhotoPageImageItem.this.getContext()).inflate(R.layout.photo_page_processing_progress, (ViewGroup) null, false);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(12);
                layoutParams.addRule(9);
                CircleStrokeProgressBar circleStrokeProgressBar = (CircleStrokeProgressBar) this.mLoadingView.findViewById(R.id.determinate_progress_bar);
                this.mDeterminateProgress = circleStrokeProgressBar;
                circleStrokeProgressBar.setDrawablesForLevels(new int[]{R.drawable.photo_download_progress_bg}, new int[]{R.drawable.photo_download_progress_second}, (int[]) null);
                this.mDeterminateProgress.setMiddleStrokeColors(new int[]{PhotoPageImageItem.this.getResources().getColor(R.color.download_progress_shadow_color)}, PhotoPageImageItem.this.getResources().getDimensionPixelSize(R.dimen.download_progress_shadow_radius_big));
                this.mDeterminateProgress.setVisibility(8);
                ProgressBar progressBar = (ProgressBar) this.mLoadingView.findViewById(R.id.indeterminate_progress_bar);
                this.mIndeterminateProgress = progressBar;
                progressBar.setVisibility(8);
                PhotoPageImageItem.this.addView(this.mLoadingView, layoutParams);
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public int getVerticalMargin() {
            if (this.mMargin == 0) {
                this.mMargin = PhotoPageImageItem.this.getContext().getResources().getDimensionPixelSize(R.dimen.process_progress_margin);
            }
            return this.mMargin;
        }

        public final void updateProgress() {
            if (this.mProcessingMedia != null) {
                showLoading(false);
            } else {
                hideLoading(true);
            }
        }

        public void setProcessingMedia(ProcessingMedia processingMedia) {
            this.mProcessingMedia = processingMedia;
            if (PhotoPageImageItem.this.isPagerSelected()) {
                updateProgress();
            }
        }

        public ProcessingMedia getProcessingMedia() {
            return this.mProcessingMedia;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void onSelected() {
            super.onSelected();
            updateProgress();
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void onUnSelected() {
            cancelQueryTask();
            if (this.mNextQueryRunnable != null) {
                ThreadManager.getMainHandler().removeCallbacks(this.mNextQueryRunnable);
            }
            hideLoading(false);
            super.onUnSelected();
        }

        public final boolean isMediaInProcessing() {
            return this.mProcessingMedia != null;
        }

        public final boolean isBlurredForProcessing() {
            return isMediaInProcessing() && this.mProcessingMedia.isUsingGaussianForTemp();
        }

        public final boolean isNeedShowProcessingView() {
            return isMediaInProcessing() && this.mProcessingMedia.getProcessingUI() != ProcessingMetadataQuery$ProcessingUI.NONE;
        }

        public boolean needShowProcessingView() {
            return !PhotoPageImageItem.this.isAnimEntering() && !PhotoPageImageItem.this.isAnimExiting() && isNeedShowProcessingView() && !isRotating() && !isDrawableDisplayInside();
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void changeVisibilityForSpecialScene() {
            if (needShowProcessingView()) {
                if (!PhotoPageImageItem.this.isPagerSelected()) {
                    return;
                }
                updateProgress();
                return;
            }
            hideLoading(true);
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void adjustLocation(boolean z, RectF rectF, boolean z2) {
            View view;
            if (this.mHasShowProgress && (view = this.mLoadingView) != null && view.getVisibility() == 0) {
                doAdjustLocation(z, rectF, z2);
            }
        }

        public final void endAnim() {
            Animator animator = this.mAnimator;
            if (animator == null || !animator.isRunning()) {
                return;
            }
            this.mAnimator.end();
            this.mAnimator = null;
        }

        public final void doAdjustLocation(boolean z, RectF rectF, boolean z2) {
            endAnim();
            float height = PhotoPageImageItem.this.getHeight() - (z ? PhotoPageImageItem.this.getMenuBarHeight() : 0);
            float height2 = rectF != null ? rectF.bottom : PhotoPageImageItem.this.getHeight();
            float f = rectF != null ? rectF.left : 0.0f;
            float max = Math.max(getMaxTranslationY(), Math.min(height, height2) - getVerticalMargin()) - PhotoPageImageItem.this.getHeight();
            float min = Math.min(getMaxTranslationX(), Math.max(f, 0.0f) + getHorizontalMargin());
            if (this.mAdjustY == max && this.mAdjustX == min) {
                return;
            }
            this.mAdjustX = min;
            this.mAdjustY = max;
            if (z2) {
                AnimatorSet animatorSet = new AnimatorSet();
                View view = this.mLoadingView;
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "TranslationY", view.getTranslationY(), max);
                View view2 = this.mLoadingView;
                animatorSet.playTogether(ObjectAnimator.ofFloat(view2, "TranslationX", view2.getTranslationX(), min), ofFloat);
                animatorSet.setDuration(getAdjustAnimDuration(z));
                animatorSet.setInterpolator(getAdjustAnimInterpolator(z));
                this.mAnimator = animatorSet;
                animatorSet.start();
                return;
            }
            this.mLoadingView.setTranslationY(max);
            this.mLoadingView.setTranslationX(min);
        }

        public final void showLoading(boolean z) {
            if (needShowProcessingView()) {
                ensureInflated();
                this.mHasShowProgress = true;
                ProcessingMedia.ProcessingMetadata processingMetadata = this.mProcessingMedia.getProcessingMetadata();
                if (processingMetadata != null) {
                    if (processingMetadata.getProgressStatus() == ProcessingMetadataQuery$ProgressStatus.DETERMINATE) {
                        this.mIndeterminateProgress.setVisibility(8);
                        this.mDeterminateProgress.setVisibility(0);
                        this.mDeterminateProgress.setMax(100);
                        this.mDeterminateProgress.setProgress(processingMetadata.getProgressPercentage());
                        fetchNext();
                    } else {
                        this.mDeterminateProgress.setVisibility(8);
                        this.mIndeterminateProgress.setVisibility(0);
                    }
                    this.mLoadingView.clearAnimation();
                    this.mLoadingView.setVisibility(0);
                    doAdjustLocation(PhotoPageImageItem.this.isActionBarVisible(), PhotoPageImageItem.this.mPhotoView.getDisplayRect(), false);
                    if (!z) {
                        return;
                    }
                    this.mLoadingView.startAnimation(generateShowAnimation());
                    return;
                }
                this.mLoadingView.setVisibility(8);
            }
        }

        public final void hideLoading(boolean z) {
            View view = this.mLoadingView;
            if (view == null || view.getVisibility() == 8) {
                return;
            }
            this.mLoadingView.setVisibility(8);
            this.mLoadingView.clearAnimation();
            if (z) {
                Animation generateHideAnimation = generateHideAnimation();
                generateHideAnimation.setAnimationListener(new AnimationListenerAdapter() { // from class: com.miui.gallery.ui.PhotoPageImageItem.ImageProcessingManager.1
                    {
                        ImageProcessingManager.this = this;
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationEnd(Animation animation) {
                        if (ImageProcessingManager.this.mLoadingView != null) {
                            ImageProcessingManager.this.mLoadingView.setVisibility(8);
                        }
                    }
                });
                this.mLoadingView.startAnimation(generateHideAnimation);
                return;
            }
            this.mLoadingView.setVisibility(8);
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public Animation generateHideAnimation() {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setInterpolator(new CubicEaseOutInterpolator());
            alphaAnimation.setDuration(350L);
            alphaAnimation.setStartOffset(50L);
            return alphaAnimation;
        }

        public final void onProgressPercentageUpdated(Uri uri, int i) {
            if (PhotoPageImageItem.DEBUG_ENABLE) {
                DefaultLogger.d("PhotoPageImageItem", "Updating progress for: %s to: %d", uri, Integer.valueOf(i));
            }
            CircleStrokeProgressBar circleStrokeProgressBar = this.mDeterminateProgress;
            if (circleStrokeProgressBar != null) {
                circleStrokeProgressBar.setProgress(i);
            }
            if (i < 100) {
                ThreadManager.getMainHandler().postDelayed(getNextQueryRunnable(), 100L);
            }
        }

        public final Runnable getNextQueryRunnable() {
            if (this.mNextQueryRunnable == null) {
                this.mNextQueryRunnable = new Runnable() { // from class: com.miui.gallery.ui.PhotoPageImageItem.ImageProcessingManager.2
                    {
                        ImageProcessingManager.this = this;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        ImageProcessingManager.this.fetchNext();
                    }
                };
            }
            return this.mNextQueryRunnable;
        }

        public final void cancelQueryTask() {
            QueryProgressAsyncTask queryProgressAsyncTask = this.mQueryTask;
            if (queryProgressAsyncTask != null) {
                queryProgressAsyncTask.cancel(true);
                this.mQueryTask = null;
            }
        }

        public final void fetchNext() {
            cancelQueryTask();
            QueryProgressAsyncTask queryProgressAsyncTask = new QueryProgressAsyncTask(PhotoPageImageItem.this.getContext(), this.mProcessingMedia.getUri());
            this.mQueryTask = queryProgressAsyncTask;
            queryProgressAsyncTask.execute(new Void[0]);
        }

        /* loaded from: classes2.dex */
        public final class QueryProgressAsyncTask extends AsyncTask<Void, Void, Integer> {
            public final Context mContext;
            public final Uri mUri;

            public QueryProgressAsyncTask(Context context, Uri uri) {
                ImageProcessingManager.this = r1;
                this.mContext = context;
                this.mUri = uri;
            }

            @Override // android.os.AsyncTask
            public Integer doInBackground(Void... voidArr) {
                if (isCancelled()) {
                    return null;
                }
                Cursor query = this.mContext.getContentResolver().query(this.mUri, new String[]{"progress_percentage"}, null, null, null);
                if (isCancelled()) {
                    return null;
                }
                if (query == null) {
                    if (PhotoPageImageItem.DEBUG_ENABLE) {
                        DefaultLogger.d("PhotoPageImageItem", "Failed to obtain cursor for: %s", this.mUri);
                    }
                    return null;
                }
                try {
                    if (!query.moveToFirst()) {
                        if (PhotoPageImageItem.DEBUG_ENABLE) {
                            DefaultLogger.d("PhotoPageImageItem", "Failed to find item for: %s", this.mUri);
                        }
                        return null;
                    }
                    return Integer.valueOf(query.getInt(query.getColumnIndexOrThrow("progress_percentage")));
                } finally {
                    query.close();
                }
            }

            @Override // android.os.AsyncTask
            public void onPostExecute(Integer num) {
                if (!isCancelled() && num != null) {
                    ImageProcessingManager.this.onProgressPercentageUpdated(this.mUri, num.intValue());
                }
            }
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void onActionBarVisibleChanged(Boolean bool, int i) {
        super.onActionBarVisibleChanged(bool, i);
        RectF displayRect = this.mPhotoView.getDisplayRect();
        this.mHigherDefinitionManager.onActionBarVisibleChanged(bool, displayRect, i);
        this.mImageProcessingManager.adjustLocation(bool.booleanValue(), displayRect, true);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem, androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onResume(LifecycleOwner lifecycleOwner) {
        super.onResume(lifecycleOwner);
        this.mMotionPhotoManager.onResume();
    }

    @Override // com.miui.gallery.ui.PhotoPageItem, androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onPause(LifecycleOwner lifecycleOwner) {
        super.onPause(lifecycleOwner);
        this.mMotionPhotoManager.onPause();
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void onActivityTransition() {
        super.onActivityTransition();
        this.mMotionPhotoManager.onActivityTransition();
    }

    @Override // com.miui.gallery.ui.PhotoPageItem, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mImageProcessingManager.adjustLocation(isActionBarVisible(), this.mPhotoView.getDisplayRect(), false);
        this.mMotionPhotoManager.onConfigurationChanged();
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doOnMatrixChanged(RectF rectF) {
        super.doOnMatrixChanged(rectF);
        this.mImageProcessingManager.onMatrixChanged(rectF);
        this.mMotionPhotoManager.onMatrixChanged();
        RectF baseDisplayRect = this.mPhotoView.getBaseDisplayRect();
        if (rectF == null || baseDisplayRect == null) {
            return;
        }
        float width = rectF.width() / baseDisplayRect.width();
        if (width <= this.mMaxScale) {
            return;
        }
        this.mMaxScale = width;
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void animExit(ItemViewInfo itemViewInfo, PhotoPageItem.PhotoTransitionListener photoTransitionListener) {
        this.mMotionPhotoManager.onUnSelected();
        this.mHigherDefinitionManager.onUnSelected();
        super.animExit(itemViewInfo, photoTransitionListener);
        this.mImageProcessingManager.onUnSelected();
        statMaxScale();
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public boolean isMediaInProcessing() {
        ImageProcessingManager imageProcessingManager = this.mImageProcessingManager;
        return imageProcessingManager != null && imageProcessingManager.isMediaInProcessing();
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public boolean isBlurredForProcessing() {
        ImageProcessingManager imageProcessingManager = this.mImageProcessingManager;
        return imageProcessingManager != null && imageProcessingManager.isBlurredForProcessing();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        MotionPhotoManager motionPhotoManager = this.mMotionPhotoManager;
        return (motionPhotoManager != null && motionPhotoManager.onTouch(motionEvent)) | dispatchTouchEvent;
    }

    public void onActionBarOperationClick() {
        this.mMotionPhotoManager.onActionBarOperationClick();
    }

    @Override // com.miui.gallery.ui.PhotoPageItem, com.miui.gallery.ui.PhotoPageDisplayHelper.ResourceCallback
    public boolean onResourceReady(String str, Bitmap bitmap, boolean z, boolean z2) {
        if (z || z2) {
            return super.onResourceReady(str, bitmap, z, z2);
        }
        ImageCheckManager imageCheckManager = (ImageCheckManager) this.mCheckManager;
        if (imageCheckManager != null && imageCheckManager.renderChecked()) {
            if (this.mAutoRenderer != null) {
                DefaultLogger.d("PhotoPageImageItem", "render uri[%s], bitmap[%s]", str, bitmap);
                this.mAutoRenderer.render(bitmap, imageCheckManager.getRenderCallback());
                return false;
            }
            DefaultLogger.w("PhotoPageImageItem", "PGEditCoreAPI released");
            return true;
        }
        return super.onResourceReady(str, bitmap, false, false);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public boolean setResource(final Bitmap bitmap, boolean z) {
        Drawable drawable = this.mPhotoView.getDrawable();
        if (drawable != null && bitmap != null && TransitionViewHolder.needTransit(this.mPhotoView)) {
            final BitmapDrawable bitmapDrawable = new BitmapDrawable(this.mPhotoView.getResources(), bitmap);
            this.mPhotoView.setImageDrawable(new LayerDrawable(new Drawable[]{drawable, bitmapDrawable}));
            ValueAnimator ofInt = ValueAnimator.ofInt(0, 255);
            ofInt.setInterpolator(new CubicEaseInOutInterpolator());
            ofInt.setDuration(500L);
            ofInt.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.PhotoPageImageItem.1
                {
                    PhotoPageImageItem.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    PhotoPageImageItem.this.mPhotoView.setImageBitmap(bitmap, true);
                    PhotoPageImageItem.this.mPhotoView.setRegionDecodeEnable(true);
                }
            });
            ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.ui.PhotoPageImageItem$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    PhotoPageImageItem.$r8$lambda$cEcVzehatatGJVgbRi3cu6LImd0(bitmapDrawable, valueAnimator);
                }
            });
            ofInt.start();
            return false;
        }
        return super.setResource(bitmap, z);
    }

    public static /* synthetic */ void lambda$setResource$1(Drawable drawable, ValueAnimator valueAnimator) {
        drawable.setAlpha(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    /* loaded from: classes2.dex */
    public class MotionPhotoManager {
        public boolean mAutoPlay;
        public Runnable mAutoStopRunnable;
        public boolean mHasPerformedLongPress;
        public boolean mHasTransition;
        public RelativeLayout.LayoutParams mLayoutParams;
        public boolean mNeedHapticFeedback;
        public MediaPlayer.OnCompletionListener mOnCompletionListener;
        public MediaPlayer.OnErrorListener mOnErrorListener;
        public MediaPlayer.OnPreparedListener mOnPreparedListener;
        public CheckForLongPress mPendingCheckForLongPress;
        public boolean mPendingStop;
        public boolean mPlayable;
        public boolean mPressed;
        public Runnable mRemoveRunnable;
        public Runnable mStartRunnable;
        public boolean mStarted;
        public GalleryVideoView mVideoView;

        public void onSelected(boolean z, boolean z2, boolean z3) {
        }

        public MotionPhotoManager() {
            PhotoPageImageItem.this = r1;
            this.mStarted = false;
            this.mPendingStop = false;
            this.mOnErrorListener = new MediaPlayer.OnErrorListener() { // from class: com.miui.gallery.ui.PhotoPageImageItem.MotionPhotoManager.2
                {
                    MotionPhotoManager.this = this;
                }

                @Override // android.media.MediaPlayer.OnErrorListener
                public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                    DefaultLogger.w("PhotoPageImageItem", "MotionPhoto play error %d %d", Integer.valueOf(i), Integer.valueOf(i2));
                    MotionPhotoManager.this.removeVideoView("error");
                    return true;
                }
            };
            this.mOnCompletionListener = new MediaPlayer.OnCompletionListener() { // from class: com.miui.gallery.ui.PhotoPageImageItem.MotionPhotoManager.3
                {
                    MotionPhotoManager.this = this;
                }

                @Override // android.media.MediaPlayer.OnCompletionListener
                public void onCompletion(MediaPlayer mediaPlayer) {
                    DefaultLogger.d("PhotoPageImageItem", "MotionPhoto play complete");
                    MotionPhotoManager.this.hideVideoView(true);
                }
            };
            this.mOnPreparedListener = new MediaPlayer.OnPreparedListener() { // from class: com.miui.gallery.ui.PhotoPageImageItem.MotionPhotoManager.4
                {
                    MotionPhotoManager.this = this;
                }

                @Override // android.media.MediaPlayer.OnPreparedListener
                public void onPrepared(MediaPlayer mediaPlayer) {
                    DefaultLogger.d("PhotoPageImageItem", "MotionPhoto play prepared");
                    if (MotionPhotoManager.this.mVideoView == null || MotionPhotoManager.this.mPendingStop) {
                        return;
                    }
                    MotionPhotoManager.this.mVideoView.start();
                    MotionPhotoManager.this.mVideoView.pause();
                    PhotoPageImageItem.this.mPhotoView.setVisibility(8);
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                    alphaAnimation.setStartOffset(150L);
                    alphaAnimation.setDuration(500L);
                    alphaAnimation.setInterpolator(new SineEaseInOutInterpolator());
                    PhotoPageImageItem.this.mPhotoView.startAnimation(alphaAnimation);
                    MotionPhotoManager.this.mVideoView.postDelayed(MotionPhotoManager.this.mStartRunnable, 150L);
                }
            };
            this.mStartRunnable = new Runnable() { // from class: com.miui.gallery.ui.PhotoPageImageItem.MotionPhotoManager.5
                {
                    MotionPhotoManager.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    DefaultLogger.d("PhotoPageImageItem", "MotionPhoto start play");
                    if (MotionPhotoManager.this.mVideoView != null) {
                        MotionPhotoManager.this.mStarted = true;
                        MotionPhotoManager.this.mVideoView.start();
                        if (!MotionPhotoManager.this.mNeedHapticFeedback) {
                            return;
                        }
                        if (LinearMotorHelper.LINEAR_MOTOR_SUPPORTED.get(null).booleanValue()) {
                            LinearMotorHelper.performHapticFeedback(MotionPhotoManager.this.mVideoView, LinearMotorHelper.HAPTIC_POPUP_NORMAL);
                        } else {
                            PhotoPageImageItem.this.performHapticFeedback(1);
                        }
                    }
                }
            };
            this.mRemoveRunnable = new Runnable() { // from class: com.miui.gallery.ui.PhotoPageImageItem.MotionPhotoManager.6
                {
                    MotionPhotoManager.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    DefaultLogger.d("PhotoPageImageItem", "MotionPhoto delayRemove");
                    MotionPhotoManager.this.removeVideoView("delayRemove");
                }
            };
            this.mAutoStopRunnable = new Runnable() { // from class: com.miui.gallery.ui.PhotoPageImageItem.MotionPhotoManager.7
                {
                    MotionPhotoManager.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    DefaultLogger.d("PhotoPageImageItem", "MotionPhoto autoStop");
                    MotionPhotoManager.this.stopPlayback(true);
                }
            };
        }

        public void onActionBarOperationClick() {
            DefaultLogger.d("PhotoPageImageItem", "MotionPhoto onActionBarClick");
            if (isPlayRequested() && !this.mAutoPlay) {
                stopPlayback(true);
            } else if (!startPlay(false, false)) {
            } else {
                SamplingStatHelper.recordCountEvent("motion_photo", "motion_photo_play_action_bar");
                TrackController.trackClick("403.11.3.1.11154", "403.11.0.1.11151");
            }
        }

        public void onUnSelected() {
            removeVideoView("unSelected");
        }

        public void onResume() {
            this.mHasTransition = false;
        }

        public void onPause() {
            removeVideoView("onPause");
        }

        public void onActivityTransition() {
            this.mHasTransition = true;
            removeVideoView("onTransition");
        }

        public void onConfigurationChanged() {
            removeVideoView("configChanged");
        }

        public void onPageScrollDragging() {
            if (this.mVideoView != null) {
                DefaultLogger.d("PhotoPageImageItem", "MotionPhoto pause onDragging");
                this.mPendingStop = true;
                if (this.mVideoView.isPlaying()) {
                    this.mVideoView.pause();
                }
                PhotoPageImageItem.this.mPhotoView.removeCallbacks(this.mAutoStopRunnable);
                this.mVideoView.removeCallbacks(this.mStartRunnable);
                this.mVideoView.removeCallbacks(this.mRemoveRunnable);
            }
        }

        public void onPageScrollIdle() {
            if (this.mPendingStop && this.mVideoView != null) {
                DefaultLogger.d("PhotoPageImageItem", "MotionPhoto hide onIdle");
                if (this.mStarted) {
                    hideVideoView(true);
                } else {
                    removeVideoView("onIdle");
                }
            }
            this.mPendingStop = false;
        }

        public void onMatrixChanged() {
            removeVideoView("matrixChanged");
        }

        public final boolean isPlayRequested() {
            return this.mVideoView != null;
        }

        public final boolean startPlay(boolean z, boolean z2) {
            boolean z3 = false;
            if (PhotoPageImageItem.this.isPaused() || this.mHasTransition || PhotoPageImageItem.this.mCheckManager.inAction() || !isItemValid()) {
                return false;
            }
            if (z) {
                if (isPlayRequested()) {
                    return false;
                }
                return addVideoView(true);
            }
            GalleryVideoView galleryVideoView = this.mVideoView;
            if (galleryVideoView != null && galleryVideoView.isPlaying()) {
                DefaultLogger.d("PhotoPageImageItem", "MotionPhoto cancel auto stop");
                this.mVideoView.removeCallbacks(this.mAutoStopRunnable);
                this.mVideoView.setVolume(1.0f);
                this.mVideoView.requestAudioFocus(2);
                if (z2) {
                    if (LinearMotorHelper.LINEAR_MOTOR_SUPPORTED.get(null).booleanValue()) {
                        LinearMotorHelper.performHapticFeedback(this.mVideoView, LinearMotorHelper.HAPTIC_POPUP_NORMAL);
                    } else {
                        PhotoPageImageItem.this.performHapticFeedback(1);
                    }
                }
                this.mAutoPlay = false;
                return true;
            }
            removeVideoView("MotionPhoto start play");
            boolean addVideoView = addVideoView(false);
            if (z2 && addVideoView) {
                z3 = true;
            }
            this.mNeedHapticFeedback = z3;
            return addVideoView;
        }

        public final boolean addVideoView(boolean z) {
            if (!isItemValid() || isPlayRequested()) {
                return false;
            }
            DefaultLogger.d("PhotoPageImageItem", "MotionPhoto addVideoView");
            this.mVideoView = new GalleryVideoView(PhotoPageImageItem.this.getContext());
            PhotoPageImageItem.this.mPhotoView.setInterceptTouch(true);
            this.mNeedHapticFeedback = false;
            this.mAutoPlay = z;
            this.mStarted = false;
            if (this.mLayoutParams == null) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
                this.mLayoutParams = layoutParams;
                layoutParams.addRule(13);
            }
            this.mVideoView.setOnCompletionListener(this.mOnCompletionListener);
            this.mVideoView.setOnErrorListener(this.mOnErrorListener);
            this.mVideoView.setOnPreparedListener(this.mOnPreparedListener);
            if (z) {
                this.mVideoView.setVolume(0.0f);
                this.mVideoView.setAudioFocusRequest(0);
            } else {
                this.mVideoView.setVolume(1.0f);
                this.mVideoView.setAudioFocusRequest(2);
            }
            Drawable drawable = PhotoPageImageItem.this.mPhotoView.getDrawable();
            if (drawable != null) {
                RectF rectF = new RectF(0.0f, 0.0f, PhotoPageImageItem.this.mPhotoView.getWidth(), PhotoPageImageItem.this.mPhotoView.getHeight());
                RectF rectF2 = new RectF(0.0f, 0.0f, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                Matrix displayMatrix = PhotoPageImageItem.this.mPhotoView.getDisplayMatrix();
                RectF rectF3 = new RectF(rectF2);
                displayMatrix.mapRect(rectF3);
                rectF3.setIntersect(rectF3, rectF);
                rectF3.offset(-rectF3.left, -rectF3.top);
                Matrix matrix = new Matrix();
                matrix.setRectToRect(rectF3, rectF2, Matrix.ScaleToFit.FILL);
                matrix.postConcat(displayMatrix);
                matrix.postTranslate(rectF3.centerX() - rectF.centerX(), rectF3.centerY() - rectF.centerY());
                this.mVideoView.setTransform(matrix);
                this.mLayoutParams.width = (int) rectF3.width();
                this.mLayoutParams.height = (int) rectF3.height();
            } else {
                this.mVideoView.setTransform(null);
                RelativeLayout.LayoutParams layoutParams2 = this.mLayoutParams;
                layoutParams2.width = -1;
                layoutParams2.height = -1;
            }
            this.mVideoView.setVideoFilePath(PhotoPageImageItem.this.mDataItem.getOriginalPath(), PhotoPageImageItem.this.mDataItem.getMotionOffset());
            PhotoPageImageItem.this.addView(this.mVideoView, 0, this.mLayoutParams);
            if (z) {
                PhotoPageImageItem.this.mPhotoView.postDelayed(this.mAutoStopRunnable, 750L);
            }
            return true;
        }

        public final void hideVideoView(boolean z) {
            DefaultLogger.d("PhotoPageImageItem", "MotionPhoto hideVideoView");
            if (z) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                alphaAnimation.setDuration(500L);
                alphaAnimation.setFillAfter(true);
                alphaAnimation.setInterpolator(new SineEaseInOutInterpolator());
                PhotoPageImageItem.this.mPhotoView.startAnimation(alphaAnimation);
                PhotoPageImageItem.this.mPhotoView.removeCallbacks(this.mAutoStopRunnable);
                GalleryVideoView galleryVideoView = this.mVideoView;
                if (galleryVideoView == null) {
                    return;
                }
                galleryVideoView.removeCallbacks(this.mRemoveRunnable);
                this.mVideoView.postDelayed(this.mRemoveRunnable, 500L);
                return;
            }
            PhotoPageImageItem.this.mPhotoView.setAlpha(1.0f);
            PhotoPageImageItem.this.mPhotoView.setVisibility(0);
            GalleryVideoView galleryVideoView2 = this.mVideoView;
            if (galleryVideoView2 == null) {
                return;
            }
            galleryVideoView2.removeCallbacks(this.mRemoveRunnable);
            removeVideoView("hideVideoView without anim");
        }

        public final void removeVideoView(String str) {
            if (this.mVideoView != null) {
                DefaultLogger.d("PhotoPageImageItem", "MotionPhoto removeVideoView %s", str);
                this.mPendingStop = false;
                if (this.mVideoView.isPlaying()) {
                    this.mVideoView.stopPlayback();
                }
                this.mVideoView.setOnCompletionListener(null);
                this.mVideoView.setOnErrorListener(null);
                this.mVideoView.setOnPreparedListener(null);
                this.mVideoView.removeCallbacks(this.mRemoveRunnable);
                this.mVideoView.removeCallbacks(this.mStartRunnable);
                this.mVideoView.post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageImageItem.MotionPhotoManager.1
                    {
                        MotionPhotoManager.this = this;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        MotionPhotoManager motionPhotoManager = MotionPhotoManager.this;
                        PhotoPageImageItem.this.removeView(motionPhotoManager.mVideoView);
                        MotionPhotoManager.this.mVideoView = null;
                    }
                });
                PhotoPageImageItem.this.mPhotoView.clearAnimation();
                PhotoPageImageItem.this.mPhotoView.setVisibility(0);
                PhotoPageImageItem.this.mPhotoView.setInterceptTouch(false);
                PhotoPageImageItem.this.mPhotoView.removeCallbacks(this.mAutoStopRunnable);
            }
        }

        public final void stopPlayback(boolean z) {
            DefaultLogger.d("PhotoPageImageItem", "MotionPhoto stopPlayback");
            GalleryVideoView galleryVideoView = this.mVideoView;
            if (galleryVideoView != null && galleryVideoView.isPlaying()) {
                this.mVideoView.stopPlayback();
                hideVideoView(z);
                return;
            }
            removeVideoView("stopPlayback");
        }

        public final boolean isItemValid() {
            BaseDataItem baseDataItem;
            return ExtraPhotoSDK.isDeviceSupportMotionPhoto(PhotoPageImageItem.this.getContext()) && (baseDataItem = PhotoPageImageItem.this.mDataItem) != null && !TextUtils.isEmpty(baseDataItem.getOriginalPath()) && PhotoPageImageItem.this.mDataItem.isMotionPhoto() && PhotoPageImageItem.this.mDataItem.getMotionOffset() > 0;
        }

        public void release() {
            this.mHasTransition = false;
            removeVideoView("release");
        }

        public boolean onTouch(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                this.mPlayable = isItemValid();
            }
            if (!this.mPlayable) {
                return false;
            }
            int action = motionEvent.getAction();
            if (action == 0) {
                this.mHasPerformedLongPress = false;
                this.mPressed = true;
                checkForLongClick();
                return true;
            }
            if (action == 1 || action == 3) {
                this.mPressed = false;
                if (this.mHasPerformedLongPress) {
                    DefaultLogger.d("PhotoPageImageItem", "MotionPhoto long press cancel");
                    stopPlayback(true);
                }
                this.mHasPerformedLongPress = false;
                removeLongPressCallback();
            }
            return true;
        }

        public final void checkForLongClick() {
            if (this.mPendingCheckForLongPress == null) {
                this.mPendingCheckForLongPress = new CheckForLongPress();
            }
            PhotoPageImageItem.this.postDelayed(this.mPendingCheckForLongPress, 300L);
        }

        public final void removeLongPressCallback() {
            CheckForLongPress checkForLongPress = this.mPendingCheckForLongPress;
            if (checkForLongPress != null) {
                PhotoPageImageItem.this.removeCallbacks(checkForLongPress);
            }
        }

        public final void performLongClick() {
            DefaultLogger.d("PhotoPageImageItem", "MotionPhoto performLongClick");
            if (startPlay(false, true)) {
                SamplingStatHelper.recordCountEvent("motion_photo", "motion_photo_play_press");
                TrackController.trackClick("403.11.4.1.11156", "403.11.0.1.11151");
            }
        }

        /* loaded from: classes2.dex */
        public final class CheckForLongPress implements Runnable {
            public CheckForLongPress() {
                MotionPhotoManager.this = r1;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (!MotionPhotoManager.this.mPressed || PhotoPageImageItem.this.getParent() == null || PhotoPageImageItem.this.mCheckManager.inAction() || PhotoPageImageItem.this.mPhotoView.isGestureOperating() || PhotoPageImageItem.this.mPhotoView.getScale() < 1.0f) {
                    return;
                }
                MotionPhotoManager.this.performLongClick();
                MotionPhotoManager.this.mHasPerformedLongPress = true;
            }
        }
    }
}
