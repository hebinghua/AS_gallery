package com.miui.gallery.ui.addtoalbum.viewbean;

import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;

/* loaded from: classes2.dex */
public class AddToAlbumItemViewBean extends CommonAlbumItemViewBean<Album, AddToAlbumItemViewBean> {
    public boolean isRecent;

    public boolean isRecentItem() {
        return this.isRecent;
    }

    public void setIsRecent(boolean z) {
        this.isRecent = z;
    }
}
