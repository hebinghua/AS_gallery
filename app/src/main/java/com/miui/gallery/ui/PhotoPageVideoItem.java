package com.miui.gallery.ui;

import android.accounts.Account;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.OnHandleTouchEventListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.OnlineVideoStrategy;
import com.miui.gallery.compat.view.ViewCompat;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.CloudItem;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.preference.MemoryPreferenceHelper;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.ImageDownloader;
import com.miui.gallery.sdk.download.assist.DownloadItem;
import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.ui.PhotoPageItem;
import com.miui.gallery.ui.PhotoPageVideoItem;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.BitmapUtils;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.IncompatibleMediaType;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.VideoPlayerCompat;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.photoview.ItemViewInfo;
import com.miui.gallery.video.online.Error;
import com.miui.gallery.video.online.PlayUrlRequest;
import com.miui.gallery.video.online.UrlRequestError;
import com.miui.gallery.widget.CircleStrokeProgressBar;
import com.miui.gallery.widget.DebounceClickListener;
import java.lang.ref.WeakReference;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class PhotoPageVideoItem extends PhotoPageItem implements OnHandleTouchEventListener {
    public final DebounceClickListener mDebounceClickListener;
    public OnSurfacePreparedListener mOnSurfacePreparedListener;
    public OnThumbnailLoadedListener mOnThumbnailLoadedListener;
    public OnlineVideo mOnlineVideo;
    public boolean mPlayEnable;
    public VideoPreviewManager mPreviewManager;
    public VideoSubtitleManager mSubtitleManager;
    public VideoThumbnailManager mThumbnailManager;
    public View mVideoIcon;
    public VideoIconStateManager mVideoIconStateManager;
    public Runnable mVideoItemRunnable;

    /* loaded from: classes2.dex */
    public interface LoadThumbnailTaskCallback {
        void onThumbnailLoadFinish(Bitmap bitmap);
    }

    /* loaded from: classes2.dex */
    public interface OnSurfacePreparedListener {
        void onSurfacePrepared(Surface surface);
    }

    /* loaded from: classes2.dex */
    public interface OnThumbnailLoadedListener {
        void onThumbnailLoaded(Bitmap bitmap);
    }

    /* loaded from: classes2.dex */
    public interface OnlinePresenter<V extends OnlineView> {
        void getOnlineUrl(OnRequestedOnlineUrl onRequestedOnlineUrl);

        void hideTip();

        void hideVipGuide();

        void onAttachView(V v);

        void onDetachView();

        void onItemUpdate(BaseDataItem baseDataItem);

        void onLoadingClicked();

        void onUpgradeVip();

        void onVipGuideIgnored();

        void showVipGuide();
    }

    /* loaded from: classes2.dex */
    public interface OnlineView<P extends OnlinePresenter> {
        void dismissErrorView();

        void hideLoadingProgress();

        void hideTipView();

        void hideVipGuide();

        void initGuideAnchor(View view);

        void initTipView(PhotoPageItem.TipView tipView);

        void showErrorView(Error error);

        void showLoadingProgress();

        void showNetworkNotice(NetworkConsider.OnConfirmed onConfirmed);

        void showVipGuide();
    }

    public static /* synthetic */ void $r8$lambda$29oVRMr1a2RAbMGyNK5SQYbLnZQ(PhotoPageVideoItem photoPageVideoItem, RequestResult requestResult, OnRequestedOnlineUrl.Extras extras) {
        photoPageVideoItem.lambda$handleFileNotExisting$1(requestResult, extras);
    }

    public static /* synthetic */ void $r8$lambda$WnqLPTddw9V77Qbd3WqYVFt0RPM(PhotoPageVideoItem photoPageVideoItem) {
        photoPageVideoItem.lambda$onFinishInflate$0();
    }

    public PhotoPageVideoItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mPlayEnable = true;
        this.mDebounceClickListener = new DebounceClickListener() { // from class: com.miui.gallery.ui.PhotoPageVideoItem.2
            {
                PhotoPageVideoItem.this = this;
            }

            @Override // com.miui.gallery.widget.DebounceClickListener
            public void onClickConfirmed(View view) {
                BaseDataItem baseDataItem;
                DefaultLogger.d("PhotoPageVideoItem", "onVideoIconClick");
                if (PhotoPageVideoItem.this.mPlayEnable && (baseDataItem = PhotoPageVideoItem.this.mDataItem) != null) {
                    if (!TextUtils.isEmpty(baseDataItem.getOriginalPath())) {
                        PhotoPageVideoItem.this.playVideo("");
                    } else {
                        PhotoPageVideoItem.this.handleFileNotExisting();
                    }
                }
            }
        };
    }

    public View getVideoIcon() {
        return this.mVideoIcon;
    }

    @Override // com.miui.gallery.ui.PhotoPageItem, com.miui.gallery.widget.slip.ISlipAnimView
    public void onSlipping(float f) {
        super.onSlipping(f);
        VideoIconStateManager videoIconStateManager = this.mVideoIconStateManager;
        if (videoIconStateManager != null) {
            videoIconStateManager.onSlipping(f);
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        View findViewById = findViewById(R.id.video_icon);
        this.mVideoIcon = findViewById;
        findViewById.setZ(1.0f);
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageVideoItem$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PhotoPageVideoItem.$r8$lambda$WnqLPTddw9V77Qbd3WqYVFt0RPM(PhotoPageVideoItem.this);
            }
        });
        this.mVideoIcon.setOnClickListener(this.mDebounceClickListener);
        this.mPhotoView.setOnHandleTouchEventListener(this);
        this.mVideoIconStateManager = new VideoIconStateManager(this);
        initForNewPlayer();
    }

    public /* synthetic */ void lambda$onFinishInflate$0() {
        FolmeUtil.setCustomTouchAnim(this.mVideoIcon, new AnimParams.Builder().setTint(0.08f, 0.0f, 0.0f, 0.0f).setScale(0.85f).build(), null, null, true);
    }

    public final boolean initForNewPlayer() {
        if (VideoPlayerCompat.isSupportNewVideoPlayer()) {
            if (this.mPreviewManager == null) {
                this.mPreviewManager = new VideoPreviewManager();
            }
            if (this.mThumbnailManager == null) {
                this.mThumbnailManager = new VideoThumbnailManager();
            }
            if (VideoPlayerCompat.isVideoPlayerSupportEditSubtitle()) {
                this.mSubtitleManager = new VideoSubtitleManager();
            }
            if (this.mOnlineVideo != null) {
                return true;
            }
            OnlineVideo onlineVideo = new OnlineVideo(this);
            this.mOnlineVideo = onlineVideo;
            onlineVideo.initTipView(this.mTipView);
            return true;
        }
        return false;
    }

    public void onMiuiVideoInstalled() {
        if (initForNewPlayer()) {
            if (this.mPhotoView.getDrawable() != null) {
                this.mThumbnailManager.onImageLoadFinish();
            }
            this.mOnlineVideo.onAttachView();
            this.mOnlineVideo.updateItem(this.mDataItem);
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void onImageLoadFinish(ErrorCode errorCode) {
        super.onImageLoadFinish(errorCode);
        setVideoItemVisible(this.mPhotoView.getDrawable() != null);
        if (!isAnimEntering() && !isAnimExiting()) {
            getPreviewSurface();
        }
        VideoThumbnailManager videoThumbnailManager = this.mThumbnailManager;
        if (videoThumbnailManager != null) {
            videoThumbnailManager.onImageLoadFinish();
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void swapItem(BaseDataItem baseDataItem) {
        if (baseDataItem == null || !baseDataItem.equals(this.mDataItem)) {
            VideoIconStateManager videoIconStateManager = this.mVideoIconStateManager;
            if (videoIconStateManager != null) {
                videoIconStateManager.resetIconHideState();
            }
            VideoPreviewManager videoPreviewManager = this.mPreviewManager;
            if (videoPreviewManager != null) {
                videoPreviewManager.onUnSelected(false);
            }
            VideoThumbnailManager videoThumbnailManager = this.mThumbnailManager;
            if (videoThumbnailManager != null) {
                videoThumbnailManager.onItemUpdated();
            }
            VideoSubtitleManager videoSubtitleManager = this.mSubtitleManager;
            if (videoSubtitleManager != null) {
                videoSubtitleManager.onUnSelected(false);
            }
            OnlineVideo onlineVideo = this.mOnlineVideo;
            if (onlineVideo != null) {
                onlineVideo.updateItem(baseDataItem);
            }
        }
        super.swapItem(baseDataItem);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void onActionBarVisibleChanged(Boolean bool, int i) {
        super.onActionBarVisibleChanged(bool, i);
        VideoIconStateManager videoIconStateManager = this.mVideoIconStateManager;
        if (videoIconStateManager != null) {
            videoIconStateManager.onActionBarVisibleChanged();
        }
        VideoSubtitleManager videoSubtitleManager = this.mSubtitleManager;
        if (videoSubtitleManager != null) {
            videoSubtitleManager.onActionBarVisibleChanged();
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public PhotoPageItem.CheckManager createCheckManager() {
        return new VideoCheckManager();
    }

    /* loaded from: classes2.dex */
    public class VideoCheckManager extends PhotoPageItem.CheckManager {
        public View mCheckRenderLayout;
        public TextView mVideoSmallText;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public VideoCheckManager() {
            super();
            PhotoPageVideoItem.this = r1;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.CheckManager
        public void ensureInflated() {
            super.ensureInflated();
            this.mVideoSmallText = (TextView) PhotoPageVideoItem.this.findViewById(R.id.video_small_icon_duration);
            View findViewById = PhotoPageVideoItem.this.findViewById(R.id.check_render_layout);
            this.mCheckRenderLayout = findViewById;
            findViewById.setImportantForAccessibility(2);
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.CheckManager
        public void startCheck(PhotoPageAdapter.ChoiceModeInterface... choiceModeInterfaceArr) {
            super.startCheck(choiceModeInterfaceArr);
            if (PhotoPageVideoItem.this.mVideoIconStateManager != null) {
                PhotoPageVideoItem.this.mVideoIconStateManager.onStartCheck();
            }
            if (PhotoPageVideoItem.this.mDataItem != null) {
                this.mVideoSmallText.setVisibility(0);
                this.mVideoSmallText.setText(FormatUtil.formatVideoDuration(PhotoPageVideoItem.this.mDataItem.getDuration()));
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.CheckManager
        public void refreshCheck(PhotoPageAdapter.ChoiceModeInterface... choiceModeInterfaceArr) {
            super.refreshCheck(choiceModeInterfaceArr);
            if (PhotoPageVideoItem.this.mDataItem != null) {
                this.mVideoSmallText.setVisibility(0);
                this.mVideoSmallText.setText(FormatUtil.formatVideoDuration(PhotoPageVideoItem.this.mDataItem.getDuration()));
                return;
            }
            this.mVideoSmallText.setVisibility(8);
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.CheckManager
        public void endCheck() {
            super.endCheck();
            if (PhotoPageVideoItem.this.mVideoIconStateManager != null) {
                PhotoPageVideoItem.this.mVideoIconStateManager.onStopHandleTouchEvent();
                PhotoPageVideoItem.this.mVideoIconStateManager.onEndCheck();
            }
            TextView textView = this.mVideoSmallText;
            if (textView != null) {
                textView.setVisibility(8);
            }
        }
    }

    public final void handleFileNotExisting() {
        BaseDataItem baseDataItem = this.mDataItem;
        if (baseDataItem != null && baseDataItem.getDownloadUri() != null) {
            if (this.mOnlineVideo != null) {
                this.mVideoIconStateManager.onPreparePlayOnline();
                this.mOnlineVideo.getOnlineUrl(new OnRequestedOnlineUrl() { // from class: com.miui.gallery.ui.PhotoPageVideoItem$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnRequestedOnlineUrl
                    public final void onRequested(PhotoPageVideoItem.RequestResult requestResult, PhotoPageVideoItem.OnRequestedOnlineUrl.Extras extras) {
                        PhotoPageVideoItem.$r8$lambda$29oVRMr1a2RAbMGyNK5SQYbLnZQ(PhotoPageVideoItem.this, requestResult, extras);
                    }
                });
                return;
            }
            this.mDownloadManager.downloadOrigin(true);
            return;
        }
        ToastUtils.makeText(getContext(), (int) R.string.video_not_exist);
    }

    public /* synthetic */ void lambda$handleFileNotExisting$1(RequestResult requestResult, OnRequestedOnlineUrl.Extras extras) {
        if (requestResult == null || TextUtils.isEmpty(requestResult.url)) {
            boolean z = false;
            boolean z2 = extras != null && extras.hasNoticedNetwork;
            if (extras == null || !z2 || extras.isAllowedMetered) {
                z = true;
            }
            if (z) {
                this.mDownloadManager.downloadOrigin(!z2);
            } else {
                this.mVideoIconStateManager.onOnlinePlayerReturn();
            }
        } else if (isPagerSelected()) {
            playVideo(requestResult.url);
        } else {
            this.mVideoIconStateManager.onOnlinePlayerReturn();
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void onActionModeChanged(boolean z) {
        super.onActionModeChanged(z);
        VideoSubtitleManager videoSubtitleManager = this.mSubtitleManager;
        if (videoSubtitleManager != null) {
            videoSubtitleManager.onActionModeChanged(z);
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doOnSelected(boolean z, boolean z2, boolean z3) {
        super.doOnSelected(z, z2, z3);
        VideoIconStateManager videoIconStateManager = this.mVideoIconStateManager;
        if (videoIconStateManager != null) {
            videoIconStateManager.resetIconHideState();
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doOnUnSelected(boolean z, int i) {
        VideoIconStateManager videoIconStateManager;
        super.doOnUnSelected(z, i);
        if (!z && (videoIconStateManager = this.mVideoIconStateManager) != null) {
            videoIconStateManager.resetIconHideState();
        }
        VideoPreviewManager videoPreviewManager = this.mPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.onUnSelected(z);
        }
        VideoSubtitleManager videoSubtitleManager = this.mSubtitleManager;
        if (videoSubtitleManager != null) {
            videoSubtitleManager.onUnSelected(z);
        }
    }

    public void onPreviewUpdate(boolean z) {
        VideoPreviewManager videoPreviewManager = this.mPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.onPreviewUpdate(z);
        }
    }

    public void onPreviewStart() {
        VideoIconStateManager videoIconStateManager = this.mVideoIconStateManager;
        if (videoIconStateManager != null) {
            videoIconStateManager.onPreviewStart();
        }
        VideoPreviewManager videoPreviewManager = this.mPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.onPreviewStart();
        }
    }

    public void onPreviewStop() {
        VideoIconStateManager videoIconStateManager = this.mVideoIconStateManager;
        if (videoIconStateManager != null) {
            videoIconStateManager.onPreviewStop();
        }
        VideoPreviewManager videoPreviewManager = this.mPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.onPreviewStop();
        }
    }

    public void setOnSurfacePreparedListener(OnSurfacePreparedListener onSurfacePreparedListener) {
        this.mOnSurfacePreparedListener = onSurfacePreparedListener;
    }

    public void setOnThumbnailLoadedListener(OnThumbnailLoadedListener onThumbnailLoadedListener) {
        this.mOnThumbnailLoadedListener = onThumbnailLoadedListener;
    }

    public void setPlayEnable(boolean z) {
        this.mPlayEnable = z;
    }

    public final void playVideo(String str) {
        DefaultLogger.d("PhotoPageVideoItem", "playVideo");
        PhotoPageAdapter.PhotoPageInteractionListener photoPageInteractionListener = this.mPhotoPageInteractionListener;
        if (photoPageInteractionListener != null) {
            photoPageInteractionListener.playVideo(this.mDataItem, str);
        } else {
            DefaultLogger.w("PhotoPageVideoItem", "playVideo callback is null");
        }
    }

    public final boolean needShowPlayIcon() {
        PhotoPageItem.DownloadManager downloadManager;
        return !isAnimExiting() && ((downloadManager = this.mDownloadManager) == null || !((VideoDownloadManager) downloadManager).isProgressVisible());
    }

    public final void cancelVideoItemRunnable() {
        if (this.mVideoItemRunnable != null) {
            ThreadManager.getMainHandler().removeCallbacks(this.mVideoItemRunnable);
        }
    }

    public void setVideoItemVisible(boolean z) {
        cancelVideoItemRunnable();
        doSetVideoItemVisible(z);
    }

    public void setVideoItemVisibleDelay(boolean z) {
        cancelVideoItemRunnable();
        this.mVideoItemRunnable = new VideoItemRunnable(z);
        ThreadManager.getMainHandler().postDelayed(this.mVideoItemRunnable, 200L);
    }

    public final void doSetVideoItemVisible(boolean z) {
        if (z && needShowPlayIcon()) {
            this.mVideoIcon.setVisibility(0);
        } else {
            this.mVideoIcon.setVisibility(8);
        }
    }

    /* loaded from: classes2.dex */
    public class VideoItemRunnable implements Runnable {
        public final boolean visible;

        public VideoItemRunnable(boolean z) {
            PhotoPageVideoItem.this = r1;
            this.visible = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            PhotoPageVideoItem.this.doSetVideoItemVisible(this.visible);
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doRelease() {
        cancelVideoItemRunnable();
        VideoIconStateManager videoIconStateManager = this.mVideoIconStateManager;
        if (videoIconStateManager != null) {
            videoIconStateManager.resetIconHideState();
        }
        VideoPreviewManager videoPreviewManager = this.mPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.release();
        }
        this.mOnSurfacePreparedListener = null;
        VideoThumbnailManager videoThumbnailManager = this.mThumbnailManager;
        if (videoThumbnailManager != null) {
            videoThumbnailManager.release();
        }
        this.mOnThumbnailLoadedListener = null;
        VideoSubtitleManager videoSubtitleManager = this.mSubtitleManager;
        if (videoSubtitleManager != null) {
            videoSubtitleManager.release();
        }
        OnlineVideo onlineVideo = this.mOnlineVideo;
        if (onlineVideo != null) {
            onlineVideo.onDetachView();
        }
        PhotoPageItem.DownloadManager downloadManager = this.mDownloadManager;
        if (downloadManager instanceof VideoDownloadManager) {
            ((VideoDownloadManager) downloadManager).release();
        }
        super.doRelease();
    }

    @Override // com.miui.gallery.ui.PhotoPageItem, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        OnlineVideo onlineVideo = this.mOnlineVideo;
        if (onlineVideo != null) {
            onlineVideo.onAttachView();
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void animEnter(ItemViewInfo itemViewInfo, final PhotoPageItem.PhotoTransitionListener photoTransitionListener) {
        PhotoPageItem.PhotoTransitionListener photoTransitionListener2 = new PhotoPageItem.PhotoTransitionListener() { // from class: com.miui.gallery.ui.PhotoPageVideoItem.1
            {
                PhotoPageVideoItem.this = this;
            }

            @Override // com.github.chrisbanes.photoview.TransitionListener
            public void onTransitEnd() {
                PhotoPageVideoItem.this.setVideoItemVisible(true);
                photoTransitionListener.onTransitEnd();
            }

            @Override // com.github.chrisbanes.photoview.TransitionListener
            public void onTransitUpdate(float f) {
                photoTransitionListener.onTransitUpdate(f);
            }
        };
        setVideoItemVisible(false);
        super.animEnter(itemViewInfo, photoTransitionListener2);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void animExit(ItemViewInfo itemViewInfo, PhotoPageItem.PhotoTransitionListener photoTransitionListener) {
        VideoPreviewManager videoPreviewManager = this.mPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.onExit();
        }
        VideoSubtitleManager videoSubtitleManager = this.mSubtitleManager;
        if (videoSubtitleManager != null) {
            videoSubtitleManager.onExit();
        }
        setVideoItemVisible(false);
        super.animExit(itemViewInfo, photoTransitionListener);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        VideoPreviewManager videoPreviewManager = this.mPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.onConfigurationChanged();
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doOnMatrixChanged(RectF rectF) {
        super.doOnMatrixChanged(rectF);
        VideoIconStateManager videoIconStateManager = this.mVideoIconStateManager;
        if (videoIconStateManager != null) {
            videoIconStateManager.onMatrixChanged();
        }
        VideoPreviewManager videoPreviewManager = this.mPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.onMatrixChanged();
        }
        VideoSubtitleManager videoSubtitleManager = this.mSubtitleManager;
        if (videoSubtitleManager != null) {
            videoSubtitleManager.onMatrixChanged(rectF);
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doDisplayImage(String str, RequestOptions requestOptions) {
        VideoPreviewManager videoPreviewManager = this.mPreviewManager;
        if (videoPreviewManager == null || !videoPreviewManager.isShowPreview()) {
            super.doDisplayImage(str, requestOptions);
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem, android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        VideoSubtitleManager videoSubtitleManager = this.mSubtitleManager;
        if (videoSubtitleManager != null) {
            videoSubtitleManager.onLayout();
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public PhotoPageItem.DownloadManager createDownloadManager() {
        return new VideoDownloadManager();
    }

    public final boolean canPlay() {
        if (!hasDecodeAbility()) {
            this.mPhotoView.setImageDrawable(null);
            onImageLoadFinish(ErrorCode.DECODE_ERROR);
            return false;
        }
        BaseDataItem baseDataItem = this.mDataItem;
        return baseDataItem != null && !TextUtils.isEmpty(baseDataItem.getOriginalPath()) && isPagerSelected() && hasWindowFocus();
    }

    public final boolean hasDecodeAbility() {
        BaseDataItem baseDataItem = this.mDataItem;
        return baseDataItem == null || !baseDataItem.is8KVideo() || !IncompatibleMediaType.isUnsupported8kVideo();
    }

    /* loaded from: classes2.dex */
    public class VideoDownloadManager extends PhotoPageItem.DownloadManager {
        public WeakReference<ProgressBar> mOriginProgressBar;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public VideoDownloadManager() {
            super();
            PhotoPageVideoItem.this = r1;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.DownloadManager
        public void doOnDownloaded(DownloadType downloadType, String str) {
            super.doOnDownloaded(downloadType, str);
            if (!DownloadType.isOrigin(downloadType) || !PhotoPageVideoItem.this.canPlay()) {
                return;
            }
            PhotoPageVideoItem.this.playVideo("");
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.DownloadManager
        public void doOnProgressVisibilityChanged(boolean z) {
            super.doOnProgressVisibilityChanged(z);
            if (DownloadType.isOrigin(getCurDownloadType())) {
                if (!z && PhotoPageVideoItem.this.canPlay()) {
                    PhotoPageVideoItem.this.setVideoItemVisibleDelay(!z);
                } else {
                    PhotoPageVideoItem.this.setVideoItemVisible(!z);
                }
                if (z) {
                    PhotoPageVideoItem.this.mErrorDrawableManager.shutDown();
                }
            }
            if (z || PhotoPageVideoItem.this.mOnlineVideo == null) {
                return;
            }
            PhotoPageVideoItem.this.mOnlineVideo.hideVipGuide();
            PhotoPageVideoItem.this.mOnlineVideo.hideTip();
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.DownloadManager
        public void doOnDownloading(DownloadType downloadType, float f) {
            super.doOnDownloading(downloadType, f);
            if (!DownloadType.isOrigin(getCurDownloadType()) || !PhotoPageVideoItem.this.isPagerSelected() || PhotoPageVideoItem.this.mOnlineVideo == null) {
                return;
            }
            PhotoPageVideoItem.this.mOnlineVideo.showVipGuide(getProgressBar());
        }

        public final ProgressBar initOriginProgressBar() {
            CircleStrokeProgressBar circleStrokeProgressBar = new CircleStrokeProgressBar(PhotoPageVideoItem.this.getContext());
            circleStrokeProgressBar.setDrawablesForLevels(new int[]{R.drawable.photo_download_progress_bg_big}, new int[]{R.drawable.photo_download_progress_second_big}, (int[]) null);
            circleStrokeProgressBar.setMiddleStrokeColors(new int[]{PhotoPageVideoItem.this.getResources().getColor(R.color.download_progress_shadow_color)}, PhotoPageVideoItem.this.getResources().getDimensionPixelSize(R.dimen.download_progress_shadow_radius));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(13);
            layoutParams.addRule(13);
            circleStrokeProgressBar.setVisibility(8);
            circleStrokeProgressBar.setId(R.id.download_progress);
            PhotoPageVideoItem.this.addView(circleStrokeProgressBar, layoutParams);
            return circleStrokeProgressBar;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.DownloadManager
        public ProgressBar getProgressBar() {
            if (DownloadType.isOrigin(getCurDownloadType())) {
                WeakReference<ProgressBar> weakReference = this.mOriginProgressBar;
                if (weakReference == null || weakReference.get() == null) {
                    this.mOriginProgressBar = new WeakReference<>(initOriginProgressBar());
                }
                return this.mOriginProgressBar.get();
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
                return !PhotoPageVideoItem.this.isInActionMode() && !isDrawableDisplayInside();
            }
            return super.needShowDownloadView();
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.DownloadManager
        public boolean filterError(ErrorCode errorCode) {
            return errorCode == ErrorCode.DECODE_ERROR && PhotoPageVideoItem.this.mPhotoView.getDrawable() != null;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.DownloadManager
        public CharSequence getErrorTip() {
            return DownloadType.isOrigin(getCurDownloadType()) ? PhotoPageVideoItem.this.getResources().getString(R.string.download_error_format, PhotoPageVideoItem.this.getResources().getString(R.string.origin_video_name)) : super.getErrorTip();
        }

        public boolean isProgressVisible() {
            WeakReference<ProgressBar> weakReference = this.mOriginProgressBar;
            return (weakReference == null || weakReference.get() == null || this.mOriginProgressBar.get().getVisibility() != 0) ? false : true;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.DownloadManager, com.miui.gallery.ui.PhotoPageItem.AbsPhotoRectAwareManager
        public void release() {
            super.release();
            WeakReference<ProgressBar> weakReference = this.mOriginProgressBar;
            if (weakReference != null) {
                weakReference.clear();
                this.mOriginProgressBar = null;
            }
        }
    }

    @Override // com.github.chrisbanes.photoview.OnHandleTouchEventListener
    public void onStartHandleTouchEvent() {
        VideoIconStateManager videoIconStateManager = this.mVideoIconStateManager;
        if (videoIconStateManager != null) {
            videoIconStateManager.onStartHandleTouchEvent();
        }
    }

    @Override // com.github.chrisbanes.photoview.OnHandleTouchEventListener
    public void onStopHandleTouchEvent() {
        VideoIconStateManager videoIconStateManager = this.mVideoIconStateManager;
        if (videoIconStateManager != null) {
            videoIconStateManager.onStopHandleTouchEvent();
        }
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public boolean isSupportZoom() {
        return this.mPreviewManager != null && VideoPlayerCompat.isVideoPlayerSupportZoom();
    }

    public Matrix getVideoZoomMatrix() {
        PhotoView photoView = this.mPhotoView;
        if (photoView == null) {
            return null;
        }
        return photoView.getSuppMatrix();
    }

    private void setVideoZoomMatrix(Matrix matrix) {
        if (this.mPhotoView == null || matrix == null) {
            return;
        }
        DefaultLogger.d("PhotoPageVideoItem", "setVideoZoomMatrix");
        this.mPhotoView.setSuppMatrix(matrix);
    }

    public void onVideoPlayerStarted() {
        VideoIconStateManager videoIconStateManager = this.mVideoIconStateManager;
        if (videoIconStateManager != null) {
            videoIconStateManager.onPlayerStarted();
        }
    }

    public void onVideoPlayerReturn(boolean z, Matrix matrix) {
        VideoIconStateManager videoIconStateManager;
        if (z && (videoIconStateManager = this.mVideoIconStateManager) != null) {
            videoIconStateManager.onPlayerReturn();
        }
        setVideoZoomMatrix(matrix);
    }

    public void onProgressChanged() {
        VideoIconStateManager videoIconStateManager = this.mVideoIconStateManager;
        if (videoIconStateManager != null) {
            videoIconStateManager.onProgressChanged();
        }
    }

    public Surface getPreviewSurface() {
        VideoPreviewManager videoPreviewManager = this.mPreviewManager;
        if (videoPreviewManager != null) {
            return videoPreviewManager.getSurface();
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public class VideoPreviewManager {
        public int mBaseHeight;
        public int mBaseWidth;
        public Bitmap mBitmap;
        public int mDisplayHeight;
        public int mDisplayWidth;
        public Runnable mHideRunnable;
        public boolean mIsPreviewUpdated;
        public boolean mIsShowPreview;
        public RelativeLayout.LayoutParams mLayoutParams;
        public boolean mPreviewStarted;
        public Surface mSurface;
        public TextureView.SurfaceTextureListener mSurfaceTextureListener;
        public TextureView mTextureView;
        public RectF mVideoRect;

        public static /* synthetic */ void $r8$lambda$3AD8f2AkQHMw0FJPgTr3XaYPY6s(VideoPreviewManager videoPreviewManager) {
            videoPreviewManager.lambda$new$0();
        }

        public VideoPreviewManager() {
            PhotoPageVideoItem.this = r1;
            this.mHideRunnable = new Runnable() { // from class: com.miui.gallery.ui.PhotoPageVideoItem$VideoPreviewManager$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageVideoItem.VideoPreviewManager.$r8$lambda$3AD8f2AkQHMw0FJPgTr3XaYPY6s(PhotoPageVideoItem.VideoPreviewManager.this);
                }
            };
            this.mSurfaceTextureListener = new TextureView.SurfaceTextureListener() { // from class: com.miui.gallery.ui.PhotoPageVideoItem.VideoPreviewManager.1
                @Override // android.view.TextureView.SurfaceTextureListener
                public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
                }

                @Override // android.view.TextureView.SurfaceTextureListener
                public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                }

                {
                    VideoPreviewManager.this = this;
                }

                @Override // android.view.TextureView.SurfaceTextureListener
                public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
                    VideoPreviewManager.this.mSurface = new Surface(surfaceTexture);
                    if (PhotoPageVideoItem.this.mOnSurfacePreparedListener != null) {
                        PhotoPageVideoItem.this.mOnSurfacePreparedListener.onSurfacePrepared(VideoPreviewManager.this.mSurface);
                    }
                }

                @Override // android.view.TextureView.SurfaceTextureListener
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                    VideoPreviewManager.this.mSurface = null;
                    return true;
                }
            };
        }

        public void onPreviewStart() {
            this.mPreviewStarted = true;
            TextureView textureView = this.mTextureView;
            if (textureView != null) {
                textureView.removeCallbacks(this.mHideRunnable);
            }
        }

        public void onPreviewStop() {
            this.mPreviewStarted = false;
            hideTextureViewDelay();
        }

        public void onPreviewUpdate(boolean z) {
            if (this.mSurface == null) {
                return;
            }
            this.mIsPreviewUpdated = true;
            if (z && !this.mPreviewStarted) {
                hideTextureView();
            } else {
                PhotoPageVideoItem.this.mPhotoView.setVisibility(8);
            }
        }

        public Surface getSurface() {
            addTextureView();
            return this.mSurface;
        }

        public boolean isShowPreview() {
            return this.mIsShowPreview;
        }

        public void onUnSelected(boolean z) {
            removeTextureView("unSelected");
            if (!z) {
                PhotoView photoView = PhotoPageVideoItem.this.mPhotoView;
                if (photoView != null) {
                    photoView.resetMatrix();
                }
                if (!this.mIsShowPreview) {
                    return;
                }
                this.mIsShowPreview = false;
                if (this.mBitmap == null) {
                    return;
                }
                DefaultLogger.d("VideoItemPreviewManager", "refresh");
                PhotoPageVideoItem.this.refreshItem();
                this.mBitmap = null;
            }
        }

        public void onMatrixChanged() {
            RectF videoRect;
            if (this.mTextureView == null || (videoRect = getVideoRect()) == null) {
                return;
            }
            if (((int) videoRect.width()) == this.mDisplayWidth && ((int) videoRect.height()) == this.mDisplayHeight) {
                return;
            }
            removeTextureView("matrixChanged");
        }

        public void onExit() {
            removeTextureView("exit");
        }

        public void onConfigurationChanged() {
            removeTextureView("configChanged");
        }

        public final void addTextureView() {
            RectF baseDisplayRect;
            if (this.mTextureView != null) {
                return;
            }
            DefaultLogger.d("VideoItemPreviewManager", "addTextureView");
            Drawable drawable = PhotoPageVideoItem.this.mPhotoView.getDrawable();
            if (drawable == null || (baseDisplayRect = PhotoPageVideoItem.this.mPhotoView.getBaseDisplayRect()) == null) {
                return;
            }
            this.mBaseWidth = (int) Math.ceil(baseDisplayRect.width());
            this.mBaseHeight = (int) Math.ceil(baseDisplayRect.height());
            RectF rectF = new RectF(0.0f, 0.0f, PhotoPageVideoItem.this.mPhotoView.getWidth(), PhotoPageVideoItem.this.mPhotoView.getHeight());
            RectF rectF2 = new RectF(0.0f, 0.0f, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            Matrix displayMatrix = PhotoPageVideoItem.this.mPhotoView.getDisplayMatrix();
            RectF rectF3 = new RectF(rectF2);
            displayMatrix.mapRect(rectF3);
            this.mDisplayWidth = (int) rectF3.width();
            this.mDisplayHeight = (int) rectF3.height();
            rectF3.setIntersect(rectF3, rectF);
            rectF3.offset(-rectF3.left, -rectF3.top);
            Matrix matrix = new Matrix();
            matrix.setRectToRect(rectF3, rectF2, Matrix.ScaleToFit.FILL);
            matrix.postConcat(displayMatrix);
            matrix.postTranslate(rectF3.centerX() - rectF.centerX(), rectF3.centerY() - rectF.centerY());
            if (this.mLayoutParams == null) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
                this.mLayoutParams = layoutParams;
                layoutParams.addRule(13);
            }
            this.mLayoutParams.width = (int) rectF3.width();
            this.mLayoutParams.height = (int) rectF3.height();
            TextureView textureView = new TextureView(PhotoPageVideoItem.this.getContext());
            this.mTextureView = textureView;
            textureView.setTransform(matrix);
            this.mTextureView.setSurfaceTextureListener(this.mSurfaceTextureListener);
            PhotoPageVideoItem.this.addView(this.mTextureView, 0, this.mLayoutParams);
        }

        public final RectF getVideoRect() {
            Drawable drawable = PhotoPageVideoItem.this.mPhotoView.getDrawable();
            if (drawable == null) {
                return null;
            }
            if (this.mVideoRect == null) {
                this.mVideoRect = new RectF();
            }
            this.mVideoRect.set(0.0f, 0.0f, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            PhotoPageVideoItem.this.mPhotoView.getDisplayMatrix().mapRect(this.mVideoRect);
            return this.mVideoRect;
        }

        public final void hideTextureViewDelay() {
            TextureView textureView = this.mTextureView;
            if (textureView == null) {
                return;
            }
            textureView.removeCallbacks(this.mHideRunnable);
            this.mTextureView.postDelayed(this.mHideRunnable, 100L);
        }

        public /* synthetic */ void lambda$new$0() {
            DefaultLogger.w("VideoItemPreviewManager", "hideTextureViewDelay");
            hideTextureView();
        }

        public final void hideTextureView() {
            if (this.mSurface == null) {
                return;
            }
            DefaultLogger.d("VideoItemPreviewManager", "hideTextureView");
            this.mTextureView.removeCallbacks(this.mHideRunnable);
            PhotoPageVideoItem.this.mPhotoView.setVisibility(0);
            if (!this.mIsPreviewUpdated) {
                return;
            }
            this.mIsPreviewUpdated = false;
            Bitmap bitmap = this.mBitmap;
            if (bitmap == null || bitmap.getWidth() != this.mBaseWidth || this.mBitmap.getHeight() != this.mBaseHeight) {
                this.mBitmap = Bitmap.createBitmap(PhotoPageVideoItem.this.getResources().getDisplayMetrics(), this.mBaseWidth, this.mBaseHeight, Bitmap.Config.ARGB_8888);
            }
            this.mTextureView.getBitmap(this.mBitmap);
            this.mIsShowPreview = true;
            PhotoPageVideoItem.this.mPhotoView.setImageBitmap(this.mBitmap, true);
        }

        public final void removeTextureView(String str) {
            if (this.mTextureView == null) {
                return;
            }
            DefaultLogger.d("VideoItemPreviewManager", "removeTextureView %s", str);
            hideTextureView();
            if (this.mTextureView == null) {
                return;
            }
            PhotoPageVideoItem.this.mPhotoView.setVisibility(0);
            this.mTextureView.setSurfaceTextureListener(null);
            this.mTextureView.removeCallbacks(this.mHideRunnable);
            PhotoPageVideoItem.this.removeView(this.mTextureView);
            this.mPreviewStarted = false;
            this.mIsPreviewUpdated = false;
            this.mSurface = null;
            this.mTextureView = null;
        }

        public final void release() {
            this.mIsShowPreview = false;
            removeTextureView("release");
        }
    }

    public void updateSubtitleView(String str, Typeface typeface) {
        VideoSubtitleManager videoSubtitleManager = this.mSubtitleManager;
        if (videoSubtitleManager != null) {
            videoSubtitleManager.update(str, typeface);
        }
    }

    /* loaded from: classes2.dex */
    public class VideoSubtitleManager {
        public RectF mBaseRect;
        public RelativeLayout.LayoutParams mLayoutParams;
        public int mSystemWindowInsetBottom;
        public TextView mTextView;
        public int mTextViewTop;
        public int mToBottomOfParent;
        public int mToBottomOfParentLandSpace;
        public RectF mVideoRect;

        /* renamed from: $r8$lambda$ycXrK8mR5B_q_AccvE4Ld7xa-8o */
        public static /* synthetic */ WindowInsetsCompat m1553$r8$lambda$ycXrK8mR5B_q_AccvE4Ld7xa8o(VideoSubtitleManager videoSubtitleManager, View view, WindowInsetsCompat windowInsetsCompat) {
            return videoSubtitleManager.lambda$setOnApplyWindowInsetsListener$0(view, windowInsetsCompat);
        }

        public final float getBottomRatio(boolean z, float f, boolean z2) {
            if (z2) {
                if (f >= 1.7f) {
                    return 0.03888889f;
                }
                return z ? 0.16203703f : 0.055555556f;
            } else if (f >= 1.7f) {
                return 0.06574074f;
            } else {
                return z ? 0.08981481f : 0.02962963f;
            }
        }

        public VideoSubtitleManager() {
            PhotoPageVideoItem.this = r1;
        }

        public void update(String str, Typeface typeface) {
            addTextView();
            TextView textView = this.mTextView;
            if (textView != null) {
                if (typeface != null) {
                    textView.setTypeface(typeface);
                }
                this.mTextView.setText(str);
            }
        }

        public void onExit() {
            removeTextView("onExit");
        }

        public void onUnSelected(boolean z) {
            if (!z) {
                removeTextView("onUnSelected");
            }
        }

        public void onMatrixChanged(RectF rectF) {
            RectF baseVideoRect;
            TextView textView = this.mTextView;
            if (textView == null || textView.getVisibility() != 0 || (baseVideoRect = getBaseVideoRect()) == null) {
                return;
            }
            if (!baseVideoRect.equals(this.mBaseRect)) {
                if (this.mBaseRect == null) {
                    this.mBaseRect = new RectF();
                }
                this.mBaseRect.set(baseVideoRect);
                updateTextViewLayout(baseVideoRect);
                this.mTextView.setTranslationX(0.0f);
                this.mTextView.setTranslationY(0.0f);
                this.mTextView.setScaleX(1.0f);
                this.mTextView.setScaleY(1.0f);
            } else if (this.mTextView.getWidth() == 0 || rectF == null) {
            } else {
                float scale = PhotoPageVideoItem.this.mPhotoView.getScale();
                int width = (int) (((rectF.width() - this.mTextView.getWidth()) / 2.0f) + rectF.left);
                int height = (int) ((rectF.height() - ((int) (this.mTextView.getHeight() * scale))) + rectF.top);
                TextView textView2 = this.mTextView;
                textView2.setTranslationX(width - textView2.getLeft());
                TextView textView3 = this.mTextView;
                textView3.setTranslationY(height - textView3.getTop());
                this.mTextView.setScaleX(scale);
                this.mTextView.setScaleY(scale);
            }
        }

        public void onLayout() {
            int top;
            TextView textView = this.mTextView;
            if (textView == null || (top = textView.getTop()) == this.mTextViewTop) {
                return;
            }
            onMatrixChanged(PhotoPageVideoItem.this.mPhotoView.getDisplayRect());
            this.mTextViewTop = top;
        }

        public void onActionModeChanged(boolean z) {
            TextView textView = this.mTextView;
            if (textView != null) {
                textView.setVisibility(z ? 4 : 0);
            }
        }

        public void onApplyWindowInsets() {
            updateTextViewLayout(getBaseVideoRect());
        }

        public void onActionBarVisibleChanged() {
            TextView textView = this.mTextView;
            if (textView == null) {
                return;
            }
            int paddingBottom = textView.getPaddingBottom();
            updateTextViewLayout(getBaseVideoRect());
            if (paddingBottom == this.mTextView.getPaddingBottom()) {
                return;
            }
            this.mTextView.setAlpha(0.0f);
            this.mTextView.animate().alpha(1.0f).setDuration(250L).start();
        }

        public final void addTextView() {
            RectF baseVideoRect;
            if (this.mTextView == null && (baseVideoRect = getBaseVideoRect()) != null) {
                this.mToBottomOfParent = PhotoPageVideoItem.this.getResources().getDimensionPixelSize(R.dimen.video_subtitle_margin_bottom);
                this.mToBottomOfParentLandSpace = PhotoPageVideoItem.this.getResources().getDimensionPixelSize(R.dimen.video_subtitle_margin_bottom_land_space);
                int insetBottom = getInsetBottom(PhotoPageVideoItem.this.mPhotoView);
                if (insetBottom == 0) {
                    insetBottom = MiscUtil.getNotchHeight(PhotoPageVideoItem.this.getContext());
                }
                this.mToBottomOfParentLandSpace += insetBottom;
                TextView textView = new TextView(PhotoPageVideoItem.this.getContext());
                this.mTextView = textView;
                textView.setShadowLayer(0.3f, 1.0f, 1.0f, Integer.MIN_VALUE);
                this.mTextView.setGravity(1);
                this.mTextView.setMaxLines(2);
                this.mTextView.setEllipsize(TextUtils.TruncateAt.END);
                this.mTextView.setTextColor(-1);
                updateTextViewLayout(baseVideoRect);
                PhotoPageVideoItem.this.addView(this.mTextView, this.mLayoutParams);
                setOnApplyWindowInsetsListener();
                PhotoPageItem.CheckManager checkManager = PhotoPageVideoItem.this.mCheckManager;
                if (checkManager == null) {
                    return;
                }
                onActionModeChanged(checkManager.inAction());
            }
        }

        public final void removeTextView(String str) {
            if (this.mTextView == null) {
                return;
            }
            DefaultLogger.d("VideoSubtitleManager", "removeTextView %s", str);
            PhotoPageVideoItem.this.removeView(this.mTextView);
            this.mTextView = null;
        }

        public final RectF getBaseVideoRect() {
            Drawable drawable = PhotoPageVideoItem.this.mPhotoView.getDrawable();
            if (drawable == null) {
                return null;
            }
            if (this.mVideoRect == null) {
                this.mVideoRect = new RectF();
            }
            this.mVideoRect.set(0.0f, 0.0f, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            PhotoPageVideoItem.this.mPhotoView.getBaseMatrix().mapRect(this.mVideoRect);
            return this.mVideoRect;
        }

        public final void updateTextViewLayout(RectF rectF) {
            if (this.mTextView == null || rectF == null) {
                return;
            }
            updateTextViewStyle(rectF);
            if (this.mLayoutParams == null) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                this.mLayoutParams = layoutParams;
                layoutParams.addRule(14);
                this.mLayoutParams.addRule(12);
            }
            this.mLayoutParams.setMargins(0, 0, 0, (int) (PhotoPageVideoItem.this.getHeight() - rectF.bottom));
            this.mTextView.setMaxWidth((int) rectF.width());
            this.mTextView.requestLayout();
        }

        public final void updateTextViewStyle(RectF rectF) {
            float width = rectF.width();
            float height = rectF.height() / width;
            int i = (height > 1.7f ? 1 : (height == 1.7f ? 0 : -1));
            int i2 = i >= 0 ? 40 : 36;
            int i3 = (int) ((i >= 0 ? 0.19814815f : 0.22685185f) * width);
            boolean z = PhotoPageVideoItem.this.getResources().getConfiguration().orientation == 2;
            int paddingBottom = getPaddingBottom(height, z, rectF);
            float f = z ? 2.0f : i >= 0 ? 9.0f : 3.0f;
            this.mTextView.setPadding(i3, 0, i3, paddingBottom);
            this.mTextView.setTextSize(0, (width * i2) / 1080.0f);
            this.mTextView.setLineSpacing(f, 1.0f);
            this.mTextView.setLetterSpacing(0.0375f);
        }

        public final int getPaddingBottom(float f, boolean z, RectF rectF) {
            float bottomRatio;
            float width;
            BaseDataItem baseDataItem = PhotoPageVideoItem.this.mDataItem;
            boolean z2 = baseDataItem != null && baseDataItem.isMovieVideo();
            if (z) {
                bottomRatio = getBottomRatio(z2, f, true);
                width = rectF.height();
            } else {
                bottomRatio = getBottomRatio(z2, f, false);
                width = rectF.width();
            }
            int i = (int) (bottomRatio * width);
            if (!PhotoPageVideoItem.this.isActionBarVisible()) {
                return i;
            }
            return (int) Math.max((z ? this.mToBottomOfParentLandSpace : this.mToBottomOfParent + getInsetBottom(this.mTextView)) - (PhotoPageVideoItem.this.getHeight() - rectF.bottom), i);
        }

        public final int getInsetBottom(View view) {
            return ViewCompat.getSystemWindowInsetBottom(view);
        }

        public final void setOnApplyWindowInsetsListener() {
            TextView textView = this.mTextView;
            if (textView == null) {
                return;
            }
            androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(textView, new OnApplyWindowInsetsListener() { // from class: com.miui.gallery.ui.PhotoPageVideoItem$VideoSubtitleManager$$ExternalSyntheticLambda0
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                    return PhotoPageVideoItem.VideoSubtitleManager.m1553$r8$lambda$ycXrK8mR5B_q_AccvE4Ld7xa8o(PhotoPageVideoItem.VideoSubtitleManager.this, view, windowInsetsCompat);
                }
            });
        }

        public /* synthetic */ WindowInsetsCompat lambda$setOnApplyWindowInsetsListener$0(View view, WindowInsetsCompat windowInsetsCompat) {
            int systemWindowInsetBottom = ViewCompat.getSystemWindowInsetBottom(this.mTextView);
            if (systemWindowInsetBottom != this.mSystemWindowInsetBottom) {
                this.mSystemWindowInsetBottom = systemWindowInsetBottom;
                onApplyWindowInsets();
            }
            return windowInsetsCompat;
        }

        public void release() {
            removeTextView("release");
        }
    }

    public Bitmap getVideoThumbnail() {
        VideoThumbnailManager videoThumbnailManager = this.mThumbnailManager;
        if (videoThumbnailManager != null) {
            return videoThumbnailManager.getVideoThumbnail();
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public class VideoThumbnailManager {
        public LoadThumbnailTask mLoadThumbnailTask;
        public int mThumbItemHeight;
        public int mThumbItemWidth;
        public Bitmap mVideoThumbnail;

        /* renamed from: $r8$lambda$z9bpB2mB_hek_36xe-NUpfhrxk0 */
        public static /* synthetic */ void m1554$r8$lambda$z9bpB2mB_hek_36xeNUpfhrxk0(VideoThumbnailManager videoThumbnailManager, Bitmap bitmap) {
            videoThumbnailManager.lambda$startLoadThumbnail$0(bitmap);
        }

        public VideoThumbnailManager() {
            PhotoPageVideoItem.this = r3;
            this.mThumbItemWidth = r3.getResources().getDimensionPixelSize(R.dimen.video_frame_thumb_width);
            this.mThumbItemHeight = r3.getResources().getDimensionPixelSize(R.dimen.video_frame_thumb_height);
        }

        public void onImageLoadFinish() {
            startLoadThumbnail();
        }

        public void onItemUpdated() {
            cancelLoadThumbnail();
            this.mVideoThumbnail = null;
        }

        public Bitmap getVideoThumbnail() {
            return this.mVideoThumbnail;
        }

        public final void startLoadThumbnail() {
            PhotoView photoView;
            if (this.mVideoThumbnail != null || (photoView = PhotoPageVideoItem.this.mPhotoView) == null) {
                return;
            }
            Drawable drawable = photoView.getDrawable();
            Bitmap bitmap = drawable instanceof BitmapDrawable ? ((BitmapDrawable) drawable).getBitmap() : null;
            if (bitmap == null) {
                return;
            }
            if (this.mLoadThumbnailTask == null) {
                this.mLoadThumbnailTask = new LoadThumbnailTask();
            }
            this.mLoadThumbnailTask.load(bitmap, this.mThumbItemWidth, this.mThumbItemHeight, new LoadThumbnailTaskCallback() { // from class: com.miui.gallery.ui.PhotoPageVideoItem$VideoThumbnailManager$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.PhotoPageVideoItem.LoadThumbnailTaskCallback
                public final void onThumbnailLoadFinish(Bitmap bitmap2) {
                    PhotoPageVideoItem.VideoThumbnailManager.m1554$r8$lambda$z9bpB2mB_hek_36xeNUpfhrxk0(PhotoPageVideoItem.VideoThumbnailManager.this, bitmap2);
                }
            });
        }

        public /* synthetic */ void lambda$startLoadThumbnail$0(Bitmap bitmap) {
            this.mVideoThumbnail = bitmap;
            if (PhotoPageVideoItem.this.mOnThumbnailLoadedListener != null) {
                PhotoPageVideoItem.this.mOnThumbnailLoadedListener.onThumbnailLoaded(bitmap);
            }
        }

        public final void cancelLoadThumbnail() {
            LoadThumbnailTask loadThumbnailTask = this.mLoadThumbnailTask;
            if (loadThumbnailTask != null) {
                loadThumbnailTask.release();
                this.mLoadThumbnailTask = null;
            }
        }

        public void release() {
            cancelLoadThumbnail();
            this.mVideoThumbnail = null;
        }
    }

    /* loaded from: classes2.dex */
    public static class LoadThumbnailTask {
        public LoadThumbnailTaskCallback mCallback;
        public Future mFuture;

        public static /* synthetic */ Bitmap $r8$lambda$YxNGQ80yL8vpc2kUhxMSswYWqWU(Bitmap bitmap, int i, int i2, ThreadPool.JobContext jobContext) {
            return lambda$load$0(bitmap, i, i2, jobContext);
        }

        public LoadThumbnailTask() {
        }

        public void load(final Bitmap bitmap, final int i, final int i2, LoadThumbnailTaskCallback loadThumbnailTaskCallback) {
            Future future = this.mFuture;
            if (future != null) {
                future.cancel();
            }
            this.mCallback = loadThumbnailTaskCallback;
            this.mFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.ui.PhotoPageVideoItem$LoadThumbnailTask$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public final Object mo1807run(ThreadPool.JobContext jobContext) {
                    return PhotoPageVideoItem.LoadThumbnailTask.$r8$lambda$YxNGQ80yL8vpc2kUhxMSswYWqWU(bitmap, i, i2, jobContext);
                }
            }, new FutureHandler<Bitmap>() { // from class: com.miui.gallery.ui.PhotoPageVideoItem.LoadThumbnailTask.1
                {
                    LoadThumbnailTask.this = this;
                }

                @Override // com.miui.gallery.concurrent.FutureHandler
                public void onPostExecute(Future<Bitmap> future2) {
                    if (future2.isCancelled() || LoadThumbnailTask.this.mCallback == null) {
                        return;
                    }
                    LoadThumbnailTask.this.mCallback.onThumbnailLoadFinish(future2.get());
                }
            });
        }

        public static /* synthetic */ Bitmap lambda$load$0(Bitmap bitmap, int i, int i2, ThreadPool.JobContext jobContext) {
            if (jobContext.isCancelled()) {
                return null;
            }
            return BitmapUtils.createScaledBitmap(bitmap, i, i2);
        }

        public void release() {
            Future future = this.mFuture;
            if (future != null) {
                future.cancel();
                this.mFuture = null;
            }
            this.mCallback = null;
        }
    }

    /* loaded from: classes2.dex */
    public static class OnlineVideo {
        public OnlineView mOnlineView;
        public OnlinePresenter mPresenter;

        public OnlineVideo(RelativeLayout relativeLayout) {
            OnlinePresenterImpl onlinePresenterImpl = new OnlinePresenterImpl();
            this.mPresenter = onlinePresenterImpl;
            this.mOnlineView = new OnlineViewImpl(relativeLayout, onlinePresenterImpl);
        }

        public void initTipView(PhotoPageItem.TipView tipView) {
            this.mOnlineView.initTipView(tipView);
        }

        public void showVipGuide(View view) {
            this.mOnlineView.initGuideAnchor(view);
            this.mPresenter.showVipGuide();
        }

        public void hideVipGuide() {
            this.mPresenter.hideVipGuide();
        }

        public void hideTip() {
            this.mPresenter.hideTip();
        }

        public void getOnlineUrl(OnRequestedOnlineUrl onRequestedOnlineUrl) {
            this.mPresenter.getOnlineUrl(onRequestedOnlineUrl);
        }

        public void updateItem(BaseDataItem baseDataItem) {
            this.mPresenter.onItemUpdate(baseDataItem);
        }

        public void onDetachView() {
            this.mPresenter.onDetachView();
        }

        public void onAttachView() {
            this.mPresenter.onAttachView(this.mOnlineView);
        }
    }

    /* loaded from: classes2.dex */
    public static class OnlineViewImpl implements OnlineView<OnlinePresenter> {
        public WeakReference<View> mAnchorRef;
        public Dialog mErrorDialog;
        public View mLoadingProgress;
        public OnlinePresenter mPresenter;
        public WeakReference<RelativeLayout> mRootRef;
        public ViewGroup mTip;
        public WeakReference<PhotoPageItem.TipView> mTipRef;
        public final Object mTipToken = new Object();

        /* renamed from: $r8$lambda$Ddjiw-tGAOUvMhM2vQqdGo06zoc */
        public static /* synthetic */ void m1551$r8$lambda$DdjiwtGAOUvMhM2vQqdGo06zoc(OnlineViewImpl onlineViewImpl, View view) {
            onlineViewImpl.lambda$showLoadingProgress$0(view);
        }

        /* renamed from: $r8$lambda$XHQpyTbo-Nrma-0NTGOs6whTD1k */
        public static /* synthetic */ void m1552$r8$lambda$XHQpyTboNrma0NTGOs6whTD1k(OnlineViewImpl onlineViewImpl, View view) {
            onlineViewImpl.lambda$inflateTip$2(view);
        }

        public static /* synthetic */ void $r8$lambda$o99CLB6uPP9s5tjBiZazNaG6IiA(OnlineViewImpl onlineViewImpl, View view) {
            onlineViewImpl.lambda$inflateTip$1(view);
        }

        public OnlineViewImpl(RelativeLayout relativeLayout, OnlinePresenter onlinePresenter) {
            this.mRootRef = new WeakReference<>(relativeLayout);
            this.mPresenter = onlinePresenter;
        }

        public final View inflateLoadingProgress() {
            RelativeLayout relativeLayout = this.mRootRef.get();
            View inflate = LayoutInflater.from(relativeLayout.getContext()).inflate(R.layout.online_video_loading, (ViewGroup) relativeLayout, false);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) inflate.getLayoutParams();
            layoutParams.addRule(13);
            relativeLayout.addView(inflate, layoutParams);
            return inflate;
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlineView
        public void initGuideAnchor(View view) {
            this.mAnchorRef = new WeakReference<>(view);
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlineView
        public void showLoadingProgress() {
            if (this.mLoadingProgress == null) {
                View inflateLoadingProgress = inflateLoadingProgress();
                this.mLoadingProgress = inflateLoadingProgress;
                inflateLoadingProgress.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PhotoPageVideoItem$OnlineViewImpl$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PhotoPageVideoItem.OnlineViewImpl.m1551$r8$lambda$DdjiwtGAOUvMhM2vQqdGo06zoc(PhotoPageVideoItem.OnlineViewImpl.this, view);
                    }
                });
            }
            this.mLoadingProgress.setVisibility(0);
        }

        public /* synthetic */ void lambda$showLoadingProgress$0(View view) {
            this.mPresenter.onLoadingClicked();
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlineView
        public void hideLoadingProgress() {
            View view = this.mLoadingProgress;
            if (view != null) {
                view.setVisibility(8);
            }
        }

        public final ViewGroup inflateTip(View view) {
            Context context = view.getContext();
            Resources resources = context.getResources();
            RelativeLayout relativeLayout = this.mRootRef.get();
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.online_video_vip_tip, (ViewGroup) relativeLayout, false);
            TextView textView = (TextView) viewGroup.findViewById(R.id.tip_text);
            Drawable drawable = context.getDrawable(R.drawable.ic_vip_tip_lightning);
            int dimensionPixelOffset = resources.getDimensionPixelOffset(R.dimen.online_video_tip_drawable_width);
            int dimensionPixelOffset2 = resources.getDimensionPixelOffset(R.dimen.online_video_tip_drawable_height);
            int dimensionPixelOffset3 = resources.getDimensionPixelOffset(R.dimen.online_video_tip_drawable_padding);
            drawable.setBounds(0, 0, dimensionPixelOffset, dimensionPixelOffset2);
            textView.setCompoundDrawablesRelative(drawable, null, null, null);
            textView.setCompoundDrawablePadding(dimensionPixelOffset3);
            textView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PhotoPageVideoItem$OnlineViewImpl$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    PhotoPageVideoItem.OnlineViewImpl.$r8$lambda$o99CLB6uPP9s5tjBiZazNaG6IiA(PhotoPageVideoItem.OnlineViewImpl.this, view2);
                }
            });
            viewGroup.findViewById(R.id.tip_close).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PhotoPageVideoItem$OnlineViewImpl$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    PhotoPageVideoItem.OnlineViewImpl.m1552$r8$lambda$XHQpyTboNrma0NTGOs6whTD1k(PhotoPageVideoItem.OnlineViewImpl.this, view2);
                }
            });
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewGroup.getLayoutParams();
            layoutParams.addRule(3, view.getId());
            layoutParams.addRule(14);
            layoutParams.topMargin = resources.getDimensionPixelOffset(R.dimen.online_video_tip_margin_top);
            relativeLayout.addView(viewGroup, layoutParams);
            return viewGroup;
        }

        public /* synthetic */ void lambda$inflateTip$1(View view) {
            IntentUtil.gotoMiCloudVipPage(view.getContext(), new Pair("source", "gallery_video_online"));
            getPresenter().onUpgradeVip();
        }

        public /* synthetic */ void lambda$inflateTip$2(View view) {
            getPresenter().onVipGuideIgnored();
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlineView
        public void showVipGuide() {
            WeakReference<View> weakReference = this.mAnchorRef;
            if (weakReference == null || weakReference.get() == null) {
                DefaultLogger.e("PhotoPageVideoItem", "guide anchor has released");
                return;
            }
            if (this.mTip == null) {
                this.mTip = inflateTip(this.mAnchorRef.get());
            }
            if (this.mTip.getVisibility() == 0) {
                return;
            }
            this.mTip.setVisibility(4);
            TransitionManager.beginDelayedTransition(this.mTip);
            this.mTip.setVisibility(0);
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlineView
        public void hideVipGuide() {
            ViewGroup viewGroup = this.mTip;
            if (viewGroup == null || viewGroup.getVisibility() != 0) {
                return;
            }
            TransitionManager.beginDelayedTransition(this.mTip);
            this.mTip.setVisibility(8);
        }

        public OnlinePresenter getPresenter() {
            return this.mPresenter;
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlineView
        public void showNetworkNotice(NetworkConsider.OnConfirmed onConfirmed) {
            if (isAttached()) {
                NetworkConsider.consider(this.mRootRef.get().getContext(), onConfirmed);
            }
        }

        public final boolean ignoreError(Error error) {
            return error == Error.NO_ERROR || error == Error.DISABLED || error == Error.VIP_LEVEL_LOW || error == Error.UNSUPPORTED_REGION;
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlineView
        public void showErrorView(Error error) {
            if (!isAttached()) {
                return;
            }
            if (ignoreError(error)) {
                DefaultLogger.d("PhotoPageVideoItem", "ignore error %s", error);
                return;
            }
            dismissErrorView();
            int i = AnonymousClass3.$SwitchMap$com$miui$gallery$video$online$Error[error.ordinal()];
            if (i == 1) {
                ToastUtils.makeText(this.mRootRef.get().getContext(), (int) R.string.online_video_normal_transcoding);
            } else if (i == 2) {
                ToastUtils.makeText(this.mRootRef.get().getContext(), (int) R.string.online_video_error_unsupported_type);
            } else {
                showTipView(new PhotoPageItem.TipDetailDisplay() { // from class: com.miui.gallery.ui.PhotoPageVideoItem.OnlineViewImpl.1
                    @Override // com.miui.gallery.ui.PhotoPageItem.TipDetailDisplay
                    /* renamed from: getParams */
                    public Object mo1549getParams() {
                        return null;
                    }

                    {
                        OnlineViewImpl.this = this;
                    }

                    @Override // com.miui.gallery.ui.PhotoPageItem.TipDetailDisplay
                    public void display(Context context, Object obj) {
                        OnlineViewImpl.this.hideTipView();
                        OnlineViewImpl.this.mErrorDialog = DialogUtil.showInfoDialog(context, context.getString(R.string.online_video_error_unknown), "", R.string.month_view_tip_button_text, null);
                    }
                });
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlineView
        public void dismissErrorView() {
            Dialog dialog = this.mErrorDialog;
            if (dialog != null) {
                dialog.dismiss();
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlineView
        public void initTipView(PhotoPageItem.TipView tipView) {
            this.mTipRef = new WeakReference<>(tipView);
        }

        public <P> void showTipView(PhotoPageItem.TipDetailDisplay<P> tipDetailDisplay) {
            WeakReference<PhotoPageItem.TipView> weakReference = this.mTipRef;
            PhotoPageItem.TipView tipView = weakReference != null ? weakReference.get() : null;
            if (tipView != null) {
                tipView.showTip(0, true, tipDetailDisplay, this.mTipToken);
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlineView
        public void hideTipView() {
            WeakReference<PhotoPageItem.TipView> weakReference = this.mTipRef;
            PhotoPageItem.TipView tipView = weakReference != null ? weakReference.get() : null;
            if (tipView != null) {
                tipView.hideTip(true, this.mTipToken);
            }
        }

        public final boolean isAttached() {
            WeakReference<RelativeLayout> weakReference = this.mRootRef;
            return (weakReference == null || weakReference.get() == null) ? false : true;
        }
    }

    /* renamed from: com.miui.gallery.ui.PhotoPageVideoItem$3 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass3 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$video$online$Error;

        static {
            int[] iArr = new int[Error.values().length];
            $SwitchMap$com$miui$gallery$video$online$Error = iArr;
            try {
                iArr[Error.TRANSCODING_NOT_DONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$video$online$Error[Error.UNSUPPORTED_TYPE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class OnlinePresenterImpl implements OnlinePresenter<OnlineView> {
        public BaseDataItem mDataItem;
        public Runnable mShowProgressWorker;
        public AsyncTask<BaseDataItem, Void, Boolean> mShowTipTask;
        public AsyncTask<CloudItem, Void, RequestResult> mUrlRequestTask;
        public OnlineView mView;

        public static /* synthetic */ void $r8$lambda$GzDN0FBa3xIBBiBjl49gLUZ1FGo(OnRequestedOnlineUrl onRequestedOnlineUrl, OnRequestedOnlineUrl.Extras extras, RequestResult requestResult, OnRequestedOnlineUrl.Extras extras2) {
            onRequestedOnlineUrl.onRequested(requestResult, extras);
        }

        /* renamed from: $r8$lambda$HoN40AhvZuQGadjJgFAli7-Il4k */
        public static /* synthetic */ void m1550$r8$lambda$HoN40AhvZuQGadjJgFAli7Il4k(OnlinePresenterImpl onlinePresenterImpl, OnRequestedOnlineUrl onRequestedOnlineUrl, RequestResult requestResult, OnRequestedOnlineUrl.Extras extras) {
            onlinePresenterImpl.lambda$getOnlineUrl$0(onRequestedOnlineUrl, requestResult, extras);
        }

        public static /* synthetic */ void $r8$lambda$hVzMnrcqV02ylB5Xw64vW8UTfGo(OnlinePresenterImpl onlinePresenterImpl) {
            onlinePresenterImpl.lambda$showLoadingProgress$3();
        }

        public static /* synthetic */ void $r8$lambda$jdpAzt028jIFJkORSjMv3ZeUijU(OnlinePresenterImpl onlinePresenterImpl, OnRequestedOnlineUrl onRequestedOnlineUrl, CloudItem cloudItem, boolean z, boolean z2) {
            onlinePresenterImpl.lambda$getOnlineUrl$2(onRequestedOnlineUrl, cloudItem, z, z2);
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlinePresenter
        public void onAttachView(OnlineView onlineView) {
            this.mView = onlineView;
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlinePresenter
        public void onDetachView() {
            this.mView = null;
            cancelUrlRequest();
            cancelShowTipRequest();
        }

        public OnlineView getView() {
            return this.mView;
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlinePresenter
        public void onItemUpdate(BaseDataItem baseDataItem) {
            if (baseDataItem == null || !baseDataItem.equals(this.mDataItem)) {
                this.mDataItem = baseDataItem;
                cancelUrlRequest();
                cancelShowTipRequest();
                if (!isAttached()) {
                    return;
                }
                getView().hideVipGuide();
                showVipGuide();
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlinePresenter
        public void showVipGuide() {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastTimeForGuideCheck(this.mDataItem) >= 2000 && !isShowingTip()) {
                setTimeForGuideCheck(this.mDataItem, currentTimeMillis);
                DefaultLogger.d("PhotoPageVideoItem", "check whether need show vip guide");
                cancelShowTipRequest();
                this.mShowTipTask = new ShowTipTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.mDataItem);
            }
        }

        /* loaded from: classes2.dex */
        public static class ShowTipTask extends AsyncTask<BaseDataItem, Void, Boolean> {
            public WeakReference<OnlinePresenterImpl> mOnlinePresenterImpl;

            public ShowTipTask(OnlinePresenterImpl onlinePresenterImpl) {
                this.mOnlinePresenterImpl = new WeakReference<>(onlinePresenterImpl);
            }

            @Override // android.os.AsyncTask
            public Boolean doInBackground(BaseDataItem... baseDataItemArr) {
                if (!isCancelled() && baseDataItemArr[0] != null) {
                    return Boolean.valueOf(OnlinePresenterImpl.needShowVipGuide(baseDataItemArr[0]));
                }
                return Boolean.FALSE;
            }

            @Override // android.os.AsyncTask
            public void onPostExecute(Boolean bool) {
                WeakReference<OnlinePresenterImpl> weakReference;
                if (isCancelled() || (weakReference = this.mOnlinePresenterImpl) == null || weakReference.get() == null || this.mOnlinePresenterImpl.get().getView() == null) {
                    return;
                }
                OnlinePresenterImpl onlinePresenterImpl = this.mOnlinePresenterImpl.get();
                OnlineView view = onlinePresenterImpl.getView();
                if (bool.booleanValue()) {
                    view.showVipGuide();
                    GalleryPreferences.OnlineVideo.updateVipTipShownTime();
                    onlinePresenterImpl.statEvent("show_guide");
                    return;
                }
                view.hideVipGuide();
            }

            @Override // android.os.AsyncTask
            public void onCancelled() {
                super.onCancelled();
                WeakReference<OnlinePresenterImpl> weakReference = this.mOnlinePresenterImpl;
                if (weakReference != null) {
                    weakReference.clear();
                    this.mOnlinePresenterImpl = null;
                }
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlinePresenter
        public void hideVipGuide() {
            cancelShowTipRequest();
            if (isAttached()) {
                getView().hideVipGuide();
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlinePresenter
        public void onVipGuideIgnored() {
            GalleryPreferences.OnlineVideo.userIgnoreVipTip();
            if (isAttached()) {
                getView().hideVipGuide();
            }
            statEvent("ignore_guide");
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlinePresenter
        public void onUpgradeVip() {
            if (isAttached()) {
                getView().hideVipGuide();
                statEvent("click_guide");
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlinePresenter
        public void hideTip() {
            if (isAttached()) {
                getView().hideTipView();
                getView().dismissErrorView();
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlinePresenter
        public void getOnlineUrl(final OnRequestedOnlineUrl onRequestedOnlineUrl) {
            if (!VideoPlayerCompat.isSupportOnlinePlayer()) {
                DefaultLogger.d("PhotoPageVideoItem", "not supported online player");
                onRequestedOnlineUrl.onRequested(null, null);
                return;
            }
            BaseDataItem baseDataItem = this.mDataItem;
            if (baseDataItem == null || baseDataItem.getDownloadUri() == null) {
                DefaultLogger.d("PhotoPageVideoItem", "not cloud item");
                onRequestedOnlineUrl.onRequested(null, null);
                return;
            }
            final CloudItem cloudItem = (CloudItem) this.mDataItem;
            OnlineVideoStrategy onlineVideoStrategy = CloudControlStrategyHelper.getOnlineVideoStrategy();
            if (!onlineVideoStrategy.isEnabled()) {
                DefaultLogger.d("PhotoPageVideoItem", "online video not supported");
                onRequestedOnlineUrl.onRequested(null, null);
            } else if (this.mDataItem.getSize() < onlineVideoStrategy.getSupportedMinSize()) {
                DefaultLogger.d("PhotoPageVideoItem", "this video is so little");
                onRequestedOnlineUrl.onRequested(null, null);
            } else {
                String onlineUrlFromCache = getOnlineUrlFromCache(this.mDataItem);
                if (!TextUtils.isEmpty(onlineUrlFromCache)) {
                    DefaultLogger.d("PhotoPageVideoItem", "got online url from cache");
                    onRequestedOnlineUrl.onRequested(new RequestResult(onlineUrlFromCache, Error.NO_ERROR), null);
                } else if (cloudItem.isShare() && !onlineVideoStrategy.isSupportShareMedia()) {
                    DefaultLogger.d("PhotoPageVideoItem", "share video not supported");
                    onRequestedOnlineUrl.onRequested(null, null);
                } else {
                    final OnRequestedOnlineUrl onRequestedOnlineUrl2 = new OnRequestedOnlineUrl() { // from class: com.miui.gallery.ui.PhotoPageVideoItem$OnlinePresenterImpl$$ExternalSyntheticLambda2
                        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnRequestedOnlineUrl
                        public final void onRequested(PhotoPageVideoItem.RequestResult requestResult, PhotoPageVideoItem.OnRequestedOnlineUrl.Extras extras) {
                            PhotoPageVideoItem.OnlinePresenterImpl.m1550$r8$lambda$HoN40AhvZuQGadjJgFAli7Il4k(PhotoPageVideoItem.OnlinePresenterImpl.this, onRequestedOnlineUrl, requestResult, extras);
                        }
                    };
                    if (needShowNetworkNotice()) {
                        getView().showNetworkNotice(new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.ui.PhotoPageVideoItem$OnlinePresenterImpl$$ExternalSyntheticLambda0
                            @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                            public final void onConfirmed(boolean z, boolean z2) {
                                PhotoPageVideoItem.OnlinePresenterImpl.$r8$lambda$jdpAzt028jIFJkORSjMv3ZeUijU(PhotoPageVideoItem.OnlinePresenterImpl.this, onRequestedOnlineUrl2, cloudItem, z, z2);
                            }
                        });
                    } else {
                        doRequestUrl(cloudItem, onRequestedOnlineUrl2);
                    }
                }
            }
        }

        public /* synthetic */ void lambda$getOnlineUrl$0(OnRequestedOnlineUrl onRequestedOnlineUrl, RequestResult requestResult, OnRequestedOnlineUrl.Extras extras) {
            onRequestedOnlineUrl.onRequested(requestResult, extras);
            if (requestResult == null || requestResult.error == Error.NO_ERROR) {
                return;
            }
            handleRequestError(requestResult);
        }

        public /* synthetic */ void lambda$getOnlineUrl$2(final OnRequestedOnlineUrl onRequestedOnlineUrl, CloudItem cloudItem, boolean z, boolean z2) {
            final OnRequestedOnlineUrl.Extras extras = new OnRequestedOnlineUrl.Extras(z2, z);
            if (z) {
                doRequestUrl(cloudItem, new OnRequestedOnlineUrl() { // from class: com.miui.gallery.ui.PhotoPageVideoItem$OnlinePresenterImpl$$ExternalSyntheticLambda1
                    @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnRequestedOnlineUrl
                    public final void onRequested(PhotoPageVideoItem.RequestResult requestResult, PhotoPageVideoItem.OnRequestedOnlineUrl.Extras extras2) {
                        PhotoPageVideoItem.OnlinePresenterImpl.$r8$lambda$GzDN0FBa3xIBBiBjl49gLUZ1FGo(PhotoPageVideoItem.OnRequestedOnlineUrl.this, extras, requestResult, extras2);
                    }
                });
            } else {
                onRequestedOnlineUrl.onRequested(null, extras);
            }
            if (z2) {
                updateMeteredNetworkAllowed(z);
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnlinePresenter
        public void onLoadingClicked() {
            if (isAttached()) {
                getView().hideLoadingProgress();
            }
            cancelUrlRequest();
        }

        public final void statEvent(String str) {
            StatHelper.recordCountEvent("online_video", str);
        }

        public final void handleRequestError(RequestResult requestResult) {
            if (requestResult == null || !isAttached()) {
                return;
            }
            getView().showErrorView(requestResult.error);
        }

        public final boolean isAttached() {
            return getView() != null;
        }

        public final boolean needShowNetworkNotice() {
            if (MemoryPreferenceHelper.contains("allow_metered_network")) {
                return !MemoryPreferenceHelper.getBoolean("allow_metered_network", false);
            }
            return true;
        }

        public final void updateMeteredNetworkAllowed(boolean z) {
            MemoryPreferenceHelper.putBoolean("allow_metered_network", z);
        }

        public final void showLoadingProgress() {
            if (this.mShowProgressWorker == null) {
                this.mShowProgressWorker = new Runnable() { // from class: com.miui.gallery.ui.PhotoPageVideoItem$OnlinePresenterImpl$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        PhotoPageVideoItem.OnlinePresenterImpl.$r8$lambda$hVzMnrcqV02ylB5Xw64vW8UTfGo(PhotoPageVideoItem.OnlinePresenterImpl.this);
                    }
                };
            } else {
                hideLoadingProgress();
            }
            ThreadManager.getMainHandler().postDelayed(this.mShowProgressWorker, 500L);
        }

        public /* synthetic */ void lambda$showLoadingProgress$3() {
            if (isAttached()) {
                getView().showLoadingProgress();
            }
        }

        public final void hideLoadingProgress() {
            if (this.mShowProgressWorker != null) {
                ThreadManager.getMainHandler().removeCallbacks(this.mShowProgressWorker);
                if (!isAttached()) {
                    return;
                }
                getView().hideLoadingProgress();
            }
        }

        public final void doRequestUrl(CloudItem cloudItem, final OnRequestedOnlineUrl onRequestedOnlineUrl) {
            cancelUrlRequest();
            this.mUrlRequestTask = new AsyncTask<CloudItem, Void, RequestResult>() { // from class: com.miui.gallery.ui.PhotoPageVideoItem.OnlinePresenterImpl.1
                {
                    OnlinePresenterImpl.this = this;
                }

                @Override // android.os.AsyncTask
                public void onPreExecute() {
                    OnlinePresenterImpl.this.showLoadingProgress();
                }

                @Override // android.os.AsyncTask
                public RequestResult doInBackground(CloudItem... cloudItemArr) {
                    if (!MemoryPreferenceHelper.getBoolean("has_requested_video_play_status", false)) {
                        requestVideoPlayLevel();
                        MemoryPreferenceHelper.putBoolean("has_requested_video_play_status", true);
                    }
                    if (!OnlinePresenterImpl.isSupportVideoPlayOnline()) {
                        DefaultLogger.d("PhotoPageVideoItem", "not support");
                        return new RequestResult("", Error.VIP_LEVEL_LOW);
                    }
                    try {
                        return new RequestResult(new PlayUrlRequest(cloudItemArr[0].getServerId()).request(), Error.NO_ERROR);
                    } catch (UrlRequestError e) {
                        DefaultLogger.d("PhotoPageVideoItem", "request url error: %s", e.toString());
                        return new RequestResult("", e.getError());
                    }
                }

                public void requestVideoPlayLevel() {
                    JSONObject jSONObject;
                    if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
                        DefaultLogger.e("PhotoPageVideoItem", "CTA not confirmed");
                    } else if (!BaseNetworkUtils.isNetworkConnected()) {
                        DefaultLogger.e("PhotoPageVideoItem", "No network");
                    } else {
                        Account account = AccountCache.getAccount();
                        if (account == null) {
                            DefaultLogger.e("PhotoPageVideoItem", "No account");
                            return;
                        }
                        try {
                            JSONObject fromXiaomi = CloudUtils.getFromXiaomi(HostManager.Slim.getVideoplayLevelUrl(), null, account, CloudUtils.getExtToken(GalleryApp.sGetAndroidContext(), account), 0, false);
                            if (fromXiaomi == null || fromXiaomi.getInt("code") != 0 || (jSONObject = fromXiaomi.getJSONObject("data")) == null) {
                                return;
                            }
                            MemoryPreferenceHelper.putString("video_play_status", jSONObject.getString("level"));
                        } catch (Exception e) {
                            DefaultLogger.d("PhotoPageVideoItem", "requestVideoPlayLevel error", e);
                        }
                    }
                }

                @Override // android.os.AsyncTask
                public void onPostExecute(RequestResult requestResult) {
                    if (OnlinePresenterImpl.this.isAttached()) {
                        OnlinePresenterImpl.this.hideLoadingProgress();
                    }
                    if (!isCancelled()) {
                        OnlinePresenterImpl.saveOnlineUrlToCache(OnlinePresenterImpl.this.mDataItem, requestResult.url);
                        onRequestedOnlineUrl.onRequested(requestResult, null);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, cloudItem);
        }

        public final void cancelUrlRequest() {
            AsyncTask<CloudItem, Void, RequestResult> asyncTask = this.mUrlRequestTask;
            if (asyncTask != null) {
                asyncTask.cancel(true);
                this.mUrlRequestTask = null;
            }
        }

        public final void cancelShowTipRequest() {
            AsyncTask<BaseDataItem, Void, Boolean> asyncTask = this.mShowTipTask;
            if (asyncTask != null) {
                asyncTask.cancel(true);
                this.mShowTipTask = null;
            }
        }

        public static boolean isSupportVideoPlayOnline() {
            String string = MemoryPreferenceHelper.getString("video_play_status", "");
            return "SPECIAL_ACTIVE".equals(string) || "VIP".equals(string);
        }

        public static String getOnlineUrlFromCache(BaseDataItem baseDataItem) {
            if (baseDataItem == null || baseDataItem.getDownloadUri() == null) {
                return null;
            }
            return MemoryPreferenceHelper.getString(baseDataItem.getDownloadUri().toString(), null);
        }

        public static void saveOnlineUrlToCache(BaseDataItem baseDataItem, String str) {
            if (baseDataItem == null || baseDataItem.getDownloadUri() == null) {
                return;
            }
            MemoryPreferenceHelper.putString(baseDataItem.getDownloadUri().toString(), str);
        }

        public final boolean isShowingTip() {
            AsyncTask<BaseDataItem, Void, Boolean> asyncTask = this.mShowTipTask;
            return asyncTask != null && (asyncTask.getStatus() == AsyncTask.Status.PENDING || this.mShowTipTask.getStatus() == AsyncTask.Status.RUNNING);
        }

        public static long lastTimeForGuideCheck(BaseDataItem baseDataItem) {
            if (baseDataItem != null) {
                return MemoryPreferenceHelper.getLong(String.valueOf(baseDataItem.getKey()), 0L);
            }
            return 0L;
        }

        public static void setTimeForGuideCheck(BaseDataItem baseDataItem, long j) {
            if (baseDataItem != null) {
                MemoryPreferenceHelper.putLong(String.valueOf(baseDataItem.getKey()), j);
            }
        }

        public static boolean needShowVipGuide(BaseDataItem baseDataItem) {
            if (!VideoPlayerCompat.isSupportOnlinePlayer()) {
                return false;
            }
            if (isSupportVideoPlayOnline()) {
                DefaultLogger.d("PhotoPageVideoItem", "memory vip");
                return false;
            }
            OnlineVideoStrategy onlineVideoStrategy = CloudControlStrategyHelper.getOnlineVideoStrategy();
            if (!onlineVideoStrategy.isEnabled()) {
                DefaultLogger.d("PhotoPageVideoItem", "online video disabled");
                return false;
            } else if (!onlineVideoStrategy.isShowGuide()) {
                DefaultLogger.d("PhotoPageVideoItem", "not show the guide of vip");
                return false;
            } else {
                DownloadItem downloadingItem = getDownloadingItem(baseDataItem);
                if (downloadingItem == null || System.currentTimeMillis() - downloadingItem.getStartTime() < onlineVideoStrategy.getWaitingTimeForShown()) {
                    return false;
                }
                if (!onlineVideoStrategy.isShownAfterIgnored() && GalleryPreferences.OnlineVideo.hasIgnoredVipTip()) {
                    DefaultLogger.d("PhotoPageVideoItem", "tip has been ignored");
                    return false;
                } else if (baseDataItem.getSize() < onlineVideoStrategy.getSizeLimitForShown()) {
                    DefaultLogger.d("PhotoPageVideoItem", "video size isn't suitable");
                    return false;
                } else if (System.currentTimeMillis() - GalleryPreferences.OnlineVideo.getVipTipShownTime() >= onlineVideoStrategy.getIntervalForShown()) {
                    return true;
                } else {
                    DefaultLogger.d("PhotoPageVideoItem", "shown interval hasn't reached");
                    return false;
                }
            }
        }

        public static DownloadItem getDownloadingItem(BaseDataItem baseDataItem) {
            if (baseDataItem == null || baseDataItem.getDownloadUri() == null) {
                return null;
            }
            DownloadItem peek = ImageDownloader.getInstance().peek(baseDataItem.getDownloadUri(), DownloadType.ORIGIN_FORCE);
            return peek != null ? peek : ImageDownloader.getInstance().peek(baseDataItem.getDownloadUri(), DownloadType.ORIGIN);
        }
    }

    /* loaded from: classes2.dex */
    public interface OnRequestedOnlineUrl {
        void onRequested(RequestResult requestResult, Extras extras);

        /* loaded from: classes2.dex */
        public static class Extras {
            public final boolean hasNoticedNetwork;
            public final boolean isAllowedMetered;

            public Extras(boolean z, boolean z2) {
                this.isAllowedMetered = z2;
                this.hasNoticedNetwork = z;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class RequestResult {
        public final Error error;
        public final String url;

        public RequestResult(String str, Error error) {
            this.url = str;
            this.error = error;
        }
    }
}
