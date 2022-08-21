package com.miui.gallery.biz.story;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.os.BundleKt;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentViewModelLazyKt;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.arch.events.ActivityHosted;
import com.miui.gallery.arch.events.ContextHosted;
import com.miui.gallery.arch.events.FragmentHosted;
import com.miui.gallery.arch.events.SingleLiveEvent;
import com.miui.gallery.arch.events.ViewEvent;
import com.miui.gallery.arch.platform.UIComponent;
import com.miui.gallery.biz.story.StoryAlbumViewModel;
import com.miui.gallery.biz.story.data.DownloadCommand;
import com.miui.gallery.biz.story.data.DownloadStatus;
import com.miui.gallery.biz.story.data.MediaInfo;
import com.miui.gallery.biz.story.data.MediaStats;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.core.LayoutParamsHelper;
import com.miui.gallery.card.ui.detail.DownloadDialog;
import com.miui.gallery.card.ui.detail.SlideShowHeaderView;
import com.miui.gallery.card.ui.detail.StoryAlbumRenameDialog;
import com.miui.gallery.card.ui.detail.StoryRecyclerViewItem;
import com.miui.gallery.model.ImageLoadParamsKt;
import com.miui.gallery.picker.uri.DownloadConfirmDialog;
import com.miui.gallery.picker.uri.Downloader;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.ui.ConfirmDialog;
import com.miui.gallery.ui.KeyboardShortcutGroupManager;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.ui.actionBar.BaseCommonActionBarHelper;
import com.miui.gallery.ui.actionBar.SimpleThemeActionBarHelper;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryPullZoomLayout;
import com.miui.gallery.widget.RoundedFrameLayout;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.editwrapper.MultiChoiceModeListener;
import com.miui.gallery.widget.menu.ImmersionMenu;
import com.miui.gallery.widget.menu.ImmersionMenuItem;
import com.miui.gallery.widget.menu.ImmersionMenuListener;
import com.miui.gallery.widget.menu.PhoneImmersionMenu;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import com.miui.gallery.widget.recyclerview.SimpleHeaderFooterWrapperAdapter;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import kotlin.Lazy;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.ranges.RangesKt___RangesKt;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.EaseManager;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AppCompatActivity;

/* compiled from: StoryAlbumFragment.kt */
/* loaded from: classes.dex */
public final class StoryAlbumFragment extends Hilt_StoryAlbumFragment implements UIComponent {
    public static final Companion Companion = new Companion(null);
    public SimpleThemeActionBarHelper actionBarHelper;
    public StoryAlbumAdapter adapter;
    public View contentLayout;
    public ViewGroup.MarginLayoutParams contentLayoutParams;
    public TextView descriptionView;
    public DownloadDialog downloadDialog;
    public EditableListViewWrapper editableListWrapper;
    public long enterShowTime;
    public int exactScreenVertical;
    public int flexboxHeight;
    public ViewGroup headerView;
    public PhoneImmersionMenu immersionMenu;
    public boolean isFromWidget;
    public boolean isInActionMode;
    public int launchOrientation;
    public View playIcon;
    public Runnable playIconUpdater;
    public GalleryRecyclerView recyclerView;
    public long reviewDuration;
    public ViewGroup rootView;
    public int screenHorizontal;
    public GalleryPullZoomLayout scrollingLayout;
    public int sliderHeight;
    public float sliderProgress;
    public SlideShowHeaderView sliderView;
    public TextView titleView;
    public Bundle transitionInfo;
    public WeakReferenceTransitionListener transitionListener;
    public int translationDy;
    public RoundedFrameLayout translationView;
    public StoryAlbumViewModel.AssistedVMFactory vmFactory;
    public final Lazy viewModel$delegate = FragmentViewModelLazyKt.createViewModelLazy(this, Reflection.getOrCreateKotlinClass(StoryAlbumViewModel.class), new StoryAlbumFragment$special$$inlined$assistedViewModel$3(new StoryAlbumFragment$special$$inlined$assistedViewModel$2(this)), new StoryAlbumFragment$special$$inlined$assistedViewModel$1(this, this));
    public final boolean isPlayIconScrollStateEnable = true;
    public StoryPageKeyboardShortcutCallback shortcutCallback = new StoryPageKeyboardShortcutCallback(this);
    public final NetworkConsider.OnConfirmed onConfirmedListener = new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda11
        @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
        public final void onConfirmed(boolean z, boolean z2) {
            StoryAlbumFragment.m605onConfirmedListener$lambda3(StoryAlbumFragment.this, z, z2);
        }
    };
    public final DialogInterface.OnClickListener confirmDownloadListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda0
        @Override // android.content.DialogInterface.OnClickListener
        public final void onClick(DialogInterface dialogInterface, int i) {
            StoryAlbumFragment.m595$r8$lambda$YwlbpWM_ce4PimTRY5Vjflk57k(StoryAlbumFragment.this, dialogInterface, i);
        }
    };
    public final DialogInterface.OnClickListener cancelDownloadListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda1
        @Override // android.content.DialogInterface.OnClickListener
        public final void onClick(DialogInterface dialogInterface, int i) {
            StoryAlbumFragment.$r8$lambda$fnNoQPE1m5sDtQDghDLeDW0H9Is(StoryAlbumFragment.this, dialogInterface, i);
        }
    };
    public final DownloadDialog.CallBack downloadCallback = new DownloadDialog.CallBack() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda9
        @Override // com.miui.gallery.card.ui.detail.DownloadDialog.CallBack
        public final void cancel() {
            StoryAlbumFragment.m593$r8$lambda$7agilS_uPWIDz_orJDjez1g6kc(StoryAlbumFragment.this);
        }
    };
    public final ImmersionMenuListener immersionMenuListener = new ImmersionMenuListener() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$immersionMenuListener$1
        @Override // com.miui.gallery.widget.menu.ImmersionMenuListener
        public boolean onPrepareImmersionMenu(ImmersionMenu menu) {
            Intrinsics.checkNotNullParameter(menu, "menu");
            return false;
        }

        @Override // com.miui.gallery.widget.menu.ImmersionMenuListener
        public void onCreateImmersionMenu(ImmersionMenu menu) {
            AppCompatActivity appCompatActivity;
            AppCompatActivity appCompatActivity2;
            AppCompatActivity appCompatActivity3;
            Intrinsics.checkNotNullParameter(menu, "menu");
            appCompatActivity = StoryAlbumFragment.this.mActivity;
            if (appCompatActivity == null) {
                return;
            }
            appCompatActivity2 = StoryAlbumFragment.this.mActivity;
            menu.add(R.id.menu_card_rename, appCompatActivity2.getString(R.string.story_option_menu_rename));
            Card value = StoryAlbumFragment.this.mo617getViewModel().getCard().getValue();
            boolean z = false;
            if (value != null && value.isDeletable()) {
                z = true;
            }
            if (!z) {
                return;
            }
            appCompatActivity3 = StoryAlbumFragment.this.mActivity;
            menu.add(R.id.menu_card_delete, appCompatActivity3.getString(R.string.story_option_menu_delete));
        }

        @Override // com.miui.gallery.widget.menu.ImmersionMenuListener
        public void onImmersionMenuSelected(ImmersionMenu menu, ImmersionMenuItem item) {
            AppCompatActivity appCompatActivity;
            Intrinsics.checkNotNullParameter(menu, "menu");
            Intrinsics.checkNotNullParameter(item, "item");
            appCompatActivity = StoryAlbumFragment.this.mActivity;
            if (appCompatActivity == null) {
                return;
            }
            switch (item.getItemId()) {
                case R.id.menu_card_delete /* 2131362880 */:
                    StoryAlbumFragment.this.showDeleteConfirmDialog();
                    return;
                case R.id.menu_card_rename /* 2131362881 */:
                    StoryAlbumFragment.this.showRenameDialog();
                    return;
                default:
                    return;
            }
        }
    };
    public final ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$globalLayoutListener$1
        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            int i;
            GalleryRecyclerView galleryRecyclerView;
            i = StoryAlbumFragment.this.flexboxHeight;
            if (i == 0) {
                galleryRecyclerView = StoryAlbumFragment.this.recyclerView;
                if (galleryRecyclerView == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
                    galleryRecyclerView = null;
                }
                galleryRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                List<MediaInfo> value = StoryAlbumFragment.this.mo617getViewModel().getMedias().getValue();
                if (value == null) {
                    return;
                }
                StoryAlbumFragment.this.updateFlexBoxLayoutSize(value);
            }
        }
    };
    public final MultiChoiceModeListener multiChoiceModeListener = new StoryAlbumFragment$multiChoiceModeListener$1(this);

    /* renamed from: $r8$lambda$-YgWCkebc4VJbusaZZxLJ5dg8J0 */
    public static /* synthetic */ void m590$r8$lambda$YgWCkebc4VJbusaZZxLJ5dg8J0(StoryAlbumFragment storyAlbumFragment, View view) {
        m602onActivityCreated$lambda16$lambda14(storyAlbumFragment, view);
    }

    /* renamed from: $r8$lambda$1ddSpb8ZJuJl-ztpovUgCdsyKf4 */
    public static /* synthetic */ void m592$r8$lambda$1ddSpb8ZJuJlztpovUgCdsyKf4(StoryAlbumFragment storyAlbumFragment, GalleryPullZoomLayout.ScrollBy scrollBy, float f) {
        m609rebuild$lambda34$lambda33(storyAlbumFragment, scrollBy, f);
    }

    public static /* synthetic */ void $r8$lambda$1jSINAJfsFFM3MCbixem6LU9bFE(StoryAlbumFragment storyAlbumFragment, List list) {
        m614subscribeMedias$lambda20(storyAlbumFragment, list);
    }

    public static /* synthetic */ void $r8$lambda$2NusQRTYkd9WvnzC5ndarUT_CFE(StoryAlbumFragment storyAlbumFragment, MediaStats mediaStats) {
        m615subscribeMedias$lambda21(storyAlbumFragment, mediaStats);
    }

    /* renamed from: $r8$lambda$7agilS_-uPWIDz_orJDjez1g6kc */
    public static /* synthetic */ void m593$r8$lambda$7agilS_uPWIDz_orJDjez1g6kc(StoryAlbumFragment storyAlbumFragment) {
        m601downloadCallback$lambda6(storyAlbumFragment);
    }

    /* renamed from: $r8$lambda$Yw-lbpWM_ce4PimTRY5Vjflk57k */
    public static /* synthetic */ void m595$r8$lambda$YwlbpWM_ce4PimTRY5Vjflk57k(StoryAlbumFragment storyAlbumFragment, DialogInterface dialogInterface, int i) {
        m600confirmDownloadListener$lambda4(storyAlbumFragment, dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$fnNoQPE1m5sDtQDghDLeDW0H9Is(StoryAlbumFragment storyAlbumFragment, DialogInterface dialogInterface, int i) {
        m599cancelDownloadListener$lambda5(storyAlbumFragment, dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$ltOK8dY7P5ccHyZMdA2JzXWPgm8(StoryAlbumFragment storyAlbumFragment, GalleryPullZoomLayout galleryPullZoomLayout) {
        m608rebuild$lambda34$lambda32(storyAlbumFragment, galleryPullZoomLayout);
    }

    public static /* synthetic */ void $r8$lambda$o4rLqeaLcOS_q_LfPvtjVuyqoVs(StoryAlbumFragment storyAlbumFragment) {
        m604onActivityCreated$lambda17(storyAlbumFragment);
    }

    /* renamed from: $r8$lambda$ulU9rF5x-HL5UprvU1K0xbfsBB0 */
    public static /* synthetic */ void m597$r8$lambda$ulU9rF5xHL5UprvU1K0xbfsBB0(StoryAlbumFragment storyAlbumFragment, int i) {
        m616updateSliderPlayStatus$lambda37(storyAlbumFragment, i);
    }

    /* renamed from: $r8$lambda$uq4nwU-jABsI7O87_yF0B5ciASs */
    public static /* synthetic */ void m598$r8$lambda$uq4nwUjABsI7O87_yF0B5ciASs(StoryAlbumFragment storyAlbumFragment, View view) {
        m603onActivityCreated$lambda16$lambda15(storyAlbumFragment, view);
    }

    public static /* synthetic */ void $r8$lambda$yE3_vM2llvQ0NpRVGjqH5Wzf4b4(StoryAlbumFragment storyAlbumFragment, View view) {
        m607rebuild$lambda31(storyAlbumFragment, view);
    }

    public static /* synthetic */ void $r8$lambda$yjJ4IRlQW7X2t5OCaHDLsJeigQU(StoryAlbumFragment storyAlbumFragment, List list) {
        m613subscribeMedias$lambda19(storyAlbumFragment, list);
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "story_album";
    }

    public final StoryAlbumViewModel.AssistedVMFactory getVmFactory$app_cnRelease() {
        StoryAlbumViewModel.AssistedVMFactory assistedVMFactory = this.vmFactory;
        if (assistedVMFactory != null) {
            return assistedVMFactory;
        }
        Intrinsics.throwUninitializedPropertyAccessException("vmFactory");
        return null;
    }

    @Override // com.miui.gallery.arch.platform.UIComponent
    /* renamed from: getViewModel */
    public StoryAlbumViewModel mo617getViewModel() {
        return (StoryAlbumViewModel) this.viewModel$delegate.mo119getValue();
    }

    /* renamed from: onConfirmedListener$lambda-3 */
    public static final void m605onConfirmedListener$lambda3(StoryAlbumFragment this$0, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (z) {
            if (!BaseNetworkUtils.isNetworkConnected()) {
                ToastUtils.makeText(this$0.mActivity, (int) R.string.picker_no_network_message);
                this$0.showConfirmDialog(true);
                return;
            }
            this$0.startDownload();
            return;
        }
        this$0.mo617getViewModel().cancelDownload();
    }

    /* renamed from: confirmDownloadListener$lambda-4 */
    public static final void m600confirmDownloadListener$lambda4(StoryAlbumFragment this$0, DialogInterface dialogInterface, int i) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (!BaseNetworkUtils.isNetworkConnected()) {
            ToastUtils.makeText(this$0.mActivity, (int) R.string.picker_no_network_message);
            this$0.showConfirmDialog(true);
            return;
        }
        this$0.startDownload();
    }

    /* renamed from: cancelDownloadListener$lambda-5 */
    public static final void m599cancelDownloadListener$lambda5(StoryAlbumFragment this$0, DialogInterface dialogInterface, int i) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.mo617getViewModel().cancelDownload();
    }

    /* renamed from: downloadCallback$lambda-6 */
    public static final void m601downloadCallback$lambda6(StoryAlbumFragment this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.mo617getViewModel().cancelDownload();
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        startObserveEvents();
    }

    @Override // com.miui.gallery.arch.platform.UIComponent
    public void onReceiveEvent(ViewEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (event instanceof ContextHosted) {
            Context requireContext = requireContext();
            Intrinsics.checkNotNullExpressionValue(requireContext, "requireContext()");
            ((ContextHosted) event).invoke(requireContext);
        } else if (event instanceof ActivityHosted) {
            FragmentActivity requireActivity = requireActivity();
            Intrinsics.checkNotNullExpressionValue(requireActivity, "requireActivity()");
            ((ActivityHosted) event).invoke(requireActivity);
        } else if (!(event instanceof FragmentHosted)) {
        } else {
            ((FragmentHosted) event).invoke(this);
        }
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        Intrinsics.checkNotNullParameter(inflater, "inflater");
        Intrinsics.checkNotNullParameter(container, "container");
        View inflate = inflater.inflate(R.layout.story_album_layout, container, false);
        this.sliderHeight = (int) getResources().getDimension(R.dimen.story_slide_header_height);
        this.exactScreenVertical = ScreenUtils.getExactScreenVertical(this.mActivity);
        this.screenHorizontal = ScreenUtils.getScreenHorizontal(this.mActivity);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
        this.rootView = (ViewGroup) inflate;
        View findViewById = inflate.findViewById(R.id.face_header_item);
        Intrinsics.checkNotNullExpressionValue(findViewById, "view.findViewById(R.id.face_header_item)");
        ViewGroup viewGroup = (ViewGroup) findViewById;
        this.headerView = viewGroup;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("headerView");
            viewGroup = null;
        }
        this.playIcon = viewGroup.findViewById(R.id.play_icon);
        ViewGroup viewGroup2 = this.headerView;
        if (viewGroup2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("headerView");
            viewGroup2 = null;
        }
        View findViewById2 = viewGroup2.findViewById(R.id.title);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "headerView.findViewById(R.id.title)");
        this.titleView = (TextView) findViewById2;
        ViewGroup viewGroup3 = this.headerView;
        if (viewGroup3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("headerView");
            viewGroup3 = null;
        }
        View findViewById3 = viewGroup3.findViewById(R.id.description);
        Intrinsics.checkNotNullExpressionValue(findViewById3, "headerView.findViewById(R.id.description)");
        this.descriptionView = (TextView) findViewById3;
        View findViewById4 = inflate.findViewById(R.id.recycler_view);
        Intrinsics.checkNotNullExpressionValue(findViewById4, "view.findViewById(R.id.recycler_view)");
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) findViewById4;
        this.recyclerView = galleryRecyclerView;
        if (galleryRecyclerView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
            galleryRecyclerView = null;
        }
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this.mActivity);
        flexboxLayoutManager.setFlexWrap(1);
        flexboxLayoutManager.setFlexDirection(0);
        flexboxLayoutManager.setAlignItems(4);
        flexboxLayoutManager.setRecycleChildrenOnDetach(true);
        galleryRecyclerView.setLayoutManager(flexboxLayoutManager);
        FlexboxItemDecoration flexboxItemDecoration = new FlexboxItemDecoration(this.mActivity);
        flexboxItemDecoration.setDrawable(galleryRecyclerView.getResources().getDrawable(R.drawable.card_item_divider));
        galleryRecyclerView.addItemDecoration(flexboxItemDecoration);
        galleryRecyclerView.setItemAnimator(null);
        galleryRecyclerView.setNestedScrollingEnabled(true);
        StoryAlbumAdapter storyAlbumAdapter = new StoryAlbumAdapter(this.mActivity);
        storyAlbumAdapter.setHasStableIds(true);
        this.adapter = storyAlbumAdapter;
        GalleryRecyclerView galleryRecyclerView2 = this.recyclerView;
        if (galleryRecyclerView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
            galleryRecyclerView2 = null;
        }
        EditableListViewWrapper editableListViewWrapper = new EditableListViewWrapper(galleryRecyclerView2);
        editableListViewWrapper.setHandleTouchAnimItemType(StoryRecyclerViewItem.class.getSimpleName());
        editableListViewWrapper.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda14
            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public final boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                boolean m606onInflateView$lambda12$lambda11;
                m606onInflateView$lambda12$lambda11 = StoryAlbumFragment.m606onInflateView$lambda12$lambda11(StoryAlbumFragment.this, recyclerView, view, i, j, f, f2);
                return m606onInflateView$lambda12$lambda11;
            }
        });
        editableListViewWrapper.enableChoiceMode(true);
        editableListViewWrapper.enterChoiceModeWithLongClick(true);
        editableListViewWrapper.setMultiChoiceModeListener(this.multiChoiceModeListener);
        StoryAlbumAdapter storyAlbumAdapter2 = this.adapter;
        if (storyAlbumAdapter2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("adapter");
            storyAlbumAdapter2 = null;
        }
        editableListViewWrapper.setAdapter(new SimpleHeaderFooterWrapperAdapter(storyAlbumAdapter2, null, null));
        this.editableListWrapper = editableListViewWrapper;
        Configuration configuration = getResources().getConfiguration();
        Intrinsics.checkNotNullExpressionValue(configuration, "resources.configuration");
        updateConfiguration(configuration);
        subscribeDownloadTasks();
        subscribeMedias();
        return inflate;
    }

    /* renamed from: onInflateView$lambda-12$lambda-11 */
    public static final boolean m606onInflateView$lambda12$lambda11(StoryAlbumFragment this$0, RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        GalleryRecyclerView galleryRecyclerView;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        StoryAlbumViewModel mo617getViewModel = this$0.mo617getViewModel();
        GalleryRecyclerView galleryRecyclerView2 = this$0.recyclerView;
        StoryAlbumAdapter storyAlbumAdapter = null;
        if (galleryRecyclerView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
            galleryRecyclerView = null;
        } else {
            galleryRecyclerView = galleryRecyclerView2;
        }
        StoryAlbumAdapter storyAlbumAdapter2 = this$0.adapter;
        if (storyAlbumAdapter2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("adapter");
        } else {
            storyAlbumAdapter = storyAlbumAdapter2;
        }
        mo617getViewModel.gotoPhotoPage(this$0, galleryRecyclerView, i, storyAlbumAdapter.getItemCount(), ImageLoadParamsKt.ImageLoadParams(new StoryAlbumFragment$onInflateView$3$1$1(this$0, i)));
        return true;
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        Intrinsics.checkNotNullParameter(newConfig, "newConfig");
        super.onConfigurationChanged(newConfig);
        updateConfiguration(newConfig);
    }

    public final void updateConfiguration(Configuration configuration) {
        TextView textView = this.titleView;
        TextView textView2 = null;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("titleView");
            textView = null;
        }
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
        TextView textView3 = this.descriptionView;
        if (textView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("descriptionView");
            textView3 = null;
        }
        ViewGroup.LayoutParams layoutParams3 = textView3.getLayoutParams();
        Objects.requireNonNull(layoutParams3, "null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
        LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) layoutParams3;
        if (getResources().getDimensionPixelSize(R.dimen.story_album_header_text_margin_start) == 0) {
            layoutParams2.gravity = 17;
            layoutParams4.gravity = 17;
        } else {
            layoutParams2.gravity = 8388611;
            layoutParams4.gravity = 8388611;
        }
        layoutParams2.setMarginStart(getResources().getDimensionPixelSize(R.dimen.story_album_header_text_margin_start));
        layoutParams4.setMarginStart(getResources().getDimensionPixelSize(R.dimen.story_album_header_text_margin_start));
        TextView textView4 = this.titleView;
        if (textView4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("titleView");
            textView4 = null;
        }
        textView4.setLayoutParams(layoutParams2);
        TextView textView5 = this.descriptionView;
        if (textView5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("descriptionView");
        } else {
            textView2 = textView5;
        }
        textView2.setLayoutParams(layoutParams4);
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        Intrinsics.checkNotNullParameter(outState, "outState");
        super.onSaveInstanceState(outState);
        outState.putBundle("transition_info", this.transitionInfo);
    }

    public final boolean checkData(Bundle bundle) {
        if (bundle != null) {
            this.transitionInfo = bundle.getBundle("transition_info");
        }
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("transition_info")) {
            this.transitionInfo = arguments.getBundle("transition_info");
        }
        if (arguments != null) {
            this.isFromWidget = arguments.getBoolean("open_story_from_widget", this.isFromWidget);
        }
        if (this.transitionInfo == null) {
            DefaultLogger.e("StoryAlbumFragment", "Missing transition info");
            return false;
        }
        return true;
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (!checkData(bundle)) {
            return;
        }
        initTranView();
        this.immersionMenu = new PhoneImmersionMenu(this.mActivity, this.immersionMenuListener);
        BaseCommonActionBarHelper.DefaultThemeConfig defaultThemeConfig = new BaseCommonActionBarHelper.DefaultThemeConfig();
        defaultThemeConfig.setActionBarLayoutRes(R.layout.story_album_actionbar_content);
        defaultThemeConfig.setOnBackClickListener(new View.OnClickListener() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StoryAlbumFragment.m590$r8$lambda$YgWCkebc4VJbusaZZxLJ5dg8J0(StoryAlbumFragment.this, view);
            }
        });
        defaultThemeConfig.setOnMenuClickListener(new View.OnClickListener() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StoryAlbumFragment.m598$r8$lambda$uq4nwUjABsI7O87_yF0B5ciASs(StoryAlbumFragment.this, view);
            }
        });
        defaultThemeConfig.setAnimActionBarBg(false);
        SimpleThemeActionBarHelper simpleThemeActionBarHelper = new SimpleThemeActionBarHelper(this.mActivity, defaultThemeConfig);
        this.actionBarHelper = simpleThemeActionBarHelper;
        simpleThemeActionBarHelper.inflateActionBar();
        SimpleThemeActionBarHelper simpleThemeActionBarHelper2 = this.actionBarHelper;
        SlideShowHeaderView slideShowHeaderView = null;
        if (simpleThemeActionBarHelper2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("actionBarHelper");
            simpleThemeActionBarHelper2 = null;
        }
        simpleThemeActionBarHelper2.setNullStyleActionBar();
        SlideShowHeaderView slideShowHeaderView2 = this.sliderView;
        if (slideShowHeaderView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            slideShowHeaderView2 = null;
        }
        slideShowHeaderView2.post(new Runnable() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                StoryAlbumFragment.$r8$lambda$o4rLqeaLcOS_q_LfPvtjVuyqoVs(StoryAlbumFragment.this);
            }
        });
        SlideShowHeaderView slideShowHeaderView3 = this.sliderView;
        if (slideShowHeaderView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
        } else {
            slideShowHeaderView = slideShowHeaderView3;
        }
        slideShowHeaderView.setIsEnter(true);
    }

    /* renamed from: onActivityCreated$lambda-16$lambda-14 */
    public static final void m602onActivityCreated$lambda16$lambda14(StoryAlbumFragment this$0, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.finish();
    }

    /* renamed from: onActivityCreated$lambda-16$lambda-15 */
    public static final void m603onActivityCreated$lambda16$lambda15(StoryAlbumFragment this$0, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        PhoneImmersionMenu phoneImmersionMenu = this$0.immersionMenu;
        if (phoneImmersionMenu == null) {
            Intrinsics.throwUninitializedPropertyAccessException("immersionMenu");
            phoneImmersionMenu = null;
        }
        phoneImmersionMenu.show(view, null);
    }

    /* renamed from: onActivityCreated$lambda-17 */
    public static final void m604onActivityCreated$lambda17(StoryAlbumFragment this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.startTransition();
    }

    public final void subscribeMedias() {
        mo617getViewModel().getCard().observe(getViewLifecycleOwner(), new Observer() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda6
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                StoryAlbumFragment.m612subscribeMedias$lambda18(StoryAlbumFragment.this, (Card) obj);
            }
        });
        mo617getViewModel().getMedias().observe(getViewLifecycleOwner(), new Observer() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda8
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                StoryAlbumFragment.$r8$lambda$yjJ4IRlQW7X2t5OCaHDLsJeigQU(StoryAlbumFragment.this, (List) obj);
            }
        });
        mo617getViewModel().getTopMedias().observe(getViewLifecycleOwner(), new Observer() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda7
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                StoryAlbumFragment.$r8$lambda$1jSINAJfsFFM3MCbixem6LU9bFE(StoryAlbumFragment.this, (List) obj);
            }
        });
        mo617getViewModel().getMediaStats().observe(getViewLifecycleOwner(), new Observer() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda5
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                StoryAlbumFragment.$r8$lambda$2NusQRTYkd9WvnzC5ndarUT_CFE(StoryAlbumFragment.this, (MediaStats) obj);
            }
        });
    }

    /* renamed from: subscribeMedias$lambda-18 */
    public static final void m612subscribeMedias$lambda18(StoryAlbumFragment this$0, Card card) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        String title = card.getTitle();
        Intrinsics.checkNotNullExpressionValue(title, "it.title");
        this$0.setTitle(title);
        TextView textView = this$0.descriptionView;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("descriptionView");
            textView = null;
        }
        textView.setText(card.getDescription());
    }

    /* renamed from: subscribeMedias$lambda-19 */
    public static final void m613subscribeMedias$lambda19(StoryAlbumFragment this$0, List it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        GalleryPullZoomLayout galleryPullZoomLayout = this$0.scrollingLayout;
        if (galleryPullZoomLayout != null) {
            galleryPullZoomLayout.setOriginalHeight((int) this$0.getResources().getDimension(R.dimen.story_slide_header_height));
        }
        StoryAlbumAdapter storyAlbumAdapter = this$0.adapter;
        if (storyAlbumAdapter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("adapter");
            storyAlbumAdapter = null;
        }
        Intrinsics.checkNotNullExpressionValue(it, "it");
        storyAlbumAdapter.setData$app_cnRelease(it);
        this$0.updateFlexBoxLayoutSize(it);
    }

    /* renamed from: subscribeMedias$lambda-20 */
    public static final void m614subscribeMedias$lambda20(StoryAlbumFragment this$0, List list) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        SlideShowHeaderView slideShowHeaderView = this$0.sliderView;
        SlideShowHeaderView slideShowHeaderView2 = null;
        if (slideShowHeaderView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            slideShowHeaderView = null;
        }
        slideShowHeaderView.updateMedias(list);
        if (this$0.transitionInfo != null) {
            SlideShowHeaderView slideShowHeaderView3 = this$0.sliderView;
            if (slideShowHeaderView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sliderView");
                slideShowHeaderView3 = null;
            }
            Bundle bundle = this$0.transitionInfo;
            Intrinsics.checkNotNull(bundle);
            slideShowHeaderView3.setLoadIndex(bundle.getInt("current_index"));
        }
        if (this$0.isEnterShowFinished()) {
            SlideShowHeaderView slideShowHeaderView4 = this$0.sliderView;
            if (slideShowHeaderView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            } else {
                slideShowHeaderView2 = slideShowHeaderView4;
            }
            slideShowHeaderView2.startSliderShow();
        }
    }

    /* renamed from: subscribeMedias$lambda-21 */
    public static final void m615subscribeMedias$lambda21(StoryAlbumFragment this$0, MediaStats it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullExpressionValue(it, "it");
        this$0.updateSliderPlayStatus(it);
    }

    public final void subscribeDownloadTasks() {
        LiveData<SingleLiveEvent<DownloadCommand>> downloadCommands = mo617getViewModel().getDownloadCommands();
        LifecycleOwner viewLifecycleOwner = getViewLifecycleOwner();
        Intrinsics.checkNotNullExpressionValue(viewLifecycleOwner, "viewLifecycleOwner");
        downloadCommands.observe(viewLifecycleOwner, new Observer() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$subscribeDownloadTasks$$inlined$observeEvent$1
            public final void onChanged(SingleLiveEvent<? extends T> singleLiveEvent) {
                T contentIfNotHandled;
                AppCompatActivity appCompatActivity;
                if (singleLiveEvent == null || (contentIfNotHandled = singleLiveEvent.getContentIfNotHandled()) == null) {
                    return;
                }
                DownloadCommand downloadCommand = (DownloadCommand) contentIfNotHandled;
                SlideShowHeaderView slideShowHeaderView = StoryAlbumFragment.this.sliderView;
                if (slideShowHeaderView == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("sliderView");
                    slideShowHeaderView = null;
                }
                slideShowHeaderView.setEnabled(true);
                List<Downloader.DownloadTask> tasks = downloadCommand.getTasks();
                if (!(tasks == null || tasks.isEmpty())) {
                    if (BaseNetworkUtils.isNetworkConnected()) {
                        if (!BaseNetworkUtils.isActiveNetworkMetered()) {
                            if (downloadCommand.getRetry()) {
                                StoryAlbumFragment.this.showConfirmDialog(true);
                                return;
                            } else {
                                StoryAlbumFragment.this.startDownload();
                                return;
                            }
                        }
                        StoryAlbumFragment.this.showNetworkMeteredDialog();
                        return;
                    }
                    appCompatActivity = StoryAlbumFragment.this.mActivity;
                    ToastUtils.makeText(appCompatActivity, (int) R.string.story_download_failed_for_notwork);
                } else if (downloadCommand.getRetry()) {
                } else {
                    StoryAlbumFragment.this.doCreation();
                }
            }

            @Override // androidx.lifecycle.Observer
            public /* bridge */ /* synthetic */ void onChanged(Object obj) {
                onChanged((SingleLiveEvent) ((SingleLiveEvent) obj));
            }
        });
        mo617getViewModel().getDownloadCallbacks().observeForever(new Observer() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$subscribeDownloadTasks$$inlined$observeEventForever$1
            public final void onChanged(SingleLiveEvent<? extends T> singleLiveEvent) {
                T contentIfNotHandled;
                DownloadDialog downloadDialog;
                DownloadDialog downloadDialog2;
                DownloadDialog downloadDialog3;
                AppCompatActivity appCompatActivity;
                if (singleLiveEvent == null || (contentIfNotHandled = singleLiveEvent.getContentIfNotHandled()) == null) {
                    return;
                }
                DownloadStatus downloadStatus = (DownloadStatus) contentIfNotHandled;
                if (downloadStatus instanceof DownloadStatus.STARTED) {
                    DefaultLogger.d("StoryAlbumFragment", Intrinsics.stringPlus("Download start with size: ", Integer.valueOf(((DownloadStatus.STARTED) downloadStatus).getTasks().size())));
                    StoryAlbumFragment storyAlbumFragment = StoryAlbumFragment.this;
                    DownloadDialog downloadDialog4 = new DownloadDialog();
                    appCompatActivity = StoryAlbumFragment.this.mActivity;
                    downloadDialog4.show(appCompatActivity);
                    storyAlbumFragment.downloadDialog = downloadDialog4;
                } else if (downloadStatus instanceof DownloadStatus.PROGRESS) {
                    DownloadStatus.PROGRESS progress = (DownloadStatus.PROGRESS) downloadStatus;
                    DefaultLogger.d("StoryAlbumFragment", Intrinsics.stringPlus("Download progress: ", Float.valueOf(progress.getProgress())));
                    downloadDialog3 = StoryAlbumFragment.this.downloadDialog;
                    if (downloadDialog3 == null) {
                        return;
                    }
                    downloadDialog3.updateProgress(progress.getProgress());
                } else if (downloadStatus instanceof DownloadStatus.CANCELLED) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Download cancelled, success: ");
                    DownloadStatus.CANCELLED cancelled = (DownloadStatus.CANCELLED) downloadStatus;
                    sb.append(cancelled.getSuccess().size());
                    sb.append(", fails: ");
                    sb.append(cancelled.getFails().size());
                    DefaultLogger.d("StoryAlbumFragment", sb.toString());
                    downloadDialog2 = StoryAlbumFragment.this.downloadDialog;
                    if (downloadDialog2 == null) {
                        return;
                    }
                    downloadDialog2.dismissSafely();
                    StoryAlbumFragment.this.downloadDialog = null;
                } else if (!(downloadStatus instanceof DownloadStatus.COMPLETED)) {
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Download end, success: ");
                    DownloadStatus.COMPLETED completed = (DownloadStatus.COMPLETED) downloadStatus;
                    sb2.append(completed.getSuccess().size());
                    sb2.append(", fails: ");
                    sb2.append(completed.getFails().size());
                    DefaultLogger.d("StoryAlbumFragment", sb2.toString());
                    downloadDialog = StoryAlbumFragment.this.downloadDialog;
                    if (downloadDialog != null) {
                        downloadDialog.dismissSafely();
                        StoryAlbumFragment.this.downloadDialog = null;
                    }
                    List<Downloader.DownloadResult> fails = completed.getFails();
                    if (!(fails == null || fails.isEmpty())) {
                        return;
                    }
                    StoryAlbumFragment.this.doCreation();
                }
            }

            @Override // androidx.lifecycle.Observer
            public /* bridge */ /* synthetic */ void onChanged(Object obj) {
                onChanged((SingleLiveEvent) ((SingleLiveEvent) obj));
            }
        });
    }

    public final void finish(boolean z) {
        SlideShowHeaderView slideShowHeaderView = this.sliderView;
        if (slideShowHeaderView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            slideShowHeaderView = null;
        }
        if (this.launchOrientation == getResources().getConfiguration().orientation) {
            Intent intent = new Intent();
            intent.putExtra("transition_info", BundleKt.bundleOf(TuplesKt.to(nexExportFormat.TAG_FORMAT_WIDTH, Integer.valueOf(slideShowHeaderView.getWidth())), TuplesKt.to(nexExportFormat.TAG_FORMAT_HEIGHT, Integer.valueOf(slideShowHeaderView.getHeight())), TuplesKt.to("x", Float.valueOf(slideShowHeaderView.getX())), TuplesKt.to("y", Float.valueOf(slideShowHeaderView.getY())), TuplesKt.to("current_index", Integer.valueOf(slideShowHeaderView.getCurrentIndex())), TuplesKt.to("info_launcher_orientation", Integer.valueOf(this.launchOrientation))));
            requireActivity().setResult(-1, intent);
        }
        AppCompatActivity appCompatActivity = this.mActivity;
        if (appCompatActivity == null || z) {
            return;
        }
        appCompatActivity.finish();
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public void finish() {
        finish(false);
    }

    public final void initTranView() {
        this.translationView = new RoundedFrameLayout(this.mActivity, (int) getResources().getDimension(R.dimen.card_cover_layout_imageview_radius));
        Bundle bundle = this.transitionInfo;
        Intrinsics.checkNotNull(bundle);
        this.launchOrientation = bundle.getInt("info_launcher_orientation");
        int i = bundle.getInt("current_index", 0);
        int i2 = bundle.getInt(nexExportFormat.TAG_FORMAT_WIDTH);
        int i3 = bundle.getInt(nexExportFormat.TAG_FORMAT_HEIGHT);
        int i4 = bundle.getInt("x");
        int i5 = bundle.getInt("y");
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(i2, i3);
        marginLayoutParams.leftMargin = i4;
        marginLayoutParams.topMargin = i5;
        ViewGroup viewGroup = this.rootView;
        ViewGroup.MarginLayoutParams marginLayoutParams2 = null;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rootView");
            viewGroup = null;
        }
        viewGroup.addView(this.translationView, 0, marginLayoutParams);
        this.sliderView = new SlideShowHeaderView(this.mActivity);
        RoundedFrameLayout roundedFrameLayout = this.translationView;
        Intrinsics.checkNotNull(roundedFrameLayout);
        SlideShowHeaderView slideShowHeaderView = this.sliderView;
        if (slideShowHeaderView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            slideShowHeaderView = null;
        }
        roundedFrameLayout.addView(slideShowHeaderView, new FrameLayout.LayoutParams(-1, -1));
        SlideShowHeaderView slideShowHeaderView2 = this.sliderView;
        if (slideShowHeaderView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            slideShowHeaderView2 = null;
        }
        slideShowHeaderView2.setShowViewTargetHeight(this.sliderHeight);
        SlideShowHeaderView slideShowHeaderView3 = this.sliderView;
        if (slideShowHeaderView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            slideShowHeaderView3 = null;
        }
        slideShowHeaderView3.setPlayVisible(false);
        SlideShowHeaderView slideShowHeaderView4 = this.sliderView;
        if (slideShowHeaderView4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            slideShowHeaderView4 = null;
        }
        slideShowHeaderView4.preUpdateShow(bundle.getString("current_uri"), i);
        this.enterShowTime = System.currentTimeMillis();
        ViewGroup viewGroup2 = this.rootView;
        if (viewGroup2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rootView");
            viewGroup2 = null;
        }
        View findViewById = viewGroup2.findViewById(R.id.pre_content);
        ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginLayoutParams2 = (ViewGroup.MarginLayoutParams) layoutParams;
        }
        if (marginLayoutParams2 == null) {
            marginLayoutParams2 = new ViewGroup.MarginLayoutParams(-1, -1);
        }
        marginLayoutParams2.topMargin = this.exactScreenVertical - this.sliderHeight;
        findViewById.setLayoutParams(marginLayoutParams2);
    }

    public final void startTransition() {
        this.transitionListener = new WeakReferenceTransitionListener(this);
        transitionCard();
        transitionList();
    }

    public final void showPlayIcon() {
        View view;
        if (!this.isFromWidget && (view = this.playIcon) != null) {
            view.setVisibility(0);
            view.setAlpha(0.0f);
            Folme.clean(view);
            Folme.useAt(view).visible().show(new AnimConfig().setEase(EaseManager.getStyle(16, 300.0f)));
        }
    }

    public final void rebuild() {
        SlideShowHeaderView slideShowHeaderView = this.sliderView;
        ViewGroup viewGroup = null;
        if (slideShowHeaderView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            slideShowHeaderView = null;
        }
        slideShowHeaderView.setIsEnter(false);
        RoundedFrameLayout roundedFrameLayout = this.translationView;
        if (roundedFrameLayout != null) {
            SlideShowHeaderView slideShowHeaderView2 = this.sliderView;
            if (slideShowHeaderView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sliderView");
                slideShowHeaderView2 = null;
            }
            roundedFrameLayout.removeView(slideShowHeaderView2);
        }
        ViewGroup viewGroup2 = this.rootView;
        if (viewGroup2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rootView");
            viewGroup2 = null;
        }
        viewGroup2.removeView(this.translationView);
        this.translationView = null;
        ViewGroup viewGroup3 = this.headerView;
        if (viewGroup3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("headerView");
            viewGroup3 = null;
        }
        ViewGroup viewGroup4 = (ViewGroup) viewGroup3.findViewById(R.id.header_layout);
        SlideShowHeaderView slideShowHeaderView3 = this.sliderView;
        if (slideShowHeaderView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            slideShowHeaderView3 = null;
        }
        SlideShowHeaderView slideShowHeaderView4 = this.sliderView;
        if (slideShowHeaderView4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            slideShowHeaderView4 = null;
        }
        viewGroup4.addView(slideShowHeaderView3, slideShowHeaderView4.getLayoutParams());
        SlideShowHeaderView slideShowHeaderView5 = this.sliderView;
        if (slideShowHeaderView5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            slideShowHeaderView5 = null;
        }
        slideShowHeaderView5.setId(R.id.header);
        SlideShowHeaderView slideShowHeaderView6 = this.sliderView;
        if (slideShowHeaderView6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            slideShowHeaderView6 = null;
        }
        slideShowHeaderView6.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StoryAlbumFragment.$r8$lambda$yE3_vM2llvQ0NpRVGjqH5Wzf4b4(StoryAlbumFragment.this, view);
            }
        });
        ViewGroup viewGroup5 = this.rootView;
        if (viewGroup5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rootView");
        } else {
            viewGroup = viewGroup5;
        }
        final GalleryPullZoomLayout galleryPullZoomLayout = (GalleryPullZoomLayout) viewGroup.findViewById(R.id.scrolling_layout);
        galleryPullZoomLayout.post(new Runnable() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                StoryAlbumFragment.$r8$lambda$ltOK8dY7P5ccHyZMdA2JzXWPgm8(StoryAlbumFragment.this, galleryPullZoomLayout);
            }
        });
        galleryPullZoomLayout.setOnScrollListener(new GalleryPullZoomLayout.OnScrollListener() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda13
            @Override // com.miui.gallery.widget.GalleryPullZoomLayout.OnScrollListener
            public final void onScrolled(GalleryPullZoomLayout.ScrollBy scrollBy, float f) {
                StoryAlbumFragment.m592$r8$lambda$1ddSpb8ZJuJlztpovUgCdsyKf4(StoryAlbumFragment.this, scrollBy, f);
            }
        });
        galleryPullZoomLayout.setOriginalHeight(this.sliderHeight);
        this.scrollingLayout = galleryPullZoomLayout;
    }

    /* renamed from: rebuild$lambda-31 */
    public static final void m607rebuild$lambda31(StoryAlbumFragment this$0, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        SlideShowHeaderView slideShowHeaderView = null;
        if (this$0.mActivity != null) {
            SlideShowHeaderView slideShowHeaderView2 = this$0.sliderView;
            if (slideShowHeaderView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sliderView");
                slideShowHeaderView2 = null;
            }
            if (slideShowHeaderView2.isPlayEnable()) {
                SlideShowHeaderView slideShowHeaderView3 = this$0.sliderView;
                if (slideShowHeaderView3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("sliderView");
                } else {
                    slideShowHeaderView = slideShowHeaderView3;
                }
                slideShowHeaderView.setEnabled(false);
                this$0.mo617getViewModel().check4Download();
                return;
            }
        }
        SlideShowHeaderView slideShowHeaderView4 = this$0.sliderView;
        if (slideShowHeaderView4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
        } else {
            slideShowHeaderView = slideShowHeaderView4;
        }
        DefaultLogger.d("StoryAlbumFragment", "initHeaderView SlideShowHeaderView is play enable %s", Boolean.valueOf(slideShowHeaderView.isPlayEnable()));
    }

    /* renamed from: rebuild$lambda-34$lambda-32 */
    public static final void m608rebuild$lambda34$lambda32(StoryAlbumFragment this$0, GalleryPullZoomLayout galleryPullZoomLayout) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        AppCompatActivity appCompatActivity = this$0.mActivity;
        if (appCompatActivity == null || appCompatActivity.isFinishing()) {
            return;
        }
        ActionBar appCompatActionBar = this$0.mActivity.getAppCompatActionBar();
        Intrinsics.checkNotNull(appCompatActionBar);
        galleryPullZoomLayout.setActionBarHeight(appCompatActionBar.getHeight());
    }

    /* renamed from: rebuild$lambda-34$lambda-33 */
    public static final void m609rebuild$lambda34$lambda33(StoryAlbumFragment this$0, GalleryPullZoomLayout.ScrollBy scrollBy, float f) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullExpressionValue(scrollBy, "scrollBy");
        this$0.updateSlideHeader(scrollBy, f);
    }

    /* compiled from: StoryAlbumFragment.kt */
    /* loaded from: classes.dex */
    public static final class WeakReferenceTransitionListener extends TransitionListener {
        public final WeakReference<StoryAlbumFragment> fragmentRef;
        public int transitionCount = 2;

        public WeakReferenceTransitionListener(StoryAlbumFragment storyAlbumFragment) {
            this.fragmentRef = new WeakReference<>(storyAlbumFragment);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onUpdate(Object toTag, Collection<? extends UpdateInfo> updateList) {
            UpdateInfo findByName;
            Intrinsics.checkNotNullParameter(toTag, "toTag");
            Intrinsics.checkNotNullParameter(updateList, "updateList");
            StoryAlbumFragment storyAlbumFragment = this.fragmentRef.get();
            if (storyAlbumFragment == null || (findByName = UpdateInfo.findByName(updateList, "topMargin")) == null) {
                return;
            }
            View view = storyAlbumFragment.contentLayout;
            ViewGroup.MarginLayoutParams marginLayoutParams = storyAlbumFragment.contentLayoutParams;
            if (view == null || marginLayoutParams == null) {
                return;
            }
            marginLayoutParams.topMargin = findByName.getIntValue();
            view.setLayoutParams(marginLayoutParams);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            StoryAlbumFragment storyAlbumFragment = this.fragmentRef.get();
            if (storyAlbumFragment == null) {
                return;
            }
            int i = this.transitionCount - 1;
            this.transitionCount = i;
            if (i > 0) {
                return;
            }
            storyAlbumFragment.rebuild();
            Runnable runnable = storyAlbumFragment.playIconUpdater;
            if (runnable == null) {
                return;
            }
            SlideShowHeaderView slideShowHeaderView = storyAlbumFragment.sliderView;
            if (slideShowHeaderView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sliderView");
                slideShowHeaderView = null;
            }
            slideShowHeaderView.post(runnable);
            storyAlbumFragment.playIconUpdater = null;
        }
    }

    public final void transitionCard() {
        RoundedFrameLayout roundedFrameLayout = this.translationView;
        Intrinsics.checkNotNull(roundedFrameLayout);
        Folme.clean(roundedFrameLayout);
        roundedFrameLayout.setEnabled(false);
        AnimConfig ease = new AnimConfig().setEase(EaseManager.getStyle(-2, 0.75f, 0.35f));
        TransitionListener[] transitionListenerArr = new TransitionListener[1];
        WeakReferenceTransitionListener weakReferenceTransitionListener = this.transitionListener;
        if (weakReferenceTransitionListener == null) {
            Intrinsics.throwUninitializedPropertyAccessException("transitionListener");
            weakReferenceTransitionListener = null;
        }
        transitionListenerArr[0] = weakReferenceTransitionListener;
        AnimConfig addListeners = ease.addListeners(transitionListenerArr);
        AnimState animState = new AnimState("transitCardFrom");
        ViewProperty viewProperty = ViewProperty.WIDTH;
        AnimState add = animState.add(viewProperty, roundedFrameLayout.getWidth());
        ViewProperty viewProperty2 = ViewProperty.HEIGHT;
        AnimState add2 = add.add(viewProperty2, roundedFrameLayout.getHeight());
        ViewProperty viewProperty3 = ViewProperty.X;
        AnimState add3 = add2.add(viewProperty3, roundedFrameLayout.getX());
        ViewProperty viewProperty4 = ViewProperty.Y;
        Folme.useAt(roundedFrameLayout).state().fromTo(add3.add(viewProperty4, roundedFrameLayout.getY()), new AnimState("transitCardTo").add(viewProperty, this.screenHorizontal).add(viewProperty2, this.sliderHeight).add(viewProperty3, SearchStatUtils.POW).add(viewProperty4, SearchStatUtils.POW), addListeners);
    }

    public final void transitionList() {
        ViewGroup viewGroup = this.rootView;
        WeakReferenceTransitionListener weakReferenceTransitionListener = null;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rootView");
            viewGroup = null;
        }
        View findViewById = viewGroup.findViewById(R.id.pre_content);
        this.contentLayout = findViewById;
        Intrinsics.checkNotNull(findViewById);
        ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLayoutParams = layoutParams instanceof ViewGroup.MarginLayoutParams ? (ViewGroup.MarginLayoutParams) layoutParams : null;
        if (marginLayoutParams == null) {
            marginLayoutParams = new ViewGroup.MarginLayoutParams(-1, -1);
        }
        this.contentLayoutParams = marginLayoutParams;
        AnimConfig ease = new AnimConfig().setDelay(50L).setEase(EaseManager.getStyle(-2, 0.75f, 0.35f));
        IStateStyle useValue = Folme.useValue(new Object[0]);
        WeakReferenceTransitionListener weakReferenceTransitionListener2 = this.transitionListener;
        if (weakReferenceTransitionListener2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("transitionListener");
        } else {
            weakReferenceTransitionListener = weakReferenceTransitionListener2;
        }
        IStateStyle addListener = useValue.addListener(weakReferenceTransitionListener);
        ViewGroup.MarginLayoutParams marginLayoutParams2 = this.contentLayoutParams;
        Intrinsics.checkNotNull(marginLayoutParams2);
        addListener.setTo("topMargin", Integer.valueOf(marginLayoutParams2.topMargin)).to("topMargin", 0, ease);
    }

    public final void updateSlideHeader(GalleryPullZoomLayout.ScrollBy scrollBy, float f) {
        SimpleThemeActionBarHelper simpleThemeActionBarHelper = null;
        if (this.translationDy == 0) {
            ViewGroup viewGroup = this.headerView;
            if (viewGroup == null) {
                Intrinsics.throwUninitializedPropertyAccessException("headerView");
                viewGroup = null;
            }
            int height = viewGroup.findViewById(R.id.header_content).getHeight();
            TextView textView = this.titleView;
            if (textView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("titleView");
                textView = null;
            }
            int bottom = height - textView.getBottom();
            SimpleThemeActionBarHelper simpleThemeActionBarHelper2 = this.actionBarHelper;
            if (simpleThemeActionBarHelper2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("actionBarHelper");
                simpleThemeActionBarHelper2 = null;
            }
            int actionBarHeight = simpleThemeActionBarHelper2.getActionBarHeight();
            SimpleThemeActionBarHelper simpleThemeActionBarHelper3 = this.actionBarHelper;
            if (simpleThemeActionBarHelper3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("actionBarHelper");
                simpleThemeActionBarHelper3 = null;
            }
            this.translationDy = bottom - ((actionBarHeight - simpleThemeActionBarHelper3.getTitleHeight()) / 2);
        }
        TextView textView2 = this.titleView;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("titleView");
            textView2 = null;
        }
        textView2.setTranslationY(this.translationDy * f);
        TextView textView3 = this.descriptionView;
        if (textView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("descriptionView");
            textView3 = null;
        }
        textView3.setTranslationY(this.translationDy * f);
        float f2 = 0.0f;
        float coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(1 - (getResources().getInteger(R.integer.story_accelerated_factor) * f), 0.0f);
        TextView textView4 = this.descriptionView;
        if (textView4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("descriptionView");
            textView4 = null;
        }
        textView4.setAlpha(coerceAtLeast);
        SlideShowHeaderView slideShowHeaderView = this.sliderView;
        if (slideShowHeaderView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            slideShowHeaderView = null;
        }
        slideShowHeaderView.setPlayIconAlpha(coerceAtLeast);
        this.sliderProgress = f;
        if (!this.isInActionMode) {
            SimpleThemeActionBarHelper simpleThemeActionBarHelper4 = this.actionBarHelper;
            if (simpleThemeActionBarHelper4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("actionBarHelper");
                simpleThemeActionBarHelper4 = null;
            }
            simpleThemeActionBarHelper4.refreshIconAndMenu(f >= ((float) getResources().getInteger(R.integer.story_action_bar_anim)) * 0.01f ? 1.0f : 0.0f);
            SimpleThemeActionBarHelper simpleThemeActionBarHelper5 = this.actionBarHelper;
            if (simpleThemeActionBarHelper5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("actionBarHelper");
            } else {
                simpleThemeActionBarHelper = simpleThemeActionBarHelper5;
            }
            if (f >= 1.0f) {
                f2 = 1.0f;
            }
            simpleThemeActionBarHelper.refreshTextAndBg(f2);
        }
    }

    public final void doCreation() {
        String str;
        SlideShowHeaderView slideShowHeaderView = this.sliderView;
        if (slideShowHeaderView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            slideShowHeaderView = null;
        }
        if (!slideShowHeaderView.isShowVideo()) {
            StoryAlbumViewModel mo617getViewModel = mo617getViewModel();
            AppCompatActivity mActivity = this.mActivity;
            Intrinsics.checkNotNullExpressionValue(mActivity, "mActivity");
            mo617getViewModel.navigateToPhotoMovie(mActivity);
            str = "movie";
        } else {
            StoryAlbumViewModel mo617getViewModel2 = mo617getViewModel();
            AppCompatActivity mActivity2 = this.mActivity;
            Intrinsics.checkNotNullExpressionValue(mActivity2, "mActivity");
            mo617getViewModel2.navigateToVLog(mActivity2);
            str = "vlog";
        }
        TrackController.trackClick("403.38.0.1.11201", AutoTracking.getRef(), str);
    }

    public final void startDownload() {
        mo617getViewModel().startDownload();
    }

    public final void showConfirmDialog(boolean z) {
        DownloadCommand peekContent;
        SingleLiveEvent<DownloadCommand> value = mo617getViewModel().getDownloadCommands().getValue();
        List<Downloader.DownloadTask> list = null;
        if (value != null && (peekContent = value.peekContent()) != null) {
            list = peekContent.getTasks();
        }
        int i = 0;
        if (list == null || list.isEmpty()) {
            DefaultLogger.w("StoryAlbumFragment", "Nothing to download");
        } else if (!z && NetworkConsider.sAgreedUsingMeteredNetwork && BaseNetworkUtils.isNetworkConnected()) {
            this.onConfirmedListener.onConfirmed(true, BaseNetworkUtils.isActiveNetworkMetered());
        } else {
            DownloadConfirmDialog downloadConfirmDialog = new DownloadConfirmDialog();
            Pair[] pairArr = new Pair[4];
            pairArr[0] = TuplesKt.to("download_file_count", Integer.valueOf(list.size()));
            List<MediaInfo> value2 = mo617getViewModel().getMedias().getValue();
            pairArr[1] = TuplesKt.to("local_file_count", Integer.valueOf(value2 == null ? 0 : value2.size()));
            for (Downloader.DownloadTask downloadTask : list) {
                i += downloadTask.mSize;
            }
            pairArr[2] = TuplesKt.to("download_file_size", Integer.valueOf(i));
            pairArr[3] = TuplesKt.to("retry_mode", Boolean.valueOf(z));
            downloadConfirmDialog.setArguments(BundleKt.bundleOf(pairArr));
            downloadConfirmDialog.showAllowingStateLoss(getParentFragmentManager(), "confirm_dialog");
        }
    }

    public final void setTitle(String str) {
        SimpleThemeActionBarHelper simpleThemeActionBarHelper = this.actionBarHelper;
        TextView textView = null;
        if (simpleThemeActionBarHelper == null) {
            Intrinsics.throwUninitializedPropertyAccessException("actionBarHelper");
            simpleThemeActionBarHelper = null;
        }
        simpleThemeActionBarHelper.setActionBarTitle(str);
        TextView textView2 = this.titleView;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("titleView");
        } else {
            textView = textView2;
        }
        textView.setText(str);
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        String str = this.isFromWidget ? "widget" : "recommend";
        Pair[] pairArr = new Pair[4];
        pairArr[0] = TuplesKt.to("tip", "403.38.0.1.11150");
        pairArr[1] = TuplesKt.to("ref_tip", AutoTracking.getRef());
        Card value = mo617getViewModel().getCard().getValue();
        pairArr[2] = TuplesKt.to(nexExportFormat.TAG_FORMAT_TYPE, value == null ? null : Integer.valueOf(value.getScenarioId()));
        pairArr[3] = TuplesKt.to("value", str);
        AutoTracking.trackView(MapsKt__MapsKt.mapOf(pairArr));
        this.reviewDuration = System.currentTimeMillis();
        mo617getViewModel().resumeDownload();
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        int i;
        String str;
        super.onPause();
        MediaStats value = mo617getViewModel().getMediaStats().getValue();
        String str2 = "";
        if (value != null) {
            i = value.getImageCount() + value.getVideoCount();
            if (value.getImageCount() > 0 && value.getVideoCount() == 0) {
                str = "images";
            } else if (value.getImageCount() > 0 && value.getVideoCount() > 0) {
                str = "mix";
            } else if (value.getImageCount() == 0 && value.getVideoCount() > 0) {
                str = "videos";
            }
            str2 = str;
        } else {
            i = 0;
        }
        TrackController.trackStats(MapsKt__MapsKt.mapOf(TuplesKt.to("tip", "403.38.0.1.18798"), TuplesKt.to("ref_tip", AutoTracking.getRef()), TuplesKt.to(nexExportFormat.TAG_FORMAT_TYPE, str2), TuplesKt.to(MiStat.Param.COUNT, Integer.valueOf(i)), TuplesKt.to("value", Long.valueOf((System.currentTimeMillis() - this.reviewDuration) / 1000))));
        mo617getViewModel().pauseDownload();
    }

    public final boolean isEnterShowFinished() {
        return System.currentTimeMillis() - this.enterShowTime >= 3500;
    }

    public final void updateFlexBoxLayoutSize(List<MediaInfo> list) {
        int width;
        int height;
        GalleryRecyclerView galleryRecyclerView = this.recyclerView;
        GalleryRecyclerView galleryRecyclerView2 = null;
        if (galleryRecyclerView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
            galleryRecyclerView = null;
        }
        this.flexboxHeight = galleryRecyclerView.getWidth();
        ArrayList arrayList = new ArrayList(list.size());
        for (MediaInfo mediaInfo : list) {
            if (ExifUtil.isWidthHeightRotated(mediaInfo.getOrientation())) {
                width = mediaInfo.getHeight();
                height = mediaInfo.getWidth();
            } else {
                width = mediaInfo.getWidth();
                height = mediaInfo.getHeight();
            }
            arrayList.add(new LayoutParamsHelper.Size(width, height));
        }
        StoryAlbumAdapter storyAlbumAdapter = this.adapter;
        if (storyAlbumAdapter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("adapter");
            storyAlbumAdapter = null;
        }
        int i = this.flexboxHeight;
        GalleryRecyclerView galleryRecyclerView3 = this.recyclerView;
        if (galleryRecyclerView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
            galleryRecyclerView3 = null;
        }
        storyAlbumAdapter.updateLayoutSizes(arrayList, i, galleryRecyclerView3.getResources().getDimensionPixelSize(R.dimen.micro_thumb_horizontal_spacing));
        if (this.flexboxHeight == 0) {
            GalleryRecyclerView galleryRecyclerView4 = this.recyclerView;
            if (galleryRecyclerView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
            } else {
                galleryRecyclerView2 = galleryRecyclerView4;
            }
            galleryRecyclerView2.getViewTreeObserver().addOnGlobalLayoutListener(this.globalLayoutListener);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:60:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0078  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void updateSliderPlayStatus(com.miui.gallery.biz.story.data.MediaStats r8) {
        /*
            r7 = this;
            int r0 = r8.getVideoCount()
            boolean r1 = r8.isShowVideo()
            r7.updateVideoLogo(r0, r1)
            int r0 = r8.getImageCount()
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            boolean r1 = r7.isInActionMode
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)
            boolean r2 = r7.isPlayIconScrollStateEnable
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)
            java.lang.String r3 = "StoryAlbumFragment"
            java.lang.String r4 = "updateSlideHeaderPlayStatus image count %d, mIsInActionMode %s, mIsPlayIconScrollStateEnable %s"
            com.miui.gallery.util.logger.DefaultLogger.d(r3, r4, r0, r1, r2)
            int r8 = r8.getImageCount()
            r0 = 3
            r1 = 0
            r2 = 1
            r3 = 0
            java.lang.String r4 = "sliderView"
            if (r8 >= r0) goto L43
            com.miui.gallery.card.ui.detail.SlideShowHeaderView r5 = r7.sliderView
            if (r5 != 0) goto L3a
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r4)
            r5 = r3
        L3a:
            boolean r5 = r5.isShowVideo()
            if (r5 == 0) goto L41
            goto L43
        L41:
            r5 = r1
            goto L44
        L43:
            r5 = r2
        L44:
            android.view.View r6 = r7.playIcon
            if (r6 == 0) goto L4d
            if (r5 == 0) goto L4d
            r7.showPlayIcon()
        L4d:
            boolean r5 = r7.isFromWidget
            if (r5 == 0) goto L85
            com.miui.gallery.card.ui.detail.SlideShowHeaderView r5 = r7.sliderView
            if (r5 != 0) goto L59
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r4)
            r5 = r3
        L59:
            if (r8 >= r0) goto L6c
            com.miui.gallery.card.ui.detail.SlideShowHeaderView r0 = r7.sliderView
            if (r0 != 0) goto L63
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r4)
            r0 = r3
        L63:
            boolean r0 = r0.isShowVideo()
            if (r0 == 0) goto L6a
            goto L6c
        L6a:
            r0 = r1
            goto L6d
        L6c:
            r0 = r2
        L6d:
            r5.setPlayVisible(r0)
            com.miui.gallery.card.ui.detail.SlideShowHeaderView r0 = r7.sliderView
            if (r0 != 0) goto L78
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r4)
            goto L79
        L78:
            r3 = r0
        L79:
            boolean r0 = r7.isInActionMode
            if (r0 != 0) goto L82
            boolean r0 = r7.isPlayIconScrollStateEnable
            if (r0 == 0) goto L82
            r1 = r2
        L82:
            r3.setPlayEnable(r1)
        L85:
            com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda16 r0 = new com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda16
            r0.<init>()
            r7.playIconUpdater = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.biz.story.StoryAlbumFragment.updateSliderPlayStatus(com.miui.gallery.biz.story.data.MediaStats):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x0038  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x003c  */
    /* renamed from: updateSliderPlayStatus$lambda-37 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static final void m616updateSliderPlayStatus$lambda37(com.miui.gallery.biz.story.StoryAlbumFragment r6, int r7) {
        /*
            java.lang.String r0 = "this$0"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
            android.view.View r0 = r6.playIcon
            if (r0 != 0) goto La
            goto Lf
        La:
            r1 = 8
            r0.setVisibility(r1)
        Lf:
            com.miui.gallery.card.ui.detail.SlideShowHeaderView r0 = r6.sliderView
            r1 = 0
            java.lang.String r2 = "sliderView"
            if (r0 != 0) goto L1a
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r2)
            r0 = r1
        L1a:
            r3 = 3
            r4 = 0
            r5 = 1
            if (r7 >= r3) goto L30
            com.miui.gallery.card.ui.detail.SlideShowHeaderView r7 = r6.sliderView
            if (r7 != 0) goto L27
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r2)
            r7 = r1
        L27:
            boolean r7 = r7.isShowVideo()
            if (r7 == 0) goto L2e
            goto L30
        L2e:
            r7 = r4
            goto L31
        L30:
            r7 = r5
        L31:
            r0.setPlayVisible(r7)
            com.miui.gallery.card.ui.detail.SlideShowHeaderView r7 = r6.sliderView
            if (r7 != 0) goto L3c
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r2)
            goto L3d
        L3c:
            r1 = r7
        L3d:
            boolean r7 = r6.isInActionMode
            if (r7 != 0) goto L46
            boolean r6 = r6.isPlayIconScrollStateEnable
            if (r6 == 0) goto L46
            r4 = r5
        L46:
            r1.setPlayEnable(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.biz.story.StoryAlbumFragment.m616updateSliderPlayStatus$lambda37(com.miui.gallery.biz.story.StoryAlbumFragment, int):void");
    }

    public final void updateVideoLogo(int i, boolean z) {
        DefaultLogger.d("StoryAlbumFragment", "updateVideoLogo video count %d", Integer.valueOf(i));
        SlideShowHeaderView slideShowHeaderView = null;
        if (i < 4 || !z) {
            DefaultLogger.d("StoryAlbumFragment", "updateVideoLogo video setIsShowVideo false");
            SlideShowHeaderView slideShowHeaderView2 = this.sliderView;
            if (slideShowHeaderView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            } else {
                slideShowHeaderView = slideShowHeaderView2;
            }
            slideShowHeaderView.setIsShowVideo(false);
            return;
        }
        DefaultLogger.d("StoryAlbumFragment", "updateVideoLogo video setIsShowVideo true");
        SlideShowHeaderView slideShowHeaderView3 = this.sliderView;
        if (slideShowHeaderView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
            slideShowHeaderView3 = null;
        }
        slideShowHeaderView3.setVideoLogo(R.drawable.story_card_vlog_logo);
        SlideShowHeaderView slideShowHeaderView4 = this.sliderView;
        if (slideShowHeaderView4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderView");
        } else {
            slideShowHeaderView = slideShowHeaderView4;
        }
        slideShowHeaderView.setIsShowVideo(true);
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment
    public List<Loader<?>> getLoaders() {
        return CollectionsKt__CollectionsKt.mutableListOf(LoaderManager.getInstance(this).getLoader(1));
    }

    public final void showRenameDialog() {
        Card value = mo617getViewModel().getCard().getValue();
        if (value == null) {
            return;
        }
        StoryAlbumRenameDialog storyAlbumRenameDialog = new StoryAlbumRenameDialog();
        storyAlbumRenameDialog.setArguments(BundleKt.bundleOf(TuplesKt.to("card_name", value.getTitle())));
        storyAlbumRenameDialog.setOnRenameDoneListener(new StoryAlbumRenameDialog.OnRenameDoneListener() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda10
            @Override // com.miui.gallery.card.ui.detail.StoryAlbumRenameDialog.OnRenameDoneListener
            public final void onOperationDone(String str) {
                StoryAlbumFragment.m611showRenameDialog$lambda40$lambda39$lambda38(StoryAlbumFragment.this, str);
            }
        });
        storyAlbumRenameDialog.showAllowingStateLoss(getParentFragmentManager(), "StoryAlbumRenameDialog");
    }

    /* renamed from: showRenameDialog$lambda-40$lambda-39$lambda-38 */
    public static final void m611showRenameDialog$lambda40$lambda39$lambda38(StoryAlbumFragment this$0, String newName) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (this$0.mActivity != null) {
            Intrinsics.checkNotNullExpressionValue(newName, "newName");
            this$0.setTitle(newName);
        }
        StoryAlbumViewModel mo617getViewModel = this$0.mo617getViewModel();
        Intrinsics.checkNotNullExpressionValue(newName, "newName");
        mo617getViewModel.renameCard(newName);
    }

    public final void showDeleteConfirmDialog() {
        ConfirmDialog.showConfirmDialog(requireActivity().getSupportFragmentManager(), this.mActivity.getString(R.string.card_delete_title), this.mActivity.getString(R.string.card_delete_description), this.mActivity.getString(R.string.card_delete_negative_text), this.mActivity.getString(R.string.card_delete_positive_text), new ConfirmDialog.ConfirmDialogInterface() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$showDeleteConfirmDialog$1
            @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
            public void onCancel(DialogFragment dialog) {
                Intrinsics.checkNotNullParameter(dialog, "dialog");
            }

            @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
            public void onConfirm(DialogFragment dialog) {
                Intrinsics.checkNotNullParameter(dialog, "dialog");
                StoryAlbumFragment.this.mo617getViewModel().deleteCard();
                StoryAlbumFragment.this.finish();
            }
        });
    }

    public final void onAttachDialogFragment(Fragment fragment) {
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        if (fragment instanceof DownloadConfirmDialog) {
            DownloadConfirmDialog downloadConfirmDialog = (DownloadConfirmDialog) fragment;
            downloadConfirmDialog.setPositiveListener(this.confirmDownloadListener);
            downloadConfirmDialog.setNegativeListener(this.cancelDownloadListener);
        } else if (!(fragment instanceof DownloadDialog)) {
        } else {
            ((DownloadDialog) fragment).setCallBack(this.downloadCallback);
        }
    }

    public final void showNetworkMeteredDialog() {
        NetworkConsider.consider(this.mActivity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$$ExternalSyntheticLambda12
            @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
            public final void onConfirmed(boolean z, boolean z2) {
                StoryAlbumFragment.m610showNetworkMeteredDialog$lambda41(StoryAlbumFragment.this, z, z2);
            }
        });
    }

    /* renamed from: showNetworkMeteredDialog$lambda-41 */
    public static final void m610showNetworkMeteredDialog$lambda41(StoryAlbumFragment this$0, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (z) {
            this$0.startDownload();
        } else {
            this$0.finish();
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        GalleryPullZoomLayout galleryPullZoomLayout = this.scrollingLayout;
        if (galleryPullZoomLayout == null) {
            return;
        }
        galleryPullZoomLayout.setOnScrollListener(null);
    }

    /* compiled from: StoryAlbumFragment.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int i) {
        Intrinsics.checkNotNullParameter(data, "data");
        Intrinsics.checkNotNullParameter(menu, "menu");
        ArrayList arrayList = new ArrayList();
        if (this.isInActionMode) {
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getSelectAllShortcutInfo());
            arrayList.addAll(KeyboardShortcutGroupManager.getInstance().getDeleteShortcutInfo());
        }
        data.add(new KeyboardShortcutGroup(getPageName(), arrayList));
    }

    public final boolean onKeyShortcut(int i, KeyEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        return KeyboardShortcutGroupManager.getInstance().onKeyShortcut(i, event, this.shortcutCallback);
    }

    /* compiled from: StoryAlbumFragment.kt */
    /* loaded from: classes.dex */
    public final class StoryPageKeyboardShortcutCallback implements KeyboardShortcutGroupManager.OnKeyShortcutCallback {
        public final /* synthetic */ StoryAlbumFragment this$0;

        public StoryPageKeyboardShortcutCallback(StoryAlbumFragment this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onSelectAllPressed() {
            if (this.this$0.isInActionMode) {
                EditableListViewWrapper editableListViewWrapper = this.this$0.editableListWrapper;
                if (editableListViewWrapper == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("editableListWrapper");
                    editableListViewWrapper = null;
                }
                editableListViewWrapper.setAllItemsCheckState(true);
            }
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onDeletePressed() {
            if (this.this$0.isInActionMode) {
                this.this$0.showDeleteConfirmDialog();
                return true;
            }
            return true;
        }
    }
}
