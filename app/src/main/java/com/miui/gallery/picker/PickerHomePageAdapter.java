package com.miui.gallery.picker;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.SparseArray;
import android.view.View;
import androidx.lifecycle.Lifecycle;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.R;
import com.miui.gallery.adapter.BaseMediaAdapter;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.adapter.CursorMultiViewMediaAdapter;
import com.miui.gallery.adapter.SyncStateDisplay$DisplayScene;
import com.miui.gallery.adapter.itemmodel.FastScrollerTimeCapsuleModel;
import com.miui.gallery.adapter.itemmodel.MediaTimeProportionTagModel;
import com.miui.gallery.assistant.cache.MediaFeatureCacheManager;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.util.BurstFilterCursor;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.TalkBackUtil;
import com.miui.gallery.widget.recyclerview.AbsSingleImageViewHolder;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsuleContentProvider;
import com.miui.gallery.widget.recyclerview.ProportionTagModel;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes2.dex */
public class PickerHomePageAdapter extends CursorMultiViewMediaAdapter implements CheckableAdapter {
    public static final String[] PROJECTION = {j.c, "alias_micro_thumbnail", "alias_create_date", "alias_create_time", "location", "sha1", "serverType", "duration", "mimeType", "alias_sync_state", "thumbnailFile", "localFile", "alias_clear_thumbnail", "alias_is_favorite", "specialTypeFlags", "alias_sort_time", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "title", "burst_group_id", "exifImageWidth", "exifImageLength", "is_time_burst", "burst_index"};
    public int mHeaderHeight;
    public FastScrollerTimeCapsuleModel mScrollerTimeModel;
    public int mSpacing;
    public int mSpanCount;
    public List<ProportionTagModel<Integer>> mTagProportionModels;

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public boolean supportFoldBurstItems() {
        return true;
    }

    public PickerHomePageAdapter(Context context, SyncStateDisplay$DisplayScene syncStateDisplay$DisplayScene, Lifecycle lifecycle) {
        super(context, syncStateDisplay$DisplayScene, lifecycle);
        this.mScrollerTimeModel = null;
        init();
    }

    public final void init() {
        this.mHeaderHeight = this.mContext.getResources().getDimensionPixelOffset(R.dimen.time_line_header_height);
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter
    public void setSpanCount(int i) {
        this.mSpanCount = i;
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter
    public void setSpacing(int i) {
        this.mSpacing = i;
    }

    public final int getSyncState(Cursor cursor) {
        return getSyncStateInternal(cursor.getInt(9));
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public AbsSingleImageViewHolder createSingleImageViewHolder(View view, Lifecycle lifecycle) {
        return new SingleImageViewHolder(view, lifecycle);
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public String getOptimalThumbFilePath(int i, boolean z) {
        return BaseMediaAdapter.getMicroPath(mo1558getItem(i), 1, 5);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMicroThumbFilePath(int i) {
        return BaseMediaAdapter.getMicroPath(mo1558getItem(i), 1, 5);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getOriginFilePath(int i) {
        return mo1558getItem(i).getString(11);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getThumbFilePath(int i) {
        return mo1558getItem(i).getString(10);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public Uri getDownloadUri(int i) {
        return BaseMediaAdapter.getDownloadUri(mo1558getItem(i), 9, 0);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getItemKey(int i) {
        return mo1558getItem(i).getLong(0);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getSha1(int i) {
        return mo1558getItem(i).getString(5);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMimeType(int i) {
        return mo1558getItem(i).getString(8);
    }

    public boolean isFavorite(int i) {
        return mo1558getItem(i).getInt(13) > 0;
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getFileLength(int i) {
        return mo1558getItem(i).getLong(16);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getCreateTime(int i) {
        return mo1558getItem(i).getLong(3);
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public long getItemSortTime(int i) {
        return mo1558getItem(i).getLong(15);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getLocation(int i) {
        return mo1558getItem(i).getString(4);
    }

    @Override // com.miui.gallery.adapter.CheckableAdapter
    public View getCheckableView(View view) {
        return ((MicroThumbGridItem) view).getCheckBox();
    }

    public ArrayList<Long> getBurstItemKeys(int i) {
        if (getCursor() != null) {
            return ((BurstFilterCursor) getCursor()).getBurstIdsInGroup(i, 0);
        }
        return null;
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter, com.miui.gallery.widget.recyclerview.FastScrollerCapsuleCalculator
    public FastScrollerCapsuleContentProvider getCapsuleContent(int i) {
        if (this.mScrollerTimeModel == null) {
            this.mScrollerTimeModel = new FastScrollerTimeCapsuleModel();
        }
        Cursor mo1558getItem = mo1558getItem(i);
        if (mo1558getItem != null && mo1558getItem.getCount() > 0) {
            this.mScrollerTimeModel.setSortTime(mo1558getItem.getLong(15));
            this.mScrollerTimeModel.setLocation(mo1558getItem.getString(4));
        }
        return this.mScrollerTimeModel;
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
        int i5 = this.mHeaderHeight;
        int i6 = this.mSpacing;
        return i5 + i6 + (i4 * (microItemHeight + i6));
    }

    public List<ProportionTagModel<Integer>> calculateTagProportionList() {
        if (this.mTagProportionModels == null) {
            this.mTagProportionModels = new ArrayList();
        }
        this.mTagProportionModels.clear();
        SparseArray sparseArray = new SparseArray();
        float f = 0.1f;
        int i = 0;
        boolean z = false;
        for (int i2 = 0; i2 < getGroupCount(); i2++) {
            int year = GalleryDateUtils.getYear(mo1558getItem(this.mClusterAdapter.getGroupStartPosition(i2)).getLong(15));
            if (i2 == 0) {
                i = year;
            }
            if (year > i) {
                z = true;
            }
            float groupHeight = getGroupHeight(i2);
            f += groupHeight;
            int indexOfKey = sparseArray.indexOfKey(year);
            if (indexOfKey >= 0) {
                sparseArray.setValueAt(indexOfKey, Float.valueOf(((Float) sparseArray.get(year)).floatValue() + groupHeight));
            } else {
                sparseArray.put(year, Float.valueOf(groupHeight));
            }
        }
        for (int i3 = 0; i3 < sparseArray.size(); i3++) {
            MediaTimeProportionTagModel mediaTimeProportionTagModel = new MediaTimeProportionTagModel();
            mediaTimeProportionTagModel.setTag(Integer.valueOf(sparseArray.keyAt(i3)));
            mediaTimeProportionTagModel.setIsAscOrder(z);
            mediaTimeProportionTagModel.setProportion(((Float) sparseArray.valueAt(i3)).floatValue() / f);
            this.mTagProportionModels.add(mediaTimeProportionTagModel);
        }
        Collections.sort(this.mTagProportionModels);
        return this.mTagProportionModels;
    }

    /* loaded from: classes2.dex */
    public class SingleImageViewHolder extends AbsSingleImageViewHolder {
        public SingleImageViewHolder(View view, Lifecycle lifecycle) {
            super(view, lifecycle);
        }

        @Override // com.miui.gallery.widget.recyclerview.AbsViewHolder
        public void bindData(int i, int i2, List<Object> list) {
            int packDataPosition = PickerHomePageAdapter.this.packDataPosition(i, i2);
            if (PickerHomePageAdapter.this.mGridItem == null) {
                PickerHomePageAdapter.this.mGridItem = this.mView;
            }
            Cursor mo1558getItem = PickerHomePageAdapter.this.mo1558getItem(packDataPosition);
            this.mView.bindImage(PickerHomePageAdapter.this.getBindImagePath(packDataPosition), PickerHomePageAdapter.this.getDownloadUri(packDataPosition), PickerHomePageAdapter.this.getRequestOptions(packDataPosition), PickerHomePageAdapter.this.getPreviewRequestOptions(packDataPosition));
            String string = mo1558getItem.getString(8);
            long j = mo1558getItem.getLong(7);
            long j2 = mo1558getItem.getLong(14);
            boolean z = (mo1558getItem instanceof BurstFilterCursor) && ((BurstFilterCursor) mo1558getItem).isBurstPosition(packDataPosition);
            boolean z2 = z && ((BurstFilterCursor) mo1558getItem).isTimeBurstPosition(packDataPosition);
            if (z) {
                j2 |= z2 ? 8388608L : 64L;
            }
            this.mView.bindIndicator(string, j, j2);
            this.mView.bindFavoriteIndicator(PickerHomePageAdapter.this.isFavorite(packDataPosition));
            int syncState = PickerHomePageAdapter.this.getSyncState(mo1558getItem);
            long j3 = mo1558getItem.getLong(0);
            this.mView.bindSyncIndicator(j3, syncState, PickerHomePageAdapter.this.mShowScene);
            this.mView.setContentDescription(TalkBackUtil.getContentDescriptionForImage(PickerHomePageAdapter.this.mContext, mo1558getItem.getLong(15), mo1558getItem.getString(4), mo1558getItem.getString(8)));
            if (z) {
                this.mView.setIsSimilarBestImage(MediaFeatureCacheManager.getInstance().shouldShowSelectionStar(j3, false, false, PickerHomePageAdapter.this.getBurstItemKeys(packDataPosition)));
            } else {
                this.mView.setIsSimilarBestImage(MediaFeatureCacheManager.getInstance().shouldShowSelectionStar(j3, false, false));
            }
            Folme.useAt(this.mView).hover().setEffect(IHoverStyle.HoverEffect.NORMAL).handleHoverOf(this.mView, new AnimConfig[0]);
        }
    }
}
