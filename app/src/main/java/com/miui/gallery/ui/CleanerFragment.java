package com.miui.gallery.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cleaner.ScanResult;
import com.miui.gallery.cleaner.ScanResultAdapter;
import com.miui.gallery.cleaner.ScannerManager;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.CleanerFragment;
import com.miui.gallery.ui.actionBar.BaseCommonActionBarHelper;
import com.miui.gallery.ui.actionBar.SimpleThemeActionBarHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.DividerItemDecoration;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryPullZoomLayout;
import com.miui.gallery.widget.recyclerview.CleanerItemAnimator;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;
import java.util.List;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;
import miuix.view.animation.CubicEaseInOutInterpolator;

/* loaded from: classes2.dex */
public class CleanerFragment extends BaseFragment {
    public boolean isFreeFromWindow;
    public boolean isPhoneSplitScreen;
    public SimpleThemeActionBarHelper mActionBarHelper;
    public ActionLayout mActionLayout;
    public ScanResultAdapter mAdapter;
    public int mBtnBottomMarginExpand;
    public long mCleanableCount;
    public View mCornerView;
    public Dialog mExitConfirmDialog;
    public int mExpandHeight;
    public CleanerRotateProgressBar mProgressBar;
    public GalleryRecyclerView mRecyclerView;
    public String mRefTip;
    public int mRetractHeight;
    public View mRootView;
    public ScanLayout mScanLayout;
    public ScannerManager.ScanObserver mScanObserver = new ScannerManager.ScanObserver() { // from class: com.miui.gallery.ui.CleanerFragment.4
        public long startTime = 0;

        {
            CleanerFragment.this = this;
        }

        @Override // com.miui.gallery.cleaner.ScannerManager.ScanObserver
        public void onScanStart() {
            this.startTime = System.currentTimeMillis();
            CleanerFragment.this.mActionLayout.show(false);
            CleanerFragment.this.mScanLayout.expand(false);
            CleanerFragment.this.mProgressBar.showRingIcon(false);
            CleanerFragment.this.mProgressBar.setNumber(0L, true);
            CleanerFragment.this.setTranslucentStatus(1);
        }

        @Override // com.miui.gallery.cleaner.ScannerManager.ScanObserver
        public void onScanCanceled() {
            CleanerFragment.this.setEnable();
        }

        @Override // com.miui.gallery.cleaner.ScannerManager.ScanObserver
        public void onScanProgress(long j) {
            CleanerFragment.this.mProgressBar.setNumber(j, true);
        }

        @Override // com.miui.gallery.cleaner.ScannerManager.ScanObserver
        public void onScanFinish(long j) {
            HashMap hashMap = new HashMap();
            hashMap.put("ref_tip", CleanerFragment.this.mRefTip == null ? AutoTracking.getRef() : CleanerFragment.this.mRefTip);
            hashMap.put("tip", "403.27.7.1.16825");
            hashMap.put(MiStat.Param.COUNT, Long.valueOf((System.currentTimeMillis() - this.startTime) / 1000));
            hashMap.put("count_extra", Long.valueOf(StorageUtils.getAvailableBytes(StorageUtils.getPrimaryStoragePath()) / 1000000));
            hashMap.put("status", Integer.valueOf(FormatUtil.getDeviceSpaceRange(StorageUtils.getTotalBytes(StorageUtils.getPrimaryStoragePath()) / 1000000000)));
            AutoTracking.trackView(hashMap);
            if (CleanerFragment.this.mScannerManager.isScanResultEmpty()) {
                CleanerFragment.this.setScannedBackgroundStatus();
                CleanerFragment.this.mRecyclerView.setNestedScrollingEnabled(false);
                CleanerFragment.this.mScrollingLayout.setEnabled(false);
                CleanerFragment.this.mScanLayout.expand(true);
                CleanerFragment.this.mProgressBar.showEmptyAnim();
                CleanerFragment.this.mActionLayout.showNoScanResultTip();
                CleanerFragment.this.mCleanableCount = 0L;
                CleanerFragment.this.setDisable();
                return;
            }
            CleanerFragment.this.updateScanResult(true);
            CleanerFragment.this.mProgressBar.setNumber(j, true, new Runnable() { // from class: com.miui.gallery.ui.CleanerFragment.4.1
                {
                    AnonymousClass4.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    CleanerFragment.this.setScannedBackgroundStatus();
                    CleanerFragment.this.mActionLayout.hide(true);
                    CleanerFragment.this.mScanLayout.retract(true);
                    CleanerFragment.this.mProgressBar.hideRingIcon(false);
                }
            });
        }

        @Override // com.miui.gallery.cleaner.ScannerManager.ScanObserver
        public void onScanResultUpdate(long j) {
            if (CleanerFragment.this.mScannerManager.isScanFinish()) {
                if (CleanerFragment.this.mScannerManager.isScanResultEmpty()) {
                    CleanerFragment.this.mRecyclerView.setNestedScrollingEnabled(false);
                    CleanerFragment.this.mScrollingLayout.setEnabled(false);
                    CleanerFragment.this.mProgressBar.showEmptyAnim();
                    CleanerFragment.this.mScanLayout.expand(true);
                    CleanerFragment.this.mActionLayout.show(true);
                    CleanerFragment.this.mActionLayout.showNoScanResultTip();
                    CleanerFragment.this.mCleanableCount = 0L;
                    return;
                }
                CleanerFragment.this.updateScanResult(false);
                if (CleanerFragment.this.mProgressBar.isCalculating()) {
                    return;
                }
                CleanerFragment.this.mProgressBar.hideRingIcon(false);
                CleanerFragment.this.mProgressBar.setNumber(CleanerFragment.this.mScannerManager.getScanSize());
            }
        }
    };
    public ScannerManager mScannerManager;
    public int mScreenHeight;
    public GalleryPullZoomLayout mScrollingLayout;

    public static /* synthetic */ void $r8$lambda$K9kfGe9mirYpuzXJdQcZ53eqlys(CleanerFragment cleanerFragment) {
        cleanerFragment.lambda$onStart$3();
    }

    /* renamed from: $r8$lambda$OP5Ufgv-q9yQ030GyBv7-B6c7_g */
    public static /* synthetic */ void m1437$r8$lambda$OP5Ufgvq9yQ030GyBv7B6c7_g(CleanerFragment cleanerFragment) {
        cleanerFragment.lambda$onConfigurationChanged$4();
    }

    /* renamed from: $r8$lambda$Vl-suzAe7YKbPF8LeiSxz-lh4_I */
    public static /* synthetic */ void m1438$r8$lambda$VlsuzAe7YKbPF8LeiSxzlh4_I(CleanerFragment cleanerFragment, GalleryPullZoomLayout.ScrollBy scrollBy, float f) {
        cleanerFragment.lambda$onInflateView$2(scrollBy, f);
    }

    public static /* synthetic */ void $r8$lambda$W7koZQYCpnn2GCtpRTrAkzuWOX4(CleanerFragment cleanerFragment, View view) {
        cleanerFragment.lambda$onInflateView$0(view);
    }

    public static /* synthetic */ void $r8$lambda$pIHQ231krqd0rAGP6TzrwAUk5jM(CleanerFragment cleanerFragment) {
        cleanerFragment.lambda$onInflateView$1();
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "cleaner";
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.cleaner_fragment, viewGroup, false);
        this.mRootView = inflate;
        BaseCommonActionBarHelper.DefaultThemeConfig defaultThemeConfig = new BaseCommonActionBarHelper.DefaultThemeConfig();
        defaultThemeConfig.setActionBarLayoutRes(R.layout.cleaner_page_title);
        defaultThemeConfig.setOnBackClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.CleanerFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CleanerFragment.$r8$lambda$W7koZQYCpnn2GCtpRTrAkzuWOX4(CleanerFragment.this, view);
            }
        });
        SimpleThemeActionBarHelper simpleThemeActionBarHelper = new SimpleThemeActionBarHelper(this.mActivity, defaultThemeConfig);
        this.mActionBarHelper = simpleThemeActionBarHelper;
        simpleThemeActionBarHelper.inflateActionBar();
        this.mActionBarHelper.setNullStyleActionBar();
        this.mActionLayout = new ActionLayout(inflate.findViewById(R.id.action_layout));
        GalleryPullZoomLayout galleryPullZoomLayout = (GalleryPullZoomLayout) inflate.findViewById(R.id.scrolling_layout);
        this.mScrollingLayout = galleryPullZoomLayout;
        galleryPullZoomLayout.post(new Runnable() { // from class: com.miui.gallery.ui.CleanerFragment$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                CleanerFragment.$r8$lambda$pIHQ231krqd0rAGP6TzrwAUk5jM(CleanerFragment.this);
            }
        });
        this.mScrollingLayout.setOnScrollListener(new GalleryPullZoomLayout.OnScrollListener() { // from class: com.miui.gallery.ui.CleanerFragment$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.widget.GalleryPullZoomLayout.OnScrollListener
            public final void onScrolled(GalleryPullZoomLayout.ScrollBy scrollBy, float f) {
                CleanerFragment.m1438$r8$lambda$VlsuzAe7YKbPF8LeiSxzlh4_I(CleanerFragment.this, scrollBy, f);
            }
        });
        View findViewById = inflate.findViewById(R.id.face_header_item);
        this.mProgressBar = (CleanerRotateProgressBar) findViewById.findViewById(R.id.cleaner_progress_view);
        this.mCornerView = findViewById.findViewById(R.id.image_corner_view);
        this.mScanLayout = new ScanLayout(findViewById);
        this.mScannerManager = ScannerManager.getInstance();
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.recycler_view);
        this.mRecyclerView = galleryRecyclerView;
        galleryRecyclerView.setLayoutManager(new ScanResultLayoutManager(this.mActivity));
        ScanResultAdapter scanResultAdapter = new ScanResultAdapter(this.mActivity);
        this.mAdapter = scanResultAdapter;
        this.mRecyclerView.setAdapter(scanResultAdapter);
        CleanerItemAnimator cleanerItemAnimator = new CleanerItemAnimator();
        cleanerItemAnimator.setSupportsChangeAnimations(false);
        this.mRecyclerView.setItemAnimator(cleanerItemAnimator);
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.cleaner_divider), 0, 1));
        this.mRecyclerView.setNestedScrollingEnabled(true);
        this.mRetractHeight = getResources().getDimensionPixelOffset(R.dimen.cleaner_scan_layout_retract_height);
        checkIsSplitScreen();
        this.mRefTip = AutoTracking.getRef();
        track();
        return inflate;
    }

    public /* synthetic */ void lambda$onInflateView$0(View view) {
        onBackPressed();
    }

    public /* synthetic */ void lambda$onInflateView$1() {
        AppCompatActivity appCompatActivity = this.mActivity;
        if (appCompatActivity == null || appCompatActivity.getAppCompatActionBar() == null) {
            return;
        }
        this.mScrollingLayout.setActionBarHeight(this.mActivity.getAppCompatActionBar().getHeight());
    }

    public /* synthetic */ void lambda$onInflateView$2(GalleryPullZoomLayout.ScrollBy scrollBy, float f) {
        this.mActionBarHelper.refreshTopBar(f);
    }

    public void track() {
        int intExtra = getActivity().getIntent().getIntExtra("extra_from_type", -1);
        if (intExtra == 1017) {
            TrackController.trackClick("403.70.1.1.16841", this.mRefTip);
        } else if (intExtra != 1018) {
        } else {
            TrackController.trackClick("403.69.1.1.16839", this.mRefTip);
        }
    }

    @Override // com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        AutoTracking.trackView("403.27.0.1.11312", this.mRefTip);
    }

    public final void setScannedBackgroundStatus() {
        this.mScanLayout.mView.setBackgroundResource(R.color.cleaner_scan_layout_scanned_background);
        AppCompatActivity appCompatActivity = this.mActivity;
        if (appCompatActivity == null || appCompatActivity.isInMultiWindowMode()) {
            return;
        }
        setTranslucentStatus(2);
    }

    public final void setTranslucentStatus(int i) {
        AppCompatActivity appCompatActivity = this.mActivity;
        if (appCompatActivity == null) {
            return;
        }
        appCompatActivity.setTranslucentStatus(i);
    }

    public boolean onBackPressed() {
        if (!this.mProgressBar.isCalculating() && !this.mScannerManager.isScanning()) {
            onExit();
            return true;
        }
        if (this.mExitConfirmDialog == null) {
            this.mExitConfirmDialog = new AlertDialog.Builder(this.mActivity).setPositiveButton(R.string.cleaner_scan_exit_dialog_positive, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.CleanerFragment.3
                {
                    CleanerFragment.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    CleanerFragment.this.onExit();
                    SamplingStatHelper.recordCountEvent("cleaner", "cleaner_scan_exit_confirm");
                    TrackController.trackClick("403.27.0.1.11313", AutoTracking.getRef());
                }
            }).setNegativeButton(17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.CleanerFragment.2
                {
                    CleanerFragment.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    CleanerFragment.this.onExitCancel();
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.ui.CleanerFragment.1
                {
                    CleanerFragment.this = this;
                }

                @Override // android.content.DialogInterface.OnCancelListener
                public void onCancel(DialogInterface dialogInterface) {
                    CleanerFragment.this.onExitCancel();
                }
            }).setTitle(R.string.cleaner_scan_exit_dialog_title).setMessage(R.string.cleaner_scan_exit_dialog_message).create();
        }
        this.mExitConfirmDialog.show();
        TrackController.trackClick("403.27.7.1.16824", AutoTracking.getRef());
        HashMap hashMap = new HashMap();
        hashMap.put("elapse_time", SamplingStatHelper.formatValueStage((int) ((System.currentTimeMillis() - this.mScannerManager.getStartTime()) / 1000), ScannerManager.TIME_COST_STAGE));
        SamplingStatHelper.recordCountEvent("cleaner", "cleaner_scan_exit", hashMap);
        return true;
    }

    public final void onExit() {
        this.mScannerManager.resetScan();
        this.mActivity.finish();
    }

    public final void onExitCancel() {
        SamplingStatHelper.recordCountEvent("cleaner", "cleaner_scan_exit_cancel");
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        this.mScannerManager.registerObserver(this.mScanObserver);
        this.mRootView.post(new Runnable() { // from class: com.miui.gallery.ui.CleanerFragment$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                CleanerFragment.$r8$lambda$K9kfGe9mirYpuzXJdQcZ53eqlys(CleanerFragment.this);
            }
        });
    }

    public void checkIsSplitScreen() {
        View view;
        AppCompatActivity appCompatActivity = this.mActivity;
        if (appCompatActivity == null || appCompatActivity.isFinishing() || this.mActivity.isDestroyed()) {
            return;
        }
        this.isPhoneSplitScreen = ActivityCompat.isInMultiWindowMode(this.mActivity) && !ActivityCompat.isInFreeFormWindow(this.mActivity) && !BaseBuildUtil.isLargeScreenDevice() && !BaseBuildUtil.isFoldableDevice();
        this.isFreeFromWindow = ActivityCompat.isInFreeFormWindow(this.mActivity);
        if (!this.isPhoneSplitScreen || (view = this.mCornerView) == null) {
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.topMargin = getResources().getDimensionPixelOffset(R.dimen.custom_corner_background_more_size);
        this.mCornerView.setLayoutParams(marginLayoutParams);
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = 1;
        if (this.mActivity.isInMultiWindowMode()) {
            setTranslucentStatus(1);
        }
        checkIsSplitScreen();
        FragmentActivity activity = getActivity();
        if (BaseBuildUtil.isLargeScreenIndependentOrientation()) {
            i = 2;
        }
        activity.setRequestedOrientation(i);
        if (this.mProgressBar.isCalculating() || this.mScannerManager.isScanning()) {
            this.mScannerManager.resetScan();
        } else if (this.mScannerManager.isScanFinish()) {
            ScanResultAdapter scanResultAdapter = new ScanResultAdapter(this.mActivity);
            this.mAdapter = scanResultAdapter;
            this.mRecyclerView.setAdapter(scanResultAdapter);
            this.mAdapter.updateDataList(this.mScannerManager.getScanResults());
        }
        this.mRootView.post(new Runnable() { // from class: com.miui.gallery.ui.CleanerFragment$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                CleanerFragment.m1437$r8$lambda$OP5Ufgvq9yQ030GyBv7B6c7_g(CleanerFragment.this);
            }
        });
    }

    /* renamed from: refreshLayoutIfHeightChange */
    public void lambda$onStart$3() {
        if (!isAdded()) {
            DefaultLogger.e("CleanerFragment", "the fragment not added to its activity");
            return;
        }
        if (this.isPhoneSplitScreen) {
            int height = this.mRootView.getHeight();
            if (height == 0) {
                height = ScreenUtils.getScreenVertical(getContext());
            }
            this.mScreenHeight = height;
            this.mExpandHeight = (((height - getResources().getDimensionPixelOffset(R.dimen.custom_corner_background_more_size)) - getResources().getDimensionPixelOffset(R.dimen.cleaner_item_action_layout_height)) - getResources().getDimensionPixelOffset(R.dimen.stop_scan_btn_margin_bottom)) - getResources().getDimensionPixelOffset(R.dimen.stop_scan_btn_margin_bottom);
        } else if (this.isFreeFromWindow) {
            int screenVertical = ScreenUtils.getScreenVertical(getActivity());
            this.mScreenHeight = screenVertical;
            this.mExpandHeight = screenVertical - this.mRetractHeight;
        } else {
            int curDisplayFullScreenHeight = ScreenUtils.getCurDisplayFullScreenHeight(getActivity()) - ScreenUtils.getNavBarHeight(getContext());
            this.mScreenHeight = curDisplayFullScreenHeight;
            this.mExpandHeight = curDisplayFullScreenHeight - this.mRetractHeight;
        }
        this.mBtnBottomMarginExpand = 0;
        this.mActionLayout.setHeight(this.mExpandHeight, this.mRetractHeight, 0);
        GalleryRecyclerView galleryRecyclerView = this.mRecyclerView;
        if (galleryRecyclerView != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) galleryRecyclerView.getLayoutParams();
            int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.cleaner_item_content_margin);
            layoutParams.rightMargin = dimensionPixelOffset;
            layoutParams.leftMargin = dimensionPixelOffset;
            this.mRecyclerView.setLayoutParams(layoutParams);
        }
        this.mProgressBar.changeConfiguration();
        if (this.mScannerManager.isReset()) {
            this.mScannerManager.startScan();
        } else if (this.mScreenHeight == 0) {
        } else {
            setLayoutByScanResult();
        }
    }

    public final void setLayoutByScanResult() {
        if (this.mScannerManager.isScanning()) {
            this.mActionLayout.show(false);
            this.mScanLayout.expand(false);
            this.mProgressBar.showRingIcon(false);
            setTranslucentStatus(1);
            return;
        }
        setScannedBackgroundStatus();
        if (this.mScannerManager.isScanResultEmpty()) {
            this.mRecyclerView.setNestedScrollingEnabled(false);
            this.mScrollingLayout.setEnabled(false);
            this.mScanLayout.expand(true, 300L);
            this.mActionLayout.show(true, 300L);
            this.mProgressBar.showEmptyAnim(300L);
            this.mActionLayout.showNoScanResultTip();
            this.mCleanableCount = 0L;
            setDisable();
            return;
        }
        updateScanResult(false);
        this.mActionLayout.hide(false);
        this.mScanLayout.retract(false);
        this.mProgressBar.hideRingIcon(false);
        this.mProgressBar.setNumber(this.mScannerManager.getScanSize());
    }

    public final void setDisable() {
        View findViewById;
        View view = this.mRootView;
        if (view == null || (findViewById = view.findViewById(R.id.intercept_cover)) == null) {
            return;
        }
        findViewById.setVisibility(0);
    }

    public final void setEnable() {
        View findViewById;
        View view = this.mRootView;
        if (view == null || (findViewById = view.findViewById(R.id.intercept_cover)) == null) {
            return;
        }
        findViewById.setVisibility(8);
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        this.mScannerManager.unregisterObserver(this.mScanObserver);
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mScannerManager.resetScan();
        Dialog dialog = this.mExitConfirmDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mExitConfirmDialog.dismiss();
        }
        GalleryPullZoomLayout galleryPullZoomLayout = this.mScrollingLayout;
        if (galleryPullZoomLayout != null) {
            galleryPullZoomLayout.setOnScrollListener(null);
        }
        GalleryPreferences.Album.setAlbumCleanableCount(this.mCleanableCount);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("com.miui.gallery.action.ACTION_CLEANER_DESTROY"));
    }

    public final void updateScanResult(boolean z) {
        List<ScanResult> dataList = this.mAdapter.getDataList();
        List<ScanResult> scanResults = this.mScannerManager.getScanResults();
        this.mCleanableCount = 0L;
        for (ScanResult scanResult : scanResults) {
            this.mCleanableCount += scanResult.getCount();
            if (z && scanResult.getType() == 0) {
                HashMap hashMap = new HashMap();
                hashMap.put("tip", scanResult.getSize() < 0 ? "403.27.7.1.16654" : "403.27.7.1.16829");
                hashMap.put("ref_tip", AutoTracking.getRef());
                AutoTracking.trackView(hashMap);
                z = false;
            }
        }
        DiffUtil.calculateDiff(new ScanResultDiffCallback(dataList, scanResults)).dispatchUpdatesTo(this.mAdapter);
        this.mAdapter.updateDataList(scanResults);
    }

    /* loaded from: classes2.dex */
    public class ScanResultLayoutManager extends LinearLayoutManager {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ScanResultLayoutManager(Context context) {
            super(context);
            CleanerFragment.this = r1;
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public boolean canScrollVertically() {
            return !CleanerFragment.this.mActionLayout.isVisible() && !CleanerFragment.this.mScanLayout.isAnimationRunning();
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager
        public int getExtraLayoutSpace(RecyclerView.State state) {
            return CleanerFragment.this.mScreenHeight;
        }
    }

    /* loaded from: classes2.dex */
    public class ActionLayout implements View.OnClickListener {
        public AlphaAnimation mAlphaAnimation = null;
        public Button mButton;
        public ViewGroup mContentLayout;
        public TextView mDescription;
        public ViewGroup.MarginLayoutParams mLayoutParams;
        public TextView mTitle;
        public View mView;

        public ActionLayout(View view) {
            CleanerFragment.this = r3;
            this.mView = view;
            view.setVisibility(8);
            this.mLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            this.mContentLayout = (ViewGroup) view.findViewById(R.id.action_content);
            this.mTitle = (TextView) view.findViewById(R.id.title);
            this.mDescription = (TextView) view.findViewById(R.id.description);
            Button button = (Button) view.findViewById(R.id.stop_scan);
            this.mButton = button;
            button.setOnClickListener(this);
            FolmeUtil.setDefaultTouchAnim(this.mButton, null, false, false, true);
        }

        public void setHeight(int i, int i2, int i3) {
            if (CleanerFragment.this.isPhoneSplitScreen) {
                this.mLayoutParams.topMargin = i + CleanerFragment.this.getResources().getDimensionPixelOffset(R.dimen.custom_corner_background_more_size) + CleanerFragment.this.getResources().getDimensionPixelOffset(R.dimen.stop_scan_btn_margin_bottom);
                this.mLayoutParams.height = CleanerFragment.this.getResources().getDimensionPixelSize(R.dimen.cleaner_item_action_layout_height);
            } else {
                ViewGroup.MarginLayoutParams marginLayoutParams = this.mLayoutParams;
                marginLayoutParams.topMargin = i;
                marginLayoutParams.height = i2;
            }
            this.mView.setLayoutParams(this.mLayoutParams);
            Resources resources = CleanerFragment.this.getResources();
            ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) this.mButton.getLayoutParams();
            marginLayoutParams2.setMarginStart(resources.getDimensionPixelOffset(R.dimen.stop_scan_btn_margin_start_end));
            marginLayoutParams2.setMarginEnd(resources.getDimensionPixelOffset(R.dimen.stop_scan_btn_margin_start_end));
            marginLayoutParams2.bottomMargin = resources.getDimensionPixelOffset(R.dimen.stop_scan_btn_margin_bottom) + i3;
            this.mButton.setLayoutParams(marginLayoutParams2);
        }

        public void show(boolean z) {
            show(z, 0L);
        }

        public void show(boolean z, long j) {
            cancelAnimation();
            if (z && !isVisible()) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                this.mAlphaAnimation = alphaAnimation;
                alphaAnimation.setStartOffset(j);
                this.mAlphaAnimation.setDuration(500L);
                this.mAlphaAnimation.setInterpolator(new CubicEaseInOutInterpolator());
                this.mView.startAnimation(this.mAlphaAnimation);
            }
            refreshContent();
            this.mView.setVisibility(0);
            CleanerFragment.this.mRecyclerView.setVisibility(8);
            CleanerFragment.this.setDisable();
        }

        public void hide(boolean z) {
            cancelAnimation();
            if (z && isVisible()) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                this.mAlphaAnimation = alphaAnimation;
                alphaAnimation.setDuration(350L);
                this.mAlphaAnimation.setInterpolator(new CubicEaseInOutInterpolator());
                this.mAlphaAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.miui.gallery.ui.CleanerFragment.ActionLayout.1
                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationRepeat(Animation animation) {
                    }

                    {
                        ActionLayout.this = this;
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationStart(Animation animation) {
                        CleanerFragment.this.mRecyclerView.setVisibility(0);
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationEnd(Animation animation) {
                        ActionLayout.this.mView.setVisibility(8);
                        CleanerFragment.this.setEnable();
                    }
                });
                this.mView.startAnimation(this.mAlphaAnimation);
                return;
            }
            this.mView.setVisibility(8);
            CleanerFragment.this.mRecyclerView.setVisibility(0);
            CleanerFragment.this.setEnable();
        }

        public final void showNoScanResultTip() {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mContentLayout.getLayoutParams();
            layoutParams.gravity = 17;
            layoutParams.setMargins(0, 0, 0, 0);
            this.mTitle.setText(R.string.cleaner_scan_finish);
            this.mDescription.setText(R.string.cleaner_scan_no_result);
            this.mButton.setText(R.string.cleaner_scan_ok);
        }

        public boolean isVisible() {
            return this.mView.getVisibility() == 0;
        }

        public final void cancelAnimation() {
            AlphaAnimation alphaAnimation = this.mAlphaAnimation;
            if (alphaAnimation != null) {
                alphaAnimation.setAnimationListener(null);
                this.mAlphaAnimation.cancel();
                this.mAlphaAnimation = null;
                this.mView.clearAnimation();
            }
        }

        public void setMarginTop(int i) {
            ViewGroup.MarginLayoutParams marginLayoutParams = this.mLayoutParams;
            marginLayoutParams.topMargin = i;
            this.mView.setLayoutParams(marginLayoutParams);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (isVisible()) {
                CleanerFragment.this.onBackPressed();
            }
        }

        public final void refreshContent() {
            ViewGroup viewGroup = this.mContentLayout;
            if (viewGroup != null) {
                viewGroup.setVisibility(CleanerFragment.this.isPhoneSplitScreen ? 8 : 0);
            }
        }
    }

    /* loaded from: classes2.dex */
    public class ScanLayout {
        public ValueAnimator mFlexAnimator = null;
        public ViewGroup.LayoutParams mLayoutParams;
        public View mView;

        /* renamed from: $r8$lambda$1oMiTPMU5c83-UXpFgQ0ZDdGYWg */
        public static /* synthetic */ void m1439$r8$lambda$1oMiTPMU5c83UXpFgQ0ZDdGYWg(ScanLayout scanLayout, ValueAnimator valueAnimator) {
            scanLayout.lambda$initFlexAnimator$0(valueAnimator);
        }

        public ScanLayout(View view) {
            CleanerFragment.this = r3;
            this.mView = view;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, r3.mExpandHeight);
            this.mLayoutParams = layoutParams;
            this.mView.setLayoutParams(layoutParams);
            r3.mScrollingLayout.setHeaderTargetHeight(r3.mExpandHeight);
        }

        public void expand(boolean z) {
            expand(z, 0L);
        }

        public void expand(boolean z, long j) {
            cancelAnimation();
            if (!z) {
                setHeight(CleanerFragment.this.mExpandHeight);
                CleanerFragment.this.mActionLayout.setHeight(CleanerFragment.this.mExpandHeight, CleanerFragment.this.mRetractHeight, CleanerFragment.this.mBtnBottomMarginExpand);
                return;
            }
            int i = this.mLayoutParams.height;
            if (i == CleanerFragment.this.mExpandHeight) {
                return;
            }
            initFlexAnimator(i, CleanerFragment.this.mExpandHeight);
            this.mFlexAnimator.setStartDelay(j);
            this.mFlexAnimator.start();
        }

        public void retract(boolean z) {
            retract(z, 0L);
        }

        public void retract(boolean z, long j) {
            cancelAnimation();
            if (!z) {
                setHeight(CleanerFragment.this.mRetractHeight);
                CleanerFragment.this.mActionLayout.setHeight(CleanerFragment.this.mRetractHeight, CleanerFragment.this.mExpandHeight, CleanerFragment.this.mBtnBottomMarginExpand);
                CleanerFragment.this.mScrollingLayout.setOriginalHeight(CleanerFragment.this.mRetractHeight);
                return;
            }
            int i = this.mLayoutParams.height;
            if (i == CleanerFragment.this.mRetractHeight) {
                return;
            }
            initFlexAnimator(i, CleanerFragment.this.mRetractHeight);
            this.mFlexAnimator.setStartDelay(j);
            this.mFlexAnimator.start();
            this.mFlexAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.CleanerFragment.ScanLayout.1
                {
                    ScanLayout.this = this;
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator, boolean z2) {
                    CleanerFragment.this.mScrollingLayout.setOriginalHeight(CleanerFragment.this.mRetractHeight);
                    CleanerFragment.this.mActionBarHelper.requestFocus();
                    CleanerFragment.this.mActionBarHelper.inflateActionBar();
                    CleanerFragment.this.mActionBarHelper.setNullStyleActionBar();
                }
            });
        }

        public boolean isAnimationRunning() {
            ValueAnimator valueAnimator = this.mFlexAnimator;
            return valueAnimator != null && valueAnimator.isRunning();
        }

        public final void initFlexAnimator(int i, int i2) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.mFlexAnimator = valueAnimator;
            valueAnimator.setIntValues(i, i2);
            this.mFlexAnimator.setDuration(500L);
            this.mFlexAnimator.setInterpolator(new CubicEaseInOutInterpolator());
            this.mFlexAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.ui.CleanerFragment$ScanLayout$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    CleanerFragment.ScanLayout.m1439$r8$lambda$1oMiTPMU5c83UXpFgQ0ZDdGYWg(CleanerFragment.ScanLayout.this, valueAnimator2);
                }
            });
        }

        public /* synthetic */ void lambda$initFlexAnimator$0(ValueAnimator valueAnimator) {
            AppCompatActivity appCompatActivity;
            Integer num = (Integer) valueAnimator.getAnimatedValue();
            if (num == null || (appCompatActivity = CleanerFragment.this.mActivity) == null || appCompatActivity.isDestroyed()) {
                return;
            }
            setHeight(num.intValue());
            CleanerFragment.this.mActionLayout.setHeight(CleanerFragment.this.mExpandHeight, CleanerFragment.this.mRetractHeight, CleanerFragment.this.mBtnBottomMarginExpand);
        }

        public final void cancelAnimation() {
            ValueAnimator valueAnimator = this.mFlexAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.mFlexAnimator.cancel();
                this.mFlexAnimator = null;
            }
        }

        public final void setHeight(int i) {
            ViewGroup.LayoutParams layoutParams = this.mLayoutParams;
            layoutParams.height = i;
            this.mView.setLayoutParams(layoutParams);
            CleanerFragment.this.mActionLayout.setMarginTop(i);
            CleanerFragment.this.mScrollingLayout.setHeaderTargetHeight(i);
        }
    }

    /* loaded from: classes2.dex */
    public static class ScanResultDiffCallback extends DiffUtil.Callback {
        public List<ScanResult> mNewList;
        public List<ScanResult> mOldList;

        public ScanResultDiffCallback(List<ScanResult> list, List<ScanResult> list2) {
            this.mOldList = list;
            this.mNewList = list2;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getOldListSize() {
            List<ScanResult> list = this.mOldList;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getNewListSize() {
            List<ScanResult> list = this.mNewList;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areItemsTheSame(int i, int i2) {
            List<ScanResult> list = this.mOldList;
            int i3 = -1;
            int type = (list == null || i >= list.size()) ? -1 : this.mOldList.get(i).getType();
            List<ScanResult> list2 = this.mNewList;
            if (list2 != null && i2 < list2.size()) {
                i3 = this.mNewList.get(i2).getType();
            }
            return type == i3;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areContentsTheSame(int i, int i2) {
            long j;
            int i3;
            int i4;
            List<ScanResult> list = this.mOldList;
            long j2 = 0;
            if (list == null || i >= list.size()) {
                j = 0;
                i3 = 0;
            } else {
                j = this.mOldList.get(i).getSize();
                i3 = this.mOldList.get(i).getAction();
            }
            List<ScanResult> list2 = this.mNewList;
            if (list2 == null || i2 >= list2.size()) {
                i4 = 0;
            } else {
                j2 = this.mNewList.get(i2).getSize();
                i4 = this.mNewList.get(i2).getAction();
            }
            return j == j2 && i4 == i3;
        }
    }
}
