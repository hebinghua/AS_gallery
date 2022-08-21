package com.miui.gallery.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.tracing.Trace;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.FastScrollerTimeCapsuleModel;
import com.miui.gallery.adapter.itemmodel.MediaTimeProportionTagModel;
import com.miui.gallery.assistant.cache.MediaFeatureCacheManager;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.model.HomeMedia;
import com.miui.gallery.provider.cache.IMediaSnapshot;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.provider.cache.MediaGroup;
import com.miui.gallery.ui.FullyDrawnCounter;
import com.miui.gallery.ui.FullyDrawnCounterImpl;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.ui.ViewProvider;
import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.TalkBackUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.AbsMultiImageViewHolder;
import com.miui.gallery.widget.recyclerview.AbsSingleImageViewHolder;
import com.miui.gallery.widget.recyclerview.AbsViewHolder;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsuleContentProvider;
import com.miui.gallery.widget.recyclerview.IDeferrableViewHolder;
import com.miui.gallery.widget.recyclerview.ProportionTagModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KProperty1;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* compiled from: HomePageAdapter2.kt */
/* loaded from: classes.dex */
public final class HomePageAdapter2 extends ListMultiViewMediaAdapter<IMediaSnapshot> implements CheckableAdapter, FullyDrawnCounter {
    public static final Companion Companion = new Companion(null);
    public static final int[] PRELOAD_SIZE = {Config$ThumbConfig.get().sTinyTargetSize.getWidth(), Config$ThumbConfig.get().sTinyTargetSize.getHeight()};
    public static final KProperty1<IMediaSnapshot, Long> SORT_TIME_PROPS = HomePageAdapter2$Companion$SORT_TIME_PROPS$1.INSTANCE;
    public final BiConsumer<RecyclerView.ViewHolder, Object> deferredBindings;
    public FastScrollerTimeCapsuleModel fastScrollerTimeCapsuleModel;
    public final FullyDrawnCounterImpl fullyDrawnCounter;
    public List<ProportionTagModel<Integer>> tagProportionModels;
    public ViewProvider viewProvider;

    public static /* synthetic */ void $r8$lambda$QSiU7NDmH6Q14Wjqc0j4m5r3SYo(HomePageAdapter2 homePageAdapter2, RecyclerView.ViewHolder viewHolder, Object obj) {
        m501deferredBindings$lambda0(homePageAdapter2, viewHolder, obj);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return 1;
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public boolean supportFoldBurstItems() {
        return true;
    }

    public final void doDelayedBindWhileIdle(View view, IMediaSnapshot iMediaSnapshot) {
        try {
            Trace.beginSection("doDelayedBindWhileIdle");
            view.setContentDescription(TalkBackUtil.getContentDescriptionForImage(view.getContext(), iMediaSnapshot.getCreateTime(), iMediaSnapshot.getLocation(), iMediaSnapshot.getMimeType()));
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    public /* synthetic */ HomePageAdapter2(Context context, Lifecycle lifecycle, SyncStateDisplay$DisplayScene syncStateDisplay$DisplayScene, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, lifecycle, (i & 4) != 0 ? SyncStateDisplay$DisplayScene.SCENE_IN_CHECK_MODE : syncStateDisplay$DisplayScene);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HomePageAdapter2(Context context, Lifecycle lifecycle, SyncStateDisplay$DisplayScene type) {
        super(context, type, lifecycle);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(lifecycle, "lifecycle");
        Intrinsics.checkNotNullParameter(type, "type");
        this.deferredBindings = new BiConsumer() { // from class: com.miui.gallery.adapter.HomePageAdapter2$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                HomePageAdapter2.$r8$lambda$QSiU7NDmH6Q14Wjqc0j4m5r3SYo(HomePageAdapter2.this, (RecyclerView.ViewHolder) obj, obj2);
            }
        };
        this.fullyDrawnCounter = null;
    }

    /* renamed from: deferredBindings$lambda-0 */
    public static final void m501deferredBindings$lambda0(HomePageAdapter2 this$0, RecyclerView.ViewHolder viewHolder, Object obj) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        View view = viewHolder.itemView;
        Intrinsics.checkNotNullExpressionValue(view, "vh.itemView");
        Objects.requireNonNull(obj, "null cannot be cast to non-null type com.miui.gallery.provider.cache.IMediaSnapshot");
        this$0.doDelayedBindWhileIdle(view, (IMediaSnapshot) obj);
    }

    @Override // com.miui.gallery.widget.recyclerview.AsyncListDiffer.ListListener
    public void onCurrentListChanged(List<? extends IRecord> previousList, List<? extends IRecord> currentList) {
        Intrinsics.checkNotNullParameter(previousList, "previousList");
        Intrinsics.checkNotNullParameter(currentList, "currentList");
        this.mClusterAdapter.setViewMode(this.mViewMode);
        this.mClusterAdapter.swapData(currentList);
        DefaultLogger.d("HomePageAdapter2", "onCurrentListChanged, " + previousList.size() + " -> " + currentList.size());
    }

    @Override // com.miui.gallery.adapter.ListMultiViewMediaAdapter
    /* renamed from: getMediaItem */
    public IMediaSnapshot mo485getMediaItem(int i) {
        IRecord item = getAdapterDelegate().getItem(i);
        Objects.requireNonNull(item, "null cannot be cast to non-null type com.miui.gallery.provider.cache.IMediaSnapshot");
        return (IMediaSnapshot) item;
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public long getItemSortTime(int i) {
        KProperty1<IMediaSnapshot, Long> kProperty1 = SORT_TIME_PROPS;
        if (Intrinsics.areEqual(kProperty1, HomePageAdapter2$getItemSortTime$1.INSTANCE)) {
            return mo485getMediaItem(i).getCreateTime();
        }
        if (!Intrinsics.areEqual(kProperty1, HomePageAdapter2$getItemSortTime$2.INSTANCE)) {
            throw new IllegalArgumentException("Unknown sort time property");
        }
        return mo485getMediaItem(i).getSortTime();
    }

    public final void setViewProvider(ViewProvider viewProvider) {
        this.viewProvider = viewProvider;
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter
    public List<PreloadItem> getPreloadItems(int i) {
        if (i >= 0 && i < getItemCount()) {
            return CollectionsKt__CollectionsJVMKt.listOf(new PreloadItem(getOptimalThumbFilePath(i, false), getFileLength(i), getItemDecodeRectF(i), getItemSecretKey(i)));
        }
        return CollectionsKt__CollectionsKt.emptyList();
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter, com.bumptech.glide.ListPreloader.PreloadSizeProvider
    public int[] getPreloadSize(PreloadItem item, int i, int i2) {
        Intrinsics.checkNotNullParameter(item, "item");
        return PRELOAD_SIZE;
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter
    public void onViewScrollStateChanged(RecyclerView view, int i) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewScrollStateChanged(view, i);
        if (i == 0) {
            RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
            Intrinsics.checkNotNull(layoutManager);
            int childCount = layoutManager.getChildCount();
            int i2 = 0;
            while (i2 < childCount) {
                int i3 = i2 + 1;
                View childAt = layoutManager.getChildAt(i2);
                Intrinsics.checkNotNull(childAt);
                RecyclerView.ViewHolder childViewHolder = view.getChildViewHolder(childAt);
                if (childViewHolder instanceof IDeferrableViewHolder) {
                    ((IDeferrableViewHolder) childViewHolder).runDeferred();
                }
                i2 = i3;
            }
        }
    }

    @Override // com.miui.gallery.adapter.CheckableAdapter
    public View getCheckableView(View itemView) {
        Intrinsics.checkNotNullParameter(itemView, "itemView");
        CheckBox checkBox = ((MicroThumbGridItem) itemView).getCheckBox();
        Intrinsics.checkNotNullExpressionValue(checkBox, "itemView as MicroThumbGridItem).checkBox");
        return checkBox;
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter, com.miui.gallery.widget.recyclerview.FastScrollerCapsuleCalculator
    public FastScrollerCapsuleContentProvider<?> getCapsuleContent(int i) {
        if (this.fastScrollerTimeCapsuleModel == null) {
            this.fastScrollerTimeCapsuleModel = new FastScrollerTimeCapsuleModel();
        }
        IMediaSnapshot mo485getMediaItem = mo485getMediaItem(i);
        FastScrollerTimeCapsuleModel fastScrollerTimeCapsuleModel = this.fastScrollerTimeCapsuleModel;
        Intrinsics.checkNotNull(fastScrollerTimeCapsuleModel);
        fastScrollerTimeCapsuleModel.setSortTime(mo485getMediaItem.getSortTime());
        FastScrollerTimeCapsuleModel fastScrollerTimeCapsuleModel2 = this.fastScrollerTimeCapsuleModel;
        Intrinsics.checkNotNull(fastScrollerTimeCapsuleModel2);
        fastScrollerTimeCapsuleModel2.setLocation(TextUtils.isEmpty(mo485getMediaItem.getSourcePkg()) ? mo485getMediaItem.getLocation() : null);
        FastScrollerTimeCapsuleModel fastScrollerTimeCapsuleModel3 = this.fastScrollerTimeCapsuleModel;
        Intrinsics.checkNotNull(fastScrollerTimeCapsuleModel3);
        return fastScrollerTimeCapsuleModel3;
    }

    public final List<ProportionTagModel<Integer>> calculateTagProportionList(int i) {
        if (this.tagProportionModels == null) {
            this.tagProportionModels = new ArrayList();
        }
        List<ProportionTagModel<Integer>> list = this.tagProportionModels;
        Intrinsics.checkNotNull(list);
        list.clear();
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.time_line_header_height);
        int span = this.mViewMode.getSpan();
        SparseArray sparseArray = new SparseArray();
        float f = 0.1f;
        int groupCount = getGroupCount();
        int i2 = 0;
        while (i2 < groupCount) {
            int i3 = i2 + 1;
            int year = GalleryDateUtils.getYear(getItemSortTime(this.mClusterAdapter.getGroupStartPosition(i2)));
            float groupHeight = this.mClusterAdapter.getGroupHeight(i2, span, dimensionPixelSize, i);
            f += groupHeight;
            int indexOfKey = sparseArray.indexOfKey(year);
            if (indexOfKey >= 0) {
                sparseArray.setValueAt(indexOfKey, Float.valueOf(((Float) sparseArray.get(year)).floatValue() + groupHeight));
            } else {
                sparseArray.put(year, Float.valueOf(groupHeight));
            }
            i2 = i3;
        }
        int size = sparseArray.size();
        for (int i4 = 0; i4 < size; i4++) {
            MediaTimeProportionTagModel mediaTimeProportionTagModel = new MediaTimeProportionTagModel();
            mediaTimeProportionTagModel.setTag(Integer.valueOf(sparseArray.keyAt(i4)));
            mediaTimeProportionTagModel.setProportion(((Number) sparseArray.valueAt(i4)).floatValue() / f);
            List<ProportionTagModel<Integer>> list2 = this.tagProportionModels;
            Intrinsics.checkNotNull(list2);
            list2.add(mediaTimeProportionTagModel);
        }
        List<ProportionTagModel<Integer>> list3 = this.tagProportionModels;
        Intrinsics.checkNotNull(list3);
        CollectionsKt__MutableCollectionsJVMKt.sort(list3);
        List<ProportionTagModel<Integer>> list4 = this.tagProportionModels;
        Intrinsics.checkNotNull(list4);
        return list4;
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter, com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    /* renamed from: onCreateGroupViewHolder */
    public BaseViewHolder mo1338onCreateGroupViewHolder(ViewGroup parent, int i) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        ViewProvider viewProvider = this.viewProvider;
        View viewByType = viewProvider == null ? null : viewProvider.getViewByType(i);
        if (viewByType != null) {
            DefaultLogger.d("HomePageAdapter2", "onCreateGroupViewHolder with provider");
            return new BaseViewHolder(viewByType);
        }
        DefaultLogger.d("HomePageAdapter2", "onCreateGroupViewHolder with inflate");
        BaseViewHolder mo1338onCreateGroupViewHolder = super.mo1338onCreateGroupViewHolder(parent, i);
        Intrinsics.checkNotNullExpressionValue(mo1338onCreateGroupViewHolder, "super.onCreateGroupViewHolder(parent, viewType)");
        return mo1338onCreateGroupViewHolder;
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter, com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    /* renamed from: onCreateChildViewHolder */
    public AbsViewHolder mo1337onCreateChildViewHolder(ViewGroup parent, int i) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        ViewProvider viewProvider = this.viewProvider;
        View viewByType = viewProvider == null ? null : viewProvider.getViewByType(i);
        if (viewByType != null) {
            DefaultLogger.d("HomePageAdapter2", "onCreateViewHolder with provider");
            if (i == 3) {
                AbsMultiImageViewHolder createMultiImageViewHolder = createMultiImageViewHolder(viewByType, this.mLifecycle);
                Intrinsics.checkNotNullExpressionValue(createMultiImageViewHolder, "{\n                create…mLifecycle)\n            }");
                return createMultiImageViewHolder;
            }
            Lifecycle mLifecycle = this.mLifecycle;
            Intrinsics.checkNotNullExpressionValue(mLifecycle, "mLifecycle");
            return createSingleImageViewHolder(viewByType, mLifecycle);
        }
        DefaultLogger.d("HomePageAdapter2", "onCreateViewHolder with inflate");
        AbsViewHolder mo1337onCreateChildViewHolder = super.mo1337onCreateChildViewHolder(parent, i);
        Intrinsics.checkNotNullExpressionValue(mo1337onCreateChildViewHolder, "super.onCreateChildViewHolder(parent, viewType)");
        return mo1337onCreateChildViewHolder;
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public AbsSingleImageViewHolder createSingleImageViewHolder(View view, Lifecycle lifecycle) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(lifecycle, "lifecycle");
        return new SingleImageViewHolder(this, view, lifecycle);
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public GlideOptions getPreviewRequestOptions(int i, PictureViewMode pictureViewMode) {
        boolean z = true;
        IMediaSnapshot mo485getMediaItem = i >= 0 && i < getItemCount() ? mo485getMediaItem(i) : null;
        if ((mo485getMediaItem instanceof HomeMedia) && PictureViewMode.isDayMode(pictureViewMode)) {
            if (i < 0 || i >= getItemCount()) {
                z = false;
            }
            if (z) {
                return GlideOptions.pixelsThumbOf(((HomeMedia) mo485getMediaItem).getSize());
            }
            return GlideOptions.pixelsThumbOf();
        }
        return super.getPreviewRequestOptions(i, pictureViewMode);
    }

    @Override // com.miui.gallery.ui.FullyDrawnCounter
    public void arrive() {
        FullyDrawnCounterImpl fullyDrawnCounterImpl = this.fullyDrawnCounter;
        if (fullyDrawnCounterImpl == null) {
            return;
        }
        fullyDrawnCounterImpl.arrive();
    }

    /* compiled from: HomePageAdapter2.kt */
    /* loaded from: classes.dex */
    public final class SingleImageViewHolder extends AbsSingleImageViewHolder {
        public final /* synthetic */ HomePageAdapter2 this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SingleImageViewHolder(HomePageAdapter2 this$0, View view, Lifecycle lifecycle) {
            super(view, lifecycle);
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(lifecycle, "lifecycle");
            this.this$0 = this$0;
        }

        @Override // com.miui.gallery.widget.recyclerview.AbsViewHolder
        public void bindData(int i, int i2, List<? extends Object> payloads) {
            Intrinsics.checkNotNullParameter(payloads, "payloads");
            int packDataPosition = this.this$0.packDataPosition(i, i2);
            IMediaSnapshot mo485getMediaItem = this.this$0.mo485getMediaItem(packDataPosition);
            if (this.this$0.fullyDrawnCounter != null && (mo485getMediaItem instanceof HomeMedia) && this.mView.setFullyDrawnCounter(this.this$0)) {
                this.this$0.fullyDrawnCounter.count();
            }
            this.mView.bindImage(this.this$0.getItemId(packDataPosition), this.this$0.getBindImagePath(packDataPosition), this.this$0.getDownloadUri(packDataPosition), this.this$0.getRequestOptions(packDataPosition), this.this$0.getPreviewRequestOptions(packDataPosition), this.this$0.getOptimalThumbFilePath(packDataPosition, false));
            boolean z = mo485getMediaItem instanceof HomeMedia;
            if (z) {
                return;
            }
            if (!this.this$0.mViewMode.isAggregated()) {
                String mimeType = mo485getMediaItem.getMimeType();
                long duration = mo485getMediaItem.getDuration();
                long specialTypeFlags = mo485getMediaItem.getSpecialTypeFlags();
                boolean z2 = (mo485getMediaItem instanceof MediaGroup) && mo485getMediaItem.getBurstGroupKey() > 0 && ((MediaGroup) mo485getMediaItem).getMedias().size() > 1;
                boolean z3 = z2 && mo485getMediaItem.isTimeBurst();
                if (z2) {
                    specialTypeFlags |= z3 ? 8388608L : 64L;
                }
                this.mView.bindIndicator(mimeType, duration, specialTypeFlags);
                this.mView.bindFavoriteIndicator(mo485getMediaItem.isFavorite());
                int syncState = this.this$0.getSyncState(mo485getMediaItem);
                long id = mo485getMediaItem.getId();
                this.mView.bindSyncIndicator(id, syncState, this.this$0.mShowScene);
                if (z2) {
                    this.mView.setIsSimilarBestImage(MediaFeatureCacheManager.getInstance().shouldShowSelectionStar(id, false, false, this.this$0.getBurstItemKeys(packDataPosition)));
                } else {
                    this.mView.setIsSimilarBestImage(MediaFeatureCacheManager.getInstance().shouldShowSelectionStar(id, false, false));
                }
                this.mView.clearSimIndicatior();
            } else {
                this.mView.clearIndicator();
            }
            if (!z) {
                HomePageAdapter2 homePageAdapter2 = this.this$0;
                if (homePageAdapter2.mViewScrollState == 0) {
                    clearDeferred(homePageAdapter2.deferredBindings);
                    this.this$0.deferredBindings.accept(this, mo485getMediaItem);
                } else {
                    defer(mo485getMediaItem, homePageAdapter2.deferredBindings);
                }
            }
            Folme.useAt(this.mView).hover().setEffect(IHoverStyle.HoverEffect.NORMAL).handleHoverOf(this.mView, new AnimConfig[0]);
        }
    }

    /* compiled from: HomePageAdapter2.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final String getDefaultThumbFilePath(IMediaSnapshot media) {
            Intrinsics.checkNotNullParameter(media, "media");
            return BaseMediaAdapter.getMicroPath(media.getSmallSizeThumb(), media.getSha1());
        }

        public final KProperty1<IMediaSnapshot, Long> getSORT_TIME_PROPS() {
            return HomePageAdapter2.SORT_TIME_PROPS;
        }
    }
}
