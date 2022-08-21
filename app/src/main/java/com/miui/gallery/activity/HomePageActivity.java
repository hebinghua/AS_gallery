package com.miui.gallery.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.tracing.Trace;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.activity.HomePageActivity;
import com.miui.gallery.activity.HomePageImmersionMenuHelper;
import com.miui.gallery.activity.HomePageStartupHelper2;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.app.screenChange.ScreenSize;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.card.ui.cardlist.AssistantPageFragment;
import com.miui.gallery.cloud.GalleryMiCloudUtil;
import com.miui.gallery.listener.OnVisibilityChangeListener;
import com.miui.gallery.map.utils.MapInitializerImpl;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.permission.core.PermissionUtils;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.reddot.DisplayStatusManager;
import com.miui.gallery.security.RiskController;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.HomePageFragment;
import com.miui.gallery.ui.ImmersionMenuHolder;
import com.miui.gallery.ui.ImmersionMenuSupport;
import com.miui.gallery.ui.OnHomePageResetListener;
import com.miui.gallery.ui.StorageGuideCallback;
import com.miui.gallery.ui.StorageGuideFragment;
import com.miui.gallery.ui.UpdateDialogFragment;
import com.miui.gallery.ui.actionBar.AnimDrawableWrapper;
import com.miui.gallery.ui.actionBar.TabActionBarHelper;
import com.miui.gallery.ui.album.callback.OnAlbumCreateHandler;
import com.miui.gallery.ui.album.main.AlbumTabFragment;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.ui.album.main.utils.factory.GalleryViewCreator;
import com.miui.gallery.util.ActionURIHandler;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.BaseScreenUtil;
import com.miui.gallery.util.GradientUtil;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.OnAppFocusedListener;
import com.miui.gallery.util.OnRequestUpdateFinishAdapter;
import com.miui.gallery.util.ReflectUtils;
import com.miui.gallery.util.RequestUpdateHelper;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.IFloatingButton;
import java.util.List;
import miui.gallery.support.actionbar.ActionBarCompat;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AppCompatActivity;
import miuix.popupwidget.widget.GuidePopupWindow;
import miuix.viewpager.widget.ViewPager;

/* loaded from: classes.dex */
public class HomePageActivity extends BaseActivity implements HomePageStartupHelper2.Attacher, OnAlbumCreateHandler, DefaultLifecycleObserver, HomePageInteractionCallback, ImmersionMenuHolder, StorageGuideCallback {
    public HomeTabActionBarHelper mActionBarHelper;
    public int mContentTopPadding;
    public IFloatingButton mFloatingButton;
    public boolean mGuideFollowed;
    public boolean mHasStoragePermission;
    public HomePageStartupHelper2 mHomePageStartupHelper2;
    public View mImmersionMenuAnchorView;
    public HomePageImmersionMenuHelper mImmersionMenuHelper;
    public boolean mIsEnterByShortcut;
    public ServiceConnection mMTCameraServiceConnection;
    public OnRequestUpdateFinishListener mOnRequestUpdateFinishListener;
    public HomeTabActionBarHelper.ViewPagerChangeListener mPagerChangeListener;
    public GuidePopupWindow mPopupWindow;
    public long mRequestPermissionTime;
    public View mRootView;
    public int mStartUpPage;
    public UpdateDialogFragment mUpdateDialog;
    public int mUpdateStatus;
    public int mCurrentPagePosition = 0;
    public boolean mIsInStartup = false;
    public int mFragmentPagerScrollState = 0;
    public boolean mIsDialogShown = true;
    public final LazyValue<Activity, ViewPager> VIEW_PAGER = new LazyValue<Activity, ViewPager>() { // from class: com.miui.gallery.activity.HomePageActivity.1
        {
            HomePageActivity.this = this;
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public ViewPager mo1272onInit(Activity activity) {
            View findViewById = activity.getWindow().findViewById(R.id.view_pager);
            if (findViewById instanceof ViewPager) {
                return (ViewPager) findViewById;
            }
            return null;
        }
    };

    public static /* synthetic */ void $r8$lambda$JQj6tC9_TmcjbKbBAz1yOmk_tK8(HomePageActivity homePageActivity) {
        homePageActivity.lambda$unbindMTCameraRemoteService$0();
    }

    public static /* synthetic */ void $r8$lambda$TNWQ9jeIuDApe1R9sIdLQTxrrxQ(HomePageActivity homePageActivity) {
        homePageActivity.updateContentPadding();
    }

    public final String getPageName(int i) {
        if (i == 0) {
            return "home";
        }
        if (i == 1) {
            return "album";
        }
        if (i != 2) {
            return null;
        }
        return "assistant";
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean hasCustomContentView() {
        return true;
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean isCheckPermissionCustomized() {
        return true;
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean supportEnterSetting() {
        return true;
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean supportShowOnScreenLocked() {
        return true;
    }

    public static /* synthetic */ void access$3400(HomePageActivity homePageActivity) {
        homePageActivity.updateContentPadding();
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        try {
            Trace.beginSection("HomeActivity#onCreate");
            Trace.beginSection("HomePageStartupHelperInit");
            HomePageStartupHelper2 homePageStartupHelper2 = new HomePageStartupHelper2(this);
            this.mHomePageStartupHelper2 = homePageStartupHelper2;
            homePageStartupHelper2.attach(this);
            this.mHomePageStartupHelper2.onActivityCreate();
            Trace.endSection();
            getLayoutInflater().setFactory2(GalleryViewCreator.getViewFactory());
            super.onCreate(bundle);
            Intent intent = getIntent();
            if (intent != null) {
                this.mStartUpPage = intent.getIntExtra("extra_start_page", 0);
            }
            this.mHasStoragePermission = PermissionUtils.canAccessStorage(this, true);
            HomeTabActionBarHelper homeTabActionBarHelper = new HomeTabActionBarHelper(this);
            this.mActionBarHelper = homeTabActionBarHelper;
            homeTabActionBarHelper.inflateActionBar();
            this.mActionBarHelper.setDefaultStyleActionBar();
            if (this.mHasStoragePermission) {
                this.mActionBarHelper.setupHomePageFragment("onCreate");
            } else {
                Fragment findFragmentByTag = getSupportFragmentManager().findFragmentByTag("HomePageFragment");
                Fragment findFragmentByTag2 = getSupportFragmentManager().findFragmentByTag("AlbumTabFragment");
                Fragment findFragmentByTag3 = getSupportFragmentManager().findFragmentByTag("AssistantPageFragment");
                FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
                if (findFragmentByTag != null) {
                    beginTransaction.remove(findFragmentByTag);
                }
                if (findFragmentByTag2 != null) {
                    beginTransaction.remove(findFragmentByTag2);
                }
                if (findFragmentByTag3 != null) {
                    beginTransaction.remove(findFragmentByTag3);
                }
                beginTransaction.commitAllowingStateLoss();
            }
            if (intent != null && intent.getData() != null && intent.getData().equals(GalleryContract.Common.URI_RECOMMEND_TAB)) {
                this.mIsEnterByShortcut = true;
                setCurrentItem(2, false);
                this.mCurrentPagePosition = 2;
            }
            this.mRootView = findViewById(16908290);
            GalleryPreferences.CTA.onCreateOrDestroyHomePage();
            this.mRequestPermissionTime = System.currentTimeMillis();
            checkPermission();
            GalleryPreferences.RequestUpdatePref.checkAppIsUpdate();
        } finally {
            Trace.endSection();
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.permission.core.PermissionCheckCallback
    public void onPermissionsChecked(Permission[] permissionArr, int[] iArr, boolean[] zArr) {
        DefaultLogger.d("HomePageActivity", "[Permission] onCreate -> onPermissionChecked consume %d", Long.valueOf(System.currentTimeMillis() - this.mRequestPermissionTime));
        if (!this.mHasStoragePermission) {
            boolean canAccessStorage = PermissionUtils.canAccessStorage(this, true);
            this.mHasStoragePermission = canAccessStorage;
            if (canAccessStorage) {
                if (this.mActionBarHelper.guideSetup()) {
                    this.mActionBarHelper.replaceGuideFragments();
                } else {
                    this.mActionBarHelper.setupHomePageFragment("onPermissionsChecked");
                }
            } else {
                this.mActionBarHelper.setupGuideFragment();
            }
        }
        super.onPermissionsChecked(permissionArr, iArr, zArr);
    }

    @Override // android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public void setTheme(int i) {
        if (BaseBuildUtil.isLargeScreenDevice()) {
            super.setTheme(2131952012);
        } else if (BaseScreenUtil.isFullScreenGestureNav(this)) {
            super.setTheme(2131952011);
        } else {
            super.setTheme(i);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onNewIntent(Intent intent) {
        DialogFragment dialogFragment;
        Dialog dialog;
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("extra_is_need_reset", false) && this.mActionBar != null) {
            ActionBarCompat.setExpandState(this, 0);
            setCurrentItem(0, false);
            for (int i = 0; i < this.mActionBarHelper.getFragmentTabCount(); i++) {
                Fragment fragmentAt = this.mActionBarHelper.getFragmentAt(i);
                if (fragmentAt instanceof OnHomePageResetListener) {
                    ((OnHomePageResetListener) fragmentAt).onHomePageReset();
                }
            }
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if ((fragment instanceof DialogFragment) && (dialog = (dialogFragment = (DialogFragment) fragment).getDialog()) != null && dialog.isShowing()) {
                    dialogFragment.dismissAllowingStateLoss();
                }
            }
            HomePageImmersionMenuHelper homePageImmersionMenuHelper = this.mImmersionMenuHelper;
            if (homePageImmersionMenuHelper != null) {
                homePageImmersionMenuHelper.dismissImmersionMenu();
            }
        }
        if (intent.getData() == null || !intent.getData().equals(GalleryContract.Common.URI_RECOMMEND_TAB)) {
            return;
        }
        setCurrentItem(2, false);
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, android.app.Activity
    public void onMultiWindowModeChanged(boolean z, Configuration configuration) {
        super.onMultiWindowModeChanged(z, configuration);
        HomePageImmersionMenuHelper homePageImmersionMenuHelper = this.mImmersionMenuHelper;
        if (homePageImmersionMenuHelper != null) {
            homePageImmersionMenuHelper.dismissImmersionMenu();
        }
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ViewGroup actionTabContainerView = this.mActionBarHelper.getActionTabContainerView();
        if (actionTabContainerView != null) {
            actionTabContainerView.post(new Runnable() { // from class: com.miui.gallery.activity.HomePageActivity$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    HomePageActivity.$r8$lambda$TNWQ9jeIuDApe1R9sIdLQTxrrxQ(HomePageActivity.this);
                }
            });
        }
        this.mActionBarHelper.refreshTabCustomViewMargin();
        int layoutDirection = configuration.getLayoutDirection();
        IFloatingButton iFloatingButton = this.mFloatingButton;
        if (iFloatingButton != null) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iFloatingButton.getView().getLayoutParams();
            if (layoutDirection == 0) {
                layoutParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.album_fab_create_album_margin_end);
            } else {
                layoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.album_fab_create_album_margin_end);
            }
            layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.album_fab_create_album_margin_bottom);
        }
        View moreView = this.mActionBarHelper.getMoreView();
        if (moreView != null) {
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) moreView.getLayoutParams();
            if (layoutDirection == 0) {
                layoutParams2.rightMargin = getResources().getDimensionPixelSize(R.dimen.action_bar_home_margin_end);
            } else {
                layoutParams2.leftMargin = getResources().getDimensionPixelSize(R.dimen.action_bar_home_margin_end);
            }
        }
        GuidePopupWindow guidePopupWindow = this.mPopupWindow;
        if (guidePopupWindow != null) {
            guidePopupWindow.dismiss();
        }
        this.mActionBarHelper.refreshMapView(false);
        HomePageImmersionMenuHelper homePageImmersionMenuHelper = this.mImmersionMenuHelper;
        if (homePageImmersionMenuHelper != null) {
            homePageImmersionMenuHelper.onConfigurationChange(configuration);
        }
    }

    public final void updateContentPadding() {
        int actionBarHeight = (!BaseBuildUtil.isLargeScreenDevice() || !BaseBuildUtil.isLargeHorizontalWindow()) ? this.mActionBarHelper.getActionBarHeight() : 0;
        if (this.mContentTopPadding != actionBarHeight) {
            this.mRootView.setPadding(0, actionBarHeight, 0, 0);
            this.mContentTopPadding = actionBarHeight;
        }
    }

    @Override // com.miui.gallery.activity.HomePageStartupHelper2.Attacher
    public void onStartup() {
        if (isDestroyed()) {
            return;
        }
        Trace.beginSection("HomePageAct#onStartup");
        attachFloatButton();
        this.mIsInStartup = true;
        this.mActionBarHelper.setupOtherFragments();
        adjustViewPagerTouchSlop();
        bindMTCameraRemoteService();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        RiskController.notify(this);
        GalleryMiCloudUtil.sendMiCloudBroadcast(getApplicationContext());
        Trace.endSection();
    }

    public final void markAlbumPageVisible() {
        if (1 < this.mActionBarHelper.getFragmentTabCount()) {
            Fragment fragmentAt = this.mActionBarHelper.getFragmentAt(1);
            if (!(fragmentAt instanceof AlbumTabFragment)) {
                return;
            }
            fragmentAt.setUserVisibleHint(true);
        }
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onResume(LifecycleOwner lifecycleOwner) {
        if (lifecycleOwner != ProcessLifecycleOwner.get() || !getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            return;
        }
        DefaultLogger.d("HomePageActivity", "onProcessResumed");
        dispatchAppFocused();
    }

    public final void attachFloatButton() {
        detachFloatButton();
        IFloatingButton iFloatingButton = (IFloatingButton) getLayoutInflater().inflate(R.layout.activity_home_page_floating_button, (ViewGroup) findViewById(16908290)).findViewById(R.id.create_album);
        this.mFloatingButton = iFloatingButton;
        if (iFloatingButton == null || iFloatingButton.getView() == null) {
            return;
        }
        this.mFloatingButton.getView().setAlpha(0.0f);
        this.mFloatingButton.getView().setVisibility(8);
    }

    public final void detachFloatButton() {
        IFloatingButton iFloatingButton = this.mFloatingButton;
        if (iFloatingButton == null || iFloatingButton.getView() == null) {
            return;
        }
        View view = this.mFloatingButton.getView();
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
        this.mFloatingButton = null;
    }

    public void dispatchAppFocused() {
        Fragment fragmentAt = this.mActionBarHelper.getFragmentAt(this.mCurrentPagePosition);
        if (fragmentAt instanceof OnAppFocusedListener) {
            ((OnAppFocusedListener) fragmentAt).onAppFocused();
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        HomeTabActionBarHelper.ViewPagerChangeListener viewPagerChangeListener;
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
        super.onDestroy();
        detachFloatButton();
        this.mHomePageStartupHelper2.onActivityDestroy();
        HomePageImmersionMenuHelper homePageImmersionMenuHelper = this.mImmersionMenuHelper;
        if (homePageImmersionMenuHelper != null) {
            homePageImmersionMenuHelper.onActivityDestroy();
            this.mImmersionMenuHelper.unregisterMenuItemsCheckListener();
        }
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null && (viewPagerChangeListener = this.mPagerChangeListener) != null) {
            actionBar.removeOnFragmentViewPagerChangeListener(viewPagerChangeListener);
            this.mPagerChangeListener = null;
        }
        GuidePopupWindow guidePopupWindow = this.mPopupWindow;
        if (guidePopupWindow != null && guidePopupWindow.isShowing()) {
            this.mPopupWindow.dismiss();
        }
        GalleryPreferences.CTA.onCreateOrDestroyHomePage();
        unbindMTCameraRemoteService();
        RequestUpdateHelper.getInstance().unregisterOnRequestUpdateFinishListener(this.mOnRequestUpdateFinishListener);
        RequestUpdateHelper.getInstance().release();
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        SamplingStatHelper.recordPageEnd(this, getPageName(this.mCurrentPagePosition));
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public void onCtaChecked(boolean z, boolean z2) {
        ActionBar actionBar = this.mActionBar;
        if (actionBar == null || actionBar.getFragmentTabCount() <= 0) {
            return;
        }
        Fragment fragmentAt = this.mActionBar.getFragmentAt(0);
        if (!(fragmentAt instanceof HomePageFragment) || requestUpdate(z2)) {
            return;
        }
        ((HomePageFragment) fragmentAt).showHomePageTips(z, z2);
    }

    public final boolean requestUpdate(boolean z) {
        if (RequestUpdateHelper.getInstance().isRequestUpdateEnable() && !z && BaseNetworkUtils.isNetworkConnected()) {
            if (GalleryPreferences.RequestUpdatePref.isAlreadyRequestToday() && GalleryPreferences.RequestUpdatePref.isForceUpdateFinish()) {
                return false;
            }
            this.mOnRequestUpdateFinishListener = new OnRequestUpdateFinishListener();
            RequestUpdateHelper requestUpdateHelper = RequestUpdateHelper.getInstance();
            requestUpdateHelper.registerOnRequestUpdateFinishListener(this.mOnRequestUpdateFinishListener);
            requestUpdateHelper.requestUpdate(this, 1);
            return true;
        }
        return false;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        UpdateDialogFragment updateDialogFragment;
        UpdateDialogFragment updateDialogFragment2;
        super.onResume();
        SamplingStatHelper.recordPageStart(this, getPageName(this.mCurrentPagePosition));
        if (!this.mHasStoragePermission && this.mGuideFollowed) {
            if (PermissionUtils.canAccessStorage(this, true)) {
                this.mHasStoragePermission = true;
                this.mActionBarHelper.replaceGuideFragments();
                DefaultLogger.d("HomePageActivity", "[Permission] after permission granted, replace fragment");
            } else {
                checkPermission();
            }
            this.mGuideFollowed = false;
        }
        if (!this.mIsDialogShown && (updateDialogFragment2 = this.mUpdateDialog) != null) {
            if (updateDialogFragment2.isAdded()) {
                getSupportFragmentManager().beginTransaction().show(this.mUpdateDialog).commitAllowingStateLoss();
            } else {
                this.mUpdateDialog.show(getSupportFragmentManager(), "HomePageActivity");
            }
            this.mIsDialogShown = true;
            if (this.mUpdateStatus == 7) {
                TrackController.trackExpose("403.64.0.1.16064", AutoTracking.getRef());
            } else {
                TrackController.trackExpose("403.63.0.1.16060");
            }
        } else if (GalleryPreferences.RequestUpdatePref.isForceUpdateFinish() || (updateDialogFragment = this.mUpdateDialog) == null) {
        } else {
            updateDialogFragment.addOnDialogButtonClickListener(this.mOnRequestUpdateFinishListener);
            if (this.mUpdateDialog.isAdded()) {
                getSupportFragmentManager().beginTransaction().show(this.mUpdateDialog).commitAllowingStateLoss();
            } else {
                this.mUpdateDialog.show(getSupportFragmentManager(), "HomePageActivity");
            }
        }
    }

    public void setImmersionMenuAnchor(View view) {
        this.mImmersionMenuAnchorView = view;
    }

    @Override // com.miui.gallery.ui.ImmersionMenuHolder
    public View getImmersionMenuAnchor() {
        return this.mImmersionMenuAnchorView;
    }

    public final void adjustViewPagerTouchSlop() {
        ViewPager viewPager = this.VIEW_PAGER.get(this);
        if (viewPager != null) {
            ReflectUtils.setField("androidx.viewpager.widget.OriginalViewPager", viewPager, "mTouchSlop", Integer.valueOf((int) (ViewConfiguration.get(this).getScaledPagingTouchSlop() * 1.5f)));
            ReflectUtils.setField("androidx.viewpager.widget.OriginalViewPager", viewPager, "mFlingDistance", Integer.valueOf((int) (getResources().getDisplayMetrics().density * 35.0f)));
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 61) {
            ActionBar actionBar = this.mActionBar;
            if (actionBar == null || actionBar.getFragmentTabCount() <= 0) {
                return;
            }
            Fragment fragmentAt = this.mActionBar.getFragmentAt(0);
            if (!(fragmentAt instanceof HomePageFragment)) {
                return;
            }
            ((HomePageFragment) fragmentAt).showHomePageTips(false, false);
            return;
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override // com.miui.gallery.activity.HomePageStartupHelper2.Attacher
    public HomePageStartupHelper2 getStartupHelper() {
        return this.mHomePageStartupHelper2;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        Fragment fragmentAt = this.mActionBar.getFragmentAt(this.mCurrentPagePosition);
        if (fragmentAt instanceof HomePageFragment) {
            ((HomePageFragment) fragmentAt).onProvideKeyboardShortcuts(list, menu, i);
        } else if (fragmentAt instanceof AlbumTabFragment) {
            ((AlbumTabFragment) fragmentAt).onProvideKeyboardShortcuts(list, menu, i);
        }
        super.onProvideKeyboardShortcuts(list, menu, i);
    }

    @Override // android.app.Activity
    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        Fragment fragmentAt = this.mActionBar.getFragmentAt(this.mCurrentPagePosition);
        if (!(fragmentAt instanceof HomePageFragment) || !((HomePageFragment) fragmentAt).onKeyShortcut(i, keyEvent)) {
            if ((fragmentAt instanceof AlbumTabFragment) && ((AlbumTabFragment) fragmentAt).onKeyShortcut(i, keyEvent)) {
                return true;
            }
            return super.onKeyShortcut(i, keyEvent);
        }
        return true;
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity, android.view.Window.Callback
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return super.onWindowStartingActionMode(new CallbackWrapper(callback));
    }

    public void changeCreateAlbumButtonVisibleStatus(boolean z) {
        if (this.mFloatingButton == null) {
            return;
        }
        if (z) {
            AlbumPageConfig.AlbumTabPage.getCurrentAlbumConfig().startAppearAnim(this.mFloatingButton.getView());
            Folme.useAt(this.mFloatingButton.getView()).hover().setEffect(IHoverStyle.HoverEffect.FLOATED_WRAPPED).handleHoverOf(this.mFloatingButton.getView(), new AnimConfig[0]);
            return;
        }
        AlbumPageConfig.AlbumTabPage.getCurrentAlbumConfig().startDisappearAnim(this.mFloatingButton.getView());
    }

    @Override // com.miui.gallery.ui.album.callback.OnAlbumCreateHandler
    public void handleAlbumCreated(long j, String str, Bundle bundle) {
        Fragment fragmentAt = this.mActionBar.getFragmentAt(1);
        if (!(fragmentAt instanceof AlbumTabFragment) || !fragmentAt.isAdded()) {
            return;
        }
        ((AlbumTabFragment) fragmentAt).onCreateAlbumOperationDone(j, str, bundle);
    }

    /* loaded from: classes.dex */
    public class CallbackWrapper implements ActionMode.Callback {
        public ActionMode.Callback mWrapped;

        public CallbackWrapper(ActionMode.Callback callback) {
            HomePageActivity.this = r1;
            this.mWrapped = callback;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onCreateActionMode(actionMode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            HomePageActivity.this.setPagerDraggable(false);
            HomePageActivity.this.enableViewPagerSpring(false);
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            this.mWrapped.onDestroyActionMode(actionMode);
            HomePageActivity.this.setPagerDraggable(true);
            HomePageActivity.this.enableViewPagerSpring(true);
        }
    }

    public final void bindMTCameraRemoteService() {
        if (!Build.DEVICE.equalsIgnoreCase("vela")) {
            return;
        }
        ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.activity.HomePageActivity.2
            {
                HomePageActivity.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (HomePageActivity.this.mMTCameraServiceConnection != null) {
                    return;
                }
                HomePageActivity.this.mMTCameraServiceConnection = new ServiceConnection() { // from class: com.miui.gallery.activity.HomePageActivity.2.1
                    {
                        AnonymousClass2.this = this;
                    }

                    @Override // android.content.ServiceConnection
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        DefaultLogger.d("HomePageActivity", "CameraRemoteService connected");
                    }

                    @Override // android.content.ServiceConnection
                    public void onServiceDisconnected(ComponentName componentName) {
                        DefaultLogger.d("HomePageActivity", "CameraRemoteService disconnected");
                    }
                };
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.mlab.cam", "com.mtlab.service.CameraRemoteService"));
                try {
                    HomePageActivity homePageActivity = HomePageActivity.this;
                    homePageActivity.bindService(intent, homePageActivity.mMTCameraServiceConnection, 5);
                    DefaultLogger.d("HomePageActivity", "bind CameraRemoteService");
                } catch (Exception e) {
                    HomePageActivity.this.mMTCameraServiceConnection = null;
                    DefaultLogger.e("HomePageActivity", "bind CameraRemoteService failed", e);
                }
            }
        });
    }

    public final void unbindMTCameraRemoteService() {
        ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.activity.HomePageActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                HomePageActivity.$r8$lambda$JQj6tC9_TmcjbKbBAz1yOmk_tK8(HomePageActivity.this);
            }
        });
    }

    public /* synthetic */ void lambda$unbindMTCameraRemoteService$0() {
        ServiceConnection serviceConnection = this.mMTCameraServiceConnection;
        if (serviceConnection == null) {
            return;
        }
        try {
            unbindService(serviceConnection);
            this.mMTCameraServiceConnection = null;
            DefaultLogger.d("HomePageActivity", "unbind CameraRemoteService");
        } catch (Exception e) {
            DefaultLogger.e("HomePageActivity", "unbind CameraRemoteService failed", e);
        }
    }

    @Override // com.miui.gallery.activity.HomePageInteractionCallback
    public void setPagerDraggable(boolean z) {
        ViewPager viewPager = this.VIEW_PAGER.get(this);
        if (viewPager == null) {
            return;
        }
        viewPager.setDraggable(z);
    }

    @Override // com.miui.gallery.ui.StorageGuideCallback
    public void onGuideFollowed() {
        this.mGuideFollowed = true;
    }

    public final void enableViewPagerSpring(boolean z) {
        ViewPager viewPager = this.VIEW_PAGER.get(this);
        if (viewPager == null) {
            return;
        }
        ActionBarCompat.enableViewPagerSpring(viewPager, z);
    }

    @SuppressLint({"RestrictedApi"})
    public final void setCurrentItem(int i, boolean z) {
        ViewPager viewPager = this.VIEW_PAGER.get(this);
        if (viewPager == null) {
            return;
        }
        if (BaseMiscUtil.isRTLDirection()) {
            viewPager.setCurrentItem((viewPager.getChildCount() - 1) - i, z);
        } else {
            viewPager.setCurrentItem(i, z);
        }
    }

    public HomeTabActionBarHelper getHomePageActionBarHelper() {
        return this.mActionBarHelper;
    }

    /* loaded from: classes.dex */
    public class HomeTabActionBarHelper extends TabActionBarHelper {
        public ColorDrawable mActionBarBgNormalDrawable;
        public Drawable mActionTabContainerBgDrawable;
        public boolean mGuideSetup;
        public TransitionDrawable mMapDrawable;
        public boolean mMapShowAsAction;
        public View mMapView;
        public Drawable mMaskBgDrawable;
        public View mMaskView;
        public TransitionDrawable mMoreBgDrawable;
        public View mMoreView;
        public View mRedDotView;
        public TransitionDrawable mSearchDrawable;
        public View mSearchView;

        public static /* synthetic */ void $r8$lambda$1I08tLN84KJC3P7Yfc7a7LnDsPI(HomeTabActionBarHelper homeTabActionBarHelper, View view) {
            homeTabActionBarHelper.lambda$inflateCommonView$3(view);
        }

        public static /* synthetic */ void $r8$lambda$H81DjVVyVChizYxlGT_L3Q2dTO0(HomeTabActionBarHelper homeTabActionBarHelper) {
            homeTabActionBarHelper.lambda$setupGuideFragment$0();
        }

        public static /* synthetic */ void $r8$lambda$L0jn8RVJGTVTgQ46YkBfqZ8Qq80(HomePageActivity homePageActivity) {
            HomePageActivity.access$3400(homePageActivity);
        }

        /* renamed from: $r8$lambda$XNtwqsCzc8p2krP5FCepyE-bEdA */
        public static /* synthetic */ void m457$r8$lambda$XNtwqsCzc8p2krP5FCepyEbEdA(HomeTabActionBarHelper homeTabActionBarHelper, View view) {
            homeTabActionBarHelper.lambda$inflateCommonView$4(view);
        }

        public static /* synthetic */ void $r8$lambda$jctAMbWe12JDz9d48zspQd_ZEAY(HomeTabActionBarHelper homeTabActionBarHelper, boolean z) {
            homeTabActionBarHelper.lambda$refreshMapView$5(z);
        }

        public static /* synthetic */ void $r8$lambda$xEStOPRFVPM0EQQXkvzvwAj48BI(HomeTabActionBarHelper homeTabActionBarHelper, View view) {
            homeTabActionBarHelper.lambda$refreshMapView$6(view);
        }

        public static /* synthetic */ void $r8$lambda$zdzT8IrSS4jZLAjOvdWzznxLvKs(HomeTabActionBarHelper homeTabActionBarHelper) {
            homeTabActionBarHelper.configMapAlbumGuide();
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public HomeTabActionBarHelper(AppCompatActivity appCompatActivity) {
            super(appCompatActivity);
            HomePageActivity.this = r1;
        }

        public boolean guideSetup() {
            return this.mGuideSetup;
        }

        public void setupGuideFragment() {
            if (this.mActionBar != null && !this.mGuideSetup) {
                this.mGuideSetup = true;
                try {
                    Trace.beginSection("setupGuideFragment");
                    ActionBar.Tab newTab = this.mActionBar.newTab();
                    if (BaseBuildUtil.isLargeScreenDevice()) {
                        newTab.setCustomView(createTabCustomTextView(0, R.string.home_page_label));
                    }
                    this.mActionBar.addFragmentTab("HomeStub", newTab.setText(R.string.home_page_label), StorageGuideFragment.class, null, false);
                    ActionBar.Tab newTab2 = this.mActionBar.newTab();
                    if (BaseBuildUtil.isLargeScreenDevice()) {
                        newTab2.setCustomView(createTabCustomTextView(1, R.string.album_page_label));
                    }
                    this.mActionBar.addFragmentTab("AlbumStub", newTab2.setText(R.string.album_page_label), StorageGuideFragment.class, null, false);
                    if (MediaFeatureManager.isDeviceSupportStoryFunction()) {
                        ActionBar.Tab newTab3 = this.mActionBar.newTab();
                        if (BaseBuildUtil.isLargeScreenDevice()) {
                            newTab3.setCustomView(createTabCustomTextView(2, R.string.recommend_page_label));
                        }
                        this.mActionBar.addFragmentTab("StoryStub", newTab3.setText(R.string.recommend_page_label), StorageGuideFragment.class, null, false);
                    }
                    ViewGroup actionTabContainerView = getActionTabContainerView();
                    this.mActionTabContainerView = actionTabContainerView;
                    if (actionTabContainerView != null) {
                        actionTabContainerView.post(new Runnable() { // from class: com.miui.gallery.activity.HomePageActivity$HomeTabActionBarHelper$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                HomePageActivity.HomeTabActionBarHelper.$r8$lambda$H81DjVVyVChizYxlGT_L3Q2dTO0(HomePageActivity.HomeTabActionBarHelper.this);
                            }
                        });
                    }
                    this.mActionBar.addOnFragmentViewPagerChangeListener(new ActionBar.FragmentViewPagerChangeListener() { // from class: com.miui.gallery.activity.HomePageActivity.HomeTabActionBarHelper.1
                        @Override // miuix.appcompat.app.ActionBar.FragmentViewPagerChangeListener
                        public void onPageScrollStateChanged(int i) {
                        }

                        @Override // miuix.appcompat.app.ActionBar.FragmentViewPagerChangeListener
                        public void onPageScrolled(int i, float f, boolean z, boolean z2) {
                        }

                        {
                            HomeTabActionBarHelper.this = this;
                        }

                        @Override // miuix.appcompat.app.ActionBar.FragmentViewPagerChangeListener
                        public void onPageSelected(int i) {
                            HomeTabActionBarHelper.this.setCurrentPosition(i);
                            if (BaseBuildUtil.isLargeScreenDevice()) {
                                HomeTabActionBarHelper.this.changeCustomTextColor();
                            }
                        }
                    });
                } finally {
                    Trace.endSection();
                }
            }
        }

        public /* synthetic */ void lambda$setupGuideFragment$0() {
            HomePageActivity.this.updateContentPadding();
        }

        public void replaceGuideFragments() {
            miuix.appcompat.app.ActionBar actionBar = this.mActionBar;
            if (actionBar == null) {
                return;
            }
            actionBar.removeAllFragmentTab();
            setupHomePageFragment("replaceGuideFragments");
        }

        public void setupHomePageFragment(String str) {
            DefaultLogger.d("HomePageActivity", "[Permission] setupHomePageFragment from %s", str);
            setupHomePageFragment();
        }

        public void setupHomePageFragment() {
            if (this.mActionBar == null) {
                return;
            }
            try {
                Trace.beginSection("setupHomePageFragment");
                ActionBar.Tab newTab = this.mActionBar.newTab();
                if (BaseBuildUtil.isLargeScreenDevice()) {
                    newTab.setCustomView(createTabCustomTextView(0, R.string.home_page_label));
                }
                this.mActionBar.addFragmentTab("HomePageFragment", newTab.setText(R.string.home_page_label), HomePageFragment.class, null, false);
                ActionBar.Tab newTab2 = this.mActionBar.newTab();
                if (BaseBuildUtil.isLargeScreenDevice()) {
                    newTab2.setCustomView(createTabCustomTextView(1, R.string.album_page_label));
                }
                this.mActionBar.addFragmentTab("AlbumStub", newTab2.setText(R.string.album_page_label), Fragment.class, null, false);
                if (MediaFeatureManager.isDeviceSupportStoryFunction()) {
                    ActionBar.Tab newTab3 = this.mActionBar.newTab();
                    if (BaseBuildUtil.isLargeScreenDevice()) {
                        newTab3.setCustomView(createTabCustomTextView(2, R.string.recommend_page_label));
                    }
                    this.mActionBar.addFragmentTab("StoryStub", newTab3.setText(R.string.recommend_page_label), Fragment.class, null, false);
                }
                Fragment fragmentAt = this.mActionBar.getFragmentAt(0);
                if (fragmentAt instanceof HomePageFragment) {
                    setOnTabChangeListener((HomePageFragment) fragmentAt);
                }
                ViewGroup actionTabContainerView = getActionTabContainerView();
                this.mActionTabContainerView = actionTabContainerView;
                if (actionTabContainerView != null) {
                    final HomePageActivity homePageActivity = HomePageActivity.this;
                    actionTabContainerView.post(new Runnable() { // from class: com.miui.gallery.activity.HomePageActivity$HomeTabActionBarHelper$$ExternalSyntheticLambda7
                        @Override // java.lang.Runnable
                        public final void run() {
                            HomePageActivity.HomeTabActionBarHelper.$r8$lambda$L0jn8RVJGTVTgQ46YkBfqZ8Qq80(HomePageActivity.this);
                        }
                    });
                }
                this.mActionBar.addOnFragmentViewPagerChangeListener(getPagerChangeListener());
            } finally {
                Trace.endSection();
            }
        }

        public final ViewPagerChangeListener getPagerChangeListener() {
            if (HomePageActivity.this.mPagerChangeListener == null) {
                HomePageActivity.this.mPagerChangeListener = new ViewPagerChangeListener();
            }
            return HomePageActivity.this.mPagerChangeListener;
        }

        /* loaded from: classes.dex */
        public class ViewPagerChangeListener implements ActionBar.FragmentViewPagerChangeListener {
            public boolean mIsPendingSelectDispatched;
            public boolean mIsTriggeredByDragging;

            public ViewPagerChangeListener() {
                HomeTabActionBarHelper.this = r1;
                this.mIsPendingSelectDispatched = false;
                this.mIsTriggeredByDragging = false;
            }

            @Override // miuix.appcompat.app.ActionBar.FragmentViewPagerChangeListener
            public void onPageScrolled(int i, float f, boolean z, boolean z2) {
                if (HomePageActivity.this.mFragmentPagerScrollState == 1 && HomePageActivity.this.mFloatingButton != null) {
                    AlbumPageConfig.AlbumTabPage.getCurrentAlbumConfig().processDisappearState(HomePageActivity.this.mFloatingButton.getView(), f, i);
                }
                if (this.mIsPendingSelectDispatched) {
                    return;
                }
                if (i == HomePageActivity.this.mCurrentPagePosition && f >= 0.15f) {
                    int i2 = i + 1;
                    DefaultLogger.d("HomePageActivity", "pending select page: %d, curr ratio: %f", Integer.valueOf(i2), Float.valueOf(f));
                    if (i2 == 1) {
                        HomePageActivity.this.markAlbumPageVisible();
                    }
                    this.mIsPendingSelectDispatched = true;
                }
                if (i != HomePageActivity.this.mCurrentPagePosition - 1 || f > 0.8f) {
                    return;
                }
                DefaultLogger.d("HomePageActivity", "pending select page: %d, curr ratio: %f", Integer.valueOf(i), Float.valueOf(f));
                if (i == 1) {
                    HomePageActivity.this.markAlbumPageVisible();
                }
                this.mIsPendingSelectDispatched = true;
            }

            @Override // miuix.appcompat.app.ActionBar.FragmentViewPagerChangeListener
            public void onPageSelected(int i) {
                DefaultLogger.d("HomePageActivity", "onPageSelected %d", Integer.valueOf(i));
                if (HomePageActivity.this.mFloatingButton != null) {
                    if (i == 1) {
                        AlbumPageConfig.AlbumTabPage.getCurrentAlbumConfig().startAppearAnim(HomePageActivity.this.mFloatingButton.getView());
                        Folme.useAt(HomePageActivity.this.mFloatingButton.getView()).hover().setEffect(IHoverStyle.HoverEffect.FLOATED_WRAPPED).handleHoverOf(HomePageActivity.this.mFloatingButton.getView(), new AnimConfig[0]);
                    } else {
                        AlbumPageConfig.AlbumTabPage.getCurrentAlbumConfig().startDisappearAnim(HomePageActivity.this.mFloatingButton.getView());
                    }
                }
                if (HomePageActivity.this.mCurrentPagePosition != i) {
                    HomeTabActionBarHelper.this.setCurrentPosition(i);
                    if (HomeTabActionBarHelper.this.isLargeScreenAndWindow) {
                        if (i == 0 && HomePageActivity.this.mCurrentPagePosition != 0 && HomeTabActionBarHelper.this.isImmerseEnable) {
                            HomeTabActionBarHelper.this.showImmerseActionBar();
                        }
                        if (i != 0 && HomePageActivity.this.mCurrentPagePosition == 0 && HomeTabActionBarHelper.this.isShowImmerse) {
                            HomeTabActionBarHelper.this.showNormalActionBar();
                        }
                    }
                    if (BaseBuildUtil.isLargeScreenDevice()) {
                        HomeTabActionBarHelper.this.changeCustomTextColor();
                    }
                    HomePageActivity homePageActivity = HomePageActivity.this;
                    SamplingStatHelper.recordPageEnd(homePageActivity, homePageActivity.getPageName(homePageActivity.mCurrentPagePosition));
                    HomePageActivity homePageActivity2 = HomePageActivity.this;
                    SamplingStatHelper.recordPageStart(homePageActivity2, homePageActivity2.getPageName(i));
                }
                HomeTabActionBarHelper.this.updateActionBarMore();
                if (i == 2) {
                    DisplayStatusManager.setRedDotClicked("assistant_tab");
                    DisplayStatusManager.setRedDotClicked("story_album");
                }
                if (HomeTabActionBarHelper.this.mOnTabChangeListener != null) {
                    HomeTabActionBarHelper.this.mOnTabChangeListener.onTabChange(i);
                }
                if (!this.mIsTriggeredByDragging) {
                    HomeTabActionBarHelper homeTabActionBarHelper = HomeTabActionBarHelper.this;
                    homeTabActionBarHelper.trackSwitchTab(HomePageActivity.this.mCurrentPagePosition, i);
                }
                this.mIsTriggeredByDragging = false;
                HomeTabActionBarHelper.this.trackCurPage(i);
                HomeTabActionBarHelper.this.notifyFragmentVisibleChange(i);
                HomePageActivity.this.mCurrentPagePosition = i;
            }

            @Override // miuix.appcompat.app.ActionBar.FragmentViewPagerChangeListener
            public void onPageScrollStateChanged(int i) {
                DefaultLogger.d("HomePageActivity", "onPageScrollStateChanged state = %d", Integer.valueOf(i));
                if (i == 0) {
                    this.mIsPendingSelectDispatched = false;
                }
                if (i == 1) {
                    this.mIsTriggeredByDragging = true;
                }
                if (HomePageActivity.this.mFragmentPagerScrollState == 1 && i == 2 && HomePageActivity.this.mFloatingButton != null) {
                    if (HomePageActivity.this.mCurrentPagePosition == 1) {
                        AlbumPageConfig.AlbumTabPage.getCurrentAlbumConfig().startAppearAnim(HomePageActivity.this.mFloatingButton.getView());
                        Folme.useAt(HomePageActivity.this.mFloatingButton.getView()).hover().setEffect(IHoverStyle.HoverEffect.FLOATED_WRAPPED).handleHoverOf(HomePageActivity.this.mFloatingButton.getView(), new AnimConfig[0]);
                    } else {
                        AlbumPageConfig.AlbumTabPage.getCurrentAlbumConfig().startDisappearAnim(HomePageActivity.this.mFloatingButton.getView());
                    }
                }
                HomePageActivity.this.mFragmentPagerScrollState = i;
                HomeTabActionBarHelper.this.setupOtherFragments();
            }
        }

        public final void notifyFragmentVisibleChange(int i) {
            if (getFragmentAt(i) instanceof OnVisibilityChangeListener) {
                ((OnVisibilityChangeListener) getFragmentAt(i)).onVisibleChange(true);
            }
        }

        public final void trackCurPage(int i) {
            if (i == 0) {
                AutoTracking.trackNav("403.1.0.1.11542");
            } else if (i == 1) {
                AutoTracking.trackNav("403.7.0.1.11543");
            } else if (i != 2) {
            } else {
                AutoTracking.trackNav("403.8.0.1.11538");
            }
        }

        public final void trackSwitchTab(int i, int i2) {
            String str = "403.8.5.1.11209";
            if (i == 0) {
                if (i2 == 1) {
                    str = "403.1.7.1.9886";
                } else if (i2 != 2) {
                    return;
                } else {
                    str = "403.1.7.1.9887";
                }
            } else if (i == 1) {
                if (i2 == 0) {
                    str = "403.7.3.1.10340";
                } else if (i2 != 2) {
                    return;
                } else {
                    str = "403.7.3.1.10341";
                }
            } else if (i != 2) {
                return;
            } else {
                if (i2 != 0 && i2 != 1) {
                    return;
                }
            }
            TrackController.trackClick(str, AutoTracking.getRef());
        }

        public void setupOtherFragments() {
            if (!HomePageActivity.this.mIsInStartup || HomePageActivity.this.mFragmentPagerScrollState != 0) {
                return;
            }
            boolean z = false;
            HomePageActivity.this.mIsInStartup = false;
            int i = HomePageActivity.this.mCurrentPagePosition;
            if (!(this.mActionBar.getFragmentAt(1) instanceof AlbumTabFragment)) {
                ActionBar.Tab newTab = this.mActionBar.newTab();
                if (BaseBuildUtil.isLargeScreenDevice()) {
                    newTab.setCustomView(createTabCustomTextView(1, R.string.album_page_label));
                }
                this.mActionBar.removeFragmentTabAt(1);
                this.mActionBar.addFragmentTab("AlbumTabFragment", newTab.setText(R.string.album_page_label), 1, AlbumTabFragment.class, null, false);
            }
            Fragment fragmentAt = this.mActionBar.getFragmentAt(1);
            if ((fragmentAt instanceof AlbumTabFragment) && HomePageActivity.this.mFloatingButton != null) {
                HomePageActivity.this.mFloatingButton.setActionHandler((AlbumTabFragment) fragmentAt);
            }
            if (MediaFeatureManager.isDeviceSupportStoryFunction() && !(this.mActionBar.getFragmentAt(2) instanceof AssistantPageFragment)) {
                ActionBar.Tab newTab2 = this.mActionBar.newTab();
                if (BaseBuildUtil.isLargeScreenDevice()) {
                    newTab2.setCustomView(createTabCustomTextView(2, R.string.recommend_page_label));
                }
                this.mActionBar.removeFragmentTabAt(2);
                this.mActionBar.addFragmentTab("AssistantPageFragment", newTab2.setText(R.string.recommend_page_label), 2, AssistantPageFragment.class, null, false);
            }
            HomePageActivity.this.mActionBarHelper.refreshTabCustomViewMargin();
            if (HomePageActivity.this.mStartUpPage <= 0 || HomePageActivity.this.mStartUpPage >= this.mActionBar.getTabCount()) {
                if (HomePageActivity.this.mIsEnterByShortcut) {
                    miuix.appcompat.app.ActionBar actionBar = this.mActionBar;
                    actionBar.selectTab(actionBar.getTabAt(i), false);
                    HomePageActivity.this.setCurrentItem(i, false);
                    HomePageActivity.this.mIsEnterByShortcut = false;
                } else {
                    miuix.appcompat.app.ActionBar actionBar2 = this.mActionBar;
                    actionBar2.selectTab(actionBar2.getTabAt(i));
                }
                notifyFragmentVisibleChange(i);
                if (!ScreenUtils.isRtl(GalleryApp.sGetAndroidContext()) || i != 1) {
                    return;
                }
                try {
                    Fragment fragmentAt2 = HomePageActivity.this.mActionBarHelper.getFragmentAt(1);
                    if (fragmentAt2.isAdded() && !fragmentAt2.getUserVisibleHint()) {
                        z = true;
                    }
                    if (!z) {
                        return;
                    }
                    HomePageActivity.this.markAlbumPageVisible();
                    return;
                } catch (Exception e) {
                    DefaultLogger.e("HomePageActivity", e);
                    return;
                }
            }
            miuix.appcompat.app.ActionBar actionBar3 = this.mActionBar;
            actionBar3.selectTab(actionBar3.getTabAt(HomePageActivity.this.mStartUpPage), false);
            notifyFragmentVisibleChange(HomePageActivity.this.mStartUpPage);
        }

        public void setupActionBar() {
            if (this.mActionBar == null) {
                return;
            }
            HomePageActivity.this.addScreenChangeListener(new IScreenChange.OnLargeScreenChangeListener() { // from class: com.miui.gallery.activity.HomePageActivity.HomeTabActionBarHelper.2
                {
                    HomeTabActionBarHelper.this = this;
                }

                @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
                public void onCreatedWhileLargeDevice(ScreenSize screenSize) {
                    HomeTabActionBarHelper.this.initImmerseResources();
                    HomeTabActionBarHelper.this.inflateCommonView();
                    HomeTabActionBarHelper.this.inflateImmerseView();
                    HomeTabActionBarHelper.this.isLargeScreenAndWindow = screenSize.isWindowHorizontalLarge();
                    if (HomeTabActionBarHelper.this.isLargeScreenAndWindow) {
                        HomeTabActionBarHelper.this.refreshImmerseUI();
                    }
                }

                @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
                public void onCreatedWhileNormalDevice(ScreenSize screenSize) {
                    HomeTabActionBarHelper.this.inflateCommonView();
                }

                @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
                public void onScreenSizeToLargeOrNormal(ScreenSize screenSize) {
                    HomeTabActionBarHelper.this.isLargeScreenAndWindow = screenSize.isWindowHorizontalLarge();
                    if (HomeTabActionBarHelper.this.isLargeScreenAndWindow) {
                        HomeTabActionBarHelper.this.refreshImmerseUI();
                    } else {
                        HomeTabActionBarHelper.this.refreshNormalUI();
                    }
                }
            });
        }

        public final void initImmerseResources() {
            this.mActionBarBgNormalDrawable = new ColorDrawable(this.mContext.getColor(R.color.action_bar_background));
            this.mActionTabContainerBgDrawable = this.mContext.getDrawable(R.drawable.action_bar_container_tab_bg);
            AnimDrawableWrapper animDrawableWrapper = new AnimDrawableWrapper(this.mActionTabContainerBgDrawable, AnimDrawableWrapper.mDefaultAnimDrawable);
            this.mActionTabContainerBgDrawable.setAlpha(0);
            addActionBarTabContainerBg(animDrawableWrapper);
            addImmerseAnimDrawable(animDrawableWrapper);
            if (Build.VERSION.SDK_INT >= 16) {
                this.mMaskBgDrawable = GradientUtil.makeCubicGradientScrimDrawable(HomePageActivity.this.getResources().getColor(R.color.action_bar_immerse_background), 8, 48);
            } else {
                GradientDrawable gradientDrawable = new GradientDrawable();
                this.mMaskBgDrawable = gradientDrawable;
                gradientDrawable.setColors(new int[]{HomePageActivity.this.getResources().getColor(R.color.action_bar_immerse_background), HomePageActivity.this.getResources().getColor(R.color.transparent)});
            }
            AnimDrawableWrapper animDrawableWrapper2 = new AnimDrawableWrapper(this.mMaskBgDrawable, AnimDrawableWrapper.mDefaultAnimDrawable);
            this.mMaskBgDrawable.setAlpha(0);
            addImmerseAnimDrawable(animDrawableWrapper2);
            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{this.mContext.getDrawable(R.drawable.home_more), this.mContext.getDrawable(R.drawable.home_more_immerse)});
            this.mMoreBgDrawable = transitionDrawable;
            putNormalToImmerseDrawable(0, transitionDrawable);
            TransitionDrawable transitionDrawable2 = new TransitionDrawable(new Drawable[]{this.mContext.getDrawable(R.drawable.home_search), this.mContext.getDrawable(R.drawable.home_search_immerse)});
            this.mSearchDrawable = transitionDrawable2;
            putNormalToImmerseDrawable(1, transitionDrawable2);
            TransitionDrawable transitionDrawable3 = new TransitionDrawable(new Drawable[]{this.mContext.getDrawable(R.drawable.ic_map_entrance_in_home_page), this.mContext.getDrawable(R.drawable.ic_map_entrance_in_home_page_immerse)});
            this.mMapDrawable = transitionDrawable3;
            putNormalToImmerseDrawable(2, transitionDrawable3);
        }

        public final void inflateCommonView() {
            View customEndViewOnly = ActionBarCompat.setCustomEndViewOnly(this.mContext, R.layout.action_bar_more);
            this.mCustomEndView = customEndViewOnly;
            this.mMoreView = customEndViewOnly.findViewById(R.id.more);
            refreshMapView(true);
            this.mRedDotView = this.mCustomEndView.findViewById(R.id.red_dot);
            HomePageActivity.this.mImmersionMenuHelper = new HomePageImmersionMenuHelper(this.mContext);
            HomePageActivity.this.mImmersionMenuHelper.registerMenuItemsCheckListener(new HomePageImmersionMenuHelper.MenuItemsCheckListener() { // from class: com.miui.gallery.activity.HomePageActivity$HomeTabActionBarHelper$$ExternalSyntheticLambda3
                @Override // com.miui.gallery.activity.HomePageImmersionMenuHelper.MenuItemsCheckListener
                public final void onMenuItemsChecked(boolean z) {
                    HomePageActivity.HomeTabActionBarHelper.this.lambda$inflateCommonView$2(z);
                }
            });
            HomePageActivity.this.mImmersionMenuHelper.checkRedDotFeature();
            HomePageActivity.this.setImmersionMenuAnchor(this.mCustomEndView);
            this.mMoreView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.activity.HomePageActivity$HomeTabActionBarHelper$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    HomePageActivity.HomeTabActionBarHelper.$r8$lambda$1I08tLN84KJC3P7Yfc7a7LnDsPI(HomePageActivity.HomeTabActionBarHelper.this, view);
                }
            });
            View customStartViewOnly = ActionBarCompat.setCustomStartViewOnly(this.mContext, R.layout.action_bar_start);
            this.mCustomStartView = customStartViewOnly;
            View inflate = ((ViewStub) customStartViewOnly.findViewById(R.id.search)).inflate();
            this.mSearchView = inflate;
            inflate.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.activity.HomePageActivity$HomeTabActionBarHelper$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    HomePageActivity.HomeTabActionBarHelper.m457$r8$lambda$XNtwqsCzc8p2krP5FCepyEbEdA(HomePageActivity.HomeTabActionBarHelper.this, view);
                }
            });
            updateActionBarMore();
        }

        public /* synthetic */ void lambda$inflateCommonView$2(boolean z) {
            View view = this.mRedDotView;
            if (view != null) {
                view.setVisibility(z ? 0 : 4);
            }
        }

        public /* synthetic */ void lambda$inflateCommonView$3(View view) {
            Fragment fragmentAt = getFragmentAt(HomePageActivity.this.mCurrentPagePosition);
            if (fragmentAt instanceof ImmersionMenuSupport) {
                HomePageActivity.this.mImmersionMenuHelper.showImmersionMenu(view, (ImmersionMenuSupport) fragmentAt);
            }
        }

        public /* synthetic */ void lambda$inflateCommonView$4(View view) {
            TimeMonitor.createNewTimeMonitor("403.20.0.1.14019");
            Bundle bundle = new Bundle(1);
            if (HomePageActivity.this.mCurrentPagePosition == 0) {
                bundle.putString("from", "from_home_page");
            } else {
                bundle.putString("from", "from_album_page");
            }
            ActionURIHandler.handleUri(this.mContext, GalleryContract.Search.URI_SEARCH_PAGE.buildUpon().build(), bundle);
        }

        public final void inflateImmerseView() {
            this.mMoreView.setBackground(this.mMoreBgDrawable);
            this.mSearchView.setBackground(this.mSearchDrawable);
            View view = this.mMapView;
            if (view != null) {
                view.setBackground(this.mMapDrawable);
            }
            this.mMaskView = new View(this.mContext);
            this.mMaskView.setLayoutParams(new FrameLayout.LayoutParams(-1, HomePageActivity.this.getResources().getDimensionPixelSize(R.dimen.immersive_header_decoration_height)));
            ((FrameLayout) HomePageActivity.this.findViewById(16908290)).addView(this.mMaskView);
            this.mMaskView.setBackground(this.mMaskBgDrawable);
        }

        public final void refreshMapView(boolean z) {
            if (this.mCustomEndView == null) {
                return;
            }
            boolean z2 = HomePageActivity.this.getResources().getBoolean(R.bool.map_entrance_show_as_action);
            this.mMapShowAsAction = z2;
            if (z2) {
                if (this.mMapView == null) {
                    View inflate = ((ViewStub) this.mCustomEndView.findViewById(R.id.map_view)).inflate();
                    this.mMapView = inflate;
                    inflate.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.activity.HomePageActivity$HomeTabActionBarHelper$$ExternalSyntheticLambda2
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            HomePageActivity.HomeTabActionBarHelper.$r8$lambda$xEStOPRFVPM0EQQXkvzvwAj48BI(HomePageActivity.HomeTabActionBarHelper.this, view);
                        }
                    });
                }
                this.mMapView.setVisibility(0);
                if (!z) {
                    return;
                }
                this.mMapView.post(new Runnable() { // from class: com.miui.gallery.activity.HomePageActivity$HomeTabActionBarHelper$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        HomePageActivity.HomeTabActionBarHelper.$r8$lambda$zdzT8IrSS4jZLAjOvdWzznxLvKs(HomePageActivity.HomeTabActionBarHelper.this);
                    }
                });
                return;
            }
            View view = this.mMapView;
            if (view != null) {
                view.setVisibility(8);
            }
            if (!z) {
                return;
            }
            this.mMoreView.post(new Runnable() { // from class: com.miui.gallery.activity.HomePageActivity$HomeTabActionBarHelper$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    HomePageActivity.HomeTabActionBarHelper.$r8$lambda$zdzT8IrSS4jZLAjOvdWzznxLvKs(HomePageActivity.HomeTabActionBarHelper.this);
                }
            });
        }

        public /* synthetic */ void lambda$refreshMapView$6(View view) {
            if (AgreementsUtils.isNetworkingAgreementAccepted()) {
                IntentUtil.goToMapAlbumDirectly(this.mContext);
            } else {
                AgreementsUtils.showUserAgreements(this.mContext, new OnAgreementInvokedListener() { // from class: com.miui.gallery.activity.HomePageActivity$HomeTabActionBarHelper$$ExternalSyntheticLambda4
                    @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                    public final void onAgreementInvoked(boolean z) {
                        HomePageActivity.HomeTabActionBarHelper.$r8$lambda$jctAMbWe12JDz9d48zspQd_ZEAY(HomePageActivity.HomeTabActionBarHelper.this, z);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$refreshMapView$5(boolean z) {
            if (z) {
                IntentUtil.goToMapAlbumDirectly(this.mContext);
            }
        }

        public final void configMapAlbumGuide() {
            if (this.mContext == null || HomePageActivity.this.isFinishing() || HomePageActivity.this.isDestroyed() || !MapInitializerImpl.checkMapAvailable() || GalleryPreferences.MapAlbum.isNoticeShowed()) {
                return;
            }
            HomePageActivity.this.mPopupWindow = new GuidePopupWindow(this.mContext);
            HomePageActivity.this.mPopupWindow.setArrowMode(8);
            HomePageActivity.this.mPopupWindow.setGuideText(R.string.map_guide_message);
            HomePageActivity.this.mPopupWindow.show(this.mMapShowAsAction ? this.mMapView : this.mMoreView, false);
            GalleryPreferences.MapAlbum.setNoticeShowed();
        }

        public void refreshImmerseUI() {
            if (this.mCustomEndView == null) {
                return;
            }
            if (HomePageActivity.this.mCurrentPagePosition == 0 && this.isImmerseEnable) {
                this.mActionBar.setBackgroundDrawable(null);
                changeCustomTextColor();
                this.mSearchDrawable.startTransition(0);
                this.mMoreBgDrawable.startTransition(0);
                this.mMapDrawable.startTransition(0);
                this.mActionTabContainerBgDrawable.setAlpha(255);
                this.mMaskBgDrawable.setAlpha(255);
                this.isShowImmerse = true;
            }
            View view = this.mMaskView;
            if (view == null) {
                return;
            }
            view.setVisibility(0);
        }

        public void refreshNormalUI() {
            if (this.mCustomEndView == null) {
                return;
            }
            this.mActionBar.setBackgroundDrawable(this.mActionBarBgNormalDrawable);
            if (HomePageActivity.this.mCurrentPagePosition == 0 && this.isShowImmerse) {
                this.mMoreBgDrawable.resetTransition();
                this.mSearchDrawable.resetTransition();
                this.mMapDrawable.resetTransition();
                this.mActionTabContainerBgDrawable.setAlpha(0);
                this.mMaskBgDrawable.setAlpha(0);
                changeCustomTextColor();
                this.isShowImmerse = false;
            }
            this.mMaskView.setVisibility(8);
        }

        public final void updateActionBarMore() {
            if (this.mActionBar == null) {
                return;
            }
            if (HomePageActivity.this.mImmersionMenuHelper != null && DisplayStatusManager.isRedDotEnabled()) {
                HomePageActivity.this.mImmersionMenuHelper.checkRedDotFeature();
            }
            this.mActionBar.setDisplayShowCustomEnabled(true);
        }

        public View getStartView() {
            return this.mCustomStartView;
        }

        public View getSearchView() {
            return this.mSearchView;
        }

        public View getMoreView() {
            return this.mMoreView;
        }
    }

    /* loaded from: classes.dex */
    public class OnRequestUpdateFinishListener extends OnRequestUpdateFinishAdapter {
        public OnRequestUpdateFinishListener() {
            HomePageActivity.this = r1;
        }

        @Override // com.miui.gallery.util.RequestUpdateHelper.OnRequestUpdateFinishListener
        public void onDialogCreate(int i, int i2, UpdateDialogFragment updateDialogFragment) {
            if (i2 == 1) {
                HomePageActivity.this.mUpdateDialog = updateDialogFragment;
                HomePageActivity.this.mUpdateStatus = i;
                updateDialogFragment.addOnDialogButtonClickListener(HomePageActivity.this.mOnRequestUpdateFinishListener);
                GalleryPreferences.RequestUpdatePref.setLastRequestDate(System.currentTimeMillis());
                GalleryPreferences.RequestUpdatePref.setIsNeedHint(true);
                if (!HomePageActivity.this.resumed()) {
                    HomePageActivity.this.mIsDialogShown = false;
                    return;
                }
                updateDialogFragment.show(HomePageActivity.this.getSupportFragmentManager(), "HomePageActivity");
                HomePageActivity.this.mIsDialogShown = true;
                if (i == 7) {
                    TrackController.trackExpose("403.64.0.1.16064", AutoTracking.getRef());
                    return;
                }
                GalleryPreferences.RequestUpdatePref.setIsForceUpdateFinish(false);
                TrackController.trackExpose("403.63.0.1.16060");
            }
        }

        @Override // com.miui.gallery.util.RequestUpdateHelper.OnRequestUpdateFinishListener
        public void onFailure(int i) {
            GalleryPreferences.RequestUpdatePref.setLastRequestDate(System.currentTimeMillis());
        }

        @Override // com.miui.gallery.util.OnRequestUpdateFinishAdapter, com.miui.gallery.util.RequestUpdateHelper.OnRequestUpdateFinishListener
        public void onRedDotShow() {
            DisplayStatusManager.updateFeature("settings");
            GalleryPreferences.RequestUpdatePref.saveRedDotShowData();
            HomePageActivity.this.mImmersionMenuHelper.checkRedDotFeature();
        }

        @Override // com.miui.gallery.util.OnRequestUpdateFinishAdapter, com.miui.gallery.util.RequestUpdateHelper.OnRequestUpdateFinishListener
        public void onNotUpdate() {
            GalleryPreferences.RequestUpdatePref.setLastRequestDate(System.currentTimeMillis());
            GalleryPreferences.RequestUpdatePref.setIsNeedHint(false);
        }

        @Override // com.miui.gallery.ui.UpdateDialogFragment.OnDialogButtonClickListener
        public void onDelayClick(boolean z, int i) {
            if (HomePageActivity.this.resumed()) {
                GalleryPreferences.RequestUpdatePref.saveDelayData(z, i);
                if (!z) {
                    DisplayStatusManager.updateFeature("settings");
                    HomePageActivity.this.mImmersionMenuHelper.checkRedDotFeature();
                }
                TrackController.trackClick("403.64.0.1.16066", AutoTracking.getRef(), z ? "yes" : "no");
            }
        }

        @Override // com.miui.gallery.ui.UpdateDialogFragment.OnDialogButtonClickListener
        public void onUpdateClick(int i) {
            if (HomePageActivity.this.resumed()) {
                GalleryPreferences.RequestUpdatePref.setIsDelayUpdate(false);
                GalleryPreferences.RequestUpdatePref.setIsIgnoreUpdate(false);
                if (i == 7) {
                    TrackController.trackClick("403.64.0.1.16065");
                } else if (i != 8) {
                } else {
                    TrackController.trackClick("403.63.0.1.16061");
                }
            }
        }

        @Override // com.miui.gallery.util.OnRequestUpdateFinishAdapter, com.miui.gallery.ui.UpdateDialogFragment.OnDialogButtonClickListener
        public void onBackClick() {
            TrackController.trackClick("403.63.0.1.16063");
            HomePageActivity.this.finish();
        }
    }
}
