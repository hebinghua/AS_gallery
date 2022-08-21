package com.miui.gallery.ui.album.hiddenalbum.viewbean;

import com.miui.gallery.R;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.util.ResourceUtils;

/* loaded from: classes2.dex */
public class HiddenAlbumItemViewBean extends CommonAlbumItemViewBean<Album, HiddenAlbumItemViewBean> {
    @Override // com.miui.gallery.ui.album.common.CommonAlbumItemViewBean, com.miui.gallery.ui.album.common.base.BaseViewBean
    public void mapping(Album album) {
        super.mapping((HiddenAlbumItemViewBean) album);
        setAlbumSubTitleText(ResourceUtils.getQuantityString(R.plurals.album_photo_count_text, album.getPhotoCount(), Integer.valueOf(album.getPhotoCount())));
    }
}
