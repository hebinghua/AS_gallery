package com.miui.gallery.share;

import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.baby.BabyInfo;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.data.DBItem;
import com.miui.gallery.data.DBShareAlbum;
import com.miui.gallery.data.ServerItem;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.share.AlbumShareOperations;

/* loaded from: classes2.dex */
public class CloudSharerMediaSet {
    public static final Uri BASE_OTHER_SET_URI = GalleryCloudUtils.SHARE_ALBUM_URI;
    public static final Uri BASE_OWNER_SET_URI = GalleryCloudUtils.CLOUD_URI;
    public DBItem mItem;
    public InnerNullItemImp mNullItem;
    public Path mPath;

    /* loaded from: classes2.dex */
    public static class InnerNullItemImp implements DBItem {
        public String getAlbumStatus() {
            return "";
        }

        public BabyInfo getBabyInfo() {
            return null;
        }

        public String getCreatorId() {
            return "";
        }

        public String getDisplayName() {
            return "";
        }

        @Override // com.miui.gallery.data.DBItem
        public String getId() {
            return "";
        }

        public String getPublicUrl() {
            return "";
        }

        public String getShareAlbumId() {
            return "";
        }

        public String getShareUrl() {
            return "";
        }

        public String getShareUrlLong() {
            return "";
        }

        public boolean getSharedToTv() {
            return false;
        }

        public String getSharerInfo() {
            return "";
        }

        public boolean isPublic() {
            return false;
        }
    }

    public void setLongUrl(String str) {
    }

    public void setSharerInfo(String str) {
    }

    public CloudSharerMediaSet(Path path) {
        this.mPath = path;
        reloadItem(path);
    }

    public final void reloadItem(Path path) {
        if (path.isOtherShared()) {
            this.mItem = CloudUtils.getDBShareAlbumByLocalId(String.valueOf(path.getId()));
        } else {
            this.mItem = AlbumDataHelper.getAlbumById(GalleryApp.sGetAndroidContext(), String.valueOf(path.getId()), null);
        }
        if (this.mItem == null) {
            this.mNullItem = new InnerNullItemImp();
        }
    }

    public void rereloadItem() {
        reloadItem(this.mPath);
    }

    public Path getPath() {
        return this.mPath;
    }

    public boolean isNormalStatus() {
        return "normal".equals(getAlbumStatus());
    }

    public synchronized int getAlbumShareState(String str) {
        return AlbumShareState.getState(getAlbumStatus(), str, hasShareDetailInfo());
    }

    public synchronized boolean hasShareDetailInfo() {
        String shareUrlLong = getShareUrlLong();
        if (TextUtils.isEmpty(shareUrlLong)) {
            return false;
        }
        if (!TextUtils.isEmpty(getSharerInfo())) {
            return true;
        }
        AlbumShareOperations.IncomingInvitation parseInvitation = AlbumShareOperations.parseInvitation(shareUrlLong);
        if (parseInvitation != null) {
            if (parseInvitation.hasSharerInfo()) {
                return false;
            }
        }
        return true;
    }

    public static Path buildPathById(long j) {
        return new Path(j, true);
    }

    public String getAlbumId() {
        return getShareAlbumId();
    }

    public boolean isOtherSharerAlbum() {
        return this.mPath.isOtherShared();
    }

    public long getId() {
        DBItem dBItem = this.mItem;
        if (dBItem == null) {
            return 0L;
        }
        return Long.parseLong(dBItem.getId());
    }

    public BabyInfo getBabyInfo() {
        if (this.mItem == null) {
            return this.mNullItem.getBabyInfo();
        }
        if (this.mPath.isOtherShared()) {
            return BabyInfo.fromJSON(((DBShareAlbum) this.mItem).getBabyInfoJson());
        }
        return BabyInfo.fromJSON(((DBAlbum) this.mItem).getBabyInfo());
    }

    public String getShareUrl() {
        if (this.mItem == null) {
            return this.mNullItem.getShareUrl();
        }
        if (!this.mPath.isOtherShared()) {
            return null;
        }
        return ((DBShareAlbum) this.mItem).getShareUrl();
    }

    public String getShareUrlLong() {
        if (this.mItem == null) {
            return this.mNullItem.getShareUrlLong();
        }
        return this.mPath.isOtherShared() ? ((DBShareAlbum) this.mItem).getShareUrlLong() : "";
    }

    public String getSharerInfo() {
        if (this.mItem == null) {
            return this.mNullItem.getSharerInfo();
        }
        return this.mPath.isOtherShared() ? ((DBShareAlbum) this.mItem).getSharerInfo() : "";
    }

    public String getShareAlbumId() {
        if (this.mItem == null) {
            return this.mNullItem.getShareAlbumId();
        }
        if (this.mPath.isOtherShared()) {
            return ((DBShareAlbum) this.mItem).getAlbumId();
        }
        return ((ServerItem) this.mItem).getServerId();
    }

    public String getAlbumStatus() {
        if (this.mItem == null) {
            return this.mNullItem.getAlbumStatus();
        }
        return this.mPath.isOtherShared() ? ((DBShareAlbum) this.mItem).getAlbumStatus() : "";
    }

    public String getCreatorId() {
        if (this.mItem == null) {
            return this.mNullItem.getCreatorId();
        }
        if (!this.mPath.isOtherShared()) {
            return null;
        }
        return ((DBShareAlbum) this.mItem).getCreatorId();
    }

    public boolean isPublic() {
        if (this.mItem == null) {
            return this.mNullItem.isPublic();
        }
        if (this.mPath.isOtherShared()) {
            return ((DBShareAlbum) this.mItem).getIsPublic();
        }
        return ((DBAlbum) this.mItem).isPublic();
    }

    public String getPublicUrl() {
        if (this.mItem == null) {
            return this.mNullItem.getPublicUrl();
        }
        if (this.mPath.isOtherShared()) {
            return ((DBShareAlbum) this.mItem).getPublicUri();
        }
        return ((DBAlbum) this.mItem).getPublicUrl();
    }

    public String getDisplayName() {
        if (this.mItem == null) {
            return this.mNullItem.getDisplayName();
        }
        if (this.mPath.isOtherShared()) {
            return ((DBShareAlbum) this.mItem).getFileName();
        }
        return ((DBAlbum) this.mItem).getName();
    }

    public boolean isSharedToTv() {
        if (this.mItem == null) {
            return this.mNullItem.getSharedToTv();
        }
        return !this.mPath.isOtherShared() && (((DBAlbum) this.mItem).getAttributes() & 1280) != 0;
    }
}
