package com.miui.gallery.provider.cloudmanager.handleFile;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.provider.cloudmanager.remark.RemarkManager;
import com.miui.gallery.provider.cloudmanager.remark.info.RemarkInfoFactory;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.trash.TrashBinItem;
import com.miui.gallery.trash.TrashManager;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.deleterecorder.DeleteRecord;
import com.miui.gallery.util.deleterecorder.DeleteRecorder;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class MediaFileHandleJob {
    public Params mParams;

    public MediaFileHandleJob() {
    }

    public MediaFileHandleJob(Params params) {
        this.mParams = params;
    }

    /* loaded from: classes2.dex */
    public static class Params {
        public String mAlbumDir;
        public String mFileName;
        public long mId;
        public String mInvokerTag;
        public String mLocalFile;
        public int mLocalFlag;
        public int mReason;
        public String mServerAlbumId;
        public String mServerId;
        public String mThumbnail;
        public TrashBinItem mTrashBinItem;

        public String toString() {
            return "Params{mId=" + this.mId + ", mServerId='" + this.mServerId + CoreConstants.SINGLE_QUOTE_CHAR + ", mServerAlbumId='" + this.mServerAlbumId + CoreConstants.SINGLE_QUOTE_CHAR + ", mLocalFlag=" + this.mLocalFlag + ", mThumbnail='" + this.mThumbnail + CoreConstants.SINGLE_QUOTE_CHAR + ", mLocalFile='" + this.mLocalFile + CoreConstants.SINGLE_QUOTE_CHAR + ", mAlbumDir='" + this.mAlbumDir + CoreConstants.SINGLE_QUOTE_CHAR + ", mFileName='" + this.mFileName + CoreConstants.SINGLE_QUOTE_CHAR + ", mTrashBinItem=" + this.mTrashBinItem + ", mReason=" + this.mReason + ", mInvokerTag=" + this.mInvokerTag + '}';
        }
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public long albumAttributes;
        public String albumName;
        public final Params params = new Params();

        public final boolean needMoveToTrash(int i) {
            return (i == 58 || i == 59 || i == 36) ? false : true;
        }

        public Builder setParams(ContentResolver contentResolver, Cursor cursor, long j, int i, String str) {
            boolean z = false;
            this.params.mId = cursor.getLong(0);
            this.params.mLocalFlag = cursor.getInt(2);
            this.params.mLocalFile = cursor.getString(7);
            this.params.mThumbnail = cursor.getString(8);
            this.params.mFileName = cursor.getString(6);
            this.params.mServerId = cursor.getString(4);
            this.params.mReason = i;
            this.params.mInvokerTag = str;
            long j2 = cursor.getLong(3);
            int i2 = cursor.getInt(5);
            boolean isOtherShareMediaId = ShareMediaManager.isOtherShareMediaId(j);
            if (needMoveToTrash(i) && ((i2 == 1 || i2 == 2) && !TextUtils.equals(cursor.getString(23), "cleanLocal") && !isOtherShareMediaId)) {
                z = true;
            }
            if (j2 == -1000) {
                this.params.mAlbumDir = "MIUI/Gallery/cloud/secretAlbum";
            } else {
                queryAlbumInfo(contentResolver, isOtherShareMediaId, j2);
            }
            if (z) {
                TrashBinItem trashBinItem = new TrashBinItem(this.params.mFileName, this.params.mId, this.params.mServerId, cursor.getString(9), j2, this.albumName, this.params.mServerAlbumId, this.params.mAlbumDir, this.albumAttributes, cursor.getLong(15));
                trashBinItem.setDuration(cursor.getLong(21));
                trashBinItem.setDeleteTime(System.currentTimeMillis());
                trashBinItem.setSize(cursor.getLong(15));
                trashBinItem.setImageHeight(cursor.getInt(31));
                trashBinItem.setImageWidth(cursor.getInt(30));
                trashBinItem.setOrientation(cursor.getInt(32));
                trashBinItem.setMimeType(cursor.getString(17));
                trashBinItem.setMicroPath(cursor.getString(12));
                trashBinItem.setMixedDateTime(cursor.getLong(29));
                trashBinItem.setServerTag(cursor.getLong(22));
                byte[] blob = cursor.getBlob(11);
                if (blob != null) {
                    trashBinItem.setSecretKey(blob);
                }
                if (AccountCache.getAccount() != null) {
                    trashBinItem.setCreatorId(AccountCache.getAccount().name);
                }
                this.params.mTrashBinItem = trashBinItem;
            }
            DefaultLogger.d("galleryAction_FileHandle_FileHandleJob", "Builder file handle job : " + this.params.toString());
            return this;
        }

        public final void queryAlbumInfo(ContentResolver contentResolver, boolean z, long j) {
            Cursor cursor = null;
            try {
                if (z) {
                    Cursor query = contentResolver.query(GalleryContract.ShareAlbum.OTHER_SHARE_URI, new String[]{"albumId"}, "_id=?", new String[]{String.valueOf(j)}, null);
                    if (query != null) {
                        try {
                            if (query.moveToFirst()) {
                                this.params.mServerAlbumId = query.getString(0);
                            }
                        } catch (Throwable th) {
                            th = th;
                            cursor = query;
                            if (cursor != null) {
                                cursor.close();
                            }
                            throw th;
                        }
                    }
                    this.params.mAlbumDir = Util.genRelativePath(null, true);
                    Params params = this.params;
                    params.mId = ShareMediaManager.convertToMediaId(params.mId);
                    if (query == null) {
                        return;
                    }
                    query.close();
                    return;
                }
                Cursor query2 = contentResolver.query(GalleryContract.Album.URI, TrashManager.TRASH_ITEM_ALBUM_PROJECTION_INFO, "_id=?", new String[]{String.valueOf(j)}, null);
                if (query2 != null && query2.moveToFirst()) {
                    this.params.mAlbumDir = query2.getString(0);
                    this.albumAttributes = query2.getLong(2);
                    if (TextUtils.isEmpty(this.params.mAlbumDir)) {
                        String string = query2.getString(1);
                        this.albumName = string;
                        this.params.mAlbumDir = Util.genRelativePath(string, false);
                    }
                }
                if (query2 == null) {
                    return;
                }
                query2.close();
            } catch (Throwable th2) {
                th = th2;
            }
        }

        public MediaFileHandleJob build() {
            return new MediaFileHandleJob(this.params);
        }
    }

    public boolean handle(Context context) {
        int i = this.mParams.mLocalFlag;
        boolean z = false;
        if (i != -2) {
            if (i == -1) {
                z = true;
            } else if (i != 2 && i != 11) {
                if (i != 17 && i != 18) {
                    switch (i) {
                        case 5:
                        case 7:
                        case 8:
                            break;
                        case 6:
                        case 9:
                            break;
                        default:
                            return false;
                    }
                }
                doMove(context);
                return true;
            }
            doDelete(context, z);
            return !z;
        }
        z = true;
        doCopy(context, z);
        return true;
    }

    public final void doMove(Context context) {
        DefaultLogger.d("galleryAction_FileHandle_FileHandleJob", "doMove => ");
        editFile(context, false, false);
    }

    public final void doCopy(Context context, boolean z) {
        DefaultLogger.d("galleryAction_FileHandle_FileHandleJob", "doCopy => ");
        editFile(context, true, z);
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x010e  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x015e  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x01d4  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0202  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void editFile(android.content.Context r20, boolean r21, boolean r22) {
        /*
            Method dump skipped, instructions count: 653
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.cloudmanager.handleFile.MediaFileHandleJob.editFile(android.content.Context, boolean, boolean):void");
    }

    public final boolean isInSameFileDir(File file, File file2) {
        if (file == null || file2 == null) {
            return false;
        }
        return TextUtils.equals(file.getParent(), file2.getParent());
    }

    public final File getDesFile(Context context, File file) {
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("galleryAction_FileHandle_FileHandleJob", "getDesFile");
        String relativePath = StorageUtils.getRelativePath(context, file.getParent());
        if (!TextUtils.isEmpty(relativePath)) {
            if (relativePath.equalsIgnoreCase(this.mParams.mAlbumDir)) {
                DefaultLogger.e("galleryAction_FileHandle_FileHandleJob", "skip copy localFile(%s), album(%s)", relativePath, this.mParams.mAlbumDir);
                return file;
            }
            String name = file.getName();
            boolean isOtherShareMediaId = ShareMediaManager.isOtherShareMediaId(this.mParams.mId);
            if (isOtherShareMediaId) {
                name = ShareMediaManager.getMediaFileName(name, this.mParams.mServerAlbumId);
                DefaultLogger.d("galleryAction_FileHandle_FileHandleJob", "Other shared dest filename %s", name);
            }
            String pathInPriorStorage = StorageUtils.getPathInPriorStorage(this.mParams.mAlbumDir);
            if (pathInPriorStorage == null || StorageSolutionProvider.get().getDocumentFile(pathInPriorStorage, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, appendInvokerTag) == null) {
                return null;
            }
            StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
            StringBuilder sb = new StringBuilder();
            sb.append(pathInPriorStorage);
            String str = File.separator;
            sb.append(str);
            sb.append(name);
            DocumentFile documentFile = storageStrategyManager.getDocumentFile(sb.toString(), IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
            if (documentFile == null || !documentFile.exists()) {
                return new File(pathInPriorStorage + str + name);
            }
            if (documentFile.exists()) {
                DefaultLogger.w("galleryAction_FileHandle_FileHandleJob", "file system name conflict found for [%s].", pathInPriorStorage + str + name);
                File forceCreate = FileUtils.forceCreate(pathInPriorStorage, name, 0);
                StorageStrategyManager storageStrategyManager2 = StorageSolutionProvider.get();
                if (storageStrategyManager2.moveFile(pathInPriorStorage + str + name, forceCreate.getAbsolutePath(), appendInvokerTag)) {
                    ContentValues contentValues = new ContentValues();
                    if (isOtherShareMediaId) {
                        contentValues.put("thumbnailFile", forceCreate.getAbsolutePath());
                        ContentResolver contentResolver = context.getContentResolver();
                        Uri uri = GalleryContract.ShareImage.SHARE_URI;
                        contentResolver.update(uri, contentValues, "thumbnailFile=?", new String[]{pathInPriorStorage + str + name});
                    } else {
                        contentValues.put("thumbnailFile", forceCreate.getAbsolutePath());
                        ContentResolver contentResolver2 = context.getContentResolver();
                        Uri uri2 = GalleryContract.Cloud.CLOUD_URI;
                        contentResolver2.update(uri2, contentValues, "thumbnailFile=?", new String[]{pathInPriorStorage + str + name});
                    }
                }
                if (!documentFile.exists()) {
                    return new File(pathInPriorStorage + str + name);
                }
            }
            return null;
        }
        DefaultLogger.e("galleryAction_FileHandle_FileHandleJob", "Could't get relative path for %s", file.getParent());
        return null;
    }

    public final void doDelete(Context context, boolean z) {
        Params params;
        if (context == null || (params = this.mParams) == null) {
            DefaultLogger.e("galleryAction_FileHandle_FileHandleJob", "doDelete => file path is empty !");
        } else {
            doDelete(true, context, params.mAlbumDir, z, this.mParams.mId, true, this.mParams.mTrashBinItem, this.mParams.mServerId, this.mParams.mLocalFlag, this.mParams.mLocalFile, this.mParams.mThumbnail, true, this.mParams.mReason, this.mParams.mInvokerTag);
        }
    }

    public static void doDelete(Context context, Cursor cursor, long j, int i, boolean z, String str) {
        new Builder().setParams(context.getContentResolver(), cursor, j, i, str).build().doDelete(context, z);
    }

    public static boolean doDelete(String str, int i, String str2) {
        return new MediaFileHandleJob().doDelete(false, null, null, false, -1L, false, null, null, -1, str, null, i > 0, i, str2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final boolean doDelete(boolean z, Context context, String str, boolean z2, long j, boolean z3, TrashBinItem trashBinItem, String str2, int i, String str3, String str4, boolean z4, int i2, String str5) {
        int i3;
        boolean z5;
        Context context2 = context;
        String str6 = TextUtils.isEmpty(str3) ? str4 : str3;
        int i4 = 0;
        if (TextUtils.isEmpty(str6) && j <= 0) {
            DefaultLogger.e("galleryAction_FileHandle_FileHandleJob", "doDelete => file path is empty and cloud id is invalid!");
            return false;
        } else if (RemarkManager.isUnHandleMedia(RemarkInfoFactory.getInventedPath(j, str6), 1003)) {
            DefaultLogger.e("galleryAction_FileHandle_FileHandleJob", "doDelete => file [%s] is not move success, don't delete!!!", str6);
            return false;
        } else {
            Object[] objArr = new Object[5];
            objArr[0] = str6 == null ? "" : str6;
            objArr[1] = Integer.valueOf(i2);
            objArr[2] = str5;
            objArr[3] = Boolean.valueOf(z3);
            objArr[4] = Boolean.valueOf(z4);
            DefaultLogger.d("galleryAction_FileHandle_FileHandleJob", "doDelete => path [%s] deleteReason [%d] tag [%s] isNeedMoveToTrash [%s] isNeedRecordReason [%s]", objArr);
            if (!z3 || (TextUtils.isEmpty(str6) && TextUtils.isEmpty(str2))) {
                i3 = 1;
                z5 = false;
            } else {
                i3 = 1;
                z5 = moveToTrash(trashBinItem, str2, i, str3, str4, i2, str5);
            }
            if (!z5 && !TextUtils.isEmpty(str6)) {
                if (z && context2 != null && !TextUtils.isEmpty(str)) {
                    z5 = checkDeleteFile(context2, str6, str);
                } else {
                    z5 = deleteFile(str6);
                }
                if (z4 && z5) {
                    DeleteRecorder.getInstance().record(new DeleteRecord(i2, str6, str5));
                }
            }
            if (z2) {
                if (context2 == null) {
                    context2 = GalleryApp.sGetAndroidContext();
                }
                if (context2 != null) {
                    if (ShareMediaManager.isOtherShareMediaId(j)) {
                        long originalMediaId = ShareMediaManager.getOriginalMediaId(j);
                        ContentResolver contentResolver = context2.getContentResolver();
                        Uri uri = GalleryContract.ShareImage.SHARE_URI;
                        String[] strArr = new String[i3];
                        strArr[0] = String.valueOf(originalMediaId);
                        if (contentResolver.delete(uri, "_id=?", strArr) > 0) {
                            i4 = i3;
                        }
                        DefaultLogger.d("galleryAction_FileHandle_FileHandleJob", "doDelete => delete share db [%d] success", Long.valueOf(originalMediaId));
                    } else {
                        ContentResolver contentResolver2 = context2.getContentResolver();
                        Uri uri2 = GalleryContract.Cloud.CLOUD_URI;
                        String[] strArr2 = new String[i3];
                        strArr2[0] = String.valueOf(j);
                        if (contentResolver2.delete(uri2, "_id=?", strArr2) > 0) {
                            i4 = i3;
                        }
                        DefaultLogger.d("galleryAction_FileHandle_FileHandleJob", "doDelete => delete owner db [%d] success", Long.valueOf(j));
                    }
                    z5 = i4;
                }
            }
            if (!TextUtils.isEmpty(str3)) {
                long lastModified = new File(str3).lastModified();
                long currentTimeMillis = (System.currentTimeMillis() - lastModified) / 1000;
                if (lastModified > 0 && currentTimeMillis < 60) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("state", String.valueOf(z2));
                    hashMap.put("elapse_time", String.valueOf(currentTimeMillis));
                    SamplingStatHelper.recordCountEvent("Sync", "sync_photo_delete_in_one_minute", hashMap);
                }
            }
            return z5;
        }
    }

    public static boolean moveToTrash(TrashBinItem trashBinItem, String str, int i, String str2, String str3, int i2, String str4) {
        boolean z = false;
        if (trashBinItem == null) {
            return false;
        }
        DefaultLogger.d("galleryAction_FileHandle_FileHandleJob", "moveToTrash => ");
        if ((!TextUtils.isEmpty(str) && i == 2) || TextUtils.isEmpty(str)) {
            if (!TextUtils.isEmpty(str2)) {
                trashBinItem.setIsOrigin(1);
            } else {
                trashBinItem.setIsOrigin(0);
                str2 = str3;
            }
            if (!TextUtils.isEmpty(str2)) {
                try {
                    TrashManager.SimpleResult moveFileToTrash = TrashManager.moveFileToTrash(str2, i2, str4);
                    if (moveFileToTrash != null) {
                        z = moveFileToTrash.isOriginFileDeleted();
                        trashBinItem.setTrashFilePath(moveFileToTrash.getTrashPath());
                    }
                } catch (StoragePermissionMissingException unused) {
                    DefaultLogger.e("galleryAction_FileHandle_FileHandleJob", "move file to trash failed for permission missing");
                }
            }
            trashBinItem.setInvokerTag(str4);
            TrashManager.getInstance().addTrashBinItem(trashBinItem);
        }
        return z;
    }

    public static boolean checkDeleteFile(Context context, String str, String str2) {
        File file = new File(str);
        String relativePath = StorageUtils.getRelativePath(context, file.getParent());
        if (TextUtils.isEmpty(relativePath) || !relativePath.equalsIgnoreCase(str2)) {
            DefaultLogger.e("galleryAction_FileHandle_FileHandleJob", "fileParent [%s] isEmpty or no equals [%s]", file.getParent(), str2);
            return false;
        }
        return deleteFile(str);
    }

    public static boolean deleteFile(String str) {
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("galleryAction_FileHandle_FileHandleJob", "deleteFile"));
        if (documentFile != null && documentFile.exists()) {
            boolean delete = documentFile.delete();
            if (!delete) {
                DefaultLogger.e("galleryAction_FileHandle_FileHandleJob", "deleteFile => delete [%s] fail, check SAF", str);
            } else {
                DefaultLogger.i("galleryAction_FileHandle_FileHandleJob", "deleteFile => delete [%s] success !", str);
            }
            return delete;
        }
        DefaultLogger.w("galleryAction_FileHandle_FileHandleJob", "deleteFile => delete [%s] fail ! not exist or has no permission, skip.", str);
        return false;
    }
}
