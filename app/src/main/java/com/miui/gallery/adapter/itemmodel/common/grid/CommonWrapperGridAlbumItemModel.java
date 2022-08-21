package com.miui.gallery.adapter.itemmodel.common.grid;

import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.common.CommonWrapperAlbumItemModel;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;

/* loaded from: classes.dex */
public class CommonWrapperGridAlbumItemModel<DATA extends CommonAlbumItemViewBean> extends CommonWrapperAlbumItemModel<DATA, CommonAlbumItemModel.ViewHolder, CommonSimpleGridAlbumItemModel<DATA>> {
    @Override // com.miui.epoxy.EpoxyModel
    public int getLayoutRes() {
        return R.layout.album_common_wrapper_grid_item;
    }

    public CommonWrapperGridAlbumItemModel(CommonSimpleGridAlbumItemModel<DATA> commonSimpleGridAlbumItemModel) {
        super(commonSimpleGridAlbumItemModel);
    }
}
