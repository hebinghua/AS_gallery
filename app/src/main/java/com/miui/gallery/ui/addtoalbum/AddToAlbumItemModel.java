package com.miui.gallery.ui.addtoalbum;

import android.content.Context;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.AndroidResourceSignature;
import com.miui.gallery.adapter.itemmodel.common.grid.CommonSimpleGridAlbumItemModel;
import com.miui.gallery.ui.addtoalbum.viewbean.AddToAlbumItemViewBean;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.util.imageloader.imageloadiotion.AlbumImageLoadOptions;

/* loaded from: classes2.dex */
public class AddToAlbumItemModel extends CommonSimpleGridAlbumItemModel<AddToAlbumItemViewBean> {
    public AddToAlbumItemModel(AddToAlbumItemViewBean addToAlbumItemViewBean) {
        super(addToAlbumItemViewBean.getId(), addToAlbumItemViewBean);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel
    /* renamed from: instanceDiffResultBean  reason: collision with other method in class */
    public AddToAlbumItemViewBean mo1574instanceDiffResultBean() {
        return new AddToAlbumItemViewBean();
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.grid.CommonSimpleGridAlbumItemModel, com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel
    public RequestOptions getImageLoaderOptions(Context context, AddToAlbumItemViewBean addToAlbumItemViewBean) {
        long id = addToAlbumItemViewBean.getId();
        if (id == AlbumPageConfig.getAddToAlbumConfig().getCreateAlbumButtonId() || id == AlbumPageConfig.getAddToAlbumConfig().getSecretAlbumButtonId()) {
            return AlbumImageLoadOptions.getInstance().getDefaultNoCacheModeOption().clone().mo976signature(AndroidResourceSignature.obtain(context));
        }
        return super.getImageLoaderOptions(context, (Context) addToAlbumItemViewBean);
    }
}
