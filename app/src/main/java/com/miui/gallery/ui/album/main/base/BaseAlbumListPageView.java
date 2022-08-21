package com.miui.gallery.ui.album.main.base;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.adapter.GallerySimpleEpoxyAdapter;
import com.miui.gallery.app.base.BaseListPageFragment;

/* loaded from: classes2.dex */
public abstract class BaseAlbumListPageView extends AbsAlbumPageView {
    public BaseListPageFragment mParent;

    public abstract RecyclerView.ItemDecoration[] getRecyclerViewDecorations();

    public void onInitRecyclerView(RecyclerView recyclerView) {
    }

    public BaseAlbumListPageView(BaseListPageFragment baseListPageFragment) {
        super(baseListPageFragment);
        this.mParent = baseListPageFragment;
    }

    @Override // com.miui.gallery.ui.album.main.base.AbsAlbumPageView
    public final void onInit(View view) {
        super.onInit(view);
        onInitRecyclerView((RecyclerView) view.findViewById(R.id.recyclerViewList));
    }

    public BaseListPageFragment getParent() {
        return this.mParent;
    }

    public GallerySimpleEpoxyAdapter getAdapter() {
        return this.mParent.getSourceAdapter();
    }

    public RecyclerView getRecyclerView() {
        return this.mParent.getRecyclerView();
    }
}
