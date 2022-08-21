package com.miui.gallery.model;

import android.content.Context;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.provider.FavoritesManager;
import com.miui.gallery.util.PhotoOperationsUtil;
import java.util.Locale;

/* loaded from: classes2.dex */
public class MediaItem extends BaseDataItem {
    @Override // com.miui.gallery.model.BaseDataItem
    public boolean isNeedQueryFavoriteInfo() {
        return true;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public PhotoDetailInfo getDetailInfo(Context context) {
        PhotoDetailInfo detailInfo = super.getDetailInfo(context);
        if (isVideo()) {
            PhotoDetailInfo.extractVideoAttr(detailInfo, getOriginalPath());
        } else {
            PhotoDetailInfo.extractExifInfo(detailInfo, getOriginalPath(), true);
        }
        return detailInfo;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public long initSupportOperations() {
        long imageSupportedOperations;
        if (isVideo()) {
            imageSupportedOperations = PhotoOperationsUtil.getVideoSupportedOperations(getOriginalPath());
        } else {
            imageSupportedOperations = PhotoOperationsUtil.getImageSupportedOperations(getOriginalPath(), getMimeType(), this.mLatitude, this.mLongitude);
        }
        return imageSupportedOperations | 8388608;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public FavoriteInfo queryFavoriteInfo(boolean z) {
        FavoritesManager.queryFavoriteInfoStateByFilePath(this.mFavoriteInfo, getOriginalPath(), z);
        return this.mFavoriteInfo;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public FavoriteInfo getFavoriteInfo() {
        return this.mFavoriteInfo;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public String toString() {
        String baseDataItem = super.toString();
        String format = String.format(Locale.US, "createTime = [%d], mSubTitle = [%s]", Long.valueOf(this.mCreateTime), getViewSubTitle(GalleryApp.sGetAndroidContext()));
        return baseDataItem + " " + format;
    }
}
