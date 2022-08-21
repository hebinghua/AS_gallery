package com.miui.gallery.ui.album.cloudalbum.viewbean;

import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.viewbean.ShareAlbumViewBean;
import java.util.Objects;

/* loaded from: classes2.dex */
public class CloudAlbumItemViewBean extends ShareAlbumViewBean {
    public boolean isBabyAlbum;
    public boolean isBackup;
    public boolean isShareToDevice;

    @Override // com.miui.gallery.ui.album.common.CommonAlbumItemViewBean, com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider
    public boolean isMoreStyle() {
        return false;
    }

    public void setBabyAlbum(boolean z) {
        this.isBabyAlbum = z;
    }

    public boolean isBackup() {
        return this.isBackup;
    }

    public void setBackup(boolean z) {
        this.isBackup = z;
    }

    public boolean isShareToDevice() {
        return this.isShareToDevice;
    }

    public void setShareToDevice(boolean z) {
        this.isShareToDevice = z;
    }

    @Override // com.miui.gallery.ui.album.common.CommonAlbumItemViewBean, com.miui.gallery.ui.album.common.base.BaseViewBean, java.util.Comparator
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass() || !super.equals(obj)) {
            return false;
        }
        CloudAlbumItemViewBean cloudAlbumItemViewBean = (CloudAlbumItemViewBean) obj;
        return this.isBackup == cloudAlbumItemViewBean.isBackup && this.isBabyAlbum == cloudAlbumItemViewBean.isBabyAlbum && this.isShareToDevice == cloudAlbumItemViewBean.isShareToDevice;
    }

    @Override // com.miui.gallery.ui.album.common.CommonAlbumItemViewBean, com.miui.gallery.ui.album.common.base.BaseViewBean
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), Boolean.valueOf(this.isBackup), Boolean.valueOf(this.isBabyAlbum), Boolean.valueOf(this.isShareToDevice));
    }

    @Override // com.miui.gallery.ui.album.common.viewbean.ShareAlbumViewBean, com.miui.gallery.ui.album.common.CommonAlbumItemViewBean, com.miui.gallery.ui.album.common.base.BaseViewBean
    public void mapping(Album album) {
        super.mapping(album);
        setBackup(album.isAutoUploadedAlbum());
        setBabyAlbum(album.isBabyAlbum());
        setShareToDevice(album.isShareToDevice());
    }
}
