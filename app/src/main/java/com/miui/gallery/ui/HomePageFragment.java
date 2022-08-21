package com.miui.gallery.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentViewModelLazyKt;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleOwnerKt;
import androidx.lifecycle.Observer;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GalleryGridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.tracing.Trace;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.request.BaseRequestOptions;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.activity.BackupSettingsLoginActivity;
import com.miui.gallery.activity.HomePageActivity;
import com.miui.gallery.activity.HomePageInteractionCallback;
import com.miui.gallery.activity.HomePageStartupHelper2;
import com.miui.gallery.activity.InternalPhotoPageActivity;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.adapter.HomePageAdapter2;
import com.miui.gallery.adapter.PreloadItem;
import com.miui.gallery.adapter.ProportionStringTagAdapter;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.app.screenChange.ScreenSize;
import com.miui.gallery.assistant.cache.MediaFeatureCacheManager;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.data.ClusteredList;
import com.miui.gallery.discovery.DiscoveryMessageManager;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.GlideRequest;
import com.miui.gallery.glide.GlideRequests;
import com.miui.gallery.glide.load.RegionConfig;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.loader.DiscoveryMessageLoader;
import com.miui.gallery.loader.HomeMediaLoader;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.model.ImageLoadParamsKt;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cache.CacheLiveData;
import com.miui.gallery.provider.cache.IMediaSnapshot;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.provider.cache.MediaCacheItem;
import com.miui.gallery.request.PicToPdfHelper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.threadpool.GallerySchedulers;
import com.miui.gallery.ui.AIAlbumDisplayHelper;
import com.miui.gallery.ui.DeletionTask;
import com.miui.gallery.ui.HomePageFragment;
import com.miui.gallery.ui.IntroductionPage;
import com.miui.gallery.ui.KeyboardShortcutGroupManager;
import com.miui.gallery.ui.actionBar.TabActionBarHelper;
import com.miui.gallery.ui.pictures.MatchPositionManager;
import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.ui.pictures.PinchCallback;
import com.miui.gallery.ui.pictures.PinchManager;
import com.miui.gallery.util.ActionURIHandler;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.OnAppFocusedListener;
import com.miui.gallery.util.PhotoPageIntent;
import com.miui.gallery.util.SoundUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.EmptyPage;
import com.miui.gallery.widget.PanelBar;
import com.miui.gallery.widget.SwitchView;
import com.miui.gallery.widget.ViewDragListener;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.editwrapper.MultiChoiceModeListener;
import com.miui.gallery.widget.recyclerview.ClickEventInterceptCallback;
import com.miui.gallery.widget.recyclerview.FastScrollerBar;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsule;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsuleViewProvider;
import com.miui.gallery.widget.recyclerview.FastScrollerTimeCapsuleView;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import com.miui.gallery.widget.recyclerview.InterceptableRecyclerView;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider;
import com.miui.gallery.widget.recyclerview.ProportionTagBaseAdapter;
import com.miui.gallery.widget.recyclerview.ProportionTagModel;
import com.miui.gallery.widget.recyclerview.RecyclerViewUtils;
import com.miui.gallery.widget.recyclerview.SpanSizeProvider;
import com.miui.gallery.widget.tsd.DrawerState;
import com.miui.gallery.widget.tsd.INestedTwoStageDrawer;
import com.miui.gallery.widget.tsd.InestedScrollerStateListener;
import com.miui.gallery.widget.tsd.NestedTwoStageDrawer;
import com.miui.itemdrag.animator.RefactoredDefaultItemAnimator;
import com.miui.itemdrag.animator.base.GeneralItemAnimator;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.Functions;
import io.reactivex.subjects.PublishSubject;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.ranges.RangesKt___RangesKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.ExecutorsKt;
import miui.cloud.util.SyncAutoSettingUtil;
import miuix.appcompat.app.AppCompatActivity;
import miuix.springback.view.SpringBackLayout;

/* compiled from: HomePageFragment.kt */
/* loaded from: classes2.dex */
public final class HomePageFragment extends BaseMediaFragment implements OnAppFocusedListener, OnHomePageResetListener, AIAlbumDisplayHelper.DisplayStatusCallback, ImmersionMenuSupport, ViewDragListener.DropReceiver, ListPreloader.PreloadModelProvider<PreloadItem>, TabActionBarHelper.OnTabChangeListener {
    public ChoiceModeListener choiceModeListener;
    public int defaultViewModeOrdinal;
    public boolean isShowSearch;
    public HomePageActivity.HomeTabActionBarHelper mActionBarHelper;
    public int[] mAfterDeletedFirstVisiblePosAndOffset;
    public final PublishSubject<List<IRecord>> mComputePhotoCountPublisher;
    public boolean mDiffEnable;
    public DiscoverChangeManager mDiscoverWidgetManager;
    public final CompositeDisposable mDisposable;
    public EditableListViewWrapper.DragDataProvider mDragDataProvider;
    public INestedTwoStageDrawer mDrawer;
    public EmptyPage mEmptyView;
    public InterceptableRecyclerView mHomeGridView;
    public EditableListViewWrapper mHomeGridViewWrapper;
    public HomePageAdapter2 mHomePageAdapter;
    public HomePagePhotoLoaderCallback mHomePagePhotoLoaderCallback;
    public HomePageStartupHelper2 mHomePageStartHelper;
    public HomePageInteractionCallback mInteractionCallback;
    public boolean mIsFirstStatSwitch;
    public boolean mIsInMultiWindowMode;
    public boolean mIsSwitchAllPhotos;
    public MatchPositionManager mMatchPositionManager;
    public boolean mNeedDismissMargin;
    public PanelBar mPanelBar;
    public boolean mPendingStartupCallback;
    public final PublishSubject<List<IRecord>> mPhotoLoaderPublisher;
    public PinchManager mPinchManager;
    public View mPlaceHolderView;
    public final Lazy mRequestManager$delegate;
    public AIAlbumDisplayHelper.WeakReferencedAIAlbumDisplayStatusObserver mSearchBarStatusObserver;
    public final PublishSubject<List<IRecord>> mSnapshotPublisher;
    public GridItemSpacingDecoration mSpacingDecoration;
    public boolean mStartCalled;
    public SwitchView mSwitchView;
    public SwitchViewWrapper mSwitchViewWrapper;
    public SyncDownloadManager mSyncDownloadManager;
    public ProportionTagBaseAdapter<Integer> mTagAdapter;
    public boolean mTagProportionChanged;
    public int mTotalCount;
    public PictureViewMode mViewMode;
    public final GeneralItemAnimator rvAnimator;
    public HomePageKeyboardShortcutCallback shortcutCallback;
    public static final Companion Companion = new Companion(null);
    public static final Uri PHOTOS_PAGE_URI = GalleryContract.Media.URI;
    public static final DrawerState DEFAULT_DRAWER_STATE = DrawerState.CLOSE;
    public static final Map<PictureViewMode, Integer> sViewModePreferenceMap = MapsKt__MapsKt.mapOf(TuplesKt.to(PictureViewMode.LARGE_THUMB, 0), TuplesKt.to(PictureViewMode.MICRO_THUMB, 1));
    public final Lazy viewModel$delegate = FragmentViewModelLazyKt.createViewModelLazy(this, Reflection.getOrCreateKotlinClass(HomePageViewModel.class), new HomePageFragment$special$$inlined$viewModels$default$2(new HomePageFragment$special$$inlined$viewModels$default$1(this)), null);
    public final Lazy liveData$delegate = LazyKt__LazyJVMKt.lazy(LazyThreadSafetyMode.NONE, new HomePageFragment$liveData$2(this));

    public static /* synthetic */ void $r8$lambda$928B3jYIDwLUtVkCFeB8lo8rmgs(HomePageFragment homePageFragment, int[] iArr) {
        m1468onStartup$lambda6(homePageFragment, iArr);
    }

    public static /* synthetic */ boolean $r8$lambda$BrsEnIbFw6ULBd4Ywos1P0ovcGM(HomePageFragment homePageFragment, RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        return m1461onStartup$lambda11$lambda10(homePageFragment, recyclerView, view, i, j, f, f2);
    }

    public static /* synthetic */ void $r8$lambda$DLOtOwTflL48VH_4SJcrdtvPGBY(HomePageFragment homePageFragment) {
        m1459callbackStartup$lambda17(homePageFragment);
    }

    public static /* synthetic */ int[] $r8$lambda$LaDwq2lftnBR9Yt1ADvErwYVBfk(HomePageFragment homePageFragment, int i) {
        return m1462onStartup$lambda12(homePageFragment, i);
    }

    public static /* synthetic */ int[] $r8$lambda$W6FzE2ldzQfS7hS4Rh3BH96IQDU(HomePageFragment homePageFragment, List list) {
        return homePageFragment.computePhotoAndVideoCount(list);
    }

    public static /* synthetic */ void $r8$lambda$X2oBZBw3ZTUZCBSu9DQEhMJYWqo(HomePageFragment homePageFragment, List list) {
        m1464onStartup$lambda15(homePageFragment, list);
    }

    /* renamed from: $r8$lambda$ZzA2GBpevmb4c-nzCPydNhgjS3Y */
    public static /* synthetic */ void m1457$r8$lambda$ZzA2GBpevmb4cnzCPydNhgjS3Y(HomePageFragment homePageFragment, RecyclerView.ViewHolder viewHolder) {
        m1470onStartup$lambda9$lambda8(homePageFragment, viewHolder);
    }

    public static /* synthetic */ int $r8$lambda$ngAKaMRzf4ytlIR54Su2oK9IFDs(HomePageFragment homePageFragment, RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        return m1469onStartup$lambda9$lambda7(homePageFragment, recyclerView, view, i, j, f, f2);
    }

    public static /* synthetic */ void $r8$lambda$tcp0NkLXdruopdiIbHKCsuw4MvQ(HomePageFragment homePageFragment, View view) {
        m1460inflateEmptyView$lambda16(homePageFragment, view);
    }

    /* renamed from: $r8$lambda$u-Wdmz72NEZ5GMWBWAGSl0qdgQ0 */
    public static /* synthetic */ void m1458$r8$lambda$uWdmz72NEZ5GMWBWAGSl0qdgQ0(HomePageFragment homePageFragment, List list) {
        m1467onStartup$lambda5(homePageFragment, list);
    }

    @Override // com.miui.gallery.widget.ViewDragListener.DropReceiver
    public boolean canReceive() {
        return true;
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "home";
    }

    public final String getPageTip() {
        return "403.1.2.1.9881";
    }

    @Override // com.miui.gallery.ui.ImmersionMenuSupport
    public int getSupportedAction() {
        return 24;
    }

    @Override // com.miui.gallery.widget.ViewDragListener.DropReceiver
    public String receivePath() {
        return "MiShare";
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public boolean recordPageByDefault() {
        return false;
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        HomePageActivity.HomeTabActionBarHelper homeTabActionBarHelper;
        Intrinsics.checkNotNullParameter(inflater, "inflater");
        Intrinsics.checkNotNullParameter(container, "container");
        try {
            Trace.beginSection("HomePageFrag#onInflateView");
            AppCompatActivity appCompatActivity = this.mActivity;
            if (appCompatActivity != null) {
                this.mHomePageStartHelper = ((HomePageStartupHelper2.Attacher) appCompatActivity).getStartupHelper();
                this.mViewMode = HomePageStartupHelper2.Companion.getHomePageViewMode();
                this.mDrawer = NestedTwoStageDrawer.create(this.mActivity);
                AppCompatActivity appCompatActivity2 = this.mActivity;
                HomePageStartupHelper2 homePageStartupHelper2 = null;
                if (!(appCompatActivity2 instanceof HomePageActivity)) {
                    homeTabActionBarHelper = null;
                } else if (appCompatActivity2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type com.miui.gallery.activity.HomePageActivity");
                } else {
                    homeTabActionBarHelper = ((HomePageActivity) appCompatActivity2).getHomePageActionBarHelper();
                }
                this.mActionBarHelper = homeTabActionBarHelper;
                InterceptableRecyclerView interceptableRecyclerView = new InterceptableRecyclerView(this.mActivity);
                interceptableRecyclerView.setId(R.id.home_page_recycler);
                interceptableRecyclerView.setClipToPadding(false);
                interceptableRecyclerView.setAdapterPos2ViewPosConverter(new GalleryRecyclerView.AdapterPos2ViewPosConverter() { // from class: com.miui.gallery.ui.HomePageFragment$onInflateView$1$1$1
                    @Override // com.miui.gallery.widget.recyclerview.GalleryRecyclerView.AdapterPos2ViewPosConverter
                    public final int convert(int i) {
                        HomePageAdapter2 access$getMHomePageAdapter$p = HomePageFragment.access$getMHomePageAdapter$p(HomePageFragment.this);
                        Intrinsics.checkNotNull(access$getMHomePageAdapter$p);
                        return access$getMHomePageAdapter$p.calculateScrollPosition(HomePageFragment.access$getMViewMode$p(HomePageFragment.this), i);
                    }
                });
                interceptableRecyclerView.setLayoutParams(new ViewGroup.MarginLayoutParams(-1, -1));
                int left = interceptableRecyclerView.getLeft();
                int top = interceptableRecyclerView.getTop();
                Context context = interceptableRecyclerView.getContext();
                Intrinsics.checkNotNullExpressionValue(context, "context");
                interceptableRecyclerView.setPadding(left, top - context.getResources().getDimensionPixelOffset(R.dimen.time_line_header_padding_top), interceptableRecyclerView.getRight(), interceptableRecyclerView.getBottom());
                interceptableRecyclerView.setHasFixedSize(true);
                interceptableRecyclerView.setOverScrollMode(0);
                interceptableRecyclerView.setItemAnimator(this.mViewMode.isAggregated() ? null : this.rvAnimator);
                this.mHomeGridView = interceptableRecyclerView;
                this.mHomeGridViewWrapper = new EditableListViewWrapper(this.mHomeGridView);
                AppCompatActivity mActivity = this.mActivity;
                Intrinsics.checkNotNullExpressionValue(mActivity, "mActivity");
                Lifecycle lifecycle = getLifecycle();
                Intrinsics.checkNotNullExpressionValue(lifecycle, "lifecycle");
                this.mHomePageAdapter = new HomePageAdapter2(mActivity, lifecycle, null, 4, null);
                HomePageAdapter2 homePageAdapter2 = this.mHomePageAdapter;
                Intrinsics.checkNotNull(homePageAdapter2);
                HomePageStartupHelper2 homePageStartupHelper22 = this.mHomePageStartHelper;
                if (homePageStartupHelper22 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("mHomePageStartHelper");
                    homePageStartupHelper22 = null;
                }
                homePageAdapter2.setViewProvider(homePageStartupHelper22.getViewProvider());
                final GalleryGridLayoutManager galleryGridLayoutManager = new GalleryGridLayoutManager(this.mActivity, Config$ThumbConfig.get().sMicroThumbColumnsPortrait);
                InterceptableRecyclerView interceptableRecyclerView2 = this.mHomeGridView;
                Intrinsics.checkNotNull(interceptableRecyclerView2);
                interceptableRecyclerView2.setScrollingCalculator(this.mHomePageAdapter);
                galleryGridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new SpanSizeProvider() { // from class: com.miui.gallery.ui.HomePageFragment$onInflateView$1$2
                    @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
                    public int getSpanSize(int i) {
                        if (GalleryGridLayoutManager.this.getSpanCount() == 1) {
                            return 1;
                        }
                        if (!this.mViewMode.isAggregated()) {
                            EditableListViewWrapper editableListViewWrapper = this.mHomeGridViewWrapper;
                            Intrinsics.checkNotNull(editableListViewWrapper);
                            return editableListViewWrapper.getSpanSize(i, GalleryGridLayoutManager.this.getSpanCount());
                        }
                        return GalleryGridLayoutManager.this.getSpanCount();
                    }

                    @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
                    public int getSpanIndex(int i, int i2) {
                        if (GalleryGridLayoutManager.this.getSpanCount() != 1 && !this.mViewMode.isAggregated()) {
                            EditableListViewWrapper editableListViewWrapper = this.mHomeGridViewWrapper;
                            Intrinsics.checkNotNull(editableListViewWrapper);
                            return editableListViewWrapper.getSpanIndex(i, GalleryGridLayoutManager.this.getSpanCount());
                        }
                        return 0;
                    }
                }));
                galleryGridLayoutManager.setSpanCount(this.mViewMode.getSpan());
                HomePageAdapter2 homePageAdapter22 = this.mHomePageAdapter;
                Intrinsics.checkNotNull(homePageAdapter22);
                homePageAdapter22.setConfiguration(getResources().getConfiguration());
                InterceptableRecyclerView interceptableRecyclerView3 = this.mHomeGridView;
                Intrinsics.checkNotNull(interceptableRecyclerView3);
                interceptableRecyclerView3.setLayoutManager(galleryGridLayoutManager);
                this.mSpacingDecoration = new GridItemSpacingDecoration(this.mHomeGridView, false, this.mViewMode.getSpacing(), this.mViewMode.getSpacing());
                InterceptableRecyclerView interceptableRecyclerView4 = this.mHomeGridView;
                Intrinsics.checkNotNull(interceptableRecyclerView4);
                GridItemSpacingDecoration gridItemSpacingDecoration = this.mSpacingDecoration;
                Intrinsics.checkNotNull(gridItemSpacingDecoration);
                interceptableRecyclerView4.addItemDecoration(gridItemSpacingDecoration);
                EditableListViewWrapper editableListViewWrapper = this.mHomeGridViewWrapper;
                Intrinsics.checkNotNull(editableListViewWrapper);
                editableListViewWrapper.setAdapter(this.mHomePageAdapter);
                EditableListViewWrapper editableListViewWrapper2 = this.mHomeGridViewWrapper;
                Intrinsics.checkNotNull(editableListViewWrapper2);
                editableListViewWrapper2.setDragDataProvider(getDragDataProvider());
                PictureViewMode mViewMode = this.mViewMode;
                Intrinsics.checkNotNullExpressionValue(mViewMode, "mViewMode");
                setPictureViewMode(mViewMode);
                HomePageStartupHelper2 homePageStartupHelper23 = this.mHomePageStartHelper;
                if (homePageStartupHelper23 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("mHomePageStartHelper");
                } else {
                    homePageStartupHelper2 = homePageStartupHelper23;
                }
                homePageStartupHelper2.setDataLoaderListener(new HomePageFragment$onInflateView$1$3(this));
                addScreenChangeListener(new IScreenChange.OnLargeScreenChangeListener() { // from class: com.miui.gallery.ui.HomePageFragment$onInflateView$1$4
                    @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
                    public void onCreatedWhileNormalDevice(ScreenSize screenSize) {
                    }

                    @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
                    public void onScreenSizeToLargeOrNormal(ScreenSize screenSize) {
                    }

                    @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
                    public void onCreatedWhileLargeDevice(ScreenSize screenSize) {
                        HomePageFragment.access$setMNeedDismissMargin$p(HomePageFragment.this, true);
                        INestedTwoStageDrawer access$getMDrawer$p = HomePageFragment.access$getMDrawer$p(HomePageFragment.this);
                        Intrinsics.checkNotNull(access$getMDrawer$p);
                        HomePageFragment homePageFragment = HomePageFragment.this;
                        HomePageFragment.access$addHeaderView(homePageFragment);
                        if (BaseBuildUtil.isLargeHorizontalWindow()) {
                            access$getMDrawer$p.setHeaderView(HomePageFragment.access$getMPlaceHolderView$p(homePageFragment));
                            access$getMDrawer$p.setDrawerState(DrawerState.HALF_OPEN, false, null);
                        }
                    }
                });
                INestedTwoStageDrawer iNestedTwoStageDrawer = this.mDrawer;
                Intrinsics.checkNotNull(iNestedTwoStageDrawer);
                iNestedTwoStageDrawer.setContentView(this.mHomeGridView);
                InterceptableRecyclerView interceptableRecyclerView5 = this.mHomeGridView;
                Intrinsics.checkNotNull(interceptableRecyclerView5);
                interceptableRecyclerView5.setImportantForAccessibility(2);
                INestedTwoStageDrawer iNestedTwoStageDrawer2 = this.mDrawer;
                if (iNestedTwoStageDrawer2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type android.view.View");
                }
                return (View) iNestedTwoStageDrawer2;
            }
            throw new NullPointerException("null cannot be cast to non-null type com.miui.gallery.activity.HomePageStartupHelper2.Attacher");
        } finally {
            Trace.endSection();
        }
    }

    public static final /* synthetic */ void access$addHeaderView(HomePageFragment homePageFragment) {
        homePageFragment.addHeaderView();
    }

    public static final /* synthetic */ INestedTwoStageDrawer access$getMDrawer$p(HomePageFragment homePageFragment) {
        return homePageFragment.mDrawer;
    }

    public static final /* synthetic */ InterceptableRecyclerView access$getMHomeGridView$p(HomePageFragment homePageFragment) {
        return homePageFragment.mHomeGridView;
    }

    public static final /* synthetic */ HomePageAdapter2 access$getMHomePageAdapter$p(HomePageFragment homePageFragment) {
        return homePageFragment.mHomePageAdapter;
    }

    public static final /* synthetic */ PanelBar access$getMPanelBar$p(HomePageFragment homePageFragment) {
        return homePageFragment.mPanelBar;
    }

    public static final /* synthetic */ View access$getMPlaceHolderView$p(HomePageFragment homePageFragment) {
        return homePageFragment.mPlaceHolderView;
    }

    public static final /* synthetic */ ProportionTagBaseAdapter access$getMTagAdapter$p(HomePageFragment homePageFragment) {
        return homePageFragment.mTagAdapter;
    }

    public static final /* synthetic */ boolean access$getMTagProportionChanged$p(HomePageFragment homePageFragment) {
        return homePageFragment.mTagProportionChanged;
    }

    public static final /* synthetic */ PictureViewMode access$getMViewMode$p(HomePageFragment homePageFragment) {
        return homePageFragment.mViewMode;
    }

    public static final /* synthetic */ void access$refreshSearchIconVisible(HomePageFragment homePageFragment) {
        homePageFragment.refreshSearchIconVisible();
    }

    public static final /* synthetic */ void access$setMNeedDismissMargin$p(HomePageFragment homePageFragment, boolean z) {
        homePageFragment.mNeedDismissMargin = z;
    }

    public static final /* synthetic */ void access$setMTagAdapter$p(HomePageFragment homePageFragment, ProportionTagBaseAdapter proportionTagBaseAdapter) {
        homePageFragment.mTagAdapter = proportionTagBaseAdapter;
    }

    public static final /* synthetic */ void access$setMTagProportionChanged$p(HomePageFragment homePageFragment, boolean z) {
        homePageFragment.mTagProportionChanged = z;
    }

    public HomePageFragment() {
        PublishSubject<List<IRecord>> create = PublishSubject.create();
        Intrinsics.checkNotNullExpressionValue(create, "create<List<IRecord>>()");
        this.mSnapshotPublisher = create;
        PublishSubject<List<IRecord>> create2 = PublishSubject.create();
        Intrinsics.checkNotNullExpressionValue(create2, "create<List<IRecord>>()");
        this.mPhotoLoaderPublisher = create2;
        PublishSubject<List<IRecord>> create3 = PublishSubject.create();
        Intrinsics.checkNotNullExpressionValue(create3, "create<List<IRecord>>()");
        this.mComputePhotoCountPublisher = create3;
        this.mDisposable = new CompositeDisposable();
        PictureViewMode pictureViewMode = PictureViewMode.MICRO_THUMB;
        this.defaultViewModeOrdinal = pictureViewMode.ordinal();
        this.mViewMode = pictureViewMode;
        this.mRequestManager$delegate = LazyKt__LazyJVMKt.lazy(new HomePageFragment$mRequestManager$2(this));
        this.mTagProportionChanged = true;
        this.mIsFirstStatSwitch = true;
        this.mDiffEnable = true;
        this.choiceModeListener = new ChoiceModeListener(this);
        this.shortcutCallback = new HomePageKeyboardShortcutCallback(this);
        RefactoredDefaultItemAnimator refactoredDefaultItemAnimator = new RefactoredDefaultItemAnimator();
        this.rvAnimator = refactoredDefaultItemAnimator;
        refactoredDefaultItemAnimator.setAddDuration(200L);
        refactoredDefaultItemAnimator.setChangeDuration(200L);
        refactoredDefaultItemAnimator.setMoveDuration(350L);
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(1.5f);
        DecelerateInterpolator decelerateInterpolator2 = new DecelerateInterpolator(1.0f);
        refactoredDefaultItemAnimator.setMoveInterpolator(decelerateInterpolator);
        refactoredDefaultItemAnimator.setAddInterpolator(decelerateInterpolator2);
        refactoredDefaultItemAnimator.setRemoveInterpolator(decelerateInterpolator2);
        refactoredDefaultItemAnimator.setChangeInterpolator(decelerateInterpolator);
        refactoredDefaultItemAnimator.setMovesDelayDuration(0L);
        refactoredDefaultItemAnimator.setChangesDelayDuration(0L);
        refactoredDefaultItemAnimator.setAddsDelayDuration(0L);
    }

    public final HomePageViewModel getViewModel() {
        return (HomePageViewModel) this.viewModel$delegate.mo119getValue();
    }

    public final CacheLiveData<MediaCacheItem, IRecord> getLiveData() {
        return (CacheLiveData) this.liveData$delegate.mo119getValue();
    }

    public final GlideRequests getMRequestManager() {
        return (GlideRequests) this.mRequestManager$delegate.mo119getValue();
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        int homePageViewMode = GalleryPreferences.HomePage.getHomePageViewMode();
        this.defaultViewModeOrdinal = homePageViewMode;
        if (bundle != null) {
            this.mViewMode = PictureViewMode.getViewModeByOrdinal(bundle.getInt("view_mode", homePageViewMode));
            this.mIsSwitchAllPhotos = bundle.getBoolean("switch_state", GalleryPreferences.HomePage.isHomePageShowAllPhotos());
            return;
        }
        this.mIsSwitchAllPhotos = GalleryPreferences.HomePage.isHomePageShowAllPhotos();
    }

    @Override // com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        super.onAttach(activity);
        if (activity instanceof HomePageInteractionCallback) {
            this.mInteractionCallback = (HomePageInteractionCallback) activity;
        }
    }

    @Override // com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mInteractionCallback = null;
    }

    @Override // com.miui.gallery.app.fragment.MiuiFragment, androidx.fragment.app.Fragment
    public void onMultiWindowModeChanged(boolean z) {
        super.onMultiWindowModeChanged(z);
        this.mIsInMultiWindowMode = z;
    }

    public final boolean isTopBarPermanent() {
        HomePageAdapter2 homePageAdapter2 = this.mHomePageAdapter;
        return (homePageAdapter2 != null && homePageAdapter2.getItemCount() == 0) && AccountCache.getAccount() == null;
    }

    public final void updateAfterLoadFinished(List<? extends IRecord> list) {
        if (this.mActivity == null) {
            return;
        }
        this.mDiffEnable = true;
        HomePageAdapter2 homePageAdapter2 = this.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter2);
        if (homePageAdapter2.getItemCount() == 0) {
            inflateEmptyView();
            if (!this.mIsSwitchAllPhotos) {
                EmptyPage emptyPage = this.mEmptyView;
                if (emptyPage != null) {
                    emptyPage.setActionButtonVisible(false);
                }
            } else {
                EmptyPage emptyPage2 = this.mEmptyView;
                if (emptyPage2 != null) {
                    emptyPage2.setActionButtonVisible(true);
                }
            }
        } else {
            EmptyPage emptyPage3 = this.mEmptyView;
            if (emptyPage3 != null) {
                emptyPage3.setActionButtonVisible(false);
            }
        }
        if (this.mSyncDownloadManager != null) {
            boolean isTopBarPermanent = isTopBarPermanent();
            SyncDownloadManager syncDownloadManager = this.mSyncDownloadManager;
            Intrinsics.checkNotNull(syncDownloadManager);
            syncDownloadManager.setPermanent(isTopBarPermanent);
            INestedTwoStageDrawer iNestedTwoStageDrawer = this.mDrawer;
            Intrinsics.checkNotNull(iNestedTwoStageDrawer);
            iNestedTwoStageDrawer.setDragEnabled(!isTopBarPermanent);
        }
        saveHomePageIds(list);
        updateSnapshot(list);
        if (!(list == null || list.isEmpty())) {
            if (this.mTotalCount > list.size() && this.mAfterDeletedFirstVisiblePosAndOffset != null) {
                InterceptableRecyclerView interceptableRecyclerView = this.mHomeGridView;
                Intrinsics.checkNotNull(interceptableRecyclerView);
                int[] iArr = this.mAfterDeletedFirstVisiblePosAndOffset;
                Intrinsics.checkNotNull(iArr);
                int i = iArr[0];
                int[] iArr2 = this.mAfterDeletedFirstVisiblePosAndOffset;
                Intrinsics.checkNotNull(iArr2);
                interceptableRecyclerView.scrollToPositionWithOffset(i, iArr2[1]);
                this.mAfterDeletedFirstVisiblePosAndOffset = null;
            }
            this.mTotalCount = list.size();
        }
        this.mComputePhotoCountPublisher.onNext(list);
        refreshScrollPosition(list);
        refreshSwitchView(list);
    }

    public final void updateSnapshot(List<? extends IRecord> list) {
        this.mSnapshotPublisher.onNext(list);
    }

    public final int[] computePhotoAndVideoCount(List<? extends IRecord> list) {
        int[] iArr = new int[2];
        long currentTimeMillis = System.currentTimeMillis();
        if (list instanceof ClusteredList) {
            ClusteredList clusteredList = (ClusteredList) list;
            int contentCount = clusteredList.getContentCount();
            int i = 0;
            while (i < contentCount) {
                int i2 = i + 1;
                IRecord iRecord = (IRecord) clusteredList.rawGetContent(i);
                if (iRecord instanceof IMediaSnapshot) {
                    if (BaseFileMimeUtil.isImageFromMimeType(((IMediaSnapshot) iRecord).getMimeType())) {
                        iArr[0] = iArr[0] + 1;
                    } else {
                        iArr[1] = iArr[1] + 1;
                    }
                }
                i = i2;
            }
        }
        DefaultLogger.i("HomePageFragment", Intrinsics.stringPlus("computePhotoAndVideoCount:duration=", Long.valueOf(System.currentTimeMillis() - currentTimeMillis)));
        return iArr;
    }

    public final void refreshScrollPosition(List<? extends IRecord> list) {
        if (this.mTotalCount > list.size() && this.mAfterDeletedFirstVisiblePosAndOffset != null) {
            InterceptableRecyclerView interceptableRecyclerView = this.mHomeGridView;
            Intrinsics.checkNotNull(interceptableRecyclerView);
            int[] iArr = this.mAfterDeletedFirstVisiblePosAndOffset;
            Intrinsics.checkNotNull(iArr);
            int i = iArr[0];
            int[] iArr2 = this.mAfterDeletedFirstVisiblePosAndOffset;
            Intrinsics.checkNotNull(iArr2);
            interceptableRecyclerView.scrollToPositionWithOffset(i, iArr2[1]);
            this.mAfterDeletedFirstVisiblePosAndOffset = null;
        } else if (this.mIsSwitchAllPhotos && GalleryPreferences.HomePage.getHomePageDiscoverPhotos() != 0) {
            InterceptableRecyclerView interceptableRecyclerView2 = this.mHomeGridView;
            Intrinsics.checkNotNull(interceptableRecyclerView2);
            interceptableRecyclerView2.scrollToPositionWithOffset(0, 0);
            MatchPositionManager matchPositionManager = this.mMatchPositionManager;
            Intrinsics.checkNotNull(matchPositionManager);
            matchPositionManager.clearMatchItem();
            DefaultLogger.i("HomePageFragment", "scrollToPositionWithOffset:clearMatchItem");
        } else {
            MatchPositionManager matchPositionManager2 = this.mMatchPositionManager;
            Intrinsics.checkNotNull(matchPositionManager2);
            matchPositionManager2.scrollToMatchItemPos(this.mViewMode);
            DefaultLogger.i("HomePageFragment", " mMatchPositionManager!!.scrollToMatchItemPos(mViewMode)");
        }
    }

    public final void refreshSwitchView(List<? extends IRecord> list) {
        if (getContext() == null) {
            return;
        }
        EditableListViewWrapper editableListViewWrapper = this.mHomeGridViewWrapper;
        Intrinsics.checkNotNull(editableListViewWrapper);
        if (!editableListViewWrapper.isInActionMode()) {
            EmptyPage emptyPage = this.mEmptyView;
            if (emptyPage != null && emptyPage.getActionButtonVisible() == 0) {
                SwitchViewWrapper switchViewWrapper = this.mSwitchViewWrapper;
                Intrinsics.checkNotNull(switchViewWrapper);
                switchViewWrapper.hideSwitchView();
            } else {
                SwitchViewWrapper switchViewWrapper2 = this.mSwitchViewWrapper;
                Intrinsics.checkNotNull(switchViewWrapper2);
                switchViewWrapper2.lambda$showSwitchViewDelay$0();
            }
        }
        if (this.mSwitchView != null && this.mIsFirstStatSwitch) {
            AutoTracking.trackView("403.1.9.1.12591", getPageTip(), this.mIsSwitchAllPhotos ? "all" : "camera");
            this.mIsFirstStatSwitch = false;
        }
        DiscoverChangeManager discoverChangeManager = this.mDiscoverWidgetManager;
        if (discoverChangeManager == null) {
            return;
        }
        discoverChangeManager.refreshDiscoveryView();
    }

    public final void saveHomePageIds(List<? extends IRecord> list) {
        ArrayList arrayList = new ArrayList(128);
        int i = 0;
        for (IRecord iRecord : list) {
            if (i >= 128) {
                break;
            } else if (iRecord instanceof IMediaSnapshot) {
                arrayList.add(Long.valueOf(iRecord.getId()));
                i++;
            }
        }
        GalleryPreferences.HomePage.setHomePageImageIds(TextUtils.join(",", arrayList));
    }

    public final void configRecycledViewPool() {
        InterceptableRecyclerView interceptableRecyclerView = this.mHomeGridView;
        Intrinsics.checkNotNull(interceptableRecyclerView);
        RecyclerView.RecycledViewPool recycledViewPool = interceptableRecyclerView.getRecycledViewPool();
        Intrinsics.checkNotNullExpressionValue(recycledViewPool, "mHomeGridView!!.recycledViewPool");
        int i = 5;
        recycledViewPool.setMaxRecycledViews(1, BaseBuildUtil.isLowRamDevice() ? 5 : 12);
        recycledViewPool.setMaxRecycledViews(3, BaseBuildUtil.isLowRamDevice() ? 2 : 5);
        if (BaseBuildUtil.isLowRamDevice()) {
            i = 2;
        }
        recycledViewPool.setMaxRecycledViews(-2147483646, i);
    }

    public final void setPictureViewMode(PictureViewMode pictureViewMode) {
        BuildersKt__Builders_commonKt.launch$default(LifecycleOwnerKt.getLifecycleScope(this), Dispatchers.getMain().mo2585getImmediate(), null, new HomePageFragment$setPictureViewMode$1(this, pictureViewMode, null), 2, null);
    }

    public final EditableListViewWrapper.DragDataProvider getDragDataProvider() {
        final HomePageAdapter2 homePageAdapter2 = this.mHomePageAdapter;
        if (homePageAdapter2 == null) {
            return null;
        }
        if (this.mDragDataProvider == null) {
            this.mDragDataProvider = new EditableListViewWrapper.DragDataProvider() { // from class: com.miui.gallery.ui.HomePageFragment$dragDataProvider$1
                @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.DragDataProvider
                public String getBestPathFromPosition(int i) {
                    return HomePageAdapter2.this.getBestQualityPath(i);
                }

                @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.DragDataProvider
                public String getMimeTypeFromPosition(int i) {
                    return HomePageAdapter2.this.getMimeType(i);
                }

                @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.DragDataProvider
                public boolean shouldStartDragInPhone() {
                    boolean z;
                    if (Build.VERSION.SDK_INT < 29) {
                        return false;
                    }
                    z = this.mIsInMultiWindowMode;
                    return z || ActivityCompat.isInFreeFormWindow(this.getActivity());
                }
            };
        }
        return this.mDragDataProvider;
    }

    public final void onStartup() {
        RecyclerViewPreloader recyclerViewPreloader;
        AppCompatActivity appCompatActivity = this.mActivity;
        if (appCompatActivity == null || appCompatActivity.isDestroyed()) {
            return;
        }
        try {
            Trace.beginSection("HomePageFrag#onStartup");
            CompositeDisposable compositeDisposable = this.mDisposable;
            Observable<R> map = this.mSnapshotPublisher.observeOn(GallerySchedulers.misc()).map(HomePageFragment$$ExternalSyntheticLambda11.INSTANCE);
            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
            compositeDisposable.add(map.delay(350L, timeUnit, GallerySchedulers.misc()).throttleLatest(3000L, timeUnit, GallerySchedulers.misc(), true).map(new HomePageStartupHelper2.SnapshotFunction()).subscribe(Functions.emptyConsumer(), HomePageFragment$$ExternalSyntheticLambda9.INSTANCE));
            this.mDisposable.add(this.mPhotoLoaderPublisher.throttleLatest(100L, timeUnit, GallerySchedulers.misc(), true).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.ui.HomePageFragment$$ExternalSyntheticLambda7
                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    HomePageFragment.m1458$r8$lambda$uWdmz72NEZ5GMWBWAGSl0qdgQ0(HomePageFragment.this, (List) obj);
                }
            }));
            this.mDisposable.add(this.mComputePhotoCountPublisher.observeOn(GallerySchedulers.misc()).map(new Function() { // from class: com.miui.gallery.ui.HomePageFragment$$ExternalSyntheticLambda10
                @Override // io.reactivex.functions.Function
                /* renamed from: apply */
                public final Object mo2564apply(Object obj) {
                    return HomePageFragment.$r8$lambda$W6FzE2ldzQfS7hS4Rh3BH96IQDU(HomePageFragment.this, (List) obj);
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.ui.HomePageFragment$$ExternalSyntheticLambda8
                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    HomePageFragment.$r8$lambda$928B3jYIDwLUtVkCFeB8lo8rmgs(HomePageFragment.this, (int[]) obj);
                }
            }));
            INestedTwoStageDrawer iNestedTwoStageDrawer = this.mDrawer;
            Intrinsics.checkNotNull(iNestedTwoStageDrawer);
            iNestedTwoStageDrawer.addScrollerStateListener(new InestedScrollerStateListener() { // from class: com.miui.gallery.ui.HomePageFragment$onStartup$6
                public int bottomPadding;
                public boolean isShowImmerse;
                public int scrollerBottomMargin;

                {
                    this.bottomPadding = HomePageFragment.this.getResources().getDimensionPixelSize(R.dimen.safe_distance_bottom);
                    this.scrollerBottomMargin = HomePageFragment.this.getResources().getDimensionPixelSize(R.dimen.fast_scroller_safe_distance_bottom);
                }

                public final int getBottomPadding() {
                    return this.bottomPadding;
                }

                public final int getScrollerBottomMargin() {
                    return this.scrollerBottomMargin;
                }

                @Override // com.miui.gallery.widget.tsd.InestedScrollerStateListener
                public void onScrollerStateChanged(DrawerState state, int i) {
                    int dimensionPixelOffset;
                    Intrinsics.checkNotNullParameter(state, "state");
                    EditableListViewWrapper editableListViewWrapper = HomePageFragment.this.mHomeGridViewWrapper;
                    Intrinsics.checkNotNull(editableListViewWrapper);
                    int i2 = 0;
                    if (editableListViewWrapper.isInActionMode() && BaseBuildUtil.isLargeScreenDevice() && HomePageFragment.this.mActionBarHelper != null) {
                        HomePageActivity.HomeTabActionBarHelper homeTabActionBarHelper = HomePageFragment.this.mActionBarHelper;
                        Intrinsics.checkNotNull(homeTabActionBarHelper);
                        i2 = 0 + homeTabActionBarHelper.getActionBarHeight();
                    }
                    if (state == DrawerState.CLOSE) {
                        InterceptableRecyclerView interceptableRecyclerView = HomePageFragment.this.mHomeGridView;
                        Intrinsics.checkNotNull(interceptableRecyclerView);
                        int paddingLeft = interceptableRecyclerView.getPaddingLeft();
                        int top = interceptableRecyclerView.getTop();
                        Context context = interceptableRecyclerView.getContext();
                        Intrinsics.checkNotNullExpressionValue(context, "context");
                        interceptableRecyclerView.setPadding(paddingLeft, top - context.getResources().getDimensionPixelOffset(R.dimen.time_line_header_padding_top), interceptableRecyclerView.getPaddingRight(), getBottomPadding() + i2);
                        interceptableRecyclerView.setFastScrollerBottomMargin(getScrollerBottomMargin() + i2);
                        HomePageFragment.this.mTagProportionChanged = true;
                    } else if (state == DrawerState.HALF_OPEN || state == DrawerState.OPEN) {
                        InterceptableRecyclerView interceptableRecyclerView2 = HomePageFragment.this.mHomeGridView;
                        Intrinsics.checkNotNull(interceptableRecyclerView2);
                        interceptableRecyclerView2.setPadding(interceptableRecyclerView2.getPaddingLeft(), interceptableRecyclerView2.getTop(), interceptableRecyclerView2.getPaddingRight(), getBottomPadding() + i + i2);
                        interceptableRecyclerView2.setFastScrollerBottomMargin(getScrollerBottomMargin() + i + i2);
                        HomePageFragment.this.mTagProportionChanged = true;
                    }
                    if (HomePageFragment.this.mActionBarHelper != null) {
                        HomePageActivity.HomeTabActionBarHelper homeTabActionBarHelper2 = HomePageFragment.this.mActionBarHelper;
                        Intrinsics.checkNotNull(homeTabActionBarHelper2);
                        boolean isShowImmerse = homeTabActionBarHelper2.isShowImmerse();
                        if (this.isShowImmerse == isShowImmerse) {
                            return;
                        }
                        if (isShowImmerse) {
                            Context context2 = HomePageFragment.this.getContext();
                            Intrinsics.checkNotNull(context2);
                            Intrinsics.checkNotNullExpressionValue(context2, "context!!");
                            dimensionPixelOffset = context2.getResources().getDimensionPixelOffset(R.dimen.immerse_fast_scroller_margin_top_to_time_line);
                        } else {
                            Context context3 = HomePageFragment.this.getContext();
                            Intrinsics.checkNotNull(context3);
                            Intrinsics.checkNotNullExpressionValue(context3, "context!!");
                            dimensionPixelOffset = context3.getResources().getDimensionPixelOffset(R.dimen.fast_scroller_margin_top_to_time_line);
                        }
                        HomePageFragment homePageFragment = HomePageFragment.this;
                        Context context4 = homePageFragment.getContext();
                        Intrinsics.checkNotNull(context4);
                        Intrinsics.checkNotNullExpressionValue(context4, "context!!");
                        int dimensionPixelOffset2 = context4.getResources().getDimensionPixelOffset(R.dimen.time_line_header_height) + dimensionPixelOffset;
                        Context context5 = HomePageFragment.this.getContext();
                        Intrinsics.checkNotNull(context5);
                        Intrinsics.checkNotNullExpressionValue(context5, "context!!");
                        homePageFragment.mFastScrollerMarginTop = dimensionPixelOffset2 - context5.getResources().getDimensionPixelOffset(R.dimen.time_line_header_padding_top);
                        InterceptableRecyclerView interceptableRecyclerView3 = HomePageFragment.this.mHomeGridView;
                        Intrinsics.checkNotNull(interceptableRecyclerView3);
                        interceptableRecyclerView3.setFastScrollerTopMargin(HomePageFragment.this.mFastScrollerMarginTop);
                        HomePageFragment.this.mTagProportionChanged = true;
                        this.isShowImmerse = isShowImmerse;
                    }
                }

                @Override // com.miui.gallery.widget.tsd.InestedScrollerStateListener
                public void onScrollerUpdate(DrawerState state, int i, int i2) {
                    Intrinsics.checkNotNullParameter(state, "state");
                    HomePageActivity.HomeTabActionBarHelper homeTabActionBarHelper = HomePageFragment.this.mActionBarHelper;
                    if (homeTabActionBarHelper == null) {
                        return;
                    }
                    homeTabActionBarHelper.refreshTopBar(RangesKt___RangesKt.coerceIn((i + i2) / i2, 0.0f, 1.0f));
                }
            });
            this.mIsInMultiWindowMode = ActivityCompat.isInMultiWindowMode(this.mActivity);
            addTopBar();
            addScreenChangeListener(new IScreenChange.OnLargeScreenChangeListener() { // from class: com.miui.gallery.ui.HomePageFragment$onStartup$7
                @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
                public void onCreatedWhileLargeDevice(ScreenSize screenSize) {
                    INestedTwoStageDrawer access$getMDrawer$p = HomePageFragment.access$getMDrawer$p(HomePageFragment.this);
                    Intrinsics.checkNotNull(access$getMDrawer$p);
                    HomePageFragment homePageFragment = HomePageFragment.this;
                    if (BaseBuildUtil.isLargeHorizontalWindow()) {
                        access$getMDrawer$p.setHeaderView(HomePageFragment.access$getMPlaceHolderView$p(homePageFragment));
                        access$getMDrawer$p.setStickEnable(false);
                        access$getMDrawer$p.setSubHeaderView(HomePageFragment.access$getMPanelBar$p(homePageFragment));
                        access$getMDrawer$p.setDrawerState(DrawerState.HALF_OPEN, false, null);
                        return;
                    }
                    access$getMDrawer$p.setHeaderView(HomePageFragment.access$getMPanelBar$p(homePageFragment));
                    access$getMDrawer$p.setDrawerState(HomePageFragment.DEFAULT_DRAWER_STATE, false, null);
                }

                @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
                public void onCreatedWhileNormalDevice(ScreenSize screenSize) {
                    INestedTwoStageDrawer access$getMDrawer$p = HomePageFragment.access$getMDrawer$p(HomePageFragment.this);
                    Intrinsics.checkNotNull(access$getMDrawer$p);
                    HomePageFragment homePageFragment = HomePageFragment.this;
                    access$getMDrawer$p.setDrawerState(HomePageFragment.DEFAULT_DRAWER_STATE, false, null);
                    access$getMDrawer$p.setHeaderView(HomePageFragment.access$getMPanelBar$p(homePageFragment));
                }

                @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
                public void onScreenSizeToLargeOrNormal(ScreenSize screenSize) {
                    INestedTwoStageDrawer access$getMDrawer$p = HomePageFragment.access$getMDrawer$p(HomePageFragment.this);
                    Intrinsics.checkNotNull(access$getMDrawer$p);
                    HomePageFragment homePageFragment = HomePageFragment.this;
                    Intrinsics.checkNotNull(screenSize);
                    if (screenSize.isWindowHorizontalLarge()) {
                        access$getMDrawer$p.setHeaderView(HomePageFragment.access$getMPlaceHolderView$p(homePageFragment));
                        access$getMDrawer$p.setSubHeaderView(HomePageFragment.access$getMPanelBar$p(homePageFragment));
                        access$getMDrawer$p.setStickEnable(false);
                    } else {
                        access$getMDrawer$p.removeHeaderView(HomePageFragment.access$getMPlaceHolderView$p(homePageFragment));
                        access$getMDrawer$p.removeSubHeaderView(HomePageFragment.access$getMPanelBar$p(homePageFragment));
                        access$getMDrawer$p.setHeaderView(HomePageFragment.access$getMPanelBar$p(homePageFragment));
                        access$getMDrawer$p.setStickEnable(true);
                    }
                    HomePageFragment.access$refreshSearchIconVisible(homePageFragment);
                }
            });
            configRecycledViewPool();
            final InterceptableRecyclerView interceptableRecyclerView = this.mHomeGridView;
            Intrinsics.checkNotNull(interceptableRecyclerView);
            interceptableRecyclerView.setItemViewCacheSize(12);
            interceptableRecyclerView.setOnDragListener(new ViewDragListener(this));
            interceptableRecyclerView.setClickEventInterceptCallback(new ClickEventInterceptCallback() { // from class: com.miui.gallery.ui.HomePageFragment$$ExternalSyntheticLambda5
                @Override // com.miui.gallery.widget.recyclerview.ClickEventInterceptCallback
                public final int onInterceptLongClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                    return HomePageFragment.$r8$lambda$ngAKaMRzf4ytlIR54Su2oK9IFDs(HomePageFragment.this, recyclerView, view, i, j, f, f2);
                }
            });
            interceptableRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() { // from class: com.miui.gallery.ui.HomePageFragment$$ExternalSyntheticLambda2
                @Override // androidx.recyclerview.widget.RecyclerView.RecyclerListener
                public final void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
                    HomePageFragment.m1457$r8$lambda$ZzA2GBpevmb4cnzCPydNhgjS3Y(HomePageFragment.this, viewHolder);
                }
            });
            boolean z = true;
            interceptableRecyclerView.setFastScrollEnabled(true);
            interceptableRecyclerView.setOnFastScrollerStateChangedListener(new FastScrollerBar.OnStateChangedListener() { // from class: com.miui.gallery.ui.HomePageFragment$onStartup$8$3
                public int preState;

                @Override // com.miui.gallery.widget.recyclerview.FastScrollerBar.OnStateChangedListener
                public void onStateChanged(int i) {
                    if (i == 2 && HomePageFragment.this.mHomeGridViewWrapper != null) {
                        EditableListViewWrapper editableListViewWrapper = HomePageFragment.this.mHomeGridViewWrapper;
                        Intrinsics.checkNotNull(editableListViewWrapper);
                        editableListViewWrapper.reductionTouchView();
                        TrackController.trackClick("403.1.2.1.9883", AutoTracking.getRef());
                    }
                    if (HomePageFragment.this.mSwitchViewWrapper != null) {
                        EditableListViewWrapper editableListViewWrapper2 = HomePageFragment.this.mHomeGridViewWrapper;
                        Intrinsics.checkNotNull(editableListViewWrapper2);
                        if (!editableListViewWrapper2.isInActionMode()) {
                            int i2 = this.preState;
                            if (i2 == 3 && i != 3) {
                                SwitchViewWrapper switchViewWrapper = HomePageFragment.this.mSwitchViewWrapper;
                                Intrinsics.checkNotNull(switchViewWrapper);
                                switchViewWrapper.showSwitchViewByAnim(2000);
                            } else if (i2 != 3 && i == 3) {
                                SwitchViewWrapper switchViewWrapper2 = HomePageFragment.this.mSwitchViewWrapper;
                                Intrinsics.checkNotNull(switchViewWrapper2);
                                switchViewWrapper2.hideSwitchViewByAnim();
                            }
                        }
                    }
                    this.preState = i;
                }
            });
            interceptableRecyclerView.setFastScrollerTopMargin(this.mFastScrollerMarginTop);
            interceptableRecyclerView.setFastScrollerCapsuleViewProvider(new FastScrollerCapsuleViewProvider() { // from class: com.miui.gallery.ui.HomePageFragment$onStartup$8$4
                @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsuleViewProvider
                public boolean isShowCapsule() {
                    return true;
                }

                @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsuleViewProvider
                public FastScrollerCapsule createFastScrollerCapsule() {
                    FastScrollerTimeCapsuleView fastScrollerTimeCapsuleView = new FastScrollerTimeCapsuleView(InterceptableRecyclerView.this.getContext());
                    fastScrollerTimeCapsuleView.setStyle(R.style.FastScrollTimeCapsule);
                    return fastScrollerTimeCapsuleView;
                }
            });
            interceptableRecyclerView.setCapsuleCalculator(this.mHomePageAdapter);
            interceptableRecyclerView.setProportionTagAdapterProvider(new ProportionTagAdapterProvider<Integer>() { // from class: com.miui.gallery.ui.HomePageFragment$onStartup$8$5
                @Override // com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider
                public boolean isShowProportionTag() {
                    return InterceptableRecyclerView.this.getResources().getConfiguration().orientation != 2 && !PictureViewMode.isYearMode(HomePageFragment.access$getMViewMode$p(this));
                }

                @Override // com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider
                public ProportionTagBaseAdapter<Integer> createTagAdapter() {
                    if (HomePageFragment.access$getMTagAdapter$p(this) == null) {
                        HomePageFragment.access$setMTagAdapter$p(this, new ProportionStringTagAdapter(InterceptableRecyclerView.this.getContext()));
                    }
                    ProportionTagBaseAdapter<Integer> access$getMTagAdapter$p = HomePageFragment.access$getMTagAdapter$p(this);
                    Intrinsics.checkNotNull(access$getMTagAdapter$p);
                    return access$getMTagAdapter$p;
                }

                @Override // com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider
                public boolean isProportionTagChanged() {
                    return HomePageFragment.access$getMTagProportionChanged$p(this);
                }

                @Override // com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider
                public List<ProportionTagModel<Integer>> getProportionTagModel() {
                    HomePageFragment.access$setMTagProportionChanged$p(this, false);
                    HomePageAdapter2 access$getMHomePageAdapter$p = HomePageFragment.access$getMHomePageAdapter$p(this);
                    Intrinsics.checkNotNull(access$getMHomePageAdapter$p);
                    InterceptableRecyclerView access$getMHomeGridView$p = HomePageFragment.access$getMHomeGridView$p(this);
                    Intrinsics.checkNotNull(access$getMHomeGridView$p);
                    return access$getMHomePageAdapter$p.calculateTagProportionList(access$getMHomeGridView$p.getWidth());
                }
            });
            EditableListViewWrapper editableListViewWrapper = this.mHomeGridViewWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            HomePageAdapter2 homePageAdapter2 = this.mHomePageAdapter;
            Intrinsics.checkNotNull(homePageAdapter2);
            if (Config$ThumbConfig.get().sPreloadNum == 0) {
                recyclerViewPreloader = null;
            } else {
                HomePageAdapter2 homePageAdapter22 = this.mHomePageAdapter;
                Intrinsics.checkNotNull(homePageAdapter22);
                recyclerViewPreloader = new RecyclerViewPreloader(this, this, homePageAdapter22, Config$ThumbConfig.get().sPreloadNum);
            }
            editableListViewWrapper.setOnScrollListener(homePageAdapter2.generateWrapScrollListener(recyclerViewPreloader));
            editableListViewWrapper.enableChoiceMode(true);
            editableListViewWrapper.setHandleTouchAnimItemType(MicroThumbGridItem.class.getSimpleName());
            editableListViewWrapper.enterChoiceModeWithLongClick(true);
            editableListViewWrapper.setMultiChoiceModeListener(this.choiceModeListener);
            editableListViewWrapper.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.ui.HomePageFragment$$ExternalSyntheticLambda6
                @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
                public final boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                    return HomePageFragment.$r8$lambda$BrsEnIbFw6ULBd4Ywos1P0ovcGM(HomePageFragment.this, recyclerView, view, i, j, f, f2);
                }
            });
            HomePageActivity.HomeTabActionBarHelper homeTabActionBarHelper = this.mActionBarHelper;
            if (homeTabActionBarHelper != null) {
                homeTabActionBarHelper.setupActionBar();
            }
            this.mMatchPositionManager = new MatchPositionManager(this.mHomeGridView, this.mHomePageAdapter, new MatchPositionManager.MatchPositionCallBack() { // from class: com.miui.gallery.ui.HomePageFragment$$ExternalSyntheticLambda3
                @Override // com.miui.gallery.ui.pictures.MatchPositionManager.MatchPositionCallBack
                public final int[] unpackGroupIndexAndOffset(int i) {
                    return HomePageFragment.$r8$lambda$LaDwq2lftnBR9Yt1ADvErwYVBfk(HomePageFragment.this, i);
                }
            });
            this.mSyncDownloadManager = new SyncDownloadManager(getActivity(), this.mDrawer);
            addScreenChangeListener(new SwitchManager(this));
            DiscoverChangeManager discoverChangeManager = new DiscoverChangeManager(getActivity(), this.mActionBarHelper, this.mSwitchView);
            this.mDiscoverWidgetManager = discoverChangeManager;
            addScreenChangeListener(discoverChangeManager);
            DiscoverChangeManager discoverChangeManager2 = this.mDiscoverWidgetManager;
            Intrinsics.checkNotNull(discoverChangeManager2);
            if (this.mIsSwitchAllPhotos) {
                z = false;
            }
            discoverChangeManager2.setDiscoverDotShowEnable(z);
            SwitchViewWrapper switchViewWrapper = this.mSwitchViewWrapper;
            Intrinsics.checkNotNull(switchViewWrapper);
            DiscoverChangeManager discoverChangeManager3 = this.mDiscoverWidgetManager;
            Intrinsics.checkNotNull(discoverChangeManager3);
            switchViewWrapper.setDiscoverDot(discoverChangeManager3.getDiscoveryDot());
            registerSearchStatusObserver();
            if (this.mStartCalled) {
                SyncDownloadManager syncDownloadManager = this.mSyncDownloadManager;
                Intrinsics.checkNotNull(syncDownloadManager);
                syncDownloadManager.onAppFocused();
                SyncDownloadManager syncDownloadManager2 = this.mSyncDownloadManager;
                Intrinsics.checkNotNull(syncDownloadManager2);
                syncDownloadManager2.onResume();
            }
            EditableListViewWrapper editableListViewWrapper2 = this.mHomeGridViewWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper2);
            editableListViewWrapper2.setOnTouchCallback(new EditableListViewWrapper.OnTouchCallBack() { // from class: com.miui.gallery.ui.HomePageFragment$$ExternalSyntheticLambda4
                @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.OnTouchCallBack
                public final void onTouch(View view, MotionEvent motionEvent) {
                    HomePageFragment.m1463onStartup$lambda13(HomePageFragment.this, view, motionEvent);
                }
            });
            getLiveData().observe(this, new Observer() { // from class: com.miui.gallery.ui.HomePageFragment$$ExternalSyntheticLambda1
                @Override // androidx.lifecycle.Observer
                public final void onChanged(Object obj) {
                    HomePageFragment.$r8$lambda$X2oBZBw3ZTUZCBSu9DQEhMJYWqo(HomePageFragment.this, (List) obj);
                }
            });
            this.mHomePagePhotoLoaderCallback = new HomePagePhotoLoaderCallback(this);
            LoaderManager loaderManager = LoaderManager.getInstance(this);
            HomePagePhotoLoaderCallback homePagePhotoLoaderCallback = this.mHomePagePhotoLoaderCallback;
            Intrinsics.checkNotNull(homePagePhotoLoaderCallback);
            loaderManager.initLoader(2, null, homePagePhotoLoaderCallback);
            this.mPinchManager = PinchManager.install(this.mHomeGridView, this.mHomePageAdapter, new PinchCallback() { // from class: com.miui.gallery.ui.HomePageFragment$onStartup$13
                @Override // com.miui.gallery.ui.pictures.PinchCallback
                public void onTransitionUpdate(float f) {
                }

                @Override // com.miui.gallery.ui.pictures.PinchCallback
                public boolean isPinchAvailable() {
                    EditableListViewWrapper editableListViewWrapper3 = HomePageFragment.this.mHomeGridViewWrapper;
                    Intrinsics.checkNotNull(editableListViewWrapper3);
                    return !editableListViewWrapper3.isInActionMode();
                }

                @Override // com.miui.gallery.ui.pictures.PinchCallback
                public boolean isTargetModeReachable(PictureViewMode targetMode) {
                    Intrinsics.checkNotNullParameter(targetMode, "targetMode");
                    HomePageAdapter2 homePageAdapter23 = HomePageFragment.this.mHomePageAdapter;
                    Intrinsics.checkNotNull(homePageAdapter23);
                    List<IRecord> currentList = homePageAdapter23.getCurrentList();
                    int clusterKey = targetMode.getClusterKey();
                    if (currentList instanceof ClusteredList) {
                        return ((ClusteredList) currentList).hasCluster(clusterKey);
                    }
                    return false;
                }

                @Override // com.miui.gallery.ui.pictures.PinchCallback
                public int[] unpackGroupIndexAndOffset(int i) {
                    EditableListViewWrapper editableListViewWrapper3 = HomePageFragment.this.mHomeGridViewWrapper;
                    Intrinsics.checkNotNull(editableListViewWrapper3);
                    int[] unpackGroupedPosition = editableListViewWrapper3.unpackGroupedPosition(i);
                    Intrinsics.checkNotNullExpressionValue(unpackGroupedPosition, "mHomeGridViewWrapper!!.u…Position(adapterPosition)");
                    return unpackGroupedPosition;
                }

                @Override // com.miui.gallery.ui.pictures.PinchCallback
                public void onPictureViewModeChanged(PictureViewMode viewMode) {
                    Intrinsics.checkNotNullParameter(viewMode, "viewMode");
                    HomePageFragment.this.setPictureViewMode(viewMode);
                }

                @Override // com.miui.gallery.ui.pictures.PinchCallback
                public void onPreTransition() {
                    HomePageInteractionCallback homePageInteractionCallback;
                    InterceptableRecyclerView interceptableRecyclerView2 = HomePageFragment.this.mHomeGridView;
                    if (interceptableRecyclerView2 != null) {
                        interceptableRecyclerView2.setItemAnimator(null);
                    }
                    homePageInteractionCallback = HomePageFragment.this.mInteractionCallback;
                    if (homePageInteractionCallback == null) {
                        return;
                    }
                    homePageInteractionCallback.setPagerDraggable(false);
                }

                @Override // com.miui.gallery.ui.pictures.PinchCallback
                public void onTransitionFinish(boolean z2) {
                    RecyclerViewUtils.setChildVisibility(HomePageFragment.this.mHomeGridView, 0);
                }

                @Override // com.miui.gallery.ui.pictures.PinchCallback
                public void onPostTransition() {
                    HomePageInteractionCallback homePageInteractionCallback;
                    InterceptableRecyclerView interceptableRecyclerView2;
                    if (!HomePageFragment.this.mViewMode.isAggregated() && (interceptableRecyclerView2 = HomePageFragment.this.mHomeGridView) != null) {
                        interceptableRecyclerView2.setItemAnimator(HomePageFragment.this.rvAnimator);
                    }
                    homePageInteractionCallback = HomePageFragment.this.mInteractionCallback;
                    if (homePageInteractionCallback == null) {
                        return;
                    }
                    homePageInteractionCallback.setPagerDraggable(true);
                }
            }, this.mViewMode);
        } finally {
            Trace.endSection();
        }
    }

    /* renamed from: onStartup$lambda-3 */
    public static final HomePageStartupHelper2.Snapshot m1465onStartup$lambda3(List it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return HomePageStartupHelper2.Snapshot.Companion.build(it);
    }

    /* renamed from: onStartup$lambda-5 */
    public static final void m1467onStartup$lambda5(HomePageFragment this$0, List list) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        BuildersKt__Builders_commonKt.launch$default(LifecycleOwnerKt.getLifecycleScope(this$0), null, null, new HomePageFragment$onStartup$3$1(list, this$0, null), 3, null);
    }

    /* renamed from: onStartup$lambda-6 */
    public static final void m1468onStartup$lambda6(HomePageFragment this$0, int[] iArr) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        SyncDownloadManager syncDownloadManager = this$0.mSyncDownloadManager;
        if (syncDownloadManager == null) {
            return;
        }
        syncDownloadManager.setPhotoCountAndVideosCount(iArr[0], iArr[1]);
    }

    /* renamed from: onStartup$lambda-9$lambda-7 */
    public static final int m1469onStartup$lambda9$lambda7(HomePageFragment this$0, RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (!this$0.mViewMode.isAggregated()) {
            PinchManager pinchManager = this$0.mPinchManager;
            if (pinchManager == null) {
                return 2;
            }
            Intrinsics.checkNotNull(pinchManager);
            return pinchManager.isTransiting() ? 1 : 2;
        }
        return 1;
    }

    /* renamed from: onStartup$lambda-9$lambda-8 */
    public static final void m1470onStartup$lambda9$lambda8(HomePageFragment this$0, RecyclerView.ViewHolder holder) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(holder, "holder");
        View findViewById = holder.itemView.findViewById(R.id.micro_thumb);
        if (findViewById != null) {
            this$0.getMRequestManager().clear(findViewById);
        }
    }

    /* renamed from: onStartup$lambda-11$lambda-10 */
    public static final boolean m1461onStartup$lambda11$lambda10(HomePageFragment this$0, RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        PinchManager pinchManager;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (this$0.mViewMode.isAggregated() && (pinchManager = this$0.mPinchManager) != null) {
            Intrinsics.checkNotNull(pinchManager);
            pinchManager.zoomInBy(f + view.getLeft() + view.getTranslationX(), f2 + view.getTop() + view.getTranslationY());
            return true;
        }
        PhotoPageIntent.Builder initPosition = new PhotoPageIntent.Builder(this$0, InternalPhotoPageActivity.class).setAdapterView(recyclerView).setUri(PHOTOS_PAGE_URI).setInitPosition(i);
        HomePageAdapter2 homePageAdapter2 = this$0.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter2);
        initPosition.setCount(homePageAdapter2.getItemCount()).setSelection(HomeMediaLoader.getHomePageSelection(this$0.mIsSwitchAllPhotos)).setOrderBy("alias_sort_time DESC ").setImageLoadParams(ImageLoadParamsKt.ImageLoadParams(new HomePageFragment$onStartup$9$1$1(this$0, i))).build().gotoPhotoPage();
        HashMap hashMap = new HashMap();
        hashMap.put("from", this$0.getPageName());
        hashMap.put("position", String.valueOf(i));
        SamplingStatHelper.recordCountEvent("photo", "click_micro_thumb", hashMap);
        HashMap hashMap2 = new HashMap();
        String ref = AutoTracking.getRef();
        Intrinsics.checkNotNullExpressionValue(ref, "getRef()");
        hashMap2.put("ref_tip", ref);
        hashMap2.put("tip", "403.1.2.1.9882");
        hashMap2.put("position", String.valueOf(i));
        TrackController.trackClick(hashMap2);
        return true;
    }

    /* renamed from: onStartup$lambda-12 */
    public static final int[] m1462onStartup$lambda12(HomePageFragment this$0, int i) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        EditableListViewWrapper editableListViewWrapper = this$0.mHomeGridViewWrapper;
        Intrinsics.checkNotNull(editableListViewWrapper);
        return editableListViewWrapper.unpackGroupedPosition(i);
    }

    /* renamed from: onStartup$lambda-13 */
    public static final void m1463onStartup$lambda13(HomePageFragment this$0, View view, MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        SyncDownloadManager syncDownloadManager = this$0.mSyncDownloadManager;
        Intrinsics.checkNotNull(syncDownloadManager);
        syncDownloadManager.setPanelItemAutoShowEnable(false);
    }

    /* renamed from: onStartup$lambda-15 */
    public static final void m1464onStartup$lambda15(HomePageFragment this$0, List list) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (list == null) {
            return;
        }
        this$0.mPhotoLoaderPublisher.onNext(list);
    }

    public final void addTopBar() {
        final PanelBar panelBar = new PanelBar(this.mActivity);
        panelBar.setId(R.id.home_page_top_bar);
        panelBar.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
        this.mPanelBar = panelBar;
        INestedTwoStageDrawer iNestedTwoStageDrawer = this.mDrawer;
        Objects.requireNonNull(iNestedTwoStageDrawer, "null cannot be cast to non-null type com.miui.gallery.widget.tsd.NestedTwoStageDrawer");
        ((NestedTwoStageDrawer) iNestedTwoStageDrawer).getSpringBackLayout().addOnScrollListener(new SpringBackLayout.OnScrollListener() { // from class: com.miui.gallery.ui.HomePageFragment$addTopBar$1
            @Override // miuix.springback.view.SpringBackLayout.OnScrollListener
            public void onStateChanged(int i, int i2, boolean z) {
            }

            @Override // miuix.springback.view.SpringBackLayout.OnScrollListener
            public void onScrolled(SpringBackLayout springBackLayout, int i, int i2) {
                PanelBar panelBar2 = PanelBar.this;
                panelBar2.setY((float) (panelBar2.getY() - (i2 / 2.0d)));
            }
        });
    }

    public final void addHeaderView() {
        if (this.mPlaceHolderView == null) {
            this.mPlaceHolderView = new View(this.mActivity);
        }
        Context context = getContext();
        Intrinsics.checkNotNull(context);
        Intrinsics.checkNotNullExpressionValue(context, "context!!");
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, context.getResources().getDimensionPixelSize(R.dimen.home_tab_place_holder_height));
        View view = this.mPlaceHolderView;
        Intrinsics.checkNotNull(view);
        view.setLayoutParams(layoutParams);
    }

    public final void statSwitchViewClick(String str, int i) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.1.9.1.12592");
        hashMap.put("ref_tip", getPageTip());
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str);
        hashMap.put(MiStat.Param.COUNT, Integer.valueOf(i));
        TrackController.trackClick(hashMap);
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        unregisterSearchStatusObserver();
        EmptyPage emptyPage = this.mEmptyView;
        if (emptyPage != null) {
            emptyPage.destroyMaml();
        }
        super.onDestroyView();
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment
    public List<Loader<?>> getLoaders() {
        ArrayList arrayList = new ArrayList();
        try {
            arrayList.add(LoaderManager.getInstance(this).getLoader(2));
        } catch (IllegalStateException e) {
            DefaultLogger.e("HomePageFragment", e);
        }
        return arrayList;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x002f, code lost:
        if (r0.getCurrentPosition() == 0) goto L7;
     */
    @Override // com.miui.gallery.ui.ImmersionMenuSupport
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onActionClick(int r4) {
        /*
            r3 = this;
            r0 = 16
            if (r4 != r0) goto L45
            boolean r4 = com.miui.gallery.preference.GalleryPreferences.HomePage.isHomePageShowAllPhotos()
            r0 = r4 ^ 1
            com.miui.gallery.preference.GalleryPreferences.HomePage.setHomePageShowAllPhotos(r0)
            com.miui.gallery.ui.DiscoverChangeManager r0 = r3.mDiscoverWidgetManager
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            boolean r1 = r3.mIsSwitchAllPhotos
            r2 = 1
            r1 = r1 ^ r2
            r0.setDiscoverDotShowEnable(r1)
            r4 = r4 ^ r2
            r3.mIsSwitchAllPhotos = r4
            com.miui.gallery.ui.DiscoverChangeManager r4 = r3.mDiscoverWidgetManager
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
            boolean r0 = r3.mIsSwitchAllPhotos
            r1 = 0
            if (r0 != 0) goto L32
            com.miui.gallery.activity.HomePageActivity$HomeTabActionBarHelper r0 = r3.mActionBarHelper
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            int r0 = r0.getCurrentPosition()
            if (r0 != 0) goto L32
            goto L33
        L32:
            r2 = r1
        L33:
            r4.refreshUiVisible(r2)
            r3.mDiffEnable = r1
            com.miui.gallery.ui.pictures.MatchPositionManager r4 = r3.mMatchPositionManager
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
            com.miui.gallery.ui.pictures.PictureViewMode r0 = r3.mViewMode
            r4.calculateMatchFromItem(r0)
            r3.configHomeMediaLoader()
        L45:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.HomePageFragment.onActionClick(int):void");
    }

    @Override // com.miui.gallery.ui.OnHomePageResetListener
    public void onHomePageReset() {
        EditableListViewWrapper editableListViewWrapper = this.mHomeGridViewWrapper;
        if (editableListViewWrapper != null) {
            editableListViewWrapper.stopActionMode();
        }
        InterceptableRecyclerView interceptableRecyclerView = this.mHomeGridView;
        if (interceptableRecyclerView != null) {
            interceptableRecyclerView.scrollToPosition(0);
        }
        INestedTwoStageDrawer iNestedTwoStageDrawer = this.mDrawer;
        if (iNestedTwoStageDrawer == null) {
            return;
        }
        iNestedTwoStageDrawer.setDrawerState(DrawerState.HALF_OPEN, false, null);
    }

    @Override // com.miui.gallery.ui.actionBar.TabActionBarHelper.OnTabChangeListener
    public void onTabChange(int i) {
        DiscoverChangeManager discoverChangeManager = this.mDiscoverWidgetManager;
        if (discoverChangeManager == null) {
            return;
        }
        discoverChangeManager.refreshUiVisible(!this.mIsSwitchAllPhotos && i == 0 && BaseBuildUtil.isLargeScreenDevice() && BaseBuildUtil.isLargeHorizontalWindow());
    }

    /* compiled from: HomePageFragment.kt */
    /* loaded from: classes2.dex */
    public final class HomePagePhotoLoaderCallback implements LoaderManager.LoaderCallbacks<Object> {
        public final /* synthetic */ HomePageFragment this$0;

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<Object> loader) {
            Intrinsics.checkNotNullParameter(loader, "loader");
        }

        public HomePagePhotoLoaderCallback(HomePageFragment this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<Object> onCreateLoader(int i, Bundle bundle) {
            if (i == 2) {
                DiscoveryMessageLoader createLoader = DiscoveryMessageManager.getInstance().createLoader(this.this$0.mActivity, 1);
                Objects.requireNonNull(createLoader, "null cannot be cast to non-null type androidx.loader.content.Loader<kotlin.Any?>");
                return createLoader;
            }
            throw new IllegalStateException("Illegal loader id".toString());
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<Object> loader, Object obj) {
            Intrinsics.checkNotNullParameter(loader, "loader");
            if (loader.getId() == 2) {
                DiscoverChangeManager discoverChangeManager = this.this$0.mDiscoverWidgetManager;
                Intrinsics.checkNotNull(discoverChangeManager);
                discoverChangeManager.setDiscoveryMessage(obj instanceof ArrayList ? (ArrayList) obj : null);
            }
        }
    }

    public final boolean needShowMultiViewTip() {
        HomePageAdapter2 homePageAdapter2;
        if (getUserVisibleHint() && (homePageAdapter2 = this.mHomePageAdapter) != null) {
            Intrinsics.checkNotNull(homePageAdapter2);
            if (homePageAdapter2.getItemCount() > 0 && !GalleryPreferences.MultiView.hasShownTip()) {
                return true;
            }
        }
        return false;
    }

    public final void inflateEmptyView() {
        if (this.mEmptyView == null) {
            ViewGroup viewGroup = null;
            int i = 0;
            View inflate = LayoutInflater.from(this.mActivity).inflate(R.layout.home_page_empty_view, (ViewGroup) null, false);
            Objects.requireNonNull(inflate, "null cannot be cast to non-null type com.miui.gallery.widget.EmptyPage");
            EmptyPage emptyPage = (EmptyPage) inflate;
            this.mEmptyView = emptyPage;
            Intrinsics.checkNotNull(emptyPage);
            emptyPage.setOnActionButtonClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.HomePageFragment$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    HomePageFragment.$r8$lambda$tcp0NkLXdruopdiIbHKCsuw4MvQ(HomePageFragment.this, view);
                }
            });
            if (this.mHomeGridView != null) {
                INestedTwoStageDrawer iNestedTwoStageDrawer = this.mDrawer;
                Objects.requireNonNull(iNestedTwoStageDrawer, "null cannot be cast to non-null type android.view.ViewGroup");
                ViewGroup viewGroup2 = (ViewGroup) iNestedTwoStageDrawer;
                int childCount = viewGroup2.getChildCount();
                int i2 = 0;
                while (true) {
                    if (i2 >= childCount) {
                        break;
                    }
                    int i3 = i2 + 1;
                    if (viewGroup2.getChildAt(i2) == this.mHomeGridView) {
                        i = i3;
                        break;
                    }
                    i2 = i3;
                }
            }
            INestedTwoStageDrawer iNestedTwoStageDrawer2 = this.mDrawer;
            if (iNestedTwoStageDrawer2 instanceof ViewGroup) {
                viewGroup = (ViewGroup) iNestedTwoStageDrawer2;
            }
            if (viewGroup != null) {
                viewGroup.addView(this.mEmptyView, i);
            }
            ViewDragListener viewDragListener = new ViewDragListener(this);
            EmptyPage emptyPage2 = this.mEmptyView;
            Intrinsics.checkNotNull(emptyPage2);
            emptyPage2.setOnDragListener(viewDragListener);
            EditableListViewWrapper editableListViewWrapper = this.mHomeGridViewWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            editableListViewWrapper.setEmptyView(this.mEmptyView);
        }
    }

    /* renamed from: inflateEmptyView$lambda-16 */
    public static final void m1460inflateEmptyView$lambda16(HomePageFragment this$0, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        IntentUtil.startCameraActivity(this$0.getActivity());
    }

    public final void callbackStartup() {
        ThreadManager.Companion.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.HomePageFragment$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                HomePageFragment.$r8$lambda$DLOtOwTflL48VH_4SJcrdtvPGBY(HomePageFragment.this);
            }
        });
    }

    /* renamed from: callbackStartup$lambda-17 */
    public static final void m1459callbackStartup$lambda17(HomePageFragment this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Lifecycle.State currentState = this$0.getLifecycle().getCurrentState();
        Intrinsics.checkNotNullExpressionValue(currentState, "lifecycle.currentState");
        DefaultLogger.w("HomePageFragment", "callbackStartup currState: %s", currentState);
        if (currentState.isAtLeast(Lifecycle.State.STARTED)) {
            this$0.onStartup();
            HomePageStartupHelper2 homePageStartupHelper2 = this$0.mHomePageStartHelper;
            if (homePageStartupHelper2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mHomePageStartHelper");
                homePageStartupHelper2 = null;
            }
            homePageStartupHelper2.onStartup();
            return;
        }
        this$0.mPendingStartupCallback = true;
        DefaultLogger.w("HomePageFragment", "fragment not started: %s", currentState);
    }

    public final boolean takePendingStartup() {
        boolean z = this.mPendingStartupCallback;
        this.mPendingStartupCallback = false;
        return z;
    }

    public final void onDataBind() {
        callbackStartup();
    }

    public final void configHomeMediaLoader() {
        String selection = getLiveData().getSelection();
        String homePageSelection = HomeMediaLoader.getHomePageSelection(this.mIsSwitchAllPhotos);
        if (!TextUtils.equals(selection, homePageSelection)) {
            getLiveData().setSelection(homePageSelection);
            getLiveData().invalidate();
        }
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        EmptyPage emptyPage = this.mEmptyView;
        if (emptyPage == null) {
            return;
        }
        emptyPage.resumeMaml();
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        EmptyPage emptyPage = this.mEmptyView;
        if (emptyPage == null) {
            return;
        }
        emptyPage.pauseMaml();
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        doOnStart();
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        doOnStop();
    }

    @Override // com.miui.gallery.util.OnAppFocusedListener
    public void onAppFocused() {
        SyncDownloadManager syncDownloadManager = this.mSyncDownloadManager;
        if (syncDownloadManager == null) {
            return;
        }
        syncDownloadManager.onAppFocused();
    }

    public final void doOnStart() {
        if (!this.mStartCalled) {
            if (takePendingStartup()) {
                DefaultLogger.d("HomePageFragment", "call startup on started");
                callbackStartup();
            }
            HomePageAdapter2 homePageAdapter2 = this.mHomePageAdapter;
            Intrinsics.checkNotNull(homePageAdapter2);
            homePageAdapter2.updateGalleryCloudSyncableState();
            SyncDownloadManager syncDownloadManager = this.mSyncDownloadManager;
            if (syncDownloadManager != null) {
                syncDownloadManager.onResume();
            }
            MediaFeatureManager.getInstance().triggerNewImageCalculation();
            this.mStartCalled = true;
            if (!getUserVisibleHint()) {
                return;
            }
            onVisibleToUser();
        }
    }

    public final void doOnStop() {
        if (this.mStartCalled) {
            SyncDownloadManager syncDownloadManager = this.mSyncDownloadManager;
            if (syncDownloadManager != null) {
                syncDownloadManager.onPause();
            }
            this.mStartCalled = false;
        }
        EditableListViewWrapper editableListViewWrapper = this.mHomeGridViewWrapper;
        if (editableListViewWrapper == null) {
            return;
        }
        editableListViewWrapper.reductionTouchView();
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment
    public void onPhotoPageCreate(Intent i) {
        Intrinsics.checkNotNullParameter(i, "i");
        super.onPhotoPageCreate(i);
        doOnStop();
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment
    public void onPhotoPageDestroy(Intent i) {
        Intrinsics.checkNotNullParameter(i, "i");
        super.onPhotoPageDestroy(i);
        doOnStart();
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment
    public void pauseDataLoading() {
        super.pauseDataLoading();
        getViewModel().getLiveData().setVisibleToUser(false);
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment
    public void resumeDataLoading() {
        super.resumeDataLoading();
        getViewModel().getLiveData().setVisibleToUser(true);
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        this.mDisposable.dispose();
        super.onDestroy();
        PinchManager pinchManager = this.mPinchManager;
        if (pinchManager == null) {
            return;
        }
        pinchManager.destroy();
    }

    /* compiled from: HomePageFragment.kt */
    /* loaded from: classes2.dex */
    public final class SwitchManager implements IScreenChange.OnLargeScreenChangeListener {
        public final /* synthetic */ HomePageFragment this$0;

        public static /* synthetic */ void $r8$lambda$BdwsqM6PuiFU1OomVKoBj1SP0k0(HomePageFragment homePageFragment, int i) {
            m1492addSwitchView$lambda2$lambda1(homePageFragment, i);
        }

        public static /* synthetic */ void $r8$lambda$ZZfoaeBK3Ylb6PNnPoCtE_LOj_M(HomePageFragment homePageFragment, int i) {
            m1491addSwitchView$lambda2$lambda0(homePageFragment, i);
        }

        public SwitchManager(HomePageFragment this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
        }

        @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
        public void onCreatedWhileLargeDevice(ScreenSize screenSize) {
            addSwitchView();
            Intrinsics.checkNotNull(screenSize);
            boolean isWindowHorizontalLarge = screenSize.isWindowHorizontalLarge();
            if (isWindowHorizontalLarge) {
                SwitchViewWrapper switchViewWrapper = this.this$0.mSwitchViewWrapper;
                Intrinsics.checkNotNull(switchViewWrapper);
                switchViewWrapper.hideSwitchView();
            }
            SwitchViewWrapper switchViewWrapper2 = this.this$0.mSwitchViewWrapper;
            Intrinsics.checkNotNull(switchViewWrapper2);
            switchViewWrapper2.setShowEnable(!isWindowHorizontalLarge);
        }

        @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
        public void onCreatedWhileNormalDevice(ScreenSize screenSize) {
            addSwitchView();
            SwitchViewWrapper switchViewWrapper = this.this$0.mSwitchViewWrapper;
            Intrinsics.checkNotNull(switchViewWrapper);
            switchViewWrapper.setShowEnable(true);
        }

        @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
        public void onScreenSizeToLargeOrNormal(ScreenSize screenSize) {
            Intrinsics.checkNotNull(screenSize);
            boolean isWindowHorizontalLarge = screenSize.isWindowHorizontalLarge();
            SwitchViewWrapper switchViewWrapper = this.this$0.mSwitchViewWrapper;
            Intrinsics.checkNotNull(switchViewWrapper);
            switchViewWrapper.setShowEnable(!isWindowHorizontalLarge);
            if (!isWindowHorizontalLarge) {
                EmptyPage emptyPage = this.this$0.mEmptyView;
                boolean z = false;
                if (emptyPage != null && emptyPage.getActionButtonVisible() == 0) {
                    z = true;
                }
                if (!z) {
                    SwitchViewWrapper switchViewWrapper2 = this.this$0.mSwitchViewWrapper;
                    Intrinsics.checkNotNull(switchViewWrapper2);
                    switchViewWrapper2.lambda$showSwitchViewDelay$0();
                    SwitchView switchView = this.this$0.mSwitchView;
                    Intrinsics.checkNotNull(switchView);
                    switchView.setSelectedTab(!this.this$0.mIsSwitchAllPhotos ? 1 : 0);
                    return;
                }
            }
            SwitchViewWrapper switchViewWrapper3 = this.this$0.mSwitchViewWrapper;
            Intrinsics.checkNotNull(switchViewWrapper3);
            switchViewWrapper3.hideSwitchView();
        }

        public final void addSwitchView() {
            HomePageFragment homePageFragment = this.this$0;
            Context context = this.this$0.getContext();
            Intrinsics.checkNotNull(context);
            homePageFragment.mSwitchView = new SwitchView(context);
            SwitchView switchView = this.this$0.mSwitchView;
            Intrinsics.checkNotNull(switchView);
            final HomePageFragment homePageFragment2 = this.this$0;
            switchView.addTab(switchView.getResources().getString(R.string.home_page_all_photos));
            switchView.addTab(switchView.getResources().getString(R.string.home_page_camera));
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(switchView.getResources().getDimensionPixelOffset(R.dimen.switch_view_width), switchView.getResources().getDimensionPixelOffset(R.dimen.switch_view_height));
            layoutParams.gravity = 81;
            layoutParams.bottomMargin = switchView.getResources().getDimensionPixelOffset(R.dimen.switch_view_margin_bottom);
            INestedTwoStageDrawer iNestedTwoStageDrawer = homePageFragment2.mDrawer;
            Objects.requireNonNull(iNestedTwoStageDrawer, "null cannot be cast to non-null type android.widget.FrameLayout");
            ((FrameLayout) iNestedTwoStageDrawer).addView(homePageFragment2.mSwitchView, layoutParams);
            switchView.setSelectedTab(!homePageFragment2.mIsSwitchAllPhotos ? 1 : 0);
            switchView.setOnSwitchClickListener(new SwitchView.OnSwitchChangeListener() { // from class: com.miui.gallery.ui.HomePageFragment$SwitchManager$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.widget.SwitchView.OnSwitchChangeListener
                public final void onSwitchChange(int i) {
                    HomePageFragment.SwitchManager.$r8$lambda$ZZfoaeBK3Ylb6PNnPoCtE_LOj_M(HomePageFragment.this, i);
                }
            }, new SwitchView.OnSwitchDoubleClickListener() { // from class: com.miui.gallery.ui.HomePageFragment$SwitchManager$$ExternalSyntheticLambda1
                @Override // com.miui.gallery.widget.SwitchView.OnSwitchDoubleClickListener
                public final void onDoubleClick(int i) {
                    HomePageFragment.SwitchManager.$r8$lambda$BdwsqM6PuiFU1OomVKoBj1SP0k0(HomePageFragment.this, i);
                }
            });
            HomePageFragment homePageFragment3 = this.this$0;
            homePageFragment3.mSwitchViewWrapper = new SwitchViewWrapper(homePageFragment3.getContext(), this.this$0.mSwitchView);
            InterceptableRecyclerView interceptableRecyclerView = this.this$0.mHomeGridView;
            Intrinsics.checkNotNull(interceptableRecyclerView);
            final HomePageFragment homePageFragment4 = this.this$0;
            interceptableRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.miui.gallery.ui.HomePageFragment$SwitchManager$addSwitchView$2
                public boolean isToShow = true;
                public int preScrollState;

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                    Intrinsics.checkNotNullParameter(recyclerView, "recyclerView");
                    super.onScrollStateChanged(recyclerView, i);
                    if (HomePageFragment.this.mSwitchViewWrapper != null) {
                        EditableListViewWrapper editableListViewWrapper = HomePageFragment.this.mHomeGridViewWrapper;
                        Intrinsics.checkNotNull(editableListViewWrapper);
                        if (editableListViewWrapper.isInActionMode()) {
                            return;
                        }
                        if (this.preScrollState != 0 && i == 0 && !this.isToShow) {
                            SwitchViewWrapper switchViewWrapper = HomePageFragment.this.mSwitchViewWrapper;
                            Intrinsics.checkNotNull(switchViewWrapper);
                            switchViewWrapper.showSwitchViewByAnim(2000);
                            this.isToShow = true;
                        }
                        this.preScrollState = i;
                    }
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                    Intrinsics.checkNotNullParameter(recyclerView, "recyclerView");
                    super.onScrolled(recyclerView, i, i2);
                    if (HomePageFragment.this.mSwitchViewWrapper != null) {
                        EditableListViewWrapper editableListViewWrapper = HomePageFragment.this.mHomeGridViewWrapper;
                        Intrinsics.checkNotNull(editableListViewWrapper);
                        if (editableListViewWrapper.isInActionMode()) {
                            return;
                        }
                        boolean z = this.isToShow;
                        if (z && i2 > 0) {
                            SwitchViewWrapper switchViewWrapper = HomePageFragment.this.mSwitchViewWrapper;
                            Intrinsics.checkNotNull(switchViewWrapper);
                            switchViewWrapper.hideSwitchViewByAnim();
                            this.isToShow = false;
                        } else if (z || i2 >= 0) {
                        } else {
                            SwitchViewWrapper switchViewWrapper2 = HomePageFragment.this.mSwitchViewWrapper;
                            Intrinsics.checkNotNull(switchViewWrapper2);
                            switchViewWrapper2.showSwitchViewByAnim(0);
                            this.isToShow = true;
                        }
                    }
                }
            });
        }

        /* renamed from: addSwitchView$lambda-2$lambda-0 */
        public static final void m1491addSwitchView$lambda2$lambda0(HomePageFragment this$0, int i) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            int i2 = 0;
            this$0.mIsSwitchAllPhotos = i == 0;
            GalleryPreferences.HomePage.setHomePageShowAllPhotos(this$0.mIsSwitchAllPhotos);
            DiscoverChangeManager discoverChangeManager = this$0.mDiscoverWidgetManager;
            Intrinsics.checkNotNull(discoverChangeManager);
            discoverChangeManager.setDiscoverDotShowEnable(true ^ this$0.mIsSwitchAllPhotos);
            if (this$0.mIsSwitchAllPhotos) {
                SwitchView switchView = this$0.mSwitchView;
                Intrinsics.checkNotNull(switchView);
                switchView.showOrHideDotView(false);
            }
            this$0.mDiffEnable = false;
            MatchPositionManager matchPositionManager = this$0.mMatchPositionManager;
            Intrinsics.checkNotNull(matchPositionManager);
            matchPositionManager.calculateMatchFromItem(this$0.mViewMode);
            this$0.configHomeMediaLoader();
            String str = this$0.mIsSwitchAllPhotos ? "all" : "camera";
            if (this$0.mIsSwitchAllPhotos && this$0.mSwitchViewWrapper != null) {
                SwitchViewWrapper switchViewWrapper = this$0.mSwitchViewWrapper;
                Intrinsics.checkNotNull(switchViewWrapper);
                i2 = switchViewWrapper.getDiscoverCount();
            }
            this$0.statSwitchViewClick(str, i2);
        }

        /* renamed from: addSwitchView$lambda-2$lambda-1 */
        public static final void m1492addSwitchView$lambda2$lambda1(HomePageFragment this$0, int i) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            InterceptableRecyclerView interceptableRecyclerView = this$0.mHomeGridView;
            Intrinsics.checkNotNull(interceptableRecyclerView);
            interceptableRecyclerView.scrollToPositionWithOffset(0, 0);
            MatchPositionManager matchPositionManager = this$0.mMatchPositionManager;
            Intrinsics.checkNotNull(matchPositionManager);
            matchPositionManager.clearMatchItem();
        }
    }

    /* compiled from: HomePageFragment.kt */
    /* loaded from: classes2.dex */
    public final class ChoiceModeListener implements MultiChoiceModeListener {
        public MenuItem mAddToAlbum;
        public MenuItem mDelete;
        public float mExitChoseModeSearchBarAlpha;
        public ActionMode mMode;
        public MenuItem mProduce;
        public MenuItem mSend;
        public float mStartChoseModeSearchBarAlpha;
        public final /* synthetic */ HomePageFragment this$0;

        /* renamed from: $r8$lambda$7ljYSde9Zx7Y-O3RhAtpyA-nvpQ */
        public static /* synthetic */ void m1473$r8$lambda$7ljYSde9Zx7YO3RhAtpyAnvpQ(HomePageFragment homePageFragment) {
            m1476onActionItemClicked$lambda3(homePageFragment);
        }

        public static /* synthetic */ void $r8$lambda$8RyUJi4VkFujMcXCgkKSFkkV4uw(HomePageFragment homePageFragment, ActionMode actionMode, long[] jArr) {
            m1478onActionItemClicked$lambda5(homePageFragment, actionMode, jArr);
        }

        public static /* synthetic */ void $r8$lambda$RXgnhTT0bo8xkcBe2Ak5uQARCzc(HomePageFragment homePageFragment, ActionMode actionMode, int i, long[] jArr) {
            m1477onActionItemClicked$lambda4(homePageFragment, actionMode, i, jArr);
        }

        /* renamed from: $r8$lambda$aFIxebG3zCh-5exc5Mb4jySNxKY */
        public static /* synthetic */ void m1474$r8$lambda$aFIxebG3zCh5exc5Mb4jySNxKY(ActionMode actionMode, long[] jArr, boolean z) {
            m1475onActionItemClicked$lambda2(actionMode, jArr, z);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(menu, "menu");
            return false;
        }

        public ChoiceModeListener(HomePageFragment this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void onAllItemsCheckedStateChanged(ActionMode mode, boolean z) {
            Intrinsics.checkNotNullParameter(mode, "mode");
            updateMenuState();
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void statGroupItemsCheckedStateChanged(boolean z) {
            if (z) {
                TrackController.trackClick("403.1.8.1.11112", "403.1.8.1.9891");
            }
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void onItemCheckedStateChanged(ActionMode mode, int i, long j, boolean z) {
            Intrinsics.checkNotNullParameter(mode, "mode");
            updateMenuState();
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(menu, "menu");
            this.mMode = mode;
            mode.getMenuInflater().inflate(R.menu.home_page_menu, menu);
            this.mAddToAlbum = menu.findItem(R.id.add_to_album);
            this.mDelete = menu.findItem(R.id.delete);
            this.mProduce = menu.findItem(R.id.action_produce);
            this.mSend = menu.findItem(R.id.action_send);
            MenuItem menuItem = this.mProduce;
            Intrinsics.checkNotNull(menuItem);
            menuItem.setVisible(GalleryPreferences.Assistant.isCreativityFunctionOn());
            Context context = this.this$0.getContext();
            Intrinsics.checkNotNull(context);
            Intrinsics.checkNotNullExpressionValue(context, "context!!");
            this.mStartChoseModeSearchBarAlpha = context.getResources().getInteger(R.integer.search_bar_start_chose_mode_alpha) / 100.0f;
            Context context2 = this.this$0.getContext();
            Intrinsics.checkNotNull(context2);
            Intrinsics.checkNotNullExpressionValue(context2, "context!!");
            this.mExitChoseModeSearchBarAlpha = context2.getResources().getInteger(R.integer.search_bar_exit_chose_mode_alpha) / 100.0f;
            if (this.this$0.mSyncDownloadManager != null) {
                SyncDownloadManager syncDownloadManager = this.this$0.mSyncDownloadManager;
                Intrinsics.checkNotNull(syncDownloadManager);
                syncDownloadManager.setEnable(false, this.mStartChoseModeSearchBarAlpha);
                PanelBar panelBar = this.this$0.mPanelBar;
                Intrinsics.checkNotNull(panelBar);
                panelBar.setAlpha(this.mStartChoseModeSearchBarAlpha);
            }
            ImageSelectionTipFragment.showImageSelectionTipDialogIfNecessary(this.this$0.getActivity());
            HomePageActivity.HomeTabActionBarHelper homeTabActionBarHelper = this.this$0.mActionBarHelper;
            if (homeTabActionBarHelper != null) {
                HomePageFragment homePageFragment = this.this$0;
                homeTabActionBarHelper.setInChoiceMode(true);
                Context context3 = homePageFragment.getContext();
                Intrinsics.checkNotNull(context3);
                homeTabActionBarHelper.addActionBarBg(new ColorDrawable(context3.getColor(R.color.action_bar_background)));
                if (homeTabActionBarHelper.isShowImmerse() && !MiscUtil.isNightMode(homePageFragment.mActivity)) {
                    homePageFragment.mActivity.setTranslucentStatus(1);
                }
                if (BaseBuildUtil.isLargeScreenDevice()) {
                    int actionBarHeight = homeTabActionBarHelper.getActionBarHeight();
                    InterceptableRecyclerView interceptableRecyclerView = homePageFragment.mHomeGridView;
                    Intrinsics.checkNotNull(interceptableRecyclerView);
                    interceptableRecyclerView.setPadding(interceptableRecyclerView.getPaddingLeft(), interceptableRecyclerView.getTop(), interceptableRecyclerView.getPaddingRight(), interceptableRecyclerView.getPaddingBottom() + actionBarHeight);
                    interceptableRecyclerView.setFastScrollerBottomMargin(interceptableRecyclerView.getFastScrollerBottomMargin() + actionBarHeight);
                }
            }
            SwitchViewWrapper switchViewWrapper = this.this$0.mSwitchViewWrapper;
            Intrinsics.checkNotNull(switchViewWrapper);
            switchViewWrapper.hideSwitchView();
            SamplingStatHelper.recordCountEvent("home", "action_mode_create");
            SamplingStatHelper.recordStringPropertyEvent("best_image_count", String.valueOf(MediaFeatureCacheManager.getInstance().getBestImageCount(false)));
            TrackController.trackExpose("403.1.8.1.9891", AutoTracking.getRef());
            return true;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(item, "item");
            if (item.getItemId() != 16908313 && item.getItemId() != 16908314) {
                LinearMotorHelper.performHapticFeedback(this.this$0.mHomeGridView, LinearMotorHelper.HAPTIC_TAP_LIGHT);
            }
            if (item.getItemId() == 16908314) {
                TrackController.trackClick("403.1.8.1.10317", "403.1.8.1.9891");
            }
            switch (item.getItemId()) {
                case R.id.action_produce /* 2131361895 */:
                    final HomePageFragment homePageFragment = this.this$0;
                    AppCompatActivity appCompatActivity = homePageFragment.mActivity;
                    MediaAndAlbumOperations.OnCompleteListener onCompleteListener = new MediaAndAlbumOperations.OnCompleteListener() { // from class: com.miui.gallery.ui.HomePageFragment$ChoiceModeListener$$ExternalSyntheticLambda2
                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnCompleteListener
                        public final void onComplete(long[] jArr) {
                            HomePageFragment.ChoiceModeListener.$r8$lambda$8RyUJi4VkFujMcXCgkKSFkkV4uw(HomePageFragment.this, mode, jArr);
                        }
                    };
                    EditableListViewWrapper editableListViewWrapper = this.this$0.mHomeGridViewWrapper;
                    Intrinsics.checkNotNull(editableListViewWrapper);
                    MediaAndAlbumOperations.doProduceCreation(appCompatActivity, onCompleteListener, editableListViewWrapper.getCheckedItems());
                    return true;
                case R.id.action_send /* 2131361903 */:
                    this.this$0.onSend(null, null);
                    return true;
                case R.id.add_to_album /* 2131361918 */:
                    TimeMonitor.createNewTimeMonitor("403.26.0.1.13762");
                    EditableListViewWrapper editableListViewWrapper2 = this.this$0.mHomeGridViewWrapper;
                    Intrinsics.checkNotNull(editableListViewWrapper2);
                    TrackController.trackClick("403.1.8.1.9897", "403.1.8.1.9891", editableListViewWrapper2.getCheckedItemCount());
                    TrackController.trackExpose("403.26.0.1.11240", "403.1.8.1.9891");
                    AppCompatActivity appCompatActivity2 = this.this$0.mActivity;
                    MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener = new MediaAndAlbumOperations.OnAddAlbumListener() { // from class: com.miui.gallery.ui.HomePageFragment$ChoiceModeListener$$ExternalSyntheticLambda1
                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnAddAlbumListener
                        public final void onComplete(long[] jArr, boolean z) {
                            HomePageFragment.ChoiceModeListener.m1474$r8$lambda$aFIxebG3zCh5exc5Mb4jySNxKY(mode, jArr, z);
                        }
                    };
                    final HomePageFragment homePageFragment2 = this.this$0;
                    MediaAndAlbumOperations.OnPicToPdfClickListener onPicToPdfClickListener = new MediaAndAlbumOperations.OnPicToPdfClickListener() { // from class: com.miui.gallery.ui.HomePageFragment$ChoiceModeListener$$ExternalSyntheticLambda3
                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnPicToPdfClickListener
                        public final void onPicToPdfClick() {
                            HomePageFragment.ChoiceModeListener.m1473$r8$lambda$7ljYSde9Zx7YO3RhAtpyAnvpQ(HomePageFragment.this);
                        }
                    };
                    EditableListViewWrapper editableListViewWrapper3 = this.this$0.mHomeGridViewWrapper;
                    Intrinsics.checkNotNull(editableListViewWrapper3);
                    boolean isCheckedItemContainVideo = editableListViewWrapper3.isCheckedItemContainVideo();
                    long[] ListToArray = MiscUtil.ListToArray(this.this$0.getBurstCheckedItemIds());
                    MediaAndAlbumOperations.addToAlbum(appCompatActivity2, onAddAlbumListener, onPicToPdfClickListener, false, true, true, isCheckedItemContainVideo, Arrays.copyOf(ListToArray, ListToArray.length));
                    return true;
                case R.id.delete /* 2131362250 */:
                    HomePageFragment homePageFragment3 = this.this$0;
                    EditableListViewWrapper editableListViewWrapper4 = homePageFragment3.mHomeGridViewWrapper;
                    Intrinsics.checkNotNull(editableListViewWrapper4);
                    homePageFragment3.mAfterDeletedFirstVisiblePosAndOffset = editableListViewWrapper4.getAfterDeletedFirstVisiblePosAndOffset();
                    final HomePageFragment homePageFragment4 = this.this$0;
                    AppCompatActivity appCompatActivity3 = homePageFragment4.mActivity;
                    DeletionTask.OnDeletionCompleteListener onDeletionCompleteListener = new DeletionTask.OnDeletionCompleteListener() { // from class: com.miui.gallery.ui.HomePageFragment$ChoiceModeListener$$ExternalSyntheticLambda0
                        @Override // com.miui.gallery.ui.DeletionTask.OnDeletionCompleteListener
                        public final void onDeleted(int i, long[] jArr) {
                            HomePageFragment.ChoiceModeListener.$r8$lambda$RXgnhTT0bo8xkcBe2Ak5uQARCzc(HomePageFragment.this, mode, i, jArr);
                        }
                    };
                    long[] longArray = CollectionsKt___CollectionsKt.toLongArray(this.this$0.getBurstCheckedItemIds());
                    MediaAndAlbumOperations.delete(appCompatActivity3, "HomePageFragmentDeleteMediaDialogFragment", onDeletionCompleteListener, null, -1L, "", 26, 1, Arrays.copyOf(longArray, longArray.length));
                    return true;
                default:
                    return false;
            }
        }

        /* renamed from: onActionItemClicked$lambda-2 */
        public static final void m1475onActionItemClicked$lambda2(ActionMode mode, long[] jArr, boolean z) {
            Intrinsics.checkNotNullParameter(mode, "$mode");
            mode.finish();
            SamplingStatHelper.recordCountEvent("home", "add_to_album");
        }

        /* renamed from: onActionItemClicked$lambda-3 */
        public static final void m1476onActionItemClicked$lambda3(HomePageFragment this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            AppCompatActivity appCompatActivity = this$0.mActivity;
            EditableListViewWrapper editableListViewWrapper = this$0.mHomeGridViewWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            if (PicToPdfHelper.prepareGotoPicToPdfPreviewPage(appCompatActivity, editableListViewWrapper.getCheckedItems())) {
                EditableListViewWrapper editableListViewWrapper2 = this$0.mHomeGridViewWrapper;
                Intrinsics.checkNotNull(editableListViewWrapper2);
                TrackController.trackClick("403.26.0.1.11243", "403.26.0.1.11240", editableListViewWrapper2.getCheckedItems().size());
                EditableListViewWrapper editableListViewWrapper3 = this$0.mHomeGridViewWrapper;
                Intrinsics.checkNotNull(editableListViewWrapper3);
                editableListViewWrapper3.stopActionMode();
            }
        }

        /* renamed from: onActionItemClicked$lambda-4 */
        public static final void m1477onActionItemClicked$lambda4(HomePageFragment this$0, ActionMode mode, int i, long[] jArr) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(mode, "$mode");
            AppCompatActivity appCompatActivity = this$0.mActivity;
            if (appCompatActivity == null) {
                return;
            }
            boolean z = true;
            ToastUtils.makeText(appCompatActivity, this$0.getResources().getQuantityString(R.plurals.delete_finish_format, i, Integer.valueOf(i)));
            if (i > 0) {
                SoundUtils.playSoundForOperation(this$0.mActivity, 0);
                TimeMonitor.trackTimeMonitor("403.45.0.1.13761", "403.1.2.1.9881", i);
            } else {
                TimeMonitor.cancelTimeMonitor("403.45.0.1.13761");
            }
            mode.finish();
            SamplingStatHelper.recordCountEvent("home", "delete_photo", MapsKt__MapsJVMKt.mapOf(TuplesKt.to(MiStat.Param.COUNT, String.valueOf(i))));
            HashMap hashMap = new HashMap();
            hashMap.put("tip", "403.1.8.1.9892");
            hashMap.put("ref_tip", "403.1.8.1.9891");
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "sure");
            hashMap.put(MiStat.Param.COUNT, Integer.valueOf(i));
            if (i <= 0) {
                z = false;
            }
            hashMap.put("success", Boolean.valueOf(z));
            TrackController.trackClick(hashMap);
        }

        /* renamed from: onActionItemClicked$lambda-5 */
        public static final void m1478onActionItemClicked$lambda5(HomePageFragment this$0, ActionMode mode, long[] jArr) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(mode, "$mode");
            EditableListViewWrapper editableListViewWrapper = this$0.mHomeGridViewWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            List<CheckableAdapter.CheckedItem> checkedItems = editableListViewWrapper.getCheckedItems();
            int i = 0;
            for (int i2 = 0; i2 < checkedItems.size(); i2++) {
                if (MediaFeatureCacheManager.getInstance().isBestImage(checkedItems.get(i2).getId(), false, false, null)) {
                    i++;
                }
            }
            SamplingStatHelper.recordCountEvent("home", "produce", MapsKt__MapsKt.mapOf(TuplesKt.to(MiStat.Param.COUNT, String.valueOf(checkedItems.size())), TuplesKt.to("best_image_count", String.valueOf(i))));
            mode.finish();
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode mode) {
            Intrinsics.checkNotNullParameter(mode, "mode");
            if (this.this$0.mSyncDownloadManager != null) {
                SyncDownloadManager syncDownloadManager = this.this$0.mSyncDownloadManager;
                Intrinsics.checkNotNull(syncDownloadManager);
                syncDownloadManager.setEnable(true, this.mExitChoseModeSearchBarAlpha);
                PanelBar panelBar = this.this$0.mPanelBar;
                Intrinsics.checkNotNull(panelBar);
                panelBar.setAlpha(this.mExitChoseModeSearchBarAlpha);
            }
            if (this.this$0.mActionBarHelper != null) {
                HomePageActivity.HomeTabActionBarHelper homeTabActionBarHelper = this.this$0.mActionBarHelper;
                Intrinsics.checkNotNull(homeTabActionBarHelper);
                homeTabActionBarHelper.setInChoiceMode(false);
                HomePageActivity.HomeTabActionBarHelper homeTabActionBarHelper2 = this.this$0.mActionBarHelper;
                Intrinsics.checkNotNull(homeTabActionBarHelper2);
                homeTabActionBarHelper2.removeActionBarBg();
                HomePageActivity.HomeTabActionBarHelper homeTabActionBarHelper3 = this.this$0.mActionBarHelper;
                Intrinsics.checkNotNull(homeTabActionBarHelper3);
                if (homeTabActionBarHelper3.isShowImmerse() && !MiscUtil.isNightMode(this.this$0.mActivity)) {
                    this.this$0.mActivity.setTranslucentStatus(2);
                }
                if (BaseBuildUtil.isLargeScreenDevice()) {
                    HomePageActivity.HomeTabActionBarHelper homeTabActionBarHelper4 = this.this$0.mActionBarHelper;
                    Intrinsics.checkNotNull(homeTabActionBarHelper4);
                    int actionBarHeight = homeTabActionBarHelper4.getActionBarHeight();
                    InterceptableRecyclerView interceptableRecyclerView = this.this$0.mHomeGridView;
                    Intrinsics.checkNotNull(interceptableRecyclerView);
                    interceptableRecyclerView.setPadding(interceptableRecyclerView.getPaddingLeft(), interceptableRecyclerView.getTop(), interceptableRecyclerView.getPaddingRight(), interceptableRecyclerView.getPaddingBottom() - actionBarHeight);
                    interceptableRecyclerView.setFastScrollerBottomMargin(interceptableRecyclerView.getFastScrollerBottomMargin() - actionBarHeight);
                }
            }
            HomePageAdapter2 homePageAdapter2 = this.this$0.mHomePageAdapter;
            Intrinsics.checkNotNull(homePageAdapter2);
            if (homePageAdapter2.getItemCount() != 0) {
                SwitchViewWrapper switchViewWrapper = this.this$0.mSwitchViewWrapper;
                Intrinsics.checkNotNull(switchViewWrapper);
                switchViewWrapper.showSwitchViewDelay();
            }
        }

        public final void updateMenuState() {
            EditableListViewWrapper editableListViewWrapper = this.this$0.mHomeGridViewWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            if (editableListViewWrapper.getCheckedItemCount() < 1) {
                MenuItem menuItem = this.mDelete;
                Intrinsics.checkNotNull(menuItem);
                menuItem.setEnabled(false);
                MenuItem menuItem2 = this.mAddToAlbum;
                Intrinsics.checkNotNull(menuItem2);
                menuItem2.setEnabled(false);
                MenuItem menuItem3 = this.mProduce;
                Intrinsics.checkNotNull(menuItem3);
                menuItem3.setEnabled(false);
                MenuItem menuItem4 = this.mSend;
                if (menuItem4 == null) {
                    return;
                }
                Intrinsics.checkNotNull(menuItem4);
                menuItem4.setEnabled(false);
                return;
            }
            MenuItem menuItem5 = this.mDelete;
            Intrinsics.checkNotNull(menuItem5);
            menuItem5.setEnabled(true);
            MenuItem menuItem6 = this.mAddToAlbum;
            Intrinsics.checkNotNull(menuItem6);
            menuItem6.setEnabled(true);
            MenuItem menuItem7 = this.mProduce;
            Intrinsics.checkNotNull(menuItem7);
            menuItem7.setEnabled(true);
            MenuItem menuItem8 = this.mSend;
            if (menuItem8 == null) {
                return;
            }
            Intrinsics.checkNotNull(menuItem8);
            menuItem8.setEnabled(true);
        }

        public final void finish() {
            ActionMode actionMode = this.mMode;
            Intrinsics.checkNotNull(actionMode);
            actionMode.finish();
        }
    }

    public final ArrayList<Long> getBurstCheckedItemIds() {
        EditableListViewWrapper editableListViewWrapper = this.mHomeGridViewWrapper;
        Intrinsics.checkNotNull(editableListViewWrapper);
        List<Integer> checkedPositions = editableListViewWrapper.getCheckedPositions();
        Intrinsics.checkNotNullExpressionValue(checkedPositions, "mHomeGridViewWrapper!!.checkedPositions");
        ArrayList<Long> arrayList = new ArrayList<>();
        for (Integer position : checkedPositions) {
            HomePageAdapter2 homePageAdapter2 = this.mHomePageAdapter;
            Intrinsics.checkNotNull(homePageAdapter2);
            Intrinsics.checkNotNullExpressionValue(position, "position");
            arrayList.addAll(homePageAdapter2.getBurstItemKeys(position.intValue()));
        }
        return arrayList;
    }

    @Override // com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (z) {
            onVisibleToUser();
        }
    }

    public final boolean isEmpty() {
        HomePageAdapter2 homePageAdapter2 = this.mHomePageAdapter;
        return homePageAdapter2 != null && homePageAdapter2.getItemCount() == 0;
    }

    public final void onVisibleToUser() {
        if (isEmpty()) {
            statHomeEmpty();
        }
    }

    public final void statHomeEmpty() {
        HashMap hashMap = new HashMap();
        hashMap.put(MiStat.Event.LOGIN, String.valueOf(AccountCache.getAccount() != null));
        SamplingStatHelper.recordCountEvent("home", "home_empty", hashMap);
    }

    public final void showHomePageTips(boolean z, boolean z2) {
        IntroductionPage chain;
        HomePageProvider homePageProvider = new HomePageProvider(this);
        Executor executor = com.miui.gallery.util.thread.ThreadManager.getExecutor(31);
        Intrinsics.checkNotNullExpressionValue(executor, "getExecutor(ThreadManager.RIGHT_NOW)");
        CoroutineDispatcher from = ExecutorsKt.from(executor);
        if (z) {
            IntroductionPage.Companion companion = IntroductionPage.Companion;
            Companion companion2 = Companion;
            chain = companion.chain(companion2.provideBackupSettingsGuide(homePageProvider, from), companion2.provideMultiViewGuide(homePageProvider, from), companion2.provideMigrateGuide(homePageProvider, from), companion2.provideDeletingPermissionGuide(homePageProvider, from), companion2.provideSpaceFullGuide(homePageProvider, from), companion2.provideSlimGuide(homePageProvider, from));
        } else {
            IntroductionPage.Companion companion3 = IntroductionPage.Companion;
            Companion companion4 = Companion;
            chain = companion3.chain(companion4.provideMultiViewGuide(homePageProvider, from), companion4.provideMigrateGuide(homePageProvider, from), companion4.provideDeletingPermissionGuide(homePageProvider, from), companion4.provideSpaceFullGuide(homePageProvider, from), companion4.provideSlimGuide(homePageProvider, from));
        }
        LifecycleOwnerKt.getLifecycleScope(this).launchWhenResumed(new HomePageFragment$showHomePageTips$1(chain, z2, null));
    }

    public final void refreshSearchIconVisible() {
        HomePageActivity.HomeTabActionBarHelper homeTabActionBarHelper = this.mActionBarHelper;
        if (homeTabActionBarHelper == null) {
            return;
        }
        View view = null;
        if (this.isShowSearch) {
            if (homeTabActionBarHelper != null) {
                view = homeTabActionBarHelper.getSearchView();
            }
            if (view == null) {
                return;
            }
            view.setVisibility(0);
            return;
        }
        if (homeTabActionBarHelper != null) {
            view = homeTabActionBarHelper.getSearchView();
        }
        if (view == null) {
            return;
        }
        view.setVisibility(8);
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        Intrinsics.checkNotNullParameter(newConfig, "newConfig");
        super.onConfigurationChanged(newConfig);
        View view = this.mPlaceHolderView;
        ViewGroup.LayoutParams layoutParams = view == null ? null : view.getLayoutParams();
        if (layoutParams != null) {
            Context context = getContext();
            Intrinsics.checkNotNull(context);
            Intrinsics.checkNotNullExpressionValue(context, "context!!");
            layoutParams.height = context.getResources().getDimensionPixelSize(R.dimen.home_tab_place_holder_height);
        }
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(getResources().getDimensionPixelOffset(R.dimen.switch_view_width), getResources().getDimensionPixelOffset(R.dimen.switch_view_height));
        layoutParams2.gravity = 81;
        Context context2 = getContext();
        Intrinsics.checkNotNull(context2);
        Intrinsics.checkNotNullExpressionValue(context2, "context!!");
        layoutParams2.bottomMargin = context2.getResources().getDimensionPixelOffset(R.dimen.switch_view_margin_bottom);
        SwitchView switchView = this.mSwitchView;
        if (switchView != null) {
            switchView.setLayoutParams(layoutParams2);
        }
        PictureViewMode mViewMode = this.mViewMode;
        Intrinsics.checkNotNullExpressionValue(mViewMode, "mViewMode");
        setPictureViewMode(mViewMode);
    }

    public final void updateConfiguration(Configuration configuration) {
        GridItemSpacingDecoration gridItemSpacingDecoration = this.mSpacingDecoration;
        Intrinsics.checkNotNull(gridItemSpacingDecoration);
        gridItemSpacingDecoration.setVerticalSpacing(this.mViewMode.getSpacing());
        HomePageAdapter2 homePageAdapter2 = this.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter2);
        homePageAdapter2.setConfiguration(configuration);
        InterceptableRecyclerView interceptableRecyclerView = this.mHomeGridView;
        Intrinsics.checkNotNull(interceptableRecyclerView);
        RecyclerView.LayoutManager layoutManager = interceptableRecyclerView.getLayoutManager();
        Objects.requireNonNull(layoutManager, "null cannot be cast to non-null type androidx.recyclerview.widget.GridLayoutManager");
        GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
        if (this.mViewMode.isAggregated()) {
            gridLayoutManager.setSpanCount(1);
        } else {
            gridLayoutManager.setSpanCount(this.mViewMode.getSpan());
        }
        if (configuration.orientation == 2) {
            EmptyPage emptyPage = this.mEmptyView;
            if (emptyPage != null) {
                emptyPage.setPadding(0, getResources().getDimensionPixelOffset(R.dimen.home_page_empty_padding_top), 0, 0);
            }
        } else {
            EmptyPage emptyPage2 = this.mEmptyView;
            if (emptyPage2 != null) {
                emptyPage2.setPadding(0, 0, 0, 0);
            }
        }
        InterceptableRecyclerView interceptableRecyclerView2 = this.mHomeGridView;
        Intrinsics.checkNotNull(interceptableRecyclerView2);
        int findFirstVisibleItemPosition = interceptableRecyclerView2.findFirstVisibleItemPosition();
        if (findFirstVisibleItemPosition != -1) {
            InterceptableRecyclerView interceptableRecyclerView3 = this.mHomeGridView;
            Intrinsics.checkNotNull(interceptableRecyclerView3);
            interceptableRecyclerView3.scrollToPositionWithOffset(findFirstVisibleItemPosition, 0);
        }
        HomePageAdapter2 homePageAdapter22 = this.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter22);
        homePageAdapter22.notifyDataSetChanged();
    }

    public final void registerSearchStatusObserver() {
        this.mSearchBarStatusObserver = new AIAlbumDisplayHelper.WeakReferencedAIAlbumDisplayStatusObserver(this);
        SparseBooleanArray registerAIAlbumDisplayStatusObserver = AIAlbumDisplayHelper.getInstance().registerAIAlbumDisplayStatusObserver(this.mSearchBarStatusObserver);
        Intrinsics.checkNotNullExpressionValue(registerAIAlbumDisplayStatusObserver, "getInstance()\n          …mSearchBarStatusObserver)");
        onStatusChanged(registerAIAlbumDisplayStatusObserver);
    }

    public final void unregisterSearchStatusObserver() {
        if (this.mSearchBarStatusObserver != null) {
            AIAlbumDisplayHelper.getInstance().unregisterAIAlbumDisplayStatusObserver(this.mSearchBarStatusObserver);
        }
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        Intrinsics.checkNotNullParameter(outState, "outState");
        super.onSaveInstanceState(outState);
        outState.putInt("view_mode", this.mViewMode.ordinal());
        outState.putBoolean("switch_state", this.mIsSwitchAllPhotos);
    }

    @Override // com.miui.gallery.ui.AIAlbumDisplayHelper.DisplayStatusCallback
    public void onStatusChanged(SparseBooleanArray changedStatus) {
        Intrinsics.checkNotNullParameter(changedStatus, "changedStatus");
        if (changedStatus.indexOfKey(1) >= 0) {
            if (changedStatus.get(1)) {
                GalleryPreferences.Search.setIsSearchCacheStatusOpen(true);
                this.isShowSearch = true;
            } else {
                GalleryPreferences.Search.setIsSearchCacheStatusOpen(false);
                this.isShowSearch = false;
            }
            refreshSearchIconVisible();
        }
    }

    public final void onSend(String str, String str2) {
        EditableListViewWrapper editableListViewWrapper = this.mHomeGridViewWrapper;
        Intrinsics.checkNotNull(editableListViewWrapper);
        List<Integer> checkedPositions = editableListViewWrapper.getCheckedPositions();
        Intrinsics.checkNotNullExpressionValue(checkedPositions, "mHomeGridViewWrapper!!.checkedPositions");
        ArrayList arrayList = new ArrayList(checkedPositions.size());
        int i = Integer.MAX_VALUE;
        for (Integer position : checkedPositions) {
            HomePageAdapter2 homePageAdapter2 = this.mHomePageAdapter;
            Intrinsics.checkNotNull(homePageAdapter2);
            Intrinsics.checkNotNullExpressionValue(position, "position");
            arrayList.add(Long.valueOf(homePageAdapter2.getItemKey(position.intValue())));
            if (position.intValue() < i) {
                i = position.intValue();
            }
        }
        if (checkedPositions.size() == 0) {
            return;
        }
        int[] iArr = new int[checkedPositions.size()];
        long[] jArr = new long[arrayList.size()];
        int size = checkedPositions.size();
        for (int i2 = 0; i2 < size; i2++) {
            Integer num = checkedPositions.get(i2);
            Intrinsics.checkNotNullExpressionValue(num, "positions[i]");
            iArr[i2] = num.intValue();
            Object obj = arrayList.get(i2);
            Intrinsics.checkNotNullExpressionValue(obj, "ids[i]");
            jArr[i2] = ((Number) obj).longValue();
        }
        int i3 = i == Integer.MAX_VALUE ? 0 : i;
        ImageLoadParams.Builder builder = new ImageLoadParams.Builder();
        HomePageAdapter2 homePageAdapter22 = this.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter22);
        ImageLoadParams.Builder key = builder.setKey(homePageAdapter22.getItemKey(i3));
        HomePageAdapter2 homePageAdapter23 = this.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter23);
        ImageLoadParams.Builder targetSize = key.setFilePath(homePageAdapter23.getBindImagePath(i3)).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize);
        HomePageAdapter2 homePageAdapter24 = this.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter24);
        ImageLoadParams.Builder initPosition = targetSize.setRegionRect(homePageAdapter24.getItemDecodeRectF(i3)).setInitPosition(i3);
        HomePageAdapter2 homePageAdapter25 = this.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter25);
        ImageLoadParams.Builder mimeType = initPosition.setMimeType(homePageAdapter25.getMimeType(i3));
        HomePageAdapter2 homePageAdapter26 = this.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter26);
        ImageLoadParams.Builder fileLength = mimeType.setFileLength(homePageAdapter26.getFileLength(i3));
        HomePageAdapter2 homePageAdapter27 = this.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter27);
        ImageLoadParams.Builder createTime = fileLength.setCreateTime(homePageAdapter27.getCreateTime(i3));
        HomePageAdapter2 homePageAdapter28 = this.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter28);
        ImageLoadParams build = createTime.setLocation(homePageAdapter28.getLocation(i3)).build();
        Uri uri = PHOTOS_PAGE_URI;
        HomePageAdapter2 homePageAdapter29 = this.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter29);
        IntentUtil.gotoPreviewSelectPage(this, uri, i3, homePageAdapter29.getItemCount(), HomeMediaLoader.getHomePageSelection(this.mIsSwitchAllPhotos), null, "alias_sort_time DESC ", build, jArr, iArr, str, str2);
        EditableListViewWrapper editableListViewWrapper2 = this.mHomeGridViewWrapper;
        Intrinsics.checkNotNull(editableListViewWrapper2);
        editableListViewWrapper2.stopActionMode();
        SamplingStatHelper.recordCountEvent("home", "send", MapsKt__MapsJVMKt.mapOf(TuplesKt.to(MiStat.Param.COUNT, String.valueOf(checkedPositions.size()))));
        TrackController.trackClick("403.1.8.1.9893", "403.1.8.1.9891", checkedPositions.size());
    }

    @Override // com.miui.gallery.widget.ViewDragListener.DropReceiver
    public boolean doAfterReceived(ArrayList<String> savedFiles) {
        Intrinsics.checkNotNullParameter(savedFiles, "savedFiles");
        Uri URI_ALBUM_PAGE = GalleryContract.Common.URI_ALBUM_PAGE;
        Intrinsics.checkNotNullExpressionValue(URI_ALBUM_PAGE, "URI_ALBUM_PAGE");
        int i = 0;
        Pair[] pairArr = {TuplesKt.to("local_path", receivePath())};
        Uri.Builder buildUpon = URI_ALBUM_PAGE.buildUpon();
        while (i < 1) {
            Pair pair = pairArr[i];
            i++;
            buildUpon.appendQueryParameter((String) pair.getFirst(), (String) pair.getSecond());
        }
        Uri build = buildUpon.build();
        Intrinsics.checkNotNullExpressionValue(build, "{\n        buildUpon().ap…}\n        }.build()\n    }");
        JumpDialogFragment.showAlbumPage(getActivity(), build);
        return true;
    }

    @Override // com.bumptech.glide.ListPreloader.PreloadModelProvider
    public List<PreloadItem> getPreloadItems(int i) {
        EditableListViewWrapper editableListViewWrapper = this.mHomeGridViewWrapper;
        Intrinsics.checkNotNull(editableListViewWrapper);
        int[] unpackGroupedPosition = editableListViewWrapper.unpackGroupedPosition(i);
        if (unpackGroupedPosition[0] == -1 || unpackGroupedPosition[1] == -1) {
            return CollectionsKt__CollectionsKt.emptyList();
        }
        HomePageAdapter2 homePageAdapter2 = this.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter2);
        int packDataPosition = homePageAdapter2.packDataPosition(unpackGroupedPosition[0], unpackGroupedPosition[1]);
        HomePageAdapter2 homePageAdapter22 = this.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter22);
        return homePageAdapter22.getPreloadItems(packDataPosition);
    }

    @Override // com.bumptech.glide.ListPreloader.PreloadModelProvider
    public RequestBuilder<?> getPreloadRequestBuilder(PreloadItem item) {
        Intrinsics.checkNotNullParameter(item, "item");
        HomePageAdapter2 homePageAdapter2 = this.mHomePageAdapter;
        Intrinsics.checkNotNull(homePageAdapter2);
        GlideOptions previewRequestOptions = homePageAdapter2.getPreviewRequestOptions();
        if (previewRequestOptions == null) {
            previewRequestOptions = this.mViewMode.getRequestOptions();
        }
        GlideRequest<Bitmap> mo962load = getMRequestManager().mo985asBitmap().mo962load((Object) GalleryModel.of(item.path));
        Intrinsics.checkNotNull(previewRequestOptions);
        GlideRequest<Bitmap> secretKey = mo962load.mo946apply((BaseRequestOptions<?>) previewRequestOptions).mo974priority(Priority.LOW).fileLength(item.fileLength).decodeRegion(RegionConfig.of(item.region)).secretKey(item.secretKey);
        Intrinsics.checkNotNullExpressionValue(secretKey, "mRequestManager\n        …secretKey(item.secretKey)");
        return secretKey;
    }

    /* compiled from: HomePageFragment.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final IntroductionPage<HomePageFragment, Boolean> provideBackupSettingsGuide(HostProvider<HomePageFragment> hostProvider, CoroutineDispatcher coroutineDispatcher) {
            return new IntroductionPage<HomePageFragment, Boolean>(hostProvider, coroutineDispatcher) { // from class: com.miui.gallery.ui.HomePageFragment$Companion$provideBackupSettingsGuide$1
                public final /* synthetic */ CoroutineDispatcher $dispatcher;
                public final /* synthetic */ HostProvider<HomePageFragment> $hostProvider;

                @Override // com.miui.gallery.ui.IntroductionPage
                public boolean prejudge(HomePageFragment host, boolean z) {
                    Intrinsics.checkNotNullParameter(host, "host");
                    return true;
                }

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(hostProvider, coroutineDispatcher);
                    this.$hostProvider = hostProvider;
                    this.$dispatcher = coroutineDispatcher;
                }

                @Override // com.miui.gallery.ui.IntroductionPage
                public /* bridge */ /* synthetic */ ShowResult show(HomePageFragment homePageFragment, Boolean bool) {
                    return show(homePageFragment, bool.booleanValue());
                }

                @Override // com.miui.gallery.ui.IntroductionPage
                public Object prepareInBackground(Continuation<? super Boolean> continuation) {
                    if (!GalleryPreferences.Sync.isNeedShowAutoDownloadDialog()) {
                        GalleryPreferences.Sync.setNeedShowAutoDownloadDialog(false);
                        return Boxing.boxBoolean(false);
                    }
                    GalleryPreferences.Sync.setNeedShowAutoDownloadDialog(false);
                    if (AccountCache.getAccount() == null) {
                        return Boxing.boxBoolean(false);
                    }
                    if (!SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically()) {
                        return Boxing.boxBoolean(false);
                    }
                    return Boxing.boxBoolean(ContentResolver.getSyncAutomatically(AccountCache.getAccount(), "com.miui.gallery.cloud.provider"));
                }

                public ShowResult show(HomePageFragment host, boolean z) {
                    Intrinsics.checkNotNullParameter(host, "host");
                    if (!z) {
                        return ShowResult.SKIPPED;
                    }
                    FragmentActivity activity = host.getActivity();
                    if (activity != null) {
                        Intent intent = new Intent(activity, BackupSettingsLoginActivity.class);
                        intent.putExtra("hide_ai_pref", true);
                        activity.startActivityForResult(intent, 61);
                    }
                    return ShowResult.SHOWN_N_WAITING;
                }
            };
        }

        public final IntroductionPage<HomePageFragment, Unit> provideMultiViewGuide(HostProvider<HomePageFragment> hostProvider, CoroutineDispatcher coroutineDispatcher) {
            return new HomePageFragment$Companion$provideMultiViewGuide$1(hostProvider, coroutineDispatcher);
        }

        public final IntroductionPage<HomePageFragment, Boolean> provideMigrateGuide(HostProvider<HomePageFragment> hostProvider, CoroutineDispatcher coroutineDispatcher) {
            return new HomePageFragment$Companion$provideMigrateGuide$1(hostProvider, coroutineDispatcher);
        }

        public final IntroductionPage<HomePageFragment, List<String>> provideDeletingPermissionGuide(HostProvider<HomePageFragment> hostProvider, CoroutineDispatcher coroutineDispatcher) {
            return new HomePageFragment$Companion$provideDeletingPermissionGuide$1(hostProvider, coroutineDispatcher);
        }

        public final IntroductionPage<HomePageFragment, Optional<String>> provideSpaceFullGuide(HostProvider<HomePageFragment> hostProvider, CoroutineDispatcher coroutineDispatcher) {
            return new HomePageFragment$Companion$provideSpaceFullGuide$1(hostProvider, coroutineDispatcher);
        }

        public final IntroductionPage<HomePageFragment, Boolean> provideSlimGuide(HostProvider<HomePageFragment> hostProvider, CoroutineDispatcher coroutineDispatcher) {
            return new HomePageFragment$Companion$provideSlimGuide$1(hostProvider, coroutineDispatcher);
        }
    }

    public final void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int i) {
        Intrinsics.checkNotNullParameter(data, "data");
        ArrayList arrayList = new ArrayList();
        EditableListViewWrapper editableListViewWrapper = this.mHomeGridViewWrapper;
        Intrinsics.checkNotNull(editableListViewWrapper);
        if (editableListViewWrapper.isInActionMode()) {
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getCopyShortcutInfo());
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getCutShortcutInfo());
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getSelectAllShortcutInfo());
            arrayList.addAll(KeyboardShortcutGroupManager.getInstance().getDeleteShortcutInfo());
        } else {
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getYearShortcutInfo());
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getMonthCompactShortcutInfo());
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getMonthLooseShortcutInfo());
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getDayShortcutInfo());
            if (this.isShowSearch) {
                arrayList.add(KeyboardShortcutGroupManager.getInstance().getSearchShortcutInfo());
            }
        }
        data.add(new KeyboardShortcutGroup(getPageName(), arrayList));
    }

    public final boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        return KeyboardShortcutGroupManager.getInstance().onKeyShortcut(i, keyEvent, this.shortcutCallback);
    }

    /* compiled from: HomePageFragment.kt */
    /* loaded from: classes2.dex */
    public final class HomePageKeyboardShortcutCallback implements KeyboardShortcutGroupManager.OnKeyShortcutCallback {
        public final /* synthetic */ HomePageFragment this$0;

        /* renamed from: $r8$lambda$ITaZYePF_VNTyceRYZSVQNw-8sY */
        public static /* synthetic */ void m1489$r8$lambda$ITaZYePF_VNTyceRYZSVQNw8sY(HomePageFragment homePageFragment, int i, long[] jArr) {
            m1490onDeletePressed$lambda0(homePageFragment, i, jArr);
        }

        public HomePageKeyboardShortcutCallback(HomePageFragment this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onCopyPressed() {
            EditableListViewWrapper editableListViewWrapper = this.this$0.mHomeGridViewWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            if (editableListViewWrapper.isInActionMode()) {
                KeyboardShortcutsCopyHelper.getInstance().setCopyList(this.this$0.getBurstCheckedItemIds(), false);
                this.this$0.choiceModeListener.finish();
                return true;
            }
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onCutPressed() {
            EditableListViewWrapper editableListViewWrapper = this.this$0.mHomeGridViewWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            if (editableListViewWrapper.isInActionMode()) {
                KeyboardShortcutsCopyHelper.getInstance().setCopyList(this.this$0.getBurstCheckedItemIds(), true);
                this.this$0.choiceModeListener.finish();
            }
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onSelectAllPressed() {
            EditableListViewWrapper editableListViewWrapper = this.this$0.mHomeGridViewWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            if (editableListViewWrapper.isInActionMode()) {
                EditableListViewWrapper editableListViewWrapper2 = this.this$0.mHomeGridViewWrapper;
                Intrinsics.checkNotNull(editableListViewWrapper2);
                editableListViewWrapper2.setAllItemsCheckState(true);
            }
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onDeletePressed() {
            EditableListViewWrapper editableListViewWrapper = this.this$0.mHomeGridViewWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            if (editableListViewWrapper.isInActionMode()) {
                HomePageFragment homePageFragment = this.this$0;
                EditableListViewWrapper editableListViewWrapper2 = homePageFragment.mHomeGridViewWrapper;
                Intrinsics.checkNotNull(editableListViewWrapper2);
                homePageFragment.mAfterDeletedFirstVisiblePosAndOffset = editableListViewWrapper2.getAfterDeletedFirstVisiblePosAndOffset();
                final HomePageFragment homePageFragment2 = this.this$0;
                AppCompatActivity appCompatActivity = homePageFragment2.mActivity;
                DeletionTask.OnDeletionCompleteListener onDeletionCompleteListener = new DeletionTask.OnDeletionCompleteListener() { // from class: com.miui.gallery.ui.HomePageFragment$HomePageKeyboardShortcutCallback$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.ui.DeletionTask.OnDeletionCompleteListener
                    public final void onDeleted(int i, long[] jArr) {
                        HomePageFragment.HomePageKeyboardShortcutCallback.m1489$r8$lambda$ITaZYePF_VNTyceRYZSVQNw8sY(HomePageFragment.this, i, jArr);
                    }
                };
                long[] longArray = CollectionsKt___CollectionsKt.toLongArray(this.this$0.getBurstCheckedItemIds());
                MediaAndAlbumOperations.delete(appCompatActivity, "HomePageFragmentDeleteMediaDialogFragment", onDeletionCompleteListener, null, -1L, "", 26, 1, Arrays.copyOf(longArray, longArray.length));
                this.this$0.choiceModeListener.finish();
                return true;
            }
            return true;
        }

        /* renamed from: onDeletePressed$lambda-0 */
        public static final void m1490onDeletePressed$lambda0(HomePageFragment this$0, int i, long[] jArr) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            AppCompatActivity appCompatActivity = this$0.mActivity;
            if (appCompatActivity == null) {
                return;
            }
            boolean z = true;
            ToastUtils.makeText(appCompatActivity, this$0.getResources().getQuantityString(R.plurals.delete_finish_format, i, Integer.valueOf(i)));
            if (i > 0) {
                SoundUtils.playSoundForOperation(this$0.mActivity, 0);
            }
            this$0.choiceModeListener.finish();
            SamplingStatHelper.recordCountEvent("home", "delete_photo", MapsKt__MapsJVMKt.mapOf(TuplesKt.to(MiStat.Param.COUNT, String.valueOf(i))));
            HashMap hashMap = new HashMap();
            hashMap.put("tip", "403.1.8.1.9892");
            hashMap.put("ref_tip", "403.1.8.1.9891");
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "sure");
            hashMap.put(MiStat.Param.COUNT, Integer.valueOf(i));
            if (i <= 0) {
                z = false;
            }
            hashMap.put("success", Boolean.valueOf(z));
            TrackController.trackClick(hashMap);
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onSearchPressed() {
            EditableListViewWrapper editableListViewWrapper = this.this$0.mHomeGridViewWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            if (editableListViewWrapper.isInActionMode()) {
                return false;
            }
            Bundle bundle = new Bundle(1);
            bundle.putString("from", "from_home_page");
            Uri.Builder buildUpon = GalleryContract.Search.URI_SEARCH_PAGE.buildUpon();
            Intrinsics.checkNotNullExpressionValue(buildUpon, "URI_SEARCH_PAGE.buildUpon()");
            ActionURIHandler.handleUri(this.this$0.getActivity(), buildUpon.build(), bundle);
            FragmentActivity activity = this.this$0.getActivity();
            Intrinsics.checkNotNull(activity);
            activity.overridePendingTransition(R.anim.appear, R.anim.disappear);
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onYearModePressed() {
            EditableListViewWrapper editableListViewWrapper = this.this$0.mHomeGridViewWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            if (editableListViewWrapper.isInActionMode()) {
                return false;
            }
            HomePageFragment homePageFragment = this.this$0;
            PictureViewMode TINY_THUMB = PictureViewMode.TINY_THUMB;
            Intrinsics.checkNotNullExpressionValue(TINY_THUMB, "TINY_THUMB");
            homePageFragment.setPictureViewMode(TINY_THUMB);
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onMonthCompactModePressed() {
            EditableListViewWrapper editableListViewWrapper = this.this$0.mHomeGridViewWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            if (editableListViewWrapper.isInActionMode()) {
                return false;
            }
            HomePageFragment homePageFragment = this.this$0;
            PictureViewMode MINI_THUMB = PictureViewMode.MINI_THUMB;
            Intrinsics.checkNotNullExpressionValue(MINI_THUMB, "MINI_THUMB");
            homePageFragment.setPictureViewMode(MINI_THUMB);
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onMonthLooseModePressed() {
            EditableListViewWrapper editableListViewWrapper = this.this$0.mHomeGridViewWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            if (editableListViewWrapper.isInActionMode()) {
                return false;
            }
            HomePageFragment homePageFragment = this.this$0;
            PictureViewMode MICRO_THUMB = PictureViewMode.MICRO_THUMB;
            Intrinsics.checkNotNullExpressionValue(MICRO_THUMB, "MICRO_THUMB");
            homePageFragment.setPictureViewMode(MICRO_THUMB);
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onDayModePressed() {
            EditableListViewWrapper editableListViewWrapper = this.this$0.mHomeGridViewWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            if (editableListViewWrapper.isInActionMode()) {
                return false;
            }
            HomePageFragment homePageFragment = this.this$0;
            PictureViewMode LARGE_THUMB = PictureViewMode.LARGE_THUMB;
            Intrinsics.checkNotNullExpressionValue(LARGE_THUMB, "LARGE_THUMB");
            homePageFragment.setPictureViewMode(LARGE_THUMB);
            return true;
        }
    }

    @Override // com.miui.gallery.app.fragment.MiuiFragment, com.miui.gallery.listener.OnVisibilityChangeListener
    public void onVisibleChange(boolean z) {
        if (z) {
            if (isEmpty()) {
                AutoTracking.trackView(getPageTip(), AutoTracking.getRef(), "empty");
            } else {
                AutoTracking.trackView(getPageTip(), AutoTracking.getRef(), PictureViewMode.isDayMode(this.mViewMode) ? "day" : "month");
            }
        }
    }

    /* compiled from: HomePageFragment.kt */
    /* loaded from: classes2.dex */
    public static final class HomePageProvider implements HostProvider<HomePageFragment>, DefaultLifecycleObserver {
        public final WeakReference<HomePageFragment> hostRef;

        public HomePageProvider(HomePageFragment host) {
            Intrinsics.checkNotNullParameter(host, "host");
            this.hostRef = new WeakReference<>(host);
            host.getLifecycle().addObserver(this);
        }

        @Override // com.miui.gallery.ui.HostProvider
        public HomePageFragment provide() {
            return this.hostRef.get();
        }

        @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
        public void onDestroy(LifecycleOwner owner) {
            Lifecycle lifecycle;
            Intrinsics.checkNotNullParameter(owner, "owner");
            HomePageFragment homePageFragment = this.hostRef.get();
            if (homePageFragment != null && (lifecycle = homePageFragment.getLifecycle()) != null) {
                lifecycle.removeObserver(this);
            }
            this.hostRef.clear();
            DefaultLogger.d("HomePageFragment", "clear host ref");
        }
    }
}
