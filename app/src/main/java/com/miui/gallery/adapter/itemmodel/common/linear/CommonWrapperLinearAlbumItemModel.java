package com.miui.gallery.adapter.itemmodel.common.linear;

import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.common.CommonWrapperAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;

/* loaded from: classes.dex */
public class CommonWrapperLinearAlbumItemModel<DATA extends CommonAlbumItemViewBean> extends CommonWrapperAlbumItemModel<DATA, CommonSimpleLinearAlbumItemModel.ViewHolder, CommonSimpleLinearAlbumItemModel<DATA, CommonSimpleLinearAlbumItemModel.ViewHolder>> {
    @Override // com.miui.epoxy.EpoxyModel
    public int getLayoutRes() {
        return R.layout.album_common_wrapper_linear_item;
    }

    public CommonWrapperLinearAlbumItemModel(CommonSimpleLinearAlbumItemModel<DATA, CommonSimpleLinearAlbumItemModel.ViewHolder> commonSimpleLinearAlbumItemModel) {
        super(commonSimpleLinearAlbumItemModel);
    }
}
