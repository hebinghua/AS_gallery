package com.miui.gallery.adapter.itemmodel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryItemModel;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryViewHolder;
import com.miui.gallery.ui.album.common.viewbean.TrashAlbumViewBean;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes.dex */
public class TrashViewItemModel extends BaseGalleryItemModel<TrashAlbumViewBean, VH> {
    @Override // com.miui.epoxy.EpoxyModel
    public /* bridge */ /* synthetic */ void bindPartialData(EpoxyViewHolder epoxyViewHolder, List list) {
        bindPartialData((VH) epoxyViewHolder, (List<Object>) list);
    }

    public TrashViewItemModel(TrashAlbumViewBean trashAlbumViewBean) {
        super(trashAlbumViewBean.getId(), trashAlbumViewBean);
    }

    @Override // com.miui.epoxy.EpoxyModel
    public Object getDiffChangeResult(EpoxyModel epoxyModel) {
        if (epoxyModel instanceof TrashViewItemModel) {
            TrashAlbumViewBean itemData = ((TrashViewItemModel) epoxyModel).getItemData();
            TrashAlbumViewBean trashAlbumViewBean = new TrashAlbumViewBean();
            if (!isEquals(getItemData().getAlbumSubTitleText(), itemData.getAlbumSubTitleText())) {
                trashAlbumViewBean.setAlbumSubTitleText(itemData.getAlbumSubTitleText());
                return trashAlbumViewBean;
            }
        }
        return super.getDiffChangeResult(epoxyModel);
    }

    public void bindPartialData(VH vh, List<Object> list) {
        super.bindPartialData((TrashViewItemModel) vh, list);
        TrashAlbumViewBean trashAlbumViewBean = (TrashAlbumViewBean) list.get(0);
        if (!isEmpty(trashAlbumViewBean.getAlbumSubTitleText())) {
            bindSubTitleText(vh.mAlbumSubTitle, trashAlbumViewBean);
        }
    }

    @Override // com.miui.epoxy.EpoxyModel
    public int getLayoutRes() {
        return AlbumPageConfig.getInstance().isGridPageMode() ? R.layout.album_trash_layout : R.layout.album_trash_layout_linear;
    }

    @Override // com.miui.epoxy.EpoxyModel
    public void bindData(VH vh) {
        super.bindData((TrashViewItemModel) vh);
        bindSubTitleText(vh.mAlbumSubTitle, getItemData());
        Folme.useAt(vh.itemView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(vh.itemView, new AnimConfig[0]);
    }

    public void bindSubTitleText(TextView textView, TrashAlbumViewBean trashAlbumViewBean) {
        if (isEmpty(textView) || isEmpty(trashAlbumViewBean)) {
            return;
        }
        setText(textView, trashAlbumViewBean.getAlbumSubTitleText());
    }

    @Override // com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator */
    public EpoxyAdapter.IViewHolderCreator<VH> mo541getViewHolderCreator() {
        return new EpoxyAdapter.IViewHolderCreator<VH>() { // from class: com.miui.gallery.adapter.itemmodel.TrashViewItemModel.1
            @Override // com.miui.epoxy.EpoxyAdapter.IViewHolderCreator
            /* renamed from: create  reason: collision with other method in class */
            public VH mo1603create(View view, View view2) {
                return new VH(view);
            }
        };
    }

    /* loaded from: classes.dex */
    public static class VH extends BaseGalleryViewHolder {
        public TextView mAlbumSubTitle;
        public ImageView mImageView;

        public VH(View view) {
            super(view);
            this.mImageView = (ImageView) view.findViewById(R.id.trashCover);
            this.mAlbumSubTitle = (TextView) view.findViewById(R.id.album_common_sub_title);
        }
    }
}
