package com.miui.gallery.ui.album.main.base.config;

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
public class LinearAlbumPageStyle extends BaseAlbumPageStyle {
    public int mDragItemSwapAnimDuration;
    public int mEmptyHeadGroupMoreAlbumTitleMarginTop;
    public int mExpandHeightWhenEmptyGroup;
    public int mFirstLineItemMarginTop;
    public int mItemHorizontalSpacing;
    public int mItemVerticalSpacing;
    public RecyclerView.ItemDecoration[] mLinearAlbumItemDecorations;
    public int mMediaTypeItemVerticalSpacing;
    public int mMoreAlbumTitleMarginBottom;
    public int mRecyclerViewMarginBottom;
    public int mRightArrowMarginEnd;
    public int mSwapItemNeedHowLongHover;
    public int mTitleAndRightArrowMargin;

    public LinearAlbumPageStyle() {
        initResource();
    }

    @Override // com.miui.gallery.ui.album.main.base.config.BaseAlbumPageStyle, com.miui.gallery.ui.album.main.base.config.BaseIconStyle
    public void initResource() {
        super.initResource();
        this.mItemVerticalSpacing = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_linear_item_vertical_spacing);
        this.mMoreAlbumTitleMarginBottom = ((int) DimensionUtils.getDimensionPixelSize(R.dimen.album_page_linear_mode_more_album_title_margin_bottom)) - this.mItemVerticalSpacing;
        this.mItemHorizontalSpacing = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_linear_item_margin_start);
        this.mFirstLineItemMarginTop = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_linear_item_first_item_margin_top);
        this.mRecyclerViewMarginBottom = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_item_placeholder_height);
        this.mDragItemSwapAnimDuration = ResourceUtils.getInt(R.integer.album_drag_item_linear_swap_anim_duration);
        this.mRightArrowMarginEnd = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_linear_item_button_margin_end);
        this.mTitleAndRightArrowMargin = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_linear_item_button_and_textview_margin);
        this.mEmptyHeadGroupMoreAlbumTitleMarginTop = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_linear_mode_more_tip_bean_padding_top_when_empty_head_group);
        this.mMediaTypeItemVerticalSpacing = ((int) DimensionUtils.getDimensionPixelSize(R.dimen.media_type_group_item_linear_veritily_space)) / 2;
        this.mExpandHeightWhenEmptyGroup = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_linear_cover_size);
    }

    public void onConfigurationChanged(Configuration configuration) {
        initResource();
    }

    public RecyclerView.ItemDecoration[] getItemDecorations() {
        if (this.mLinearAlbumItemDecorations == null) {
            this.mLinearAlbumItemDecorations = new RecyclerView.ItemDecoration[]{new RecyclerView.ItemDecoration() { // from class: com.miui.gallery.ui.album.main.base.config.LinearAlbumPageStyle.1
                @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                    super.getItemOffsets(rect, view, recyclerView, state);
                    int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                    if (LinearAlbumPageStyle.this.isGroupHeader(view, recyclerView)) {
                        if (!LinearAlbumPageStyle.this.isAlbumGroupHeader(view, recyclerView) || LinearAlbumPageStyle.this.getAlbumGroupState(view) != 1) {
                            if (childAdapterPosition == 0) {
                                rect.top = LinearAlbumPageStyle.this.mEmptyHeadGroupMoreAlbumTitleMarginTop;
                            } else {
                                rect.top = LinearAlbumPageStyle.this.mFirstLineItemMarginTop;
                            }
                            rect.bottom = LinearAlbumPageStyle.this.mMoreAlbumTitleMarginBottom;
                        }
                    } else if (LinearAlbumPageStyle.this.isMediaTypeItem(view, recyclerView)) {
                        int i = LinearAlbumPageStyle.this.mMediaTypeItemVerticalSpacing;
                        rect.bottom = i;
                        rect.top = i;
                    } else {
                        int i2 = LinearAlbumPageStyle.this.mItemVerticalSpacing;
                        rect.bottom = i2;
                        rect.top = i2;
                    }
                    rect.right = LinearAlbumPageStyle.this.getRightArrowMarginEnd();
                    rect.left = LinearAlbumPageStyle.this.mItemHorizontalSpacing;
                }
            }, new RecyclerView.ItemDecoration() { // from class: com.miui.gallery.ui.album.main.base.config.LinearAlbumPageStyle.2
                @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                    RecyclerView.Adapter adapter;
                    if (LinearAlbumPageStyle.this.getAlbumGroupState(view) == 3) {
                        rect.bottom = LinearAlbumPageStyle.this.mExpandHeightWhenEmptyGroup;
                    } else if (LinearAlbumPageStyle.this.getAlbumGroupState(view) != 2 || (adapter = recyclerView.getAdapter()) == null) {
                    } else {
                        if (!LinearAlbumPageStyle.this.isGroupHeader(adapter.getItemId(recyclerView.getChildAdapterPosition(view) + 1))) {
                            return;
                        }
                        rect.bottom = LinearAlbumPageStyle.this.mExpandHeightWhenEmptyGroup;
                    }
                }
            }};
        }
        return this.mLinearAlbumItemDecorations;
    }

    public GridLayoutManager getLayoutManager(Context context, EpoxyAdapter epoxyAdapter) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = epoxyAdapter.getSpanSizeLookup(1);
        spanSizeLookup.setSpanIndexCacheEnabled(true);
        gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
        return gridLayoutManager;
    }

    public int getRecyclerViewMarginBottom() {
        return this.mRecyclerViewMarginBottom;
    }

    public int getRecyclerViewMargin(boolean z) {
        if (z) {
            return (int) DimensionUtils.getDimensionPixelSize(R.dimen.miuix_appcompat_window_extra_padding_horizontal_small);
        }
        return 0;
    }

    public int getDragItemSwapAnimDuration() {
        return this.mDragItemSwapAnimDuration;
    }

    public int getTitleAndRightArrowMargin() {
        return this.mTitleAndRightArrowMargin;
    }

    public int getRightArrowMarginEnd() {
        return this.mRightArrowMarginEnd;
    }

    public int getSwapItemNeedHowLongHover() {
        return this.mSwapItemNeedHowLongHover;
    }

    public int getItemVerticalSpacing() {
        return this.mItemVerticalSpacing;
    }
}
