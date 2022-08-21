package com.miui.gallery.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Lifecycle;
import com.miui.gallery.R;
import com.miui.gallery.adapter.PickCleanerPhotoAdapter;
import com.miui.gallery.ui.SimilarPhotoPickGridHeaderItem;
import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.widget.recyclerview.AbsSingleImageViewHolder;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import java.util.List;

/* loaded from: classes.dex */
public class PickSimilarPhotoAdapter extends PickCleanerPhotoAdapter {
    public PickSimilarPhotoAdapter(Context context, GalleryRecyclerView galleryRecyclerView, Lifecycle lifecycle) {
        super(context, galleryRecyclerView, lifecycle);
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter, com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    /* renamed from: onCreateGroupViewHolder  reason: collision with other method in class */
    public BaseViewHolder mo1338onCreateGroupViewHolder(ViewGroup viewGroup, int i) {
        return BaseViewHolder.create(viewGroup, R.layout.similar_photo_pick_grid_header_item);
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public void doBindGroupViewHolder(BaseViewHolder baseViewHolder, int i, PictureViewMode pictureViewMode) {
        ((SimilarPhotoPickGridHeaderItem) baseViewHolder.itemView).bindData(getSortTime(this.mClusterAdapter.getGroupStartPosition(i)));
    }

    public long getSortTime(int i) {
        return mo1558getItem(i).getLong(17);
    }

    public List<Integer> getHeaderPositions() {
        return this.mClusterAdapter.getGroupStartPositions();
    }

    @Override // com.miui.gallery.adapter.PickCleanerPhotoAdapter, com.miui.gallery.adapter.AlbumDetailAdapter, com.miui.gallery.adapter.MultiViewMediaAdapter
    public AbsSingleImageViewHolder createSingleImageViewHolder(View view, Lifecycle lifecycle) {
        return new SingleImageViewHolder(view, lifecycle);
    }

    /* loaded from: classes.dex */
    public class SingleImageViewHolder extends PickCleanerPhotoAdapter.BaseSingleImageViewHolder {
        public SingleImageViewHolder(View view, Lifecycle lifecycle) {
            super(view, lifecycle);
        }

        @Override // com.miui.gallery.adapter.PickCleanerPhotoAdapter.BaseSingleImageViewHolder, com.miui.gallery.widget.recyclerview.AbsViewHolder
        public void bindData(int i, int i2, List<Object> list) {
            super.bindData(i, i2, list);
            this.mView.setIsSimilarBestImage(PickSimilarPhotoAdapter.this.getHeaderPositions().contains(Integer.valueOf(PickSimilarPhotoAdapter.this.packDataPosition(i, i2))));
        }
    }
}
