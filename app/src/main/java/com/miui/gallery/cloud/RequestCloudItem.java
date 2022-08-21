package com.miui.gallery.cloud;

import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.data.LocalUbifocus;
import com.miui.gallery.data.UbiIndexMapper;
import com.miui.gallery.util.StorageUtils;
import java.io.File;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class RequestCloudItem extends RequestItemBase {
    public DBImage dbCloud;
    public boolean mIsCloudInvalid;

    @Override // com.miui.gallery.cloud.RequestItemBase
    public int getRequestLimitAGroup() {
        return 10;
    }

    public RequestCloudItem(int i, DBImage dBImage) {
        this(i, dBImage, false);
    }

    public RequestCloudItem(int i, DBImage dBImage, boolean z) {
        super(i, z ? 0L : RequestItemBase.getDelay(dBImage.getLocalFlag(), dBImage.getLocalFile()));
        this.dbCloud = dBImage;
        init();
    }

    @Override // com.miui.gallery.cloud.RequestItemBase
    public ArrayList<RequestItemBase> getItems() {
        ArrayList<RequestItemBase> arrayList = new ArrayList<>();
        arrayList.add(this);
        return arrayList;
    }

    public String getIdentity() {
        return this.dbCloud.getTagId();
    }

    public boolean isSecretItem() {
        return this.dbCloud.isSecretItem();
    }

    public String getFileName() {
        int downloadType = getDownloadType();
        if (downloadType == 1) {
            return isSecretItem() ? this.dbCloud.getSha1ThumbnailSA() : this.dbCloud.getSha1Thumbnail();
        } else if (downloadType == 2) {
            if (isSecretItem()) {
                return this.dbCloud.getSha1ThumbnailSA();
            }
            return DownloadPathHelper.getThumbnailDownloadFileNameNotSecret(this.dbCloud);
        } else if (downloadType == 3 || downloadType == 4) {
            return isSecretItem() ? this.dbCloud.getEncodedFileName() : DownloadPathHelper.getOriginDownloadFileNameNotSecret(this.dbCloud);
        } else {
            throw new UnsupportedOperationException("bad checktype, checktype=" + getDownloadType());
        }
    }

    public String getFolderPathForWrite() {
        int downloadType = getDownloadType();
        if (downloadType != 1) {
            return (downloadType == 2 || downloadType == 3 || downloadType == 4) ? DownloadPathHelper.getDownloadFolderPath(this.dbCloud) : "";
        }
        return StorageUtils.getPathInPriorStorage("/Android/data/com.miui.gallery/cache/downloadThumbnail");
    }

    public String getDownloadFilePathForWrite() {
        String fileName = getFileName();
        if (getDownloadType() == 3 && (this.dbCloud.isUbiFocus() || this.dbCloud.isSubUbiFocus())) {
            fileName = LocalUbifocus.createInnerFileName(fileName, UbiIndexMapper.cloudToLocal(this.dbCloud.getSubUbiIndex(), this.dbCloud.getSubUbiImageCount() + 1), this.dbCloud.getSubUbiImageCount() + 1);
        }
        return getFolderPathForWrite() + File.separator + fileName;
    }

    public String getDownloadFilePathForRead() {
        int downloadType = getDownloadType();
        if (downloadType == 1) {
            String microThumbnailFile = this.dbCloud.getMicroThumbnailFile();
            return new File(microThumbnailFile).exists() ? microThumbnailFile : DownloadPathHelper.getFilePathForRead(StorageUtils.getMicroThumbnailDirectories(GalleryApp.sGetAndroidContext()), getFileName());
        } else if (downloadType == 2) {
            String thumbnailFile = this.dbCloud.getThumbnailFile();
            return new File(thumbnailFile).exists() ? thumbnailFile : DownloadPathHelper.getFilePathForRead(DownloadPathHelper.getDownloadFolderRelativePath(this.dbCloud), getFileName());
        } else if (downloadType != 3 && downloadType != 4) {
            return "";
        } else {
            String localFile = this.dbCloud.getLocalFile();
            if (isOriginFileValidate(localFile)) {
                return localFile;
            }
            String filePathForRead = DownloadPathHelper.getFilePathForRead(DownloadPathHelper.getDownloadFolderRelativePath(this.dbCloud), getFileName());
            return isOriginFileValidate(filePathForRead) ? filePathForRead : "";
        }
    }

    public final boolean isOriginFileValidate(String str) {
        return !TextUtils.isEmpty(str) && new File(str).length() >= this.dbCloud.getSize();
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x00b7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String getVerifiedDownloadFilePathForRead() {
        /*
            r6 = this;
            java.lang.String r0 = r6.getDownloadFilePathForRead()
            int r1 = r6.getDownloadType()
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            java.lang.String r3 = "localFile"
            java.lang.String r4 = "thumbnailFile"
            java.lang.String r5 = ""
            if (r2 == 0) goto L16
            goto Laf
        L16:
            r2 = 1
            if (r1 != r2) goto L29
            com.miui.gallery.data.DBImage r1 = r6.dbCloud
            java.lang.String r1 = r1.getMicroThumbnailFile()
            boolean r1 = r0.equalsIgnoreCase(r1)
            if (r1 != 0) goto Laf
            java.lang.String r3 = "microthumbfile"
            goto Lb0
        L29:
            r2 = 2
            if (r1 != r2) goto L59
            com.miui.gallery.data.DBImage r1 = r6.dbCloud
            java.lang.String r1 = r1.getThumbnailFile()
            boolean r1 = r0.equalsIgnoreCase(r1)
            if (r1 != 0) goto Laf
            boolean r1 = r6.isSecretItem()
            if (r1 != 0) goto L51
            com.miui.gallery.data.DBImage r1 = r6.dbCloud
            java.lang.String r1 = r1.getSha1()
            java.lang.String r2 = com.miui.gallery.util.ExifUtil.getUserCommentSha1(r0)
            boolean r1 = android.text.TextUtils.equals(r1, r2)
            if (r1 == 0) goto L4f
            goto L51
        L4f:
            r3 = r5
            goto Lb1
        L51:
            com.miui.gallery.data.DBImage r1 = r6.dbCloud
            r1.setThumbnailFile(r0)
            r5 = r0
        L57:
            r3 = r4
            goto Lb1
        L59:
            r2 = 3
            if (r1 == r2) goto L5f
            r2 = 4
            if (r1 != r2) goto Laf
        L5f:
            com.miui.gallery.data.DBImage r1 = r6.dbCloud
            java.lang.String r1 = r1.getLocalFile()
            boolean r1 = r0.equalsIgnoreCase(r1)
            if (r1 != 0) goto Laf
            boolean r1 = r6.isSecretItem()
            if (r1 == 0) goto L77
            com.miui.gallery.data.DBImage r1 = r6.dbCloud
            r1.setLocalFile(r0)
            goto Lb0
        L77:
            java.lang.String r1 = com.miui.gallery.util.ExifUtil.getUserCommentSha1(r0)
            com.miui.gallery.data.DBImage r2 = r6.dbCloud
            java.lang.String r2 = r2.getSha1()
            boolean r1 = android.text.TextUtils.equals(r1, r2)
            if (r1 == 0) goto L99
            com.miui.gallery.data.DBImage r1 = r6.dbCloud
            java.lang.String r1 = r1.getThumbnailFile()
            boolean r1 = r0.equalsIgnoreCase(r1)
            if (r1 != 0) goto L4f
            com.miui.gallery.data.DBImage r1 = r6.dbCloud
            r1.setThumbnailFile(r0)
            goto L57
        L99:
            com.miui.gallery.data.DBImage r1 = r6.dbCloud
            java.lang.String r1 = r1.getSha1()
            java.lang.String r2 = com.miui.gallery.util.FileUtils.getSha1(r0)
            boolean r1 = android.text.TextUtils.equals(r1, r2)
            if (r1 == 0) goto L4f
            com.miui.gallery.data.DBImage r1 = r6.dbCloud
            r1.setLocalFile(r0)
            goto Lb0
        Laf:
            r3 = r5
        Lb0:
            r5 = r0
        Lb1:
            boolean r1 = android.text.TextUtils.isEmpty(r3)
            if (r1 != 0) goto Lce
            android.content.ContentValues r1 = new android.content.ContentValues
            r1.<init>()
            r1.put(r3, r0)
            com.miui.gallery.data.DBImage r0 = r6.dbCloud
            android.net.Uri r0 = r0.getBaseUri()
            com.miui.gallery.data.DBImage r2 = r6.dbCloud
            java.lang.String r2 = r2.getId()
            com.miui.gallery.cloud.CloudUtils.updateToLocalDB(r0, r1, r2)
        Lce:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloud.RequestCloudItem.getVerifiedDownloadFilePathForRead():java.lang.String");
    }

    public String getDownloadTempFilePath() {
        int downloadType = getDownloadType();
        if (downloadType == 1) {
            return getDownloadFilePathForWrite() + ".temp";
        } else if (downloadType == 2) {
            return StorageUtils.getPathInPriorStorage("/Android/data/com.miui.gallery/cache/downloadThumbnail" + File.separator + this.dbCloud.getSha1());
        } else if (downloadType != 3 && downloadType != 4) {
            return "";
        } else {
            return StorageUtils.getPathInPriorStorage("/Android/data/com.miui.gallery/cache/downloadFile" + File.separator + this.dbCloud.getSha1());
        }
    }

    public static String getDownloadOriginalFilePath(DBImage dBImage) {
        return new RequestCloudItem(dBImage.isVideoType() ? 9 : 10, dBImage).getVerifiedDownloadFilePathForRead();
    }

    @Override // com.miui.gallery.cloud.RequestItemBase
    public boolean isInSameAlbum(RequestItemBase requestItemBase) {
        RequestCloudItem requestCloudItem = (RequestCloudItem) requestItemBase;
        if (requestCloudItem.getDownloadType() != 1 || getDownloadType() != 1 || !requestCloudItem.dbCloud.isShareItem() || !this.dbCloud.isShareItem()) {
            return true;
        }
        return TextUtils.equals(requestCloudItem.dbCloud.getShareAlbumId(), this.dbCloud.getShareAlbumId());
    }

    @Override // com.miui.gallery.cloud.RequestItemBase
    public boolean supportMultiRequest() {
        return getDownloadType() == 1;
    }

    public void setIsCloudInvalid(boolean z) {
        this.mIsCloudInvalid = z;
    }

    public boolean isCloudInvalid() {
        return this.mIsCloudInvalid;
    }

    public String getServerStatus() {
        return this.dbCloud.getServerStatus();
    }
}
