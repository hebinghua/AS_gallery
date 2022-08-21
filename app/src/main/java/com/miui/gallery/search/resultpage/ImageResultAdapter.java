package com.miui.gallery.search.resultpage;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.adapter.BaseMediaAdapter;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.adapter.CursorGroupedMediaAdapter;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.display.RequestLoadMoreListener;
import com.miui.gallery.search.core.suggestion.RankInfo;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.TalkBackUtil;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.xiaomi.stat.a.j;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes2.dex */
public class ImageResultAdapter extends CursorGroupedMediaAdapter implements CheckableAdapter {
    public int INDEX_CLOUD_ID;
    public int INDEX_CREATE_TIME;
    public int INDEX_DURATION;
    public int INDEX_IMAGE_HEIGHT;
    public int INDEX_IMAGE_WIDTH;
    public int INDEX_IS_FAVORITE;
    public int INDEX_LOCATION;
    public int INDEX_MICRO_THUMBNAIL_PATH;
    public int INDEX_MIME_TYPE;
    public int INDEX_ORIGINAL_PATH;
    public int INDEX_SERVER_ID;
    public int INDEX_SHA1;
    public int INDEX_SIZE;
    public int INDEX_SPECIAL_TYPE_FLAGS;
    public int INDEX_SYNC_STATE;
    public int INDEX_THUMBNAIL_PATH;
    public OnHeaderItemClickedListener mHeaderClickListener;
    public boolean mLoadMoreRequested;
    public boolean mNextLoadEnable;
    public QueryInfo mQueryInfo;
    public RankInfo mRankInfo;
    public RequestLoadMoreListener mRequestLoadMoreListener;

    /* loaded from: classes2.dex */
    public interface OnHeaderItemClickedListener {
        void onHeaderItemClicked(int i);
    }

    public static /* synthetic */ void $r8$lambda$gjJ7TSETMzHZibbVWIX_pEUTDVg(ImageResultAdapter imageResultAdapter, int i, View view) {
        imageResultAdapter.lambda$onBindGroupViewHolder$0(i, view);
    }

    public void setRequestLoadMoreListener(RequestLoadMoreListener requestLoadMoreListener) {
        this.mRequestLoadMoreListener = requestLoadMoreListener;
    }

    public void openLoadMore() {
        this.mNextLoadEnable = true;
        this.mLoadMoreRequested = false;
    }

    public void closeLoadMore() {
        if (this.mNextLoadEnable) {
            this.mNextLoadEnable = false;
        }
        this.mLoadMoreRequested = false;
    }

    public boolean isLoading() {
        return this.mNextLoadEnable;
    }

    public final void requestLoadMoreIfNeeded() {
        RequestLoadMoreListener requestLoadMoreListener;
        if (!this.mNextLoadEnable || (requestLoadMoreListener = this.mRequestLoadMoreListener) == null || this.mLoadMoreRequested) {
            return;
        }
        this.mLoadMoreRequested = true;
        requestLoadMoreListener.onLoadMoreRequested();
    }

    public ImageResultAdapter(Context context) {
        super(context);
        this.mNextLoadEnable = false;
        this.mLoadMoreRequested = false;
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public int getGroupCount() {
        if (getResultCursor() == null) {
            return 0;
        }
        return getResultCursor().getGroupCount();
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public int getChildCount(int i) {
        return getResultCursor().getChildrenCount(i);
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    /* renamed from: onCreateGroupViewHolder */
    public BaseViewHolder mo1338onCreateGroupViewHolder(ViewGroup viewGroup, int i) {
        ImageResultHeaderItem imageResultHeaderItem = new ImageResultHeaderItem(viewGroup.getContext(), null);
        imageResultHeaderItem.setLayoutParams(new ViewGroup.LayoutParams(-1, 0));
        return new BaseViewHolder(imageResultHeaderItem);
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    /* renamed from: onCreateChildViewHolder */
    public BaseViewHolder mo1337onCreateChildViewHolder(ViewGroup viewGroup, int i) {
        MicroThumbGridItem microThumbGridItem;
        if (SearchConstants.isHorizontalDocumentStyle(this.mRankInfo)) {
            microThumbGridItem = (SearchDocumentGridItem) LayoutInflater.from(this.mContext).inflate(R.layout.search_document_grid_item, viewGroup, false);
        } else {
            microThumbGridItem = (MicroThumbGridItem) LayoutInflater.from(this.mContext).inflate(R.layout.base_image_grid_item, viewGroup, false);
        }
        microThumbGridItem.setImageForeground(R.drawable.rect_item_fg_with_stroke);
        return new BaseViewHolder(microThumbGridItem);
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public void onBindGroupViewHolder(BaseViewHolder baseViewHolder, final int i, int i2) {
        ((ImageResultHeaderItem) baseViewHolder.itemView).bindData(getGroupRankValue(i), getGroupTitle(i), getGroupCreateTime(i) >= 0 && this.mHeaderClickListener != null, this.mShowTimeLine, new View.OnClickListener() { // from class: com.miui.gallery.search.resultpage.ImageResultAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ImageResultAdapter.$r8$lambda$gjJ7TSETMzHZibbVWIX_pEUTDVg(ImageResultAdapter.this, i, view);
            }
        });
    }

    public /* synthetic */ void lambda$onBindGroupViewHolder$0(int i, View view) {
        OnHeaderItemClickedListener onHeaderItemClickedListener = this.mHeaderClickListener;
        if (onHeaderItemClickedListener != null) {
            onHeaderItemClickedListener.onHeaderItemClicked(i);
        }
    }

    @Override // com.miui.gallery.adapter.BaseGroupedMediaAdapter
    public void doBindChildViewHolder(BaseViewHolder baseViewHolder, int i, int i2, List<Object> list) {
        MicroThumbGridItem microThumbGridItem = (MicroThumbGridItem) baseViewHolder.itemView;
        Cursor mo1558getItem = mo1558getItem(i);
        microThumbGridItem.bindImage(getBindImagePath(i), getDownloadUri(i), getRequestOptions(i), getPreviewRequestOptions(i));
        String string = mo1558getItem.getString(this.INDEX_MIME_TYPE);
        microThumbGridItem.bindIndicator(string, mo1558getItem.getLong(this.INDEX_DURATION), mo1558getItem.getLong(this.INDEX_SPECIAL_TYPE_FLAGS));
        microThumbGridItem.bindFavoriteIndicator(isFavorite(i));
        microThumbGridItem.setContentDescription(TalkBackUtil.getContentDescriptionForImage(this.mContext, mo1558getItem.getLong(this.INDEX_CREATE_TIME), mo1558getItem.getString(this.INDEX_LOCATION), string));
        if (i == getItemCount() - 1) {
            requestLoadMoreIfNeeded();
        }
        Folme.useAt(microThumbGridItem).hover().setEffect(IHoverStyle.HoverEffect.NORMAL).handleHoverOf(microThumbGridItem, new AnimConfig[0]);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewRecycled(BaseViewHolder baseViewHolder) {
        super.onViewRecycled((ImageResultAdapter) baseViewHolder);
        View view = baseViewHolder.itemView;
        if (view instanceof MicroThumbGridItem) {
            ((MicroThumbGridItem) view).recycle();
        }
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter
    public GlideOptions getRequestOptions(int i) {
        if (SearchConstants.isHorizontalDocumentStyle(this.mRankInfo)) {
            Size microThumbnailSize = getMicroThumbnailSize();
            return GlideOptions.thumbOf(getFileLength(i)).mo950diskCacheStrategy(DiskCacheStrategy.NONE).mo971override(microThumbnailSize.getWidth(), microThumbnailSize.getHeight());
        }
        return super.getRequestOptions(i);
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter
    public GlideOptions getPreviewRequestOptions(int i) {
        if (SearchConstants.isHorizontalDocumentStyle(this.mRankInfo)) {
            return null;
        }
        return super.getPreviewRequestOptions(i);
    }

    public Size getMicroThumbnailSize() {
        if (SearchConstants.isHorizontalDocumentStyle(this.mRankInfo)) {
            return Config$ThumbConfig.get().sMicroHorizontalDocumentTargetSize;
        }
        return Config$ThumbConfig.get().sMicroTargetSize;
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMicroThumbFilePath(int i) {
        return getSuggestionCursorByPosition(i).getString(this.INDEX_MICRO_THUMBNAIL_PATH);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getOriginFilePath(int i) {
        return getSuggestionCursorByPosition(i).getString(this.INDEX_ORIGINAL_PATH);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getThumbFilePath(int i) {
        return getSuggestionCursorByPosition(i).getString(this.INDEX_THUMBNAIL_PATH);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getOptimalThumbFilePath(int i) {
        return getMicroThumbFilePath(i);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public Uri getDownloadUri(int i) {
        return BaseMediaAdapter.getDownloadUri(getSuggestionCursorByPosition(i).getInt(this.INDEX_SYNC_STATE), getItemKey(i));
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getItemKey(int i) {
        return getSuggestionCursorByPosition(i).getLong(this.INDEX_CLOUD_ID);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getSha1(int i) {
        return mo1558getItem(i).getString(this.INDEX_SHA1);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getCreateTime(int i) {
        return mo1558getItem(i).getLong(this.INDEX_CREATE_TIME);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getLocation(int i) {
        return mo1558getItem(i).getString(this.INDEX_LOCATION);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMimeType(int i) {
        return getSuggestionCursorByPosition(i).getString(this.INDEX_MIME_TYPE);
    }

    public boolean isFavorite(int i) {
        return getSuggestionCursorByPosition(i).getInt(this.INDEX_IS_FAVORITE) > 0;
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getFileLength(int i) {
        return getSuggestionCursorByPosition(i).getLong(this.INDEX_SIZE);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public int getImageWidth(int i) {
        return getSuggestionCursorByPosition(i).getInt(this.INDEX_IMAGE_WIDTH);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public int getImageHeight(int i) {
        return getSuggestionCursorByPosition(i).getInt(this.INDEX_IMAGE_HEIGHT);
    }

    public String getServerId(int i) {
        return getSuggestionCursorByPosition(i).getString(this.INDEX_SERVER_ID);
    }

    public final SuggestionCursor getSuggestionCursorByPosition(int i) {
        return (SuggestionCursor) mo1558getItem(i);
    }

    @Override // com.miui.gallery.adapter.CheckableAdapter
    public View getCheckableView(View view) {
        return ((MicroThumbGridItem) view).getCheckBox();
    }

    public void changeSuggestions(QueryInfo queryInfo, RankInfo rankInfo, SuggestionCursor suggestionCursor) {
        this.mQueryInfo = queryInfo;
        this.mRankInfo = rankInfo;
        swapCursor(suggestionCursor);
    }

    public ImageResultSuggestionCursor getResultCursor() {
        if (getCursor() == null) {
            return null;
        }
        return (ImageResultSuggestionCursor) getCursor();
    }

    public String getImageIds() {
        return getResultCursor().getImageIds();
    }

    @Override // com.miui.gallery.adapter.CursorGroupedMediaAdapter, com.miui.gallery.adapter.ICursorAdapter
    public Cursor swapCursor(Cursor cursor) {
        if (cursor != null) {
            this.INDEX_SERVER_ID = cursor.getColumnIndex("serverId");
            this.INDEX_MICRO_THUMBNAIL_PATH = cursor.getColumnIndex("alias_micro_thumbnail");
            this.INDEX_THUMBNAIL_PATH = cursor.getColumnIndex("thumbnailFile");
            this.INDEX_ORIGINAL_PATH = cursor.getColumnIndex("localFile");
            this.INDEX_SHA1 = cursor.getColumnIndex("sha1");
            this.INDEX_MIME_TYPE = cursor.getColumnIndex("mimeType");
            this.INDEX_SYNC_STATE = cursor.getColumnIndex("alias_sync_state");
            this.INDEX_CLOUD_ID = cursor.getColumnIndex(j.c);
            this.INDEX_DURATION = cursor.getColumnIndex("duration");
            this.INDEX_IS_FAVORITE = cursor.getColumnIndex("alias_is_favorite");
            this.INDEX_LOCATION = cursor.getColumnIndex("location");
            this.INDEX_CREATE_TIME = cursor.getColumnIndex("alias_create_time");
            this.INDEX_SPECIAL_TYPE_FLAGS = cursor.getColumnIndex("specialTypeFlags");
            this.INDEX_SIZE = cursor.getColumnIndex(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE);
            this.INDEX_IMAGE_WIDTH = cursor.getColumnIndex("exifImageWidth");
            this.INDEX_IMAGE_HEIGHT = cursor.getColumnIndex("exifImageLength");
        }
        return super.swapCursor(cursor);
    }

    public int getGroupCreateDate(int i) {
        return getResultCursor().getGroupCreateDate(i);
    }

    public long getGroupCreateTime(int i) {
        return getResultCursor().getGroupCreateTime(i);
    }

    public String getGroupTitle(int i) {
        return getResultCursor().getGroupTitle(i);
    }

    public String getGroupRankValue(int i) {
        String groupRankValue = getResultCursor().getGroupRankValue(i);
        return (!TextUtils.isEmpty(groupRankValue) || getGroupCreateTime(i) <= 0) ? groupRankValue : GalleryDateUtils.formatRelativeDate(getGroupCreateTime(i));
    }

    public void setHeaderClickListener(OnHeaderItemClickedListener onHeaderItemClickedListener) {
        this.mHeaderClickListener = onHeaderItemClickedListener;
    }
}
