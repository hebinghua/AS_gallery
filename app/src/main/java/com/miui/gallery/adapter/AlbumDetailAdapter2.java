package com.miui.gallery.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Size;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleOwnerKt;
import androidx.recyclerview.widget.RecyclerView;
import androidx.tracing.Trace;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.Config$SecretAlbumConfig;
import com.miui.gallery.Config$ShareAlbumConfig;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.FastScrollerTimeCapsuleModel;
import com.miui.gallery.adapter.itemmodel.MediaTimeProportionTagModel;
import com.miui.gallery.assistant.cache.MediaFeatureCacheManager;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.InternalContract$Cloud;
import com.miui.gallery.provider.cache.IMedia;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.provider.cache.MediaGroup;
import com.miui.gallery.ui.AlbumDetailGridItem;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.PackageUtils;
import com.miui.gallery.util.TalkBackUtil;
import com.miui.gallery.util.Utils;
import com.miui.gallery.widget.recyclerview.AbsSingleImageViewHolder;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsuleContentProvider;
import com.miui.gallery.widget.recyclerview.IDeferrableViewHolder;
import com.miui.gallery.widget.recyclerview.ProportionTagModel;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.Dispatchers;

/* compiled from: AlbumDetailAdapter2.kt */
/* loaded from: classes.dex */
public class AlbumDetailAdapter2 extends ListMultiViewMediaAdapter<IMedia> implements IAlbumAdapter, CheckableAdapter {
    public static final Uri ALL_PHOTOS_PICKER_URI;
    public static final String ALL_PHOTOS_SELECTION_LOCAL_MEDIA;
    public static final Uri ALL_PHOTOS_URI;
    public static final Companion Companion = new Companion(null);
    public static final String[] PROJECTION;
    public static final String[] PROJECTION_INTERNAL;
    public AlbumType albumType;
    public final BiConsumer<RecyclerView.ViewHolder, Object> deferredBindings;
    public SortBy lastSortBy;
    public FastScrollerTimeCapsuleModel mAlbumDetailScrollerTimeModel;
    public SortBy sortBy;
    public List<ProportionTagModel<Integer>> tagProportionModels;

    public static /* synthetic */ void $r8$lambda$5AqP_1X0c69xYhiFDSs9mZNkcuI(AlbumDetailAdapter2 albumDetailAdapter2, RecyclerView.ViewHolder viewHolder, Object obj) {
        m484deferredBindings$lambda0(albumDetailAdapter2, viewHolder, obj);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public boolean supportFoldBurstItems() {
        return true;
    }

    public final void bindContentDescription(View item, IMedia media) {
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(media, "media");
        try {
            Trace.beginSection("bindContentDescription");
            item.setContentDescription(TalkBackUtil.getContentDescriptionForImage(item.getContext(), media.getCreateTime(), media.getLocation(), media.getMimeType()));
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    public final SortBy getLastSortBy() {
        return this.lastSortBy;
    }

    public final void setLastSortBy(SortBy sortBy) {
        this.lastSortBy = sortBy;
    }

    /* renamed from: deferredBindings$lambda-0 */
    public static final void m484deferredBindings$lambda0(AlbumDetailAdapter2 this$0, RecyclerView.ViewHolder viewHolder, Object obj) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        View view = viewHolder.itemView;
        Intrinsics.checkNotNullExpressionValue(view, "vh.itemView");
        Objects.requireNonNull(obj, "null cannot be cast to non-null type com.miui.gallery.provider.cache.IMedia");
        this$0.bindContentDescription(view, (IMedia) obj);
    }

    @Override // com.miui.gallery.adapter.ListMultiViewMediaAdapter
    /* renamed from: getMediaItem */
    public IMedia mo485getMediaItem(int i) {
        IRecord item = getAdapterDelegate().getItem(i);
        Objects.requireNonNull(item, "null cannot be cast to non-null type com.miui.gallery.provider.cache.IMedia");
        return (IMedia) item;
    }

    @Override // com.miui.gallery.widget.recyclerview.AsyncListDiffer.ListListener
    public void onCurrentListChanged(List<? extends IRecord> previousList, List<? extends IRecord> currentList) {
        Intrinsics.checkNotNullParameter(previousList, "previousList");
        Intrinsics.checkNotNullParameter(currentList, "currentList");
        this.mClusterAdapter.setViewMode(this.mViewMode);
        this.mClusterAdapter.swapData(currentList);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AlbumDetailAdapter2(Context context, Lifecycle lifecycle) {
        super(context, SyncStateDisplay$DisplayScene.SCENE_IN_CHECK_MODE, lifecycle);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(lifecycle, "lifecycle");
        this.albumType = AlbumType.NORMAL;
        this.deferredBindings = new BiConsumer() { // from class: com.miui.gallery.adapter.AlbumDetailAdapter2$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AlbumDetailAdapter2.$r8$lambda$5AqP_1X0c69xYhiFDSs9mZNkcuI(AlbumDetailAdapter2.this, (RecyclerView.ViewHolder) obj, obj2);
            }
        };
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AlbumDetailAdapter2(Context context, SyncStateDisplay$DisplayScene scene, Lifecycle lifecycle) {
        super(context, scene, lifecycle);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(scene, "scene");
        Intrinsics.checkNotNullParameter(lifecycle, "lifecycle");
        this.albumType = AlbumType.NORMAL;
        this.deferredBindings = new BiConsumer() { // from class: com.miui.gallery.adapter.AlbumDetailAdapter2$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AlbumDetailAdapter2.$r8$lambda$5AqP_1X0c69xYhiFDSs9mZNkcuI(AlbumDetailAdapter2.this, (RecyclerView.ViewHolder) obj, obj2);
            }
        };
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public View createSingleImageView(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "viewGroup");
        if (isScreenshotAlbum() || isVideoAlbum()) {
            View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.album_detail_grid_item, viewGroup, false);
            Objects.requireNonNull(inflate, "null cannot be cast to non-null type com.miui.gallery.ui.AlbumDetailGridItem");
            return (AlbumDetailGridItem) inflate;
        }
        View inflate2 = LayoutInflater.from(this.mContext).inflate(R.layout.album_detail_grid_item, viewGroup, false);
        Objects.requireNonNull(inflate2, "null cannot be cast to non-null type com.miui.gallery.ui.AlbumDetailGridItem");
        return (AlbumDetailGridItem) inflate2;
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public AbsSingleImageViewHolder createSingleImageViewHolder(View view, Lifecycle lifecycle) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(lifecycle, "lifecycle");
        return new BaseSingleImageViewHolder(this, view, lifecycle);
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public long getItemSortTime(int i) {
        IMedia mo485getMediaItem = mo485getMediaItem(i);
        SortBy sortBy = this.sortBy;
        if (sortBy != null) {
            if (sortBy == SortBy.CREATE_DATE) {
                return mo485getMediaItem.getCreateTime();
            }
            if (sortBy == SortBy.UPDATE_DATE) {
                return mo485getMediaItem.getDateModified();
            }
        }
        return mo485getMediaItem.getSortTime();
    }

    public final Size getDisplayImageSize() {
        if (isScreenshotAlbum()) {
            Size size = Config$ThumbConfig.get().sMicroScreenshotTargetSize;
            Intrinsics.checkNotNullExpressionValue(size, "{\n            Config.Thu…nshotTargetSize\n        }");
            return size;
        } else if (isVideoAlbum()) {
            Size size2 = Config$ThumbConfig.get().sMicroVideoTargetSize;
            Intrinsics.checkNotNullExpressionValue(size2, "{\n            Config.Thu…VideoTargetSize\n        }");
            return size2;
        } else {
            Size size3 = Config$ThumbConfig.get().sMicroTargetSize;
            Intrinsics.checkNotNullExpressionValue(size3, "{\n            Config.Thu…MicroTargetSize\n        }");
            return size3;
        }
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter, com.miui.gallery.adapter.BaseMediaAdapter
    public Size getDisplayImageSize(int i) {
        return getDisplayImageSize();
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter, com.miui.gallery.adapter.BaseMediaAdapter
    public GlideOptions getRequestOptions(int i) {
        GlideOptions requestOptions = super.getRequestOptions(i);
        Intrinsics.checkNotNullExpressionValue(requestOptions, "super.getRequestOptions(position)");
        return (!isSecretAlbum() || i < 0 || i >= getItemCount()) ? requestOptions : buildSecretPhotoRequestOptions(requestOptions, mo485getMediaItem(i).getSecretKey());
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter, com.miui.gallery.adapter.BaseMediaAdapter
    public GlideOptions getPreviewRequestOptions(int i) {
        GlideOptions previewRequestOptions = super.getPreviewRequestOptions(i);
        if (previewRequestOptions == null) {
            return null;
        }
        return (!isSecretAlbum() || i < 0 || i >= getItemCount()) ? previewRequestOptions : buildSecretPhotoRequestOptions(previewRequestOptions, mo485getMediaItem(i).getSecretKey());
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter
    public void onViewScrollStateChanged(RecyclerView view, int i) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewScrollStateChanged(view, i);
        if (i != 0 || view.hasPendingAdapterUpdates()) {
            return;
        }
        int childCount = view.getChildCount();
        int i2 = 0;
        while (i2 < childCount) {
            int i3 = i2 + 1;
            RecyclerView.ViewHolder childViewHolder = view.getChildViewHolder(view.getChildAt(i2));
            if (childViewHolder instanceof IDeferrableViewHolder) {
                ((IDeferrableViewHolder) childViewHolder).runDeferred();
            }
            i2 = i3;
        }
    }

    public final GlideOptions buildSecretPhotoRequestOptions(GlideOptions glideOptions, byte[] bArr) {
        GlideOptions secretKey = glideOptions.secretKey(bArr);
        Intrinsics.checkNotNullExpressionValue(secretKey, "base.secretKey(secretKey)");
        return secretKey;
    }

    @Override // com.miui.gallery.adapter.ListMultiViewMediaAdapter, com.miui.gallery.adapter.MultiViewMediaAdapter
    public String getOptimalThumbFilePath(int i, boolean z) {
        IMedia mo485getMediaItem = mo485getMediaItem(i);
        if (isScreenshotAlbum() || isVideoAlbum() || z) {
            return BaseMediaAdapter.getMicroPath(mo485getMediaItem.getClearThumbnail(), mo485getMediaItem.getSha1());
        }
        return BaseMediaAdapter.getMicroPath(mo485getMediaItem.getSmallSizeThumb(), mo485getMediaItem.getSha1());
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public byte[] getItemSecretKey(int i) {
        if (isSecretAlbum()) {
            return mo485getMediaItem(i).getSecretKey();
        }
        return null;
    }

    @Override // com.miui.gallery.adapter.ListMultiViewMediaAdapter, com.miui.gallery.adapter.IMediaAdapter
    public String getLocation(int i) {
        IMedia mo485getMediaItem = mo485getMediaItem(i);
        String sourcePkg = mo485getMediaItem.getSourcePkg();
        if (!TextUtils.isEmpty(sourcePkg)) {
            Intrinsics.checkNotNull(sourcePkg);
            return PackageUtils.getAppNameByPackage(sourcePkg);
        }
        return mo485getMediaItem.getLocation();
    }

    @Override // com.miui.gallery.adapter.IAlbumAdapter
    public String getCreatorId(int i) {
        return mo485getMediaItem(i).getCreatorId();
    }

    @Override // com.miui.gallery.adapter.IAlbumAdapter
    public boolean isBabyAlbum() {
        AlbumType albumType = this.albumType;
        return albumType == AlbumType.BABY || albumType == AlbumType.OTHER_SHARE_BABY;
    }

    public boolean isSecretAlbum() {
        return this.albumType == AlbumType.SECRET;
    }

    public boolean isOtherShareAlbum() {
        AlbumType albumType = this.albumType;
        return albumType == AlbumType.OTHER_SHARE || albumType == AlbumType.OTHER_SHARE_BABY;
    }

    public boolean isScreenshotAlbum() {
        return this.albumType == AlbumType.SCREENSHOT;
    }

    public boolean isVideoAlbum() {
        return this.albumType == AlbumType.VIDEO;
    }

    @Override // com.miui.gallery.adapter.IAlbumAdapter
    public boolean isAllPhotosAlbum() {
        return this.albumType == AlbumType.ALL_PHOTOS;
    }

    public void setCurrentSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }

    @Override // com.miui.gallery.adapter.IAlbumAdapter
    public void setAlbumType(AlbumType albumType) {
        Intrinsics.checkNotNullParameter(albumType, "albumType");
        this.albumType = albumType;
    }

    @Override // com.miui.gallery.adapter.CheckableAdapter
    public View getCheckableView(View itemView) {
        Intrinsics.checkNotNullParameter(itemView, "itemView");
        CheckBox checkBox = ((AlbumDetailGridItem) itemView).getCheckBox();
        Intrinsics.checkNotNullExpressionValue(checkBox, "itemView as AlbumDetailGridItem).checkBox");
        return checkBox;
    }

    public final int getItemDate(int i) {
        IMedia mo485getMediaItem = mo485getMediaItem(i);
        SortBy sortBy = this.sortBy;
        if (sortBy != null) {
            if (sortBy == SortBy.CREATE_DATE) {
                return mo485getMediaItem.getCreateDate();
            }
            if (sortBy == SortBy.UPDATE_DATE) {
                return mo485getMediaItem.getModifyDate();
            }
        }
        return mo485getMediaItem.getSortDate();
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter, com.miui.gallery.widget.recyclerview.FastScrollerCapsuleCalculator
    public FastScrollerCapsuleContentProvider<?> getCapsuleContent(int i) {
        if (this.mAlbumDetailScrollerTimeModel == null) {
            this.mAlbumDetailScrollerTimeModel = new FastScrollerTimeCapsuleModel();
        }
        IMedia mo485getMediaItem = mo485getMediaItem(i);
        FastScrollerTimeCapsuleModel fastScrollerTimeCapsuleModel = this.mAlbumDetailScrollerTimeModel;
        Intrinsics.checkNotNull(fastScrollerTimeCapsuleModel);
        fastScrollerTimeCapsuleModel.setSortTime(getItemSortTime(i));
        FastScrollerTimeCapsuleModel fastScrollerTimeCapsuleModel2 = this.mAlbumDetailScrollerTimeModel;
        Intrinsics.checkNotNull(fastScrollerTimeCapsuleModel2);
        fastScrollerTimeCapsuleModel2.setLocation(TextUtils.isEmpty(mo485getMediaItem.getSourcePkg()) ? mo485getMediaItem.getLocation() : null);
        FastScrollerTimeCapsuleModel fastScrollerTimeCapsuleModel3 = this.mAlbumDetailScrollerTimeModel;
        Intrinsics.checkNotNull(fastScrollerTimeCapsuleModel3);
        return fastScrollerTimeCapsuleModel3;
    }

    public final int getYearFromAliasDate(int i) {
        if (i >= 10000000) {
            String substring = String.valueOf(i).substring(0, 4);
            Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
            return Utils.parseIntSafely(substring, 0);
        }
        return GalleryDateUtils.getRelativeOnlyYear(i);
    }

    public final float getGroupHeight(int i) {
        int i2;
        float microItemHeight = getMicroItemHeight();
        int childCount = getChildCount(i);
        int i3 = this.mSpanCount;
        if (childCount % i3 == 0) {
            i2 = childCount / i3;
        } else {
            i2 = (childCount / i3) + 1;
        }
        return getHeaderHeight(i) + (i2 * (microItemHeight + this.mSpacing));
    }

    @Override // com.miui.gallery.adapter.IAlbumAdapter
    public List<ProportionTagModel<Integer>> calculateTagProportionList() {
        List<ProportionTagModel<Integer>> calculateTagProportionListWithoutTimeLine;
        if (this.tagProportionModels == null) {
            this.tagProportionModels = new ArrayList();
        }
        List<ProportionTagModel<Integer>> list = this.tagProportionModels;
        Intrinsics.checkNotNull(list);
        list.clear();
        if (this.mShowTimeLine) {
            calculateTagProportionListWithoutTimeLine = calculateTagProportionListWithTimeLine();
        } else {
            calculateTagProportionListWithoutTimeLine = calculateTagProportionListWithoutTimeLine();
        }
        this.tagProportionModels = calculateTagProportionListWithoutTimeLine;
        Intrinsics.checkNotNull(calculateTagProportionListWithoutTimeLine);
        CollectionsKt__MutableCollectionsJVMKt.sort(calculateTagProportionListWithoutTimeLine);
        List<ProportionTagModel<Integer>> list2 = this.tagProportionModels;
        Intrinsics.checkNotNull(list2);
        return list2;
    }

    public final List<ProportionTagModel<Integer>> calculateTagProportionListWithoutTimeLine() {
        SparseArray sparseArray = new SparseArray();
        int itemCount = getItemCount();
        int i = 0;
        float f = 0.0f;
        int i2 = 0;
        int i3 = 0;
        boolean z = false;
        while (i2 < itemCount) {
            int i4 = i2 + 1;
            int yearFromAliasDate = getYearFromAliasDate(getItemDate(i2));
            if (i2 == 0) {
                i3 = yearFromAliasDate;
            }
            if (yearFromAliasDate != i3) {
                if (yearFromAliasDate > i3) {
                    z = true;
                }
                sparseArray.put(i3, Float.valueOf(f));
                f = 0.0f;
                i3 = yearFromAliasDate;
            } else {
                f += 1.0f;
            }
            if (i2 == itemCount - 1) {
                sparseArray.put(yearFromAliasDate, Float.valueOf(f));
            }
            i2 = i4;
        }
        int size = sparseArray.size();
        while (i < size) {
            int i5 = i + 1;
            MediaTimeProportionTagModel mediaTimeProportionTagModel = new MediaTimeProportionTagModel();
            mediaTimeProportionTagModel.setTag(Integer.valueOf(sparseArray.keyAt(i)));
            mediaTimeProportionTagModel.setIsAscOrder(z);
            mediaTimeProportionTagModel.setProportion(((Number) sparseArray.valueAt(i)).floatValue() / itemCount);
            List<ProportionTagModel<Integer>> list = this.tagProportionModels;
            Intrinsics.checkNotNull(list);
            list.add(mediaTimeProportionTagModel);
            i = i5;
        }
        return this.tagProportionModels;
    }

    public final List<ProportionTagModel<Integer>> calculateTagProportionListWithTimeLine() {
        SparseArray sparseArray = new SparseArray();
        int groupCount = getGroupCount();
        int i = 0;
        float f = 0.1f;
        int i2 = 0;
        int i3 = 0;
        boolean z = false;
        while (i2 < groupCount) {
            int i4 = i2 + 1;
            int yearFromAliasDate = getYearFromAliasDate(getItemDate(this.mClusterAdapter.getGroupStartPosition(i2)));
            if (i2 == 0) {
                i3 = yearFromAliasDate;
            }
            if (yearFromAliasDate > i3) {
                z = true;
            }
            float groupHeight = getGroupHeight(i2);
            f += groupHeight;
            int indexOfKey = sparseArray.indexOfKey(yearFromAliasDate);
            if (indexOfKey >= 0) {
                sparseArray.setValueAt(indexOfKey, Float.valueOf(((Float) sparseArray.get(yearFromAliasDate)).floatValue() + groupHeight));
            } else {
                sparseArray.put(yearFromAliasDate, Float.valueOf(groupHeight));
            }
            i2 = i4;
        }
        int size = sparseArray.size();
        while (i < size) {
            int i5 = i + 1;
            MediaTimeProportionTagModel mediaTimeProportionTagModel = new MediaTimeProportionTagModel();
            mediaTimeProportionTagModel.setTag(Integer.valueOf(sparseArray.keyAt(i)));
            mediaTimeProportionTagModel.setIsAscOrder(z);
            mediaTimeProportionTagModel.setProportion(((Number) sparseArray.valueAt(i)).floatValue() / f);
            List<ProportionTagModel<Integer>> list = this.tagProportionModels;
            Intrinsics.checkNotNull(list);
            list.add(mediaTimeProportionTagModel);
            i = i5;
        }
        return this.tagProportionModels;
    }

    @Override // com.miui.gallery.adapter.IAlbumAdapter
    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

    @Override // com.miui.gallery.adapter.IAlbumAdapter
    public void setViewMode(PictureViewMode viewMode, LifecycleOwner lifecycleOwner) {
        Intrinsics.checkNotNullParameter(viewMode, "viewMode");
        Intrinsics.checkNotNullParameter(lifecycleOwner, "lifecycleOwner");
        BuildersKt__Builders_commonKt.launch$default(LifecycleOwnerKt.getLifecycleScope(lifecycleOwner), Dispatchers.getMain().mo2585getImmediate(), null, new AlbumDetailAdapter2$setViewMode$1(this, viewMode, null), 2, null);
    }

    /* compiled from: AlbumDetailAdapter2.kt */
    /* loaded from: classes.dex */
    public final class BaseSingleImageViewHolder extends AbsSingleImageViewHolder {
        public final /* synthetic */ AlbumDetailAdapter2 this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public BaseSingleImageViewHolder(AlbumDetailAdapter2 this$0, View view, Lifecycle lifecycle) {
            super(view, lifecycle);
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(lifecycle, "lifecycle");
            this.this$0 = this$0;
        }

        @Override // com.miui.gallery.widget.recyclerview.AbsViewHolder
        public void bindData(int i, int i2, List<? extends Object> payloads) {
            Intrinsics.checkNotNullParameter(payloads, "payloads");
            int packDataPosition = this.this$0.packDataPosition(i, i2);
            MicroThumbGridItem microThumbGridItem = this.mView;
            Objects.requireNonNull(microThumbGridItem, "null cannot be cast to non-null type com.miui.gallery.ui.AlbumDetailGridItem");
            AlbumDetailGridItem albumDetailGridItem = (AlbumDetailGridItem) microThumbGridItem;
            albumDetailGridItem.bindImage(this.this$0.getBindImagePath(packDataPosition), this.this$0.getDownloadUri(packDataPosition), this.this$0.getRequestOptions(packDataPosition), this.this$0.getPreviewRequestOptions(packDataPosition));
            IMedia mo485getMediaItem = this.this$0.mo485getMediaItem(packDataPosition);
            boolean z = true;
            boolean z2 = (mo485getMediaItem instanceof MediaGroup) && mo485getMediaItem.getBurstGroupKey() > 0;
            boolean z3 = z2 && mo485getMediaItem.isTimeBurst();
            if (this.this$0.sortBy == SortBy.SIZE) {
                albumDetailGridItem.bindFileSize(mo485getMediaItem.getSize());
            } else {
                long specialTypeFlags = mo485getMediaItem.getSpecialTypeFlags();
                if (this.this$0.isSecretAlbum()) {
                    specialTypeFlags = Config$SecretAlbumConfig.getSupportedSpecialTypeFlags(specialTypeFlags);
                } else if (this.this$0.isOtherShareAlbum()) {
                    specialTypeFlags = Config$ShareAlbumConfig.getSupportedSpecialTypeFlags(specialTypeFlags);
                }
                if (z2) {
                    specialTypeFlags |= z3 ? 8388608L : 64L;
                }
                albumDetailGridItem.bindFileSize(0L);
                albumDetailGridItem.bindIndicator(mo485getMediaItem.getMimeType(), mo485getMediaItem.getDuration(), specialTypeFlags);
            }
            if (this.this$0.isSecretAlbum() || !mo485getMediaItem.isFavorite()) {
                z = false;
            }
            albumDetailGridItem.bindFavoriteIndicator(z);
            int syncState = this.this$0.getSyncState(mo485getMediaItem);
            long id = mo485getMediaItem.getId();
            AlbumDetailAdapter2 albumDetailAdapter2 = this.this$0;
            albumDetailGridItem.bindSyncIndicator(id, syncState, albumDetailAdapter2.mShowScene, albumDetailAdapter2.mSyncStateDisplayOptions);
            AlbumDetailAdapter2 albumDetailAdapter22 = this.this$0;
            if (albumDetailAdapter22.mViewScrollState == 0) {
                clearDeferred(albumDetailAdapter22.deferredBindings);
                this.this$0.deferredBindings.accept(this, mo485getMediaItem);
            } else {
                defer(mo485getMediaItem, albumDetailAdapter22.deferredBindings);
            }
            if (z2) {
                albumDetailGridItem.setIsSimilarBestImage(MediaFeatureCacheManager.getInstance().shouldShowSelectionStar(id, false, false, this.this$0.getBurstItemKeys(packDataPosition)));
            } else {
                albumDetailGridItem.setIsSimilarBestImage(MediaFeatureCacheManager.getInstance().shouldShowSelectionStar(id, false, false));
            }
            AlbumDetailAdapter2 albumDetailAdapter23 = this.this$0;
            if (albumDetailAdapter23.mGridItem == null) {
                albumDetailAdapter23.mGridItem = albumDetailGridItem;
            }
        }
    }

    /* compiled from: AlbumDetailAdapter2.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    static {
        String[] strArr = {j.c, "alias_micro_thumbnail", "localFile", "title", "alias_create_date", "alias_create_time", "location", "sha1", "serverType", "duration", "mimeType", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "alias_sync_state", "secretKey", "thumbnailFile", "localFile", "creatorId", "alias_sort_time", "alias_clear_thumbnail", "alias_is_favorite", "specialTypeFlags", "burst_group_id", "exifImageWidth", "exifImageLength", "dateModified", "is_time_burst", "burst_index", "alias_modify_date", "alias_sort_date", "sourcePackage"};
        PROJECTION_INTERNAL = strArr;
        String[] copyStringArray = MiscUtil.copyStringArray(strArr);
        Intrinsics.checkNotNullExpressionValue(copyStringArray, "copyStringArray(PROJECTION_INTERNAL)");
        PROJECTION = copyStringArray;
        Uri uri = GalleryContract.Media.URI;
        Uri build = uri.buildUpon().appendQueryParameter("remove_duplicate_items", "false").appendQueryParameter("remove_rubbish_items", "true").appendQueryParameter("extra_timeline_only_show_valid_location", "false").build();
        Intrinsics.checkNotNullExpressionValue(build, "URI.buildUpon()\n        …   )\n            .build()");
        ALL_PHOTOS_URI = build;
        Uri build2 = uri.buildUpon().appendQueryParameter("remove_duplicate_items", "false").appendQueryParameter("remove_rubbish_items", "true").appendQueryParameter("extra_timeline_only_show_valid_location", "false").appendQueryParameter("remove_processing_items", "true").build();
        Intrinsics.checkNotNullExpressionValue(build2, "URI.buildUpon()\n        …   )\n            .build()");
        ALL_PHOTOS_PICKER_URI = build2;
        ALL_PHOTOS_SELECTION_LOCAL_MEDIA = Intrinsics.stringPlus("sha1 NOT NULL AND alias_hidden = 0 AND alias_rubbish = 0 AND localGroupId != -1000 AND ", InternalContract$Cloud.ALIAS_LOCAL_MEDIA);
    }
}
