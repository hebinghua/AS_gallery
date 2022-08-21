package com.miui.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Size;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
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
import com.miui.gallery.ui.AlbumDetailGridItem;
import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.util.BurstFilterCursor;
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
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes.dex */
public class AlbumDetailAdapter extends CursorMultiViewMediaAdapter implements IAlbumAdapter, CheckableAdapter {
    public static Uri ALL_PHOTOS_PICKER_URI;
    public static final String ALL_PHOTOS_SELECTION_LOCAL_MEDIA;
    public static Uri ALL_PHOTOS_URI;
    public static final String[] PROJECTION;
    public static final String[] PROJECTION_INTERNAL;
    public FastScrollerTimeCapsuleModel mAlbumDetailScrollerTimeModel;
    public AlbumType mAlbumType;
    public final BiConsumer<RecyclerView.ViewHolder, Object> mDeferredBindings;
    public SortBy mSortBy;
    public List<ProportionTagModel<Integer>> mTagProportionModels;

    public static /* synthetic */ void $r8$lambda$VxS7bwaV5iuHqy5rbPiXwdGhou4(AlbumDetailAdapter albumDetailAdapter, RecyclerView.ViewHolder viewHolder, Object obj) {
        albumDetailAdapter.lambda$new$0(viewHolder, obj);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public boolean supportFoldBurstItems() {
        return true;
    }

    static {
        String[] strArr = {j.c, "alias_micro_thumbnail", "localFile", "title", "alias_create_date", "alias_create_time", "location", "sha1", "serverType", "duration", "mimeType", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "alias_sync_state", "secretKey", "thumbnailFile", "localFile", "creatorId", "alias_sort_time", "alias_clear_thumbnail", "alias_is_favorite", "specialTypeFlags", "burst_group_id", "exifImageWidth", "exifImageLength", "dateModified", "is_time_burst", "burst_index", "alias_modify_date", "alias_sort_date", "sourcePackage"};
        PROJECTION_INTERNAL = strArr;
        PROJECTION = MiscUtil.copyStringArray(strArr);
        Uri uri = GalleryContract.Media.URI;
        ALL_PHOTOS_URI = uri.buildUpon().appendQueryParameter("remove_duplicate_items", String.valueOf(false)).appendQueryParameter("remove_rubbish_items", String.valueOf(true)).appendQueryParameter("extra_timeline_only_show_valid_location", String.valueOf(false)).build();
        ALL_PHOTOS_PICKER_URI = uri.buildUpon().appendQueryParameter("remove_duplicate_items", String.valueOf(false)).appendQueryParameter("remove_rubbish_items", String.valueOf(true)).appendQueryParameter("extra_timeline_only_show_valid_location", String.valueOf(false)).appendQueryParameter("remove_processing_items", String.valueOf(true)).build();
        ALL_PHOTOS_SELECTION_LOCAL_MEDIA = "sha1 NOT NULL AND alias_hidden = 0 AND alias_rubbish = 0 AND localGroupId != -1000 AND " + InternalContract$Cloud.ALIAS_LOCAL_MEDIA;
    }

    public /* synthetic */ void lambda$new$0(RecyclerView.ViewHolder viewHolder, Object obj) {
        bindContentDescription(viewHolder.itemView, ((Integer) obj).intValue());
    }

    public AlbumDetailAdapter(Context context, Lifecycle lifecycle) {
        super(context, SyncStateDisplay$DisplayScene.SCENE_IN_CHECK_MODE, lifecycle);
        this.mAlbumType = AlbumType.NORMAL;
        this.mDeferredBindings = new BiConsumer() { // from class: com.miui.gallery.adapter.AlbumDetailAdapter$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AlbumDetailAdapter.$r8$lambda$VxS7bwaV5iuHqy5rbPiXwdGhou4(AlbumDetailAdapter.this, (RecyclerView.ViewHolder) obj, obj2);
            }
        };
    }

    public AlbumDetailAdapter(Context context, SyncStateDisplay$DisplayScene syncStateDisplay$DisplayScene, Lifecycle lifecycle) {
        super(context, syncStateDisplay$DisplayScene, lifecycle);
        this.mAlbumType = AlbumType.NORMAL;
        this.mDeferredBindings = new BiConsumer() { // from class: com.miui.gallery.adapter.AlbumDetailAdapter$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AlbumDetailAdapter.$r8$lambda$VxS7bwaV5iuHqy5rbPiXwdGhou4(AlbumDetailAdapter.this, (RecyclerView.ViewHolder) obj, obj2);
            }
        };
    }

    public AlbumDetailAdapter(Context context, SyncStateDisplay$DisplayScene syncStateDisplay$DisplayScene, int i, Lifecycle lifecycle) {
        super(context, syncStateDisplay$DisplayScene, i, lifecycle);
        this.mAlbumType = AlbumType.NORMAL;
        this.mDeferredBindings = new BiConsumer() { // from class: com.miui.gallery.adapter.AlbumDetailAdapter$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AlbumDetailAdapter.$r8$lambda$VxS7bwaV5iuHqy5rbPiXwdGhou4(AlbumDetailAdapter.this, (RecyclerView.ViewHolder) obj, obj2);
            }
        };
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public View createSingleImageView(ViewGroup viewGroup) {
        return (AlbumDetailGridItem) LayoutInflater.from(this.mContext).inflate(R.layout.album_detail_grid_item, viewGroup, false);
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public AbsSingleImageViewHolder createSingleImageViewHolder(View view, Lifecycle lifecycle) {
        return new BaseSingleImageViewHolder(view, lifecycle);
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public long getItemSortTime(int i) {
        return mo1558getItem(i).getLong(getTimeIndex());
    }

    public Size getDisplayImageSize() {
        if (isScreenshotAlbum()) {
            return Config$ThumbConfig.get().sMicroScreenshotTargetSize;
        }
        if (isVideoAlbum()) {
            return Config$ThumbConfig.get().sMicroVideoTargetSize;
        }
        return Config$ThumbConfig.get().sMicroTargetSize;
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter, com.miui.gallery.adapter.BaseMediaAdapter
    public Size getDisplayImageSize(int i) {
        return getDisplayImageSize();
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter, com.miui.gallery.adapter.BaseMediaAdapter
    public GlideOptions getRequestOptions(int i) {
        Cursor mo1558getItem;
        GlideOptions requestOptions = super.getRequestOptions(i);
        return (!isSecretAlbum() || (mo1558getItem = mo1558getItem(i)) == null) ? requestOptions : buildSecretPhotoRequestOptions(requestOptions, mo1558getItem.getBlob(13));
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter, com.miui.gallery.adapter.BaseMediaAdapter
    public GlideOptions getPreviewRequestOptions(int i) {
        Cursor mo1558getItem;
        GlideOptions previewRequestOptions = super.getPreviewRequestOptions(i);
        if (previewRequestOptions == null) {
            return null;
        }
        return (!isSecretAlbum() || i < 0 || i >= getItemCount() || (mo1558getItem = mo1558getItem(i)) == null) ? previewRequestOptions : buildSecretPhotoRequestOptions(previewRequestOptions, mo1558getItem.getBlob(13));
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter
    public void onViewScrollStateChanged(RecyclerView recyclerView, int i) {
        super.onViewScrollStateChanged(recyclerView, i);
        if (i != 0 || recyclerView.hasPendingAdapterUpdates()) {
            return;
        }
        int childCount = recyclerView.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = recyclerView.getChildAt(i2);
            recyclerView.getChildAdapterPosition(childAt);
            RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(childAt);
            if (childViewHolder instanceof IDeferrableViewHolder) {
                ((IDeferrableViewHolder) childViewHolder).runDeferred();
            }
        }
    }

    public void bindContentDescription(View view, int i) {
        Cursor mo1558getItem = mo1558getItem(i);
        view.setContentDescription(TalkBackUtil.getContentDescriptionForImage(view.getContext(), mo1558getItem.getLong(5), mo1558getItem.getString(6), mo1558getItem.getString(10)));
    }

    public int getSyncState(Cursor cursor) {
        return getSyncStateInternal(cursor.getInt(12));
    }

    public final GlideOptions buildSecretPhotoRequestOptions(GlideOptions glideOptions, byte[] bArr) {
        return glideOptions.secretKey(bArr);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public Uri getDownloadUri(int i) {
        return BaseMediaAdapter.getDownloadUri(mo1558getItem(i), 12, 0);
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public String getOptimalThumbFilePath(int i, boolean z) {
        if (isScreenshotAlbum() || isVideoAlbum()) {
            return BaseMediaAdapter.getMicroPath(mo1558getItem(i), 18, 7);
        }
        return BaseMediaAdapter.getMicroPath(mo1558getItem(i), 1, 7);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMicroThumbFilePath(int i) {
        return BaseMediaAdapter.getMicroPath(mo1558getItem(i), 1, 7);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getOriginFilePath(int i) {
        return mo1558getItem(i).getString(15);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getThumbFilePath(int i) {
        return mo1558getItem(i).getString(14);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getItemKey(int i) {
        return mo1558getItem(i).getLong(0);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getSha1(int i) {
        return mo1558getItem(i).getString(7);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public byte[] getItemSecretKey(int i) {
        if (isSecretAlbum()) {
            return mo1558getItem(i).getBlob(13);
        }
        return super.getItemSecretKey(i);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMimeType(int i) {
        return mo1558getItem(i).getString(10);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getCreateTime(int i) {
        return mo1558getItem(i).getLong(5);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getLocation(int i) {
        Cursor mo1558getItem = mo1558getItem(i);
        String string = mo1558getItem.getString(29);
        if (!TextUtils.isEmpty(string)) {
            return PackageUtils.getAppNameByPackage(string);
        }
        return mo1558getItem.getString(6);
    }

    public boolean isFavorite(int i) {
        return mo1558getItem(i).getInt(19) > 0;
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getFileLength(int i) {
        return mo1558getItem(i).getLong(11);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public int getImageWidth(int i) {
        return mo1558getItem(i).getInt(22);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public int getImageHeight(int i) {
        return mo1558getItem(i).getInt(23);
    }

    @Override // com.miui.gallery.adapter.IAlbumAdapter
    public String getCreatorId(int i) {
        return mo1558getItem(i).getString(16);
    }

    public void setCurrentSortBy(SortBy sortBy) {
        this.mSortBy = sortBy;
    }

    public SortBy getCurrentSortBy() {
        return this.mSortBy;
    }

    @Override // com.miui.gallery.adapter.IAlbumAdapter
    public boolean isBabyAlbum() {
        AlbumType albumType = this.mAlbumType;
        return albumType == AlbumType.BABY || albumType == AlbumType.OTHER_SHARE_BABY;
    }

    public boolean isSecretAlbum() {
        return this.mAlbumType == AlbumType.SECRET;
    }

    public boolean isOtherShareAlbum() {
        AlbumType albumType = this.mAlbumType;
        return albumType == AlbumType.OTHER_SHARE || albumType == AlbumType.OTHER_SHARE_BABY;
    }

    public boolean isScreenshotAlbum() {
        return this.mAlbumType == AlbumType.SCREENSHOT;
    }

    public boolean isVideoAlbum() {
        return this.mAlbumType == AlbumType.VIDEO;
    }

    @Override // com.miui.gallery.adapter.IAlbumAdapter
    public boolean isAllPhotosAlbum() {
        return this.mAlbumType == AlbumType.ALL_PHOTOS;
    }

    @Override // com.miui.gallery.adapter.IAlbumAdapter
    public void setAlbumType(AlbumType albumType) {
        this.mAlbumType = albumType;
    }

    @Override // com.miui.gallery.adapter.CheckableAdapter
    public View getCheckableView(View view) {
        return ((AlbumDetailGridItem) view).getCheckBox();
    }

    @Override // com.miui.gallery.adapter.IAlbumAdapter
    public ArrayList<Long> getBurstItemKeys(int i) {
        if (supportFoldBurstItems() && getCursor() != null) {
            return ((BurstFilterCursor) getCursor()).getBurstIdsInGroup(i, 0);
        }
        ArrayList<Long> arrayList = new ArrayList<>(1);
        arrayList.add(Long.valueOf(getItemKey(i)));
        return arrayList;
    }

    public int getTimeIndex() {
        SortBy currentSortBy = getCurrentSortBy();
        if (currentSortBy != null) {
            if (currentSortBy == SortBy.CREATE_DATE) {
                return 5;
            }
            return currentSortBy == SortBy.UPDATE_DATE ? 24 : 17;
        }
        return 17;
    }

    public int getDateIndex() {
        SortBy currentSortBy = getCurrentSortBy();
        if (currentSortBy != null) {
            if (currentSortBy == SortBy.CREATE_DATE) {
                return 4;
            }
            return currentSortBy == SortBy.UPDATE_DATE ? 27 : 28;
        }
        return 28;
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter, com.miui.gallery.widget.recyclerview.FastScrollerCapsuleCalculator
    public FastScrollerCapsuleContentProvider getCapsuleContent(int i) {
        if (this.mAlbumDetailScrollerTimeModel == null) {
            this.mAlbumDetailScrollerTimeModel = new FastScrollerTimeCapsuleModel();
        }
        Cursor mo1558getItem = mo1558getItem(i);
        if (mo1558getItem != null && mo1558getItem.getCount() > 0) {
            this.mAlbumDetailScrollerTimeModel.setSortTime(mo1558getItem.getLong(getTimeIndex()));
            this.mAlbumDetailScrollerTimeModel.setLocation(mo1558getItem.getString(6));
        }
        return this.mAlbumDetailScrollerTimeModel;
    }

    public int getYearFromAliasDate(int i) {
        if (i >= 10000000) {
            return Utils.parseIntSafely(String.valueOf(i).substring(0, 4), 0);
        }
        return GalleryDateUtils.getRelativeOnlyYear(i);
    }

    public float getGroupHeight(int i) {
        float microItemHeight = getMicroItemHeight();
        int childCount = getChildCount(i);
        int i2 = this.mSpanCount;
        int i3 = childCount % i2;
        int i4 = childCount / i2;
        if (i3 != 0) {
            i4++;
        }
        return getHeaderHeight(i) + (i4 * (microItemHeight + this.mSpacing));
    }

    @Override // com.miui.gallery.adapter.IAlbumAdapter
    public List<ProportionTagModel<Integer>> calculateTagProportionList() {
        if (this.mTagProportionModels == null) {
            this.mTagProportionModels = new ArrayList();
        }
        this.mTagProportionModels.clear();
        if (this.mShowTimeLine) {
            this.mTagProportionModels = calculateTagProportionListWithTimeLine();
        } else {
            this.mTagProportionModels = calculateTagProportionListWithoutTimeLine();
        }
        Collections.sort(this.mTagProportionModels);
        return this.mTagProportionModels;
    }

    public final List<ProportionTagModel<Integer>> calculateTagProportionListWithoutTimeLine() {
        SparseArray sparseArray = new SparseArray();
        int itemCount = getItemCount();
        float f = 0.0f;
        int i = 0;
        boolean z = false;
        for (int i2 = 0; i2 < itemCount; i2++) {
            int yearFromAliasDate = getYearFromAliasDate(mo1558getItem(i2).getInt(getDateIndex()));
            if (i2 == 0) {
                i = yearFromAliasDate;
            }
            if (yearFromAliasDate != i) {
                if (yearFromAliasDate > i) {
                    z = true;
                }
                sparseArray.put(i, Float.valueOf(f));
                f = 0.0f;
                i = yearFromAliasDate;
            } else {
                f += 1.0f;
            }
            if (i2 == itemCount - 1) {
                sparseArray.put(yearFromAliasDate, Float.valueOf(f));
            }
        }
        for (int i3 = 0; i3 < sparseArray.size(); i3++) {
            MediaTimeProportionTagModel mediaTimeProportionTagModel = new MediaTimeProportionTagModel();
            mediaTimeProportionTagModel.setTag(Integer.valueOf(sparseArray.keyAt(i3)));
            mediaTimeProportionTagModel.setIsAscOrder(z);
            mediaTimeProportionTagModel.setProportion(((Float) sparseArray.valueAt(i3)).floatValue() / itemCount);
            this.mTagProportionModels.add(mediaTimeProportionTagModel);
        }
        return this.mTagProportionModels;
    }

    public final List<ProportionTagModel<Integer>> calculateTagProportionListWithTimeLine() {
        SparseArray sparseArray = new SparseArray();
        float f = 0.1f;
        int i = 0;
        boolean z = false;
        for (int i2 = 0; i2 < getGroupCount(); i2++) {
            int yearFromAliasDate = getYearFromAliasDate(mo1558getItem(this.mClusterAdapter.getGroupStartPosition(i2)).getInt(getDateIndex()));
            if (i2 == 0) {
                i = yearFromAliasDate;
            }
            if (yearFromAliasDate > i) {
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
        }
        for (int i3 = 0; i3 < sparseArray.size(); i3++) {
            MediaTimeProportionTagModel mediaTimeProportionTagModel = new MediaTimeProportionTagModel();
            mediaTimeProportionTagModel.setTag(Integer.valueOf(sparseArray.keyAt(i3)));
            mediaTimeProportionTagModel.setIsAscOrder(z);
            mediaTimeProportionTagModel.setProportion(((Float) sparseArray.valueAt(i3)).floatValue() / f);
            this.mTagProportionModels.add(mediaTimeProportionTagModel);
        }
        return this.mTagProportionModels;
    }

    @Override // com.miui.gallery.adapter.IAlbumAdapter
    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

    @Override // com.miui.gallery.adapter.IAlbumAdapter
    public void setViewMode(PictureViewMode pictureViewMode, LifecycleOwner lifecycleOwner) {
        setPictureViewMode(pictureViewMode);
        notifyDataChanged();
    }

    /* loaded from: classes.dex */
    public class BaseSingleImageViewHolder extends AbsSingleImageViewHolder {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public BaseSingleImageViewHolder(View view, Lifecycle lifecycle) {
            super(view, lifecycle);
            AlbumDetailAdapter.this = r1;
        }

        @Override // com.miui.gallery.widget.recyclerview.AbsViewHolder
        public void bindData(int i, int i2, List<Object> list) {
            int packDataPosition = AlbumDetailAdapter.this.packDataPosition(i, i2);
            this.mView.setTag(R.id.tag_item_unique_id, Long.valueOf(AlbumDetailAdapter.this.getItemKey(packDataPosition)));
            AlbumDetailGridItem albumDetailGridItem = (AlbumDetailGridItem) this.mView;
            albumDetailGridItem.bindImage(AlbumDetailAdapter.this.getBindImagePath(packDataPosition), AlbumDetailAdapter.this.getDownloadUri(packDataPosition), AlbumDetailAdapter.this.getRequestOptions(packDataPosition), AlbumDetailAdapter.this.getPreviewRequestOptions(packDataPosition));
            Cursor mo1558getItem = AlbumDetailAdapter.this.mo1558getItem(packDataPosition);
            String string = mo1558getItem.getString(10);
            long j = mo1558getItem.getLong(9);
            boolean z = (mo1558getItem instanceof BurstFilterCursor) && ((BurstFilterCursor) mo1558getItem).isBurstPosition(packDataPosition);
            boolean z2 = z && ((BurstFilterCursor) mo1558getItem).isTimeBurstPosition(packDataPosition);
            if (AlbumDetailAdapter.this.mSortBy == SortBy.SIZE) {
                albumDetailGridItem.bindFileSize(mo1558getItem.getLong(11));
            } else {
                long j2 = mo1558getItem.getLong(20);
                if (AlbumDetailAdapter.this.isSecretAlbum()) {
                    j2 = Config$SecretAlbumConfig.getSupportedSpecialTypeFlags(j2);
                } else if (AlbumDetailAdapter.this.isOtherShareAlbum()) {
                    j2 = Config$ShareAlbumConfig.getSupportedSpecialTypeFlags(j2);
                }
                if (z) {
                    j2 |= z2 ? 8388608L : 64L;
                }
                albumDetailGridItem.bindFileSize(0L);
                albumDetailGridItem.bindIndicator(string, j, j2);
            }
            albumDetailGridItem.bindFavoriteIndicator(!AlbumDetailAdapter.this.isSecretAlbum() && AlbumDetailAdapter.this.isFavorite(mo1558getItem.getPosition()));
            int syncState = AlbumDetailAdapter.this.getSyncState(mo1558getItem);
            long j3 = mo1558getItem.getLong(0);
            AlbumDetailAdapter albumDetailAdapter = AlbumDetailAdapter.this;
            albumDetailGridItem.bindSyncIndicator(j3, syncState, albumDetailAdapter.mShowScene, albumDetailAdapter.mSyncStateDisplayOptions);
            AlbumDetailAdapter albumDetailAdapter2 = AlbumDetailAdapter.this;
            if (albumDetailAdapter2.mViewScrollState == 0) {
                clearDeferred(albumDetailAdapter2.mDeferredBindings);
                AlbumDetailAdapter.this.mDeferredBindings.accept(this, Integer.valueOf(packDataPosition));
            } else {
                defer(Integer.valueOf(packDataPosition), AlbumDetailAdapter.this.mDeferredBindings);
            }
            if (z) {
                albumDetailGridItem.setIsSimilarBestImage(MediaFeatureCacheManager.getInstance().shouldShowSelectionStar(j3, false, false, AlbumDetailAdapter.this.getBurstItemKeys(packDataPosition)));
            } else {
                albumDetailGridItem.setIsSimilarBestImage(MediaFeatureCacheManager.getInstance().shouldShowSelectionStar(j3, false, false));
            }
            AlbumDetailAdapter albumDetailAdapter3 = AlbumDetailAdapter.this;
            if (albumDetailAdapter3.mGridItem == null) {
                albumDetailAdapter3.mGridItem = albumDetailGridItem;
            }
            Folme.useAt(this.mView).hover().setEffect(IHoverStyle.HoverEffect.NORMAL).handleHoverOf(this.mView, new AnimConfig[0]);
        }
    }
}
