package com.miui.gallery.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.FragmentViewModelLazyKt;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwnerKt;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.adapter.AlbumDetailAdapter2;
import com.miui.gallery.adapter.AlbumType;
import com.miui.gallery.adapter.PreloadItem;
import com.miui.gallery.adapter.SortBy;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.data.ClusteredList;
import com.miui.gallery.provider.cache.CacheLiveData;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.provider.cache.MediaCacheItem;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.recyclerview.AsyncListDiffer;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import miuix.appcompat.app.AppCompatActivity;

/* compiled from: ModernAlbumDetailFragment.kt */
/* loaded from: classes2.dex */
public class ModernAlbumDetailFragment extends AlbumDetailFragment<AlbumDetailAdapter2> {
    public static final Companion Companion = new Companion(null);
    public final Lazy _itemAnimator$delegate;
    public final Lazy liveData$delegate;
    public final Lazy viewModel$delegate = FragmentViewModelLazyKt.createViewModelLazy(this, Reflection.getOrCreateKotlinClass(PhotoListViewModel.class), new ModernAlbumDetailFragment$special$$inlined$viewModels$default$2(new ModernAlbumDetailFragment$special$$inlined$viewModels$default$1(this)), null);

    public ModernAlbumDetailFragment() {
        LazyThreadSafetyMode lazyThreadSafetyMode = LazyThreadSafetyMode.NONE;
        this.liveData$delegate = LazyKt__LazyJVMKt.lazy(lazyThreadSafetyMode, new ModernAlbumDetailFragment$liveData$2(this));
        this._itemAnimator$delegate = LazyKt__LazyJVMKt.lazy(lazyThreadSafetyMode, ModernAlbumDetailFragment$_itemAnimator$2.INSTANCE);
    }

    public final PhotoListViewModel getViewModel() {
        return (PhotoListViewModel) this.viewModel$delegate.mo119getValue();
    }

    public final CacheLiveData<MediaCacheItem, IRecord> getLiveData() {
        return (CacheLiveData) this.liveData$delegate.mo119getValue();
    }

    public final DefaultItemAnimator get_itemAnimator() {
        return (DefaultItemAnimator) this._itemAnimator$delegate.mo119getValue();
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public RecyclerView.ItemAnimator getItemAnimator() {
        return get_itemAnimator();
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    /* renamed from: getViewAdapter */
    public AlbumDetailAdapter2 mo1517getViewAdapter() {
        GalleryRecyclerView galleryRecyclerView = this.mRecyclerView;
        if (galleryRecyclerView == null) {
            return null;
        }
        AlbumDetailAdapter2 albumDetailAdapter2 = (AlbumDetailAdapter2) ((RecyclerView.Adapter) WrapperAdapterUtils.findWrappedAdapter(galleryRecyclerView.getAdapter(), AlbumDetailAdapter2.class));
        return albumDetailAdapter2 == null ? (AlbumDetailAdapter2) super.mo1517getViewAdapter() : albumDetailAdapter2;
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    /* renamed from: getAdapter */
    public AlbumDetailAdapter2 mo1564getAdapter() {
        if (this.mAlbumDetailAdapter == 0) {
            AppCompatActivity mActivity = this.mActivity;
            Intrinsics.checkNotNullExpressionValue(mActivity, "mActivity");
            Lifecycle lifecycle = getLifecycle();
            Intrinsics.checkNotNullExpressionValue(lifecycle, "lifecycle");
            AlbumDetailAdapter2 albumDetailAdapter2 = new AlbumDetailAdapter2(mActivity, lifecycle);
            if (this.mIsOtherShareAlbum) {
                albumDetailAdapter2.setAlbumType(AlbumType.OTHER_SHARE);
            } else if (this.mIsScreenshotAlbum) {
                albumDetailAdapter2.setAlbumType(AlbumType.SCREENSHOT);
            } else if (this.mIsVideoAlbum) {
                albumDetailAdapter2.setAlbumType(AlbumType.VIDEO);
            } else if (isSecretAlbum()) {
                albumDetailAdapter2.setAlbumType(AlbumType.SECRET);
            } else if (isFavoritesAlbum()) {
                albumDetailAdapter2.setAlbumType(AlbumType.FAVORITES);
            } else if (isAllPhotosAlbum()) {
                albumDetailAdapter2.setAlbumType(AlbumType.ALL_PHOTOS);
            }
            this.mAlbumDetailAdapter = albumDetailAdapter2;
        }
        ADAPTER mAlbumDetailAdapter = this.mAlbumDetailAdapter;
        Intrinsics.checkNotNullExpressionValue(mAlbumDetailAdapter, "mAlbumDetailAdapter");
        return (AlbumDetailAdapter2) mAlbumDetailAdapter;
    }

    @Override // com.bumptech.glide.ListPreloader.PreloadModelProvider
    public List<PreloadItem> getPreloadItems(int i) {
        int[] unpackGroupedPosition = this.mEditableWrapper.unpackGroupedPosition(i);
        if (unpackGroupedPosition[0] == -1 || unpackGroupedPosition[1] == -1) {
            return CollectionsKt__CollectionsKt.emptyList();
        }
        List<PreloadItem> preloadItems = mo1564getAdapter().getPreloadItems(mo1564getAdapter().packDataPosition(unpackGroupedPosition[0], unpackGroupedPosition[1]));
        Intrinsics.checkNotNullExpressionValue(preloadItems, "{\n            val dataPo…dItems(dataPos)\n        }");
        return preloadItems;
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragment, com.miui.gallery.ui.AlbumDetailFragmentBase
    public void onDataFirstBound(int i) {
        super.onDataFirstBound(i);
        if (isAllPhotosAlbum()) {
            TimeMonitor.trackTimeMonitor("403.44.0.1.13763", i);
        }
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

    @Override // com.miui.gallery.ui.AlbumDetailFragmentBase
    public Uri getUri(SortBy sortBy) {
        Uri uri = super.getUri(sortBy);
        Intrinsics.checkNotNullExpressionValue(uri, "super.getUri(sortBy)");
        int i = 0;
        Pair[] pairArr = {TuplesKt.to("param_show_headers", String.valueOf(needShowTimeLine()))};
        Uri.Builder buildUpon = uri.buildUpon();
        while (i < 1) {
            Pair pair = pairArr[i];
            i++;
            buildUpon.appendQueryParameter((String) pair.getFirst(), (String) pair.getSecond());
        }
        Uri build = buildUpon.build();
        Intrinsics.checkNotNullExpressionValue(build, "{\n        buildUpon().ap…}\n        }.build()\n    }");
        return build;
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public void startDataLoading() {
        getLiveData().observe(getViewLifecycleOwner(), new ObserverWrapper(this, new AlbumDetailObserver(this)));
        mo1564getAdapter().getDiffer().addListListener(new AsyncListDiffer.ListListener<IRecord>() { // from class: com.miui.gallery.ui.ModernAlbumDetailFragment$startDataLoading$1
            @Override // com.miui.gallery.widget.recyclerview.AsyncListDiffer.ListListener
            public void onCurrentListChanged(List<? extends IRecord> previousList, List<? extends IRecord> currentList) {
                Intrinsics.checkNotNullParameter(previousList, "previousList");
                Intrinsics.checkNotNullParameter(currentList, "currentList");
                ModernAlbumDetailFragment modernAlbumDetailFragment = ModernAlbumDetailFragment.this;
                modernAlbumDetailFragment.mTagProportionChanged = true;
                modernAlbumDetailFragment.mo1564getAdapter().setShowTimeLine(ModernAlbumDetailFragment.this.needShowTimeLine());
            }
        });
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragment
    public void onSortByChanged() {
        mo1564getAdapter().setCurrentSortBy(this.mViewModel.getSortBy());
        mo1564getAdapter().setShowTimeLine(needShowTimeLine());
        reQuery();
    }

    public final void reQuery() {
        getLiveData().updateQueryArgs(getUri(this.mViewModel.getSortBy()), getSelection(), getSelectionArgs(), configOrderBy(this.mViewModel.getSortBy()), true);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, bundle);
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        EditableListViewWrapper editableListViewWrapper = this.mEditableWrapper;
        Intrinsics.checkNotNull(editableListViewWrapper);
        editableListViewWrapper.setAdapter(mo1564getAdapter());
        EditableListViewWrapper editableListViewWrapper2 = this.mEditableWrapper;
        Intrinsics.checkNotNull(editableListViewWrapper2);
        editableListViewWrapper2.setHandleTouchAnimItemType(AlbumDetailGridItem.class.getSimpleName());
        this.mRecyclerView.setScrollingCalculator(mo1564getAdapter());
        EditableListViewWrapper editableListViewWrapper3 = this.mEditableWrapper;
        Intrinsics.checkNotNull(editableListViewWrapper3);
        editableListViewWrapper3.setOnScrollListener(mo1564getAdapter().generateWrapScrollListener(Config$ThumbConfig.get().sPreloadNum == 0 ? null : new RecyclerViewPreloader(this, this, mo1564getAdapter(), Config$ThumbConfig.get().sPreloadNum)));
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragment, com.miui.gallery.ui.PhotoListFragmentBase, com.miui.gallery.ui.BaseMediaFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mo1564getAdapter().setCurrentSortBy(this.mViewModel.getSortBy());
        mo1564getAdapter().setLastSortBy(this.mViewModel.getSortBy());
    }

    /* compiled from: ModernAlbumDetailFragment.kt */
    /* loaded from: classes2.dex */
    public final class ObserverWrapper implements Observer<List<? extends IRecord>> {
        public final /* synthetic */ ModernAlbumDetailFragment this$0;
        public final Observer<List<IRecord>> wrapped;

        public ObserverWrapper(ModernAlbumDetailFragment this$0, Observer<List<IRecord>> wrapped) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(wrapped, "wrapped");
            this.this$0 = this$0;
            this.wrapped = wrapped;
        }

        @Override // androidx.lifecycle.Observer
        public void onChanged(List<? extends IRecord> list) {
            int clusterKey = this.this$0.mViewMode.getClusterKey();
            boolean z = true;
            boolean z2 = !this.this$0.needShowTimeLine();
            if (list instanceof ClusteredList) {
                ((ClusteredList) list).select(clusterKey, z2);
            }
            this.wrapped.onChanged(list);
            int i = 0;
            if (list != null && !list.isEmpty()) {
                z = false;
            }
            if (z) {
                this.this$0.setEmptyViewVisibility(0);
            } else {
                this.this$0.setEmptyViewVisibility(8);
            }
            ModernAlbumDetailFragment modernAlbumDetailFragment = this.this$0;
            if (list != null) {
                i = list.size();
            }
            modernAlbumDetailFragment.onDataLoaded(i);
        }
    }

    /* compiled from: ModernAlbumDetailFragment.kt */
    /* loaded from: classes2.dex */
    public final class AlbumDetailObserver implements Observer<List<? extends IRecord>> {
        public final /* synthetic */ ModernAlbumDetailFragment this$0;

        public AlbumDetailObserver(ModernAlbumDetailFragment this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
        }

        @Override // androidx.lifecycle.Observer
        public void onChanged(List<? extends IRecord> list) {
            int dimensionPixelSize;
            AlbumDetailAdapter2 mo1564getAdapter = this.this$0.mo1564getAdapter();
            BuildersKt__Builders_commonKt.launch$default(LifecycleOwnerKt.getLifecycleScope(this.this$0), null, null, new ModernAlbumDetailFragment$AlbumDetailObserver$onChanged$1(mo1564getAdapter, list, this.this$0, null), 3, null);
            mo1564getAdapter.setSpacing(this.this$0.mSpacing);
            mo1564getAdapter.setSpanCount(this.this$0.mColumns);
            ModernAlbumDetailFragment modernAlbumDetailFragment = this.this$0;
            if (modernAlbumDetailFragment.mViewModel.getSortBy() == SortBy.UPDATE_DATE || this.this$0.mViewModel.getSortBy() == SortBy.CREATE_DATE) {
                this.this$0.mRecyclerView.setCapsuleCalculator(mo1564getAdapter);
                if (!this.this$0.mViewModel.isTimeGroup()) {
                    Context context = this.this$0.getContext();
                    Intrinsics.checkNotNull(context);
                    Intrinsics.checkNotNullExpressionValue(context, "context!!");
                    dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.fast_scroller_margin_top_to_time_line);
                } else {
                    Context context2 = this.this$0.getContext();
                    Intrinsics.checkNotNull(context2);
                    Intrinsics.checkNotNullExpressionValue(context2, "context!!");
                    int dimensionPixelOffset = context2.getResources().getDimensionPixelOffset(R.dimen.time_line_header_height);
                    Context context3 = this.this$0.getContext();
                    Intrinsics.checkNotNull(context3);
                    Intrinsics.checkNotNullExpressionValue(context3, "context!!");
                    dimensionPixelSize = dimensionPixelOffset + context3.getResources().getDimensionPixelSize(R.dimen.fast_scroller_margin_top_to_time_line);
                }
            } else {
                this.this$0.mRecyclerView.setCapsuleCalculator(null);
                Context context4 = this.this$0.getContext();
                Intrinsics.checkNotNull(context4);
                Intrinsics.checkNotNullExpressionValue(context4, "context!!");
                dimensionPixelSize = context4.getResources().getDimensionPixelSize(R.dimen.fast_scroller_margin_top_to_time_line);
            }
            modernAlbumDetailFragment.mFastScrollerMarginTop = dimensionPixelSize;
            ModernAlbumDetailFragment modernAlbumDetailFragment2 = this.this$0;
            modernAlbumDetailFragment2.mRecyclerView.setFastScrollerTopMargin(modernAlbumDetailFragment2.mFastScrollerMarginTop);
        }
    }

    /* compiled from: ModernAlbumDetailFragment.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final ModernAlbumDetailFragment newInstance(long j) {
            Bundle bundle = new Bundle();
            bundle.putLong("childAlbumId", j);
            ModernAlbumDetailFragment modernAlbumDetailFragment = new ModernAlbumDetailFragment();
            modernAlbumDetailFragment.setArguments(bundle);
            return modernAlbumDetailFragment;
        }
    }
}
