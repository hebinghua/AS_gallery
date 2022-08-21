package com.miui.gallery.model;

import android.content.Context;
import com.miui.gallery.provider.FavoritesManager;
import com.miui.gallery.util.PhotoOperationsUtil;
import java.io.File;

/* loaded from: classes2.dex */
public class StorageItem extends BaseDataItem {
    @Override // com.miui.gallery.model.BaseDataItem
    public boolean isNeedQueryFavoriteInfo() {
        return true;
    }

    public StorageItem() {
        this.mSize = -1L;
        this.mCreateTime = -1L;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public long getSize() {
        if (this.mSize == -1) {
            this.mSize = new File(getOriginalPath()).length();
        }
        return this.mSize;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public long getCreateTime() {
        if (this.mCreateTime == -1) {
            this.mCreateTime = new File(getOriginalPath()).lastModified();
        }
        return this.mCreateTime;
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
}
