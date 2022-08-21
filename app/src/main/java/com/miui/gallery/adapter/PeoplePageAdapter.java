package com.miui.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.ui.PeoplePageGridHeaderItem;
import com.miui.gallery.ui.PeoplePageGridItem;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.miui.gallery.util.face.PeopleCursorHelper;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class PeoplePageAdapter extends CursorGroupedMediaAdapter implements CheckableAdapter {
    public boolean mCheckable;
    public List<HeaderData> mHeaders;
    public HashMap<String, Integer> mUserDefineRelationMap;

    @Override // com.miui.gallery.adapter.CheckableAdapter
    public View getCheckableView(View view) {
        return null;
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getCreateTime(int i) {
        return 0L;
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getLocation(int i) {
        return "";
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMimeType(int i) {
        return null;
    }

    public PeoplePageAdapter(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.adapter.CursorGroupedMediaAdapter, com.miui.gallery.adapter.ICursorAdapter
    public Cursor swapCursor(Cursor cursor) {
        Cursor cursor2 = getCursor();
        if (cursor == null || cursor != cursor2) {
            this.mHeaders = null;
        }
        return super.swapCursor(cursor);
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter
    public GlideOptions getRequestOptions(int i) {
        return GlideOptions.peopleFaceOf(getFaceRegionRectF(i), getFileLength(i));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewRecycled(BaseViewHolder baseViewHolder) {
        super.onViewRecycled((PeoplePageAdapter) baseViewHolder);
        View view = baseViewHolder.itemView;
        if (view instanceof PeoplePageGridItem) {
            ((PeoplePageGridItem) view).clearImage();
        }
    }

    @Override // com.miui.gallery.adapter.BaseGroupedMediaAdapter
    public void doBindChildViewHolder(BaseViewHolder baseViewHolder, int i, int i2, List<Object> list) {
        PeoplePageGridItem peoplePageGridItem = (PeoplePageGridItem) baseViewHolder.itemView;
        Cursor mo1558getItem = mo1558getItem(i);
        peoplePageGridItem.bindImage(getBindImagePath(i), getDownloadUri(i), getRequestOptions(i), PeopleCursorHelper.getThumbnailDownloadType(mo1558getItem));
        String peopleName = PeopleCursorHelper.getPeopleName(mo1558getItem);
        String peopleServerId = PeopleCursorHelper.getPeopleServerId(mo1558getItem);
        long parseLong = Long.parseLong(PeopleCursorHelper.getPeopleLocalId(mo1558getItem));
        if (!TextUtils.isEmpty(peopleName)) {
            peoplePageGridItem.setName(peopleName);
        } else {
            peoplePageGridItem.setName(this.mContext.getString(R.string.people_page_unname));
        }
        peoplePageGridItem.saveIds2Tag(parseLong, peopleServerId);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public Uri getDownloadUri(int i) {
        return PeopleCursorHelper.getThumbnailDownloadUri(mo1558getItem(i));
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getItemKey(int i) {
        return PeopleCursorHelper.getPeopleLocalId(mo1558getItem(i)).hashCode();
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getSha1(int i) {
        return PeopleCursorHelper.getCoverSha1(mo1558getItem(i));
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getOptimalThumbFilePath(int i) {
        return getClearThumbFilePath(i);
    }

    public String getClearThumbFilePath(int i) {
        return PeopleCursorHelper.getThumbnailPath(mo1558getItem(i));
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMicroThumbFilePath(int i) {
        return PeopleCursorHelper.getMicroThumbnailPath(mo1558getItem(i));
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getThumbFilePath(int i) {
        return PeopleCursorHelper.getThumbnailPath(mo1558getItem(i));
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getFileLength(int i) {
        return PeopleCursorHelper.getCoverSize(mo1558getItem(i));
    }

    public FaceRegionRectF getFaceRegionRectF(int i) {
        return PeopleCursorHelper.getFaceRegionRectF(mo1558getItem(i));
    }

    public String getPeopleIdOfItem(int i) {
        return PeopleCursorHelper.getPeopleServerId(mo1558getItem(i));
    }

    public String getPeopleLocalIdOfItem(int i) {
        return PeopleCursorHelper.getPeopleLocalId(mo1558getItem(i));
    }

    public int getRelationTypeOfItem(int i) {
        return PeopleCursorHelper.getRelationType(mo1558getItem(i));
    }

    public String getRelationTextOfItem(int i) {
        return PeopleCursorHelper.getRelationText(mo1558getItem(i));
    }

    public void setUserDefineRelationMap(HashMap<String, Integer> hashMap) {
        this.mUserDefineRelationMap = hashMap;
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    /* renamed from: onCreateGroupViewHolder  reason: collision with other method in class */
    public BaseViewHolder mo1338onCreateGroupViewHolder(ViewGroup viewGroup, int i) {
        if (PeopleContactInfo.isUnKnownRelation(i)) {
            return new UndefinedRelationViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.people_page_undifined_header_item, viewGroup, false));
        }
        return new BaseViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.people_page_grid_header_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    /* renamed from: onCreateChildViewHolder  reason: collision with other method in class */
    public BaseViewHolder mo1337onCreateChildViewHolder(ViewGroup viewGroup, int i) {
        return BaseViewHolder.create(viewGroup, R.layout.people_page_grid_item);
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public void onBindGroupViewHolder(BaseViewHolder baseViewHolder, int i, int i2) {
        String relationShow;
        int groupItemViewType = getGroupItemViewType(i);
        if (PeopleContactInfo.isUnKnownRelation(groupItemViewType)) {
            if (i == 0) {
                ((UndefinedRelationViewHolder) baseViewHolder).undefinedLine.setVisibility(8);
                return;
            } else {
                ((UndefinedRelationViewHolder) baseViewHolder).undefinedLine.setVisibility(0);
                return;
            }
        }
        if (PeopleContactInfo.isUserDefineRelation(groupItemViewType)) {
            relationShow = getRelationTextOfItem(getGroupStartPos(i));
        } else {
            relationShow = PeopleContactInfo.getRelationShow(groupItemViewType);
        }
        ((PeoplePageGridHeaderItem) baseViewHolder.itemView).bindData(relationShow);
    }

    public final int getHeaderIndex(int i) {
        int relationTypeOfItem = getRelationTypeOfItem(i);
        if (PeopleContactInfo.isUserDefineRelation(relationTypeOfItem)) {
            int i2 = 0;
            String relationTextOfItem = getRelationTextOfItem(i);
            HashMap<String, Integer> hashMap = this.mUserDefineRelationMap;
            if (hashMap != null && hashMap.get(relationTextOfItem) != null) {
                i2 = this.mUserDefineRelationMap.get(relationTextOfItem).intValue();
            }
            return PeopleContactInfo.getRelationTypesCount() + i2;
        }
        return relationTypeOfItem;
    }

    public void enterChoiceMode() {
        this.mCheckable = true;
    }

    public void exitChoiceMode() {
        this.mCheckable = false;
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public int getChildCount(int i) {
        generateHeaderIfNecessary();
        HeaderData headerData = this.mHeaders.get(i);
        if (headerData != null) {
            return headerData.mCount;
        }
        return 0;
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public int getGroupCount() {
        generateHeaderIfNecessary();
        return this.mHeaders.size();
    }

    public List<HeaderData> generateHeaderList() {
        ArrayList arrayList = new ArrayList();
        int i = Integer.MIN_VALUE;
        HeaderData headerData = null;
        for (int i2 = 0; i2 < getItemCount(); i2++) {
            int headerIndex = getHeaderIndex(i2);
            int relationTypeOfItem = getRelationTypeOfItem(i2);
            if (headerData == null || headerIndex != i) {
                HeaderData headerData2 = new HeaderData(headerIndex, relationTypeOfItem, i2);
                arrayList.add(headerData2);
                headerData = headerData2;
                i = headerIndex;
            }
            headerData.incrementCount();
        }
        return arrayList;
    }

    @Override // com.miui.gallery.widget.recyclerview.GroupedItemAdapter
    public int packDataPosition(int i, int i2) {
        generateHeaderIfNecessary();
        HeaderData headerData = this.mHeaders.get(i);
        return headerData != null ? headerData.mRefPosition + i2 : i2;
    }

    public int getGroupStartPos(int i) {
        generateHeaderIfNecessary();
        HeaderData headerData = this.mHeaders.get(i);
        if (headerData != null) {
            return headerData.mRefPosition;
        }
        return -1;
    }

    public final void generateHeaderIfNecessary() {
        if (this.mHeaders == null) {
            this.mHeaders = generateHeaderList();
        }
    }

    @Override // com.miui.gallery.adapter.BaseGroupedMediaAdapter, com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public long getGroupId(int i) {
        return this.mHeaders.get(i).mHeaderIndex;
    }

    @Override // com.miui.gallery.adapter.BaseGroupedMediaAdapter, com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public long getChildId(int i, int i2) {
        return getItemId(packDataPosition(i, i2));
    }

    @Override // com.miui.gallery.adapter.BaseGroupedMediaAdapter, com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public int getGroupItemViewType(int i) {
        return this.mHeaders.get(i).mRelationType;
    }

    /* loaded from: classes.dex */
    public static class UndefinedRelationViewHolder extends BaseViewHolder {
        public View undefinedLine;

        public UndefinedRelationViewHolder(View view) {
            super(view);
            this.undefinedLine = view.findViewById(R.id.undefined_line);
        }
    }

    /* loaded from: classes.dex */
    public static class HeaderData {
        public int mCount = 0;
        public int mHeaderIndex;
        public int mRefPosition;
        public int mRelationType;

        public HeaderData(int i, int i2, int i3) {
            this.mHeaderIndex = i;
            this.mRelationType = i2;
            this.mRefPosition = i3;
        }

        public void incrementCount() {
            this.mCount++;
        }
    }
}
