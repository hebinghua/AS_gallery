package com.miui.gallery.adapter.itemmodel;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel;
import com.miui.gallery.ui.album.hiddenalbum.viewbean.HiddenAlbumItemViewBean;
import com.miui.gallery.util.DimensionUtils;

/* loaded from: classes.dex */
public class HiddenAlbumItemModel extends CommonSimpleLinearAlbumItemModel<HiddenAlbumItemViewBean, HiddenAlbumViewHolder> {
    public HiddenAlbumItemModel(HiddenAlbumItemViewBean hiddenAlbumItemViewBean) {
        super(hiddenAlbumItemViewBean);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel, com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator */
    public EpoxyAdapter.IViewHolderCreator<HiddenAlbumViewHolder> mo541getViewHolderCreator() {
        return new EpoxyAdapter.IViewHolderCreator<HiddenAlbumViewHolder>() { // from class: com.miui.gallery.adapter.itemmodel.HiddenAlbumItemModel.1
            @Override // com.miui.epoxy.EpoxyAdapter.IViewHolderCreator
            /* renamed from: create  reason: collision with other method in class */
            public HiddenAlbumViewHolder mo1603create(View view, View view2) {
                return new HiddenAlbumViewHolder(view);
            }
        };
    }

    /* loaded from: classes.dex */
    public static class HiddenAlbumViewHolder extends CommonSimpleLinearAlbumItemModel.ViewHolder {
        public Button mBtnCancelButton;

        @Override // com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel.ViewHolder
        public boolean needRightArrowButton() {
            return false;
        }

        public HiddenAlbumViewHolder(View view) {
            super(view);
            Button button = (Button) inflateOtherView(R.layout.album_common_vertical_button_layout, R.id.btnCancelHidden);
            this.mBtnCancelButton = button;
            button.setText(R.string.remove_hidden_status_title);
            ViewGroup.LayoutParams layoutParams = this.mBtnCancelButton.getLayoutParams();
            layoutParams.height = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_linear_item_button_height);
            this.mBtnCancelButton.setLayoutParams(layoutParams);
            this.mAlbumCover.setForeground(view.getResources().getDrawable(R.drawable.album_page_list_rect_item_pressed_with_color_fg));
        }
    }
}
