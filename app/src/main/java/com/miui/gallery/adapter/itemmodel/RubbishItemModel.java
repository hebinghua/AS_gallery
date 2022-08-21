package com.miui.gallery.adapter.itemmodel;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel;
import com.miui.gallery.ui.album.rubbishalbum.viewbean.RubbishItemItemViewBean;
import com.miui.gallery.util.DimensionUtils;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes.dex */
public class RubbishItemModel extends CommonSimpleLinearAlbumItemModel<RubbishItemItemViewBean, ViewHolder> {
    public static final float ALPHA_NORMAL = GalleryApp.sGetAndroidContext().getResources().getInteger(R.integer.item_normal_alpha_value) / 100.0f;
    public static final float ALPHA_BANNED = GalleryApp.sGetAndroidContext().getResources().getInteger(R.integer.item_banned_alpha_value) / 100.0f;

    public RubbishItemModel(RubbishItemItemViewBean rubbishItemItemViewBean) {
        super(rubbishItemItemViewBean);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel
    public void bindCover(ImageView imageView, RubbishItemItemViewBean rubbishItemItemViewBean) {
        super.bindCover(imageView, (ImageView) rubbishItemItemViewBean);
        if (isEmpty(imageView) || isEmpty(rubbishItemItemViewBean)) {
            return;
        }
        if (rubbishItemItemViewBean.isManualHide()) {
            imageView.setAlpha(ALPHA_BANNED);
        } else {
            imageView.setAlpha(ALPHA_NORMAL);
        }
        Folme.useAt(imageView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(imageView, new AnimConfig[0]);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel, com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator */
    public EpoxyAdapter.IViewHolderCreator<ViewHolder> mo541getViewHolderCreator() {
        return new EpoxyAdapter.IViewHolderCreator<ViewHolder>() { // from class: com.miui.gallery.adapter.itemmodel.RubbishItemModel.1
            @Override // com.miui.epoxy.EpoxyAdapter.IViewHolderCreator
            /* renamed from: create  reason: collision with other method in class */
            public ViewHolder mo1603create(View view, View view2) {
                return new ViewHolder(view);
            }
        };
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel, com.miui.gallery.ui.CommonWrapperCheckableLinearItemLayout.OnChangeCheckableStatusCallback
    public void onChangeCheckableStatus(ConstraintLayout constraintLayout, boolean z) {
        constraintLayout.findViewById(R.id.btnMoveTo).setVisibility(z ? 8 : 0);
    }

    /* loaded from: classes.dex */
    public static class ViewHolder extends CommonSimpleLinearAlbumItemModel.ViewHolder {
        public Button mBtnMoveTo;

        @Override // com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel.ViewHolder
        public boolean needRightArrowButton() {
            return false;
        }

        public ViewHolder(View view) {
            super(view);
            Button button = (Button) inflateOtherView(R.layout.album_common_vertical_button_layout, R.id.btnMoveTo);
            this.mBtnMoveTo = button;
            button.setText(R.string.operation_remove_rubbish);
            ViewGroup.LayoutParams layoutParams = this.mBtnMoveTo.getLayoutParams();
            layoutParams.height = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_linear_item_button_height);
            this.mBtnMoveTo.setLayoutParams(layoutParams);
            this.mAlbumCover.setForeground(view.getResources().getDrawable(R.drawable.album_page_list_rect_item_pressed_with_color_fg));
        }
    }
}
