package com.miui.gallery.sdk.download.downloader;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.BaseConfig$ScreenConfig;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.ThumbnailMetaWriter;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.assist.RequestItem;
import com.miui.gallery.sdk.download.util.DownloadUtil;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class ThumbnailDownloader extends AbsThumbnailDownloader {
    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public int getFileType() {
        return 1;
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public String getInvokerTag() {
        return "ThumbnailDownloader";
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public String getTag() {
        return "ThumbnailDownloader";
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader, com.miui.gallery.sdk.download.downloader.IDownloader
    public /* bridge */ /* synthetic */ void download(Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, List list) {
        super.download(account, galleryExtendedAuthToken, list);
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public boolean updateDatabase(RequestItem requestItem, FileItem fileItem) {
        DBImage dBImage = requestItem.mDBItem;
        if (dBImage.isDeleteItem()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("isOrigin", Boolean.FALSE);
            contentValues.put("trashFilePath", fileItem.getPath());
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            Uri uri = GalleryContract.TrashBin.TRASH_BIN_URI;
            StringBuilder sb = new StringBuilder();
            sb.append("cloudServerId=");
            sb.append(dBImage.getServerId());
            return SafeDBUtil.safeUpdate(sGetAndroidContext, uri, contentValues, sb.toString(), (String[]) null) > 0;
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(fileItem.getPath(), IStoragePermissionStrategy.Permission.UPDATE, "updateDatabase");
        long dateModified = requestItem.mDBItem.getDateModified();
        if (dateModified > 0 && documentFile != null) {
            StorageSolutionProvider.get().setLastModified(documentFile, dateModified);
        } else {
            DefaultLogger.e("ThumbnailDownloader", "Negative modify time: %d, %s", Long.valueOf(dateModified), requestItem.mDBItem.getFileName());
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int fileType = fileItem.getFileType();
        if (fileType == 1) {
            arrayList.add("thumbnailFile");
            arrayList2.add(fileItem.getPath());
            if (!TextUtils.isEmpty(dBImage.getLocalFile())) {
                arrayList.add("localFile");
                arrayList2.add("");
            }
        } else if (fileType == 2) {
            arrayList.add("localFile");
            arrayList2.add(fileItem.getPath());
            if (!TextUtils.isEmpty(dBImage.getThumbnailFile())) {
                arrayList.add("thumbnailFile");
                arrayList2.add("");
            }
        }
        if (!dBImage.isShareItem()) {
            arrayList.add("realSize");
            arrayList2.add(String.valueOf(documentFile.length()));
            arrayList.add("realDateModified");
            arrayList2.add(String.valueOf(documentFile.lastModified()));
        }
        boolean update = arrayList.size() > 0 ? CloudUtils.update(requestItem.mDBItem.getBaseUri(), (String[]) arrayList.toArray(new String[arrayList.size()]), (String[]) arrayList2.toArray(new String[arrayList2.size()]), requestItem.mDBItem.getId(), false) : false;
        GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.Album.URI, (ContentObserver) null, false);
        return update;
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public int getRequestWidth() {
        return BaseConfig$ScreenConfig.getScreenWidth();
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public int getRequestHeight() {
        return BaseConfig$ScreenConfig.getScreenHeight();
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public FileItem checkValidFile(RequestItem requestItem) {
        FileItem fileItem = FileItem.NONE;
        String checkAndReturnValidFilePathForType = DownloadUtil.checkAndReturnValidFilePathForType(requestItem.mDBItem, DownloadType.THUMBNAIL);
        boolean z = true;
        if (!TextUtils.isEmpty(checkAndReturnValidFilePathForType)) {
            DefaultLogger.w("ThumbnailDownloader", "already exist thumbnail file");
            fileItem = new FileItem(1, checkAndReturnValidFilePathForType);
        } else {
            checkAndReturnValidFilePathForType = DownloadUtil.checkAndReturnValidOriginalFilePath(requestItem.mDBItem, DownloadType.ORIGIN);
            if (!TextUtils.isEmpty(checkAndReturnValidFilePathForType)) {
                DefaultLogger.w("ThumbnailDownloader", "already exist original file");
                fileItem = new FileItem(2, checkAndReturnValidFilePathForType);
            }
        }
        if (!TextUtils.isEmpty(checkAndReturnValidFilePathForType)) {
            if (requestItem.mDownloadItem.isManual() && CloudControlStrategyHelper.getSyncStrategy().isFrontForManualDownload()) {
                z = false;
            }
            if (z) {
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(checkAndReturnValidFilePathForType, IStoragePermissionStrategy.Permission.UPDATE, FileHandleRecordHelper.appendInvokerTag(getInvokerTag(), "checkValidFile"));
                if (requestItem.mDBItem.getMixedDateTime() > 0 && documentFile != null && documentFile.exists() && !StorageSolutionProvider.get().setLastModified(documentFile, requestItem.mDBItem.getMixedDateTime())) {
                    DefaultLogger.w("ThumbnailDownloader", "failed to set last modified time for file [%s]", checkAndReturnValidFilePathForType);
                }
            } else {
                DefaultLogger.d("ThumbnailDownloader", "front for manual downloading %s", requestItem.mDBItem.getFileName());
            }
        }
        return fileItem;
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public boolean handleDownloadTempFile(RequestItem requestItem, String str) {
        return new ThumbnailMetaWriter(requestItem.mDBItem).write(str);
    }
}
