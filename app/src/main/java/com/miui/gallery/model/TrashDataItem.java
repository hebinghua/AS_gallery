package com.miui.gallery.model;

import android.content.Context;
import android.net.Uri;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;

/* loaded from: classes2.dex */
public class TrashDataItem extends BaseDataItem {
    private String mAlbumName;
    private String mAlbumPath;
    private String mAlbumServerId;
    private long mCloudId;
    private String mCloudServerId;
    private long mDeleteTime;
    private String mFileName;
    private int mIsOrigin;
    public long mServerTag;
    private String mSha1;

    public String getSha1() {
        return this.mSha1;
    }

    public TrashDataItem setSha1(String str) {
        this.mSha1 = str;
        return this;
    }

    public long getCloudId() {
        return this.mCloudId;
    }

    public TrashDataItem setCloudId(long j) {
        this.mCloudId = j;
        return this;
    }

    public String getCloudServerId() {
        return this.mCloudServerId;
    }

    public TrashDataItem setCloudServerId(String str) {
        this.mCloudServerId = str;
        return this;
    }

    public String getFileName() {
        return this.mFileName;
    }

    public TrashDataItem setFileName(String str) {
        this.mFileName = str;
        return this;
    }

    public int getIsOrigin() {
        return this.mIsOrigin;
    }

    public TrashDataItem setIsOrigin(int i) {
        this.mIsOrigin = i;
        return this;
    }

    public TrashDataItem setDeleteTime(long j) {
        this.mDeleteTime = j;
        return this;
    }

    public String getAlbumServerId() {
        return this.mAlbumServerId;
    }

    public TrashDataItem setAlbumServerId(String str) {
        this.mAlbumServerId = str;
        return this;
    }

    public String getAlbumName() {
        return this.mAlbumName;
    }

    public TrashDataItem setAlbumName(String str) {
        this.mAlbumName = str;
        return this;
    }

    public String getAlbumPath() {
        return this.mAlbumPath;
    }

    public TrashDataItem setAlbumPath(String str) {
        this.mAlbumPath = str;
        return this;
    }

    public TrashDataItem setServerTag(long j) {
        this.mServerTag = j;
        return this;
    }

    public long getServerTag() {
        return this.mServerTag;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public boolean isSecret() {
        return super.isSecret() || this.mLocalGroupId == -1000;
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public Uri getDownloadUri() {
        return CloudUriAdapter.getDownloadUri(this.mCloudId);
    }

    @Override // com.miui.gallery.model.BaseDataItem
    public PhotoDetailInfo getDetailInfo(Context context) {
        PhotoDetailInfo detailInfo = super.getDetailInfo(context);
        PhotoDetailInfo.extractExifInfo(detailInfo, getOriginalPath(), true);
        return detailInfo;
    }
}
