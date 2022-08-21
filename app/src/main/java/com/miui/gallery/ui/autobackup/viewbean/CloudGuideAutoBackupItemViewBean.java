package com.miui.gallery.ui.autobackup.viewbean;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.ResourceUtils;

/* loaded from: classes2.dex */
public class CloudGuideAutoBackupItemViewBean extends CommonAlbumItemViewBean<Album, CloudGuideAutoBackupItemViewBean> {
    public boolean isBackup;
    public boolean isEnable;

    @Override // com.miui.gallery.ui.album.common.CommonAlbumItemViewBean, com.miui.gallery.ui.album.common.base.BaseViewBean
    public void mapping(Album album) {
        super.mapping((CloudGuideAutoBackupItemViewBean) album);
        int photoCount = album.getPhotoCount();
        boolean z = false;
        setAlbumSubTitleText(ResourceUtils.getQuantityString(R.plurals.album_count_and_size, photoCount, Integer.valueOf(photoCount), FormatUtil.formatFileSize(GalleryApp.sGetAndroidContext(), album.getAlbumSize())));
        boolean isBabyAlbum = album.isBabyAlbum();
        boolean isOwnerShareAlbum = album.isOwnerShareAlbum();
        boolean isCameraAlbum = album.isCameraAlbum();
        boolean isAutoUploadedAlbum = album.isAutoUploadedAlbum();
        this.isBackup = isAutoUploadedAlbum;
        if ((!isBabyAlbum && !isOwnerShareAlbum && !isCameraAlbum) || !isAutoUploadedAlbum) {
            z = true;
        }
        this.isEnable = z;
    }

    public boolean isEnable() {
        return this.isEnable;
    }

    public boolean isBackup() {
        return this.isBackup;
    }
}
