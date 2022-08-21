package com.miui.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Size;
import android.view.View;
import android.widget.CheckBox;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import com.miui.gallery.Config$ShareAlbumConfig;
import com.miui.gallery.R;
import com.miui.gallery.activity.InternalPhotoPageActivity;
import com.miui.gallery.adapter.ExcludeOnTouchListener;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.picker.helper.CursorUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.ui.AlbumDetailGridItem;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.util.PhotoPageIntent;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.AbsSingleImageViewHolder;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import java.util.List;
import java.util.Locale;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes.dex */
public class PickCleanerPhotoAdapter extends AlbumDetailAdapter {
    public boolean mClickToPhotoPage;
    public final GalleryRecyclerView mRecyclerView;

    @Override // com.miui.gallery.adapter.AlbumDetailAdapter
    public int getDateIndex() {
        return 28;
    }

    @Override // com.miui.gallery.adapter.AlbumDetailAdapter
    public int getTimeIndex() {
        return 17;
    }

    @Override // com.miui.gallery.adapter.AlbumDetailAdapter, com.miui.gallery.adapter.IMediaAdapter
    public boolean supportFoldBurstItems() {
        return false;
    }

    public PickCleanerPhotoAdapter(Context context, GalleryRecyclerView galleryRecyclerView, Lifecycle lifecycle) {
        super(context, lifecycle);
        this.mClickToPhotoPage = true;
        this.mRecyclerView = galleryRecyclerView;
        init();
    }

    public final void init() {
        this.mHeaderHeight = this.mContext.getResources().getDimensionPixelOffset(R.dimen.time_line_header_height);
    }

    public void setClickToPhotoPageEnable(boolean z) {
        this.mClickToPhotoPage = z;
    }

    public void onItemClick(View view, int i) {
        Cursor mo1558getItem = mo1558getItem(i);
        view.getLocationInWindow(new int[2]);
        new PhotoPageIntent.Builder((FragmentActivity) this.mContext, InternalPhotoPageActivity.class).setAdapterView(this.mRecyclerView).setUri(GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA.buildUpon().build()).setSelection(String.format(Locale.US, "_id = %d", Long.valueOf(CursorUtils.getId(mo1558getItem)))).setImageLoadParams(new ImageLoadParams.Builder().setKey(getItemKey(i)).setFilePath(getBindImagePath(i)).setTargetSize(new Size((int) (view.getWidth() * view.getScaleX()), (int) (view.getHeight() * view.getScaleY()))).setInitPosition(0).setMimeType(getMimeType(i)).setFileLength(getFileLength(i)).setImageWidth(getImageWidth(i)).setImageHeight(getImageHeight(i)).setCreateTime(getCreateTime(i)).setLocation(getLocation(i)).build()).setIdForPicker(getItemKey(i)).setUnfoldBurst(!supportFoldBurstItems()).setPreview(true).build().gotoPhotoPage();
    }

    @Override // com.miui.gallery.adapter.AlbumDetailAdapter, com.miui.gallery.adapter.MultiViewMediaAdapter
    public AbsSingleImageViewHolder createSingleImageViewHolder(View view, Lifecycle lifecycle) {
        return new BaseSingleImageViewHolder(view, lifecycle);
    }

    /* loaded from: classes.dex */
    public class BaseSingleImageViewHolder extends AbsSingleImageViewHolder {
        public BaseSingleImageViewHolder(View view, Lifecycle lifecycle) {
            super(view, lifecycle);
        }

        @Override // com.miui.gallery.widget.recyclerview.AbsViewHolder
        public void bindData(int i, int i2, List<Object> list) {
            final int packDataPosition = PickCleanerPhotoAdapter.this.packDataPosition(i, i2);
            PickCleanerPhotoAdapter pickCleanerPhotoAdapter = PickCleanerPhotoAdapter.this;
            if (pickCleanerPhotoAdapter.mGridItem == null) {
                pickCleanerPhotoAdapter.mGridItem = (AlbumDetailGridItem) this.mView;
            }
            Cursor mo1558getItem = pickCleanerPhotoAdapter.mo1558getItem(packDataPosition);
            AlbumDetailGridItem albumDetailGridItem = (AlbumDetailGridItem) this.mView;
            albumDetailGridItem.bindImage(PickCleanerPhotoAdapter.this.getBindImagePath(packDataPosition), PickCleanerPhotoAdapter.this.getDownloadUri(packDataPosition), PickCleanerPhotoAdapter.this.getRequestOptions(packDataPosition), PickCleanerPhotoAdapter.this.getPreviewRequestOptions(packDataPosition));
            String string = mo1558getItem.getString(10);
            long j = mo1558getItem.getLong(9);
            if (PickCleanerPhotoAdapter.this.getCurrentSortBy() == SortBy.SIZE) {
                albumDetailGridItem.bindIndicator(null, 0L, 0L);
                albumDetailGridItem.bindFileSize(mo1558getItem.getLong(11));
            } else {
                long supportedSpecialTypeFlags = Config$ShareAlbumConfig.getSupportedSpecialTypeFlags(mo1558getItem.getLong(20));
                albumDetailGridItem.bindFileSize(0L);
                albumDetailGridItem.bindIndicator(string, j, supportedSpecialTypeFlags);
            }
            PickCleanerPhotoAdapter pickCleanerPhotoAdapter2 = PickCleanerPhotoAdapter.this;
            if (pickCleanerPhotoAdapter2.mViewScrollState == 0) {
                pickCleanerPhotoAdapter2.bindContentDescription(albumDetailGridItem, packDataPosition);
            }
            CheckBox checkBox = albumDetailGridItem.getCheckBox();
            if (PickCleanerPhotoAdapter.this.mClickToPhotoPage) {
                MicroThumbGridItem microThumbGridItem = this.mView;
                microThumbGridItem.setOnTouchListener(new ExcludeOnTouchListener(microThumbGridItem, checkBox, new ExcludeOnTouchListener.OnTouchValidListener() { // from class: com.miui.gallery.adapter.PickCleanerPhotoAdapter.BaseSingleImageViewHolder.1
                    @Override // com.miui.gallery.adapter.ExcludeOnTouchListener.OnTouchValidListener
                    public void onTouchValid(View view) {
                        FolmeUtil.setFakeTouchAnim(view, 0.9f, 200L);
                        PickCleanerPhotoAdapter.this.onItemClick(view, packDataPosition);
                    }
                }));
            } else {
                Folme.useAt(this.mView).touch().handleTouchOf(this.mView, false, new AnimConfig());
            }
            Folme.useAt(this.mView).hover().setEffect(IHoverStyle.HoverEffect.NORMAL).handleHoverOf(this.mView, new AnimConfig[0]);
        }
    }
}
