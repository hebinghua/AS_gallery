package com.miui.gallery.sdk.download.downloader;

import android.accounts.Account;
import android.content.ContentValues;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.assist.RequestItem;
import com.miui.gallery.sdk.download.util.DownloadUtil;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.IncompatibleMediaType;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MicroThumbnailDownloader extends AbsThumbnailDownloader {
    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public int getFileType() {
        return 0;
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public String getInvokerTag() {
        return "MicroThumbnailDownloader";
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public int getRequestHeight() {
        return 250;
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public int getRequestWidth() {
        return 250;
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public String getTag() {
        return "MicroThumbnailDownloader";
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader, com.miui.gallery.sdk.download.downloader.IDownloader
    public /* bridge */ /* synthetic */ void download(Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, List list) {
        super.download(account, galleryExtendedAuthToken, list);
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public boolean updateDatabase(RequestItem requestItem, FileItem fileItem) {
        String[] strArr;
        DBImage dBImage = requestItem.mDBItem;
        String[] strArr2 = null;
        if (dBImage.isDeleteItem()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("microFilePath", fileItem.getPath());
            if (SafeDBUtil.safeUpdate(GalleryApp.sGetAndroidContext(), GalleryContract.TrashBin.TRASH_BIN_URI, contentValues, "cloudServerId=" + dBImage.getServerId(), (String[]) null) <= 0) {
                return false;
            }
        } else {
            File file = new File(fileItem.getPath());
            long dateModified = requestItem.mDBItem.getDateModified();
            if (dateModified > 0) {
                file.setLastModified(dateModified);
            } else {
                DefaultLogger.e("MicroThumbnailDownloader", "Negative modify time: %d, %s", Long.valueOf(dateModified), requestItem.mDBItem.getFileName());
            }
            int fileType = fileItem.getFileType();
            if (fileType != 0) {
                if (fileType == 1) {
                    strArr2 = new String[]{"microthumbfile", "thumbnailFile"};
                    strArr = new String[]{"", file.getAbsolutePath()};
                } else if (fileType != 2) {
                    strArr = null;
                } else {
                    strArr2 = new String[]{"microthumbfile", "localFile"};
                    strArr = new String[]{"", file.getAbsolutePath()};
                }
            } else if (IncompatibleMediaType.isUnsupportedMediaType(requestItem.mDBItem.getMimeType())) {
                strArr2 = new String[]{"microthumbfile"};
                strArr = new String[]{file.getAbsolutePath()};
            } else {
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                arrayList.add("microthumbfile");
                arrayList2.add(file.getAbsolutePath());
                if (!TextUtils.isEmpty(dBImage.getThumbnailFile())) {
                    arrayList.add("thumbnailFile");
                    arrayList2.add("");
                }
                if (!TextUtils.isEmpty(dBImage.getLocalFile())) {
                    arrayList.add("localFile");
                    arrayList2.add("");
                }
                String[] strArr3 = new String[arrayList.size()];
                String[] strArr4 = new String[arrayList2.size()];
                arrayList.toArray(strArr3);
                arrayList2.toArray(strArr4);
                strArr2 = strArr3;
                strArr = strArr4;
            }
            if (strArr2 != null && !CloudUtils.update(requestItem.mDBItem.getBaseUri(), strArr2, strArr, requestItem.mDBItem.getId(), true)) {
                return false;
            }
        }
        return true;
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public FileItem checkValidFile(RequestItem requestItem) {
        String checkAndReturnValidFilePathForType = DownloadUtil.checkAndReturnValidFilePathForType(requestItem.mDBItem, DownloadType.MICRO);
        if (!TextUtils.isEmpty(checkAndReturnValidFilePathForType)) {
            DefaultLogger.w("MicroThumbnailDownloader", "already exist micro thumbnail file");
            return new FileItem(0, checkAndReturnValidFilePathForType);
        }
        String checkAndReturnValidFilePathForType2 = DownloadUtil.checkAndReturnValidFilePathForType(requestItem.mDBItem, DownloadType.THUMBNAIL);
        if (!TextUtils.isEmpty(checkAndReturnValidFilePathForType2)) {
            DefaultLogger.w("MicroThumbnailDownloader", "already exist thumbnail file");
            return new FileItem(1, checkAndReturnValidFilePathForType2);
        }
        String checkAndReturnValidOriginalFilePath = DownloadUtil.checkAndReturnValidOriginalFilePath(requestItem.mDBItem, DownloadType.ORIGIN);
        if (!TextUtils.isEmpty(checkAndReturnValidOriginalFilePath)) {
            String mimeType = BaseFileMimeUtil.getMimeType(checkAndReturnValidOriginalFilePath);
            if (IncompatibleMediaType.isUnsupportedMediaType(mimeType)) {
                DefaultLogger.w("MicroThumbnailDownloader", "already exist original file, however unsupported type %s", mimeType);
                return FileItem.NONE;
            }
            DefaultLogger.w("MicroThumbnailDownloader", "already exist original file");
            return new FileItem(2, checkAndReturnValidOriginalFilePath);
        }
        return FileItem.NONE;
    }
}
