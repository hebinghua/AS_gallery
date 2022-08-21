package com.miui.gallery.adapter.itemmodel;

import android.view.View;
import com.bumptech.glide.signature.AndroidResourceSignature;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.AlbumTabToolItemModel;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryItemModel;
import com.miui.gallery.ui.album.common.MediaGroupTypeViewBean;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.util.imageloader.imageloadiotion.AlbumImageLoadOptions;

/* loaded from: classes.dex */
public class MediaGroupItemModel extends BaseGalleryItemModel<MediaGroupTypeViewBean, AlbumTabToolItemModel.VH> {
    public MediaGroupItemModel(MediaGroupTypeViewBean mediaGroupTypeViewBean) {
        super(mediaGroupTypeViewBean.getId(), mediaGroupTypeViewBean);
    }

    @Override // com.miui.epoxy.EpoxyModel
    public int getLayoutRes() {
        return AlbumPageConfig.getInstance().isGridPageMode() ? AlbumPageConfig.getAlbumTabConfig().isMediaTypeItemNormalStyle() ? R.layout.layout_album_media_group_item_grid_normal : R.layout.layout_album_media_group_item_grid_large : R.layout.layout_album_media_group_item_linear;
    }

    @Override // com.miui.epoxy.EpoxyModel
    public Object getDiffChangeResult(EpoxyModel epoxyModel) {
        return Boolean.valueOf(getLayoutRes() != epoxyModel.getLayoutRes());
    }

    @Override // com.miui.epoxy.EpoxyModel
    public void bindData(AlbumTabToolItemModel.VH vh) {
        super.bindData((MediaGroupItemModel) vh);
        MediaGroupTypeViewBean itemData = getItemData();
        setText(vh.getTitle(), itemData.getTitle());
        bindImage(vh.getIcon(), itemData.getCoverUri(), AlbumImageLoadOptions.getInstance().getDefaultNoCacheModeOption().clone().mo976signature(AndroidResourceSignature.obtain(getThemeContext(vh))));
    }

    @Override // com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator */
    public EpoxyAdapter.IViewHolderCreator<AlbumTabToolItemModel.VH> mo541getViewHolderCreator() {
        return new EpoxyAdapter.IViewHolderCreator<AlbumTabToolItemModel.VH>() { // from class: com.miui.gallery.adapter.itemmodel.MediaGroupItemModel.1
            @Override // com.miui.epoxy.EpoxyAdapter.IViewHolderCreator
            /* renamed from: create  reason: collision with other method in class */
            public AlbumTabToolItemModel.VH mo1603create(View view, View view2) {
                return new AlbumTabToolItemModel.VH(view);
            }
        };
    }
}
