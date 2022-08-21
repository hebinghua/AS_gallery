package com.miui.gallery.ui.photoPage.bars;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.tracing.Trace;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.activity.HomePageActivity;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.compat.app.ActionBarCompat;
import com.miui.gallery.data.LocationManager;
import com.miui.gallery.map.utils.MapInitializerImpl;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.threadpool.GallerySchedulers;
import com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment;
import com.miui.gallery.ui.photoPage.PhotoPageOrientationManager;
import com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.ui.photoPage.bars.view.ActionBarCustomViewBuilder;
import com.miui.gallery.ui.photoPage.bars.view.IPhotoPageTopBar;
import com.miui.gallery.ui.photoPage.bars.view.IViewProvider;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.IdleUITaskHelper;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.SpecialTypeMediaUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.VideoPlayerCompat;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.processors.PublishProcessor;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import miuix.appcompat.app.ActionBar;
import miuix.pickerwidget.date.DateUtils;

/* loaded from: classes2.dex */
public class PhotoPageActionBarManager implements IPhotoPageActionBarManager {
    public ActionBar mActionBar;
    public View.OnClickListener mBackClickListener;
    public final Context mContext;
    public final IDataProvider mDataProvider;
    public final WeakReference<FragmentActivity> mFragmentActivityWeakReference;
    public View.OnClickListener mOnLocationInfoClickListener;
    public View.OnClickListener mOnLockClickListener;
    public View.OnClickListener mOnRotateClickListener;
    public View.OnClickListener mOnWatchAllClickListener;
    public final IActionBarOwner mOwner;
    public Disposable mRefreshTopBarDisposable;
    public IPhotoPageTopBar mTopBar;
    public final IViewProvider mViewProvider;
    public final PublishProcessor<Bean> mRefreshTopBarPublishProcessor = PublishProcessor.create();
    public final BarSelector mBarSelector = new BarSelector();
    public AccessibilityDelegate mAccessibilityDelegate = new AccessibilityDelegate();

    /* loaded from: classes2.dex */
    public interface IActionBarOwner extends IBarsOwner {
        void doExit(boolean z);

        void onActivityTransitionForSpecialType();

        void setCurrentFocusView(View view);
    }

    /* renamed from: $r8$lambda$oe-97xqo5EdArpqBoYgC5CROZCo */
    public static /* synthetic */ void m1606$r8$lambda$oe97xqo5EdArpqBoYgC5CROZCo(PhotoPageActionBarManager photoPageActionBarManager, Bean bean) {
        photoPageActionBarManager.lambda$subscribe$3(bean);
    }

    public static /* synthetic */ Bean $r8$lambda$ynsXSExNln2ArReIbZN7h7_haWo(PhotoPageActionBarManager photoPageActionBarManager, Bean bean) {
        return photoPageActionBarManager.lambda$subscribe$2(bean);
    }

    public PhotoPageActionBarManager(IActionBarOwner iActionBarOwner, IDataProvider iDataProvider, IViewProvider iViewProvider) {
        this.mOwner = iActionBarOwner;
        this.mFragmentActivityWeakReference = new WeakReference<>(iActionBarOwner.getActivity());
        this.mContext = iActionBarOwner.getActivity().getApplicationContext();
        this.mDataProvider = iDataProvider;
        this.mViewProvider = iViewProvider;
    }

    public final FragmentActivity getFragmentActivity() {
        return this.mFragmentActivityWeakReference.get();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void onConfigurationChanged(Configuration configuration) {
        DefaultLogger.d("PhotoPageFragment_ActionBarManager", "onConfigurationChanged =>");
        this.mBarSelector.onConfigurationChanged(configuration);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void prepareViews() {
        DefaultLogger.d("PhotoPageFragment_ActionBarManager", "prepareViews =>");
        this.mViewProvider.prepareActionBarViews();
        this.mTopBar = this.mBarSelector.checkAndCreateBar();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void delegate(ActionBar actionBar) {
        DefaultLogger.d("PhotoPageFragment_ActionBarManager", "delegate ActionBar => " + actionBar);
        this.mActionBar = actionBar;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void attach() {
        DefaultLogger.d("PhotoPageFragment_ActionBarManager", "attach =>");
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void show(boolean z) {
        ActionBar actionBar = this.mActionBar;
        if (actionBar == null) {
            return;
        }
        if (z) {
            actionBar.show();
        } else {
            noAnimAction(new BasePhotoPageBarsDelegateFragment.SimpleCallback() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.SimpleCallback
                public final void duringAction() {
                    PhotoPageActionBarManager.this.lambda$show$0();
                }
            });
        }
    }

    public /* synthetic */ void lambda$show$0() {
        this.mActionBar.show();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void hide(boolean z) {
        ActionBar actionBar = this.mActionBar;
        if (actionBar == null) {
            return;
        }
        if (z) {
            actionBar.hide();
        } else {
            noAnimAction(new BasePhotoPageBarsDelegateFragment.SimpleCallback() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager$$ExternalSyntheticLambda1
                @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.SimpleCallback
                public final void duringAction() {
                    PhotoPageActionBarManager.this.lambda$hide$1();
                }
            });
        }
    }

    public /* synthetic */ void lambda$hide$1() {
        this.mActionBar.hide();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public boolean isShowing() {
        ActionBar actionBar = this.mActionBar;
        return actionBar != null && actionBar.isShowing();
    }

    public final void noAnimAction(BasePhotoPageBarsDelegateFragment.SimpleCallback simpleCallback) {
        if (simpleCallback == null) {
            return;
        }
        ActionBarCompat.setShowHideAnimationEnabled(this.mActionBar, false);
        simpleCallback.duringAction();
        ActionBarCompat.setShowHideAnimationEnabled(this.mActionBar, true);
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onStart(LifecycleOwner lifecycleOwner) {
        refreshTopBarAllElements();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void refreshTopBarAllElements() {
        PhotoPageOrientationManager.IPhotoPageOrientationManagerController orientationController = this.mOwner.getOrientationController();
        if (this.mTopBar == null || orientationController == null) {
            return;
        }
        boolean isScreenOrientationLocked = orientationController.isScreenOrientationLocked();
        boolean z = this.mDataProvider.getFieldData().isFromCamera;
        boolean isInMultiWindowMode = this.mDataProvider.getFieldData().mCurrent.isInMultiWindowMode();
        boolean z2 = true;
        refreshTopBarRotateEnter(isScreenOrientationLocked && !z && !isInMultiWindowMode);
        boolean isVideoPlayerSupportLockOrientation = VideoPlayerCompat.isVideoPlayerSupportLockOrientation();
        boolean isOrientationLocked = orientationController.isOrientationLocked();
        if (isScreenOrientationLocked || z || isInMultiWindowMode || !isVideoPlayerSupportLockOrientation) {
            z2 = false;
        }
        refreshTopBarLockEnter(z2, isOrientationLocked);
        if (isScreenOrientationLocked || isOrientationLocked || this.mDataProvider.getFieldData().isHideInAdvanceByLandAction) {
            return;
        }
        orientationController.setRequestedOrientation(4, "refreshTopBarAllElements");
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void refreshTopBarInfo(BaseDataItem baseDataItem) {
        if (this.mTopBar == null) {
            return;
        }
        Trace.beginSection("onNext");
        Disposable disposable = this.mRefreshTopBarDisposable;
        if (disposable == null || disposable.isDisposed()) {
            subscribe();
        }
        this.mRefreshTopBarPublishProcessor.onNext(new Bean(baseDataItem, this.mTopBar));
        Trace.endSection();
    }

    public void refreshTopBarRotateEnter(boolean z) {
        if (this.mTopBar == null) {
            return;
        }
        Trace.beginSection("setRotateButtonVisible");
        this.mTopBar.setRotateButtonVisible(z);
        Trace.endSection();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void refreshTopBarSpecialTypeEnter(BaseDataItem baseDataItem, View.OnClickListener onClickListener) {
        if (this.mTopBar == null) {
            return;
        }
        Configuration configuration = this.mContext.getResources().getConfiguration();
        boolean z = configuration.smallestScreenWidthDp >= BaseBuildUtil.BIG_HORIZONTAL_WINDOW_STANDARD || this.mDataProvider.getFieldData().mCurrent.isInMultiWindowMode();
        boolean z2 = configuration.screenWidthDp >= configuration.screenHeightDp;
        if (baseDataItem != null && baseDataItem.isSpecialTypeEditable() && z2 && !z) {
            this.mTopBar.setSpecialTypeEnterVisible(true);
            List<Integer> specialTypeEnterIconId = SpecialTypeMediaUtils.getSpecialTypeEnterIconId(this.mContext, baseDataItem.getSpecialTypeFlags(), baseDataItem.getAIModeTypeFlags());
            List<Integer> specialTypeEnterTextId = SpecialTypeMediaUtils.getSpecialTypeEnterTextId(baseDataItem.getSpecialTypeFlags(), baseDataItem.getAIModeTypeFlags());
            if (!BaseMiscUtil.isValid(specialTypeEnterIconId) || specialTypeEnterIconId.size() != specialTypeEnterTextId.size()) {
                return;
            }
            this.mTopBar.updateSpecialTypeEnter(specialTypeEnterIconId.get(0).intValue(), specialTypeEnterTextId.get(0).intValue());
            this.mTopBar.setSpecialTypeEnterClickListener(onClickListener);
            return;
        }
        this.mTopBar.setSpecialTypeEnterVisible(false);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void refreshTopBarMotionPhotoEnter(boolean z, View.OnClickListener onClickListener) {
        IPhotoPageTopBar iPhotoPageTopBar = this.mTopBar;
        if (iPhotoPageTopBar == null) {
            return;
        }
        int i = 0;
        iPhotoPageTopBar.setOperationViewVisibility(z ? 0 : 8);
        this.mTopBar.setOperationViewClickListener(onClickListener);
        BarSelector barSelector = this.mBarSelector;
        if (!z) {
            i = 8;
        }
        barSelector.cacheOperationViewVisibility(i);
        this.mBarSelector.cacheOperationViewClickListener(onClickListener);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void refreshTopBarLockEnter(boolean z, boolean z2) {
        if (this.mTopBar == null) {
            return;
        }
        Trace.beginSection("setLockButtonVisible");
        this.mTopBar.setLockButtonVisible(z);
        Trace.endSection();
        if (!z) {
            return;
        }
        Trace.beginSection("setLockButtonLockIcon");
        this.mTopBar.setLockButtonLock(z2);
        Trace.endSection();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void refreshTopBarLocation(int i, int i2) {
        IPhotoPageTopBar iPhotoPageTopBar = this.mTopBar;
        if (iPhotoPageTopBar == null) {
            return;
        }
        iPhotoPageTopBar.onOrientationChanged(i, i2);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void showLockButtonGuide() {
        IPhotoPageTopBar iPhotoPageTopBar = this.mTopBar;
        if (iPhotoPageTopBar == null) {
            return;
        }
        iPhotoPageTopBar.showLockButtonGuide();
    }

    public final void subscribe() {
        this.mRefreshTopBarDisposable = this.mRefreshTopBarPublishProcessor.observeOn(GallerySchedulers.misc()).map(new Function() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager$$ExternalSyntheticLambda3
            @Override // io.reactivex.functions.Function
            /* renamed from: apply */
            public final Object mo2564apply(Object obj) {
                return PhotoPageActionBarManager.$r8$lambda$ynsXSExNln2ArReIbZN7h7_haWo(PhotoPageActionBarManager.this, (PhotoPageActionBarManager.Bean) obj);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager$$ExternalSyntheticLambda2
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                PhotoPageActionBarManager.m1606$r8$lambda$oe97xqo5EdArpqBoYgC5CROZCo(PhotoPageActionBarManager.this, (PhotoPageActionBarManager.Bean) obj);
            }
        });
    }

    public /* synthetic */ Bean lambda$subscribe$2(Bean bean) throws Exception {
        String viewTitle;
        String viewSubTitle;
        String location;
        BaseDataItem baseDataItem = bean.baseDataItem;
        if (baseDataItem == null) {
            long j = this.mDataProvider.getFieldData().mEnterTime;
            String str = this.mDataProvider.getFieldData().mEnterLocation;
            viewTitle = DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), j, 896);
            viewSubTitle = DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), j, 44);
            location = TextUtils.isEmpty(str) ? "" : LocationManager.getInstance().generateTitleLine(str);
        } else {
            viewTitle = baseDataItem.getViewTitle(this.mContext);
            viewSubTitle = bean.baseDataItem.getViewSubTitle(this.mContext);
            location = bean.baseDataItem.getLocation();
        }
        bean.title = viewTitle;
        bean.subTitle = viewSubTitle;
        bean.location = location;
        return bean;
    }

    public /* synthetic */ void lambda$subscribe$3(Bean bean) throws Exception {
        WeakReference<IPhotoPageTopBar> weakReference = bean.topBar;
        if (weakReference == null || weakReference.get() == null || bean.baseDataItem == null || this.mContext == null) {
            return;
        }
        Trace.beginSection("refreshTopTitle");
        bean.topBar.get().setTitle(bean.title);
        bean.topBar.get().setSubTitle(bean.subTitle);
        bean.topBar.get().setLocation(this.mContext, bean.baseDataItem, this.mDataProvider.getFieldData().isStartWhenLocked);
        Trace.endSection();
    }

    /* loaded from: classes2.dex */
    public static class Bean {
        public BaseDataItem baseDataItem;
        public String location;
        public String subTitle;
        public String title;
        public WeakReference<IPhotoPageTopBar> topBar;

        public Bean(BaseDataItem baseDataItem, IPhotoPageTopBar iPhotoPageTopBar) {
            this.baseDataItem = baseDataItem;
            this.topBar = new WeakReference<>(iPhotoPageTopBar);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void inflateActionBarCustomView() {
        this.mBarSelector.inflateActionBarCustomView();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void setTopBarContentVisibility(boolean z) {
        IPhotoPageTopBar iPhotoPageTopBar = this.mTopBar;
        if (iPhotoPageTopBar == null) {
            return;
        }
        if (z) {
            iPhotoPageTopBar.show();
        } else {
            iPhotoPageTopBar.hide();
        }
    }

    /* loaded from: classes2.dex */
    public class BarSelector {
        public final Map<ActionBarCustomViewBuilder.CustomViewType, IPhotoPageTopBar> mBarMap = new HashMap();
        public final Map<ActionBarCustomViewBuilder.CustomViewType, Integer> mBarHeightMap = new HashMap();
        public final CacheHolder mCacheHolder = new CacheHolder();

        public static /* synthetic */ void $r8$lambda$4dZrMGLrCDW6946CM8BW4aSp5m8(BarSelector barSelector, View view) {
            barSelector.lambda$bindListener$2(view);
        }

        public static /* synthetic */ void $r8$lambda$RFwMtS7dnQ2DK_bpudMOyTEnKEg(View view) {
            view.measure(0, 0);
        }

        /* renamed from: $r8$lambda$gsDbSFpVZ4lI4NL-Q05rY9QMn0g */
        public static /* synthetic */ void m1608$r8$lambda$gsDbSFpVZ4lI4NLQ05rY9QMn0g(BarSelector barSelector, View view) {
            barSelector.lambda$bindListener$4(view);
        }

        public static /* synthetic */ void $r8$lambda$rGp5mXr_cCyHwIJmHQoyBlFcWfs(BarSelector barSelector, View view) {
            barSelector.lambda$bindListener$0(view);
        }

        /* renamed from: $r8$lambda$xc0hS5-LtycbaotlANHkxxOpY_A */
        public static /* synthetic */ void m1609$r8$lambda$xc0hS5LtycbaotlANHkxxOpY_A(BarSelector barSelector, View view) {
            barSelector.lambda$bindListener$3(view);
        }

        public static /* synthetic */ void $r8$lambda$yj9e8idaOXR3JAhfUcvDmfXVU6c(BarSelector barSelector, View view) {
            barSelector.lambda$bindListener$6(view);
        }

        public BarSelector() {
            PhotoPageActionBarManager.this = r1;
            bindListener();
        }

        public /* synthetic */ void lambda$bindListener$0(View view) {
            PhotoPageActionBarManager.this.mOwner.doExit(false);
        }

        public final void bindListener() {
            PhotoPageActionBarManager.this.mBackClickListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager$BarSelector$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PhotoPageActionBarManager.BarSelector.$r8$lambda$rGp5mXr_cCyHwIJmHQoyBlFcWfs(PhotoPageActionBarManager.BarSelector.this, view);
                }
            };
            PhotoPageActionBarManager.this.mOnRotateClickListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager$BarSelector$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PhotoPageActionBarManager.BarSelector.$r8$lambda$4dZrMGLrCDW6946CM8BW4aSp5m8(PhotoPageActionBarManager.BarSelector.this, view);
                }
            };
            PhotoPageActionBarManager.this.mOnWatchAllClickListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager$BarSelector$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PhotoPageActionBarManager.BarSelector.m1609$r8$lambda$xc0hS5LtycbaotlANHkxxOpY_A(PhotoPageActionBarManager.BarSelector.this, view);
                }
            };
            PhotoPageActionBarManager.this.mOnLockClickListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager$BarSelector$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PhotoPageActionBarManager.BarSelector.m1608$r8$lambda$gsDbSFpVZ4lI4NLQ05rY9QMn0g(PhotoPageActionBarManager.BarSelector.this, view);
                }
            };
            PhotoPageActionBarManager.this.mOnLocationInfoClickListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager$BarSelector$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PhotoPageActionBarManager.BarSelector.$r8$lambda$yj9e8idaOXR3JAhfUcvDmfXVU6c(PhotoPageActionBarManager.BarSelector.this, view);
                }
            };
        }

        public /* synthetic */ void lambda$bindListener$2(View view) {
            final PhotoPageOrientationManager.IPhotoPageOrientationManagerController orientationController = PhotoPageActionBarManager.this.mOwner.getOrientationController();
            if (orientationController == null) {
                return;
            }
            if (orientationController.isLandscapeConfiguration()) {
                PhotoPageActionBarManager.this.mOwner.hideBars(false);
                PhotoPageActionBarManager.this.mOwner.onActivityTransitionForSpecialType();
                ThreadManager.getMainHandler().postDelayed(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager$BarSelector$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        PhotoPageActionBarManager.BarSelector.this.lambda$bindListener$1(orientationController);
                    }
                }, 10L);
                return;
            }
            orientationController.onRotateClick();
        }

        public /* synthetic */ void lambda$bindListener$1(PhotoPageOrientationManager.IPhotoPageOrientationManagerController iPhotoPageOrientationManagerController) {
            iPhotoPageOrientationManagerController.onRotateClick();
            PhotoPageActionBarManager.this.mOwner.showBars(true);
        }

        public /* synthetic */ void lambda$bindListener$3(View view) {
            FragmentActivity fragmentActivity = PhotoPageActionBarManager.this.getFragmentActivity();
            if (fragmentActivity == null) {
                return;
            }
            if (MiscUtil.isKeyGuardLocked(fragmentActivity)) {
                fragmentActivity.sendBroadcast(new Intent("xiaomi.intent.action.SHOW_SECURE_KEYGUARD"));
            }
            Intent makeMainActivity = Intent.makeMainActivity(new ComponentName(fragmentActivity, HomePageActivity.class));
            makeMainActivity.putExtra("extra_is_need_reset", true);
            makeMainActivity.setFlags(335544320);
            fragmentActivity.startActivity(makeMainActivity);
            PhotoPageActionBarManager.this.mDataProvider.getFieldData().isStartingHomePage = true;
            fragmentActivity.finish();
            PhotoPageActionBarManager.this.mOwner.postRecordCountEvent("camera_watch_all_photo", "camera_watch_all_photo_click");
        }

        public /* synthetic */ void lambda$bindListener$4(View view) {
            PhotoPageOrientationManager.IPhotoPageOrientationManagerController orientationController = PhotoPageActionBarManager.this.mOwner.getOrientationController();
            if (PhotoPageActionBarManager.this.mTopBar == null || orientationController == null) {
                return;
            }
            orientationController.onLockClick();
            PhotoPageActionBarManager.this.mTopBar.playLockButtonAnimation(orientationController.isOrientationLocked());
            ToastUtils.makeText(PhotoPageActionBarManager.this.mContext, orientationController.isOrientationLocked() ? R.string.toast_orientation_locked : R.string.toast_orientation_unlock);
        }

        public /* synthetic */ void lambda$bindListener$6(View view) {
            final FragmentActivity fragmentActivity = PhotoPageActionBarManager.this.getFragmentActivity();
            if (fragmentActivity == null) {
                return;
            }
            TrackController.trackClick("403.11.3.1.15335");
            if (AgreementsUtils.isNetworkingAgreementAccepted()) {
                goToMapAlbum(fragmentActivity);
            } else {
                AgreementsUtils.showUserAgreements(fragmentActivity, new OnAgreementInvokedListener() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager$BarSelector$$ExternalSyntheticLambda5
                    @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                    public final void onAgreementInvoked(boolean z) {
                        PhotoPageActionBarManager.BarSelector.this.lambda$bindListener$5(fragmentActivity, z);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$bindListener$5(FragmentActivity fragmentActivity, boolean z) {
            if (z) {
                goToMapAlbum(fragmentActivity);
            }
        }

        public final void goToMapAlbum(FragmentActivity fragmentActivity) {
            BaseDataItem dataItem = PhotoPageActionBarManager.this.mDataProvider.getFieldData().mCurrent.getDataItem();
            IntentUtil.goToMapAlbumDirectly(fragmentActivity, dataItem.getTitle(), dataItem.getCoordidate(), false);
        }

        public void inflateActionBarCustomView() {
            IPhotoPageTopBar checkAndCreateBar = checkAndCreateBar();
            if (checkAndCreateBar == null || checkAndCreateBar.getView() == null) {
                return;
            }
            setActionBarCustomView(checkAndCreateBar.getView());
        }

        public final IPhotoPageTopBar checkAndCreateBar() {
            FragmentActivity fragmentActivity = PhotoPageActionBarManager.this.getFragmentActivity();
            View.OnClickListener onClickListener = null;
            if (fragmentActivity == null) {
                return null;
            }
            ActionBarCustomViewBuilder.CustomViewType customViewType = PhotoPageActionBarManager.this.mOwner.getCustomViewType();
            IPhotoPageTopBar iPhotoPageTopBar = this.mBarMap.get(customViewType);
            PhotoPageActionBarManager.this.mTopBar = iPhotoPageTopBar;
            if (iPhotoPageTopBar == null) {
                ActionBarCustomViewBuilder.Builder onLockClickListener = new ActionBarCustomViewBuilder.Builder((GalleryActivity) fragmentActivity, customViewType, PhotoPageActionBarManager.this.mViewProvider, PhotoPageActionBarManager.this).setOnBackClickListener(PhotoPageActionBarManager.this.mBackClickListener).setOnRotateClickListener(PhotoPageActionBarManager.this.mOnRotateClickListener).setOnWatchAllClickListener(PhotoPageActionBarManager.this.mDataProvider.getFieldData().isFromCamera ? PhotoPageActionBarManager.this.mOnWatchAllClickListener : null).setOnLockClickListener(PhotoPageActionBarManager.this.mOnLockClickListener);
                if (MapInitializerImpl.checkMapAvailable()) {
                    onClickListener = PhotoPageActionBarManager.this.mOnLocationInfoClickListener;
                }
                IPhotoPageTopBar topBar = onLockClickListener.setOnLocationInfoClickListener(onClickListener).build().getTopBar();
                this.mBarMap.put(customViewType, topBar);
                PhotoPageActionBarManager.this.mTopBar = topBar;
                return topBar;
            }
            iPhotoPageTopBar.getView().setLayoutDirection(fragmentActivity.getResources().getConfiguration().getLayoutDirection());
            return iPhotoPageTopBar;
        }

        public final void updateBlurDrawable() {
            if (PhotoPageActionBarManager.this.isVideoPlayerSupportActionBarAdjust()) {
                PhotoPageActionBarManager.this.mActionBar.setBackgroundDrawable(ContextCompat.getDrawable(PhotoPageActionBarManager.this.mContext, R.drawable.action_bar_blur_background_on_low_ram_devices));
            } else {
                PhotoPageActionBarManager.this.mActionBar.setBackgroundDrawable(ContextCompat.getDrawable(PhotoPageActionBarManager.this.mContext, R.drawable.photo_page_action_bar_background_with_divider));
            }
        }

        public final void setActionBarCustomView(final View view) {
            if (PhotoPageActionBarManager.this.mActionBar == null || view == null) {
                return;
            }
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(view);
            }
            PhotoPageActionBarManager.this.mActionBar.setCustomView(view, new ActionBar.LayoutParams(-1, -1, 8388627));
            updateBlurDrawable();
            int i = 0;
            view.setVisibility(0);
            ActionBarCustomViewBuilder.CustomViewType customViewType = PhotoPageActionBarManager.this.mOwner.getCustomViewType();
            if (this.mBarHeightMap.containsKey(customViewType) && this.mBarHeightMap.get(customViewType) != null) {
                return;
            }
            IdleUITaskHelper.getInstance().addTask(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager$BarSelector$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageActionBarManager.BarSelector.$r8$lambda$RFwMtS7dnQ2DK_bpudMOyTEnKEg(view);
                }
            });
            Map<ActionBarCustomViewBuilder.CustomViewType, Integer> map = this.mBarHeightMap;
            int measuredHeight = view.getMeasuredHeight();
            if (BaseBuildUtil.isLargeScreenDevice() || !MiscUtil.isLandModeAndSupportVersion(PhotoPageActionBarManager.this.mContext)) {
                i = MiscUtil.getStatusBarHeight(PhotoPageActionBarManager.this.mContext);
            }
            map.put(customViewType, Integer.valueOf(measuredHeight + i));
        }

        public void onConfigurationChanged(Configuration configuration) {
            if (PhotoPageActionBarManager.this.mActionBar == null) {
                return;
            }
            IPhotoPageTopBar iPhotoPageTopBar = this.mBarMap.get(ActionBarCustomViewBuilder.CustomViewType.TopMenu);
            if (iPhotoPageTopBar != null) {
                iPhotoPageTopBar.dismissPopMenu();
            }
            IPhotoPageTopBar checkAndCreateBar = checkAndCreateBar();
            if (checkAndCreateBar == null) {
                return;
            }
            checkAndCreateBar.setCacheHolder(getCacheHolder());
            setActionBarCustomView(checkAndCreateBar.getView());
            checkAndCreateBar.onActivityConfigurationChanged(configuration);
            PhotoPageActionBarManager.this.refreshTopBarAllElements();
        }

        public CacheHolder getCacheHolder() {
            return this.mCacheHolder;
        }

        public void cacheOperationViewClickListener(View.OnClickListener onClickListener) {
            this.mCacheHolder.cacheClickListener(onClickListener);
        }

        public void cacheOperationViewVisibility(int i) {
            this.mCacheHolder.cacheVisible(i);
        }

        /* loaded from: classes2.dex */
        public class CacheHolder {
            public View.OnClickListener mOnClickListener;
            public int mVisible = 8;

            public CacheHolder() {
                BarSelector.this = r1;
            }

            public void cacheClickListener(View.OnClickListener onClickListener) {
                this.mOnClickListener = onClickListener;
            }

            public void cacheVisible(int i) {
                this.mVisible = i;
            }
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public int getActionBarHeight() {
        miuix.appcompat.app.ActionBar actionBar = this.mActionBar;
        int height = actionBar != null ? actionBar.getHeight() : 0;
        if (height > 0) {
            return height;
        }
        int statusBarHeight = MiscUtil.getStatusBarHeight(GalleryApp.sGetAndroidContext());
        if (MiscUtil.isLandMode(GalleryApp.sGetAndroidContext()) && !BaseBuildUtil.isLargeScreen(GalleryApp.sGetAndroidContext()) && !BaseBuildUtil.isFoldableDevice()) {
            statusBarHeight = MiscUtil.getNotchHeight(GalleryApp.sGetAndroidContext());
        }
        return statusBarHeight + getStationaryActionBarHeight();
    }

    public static int getStationaryActionBarHeight() {
        return GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(MiscUtil.isLandMode(GalleryApp.sGetAndroidContext()) ? R.dimen.photo_page_top_menu_height : R.dimen.photo_page_top_menu_bar_height);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void release() {
        Disposable disposable = this.mRefreshTopBarDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mRefreshTopBarDisposable.dispose();
        }
        WeakReference<FragmentActivity> weakReference = this.mFragmentActivityWeakReference;
        if (weakReference != null) {
            weakReference.clear();
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public void setAccessibilityDelegateFor(View view) {
        IActionBarOwner iActionBarOwner;
        if (view == null || (iActionBarOwner = this.mOwner) == null || !iActionBarOwner.isInTalkBackModel()) {
            return;
        }
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setAccessibilityDelegate(this.mAccessibilityDelegate);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager
    public boolean isVideoPlayerSupportActionBarAdjust() {
        IDataProvider iDataProvider = this.mDataProvider;
        if (iDataProvider == null) {
            return false;
        }
        return iDataProvider.getFieldData().isVideoPlayerSupportActionBarAdjust;
    }

    /* loaded from: classes2.dex */
    public class AccessibilityDelegate extends View.AccessibilityDelegate {
        public AccessibilityDelegate() {
            PhotoPageActionBarManager.this = r1;
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            if (PhotoPageActionBarManager.this.mOwner != null) {
                PhotoPageActionBarManager.this.mOwner.setCurrentFocusView(view);
            }
            return super.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
        }
    }
}
