package com.miui.gallery.ui.album.common.viewbean;

import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.util.ResourceUtils;

/* loaded from: classes2.dex */
public class ShareAlbumViewBean extends CommonAlbumItemViewBean<Album, ShareAlbumViewBean> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.ui.album.common.CommonAlbumItemViewBean, com.miui.gallery.ui.album.common.base.BaseViewBean
    public void mapping(Album album) {
        String str;
        super.mapping((ShareAlbumViewBean) album);
        String shareAlbumInfoTipTextIfNeed = ShareAlbumHelper.getShareAlbumInfoTipTextIfNeed(album);
        StringBuilder sb = new StringBuilder();
        sb.append(ResourceUtils.getQuantityString(R.plurals.album_photo_count_text, album.getPhotoCount(), Integer.valueOf(album.getPhotoCount())));
        if (TextUtils.isEmpty(shareAlbumInfoTipTextIfNeed)) {
            str = "";
        } else {
            str = " | " + shareAlbumInfoTipTextIfNeed;
        }
        sb.append(str);
        setAlbumSubTitleText(sb.toString());
    }
}
