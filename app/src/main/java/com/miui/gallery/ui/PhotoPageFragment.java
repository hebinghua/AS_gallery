package com.miui.gallery.ui;

import android.animation.Animator;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.tracing.Trace;
import com.android.internal.CompatHandler;
import com.android.internal.WindowCompat;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.core.SdkHelper;
import com.miui.extraphoto.sdk.ExtraPhotoSDK;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.data.CacheOfAllFacesInOnePhoto;
import com.miui.gallery.miplay.GalleryMiPlayManager;
import com.miui.gallery.miplay.MiplayCirculateStatusListener;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.model.CloudItem;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.projection.ConnectControllerSingleton;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment;
import com.miui.gallery.ui.PhotoPageFragment;
import com.miui.gallery.ui.PhotoPageItem;
import com.miui.gallery.ui.PhotoPageVideoItem;
import com.miui.gallery.ui.PhotoPagerHelper;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.ui.photoPage.IPhotoPageManager;
import com.miui.gallery.ui.photoPage.PhotoPageBaseManager;
import com.miui.gallery.ui.photoPage.PhotoPageLifeCircleHooker;
import com.miui.gallery.ui.photoPage.PhotoPageOrientationManager;
import com.miui.gallery.ui.photoPage.PhotoPageThemeManager;
import com.miui.gallery.ui.photoPage.bars.data.DataPrepareHelper;
import com.miui.gallery.ui.photoPage.bars.data.DataProvider;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.ui.photoPage.bars.manager.edit.TransitionEditorManager;
import com.miui.gallery.ui.photoPage.bars.manager.edit.UpdatableEditorManager;
import com.miui.gallery.ui.photoPage.bars.menuitem.Cast;
import com.miui.gallery.ui.photoPage.bars.menuitem.CorrectDoc;
import com.miui.gallery.ui.photoPage.bars.menuitem.Edit;
import com.miui.gallery.ui.photoPage.bars.menuitem.Favorite;
import com.miui.gallery.ui.photoPage.bars.menuitem.Send;
import com.miui.gallery.ui.photoPage.bars.menuitem.VideoCompress;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BaseScreenUtil;
import com.miui.gallery.util.BrightnessProvider;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.IdleUITaskHelper;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.PhotoOperationsUtil;
import com.miui.gallery.util.ReceiverUtils;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.SecretAlbumCryptoUtils;
import com.miui.gallery.util.SpecialTypeMediaUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.TalkBackUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.VideoPlayerCompat;
import com.miui.gallery.util.XmpHelper;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.photoview.ItemViewInfo;
import com.miui.gallery.util.photoview.PhotoPageDataCache;
import com.miui.gallery.util.photoview.ScreenSceneAlgorithmManager;
import com.miui.gallery.util.photoview.ScreenSceneClassificationUtil;
import com.miui.gallery.util.photoview.TileReusedBitCache;
import com.miui.gallery.util.photoview.preload.PhotoPagePreloadHelper;
import com.miui.gallery.video.VideoFrameProvider;
import com.miui.gallery.video.VideoFrameSeekBar;
import com.miui.gallery.video.VideoSubtitleProvider;
import com.miui.gallery.video.VideoTagsParser;
import com.miui.gallery.widget.IMultiThemeView;
import com.miui.gallery.widget.ViewPager;
import com.miui.gallery.widget.slip.VerticalSlipLayout;
import com.miui.preload.PreloadedAppHelper;
import com.miui.video.gallery.galleryvideo.gallery.GalleryVideoInfo;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.miplay.phoneclientsdk.info.MediaMetaData;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import miui.gallery.support.MiuiSdkCompat;
import miuix.view.animation.CubicEaseOutInterpolator;
import miuix.view.animation.SineEaseInInterpolator;
import miuix.view.animation.SineEaseInOutInterpolator;

/* loaded from: classes2.dex */
public class PhotoPageFragment extends BasePhotoPageBarsDelegateFragment implements IPhotoPageManager.IPhotoPageManagerCallback {
    public boolean isVideoSeekBarVisible;
    public BurstPhotoManager mBurstPhotoManager;
    public CameraAnimManager mCameraAnimManager;
    public HashMap<String, Object> mConfig;
    public Runnable mConfigurationChangeRunnable;
    public boolean mDataLoaded;
    public DocPhotoManager mDocPhotoManager;
    public boolean mHasSendDismissCast;
    public boolean mIsConfigurationChanged;
    public boolean mIsNightMode;
    public Runnable mItemSettleRunnable;
    public MotionPhotoManager mMotionPhotoManager;
    public PhotoPageOrientationManager.IPhotoPageOrientationManagerController mOrientationController;
    public PhotoPageLifeCircleHooker mPhotoPageLifeCircleHooker;
    public PhotoRefreshReceiver mPhotoRefreshReceiver;
    public RefocusManager mRefocusManager;
    public ScreenBroadcastReceiver mScreenReceiver;
    public ScreenSceneEffectHelper mScreenSceneEffectHelper;
    public ScrollPrivacyManager mScrollPrivacyManager;
    public SpecialTypeManager mSpecialTypeManager;
    public PhotoPageThemeManager.IPhotoPageThemeManagerController mThemeController;
    public VerticalSlipLayout mVerticalSlipLayout;
    public VideoPlayerManager mVideoPlayerManager;
    public VideoPreviewManager mVideoPreviewManager;
    public WaterSpecialTypeListener mWaterSpecialTypeListener;
    public WatermarkManager mWatermarkManager;
    public int mUserShowBarIndex = -1;
    public Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.miui.gallery.ui.PhotoPageFragment.2
        {
            PhotoPageFragment.this = this;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            StringBuilder sb;
            BaseActivity baseActivity;
            PhotoPageItem currentItem;
            BaseDataItem dataItem;
            super.handleMessage(message);
            long currentTimeMillis = System.currentTimeMillis();
            try {
                DefaultLogger.i("PhotoPageFragment", "TRANSITEND_STEP -> [" + message.what + "] -> START");
                if (PhotoPageFragment.this.getActivity() != null && (baseActivity = PhotoPageFragment.this.mActivity) != null && !baseActivity.isFinishing()) {
                    switch (message.what) {
                        case 101:
                            PhotoPagerHelper photoPagerHelper = PhotoPageFragment.this.mPagerHelper;
                            if (photoPagerHelper != null && (currentItem = photoPagerHelper.getCurrentItem()) != null && (dataItem = currentItem.getDataItem()) != null) {
                                PhotoPageFragment.this.mDataProvider.getViewModelData().setCurrentDataItem(dataItem);
                                PhotoPageFragment.this.prepareData(dataItem);
                            }
                            sendEmptyMessage(message.what + 1);
                            return;
                        case 102:
                            PhotoPageFragment.this.attachBars();
                            sendEmptyMessage(message.what + 1);
                            return;
                        case 103:
                            PhotoPageFragment.this.initBars();
                            if (!PhotoPageFragment.this.isShowBarsWhenEntering()) {
                                PhotoPageFragment.this.hideBars(false);
                            }
                            if (PhotoPageFragment.this.mDataProvider.getFieldData().isTransparentTheme) {
                                LocalBroadcastManager.getInstance(PhotoPageFragment.this.mActivity).sendBroadcast(new Intent("com.miu.gallery.action.ENTER_PHOTO_PAGE"));
                            }
                            if (PhotoPageFragment.this.mDataProvider.getFieldData().isFromRecommendFacePage || PhotoPageFragment.this.mDataProvider.getFieldData().isPreviewMode) {
                                PhotoPageFragment.this.setActionBarVisible(false, false);
                                PhotoPageFragment photoPageFragment = PhotoPageFragment.this;
                                photoPageFragment.mVideoPlayerManager = new VideoPlayerManager(photoPageFragment.mDataProvider, photoPageFragment, null);
                                sb = new StringBuilder();
                                sb.append("TRANSITEND_STEP -> end -> [");
                                sb.append(System.currentTimeMillis() - currentTimeMillis);
                                sb.append("]");
                                DefaultLogger.i("PhotoPageFragment", sb.toString());
                            }
                            sendEmptyMessage(message.what + 1);
                            return;
                        case 104:
                            if (PhotoPageFragment.this.mDataProvider.getFieldData().isFromTrash) {
                                PhotoPageFragment photoPageFragment2 = PhotoPageFragment.this;
                                photoPageFragment2.mScrollPrivacyManager = new ScrollPrivacyManager();
                                sb = new StringBuilder();
                                sb.append("TRANSITEND_STEP -> end -> [");
                                sb.append(System.currentTimeMillis() - currentTimeMillis);
                                sb.append("]");
                                DefaultLogger.i("PhotoPageFragment", sb.toString());
                            }
                            PhotoPageFragment photoPageFragment3 = PhotoPageFragment.this;
                            photoPageFragment3.mSpecialTypeManager = new SpecialTypeManager(photoPageFragment3, photoPageFragment3.mDataProvider.getFieldData().mOperationMask);
                            PhotoPageFragment photoPageFragment4 = PhotoPageFragment.this;
                            photoPageFragment4.mRefocusManager = new RefocusManager(photoPageFragment4.mDataProvider, photoPageFragment4, photoPageFragment4.mSpecialTypeManager);
                            PhotoPageFragment photoPageFragment5 = PhotoPageFragment.this;
                            photoPageFragment5.mDocPhotoManager = new DocPhotoManager(photoPageFragment5.mDataProvider, photoPageFragment5, photoPageFragment5.mSpecialTypeManager);
                            PhotoPageFragment photoPageFragment6 = PhotoPageFragment.this;
                            photoPageFragment6.mVideoPlayerManager = new VideoPlayerManager(photoPageFragment6.mDataProvider, photoPageFragment6, photoPageFragment6.mSpecialTypeManager);
                            PhotoPageFragment photoPageFragment7 = PhotoPageFragment.this;
                            photoPageFragment7.mMotionPhotoManager = new MotionPhotoManager(photoPageFragment7.mDataProvider, photoPageFragment7, photoPageFragment7.mSpecialTypeManager);
                            PhotoPageFragment photoPageFragment8 = PhotoPageFragment.this;
                            photoPageFragment8.mWatermarkManager = new WatermarkManager(photoPageFragment8.mDataProvider, photoPageFragment8, photoPageFragment8.mSpecialTypeManager);
                            sendEmptyMessage(message.what + 1);
                            return;
                        case 105:
                            if (PhotoPageFragment.this.mWaterSpecialTypeListener != null) {
                                WaterSpecialTypeListener waterSpecialTypeListener = PhotoPageFragment.this.mWaterSpecialTypeListener;
                                PhotoPageFragment photoPageFragment9 = PhotoPageFragment.this;
                                waterSpecialTypeListener.onSpecialManagerInitialized(photoPageFragment9.mAdapter.getDataItem(photoPageFragment9.mPager.getCurrentItem()));
                                PhotoPageFragment.this.mWaterSpecialTypeListener = null;
                            }
                            if (PhotoPageFragment.this.mDataProvider.getFieldData().isSupportFordBurst) {
                                PhotoPageFragment photoPageFragment10 = PhotoPageFragment.this;
                                photoPageFragment10.mBurstPhotoManager = new BurstPhotoManager(photoPageFragment10.mSpecialTypeManager);
                            }
                            if (VideoPlayerCompat.isSupportNewVideoPlayer()) {
                                PhotoPageFragment photoPageFragment11 = PhotoPageFragment.this;
                                photoPageFragment11.mVideoPreviewManager = new VideoPreviewManager();
                            }
                            if (PhotoPageFragment.this.isResumed()) {
                                PhotoPageFragment.this.doOnResume();
                            }
                            sendEmptyMessage(message.what + 1);
                            return;
                        case 106:
                            if (PhotoPageFragment.this.mDataLoaded) {
                                PhotoPageFragment photoPageFragment12 = PhotoPageFragment.this;
                                photoPageFragment12.doDataSetLoaded(photoPageFragment12.mAdapter.getDataSet(), true);
                                PhotoPageFragment photoPageFragment13 = PhotoPageFragment.this;
                                photoPageFragment13.doOnItemChanged(photoPageFragment13.mPager.getCurrentItem());
                                PhotoPageFragment photoPageFragment14 = PhotoPageFragment.this;
                                photoPageFragment14.doOnItemSettled(photoPageFragment14.mPager.getCurrentItem());
                            }
                            if (PhotoPageFragment.this.mThemeController != null) {
                                PhotoPageFragment.this.mThemeController.setBackgroundAlpha(1.0f);
                            }
                            if (PhotoPageFragment.this.mCameraAnimManager != null) {
                                PhotoPageFragment.this.mCameraAnimManager.doOnTransitEnd();
                            }
                            sendEmptyMessage(message.what + 1);
                            return;
                        default:
                            DefaultLogger.i("PhotoPageFragment", "TRANSITEND_FINISH");
                            sb = new StringBuilder();
                            sb.append("TRANSITEND_STEP -> end -> [");
                            sb.append(System.currentTimeMillis() - currentTimeMillis);
                            sb.append("]");
                            DefaultLogger.i("PhotoPageFragment", sb.toString());
                    }
                }
                DefaultLogger.w("PhotoPageFragment", "mActivity is finishing!!");
                sb = new StringBuilder();
                sb.append("TRANSITEND_STEP -> end -> [");
                sb.append(System.currentTimeMillis() - currentTimeMillis);
                sb.append("]");
                DefaultLogger.i("PhotoPageFragment", sb.toString());
            } finally {
                DefaultLogger.i("PhotoPageFragment", "TRANSITEND_STEP -> end -> [" + (System.currentTimeMillis() - currentTimeMillis) + "]");
            }
        }
    };
    public Runnable mDelayShowTask = new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFragment$$ExternalSyntheticLambda8
        @Override // java.lang.Runnable
        public final void run() {
            PhotoPageFragment.this.showLockButtonGuide();
        }
    };
    public final PhotoPagerHelper.OnExitListener mOnExitListener = new PhotoPagerHelper.OnExitListener() { // from class: com.miui.gallery.ui.PhotoPageFragment$$ExternalSyntheticLambda1
        @Override // com.miui.gallery.ui.PhotoPagerHelper.OnExitListener
        public final void onExit() {
            PhotoPageFragment.this.lambda$new$2();
        }
    };
    public final PhotoPagerHelper.OnPhotoViewDragDownOutListener mOnPhotoViewDragDownOutListener = new PhotoPagerHelper.OnPhotoViewDragDownOutListener() { // from class: com.miui.gallery.ui.PhotoPageFragment$$ExternalSyntheticLambda3
        @Override // com.miui.gallery.ui.PhotoPagerHelper.OnPhotoViewDragDownOutListener
        public final void onPhotoDragDownOut() {
            PhotoPageFragment.this.doEntrancesClose();
        }
    };
    public final PhotoPagerHelper.OnAlphaChangedListener mAlphaChangedListener = new PhotoPagerHelper.OnAlphaChangedListener() { // from class: com.miui.gallery.ui.PhotoPageFragment.5
        {
            PhotoPageFragment.this = this;
        }

        @Override // com.miui.gallery.ui.PhotoPagerHelper.OnAlphaChangedListener
        public void onAlphaChanged(float f) {
            if (!PhotoPageFragment.this.mDataProvider.getFieldData().isTransparentTheme) {
                return;
            }
            if (PhotoPageFragment.this.mThemeController != null) {
                PhotoPageFragment.this.mThemeController.setBackgroundAlpha(f);
            }
            if (PhotoPageFragment.this.mCameraAnimManager == null) {
                return;
            }
            PhotoPageFragment.this.mCameraAnimManager.onAlphaChanged(f < 1.0f);
        }
    };
    public final PhotoPagerHelper.OnSingleTapListener mOnSingleTapListener = new PhotoPagerHelper.OnSingleTapListener() { // from class: com.miui.gallery.ui.PhotoPageFragment$$ExternalSyntheticLambda6
        @Override // com.miui.gallery.ui.PhotoPagerHelper.OnSingleTapListener
        public final void onTap(float f, float f2) {
            PhotoPageFragment.this.lambda$new$3(f, f2);
        }
    };
    public final PhotoPagerHelper.OnScaleChangedListener mOnPhotoScaleChangeListener = new PhotoPagerHelper.OnScaleChangedListener() { // from class: com.miui.gallery.ui.PhotoPageFragment$$ExternalSyntheticLambda5
        @Override // com.miui.gallery.ui.PhotoPagerHelper.OnScaleChangedListener
        public final void onScaleChanged(float f, float f2, float f3) {
            PhotoPageFragment.this.lambda$new$4(f, f2, f3);
        }
    };
    public final PhotoPagerHelper.OnRotateListener mOnRotateListener = new PhotoPagerHelper.OnRotateListener() { // from class: com.miui.gallery.ui.PhotoPageFragment$$ExternalSyntheticLambda4
        @Override // com.miui.gallery.ui.PhotoPagerHelper.OnRotateListener
        public final void onRotate(float f) {
            PhotoPageFragment.this.lambda$new$5(f);
        }
    };
    public final PhotoPagerHelper.OnDisplayRectChangedListener mOnPhotoMatrixChangeListener = new PhotoPagerHelper.OnDisplayRectChangedListener() { // from class: com.miui.gallery.ui.PhotoPageFragment$$ExternalSyntheticLambda0
        @Override // com.miui.gallery.ui.PhotoPagerHelper.OnDisplayRectChangedListener
        public final void onDisplayRectChanged(RectF rectF) {
            PhotoPageFragment.this.lambda$new$6(rectF);
        }
    };
    public final PhotoPagerHelper.OnPhotoDeleteListener mOnPhotoDeleteListener = new PhotoPagerHelper.OnPhotoDeleteListener() { // from class: com.miui.gallery.ui.PhotoPageFragment$$ExternalSyntheticLambda2
        @Override // com.miui.gallery.ui.PhotoPagerHelper.OnPhotoDeleteListener
        public final void onDelete() {
            PhotoPageFragment.this.lambda$new$7();
        }
    };

    /* renamed from: $r8$lambda$-oaJV1jJZHZvdEmM27OT07xI5FI */
    public static /* synthetic */ void m1534$r8$lambda$oaJV1jJZHZvdEmM27OT07xI5FI(PhotoPageFragment photoPageFragment) {
        photoPageFragment.lambda$delayDoAfterTransit$0();
    }

    public static /* synthetic */ void $r8$lambda$bmHS8brnKtRoWHuppTgxrulbc6o(PhotoPageFragment photoPageFragment) {
        photoPageFragment.lambda$updateLayoutForConfigurationChanged$8();
    }

    public static /* synthetic */ void $r8$lambda$dVbSUx0WvVPUEriFENA_ummCUt0(PhotoPageFragment photoPageFragment) {
        photoPageFragment.exeExit();
    }

    public static /* synthetic */ void $r8$lambda$j0IJ9etLVy5vOSY9bo6NvbHHAeA(PhotoPageFragment photoPageFragment, boolean z) {
        photoPageFragment.lambda$setActionBarVisible$1(z);
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public String getPageName() {
        return "photo";
    }

    public static PhotoPageFragment newInstance(Uri uri, String str, Bundle bundle, int i) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt("key_theme", i);
        if (uri != null) {
            bundle.putString("photo_uri", uri.toString());
        }
        if (str != null) {
            bundle.putString("mime_type", str);
        }
        PhotoPageFragment photoPageFragment = new PhotoPageFragment();
        photoPageFragment.setArguments(bundle);
        return photoPageFragment;
    }

    @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment, com.miui.gallery.ui.AbstractViewPagerFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        try {
            Trace.beginSection("PhotoPageFragmentOnCreate");
            initDataProvider();
            boolean parseArguments = this.mDataProvider.getFieldData().parseArguments(getArguments());
            super.onCreate(bundle);
            if (!parseArguments) {
                DefaultLogger.e("PhotoPageFragment", "params error %s", getArguments());
                finish();
                return;
            }
            Trace.beginSection("PhotoPageFragmentSelfCreate");
            if (this.mDataProvider.getFieldData().isFromTrash) {
                this.mDataProvider.getFieldData().isNeedConfirmPassWord = true;
                if (this.mDataProvider.getFieldData().isArgumentsConfirmPassWord) {
                    this.mDataProvider.getFieldData().isNeedConfirmPassWord = false;
                    AuthenticatePrivacyPasswordFragment.startAuthenticatePrivacyPassword(this, false);
                }
            }
            if (this.mDataProvider.getFieldData().isFromCamera) {
                this.mDataProvider.getFieldData().isStartWhenLocked = getArguments().getBoolean("StartActivityWhenLocked", false);
                this.mDataProvider.getFieldData().isStartWhenLockedAndSecret = this.mDataProvider.getFieldData().isStartWhenLocked && isSecureKeyguard();
                DefaultLogger.d("PhotoPageFragment", "arguments : isStartWhenLocked = [%s]", Boolean.valueOf(this.mDataProvider.getFieldData().isStartWhenLocked));
            }
            WindowCompat.setShowWhenLocked(this.mActivity, false);
            if (this.mDataProvider.getFieldData().isFromCamera) {
                if (this.mDataProvider.getFieldData().isStartWhenLocked) {
                    WindowCompat.setShowWhenLocked(this.mActivity, true);
                    ScreenBroadcastReceiver screenBroadcastReceiver = new ScreenBroadcastReceiver();
                    this.mScreenReceiver = screenBroadcastReceiver;
                    ReceiverUtils.registerReceiver(this.mActivity, screenBroadcastReceiver, "android.intent.action.SCREEN_OFF");
                }
                this.mPhotoRefreshReceiver = new PhotoRefreshReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("com.miui.gallery.SAVE_TO_CLOUD");
                LocalBroadcastManager.getInstance(this.mActivity).registerReceiver(this.mPhotoRefreshReceiver, intentFilter);
            }
            this.mIsNightMode = MiscUtil.isNightMode(getActivity());
            registerManager(bundle);
            Trace.endSection();
        } finally {
            Trace.endSection();
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, com.miui.gallery.app.fragment.GalleryFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        PhotoPageLifeCircleHooker photoPageLifeCircleHooker = this.mPhotoPageLifeCircleHooker;
        if (photoPageLifeCircleHooker != null) {
            photoPageLifeCircleHooker.onSaveInstanceState(bundle);
        }
    }

    public final void registerManager(Bundle bundle) {
        int i = 2;
        HashMap<String, Object> hashMap = new HashMap<>(2, 1.0f);
        this.mConfig = hashMap;
        if (this.mDataProvider.getFieldData().isFromCamera) {
            i = 1;
        } else if (!this.mDataProvider.getFieldData().isFromScreenRecorder) {
            i = 0;
        }
        hashMap.put("scene", Integer.valueOf(i));
        this.mConfig.put("window_mode", Integer.valueOf(ActivityCompat.isInFreeFormWindow(this.mActivity) ? 1 : 0));
        PhotoPageOrientationManager.IPhotoPageOrientationManagerController iPhotoPageOrientationManagerController = (PhotoPageOrientationManager.IPhotoPageOrientationManagerController) PhotoPageBaseManager.registerManager(this, this.mConfig, this, PhotoPageOrientationManager.class);
        this.mOrientationController = iPhotoPageOrientationManagerController;
        iPhotoPageOrientationManagerController.setDataProvider(this.mDataProvider);
        PhotoPageLifeCircleHooker photoPageLifeCircleHooker = new PhotoPageLifeCircleHooker();
        this.mPhotoPageLifeCircleHooker = photoPageLifeCircleHooker;
        photoPageLifeCircleHooker.onCreate(bundle);
        this.mCameraAnimManager = new CameraAnimManager();
    }

    public final boolean isSecureKeyguard() {
        return ((KeyguardManager) this.mActivity.getSystemService("keyguard")).isKeyguardSecure();
    }

    public boolean isUsingCameraAnim() {
        return this.mDataProvider.getFieldData().isUsingCameraAnim;
    }

    @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment
    public int getThemeRes() {
        return this.mDataProvider.getFieldData().isTransparentTheme ? 2131952029 : 2131952028;
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public View getLayout(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        Trace.beginSection("getLayout");
        PhotoPagePreloadHelper photoPagePreloadHelper = this.mPreloadHelper;
        View pageLayout = photoPagePreloadHelper != null ? photoPagePreloadHelper.getPageLayout() : null;
        if (pageLayout != null) {
            DefaultLogger.d("photoPageStartup", "从缓存中取出PageLayoutView");
        }
        if (pageLayout == null) {
            pageLayout = LayoutInflater.from(this.mActivity).inflate(R.layout.photo_page, viewGroup, false);
        }
        uninstallFunctionsIfReInflate();
        Trace.endSection();
        return pageLayout;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public boolean isLandscapeWindowMode() {
        return MiscUtil.isLandModeAndSupportVersion(this.mActivity);
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public void onViewInflated(final View view) {
        Trace.beginSection("onViewInflated");
        prepareViews();
        refreshNavBarColor(isActionBarShowing());
        this.mPhotoPageLifeCircleHooker.onViewInflated();
        this.mAdapter.setDataProvider(this.mDataProvider);
        this.mDataProvider.getViewModelData().setInMultiWindowMode(ActivityCompat.isInMultiWindowMode(this.mActivity));
        this.mDataProvider.getViewModelData().setConfiguration(this.mActivity.getResources().getConfiguration());
        this.mScreenSceneEffectHelper = new ScreenSceneEffectHelper(this.mDataProvider, this.mActivity, this, this.mPagerHelper);
        if (view.isAttachedToWindow()) {
            configViewLayout(view, ((PhotoChoiceTitle) view.findViewById(R.id.photo_choice_title)).getLayoutParams().height);
        } else {
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.miui.gallery.ui.PhotoPageFragment.1
                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewDetachedFromWindow(View view2) {
                }

                {
                    PhotoPageFragment.this = this;
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewAttachedToWindow(View view2) {
                    view2.removeOnAttachStateChangeListener(this);
                    PhotoPageFragment.this.configViewLayout(view2, ((PhotoChoiceTitle) view.findViewById(R.id.photo_choice_title)).getLayoutParams().height);
                }
            });
        }
        if (this.mDataProvider.getFieldData().isFromRecommendFacePage) {
            this.mOrientationController.setRequestedOrientation(1, "onViewInflated");
        }
        this.mPagerHelper.setOnTapListener(this.mOnSingleTapListener);
        this.mPagerHelper.setOnScaleChangedListener(this.mOnPhotoScaleChangeListener);
        this.mPagerHelper.setOnRotateListener(this.mOnRotateListener);
        this.mPagerHelper.setOnDisplayRectChangedListener(this.mOnPhotoMatrixChangeListener);
        this.mPagerHelper.setOnExitListener(this.mOnExitListener);
        this.mPagerHelper.setOnPhotoViewDragDownOutListener(this.mOnPhotoViewDragDownOutListener);
        this.mPagerHelper.setOnAlphaChangedListener(this.mAlphaChangedListener);
        this.mPagerHelper.setOnDeleteListener(this.mOnPhotoDeleteListener);
        CloudImageLoadingListener cloudImageLoadingListener = this.mDownloadListener;
        if (cloudImageLoadingListener != null) {
            this.mPagerHelper.setOnDownloadListener(cloudImageLoadingListener);
        }
        VerticalSlipLayout verticalSlipLayout = (VerticalSlipLayout) view.findViewById(R.id.slip_layout);
        this.mVerticalSlipLayout = verticalSlipLayout;
        verticalSlipLayout.setDraggable(false);
        this.mConfig.put("theme", Boolean.valueOf(this.mDataProvider.getFieldData().isTransparentTheme));
        PhotoPageThemeManager.IPhotoPageThemeManagerController iPhotoPageThemeManagerController = (PhotoPageThemeManager.IPhotoPageThemeManagerController) PhotoPageBaseManager.registerManager(this, this.mConfig, this, PhotoPageThemeManager.class);
        this.mThemeController = iPhotoPageThemeManagerController;
        iPhotoPageThemeManagerController.setHostView((IMultiThemeView) view.findViewById(R.id.photo_layout));
        if (this.mDataProvider.getFieldData().isFromCamera || this.mDataProvider.getFieldData().isFromCustomWidget || this.mDataProvider.getFieldData().isFromRecommendFacePage || this.mDataProvider.getFieldData().isPreviewMode || isUsingCameraAnim()) {
            this.mThemeController.setDarkTheme(false, true);
        }
        if (!this.mDataProvider.getFieldData().isFromTrash) {
            this.mThemeController.setBackgroundAlpha(0.0f);
        }
        SystemUiUtil.setLayoutFullScreen(this.mActivity.getWindow().getDecorView(), isShowBarsWhenEntering(), this.mActivity.isInMultiWindowMode());
        Trace.endSection();
    }

    @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment
    public void setDownloadListener(CloudImageLoadingListener cloudImageLoadingListener) {
        PhotoPagerHelper photoPagerHelper = this.mPagerHelper;
        if (photoPagerHelper == null || cloudImageLoadingListener == null) {
            return;
        }
        photoPagerHelper.setOnDownloadListener(cloudImageLoadingListener);
    }

    @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment
    public void delayDoAfterTransit() {
        if (!isAdded()) {
            DefaultLogger.i("PhotoPageFragment", "delayDoAfterTransit but not added");
        } else if (this.mDataProvider.getFieldData().isFromCamera || isUsingCameraAnim()) {
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFragment$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageFragment.m1534$r8$lambda$oaJV1jJZHZvdEmM27OT07xI5FI(PhotoPageFragment.this);
                }
            });
        } else {
            doOnTransitEnd();
            trackPhotoPageLoaded();
        }
    }

    public /* synthetic */ void lambda$delayDoAfterTransit$0() {
        if (this.mActivity == null) {
            return;
        }
        doOnTransitEnd();
        trackPhotoPageLoaded();
    }

    public final void trackPhotoPageLoaded() {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.11.0.1.13760");
        if (this.mDataProvider.getFieldData().isFromCamera) {
            hashMap.put("from", "相机");
        } else if (this.mDataProvider.getFieldData().isFromFileManager) {
            hashMap.put("from", "文管");
        } else if (this.mDataProvider.getFieldData().isFromInternal) {
            hashMap.put("from", "相册");
        } else {
            hashMap.put("from", "其他");
        }
        TimeMonitor.trackTimeMonitor("403.11.0.1.13760", hashMap);
    }

    public final void doOnTransitEnd() {
        this.mHandler.sendEmptyMessage(101);
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public ItemViewInfo getEnterViewInfo(int i, long j) {
        CameraAnimManager cameraAnimManager = this.mCameraAnimManager;
        if (cameraAnimManager != null && cameraAnimManager.shouldWork()) {
            if (this.mCameraAnimManager.isLocked()) {
                return null;
            }
            int i2 = getArguments().getInt("anchor_pivot_x", -1);
            int i3 = getArguments().getInt("anchor_pivot_y", -1);
            int i4 = getArguments().getInt("anchor_width", 0);
            int i5 = getArguments().getInt("anchor_height", 0);
            int i6 = i4 / 2;
            int i7 = getArguments().getInt("anchor_radius", i6);
            if (i2 != -1 && i3 != -1) {
                return new ItemViewInfo(0, i2 - i6, i3 - (i5 / 2), i4, i5, i7);
            }
        }
        return super.getEnterViewInfo(i, j);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public void onMenuInflated() {
        inflateActionBarCustomView();
        if (isShowBarsWhenEntering()) {
            setActionBarVisible(true, true);
        }
        refreshTopBarAllElements();
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public ItemViewInfo getItemViewInfo(int i, long j) {
        if (this.mDataProvider.getFieldData().isFromCamera && getArguments().getBoolean("photo_enter_transit", false)) {
            return getEnterViewInfo(i, j);
        }
        if (!this.mDataProvider.getFieldData().isTransparentTheme) {
            return null;
        }
        return PhotoPageDataCache.getInstance().getItemViewInfo(getArguments().getString("extra_photo_page_from", null), j, i);
    }

    public final void doDataSetLoaded(BaseDataSet baseDataSet, boolean z) {
        this.mDataLoaded = true;
        Favorite.FavoritesManager favoritesManager = this.mFavoritesManager;
        if (favoritesManager != null) {
            favoritesManager.updateSet(baseDataSet);
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public void onDataSetLoaded(BaseDataSet baseDataSet, boolean z) {
        super.onDataSetLoaded(baseDataSet, z);
        doDataSetLoaded(baseDataSet, z);
    }

    public final boolean isShowBarsWhenEntering() {
        String string = getArguments().getString("photo_uri");
        return (!TextUtils.isEmpty(string) && UriUtil.isNetUri(Uri.parse(string))) || this.mDataProvider.getFieldData().isAlwaysShowMenu || (getArguments().getBoolean("com.miui.gallery.extra.show_bars_when_enter", true) && !getArguments().getBoolean("com.miui.gallery.extra.photo_enter_choice_mode", false) && !this.mDataProvider.getFieldData().isFromRecommendFacePage && !this.mDataProvider.getFieldData().isPreviewMode && !this.mDataProvider.getFieldData().isFromCamera && !this.mDataProvider.getFieldData().isFromCustomWidget && !isUsingCameraAnim());
    }

    public boolean onBackPressed() {
        if (isMoreActionsShown()) {
            hideMoreActions(true);
            return true;
        }
        Send.ChoiceManager choiceManager = this.mChoiceManager;
        if (choiceManager == null || !choiceManager.onBackPressed()) {
            VideoPreviewManager videoPreviewManager = this.mVideoPreviewManager;
            if (videoPreviewManager != null && videoPreviewManager.mSeekBar != null) {
                this.mVideoPreviewManager.mSeekBar.stopScroll();
            }
            doExit(false);
        }
        return true;
    }

    public ImageView getPhotoView() {
        PhotoPageItem currentItem = this.mPagerHelper.getCurrentItem();
        if (currentItem != null) {
            return (ImageView) currentItem.findViewById(R.id.photoview);
        }
        return null;
    }

    public PhotoPageItem getPageItem() {
        PhotoPagerHelper photoPagerHelper = this.mPagerHelper;
        if (photoPagerHelper != null) {
            return photoPagerHelper.getCurrentItem();
        }
        return null;
    }

    public void onExiting() {
        setActionBarVisible(false, true);
        VerticalSlipLayout verticalSlipLayout = this.mVerticalSlipLayout;
        if (verticalSlipLayout != null) {
            verticalSlipLayout.setDraggable(false);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager.IActionBarOwner
    public void doExit(boolean z) {
        Send.ChoiceManager choiceManager;
        if (z || (choiceManager = this.mChoiceManager) == null || !choiceManager.isSlipped()) {
            PhotoPageOrientationManager.IPhotoPageOrientationManagerController iPhotoPageOrientationManagerController = this.mOrientationController;
            if (iPhotoPageOrientationManagerController != null && iPhotoPageOrientationManagerController.isOrientationChanged() && this.mOrientationController.isScreenOrientationLocked()) {
                setTopBarContentVisibility(false);
                VideoPlayerManager videoPlayerManager = this.mVideoPlayerManager;
                if (videoPlayerManager != null) {
                    videoPlayerManager.setActivityVisible(false);
                }
                onActivityResultForSpecialType();
            }
            PhotoPageLifeCircleHooker photoPageLifeCircleHooker = this.mPhotoPageLifeCircleHooker;
            if (photoPageLifeCircleHooker != null) {
                photoPageLifeCircleHooker.doExit();
            }
            CameraAnimManager cameraAnimManager = this.mCameraAnimManager;
            if (cameraAnimManager != null && cameraAnimManager.doExit()) {
                ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFragment$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        PhotoPageFragment.$r8$lambda$dVbSUx0WvVPUEriFENA_ummCUt0(PhotoPageFragment.this);
                    }
                });
            } else if (this.mDataProvider.getFieldData().isTransparentTheme && !PhotoPageDataCache.getInstance().isItemVisible(getCurrentItemKey(), this.mPager.getCurrentItem(), getArguments().getString("extra_photo_page_from", null)) && PhotoPageDataCache.getInstance().viewToPosition(this.mPager.getCurrentItem(), getArguments().getString("extra_photo_page_from", null))) {
                ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFragment$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        PhotoPageFragment.$r8$lambda$dVbSUx0WvVPUEriFENA_ummCUt0(PhotoPageFragment.this);
                    }
                });
            } else {
                exeExit();
            }
        }
    }

    public final void exeExit() {
        if (this.isExiting) {
            DefaultLogger.d("PhotoPageFragment", "is exiting, return.");
            return;
        }
        this.isExiting = true;
        onExiting();
        PhotoPageItem.PhotoTransitionListener photoTransitionListener = new PhotoPageItem.PhotoTransitionListener() { // from class: com.miui.gallery.ui.PhotoPageFragment.3
            {
                PhotoPageFragment.this = this;
            }

            @Override // com.github.chrisbanes.photoview.TransitionListener
            public void onTransitEnd() {
                DefaultLogger.d("PhotoPageFragment", "exit transition end, finish.");
                PhotoPageFragment photoPageFragment = PhotoPageFragment.this;
                photoPageFragment.isExiting = false;
                photoPageFragment.finish();
                DefaultLogger.d("PhotoPageFragment", "called finish");
            }

            @Override // com.github.chrisbanes.photoview.TransitionListener
            public void onTransitUpdate(float f) {
                if (Math.abs(1.0f - f) <= 0.05f) {
                    PhotoPageFragment photoPageFragment = PhotoPageFragment.this;
                    if (photoPageFragment.isTransitEndSoonCallbackDone) {
                        return;
                    }
                    photoPageFragment.isTransitEndSoonCallbackDone = true;
                    PhotoPageDataCache.getInstance().hidePlaceholderItem(PhotoPageFragment.this.getArguments().getString("extra_photo_page_from", null));
                }
            }
        };
        this.mPager.getCurrentItem();
        if (this.mPagerHelper.doExitTransition(getItemViewInfo(this.mPager.getCurrentItem(), getCurrentItemKey()), photoTransitionListener)) {
            return;
        }
        DefaultLogger.d("PhotoPageFragment", "photoPageItem is null, return directly.");
        finish();
        DefaultLogger.d("PhotoPageFragment", "called finish");
        this.isExiting = false;
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public void finish() {
        finishActivity(-1);
    }

    public final void finishActivity(int i) {
        BaseActivity baseActivity = this.mActivity;
        if (baseActivity == null || baseActivity.isFinishing()) {
            return;
        }
        if (this.mDataProvider.getFieldData().isTransparentTheme) {
            Intent intent = new Intent("com.miui.gallery.action.EXIT_PHOTO_PAGE");
            intent.putExtra("photo_result_code", i);
            LocalBroadcastManager.getInstance(this.mActivity).sendBroadcast(intent);
        }
        this.mActivity.finish();
    }

    public final void doOnPause() {
        PhotoPageLifeCircleHooker photoPageLifeCircleHooker = this.mPhotoPageLifeCircleHooker;
        if (photoPageLifeCircleHooker != null) {
            photoPageLifeCircleHooker.pause();
        }
        Cast.ProjectionManager projectionManager = this.mProjectManager;
        if (projectionManager != null) {
            projectionManager.disableRemoteControl();
        }
    }

    public final void doOnResume() {
        Send.ChoiceManager choiceManager;
        Trace.beginSection("doOnResume");
        setScreenSceneEffect(false);
        if (this.mPhotoPageLifeCircleHooker != null && ((choiceManager = this.mChoiceManager) == null || !choiceManager.isSlipped())) {
            this.mPhotoPageLifeCircleHooker.resume();
        }
        Cast.ProjectionManager projectionManager = this.mProjectManager;
        if (projectionManager != null) {
            projectionManager.enableRemoteControl();
        }
        if (isNeedConfirmPassWord(this.mAdapter.getDataItem(this.mPager.getCurrentItem()))) {
            this.mDataProvider.getFieldData().isNeedConfirmPassWord = false;
            AuthenticatePrivacyPasswordFragment.startAuthenticatePrivacyPassword(this);
        }
        VideoPlayerManager videoPlayerManager = this.mVideoPlayerManager;
        if (videoPlayerManager != null) {
            videoPlayerManager.onResume();
        }
        VideoCompress.VideoCompressManager videoCompressManager = this.mVideoCompressManager;
        if (videoCompressManager != null) {
            videoCompressManager.resume();
        }
        Send.ChoiceManager choiceManager2 = this.mChoiceManager;
        if (choiceManager2 != null) {
            choiceManager2.updateSubTitle();
        }
        Trace.endSection();
    }

    @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment, com.miui.gallery.ui.AbstractViewPagerFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        doOnPause();
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        doOnResume();
    }

    public void onCtaChecked(boolean z) {
        PhotoPageAdapter photoPageAdapter;
        if (this.mProjectManager == null) {
            Cast.ProjectionManager checkAndCreateProjectionManager = checkAndCreateProjectionManager();
            this.mProjectManager = checkAndCreateProjectionManager;
            if (checkAndCreateProjectionManager == null || (photoPageAdapter = this.mAdapter) == null || this.mPager == null) {
                return;
            }
            checkAndCreateProjectionManager.updateSet(photoPageAdapter.getDataSet());
            this.mProjectManager.settleItem(this.mAdapter.getDataItem(this.mPager.getCurrentItem()), this.mPager.getCurrentItem());
        }
    }

    public void onActivityReenter(int i, Intent intent) {
        BurstPhotoManager burstPhotoManager;
        if (intent != null) {
            String stringExtra = intent.getStringExtra("extra_photo_edit_type");
            if ("extra_photo_editor_type_common".equals(stringExtra)) {
                Edit.PhotoEditorManager photoEditorManager = this.mEditorManager;
                if (photoEditorManager == null) {
                    return;
                }
                photoEditorManager.onActivityReenter(intent);
            } else if ("extra_photo_editor_type_refocus".equals(stringExtra)) {
                RefocusManager refocusManager = this.mRefocusManager;
                if (refocusManager == null) {
                    return;
                }
                refocusManager.onActivityReenter(intent);
            } else if ("extra_photo_editor_type_re_pick".equals(stringExtra)) {
                MotionPhotoManager motionPhotoManager = this.mMotionPhotoManager;
                if (motionPhotoManager == null) {
                    return;
                }
                motionPhotoManager.onActivityReenter(intent);
            } else if ("extra_photo_editor_type_doc_photo".equals(stringExtra)) {
                DocPhotoManager docPhotoManager = this.mDocPhotoManager;
                if (docPhotoManager == null) {
                    return;
                }
                docPhotoManager.onActivityReenter(intent);
            } else if ("extra_photo_editor_type_watermark".equals(stringExtra)) {
                WatermarkManager watermarkManager = this.mWatermarkManager;
                if (watermarkManager == null) {
                    return;
                }
                watermarkManager.onActivityReenter(intent);
            } else if ((!"burst_photo_type".equals(stringExtra) && !"time_burst_photo_save_result".equals(stringExtra)) || (burstPhotoManager = this.mBurstPhotoManager) == null) {
            } else {
                burstPhotoManager.onActivityReenter(intent);
            }
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        BurstPhotoManager burstPhotoManager;
        if (i == 24) {
            Cast.ProjectionManager projectionManager = this.mProjectManager;
            if (projectionManager != null) {
                projectionManager.exitSlideShow();
            }
        } else if (i == 30) {
            Edit.PhotoEditorManager photoEditorManager = this.mEditorManager;
            if (photoEditorManager != null) {
                photoEditorManager.onActivityResult(i, i2, intent);
            }
            if (intent != null && intent.getBooleanExtra("photo_secret_finish", false)) {
                finishActivity(0);
            } else {
                this.mDataProvider.getFieldData().isNeedConfirmPassWord = false;
            }
        } else if (i != 36) {
            if (i != 38) {
                if (i == 50) {
                    MotionPhotoManager motionPhotoManager = this.mMotionPhotoManager;
                    if (motionPhotoManager != null) {
                        motionPhotoManager.onActivityResult(i, i2, intent);
                    }
                } else if (i == 59) {
                    CorrectDoc.DocCorrectionManager docCorrectionManager = this.mDocCorrectionManager;
                    if (docCorrectionManager != null) {
                        docCorrectionManager.onActivityResult(i, i2, intent);
                    }
                } else if (i == 44) {
                    RefocusManager refocusManager = this.mRefocusManager;
                    if (refocusManager != null) {
                        refocusManager.onActivityResult(i, i2, intent);
                    }
                } else if (i == 45) {
                    VideoPlayerManager videoPlayerManager = this.mVideoPlayerManager;
                    if (videoPlayerManager != null) {
                        videoPlayerManager.onActivityResult(i, i2, intent);
                    }
                } else if (i != 52) {
                    if (i == 53) {
                        DocPhotoManager docPhotoManager = this.mDocPhotoManager;
                        if (docPhotoManager != null) {
                            docPhotoManager.onActivityResult(i, i2, intent);
                        }
                    } else if (i == 64) {
                        WatermarkManager watermarkManager = this.mWatermarkManager;
                        if (watermarkManager != null) {
                            watermarkManager.onActivityResult(i, i2, intent);
                        }
                        if (intent != null && intent.getBooleanExtra("photo_secret_finish", false)) {
                            finishActivity(0);
                        } else {
                            this.mDataProvider.getFieldData().isNeedConfirmPassWord = false;
                        }
                    } else if (i == 65 && i2 == -1 && this.mVideoPreviewManager != null && intent != null) {
                        long longExtra = intent.getLongExtra("video_miplay_progress", -1L);
                        boolean booleanExtra = intent.getBooleanExtra("video_miplay_end", false);
                        if (longExtra != -1) {
                            VideoPreviewManager videoPreviewManager = this.mVideoPreviewManager;
                            if (booleanExtra) {
                                longExtra = videoPreviewManager.mTotalDuration;
                            }
                            videoPreviewManager.seekTo(longExtra, false);
                        }
                    }
                } else if (i2 == -1 && (burstPhotoManager = this.mBurstPhotoManager) != null) {
                    burstPhotoManager.onActivityResult(intent);
                }
            } else if (i2 == -1) {
                this.mDataProvider.getFieldData().isNeedConfirmPassWord = false;
                if (intent != null) {
                    String stringExtra = intent.getStringExtra("photo_focused_path");
                    boolean booleanExtra2 = intent.getBooleanExtra("photodetail_is_photo_renamed", false);
                    if (this.mDataProvider.getFieldData().isFromFileManager && !TextUtils.isEmpty(stringExtra) && booleanExtra2) {
                        this.mAdapter.getDataSet().replaceFile(this.mAdapter.getDataItem(this.mPager.getCurrentItem()).getPathDisplayBetter(), stringExtra);
                    }
                    onContentChanged();
                }
            }
        } else if (i2 != -1) {
            if (this.mDataProvider.getFieldData().isFromTrash) {
                finishActivity(-1);
            } else {
                finishActivity(i2);
            }
        } else {
            this.mDataProvider.getFieldData().isNeedConfirmPassWord = false;
        }
        if (i2 == -1 && intent != null && intent.getExtras() != null && intent.getExtras().getInt("request_code") == 67) {
            this.mDataProvider.onContentChanged();
        }
        hideNarBarForFullScreenGesture();
        super.onActivityResult(i, i2, intent);
        this.mPagerHelper.onActivityResult(i, i2, intent);
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public void onShared(boolean z) {
        super.onShared(z);
        Send.ChoiceManager choiceManager = this.mChoiceManager;
        if (choiceManager != null) {
            choiceManager.onShared(z);
        }
    }

    @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment, com.miui.gallery.ui.AbstractViewPagerFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        PhotoPagerSamplingStatHelper.onDestroy();
        PhotoPageLifeCircleHooker photoPageLifeCircleHooker = this.mPhotoPageLifeCircleHooker;
        if (photoPageLifeCircleHooker != null) {
            photoPageLifeCircleHooker.onDestroyView();
        }
        Send.ChoiceManager choiceManager = this.mChoiceManager;
        if (choiceManager != null) {
            choiceManager.release();
        }
        RefocusManager refocusManager = this.mRefocusManager;
        if (refocusManager != null) {
            refocusManager.release();
            this.mRefocusManager = null;
        }
        DocPhotoManager docPhotoManager = this.mDocPhotoManager;
        if (docPhotoManager != null) {
            docPhotoManager.release();
            this.mDocPhotoManager = null;
        }
        WatermarkManager watermarkManager = this.mWatermarkManager;
        if (watermarkManager != null) {
            watermarkManager.release();
            this.mWatermarkManager = null;
        }
        BurstPhotoManager burstPhotoManager = this.mBurstPhotoManager;
        if (burstPhotoManager != null) {
            burstPhotoManager.release();
        }
        IdleUITaskHelper.getInstance().clean();
        this.mDataLoaded = false;
    }

    @Override // com.miui.gallery.app.fragment.MiuiFragment, androidx.fragment.app.Fragment
    public void onMultiWindowModeChanged(boolean z) {
        DefaultLogger.d("PhotoPageFragment", "isInMultiWindowMode: %b", Boolean.valueOf(z));
        this.mDataProvider.getViewModelData().setInMultiWindowMode(z);
        if (z) {
            setScreenSceneEffect(true);
        } else {
            setScreenSceneEffect(false);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public void setCurrentPosition(int i, boolean z) {
        if (this.mPager.isLeftScrolled() || i <= 0) {
            return;
        }
        this.mPager.setCurrentItem(i, z);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public int getPageCount() {
        return this.mAdapter.getCount();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public int getPageWidth() {
        return this.mPager.getWidth();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public int getPageHeight() {
        return this.mPager.getHeight();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public void setActionBarVisible(final boolean z, boolean z2) {
        if (z == isActionBarShowing()) {
            return;
        }
        if (z) {
            Send.ChoiceManager choiceManager = this.mChoiceManager;
            if (choiceManager != null && choiceManager.isSlipped()) {
                DefaultLogger.d("PhotoPageFragment", "setActionBarVisible cancer mChoiceManager.isSlipped()");
                return;
            } else if (this.mDataProvider.getFieldData().isFromRecommendFacePage || this.mDataProvider.getFieldData().isPreviewMode) {
                DefaultLogger.d("PhotoPageFragment", "setActionBarVisible cancer isFromRecommendFacePage | isPreviewMode");
                return;
            } else {
                showBars(z2);
            }
        } else if (this.mDataProvider.getFieldData().isAlwaysShowMenu) {
            DefaultLogger.d("PhotoPageFragment", "setActionBarVisible cancer isAlwaysShowMenu");
            return;
        } else {
            hideBars(z2);
        }
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFragment$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                PhotoPageFragment.$r8$lambda$j0IJ9etLVy5vOSY9bo6NvbHHAeA(PhotoPageFragment.this, z);
            }
        });
    }

    /* renamed from: onActionBarVisibilityChanged */
    public final void lambda$setActionBarVisible$1(boolean z) {
        if (z && this.mIsConfigurationChanged && !GalleryPreferences.LockOrientation.isNoticeShowed()) {
            ThreadManager.getMainHandler().postDelayed(this.mDelayShowTask, 300L);
            GalleryPreferences.LockOrientation.hasNoticeShowed();
        }
        VideoPreviewManager videoPreviewManager = this.mVideoPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.onActionBarVisibilityChanged(z);
        }
        if (!isExiting()) {
            refreshTheme(false);
            this.mPagerHelper.onActionBarVisibleChanged(z, getActionBarHeight());
            if (this.mActivity == null) {
                return;
            }
            if (!hideNarBarForFullScreenGesture()) {
                setSystemBarVisibility(z);
            }
            refreshNavBarColor(z);
        }
    }

    public final void SpecialTypeEnterExitAnim() {
        SpecialTypeManager specialTypeManager;
        if (!this.mDataProvider.getFieldData().isAlwaysShowMenu && (specialTypeManager = this.mSpecialTypeManager) != null) {
            specialTypeManager.setEnterVisible(false);
        }
    }

    public boolean isPlaySlideshow() {
        return this.mDataProvider.getFieldData().isPlaySlideshow;
    }

    public void setPlaySlideshow(boolean z) {
        this.mDataProvider.getFieldData().isPlaySlideshow = z;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IBarsOwner
    public PhotoPageOrientationManager.IPhotoPageOrientationManagerController getOrientationController() {
        return this.mOrientationController;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IBarsOwner
    public PhotoPageThemeManager.IPhotoPageThemeManagerController getThemeController() {
        return this.mThemeController;
    }

    public CameraAnimManager getCameraAnimManager() {
        return this.mCameraAnimManager;
    }

    public final boolean inPreviewMode() {
        Send.ChoiceManager choiceManager;
        return !isActionBarShowing() && ((choiceManager = this.mChoiceManager) == null || !choiceManager.isPendingSlipped());
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public void refreshTheme(boolean z) {
        if (this.mThemeController != null) {
            if (inPreviewMode()) {
                this.mThemeController.setDarkTheme(true, z);
            } else {
                this.mThemeController.setLightTheme(true, z);
            }
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public void onSendClick() {
        setSystemBarVisibility(false);
        SpecialTypeManager specialTypeManager = this.mSpecialTypeManager;
        if (specialTypeManager != null) {
            specialTypeManager.setSpecialEnterState(false, false);
        }
        this.mPagerHelper.onActionBarVisibleChanged(false, getActionBarHeight());
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public PhotoPageAdapter getAdapter() {
        return this.mAdapter;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public float getCurrentItemScale() {
        return this.mPagerHelper.getCurrentItemScale();
    }

    public final void refreshNavBarColor(boolean z) {
        if (BaseBuildUtil.isLargeScreen(this.mActivity)) {
            refreshNavBarColorForFullNavAndShown(this.mDataProvider.getFieldData().mCurrent.getDataItem());
            return;
        }
        int i = 0;
        if (BaseScreenUtil.isFullScreenGestureNav(this.mActivity)) {
            com.miui.gallery.compat.view.WindowCompat.setNavigationBarColor(this.mActivity.getWindow(), 0);
        } else if (this.mDataProvider.getFieldData().isVideoPlayerSupportActionBarAdjust) {
            if (z) {
                i = getResources().getColor(R.color.navigation_bar_alpha_background, null);
            }
            SystemUiUtil.setNavigationBarColor(this.mActivity, i, this.mIsNightMode);
        } else {
            if (z) {
                i = this.mIsNightMode ? -16777216 : -1;
            }
            com.miui.gallery.compat.view.WindowCompat.setNavigationBarColor(this.mActivity.getWindow(), i);
        }
    }

    public final void refreshNavBarColorForFullNavAndShown(BaseDataItem baseDataItem) {
        if (!BaseBuildUtil.isLargeScreen(this.mActivity)) {
            return;
        }
        com.miui.gallery.compat.view.WindowCompat.setNavigationBarColor(this.mActivity.getWindow(), (!BaseScreenUtil.isFullScreenGestureNav(this.mActivity) || baseDataItem == null || baseDataItem.isVideo()) ? this.mIsNightMode ? -16777216 : -1 : 0);
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        VideoPreviewManager videoPreviewManager = this.mVideoPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.onStart();
        }
        PhotoPageLifeCircleHooker photoPageLifeCircleHooker = this.mPhotoPageLifeCircleHooker;
        if (photoPageLifeCircleHooker != null) {
            photoPageLifeCircleHooker.onStart();
        }
        refreshNavBarColorForFullNavAndShown(this.mDataProvider.getFieldData().mCurrent.getDataItem());
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onStop() {
        this.mDataProvider.getFieldData().isNeedConfirmPassWord = true;
        super.onStop();
        VideoPreviewManager videoPreviewManager = this.mVideoPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.onStop();
        }
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
        DefaultLogger.d("PhotoPageFragment", "onLowMemory");
        TileReusedBitCache.getInstance().clear();
    }

    @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment, com.miui.gallery.ui.AbstractViewPagerFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        TileReusedBitCache.getInstance().clear();
        ScreenSceneEffectHelper screenSceneEffectHelper = this.mScreenSceneEffectHelper;
        if (screenSceneEffectHelper != null) {
            screenSceneEffectHelper.release();
        }
        ScreenBroadcastReceiver screenBroadcastReceiver = this.mScreenReceiver;
        if (screenBroadcastReceiver != null) {
            ReceiverUtils.safeUnregisterReceiver(this.mActivity, screenBroadcastReceiver);
        }
        if (this.mPhotoRefreshReceiver != null) {
            LocalBroadcastManager.getInstance(this.mActivity).unregisterReceiver(this.mPhotoRefreshReceiver);
        }
        PhotoPageLifeCircleHooker photoPageLifeCircleHooker = this.mPhotoPageLifeCircleHooker;
        if (photoPageLifeCircleHooker != null) {
            photoPageLifeCircleHooker.onDestroy();
        }
        Edit.PhotoEditorManager photoEditorManager = this.mEditorManager;
        if (photoEditorManager != null) {
            photoEditorManager.onDestroy();
        }
        VideoPlayerManager videoPlayerManager = this.mVideoPlayerManager;
        if (videoPlayerManager != null) {
            videoPlayerManager.onDestroy();
        }
        MotionPhotoManager motionPhotoManager = this.mMotionPhotoManager;
        if (motionPhotoManager != null) {
            motionPhotoManager.onDestroy();
        }
        SpecialTypeManager specialTypeManager = this.mSpecialTypeManager;
        if (specialTypeManager != null) {
            specialTypeManager.onDestroy();
        }
        VideoPreviewManager videoPreviewManager = this.mVideoPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.onDestroy();
        }
        if (this.mDelayShowTask != null) {
            ThreadManager.getMainHandler().removeCallbacks(this.mDelayShowTask);
            this.mDelayShowTask = null;
        }
        if (ConnectControllerSingleton.getInstance() != null) {
            ConnectControllerSingleton.getInstance().release();
        }
        CacheOfAllFacesInOnePhoto.getInstance().clearCache();
        PhotoPageDataCache.getInstance().hidePlaceholderItem(getArguments().getString("extra_photo_page_from", null));
        if (isStatUserShowMenuBar()) {
            statUserShowMenuBar();
        }
        PhotoPageAdapter photoPageAdapter = this.mAdapter;
        if (photoPageAdapter != null) {
            photoPageAdapter.release();
            this.mAdapter = null;
        }
        if (this.mItemSettleRunnable != null) {
            ThreadManager.getMainHandler().removeCallbacks(this.mItemSettleRunnable);
            this.mItemSettleRunnable = null;
        }
        TimeMonitor.cancelTimeMonitor("403.11.0.1.13760");
        super.onDestroy();
    }

    public final boolean isStatUserShowMenuBar() {
        return this.mDataProvider.getFieldData().isFromCamera;
    }

    public final void statUserShowMenuBar() {
        HashMap hashMap = new HashMap(1);
        hashMap.put(MapBundleKey.MapObjKey.OBJ_SL_INDEX, String.valueOf(this.mUserShowBarIndex));
        SamplingStatHelper.recordCountEvent("photo", "photo_user_show_menu_bar", hashMap);
    }

    public final void doOnItemChanged(int i) {
        PhotoPagerHelper photoPagerHelper;
        PhotoPageItem currentItem;
        if (this.mAdapter == null) {
            return;
        }
        Trace.beginSection("doOnItemChanged");
        BaseDataItem dataItem = this.mAdapter.getDataItem(i);
        if (this.mDataProvider.getFieldData().isFromTrash && (photoPagerHelper = this.mPagerHelper) != null && (currentItem = photoPagerHelper.getCurrentItem()) != null) {
            if (currentItem instanceof PhotoPageVideoItem) {
                ((PhotoPageVideoItem) currentItem).setPlayEnable(!this.mDataProvider.getFieldData().isFromTrash);
            }
            if (currentItem instanceof PhotoPageGifItem) {
                ((PhotoPageGifItem) currentItem).setPlayEnable(!this.mDataProvider.getFieldData().isFromTrash);
            }
        }
        if (dataItem == null) {
            Trace.endSection();
            return;
        }
        VideoPreviewManager videoPreviewManager = this.mVideoPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.updateItem(dataItem);
        }
        VideoPlayerManager videoPlayerManager = this.mVideoPlayerManager;
        if (videoPlayerManager != null) {
            videoPlayerManager.mCirculateStatusListener = null;
        }
        updateCurrentData(i, dataItem);
        refreshMenuItemsIfPrepared(dataItem);
        refreshTopBarInfo(dataItem);
        Trace.endSection();
    }

    public final void updateCurrentData(int i, BaseDataItem baseDataItem) {
        DefaultLogger.d("PhotoPageFragment", "update current data when changed/settled, position [%d], item [%s]", Integer.valueOf(i), baseDataItem);
        this.mDataProvider.getViewModelData().setCurrentPosition(i);
        this.mDataProvider.getViewModelData().setCurrentDataItem(baseDataItem);
        this.mDataProvider.getFieldData().mCurrent.itemView = this.mPagerHelper.getCurrentItem();
    }

    public void doOnItemSettled(final int i) {
        final BaseDataItem dataItem;
        Cast.ProjectionManager projectionManager;
        Send.ChoiceManager choiceManager;
        PhotoPageAdapter photoPageAdapter = this.mAdapter;
        if (photoPageAdapter == null || (dataItem = photoPageAdapter.getDataItem(i)) == null) {
            return;
        }
        updateCurrentData(i, dataItem);
        TrackController.trackFling("403.11.4.1.11158");
        Trace.beginSection("doOnItemSettled");
        refreshNavBarColorForFullNavAndShown(dataItem);
        Trace.beginSection("ProjectSettled");
        Cast.ProjectionManager projectionManager2 = this.mProjectManager;
        if (projectionManager2 != null) {
            projectionManager2.settleItem(dataItem, i);
        }
        Trace.endSection();
        Trace.beginSection("LifeCircleSettled");
        if (this.mPhotoPageLifeCircleHooker != null && ((choiceManager = this.mChoiceManager) == null || !choiceManager.isSlipped())) {
            this.mPhotoPageLifeCircleHooker.settleItem(dataItem);
        }
        Trace.endSection();
        Trace.beginSection("ChoiceSettled");
        Send.ChoiceManager choiceManager2 = this.mChoiceManager;
        if (choiceManager2 != null) {
            choiceManager2.settleItem(dataItem);
        }
        Trace.endSection();
        Trace.beginSection("refreshProjectState");
        if (ShareStateRouter.IS_MISHARE_AVAILABLE.get(this.mActivity).booleanValue() && (projectionManager = this.mProjectManager) != null) {
            projectionManager.refreshProjectState();
        }
        Trace.endSection();
        Trace.beginSection("VideoPreviewSettled");
        VideoPreviewManager videoPreviewManager = this.mVideoPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.settleItem();
        }
        Trace.endSection();
        Trace.beginSection("SpecialTypeSettled");
        SpecialTypeManager specialTypeManager = this.mSpecialTypeManager;
        if (specialTypeManager != null) {
            specialTypeManager.updateItem(dataItem);
        }
        Trace.endSection();
        if (this.mItemSettleRunnable != null) {
            ThreadManager.getMainHandler().removeCallbacks(this.mItemSettleRunnable);
        }
        this.mItemSettleRunnable = new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFragment.4
            {
                PhotoPageFragment.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                Trace.beginSection("refreshMenuItems");
                PhotoPageFragment.this.refreshMenuItems(dataItem);
                Trace.endSection();
                Trace.beginSection("setScreenSceneEffect");
                PhotoPageFragment.this.setScreenSceneEffect(dataItem.isVideo());
                Trace.endSection();
                Trace.beginSection("onActionBarVisibleChanged");
                PhotoPageFragment photoPageFragment = PhotoPageFragment.this;
                photoPageFragment.mPagerHelper.onActionBarVisibleChanged(photoPageFragment.isActionBarShowing(), PhotoPageFragment.this.getActionBarHeight());
                Trace.endSection();
                Trace.beginSection("hideNarBarForFullScreenGesture");
                PhotoPageFragment.this.hideNarBarForFullScreenGesture();
                Trace.endSection();
                Trace.beginSection("refreshSecureFlag");
                PhotoPageFragment.this.refreshSecureFlag(dataItem.isSecret());
                Trace.endSection();
                Trace.beginSection("requestAnnouncementEvent");
                PhotoPageFragment photoPageFragment2 = PhotoPageFragment.this;
                TalkBackUtil.requestAnnouncementEvent(photoPageFragment2.mPager, dataItem.getContentDescription(photoPageFragment2.mActivity));
                Trace.endSection();
                Trace.beginSection("showPlaceholderItem");
                PhotoPageDataCache.getInstance().postShowPlaceholderItem(i, PhotoPageFragment.this.getArguments().getString("extra_photo_page_from", null));
                Trace.endSection();
            }
        };
        ThreadManager.getMainHandler().post(this.mItemSettleRunnable);
        Trace.endSection();
    }

    public final void refreshSecureFlag(boolean z) {
        BaseActivity baseActivity = this.mActivity;
        if (baseActivity == null || baseActivity.isFinishing() || this.mActivity.isDestroyed()) {
            return;
        }
        int i = this.mActivity.getWindow().getAttributes().flags;
        if (z) {
            if ((i & 8192) != 0) {
                return;
            }
            DefaultLogger.d("PhotoPageFragment", "add FLAG_SECURE");
            this.mActivity.getWindow().addFlags(8192);
        } else if ((i & 8192) == 0) {
        } else {
            DefaultLogger.d("PhotoPageFragment", "clear FLAG_SECURE");
            this.mActivity.getWindow().clearFlags(8192);
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public void onItemSettled(int i) {
        super.onItemSettled(i);
        doOnItemSettled(i);
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public void onItemChanged(int i) {
        super.onItemChanged(i);
        doOnItemChanged(i);
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, com.miui.gallery.ui.PhotoPagerHelper.OnPageChangedListener
    public void onPageScrolled(int i, float f, int i2) {
        super.onPageScrolled(i, f, i2);
        VideoPreviewManager videoPreviewManager = this.mVideoPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.onPageScrolled(i, f, i2);
        }
        ScrollPrivacyManager scrollPrivacyManager = this.mScrollPrivacyManager;
        if (scrollPrivacyManager != null) {
            scrollPrivacyManager.onPageScrolled(i, f, i2);
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, com.miui.gallery.ui.PhotoPagerHelper.OnPageChangedListener
    public void onPageScrollStateChanged(int i) {
        super.onPageScrollStateChanged(i);
        VideoPreviewManager videoPreviewManager = this.mVideoPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.onPageScrollStateChanged(i);
        }
        ScrollPrivacyManager scrollPrivacyManager = this.mScrollPrivacyManager;
        if (scrollPrivacyManager != null) {
            scrollPrivacyManager.onPageScrollStateChanged(i);
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, com.miui.gallery.ui.PhotoPagerHelper.OnImageLoadFinishListener
    public void onImageLoadFinish(String str) {
        setScreenSceneEffect(false);
        Edit.PhotoEditorManager photoEditorManager = this.mEditorManager;
        if (photoEditorManager != null) {
            photoEditorManager.onImageLoadFinish(str);
        }
        RefocusManager refocusManager = this.mRefocusManager;
        if (refocusManager != null) {
            refocusManager.onImageLoadFinish(str);
        }
        DocPhotoManager docPhotoManager = this.mDocPhotoManager;
        if (docPhotoManager != null) {
            docPhotoManager.onImageLoadFinish(str);
        }
        WatermarkManager watermarkManager = this.mWatermarkManager;
        if (watermarkManager != null) {
            watermarkManager.onImageLoadFinish(str);
        }
        MotionPhotoManager motionPhotoManager = this.mMotionPhotoManager;
        if (motionPhotoManager != null) {
            motionPhotoManager.onImageLoadFinish(str);
        }
        CorrectDoc.DocCorrectionManager docCorrectionManager = this.mDocCorrectionManager;
        if (docCorrectionManager != null) {
            docCorrectionManager.onImageLoadFinish(str);
        }
        VideoPlayerManager videoPlayerManager = this.mVideoPlayerManager;
        if (videoPlayerManager != null) {
            videoPlayerManager.onImageLoadFinish(str);
        }
        BurstPhotoManager burstPhotoManager = this.mBurstPhotoManager;
        if (burstPhotoManager != null) {
            burstPhotoManager.onImageLoadFinish(str);
        }
    }

    /* loaded from: classes2.dex */
    public class ScreenSceneEffectHelper extends DataPrepareHelper implements ScreenSceneAlgorithmManager.ClassifyResultListener {
        public final Set<Long> mAiModeEnterUpdateSet;
        public final HashMap<Long, Integer> mModeCache;
        public PendingSetRunnable mPendingSetRunnable;
        public final PhotoPagerHelper mPhotoPagerHelper;
        public final Set<Long> mWorkingSet;

        @Override // com.miui.gallery.ui.photoPage.bars.data.DataPrepareHelper
        public void doPrepare(BaseDataItem baseDataItem, int i) {
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ScreenSceneEffectHelper(IDataProvider iDataProvider, FragmentActivity fragmentActivity, LifecycleOwner lifecycleOwner, PhotoPagerHelper photoPagerHelper) {
            super(iDataProvider, fragmentActivity, lifecycleOwner);
            PhotoPageFragment.this = r1;
            this.mModeCache = new HashMap<>();
            this.mWorkingSet = new HashSet();
            this.mAiModeEnterUpdateSet = new HashSet();
            this.mPhotoPagerHelper = photoPagerHelper;
        }

        public final void doClassify(BaseDataItem baseDataItem, PhotoPageItem photoPageItem) {
            if (baseDataItem == null || photoPageItem == null || !(photoPageItem.mPhotoView.getDrawable() instanceof BitmapDrawable) || this.mWorkingSet.contains(Long.valueOf(baseDataItem.getKey()))) {
                return;
            }
            DefaultLogger.d("PhotoPageFragment_ScreenScene", "doClassify [%s] =>", Long.valueOf(baseDataItem.getKey()));
            if (ScreenSceneAlgorithmManager.isInitAlgorithm()) {
                this.mWorkingSet.add(Long.valueOf(baseDataItem.getKey()));
                if (!this.mAiModeEnterUpdateSet.contains(Long.valueOf(baseDataItem.getKey()))) {
                    this.mAiModeEnterUpdateSet.add(Long.valueOf(baseDataItem.getKey()));
                }
            }
            ScreenSceneAlgorithmManager.classify(((BitmapDrawable) photoPageItem.mPhotoView.getDrawable()).getBitmap(), baseDataItem.getKey(), baseDataItem.getOrientation(), this);
        }

        @Override // com.miui.gallery.util.photoview.ScreenSceneAlgorithmManager.ClassifyResultListener
        public void onClassifyResult(int i, long j) {
            FragmentActivity fragmentActivity;
            BaseDataItem dataItem;
            if (i != 18) {
                this.mAiModeEnterUpdateSet.remove(Long.valueOf(j));
            }
            if (i == -1 || (fragmentActivity = this.mContext) == null || fragmentActivity.isFinishing() || this.mContext.isDestroyed()) {
                return;
            }
            DefaultLogger.d("PhotoPageFragment_ScreenScene", "onClassifyResult [%s] [%s] =>", Long.valueOf(j), Integer.valueOf(i));
            this.mModeCache.put(Long.valueOf(j), Integer.valueOf(i));
            this.mWorkingSet.remove(Long.valueOf(j));
            if (i == -2 || (dataItem = this.mDataProvider.getFieldData().mCurrent.getDataItem()) == null || dataItem.getKey() != j) {
                return;
            }
            setScreenSceneClassificationInternal(dataItem, i);
        }

        public final void setScreenSceneClassification(BaseDataItem baseDataItem, PhotoPageItem photoPageItem) {
            if (baseDataItem == null) {
                return;
            }
            Integer orDefault = this.mModeCache.getOrDefault(Long.valueOf(baseDataItem.getKey()), -1);
            if (orDefault == null || orDefault.intValue() <= -1) {
                DefaultLogger.d("PhotoPageFragment_ScreenScene", "setScreenSceneClassification no cache [%s] =>", Long.valueOf(baseDataItem.getKey()));
                doClassify(baseDataItem, photoPageItem);
                return;
            }
            if (orDefault.intValue() == 18 && !baseDataItem.isWatermarked() && !baseDataItem.isAIModeType(17592186044416L)) {
                baseDataItem.setAIModeTypeFlags(17592186044416L);
                this.mAiModeEnterUpdateSet.add(Long.valueOf(baseDataItem.getKey()));
            }
            setScreenSceneClassificationInternal(baseDataItem, orDefault.intValue());
        }

        public final void clearScreenSceneClassificationMode() {
            DefaultLogger.d("PhotoPageFragment_ScreenScene", "clearScreenSceneClassification");
            ThreadManager.getWorkHandler().post(PhotoPageFragment$ScreenSceneEffectHelper$$ExternalSyntheticLambda0.INSTANCE);
        }

        public final void setScreenSceneClassificationInternal(BaseDataItem baseDataItem, int i) {
            this.mDataProvider.getFieldData().mCurrentClassification = i;
            PendingSetRunnable pendingSetRunnable = this.mPendingSetRunnable;
            if (pendingSetRunnable != null && pendingSetRunnable.isSameTask(baseDataItem.getKey(), i)) {
                ThreadManager.getWorkHandler().removeCallbacks(this.mPendingSetRunnable);
            }
            if (this.mDataProvider.getFieldData().mCurrent.isInMultiWindowMode()) {
                clearScreenSceneClassificationMode();
            } else {
                this.mPendingSetRunnable = new PendingSetRunnable(baseDataItem.getKey(), i);
                ThreadManager.getWorkHandler().post(this.mPendingSetRunnable);
            }
            if (this.mAiModeEnterUpdateSet.contains(Long.valueOf(baseDataItem.getKey()))) {
                this.mAiModeEnterUpdateSet.remove(Long.valueOf(baseDataItem.getKey()));
                if (checkWatermarkShowConditions(baseDataItem)) {
                    DefaultLogger.d("PhotoPageFragment_ScreenScene", "show dynamic entrance");
                    PhotoPageFragment.this.readyToUpdateEnters(baseDataItem);
                    return;
                }
                DefaultLogger.d("PhotoPageFragment_ScreenScene", "won't show dynamic entrance");
            }
        }

        public final boolean checkWatermarkShowConditions(BaseDataItem baseDataItem) {
            if (baseDataItem.isSecret() && this.mDataProvider.getFieldData().mArguments.getBoolean("photo_secret_watermark", false)) {
                this.mDataProvider.getFieldData().mArguments.remove("photo_secret_watermark");
                DefaultLogger.d("PhotoPageFragment_ScreenScene", "item comes from secret after watermark edition");
                return false;
            } else if ((baseDataItem instanceof CloudItem) && ((CloudItem) baseDataItem).isShare()) {
                DefaultLogger.d("PhotoPageFragment_ScreenScene", "item comes from share album");
                return false;
            } else if (baseDataItem.isWatermarked()) {
                DefaultLogger.d("PhotoPageFragment_ScreenScene", "item has been watermarked");
                return false;
            } else if (this.mDataProvider.getFieldData().mCurrentClassification == 18) {
                return true;
            } else {
                DefaultLogger.d("PhotoPageFragment_ScreenScene", "item is not a ID_CARD");
                return false;
            }
        }

        public final void release() {
            if (this.mPendingSetRunnable != null) {
                ThreadManager.getWorkHandler().removeCallbacks(this.mPendingSetRunnable);
            }
        }

        /* loaded from: classes2.dex */
        public class PendingSetRunnable implements Runnable {
            public final int classification;
            public final long id;

            public PendingSetRunnable(long j, int i) {
                ScreenSceneEffectHelper.this = r1;
                this.id = j;
                this.classification = i;
            }

            public boolean isSameTask(long j, int i) {
                return j == this.id && i == this.classification;
            }

            @Override // java.lang.Runnable
            public void run() {
                DefaultLogger.d("PhotoPageFragment_ScreenScene", "setScreenSceneClassification [%s] [%s]", Long.valueOf(this.id), Integer.valueOf(this.classification));
                ScreenSceneClassificationUtil.setScreenSceneClassification(this.classification);
            }
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public void setScreenSceneEffect(boolean z) {
        PhotoPageItem pageItem;
        BaseDataItem dataItem;
        IDataProvider iDataProvider = this.mDataProvider;
        if (iDataProvider == null || iDataProvider.getFieldData() == null) {
            return;
        }
        DataProvider.FieldData fieldData = this.mDataProvider.getFieldData();
        if (z && fieldData.mCurrentClassification != -1) {
            fieldData.mCurrentClassification = -1;
            this.mScreenSceneEffectHelper.clearScreenSceneClassificationMode();
        } else if (!isResumed() || (pageItem = getPageItem()) == null || (dataItem = pageItem.getDataItem()) == null || !SdkHelper.IS_MIUI || !ScreenUtils.isDeviceSupportAIMode()) {
        } else {
            Send.ChoiceManager choiceManager = this.mChoiceManager;
            boolean z2 = choiceManager != null && (choiceManager.isSlipping() || this.mChoiceManager.isSlipped());
            if (fieldData.isFromRecommendFacePage || fieldData.isPreviewMode || z2 || dataItem.isVideo()) {
                this.mScreenSceneEffectHelper.clearScreenSceneClassificationMode();
            } else if (!(pageItem.mPhotoView.getDrawable() instanceof BitmapDrawable)) {
            } else {
                this.mScreenSceneEffectHelper.setScreenSceneClassification(dataItem, pageItem);
            }
        }
    }

    public /* synthetic */ void lambda$new$2() {
        doExit(false);
    }

    public final void doEntrancesClose() {
        if (isActionBarShowing()) {
            hideBars(true);
            SpecialTypeEnterExitAnim();
        }
    }

    public /* synthetic */ void lambda$new$3(float f, float f2) {
        if (this.mDataProvider.getFieldData().isFromRecommendFacePage || this.mDataProvider.getFieldData().isPreviewMode) {
            doExit(false);
        } else if (isActionBarShowing()) {
            setActionBarVisible(false, true);
            this.mDataProvider.getFieldData().isInPreviewMode = true;
        } else {
            setActionBarVisible(true, true);
            this.mDataProvider.getFieldData().isInPreviewMode = false;
            SpecialTypeManager specialTypeManager = this.mSpecialTypeManager;
            if (specialTypeManager != null) {
                specialTypeManager.setEnterVisible(true);
            }
            if (this.mUserShowBarIndex >= 0) {
                return;
            }
            this.mUserShowBarIndex = this.mPager.getCurrentItem();
        }
    }

    public /* synthetic */ void lambda$new$4(float f, float f2, float f3) {
        if (isEntering() || isExiting()) {
            return;
        }
        Send.ChoiceManager choiceManager = this.mChoiceManager;
        if (choiceManager != null) {
            choiceManager.onPhotoScale(f3);
        }
        if (isActionBarShowing() && f2 > 1.0f && f3 > 1.0f) {
            setActionBarVisible(false, true);
        }
        if (!this.mDataProvider.getFieldData().isTransparentTheme || f3 >= 1.0f || f2 >= 1.0f || PhotoPageDataCache.getInstance().isItemVisible(getCurrentItemKey(), this.mPager.getCurrentItem(), getArguments().getString("extra_photo_page_from", null))) {
            return;
        }
        PhotoPageDataCache.getInstance().viewToPosition(this.mPager.getCurrentItem(), getArguments().getString("extra_photo_page_from", null));
    }

    public /* synthetic */ void lambda$new$5(float f) {
        Cast.ProjectionManager projectionManager;
        if (isEntering() || isExiting()) {
            return;
        }
        setActionBarVisible(false, true);
        if (this.mAdapter.getDataItem(this.mPager.getCurrentItem()) == null || (projectionManager = this.mProjectManager) == null) {
            return;
        }
        projectionManager.updateRotate(f);
    }

    public /* synthetic */ void lambda$new$6(RectF rectF) {
        BaseDataItem dataItem;
        Cast.ProjectionManager projectionManager;
        if (isEntering()) {
            return;
        }
        Send.ChoiceManager choiceManager = this.mChoiceManager;
        if (choiceManager != null && (choiceManager.isSlipping() || this.mChoiceManager.isSlipped())) {
            return;
        }
        PhotoPageDataCache photoPageDataCache = PhotoPageDataCache.getInstance();
        String str = null;
        if (getArguments() != null) {
            str = getArguments().getString("extra_photo_page_from", null);
        }
        photoPageDataCache.exeShowPlaceholderItem(str);
        if (isExiting() || (dataItem = this.mAdapter.getDataItem(this.mPager.getCurrentItem())) == null || (projectionManager = this.mProjectManager) == null || !projectionManager.isConnected()) {
            return;
        }
        this.mProjectManager.updateRemoteView(dataItem, rectF);
    }

    public /* synthetic */ void lambda$new$7() {
        this.mDataProvider.onContentChanged();
    }

    public void readyToUpdateEnters(BaseDataItem baseDataItem) {
        TrackController.trackExpose("403.11.8.1.16476", AutoTracking.getRef());
        if (this.mSpecialTypeManager != null) {
            DefaultLogger.d("PhotoPageFragment", "special type recognition finished first");
            updateWaterMarkEnters(baseDataItem);
            this.mWaterSpecialTypeListener = null;
            return;
        }
        DefaultLogger.d("PhotoPageFragment", "special type classification finished first");
        this.mWaterSpecialTypeListener = new WaterSpecialTypeListener(baseDataItem.getKey());
    }

    /* loaded from: classes2.dex */
    public class WaterSpecialTypeListener {
        public long id;

        public WaterSpecialTypeListener(long j) {
            PhotoPageFragment.this = r1;
            this.id = j;
        }

        public void onSpecialManagerInitialized(BaseDataItem baseDataItem) {
            if (baseDataItem == null || baseDataItem.getKey() != this.id) {
                return;
            }
            PhotoPageFragment.this.updateWaterMarkEnters(baseDataItem);
        }
    }

    public void updateWaterMarkEnters(BaseDataItem baseDataItem) {
        baseDataItem.setAIModeTypeFlags(17592186044416L);
        baseDataItem.setSpecialTypeEditable(true);
        this.mSpecialTypeManager.updateItemSpecialTypeEnter(baseDataItem);
    }

    public final boolean needDismissKeyGuard() {
        return this.mDataProvider.getFieldData().isStartWhenLocked && !this.mDataProvider.getFieldData().isStartWhenLockedAndSecret && !this.mHasSendDismissCast;
    }

    public final void dismissKeyGuard() {
        this.mHasSendDismissCast = true;
        this.mActivity.sendBroadcast(new Intent("xiaomi.intent.action.SHOW_SECURE_KEYGUARD"));
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public boolean prohibitOperateProcessingItem(BaseDataItem baseDataItem) {
        if (this.mDataProvider.isProcessingMedia(baseDataItem)) {
            ToastUtils.makeText(this.mActivity, (int) R.string.operate_processing_file_error);
            return true;
        }
        return false;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public void checkDismissKeyGuard(boolean z) {
        if (!needDismissKeyGuard() || !z) {
            return;
        }
        dismissKeyGuard();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public void configForLargeScreenDevice(Configuration configuration) {
        if (configuration.orientation != 1 || getView() == null) {
            return;
        }
        configPager(getView(), getResources().getDimensionPixelSize(R.dimen.photo_choice_title_height));
    }

    @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment, com.miui.gallery.ui.AbstractViewPagerFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mIsConfigurationChanged = true;
        hideNarBarForFullScreenGesture();
        if (VideoPlayerCompat.isVideoPlayerSupportShortLandscape()) {
            updateLayoutForConfigurationChanged(configuration);
        }
        CameraAnimManager cameraAnimManager = this.mCameraAnimManager;
        if (cameraAnimManager != null) {
            cameraAnimManager.dealWithConfigurationChanged(configuration);
        }
        VideoPreviewManager videoPreviewManager = this.mVideoPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.onOrientationChanged(configuration);
        }
        SpecialTypeManager specialTypeManager = this.mSpecialTypeManager;
        if (specialTypeManager != null) {
            specialTypeManager.onConfigurationChanged();
        }
        PhotoPageLifeCircleHooker photoPageLifeCircleHooker = this.mPhotoPageLifeCircleHooker;
        if (photoPageLifeCircleHooker != null) {
            photoPageLifeCircleHooker.onConfigurationChanged(configuration);
        }
        MotionPhotoManager motionPhotoManager = this.mMotionPhotoManager;
        if (motionPhotoManager != null) {
            motionPhotoManager.onConfigurationChanged();
        }
    }

    public final void updateLayoutForConfigurationChanged(Configuration configuration) {
        refreshTopBarInfo(this.mAdapter.getDataItem(this.mPager.getCurrentItem()));
        if (!isActionBarShowing()) {
            DefaultLogger.d("PhotoPageFragment", "ActionBar is not showing.");
            return;
        }
        hideMoreActions(true);
        hideBars(true);
        setSystemBarVisibility(false);
        PhotoPagerHelper photoPagerHelper = this.mPagerHelper;
        if (photoPagerHelper != null) {
            photoPagerHelper.onActionBarVisibleChanged(false, getActionBarHeight());
        }
        if (this.mConfigurationChangeRunnable == null) {
            this.mConfigurationChangeRunnable = new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFragment$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageFragment.$r8$lambda$bmHS8brnKtRoWHuppTgxrulbc6o(PhotoPageFragment.this);
                }
            };
        }
        ThreadManager.getMainHandler().removeCallbacks(this.mConfigurationChangeRunnable);
        ThreadManager.getMainHandler().postDelayed(this.mConfigurationChangeRunnable, 750L);
    }

    public /* synthetic */ void lambda$updateLayoutForConfigurationChanged$8() {
        setActionBarVisible(true, true);
        restoreMoreActions(true);
    }

    @Override // com.miui.gallery.ui.photoPage.IPhotoPageManager.IPhotoPageManagerCallback
    public void onAccelerometerRotationChange() {
        refreshTopBarAllElements();
    }

    public final void refreshTopBarLockEnter() {
        PhotoPageOrientationManager.IPhotoPageOrientationManagerController iPhotoPageOrientationManagerController = this.mOrientationController;
        if (iPhotoPageOrientationManagerController == null) {
            return;
        }
        refreshTopBarLockEnter(!this.mOrientationController.isScreenOrientationLocked() && !this.mDataProvider.getFieldData().isFromCamera && !this.mDataProvider.getFieldData().mCurrent.isInMultiWindowMode() && VideoPlayerCompat.isVideoPlayerSupportLockOrientation(), iPhotoPageOrientationManagerController.isOrientationLocked());
    }

    @Override // com.miui.gallery.ui.photoPage.IPhotoPageManager.IPhotoPageManagerCallback
    public void onOrientationChanged(int i, int i2) {
        refreshTopBarLocation(i, i2);
        SpecialTypeManager specialTypeManager = this.mSpecialTypeManager;
        if (specialTypeManager != null) {
            specialTypeManager.onOrientationChanged(i, i2);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public void onDownloadComplete(BaseDataItem baseDataItem) {
        SpecialTypeManager specialTypeManager = this.mSpecialTypeManager;
        if (specialTypeManager != null) {
            specialTypeManager.updateItem(baseDataItem);
        }
        VideoPreviewManager videoPreviewManager = this.mVideoPreviewManager;
        if (videoPreviewManager != null) {
            videoPreviewManager.updateItem(baseDataItem);
            this.mVideoPreviewManager.settleItem();
        }
    }

    public void clearTrimMemory() {
        PhotoPagerHelper photoPagerHelper = this.mPagerHelper;
        if (photoPagerHelper != null) {
            photoPagerHelper.stopTrimMemory();
            this.mPagerHelper.clearTrimMemoryFlag();
        }
    }

    public void onActivityTransition() {
        PhotoPagerHelper photoPagerHelper = this.mPagerHelper;
        if (photoPagerHelper != null) {
            photoPagerHelper.onActivityTransition();
        }
        onActivityTransitionForSpecialType();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager.IActionBarOwner
    public void onActivityTransitionForSpecialType() {
        SpecialTypeManager specialTypeManager = this.mSpecialTypeManager;
        if (specialTypeManager != null) {
            specialTypeManager.onActivityTransition();
        }
    }

    public void onActivityResultForSpecialType() {
        SpecialTypeManager specialTypeManager = this.mSpecialTypeManager;
        if (specialTypeManager != null) {
            specialTypeManager.onActivityResult();
        }
    }

    public boolean isStartingHomePage() {
        return this.mDataProvider.getFieldData().isStartingHomePage;
    }

    /* loaded from: classes2.dex */
    public class ScreenBroadcastReceiver extends BroadcastReceiver {
        public ScreenBroadcastReceiver() {
            PhotoPageFragment.this = r1;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                DefaultLogger.d("PhotoPageFragment", "receiver action_screen_off finish.");
                PhotoPageFragment.this.finishActivity(-1);
            }
        }
    }

    /* loaded from: classes2.dex */
    public class PhotoRefreshReceiver extends BroadcastReceiver {
        public PhotoRefreshReceiver() {
            PhotoPageFragment.this = r1;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals("com.miui.gallery.SAVE_TO_CLOUD")) {
                DefaultLogger.d("PhotoPageFragment", "receiver action_save_to_cloud");
                onActionSaveToCloud(intent);
            }
        }

        public final void onActionSaveToCloud(Intent intent) {
            PhotoPageItem photoPageItem;
            BaseDataItem dataItem;
            BaseDataItem dataItem2;
            String stringExtra = intent.getStringExtra("extra_file_path");
            if (TextUtils.isEmpty(stringExtra) || PhotoPageFragment.this.mPager == null) {
                return;
            }
            int i = 0;
            while (true) {
                if (i >= PhotoPageFragment.this.mPagerHelper.getActiveItemCount()) {
                    photoPageItem = null;
                    break;
                }
                photoPageItem = PhotoPageFragment.this.mPagerHelper.getItemByNativeIndex(i);
                if (photoPageItem != null && (dataItem2 = photoPageItem.getDataItem()) != null && stringExtra.equals(dataItem2.getOriginalPath())) {
                    break;
                }
                i++;
            }
            if (photoPageItem == null) {
                return;
            }
            if (photoPageItem == PhotoPageFragment.this.mPagerHelper.getCurrentItem() && (dataItem = photoPageItem.getDataItem()) != null) {
                dataItem.resetSpecialTypeFlags();
                if (PhotoPageFragment.this.mSpecialTypeManager != null) {
                    DefaultLogger.d("PhotoPageFragment", "onActionSaveToCloud update SpecialType");
                    PhotoPageFragment.this.mSpecialTypeManager.updateItem(dataItem, true);
                }
            }
            if (intent.hasExtra("extra_is_temp_file")) {
                return;
            }
            DefaultLogger.d("PhotoPageFragment", "onActionSaveToCloud refresh targetItem");
            photoPageItem.refreshItem();
        }
    }

    /* loaded from: classes2.dex */
    public class DocPhotoManager extends TransitionEditorManager implements SpecialTypeManager.Callback {
        public boolean mSupportDocPhoto;

        @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
        public void onRecognized(BaseDataItem baseDataItem, boolean z) {
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public DocPhotoManager(IDataProvider iDataProvider, PhotoPageFragment photoPageFragment, SpecialTypeManager specialTypeManager) {
            super(iDataProvider, photoPageFragment);
            PhotoPageFragment.this = r1;
            boolean isDeviceSupportDocPhoto = ExtraPhotoSDK.isDeviceSupportDocPhoto(GalleryApp.sGetAndroidContext());
            this.mSupportDocPhoto = isDeviceSupportDocPhoto;
            if (isDeviceSupportDocPhoto) {
                specialTypeManager.addTypeCallback(65536L, this, 1);
            }
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.TransitionEditorManager
        public boolean handleEditorResult(Intent intent) {
            Uri data;
            if (intent == null || (data = intent.getData()) == null) {
                return false;
            }
            String path = data.getPath();
            if (TextUtils.isEmpty(path)) {
                return false;
            }
            setTargetPath(path);
            insertAndNotifyDataSet(path);
            return true;
        }

        @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
        public void onEnterClick(final BaseDataItem baseDataItem) {
            if (!this.mSupportDocPhoto || baseDataItem == null || TextUtils.isEmpty(baseDataItem.getOriginalPath())) {
                return;
            }
            PhotoPageFragment.this.doAfterHideAnimByClickSpecialEnter(new BasePhotoPageBarsDelegateFragment.SimpleCallback() { // from class: com.miui.gallery.ui.PhotoPageFragment$DocPhotoManager$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.SimpleCallback
                public final void duringAction() {
                    PhotoPageFragment.DocPhotoManager.this.lambda$onEnterClick$0(baseDataItem);
                }
            });
        }

        public /* synthetic */ void lambda$onEnterClick$0(BaseDataItem baseDataItem) {
            IntentUtil.startDocPhotoAction(baseDataItem, this.mActivity, PhotoPageFragment.this);
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.TransitionEditorManager, com.miui.gallery.ui.photoPage.bars.manager.edit.BaseEditorManager
        public void release() {
            super.release();
        }
    }

    /* loaded from: classes2.dex */
    public class WatermarkManager extends TransitionEditorManager implements SpecialTypeManager.Callback {
        public boolean mSupportWatermark;

        public static /* synthetic */ Integer $r8$lambda$eOGPZaZPER6CrPgostzilocR06M(ContentValues contentValues, String str, ThreadPool.JobContext jobContext) {
            return lambda$handleEditorResult$1(contentValues, str, jobContext);
        }

        @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
        public void onRecognized(BaseDataItem baseDataItem, boolean z) {
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public WatermarkManager(IDataProvider iDataProvider, PhotoPageFragment photoPageFragment, SpecialTypeManager specialTypeManager) {
            super(iDataProvider, photoPageFragment);
            PhotoPageFragment.this = r1;
            boolean isDeviceSupportWatermark = ExtraPhotoSDK.isDeviceSupportWatermark(GalleryApp.sGetAndroidContext());
            this.mSupportWatermark = isDeviceSupportWatermark;
            if (isDeviceSupportWatermark) {
                specialTypeManager.addTypeCallback(17592186044416L, this, 1);
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
        public void onEnterClick(final BaseDataItem baseDataItem) {
            final View findViewById;
            final ImageView photoView;
            if (!this.mSupportWatermark || baseDataItem == null) {
                return;
            }
            PhotoPageItem currentItem = PhotoPageFragment.this.mPagerHelper.getCurrentItem();
            if (currentItem instanceof PhotoPageImageItem) {
                ((PhotoPageImageItem) currentItem).onSpecialTypeEnterClick();
            }
            View view = PhotoPageFragment.this.getView();
            if (view == null || (findViewById = view.findViewById(R.id.photo_pager)) == null || (photoView = PhotoPageFragment.this.getPhotoView()) == null) {
                return;
            }
            TrackController.trackClick("403.11.8.1.16477", AutoTracking.getRef());
            PhotoPageFragment.this.doAfterHideAnimByClickSpecialEnter(new BasePhotoPageBarsDelegateFragment.SimpleCallback() { // from class: com.miui.gallery.ui.PhotoPageFragment$WatermarkManager$$ExternalSyntheticLambda1
                @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.SimpleCallback
                public final void duringAction() {
                    PhotoPageFragment.WatermarkManager.this.lambda$onEnterClick$0(baseDataItem, findViewById, photoView);
                }
            });
        }

        public /* synthetic */ void lambda$onEnterClick$0(BaseDataItem baseDataItem, View view, ImageView imageView) {
            IntentUtil.startWatermarkAction(baseDataItem, this.mActivity, PhotoPageFragment.this, view, imageView);
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.TransitionEditorManager
        public boolean handleEditorResult(Intent intent) {
            Uri data;
            final String format;
            if (intent == null || (data = intent.getData()) == null) {
                return false;
            }
            String path = data.getPath();
            if (TextUtils.isEmpty(path)) {
                return false;
            }
            long longExtra = intent.getLongExtra("photo_secret_id", 0L);
            if (longExtra > 0) {
                setTargetId(longExtra);
                notifyDataSetChange(longExtra);
                this.mDataProvider.getFieldData().mArguments.putBoolean("photo_secret_watermark", true);
                format = String.format(Locale.US, "%s = %d", j.c, Long.valueOf(longExtra));
            } else {
                setTargetPath(path);
                insertAndNotifyDataSet(path);
                format = String.format(Locale.US, "%s = '%s'", "localFile", path);
            }
            final ContentValues contentValues = new ContentValues();
            contentValues.put("specialTypeFlags", (Long) Long.MIN_VALUE);
            ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.ui.PhotoPageFragment$WatermarkManager$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public final Object mo1807run(ThreadPool.JobContext jobContext) {
                    return PhotoPageFragment.WatermarkManager.$r8$lambda$eOGPZaZPER6CrPgostzilocR06M(contentValues, format, jobContext);
                }
            });
            return true;
        }

        public static /* synthetic */ Integer lambda$handleEditorResult$1(ContentValues contentValues, String str, ThreadPool.JobContext jobContext) {
            if (jobContext.isCancelled()) {
                return null;
            }
            return Integer.valueOf(SafeDBUtil.safeUpdate(StaticContext.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, contentValues, str, (String[]) null));
        }
    }

    /* loaded from: classes2.dex */
    public class MotionPhotoManager extends TransitionEditorManager implements SpecialTypeManager.Callback, View.OnClickListener {
        public boolean mIsMotionPhoto;
        public final boolean mSupported;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MotionPhotoManager(IDataProvider iDataProvider, PhotoPageFragment photoPageFragment, SpecialTypeManager specialTypeManager) {
            super(iDataProvider, photoPageFragment);
            PhotoPageFragment.this = r1;
            this.mIsMotionPhoto = false;
            boolean isDeviceSupportMotionPhoto = ExtraPhotoSDK.isDeviceSupportMotionPhoto(GalleryApp.sGetAndroidContext());
            this.mSupported = isDeviceSupportMotionPhoto;
            if (isDeviceSupportMotionPhoto) {
                specialTypeManager.addTypeCallback(32L, this, 1);
            }
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            PhotoPagerHelper photoPagerHelper;
            if (!this.mSupported || (photoPagerHelper = PhotoPageFragment.this.mPagerHelper) == null) {
                return;
            }
            PhotoPageItem currentItem = photoPagerHelper.getCurrentItem();
            if (!(currentItem instanceof PhotoPageImageItem)) {
                return;
            }
            ((PhotoPageImageItem) currentItem).onActionBarOperationClick();
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.TransitionEditorManager
        public boolean handleEditorResult(Intent intent) {
            Uri data;
            if (intent == null || (data = intent.getData()) == null) {
                return false;
            }
            String path = data.getPath();
            if (TextUtils.isEmpty(path)) {
                return false;
            }
            setTargetPath(path);
            insertAndNotifyDataSet(path);
            return true;
        }

        @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
        public void onRecognized(BaseDataItem baseDataItem, boolean z) {
            if (!this.mSupported) {
                return;
            }
            this.mIsMotionPhoto = z;
            PhotoPageFragment.this.refreshTopBarMotionPhotoEnter(z, this);
        }

        @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
        public void onEnterClick(final BaseDataItem baseDataItem) {
            if (!this.mSupported || baseDataItem == null || TextUtils.isEmpty(baseDataItem.getOriginalPath())) {
                return;
            }
            PhotoPageItem currentItem = PhotoPageFragment.this.mPagerHelper.getCurrentItem();
            if (currentItem instanceof PhotoPageImageItem) {
                ((PhotoPageImageItem) currentItem).onSpecialTypeEnterClick();
            }
            PhotoPageFragment.this.doAfterHideAnimByClickSpecialEnter(new BasePhotoPageBarsDelegateFragment.SimpleCallback() { // from class: com.miui.gallery.ui.PhotoPageFragment$MotionPhotoManager$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.SimpleCallback
                public final void duringAction() {
                    PhotoPageFragment.MotionPhotoManager.this.lambda$onEnterClick$0(baseDataItem);
                }
            });
        }

        public /* synthetic */ void lambda$onEnterClick$0(BaseDataItem baseDataItem) {
            IntentUtil.startMotionPhotoAction(baseDataItem, this.mActivity, PhotoPageFragment.this);
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.TransitionEditorManager
        public void onCanceled() {
            super.onCanceled();
            SamplingStatHelper.recordCountEvent("motion_photo", "motion_photo_cancel");
        }

        public void onDestroy() {
            release();
        }

        public void onConfigurationChanged() {
            if (!this.mSupported) {
                return;
            }
            PhotoPageFragment.this.refreshTopBarMotionPhotoEnter(this.mIsMotionPhoto, this);
        }
    }

    /* loaded from: classes2.dex */
    public class RefocusManager extends TransitionEditorManager implements SpecialTypeManager.Callback {
        public boolean mRefocusSupport;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public RefocusManager(IDataProvider iDataProvider, PhotoPageFragment photoPageFragment, SpecialTypeManager specialTypeManager) {
            super(iDataProvider, photoPageFragment);
            PhotoPageFragment.this = r1;
            boolean isDeviceSupportRefocus = ExtraPhotoSDK.isDeviceSupportRefocus(GalleryApp.sGetAndroidContext());
            this.mRefocusSupport = isDeviceSupportRefocus;
            if (isDeviceSupportRefocus) {
                specialTypeManager.addTypeCallback(1L, this, 1);
            }
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.TransitionEditorManager
        public boolean handleEditorResult(Intent intent) {
            Uri data;
            if (intent == null || (data = intent.getData()) == null) {
                return false;
            }
            String path = data.getPath();
            if (TextUtils.isEmpty(path)) {
                return false;
            }
            ExtraPhotoSDK.sendResultStatic(intent);
            setTargetPath(path);
            insertAndNotifyDataSet(path);
            return true;
        }

        @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
        public void onRecognized(BaseDataItem baseDataItem, boolean z) {
            if (!this.mRefocusSupport || !z) {
                return;
            }
            ExtraPhotoSDK.sendExposureStatic();
        }

        @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
        public void onEnterClick(final BaseDataItem baseDataItem) {
            if (!this.mRefocusSupport || baseDataItem == null || TextUtils.isEmpty(baseDataItem.getOriginalPath())) {
                return;
            }
            PhotoPageFragment.this.doAfterHideAnimByClickSpecialEnter(new BasePhotoPageBarsDelegateFragment.SimpleCallback() { // from class: com.miui.gallery.ui.PhotoPageFragment$RefocusManager$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.SimpleCallback
                public final void duringAction() {
                    PhotoPageFragment.RefocusManager.this.lambda$onEnterClick$0(baseDataItem);
                }
            });
        }

        public /* synthetic */ void lambda$onEnterClick$0(BaseDataItem baseDataItem) {
            IntentUtil.startAdvancedRefocusAction(baseDataItem, this.mActivity, PhotoPageFragment.this);
            ExtraPhotoSDK.sendRefocusEnterStatic();
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.TransitionEditorManager, com.miui.gallery.ui.photoPage.bars.manager.edit.BaseEditorManager
        public void release() {
            super.release();
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public boolean isCurrentImageOverDisplayArea() {
        RectF curItemDisplayRect;
        PhotoPagerHelper photoPagerHelper = this.mPagerHelper;
        if (photoPagerHelper == null || (curItemDisplayRect = photoPagerHelper.getCurItemDisplayRect()) == null) {
            return false;
        }
        return isImageOverDisplayArea(curItemDisplayRect.height());
    }

    public boolean isImageOverDisplayArea(float f) {
        ViewPager viewPager;
        if (f <= 0.0f || (viewPager = this.mPager) == null) {
            return false;
        }
        float height = viewPager.getHeight() - (Math.max(getSplitBarHeight(), getActionBarHeight()) * 2);
        return height > 0.0f && f > height;
    }

    /* loaded from: classes2.dex */
    public class BurstPhotoManager implements SpecialTypeManager.Callback {
        public String mTargetFilePath;
        public boolean mResultHandled = false;
        public Runnable mOnLoadTimeOut = new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFragment.BurstPhotoManager.2
            {
                BurstPhotoManager.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                BurstPhotoManager.this.finishTransition();
            }
        };

        @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
        public void onRecognized(BaseDataItem baseDataItem, boolean z) {
        }

        public BurstPhotoManager(SpecialTypeManager specialTypeManager) {
            PhotoPageFragment.this = r3;
            specialTypeManager.addTypeCallback(64L, this, 4);
            specialTypeManager.addTypeCallback(8388608L, this, 4);
        }

        @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
        public void onEnterClick(final BaseDataItem baseDataItem) {
            if (baseDataItem == null) {
                return;
            }
            PhotoPageFragment.this.doAfterHideAnimByClickSpecialEnter(new BasePhotoPageBarsDelegateFragment.SimpleCallback() { // from class: com.miui.gallery.ui.PhotoPageFragment$BurstPhotoManager$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.SimpleCallback
                public final void duringAction() {
                    PhotoPageFragment.BurstPhotoManager.this.lambda$onEnterClick$0(baseDataItem);
                }
            });
        }

        public /* synthetic */ void lambda$onEnterClick$0(BaseDataItem baseDataItem) {
            gotoBurstPhotoActivity(baseDataItem);
            if (baseDataItem.isTimeBurstItem()) {
                SamplingStatHelper.recordCountEvent("photo_extra", "time_burst_enter");
            }
        }

        public final void gotoBurstPhotoActivity(BaseDataItem baseDataItem) {
            if (PhotoPageFragment.this.mSpecialTypeManager != null) {
                PhotoPageFragment.this.mSpecialTypeManager.setEnterVisible(false);
            }
            if (PhotoPageFragment.this.isCurrentImageOverDisplayArea()) {
                PhotoPageFragment.this.setTopBarContentVisibility(false);
                PhotoPageFragment.this.hideBars(true);
            }
            if (PhotoPageFragment.this.getArguments().getBoolean("from_gallery", false)) {
                FragmentActivity activity = PhotoPageFragment.this.getActivity();
                PhotoPageFragment photoPageFragment = PhotoPageFragment.this;
                IntentUtil.gotoBurstPhotoActivity(activity, photoPageFragment, baseDataItem, photoPageFragment.getArguments().getLong("album_id", -1L), String.format("%s IN (%s)", j.c, TextUtils.join(",", baseDataItem.getBurstKeys())));
                return;
            }
            List<BaseDataItem> burstGroup = baseDataItem.getBurstGroup();
            ArrayList arrayList = new ArrayList();
            for (BaseDataItem baseDataItem2 : burstGroup) {
                if (!TextUtils.isEmpty(baseDataItem2.getOriginalPath())) {
                    arrayList.add(baseDataItem2.getOriginalPath());
                }
            }
            if (!BaseMiscUtil.isValid(arrayList)) {
                return;
            }
            FragmentActivity activity2 = PhotoPageFragment.this.getActivity();
            PhotoPageFragment photoPageFragment2 = PhotoPageFragment.this;
            IntentUtil.gotoBurstPhotoActivity(activity2, photoPageFragment2, baseDataItem, photoPageFragment2.getArguments().getLong("album_id", -1L), String.format("%s IN ('%s')", "alias_clear_thumbnail", TextUtils.join("','", arrayList)));
        }

        public final void notifyDataSetChange() {
            PhotoPageFragment.this.onContentChanged();
        }

        public void onActivityResult(final Intent intent) {
            ThreadManager.getMainHandler().postDelayed(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFragment.BurstPhotoManager.1
                {
                    BurstPhotoManager.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    PhotoPageFragment.this.setTopBarContentVisibility(true);
                    PhotoPageFragment.this.setActionBarVisible(true, true);
                    if (PhotoPageFragment.this.mSpecialTypeManager != null) {
                        SpecialTypeManager specialTypeManager = PhotoPageFragment.this.mSpecialTypeManager;
                        PhotoPageFragment photoPageFragment = PhotoPageFragment.this;
                        specialTypeManager.updateItem(photoPageFragment.mAdapter.getDataItem(photoPageFragment.mPager.getCurrentItem()));
                        PhotoPageFragment.this.mSpecialTypeManager.onActivityResult();
                    }
                    if (!BurstPhotoManager.this.mResultHandled) {
                        BurstPhotoManager.this.handleResult(intent);
                        BurstPhotoManager.this.mTargetFilePath = null;
                    }
                    BurstPhotoManager.this.mResultHandled = false;
                }
            }, 10L);
        }

        public void onActivityReenter(Intent intent) {
            this.mTargetFilePath = null;
            boolean handleResult = handleResult(intent);
            this.mResultHandled = handleResult;
            if (handleResult) {
                ActivityCompat.postponeEnterTransition(PhotoPageFragment.this.mActivity);
                ThreadManager.getMainHandler().postDelayed(this.mOnLoadTimeOut, 2000L);
                if (hasBurstCoverLoaded()) {
                    finishTransition();
                } else {
                    PhotoPageFragment.this.loadInBackground();
                }
            }
        }

        public final boolean handleResult(Intent intent) {
            Uri data;
            if (intent == null || (data = intent.getData()) == null) {
                return false;
            }
            if (intent.getBooleanExtra("time_burst_photo_save_result", false)) {
                this.mTargetFilePath = data.getPath();
                PhotoPageFragment.this.getArguments().putString("photo_focused_path", this.mTargetFilePath);
            } else {
                this.mTargetFilePath = data.getPath();
            }
            notifyDataSetChange();
            return true;
        }

        public void onImageLoadFinish(String str) {
            if (this.mTargetFilePath == null || str == null || !hasBurstCoverLoaded()) {
                return;
            }
            finishTransition();
        }

        public final void finishTransition() {
            ThreadManager.getMainHandler().removeCallbacks(this.mOnLoadTimeOut);
            BaseActivity baseActivity = PhotoPageFragment.this.mActivity;
            if (baseActivity != null) {
                ActivityCompat.startPostponedEnterTransition(baseActivity);
            }
            this.mTargetFilePath = null;
        }

        public void release() {
            ThreadManager.getMainHandler().removeCallbacks(this.mOnLoadTimeOut);
        }

        public final boolean hasBurstCoverLoaded() {
            String str;
            if (PhotoPageFragment.this.mPagerHelper.getCurrentItem().getPhotoView().getDrawable() != null && (str = this.mTargetFilePath) != null) {
                PhotoPageFragment photoPageFragment = PhotoPageFragment.this;
                if (str.equals(photoPageFragment.mAdapter.getDataItem(photoPageFragment.mPager.getCurrentItem()).getPathDisplayBetter())) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public void doAfterHideAnimByClickSpecialEnter(final BasePhotoPageBarsDelegateFragment.SimpleCallback simpleCallback) {
        hideBars(true);
        refreshNavBarColor(false);
        SpecialTypeManager specialTypeManager = this.mSpecialTypeManager;
        if (specialTypeManager != null) {
            specialTypeManager.setSpecialEnterState(false, true);
        }
        ThreadManager.getMainHandler().postDelayed(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFragment$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                PhotoPageFragment.lambda$doAfterHideAnimByClickSpecialEnter$9(BasePhotoPageBarsDelegateFragment.SimpleCallback.this);
            }
        }, 100L);
    }

    public static /* synthetic */ void lambda$doAfterHideAnimByClickSpecialEnter$9(BasePhotoPageBarsDelegateFragment.SimpleCallback simpleCallback) {
        if (simpleCallback != null) {
            simpleCallback.duringAction();
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public void onPlayVideo(BaseDataItem baseDataItem, String str) {
        super.onPlayVideo(baseDataItem, str);
        VideoPlayerManager videoPlayerManager = this.mVideoPlayerManager;
        if (videoPlayerManager != null) {
            videoPlayerManager.playVideo(baseDataItem, str, 0);
        }
    }

    /* loaded from: classes2.dex */
    public class VideoPlayerManager extends UpdatableEditorManager {
        public MiplayCirculateStatusListener mCirculateStatusListener;
        public ProgressDialogFragment mCirculatingDialog;
        public boolean mHasChangeWindowBackground;
        public boolean mMiplayCirculateHandled;
        public final GalleryMiPlayManager mMiplayManager;
        public MediaMetaData mMiplayMetaData;
        public PreloadedAppHelper.PackageInstallObserver mPlayerInstallObserver;
        public PlayerInstaller mPlayerInstaller;
        public PlayerBroadcastReceiver mPlayerReceiver;
        public boolean mPlayerResultHandled;
        public SpecialTypeManager.Callback mSlowMotionCallback;
        public SpecialTypeManager.Callback mSuperSlowMotionCallback;
        public int mVideoPlayMode;
        public Drawable mWindowBackgroundDrawable;

        @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.UpdatableEditorManager
        public boolean shouldInsertMediaStore() {
            return true;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public VideoPlayerManager(IDataProvider iDataProvider, PhotoPageFragment photoPageFragment, SpecialTypeManager specialTypeManager) {
            super(iDataProvider, photoPageFragment);
            PhotoPageFragment.this = r2;
            this.mMiplayManager = GalleryMiPlayManager.getInstance();
            this.mSlowMotionCallback = new SpecialTypeManager.Callback() { // from class: com.miui.gallery.ui.PhotoPageFragment.VideoPlayerManager.1
                @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
                public void onRecognized(BaseDataItem baseDataItem, boolean z) {
                }

                {
                    VideoPlayerManager.this = this;
                }

                @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
                public void onEnterClick(BaseDataItem baseDataItem) {
                    VideoPlayerManager.this.playVideo(baseDataItem, 1);
                }
            };
            this.mSuperSlowMotionCallback = new SpecialTypeManager.Callback() { // from class: com.miui.gallery.ui.PhotoPageFragment.VideoPlayerManager.2
                @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
                public void onRecognized(BaseDataItem baseDataItem, boolean z) {
                }

                {
                    VideoPlayerManager.this = this;
                }

                @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
                public void onEnterClick(BaseDataItem baseDataItem) {
                    VideoPlayerManager.this.playVideo(baseDataItem, 2);
                }
            };
            if (specialTypeManager == null || !VideoPlayerCompat.isSupportNewVideoPlayer()) {
                return;
            }
            specialTypeManager.addTypeCallback(4L, this.mSlowMotionCallback, 2);
            specialTypeManager.addTypeCallback(8L, this.mSlowMotionCallback, 2);
            if (VideoPlayerCompat.isVideoPlayerSupportEdit480FPS()) {
                specialTypeManager.addTypeCallback(4503599627370496L, this.mSuperSlowMotionCallback, 2);
                specialTypeManager.addTypeCallback(67108864L, this.mSuperSlowMotionCallback, 2);
            }
            specialTypeManager.addTypeCallback(9007199254740992L, this.mSuperSlowMotionCallback, 2);
            specialTypeManager.addTypeCallback(16L, this.mSuperSlowMotionCallback, 2);
            if (VideoPlayerCompat.isVideoPlayerSupportEdit1920FPS()) {
                specialTypeManager.addTypeCallback(134217728L, this.mSuperSlowMotionCallback, 2);
                specialTypeManager.addTypeCallback(18014398509481984L, this.mSuperSlowMotionCallback, 2);
            }
            if (!VideoPlayerCompat.isVideoPlayerSupportEdit3840FPS()) {
                return;
            }
            specialTypeManager.addTypeCallback(72057594037927936L, this.mSuperSlowMotionCallback, 2);
            specialTypeManager.addTypeCallback(36028797018963968L, this.mSuperSlowMotionCallback, 2);
        }

        public void onResume() {
            if (!this.mMiplayManager.hasInit()) {
                this.mMiplayManager.init();
            }
            this.mMiplayCirculateHandled = false;
            PhotoPageVideoItem currentItem = getCurrentItem();
            if (currentItem != null) {
                currentItem.onVideoPlayerReturn(true, null);
            }
        }

        public void playVideo(BaseDataItem baseDataItem, int i) {
            playVideo(baseDataItem, "", i);
        }

        public void playVideo(final BaseDataItem baseDataItem, String str, final int i) {
            IDataProvider iDataProvider = this.mDataProvider;
            if (iDataProvider != null && iDataProvider.getFieldData().mCurrent.isSlipped) {
                DefaultLogger.d("VideoPlayerManager", "pre playVideo enter share, return");
                return;
            }
            DefaultLogger.d("VideoPlayerManager", "playVideo %d", Integer.valueOf(i));
            if (!TextUtils.isEmpty(str)) {
                playVideo(baseDataItem, Uri.parse(str), baseDataItem.isSecret(), 0);
                SamplingStatHelper.recordCountEvent("online_video", "play_video");
                trackPlayVideo(baseDataItem, true);
            } else if (baseDataItem == null || TextUtils.isEmpty(baseDataItem.getOriginalPath())) {
            } else {
                boolean z = i == 0;
                if (!this.mMiplayCirculateHandled && this.mMiplayManager.checkMiplayCondition() && !baseDataItem.isSecret() && PhotoPageFragment.this.mVideoPlayerManager != null && z) {
                    if (this.mCirculateStatusListener == null) {
                        this.mCirculateStatusListener = new AnonymousClass3(baseDataItem, str);
                    }
                    this.mMiplayManager.registerMiplayCirculateStatusListener(this.mCirculateStatusListener);
                    this.mMiplayManager.play(baseDataItem, PhotoPageFragment.this.mVideoPreviewManager.mTotalDuration, PhotoPageFragment.this.mVideoPreviewManager.getSeekTime());
                } else if (ConnectControllerSingleton.getInstance() != null && ConnectControllerSingleton.getInstance().isConnected() && !baseDataItem.isSecret() && z) {
                    ProjectVideoFragment.showProjectVideoFragment(this.mActivity, baseDataItem);
                } else {
                    Cast.ProjectionManager projectionManager = PhotoPageFragment.this.mProjectManager;
                    if (projectionManager != null && projectionManager.isMiPlayConnected()) {
                        PhotoPageFragment.this.mProjectManager.stopMiPlay();
                    }
                    final Uri fromFile = Uri.fromFile(new File(baseDataItem.getOriginalPath()));
                    if (baseDataItem.isSecret() && baseDataItem.getSecretKey() != null) {
                        ProcessTask processTask = new ProcessTask(new ProcessTask.ProcessCallback() { // from class: com.miui.gallery.ui.PhotoPageFragment$VideoPlayerManager$$ExternalSyntheticLambda1
                            @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
                            public final Object doProcess(Object[] objArr) {
                                Uri lambda$playVideo$0;
                                lambda$playVideo$0 = PhotoPageFragment.VideoPlayerManager.lambda$playVideo$0(fromFile, baseDataItem, (Uri[]) objArr);
                                return lambda$playVideo$0;
                            }
                        }, new ProcessTask.OnCompleteListener() { // from class: com.miui.gallery.ui.PhotoPageFragment$VideoPlayerManager$$ExternalSyntheticLambda0
                            @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
                            public final void onCompleteProcess(Object obj) {
                                PhotoPageFragment.VideoPlayerManager.this.lambda$playVideo$1(baseDataItem, i, (Uri) obj);
                            }
                        });
                        processTask.setCancelable(true);
                        BaseActivity baseActivity = this.mActivity;
                        processTask.showProgress(baseActivity, baseActivity.getResources().getString(R.string.decrypting_video));
                        processTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, fromFile);
                        return;
                    }
                    playVideo(baseDataItem, fromFile, baseDataItem.isSecret(), i);
                }
            }
        }

        /* renamed from: com.miui.gallery.ui.PhotoPageFragment$VideoPlayerManager$3 */
        /* loaded from: classes2.dex */
        public class AnonymousClass3 implements MiplayCirculateStatusListener {
            public final /* synthetic */ BaseDataItem val$dataItem;
            public final /* synthetic */ String val$urlOnline;

            public AnonymousClass3(BaseDataItem baseDataItem, String str) {
                VideoPlayerManager.this = r1;
                this.val$dataItem = baseDataItem;
                this.val$urlOnline = str;
            }

            @Override // com.miui.gallery.miplay.MiplayCirculateStatusListener
            public void onCirculatePreparing(MediaMetaData mediaMetaData) {
                DefaultLogger.d("PhotoPageFragment_Miplay", "Miplay circulate prepare, show loding dialog");
                VideoPlayerManager.this.mMiplayCirculateHandled = true;
                VideoPlayerManager.this.mMiplayMetaData = mediaMetaData;
                VideoPlayerManager.this.showCirculatingDialog();
            }

            @Override // com.miui.gallery.miplay.MiplayCirculateStatusListener
            public void onCirculateStart() {
                DefaultLogger.d("PhotoPageFragment_Miplay", "Miplay circulate start");
                VideoPlayerManager.this.hideCirculatingDialog();
                IntentUtil.goToMipalyControlActivity(VideoPlayerManager.this.mActivity, VideoPlayerManager.this.mMiplayMetaData);
            }

            @Override // com.miui.gallery.miplay.MiplayCirculateStatusListener
            public void onCirculateFailed() {
                VideoPlayerManager.this.mMiplayCirculateHandled = true;
                DefaultLogger.d("PhotoPageFragment_Miplay", "Miplay circulate failed");
                ToastUtils.makeText(GalleryApp.sGetAndroidContext(), ResourceUtils.getString(R.string.miplay_circulate_failed));
                VideoPlayerManager.this.hideCirculatingDialog();
                CompatHandler mainHandler = ThreadManager.getMainHandler();
                final BaseDataItem baseDataItem = this.val$dataItem;
                final String str = this.val$urlOnline;
                mainHandler.post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFragment$VideoPlayerManager$3$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PhotoPageFragment.VideoPlayerManager.AnonymousClass3.this.lambda$onCirculateFailed$0(baseDataItem, str);
                    }
                });
            }

            public /* synthetic */ void lambda$onCirculateFailed$0(BaseDataItem baseDataItem, String str) {
                VideoPlayerManager.this.playVideo(baseDataItem, str, 0);
            }

            @Override // com.miui.gallery.miplay.MiplayCirculateStatusListener
            public void onMirrorResumed() {
                BaseDataItem dataItem;
                DefaultLogger.d("PhotoPageFragment_Miplay", "mirror resume, refresh tv");
                PhotoPagerHelper photoPagerHelper = PhotoPageFragment.this.mPagerHelper;
                if (photoPagerHelper == null || photoPagerHelper.getCurrentItem() == null) {
                    return;
                }
                PhotoPageFragment photoPageFragment = PhotoPageFragment.this;
                if (photoPageFragment.mProjectManager == null || (dataItem = photoPageFragment.mPagerHelper.getCurrentItem().getDataItem()) == null) {
                    return;
                }
                PhotoPageFragment.this.mProjectManager.onMirrorResume(dataItem.getPathDisplayBetter());
            }
        }

        public static /* synthetic */ Uri lambda$playVideo$0(Uri uri, BaseDataItem baseDataItem, Uri[] uriArr) {
            return SecretAlbumCryptoUtils.decryptVideo2CacheFolder(uri, baseDataItem.getSecretKey(), baseDataItem.getKey());
        }

        public /* synthetic */ void lambda$playVideo$1(BaseDataItem baseDataItem, int i, Uri uri) {
            if (uri != null) {
                playVideo(baseDataItem, uri, true, i);
            } else {
                ToastUtils.makeText(this.mActivity, (int) R.string.decrypt_video_failed);
            }
        }

        public final void showCirculatingDialog() {
            if (PhotoPageFragment.this.getActivity() == null) {
                return;
            }
            ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
            this.mCirculatingDialog = progressDialogFragment;
            progressDialogFragment.setIndeterminate(false);
            this.mCirculatingDialog.setMessage(ResourceUtils.getString(R.string.miplay_circulating_msg));
            this.mCirculatingDialog.setCancelable(false);
            this.mCirculatingDialog.showAllowingStateLoss(PhotoPageFragment.this.getActivity().getSupportFragmentManager(), "PhotoPageFragment_Miplay");
        }

        public final void hideCirculatingDialog() {
            ProgressDialogFragment progressDialogFragment = this.mCirculatingDialog;
            if (progressDialogFragment != null) {
                progressDialogFragment.dismissSafely();
            }
        }

        public final void playVideo(final BaseDataItem baseDataItem, final Uri uri, final boolean z, final int i) {
            if (!doPlayVideo(baseDataItem, uri, z, i)) {
                if (this.mPlayerInstallObserver == null) {
                    this.mPlayerInstallObserver = new PreloadedAppHelper.PackageInstallObserver() { // from class: com.miui.gallery.ui.PhotoPageFragment$VideoPlayerManager$$ExternalSyntheticLambda2
                        @Override // com.miui.preload.PreloadedAppHelper.PackageInstallObserver
                        public final void onPackageInstalled(String str, int i2, String str2, Bundle bundle) {
                            PhotoPageFragment.VideoPlayerManager.this.lambda$playVideo$2(baseDataItem, uri, z, i, str, i2, str2, bundle);
                        }
                    };
                    this.mPlayerInstaller = new PlayerInstaller().setInstallObserver(this.mPlayerInstallObserver);
                }
                if (this.mPlayerInstaller.install()) {
                    return;
                }
                IntentUtil.playVideoViaView(this.mActivity, uri, baseDataItem.getMimeType());
            }
        }

        public /* synthetic */ void lambda$playVideo$2(BaseDataItem baseDataItem, Uri uri, boolean z, int i, String str, int i2, String str2, Bundle bundle) {
            PhotoPageFragment.this.onMiuiVideoInstalled();
            if (!PhotoPageFragment.this.isResumed() || getCurrentItem() == null || !baseDataItem.equals(getCurrentItem().getDataItem())) {
                return;
            }
            doPlayVideo(baseDataItem, uri, z, i);
        }

        /* JADX WARN: Removed duplicated region for block: B:154:0x00cc  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final boolean doPlayVideo(com.miui.gallery.model.BaseDataItem r18, android.net.Uri r19, boolean r20, int r21) {
            /*
                Method dump skipped, instructions count: 565
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.PhotoPageFragment.VideoPlayerManager.doPlayVideo(com.miui.gallery.model.BaseDataItem, android.net.Uri, boolean, int):boolean");
        }

        public final void trackPlayVideo(BaseDataItem baseDataItem, boolean z) {
            HashMap hashMap = new HashMap();
            hashMap.put("tip", "403.11.4.1.15785");
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, z ? "online" : "local");
            if (baseDataItem != null && baseDataItem.getVideoResolution() != null && baseDataItem.getVideoResolution().length == 2) {
                hashMap.put("value", String.format(Locale.US, "%dx%d", Integer.valueOf(baseDataItem.getVideoResolution()[0]), Integer.valueOf(baseDataItem.getVideoResolution()[1])));
            }
            TrackController.trackClick(hashMap);
        }

        public final void prepareParams(BaseDataItem baseDataItem, Bundle bundle, int i) {
            Matrix videoZoomMatrix;
            if (bundle == null || this.mActivity == null) {
                return;
            }
            int currentItem = PhotoPageFragment.this.mPager.getCurrentItem();
            BaseDataItem dataItem = PhotoPageFragment.this.mAdapter.getDataItem(currentItem - 1);
            BaseDataItem dataItem2 = PhotoPageFragment.this.mAdapter.getDataItem(currentItem + 1);
            boolean z = false;
            Uri[] uriArr = {getItemUri(dataItem), getItemUri(dataItem2)};
            String[] strArr = {getItemMimeType(dataItem), getItemMimeType(dataItem2)};
            bundle.putString("title", baseDataItem.getViewTitle(this.mActivity));
            bundle.putString("subtitle", baseDataItem.getViewSubTitle(this.mActivity));
            bundle.putString("location", baseDataItem.getLocation());
            bundle.putParcelableArray("uri_list", uriArr);
            bundle.putStringArray("mime_type_list", strArr);
            bundle.putBoolean("action_bar_visible", PhotoPageFragment.this.isActionBarShowing());
            bundle.putInt("menu_height", PhotoPageFragment.this.getMenuCollapsedHeight());
            if (this.mDataProvider.getFieldData().isFromRecommendFacePage || this.mDataProvider.getFieldData().isPreviewMode) {
                z = true;
            }
            bundle.putBoolean("preview_mode", z);
            bundle.putFloat("com.miui.video.extra.volume", VideoFrameProvider.getVolume());
            bundle.putInt("com.miui.video.extra.from.place", this.mDataProvider.getFieldData().isFromCamera ? 1 : 0);
            bundle.putInt("miui_sdk_level", this.mDataProvider.getFieldData().isFromCamera ? MiuiSdkCompat.getMIUISdkLevel(this.mActivity) : ScreenUtils.checkCutoutLimit(this.mActivity));
            if (PhotoPageFragment.this.mOrientationController != null) {
                bundle.putInt("small_gallery_screen_orientation", PhotoPageFragment.this.mOrientationController.getEnterRequestedOrientation());
                bundle.putInt("big_gallery_screen_orientation", PhotoPageFragment.this.mOrientationController.getRequestedOrientation());
            }
            PhotoPageVideoItem currentItem2 = getCurrentItem();
            if (currentItem2 != null && (videoZoomMatrix = currentItem2.getVideoZoomMatrix()) != null) {
                float[] fArr = new float[9];
                videoZoomMatrix.getValues(fArr);
                bundle.putFloatArray("zoom_matrix", fArr);
            }
            BaseActivity baseActivity = this.mActivity;
            if (baseActivity instanceof BrightnessProvider) {
                BrightnessProvider brightnessProvider = (BrightnessProvider) baseActivity;
                bundle.putFloat("photo-brightness-manual", brightnessProvider.getManualBrightness());
                bundle.putFloat("photo-brightness-auto", brightnessProvider.getAutoBrightness());
            }
            if (PhotoPageFragment.this.mVideoPreviewManager != null && baseDataItem == PhotoPageFragment.this.mVideoPreviewManager.mCurrentItem) {
                bundle.putLong("seek_time", PhotoPageFragment.this.mVideoPreviewManager.getSeekTime());
            }
            ItemViewInfo itemViewInfo = PhotoPageFragment.this.getItemViewInfo(currentItem, baseDataItem.getKey());
            if (itemViewInfo != null) {
                bundle.putInt("item_x", itemViewInfo.getX());
                bundle.putInt("item_y", itemViewInfo.getY());
                bundle.putInt("item_width", itemViewInfo.getWidth());
                bundle.putInt("item_height", itemViewInfo.getHeight());
                bundle.putFloat("item_radius", itemViewInfo.getViewRadius());
            }
            bundle.putInt("mode", i);
        }

        public final Uri getItemUri(BaseDataItem baseDataItem) {
            if (baseDataItem == null) {
                return null;
            }
            String pathDisplayBetter = baseDataItem.getPathDisplayBetter();
            if (TextUtils.isEmpty(pathDisplayBetter)) {
                return Uri.parse("");
            }
            return Uri.fromFile(new File(pathDisplayBetter));
        }

        public final String getItemMimeType(BaseDataItem baseDataItem) {
            if (baseDataItem == null) {
                return null;
            }
            String pathDisplayBetter = baseDataItem.getPathDisplayBetter();
            if (TextUtils.isEmpty(pathDisplayBetter)) {
                return "*/*";
            }
            String mimeType = baseDataItem.getMimeType();
            return TextUtils.isEmpty(mimeType) ? BaseFileMimeUtil.getMimeType(pathDisplayBetter) : mimeType;
        }

        public final void registerPlayerReceiver() {
            DefaultLogger.d("VideoPlayerManager", "registerPlayerReceiver");
            if (this.mActivity == null || this.mPlayerReceiver != null) {
                return;
            }
            this.mPlayerReceiver = new PlayerBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.miui.gallery.action.VIDEO_PLAYER_STARTED");
            intentFilter.addAction("com.miui.gallery.action.VIDEO_PLAYER_RETURN");
            intentFilter.addAction("com.miui.gallery.action.VIDEO_PLAYER_FINISH");
            intentFilter.addAction("com.miui.gallery.action.GALLERY_SHOW_ITEM");
            this.mActivity.registerReceiver(this.mPlayerReceiver, intentFilter);
        }

        public final void unregisterPlayerReceiver() {
            PlayerBroadcastReceiver playerBroadcastReceiver;
            DefaultLogger.d("VideoPlayerManager", "unregisterPlayerReceiver");
            BaseActivity baseActivity = this.mActivity;
            if (baseActivity == null || (playerBroadcastReceiver = this.mPlayerReceiver) == null) {
                return;
            }
            baseActivity.unregisterReceiver(playerBroadcastReceiver);
            this.mPlayerReceiver = null;
        }

        /* loaded from: classes2.dex */
        public class PlayerBroadcastReceiver extends BroadcastReceiver {
            public PlayerBroadcastReceiver() {
                VideoPlayerManager.this = r1;
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent == null) {
                    return;
                }
                String action = intent.getAction();
                if ("com.miui.gallery.action.GALLERY_SHOW_ITEM".equals(action)) {
                    PhotoPageDataCache.getInstance().hidePlaceholderItem(PhotoPageFragment.this.getArguments().getString("extra_photo_page_from", null));
                } else if ("com.miui.gallery.action.VIDEO_PLAYER_STARTED".equals(action)) {
                    DefaultLogger.d("VideoPlayerManager", "onReceive VideoPlayer started");
                    VideoPlayerManager.this.setActivityVisible(false);
                    Cast.ProjectionManager projectionManager = PhotoPageFragment.this.mProjectManager;
                    if (projectionManager == null) {
                        return;
                    }
                    projectionManager.setVideoIsPlaying(true);
                } else if ("com.miui.gallery.action.VIDEO_PLAYER_RETURN".equals(action)) {
                    DefaultLogger.d("VideoPlayerManager", "onReceive VideoPlayer return");
                    if (!IntentUtil.isActionVideoEdit(VideoPlayerManager.this.mVideoPlayMode) && !ActivityCompat.isInFreeFormWindow(VideoPlayerManager.this.mActivity) && PhotoPageFragment.this.mOrientationController != null && !PhotoPageFragment.this.mOrientationController.isScreenOrientationLocked()) {
                        PhotoPageFragment.this.mOrientationController.setOrientationLocked(intent.getBooleanExtra("com.miui.video.extra.is.lock", false));
                        PhotoPageFragment.this.refreshTopBarLockEnter();
                    }
                    if (PhotoPageFragment.this.mOrientationController != null && PhotoPageFragment.this.mOrientationController.isScreenOrientationLocked()) {
                        int intExtra = intent.getIntExtra("com.miui.video.extra.screen_orientation", 1);
                        if (intExtra == PhotoPageFragment.this.mOrientationController.getRequestedOrientation()) {
                            VideoPlayerManager.this.setActivityVisible(true);
                        } else if (VideoPlayerManager.this.mVideoPlayMode == 3) {
                            PhotoPageFragment.this.mOrientationController.tryRestoreOrientation();
                        } else if (!IntentUtil.isActionVideoEdit(VideoPlayerManager.this.mVideoPlayMode)) {
                            PhotoPageFragment.this.mOrientationController.setRequestedOrientation(intExtra, "PlayerBroadcastReceiver");
                        }
                    }
                    VideoFrameProvider.setVolume(intent.getFloatExtra("com.miui.video.extra.volume", VideoFrameProvider.getVolume()));
                    boolean booleanExtra = intent.getBooleanExtra("action_bar_visible", true);
                    if (!VideoPlayerManager.this.mDataProvider.getFieldData().isFromRecommendFacePage && !VideoPlayerManager.this.mDataProvider.getFieldData().isPreviewMode) {
                        VideoPlayerManager.this.setActionBarVisibleImmediately(booleanExtra);
                    }
                    VideoPlayerManager.this.setActivityVisible(true);
                    Cast.ProjectionManager projectionManager2 = PhotoPageFragment.this.mProjectManager;
                    if (projectionManager2 != null) {
                        projectionManager2.setVideoIsPlaying(false);
                    }
                    PhotoPageVideoItem currentItem = VideoPlayerManager.this.getCurrentItem();
                    if (currentItem != null) {
                        currentItem.onVideoPlayerReturn(false, VideoPlayerManager.this.getExtraZoomMatrix(intent));
                    }
                    long longExtra = intent.getLongExtra("seek_time", -1L);
                    int intExtra2 = intent.getIntExtra("relative_index", 0);
                    if (intExtra2 == 0) {
                        if (longExtra >= 0 && PhotoPageFragment.this.mVideoPreviewManager != null) {
                            PhotoPageFragment.this.mVideoPreviewManager.seekTo(longExtra, true);
                            PhotoPageFragment.this.mVideoPreviewManager.refreshSeekBarIfNeed();
                        } else {
                            VideoPlayerManager.this.prepareFinish(context);
                        }
                    } else {
                        ViewPager viewPager = PhotoPageFragment.this.mPager;
                        if (viewPager != null) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + intExtra2, false);
                        }
                        VideoPlayerManager.this.prepareFinish(context);
                    }
                    VideoPlayerManager.this.mPlayerResultHandled = true;
                } else if (!"com.miui.gallery.action.VIDEO_PLAYER_FINISH".equals(action)) {
                } else {
                    DefaultLogger.d("VideoPlayerManager", "video_player_finish");
                    PhotoPageFragment.this.finish();
                }
            }
        }

        public final Matrix getExtraZoomMatrix(Intent intent) {
            float[] floatArrayExtra;
            if (intent == null || (floatArrayExtra = intent.getFloatArrayExtra("zoom_matrix")) == null || floatArrayExtra.length != 9) {
                return null;
            }
            Matrix matrix = new Matrix();
            matrix.setValues(floatArrayExtra);
            return matrix;
        }

        public final void setActionBarVisibleImmediately(boolean z) {
            if (PhotoPageFragment.this.mThemeController != null) {
                if (z) {
                    PhotoPageFragment.this.mThemeController.setLightTheme(false, false);
                } else {
                    PhotoPageFragment.this.mThemeController.setDarkTheme(false, false);
                }
            }
            PhotoPageFragment.this.setActionBarVisible(z, false);
        }

        public void prepareFinish(Context context) {
            if (context == null) {
                return;
            }
            Intent intent = new Intent("com.miui.video.ACTION_GALLERY_PREPARED");
            intent.setPackage(VideoPlayerCompat.getMiuiVideoPackageName());
            context.sendBroadcast(intent);
        }

        public final void setActivityVisible(boolean z) {
            Window window;
            DefaultLogger.d("VideoPlayerManager", "setActivityVisible %b", Boolean.valueOf(z));
            if (this.mActivity == null) {
                return;
            }
            int i = 0;
            if (((PhotoPageFragment.this.mCameraAnimManager != null && PhotoPageFragment.this.mCameraAnimManager.isLocked()) || (VideoPlayerCompat.isVideoPlayerSupportZoom() && VideoPlayerCompat.isVideoPlayerSupportImmersive())) && (window = this.mActivity.getWindow()) != null) {
                if (z) {
                    if (this.mHasChangeWindowBackground) {
                        window.setBackgroundDrawable(this.mWindowBackgroundDrawable);
                        this.mHasChangeWindowBackground = false;
                    }
                } else {
                    this.mWindowBackgroundDrawable = window.getDecorView().getBackground();
                    window.setBackgroundDrawable(new ColorDrawable(-16777216));
                    this.mHasChangeWindowBackground = true;
                }
            }
            View findViewById = this.mActivity.findViewById(16908290);
            if (findViewById == null) {
                return;
            }
            if (!z) {
                i = 4;
            }
            findViewById.setVisibility(i);
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.UpdatableEditorManager, com.miui.gallery.ui.photoPage.bars.manager.edit.BaseEditorManager
        public void onActivityResult(int i, int i2, Intent intent) {
            Uri data;
            super.onActivityResult(i, i2, intent);
            DefaultLogger.d("VideoPlayerManager", "onActivityResult");
            PhotoPageFragment.this.refreshTopBarAllElements();
            unregisterPlayerReceiver();
            if (intent == null) {
                if (i != 45) {
                    return;
                }
                DefaultLogger.w("VideoPlayerManager", "video player may be crashed, finish self.");
                PhotoPageFragment.this.finish();
                return;
            }
            VideoFrameProvider.setVolume(intent.getFloatExtra("com.miui.video.extra.volume", VideoFrameProvider.getVolume()));
            if (intent.getBooleanExtra("request_finish", false)) {
                if (VideoPlayerCompat.isVideoPlayerSupportRotateScreen() && PhotoPageFragment.this.mOrientationController != null && PhotoPageFragment.this.mOrientationController.isScreenOrientationLocked()) {
                    PhotoPageFragment.this.doExit(true);
                } else if (VideoPlayerCompat.isVideoPlayerSupportLockOrientation() && PhotoPageFragment.this.mOrientationController != null && !PhotoPageFragment.this.mOrientationController.isScreenOrientationLocked()) {
                    PhotoPageFragment.this.doExit(true);
                } else {
                    PhotoPageDataCache.getInstance().hidePlaceholderItem(PhotoPageFragment.this.getArguments().getString("extra_photo_page_from", null));
                    setActivityVisible(false);
                    if (PhotoPageFragment.this.mCameraAnimManager != null && PhotoPageFragment.this.mCameraAnimManager.doExit()) {
                        CompatHandler mainHandler = ThreadManager.getMainHandler();
                        final PhotoPageFragment photoPageFragment = PhotoPageFragment.this;
                        mainHandler.post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFragment$VideoPlayerManager$$ExternalSyntheticLambda3
                            @Override // java.lang.Runnable
                            public final void run() {
                                PhotoPageFragment.this.finish();
                            }
                        });
                    } else {
                        PhotoPageFragment.this.finish();
                    }
                }
            } else {
                setActivityVisible(true);
                if (!this.mPlayerResultHandled) {
                    PhotoPageVideoItem currentItem = getCurrentItem();
                    if (currentItem != null) {
                        currentItem.onVideoPlayerReturn(false, getExtraZoomMatrix(intent));
                    }
                    int intExtra = intent.getIntExtra("relative_index", 0);
                    if (intExtra != 0) {
                        ViewPager viewPager = PhotoPageFragment.this.mPager;
                        if (viewPager != null) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + intExtra, false);
                        }
                    } else {
                        long longExtra = intent.getLongExtra("seek_time", -1L);
                        if (PhotoPageFragment.this.mVideoPreviewManager != null) {
                            PhotoPageFragment.this.mVideoPreviewManager.seekTo(longExtra, false);
                        }
                    }
                }
            }
            this.mPlayerResultHandled = false;
            if (i2 != -1 || (data = intent.getData()) == null) {
                return;
            }
            String path = data.getPath();
            if (TextUtils.isEmpty(path)) {
                return;
            }
            PhotoPageFragment.this.setActionBarVisible(true, true);
            insertAndNotifyDataSet(path, false);
        }

        public PhotoPageVideoItem getCurrentItem() {
            PhotoPagerHelper photoPagerHelper = PhotoPageFragment.this.mPagerHelper;
            if (photoPagerHelper != null) {
                PhotoPageItem currentItem = photoPagerHelper.getCurrentItem();
                if (!(currentItem instanceof PhotoPageVideoItem)) {
                    return null;
                }
                return (PhotoPageVideoItem) currentItem;
            }
            return null;
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.UpdatableEditorManager
        public String getTargetPackageName() {
            return VideoPlayerCompat.getMiuiVideoPackageName();
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.UpdatableEditorManager
        public void onDestroy() {
            super.onDestroy();
            unregisterPlayerReceiver();
            PlayerInstaller playerInstaller = this.mPlayerInstaller;
            if (playerInstaller != null) {
                playerInstaller.setInstallObserver(null);
            }
            GalleryMiPlayManager galleryMiPlayManager = this.mMiplayManager;
            if (galleryMiPlayManager != null) {
                this.mCirculateStatusListener = null;
                galleryMiPlayManager.registerMiplayCirculateStatusListener(null);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class PlayerInstaller {
        public WeakReference<PreloadedAppHelper.PackageInstallObserver> mObserver;

        /* renamed from: $r8$lambda$-zwFaHgYZWPhfc9M-AFeIxt261M */
        public static /* synthetic */ void m1539$r8$lambda$zwFaHgYZWPhfc9MAFeIxt261M(PlayerInstaller playerInstaller, String str, int i, String str2, Bundle bundle) {
            playerInstaller.lambda$install$0(str, i, str2, bundle);
        }

        public static /* synthetic */ void $r8$lambda$IikOcqntjnHwy3Uhxkl4u70Zx6M(PlayerInstaller playerInstaller, String str, int i, String str2, Bundle bundle) {
            playerInstaller.lambda$install$1(str, i, str2, bundle);
        }

        public PlayerInstaller() {
        }

        public PlayerInstaller setInstallObserver(PreloadedAppHelper.PackageInstallObserver packageInstallObserver) {
            this.mObserver = new WeakReference<>(packageInstallObserver);
            return this;
        }

        public PreloadedAppHelper.PackageInstallObserver getInstallObserver() {
            WeakReference<PreloadedAppHelper.PackageInstallObserver> weakReference = this.mObserver;
            if (weakReference != null) {
                return weakReference.get();
            }
            return null;
        }

        public /* synthetic */ void lambda$install$1(final String str, final int i, final String str2, final Bundle bundle) {
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFragment$PlayerInstaller$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageFragment.PlayerInstaller.m1539$r8$lambda$zwFaHgYZWPhfc9MAFeIxt261M(PhotoPageFragment.PlayerInstaller.this, str, i, str2, bundle);
                }
            });
        }

        public boolean install() {
            return VideoPlayerCompat.tryInstallVideoPlayer(new PreloadedAppHelper.PackageInstallObserver() { // from class: com.miui.gallery.ui.PhotoPageFragment$PlayerInstaller$$ExternalSyntheticLambda0
                @Override // com.miui.preload.PreloadedAppHelper.PackageInstallObserver
                public final void onPackageInstalled(String str, int i, String str2, Bundle bundle) {
                    PhotoPageFragment.PlayerInstaller.$r8$lambda$IikOcqntjnHwy3Uhxkl4u70Zx6M(PhotoPageFragment.PlayerInstaller.this, str, i, str2, bundle);
                }
            });
        }

        public /* synthetic */ void lambda$install$0(String str, int i, String str2, Bundle bundle) {
            PreloadedAppHelper.PackageInstallObserver installObserver = getInstallObserver();
            if (installObserver != null) {
                installObserver.onPackageInstalled(str, i, str2, bundle);
            }
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IBarsOwner
    public boolean hideNarBarForFullScreenGesture() {
        if (!isLandscapeWindowMode() || !BaseScreenUtil.isFullScreenGestureNav(this.mActivity) || !VideoPlayerCompat.isVideoPlayerSupportHideNavigationBar() || BaseBuildUtil.isLargeScreen(this.mActivity)) {
            return false;
        }
        setSystemBarVisibility(false);
        return true;
    }

    public void setSystemBarVisibility(boolean z) {
        SystemUiUtil.setSystemBarsVisibility(z, this.mActivity.getWindow().getDecorView(), this.mDataProvider.getFieldData().mCurrent.isInMultiWindowMode());
    }

    /* loaded from: classes2.dex */
    public static class SpecialTypeManager implements PhotoPagerHelper.OnSpecialTypeEnterListener, View.OnClickListener {
        public LongSparseArray<Callback> mCallbacks;
        public PhotoPageFragment mFragment;
        public int mMediaType;
        public long mOperationMask;
        public RecognitionTask.Callback mRecognitionCallback = new RecognitionTask.Callback() { // from class: com.miui.gallery.ui.PhotoPageFragment$SpecialTypeManager$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.PhotoPageFragment.RecognitionTask.Callback
            public final void onDataItemRecognized(BaseDataItem baseDataItem) {
                PhotoPageFragment.SpecialTypeManager.this.lambda$new$0(baseDataItem);
            }
        };
        public RecognitionTask mRecognitionTask;
        public boolean mSupportEnter;

        /* loaded from: classes2.dex */
        public interface Callback {
            void onEnterClick(BaseDataItem baseDataItem);

            void onRecognized(BaseDataItem baseDataItem, boolean z);
        }

        public SpecialTypeManager(PhotoPageFragment photoPageFragment, long j) {
            this.mFragment = photoPageFragment;
            photoPageFragment.mPagerHelper.setOnSpecialTypeEnterListener(this);
            this.mSupportEnter = !photoPageFragment.mDataProvider.getFieldData().isStartWhenLockedAndSecret;
            this.mCallbacks = new LongSparseArray<>();
            photoPageFragment.getArguments();
            this.mOperationMask = j;
            if (SpecialTypeMediaUtils.isMTSpecialAITypeSupport()) {
                this.mMediaType |= 1;
            }
            photoPageFragment.mPagerHelper.setSpecialTypeEnterViewCache(SpecialTypeEnterView.buildCache(photoPageFragment.mActivity));
        }

        public void addTypeCallback(long j, Callback callback, int i) {
            if (callback == null) {
                return;
            }
            this.mMediaType = i | this.mMediaType;
            this.mCallbacks.put(j, callback);
        }

        public void onActivityTransition() {
            PhotoPageItem currentPageItem = getCurrentPageItem();
            if (currentPageItem != null) {
                currentPageItem.setSpecialTypeEnterViewVisible(false);
            }
        }

        public void setSpecialEnterState(boolean z, boolean z2) {
            PhotoPageItem currentPageItem = getCurrentPageItem();
            if (currentPageItem != null) {
                currentPageItem.setSpecialEnterState(z, z2);
            }
        }

        public void onActivityResult() {
            PhotoPageItem currentPageItem;
            if (this.mMediaType == 0 || (currentPageItem = getCurrentPageItem()) == null) {
                return;
            }
            PhotoPageFragment photoPageFragment = this.mFragment;
            currentPageItem.updateSpecialTypeEnterView(true, photoPageFragment != null && photoPageFragment.isActionBarShowing());
        }

        public void onConfigurationChanged() {
            if (this.mMediaType == 0) {
                return;
            }
            this.mFragment.refreshTopBarSpecialTypeEnter(getCurrentItem(), this);
        }

        public void onOrientationChanged(int i, int i2) {
            PhotoPageItem currentPageItem = getCurrentPageItem();
            if (currentPageItem != null) {
                currentPageItem.onOrientationChanged(i, i2);
            }
        }

        public void setEnterVisible(boolean z) {
            PhotoPageItem currentPageItem = getCurrentPageItem();
            if (currentPageItem != null) {
                currentPageItem.startSpecialTypeEnterAnim(z);
            }
        }

        public final boolean checkUpdateItem(BaseDataItem baseDataItem) {
            if (this.mMediaType == 0) {
                return false;
            }
            if (baseDataItem == null || ((!baseDataItem.isImage() && !baseDataItem.isVideo()) || baseDataItem.hasFace())) {
                onRecognized(null);
                return false;
            } else if (baseDataItem.isSpecialTypeRecognized()) {
                onRecognized(baseDataItem);
                return false;
            } else if (baseDataItem.isBurstItem() && (this.mMediaType & 4) == 0) {
                onRecognized(null);
                return false;
            } else if ((baseDataItem.isVideo() && (this.mMediaType & 2) == 0) || (baseDataItem.isImage() && (this.mMediaType & 1) == 0 && (baseDataItem.getOriginalPath() == null || !BaseFileMimeUtil.isHeifMimeTypeByPath(baseDataItem.getOriginalPath())))) {
                onRecognized(null);
                return false;
            } else if (TextUtils.isEmpty(baseDataItem.getOriginalPath())) {
                onRecognized(null);
                return false;
            } else if (!baseDataItem.isSecret()) {
                return true;
            } else {
                onRecognized(baseDataItem);
                return false;
            }
        }

        public void updateItem(BaseDataItem baseDataItem) {
            updateItem(baseDataItem, false);
        }

        public void updateItem(BaseDataItem baseDataItem, boolean z) {
            if (!z && !checkUpdateItem(baseDataItem)) {
                DefaultLogger.d("PhotoPageFragment", "skip recognize task since check failed.");
            } else {
                submitRecognizeTask(baseDataItem);
            }
        }

        public final void submitRecognizeTask(BaseDataItem baseDataItem) {
            if (this.mRecognitionTask == null) {
                this.mRecognitionTask = new RecognitionTask(this.mRecognitionCallback, this.mFragment);
            }
            this.mRecognitionTask.execute(baseDataItem);
        }

        public /* synthetic */ void lambda$new$0(BaseDataItem baseDataItem) {
            BaseDataItem currentItem = getCurrentItem();
            if (baseDataItem != null && baseDataItem.equals(currentItem)) {
                onRecognized(baseDataItem);
            }
            updateItemSpecialIndicator(baseDataItem);
        }

        public final void onRecognized(BaseDataItem baseDataItem) {
            if (this.mFragment == null) {
                return;
            }
            if (baseDataItem != null && isItemSupportEdit(baseDataItem)) {
                baseDataItem.setSpecialTypeEditable(true);
                updateItemSpecialTypeEnter(baseDataItem);
            }
            int size = this.mCallbacks.size();
            for (int i = 0; i < size; i++) {
                this.mCallbacks.valueAt(i).onRecognized(baseDataItem, baseDataItem != null && baseDataItem.isSpecialType(this.mCallbacks.keyAt(i)));
            }
            this.mFragment.refreshTopBarSpecialTypeEnter(baseDataItem, this);
        }

        public final void updateItemSpecialIndicator(BaseDataItem baseDataItem) {
            PhotoPageItem currentPageItem;
            if (this.mFragment == null || baseDataItem == null || (currentPageItem = getCurrentPageItem()) == null) {
                return;
            }
            BaseDataItem dataItem = currentPageItem.getDataItem();
            if (!baseDataItem.equals(dataItem)) {
                return;
            }
            dataItem.setSpecialTypeFlags(baseDataItem.getSpecialTypeFlags());
            currentPageItem.updateSpecialTypeIndicator();
        }

        public final void updateItemSpecialTypeEnter(BaseDataItem baseDataItem) {
            PhotoPageItem currentPageItem;
            if (this.mFragment == null || baseDataItem == null || (currentPageItem = getCurrentPageItem()) == null) {
                return;
            }
            BaseDataItem dataItem = currentPageItem.getDataItem();
            if (!baseDataItem.equals(dataItem)) {
                return;
            }
            dataItem.setSpecialTypeFlags(baseDataItem.getSpecialTypeFlags());
            dataItem.setAIModeTypeFlags(baseDataItem.getAIModeTypeFlags());
            dataItem.setMotionOffset(baseDataItem.getMotionOffset());
            dataItem.setVideoTags(baseDataItem.getVideoTags());
            dataItem.setSpecialTypeEditable(baseDataItem.isSpecialTypeEditable());
            PhotoPageFragment photoPageFragment = this.mFragment;
            currentPageItem.updateSpecialTypeEnterView(true, photoPageFragment != null && photoPageFragment.isActionBarShowing());
        }

        public final boolean isItemSupportEdit(BaseDataItem baseDataItem) {
            return this.mSupportEnter && baseDataItem != null && PhotoOperationsUtil.isSupportedOptions(baseDataItem.getSupportOperations() & this.mOperationMask, 512L) && getCallbacks(baseDataItem) != null;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            onEnterClick(getCurrentItem());
        }

        @Override // com.miui.gallery.ui.PhotoPagerHelper.OnSpecialTypeEnterListener
        public void onEntersClick(BaseDataItem baseDataItem, long j) {
            if (baseDataItem == null) {
                return;
            }
            if (this.mFragment.needDismissKeyGuard()) {
                this.mFragment.dismissKeyGuard();
            }
            HashMap<Long, Callback> callbacks = getCallbacks(baseDataItem);
            if (callbacks == null) {
                return;
            }
            Callback callback = callbacks.get(Long.valueOf(j));
            if (callback != null) {
                callback.onEnterClick(baseDataItem);
            }
            ExtraPhotoSDK.sendEnterStatic(baseDataItem.getSpecialTypeFlags());
        }

        public void onEnterClick(BaseDataItem baseDataItem) {
            if (baseDataItem == null) {
                return;
            }
            if (this.mFragment.needDismissKeyGuard()) {
                this.mFragment.dismissKeyGuard();
            }
            Callback callback = getCallback(baseDataItem);
            if (callback != null) {
                callback.onEnterClick(baseDataItem);
            }
            ExtraPhotoSDK.sendEnterStatic(baseDataItem.getSpecialTypeFlags());
        }

        public final Callback getCallback(BaseDataItem baseDataItem) {
            if (baseDataItem != null && baseDataItem.getSpecialTypeFlags() > 0) {
                int size = this.mCallbacks.size();
                for (int i = 0; i < size; i++) {
                    if (baseDataItem.isSpecialType(this.mCallbacks.keyAt(i))) {
                        return this.mCallbacks.valueAt(i);
                    }
                }
            }
            return null;
        }

        public final HashMap<Long, Callback> getCallbacks(BaseDataItem baseDataItem) {
            if (baseDataItem != null) {
                if (baseDataItem.getSpecialTypeFlags() <= 0 && baseDataItem.getAIModeTypeFlags() <= 0) {
                    return null;
                }
                HashMap<Long, Callback> hashMap = new HashMap<>();
                int size = this.mCallbacks.size();
                for (int i = 0; i < size; i++) {
                    long keyAt = this.mCallbacks.keyAt(i);
                    if (baseDataItem.isSpecialType(keyAt)) {
                        hashMap.put(Long.valueOf(keyAt), this.mCallbacks.valueAt(i));
                    } else if (baseDataItem.isAIModeType(keyAt)) {
                        hashMap.put(Long.valueOf(keyAt), this.mCallbacks.valueAt(i));
                    }
                }
                if (hashMap.size() <= 0) {
                    return null;
                }
                return hashMap;
            }
            return null;
        }

        public final BaseDataItem getCurrentItem() {
            ViewPager viewPager;
            PhotoPageFragment photoPageFragment = this.mFragment;
            if (photoPageFragment == null || (viewPager = photoPageFragment.mPager) == null || photoPageFragment.mAdapter == null) {
                return null;
            }
            return this.mFragment.mAdapter.getDataItem(viewPager.getCurrentItem());
        }

        public final PhotoPageItem getCurrentPageItem() {
            PhotoPagerHelper photoPagerHelper;
            PhotoPageFragment photoPageFragment = this.mFragment;
            if (photoPageFragment == null || (photoPagerHelper = photoPageFragment.mPagerHelper) == null) {
                return null;
            }
            return photoPagerHelper.getCurrentItem();
        }

        public void onDestroy() {
            this.mFragment = null;
            this.mCallbacks.clear();
            RecognitionTask recognitionTask = this.mRecognitionTask;
            if (recognitionTask != null) {
                recognitionTask.release();
                this.mRecognitionTask = null;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class RecognitionTask {
        public Callback mCallback;
        public WeakReference<PhotoPageFragment> mFragment;
        public Future mFuture;

        /* loaded from: classes2.dex */
        public interface Callback {
            void onDataItemRecognized(BaseDataItem baseDataItem);
        }

        public RecognitionTask(Callback callback, PhotoPageFragment photoPageFragment) {
            this.mCallback = callback;
            this.mFragment = new WeakReference<>(photoPageFragment);
        }

        public void execute(final BaseDataItem baseDataItem) {
            Future future = this.mFuture;
            if (future != null) {
                future.cancel();
            }
            this.mFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.ui.PhotoPageFragment.RecognitionTask.1
                {
                    RecognitionTask.this = this;
                }

                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public Void mo1807run(ThreadPool.JobContext jobContext) {
                    BaseDataItem baseDataItem2;
                    long parseFlagsForVideo;
                    if (!jobContext.isCancelled() && (baseDataItem2 = baseDataItem) != null) {
                        String originalPath = baseDataItem2.getOriginalPath();
                        if (TextUtils.isEmpty(originalPath)) {
                            return null;
                        }
                        DefaultLogger.d("PhotoPageFragment", "RecognitionTask run.");
                        long j = 0;
                        if (baseDataItem.isImage()) {
                            parseFlagsForVideo = SpecialTypeMediaUtils.parseFlagsForImage(originalPath);
                            if ((32 & parseFlagsForVideo) != 0) {
                                long readMicroVideoOffset = XmpHelper.readMicroVideoOffset(originalPath);
                                if (readMicroVideoOffset > 0) {
                                    baseDataItem.setMotionOffset(readMicroVideoOffset);
                                }
                            }
                            j = parseFlagsForVideo;
                        } else if (baseDataItem.isVideo()) {
                            parseFlagsForVideo = SpecialTypeMediaUtils.parseFlagsForVideo(originalPath);
                            if ((16 & parseFlagsForVideo) != 0 && !SpecialTypeMediaUtils.is960VideoEditable(originalPath)) {
                                parseFlagsForVideo = 0;
                            }
                            if ((67108864 & parseFlagsForVideo) != 0 && !SpecialTypeMediaUtils.is480VideoEditable(originalPath)) {
                                parseFlagsForVideo = 0;
                            }
                            if ((134217728 & parseFlagsForVideo) != 0 && !SpecialTypeMediaUtils.is1920VideoEditable(originalPath)) {
                                parseFlagsForVideo = 0;
                            }
                            if ((72057594037927936L & parseFlagsForVideo) != 0 && !SpecialTypeMediaUtils.is3840VideoEditable(originalPath)) {
                                parseFlagsForVideo = 0;
                            }
                            if ((32768 & parseFlagsForVideo) != 0) {
                                baseDataItem.setVideoTags(VideoTagsParser.parse(originalPath));
                            }
                            j = parseFlagsForVideo;
                        }
                        baseDataItem.setSpecialTypeFlags(j);
                        if (baseDataItem instanceof CloudItem) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("specialTypeFlags", Long.valueOf(j));
                            SafeDBUtil.safeUpdate(StaticContext.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, contentValues, String.format(Locale.US, "%s = %d AND %s <> %d", j.c, Long.valueOf(((CloudItem) baseDataItem).getId()), "specialTypeFlags", Long.valueOf(j)), (String[]) null);
                        }
                        return null;
                    }
                    return null;
                }
            }, new FutureHandler<Void>() { // from class: com.miui.gallery.ui.PhotoPageFragment.RecognitionTask.2
                {
                    RecognitionTask.this = this;
                }

                @Override // com.miui.gallery.concurrent.FutureHandler
                public void onPostExecute(Future<Void> future2) {
                    if (RecognitionTask.this.mCallback != null) {
                        RecognitionTask.this.mCallback.onDataItemRecognized(baseDataItem);
                    }
                }
            });
        }

        public void release() {
            this.mCallback = null;
            Future future = this.mFuture;
            if (future != null) {
                future.cancel();
                this.mFuture = null;
            }
        }
    }

    public void onMiuiVideoInstalled() {
        if (VideoPlayerCompat.isSupportNewVideoPlayer()) {
            if (this.mVideoPreviewManager == null) {
                this.mVideoPreviewManager = new VideoPreviewManager();
            }
            this.mVideoPreviewManager.onMiuiVideoInstalled();
        }
    }

    /* loaded from: classes2.dex */
    public class VideoPreviewManager implements VideoFrameProvider.Listener, VideoFrameSeekBar.OnSeekBarChangeListener, PhotoPageVideoItem.OnSurfacePreparedListener, PhotoPageVideoItem.OnThumbnailLoadedListener, VideoSubtitleProvider.Listener {
        public BaseDataItem mCurrentItem;
        public Runnable mDelayTask;
        public ViewGroup mDurationBar;
        public int mDurationBarMargin;
        public TextView mDurationTextView;
        public boolean mIsRtl;
        public long mLastRequestTime;
        public TextView mProgressTextView;
        public VideoFrameProvider mProvider;
        public VideoFrameSeekBar mSeekBar;
        public long mSeekTimeMarker;
        public boolean mShowDefaultThumb;
        public List<VideoSubtitleProvider.SubtitleItem> mSubtitleItems;
        public VideoSubtitleProvider mSubtitleProvider;
        public Typeface mSubtitleTypeface;
        public boolean mSupportTags;
        public int mThumbItemHeight;
        public int mThumbItemWidth;
        public VideoFrameProvider.ThumbListInfo mThumbListInfo;
        public long mTotalDuration;
        public PhotoPageVideoItem mVideoPageItem;
        public boolean mSeekNeedCallback = false;
        public int mDraggingIndex = 0;
        public float mDragStartTransitionY = 0.0f;
        public int mPagerScrollState = 0;
        public boolean mSeekBarRunningAnim = false;
        public boolean mIsLeftItemVideo = false;
        public boolean mIsRightItemVideo = false;
        public boolean mIsTagsUpdated = false;
        public long mPendingSeekTime = -1;
        public SpecialTypeManager.Callback mEditSubtitleCallback = new SpecialTypeManager.Callback() { // from class: com.miui.gallery.ui.PhotoPageFragment.VideoPreviewManager.2
            {
                VideoPreviewManager.this = this;
            }

            @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
            public void onRecognized(BaseDataItem baseDataItem, boolean z) {
                if (!z || baseDataItem == null) {
                    return;
                }
                VideoPreviewManager.this.mSubtitleProvider.request(baseDataItem.getOriginalPath());
            }

            @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
            public void onEnterClick(BaseDataItem baseDataItem) {
                if (PhotoPageFragment.this.mVideoPlayerManager != null) {
                    if (PhotoPageFragment.this.mOrientationController != null) {
                        PhotoPageFragment.this.mOrientationController.noteRestoreOrientation();
                    }
                    PhotoPageFragment.this.mVideoPlayerManager.playVideo(baseDataItem, 3);
                }
            }
        };
        public SpecialTypeManager.Callback mViewTagsCallback = new SpecialTypeManager.Callback() { // from class: com.miui.gallery.ui.PhotoPageFragment.VideoPreviewManager.3
            {
                VideoPreviewManager.this = this;
            }

            @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
            public void onRecognized(BaseDataItem baseDataItem, boolean z) {
                if (z) {
                    VideoPreviewManager.this.updateTagsView();
                }
            }

            @Override // com.miui.gallery.ui.PhotoPageFragment.SpecialTypeManager.Callback
            public void onEnterClick(BaseDataItem baseDataItem) {
                if (PhotoPageFragment.this.mVideoPlayerManager != null) {
                    PhotoPageFragment.this.mVideoPlayerManager.playVideo(baseDataItem, 4);
                }
            }
        };
        public Animator.AnimatorListener mSeekBarAnimListener = new Animator.AnimatorListener() { // from class: com.miui.gallery.ui.PhotoPageFragment.VideoPreviewManager.4
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            {
                VideoPreviewManager.this = this;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                VideoPreviewManager.this.mSeekBarRunningAnim = true;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                VideoPreviewManager.this.mSeekBarRunningAnim = false;
                if (VideoPreviewManager.this.mDelayTask != null) {
                    VideoPreviewManager.this.mDelayTask.run();
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                VideoPreviewManager.this.mSeekBarRunningAnim = false;
            }
        };
        public Runnable mDurationBarHideRunnable = new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFragment.VideoPreviewManager.5
            {
                VideoPreviewManager.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                VideoPreviewManager.this.mDurationBar.animate().alpha(0.0f).setDuration(0L).setInterpolator(new SineEaseInInterpolator()).start();
            }
        };

        public static /* synthetic */ void $r8$lambda$3GD9AQhxzOm265thT_wFOcfLMpQ(VideoPreviewManager videoPreviewManager) {
            videoPreviewManager.doUpdateTagsView();
        }

        public static /* synthetic */ void $r8$lambda$hRi7CxNottoowXuamHDBKUmBhxY(VideoPreviewManager videoPreviewManager) {
            videoPreviewManager.lambda$onOrientationChanged$0();
        }

        public void onStop() {
        }

        public VideoPreviewManager() {
            PhotoPageFragment.this = r7;
            this.mSupportTags = false;
            VideoFrameProvider videoFrameProvider = new VideoFrameProvider();
            this.mProvider = videoFrameProvider;
            videoFrameProvider.addListener(this);
            this.mSubtitleProvider = new VideoSubtitleProvider(this);
            this.mSeekBar = (VideoFrameSeekBar) r7.mViewProvider.getVideoSeekBarView(null);
            this.mThumbItemWidth = r7.getResources().getDimensionPixelSize(R.dimen.video_frame_thumb_width);
            this.mThumbItemHeight = r7.getResources().getDimensionPixelSize(R.dimen.video_frame_thumb_height);
            this.mDurationBarMargin = r7.getResources().getDimensionPixelSize(R.dimen.video_duration_bar_margin);
            this.mIsRtl = r7.getResources().getConfiguration().getLayoutDirection() == 1;
            r7.setFrameBar(this.mSeekBar);
            this.mSeekBar.setOnSeekBarChangeListener(this);
            this.mSeekBar.setVisibility(8);
            r7.isVideoSeekBarVisible = false;
            if (r7.mSpecialTypeManager != null) {
                if (VideoPlayerCompat.isVideoPlayerSupportEditSubtitle()) {
                    r7.mSpecialTypeManager.addTypeCallback(16384L, this.mEditSubtitleCallback, 2);
                }
                if (!VideoPlayerCompat.isVideoPlayerSupportViewTags()) {
                    return;
                }
                r7.mSpecialTypeManager.addTypeCallback(32768L, this.mViewTagsCallback, 2);
                this.mSupportTags = true;
            }
        }

        public void onMiuiVideoInstalled() {
            PhotoPageFragment.this.mPagerHelper.invokePageItems(new PhotoPagerHelper.ItemFunction<Void>() { // from class: com.miui.gallery.ui.PhotoPageFragment.VideoPreviewManager.1
                {
                    VideoPreviewManager.this = this;
                }

                @Override // com.miui.gallery.ui.PhotoPagerHelper.ItemFunction
                public Void run(PhotoPageItem photoPageItem) {
                    ((PhotoPageVideoItem) photoPageItem).onMiuiVideoInstalled();
                    return null;
                }

                @Override // com.miui.gallery.ui.PhotoPagerHelper.ItemFunction
                public boolean accept(PhotoPageItem photoPageItem) {
                    return photoPageItem instanceof PhotoPageVideoItem;
                }
            });
            this.mProvider.onMiuiVideoInstalled();
            PhotoPageFragment photoPageFragment = PhotoPageFragment.this;
            PhotoPageFragment.this.mVideoPreviewManager.updateItem(photoPageFragment.mAdapter.getDataItem(photoPageFragment.mPager.getCurrentItem()));
            PhotoPageFragment.this.mVideoPreviewManager.settleItem();
        }

        public void settleItem() {
            Trace.beginSection("dismiss");
            if (this.mCurrentItem == null || this.mVideoPageItem == null) {
                dismissSeekBar();
                dismissDurationBar();
                Trace.endSection();
                return;
            }
            Trace.endSection();
            Trace.beginSection("prepare");
            this.mVideoPageItem.getPreviewSurface();
            this.mVideoPageItem.setOnThumbnailLoadedListener(this);
            if (VideoPlayerCompat.isVideoPlayerSupportBanHdrFrame()) {
                this.mProvider.requestVideoInfo(this.mCurrentItem.getOriginalPath());
            } else {
                this.mProvider.requestThumbList(this.mCurrentItem.getOriginalPath(), this.mThumbItemWidth, this.mThumbItemHeight, false);
            }
            Trace.endSection();
            Trace.beginSection("update");
            updateLeftAndRightItemState();
            updateSubtitleView();
            Trace.endSection();
        }

        public void updateItem(BaseDataItem baseDataItem) {
            BaseDataItem baseDataItem2 = this.mCurrentItem;
            if (baseDataItem2 != null) {
                this.mProvider.releaseForVideo(baseDataItem2.getOriginalPath());
            }
            PhotoPageVideoItem photoPageVideoItem = this.mVideoPageItem;
            if (photoPageVideoItem != null) {
                photoPageVideoItem.setOnSurfacePreparedListener(null);
                this.mVideoPageItem.setOnThumbnailLoadedListener(null);
            }
            this.mThumbListInfo = null;
            this.mShowDefaultThumb = false;
            this.mSubtitleItems = null;
            this.mSubtitleTypeface = null;
            this.mIsTagsUpdated = false;
            this.mPendingSeekTime = -1L;
            PhotoPageItem currentItem = PhotoPageFragment.this.mPagerHelper.getCurrentItem();
            if (!isValidVideoItem(baseDataItem) || !(currentItem instanceof PhotoPageVideoItem)) {
                this.mCurrentItem = null;
                this.mVideoPageItem = null;
                return;
            }
            DefaultLogger.d("VideoPreviewManager", "updateItem");
            this.mCurrentItem = baseDataItem;
            PhotoPageVideoItem photoPageVideoItem2 = (PhotoPageVideoItem) currentItem;
            this.mVideoPageItem = photoPageVideoItem2;
            photoPageVideoItem2.setOnSurfacePreparedListener(this);
            this.mProvider.prepareForVideo(baseDataItem.getOriginalPath());
        }

        public void cancelFrameTask() {
            this.mProvider.cancelFrameTask();
        }

        public void onOrientationChanged(Configuration configuration) {
            dismissDurationBar();
            VideoFrameSeekBar videoFrameSeekBar = this.mSeekBar;
            if (videoFrameSeekBar == null) {
                return;
            }
            videoFrameSeekBar.postDelayed(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFragment$VideoPreviewManager$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageFragment.VideoPreviewManager.$r8$lambda$hRi7CxNottoowXuamHDBKUmBhxY(PhotoPageFragment.VideoPreviewManager.this);
                }
            }, 450L);
        }

        public /* synthetic */ void lambda$onOrientationChanged$0() {
            PhotoPageFragment.this.setFrameBar(this.mSeekBar);
        }

        public void onActionBarVisibilityChanged(boolean z) {
            if (!z) {
                hideDurationBar(false);
            }
        }

        public void onPageScrolled(int i, float f, int i2) {
            if (this.mSeekBar.getVisibility() != 0) {
                return;
            }
            int correctPosition = PhotoPageFragment.this.mPagerHelper.getCorrectPosition(i, this.mIsRtl);
            int i3 = this.mDraggingIndex;
            if (i3 != correctPosition) {
                if (this.mIsRtl) {
                    if (i3 + 1 != correctPosition) {
                        return;
                    }
                } else if (i3 - 1 != correctPosition) {
                    return;
                }
            }
            boolean z = i3 != correctPosition;
            if (i3 != correctPosition) {
                f = 1.0f - f;
            }
            translateSeekBar(z, f);
        }

        public void onPageScrollStateChanged(int i) {
            this.mPagerScrollState = i;
            if (this.mCurrentItem == null) {
                return;
            }
            if (i == 0) {
                DefaultLogger.d("VideoPreviewManager", "onPageScrollIdle");
                settleItem();
                Runnable runnable = this.mDelayTask;
                if (runnable == null) {
                    return;
                }
                runnable.run();
            } else if (i != 1) {
            } else {
                this.mDraggingIndex = PhotoPageFragment.this.mPager.getCurrentItem();
                this.mDragStartTransitionY = this.mSeekBar.getTranslationY();
                updateLeftAndRightItemState();
                hideDurationBar(false);
            }
        }

        public void onStart() {
            this.mProvider.onStart();
        }

        @Override // com.miui.gallery.video.VideoFrameSeekBar.OnSeekBarChangeListener
        public void onProgressChanged(float f) {
            if (this.mCurrentItem == null) {
                return;
            }
            this.mSeekTimeMarker = 0L;
            updateDuration();
            long j = (int) (f * ((float) this.mTotalDuration));
            requestFrame(j);
            updateSubtitleView(j);
            PhotoPageVideoItem photoPageVideoItem = this.mVideoPageItem;
            if (photoPageVideoItem == null) {
                return;
            }
            photoPageVideoItem.onProgressChanged();
        }

        @Override // com.miui.gallery.video.VideoFrameSeekBar.OnSeekBarChangeListener
        public void onScrollStateChanged(boolean z) {
            if (z) {
                showDurationBar();
                PhotoPageVideoItem photoPageVideoItem = this.mVideoPageItem;
                if (photoPageVideoItem != null) {
                    photoPageVideoItem.onPreviewStart();
                }
            } else {
                hideDurationBar(true);
                PhotoPageVideoItem photoPageVideoItem2 = this.mVideoPageItem;
                if (photoPageVideoItem2 != null) {
                    photoPageVideoItem2.onPreviewStop();
                }
            }
            if (PhotoPageFragment.this.mSpecialTypeManager == null || PhotoPageFragment.this.isLandscapeWindowMode()) {
                return;
            }
            PhotoPageFragment.this.mSpecialTypeManager.setEnterVisible(!z);
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnSurfacePreparedListener
        public void onSurfacePrepared(Surface surface) {
            if (this.mCurrentItem == null) {
                return;
            }
            DefaultLogger.d("VideoPreviewManager", "onSurfacePrepared");
            long j = this.mPendingSeekTime;
            if (j >= 0) {
                requestFrame(j);
            } else {
                this.mProvider.setSurfaceForVideo(this.mCurrentItem.getOriginalPath(), surface);
            }
        }

        @Override // com.miui.gallery.ui.PhotoPageVideoItem.OnThumbnailLoadedListener
        public void onThumbnailLoaded(Bitmap bitmap) {
            if (this.mPagerScrollState == 0) {
                updateAndShowSeekBar(this.mThumbListInfo, null);
            }
        }

        @Override // com.miui.gallery.video.VideoFrameProvider.Listener
        public void onVideoInfoResponse(String str, GalleryVideoInfo galleryVideoInfo) {
            DefaultLogger.d("VideoPreviewManager", "onVideoInfoResponse [%s].", galleryVideoInfo);
            BaseDataItem baseDataItem = this.mCurrentItem;
            if (baseDataItem == null || TextUtils.equals(str, baseDataItem.getOriginalPath())) {
                if (this.mCurrentItem == null || (galleryVideoInfo != null && (galleryVideoInfo.isHdr() || (VideoPlayerCompat.isVideoPlayerSupportOpenVideoFormat() && !galleryVideoInfo.isSupportFrame())))) {
                    if (this.mSeekBar.getVisibility() != 0 || this.mSeekBar.getTranslationY() >= this.mSeekBar.getHeight()) {
                        return;
                    }
                    startSeekBarTranslateAnim(this.mSeekBar.getHeight() - this.mSeekBar.getTranslationY());
                    return;
                }
                if (galleryVideoInfo == null) {
                    this.mProvider.requestThumbList(this.mCurrentItem.getOriginalPath(), this.mThumbItemWidth, this.mThumbItemHeight, false);
                } else {
                    this.mProvider.requestThumbList(this.mCurrentItem.getOriginalPath(), this.mThumbItemWidth, this.mThumbItemHeight, galleryVideoInfo.getWidth(), galleryVideoInfo.getHeight(), galleryVideoInfo.getDuration());
                }
                updateAndShowSeekBar(this.mThumbListInfo, galleryVideoInfo);
            }
        }

        @Override // com.miui.gallery.video.VideoFrameProvider.Listener
        public void onThumbListResponse(String str, VideoFrameProvider.ThumbListInfo thumbListInfo) {
            BaseDataItem baseDataItem;
            DefaultLogger.d("VideoPreviewManager", "onThumbListResponse");
            if (thumbListInfo == null || (baseDataItem = this.mCurrentItem) == null || !TextUtils.equals(str, baseDataItem.getOriginalPath()) || this.mPagerScrollState != 0) {
                return;
            }
            updateAndShowSeekBar(thumbListInfo, null);
        }

        @Override // com.miui.gallery.video.VideoFrameProvider.Listener
        public void onSingleFrameResponse(String str, long j) {
            BaseDataItem baseDataItem = this.mCurrentItem;
            if (baseDataItem != null && TextUtils.equals(str, baseDataItem.getOriginalPath())) {
                DefaultLogger.d("VideoPreviewManager", "onSingleFrameResponse %d", Long.valueOf(j));
                this.mVideoPageItem.onPreviewUpdate(j == this.mLastRequestTime);
            }
            onSeekFinish();
        }

        @Override // com.miui.gallery.video.VideoSubtitleProvider.Listener
        public void onSubtitleParsed(String str, List<VideoSubtitleProvider.SubtitleItem> list, Typeface typeface) {
            if (this.mCurrentItem == null || !BaseMiscUtil.isValid(list) || !TextUtils.equals(str, this.mCurrentItem.getOriginalPath())) {
                return;
            }
            this.mSubtitleItems = list;
            this.mSubtitleTypeface = typeface;
            updateSubtitleView();
        }

        public void seekTo(long j, boolean z) {
            this.mSeekTimeMarker = j;
            this.mSeekNeedCallback = z;
            long j2 = this.mTotalDuration;
            if (j2 <= 0 || j < 0) {
                onSeekFinish();
                return;
            }
            long max = Math.max(0L, Math.min(j2, j));
            float f = (((float) max) * 1.0f) / ((float) this.mTotalDuration);
            this.mSeekBar.setProgress(Math.max(0.0f, Math.min(1.0f, f)));
            requestFrame(max);
            updateSubtitleView(max);
            PhotoPageVideoItem photoPageVideoItem = this.mVideoPageItem;
            if (photoPageVideoItem != null) {
                photoPageVideoItem.onPreviewStop();
            }
            DefaultLogger.d("VideoPreviewManager", "seekTo %d, total %d, percent %f", Long.valueOf(max), Long.valueOf(this.mTotalDuration), Float.valueOf(f));
        }

        public final boolean isValidVideoItem(BaseDataItem baseDataItem) {
            return baseDataItem != null && baseDataItem.isVideo() && !baseDataItem.isSecret() && !TextUtils.isEmpty(baseDataItem.getOriginalPath()) && !baseDataItem.is8KVideo() && !baseDataItem.isHdr10VideoRecognized() && !baseDataItem.getOriginalPath().contains("/Android/data/com.miui.gallery/files/trashBin");
        }

        public final void updateLeftAndRightItemState() {
            PhotoPageFragment photoPageFragment = PhotoPageFragment.this;
            ViewPager viewPager = photoPageFragment.mPager;
            if (viewPager == null || photoPageFragment.mAdapter == null) {
                return;
            }
            int currentItem = viewPager.getCurrentItem();
            BaseDataItem dataItem = PhotoPageFragment.this.mAdapter.getDataItem(currentItem - 1);
            boolean isValidVideoItem = isValidVideoItem(dataItem);
            this.mIsLeftItemVideo = isValidVideoItem;
            if (isValidVideoItem) {
                this.mProvider.requestVideoInfo(dataItem.getOriginalPath());
            }
            BaseDataItem dataItem2 = PhotoPageFragment.this.mAdapter.getDataItem(currentItem + 1);
            boolean isValidVideoItem2 = isValidVideoItem(dataItem2);
            this.mIsRightItemVideo = isValidVideoItem2;
            if (isValidVideoItem2) {
                this.mProvider.requestVideoInfo(dataItem2.getOriginalPath());
            }
            if (!this.mIsRtl) {
                return;
            }
            boolean z = this.mIsLeftItemVideo;
            this.mIsLeftItemVideo = this.mIsRightItemVideo;
            this.mIsRightItemVideo = z;
        }

        public final void onSeekFinish() {
            if (this.mSeekNeedCallback && PhotoPageFragment.this.mVideoPlayerManager != null) {
                PhotoPageFragment.this.mVideoPlayerManager.prepareFinish(PhotoPageFragment.this.mActivity);
                DefaultLogger.d("VideoPreviewManager", "onSeekFinish");
            }
            this.mSeekNeedCallback = false;
        }

        public long getSeekTime() {
            long j = this.mTotalDuration;
            if (j <= 0) {
                long j2 = this.mSeekTimeMarker;
                if (j2 > 0) {
                    return j2;
                }
            }
            if (this.mSeekTimeMarker > j) {
                return 0L;
            }
            float progress = this.mSeekBar.getProgress();
            long j3 = this.mTotalDuration;
            return Math.max(0L, Math.min((int) (progress * ((float) j3)), j3));
        }

        public final void updateAndShowSeekBar(VideoFrameProvider.ThumbListInfo thumbListInfo, GalleryVideoInfo galleryVideoInfo) {
            DefaultLogger.d("VideoPreviewManager", "updateAndShowSeekBar");
            if (this.mVideoPageItem != null && isValidVideoItem(this.mCurrentItem)) {
                if (thumbListInfo != null) {
                    VideoFrameProvider.ThumbListInfo thumbListInfo2 = this.mThumbListInfo;
                    if (thumbListInfo2 == null || !thumbListInfo2.equals(thumbListInfo) || this.mShowDefaultThumb) {
                        this.mTotalDuration = thumbListInfo.getDuration();
                        List<Bitmap> thumbList = thumbListInfo.getThumbList();
                        boolean z = this.mShowDefaultThumb;
                        if (!BaseMiscUtil.isValid(thumbList)) {
                            thumbList = generateThumbList(this.mVideoPageItem.getVideoThumbnail(), this.mTotalDuration);
                            this.mShowDefaultThumb = true;
                        } else {
                            this.mShowDefaultThumb = false;
                        }
                        this.mSeekBar.updateFrameList(thumbList, !this.mShowDefaultThumb || !z, !z);
                        this.mThumbListInfo = thumbListInfo;
                    }
                } else if (!this.mShowDefaultThumb) {
                    if (this.mCurrentItem.getDuration() > 0) {
                        this.mTotalDuration = this.mCurrentItem.getDuration() * 1000;
                    } else if (galleryVideoInfo != null && galleryVideoInfo.getDuration() > 0) {
                        this.mTotalDuration = galleryVideoInfo.getDuration();
                    }
                    List<Bitmap> generateThumbList = generateThumbList(this.mVideoPageItem.getVideoThumbnail(), this.mTotalDuration);
                    this.mShowDefaultThumb = BaseMiscUtil.isValid(generateThumbList);
                    this.mSeekBar.updateFrameList(generateThumbList, true, true);
                }
                if (this.mSeekBar.getVisibility() != 0) {
                    this.mSeekBar.setVisibility(0);
                    VideoFrameSeekBar videoFrameSeekBar = this.mSeekBar;
                    videoFrameSeekBar.setTranslationY(videoFrameSeekBar.getHeight());
                    PhotoPageFragment.this.isVideoSeekBarVisible = true;
                }
                updateTagsView();
                startSeekBarTranslateAnim(-this.mSeekBar.getTranslationY());
            }
        }

        public final List<Bitmap> generateThumbList(Bitmap bitmap, long j) {
            if (bitmap == null) {
                return null;
            }
            int max = Math.max(VideoFrameProvider.getFrameCount(j), 5);
            ArrayList arrayList = new ArrayList(max);
            for (int i = 0; i < max; i++) {
                arrayList.add(bitmap);
            }
            return arrayList;
        }

        public final void updateSubtitleView() {
            updateSubtitleView(((float) this.mTotalDuration) * this.mSeekBar.getProgress());
        }

        public final void updateSubtitleView(long j) {
            BaseDataItem baseDataItem;
            if (this.mPagerScrollState != 0 || (baseDataItem = this.mCurrentItem) == null || this.mVideoPageItem == null || !baseDataItem.isSupportSubtitle() || !BaseMiscUtil.isValid(this.mSubtitleItems)) {
                return;
            }
            VideoSubtitleProvider.SubtitleItem findActiveSubtitle = VideoSubtitleProvider.findActiveSubtitle(this.mSubtitleItems, j);
            this.mVideoPageItem.updateSubtitleView(findActiveSubtitle == null ? null : findActiveSubtitle.getText(), this.mSubtitleTypeface);
        }

        public final void updateTagsView() {
            if (!this.mSupportTags || this.mIsTagsUpdated) {
                return;
            }
            if (this.mPagerScrollState != 0) {
                this.mDelayTask = new Runnable() { // from class: com.miui.gallery.ui.PhotoPageFragment$VideoPreviewManager$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PhotoPageFragment.VideoPreviewManager.$r8$lambda$3GD9AQhxzOm265thT_wFOcfLMpQ(PhotoPageFragment.VideoPreviewManager.this);
                    }
                };
            } else {
                doUpdateTagsView();
            }
        }

        public final void doUpdateTagsView() {
            if (this.mSeekBarRunningAnim) {
                return;
            }
            BaseDataItem baseDataItem = this.mCurrentItem;
            if (baseDataItem == null || !baseDataItem.isSupportTags() || this.mTotalDuration <= 0 || (!this.mShowDefaultThumb && this.mThumbListInfo == null)) {
                this.mSeekBar.setVideoTags(null);
                return;
            }
            List<Long> videoTags = this.mCurrentItem.getVideoTags();
            if (!BaseMiscUtil.isValid(videoTags)) {
                this.mSeekBar.setVideoTags(null);
                return;
            }
            ArrayList arrayList = new ArrayList();
            for (Long l : videoTags) {
                if (l.longValue() >= 0 && l.longValue() <= this.mTotalDuration) {
                    arrayList.add(Float.valueOf((((float) l.longValue()) * 1.0f) / ((float) this.mTotalDuration)));
                }
            }
            this.mSeekBar.setVideoTags(arrayList);
            this.mIsTagsUpdated = true;
            this.mDelayTask = null;
        }

        public final void startSeekBarTranslateAnim(float f) {
            if (!this.mSeekBarRunningAnim) {
                this.mSeekBar.animate().translationYBy(f).setDuration(Math.max((int) (((1.0f * f) / this.mSeekBar.getHeight()) * 250.0f), 150)).setStartDelay(0L).setInterpolator(new CubicEaseOutInterpolator()).setListener(this.mSeekBarAnimListener).start();
            }
        }

        public final void dismissSeekBar() {
            if (this.mSeekBar.getVisibility() == 0) {
                this.mSeekBar.updateFrameList(null, true, true);
                this.mSeekBar.setVideoTags(null);
                this.mSeekBar.setVisibility(8);
            }
            PhotoPageFragment.this.isVideoSeekBarVisible = false;
            this.mShowDefaultThumb = false;
            this.mIsTagsUpdated = false;
            this.mSeekTimeMarker = 0L;
        }

        public final void translateSeekBar(boolean z, float f) {
            if ((z && this.mIsLeftItemVideo) || ((!z && this.mIsRightItemVideo) || f <= 0.0f)) {
                startSeekBarTranslateAnim(-this.mSeekBar.getTranslationY());
                this.mDragStartTransitionY = 0.0f;
                return;
            }
            if (this.mSeekBarRunningAnim) {
                this.mSeekBar.animate().cancel();
                this.mSeekBarRunningAnim = false;
                this.mDragStartTransitionY = this.mSeekBar.getTranslationY();
            }
            float height = this.mSeekBar.getHeight();
            float f2 = f * height * 2.0f;
            float f3 = this.mDragStartTransitionY;
            if (f3 > 0.0f) {
                f2 = Math.min(f3 + f2, height);
            }
            this.mSeekBar.setTranslationY(f2);
        }

        public final void showDurationBar() {
            if (this.mDurationBar == null) {
                ViewGroup viewGroup = (ViewGroup) ((ViewStub) PhotoPageFragment.this.getView().findViewById(R.id.video_duration_bar_stub)).inflate();
                this.mDurationBar = viewGroup;
                this.mProgressTextView = (TextView) viewGroup.findViewById(R.id.video_progress);
                this.mDurationTextView = (TextView) this.mDurationBar.findViewById(R.id.video_duration);
                this.mDurationBar.setAlpha(0.0f);
            }
            this.mDurationBar.setVisibility(0);
            updateDuration();
            updateDurationBarLayout();
            this.mDurationBar.removeCallbacks(this.mDurationBarHideRunnable);
            if (this.mDurationBar.getAlpha() < 1.0f) {
                this.mDurationBar.animate().alpha(1.0f).setDuration(0L).setInterpolator(new SineEaseInOutInterpolator()).start();
            }
        }

        public final void hideDurationBar(boolean z) {
            ViewGroup viewGroup = this.mDurationBar;
            if (viewGroup == null || viewGroup.getAlpha() == 0.0f) {
                return;
            }
            this.mDurationBar.removeCallbacks(this.mDurationBarHideRunnable);
            if (z) {
                this.mDurationBar.postDelayed(this.mDurationBarHideRunnable, 0L);
            } else {
                this.mDurationBarHideRunnable.run();
            }
        }

        public final void updateDurationBarLayout() {
            ViewGroup viewGroup = this.mDurationBar;
            if (viewGroup == null) {
                return;
            }
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) viewGroup.getLayoutParams();
            marginLayoutParams.bottomMargin = (PhotoPageFragment.this.isLandscapeWindowMode() ? 0 : PhotoPageFragment.this.getSplitBarHeight()) + this.mSeekBar.getHeight() + this.mDurationBarMargin;
            this.mDurationBar.setLayoutParams(marginLayoutParams);
        }

        public final void updateDuration() {
            if (this.mDurationBar == null) {
                return;
            }
            this.mProgressTextView.setText(FormatUtil.formatVideoDuration((long) Math.floor((this.mSeekBar.getProgress() * ((float) this.mTotalDuration)) / 1000.0d)));
            this.mDurationTextView.setText(FormatUtil.formatVideoDuration((long) Math.floor(this.mTotalDuration / 1000.0d)));
        }

        public final void dismissDurationBar() {
            ViewGroup viewGroup = this.mDurationBar;
            if (viewGroup != null) {
                viewGroup.setVisibility(8);
                this.mDurationBar.removeCallbacks(this.mDurationBarHideRunnable);
                this.mDurationBar.animate().cancel();
                this.mDurationBar.setAlpha(0.0f);
            }
        }

        public final void requestFrame(long j) {
            if (this.mCurrentItem != null && this.mThumbListInfo != null) {
                long j2 = this.mTotalDuration;
                if (j2 > 0 && j >= 0 && j <= j2) {
                    Surface previewSurface = this.mVideoPageItem.getPreviewSurface();
                    if (previewSurface == null) {
                        this.mPendingSeekTime = j;
                        return;
                    }
                    this.mLastRequestTime = j;
                    this.mProvider.requestSingleFrame(this.mCurrentItem.getOriginalPath(), this.mThumbListInfo.getWidth(), this.mThumbListInfo.getHeight(), j, previewSurface);
                    this.mPendingSeekTime = -1L;
                    return;
                }
            }
            onRequestFrameFailed();
        }

        public final void onRequestFrameFailed() {
            DefaultLogger.d("VideoPreviewManager", "onRequestFrameFailed");
            onSeekFinish();
        }

        public void onDestroy() {
            BaseDataItem baseDataItem = this.mCurrentItem;
            if (baseDataItem != null) {
                this.mProvider.releaseForVideo(baseDataItem.getOriginalPath());
            }
            this.mProvider.release();
            this.mSubtitleProvider.release();
            dismissDurationBar();
            if (this.mDelayTask != null) {
                this.mDelayTask = null;
            }
            VideoFrameSeekBar videoFrameSeekBar = this.mSeekBar;
            if (videoFrameSeekBar != null) {
                videoFrameSeekBar.setOnSeekBarChangeListener(null);
            }
        }

        public final void refreshSeekBarIfNeed() {
            BaseDataItem baseDataItem;
            if (!this.mShowDefaultThumb || (baseDataItem = this.mCurrentItem) == null) {
                return;
            }
            this.mProvider.requestThumbList(baseDataItem.getOriginalPath(), this.mThumbItemWidth, this.mThumbItemHeight, true);
        }
    }

    /* loaded from: classes2.dex */
    public class ScrollPrivacyManager {
        public int mDraggingIndex;
        public boolean mIsRtl;
        public int mPageMargin;

        public ScrollPrivacyManager() {
            PhotoPageFragment.this = r4;
            boolean z = false;
            this.mDraggingIndex = 0;
            this.mPageMargin = r4.getResources().getDimensionPixelSize(R.dimen.viewpager_page_margin);
            this.mIsRtl = r4.getResources().getConfiguration().getLayoutDirection() == 1 ? true : z;
        }

        /* JADX WARN: Code restructure failed: missing block: B:49:0x002b, code lost:
            if ((com.miui.gallery.ktx.DisplayKt.getDisplayWidth(com.miui.gallery.ui.PhotoPageFragment.this.mPager) - r5) >= 0) goto L15;
         */
        /* JADX WARN: Code restructure failed: missing block: B:51:0x002e, code lost:
            r1 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:57:0x0039, code lost:
            if (r5 > r2.mPageMargin) goto L15;
         */
        /* JADX WARN: Code restructure failed: missing block: B:58:0x003b, code lost:
            r3 = com.miui.gallery.ui.PhotoPageFragment.this.mPagerHelper.getItem(r4);
         */
        /* JADX WARN: Code restructure failed: missing block: B:59:0x0043, code lost:
            if (r1 == false) goto L24;
         */
        /* JADX WARN: Code restructure failed: missing block: B:60:0x0045, code lost:
            if (r3 == null) goto L23;
         */
        /* JADX WARN: Code restructure failed: missing block: B:62:0x0051, code lost:
            if (com.miui.gallery.ui.PhotoPageFragment.this.isNeedConfirmPassWord(r3.getDataItem()) == false) goto L22;
         */
        /* JADX WARN: Code restructure failed: missing block: B:63:0x0053, code lost:
            com.miui.gallery.ui.PhotoPageFragment.this.mDataProvider.getFieldData().isNeedConfirmPassWord = false;
            com.miui.gallery.ui.AuthenticatePrivacyPasswordFragment.startAuthenticatePrivacyPassword(com.miui.gallery.ui.PhotoPageFragment.this, false);
         */
        /* JADX WARN: Code restructure failed: missing block: B:64:0x0062, code lost:
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:65:?, code lost:
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:66:?, code lost:
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:67:?, code lost:
            return;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void onPageScrolled(int r3, float r4, int r5) {
            /*
                r2 = this;
                r0 = 0
                int r4 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
                if (r4 != 0) goto L6
                return
            L6:
                com.miui.gallery.ui.PhotoPageFragment r4 = com.miui.gallery.ui.PhotoPageFragment.this
                com.miui.gallery.ui.PhotoPagerHelper r4 = r4.mPagerHelper
                boolean r0 = r2.mIsRtl
                int r3 = r4.getCorrectPosition(r3, r0)
                int r4 = r2.mDraggingIndex
                r0 = 0
                r1 = 1
                if (r4 == r3) goto L18
                r3 = r1
                goto L19
            L18:
                r3 = r0
            L19:
                if (r3 == 0) goto L30
                boolean r3 = r2.mIsRtl
                if (r3 == 0) goto L21
                int r4 = r4 + r1
                goto L22
            L21:
                int r4 = r4 - r1
            L22:
                com.miui.gallery.ui.PhotoPageFragment r3 = com.miui.gallery.ui.PhotoPageFragment.this
                com.miui.gallery.widget.ViewPager r3 = r3.mPager
                int r3 = com.miui.gallery.ktx.DisplayKt.getDisplayWidth(r3)
                int r3 = r3 - r5
                if (r3 < 0) goto L2e
                goto L3b
            L2e:
                r1 = r0
                goto L3b
            L30:
                boolean r3 = r2.mIsRtl
                if (r3 == 0) goto L36
                int r4 = r4 - r1
                goto L37
            L36:
                int r4 = r4 + r1
            L37:
                int r3 = r2.mPageMargin
                if (r5 <= r3) goto L2e
            L3b:
                com.miui.gallery.ui.PhotoPageFragment r3 = com.miui.gallery.ui.PhotoPageFragment.this
                com.miui.gallery.ui.PhotoPagerHelper r3 = r3.mPagerHelper
                com.miui.gallery.ui.PhotoPageItem r3 = r3.getItem(r4)
                if (r1 == 0) goto L62
                if (r3 == 0) goto L62
                com.miui.gallery.ui.PhotoPageFragment r4 = com.miui.gallery.ui.PhotoPageFragment.this
                com.miui.gallery.model.BaseDataItem r3 = r3.getDataItem()
                boolean r3 = r4.isNeedConfirmPassWord(r3)
                if (r3 == 0) goto L62
                com.miui.gallery.ui.PhotoPageFragment r3 = com.miui.gallery.ui.PhotoPageFragment.this
                com.miui.gallery.ui.photoPage.bars.data.IDataProvider r3 = r3.mDataProvider
                com.miui.gallery.ui.photoPage.bars.data.DataProvider$FieldData r3 = r3.getFieldData()
                r3.isNeedConfirmPassWord = r0
                com.miui.gallery.ui.PhotoPageFragment r3 = com.miui.gallery.ui.PhotoPageFragment.this
                com.miui.gallery.ui.AuthenticatePrivacyPasswordFragment.startAuthenticatePrivacyPassword(r3, r0)
            L62:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.PhotoPageFragment.ScrollPrivacyManager.onPageScrolled(int, float, int):void");
        }

        public void onPageScrollStateChanged(int i) {
            if (i == 1) {
                this.mDraggingIndex = PhotoPageFragment.this.mPager.getCurrentItem();
            }
        }
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.strategy.IStrategyFollower
    public boolean isSupportCutoutModeShortEdges() {
        return VideoPlayerCompat.isVideoPlayerSupportCutoutModeShortEdges();
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.strategy.IStrategyFollower
    public boolean isNeedCheckCutoutBlacklist() {
        return !this.mDataProvider.getFieldData().isFromCamera;
    }

    /* loaded from: classes2.dex */
    public class CameraAnimManager {
        public boolean mIsLocked;
        public boolean mIsTranslucent;
        public boolean mShouldWork;

        public CameraAnimManager() {
            PhotoPageFragment.this = r1;
            config(r1.getArguments());
        }

        public final void config(Bundle bundle) {
            if (bundle == null) {
                return;
            }
            boolean z = false;
            if (PhotoPageFragment.this.mDataProvider.getFieldData().isFromCamera && bundle.getBoolean("photo_enter_transit", false)) {
                z = true;
            }
            this.mShouldWork = z;
            if (bundle.getInt("enter_orientation", -1) == 1) {
                unlock();
            } else {
                lock();
            }
        }

        public boolean shouldWork() {
            return this.mShouldWork;
        }

        public void lock() {
            if (!this.mShouldWork) {
                return;
            }
            DefaultLogger.d("CameraAnimManager", "lock.");
            this.mIsLocked = true;
        }

        public void unlock() {
            if (!this.mShouldWork) {
                return;
            }
            DefaultLogger.d("CameraAnimManager", "unlock.");
            this.mIsLocked = false;
        }

        public boolean isLocked() {
            if (!this.mShouldWork) {
                return false;
            }
            return this.mIsLocked;
        }

        public void dealWithConfigurationChanged(Configuration configuration) {
            if (!this.mShouldWork) {
                return;
            }
            DefaultLogger.d("CameraAnimManager", "dealWithConfigurationChanged.");
            if (configuration.orientation == 1) {
                unlock();
            } else {
                lock();
            }
        }

        public boolean doExit() {
            if (!this.mShouldWork || !isLocked()) {
                return false;
            }
            SystemUiUtil.enableSeamlessRotation(PhotoPageFragment.this.getActivity(), false);
            return true;
        }

        public void doOnTransitEnd() {
            if (!this.mShouldWork || !isLocked()) {
                return;
            }
            SystemUiUtil.disableSeamlessRotation(PhotoPageFragment.this.getActivity());
        }

        public void onAlphaChanged(boolean z) {
            if (this.mIsTranslucent == z) {
                return;
            }
            this.mIsTranslucent = z;
            if (!this.mShouldWork) {
                return;
            }
            sendBroadcast(z);
        }

        public final void sendBroadcast(boolean z) {
            Intent intent = new Intent("com.miui.gallery.ACTION_CHANGE_PHOTO_PAGE_BACKGROUND_ALPHA");
            intent.setPackage("com.android.camera");
            intent.putExtra("is_translucent", z);
            Context context = PhotoPageFragment.this.getContext();
            if (context != null) {
                context.sendBroadcast(intent);
            }
        }
    }
}
