package com.miui.gallery.cloud;

import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.data.DBShareAlbum;
import com.miui.gallery.data.DBShareSubUbiImage;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.ExtraTextUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class DownloadPathHelper {
    public static String getDownloadFolderPath(DBImage dBImage) {
        String downloadFolderRelativePath = getDownloadFolderRelativePath(dBImage);
        return downloadFolderRelativePath == null ? "" : StorageUtils.getFilePathUnder(StorageUtils.getPriorStoragePath(), downloadFolderRelativePath);
    }

    public static String getDownloadFolderRelativePath(DBImage dBImage) {
        String localGroupId;
        String valueOf;
        String ownerAlbumLocalFile;
        if (dBImage == null) {
            DefaultLogger.e("DownloadPathHelper", "dbImage is null!");
            return null;
        }
        if (dBImage.isShareItem()) {
            ownerAlbumLocalFile = StorageConstants.RELATIVE_DIRECTORY_SHARER_ALBUM;
        } else if (dBImage.getServerType() == 0 && !TextUtils.isEmpty(dBImage.getLocalFile())) {
            ownerAlbumLocalFile = dBImage.getLocalFile();
        } else if (dBImage.getServerType() == 0 || !isTrashItem(dBImage.getServerStatus())) {
            if (dBImage.getServerType() == 0) {
                localGroupId = dBImage.getId();
                valueOf = dBImage.getServerId();
            } else {
                localGroupId = dBImage.getLocalGroupId();
                valueOf = String.valueOf(dBImage.getGroupId());
            }
            if (CloudTableUtils.isCameraGroup(valueOf)) {
                ownerAlbumLocalFile = AlbumDataHelper.getCameraLocalPath();
            } else if (CloudTableUtils.isScreenshotGroup(valueOf)) {
                ownerAlbumLocalFile = AlbumDataHelper.getScreenshotsLocalPath();
            } else if (CloudTableUtils.isSecretAlbum(valueOf, localGroupId)) {
                ownerAlbumLocalFile = "MIUI/Gallery/cloud/secretAlbum";
            } else if (dBImage.getServerType() == 0) {
                ownerAlbumLocalFile = AlbumDataHelper.getOwnerAlbumLocalFile(dBImage.getFileName(), dBImage.getAppKey());
            } else {
                DBAlbum albumById = AlbumDataHelper.getAlbumById(GalleryApp.sGetAndroidContext(), localGroupId, null);
                if (albumById != null) {
                    return !TextUtils.isEmpty(albumById.getLocalPath()) ? albumById.getLocalPath() : AlbumDataHelper.getOwnerAlbumLocalFile(albumById.getName(), albumById.getAppKey());
                }
                DefaultLogger.e("DownloadPathHelper", "dbImage is null!");
                return null;
            }
        } else {
            ownerAlbumLocalFile = "/Android/data/com.miui.gallery/files/trashBin";
        }
        if (!dBImage.isSubUbiFocus()) {
            return ownerAlbumLocalFile;
        }
        return ownerAlbumLocalFile + File.separator + ".ubifocus";
    }

    public static boolean isTrashItem(String str) {
        return "deleted".equalsIgnoreCase(str) || "purged".equalsIgnoreCase(str) || "toBePurged".equalsIgnoreCase(str);
    }

    public static String getFolderRelativePathInCloud(String str) {
        return StorageConstants.RELATIVE_DIRECTORY_OWNER_ALBUM + File.separator + str;
    }

    public static String getShareFolderRelativePathInCloud() {
        return StorageConstants.RELATIVE_DIRECTORY_SHARER_ALBUM;
    }

    public static String getOriginDownloadFileNameNotSecret(DBImage dBImage) {
        if (dBImage == null) {
            DefaultLogger.e("DownloadPathHelper", "dbImage is null!");
            return "";
        }
        return getDownloadFileNameNotSecret(dBImage, dBImage.getFileName());
    }

    public static String getThumbnailDownloadFileNameNotSecret(DBImage dBImage) {
        if (dBImage == null) {
            DefaultLogger.e("DownloadPathHelper", "dbImage is null!");
            return "";
        }
        String fileTitle = BaseFileUtils.getFileTitle(getDownloadFileNameNotSecret(dBImage, dBImage.getFileName()));
        return fileTitle + ".jpg";
    }

    public static String getDownloadFileNameNotSecret(DBImage dBImage, String str) {
        if (dBImage == null) {
            DefaultLogger.e("DownloadPathHelper", "dbImage is null!");
            return "";
        } else if (!dBImage.isShareItem()) {
            return str;
        } else {
            if (dBImage.isSubUbiFocus()) {
                dBImage = CloudUtils.getItem(GalleryCloudUtils.SHARE_IMAGE_URI, GalleryApp.sGetAndroidContext(), j.c, ((DBShareSubUbiImage) dBImage).getUbiLocalId());
                if (dBImage == null) {
                    DefaultLogger.e("DownloadPathHelper", "mainDBImage is null!");
                    return "";
                }
            }
            String albumId = dBImage.getAlbumId();
            if (TextUtils.isEmpty(albumId)) {
                DBShareAlbum dBShareAlbumByLocalId = CloudUtils.getDBShareAlbumByLocalId(dBImage.getLocalGroupId());
                if (dBShareAlbumByLocalId == null) {
                    DefaultLogger.e("DownloadPathHelper", "dbShareAlbum should not be null!");
                    return "";
                }
                albumId = dBShareAlbumByLocalId.getAlbumId();
            }
            return addPostfixToFileName(str, albumId);
        }
    }

    public static String addPostfixToFileName(String str, String str2) {
        String str3 = BaseFileUtils.getFileTitle(str) + "_" + str2;
        String extension = BaseFileUtils.getExtension(str);
        if (TextUtils.isEmpty(extension)) {
            return str3;
        }
        return str3 + "." + extension;
    }

    public static String getFilePathForRead(String[] strArr, String str) {
        if (strArr != null) {
            for (String str2 : strArr) {
                String str3 = str2 + File.separator + str;
                if (new File(str3).exists()) {
                    return str3;
                }
            }
            return "";
        }
        return "";
    }

    public static boolean isShareFolderRelativePath(String str) {
        return ExtraTextUtils.startsWithIgnoreCase(str, StorageConstants.RELATIVE_DIRECTORY_SHARER_ALBUM);
    }

    public static List<String> getAllFilePathForRead(String[] strArr, String str) {
        ArrayList arrayList = new ArrayList();
        if (strArr != null) {
            for (String str2 : strArr) {
                String str3 = str2 + File.separator + str;
                if (new File(str3).exists()) {
                    arrayList.add(str3);
                }
            }
        }
        return arrayList;
    }

    public static String getFilePathForRead(String str, String str2) {
        return str == null ? "" : getFilePathForRead(StorageUtils.getPathsInExternalStorage(GalleryApp.sGetAndroidContext(), str), str2);
    }
}
