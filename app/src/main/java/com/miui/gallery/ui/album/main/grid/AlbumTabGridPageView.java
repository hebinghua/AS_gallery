package com.miui.gallery.ui.album.main.grid;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.utils.EpoxyAdapterUtils;
import com.miui.gallery.R;
import com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$V;
import com.miui.gallery.ui.album.main.base.BaseAlbumTabPageView;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.ui.album.main.base.config.BaseAlbumPageStyle;
import com.miui.gallery.ui.album.main.base.config.GridAlbumPageStyle;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.itemdrag.RecyclerViewDragItemManager;

/* loaded from: classes2.dex */
public class AlbumTabGridPageView extends BaseAlbumTabPageView {
    public GridAlbumPageStyle mConfig;

    public AlbumTabGridPageView(BaseAlbumTabContract$V baseAlbumTabContract$V) {
        super(baseAlbumTabContract$V);
    }

    public AlbumTabGridPageView(BaseAlbumTabPageView baseAlbumTabPageView) {
        super(baseAlbumTabPageView);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumListPageView
    public RecyclerView.ItemDecoration[] getRecyclerViewDecorations() {
        return AlbumPageConfig.AlbumTabPage.getGridAlbumConfig().getItemDecorations();
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPageView
    public void init(BaseAlbumTabPageView baseAlbumTabPageView) {
        this.mConfig = AlbumPageConfig.AlbumTabPage.getGridAlbumConfig();
        super.init(baseAlbumTabPageView);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPageView
    public void initChoiceMode(EditableListViewWrapper editableListViewWrapper) {
        editableListViewWrapper.clearBindViewHolderHook();
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPageView
    public RecyclerViewDragItemManager initDragMode(RecyclerViewDragItemManager.Config.Builder builder) {
        builder.setDragItemReturnToSourcePositionAnimDuration(this.mConfig.getDragItemReturnToSourcePositionAnimDuration()).setSwapItemNeedHowLongHover(this.mConfig.getSwapItemNeedHowLongHover()).setAnimInterpolator(4, this.mConfig.getChangeAnimInterpolator()).setAnimInterpolator(1, this.mConfig.getMoveAnimInterpolator()).setAnimInterpolator(3, this.mConfig.getAddAnimInterpolator()).setAnimInterpolator(2, this.mConfig.getRemoveAnimInterpolator()).setLongPressTimeout(this.mConfig.getEnterDragPressTimeout()).setAnimDuration(1, this.mConfig.getDragItemSwapAnimDuration()).setAnimDuration(4, this.mConfig.getDragItemSwapAnimDuration()).setOnDragItemEffectCallback(new RecyclerViewDragItemManager.OnDragItemEffectCallback() { // from class: com.miui.gallery.ui.album.main.grid.AlbumTabGridPageView.1
            @Override // com.miui.itemdrag.RecyclerViewDragItemManager.OnDragItemEffectCallback
            public boolean onDraw(Canvas canvas, int i, int i2, Bitmap bitmap, Paint paint, int i3, int i4) {
                if (AlbumTabGridPageView.this.isSetDragItemOffsetWhenStartDrag()) {
                    int save = canvas.save();
                    canvas.translate(i, i2);
                    canvas.drawBitmap(bitmap, -i3, -i4, paint);
                    canvas.restoreToCount(save);
                }
                return AlbumTabGridPageView.this.isSetDragItemOffsetWhenStartDrag();
            }
        });
        return new RecyclerViewDragItemManager(builder.build());
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPageView, com.miui.gallery.ui.album.main.base.BaseAlbumListPageView
    public void onInitRecyclerView(RecyclerView recyclerView) {
        super.onInitRecyclerView(recyclerView);
        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        int spanCount = getSpanCount();
        if (gridLayoutManager == null) {
            GridLayoutManager layoutManager = this.mConfig.getLayoutManager(recyclerView.getContext(), spanCount, getAdapter());
            configRecyclerPool(recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            recyclerView.getRecycledViewPool().clear();
            configRecyclerPool(recyclerView);
            gridLayoutManager.setSpanCount(spanCount);
            gridLayoutManager.setSpanSizeLookup(getAdapter().getSpanSizeLookup(spanCount));
        }
        recyclerView.setItemViewCacheSize(10);
        getParent().setRecyclerviewPadding(this.mConfig.getRecyclerViewMarginStart(), this.mConfig.getRecyclerViewMarginTop(), this.mConfig.getRecyclerViewMarginStart(), this.mConfig.getRecyclerViewMarginBottom());
        recyclerView.setClipToPadding(false);
    }

    public final int getSpanCount() {
        return AlbumPageConfig.AlbumTabPage.getGridAlbumConfig().getSpanCount(getActivity());
    }

    public final void configRecyclerPool(RecyclerView recyclerView) {
        recyclerView.getRecycledViewPool().setMaxRecycledViews(EpoxyAdapterUtils.calculateItemViewType(R.layout.album_common_wrapper_grid_item), 4);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(EpoxyAdapterUtils.calculateItemViewType(R.layout.album_common_wrapper_checkable_grid_item), 12);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(EpoxyAdapterUtils.calculateItemViewType(R.layout.album_coveraggregation_item_grid), 2);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(EpoxyAdapterUtils.calculateItemViewType(R.layout.album_group_more_tip), 1);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(EpoxyAdapterUtils.calculateItemViewType(R.layout.album_trash_layout), 1);
        recyclerView.setItemViewCacheSize(12);
    }

    @Override // com.miui.gallery.ui.album.main.base.AbsAlbumPageView
    public void onConfigurationChanged(Configuration configuration) {
        AlbumPageConfig.getAlbumTabConfig().updateConfigResource(configuration);
        onSpanCountUpdate();
        getParent().setRecyclerviewPadding(this.mConfig.getRecyclerViewMarginStart(), this.mConfig.getRecyclerViewMarginTop(), this.mConfig.getRecyclerViewMarginStart(), this.mConfig.getRecyclerViewMarginBottom());
    }

    public void onSpanCountUpdate() {
        int spanCount = getSpanCount();
        getAdapter().setSpanCount(spanCount);
        ((GridLayoutManager) getRecyclerView().getLayoutManager()).setSpanCount(spanCount);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabPageView
    public BaseAlbumPageStyle getStyle() {
        return this.mConfig;
    }
}
