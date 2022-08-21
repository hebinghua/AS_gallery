package com.miui.gallery.ui.album.main.base.config;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.gallery.R;
import com.miui.gallery.util.DimensionUtils;
import com.miui.gallery.util.ResourceUtils;

/* loaded from: classes2.dex */
public class GridAlbumPageStyle extends BaseAlbumPageStyle {
    public int mAlbumToolItemVerticalSpacing;
    public int mDragItemSwapAnimDuration;
    public int mDragItemXOffsetWhenEnterDrag;
    public int mDragItemYOffsetWhenEnterDrag;
    public int mEmptyHeadGroupMoreAlbumTitleMarginTop;
    public int mExpandHeightWhenEmptyGroup;
    public RecyclerView.ItemDecoration[] mGridAlbumItemDecorations;
    public int mItemHorizontalSpacing;
    public int mItemVerticalSpacing;
    public int mMediaTypeItemStyle;
    public int mMediaTypeItemVerticalSpacing;
    public int mMoreAlbumTitleMarginBottom;
    public int mMoreAlbumTitleMarginTop;
    public int mMoreTipPaddingTopWhenEmptyHeadGroup;
    public int mRecyclerViewMarginBottom;
    public int mRecyclerViewMarginStart;
    public int mRecyclerViewMarginTop;
    public int mSwapItemNeedHowLongHover;

    public GridAlbumPageStyle() {
        initResource();
    }

    public void onConfigurationChanged(Configuration configuration) {
        initResource();
    }

    @Override // com.miui.gallery.ui.album.main.base.config.BaseAlbumPageStyle, com.miui.gallery.ui.album.main.base.config.BaseIconStyle
    public void initResource() {
        super.initResource();
        this.mItemVerticalSpacing = (int) Math.round(((int) DimensionUtils.getDimensionPixelSize(R.dimen.album_grid_veritily_space)) / 2.0d);
        this.mItemHorizontalSpacing = ((int) DimensionUtils.getDimensionPixelSize(R.dimen.album_grid_horizontal_space)) / 2;
        this.mMoreAlbumTitleMarginTop = ((int) DimensionUtils.getDimensionPixelSize(R.dimen.album_page_more_album_title_margin_top)) - this.mItemVerticalSpacing;
        this.mEmptyHeadGroupMoreAlbumTitleMarginTop = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_more_tip_bean_padding_top_when_empty_head_group);
        this.mMoreAlbumTitleMarginBottom = ((int) DimensionUtils.getDimensionPixelSize(R.dimen.album_page_more_album_title_margin_bottom)) - this.mItemVerticalSpacing;
        this.mRecyclerViewMarginTop = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_page_main_recycleview_margin_top);
        this.mRecyclerViewMarginStart = ((int) DimensionUtils.getDimensionPixelSize(R.dimen.album_page_main_recycleview_margin_start_end)) - this.mItemHorizontalSpacing;
        this.mRecyclerViewMarginBottom = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_item_placeholder_height);
        this.mSwapItemNeedHowLongHover = ResourceUtils.getInt(R.integer.album_swap_item_need_how_long_hover);
        this.mDragItemXOffsetWhenEnterDrag = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_enter_drag_x_offset);
        this.mDragItemYOffsetWhenEnterDrag = (int) (-DimensionUtils.getDimensionPixelSize(R.dimen.album_enter_drag_y_offset));
        this.mMoreTipPaddingTopWhenEmptyHeadGroup = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_more_tip_bean_padding_top_when_empty_head_group);
        this.mDragItemSwapAnimDuration = ResourceUtils.getInt(R.integer.album_drag_item_swap_anim_duration);
        this.mMediaTypeItemVerticalSpacing = ((int) DimensionUtils.getDimensionPixelSize(R.dimen.media_type_group_item_grid_veritily_space)) / 2;
        this.mMediaTypeItemStyle = ResourceUtils.getInt(R.integer.album_tab_media_group_style);
        this.mAlbumToolItemVerticalSpacing = ((int) DimensionUtils.getDimensionPixelSize(R.dimen.album_tool_item_grid_vertical_space)) / 2;
        this.mExpandHeightWhenEmptyGroup = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_grid_cover_size);
    }

    public int getItemVerticalSpacing() {
        return this.mItemVerticalSpacing;
    }

    public int getItemHorizontalSpacing() {
        return this.mItemHorizontalSpacing;
    }

    public int getRecyclerViewMarginTop() {
        return this.mRecyclerViewMarginTop;
    }

    public int getRecyclerViewMarginStart() {
        return this.mRecyclerViewMarginStart;
    }

    public int getRecyclerViewMarginBottom() {
        return this.mRecyclerViewMarginBottom;
    }

    public int getSwapItemNeedHowLongHover() {
        return this.mSwapItemNeedHowLongHover;
    }

    public int getDragItemSwapAnimDuration() {
        return this.mDragItemSwapAnimDuration;
    }

    public int getMediaTypeItemStyle() {
        return this.mMediaTypeItemStyle;
    }

    public RecyclerView.ItemDecoration[] getItemDecorations() {
        if (this.mGridAlbumItemDecorations == null) {
            this.mGridAlbumItemDecorations = new RecyclerView.ItemDecoration[]{new RecyclerView.ItemDecoration() { // from class: com.miui.gallery.ui.album.main.base.config.GridAlbumPageStyle.1
                @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                    int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                    if (GridAlbumPageStyle.this.isGroupHeader(view, recyclerView)) {
                        if (!GridAlbumPageStyle.this.isAlbumGroupHeader(view, recyclerView) || GridAlbumPageStyle.this.getAlbumGroupState(view) != 1) {
                            if (childAdapterPosition == 0) {
                                rect.top = GridAlbumPageStyle.this.mMoreTipPaddingTopWhenEmptyHeadGroup;
                            } else {
                                rect.top = GridAlbumPageStyle.this.mMoreAlbumTitleMarginTop;
                            }
                            rect.bottom = GridAlbumPageStyle.this.mMoreAlbumTitleMarginBottom;
                        }
                    } else if (GridAlbumPageStyle.this.isMediaTypeItem(view, recyclerView)) {
                        int i = GridAlbumPageStyle.this.mMediaTypeItemVerticalSpacing;
                        rect.bottom = i;
                        rect.top = i;
                    } else if (GridAlbumPageStyle.this.isAlbumToolItem(view, recyclerView)) {
                        int i2 = GridAlbumPageStyle.this.mAlbumToolItemVerticalSpacing;
                        rect.bottom = i2;
                        rect.top = i2;
                    } else {
                        int i3 = GridAlbumPageStyle.this.mItemVerticalSpacing;
                        rect.bottom = i3;
                        rect.top = i3;
                    }
                    int i4 = GridAlbumPageStyle.this.mItemHorizontalSpacing;
                    rect.right = i4;
                    rect.left = i4;
                }
            }, new RecyclerView.ItemDecoration() { // from class: com.miui.gallery.ui.album.main.base.config.GridAlbumPageStyle.2
                @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                    RecyclerView.Adapter adapter;
                    if (GridAlbumPageStyle.this.getAlbumGroupState(view) == 3) {
                        rect.bottom = GridAlbumPageStyle.this.mExpandHeightWhenEmptyGroup;
                    } else if (GridAlbumPageStyle.this.getAlbumGroupState(view) != 2 || (adapter = recyclerView.getAdapter()) == null) {
                    } else {
                        if (!GridAlbumPageStyle.this.isGroupHeader(adapter.getItemId(recyclerView.getChildAdapterPosition(view) + 1))) {
                            return;
                        }
                        rect.bottom = GridAlbumPageStyle.this.mExpandHeightWhenEmptyGroup;
                    }
                }
            }};
        }
        return this.mGridAlbumItemDecorations;
    }

    public int getSpanCount(Context context, boolean z) {
        return z ? context == null ? ResourceUtils.getInt(R.integer.album_landscape_show_number) : context.getResources().getInteger(R.integer.album_landscape_show_number) : context == null ? ResourceUtils.getInt(R.integer.album_portrait_show_number) : context.getResources().getInteger(R.integer.album_portrait_show_number);
    }

    public int getSpanCount(Activity activity) {
        return getSpanCount(activity, AlbumPageConfig.getInstance().isNeedUseHorizontalSettingSpanCount(activity));
    }

    public GridLayoutManager getLayoutManager(Context context, int i, EpoxyAdapter epoxyAdapter) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, i);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = epoxyAdapter.getSpanSizeLookup(i);
        spanSizeLookup.setSpanIndexCacheEnabled(true);
        gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
        return gridLayoutManager;
    }
}
