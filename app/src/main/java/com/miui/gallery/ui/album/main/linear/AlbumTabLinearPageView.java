package com.miui.gallery.ui.album.main.linear;

import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.utils.EpoxyAdapterUtils;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.common.linear.CommonWrapperCheckableLinearAlbumItemModel;
import com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$V;
import com.miui.gallery.ui.album.main.base.BaseAlbumTabPageView;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.ui.album.main.base.config.BaseAlbumPageStyle;
import com.miui.gallery.ui.album.main.base.config.LinearAlbumPageStyle;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.itemdrag.RecyclerViewDragItemManager;

/* loaded from: classes2.dex */
public class AlbumTabLinearPageView extends BaseAlbumTabPageView {
    public LinearAlbumPageStyle mConfig;

    public AlbumTabLinearPageView(BaseAlbumTabContract$V baseAlbumTabContract$V) {
        super(baseAlbumTabContract$V);
        this.mConfig = AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig();
    }

    public AlbumTabLinearPageView(BaseAlbumTabPageView baseAlbumTabPageView) {
        super(baseAlbumTabPageView);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumListPageView
    public RecyclerView.ItemDecoration[] getRecyclerViewDecorations() {
        return this.mConfig.getItemDecorations();
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPageView
    public void init(BaseAlbumTabPageView baseAlbumTabPageView) {
        this.mConfig = AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig();
        super.init(baseAlbumTabPageView);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPageView
    public void initChoiceMode(EditableListViewWrapper editableListViewWrapper) {
        editableListViewWrapper.setOnBindViewHolderHook(new EditableListViewWrapper.OnBindViewHolderHook() { // from class: com.miui.gallery.ui.album.main.linear.AlbumTabLinearPageView.1
            @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.OnBindViewHolderHook
            public void onBind(RecyclerView.ViewHolder viewHolder, int i) {
                View findViewById;
                if ((viewHolder instanceof CommonWrapperCheckableLinearAlbumItemModel.VH) || (findViewById = viewHolder.itemView.findViewById(R.id.tvRightArrow)) == null) {
                    return;
                }
                if (AlbumTabLinearPageView.this.isInChoiceMode()) {
                    findViewById.setVisibility(4);
                } else {
                    findViewById.setVisibility(0);
                }
            }
        }, true);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPageView
    public RecyclerViewDragItemManager initDragMode(RecyclerViewDragItemManager.Config.Builder builder) {
        builder.setSwapItemNeedHowLongHover(this.mConfig.getSwapItemNeedHowLongHover()).setAnimInterpolator(4, this.mConfig.getChangeAnimInterpolator()).setAnimInterpolator(1, this.mConfig.getMoveAnimInterpolator()).setAnimInterpolator(3, this.mConfig.getAddAnimInterpolator()).setAnimInterpolator(2, this.mConfig.getRemoveAnimInterpolator()).setDragItemReturnToSourcePositionAnimDuration(this.mConfig.getDragItemReturnToSourcePositionAnimDuration()).setLongPressTimeout(this.mConfig.getEnterDragPressTimeout()).setAnimDuration(1, this.mConfig.getDragItemSwapAnimDuration()).setAnimDuration(4, this.mConfig.getDragItemSwapAnimDuration());
        return new RecyclerViewDragItemManager(builder.build());
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPageView, com.miui.gallery.ui.album.main.base.BaseAlbumListPageView
    public void onInitRecyclerView(RecyclerView recyclerView) {
        super.onInitRecyclerView(recyclerView);
        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        if (gridLayoutManager == null) {
            GridLayoutManager layoutManager = this.mConfig.getLayoutManager(recyclerView.getContext(), getAdapter());
            configRecyclerPool(recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            recyclerView.getRecycledViewPool().clear();
            configRecyclerPool(recyclerView);
            gridLayoutManager.setSpanCount(1);
            gridLayoutManager.setSpanSizeLookup(getAdapter().getSpanSizeLookup(1));
        }
        getParent().setRecyclerviewPadding(0, 0, 0, AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getRecyclerViewMarginBottom());
        recyclerView.setClipToPadding(false);
    }

    public final void configRecyclerPool(RecyclerView recyclerView) {
        recyclerView.getRecycledViewPool().setMaxRecycledViews(EpoxyAdapterUtils.calculateItemViewType(R.layout.album_coveraggregation_item_linear), 2);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(EpoxyAdapterUtils.calculateItemViewType(R.layout.album_group_more_tip_linear), 1);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(EpoxyAdapterUtils.calculateItemViewType(R.layout.album_trash_layout_linear), 1);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(EpoxyAdapterUtils.calculateItemViewType(R.layout.album_common_wrapper_linear_item), 4);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(EpoxyAdapterUtils.calculateItemViewType(R.layout.album_common_wrapper_checkable_linear_item), 12);
        recyclerView.setItemViewCacheSize(12);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPageView
    public BaseAlbumPageStyle getStyle() {
        return this.mConfig;
    }
}
