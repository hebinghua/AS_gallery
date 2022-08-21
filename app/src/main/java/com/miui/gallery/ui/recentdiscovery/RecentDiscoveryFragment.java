package com.miui.gallery.ui.recentdiscovery;

import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentViewModelLazyKt;
import androidx.lifecycle.LifecycleOwnerKt;
import androidx.lifecycle.Observer;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GalleryGridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.activity.InternalPhotoPageActivity;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.adapter.RecentDiscoveryAdapter2;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.assistant.cache.MediaFeatureCacheManager;
import com.miui.gallery.model.AlbumConstants;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.album.AlbumManager;
import com.miui.gallery.provider.cache.CacheLiveData;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.provider.cache.MediaCacheItem;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AlbumDetailGridItem;
import com.miui.gallery.ui.BaseMediaFragment;
import com.miui.gallery.ui.DeletionTask;
import com.miui.gallery.ui.KeyboardShortcutGroupManager;
import com.miui.gallery.ui.PhotoListViewModel;
import com.miui.gallery.ui.recentdiscovery.RecentDiscoveryFragment;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.PhotoPageIntent;
import com.miui.gallery.util.SoundUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.widget.EmptyPage;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.editwrapper.MultiChoiceModeListener;
import com.miui.gallery.widget.recyclerview.EditableListSpanSizeProvider;
import com.miui.gallery.widget.recyclerview.FastScrollerBar;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import miuix.appcompat.app.AppCompatActivity;

/* compiled from: RecentDiscoveryFragment.kt */
/* loaded from: classes2.dex */
public final class RecentDiscoveryFragment extends BaseMediaFragment {
    public static final Companion Companion = new Companion(null);
    public long mAlbumId;
    public String mAlbumName;
    public EditableListViewWrapper mEditableWrapper;
    public View mEmptyView;
    public LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks;
    public RecentDiscoveryAdapter2 mRecentDiscoveryAdapter;
    public GalleryRecyclerView mRecyclerView;
    public final Lazy viewModel$delegate = FragmentViewModelLazyKt.createViewModelLazy(this, Reflection.getOrCreateKotlinClass(PhotoListViewModel.class), new RecentDiscoveryFragment$special$$inlined$viewModels$default$2(new RecentDiscoveryFragment$special$$inlined$viewModels$default$1(this)), null);
    public final Lazy liveData$delegate = LazyKt__LazyJVMKt.lazy(LazyThreadSafetyMode.NONE, new RecentDiscoveryFragment$liveData$2(this));
    public long mOldestDateModified = Long.MAX_VALUE;
    public RecentMultiChoiceModeListener choiceModeListener = new RecentMultiChoiceModeListener(this);
    public RecentKeyboardShortcutCallback shortcutCallback = new RecentKeyboardShortcutCallback(this);

    /* renamed from: $r8$lambda$RAVkvmp49OpIm-KXbbESRiBysXU */
    public static /* synthetic */ void m1650$r8$lambda$RAVkvmp49OpImKXbbESRiBysXU(RecentDiscoveryFragment recentDiscoveryFragment, List list) {
        m1654onActivityCreated$lambda3(recentDiscoveryFragment, list);
    }

    /* renamed from: $r8$lambda$yA-aCtubYf8Ks_E3DSrn2Gb1vPE */
    public static /* synthetic */ boolean m1651$r8$lambda$yAaCtubYf8Ks_E3DSrn2Gb1vPE(RecentDiscoveryFragment recentDiscoveryFragment, RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        return m1653_get_gridViewOnItemClickListener_$lambda4(recentDiscoveryFragment, recyclerView, view, i, j, f, f2);
    }

    /* renamed from: $r8$lambda$zyy-0D2edcACoY-irMRzo5mx9NQ */
    public static /* synthetic */ void m1652$r8$lambda$zyy0D2edcACoYirMRzo5mx9NQ(RecentDiscoveryFragment recentDiscoveryFragment, int i) {
        m1655onInflateView$lambda1$lambda0(recentDiscoveryFragment, i);
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "album_recent";
    }

    public final String getSelection() {
        return null;
    }

    public final String[] getSelectionArgs() {
        return null;
    }

    public final String getSortOrder() {
        return "dateModified DESC";
    }

    public final int getSupportOperationMask() {
        return -1;
    }

    public static final /* synthetic */ AppCompatActivity access$getMActivity$p$s1706430181(RecentDiscoveryFragment recentDiscoveryFragment) {
        return recentDiscoveryFragment.mActivity;
    }

    public final PhotoListViewModel getViewModel() {
        return (PhotoListViewModel) this.viewModel$delegate.mo119getValue();
    }

    public final CacheLiveData<MediaCacheItem, IRecord> getLiveData() {
        return (CacheLiveData) this.liveData$delegate.mo119getValue();
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        recordPageEnterSource();
    }

    public final void recordPageEnterSource() {
        Uri data = this.mActivity.getIntent().getData();
        if (data != null) {
            HashMap hashMap = new HashMap();
            String queryParameter = data.getQueryParameter("source");
            if (queryParameter == null) {
                queryParameter = "unknown";
            }
            hashMap.put("from", queryParameter);
            SamplingStatHelper.recordCountEvent("recent_album", "recent_album_enter_source", hashMap);
        }
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        Intrinsics.checkNotNullParameter(inflater, "inflater");
        Intrinsics.checkNotNullParameter(container, "container");
        View view = inflater.inflate(R.layout.album_recent, container, false);
        this.mAlbumName = getResources().getString(R.string.album_name_recent_discovery);
        this.mAlbumId = 2147483644L;
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) view.findViewById(R.id.grid);
        if (galleryRecyclerView == null) {
            galleryRecyclerView = null;
        } else {
            galleryRecyclerView.addItemDecoration(new GridItemSpacingDecoration(galleryRecyclerView, false, galleryRecyclerView.getResources().getDimensionPixelSize(R.dimen.micro_thumb_horizontal_spacing), galleryRecyclerView.getResources().getDimensionPixelSize(R.dimen.micro_thumb_vertical_spacing)));
            galleryRecyclerView.setFastScrollEnabled(true);
            galleryRecyclerView.setOnFastScrollerStateChangedListener(new FastScrollerBar.OnStateChangedListener() { // from class: com.miui.gallery.ui.recentdiscovery.RecentDiscoveryFragment$$ExternalSyntheticLambda1
                @Override // com.miui.gallery.widget.recyclerview.FastScrollerBar.OnStateChangedListener
                public final void onStateChanged(int i) {
                    RecentDiscoveryFragment.m1652$r8$lambda$zyy0D2edcACoYirMRzo5mx9NQ(RecentDiscoveryFragment.this, i);
                }
            });
            galleryRecyclerView.setFastScrollerTopMargin(this.mFastScrollerMarginTop);
            galleryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
        this.mRecyclerView = galleryRecyclerView;
        this.mEmptyView = view.findViewById(16908292);
        EditableListViewWrapper editableListViewWrapper = new EditableListViewWrapper(this.mRecyclerView);
        editableListViewWrapper.setEmptyView(this.mEmptyView);
        editableListViewWrapper.setAdapter(getAdapter());
        editableListViewWrapper.setOnItemClickListener(getGridViewOnItemClickListener());
        editableListViewWrapper.enableChoiceMode(true);
        editableListViewWrapper.enterChoiceModeWithLongClick(true);
        editableListViewWrapper.setMultiChoiceModeListener(this.choiceModeListener);
        editableListViewWrapper.setHandleTouchAnimItemType(AlbumDetailGridItem.class.getSimpleName());
        GalleryGridLayoutManager galleryGridLayoutManager = new GalleryGridLayoutManager(this.mActivity, Config$ThumbConfig.get().sMicroThumbColumnsPortrait);
        galleryGridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new EditableListSpanSizeProvider(editableListViewWrapper, galleryGridLayoutManager)));
        editableListViewWrapper.setLayoutManager(galleryGridLayoutManager);
        this.mEditableWrapper = editableListViewWrapper;
        setEmptyViewVisibility(8);
        Configuration configuration = getResources().getConfiguration();
        Intrinsics.checkNotNullExpressionValue(configuration, "resources.configuration");
        updateConfiguration(configuration);
        Intrinsics.checkNotNullExpressionValue(view, "view");
        return view;
    }

    /* renamed from: onInflateView$lambda-1$lambda-0 */
    public static final void m1655onInflateView$lambda1$lambda0(RecentDiscoveryFragment this$0, int i) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        EditableListViewWrapper editableListViewWrapper = this$0.mEditableWrapper;
        if (editableListViewWrapper == null) {
            return;
        }
        editableListViewWrapper.reductionTouchView();
    }

    public final void hideScrollerBar() {
        GalleryRecyclerView galleryRecyclerView = this.mRecyclerView;
        if (galleryRecyclerView == null) {
            return;
        }
        galleryRecyclerView.hideScrollerBar();
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        View view = this.mEmptyView;
        if (view != null && (view instanceof EmptyPage)) {
            Objects.requireNonNull(view, "null cannot be cast to non-null type com.miui.gallery.widget.EmptyPage");
            ((EmptyPage) view).resumeMaml();
        }
        AutoTracking.trackView("403.44.0.1.11210", AutoTracking.getRef(), "recent");
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        View view = this.mEmptyView;
        if (view == null || !(view instanceof EmptyPage)) {
            return;
        }
        Objects.requireNonNull(view, "null cannot be cast to non-null type com.miui.gallery.widget.EmptyPage");
        ((EmptyPage) view).pauseMaml();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        View view = this.mEmptyView;
        if (view == null || !(view instanceof EmptyPage)) {
            return;
        }
        Objects.requireNonNull(view, "null cannot be cast to non-null type com.miui.gallery.widget.EmptyPage");
        ((EmptyPage) view).destroyMaml();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        recordOldestOperatedPicture();
        super.onDestroy();
    }

    public final void recordOldestOperatedPicture() {
        if (this.mOldestDateModified < Long.MAX_VALUE) {
            HashMap hashMap = new HashMap();
            hashMap.put("elapse_time", String.valueOf((System.currentTimeMillis() - this.mOldestDateModified) / 86400000));
            SamplingStatHelper.recordCountEvent("recent_album", "recent_album_oldest_operated_picture", hashMap);
        }
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getLiveData().observe(getViewLifecycleOwner(), new Observer() { // from class: com.miui.gallery.ui.recentdiscovery.RecentDiscoveryFragment$$ExternalSyntheticLambda0
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                RecentDiscoveryFragment.m1650$r8$lambda$RAVkvmp49OpImKXbbESRiBysXU(RecentDiscoveryFragment.this, (List) obj);
            }
        });
    }

    /* renamed from: onActivityCreated$lambda-3 */
    public static final void m1654onActivityCreated$lambda3(RecentDiscoveryFragment this$0, List list) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.doOnDataLoadFinished(list == null ? 0 : list.size());
        BuildersKt__Builders_commonKt.launch$default(LifecycleOwnerKt.getLifecycleScope(this$0), null, null, new RecentDiscoveryFragment$onActivityCreated$1$1(this$0, list, null), 3, null);
    }

    public final void startDeferredLoaders() {
        getLoaderManager().initLoader(2, null, getLoaderCallback());
        getLoaderManager().initLoader(3, null, getLoaderCallback());
    }

    public final LoaderManager.LoaderCallbacks<Cursor> getLoaderCallback() {
        if (this.mLoaderCallbacks == null) {
            this.mLoaderCallbacks = new RecentDiscoveryLoaderCallback(this);
        }
        LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = this.mLoaderCallbacks;
        Intrinsics.checkNotNull(loaderCallbacks);
        return loaderCallbacks;
    }

    public final void setEmptyViewVisibility(int i) {
        View view = this.mEmptyView;
        if (view != null) {
            Intrinsics.checkNotNull(view);
            view.setVisibility(i);
        }
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        Intrinsics.checkNotNullParameter(newConfig, "newConfig");
        super.onConfigurationChanged(newConfig);
        updateConfiguration(newConfig);
        GalleryRecyclerView galleryRecyclerView = this.mRecyclerView;
        Intrinsics.checkNotNull(galleryRecyclerView);
        int findFirstVisibleItemPosition = galleryRecyclerView.findFirstVisibleItemPosition();
        GalleryRecyclerView galleryRecyclerView2 = this.mRecyclerView;
        Intrinsics.checkNotNull(galleryRecyclerView2);
        galleryRecyclerView2.scrollToPosition(findFirstVisibleItemPosition);
    }

    public final void updateConfiguration(Configuration configuration) {
        int i;
        if (configuration.orientation == 2) {
            i = Config$ThumbConfig.get().sMicroThumbRecentColumnsLand;
        } else {
            i = Config$ThumbConfig.get().sMicroThumbRecentColumnsPortrait;
        }
        GalleryRecyclerView galleryRecyclerView = this.mRecyclerView;
        Intrinsics.checkNotNull(galleryRecyclerView);
        GridLayoutManager gridLayoutManager = (GridLayoutManager) galleryRecyclerView.getLayoutManager();
        Intrinsics.checkNotNull(gridLayoutManager);
        gridLayoutManager.setSpanCount(i);
    }

    public final RecentDiscoveryAdapter2 getAdapter() {
        if (this.mRecentDiscoveryAdapter == null) {
            this.mRecentDiscoveryAdapter = new RecentDiscoveryAdapter2(this.mActivity, getLifecycle());
        }
        RecentDiscoveryAdapter2 recentDiscoveryAdapter2 = this.mRecentDiscoveryAdapter;
        Intrinsics.checkNotNull(recentDiscoveryAdapter2);
        return recentDiscoveryAdapter2;
    }

    public final Uri getUri() {
        Uri build = GalleryContract.Media.URI_RECENT_MEDIA.buildUpon().appendQueryParameter("generate_headers", "true").build();
        Intrinsics.checkNotNullExpressionValue(build, "URI_RECENT_MEDIA\n       …ue\")\n            .build()");
        return build;
    }

    public final ItemClickSupport.OnItemClickListener getGridViewOnItemClickListener() {
        return new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.ui.recentdiscovery.RecentDiscoveryFragment$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public final boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                return RecentDiscoveryFragment.m1651$r8$lambda$yAaCtubYf8Ks_E3DSrn2Gb1vPE(RecentDiscoveryFragment.this, recyclerView, view, i, j, f, f2);
            }
        };
    }

    /* renamed from: _get_gridViewOnItemClickListener_$lambda-4 */
    public static final boolean m1653_get_gridViewOnItemClickListener_$lambda4(RecentDiscoveryFragment this$0, RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        RecentDiscoveryAdapter2 adapter = this$0.getAdapter();
        PhotoPageIntent.Builder operationMask = new PhotoPageIntent.Builder(this$0, InternalPhotoPageActivity.class).setAdapterView(recyclerView).setUri(this$0.getUri()).setInitPosition(i).setCount(adapter.getItemCount()).setSelection(this$0.getSelection()).setSelectionArgs(this$0.getSelectionArgs()).setOrderBy(this$0.getSortOrder()).setImageLoadParams(new ImageLoadParams.Builder().setKey(adapter.getItemKey(i)).setFilePath(adapter.getBindImagePath(i)).setTargetSize(Config$ThumbConfig.get().sMicroRecentTargetSize).setRegionRect(adapter.getItemDecodeRectF(i)).setInitPosition(i).setMimeType(adapter.getMimeType(i)).setSecretKey(adapter.getItemSecretKey(i)).setFileLength(adapter.getFileLength(i)).setImageWidth(adapter.getImageWidth(i)).setImageHeight(adapter.getImageHeight(i)).setCreateTime(adapter.getCreateTime(i)).setLocation(adapter.getLocation(i)).build()).setAlbumId(this$0.mAlbumId).setAlbumName(this$0.mAlbumName).setOperationMask(this$0.getSupportOperationMask());
        RecentDiscoveryAdapter2 recentDiscoveryAdapter2 = this$0.mRecentDiscoveryAdapter;
        Intrinsics.checkNotNull(recentDiscoveryAdapter2);
        operationMask.setUnfoldBurst(!recentDiscoveryAdapter2.supportFoldBurstItems()).build().gotoPhotoPage();
        long dateModified = adapter.getDateModified(i);
        if (dateModified < this$0.mOldestDateModified) {
            this$0.mOldestDateModified = dateModified;
        }
        SamplingStatHelper.recordCountEvent("photo", "click_micro_thumb", MapsKt__MapsKt.mapOf(TuplesKt.to("from", this$0.getPageName()), TuplesKt.to("position", String.valueOf(i))));
        return true;
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment
    public List<Loader<?>> getLoaders() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(getLoaderManager().getLoader(3));
        arrayList.add(getLoaderManager().getLoader(2));
        return arrayList;
    }

    /* compiled from: RecentDiscoveryFragment.kt */
    /* loaded from: classes2.dex */
    public final class RecentDiscoveryLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        public final /* synthetic */ RecentDiscoveryFragment this$0;

        public RecentDiscoveryLoaderCallback(RecentDiscoveryFragment this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader;
            if (i == 2) {
                cursorLoader = new CursorLoader(this.this$0.mActivity);
                cursorLoader.setUri(GalleryContract.Album.URI_SHARE_ALL);
                cursorLoader.setProjection(AlbumConstants.SHARED_ALBUM_PROJECTION);
                cursorLoader.setSelection("count > 0");
            } else if (i != 3) {
                cursorLoader = null;
            } else {
                cursorLoader = new CursorLoader(this.this$0.mActivity);
                Uri URI = GalleryContract.Album.URI;
                Intrinsics.checkNotNullExpressionValue(URI, "URI");
                int i2 = 0;
                Pair[] pairArr = {TuplesKt.to("param_exclude_hidden_album", "true"), TuplesKt.to("param_exclude_rubbish_album", "true"), TuplesKt.to("join_share", "true"), TuplesKt.to("param_join_virtual_screenshots_and_recorders", "true"), TuplesKt.to("param_exclude_real_screenshots_and_recorders", "true")};
                Uri.Builder buildUpon = URI.buildUpon();
                while (i2 < 5) {
                    Pair pair = pairArr[i2];
                    i2++;
                    buildUpon.appendQueryParameter((String) pair.getFirst(), (String) pair.getSecond());
                }
                Uri build = buildUpon.build();
                Intrinsics.checkNotNullExpressionValue(build, "{\n        buildUpon().ap…}\n        }.build()\n    }");
                cursorLoader.setUri(build);
                cursorLoader.setProjection(AlbumManager.QUERY_ALBUM_PROJECTION);
            }
            Intrinsics.checkNotNull(cursorLoader);
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            Intrinsics.checkNotNullParameter(loader, "loader");
            int id = loader.getId();
            if (id == 2) {
                this.this$0.getAdapter().setShareAlbums(cursor);
            } else if (id != 3) {
            } else {
                this.this$0.getAdapter().setAllAlbums(cursor);
            }
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<Cursor> loader) {
            Intrinsics.checkNotNullParameter(loader, "loader");
            int id = loader.getId();
            if (id == 2) {
                this.this$0.getAdapter().resetShareAlbums();
            } else if (id != 3) {
            } else {
                this.this$0.getAdapter().setAllAlbums(null);
            }
        }
    }

    public final void doOnDataLoadFinished(int i) {
        if (i == 0) {
            setEmptyViewVisibility(0);
            return;
        }
        setEmptyViewVisibility(8);
        startDeferredLoaders();
    }

    /* compiled from: RecentDiscoveryFragment.kt */
    /* loaded from: classes2.dex */
    public final class RecentMultiChoiceModeListener implements MultiChoiceModeListener {
        public MenuItem mAddToAlbum;
        public MenuItem mCreativity;
        public MenuItem mDelete;
        public ActionMode mMode;
        public MenuItem mSend;
        public final /* synthetic */ RecentDiscoveryFragment this$0;

        public static /* synthetic */ void $r8$lambda$5qWdpfzwqZZHafnwcIe9m8KgDvs(RecentMultiChoiceModeListener recentMultiChoiceModeListener, long[] jArr, boolean z) {
            m1658onActionItemClicked$lambda0(recentMultiChoiceModeListener, jArr, z);
        }

        public static /* synthetic */ void $r8$lambda$vvUxQJCQYQtSgq2QCTuphspPpKw(RecentDiscoveryFragment recentDiscoveryFragment, ActionMode actionMode, long[] jArr) {
            m1659onActionItemClicked$lambda1(recentDiscoveryFragment, actionMode, jArr);
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode mode) {
            Intrinsics.checkNotNullParameter(mode, "mode");
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(menu, "menu");
            return false;
        }

        public RecentMultiChoiceModeListener(RecentDiscoveryFragment this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void onAllItemsCheckedStateChanged(ActionMode mode, boolean z) {
            Intrinsics.checkNotNullParameter(mode, "mode");
            updateMenuState();
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
            this.mCreativity = menu.findItem(R.id.action_produce);
            this.mDelete = menu.findItem(R.id.delete);
            if (this.mCreativity != null && GalleryPreferences.Assistant.isCreativityFunctionOn()) {
                MenuItem menuItem = this.mCreativity;
                Intrinsics.checkNotNull(menuItem);
                menuItem.setVisible(true);
            }
            MenuItem menuItem2 = this.mDelete;
            if (menuItem2 != null) {
                Intrinsics.checkNotNull(menuItem2);
                menuItem2.setVisible(true);
            }
            this.mSend = menu.findItem(R.id.action_send);
            SamplingStatHelper.recordCountEvent("recent_album", "action_mode_create");
            SamplingStatHelper.recordStringPropertyEvent("best_image_count", String.valueOf(MediaFeatureCacheManager.getInstance().getBestImageCount(false)));
            return true;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(item, "item");
            if (item.getItemId() != 16908313 && item.getItemId() != 16908314) {
                LinearMotorHelper.performHapticFeedback(this.this$0.mActivity, LinearMotorHelper.HAPTIC_TAP_LIGHT);
            }
            boolean z = false;
            switch (item.getItemId()) {
                case R.id.action_produce /* 2131361895 */:
                    AppCompatActivity appCompatActivity = this.this$0.mActivity;
                    final RecentDiscoveryFragment recentDiscoveryFragment = this.this$0;
                    MediaAndAlbumOperations.OnCompleteListener onCompleteListener = new MediaAndAlbumOperations.OnCompleteListener() { // from class: com.miui.gallery.ui.recentdiscovery.RecentDiscoveryFragment$RecentMultiChoiceModeListener$$ExternalSyntheticLambda1
                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnCompleteListener
                        public final void onComplete(long[] jArr) {
                            RecentDiscoveryFragment.RecentMultiChoiceModeListener.$r8$lambda$vvUxQJCQYQtSgq2QCTuphspPpKw(RecentDiscoveryFragment.this, mode, jArr);
                        }
                    };
                    EditableListViewWrapper editableListViewWrapper = this.this$0.mEditableWrapper;
                    Intrinsics.checkNotNull(editableListViewWrapper);
                    MediaAndAlbumOperations.doProduceCreation(appCompatActivity, onCompleteListener, editableListViewWrapper.getCheckedItems());
                    break;
                case R.id.action_send /* 2131361903 */:
                    EditableListViewWrapper editableListViewWrapper2 = this.this$0.mEditableWrapper;
                    Intrinsics.checkNotNull(editableListViewWrapper2);
                    List<Integer> checkedPositions = editableListViewWrapper2.getCheckedPositions();
                    Intrinsics.checkNotNullExpressionValue(checkedPositions, "mEditableWrapper!!.checkedPositions");
                    ArrayList arrayList = new ArrayList(checkedPositions.size());
                    int i = Integer.MAX_VALUE;
                    for (Integer position : checkedPositions) {
                        RecentDiscoveryAdapter2 adapter = this.this$0.getAdapter();
                        Intrinsics.checkNotNullExpressionValue(position, "position");
                        arrayList.add(Long.valueOf(adapter.getItemKey(position.intValue())));
                        if (position.intValue() < i) {
                            i = position.intValue();
                        }
                    }
                    int size = checkedPositions.size();
                    int[] iArr = new int[size];
                    long[] jArr = new long[arrayList.size()];
                    for (int i2 = 0; i2 < checkedPositions.size(); i2++) {
                        Integer num = checkedPositions.get(i2);
                        Intrinsics.checkNotNullExpressionValue(num, "positions[i]");
                        iArr[i2] = num.intValue();
                        Object obj = arrayList.get(i2);
                        Intrinsics.checkNotNullExpressionValue(obj, "ids[i]");
                        jArr[i2] = ((Number) obj).longValue();
                    }
                    int i3 = i == Integer.MAX_VALUE ? 0 : i;
                    if (size == 0) {
                        z = true;
                    }
                    if ((!z) && this.this$0.getAdapter().getItemCount() > 0) {
                        ImageLoadParams build = new ImageLoadParams.Builder().setKey(this.this$0.getAdapter().getItemKey(i3)).setFilePath(this.this$0.getAdapter().getBindImagePath(i3)).setTargetSize(Config$ThumbConfig.get().sMicroRecentTargetSize).setRegionRect(this.this$0.getAdapter().getItemDecodeRectF(i3)).setInitPosition(i3).setMimeType(this.this$0.getAdapter().getMimeType(i3)).setSecretKey(this.this$0.getAdapter().getItemSecretKey(i3)).setFileLength(this.this$0.getAdapter().getFileLength(i3)).setCreateTime(this.this$0.getAdapter().getCreateTime(i3)).setLocation(this.this$0.getAdapter().getLocation(i3)).build();
                        RecentDiscoveryFragment recentDiscoveryFragment2 = this.this$0;
                        IntentUtil.gotoPreviewSelectPage(recentDiscoveryFragment2, recentDiscoveryFragment2.getUri(), i3, this.this$0.getAdapter().getItemCount(), this.this$0.getSelection(), this.this$0.getSelectionArgs(), this.this$0.getSortOrder(), build, jArr, iArr);
                    }
                    mode.finish();
                    SamplingStatHelper.recordCountEvent("recent_album", "send");
                    break;
                case R.id.add_to_album /* 2131361918 */:
                    AppCompatActivity appCompatActivity2 = this.this$0.mActivity;
                    MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener = new MediaAndAlbumOperations.OnAddAlbumListener() { // from class: com.miui.gallery.ui.recentdiscovery.RecentDiscoveryFragment$RecentMultiChoiceModeListener$$ExternalSyntheticLambda0
                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnAddAlbumListener
                        public final void onComplete(long[] jArr2, boolean z2) {
                            RecentDiscoveryFragment.RecentMultiChoiceModeListener.$r8$lambda$5qWdpfzwqZZHafnwcIe9m8KgDvs(RecentDiscoveryFragment.RecentMultiChoiceModeListener.this, jArr2, z2);
                        }
                    };
                    EditableListViewWrapper editableListViewWrapper3 = this.this$0.mEditableWrapper;
                    Intrinsics.checkNotNull(editableListViewWrapper3);
                    boolean isCheckedItemContainVideo = editableListViewWrapper3.isCheckedItemContainVideo();
                    EditableListViewWrapper editableListViewWrapper4 = this.this$0.mEditableWrapper;
                    Intrinsics.checkNotNull(editableListViewWrapper4);
                    long[] checkedItemIds = editableListViewWrapper4.getCheckedItemIds();
                    MediaAndAlbumOperations.addToAlbum(appCompatActivity2, onAddAlbumListener, false, true, true, isCheckedItemContainVideo, Arrays.copyOf(checkedItemIds, checkedItemIds.length));
                    SamplingStatHelper.recordCountEvent("recent_album", "add_to_album");
                    TrackController.trackExpose("403.26.0.1.11240", "403.44.3.1.11229");
                    break;
                case R.id.delete /* 2131362250 */:
                    this.this$0.doDelete(mode);
                    SamplingStatHelper.recordCountEvent("recent_album", "delete_photo");
                    break;
                default:
                    return false;
            }
            EditableListViewWrapper editableListViewWrapper5 = this.this$0.mEditableWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper5);
            List<Integer> checkedPositions2 = editableListViewWrapper5.getCheckedPositions();
            Intrinsics.checkNotNullExpressionValue(checkedPositions2, "mEditableWrapper!!.checkedPositions");
            RecentDiscoveryAdapter2 adapter2 = this.this$0.getAdapter();
            for (Integer positions : checkedPositions2) {
                Intrinsics.checkNotNullExpressionValue(positions, "positions");
                long dateModified = adapter2.getDateModified(positions.intValue());
                if (dateModified < this.this$0.mOldestDateModified) {
                    this.this$0.mOldestDateModified = dateModified;
                }
            }
            return true;
        }

        /* renamed from: onActionItemClicked$lambda-0 */
        public static final void m1658onActionItemClicked$lambda0(RecentMultiChoiceModeListener this$0, long[] jArr, boolean z) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            ActionMode actionMode = this$0.mMode;
            Intrinsics.checkNotNull(actionMode);
            actionMode.finish();
        }

        /* renamed from: onActionItemClicked$lambda-1 */
        public static final void m1659onActionItemClicked$lambda1(RecentDiscoveryFragment this$0, ActionMode mode, long[] jArr) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(mode, "$mode");
            EditableListViewWrapper editableListViewWrapper = this$0.mEditableWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            List<CheckableAdapter.CheckedItem> checkedItems = editableListViewWrapper.getCheckedItems();
            int i = 0;
            for (int i2 = 0; i2 < checkedItems.size(); i2++) {
                if (MediaFeatureCacheManager.getInstance().isBestImage(checkedItems.get(i2).getId(), false, false, null)) {
                    i++;
                }
            }
            SamplingStatHelper.recordCountEvent("recent_album", "produce", MapsKt__MapsKt.mapOf(TuplesKt.to(MiStat.Param.COUNT, String.valueOf(checkedItems.size())), TuplesKt.to("best_image_count", String.valueOf(i))));
            mode.finish();
        }

        public final void updateMenuState() {
            EditableListViewWrapper editableListViewWrapper = this.this$0.mEditableWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            if (editableListViewWrapper.getCheckedItemCount() < 1) {
                if (isMenuItemVisible(this.mDelete)) {
                    MenuItem menuItem = this.mDelete;
                    Intrinsics.checkNotNull(menuItem);
                    menuItem.setEnabled(false);
                }
                if (isMenuItemVisible(this.mAddToAlbum)) {
                    MenuItem menuItem2 = this.mAddToAlbum;
                    Intrinsics.checkNotNull(menuItem2);
                    menuItem2.setEnabled(false);
                }
                if (isMenuItemVisible(this.mCreativity)) {
                    MenuItem menuItem3 = this.mCreativity;
                    Intrinsics.checkNotNull(menuItem3);
                    menuItem3.setEnabled(false);
                }
                if (!isMenuItemVisible(this.mSend)) {
                    return;
                }
                MenuItem menuItem4 = this.mSend;
                Intrinsics.checkNotNull(menuItem4);
                menuItem4.setEnabled(false);
                return;
            }
            if (isMenuItemVisible(this.mDelete)) {
                MenuItem menuItem5 = this.mDelete;
                Intrinsics.checkNotNull(menuItem5);
                menuItem5.setEnabled(true);
            }
            if (isMenuItemVisible(this.mAddToAlbum)) {
                MenuItem menuItem6 = this.mAddToAlbum;
                Intrinsics.checkNotNull(menuItem6);
                menuItem6.setEnabled(true);
            }
            if (isMenuItemVisible(this.mCreativity)) {
                MenuItem menuItem7 = this.mCreativity;
                Intrinsics.checkNotNull(menuItem7);
                menuItem7.setEnabled(true);
            }
            if (!isMenuItemVisible(this.mSend)) {
                return;
            }
            MenuItem menuItem8 = this.mSend;
            Intrinsics.checkNotNull(menuItem8);
            menuItem8.setEnabled(true);
        }

        public final boolean isMenuItemVisible(MenuItem menuItem) {
            return menuItem != null && menuItem.isVisible();
        }

        public final ActionMode getMode() {
            return this.mMode;
        }
    }

    public final void doDelete(final ActionMode actionMode) {
        EditableListViewWrapper editableListViewWrapper = this.mEditableWrapper;
        Intrinsics.checkNotNull(editableListViewWrapper);
        long[] checkedItemIds = editableListViewWrapper.getCheckedItemIds();
        if (checkedItemIds != null) {
            if (!(checkedItemIds.length == 0)) {
                MediaAndAlbumOperations.delete(this.mActivity, "RecentDiscoveryFragmentDeleteMediaDialogFragment", new DeletionTask.OnDeletionCompleteListener() { // from class: com.miui.gallery.ui.recentdiscovery.RecentDiscoveryFragment$doDelete$1
                    @Override // com.miui.gallery.ui.DeletionTask.OnDeletionCompleteListener
                    public void onDeleted(int i, long[] jArr) {
                        if (RecentDiscoveryFragment.access$getMActivity$p$s1706430181(RecentDiscoveryFragment.this) == null) {
                            return;
                        }
                        ToastUtils.makeText(RecentDiscoveryFragment.access$getMActivity$p$s1706430181(RecentDiscoveryFragment.this), RecentDiscoveryFragment.this.getResources().getQuantityString(R.plurals.delete_finish_format, i, Integer.valueOf(i)));
                        if (i > 0) {
                            SoundUtils.playSoundForOperation(RecentDiscoveryFragment.access$getMActivity$p$s1706430181(RecentDiscoveryFragment.this), 0);
                        }
                        ActionMode actionMode2 = actionMode;
                        Intrinsics.checkNotNull(actionMode2);
                        actionMode2.finish();
                    }
                }, null, this.mAlbumId, this.mAlbumName, 29, Arrays.copyOf(checkedItemIds, checkedItemIds.length));
                return;
            }
        }
        ToastUtils.makeText(this.mActivity, getString(R.string.delete_other_shared_images));
        Intrinsics.checkNotNull(actionMode);
        actionMode.finish();
    }

    /* compiled from: RecentDiscoveryFragment.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int i) {
        Intrinsics.checkNotNullParameter(data, "data");
        ArrayList arrayList = new ArrayList();
        EditableListViewWrapper editableListViewWrapper = this.mEditableWrapper;
        Intrinsics.checkNotNull(editableListViewWrapper);
        if (editableListViewWrapper.isInActionMode()) {
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getSelectAllShortcutInfo());
            arrayList.addAll(KeyboardShortcutGroupManager.getInstance().getDeleteShortcutInfo());
        }
        data.add(new KeyboardShortcutGroup(getPageName(), arrayList));
    }

    public final boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        return KeyboardShortcutGroupManager.getInstance().onKeyShortcut(i, keyEvent, this.shortcutCallback);
    }

    /* compiled from: RecentDiscoveryFragment.kt */
    /* loaded from: classes2.dex */
    public final class RecentKeyboardShortcutCallback implements KeyboardShortcutGroupManager.OnKeyShortcutCallback {
        public final /* synthetic */ RecentDiscoveryFragment this$0;

        public RecentKeyboardShortcutCallback(RecentDiscoveryFragment this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onSelectAllPressed() {
            EditableListViewWrapper editableListViewWrapper = this.this$0.mEditableWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            if (editableListViewWrapper.isInActionMode()) {
                EditableListViewWrapper editableListViewWrapper2 = this.this$0.mEditableWrapper;
                Intrinsics.checkNotNull(editableListViewWrapper2);
                editableListViewWrapper2.setAllItemsCheckState(true);
            }
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onDeletePressed() {
            EditableListViewWrapper editableListViewWrapper = this.this$0.mEditableWrapper;
            Intrinsics.checkNotNull(editableListViewWrapper);
            if (editableListViewWrapper.isInActionMode()) {
                RecentDiscoveryFragment recentDiscoveryFragment = this.this$0;
                recentDiscoveryFragment.doDelete(recentDiscoveryFragment.choiceModeListener.getMode());
                return true;
            }
            return true;
        }
    }
}
