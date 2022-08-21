package com.miui.gallery.ui.addtoalbum.style;

import android.content.res.Configuration;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.adapter.GallerySimpleEpoxyAdapter;
import com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.base.AbsAlbumPageView;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;

/* loaded from: classes2.dex */
public class AddToAlbumGridPageView extends AbsAlbumPageView {
    public GallerySimpleEpoxyAdapter<BaseViewBean> mAdapter;
    public RecyclerView mRecyclerView;

    public AddToAlbumGridPageView(AddToAlbumPageActivity addToAlbumPageActivity, GallerySimpleEpoxyAdapter<BaseViewBean> gallerySimpleEpoxyAdapter) {
        super(addToAlbumPageActivity);
        this.mAdapter = gallerySimpleEpoxyAdapter;
    }

    @Override // com.miui.gallery.ui.album.main.base.AbsAlbumPageView
    public void onInit(View view) {
        super.onInit(view);
        AlbumPageConfig.getAddToAlbumConfig().updateConfigResource(getActivity(), getResources().getConfiguration());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewList);
        this.mRecyclerView = recyclerView;
        recyclerView.setLayoutManager(AlbumPageConfig.getAddToAlbumConfig().getNormalGroupLayoutManager(this.mRecyclerView, this.mAdapter));
        this.mRecyclerView.addItemDecoration(AlbumPageConfig.getAddToAlbumConfig().getNormalGroupItemDecoration());
        this.mRecyclerView.setPadding(AlbumPageConfig.getAddToAlbumConfig().getContentPaddingStart(), 0, AlbumPageConfig.getAddToAlbumConfig().getContentPaddingEnd(), 0);
    }

    @Override // com.miui.gallery.ui.album.main.base.AbsAlbumPageView
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        AlbumPageConfig.getAddToAlbumConfig().updateConfigResource(getActivity(), configuration);
        onSpanCountUpdate();
    }

    public final void onSpanCountUpdate() {
        if (this.mRecyclerView == null || this.mAdapter == null) {
            return;
        }
        int normalGroupGridSpanCount = AlbumPageConfig.getAddToAlbumConfig().getNormalGroupGridSpanCount();
        this.mAdapter.setSpanCount(normalGroupGridSpanCount);
        if (!(this.mRecyclerView.getLayoutManager() instanceof GridLayoutManager)) {
            return;
        }
        ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(normalGroupGridSpanCount);
    }
}
