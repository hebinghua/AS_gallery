package com.miui.gallery.adapter.itemmodel;

import android.view.View;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryItemModel;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryViewHolder;
import com.miui.gallery.ui.album.common.viewbean.MoreAlbumTipViewBean;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;

/* loaded from: classes.dex */
public class MoreAlbumTipViewItemModel extends BaseGalleryItemModel<MoreAlbumTipViewBean, BaseGalleryViewHolder> {
    @Override // com.miui.epoxy.EpoxyModel
    public int getSpanSize(int i, int i2, int i3) {
        return i;
    }

    public MoreAlbumTipViewItemModel(MoreAlbumTipViewBean moreAlbumTipViewBean) {
        super(moreAlbumTipViewBean.getId(), moreAlbumTipViewBean);
    }

    @Override // com.miui.epoxy.EpoxyModel
    public int getLayoutRes() {
        return AlbumPageConfig.getInstance().isGridPageMode() ? R.layout.album_group_more_tip : R.layout.album_group_more_tip_linear;
    }

    @Override // com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator */
    public EpoxyAdapter.IViewHolderCreator<BaseGalleryViewHolder> mo541getViewHolderCreator() {
        return new EpoxyAdapter.IViewHolderCreator<BaseGalleryViewHolder>() { // from class: com.miui.gallery.adapter.itemmodel.MoreAlbumTipViewItemModel.1
            @Override // com.miui.epoxy.EpoxyAdapter.IViewHolderCreator
            /* renamed from: create  reason: collision with other method in class */
            public BaseGalleryViewHolder mo1603create(View view, View view2) {
                return new BaseGalleryViewHolder(view);
            }
        };
    }
}
