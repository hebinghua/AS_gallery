package com.miui.gallery.provider.cloudmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.cloud.DownloadPathHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.deleterecorder.DeleteRecord;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Arrays;
import java.util.HashSet;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class Util {
    public static ContentValues desensitization(ContentValues contentValues) {
        if (contentValues == null) {
            return null;
        }
        ContentValues contentValues2 = new ContentValues(contentValues);
        contentValues2.remove("address");
        contentValues2.remove("exifGPSLatitude");
        contentValues2.remove("exifGPSLongitude");
        contentValues2.remove("extraGPS");
        contentValues2.remove("location");
        return contentValues2;
    }

    public static Cursor queryCloudItemByFilePath(Context context, SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("CloudManager_Util", "queryCloudItemByFilePath"));
        if (documentFile == null || !documentFile.exists()) {
            return supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(Contracts.QUERY_BY_PATH_PROJECTION).selection("(localFile LIKE ? or thumbnailFile LIKE ?) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", new String[]{str, str}).create());
        }
        try {
            ExifUtil.UserCommentData userCommentData = ExifUtil.getUserCommentData(str);
            String sha1 = userCommentData != null ? userCommentData.getSha1() : null;
            String relativePath = StorageUtils.getRelativePath(context, BaseFileUtils.getParentFolderPath(str));
            if (TextUtils.isEmpty(relativePath)) {
                DefaultLogger.w("CloudManager_Util", "Could't get album path for %s", str);
                return null;
            } else if (TextUtils.isEmpty(sha1)) {
                SupportSQLiteQueryBuilder builder = SupportSQLiteQueryBuilder.builder("cloud");
                String[] strArr = Contracts.QUERY_BY_PATH_PROJECTION;
                Cursor query = supportSQLiteDatabase.query(builder.columns(strArr).selection("fileName LIKE ? AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND localGroupId IN (SELECT _id FROM album WHERE localPath LIKE ?)", new String[]{BaseFileUtils.getFileName(str), relativePath}).create());
                if (query != null && query.getCount() >= 1) {
                    return query;
                }
                ScanResult scanFile = ScannerEngine.getInstance().scanFile(context, str, 8);
                StorageSolutionProvider.get().apply(documentFile);
                return scanFile.getResult() == ScanResult.Result.SUCCESS ? supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(strArr).selection("fileName LIKE ? AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND localGroupId IN (SELECT _id FROM album WHERE localPath LIKE ?)", new String[]{BaseFileUtils.getFileName(str), relativePath}).create()) : query;
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean("is_thumbnail", true);
                Cursor query2 = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(Contracts.QUERY_BY_PATH_PROJECTION).selection("sha1=? AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND localGroupId IN (SELECT _id FROM album WHERE localPath LIKE ?)", new String[]{sha1, relativePath}).create());
                if (query2 instanceof AbstractCursor) {
                    query2.setExtras(bundle);
                }
                return query2;
            }
        } catch (Exception e) {
            DefaultLogger.w("CloudManager_Util", "exif exifSha1 read fail %s", e);
            return null;
        }
    }

    public static ContentValues copyOf(String[] strArr, Cursor cursor) {
        HashSet hashSet = new HashSet(Arrays.asList(strArr));
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            String columnName = cursor.getColumnName(i);
            if (hashSet.contains(columnName)) {
                if ("babyInfoJson".equals(columnName)) {
                    DefaultLogger.d("CloudManager_Util", "catch column(%s), remove local_flag ", columnName);
                    try {
                        String string = cursor.getString(i);
                        if (!TextUtils.isEmpty(string)) {
                            JSONObject jSONObject = new JSONObject(string);
                            if (jSONObject.has("localFlag")) {
                                jSONObject.remove("localFlag");
                            }
                            contentValues.put("babyInfoJson", jSONObject.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    int type = cursor.getType(i);
                    if (type == 1) {
                        contentValues.put(columnName, Long.valueOf(cursor.getLong(i)));
                    } else if (type == 2) {
                        contentValues.put(columnName, Double.valueOf(cursor.getDouble(i)));
                    } else if (type == 3) {
                        contentValues.put(columnName, cursor.getString(i));
                    } else if (type == 4) {
                        contentValues.put(columnName, cursor.getBlob(i));
                    } else {
                        contentValues.putNull(columnName);
                    }
                }
            }
        }
        return contentValues;
    }

    public static DeleteRecord createDeleteRecord(int i, Cursor cursor, String str) {
        if (cursor != null) {
            String string = cursor.getString(cursor.getColumnIndex("localFile"));
            if (TextUtils.isEmpty(string)) {
                string = cursor.getString(cursor.getColumnIndex("thumbnailFile"));
            }
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            return new DeleteRecord(i, string, str);
        }
        return null;
    }

    public static int getValidCount(long... jArr) {
        if (jArr != null) {
            int length = jArr.length;
            int i = 0;
            for (int i2 = 0; i2 < length; i2++) {
                i += jArr[i2] > 0 ? 1 : 0;
            }
            return i;
        }
        return 0;
    }

    public static String genRelativePath(String str, boolean z) {
        if (z) {
            return DownloadPathHelper.getShareFolderRelativePathInCloud();
        }
        return DownloadPathHelper.getFolderRelativePathInCloud(str);
    }

    public static String generatedNewName(String str, String str2) {
        String extensionWithFileName = BaseFileUtils.getExtensionWithFileName(str);
        String fileTitle = BaseFileUtils.getFileTitle(str);
        return fileTitle + "_" + str2.substring(0, 6) + "." + extensionWithFileName;
    }

    public static String generatedNewNameWithUUID(String str) {
        String extensionWithFileName = BaseFileUtils.getExtensionWithFileName(str);
        String fileTitle = BaseFileUtils.getFileTitle(str);
        String str2 = GalleryPreferences.UUID.get();
        return fileTitle + "_" + str2 + "." + extensionWithFileName;
    }
}
