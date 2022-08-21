package com.miui.gallery.adapter.itemmodel.trans;

import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.adapter.itemmodel.common.grid.CommonSimpleGridAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.common.grid.CommonWrapperCheckableGridAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.common.grid.CommonWrapperGridAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.common.linear.CommonSimpleLinearAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.common.linear.CommonWrapperCheckableLinearAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.common.linear.CommonWrapperLinearAlbumItemModel;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.ui.album.common.viewbean.ShareAlbumViewBean;
import com.miui.gallery.ui.album.common.viewbean.SystemAlbumViewBean;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;

/* loaded from: classes.dex */
public class CommonItemModelTransSolver implements TransDataToModelSolver {
    @Override // com.miui.gallery.adapter.itemmodel.trans.TransDataToModelSolver
    public EpoxyModel<?> transDataToModel(Object obj) {
        boolean shouldUseGridLayout = AlbumPageConfig.getInstance().shouldUseGridLayout();
        if (obj instanceof SystemAlbumViewBean) {
            if (shouldUseGridLayout) {
                return new CommonWrapperCheckableGridAlbumItemModel(new CommonWrapperGridAlbumItemModel(new CommonSimpleGridAlbumItemModel((SystemAlbumViewBean) obj)));
            }
            return new CommonWrapperCheckableLinearAlbumItemModel(new CommonWrapperLinearAlbumItemModel(new CommonSimpleLinearAlbumItemModel((CommonAlbumItemViewBean) obj)));
        } else if (shouldUseGridLayout) {
            return new CommonWrapperCheckableGridAlbumItemModel(new CommonWrapperGridAlbumItemModel(new CommonSimpleGridAlbumItemModel((CommonAlbumItemViewBean) obj)));
        } else {
            return new CommonWrapperCheckableLinearAlbumItemModel(new CommonWrapperLinearAlbumItemModel(new CommonSimpleLinearAlbumItemModel((CommonAlbumItemViewBean) obj)));
        }
    }

    @Override // com.miui.gallery.adapter.itemmodel.trans.TransDataToModelSolver
    public Class[] supportTypes() {
        return new Class[]{CommonAlbumItemViewBean.class, SystemAlbumViewBean.class, ShareAlbumViewBean.class};
    }
}
