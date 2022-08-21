package com.miui.gallery.adapter.itemmodel.common.linear;

import android.view.View;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.epoxy.common.BaseWrapperItemModel;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryWrapperItemModel;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes.dex */
public class CommonWrapperCheckableLinearAlbumItemModel extends BaseGalleryWrapperItemModel<BaseViewBean, EpoxyViewHolder, VH, EpoxyModel<EpoxyViewHolder>> {
    @Override // com.miui.epoxy.EpoxyModel
    public int getLayoutRes() {
        return R.layout.album_common_wrapper_checkable_linear_item;
    }

    public CommonWrapperCheckableLinearAlbumItemModel(EpoxyModel epoxyModel) {
        super(epoxyModel);
    }

    @Override // com.miui.epoxy.EpoxyWrapperModel
    public void bindData(VH vh) {
        super.bindData((CommonWrapperCheckableLinearAlbumItemModel) vh);
        vh.itemView.setTag(R.id.tag_item_model, this);
        Folme.useAt(vh.itemView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(vh.itemView, new AnimConfig[0]);
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [com.miui.epoxy.EpoxyModel] */
    /* JADX WARN: Type inference failed for: r2v0, types: [com.miui.epoxy.EpoxyModel] */
    @Override // com.miui.epoxy.EpoxyWrapperModel, com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator  reason: collision with other method in class */
    public EpoxyAdapter.WrapperViewHolderCreator<VH, EpoxyViewHolder> mo541getViewHolderCreator() {
        return new EpoxyAdapter.WrapperViewHolderCreator<VH, EpoxyViewHolder>(getChildModel().getLayoutRes(), getChildModel().mo541getViewHolderCreator()) { // from class: com.miui.gallery.adapter.itemmodel.common.linear.CommonWrapperCheckableLinearAlbumItemModel.1
            @Override // com.miui.epoxy.EpoxyAdapter.WrapperViewHolderCreator
            public int childViewId() {
                return R.id.album_common_wrapper_main;
            }

            @Override // com.miui.epoxy.EpoxyAdapter.WrapperViewHolderCreator
            public VH create(View view, EpoxyViewHolder epoxyViewHolder) {
                return new VH(view, epoxyViewHolder);
            }
        };
    }

    /* loaded from: classes.dex */
    public static class VH extends BaseWrapperItemModel.VH<EpoxyViewHolder> {
        public VH(View view, EpoxyViewHolder epoxyViewHolder) {
            super(view, epoxyViewHolder);
        }
    }
}
