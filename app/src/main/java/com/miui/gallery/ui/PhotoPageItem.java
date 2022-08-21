package com.miui.gallery.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.MessageQueue;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewKt;
import androidx.tracing.Trace;
import com.android.internal.WindowInsetsCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.OnBackgroundAlphaChangedListener;
import com.github.chrisbanes.photoview.OnExitListener;
import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.github.chrisbanes.photoview.OnPhotoViewDragDownOutListener;
import com.github.chrisbanes.photoview.OnPhotoViewTransitionListener;
import com.github.chrisbanes.photoview.OnRotateListener;
import com.github.chrisbanes.photoview.OnScaleChangeListener;
import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.TransitionListener;
import com.miui.gallery.BaseConfig$ScreenConfig;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.compat.view.ViewCompat;
import com.miui.gallery.error.BaseErrorCodeTranslator;
import com.miui.gallery.error.core.ErrorActionCallback;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.error.core.ErrorTip;
import com.miui.gallery.error.core.ErrorTranslateCallback;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.RegionConfig;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.ktx.DisplayKt;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.model.MediaItem;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.ProcessingMedia;
import com.miui.gallery.sdk.SyncStatus;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.assist.DownloadItemStatus;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AlertDialogFragment;
import com.miui.gallery.ui.DeletionTask;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.ui.PhotoPageDisplayHelper;
import com.miui.gallery.ui.PhotoPageItem;
import com.miui.gallery.ui.PhotoPagerHelper;
import com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseFeatureUtil;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BulkDownloadHelper;
import com.miui.gallery.util.DecodeInfoHelper;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.IdleUITaskHelper;
import com.miui.gallery.util.IncompatibleMediaType;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.RecyclerLayoutCache;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.SpecialTypeMediaUtils;
import com.miui.gallery.util.TalkBackUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.VideoPlayerCompat;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingProgressListener;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.glide.CloudImageLoader;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.photoview.ItemViewInfo;
import com.miui.gallery.util.photoview.TrimMemoryCallback;
import com.miui.gallery.view.animation.AnimationListenerAdapter;
import com.miui.gallery.widget.CircleStrokeProgressBar;
import com.miui.gallery.widget.GalleryDialogFragment;
import com.miui.gallery.widget.slip.ISlipAnimView;
import com.miui.mediaeditor.api.MediaEditorApiHelper;
import com.nexstreaming.nexeditorsdk.nexClip;
import com.xiaomi.miai.api.StatusCode;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import miuix.view.animation.CubicEaseInInterpolator;
import miuix.view.animation.CubicEaseOutInterpolator;
import miuix.view.animation.QuadraticEaseInOutInterpolator;

/* loaded from: classes2.dex */
public abstract class PhotoPageItem extends RelativeLayout implements ISlipAnimView, OnMatrixChangedListener, TrimMemoryCallback, PhotoPageDisplayHelper.ResourceCallback, DefaultLifecycleObserver {
    public static final Interpolator ZOOM_INTERPOLATOR = new QuadraticEaseInOutInterpolator();
    public static int sMenuBarHeight = -1;
    public static int sStatusBarHeight = -1;
    public static int sStrokeColor = Integer.MIN_VALUE;
    public boolean isActionBarVisible;
    public boolean isAnimEntering;
    public boolean isAnimExiting;
    public boolean isCacheImageLoading;
    public boolean isFromCamera;
    public boolean isInitialized;
    public boolean isPaused;
    public boolean isSelected;
    public ImageLoadParams mCacheItem;
    public CheckManager mCheckManager;
    public BaseDataItem mDataItem;
    public DownloadManager mDownloadManager;
    public ErrorDrawableManager mErrorDrawableManager;
    public OnImageLoadFinishListener mExternalCacheLoadCallback;
    public OnImageLoadFinishListener mExternalImageLoadCallback;
    public IdleHandler mIdleHandler;
    public AsyncTask mInitTask;
    public boolean mIsActionBarFirstInit;
    public int mMemoryTrimFlags;
    public NoStoragePermissionTipView mNoStoragePermissionView;
    public PhotoPagerHelper.OnPhotoDeleteListener mOnDeleteListener;
    public PhotoPagerHelper.OnRotateListener mOnRotateListener;
    public OnSpecialTypeEnterListener mOnSpecialTypeEnterListener;
    public PhotoPageDisplayHelper mPhotoPageDisplayHelper;
    public PhotoPageAdapter.PhotoPageInteractionListener mPhotoPageInteractionListener;
    public PhotoView mPhotoView;
    public SpecialTypeEnterManager mSpecialTypeEnterManager;
    public SpecialTypeManager mSpecialTypeManager;
    public WarnTipView mTipView;
    public TrimMemoryCallback mTrimMemoryCallback;
    public boolean mUseSlipModeV2;

    /* loaded from: classes2.dex */
    public interface OnImageLoadFinishListener {
        void onImageLoadFinish(String str, boolean z, Bitmap bitmap);
    }

    /* loaded from: classes2.dex */
    public interface OnSpecialTypeEnterListener {
        void onEnterClick(BaseDataItem baseDataItem, long j);
    }

    /* loaded from: classes2.dex */
    public interface PhotoTransitionListener extends TransitionListener {
    }

    /* loaded from: classes2.dex */
    public interface TipDetailDisplay<P> {
        void display(Context context, P p);

        /* renamed from: getParams */
        P mo1549getParams();
    }

    /* loaded from: classes2.dex */
    public interface TipView {
        boolean hideTip(boolean z, Object obj);

        <P> boolean showTip(int i, boolean z, TipDetailDisplay<P> tipDetailDisplay, Object obj);
    }

    public void doOnMatrixChanged(RectF rectF) {
    }

    public ProcessingMedia getProcessingMedia() {
        return null;
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public boolean isMediaInProcessing() {
        return false;
    }

    public boolean needRegionDecode() {
        return true;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
    }

    public void onActivityTransition() {
    }

    public void onPageScrollDragging() {
    }

    public void onPageScrollIdle() {
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onStop(LifecycleOwner lifecycleOwner) {
    }

    public void setProcessingMedia(ProcessingMedia processingMedia) {
    }

    public PhotoPageItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mInitTask = null;
        this.mIsActionBarFirstInit = true;
        if (sStrokeColor == Integer.MIN_VALUE) {
            sStrokeColor = getResources().getColor(R.color.black_15_transparent);
        }
        this.mPhotoPageDisplayHelper = new PhotoPageDisplayHelper(Glide.with(this), this);
    }

    public PhotoView getPhotoView() {
        return this.mPhotoView;
    }

    public void setPhotoPageCallback(PhotoPageAdapter.PhotoPageInteractionListener photoPageInteractionListener) {
        this.mPhotoPageInteractionListener = photoPageInteractionListener;
    }

    public ImageLoadParams getCacheItem() {
        return this.mCacheItem;
    }

    public final void setCacheItem(ImageLoadParams imageLoadParams, OnImageLoadFinishListener onImageLoadFinishListener) {
        this.mCacheItem = imageLoadParams;
        if (imageLoadParams != null) {
            this.isCacheImageLoading = true;
            this.mExternalCacheLoadCallback = onImageLoadFinishListener;
            displayCacheImage(imageLoadParams);
        }
    }

    public void setIsFromCamera(boolean z) {
        this.isFromCamera = z;
    }

    public final void displayCacheImage(ImageLoadParams imageLoadParams) {
        String path = imageLoadParams.getPath();
        if (Scheme.ofUri(path) == Scheme.UNKNOWN) {
            path = Scheme.FILE.wrap(path);
            imageLoadParams = new ImageLoadParams.Builder().cloneFrom(imageLoadParams).setFilePath(path).build();
        }
        if (imageLoadParams.isFromCamera()) {
            DecodeInfoHelper.DecodeInfo decodeInfo = new DecodeInfoHelper.DecodeInfo();
            decodeInfo.mediaUri = Uri.parse(path);
            decodeInfo.width = imageLoadParams.getImageWidth();
            decodeInfo.height = imageLoadParams.getImageHeight();
            decodeInfo.lastModified = System.currentTimeMillis();
            DecodeInfoHelper.getInstance().put(decodeInfo);
        }
        doDisplayCache(path, getCacheRequestOptions(imageLoadParams), imageLoadParams.isFromCamera());
    }

    public RequestOptions getCacheRequestOptions(ImageLoadParams imageLoadParams) {
        RequestOptions requestOptions = imageLoadParams.getRequestOptions();
        if (requestOptions == null) {
            return GlideOptions.microThumbOf(imageLoadParams.getFileLength()).secretKey(imageLoadParams.getSecretKey()).decodeRegion(RegionConfig.of(imageLoadParams.getRegionRectF())).mo974priority(Priority.IMMEDIATE).mo971override(imageLoadParams.getTargetSize().getWidth(), imageLoadParams.getTargetSize().getHeight());
        }
        if (requestOptions.isLocked()) {
            return requestOptions.clone().mo974priority(Priority.IMMEDIATE);
        }
        return requestOptions.mo974priority(Priority.IMMEDIATE);
    }

    public TrimMemoryCallback getTrimMemoryCallback() {
        return this.mTrimMemoryCallback;
    }

    public void setTrimMemoryCallback(TrimMemoryCallback trimMemoryCallback) {
        this.mTrimMemoryCallback = trimMemoryCallback;
    }

    @Override // com.miui.gallery.util.photoview.TrimMemoryCallback
    public void onTrimMemory(int i) {
        DefaultLogger.d("PhotoPageItem", "onTrimMemory flag: %d, before: %d", Integer.valueOf(i), Integer.valueOf(this.mMemoryTrimFlags));
        if (this.mMemoryTrimFlags == 0) {
            BaseDataItem baseDataItem = this.mDataItem;
            DefaultLogger.d("PhotoPageItem", "trimMemory: %s", baseDataItem != null ? Long.valueOf(baseDataItem.getKey()) : Integer.toHexString(hashCode()));
            this.mPhotoPageDisplayHelper.onTrimMemory();
        }
        this.mMemoryTrimFlags = i | this.mMemoryTrimFlags;
    }

    @Override // com.miui.gallery.util.photoview.TrimMemoryCallback
    public void onStopTrimMemory(int i) {
        DefaultLogger.d("PhotoPageItem", "onStopTrimMemory flag: %d, before: %d", Integer.valueOf(i), Integer.valueOf(this.mMemoryTrimFlags));
        int i2 = this.mMemoryTrimFlags;
        if ((i2 & i) > 0) {
            int i3 = (~i) & i2;
            this.mMemoryTrimFlags = i3;
            if (i3 != 0) {
                return;
            }
            BaseDataItem baseDataItem = this.mDataItem;
            DefaultLogger.d("PhotoPageItem", "reloadData: %s", baseDataItem != null ? Long.valueOf(baseDataItem.getKey()) : Integer.toHexString(hashCode()));
            refreshItem();
        }
    }

    public void clearTrimMemoryFlag() {
        DefaultLogger.d("PhotoPageItem", "clearTrimMemoryFlag");
        this.mMemoryTrimFlags = 0;
    }

    public void setSpecialEnterState(boolean z, boolean z2) {
        SpecialTypeEnterManager specialTypeEnterManager = this.mSpecialTypeEnterManager;
        if (specialTypeEnterManager != null) {
            if (z) {
                specialTypeEnterManager.show(z2);
            } else {
                specialTypeEnterManager.hide(z2);
            }
        }
    }

    public void resetDefaultPhotoStatus() {
        this.mPhotoView.resetDefaultPhotoStatus();
    }

    public final boolean isUnsupportedMedia() {
        BaseDataItem baseDataItem = this.mDataItem;
        return baseDataItem != null && IncompatibleMediaType.isUnsupportedMediaType(baseDataItem.getMimeType()) && !TextUtils.isEmpty(this.mDataItem.getOriginalPath());
    }

    public void onImageLoadFinish(ErrorCode errorCode) {
        String str;
        Drawable drawable;
        String str2 = null;
        boolean z = true;
        if (this.mPhotoView.getDrawable() == null) {
            Resources resources = getResources();
            if (errorCode != null && errorCode != ErrorCode.NO_ERROR) {
                if (errorCode == ErrorCode.DECODE_ERROR) {
                    if (isUnsupportedMedia()) {
                        str2 = IncompatibleMediaType.getUnsupporedMediaViewTip(getContext(), this.mDataItem.getMimeType());
                        drawable = resources.getDrawable(R.drawable.icon_empty_photo);
                    } else {
                        BaseDataItem baseDataItem = this.mDataItem;
                        if (baseDataItem == null || !TextUtils.isEmpty(baseDataItem.getOriginalPath())) {
                            str = resources.getString(R.string.error_decode_tip);
                            str2 = str;
                            drawable = null;
                        }
                    }
                } else {
                    str2 = resources.getString(R.string.error_download_tip);
                    drawable = resources.getDrawable(R.drawable.icon_empty_photo);
                }
            }
            drawable = null;
        } else {
            ErrorCode errorCode2 = ErrorCode.DECODE_ERROR;
            if (errorCode == errorCode2 && isUnsupportedMedia()) {
                str2 = IncompatibleMediaType.getUnsupporedMediaViewTip(getContext(), this.mDataItem.getMimeType());
                drawable = getResources().getDrawable(R.drawable.icon_empty_photo);
            } else {
                if (errorCode == errorCode2) {
                    str = getResources().getString(R.string.error_decode_tip);
                } else {
                    z = false;
                    str = null;
                }
                BaseDataItem baseDataItem2 = this.mDataItem;
                if (baseDataItem2 != null) {
                    DefaultLogger.d("PhotoPageItem", "onImageLoadFinish: [%s], host [%s]", baseDataItem2.getOriginalPath(), toString());
                }
                str2 = str;
                drawable = null;
            }
        }
        if (z) {
            this.mErrorDrawableManager.setSource(this.mCacheItem, this.mDataItem);
            this.mErrorDrawableManager.setTip(str2);
            this.mErrorDrawableManager.setIcon(drawable);
            this.mErrorDrawableManager.work();
        }
    }

    public void onCacheImageLoadFinish(boolean z) {
        this.isCacheImageLoading = false;
        if (!z) {
            swapItem(this.mDataItem);
        }
    }

    public void updateFeatures() {
        PhotoView photoView = this.mPhotoView;
        if (photoView != null && this.mDataItem != null) {
            photoView.setZoomable(isSupportZoom());
            this.mPhotoView.setRotatable(!this.mDataItem.isVideo());
        }
        updateSpecialTypeIndicator();
        SpecialTypeEnterManager specialTypeEnterManager = this.mSpecialTypeEnterManager;
        if (specialTypeEnterManager != null) {
            specialTypeEnterManager.update(false);
        }
    }

    public boolean isSupportZoom() {
        return !this.mDataItem.isVideo();
    }

    public void updateSpecialTypeIndicator() {
        int parseSpecialTypeDescriptionRes;
        BaseDataItem baseDataItem = this.mDataItem;
        if (baseDataItem != null && baseDataItem.isSpecialTypeRecognized() && (parseSpecialTypeDescriptionRes = SpecialTypeMediaUtils.parseSpecialTypeDescriptionRes(this.mDataItem.getSpecialTypeFlags())) > 0) {
            this.mSpecialTypeManager.setTypeIndicatorText(getContext().getString(parseSpecialTypeDescriptionRes));
            this.mSpecialTypeManager.showIndicator(false);
            return;
        }
        this.mSpecialTypeManager.setTypeIndicatorText(null);
        this.mSpecialTypeManager.hideIndicator(false);
    }

    public void updateSpecialTypeEnterView(boolean z, boolean z2) {
        SpecialTypeEnterManager specialTypeEnterManager = this.mSpecialTypeEnterManager;
        if (specialTypeEnterManager != null) {
            this.isActionBarVisible = z2;
            specialTypeEnterManager.update(z);
        }
    }

    public void setSpecialTypeEnterViewVisible(boolean z) {
        SpecialTypeEnterManager specialTypeEnterManager = this.mSpecialTypeEnterManager;
        if (specialTypeEnterManager != null) {
            specialTypeEnterManager.setVisible(z);
        }
    }

    public void startSpecialTypeEnterAnim(boolean z) {
        SpecialTypeEnterManager specialTypeEnterManager = this.mSpecialTypeEnterManager;
        if (specialTypeEnterManager != null) {
            specialTypeEnterManager.startAnim(z);
        }
    }

    public void setSpecialTypeEnterViewCache(RecyclerLayoutCache recyclerLayoutCache) {
        SpecialTypeEnterManager specialTypeEnterManager = this.mSpecialTypeEnterManager;
        if (specialTypeEnterManager != null) {
            specialTypeEnterManager.setSpecialTypeEnterViewCache(recyclerLayoutCache);
        }
    }

    public void refreshItem() {
        swapItem(this.mDataItem);
    }

    public void swapItem(BaseDataItem baseDataItem, ImageLoadParams imageLoadParams) {
        if (imageLoadParams != this.mCacheItem) {
            if (imageLoadParams == null) {
                this.isCacheImageLoading = false;
                if (baseDataItem != null) {
                    baseDataItem.getKey();
                    this.mCacheItem.getKey();
                }
            }
            setCacheItem(imageLoadParams, null);
        }
        swapItem(baseDataItem);
    }

    public void swapItem(BaseDataItem baseDataItem) {
        BaseDataItem baseDataItem2 = this.mDataItem;
        if (baseDataItem2 != null && (baseDataItem == null || !baseDataItem.equals(baseDataItem2))) {
            this.mPhotoView.resetMatrix();
        }
        this.mDataItem = baseDataItem;
        if (this.isCacheImageLoading) {
            return;
        }
        if (baseDataItem != null) {
            if (this.mCacheItem == null) {
                String originalPath = baseDataItem.getOriginalPath();
                if (TextUtils.isEmpty(originalPath)) {
                    originalPath = this.mDataItem.getThumnailPath();
                }
                if (!TextUtils.isEmpty(originalPath)) {
                    ImageLoadParams build = new ImageLoadParams.Builder().setKey(this.mDataItem.getKey()).setFilePath(originalPath).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize).setMimeType(this.mDataItem.getMimeType()).setSecretKey(this.mDataItem.getSecretKey()).setFileLength(this.mDataItem.getSize()).setCreateTime(this.mDataItem.getCreateTime()).setLocation(this.mDataItem.getLocation()).setImageWidth(this.mDataItem.getWidth()).setImageHeight(this.mDataItem.getHeight()).build();
                    this.mCacheItem = build;
                    displayCacheImage(build);
                }
            }
            Trace.beginSection("executeInitTask");
            executeInitTask();
            Trace.endSection();
            removeIdleHandler();
            addIdleHandler();
        }
        updateFeatures();
    }

    public final void removeIdleHandler() {
        try {
            if (this.mIdleHandler == null) {
                return;
            }
            ThreadManager.getMainHandler().getLooper().getQueue().removeIdleHandler(this.mIdleHandler);
            this.mIdleHandler = null;
            DefaultLogger.d("PhotoPageItem", "removeIdleHandler [%s] =>", Integer.valueOf(hashCode()));
        } catch (Exception e) {
            DefaultLogger.e("PhotoPageItem", e);
        }
    }

    public final void addIdleHandler() {
        try {
            this.mIdleHandler = new IdleHandler(this, null);
            ThreadManager.getMainHandler().getLooper().getQueue().addIdleHandler(this.mIdleHandler);
            DefaultLogger.d("PhotoPageItem", "addIdleHandler [%s] =>", Integer.valueOf(hashCode()));
        } catch (Exception e) {
            DefaultLogger.e("PhotoPageItem", e);
        }
    }

    /* loaded from: classes2.dex */
    public class IdleHandler implements MessageQueue.IdleHandler {
        public IdleHandler() {
            PhotoPageItem.this = r1;
        }

        public /* synthetic */ IdleHandler(PhotoPageItem photoPageItem, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // android.os.MessageQueue.IdleHandler
        public boolean queueIdle() {
            DefaultLogger.d("PhotoPageItem", "queueIdle [%s] =>", Integer.valueOf(PhotoPageItem.this.hashCode()));
            Trace.beginSection("setContentDescription");
            PhotoPageItem photoPageItem = PhotoPageItem.this;
            photoPageItem.setContentDescription(photoPageItem.getContentDescriptionForTalkBack());
            Trace.endSection();
            return false;
        }
    }

    public final void displayImage(BaseDataItem baseDataItem) {
        Pair<String, Boolean> displayImageUri;
        if (this.mMemoryTrimFlags == 0) {
            if (((this.isCacheImageLoading || !(this.mCacheItem == null || this.mPhotoView.getDrawable() == null)) && isMediaInProcessing()) || (displayImageUri = getDisplayImageUri(baseDataItem)) == null) {
                return;
            }
            if (this.isFromCamera) {
                DecodeInfoHelper.DecodeInfo decodeInfo = new DecodeInfoHelper.DecodeInfo();
                decodeInfo.fileUri = Uri.parse((String) displayImageUri.first);
                decodeInfo.width = baseDataItem.getWidth();
                decodeInfo.height = baseDataItem.getHeight();
                decodeInfo.lastModified = baseDataItem.getCreateTime();
                DecodeInfoHelper.getInstance().put(decodeInfo);
            }
            doDisplayImage((String) displayImageUri.first, getRequestOptions(baseDataItem, ((Boolean) displayImageUri.second).booleanValue()));
        }
    }

    public static Pair<String, Boolean> getDisplayImageUri(BaseDataItem baseDataItem) {
        if (baseDataItem == null) {
            return null;
        }
        boolean z = false;
        String originalPath = baseDataItem.getOriginalPath();
        if (!isPathValid(originalPath)) {
            originalPath = baseDataItem.getThumnailPath();
        }
        if (!isPathValid(originalPath)) {
            originalPath = baseDataItem.getMicroPath();
            z = true;
        }
        if (!TextUtils.isEmpty(originalPath) && ((z || isPathValid(originalPath)) && originalPath.startsWith(File.separator))) {
            originalPath = Scheme.FILE.wrap(originalPath);
        }
        if (originalPath != null) {
            return new Pair<>(originalPath, Boolean.valueOf(z));
        }
        return null;
    }

    public Size getImageSize(boolean z) {
        if (z || (!BaseFeatureUtil.isDisableFastBlur() && isBlurredForProcessing())) {
            return Config$ThumbConfig.get().sMicroTargetSize;
        }
        Point displaySize = DisplayKt.getDisplaySize(this);
        if (displaySize.x != 0 && displaySize.y != 0) {
            return new Size(displaySize.x, displaySize.y);
        }
        return new Size(BaseConfig$ScreenConfig.getScreenWidth(), BaseConfig$ScreenConfig.getScreenHeight());
    }

    public final void executeInitTask() {
        this.isInitialized = false;
        AsyncTask asyncTask = this.mInitTask;
        if (asyncTask != null) {
            asyncTask.cancel(false);
        }
        this.mInitTask = new InitTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public void onPostInitialized() {
        this.isInitialized = true;
        if (canDoSelected()) {
            doOnSelected(false, false, true);
        }
    }

    public final void correctVideoMetaData() {
        RectF displayRect;
        BaseDataItem baseDataItem = this.mDataItem;
        if (baseDataItem != null && BaseFileMimeUtil.isVideoFromMimeType(baseDataItem.getMimeType())) {
            if (this.mDataItem.getOrientation() != 0) {
                DefaultLogger.d("PhotoPageItem", "BaseDataItem has already had orientation information");
            }
            PhotoView photoView = this.mPhotoView;
            if (photoView == null || (displayRect = photoView.getDisplayRect()) == null) {
                return;
            }
            float f = displayRect.right - displayRect.left;
            float f2 = displayRect.bottom - displayRect.top;
            int width = this.mDataItem.getWidth();
            int height = this.mDataItem.getHeight();
            if (width <= 0 || height <= 0) {
                this.mDataItem.setDisplayHeight((int) f2);
                this.mDataItem.setDisplayWidth((int) f);
            } else if ((f >= f2 && width >= height) || (f <= f2 && width <= height)) {
                correctVideoDataItemOrientation(false);
            } else {
                correctVideoDataItemOrientation(true);
            }
        }
    }

    public final void correctVideoDataItemOrientation(boolean z) {
        if (!z) {
            return;
        }
        BaseDataItem baseDataItem = this.mDataItem;
        if (baseDataItem instanceof MediaItem) {
            baseDataItem.mo1096setOrientation(90);
        } else {
            baseDataItem.mo1096setOrientation(6);
        }
    }

    public void onOrientationChanged(int i, int i2) {
        SpecialTypeEnterManager specialTypeEnterManager = this.mSpecialTypeEnterManager;
        if (specialTypeEnterManager != null) {
            specialTypeEnterManager.onOrientationChanged(i, i2);
        }
    }

    /* loaded from: classes2.dex */
    public static class InitTask extends AsyncTask<Void, Void, BaseDataItem> {
        public long mClock;
        public WeakReference<PhotoPageItem> mPhotoPageItem;

        public InitTask(PhotoPageItem photoPageItem) {
            this.mPhotoPageItem = new WeakReference<>(photoPageItem);
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            super.onPreExecute();
            DefaultLogger.d("PhotoPageItem", "InitTask onPreExecute =>");
        }

        @Override // android.os.AsyncTask
        public BaseDataItem doInBackground(Void... voidArr) {
            WeakReference<PhotoPageItem> weakReference;
            if (isCancelled() || (weakReference = this.mPhotoPageItem) == null || weakReference.get() == null || this.mPhotoPageItem.get().mDataItem == null) {
                return null;
            }
            this.mClock = System.currentTimeMillis();
            BaseDataItem baseDataItem = this.mPhotoPageItem.get().mDataItem;
            baseDataItem.reloadCache();
            return baseDataItem;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(BaseDataItem baseDataItem) {
            WeakReference<PhotoPageItem> weakReference;
            DefaultLogger.d("PhotoPageItem", "init costs %d, task is cancelled: %b", Long.valueOf(System.currentTimeMillis() - this.mClock), Boolean.valueOf(isCancelled()));
            if (isCancelled() || (weakReference = this.mPhotoPageItem) == null || weakReference.get() == null || baseDataItem == null) {
                return;
            }
            DefaultLogger.d("PhotoPageItem", "InitTask onPostExecute =>");
            this.mPhotoPageItem.get().displayImage(baseDataItem);
            this.mPhotoPageItem.get().onPostInitialized();
        }

        @Override // android.os.AsyncTask
        public void onCancelled() {
            super.onCancelled();
            WeakReference<PhotoPageItem> weakReference = this.mPhotoPageItem;
            if (weakReference != null) {
                weakReference.clear();
                this.mPhotoPageItem = null;
            }
        }
    }

    public void doDisplayCache(String str, RequestOptions requestOptions, boolean z) {
        this.mPhotoPageDisplayHelper.displayPreviewImage(GalleryModel.of(str).setIsCameraPreview(z), requestOptions);
    }

    public void doDisplayImage(String str, RequestOptions requestOptions) {
        this.mPhotoPageDisplayHelper.displayImage(GalleryModel.of(str).setIsJustEditExported(MediaEditorApiHelper.isJustEditExportedPath(str)), requestOptions);
    }

    public RequestOptions getRequestOptions(BaseDataItem baseDataItem, boolean z) {
        GlideOptions microThumbOf;
        Size imageSize = getImageSize(z);
        if (!z) {
            microThumbOf = GlideOptions.bigPhotoOf(baseDataItem.getSize());
        } else {
            microThumbOf = GlideOptions.microThumbOf(baseDataItem.getSize());
        }
        GlideOptions mo978skipMemoryCache = microThumbOf.secretKey(baseDataItem.getSecretKey()).mo978skipMemoryCache(!isPagerSelected());
        return imageSize != null ? mo978skipMemoryCache.mo971override(imageSize.getWidth(), imageSize.getHeight()) : mo978skipMemoryCache;
    }

    public static boolean isPathValid(String str) {
        return !TextUtils.isEmpty(str);
    }

    public BaseDataItem getDataItem() {
        return this.mDataItem;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mPhotoView = (PhotoView) findViewById(R.id.photoview);
        this.mDownloadManager = createDownloadManager();
        this.mSpecialTypeManager = new SpecialTypeManager();
        this.mCheckManager = createCheckManager();
        this.mErrorDrawableManager = new ErrorDrawableManager();
        this.mSpecialTypeEnterManager = new SpecialTypeEnterManager(this, null);
        this.mTipView = new WarnTipView(this, this, null);
        this.mNoStoragePermissionView = new NoStoragePermissionTipView(this, R.layout.special_type_enter_layout, null);
        configPhotoView(this.mPhotoView);
    }

    public final void configPhotoView(PhotoView photoView) {
        photoView.setZoomInterpolator(ZOOM_INTERPOLATOR);
        photoView.setZoomDuration(StatusCode.BAD_REQUEST);
        photoView.setZoomDurationLengthenFactor(1.5f);
    }

    public CheckManager createCheckManager() {
        return new CheckManager();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        LifecycleOwner findViewTreeLifecycleOwner = ViewKt.findViewTreeLifecycleOwner(this);
        if (findViewTreeLifecycleOwner != null) {
            findViewTreeLifecycleOwner.getLifecycle().addObserver(this);
        }
        this.mPhotoPageDisplayHelper.attach();
        super.onAttachedToWindow();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        LifecycleOwner findViewTreeLifecycleOwner = ViewKt.findViewTreeLifecycleOwner(this);
        if (findViewTreeLifecycleOwner != null) {
            findViewTreeLifecycleOwner.getLifecycle().removeObserver(this);
        }
        doRelease();
        super.onDetachedFromWindow();
    }

    public void doRelease() {
        Trace.beginSection("doRelease");
        this.mPhotoPageDisplayHelper.detach();
        Trace.beginSection("onTrimMemory");
        this.mPhotoPageDisplayHelper.onTrimMemory();
        Trace.endSection();
        removeIdleHandler();
        removeOnMatrixChangeListener(this);
        PhotoView photoView = this.mPhotoView;
        if (photoView != null) {
            photoView.release();
        }
        this.mDownloadManager.release();
        this.mSpecialTypeManager.release();
        this.mSpecialTypeEnterManager.release();
        this.mCheckManager.endCheck();
        AsyncTask asyncTask = this.mInitTask;
        if (asyncTask != null) {
            asyncTask.cancel(false);
            this.mInitTask = null;
        }
        this.mErrorDrawableManager.shutDown();
        this.mCacheItem = null;
        this.mDataItem = null;
        setContentDescription(null);
        this.mPhotoPageInteractionListener = null;
        this.mExternalCacheLoadCallback = null;
        this.mExternalImageLoadCallback = null;
        this.isSelected = false;
        this.isInitialized = false;
        this.isActionBarVisible = false;
        this.mIsActionBarFirstInit = true;
        this.isAnimEntering = false;
        this.isAnimExiting = false;
        this.isCacheImageLoading = false;
        this.mMemoryTrimFlags = 0;
        this.isPaused = false;
        setPhotoPageCallback(null);
        Trace.endSection();
    }

    public boolean isInitialized() {
        return this.isInitialized;
    }

    public boolean isAnimEntering() {
        return this.isAnimEntering;
    }

    public boolean isAnimExiting() {
        return this.isAnimExiting;
    }

    public void onSelecting() {
        SpecialTypeEnterManager specialTypeEnterManager = this.mSpecialTypeEnterManager;
        if (specialTypeEnterManager != null) {
            specialTypeEnterManager.onSelecting();
        }
    }

    public final void onSelected(boolean z) {
        if (!this.isSelected) {
            this.isSelected = true;
            if (canDoSelected()) {
                doOnSelected(z, false, false);
            }
            addOnMatrixChangeListener(this);
        }
    }

    public final boolean canDoSelected() {
        boolean z = isPagerSelected() && !isAnimEntering() && isInitialized();
        if (!z && isPagerSelected()) {
            DefaultLogger.d("PhotoPageItem", "canDoSelected %s, isAnimEntering %s, isInitialized %s", Boolean.valueOf(z), Boolean.valueOf(isAnimEntering()), Boolean.valueOf(isInitialized()));
        }
        return z;
    }

    public void doOnSelected(boolean z, boolean z2, boolean z3) {
        if (z && this.mCheckManager.inAction()) {
            this.mDownloadManager.onUnSelected();
            this.mSpecialTypeManager.onUnSelected();
        } else {
            this.mDownloadManager.onSelected();
            this.mSpecialTypeManager.onSelected();
        }
        SpecialTypeEnterManager specialTypeEnterManager = this.mSpecialTypeEnterManager;
        if (specialTypeEnterManager != null) {
            specialTypeEnterManager.onSelected();
        }
    }

    public final void onUnSelected(boolean z, int i) {
        if (this.isSelected) {
            this.isSelected = false;
            doOnUnSelected(z, i);
            removeOnMatrixChangeListener(this);
        }
        SpecialTypeEnterManager specialTypeEnterManager = this.mSpecialTypeEnterManager;
        if (specialTypeEnterManager != null) {
            specialTypeEnterManager.onUnSelected(i);
        }
    }

    public void doOnUnSelected(boolean z, int i) {
        Trace.beginSection("itemOnUnSelected");
        Trace.beginSection("DownloadManagerOnUnSelected");
        this.mDownloadManager.onUnSelected();
        Trace.endSection();
        Trace.beginSection("SpecialTypeOnUnSelected");
        this.mSpecialTypeManager.onUnSelected();
        Trace.endSection();
        Trace.endSection();
    }

    @Override // com.github.chrisbanes.photoview.OnMatrixChangedListener
    public final void onMatrixChanged(RectF rectF) {
        if (isAnimEntering() || isAnimExiting()) {
            return;
        }
        this.mDownloadManager.onMatrixChanged(rectF);
        this.mCheckManager.onMatrixChanged(rectF);
        this.mSpecialTypeManager.onMatrixChanged(rectF);
        this.mTipView.onMatrixChanged(rectF);
        doOnMatrixChanged(rectF);
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public boolean isPagerSelected() {
        return this.isSelected;
    }

    public void setOnImageLoadFinishListener(OnImageLoadFinishListener onImageLoadFinishListener) {
        this.mExternalImageLoadCallback = onImageLoadFinishListener;
    }

    public void setOnViewTapListener(OnViewTapListener onViewTapListener) {
        this.mPhotoView.setOnViewTapListener(onViewTapListener);
    }

    public void setOnExitListener(OnExitListener onExitListener) {
        this.mPhotoView.setOnExitListener(onExitListener);
    }

    public void setOnPhotoViewDragDownOutListener(OnPhotoViewDragDownOutListener onPhotoViewDragDownOutListener) {
        this.mPhotoView.setOnPhotoViewDragDownOutListener(onPhotoViewDragDownOutListener);
    }

    public void setOnScaleChangeListener(OnScaleChangeListener onScaleChangeListener) {
        this.mPhotoView.setOnScaleChangeListener(onScaleChangeListener);
    }

    public void setOnRotateListener(PhotoPagerHelper.OnRotateListener onRotateListener) {
        this.mOnRotateListener = onRotateListener;
    }

    public void addOnMatrixChangeListener(OnMatrixChangedListener onMatrixChangedListener) {
        this.mPhotoView.addOnMatrixChangeListener(onMatrixChangedListener);
    }

    public void removeOnMatrixChangeListener(OnMatrixChangedListener onMatrixChangedListener) {
        this.mPhotoView.removeOnMatrixChangeListener(onMatrixChangedListener);
    }

    public void setOnBackgroundAlphaChangedListener(OnBackgroundAlphaChangedListener onBackgroundAlphaChangedListener) {
        this.mPhotoView.setOnBackgroundAlphaChangedListener(onBackgroundAlphaChangedListener);
    }

    public void setOnDeleteListener(PhotoPagerHelper.OnPhotoDeleteListener onPhotoDeleteListener) {
        this.mOnDeleteListener = onPhotoDeleteListener;
    }

    public void downloadOrigin() {
        if (this.mDataItem != null) {
            this.mDownloadManager.downloadOrigin(true);
        }
    }

    public void setCloudImageLoadingListener(CloudImageLoadingListener cloudImageLoadingListener) {
        this.mDownloadManager.setCloudImageLoadingListener(cloudImageLoadingListener);
    }

    public void setOnSpecialTypeEnterListener(OnSpecialTypeEnterListener onSpecialTypeEnterListener) {
        this.mOnSpecialTypeEnterListener = onSpecialTypeEnterListener;
    }

    /* renamed from: com.miui.gallery.ui.PhotoPageItem$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements PhotoTransitionListener {
        public final /* synthetic */ PhotoTransitionListener val$listener;

        public static /* synthetic */ void $r8$lambda$ASNLCCpKn12L_6UwsxZKFTDbPrI(AnonymousClass1 anonymousClass1) {
            anonymousClass1.lambda$onTransitEnd$0();
        }

        public AnonymousClass1(PhotoTransitionListener photoTransitionListener) {
            PhotoPageItem.this = r1;
            this.val$listener = photoTransitionListener;
        }

        @Override // com.github.chrisbanes.photoview.TransitionListener
        public void onTransitEnd() {
            this.val$listener.onTransitEnd();
            PhotoPageItem.this.isAnimEntering = false;
            if (PhotoPageItem.this.canDoSelected()) {
                IdleUITaskHelper.getInstance().addTask(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageItem$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PhotoPageItem.AnonymousClass1.$r8$lambda$ASNLCCpKn12L_6UwsxZKFTDbPrI(PhotoPageItem.AnonymousClass1.this);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$onTransitEnd$0() {
            PhotoPageItem.this.doOnSelected(false, true, false);
        }

        @Override // com.github.chrisbanes.photoview.TransitionListener
        public void onTransitUpdate(float f) {
            this.val$listener.onTransitUpdate(f);
        }
    }

    public void animEnter(ItemViewInfo itemViewInfo, PhotoTransitionListener photoTransitionListener) {
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(photoTransitionListener);
        this.isAnimEntering = true;
        this.mPhotoView.animEnter(itemViewInfo, anonymousClass1);
    }

    public void animExit(ItemViewInfo itemViewInfo, PhotoTransitionListener photoTransitionListener) {
        this.isAnimExiting = true;
        this.mPhotoView.animExit(itemViewInfo, photoTransitionListener);
        this.mDownloadManager.release();
        this.mSpecialTypeManager.release();
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onStart(LifecycleOwner lifecycleOwner) {
        onStopTrimMemory(2);
    }

    public void onResume(LifecycleOwner lifecycleOwner) {
        this.isPaused = false;
    }

    public void onPause(LifecycleOwner lifecycleOwner) {
        this.isPaused = true;
    }

    /* loaded from: classes2.dex */
    public abstract class AbsPhotoRectAwareManager implements OnRotateListener {
        public boolean isDrawableDisplayInside;
        public boolean isRotating;
        public int mMargin;

        public abstract void adjustLocation(boolean z, RectF rectF, boolean z2);

        public abstract void changeVisibilityForSpecialScene();

        public int getAdjustAnimDuration(boolean z) {
            return z ? 350 : 250;
        }

        @Override // com.github.chrisbanes.photoview.OnRotateListener
        public void onRotate(float f, float f2, float f3, float f4) {
        }

        public AbsPhotoRectAwareManager() {
            PhotoPageItem.this = r1;
        }

        public void onActionModeChanged(boolean z) {
            DefaultLogger.d("PhotoPageItem", "onActionModeChanged %s", Boolean.valueOf(z));
            changeVisibilityForSpecialScene();
        }

        public void onRotateStateChanged(boolean z) {
            DefaultLogger.d("PhotoPageItem", "onRotateStateChanged %s", Boolean.valueOf(z));
            changeVisibilityForSpecialScene();
        }

        public void onScaleInsideChanged(boolean z) {
            DefaultLogger.d("PhotoPageItem", "onScaleInsideChanged %s", Boolean.valueOf(z));
            changeVisibilityForSpecialScene();
        }

        public Animation generateShowAnimation() {
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(new AlphaAnimation(0.0f, 1.0f));
            animationSet.setInterpolator(new CubicEaseOutInterpolator());
            animationSet.setDuration(300L);
            return animationSet;
        }

        public Animation generateHideAnimation() {
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));
            animationSet.setInterpolator(new CubicEaseOutInterpolator());
            animationSet.setDuration(200L);
            return animationSet;
        }

        public boolean isRotating() {
            return this.isRotating;
        }

        public boolean isDrawableDisplayInside() {
            return this.isDrawableDisplayInside;
        }

        public final void onMatrixChanged(RectF rectF) {
            RectF baseDisplayRect;
            boolean z = (rectF == null || (baseDisplayRect = PhotoPageItem.this.mPhotoView.getBaseDisplayRect()) == null || rectF.width() + 5.0f >= baseDisplayRect.width()) ? false : true;
            if (z != this.isDrawableDisplayInside) {
                this.isDrawableDisplayInside = z;
                onScaleInsideChanged(z);
            }
            adjustLocation(PhotoPageItem.this.isActionBarVisible(), rectF, false);
        }

        public Interpolator getAdjustAnimInterpolator(boolean z) {
            return z ? new CubicEaseOutInterpolator() : new CubicEaseInInterpolator();
        }

        public float getMaxTranslationY() {
            return PhotoPageItem.this.getHeight() / 2;
        }

        public float getMaxTranslationX() {
            return PhotoPageItem.this.getWidth() / 2;
        }

        @Override // com.github.chrisbanes.photoview.OnRotateListener
        public final void onRotateBegin(float f) {
            this.isRotating = true;
            onRotateStateChanged(true);
        }

        @Override // com.github.chrisbanes.photoview.OnRotateListener
        public final void onRotateEnd(float f) {
            this.isRotating = false;
            onRotateStateChanged(false);
            if (PhotoPageItem.this.mOnRotateListener != null) {
                PhotoPageItem.this.mOnRotateListener.onRotate(f);
            }
            ArrayMap arrayMap = new ArrayMap(1);
            arrayMap.put("toDegree", String.valueOf(f));
            SamplingStatHelper.recordCountEvent("photo", "manual_rotate", arrayMap);
        }

        public int getVerticalMargin() {
            if (this.mMargin == 0) {
                this.mMargin = PhotoPageItem.this.getContext().getResources().getDimensionPixelSize(R.dimen.download_progress_margin);
            }
            return this.mMargin;
        }

        public int getHorizontalMargin() {
            int verticalMargin;
            int displayCutoutInsetsRight;
            if (PhotoPageItem.this.getResources().getConfiguration().orientation == 2) {
                if (PhotoPageItem.this.isActionBarVisible()) {
                    verticalMargin = getVerticalMargin();
                    displayCutoutInsetsRight = ViewCompat.getSystemWindowInsetRight(PhotoPageItem.this);
                } else {
                    verticalMargin = getVerticalMargin();
                    displayCutoutInsetsRight = WindowInsetsCompat.getDisplayCutoutInsetsRight(PhotoPageItem.this);
                }
                return verticalMargin + displayCutoutInsetsRight;
            }
            return getVerticalMargin();
        }

        public void onSelected() {
            PhotoPageItem.this.mPhotoView.addOnRotateListener(this);
        }

        public void onUnSelected() {
            PhotoPageItem.this.mPhotoView.removeOnRotateListener(this);
        }

        public void release() {
            this.isRotating = false;
            this.isDrawableDisplayInside = false;
        }
    }

    public DownloadManager createDownloadManager() {
        return new DownloadManager();
    }

    /* loaded from: classes2.dex */
    public class DownloadManager extends AbsPhotoRectAwareManager {
        public boolean isUserDownload;
        public float mAdjustX;
        public float mAdjustY;
        public Animator mAnimator;
        public PhotoItemBulkDownloadListener mBulkDownloadListener;
        public ProgressBar mDownloadProgress;
        public View mDownloadRoot;
        public DownloadType mDownloadingType;
        public DownloadErrorDisplay mErrorDisplayer;
        public CloudImageLoadingListener mExternalListener;
        public AsyncTask<Void, Void, DownloadItemStatus> mGetOriginTask;
        public AsyncTask<Void, Void, DownloadItemStatus> mGetThumbnailTask;
        public boolean mHasShowProgress;
        public RequestListener<Bitmap> mRequestListener;
        public final Object mTipToken;

        public static /* synthetic */ void $r8$lambda$goQEwJU9RHspx_8L1BBG1b3wK5c(DownloadManager downloadManager, boolean z, DialogInterface dialogInterface, int i) {
            downloadManager.lambda$considerCompatibility$1(z, dialogInterface, i);
        }

        public static /* synthetic */ void $r8$lambda$vbHSj5N0_1RlXvtPu2F_T9qResQ(DownloadManager downloadManager, boolean z, boolean z2) {
            downloadManager.lambda$considerNetwork$3(z, z2);
        }

        public void doOnCancel(DownloadType downloadType) {
        }

        public void doOnDownloaded(DownloadType downloadType, String str) {
        }

        public void doOnDownloading(DownloadType downloadType, float f) {
        }

        public void doOnLoadFail(DownloadType downloadType, ErrorCode errorCode) {
        }

        public void doOnLoadStart(DownloadType downloadType) {
        }

        public void doOnProgressVisibilityChanged(boolean z) {
        }

        public boolean filterError(ErrorCode errorCode) {
            return false;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public DownloadManager() {
            super();
            PhotoPageItem.this = r1;
            this.mBulkDownloadListener = new PhotoItemBulkDownloadListener();
            this.mTipToken = new Object();
            this.mRequestListener = new RequestListener<Bitmap>() { // from class: com.miui.gallery.ui.PhotoPageItem.DownloadManager.1
                {
                    DownloadManager.this = this;
                }

                @Override // com.bumptech.glide.request.RequestListener
                public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
                    if (obj instanceof GalleryModel) {
                        DownloadManager.this.onLoadFail((DownloadType) ((GalleryModel) obj).getExtras().getParcelable(GalleryPreferences.PrefKeys.SYNC_DOWNLOAD_TYPE), ErrorCode.DECODE_ERROR, null);
                        return false;
                    }
                    return false;
                }

                @Override // com.bumptech.glide.request.RequestListener
                public boolean onResourceReady(Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
                    if (obj instanceof GalleryModel) {
                        DownloadManager.this.onLoaded(bitmap);
                        return false;
                    }
                    return false;
                }
            };
        }

        public final void initDownloadLayout() {
            this.mDownloadRoot = LayoutInflater.from(PhotoPageItem.this.getContext()).inflate(R.layout.photo_page_download_progress, (ViewGroup) null, false);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(21);
            layoutParams.addRule(12);
            PhotoPageItem.this.addView(this.mDownloadRoot, layoutParams);
        }

        public final DownloadType getCurDownloadType() {
            return this.mDownloadingType;
        }

        public ProgressBar getProgressBar() {
            if (this.mDownloadProgress == null) {
                this.mDownloadProgress = initProgressBar();
            }
            return this.mDownloadProgress;
        }

        public final ProgressBar initProgressBar() {
            if (this.mDownloadRoot == null) {
                initDownloadLayout();
            }
            CircleStrokeProgressBar circleStrokeProgressBar = (CircleStrokeProgressBar) this.mDownloadRoot.findViewById(R.id.download_progress);
            circleStrokeProgressBar.setDrawablesForLevels(new int[]{R.drawable.photo_download_progress_bg}, new int[]{R.drawable.photo_download_progress_second}, (int[]) null);
            circleStrokeProgressBar.setMiddleStrokeColors(new int[]{PhotoPageItem.this.getResources().getColor(R.color.download_progress_shadow_color)}, PhotoPageItem.this.getResources().getDimensionPixelSize(R.dimen.download_progress_shadow_radius_big));
            circleStrokeProgressBar.setVisibility(8);
            return circleStrokeProgressBar;
        }

        public void setCloudImageLoadingListener(CloudImageLoadingListener cloudImageLoadingListener) {
            this.mExternalListener = cloudImageLoadingListener;
        }

        public final void onLoadStart(DownloadType downloadType) {
            doOnLoadStart(downloadType);
        }

        public final void onLoadFail(DownloadType downloadType, ErrorCode errorCode, String str) {
            doOnLoadFail(downloadType, errorCode);
            PhotoPageItem.this.onImageLoadFinish(errorCode);
            if (needRefreshUI(downloadType) && !filterError(errorCode)) {
                if (this.mErrorDisplayer == null) {
                    this.mErrorDisplayer = createErrorDisplay();
                }
                this.mErrorDisplayer.handleError(errorCode, str);
                if (needShowDownloadErrorView()) {
                    showErrorView(true, errorCode);
                }
                statShowError(downloadType, errorCode);
            }
            this.isUserDownload = false;
        }

        public final void onDownloaded(DownloadType downloadType, String str) {
            if (PhotoPageItem.this.mDataItem != null) {
                if (DownloadType.isOrigin(downloadType)) {
                    PhotoPageItem.this.mDataItem.setFilePath(str);
                } else {
                    PhotoPageItem.this.mDataItem.setThumbPath(str);
                }
                GalleryModel of = GalleryModel.of(str);
                of.getExtras().putParcelable(GalleryPreferences.PrefKeys.SYNC_DOWNLOAD_TYPE, downloadType);
                PhotoPageItem photoPageItem = PhotoPageItem.this;
                photoPageItem.mPhotoPageDisplayHelper.displayImage(of, photoPageItem.getRequestOptions(photoPageItem.mDataItem, false), this.mRequestListener);
            }
            if (needRefreshUI(downloadType)) {
                hideProgress(true);
            }
            doOnDownloaded(downloadType, str);
        }

        public final void onDownloaded(DownloadType downloadType) {
            if (needRefreshUI(downloadType)) {
                hideProgress(true);
            }
        }

        public final void onLoaded(Bitmap bitmap) {
            this.isUserDownload = false;
        }

        public final void onCancel(DownloadType downloadType) {
            if (needRefreshUI(downloadType)) {
                hideProgress(true);
            }
            this.isUserDownload = false;
            doOnCancel(downloadType);
        }

        public final void onDownloading(DownloadType downloadType, float f) {
            if (needRefreshUI(downloadType)) {
                setProgress(f);
            }
            doOnDownloading(downloadType, f);
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void changeVisibilityForSpecialScene() {
            if (needShowDownloadView()) {
                if (!PhotoPageItem.this.isPagerSelected()) {
                    return;
                }
                processDownload();
                return;
            }
            hideProgress(true);
            hideErrorView(true);
        }

        public final void setProgress(float f) {
            ProgressBar progressBar = getProgressBar();
            if (progressBar == null || f < 0.0f || f > 1.0f) {
                return;
            }
            progressBar.setProgress((int) (f * progressBar.getMax()));
        }

        public final boolean needShowDownloadErrorView() {
            return !PhotoPageItem.this.isInActionMode() && !isRotating() && !isDrawableDisplayInside();
        }

        public boolean needShowDownloadView() {
            return !PhotoPageItem.this.isInActionMode() && !isRotating() && !isDrawableDisplayInside();
        }

        public final boolean showProgress(boolean z) {
            if (needShowDownloadView()) {
                this.mHasShowProgress = true;
                ProgressBar progressBar = getProgressBar();
                if (progressBar != null && progressBar.getVisibility() != 0) {
                    setProgress(0.0f);
                    hideErrorView(false);
                    progressBar.clearAnimation();
                    progressBar.setVisibility(0);
                    adjustProgressBarLocation(PhotoPageItem.this.isActionBarVisible(), PhotoPageItem.this.mPhotoView.getDisplayRect(), false);
                    if (z) {
                        progressBar.startAnimation(generateShowAnimation());
                    }
                    doOnProgressVisibilityChanged(true);
                    return true;
                }
            }
            return false;
        }

        public final boolean hideProgress(boolean z) {
            ProgressBar progressBar;
            if (!this.mHasShowProgress || (progressBar = getProgressBar()) == null || progressBar.getVisibility() == 8) {
                return false;
            }
            progressBar.clearAnimation();
            if (z) {
                Animation generateHideAnimation = generateHideAnimation();
                generateHideAnimation.setAnimationListener(new AnimationListenerAdapter() { // from class: com.miui.gallery.ui.PhotoPageItem.DownloadManager.2
                    {
                        DownloadManager.this = this;
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationEnd(Animation animation) {
                        ProgressBar progressBar2 = DownloadManager.this.getProgressBar();
                        if (progressBar2 != null) {
                            progressBar2.setVisibility(8);
                            DownloadManager.this.doOnProgressVisibilityChanged(false);
                        }
                    }
                });
                progressBar.startAnimation(generateHideAnimation);
                return true;
            }
            progressBar.setVisibility(8);
            doOnProgressVisibilityChanged(false);
            return true;
        }

        public CharSequence getErrorTip() {
            if (DownloadType.isOrigin(getCurDownloadType())) {
                return PhotoPageItem.this.getResources().getString(R.string.download_error_format, PhotoPageItem.this.getResources().getString(R.string.origin_image_name));
            }
            return null;
        }

        public final boolean showErrorView(boolean z, ErrorCode errorCode) {
            boolean showTip;
            if (errorCode == ErrorCode.STORAGE_NO_WRITE_PERMISSION && !PhotoPageItem.this.mDataItem.isSpecialTypeRecognized()) {
                showTip = PhotoPageItem.this.mNoStoragePermissionView.showTip(R.string.scope_storage_dialog_guide_content, z, this.mErrorDisplayer, this.mTipToken);
            } else {
                showTip = PhotoPageItem.this.mTipView.showTip(R.string.error_download_tip, z, this.mErrorDisplayer, this.mTipToken);
            }
            if (showTip) {
                hideProgress(false);
                if (this.isUserDownload) {
                    CharSequence errorTip = getErrorTip();
                    if (!TextUtils.isEmpty(errorTip)) {
                        ToastUtils.makeText(PhotoPageItem.this.getContext(), errorTip);
                    }
                }
            }
            return showTip;
        }

        public final boolean hideErrorView(boolean z) {
            return PhotoPageItem.this.mNoStoragePermissionView.hideTip(z, this.mTipToken) || PhotoPageItem.this.mTipView.hideTip(z, this.mTipToken);
        }

        public final void cancelAnim() {
            Animator animator = this.mAnimator;
            if (animator == null || !animator.isRunning()) {
                return;
            }
            this.mAnimator.cancel();
            this.mAnimator = null;
            this.mAdjustX = this.mDownloadRoot.getTranslationX();
            this.mAdjustY = this.mDownloadRoot.getTranslationY();
        }

        public final void doAdjustLocation(boolean z, RectF rectF, boolean z2) {
            cancelAnim();
            float menuBarHeight = (!z || MiscUtil.isLandModeAndSupportVersion(PhotoPageItem.this.getContext())) ? 0.0f : PhotoPageItem.this.getMenuBarHeight();
            float height = rectF != null ? rectF.bottom : PhotoPageItem.this.getHeight();
            float width = rectF != null ? rectF.right : PhotoPageItem.this.getWidth();
            float min = 0.0f - Math.min(getMaxTranslationY(), Math.max(menuBarHeight, PhotoPageItem.this.getHeight() - height) + getVerticalMargin());
            float min2 = 0.0f - Math.min(getMaxTranslationX(), Math.max(PhotoPageItem.this.getWidth() - width, 0.0f) + getHorizontalMargin());
            if (this.mAdjustY == min && this.mAdjustX == min2) {
                return;
            }
            this.mAdjustX = min2;
            this.mAdjustY = min;
            if (z2) {
                AnimatorSet animatorSet = new AnimatorSet();
                View view = this.mDownloadRoot;
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "TranslationY", view.getTranslationY(), min);
                View view2 = this.mDownloadRoot;
                animatorSet.playTogether(ObjectAnimator.ofFloat(view2, "TranslationX", view2.getTranslationX(), min2), ofFloat);
                animatorSet.setDuration(getAdjustAnimDuration(z));
                animatorSet.setInterpolator(getAdjustAnimInterpolator(z));
                this.mAnimator = animatorSet;
                animatorSet.start();
                return;
            }
            this.mDownloadRoot.setTranslationY(min);
            this.mDownloadRoot.setTranslationX(min2);
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public final void adjustLocation(boolean z, RectF rectF, boolean z2) {
            adjustProgressBarLocation(z, rectF, z2);
        }

        public void adjustProgressBarLocation(boolean z, RectF rectF, boolean z2) {
            ProgressBar progressBar;
            if (this.mHasShowProgress && (progressBar = getProgressBar()) != null && progressBar.getVisibility() == 0) {
                doAdjustLocation(z, rectF, z2);
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void onSelected() {
            super.onSelected();
            processDownload();
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void onUnSelected() {
            hideProgress(false);
            hideErrorView(false);
            super.onUnSelected();
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void release() {
            onUnSelected();
            super.release();
            cancelDisplayTask();
            cancelGetStatusAsyncTask();
            this.mDownloadingType = null;
            this.mExternalListener = null;
            this.mAdjustX = 0.0f;
            this.mAdjustY = 0.0f;
            this.mHasShowProgress = false;
            this.mErrorDisplayer = null;
        }

        public final void cancelGetStatusAsyncTask() {
            AsyncTask<Void, Void, DownloadItemStatus> asyncTask = this.mGetOriginTask;
            if (asyncTask != null && !asyncTask.isCancelled()) {
                this.mGetOriginTask.cancel(false);
                this.mGetOriginTask = null;
            }
            AsyncTask<Void, Void, DownloadItemStatus> asyncTask2 = this.mGetThumbnailTask;
            if (asyncTask2 == null || asyncTask2.isCancelled()) {
                return;
            }
            this.mGetThumbnailTask.cancel(false);
            this.mGetThumbnailTask = null;
        }

        public final void cancelDisplayTask() {
            if (this.mDownloadingType != null) {
                CloudImageLoader.getInstance().cancelDisplayTask(PhotoPageItem.this.mDataItem.getDownloadUri(), this.mDownloadingType, PhotoPageItem.this.mPhotoView);
            }
        }

        public void processDownload() {
            final DownloadType downloadType;
            BaseDataItem baseDataItem = PhotoPageItem.this.mDataItem;
            if (baseDataItem == null || baseDataItem.getDownloadUri() == null || isOriginDownloaded()) {
                return;
            }
            if (isOriginRequestForce()) {
                downloadType = DownloadType.ORIGIN_FORCE;
            } else {
                downloadType = DownloadType.ORIGIN;
            }
            if (downloadType == DownloadType.ORIGIN_FORCE || CloudImageLoader.getInstance().isRequesting(PhotoPageItem.this.mDataItem.getDownloadUri(), downloadType) || (PhotoPageItem.this.mDataItem.isBurstItem() && this.mDownloadingType == DownloadType.ORIGIN)) {
                downloadOrigin(downloadType);
                CloudImageLoadingListener cloudImageLoadingListener = this.mExternalListener;
                if (cloudImageLoadingListener == null) {
                    return;
                }
                cloudImageLoadingListener.onLoadingStarted(PhotoPageItem.this.mDataItem.getDownloadUri(), downloadType, PhotoPageItem.this.mPhotoView);
            } else if (!TextUtils.isEmpty(PhotoPageItem.this.mDataItem.getOriginalPath())) {
            } else {
                this.mGetOriginTask = CloudImageLoader.getInstance().getStatusAsync(PhotoPageItem.this.mDataItem.getDownloadUri(), downloadType, new CloudImageLoader.GetStatusCallBack() { // from class: com.miui.gallery.ui.PhotoPageItem.DownloadManager.3
                    {
                        DownloadManager.this = this;
                    }

                    @Override // com.miui.gallery.util.glide.CloudImageLoader.GetStatusCallBack
                    public void onStatusGotten(DownloadItemStatus downloadItemStatus) {
                        DownloadManager downloadManager = DownloadManager.this;
                        if (downloadManager.needRefreshUI(downloadManager.mDownloadingType)) {
                            if (downloadItemStatus.isDownloading()) {
                                DownloadManager.this.downloadOrigin(downloadType);
                                if (DownloadManager.this.mExternalListener == null) {
                                    return;
                                }
                                DownloadManager.this.mExternalListener.onLoadingStarted(PhotoPageItem.this.mDataItem.getDownloadUri(), downloadType, PhotoPageItem.this.mPhotoView);
                            } else if (downloadItemStatus.getStatus() != SyncStatus.STATUS_SUCCESS) {
                                DownloadManager.this.processThumbnail();
                            } else {
                                PhotoPageItem.this.mDataItem.setFilePath(downloadItemStatus.getDownloadedPath());
                                PhotoPageItem photoPageItem = PhotoPageItem.this;
                                photoPageItem.swapItem(photoPageItem.mDataItem);
                                if (DownloadManager.this.mExternalListener == null) {
                                    return;
                                }
                                DownloadManager.this.mExternalListener.onDownloadComplete(PhotoPageItem.this.mDataItem.getDownloadUri(), downloadType, PhotoPageItem.this.mPhotoView, downloadItemStatus.getDownloadedPath());
                            }
                        }
                    }
                });
            }
        }

        public final boolean isOriginDownloaded() {
            if (PhotoPageItem.this.mDataItem.isBurstItem()) {
                for (BaseDataItem baseDataItem : PhotoPageItem.this.mDataItem.getBurstGroup()) {
                    if (TextUtils.isEmpty(baseDataItem.getOriginalPath())) {
                        return false;
                    }
                }
                return true;
            }
            return !TextUtils.isEmpty(PhotoPageItem.this.mDataItem.getOriginalPath());
        }

        public final boolean isOriginRequestForce() {
            if (PhotoPageItem.this.mDataItem == null) {
                return false;
            }
            if (CloudImageLoader.getInstance().isRequesting(PhotoPageItem.this.mDataItem.getDownloadUri(), DownloadType.ORIGIN_FORCE)) {
                return true;
            }
            List<BaseDataItem> burstGroup = PhotoPageItem.this.mDataItem.getBurstGroup();
            if (BaseMiscUtil.isValid(burstGroup)) {
                for (BaseDataItem baseDataItem : burstGroup) {
                    if (CloudImageLoader.getInstance().isRequesting(baseDataItem.getDownloadUri(), DownloadType.ORIGIN_FORCE)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public final void processThumbnail() {
            BaseDataItem baseDataItem = PhotoPageItem.this.mDataItem;
            if (baseDataItem != null && TextUtils.isEmpty(baseDataItem.getThumnailPath()) && TextUtils.isEmpty(PhotoPageItem.this.mDataItem.getOriginalPath())) {
                this.mGetThumbnailTask = CloudImageLoader.getInstance().getStatusAsync(PhotoPageItem.this.mDataItem.getDownloadUri(), DownloadType.THUMBNAIL, new CloudImageLoader.GetStatusCallBack() { // from class: com.miui.gallery.ui.PhotoPageItem$DownloadManager$$ExternalSyntheticLambda3
                    @Override // com.miui.gallery.util.glide.CloudImageLoader.GetStatusCallBack
                    public final void onStatusGotten(DownloadItemStatus downloadItemStatus) {
                        PhotoPageItem.DownloadManager.this.lambda$processThumbnail$0(downloadItemStatus);
                    }
                });
                return;
            }
            hideProgress(false);
            hideErrorView(false);
            this.mDownloadingType = null;
        }

        public /* synthetic */ void lambda$processThumbnail$0(DownloadItemStatus downloadItemStatus) {
            if (needRefreshUI(this.mDownloadingType)) {
                if (downloadItemStatus.getStatus() == SyncStatus.STATUS_SUCCESS) {
                    PhotoPageItem.this.mDataItem.setThumbPath(downloadItemStatus.getDownloadedPath());
                    PhotoPageItem photoPageItem = PhotoPageItem.this;
                    photoPageItem.swapItem(photoPageItem.mDataItem);
                    return;
                }
                this.mDownloadingType = DownloadType.THUMBNAIL;
                hideErrorView(false);
                showProgress(true);
                CloudImageLoader.getInstance().loadImage(PhotoPageItem.this.mDataItem.getDownloadUri(), this.mDownloadingType, PhotoPageItem.getLoadingListener(this), PhotoPageItem.getLoadingProgressListener(this));
            }
        }

        public void downloadOrigin(DownloadType downloadType) {
            if (PhotoPageItem.this.mDataItem != null) {
                hideProgress(false);
                hideErrorView(false);
                CloudImageLoader.getInstance().cancel(PhotoPageItem.this.mDataItem.getDownloadUri(), DownloadType.THUMBNAIL);
                this.mDownloadingType = downloadType;
                showProgress(true);
                if (PhotoPageItem.this.mDataItem.isBurstItem()) {
                    List<BaseDataItem> burstGroup = PhotoPageItem.this.mDataItem.getBurstGroup();
                    ArrayList arrayList = new ArrayList();
                    for (BaseDataItem baseDataItem : burstGroup) {
                        if (TextUtils.isEmpty(baseDataItem.getOriginalPath())) {
                            arrayList.add(new BulkDownloadHelper.BulkDownloadItem(baseDataItem.getDownloadUri(), this.mDownloadingType, PhotoPageItem.this.mDataItem.getSize()));
                        }
                    }
                    if (!BaseMiscUtil.isValid(arrayList)) {
                        return;
                    }
                    CloudImageLoadingListener cloudImageLoadingListener = this.mExternalListener;
                    if (cloudImageLoadingListener != null) {
                        cloudImageLoadingListener.onLoadingStarted(null, downloadType, null);
                    }
                    this.mBulkDownloadListener.setDownloadType(downloadType);
                    new BulkDownloadHelper().download(arrayList, true, this.mBulkDownloadListener);
                    return;
                }
                CloudImageLoader.getInstance().loadImage(PhotoPageItem.this.mDataItem.getDownloadUri(), this.mDownloadingType, PhotoPageItem.getLoadingListener(this), PhotoPageItem.getLoadingProgressListener(this));
            }
        }

        public void downloadOrigin(boolean z) {
            BaseDataItem baseDataItem = PhotoPageItem.this.mDataItem;
            if (baseDataItem == null) {
                DefaultLogger.w("PhotoPageItem", "data is null while downloading original file");
                return;
            }
            this.isUserDownload = true;
            if (IncompatibleMediaType.isUnsupportedMediaType(baseDataItem.getMimeType())) {
                considerCompatibility(z);
            } else {
                considerNetwork(z);
            }
        }

        public final void considerCompatibility(final boolean z) {
            String string = PhotoPageItem.this.getResources().getString(R.string.download_tip);
            DialogUtil.showInfoDialog(PhotoPageItem.this.getContext(), IncompatibleMediaType.getUnsupportedMediaDownloadingTip(PhotoPageItem.this.getContext(), PhotoPageItem.this.mDataItem.getMimeType()), string, (int) R.string.download_tip, 17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.PhotoPageItem$DownloadManager$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    PhotoPageItem.DownloadManager.$r8$lambda$goQEwJU9RHspx_8L1BBG1b3wK5c(PhotoPageItem.DownloadManager.this, z, dialogInterface, i);
                }
            }, PhotoPageItem$DownloadManager$$ExternalSyntheticLambda1.INSTANCE);
        }

        public /* synthetic */ void lambda$considerCompatibility$1(boolean z, DialogInterface dialogInterface, int i) {
            considerNetwork(z);
        }

        public final void considerNetwork(boolean z) {
            if (z) {
                NetworkConsider.consider(PhotoPageItem.this.getContext(), new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.ui.PhotoPageItem$DownloadManager$$ExternalSyntheticLambda2
                    @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                    public final void onConfirmed(boolean z2, boolean z3) {
                        PhotoPageItem.DownloadManager.$r8$lambda$vbHSj5N0_1RlXvtPu2F_T9qResQ(PhotoPageItem.DownloadManager.this, z2, z3);
                    }
                });
            } else {
                downloadOrigin(DownloadType.ORIGIN_FORCE);
            }
        }

        public /* synthetic */ void lambda$considerNetwork$3(boolean z, boolean z2) {
            DownloadType downloadType;
            if (z) {
                if (z2) {
                    downloadType = DownloadType.ORIGIN_FORCE;
                } else {
                    downloadType = DownloadType.ORIGIN;
                }
                downloadOrigin(downloadType);
            }
        }

        public final boolean needRefreshUI(DownloadType downloadType) {
            PhotoPageItem photoPageItem = PhotoPageItem.this;
            return photoPageItem.mDataItem != null && photoPageItem.isPagerSelected() && downloadType == this.mDownloadingType;
        }

        public DownloadErrorDisplay createErrorDisplay() {
            return new DownloadErrorDisplay();
        }

        /* loaded from: classes2.dex */
        public class DownloadErrorDisplay implements TipDetailDisplay<ErrorTip>, ErrorActionCallback, ErrorTranslateCallback {
            public GalleryDialogFragment mErrorDialog;
            public ErrorTip mErrorTip;
            public BaseErrorCodeTranslator mErrorTranslator = new BaseErrorCodeTranslator();

            /* renamed from: $r8$lambda$nV-oB-lRqRRiq8N3LEppakDqq8s */
            public static /* synthetic */ void m1548$r8$lambda$nVoBlRqRRiq8N3LEppakDqq8s(DownloadErrorDisplay downloadErrorDisplay, ErrorTip errorTip, Context context, DialogInterface dialogInterface, int i) {
                downloadErrorDisplay.lambda$display$2(errorTip, context, dialogInterface, i);
            }

            public static /* synthetic */ void $r8$lambda$u5j436g8iP8yTjxsEyZqyD8_OD0(DownloadErrorDisplay downloadErrorDisplay, boolean z) {
                downloadErrorDisplay.lambda$display$0(z);
            }

            public DownloadErrorDisplay() {
                DownloadManager.this = r1;
            }

            public void handleError(ErrorCode errorCode, String str) {
                this.mErrorTranslator.translate(PhotoPageItem.this.getContext(), errorCode, str, this);
            }

            @Override // com.miui.gallery.error.core.ErrorTranslateCallback
            public void onTranslate(ErrorTip errorTip) {
                this.mErrorTip = errorTip;
            }

            @Override // com.miui.gallery.ui.PhotoPageItem.TipDetailDisplay
            /* renamed from: getParams */
            public ErrorTip mo1549getParams() {
                return this.mErrorTip;
            }

            @Override // com.miui.gallery.ui.PhotoPageItem.TipDetailDisplay
            public final void display(final Context context, final ErrorTip errorTip) {
                if (errorTip == null) {
                    return;
                }
                GalleryDialogFragment galleryDialogFragment = this.mErrorDialog;
                if (galleryDialogFragment != null) {
                    galleryDialogFragment.dismissAllowingStateLoss();
                    this.mErrorDialog = null;
                }
                if (errorTip.getCode() == ErrorCode.NO_CTA_PERMISSION) {
                    AgreementsUtils.showNetworkingAgreement((FragmentActivity) context, new OnAgreementInvokedListener() { // from class: com.miui.gallery.ui.PhotoPageItem$DownloadManager$DownloadErrorDisplay$$ExternalSyntheticLambda2
                        @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                        public final void onAgreementInvoked(boolean z) {
                            PhotoPageItem.DownloadManager.DownloadErrorDisplay.$r8$lambda$u5j436g8iP8yTjxsEyZqyD8_OD0(PhotoPageItem.DownloadManager.DownloadErrorDisplay.this, z);
                        }
                    });
                } else if (errorTip.getCode() == ErrorCode.STORAGE_NO_WRITE_PERMISSION) {
                    errorTip.action(context, this);
                } else if (!TextUtils.isEmpty(errorTip.getActionStr(context))) {
                    this.mErrorDialog = new AlertDialogFragment.Builder().setTitle(errorTip.getTitle(context)).setMessage(errorTip.getMessage(context)).setNegativeButton(PhotoPageItem.this.getResources().getString(R.string.cancel), PhotoPageItem$DownloadManager$DownloadErrorDisplay$$ExternalSyntheticLambda1.INSTANCE).setPositiveButton(errorTip.getActionStr(context), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.PhotoPageItem$DownloadManager$DownloadErrorDisplay$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            PhotoPageItem.DownloadManager.DownloadErrorDisplay.m1548$r8$lambda$nVoBlRqRRiq8N3LEppakDqq8s(PhotoPageItem.DownloadManager.DownloadErrorDisplay.this, errorTip, context, dialogInterface, i);
                        }
                    }).create();
                } else {
                    ToastUtils.makeText(context, errorTip.getTitle(context));
                }
                GalleryDialogFragment galleryDialogFragment2 = this.mErrorDialog;
                if (galleryDialogFragment2 != null) {
                    galleryDialogFragment2.showAllowingStateLoss(((FragmentActivity) context).getSupportFragmentManager(), "error");
                }
                DownloadManager downloadManager = DownloadManager.this;
                downloadManager.statClickError(downloadManager.mDownloadingType, errorTip);
            }

            public /* synthetic */ void lambda$display$0(boolean z) {
                if (z) {
                    onAction(ErrorCode.NO_CTA_PERMISSION, false);
                }
            }

            public /* synthetic */ void lambda$display$2(ErrorTip errorTip, Context context, DialogInterface dialogInterface, int i) {
                errorTip.action(context, this);
            }

            @Override // com.miui.gallery.error.core.ErrorActionCallback
            public void onAction(ErrorCode errorCode, boolean z) {
                if (!z) {
                    int i = AnonymousClass5.$SwitchMap$com$miui$gallery$error$core$ErrorCode[errorCode.ordinal()];
                    if (i == 1 || i == 2) {
                        DownloadManager.this.downloadOrigin(true);
                    } else if (i == 3) {
                        DownloadManager.this.downloadOrigin(DownloadType.ORIGIN_FORCE);
                    } else if (i != 4) {
                        if (i == 5) {
                            DeletionTask deletionTask = new DeletionTask();
                            deletionTask.setFragmentActivityForStoragePermissionMiss((FragmentActivity) PhotoPageItem.this.getContext());
                            deletionTask.setOnDeletionCompleteListener(new DeletionTask.OnDeletionCompleteListener() { // from class: com.miui.gallery.ui.PhotoPageItem.DownloadManager.DownloadErrorDisplay.1
                                {
                                    DownloadErrorDisplay.this = this;
                                }

                                @Override // com.miui.gallery.ui.DeletionTask.OnDeletionCompleteListener
                                public void onDeleted(int i2, long[] jArr) {
                                    if (PhotoPageItem.this.mOnDeleteListener != null) {
                                        PhotoPageItem.this.mOnDeleteListener.onDelete();
                                    }
                                }
                            });
                            deletionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new DeletionTask.Param(new long[]{PhotoPageItem.this.mDataItem.getKey()}, -1L, "", 92));
                        }
                    } else if (BaseGalleryPreferences.CTA.canConnectNetwork()) {
                        DownloadManager.this.processDownload();
                    }
                }
                DownloadManager downloadManager = DownloadManager.this;
                downloadManager.statActionError(downloadManager.mDownloadingType, this.mErrorTip);
            }
        }

        public final void statShowError(DownloadType downloadType, ErrorCode errorCode) {
            HashMap hashMap = new HashMap();
            hashMap.put("error", errorCode.toString());
            SamplingStatHelper.recordCountEvent("photo", String.format(Locale.US, "photo_download_show_error_%s", downloadType), hashMap);
        }

        public final void statClickError(DownloadType downloadType, ErrorTip errorTip) {
            HashMap hashMap = new HashMap();
            hashMap.put("error_extra", errorTip.toString());
            SamplingStatHelper.recordCountEvent("photo", String.format(Locale.US, "photo_download_click_error_%s", downloadType), hashMap);
        }

        public final void statActionError(DownloadType downloadType, ErrorTip errorTip) {
            HashMap hashMap = new HashMap();
            hashMap.put("error_extra", errorTip.toString());
            SamplingStatHelper.recordCountEvent("photo", String.format(Locale.US, "photo_download_click_error_action_%s", downloadType), hashMap);
        }

        /* loaded from: classes2.dex */
        public class PhotoItemBulkDownloadListener implements BulkDownloadHelper.BulkDownloadListener {
            public DownloadType mDownloadType = DownloadType.ORIGIN;

            public PhotoItemBulkDownloadListener() {
                DownloadManager.this = r1;
            }

            public void setDownloadType(DownloadType downloadType) {
                this.mDownloadType = downloadType;
            }

            @Override // com.miui.gallery.util.BulkDownloadHelper.BulkDownloadListener
            public void onDownloadProgress(float f) {
                DownloadManager.this.onDownloading(this.mDownloadType, f);
            }

            @Override // com.miui.gallery.util.BulkDownloadHelper.BulkDownloadListener
            public void onDownloadEnd(List<BulkDownloadHelper.BulkDownloadItem> list, List<BulkDownloadHelper.BulkDownloadItem> list2) {
                boolean z;
                boolean z2;
                BaseDataItem baseDataItem = PhotoPageItem.this.mDataItem;
                if (baseDataItem == null) {
                    return;
                }
                List<BaseDataItem> burstGroup = baseDataItem.getBurstGroup();
                if (!BaseMiscUtil.isValid(burstGroup)) {
                    DownloadManager downloadManager = DownloadManager.this;
                    downloadManager.onDownloaded(downloadManager.mDownloadingType);
                    if (DownloadManager.this.mExternalListener == null) {
                        return;
                    }
                    DownloadManager.this.mExternalListener.onDownloadComplete(null, this.mDownloadType, null, null);
                    return;
                }
                if (BaseMiscUtil.isValid(list)) {
                    loop0: while (true) {
                        z = true;
                        for (BaseDataItem baseDataItem2 : burstGroup) {
                            if (TextUtils.isEmpty(baseDataItem2.getOriginalPath())) {
                                Iterator<BulkDownloadHelper.BulkDownloadItem> it = list.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        z2 = false;
                                        break;
                                    }
                                    BulkDownloadHelper.BulkDownloadItem next = it.next();
                                    if (baseDataItem2.getDownloadUri().equals(next.getDownloadUri())) {
                                        if (CloudImageLoader.getInstance().isOrigin(next.getType())) {
                                            baseDataItem2.setFilePath(next.getDownloadPath());
                                        } else {
                                            baseDataItem2.setThumbPath(next.getDownloadPath());
                                        }
                                        z2 = true;
                                    }
                                }
                                if (!z || !z2) {
                                    z = false;
                                }
                            }
                        }
                    }
                } else {
                    z = false;
                }
                if (!z && BaseMiscUtil.isValid(list2)) {
                    BulkDownloadHelper.BulkDownloadItem bulkDownloadItem = list2.get(0);
                    DownloadManager.this.onLoadFail(this.mDownloadType, bulkDownloadItem.getErrorCode(), bulkDownloadItem.getErrorDesc());
                    if (DownloadManager.this.mExternalListener == null) {
                        return;
                    }
                    DownloadManager.this.mExternalListener.onLoadingFailed(null, this.mDownloadType, null, bulkDownloadItem.getErrorCode(), bulkDownloadItem.getErrorDesc());
                } else if (z) {
                    DownloadManager downloadManager2 = DownloadManager.this;
                    downloadManager2.onDownloaded(downloadManager2.mDownloadingType);
                    if (DownloadManager.this.mExternalListener == null) {
                        return;
                    }
                    DownloadManager.this.mExternalListener.onDownloadComplete(null, this.mDownloadType, null, null);
                }
            }
        }
    }

    /* renamed from: com.miui.gallery.ui.PhotoPageItem$5 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass5 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$error$core$ErrorCode;

        static {
            int[] iArr = new int[ErrorCode.values().length];
            $SwitchMap$com$miui$gallery$error$core$ErrorCode = iArr;
            try {
                iArr[ErrorCode.THUMBNAIL_BUILD_ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.WRITE_EXIF_ERROR.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.NO_WIFI_CONNECTED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.NO_CTA_PERMISSION.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.SERVER_INVALID.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public static CloudImageLoadingListener getLoadingListener(DownloadManager downloadManager) {
        return new CloudImageLoadingListener() { // from class: com.miui.gallery.ui.PhotoPageItem.2
            public final WeakReference<DownloadManager> mDownloadManagerRef;

            {
                DownloadManager.this = downloadManager;
                this.mDownloadManagerRef = new WeakReference<>(downloadManager);
            }

            @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
            public void onLoadingStarted(Uri uri, DownloadType downloadType, View view) {
                DownloadManager downloadManager2 = this.mDownloadManagerRef.get();
                if (downloadManager2 == null) {
                    return;
                }
                downloadManager2.onLoadStart(downloadType);
                if (downloadManager2.mExternalListener == null) {
                    return;
                }
                downloadManager2.mExternalListener.onLoadingStarted(uri, downloadType, view);
            }

            @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
            public void onLoadingFailed(Uri uri, DownloadType downloadType, View view, ErrorCode errorCode, String str) {
                DownloadManager downloadManager2 = this.mDownloadManagerRef.get();
                if (downloadManager2 == null) {
                    return;
                }
                downloadManager2.onLoadFail(downloadType, errorCode, str);
                if (downloadManager2.mExternalListener == null) {
                    return;
                }
                downloadManager2.mExternalListener.onLoadingFailed(uri, downloadType, view, errorCode, str);
            }

            @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
            public void onLoadingComplete(Uri uri, DownloadType downloadType, View view, Bitmap bitmap) {
                DownloadManager downloadManager2 = this.mDownloadManagerRef.get();
                if (downloadManager2 == null) {
                    return;
                }
                downloadManager2.onLoaded(bitmap);
                if (downloadManager2.mExternalListener == null) {
                    return;
                }
                downloadManager2.mExternalListener.onLoadingComplete(uri, downloadType, view, bitmap);
            }

            @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
            public void onDownloadComplete(Uri uri, DownloadType downloadType, View view, String str) {
                DownloadManager downloadManager2 = this.mDownloadManagerRef.get();
                if (downloadManager2 == null) {
                    return;
                }
                downloadManager2.onDownloaded(downloadType, str);
                if (downloadManager2.mExternalListener == null) {
                    return;
                }
                downloadManager2.mExternalListener.onDownloadComplete(uri, downloadType, view, str);
            }

            @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
            public void onLoadingCancelled(Uri uri, DownloadType downloadType, View view) {
                DownloadManager downloadManager2 = this.mDownloadManagerRef.get();
                if (downloadManager2 == null) {
                    return;
                }
                downloadManager2.onCancel(downloadType);
                if (downloadManager2.mExternalListener == null) {
                    return;
                }
                downloadManager2.mExternalListener.onLoadingCancelled(uri, downloadType, view);
            }
        };
    }

    public static CloudImageLoadingProgressListener getLoadingProgressListener(DownloadManager downloadManager) {
        return new CloudImageLoadingProgressListener() { // from class: com.miui.gallery.ui.PhotoPageItem.3
            public final WeakReference<DownloadManager> mDownloadManagerRef;

            {
                DownloadManager.this = downloadManager;
                this.mDownloadManagerRef = new WeakReference<>(downloadManager);
            }

            @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingProgressListener
            public void onProgressUpdate(Uri uri, DownloadType downloadType, View view, int i, int i2) {
                DownloadManager downloadManager2 = this.mDownloadManagerRef.get();
                if (downloadManager2 == null) {
                    return;
                }
                downloadManager2.onDownloading(downloadType, (i * 1.0f) / i2);
            }
        };
    }

    public void startActionMode(PhotoPageAdapter.ChoiceModeInterface choiceModeInterface, PhotoPageAdapter.ChoiceModeInterface choiceModeInterface2, PhotoPageAdapter.ChoiceModeInterface choiceModeInterface3) {
        if (this.mCheckManager.inAction()) {
            this.mCheckManager.refreshCheck(choiceModeInterface, choiceModeInterface2, choiceModeInterface3);
            return;
        }
        this.mCheckManager.startCheck(choiceModeInterface, choiceModeInterface2, choiceModeInterface3);
        onActionModeChanged(true);
    }

    /* loaded from: classes2.dex */
    public class SpecialTypeManager extends AbsPhotoRectAwareManager {
        public boolean isPendingHide;
        public boolean isPendingShow;
        public boolean mHasShowIndicator;
        public String mIndicatorText;
        public TextView mTypeIndicator;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SpecialTypeManager() {
            super();
            PhotoPageItem.this = r1;
        }

        public void setTypeIndicatorText(String str) {
            this.mIndicatorText = str;
            TextView textView = this.mTypeIndicator;
            if (textView != null) {
                textView.setText(str);
            }
        }

        public final void initLayout() {
            if (this.mTypeIndicator != null) {
                return;
            }
            TextView textView = (TextView) LayoutInflater.from(PhotoPageItem.this.getContext()).inflate(R.layout.special_type_indicator, (ViewGroup) PhotoPageItem.this, false);
            this.mTypeIndicator = textView;
            textView.setText(this.mIndicatorText);
            PhotoPageItem.this.addView(this.mTypeIndicator);
            this.mTypeIndicator.setVisibility(8);
        }

        public boolean needShowIndicatorView() {
            return !TextUtils.isEmpty(this.mIndicatorText) && !PhotoPageItem.this.isInActionMode() && !isRotating() && !isDrawableDisplayInside() && PhotoPageItem.this.isActionBarVisible && PhotoPageItem.this.mPhotoView.getDisplayRect() != null;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void changeVisibilityForSpecialScene() {
            if (needShowIndicatorView()) {
                hideIndicator(false);
                showIndicator(true);
                this.mTypeIndicator.clearAnimation();
                this.mTypeIndicator.setVisibility(0);
                return;
            }
            hideIndicator(true);
        }

        public final boolean showIndicator(boolean z) {
            if (needShowIndicatorView()) {
                this.mHasShowIndicator = true;
                initLayout();
                TextView textView = this.mTypeIndicator;
                if (textView != null && (textView.getVisibility() != 0 || this.isPendingHide)) {
                    this.mTypeIndicator.clearAnimation();
                    this.mTypeIndicator.setVisibility(0);
                    adjustLocation(PhotoPageItem.this.isActionBarVisible(), PhotoPageItem.this.mPhotoView.getDisplayRect(), false);
                    if (z) {
                        this.isPendingShow = true;
                        Animation generateShowAnimation = generateShowAnimation();
                        generateShowAnimation.setAnimationListener(new AnimationListenerAdapter() { // from class: com.miui.gallery.ui.PhotoPageItem.SpecialTypeManager.1
                            {
                                SpecialTypeManager.this = this;
                            }

                            @Override // android.view.animation.Animation.AnimationListener
                            public void onAnimationEnd(Animation animation) {
                                SpecialTypeManager.this.isPendingShow = false;
                            }
                        });
                        this.mTypeIndicator.startAnimation(generateShowAnimation);
                    }
                    return true;
                }
            }
            return false;
        }

        public final boolean hideIndicator(boolean z) {
            TextView textView;
            if (!this.mHasShowIndicator || (textView = this.mTypeIndicator) == null || textView.getVisibility() == 8) {
                return false;
            }
            this.mTypeIndicator.clearAnimation();
            if (z) {
                this.isPendingHide = true;
                Animation generateHideAnimation = generateHideAnimation();
                generateHideAnimation.setAnimationListener(new AnimationListenerAdapter() { // from class: com.miui.gallery.ui.PhotoPageItem.SpecialTypeManager.2
                    {
                        SpecialTypeManager.this = this;
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationEnd(Animation animation) {
                        if (SpecialTypeManager.this.mTypeIndicator != null && !SpecialTypeManager.this.isPendingShow) {
                            SpecialTypeManager.this.mTypeIndicator.setVisibility(8);
                        }
                        SpecialTypeManager.this.isPendingHide = false;
                    }
                });
                this.mTypeIndicator.startAnimation(generateHideAnimation);
            } else {
                this.mTypeIndicator.setVisibility(8);
            }
            return true;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void onUnSelected() {
            super.onUnSelected();
            hideIndicator(false);
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public Animation generateShowAnimation() {
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(new AlphaAnimation(0.0f, 1.0f));
            animationSet.setInterpolator(new CubicEaseOutInterpolator());
            animationSet.setDuration(300L);
            return animationSet;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public Animation generateHideAnimation() {
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));
            animationSet.setInterpolator(new CubicEaseOutInterpolator());
            animationSet.setDuration(200L);
            return animationSet;
        }

        public void onActionBarVisibleChanged(boolean z) {
            if (z) {
                showIndicator(true);
            } else {
                hideIndicator(true);
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void adjustLocation(boolean z, RectF rectF, boolean z2) {
            TextView textView;
            if (this.mHasShowIndicator && (textView = this.mTypeIndicator) != null && textView.getVisibility() == 0) {
                doAdjustLocation(z, rectF);
            }
        }

        public final void doAdjustLocation(boolean z, RectF rectF) {
            float f;
            float f2 = 0.0f;
            float actionBarHeight = z ? PhotoPageItem.this.getActionBarHeight() : 0.0f;
            float f3 = rectF != null ? rectF.top : 0.0f;
            float f4 = rectF != null ? rectF.left : 0.0f;
            float f5 = rectF != null ? rectF.right : 0.0f;
            float min = Math.min(getMaxTranslationY(), Math.max(actionBarHeight, f3) + getVerticalMargin());
            View rootView = PhotoPageItem.this.getRootView();
            boolean z2 = PhotoPageItem.this.getResources().getConfiguration().orientation == 2;
            if (ScreenUtils.isRtl(PhotoPageItem.this.getContext())) {
                f = Math.min(getMaxTranslationX(), Math.min(0.0f, f5 - PhotoPageItem.this.getWidth()) - getHorizontalMargin());
                if (rootView != null && z2) {
                    float systemWindowInsetRight = ViewCompat.getSystemWindowInsetRight(rootView);
                    if (f5 + systemWindowInsetRight >= ScreenUtils.getScreenHeight()) {
                        f2 = systemWindowInsetRight;
                    }
                    f -= f2;
                }
            } else {
                float min2 = Math.min(getMaxTranslationX(), Math.max(f4, 0.0f) + getHorizontalMargin());
                if (rootView == null || !z2) {
                    f = min2;
                } else {
                    float systemWindowInsetLeft = ViewCompat.getSystemWindowInsetLeft(rootView);
                    if (f4 <= systemWindowInsetLeft) {
                        f2 = systemWindowInsetLeft;
                    }
                    f = min2 + f2;
                }
            }
            this.mTypeIndicator.setTranslationY(min);
            this.mTypeIndicator.setTranslationX(f);
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public int getVerticalMargin() {
            if (this.mMargin == 0) {
                this.mMargin = PhotoPageItem.this.getContext().getResources().getDimensionPixelSize(R.dimen.special_type_indicator_margin);
            }
            return this.mMargin;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public int getHorizontalMargin() {
            if (this.mMargin == 0) {
                this.mMargin = PhotoPageItem.this.getContext().getResources().getDimensionPixelSize(R.dimen.special_type_indicator_margin);
            }
            return this.mMargin;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void onSelected() {
            super.onSelected();
            if (PhotoPageItem.this.isActionBarVisible) {
                showIndicator(false);
            } else {
                hideIndicator(false);
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void release() {
            hideIndicator(false);
            PhotoPageItem.this.startSpecialTypeEnterAnim(false);
            super.release();
            this.mHasShowIndicator = false;
            this.mTypeIndicator = null;
        }
    }

    /* loaded from: classes2.dex */
    public class SpecialTypeEnterManager {
        public RecyclerLayoutCache mCache;
        public PhotoPagerHelper.OnSpecialTypeEnterListener mSpecialTypeEnterListener;
        public SpecialTypeEnterView mSpecialTypeEnterView;

        public SpecialTypeEnterManager() {
            PhotoPageItem.this = r1;
            this.mSpecialTypeEnterListener = new PhotoPagerHelper.OnSpecialTypeEnterListener() { // from class: com.miui.gallery.ui.PhotoPageItem.SpecialTypeEnterManager.1
                {
                    SpecialTypeEnterManager.this = this;
                }

                @Override // com.miui.gallery.ui.PhotoPagerHelper.OnSpecialTypeEnterListener
                public void onEntersClick(BaseDataItem baseDataItem, long j) {
                    if (PhotoPageItem.this.mOnSpecialTypeEnterListener == null || j <= -1) {
                        return;
                    }
                    PhotoPageItem.this.mOnSpecialTypeEnterListener.onEnterClick(PhotoPageItem.this.mDataItem, j);
                }
            };
        }

        public /* synthetic */ SpecialTypeEnterManager(PhotoPageItem photoPageItem, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void update(boolean z) {
            BaseDataItem baseDataItem = PhotoPageItem.this.mDataItem;
            if (baseDataItem == null || !baseDataItem.isSpecialTypeEditable() || !PhotoPageItem.this.isActionBarVisible || (PhotoPageItem.this.getResources().getConfiguration().orientation == 2 && !VideoPlayerCompat.isVideoPlayerSupportShortLandscape())) {
                SpecialTypeEnterView specialTypeEnterView = this.mSpecialTypeEnterView;
                if (specialTypeEnterView == null) {
                    return;
                }
                specialTypeEnterView.hide(z);
            } else if (this.mSpecialTypeEnterView == null) {
            } else {
                List<SpecialTypeMediaUtils.SpecialEnterIconAndText> specialTypeEnterIconAndText = SpecialTypeMediaUtils.getSpecialTypeEnterIconAndText(PhotoPageItem.this.mDataItem.getSpecialTypeFlags(), PhotoPageItem.this.mDataItem.getAIModeTypeFlags());
                SpecialTypeEnterView specialTypeEnterView2 = this.mSpecialTypeEnterView;
                PhotoPageItem photoPageItem = PhotoPageItem.this;
                boolean z2 = true;
                specialTypeEnterView2.update(photoPageItem, photoPageItem.mDataItem.isVideo() && !PhotoPageItem.this.mDataItem.is8KVideoRecognized(), specialTypeEnterIconAndText);
                boolean isInMultiWindowMode = ((Activity) PhotoPageItem.this.getContext()).isInMultiWindowMode();
                if (PhotoPageItem.this.getContext().getResources().getConfiguration().orientation != 2) {
                    z2 = false;
                }
                boolean isFoldableDevice = BaseBuildUtil.isFoldableDevice();
                if (SpecialTypeMediaUtils.needToBeHiddenWhenInLandMultiWindowMode(specialTypeEnterIconAndText)) {
                    if (isFoldableDevice && BaseBuildUtil.isLargerHorizontalWidthDevice() && z2) {
                        this.mSpecialTypeEnterView.hide(false);
                        return;
                    } else if (isInMultiWindowMode && !BaseBuildUtil.isLargerHorizontalWidthDevice() && z2) {
                        this.mSpecialTypeEnterView.hide(false);
                        return;
                    }
                }
                this.mSpecialTypeEnterView.show(z);
            }
        }

        public void hide(boolean z) {
            SpecialTypeEnterView specialTypeEnterView = this.mSpecialTypeEnterView;
            if (specialTypeEnterView != null) {
                specialTypeEnterView.hide(z);
            }
        }

        public void show(boolean z) {
            SpecialTypeEnterView specialTypeEnterView = this.mSpecialTypeEnterView;
            if (specialTypeEnterView != null) {
                specialTypeEnterView.show(z);
            }
        }

        public void setVisible(boolean z) {
            SpecialTypeEnterView specialTypeEnterView = this.mSpecialTypeEnterView;
            if (specialTypeEnterView != null) {
                specialTypeEnterView.setVisible(z);
            }
        }

        public void startAnim(boolean z) {
            SpecialTypeEnterView specialTypeEnterView = this.mSpecialTypeEnterView;
            if (specialTypeEnterView != null) {
                specialTypeEnterView.startEnterAlphaAnim(z);
            }
        }

        public void setSpecialTypeEnterViewCache(RecyclerLayoutCache recyclerLayoutCache) {
            this.mCache = recyclerLayoutCache;
        }

        public void onSelecting() {
            RecyclerLayoutCache recyclerLayoutCache;
            View view;
            if (this.mSpecialTypeEnterView != null || (recyclerLayoutCache = this.mCache) == null || (view = recyclerLayoutCache.get()) == null) {
                return;
            }
            this.mSpecialTypeEnterView = new SpecialTypeEnterView(view, this.mSpecialTypeEnterListener);
            update(true);
        }

        public void onSelected() {
            if (this.mSpecialTypeEnterView == null) {
                this.mSpecialTypeEnterView = new SpecialTypeEnterView(PhotoPageItem.this, (int) R.layout.special_type_enter_layout, this.mSpecialTypeEnterListener);
                update(true);
            }
        }

        public void onUnSelected(int i) {
            if (this.mCache == null) {
                return;
            }
            if (this.mSpecialTypeEnterView == null && Math.abs(i) == 1) {
                View view = this.mCache.get();
                if (view == null) {
                    return;
                }
                this.mSpecialTypeEnterView = new SpecialTypeEnterView(PhotoPageItem.this, view, this.mSpecialTypeEnterListener);
                update(true);
            } else if (this.mSpecialTypeEnterView == null || Math.abs(i) <= 1) {
            } else {
                this.mCache.put(this.mSpecialTypeEnterView.getView());
                this.mSpecialTypeEnterView = null;
            }
        }

        public void release() {
            RecyclerLayoutCache recyclerLayoutCache;
            SpecialTypeEnterView specialTypeEnterView = this.mSpecialTypeEnterView;
            if (specialTypeEnterView == null || (recyclerLayoutCache = this.mCache) == null) {
                return;
            }
            recyclerLayoutCache.put(specialTypeEnterView.getView());
            this.mSpecialTypeEnterView = null;
        }

        public void onOrientationChanged(int i, int i2) {
            SpecialTypeEnterView specialTypeEnterView = this.mSpecialTypeEnterView;
            if (specialTypeEnterView != null) {
                int i3 = nexClip.kClip_Rotate_270;
                if (i2 != 90 && i2 != 270) {
                    return;
                }
                if (i2 != 90) {
                    i3 = 90;
                }
                specialTypeEnterView.updatePosition(i3);
            }
        }
    }

    public void finishActionMode() {
        if (this.mCheckManager.inAction()) {
            this.mCheckManager.endCheck();
            onActionModeChanged(false);
        }
    }

    public void onActionModeChanged(boolean z) {
        if (z) {
            this.mPhotoView.setZoomable(false);
            this.mPhotoView.setStroke(sStrokeColor, 1);
        } else {
            this.mPhotoView.setStroke(0, 0);
            if (this.mPhotoView.getDrawable() != null) {
                displayImage(this.mDataItem);
            }
            updateFeatures();
        }
        this.mDownloadManager.onActionModeChanged(z);
        this.mSpecialTypeManager.onActionModeChanged(z);
        this.mTipView.onActionModeChanged(z);
    }

    public boolean isInActionMode() {
        return this.mCheckManager.inAction();
    }

    @Override // com.miui.gallery.widget.slip.ISlipAnimView
    public void onSlipping(float f) {
        setSlipProgress(f);
        if (isPagerSelected()) {
            this.mCheckManager.onAlphaChanged(f);
        }
    }

    public void setUseSlipModeV2(boolean z) {
        this.mUseSlipModeV2 = z;
    }

    public void setSlipProgress(float f) {
        if (this.mUseSlipModeV2) {
            this.mPhotoView.setSlipProgress(f);
        }
    }

    public void setSlippedRect(int i, int i2) {
        this.mPhotoView.setSlippedRect(i, i2);
    }

    public boolean isBlurredForProcessing() {
        return isMediaInProcessing();
    }

    /* loaded from: classes2.dex */
    public class CheckManager implements View.OnClickListener {
        public View mCheckOriginLayout;
        public View mCheckRenderLayout;
        public View mCheckRoot;
        public CheckBox mOriginCheckBox;
        public PhotoPageAdapter.ChoiceModeInterface mOriginInterface;
        public TextView mOriginText;
        public CheckBox mSelectCheckBox;
        public PhotoPageAdapter.ChoiceModeInterface mSelectInterface;
        public View mSelectLayout;
        public boolean mVisiblePending;

        public CheckManager() {
            PhotoPageItem.this = r1;
        }

        public void ensureInflated() {
            if (this.mCheckRoot == null) {
                View inflate = ((ViewStub) PhotoPageItem.this.findViewById(R.id.page_item_check)).inflate();
                this.mCheckRoot = inflate;
                this.mSelectLayout = inflate.findViewById(R.id.check_select_layout);
                this.mSelectCheckBox = (CheckBox) this.mCheckRoot.findViewById(R.id.check_select);
                this.mCheckOriginLayout = this.mCheckRoot.findViewById(R.id.check_origin_layout);
                this.mOriginText = (TextView) this.mCheckRoot.findViewById(R.id.check_origin_text);
                this.mOriginCheckBox = (CheckBox) this.mCheckRoot.findViewById(R.id.check_origin);
                this.mCheckRenderLayout = PhotoPageItem.this.findViewById(R.id.check_render_layout);
            }
        }

        public final void onMatrixChanged(RectF rectF) {
            relayout(rectF);
        }

        public final void relayout(RectF rectF) {
            if (inAction()) {
                ensureInflated();
                Rect rect = new Rect(0, 0, PhotoPageItem.this.getWidth(), PhotoPageItem.this.getHeight());
                if (rectF != null) {
                    if (PhotoPageItem.this.mUseSlipModeV2 || rectF.height() > DisplayKt.getDisplayHeight(this.mCheckRoot) * 0.2f) {
                        int max = (int) Math.max(0.0f, rectF.left);
                        int max2 = (int) Math.max(0.0f, rectF.top);
                        rect = new Rect(max, max2, Math.max(max, (int) Math.min(PhotoPageItem.this.getWidth(), rectF.right)), Math.max(max2, (int) Math.min(PhotoPageItem.this.getHeight(), rectF.bottom)));
                    }
                } else if (PhotoPageItem.this.isUnsupportedMedia()) {
                    setCheckViewVisible(this.mCheckOriginLayout, false);
                    setCheckViewVisible(this.mCheckRenderLayout, false);
                }
                if (this.mCheckRoot.isLaidOut()) {
                    this.mCheckRoot.layout(rect.left, rect.top, rect.right, rect.bottom);
                }
                DefaultLogger.d("PhotoPageItem", "relayout: %s, isLaidOut %s", rect, Boolean.valueOf(this.mCheckRoot.isLaidOut()));
                if (this.mCheckRoot.getVisibility() == 0 || !this.mVisiblePending) {
                    return;
                }
                this.mVisiblePending = false;
                setCheckRootVisible(true);
            }
        }

        public void startCheck(PhotoPageAdapter.ChoiceModeInterface... choiceModeInterfaceArr) {
            ensureInflated();
            this.mCheckRoot.setOnClickListener(this);
            this.mSelectLayout.setOnClickListener(this);
            this.mCheckOriginLayout.setOnClickListener(this);
            if (this.mCheckRoot.getVisibility() != 0) {
                if (PhotoPageItem.this.mPhotoView.getDisplayRect() == null) {
                    this.mVisiblePending = true;
                } else {
                    setCheckRootVisible(true);
                    relayout(PhotoPageItem.this.mPhotoView.getDisplayRect());
                }
            }
            refreshCheck(choiceModeInterfaceArr);
            PhotoPageItem.this.mPhotoView.setInterceptTouch(true);
        }

        public boolean inAction() {
            View view = this.mCheckRoot;
            return (view != null && view.getVisibility() == 0) || this.mVisiblePending;
        }

        public void onAlphaChanged(float f) {
            ensureInflated();
            this.mCheckRoot.setAlpha(f);
        }

        public final void setCheckRootVisible(boolean z) {
            if (z) {
                this.mCheckRoot.setVisibility(0);
            } else {
                this.mCheckRoot.setVisibility(8);
            }
        }

        public final void setCheckViewVisible(View view, boolean z) {
            if (z) {
                view.setVisibility(0);
                view.setOnClickListener(this);
                return;
            }
            view.setVisibility(4);
            view.setOnClickListener(null);
        }

        public void endCheck() {
            View view;
            PhotoPageItem.this.mPhotoView.setInterceptTouch(false);
            dispatchInterfaces(new PhotoPageAdapter.ChoiceModeInterface[0]);
            this.mVisiblePending = false;
            if (this.mCheckRoot == null || (view = this.mCheckOriginLayout) == null || this.mSelectLayout == null || this.mCheckRenderLayout == null) {
                return;
            }
            view.setOnClickListener(null);
            this.mSelectLayout.setOnClickListener(null);
            this.mCheckRenderLayout.setOnClickListener(null);
            if (this.mCheckRoot.getVisibility() == 0) {
                onAlphaChanged(1.0f);
                setCheckRootVisible(false);
            }
            this.mCheckRoot.setContentDescription(null);
        }

        public void toggleCheckBox(CheckBox checkBox, PhotoPageAdapter.ChoiceModeInterface choiceModeInterface) {
            if (PhotoPageItem.this.mDataItem == null) {
                return;
            }
            boolean isChecked = checkBox.isChecked();
            if (choiceModeInterface != null) {
                choiceModeInterface.onItemCheckedChanged(((Integer) PhotoPageItem.this.getTag(R.id.tag_item_position)).intValue(), PhotoPageItem.this.mDataItem.getKey(), !isChecked);
            }
            setCheckBoxState(checkBox, !isChecked, true);
            TalkBackUtil.requestAnnouncementEvent(checkBox, PhotoPageItem.this.getResources().getString(!isChecked ? R.string.checked : R.string.unchecked));
        }

        public void setCheckBoxState(CheckBox checkBox, boolean z, boolean z2) {
            checkBox.setChecked(z);
            Drawable buttonDrawable = checkBox.getButtonDrawable();
            if (!z2 && buttonDrawable != null && (buttonDrawable instanceof AnimatedStateListDrawable)) {
                buttonDrawable.jumpToCurrentState();
            }
            int id = checkBox.getId();
            if (id == R.id.check_origin) {
                this.mCheckOriginLayout.setContentDescription(formatContentDescriptionWithCheckState(this.mOriginText.getText(), z));
            } else if (id == R.id.check_render) {
                this.mCheckRenderLayout.setContentDescription(formatContentDescriptionWithCheckState(PhotoPageItem.this.getResources().getString(R.string.fast_share_auto_render), z));
            } else if (id != R.id.check_select) {
            } else {
                PhotoPageItem photoPageItem = PhotoPageItem.this;
                String formatContentDescriptionWithCheckState = formatContentDescriptionWithCheckState(photoPageItem.mDataItem.getContentDescription(photoPageItem.getContext()), z);
                this.mSelectLayout.setContentDescription(formatContentDescriptionWithCheckState);
                this.mCheckRoot.setContentDescription(formatContentDescriptionWithCheckState);
            }
        }

        public String getContentDescription() {
            PhotoPageItem photoPageItem = PhotoPageItem.this;
            if (photoPageItem.mDataItem == null || this.mSelectInterface == null) {
                return null;
            }
            Resources resources = photoPageItem.getResources();
            int i = this.mSelectInterface.isChecked(PhotoPageItem.this.mDataItem.getKey()) ? R.string.checked_something : R.string.unchecked_something;
            PhotoPageItem photoPageItem2 = PhotoPageItem.this;
            return resources.getString(i, photoPageItem2.mDataItem.getContentDescription(photoPageItem2.getContext()));
        }

        public final String formatContentDescriptionWithCheckState(CharSequence charSequence, boolean z) {
            if (charSequence == null) {
                return PhotoPageItem.this.getResources().getString(z ? R.string.checked : R.string.unchecked);
            }
            return PhotoPageItem.this.getResources().getString(z ? R.string.checked_something : R.string.unchecked_something, charSequence);
        }

        public boolean originChecked() {
            return PhotoPageItem.this.mDataItem.isVideo() || PhotoPageItem.this.mDataItem.isGif() || !TextUtils.isEmpty(PhotoPageItem.this.mDataItem.getOriginalPath());
        }

        public void dispatchInterfaces(PhotoPageAdapter.ChoiceModeInterface... choiceModeInterfaceArr) {
            PhotoPageAdapter.ChoiceModeInterface choiceModeInterface = null;
            if (choiceModeInterfaceArr != null) {
                this.mOriginInterface = choiceModeInterfaceArr.length > 0 ? choiceModeInterfaceArr[0] : null;
                if (choiceModeInterfaceArr.length > 1) {
                    choiceModeInterface = choiceModeInterfaceArr[1];
                }
                this.mSelectInterface = choiceModeInterface;
                return;
            }
            this.mOriginInterface = null;
            this.mSelectInterface = null;
        }

        public void refreshCheck(PhotoPageAdapter.ChoiceModeInterface... choiceModeInterfaceArr) {
            ensureInflated();
            dispatchInterfaces(choiceModeInterfaceArr);
            PhotoPageItem photoPageItem = PhotoPageItem.this;
            if (photoPageItem.mDataItem != null) {
                this.mCheckRoot.setContentDescription(photoPageItem.getContentDescriptionForTalkBack());
                setCheckBoxState(this.mSelectCheckBox, this.mSelectInterface.isChecked(PhotoPageItem.this.mDataItem.getKey()), false);
                setCheckBoxState(this.mOriginCheckBox, this.mOriginInterface.isChecked(PhotoPageItem.this.mDataItem.getKey()), false);
                this.mOriginText.setText(PhotoPageItem.this.getResources().getString(R.string.select_origin_tip, FormatUtil.formatFileSize(PhotoPageItem.this.getContext(), PhotoPageItem.this.mDataItem.getSize())));
                if (PhotoPageItem.this.isUnsupportedMedia()) {
                    setCheckViewVisible(this.mCheckOriginLayout, false);
                    setCheckViewVisible(this.mCheckRenderLayout, false);
                    return;
                }
                if (originChecked()) {
                    setCheckViewVisible(this.mCheckOriginLayout, false);
                    this.mOriginInterface.setChecked(((Integer) PhotoPageItem.this.getTag(R.id.tag_item_position)).intValue(), PhotoPageItem.this.mDataItem.getKey(), true);
                } else {
                    setCheckViewVisible(this.mCheckOriginLayout, true);
                }
                setCheckViewVisible(this.mCheckRenderLayout, true);
            }
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int id = view.getId();
            if (id != R.id.check_layout) {
                if (id == R.id.check_origin_layout) {
                    toggleCheckBox(this.mOriginCheckBox, this.mOriginInterface);
                    return;
                } else if (id != R.id.check_select_layout) {
                    return;
                }
            }
            LinearMotorHelper.performHapticFeedback(this.mSelectCheckBox, LinearMotorHelper.HAPTIC_MESH_LIGHT);
            toggleCheckBox(this.mSelectCheckBox, this.mSelectInterface);
        }
    }

    /* loaded from: classes2.dex */
    public final class ErrorDrawableManager implements OnPhotoViewTransitionListener {
        public PhotoPageErrorDrawable mErrorDrawable;
        public int mHeight;
        public int mWidth;

        public final boolean isValid(int i) {
            return (i == 0 || i == -1) ? false : true;
        }

        public ErrorDrawableManager() {
            PhotoPageItem.this = r1;
        }

        public final void genErrorDrawableIfNeed() {
            if (this.mErrorDrawable == null) {
                this.mErrorDrawable = new PhotoPageErrorDrawable(PhotoPageItem.this.getContext(), this.mWidth, this.mHeight);
            }
        }

        public void work() {
            genErrorDrawableIfNeed();
            PhotoPageItem.this.mPhotoView.setImageDrawable(this.mErrorDrawable);
            PhotoPageItem.this.mPhotoView.setRotatable(false);
            PhotoPageItem.this.mPhotoView.setZoomable(false);
            PhotoPageItem.this.mPhotoView.setOnPhotoViewTransitionListener(this);
        }

        public void shutDown() {
            if (this.mErrorDrawable != null) {
                this.mErrorDrawable = null;
                PhotoPageItem.this.mPhotoView.setImageDrawable(null);
                PhotoPageItem.this.mPhotoView.setRotatable(true);
                PhotoPageItem.this.mPhotoView.setZoomable(true);
            }
        }

        public void setIcon(Drawable drawable) {
            if (!(drawable instanceof BitmapDrawable)) {
                return;
            }
            genErrorDrawableIfNeed();
            this.mErrorDrawable.setIcon((BitmapDrawable) drawable);
        }

        public void setTip(CharSequence charSequence) {
            if (!TextUtils.isEmpty(charSequence) || this.mErrorDrawable != null) {
                genErrorDrawableIfNeed();
                this.mErrorDrawable.setTip(charSequence);
            }
        }

        @Override // com.github.chrisbanes.photoview.OnPhotoViewTransitionListener
        public void onProgressChanged(float f, boolean z) {
            PhotoPageErrorDrawable photoPageErrorDrawable = this.mErrorDrawable;
            if (photoPageErrorDrawable == null || !z) {
                return;
            }
            photoPageErrorDrawable.setAlpha((int) ((1.0f - f) * 255.0f));
        }

        public void setSource(ImageLoadParams imageLoadParams, BaseDataItem baseDataItem) {
            if (imageLoadParams != null && isValid(imageLoadParams.getImageWidth()) && isValid(imageLoadParams.getImageHeight())) {
                this.mWidth = imageLoadParams.getImageWidth();
                this.mHeight = imageLoadParams.getImageHeight();
            } else if (baseDataItem == null || !isValid(baseDataItem.getWidth()) || !isValid(baseDataItem.getHeight())) {
            } else {
                this.mWidth = baseDataItem.getWidth();
                this.mHeight = baseDataItem.getHeight();
            }
        }
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mCheckManager.inAction()) {
            this.mCheckManager.relayout(this.mPhotoView.getDisplayRect());
        }
    }

    public void dispatchActionBarVisibleChanged(Boolean bool, int i, boolean z) {
        this.isActionBarVisible = bool.booleanValue();
        if (z) {
            onActionBarVisibleChanged(bool, i);
        }
        SpecialTypeEnterManager specialTypeEnterManager = this.mSpecialTypeEnterManager;
        if (specialTypeEnterManager != null) {
            specialTypeEnterManager.update(z);
        }
    }

    public void onActionBarVisibleChanged(Boolean bool, int i) {
        this.mDownloadManager.adjustLocation(bool.booleanValue(), this.mPhotoView.getDisplayRect(), true);
        this.mTipView.adjustLocation(bool.booleanValue(), this.mPhotoView.getDisplayRect(), true);
        this.mSpecialTypeManager.onActionBarVisibleChanged(bool.booleanValue());
        if (this.mIsActionBarFirstInit) {
            this.mIsActionBarFirstInit = false;
        }
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mDownloadManager.adjustLocation(isActionBarVisible(), this.mPhotoView.getDisplayRect(), false);
        this.mTipView.adjustLocation(isActionBarVisible(), this.mPhotoView.getDisplayRect(), false);
        this.mSpecialTypeManager.adjustLocation(isActionBarVisible(), this.mPhotoView.getDisplayRect(), true);
        SpecialTypeEnterManager specialTypeEnterManager = this.mSpecialTypeEnterManager;
        if (specialTypeEnterManager != null) {
            specialTypeEnterManager.update(true);
        }
    }

    public final boolean isActionBarVisible() {
        return this.isActionBarVisible;
    }

    public int getActionBarHeight() {
        PhotoPageAdapter.PhotoPageInteractionListener photoPageInteractionListener = this.mPhotoPageInteractionListener;
        int actionBarHeight = photoPageInteractionListener == null ? 0 : photoPageInteractionListener.getActionBarHeight();
        if (actionBarHeight > 0) {
            return actionBarHeight;
        }
        if (sStatusBarHeight == -1) {
            sStatusBarHeight = MiscUtil.getStatusBarHeight(getContext());
        }
        if (MiscUtil.isLandMode(GalleryApp.sGetAndroidContext()) && !BaseBuildUtil.isLargeScreen(GalleryApp.sGetAndroidContext()) && !BaseBuildUtil.isFoldableDevice()) {
            sStatusBarHeight = MiscUtil.getNotchHeight(GalleryApp.sGetAndroidContext());
        }
        return sStatusBarHeight + PhotoPageActionBarManager.getStationaryActionBarHeight();
    }

    public int getMenuBarHeight() {
        PhotoPageAdapter.PhotoPageInteractionListener photoPageInteractionListener;
        if (sMenuBarHeight == -1 && (photoPageInteractionListener = this.mPhotoPageInteractionListener) != null) {
            sMenuBarHeight = photoPageInteractionListener.getMenuBarHeight();
        }
        return sMenuBarHeight + ViewCompat.getSystemWindowInsetBottom(this);
    }

    public String getContentDescriptionForTalkBack() {
        CheckManager checkManager;
        if (this.mDataItem != null) {
            if (isInActionMode() && (checkManager = this.mCheckManager) != null) {
                return checkManager.getContentDescription();
            }
            return this.mDataItem.getContentDescription(getContext());
        }
        return null;
    }

    public boolean onResourceReady(final String str, final Bitmap bitmap, final boolean z, boolean z2) {
        boolean z3 = !z && setResource(bitmap, z2);
        DefaultLogger.d("PhotoPageItem", "onResourceReady uri[%s], bitmap[%s], isPreview[%b], host [%s]", str, bitmap, Boolean.valueOf(z2), toString());
        if (z2) {
            onCacheImageLoadFinish(z);
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageItem.4
                {
                    PhotoPageItem.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (PhotoPageItem.this.mExternalCacheLoadCallback != null) {
                        PhotoPageItem.this.mExternalCacheLoadCallback.onImageLoadFinish(str, z, bitmap);
                    }
                }
            });
        } else {
            correctVideoMetaData();
            if (!z) {
                onImageLoadFinish(bitmap != null ? ErrorCode.NO_ERROR : ErrorCode.DECODE_ERROR);
            }
            OnImageLoadFinishListener onImageLoadFinishListener = this.mExternalImageLoadCallback;
            if (onImageLoadFinishListener != null) {
                onImageLoadFinishListener.onImageLoadFinish(str, z, bitmap);
            }
        }
        return z3;
    }

    @Override // com.miui.gallery.ui.PhotoPageDisplayHelper.ResourceCallback
    public void onResourceCleared() {
        this.mPhotoView.setImageDrawable(null);
    }

    public boolean setResource(Bitmap bitmap, boolean z) {
        if (bitmap != null) {
            this.mPhotoView.setImageBitmap(bitmap, this.mPhotoView.getDrawable() != null);
            DefaultLogger.d("photoPageStartup", "photoView bind bitmap end");
        } else {
            this.mPhotoView.setImageDrawable(null);
        }
        return true;
    }

    /* loaded from: classes2.dex */
    public class WarnTipView extends AbsPhotoRectAwareManager implements TipView {
        public float mAdjustX;
        public float mAdjustY;
        public Animator mLocationAnimator;
        public WeakReference<RelativeLayout> mRootRef;
        public ImageView mTip;
        public Object mToken;

        public static /* synthetic */ void $r8$lambda$VIEnCPk9_7tY8vt1tON_KxvaWvc(WarnTipView warnTipView, TipDetailDisplay tipDetailDisplay, View view) {
            warnTipView.lambda$showTip$0(tipDetailDisplay, view);
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void changeVisibilityForSpecialScene() {
        }

        public /* synthetic */ WarnTipView(PhotoPageItem photoPageItem, RelativeLayout relativeLayout, AnonymousClass1 anonymousClass1) {
            this(relativeLayout);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public WarnTipView(RelativeLayout relativeLayout) {
            super();
            PhotoPageItem.this = r1;
            this.mRootRef = new WeakReference<>(relativeLayout);
        }

        public final boolean needShowTip() {
            return !PhotoPageItem.this.isInActionMode() && !isRotating() && !isDrawableDisplayInside();
        }

        public final void initErrorView() {
            if (this.mTip == null) {
                ImageView imageView = new ImageView(this.mRootRef.get().getContext());
                this.mTip = imageView;
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                this.mTip.setImageResource(R.drawable.photo_download_error);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(11);
                layoutParams.addRule(12);
                this.mTip.setVisibility(8);
                this.mRootRef.get().addView(this.mTip, layoutParams);
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.TipView
        public <P> boolean showTip(int i, boolean z, final TipDetailDisplay<P> tipDetailDisplay, Object obj) {
            if (needShowTip()) {
                this.mToken = obj;
                initErrorView();
                this.mTip.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PhotoPageItem$WarnTipView$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PhotoPageItem.WarnTipView.$r8$lambda$VIEnCPk9_7tY8vt1tON_KxvaWvc(PhotoPageItem.WarnTipView.this, tipDetailDisplay, view);
                    }
                });
                if (i != 0) {
                    ImageView imageView = this.mTip;
                    imageView.setContentDescription(imageView.getContext().getString(i));
                }
                ImageView imageView2 = this.mTip;
                if (imageView2 != null && imageView2.getVisibility() != 0) {
                    this.mTip.clearAnimation();
                    this.mTip.setVisibility(0);
                    adjustLocation(PhotoPageItem.this.isActionBarVisible(), PhotoPageItem.this.mPhotoView.getDisplayRect(), false);
                    if (!z) {
                        return true;
                    }
                    this.mTip.startAnimation(generateShowAnimation());
                    return true;
                }
            }
            return false;
        }

        public /* synthetic */ void lambda$showTip$0(TipDetailDisplay tipDetailDisplay, View view) {
            if (tipDetailDisplay != null) {
                tipDetailDisplay.display(PhotoPageItem.this.getContext(), tipDetailDisplay.mo1549getParams());
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.TipView
        public boolean hideTip(boolean z, Object obj) {
            ImageView imageView = this.mTip;
            if (imageView == null || imageView.getVisibility() == 8) {
                return false;
            }
            if (obj != this.mToken) {
                DefaultLogger.w("PhotoPageItem", "token doesn't matched");
                return false;
            }
            this.mTip.clearAnimation();
            if (z) {
                Animation generateHideAnimation = generateHideAnimation();
                generateHideAnimation.setAnimationListener(new AnimationListenerAdapter() { // from class: com.miui.gallery.ui.PhotoPageItem.WarnTipView.1
                    {
                        WarnTipView.this = this;
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationEnd(Animation animation) {
                        WarnTipView.this.mTip.setVisibility(8);
                    }
                });
                this.mTip.startAnimation(generateHideAnimation);
                return true;
            }
            this.mTip.setVisibility(8);
            return true;
        }

        public final void cancelLocationAnim() {
            Animator animator = this.mLocationAnimator;
            if (animator == null || !animator.isRunning()) {
                return;
            }
            this.mLocationAnimator.cancel();
            this.mLocationAnimator = null;
            this.mAdjustX = this.mTip.getTranslationX();
            this.mAdjustY = this.mTip.getTranslationY();
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void adjustLocation(boolean z, RectF rectF, boolean z2) {
            ImageView imageView = this.mTip;
            if (imageView == null || imageView.getVisibility() != 0) {
                return;
            }
            cancelLocationAnim();
            float menuBarHeight = (!z || MiscUtil.isLandModeAndSupportVersion(PhotoPageItem.this.getContext())) ? 0.0f : PhotoPageItem.this.getMenuBarHeight();
            float height = rectF != null ? rectF.bottom : PhotoPageItem.this.getHeight();
            float width = rectF != null ? rectF.right : PhotoPageItem.this.getWidth();
            float min = 0.0f - Math.min(getMaxTranslationY(), Math.max(menuBarHeight, PhotoPageItem.this.getHeight() - height) + getVerticalMargin());
            float min2 = 0.0f - Math.min(getMaxTranslationX(), Math.max(PhotoPageItem.this.getWidth() - width, 0.0f) + getHorizontalMargin());
            if (this.mAdjustY == min && this.mAdjustX == min2) {
                return;
            }
            this.mAdjustX = min2;
            this.mAdjustY = min;
            if (z2) {
                AnimatorSet animatorSet = new AnimatorSet();
                ImageView imageView2 = this.mTip;
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(imageView2, "TranslationY", imageView2.getTranslationY(), min);
                ImageView imageView3 = this.mTip;
                animatorSet.playTogether(ObjectAnimator.ofFloat(imageView3, "TranslationX", imageView3.getTranslationX(), min2), ofFloat);
                animatorSet.setDuration(getAdjustAnimDuration(z));
                animatorSet.setInterpolator(getAdjustAnimInterpolator(z));
                this.mLocationAnimator = animatorSet;
                animatorSet.start();
                return;
            }
            this.mTip.setTranslationY(min);
            this.mTip.setTranslationX(min2);
        }
    }

    /* loaded from: classes2.dex */
    public class NoStoragePermissionTipView extends SpecialTypeEnterView implements TipView {
        public Object mToken;

        public static /* synthetic */ void $r8$lambda$6FE8NocnuV1YtyZud4hEZZFCyrU(NoStoragePermissionTipView noStoragePermissionTipView, TipDetailDisplay tipDetailDisplay, View view) {
            noStoragePermissionTipView.lambda$showTip$0(tipDetailDisplay, view);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public NoStoragePermissionTipView(ViewGroup viewGroup, int i, PhotoPagerHelper.OnSpecialTypeEnterListener onSpecialTypeEnterListener) {
            super(viewGroup, i, onSpecialTypeEnterListener);
            PhotoPageItem.this = r1;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.TipView
        public <P> boolean showTip(int i, boolean z, final TipDetailDisplay<P> tipDetailDisplay, Object obj) {
            this.mToken = obj;
            ArrayList arrayList = new ArrayList();
            arrayList.add(new SpecialTypeMediaUtils.SpecialEnterIconAndText(0L, R.drawable.ic_no_storage_permission, R.string.scope_storage_dialog_title));
            update(null, PhotoPageItem.this.getDataItem().isVideo(), arrayList);
            setCommonEnterClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PhotoPageItem$NoStoragePermissionTipView$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PhotoPageItem.NoStoragePermissionTipView.$r8$lambda$6FE8NocnuV1YtyZud4hEZZFCyrU(PhotoPageItem.NoStoragePermissionTipView.this, tipDetailDisplay, view);
                }
            });
            show(z);
            return true;
        }

        public /* synthetic */ void lambda$showTip$0(TipDetailDisplay tipDetailDisplay, View view) {
            if (tipDetailDisplay != null) {
                tipDetailDisplay.display(PhotoPageItem.this.getContext(), tipDetailDisplay.mo1549getParams());
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.TipView
        public boolean hideTip(boolean z, Object obj) {
            View view = this.mCommonEnterView;
            if (view == null || view.getVisibility() == 8) {
                return false;
            }
            if (obj != this.mToken) {
                DefaultLogger.w("PhotoPageItem", "token doesn't matched");
                return false;
            }
            this.mCommonEnterView.clearAnimation();
            hide(z);
            return true;
        }
    }
}
