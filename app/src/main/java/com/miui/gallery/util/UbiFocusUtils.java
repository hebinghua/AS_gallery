package com.miui.gallery.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.google.common.collect.Lists;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.data.DBCloud;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.data.DBOwnerSubUbiImage;
import com.miui.gallery.data.DBShareImage;
import com.miui.gallery.data.DBShareSubUbiImage;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class UbiFocusUtils {

    /* loaded from: classes2.dex */
    public static class UbiQuery {
        public String[] queryArgs;
        public String queryString;

        public boolean isValid() {
            String[] strArr;
            return !TextUtils.isEmpty(this.queryString) && (strArr = this.queryArgs) != null && strArr.length > 0;
        }
    }

    /* loaded from: classes2.dex */
    public static class SubUbiQuery extends UbiQuery {
        public SubUbiQuery(String str, String str2, String str3) {
            if (!TextUtils.isEmpty(str) || !TextUtils.isEmpty(str2)) {
                boolean z = !TextUtils.isEmpty(str3);
                String str4 = " AND ubiSubIndex = ? ";
                if (TextUtils.isEmpty(str)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("ubiLocalId = ? ");
                    sb.append(!z ? "" : str4);
                    this.queryString = sb.toString();
                    this.queryArgs = z ? new String[]{str2, str3} : new String[]{str2};
                } else if (TextUtils.isEmpty(str2)) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("ubiServerId = ? ");
                    sb2.append(!z ? "" : str4);
                    this.queryString = sb2.toString();
                    this.queryArgs = z ? new String[]{str, str3} : new String[]{str};
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(" ( ubiServerId = ? OR ubiLocalId = ? ) ");
                    sb3.append(!z ? "" : str4);
                    this.queryString = sb3.toString();
                    this.queryArgs = z ? new String[]{str, str2, str3} : new String[]{str, str2};
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class MainUbiQuery extends UbiQuery {
        public MainUbiQuery(boolean z, String str, String str2) {
            if (!TextUtils.isEmpty(str) || !TextUtils.isEmpty(str2)) {
                String str3 = z ? "shareId" : "serverId";
                if (TextUtils.isEmpty(str)) {
                    this.queryString = "_id = ? ";
                    this.queryArgs = new String[]{str2};
                } else if (TextUtils.isEmpty(str2)) {
                    this.queryString = str3 + " = ? ";
                    this.queryArgs = new String[]{str};
                } else {
                    this.queryString = " ( " + str3 + " = ? OR " + j.c + " = ? ) ";
                    this.queryArgs = new String[]{str, str2};
                }
            }
        }
    }

    public static void handleSubUbiImage(JSONObject jSONObject, boolean z, String str) throws JSONException {
        String string;
        if (z) {
            string = jSONObject.getString("shareId");
        } else {
            string = jSONObject.getString("id");
        }
        handleSubUbiImage(jSONObject, z, string, str);
    }

    public static void handleSubUbiImage(JSONObject jSONObject, boolean z, String str, String str2) throws JSONException {
        JSONObject optJSONObject;
        Uri uri;
        if (!jSONObject.optBoolean("isUbiImage")) {
            return;
        }
        JSONObject optJSONObject2 = jSONObject.optJSONObject("ubiSubImageContentMap");
        int optInt = jSONObject.optInt("ubiSubImageCount");
        int optInt2 = jSONObject.optInt("ubiDefaultIndex");
        if (optJSONObject2 == null) {
            return;
        }
        for (int i = 0; i <= optInt; i++) {
            if (i != optInt2 && (optJSONObject = optJSONObject2.optJSONObject(String.valueOf(i))) != null) {
                ContentValues contentValuesForAllAndThumbNull = CloudUtils.getContentValuesForAllAndThumbNull(optJSONObject);
                contentValuesForAllAndThumbNull.put("ubiServerId", str);
                contentValuesForAllAndThumbNull.put("ubiSubIndex", Integer.valueOf(i));
                if (!TextUtils.isEmpty(str2)) {
                    contentValuesForAllAndThumbNull.put("ubiLocalId", str2);
                }
                if (jSONObject.has("groupId")) {
                    contentValuesForAllAndThumbNull.put("groupId", jSONObject.getString("groupId"));
                }
                if (z && jSONObject.has("shareId")) {
                    contentValuesForAllAndThumbNull.put("shareId", jSONObject.getString("shareId"));
                }
                if (z) {
                    uri = GalleryCloudUtils.SHARE_SUB_UBIFOCUS_URI;
                } else {
                    uri = GalleryCloudUtils.OWNER_SUB_UBIFOCUS_URI;
                }
                synchronized (uri) {
                    DBImage subUbiImage = getSubUbiImage(z, str, str2, i);
                    if (subUbiImage == null) {
                        DBImage subUbiImage2 = getSubUbiImage(z, contentValuesForAllAndThumbNull.getAsString("fileName"));
                        if (subUbiImage2 != null) {
                            try {
                                try {
                                    CloudUtils.renameAnItemInLocal(subUbiImage2);
                                } catch (StoragePermissionMissingException unused) {
                                    SyncLogger.w("UbiFocusUtils", "skip file name conflict for [%s] since storage permission missing.", subUbiImage2.getFileName());
                                }
                            } catch (StoragePermissionMissingException unused2) {
                                SyncLogger.w("UbiFocusUtils", "delete file and mask db for [%s] since storage permission missing.", subUbiImage2.getFileName());
                                CloudUtils.markAndDeleteFile(subUbiImage2, null);
                            }
                        }
                        safeInsertSubUbiImage(z, contentValuesForAllAndThumbNull);
                    } else {
                        safeUpdateSubUbiImage(z, contentValuesForAllAndThumbNull, str, str2, i);
                        try {
                            try {
                                CloudUtils.renameItemIfNeeded(subUbiImage, contentValuesForAllAndThumbNull);
                            } catch (StoragePermissionMissingException unused3) {
                                SyncLogger.w("UbiFocusUtils", "delete file and mask db for [%s] since storage permission missing.", subUbiImage.getFileName());
                                CloudUtils.markAndDeleteFile(subUbiImage, contentValuesForAllAndThumbNull);
                            }
                        } catch (StoragePermissionMissingException unused4) {
                            SyncLogger.w("UbiFocusUtils", "skip file name conflict for [%s] since storage permission missing.", subUbiImage.getFileName());
                        }
                    }
                }
            }
        }
    }

    public static DBImage getSubUbiImage(final boolean z, String str) {
        Uri uri;
        if (z) {
            uri = GalleryCloudUtils.SHARE_SUB_UBIFOCUS_URI;
        } else {
            uri = GalleryCloudUtils.OWNER_SUB_UBIFOCUS_URI;
        }
        return (DBImage) GalleryUtils.safeQuery(uri, CloudUtils.getProjectionAll(), String.format(Locale.US, "%s=?", "fileName"), new String[]{str}, (String) null, new GalleryUtils.QueryHandler<DBImage>() { // from class: com.miui.gallery.util.UbiFocusUtils.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
            /* renamed from: handle */
            public DBImage mo1712handle(Cursor cursor) {
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                if (z) {
                    return new DBShareSubUbiImage(cursor);
                }
                return new DBOwnerSubUbiImage(cursor);
            }
        });
    }

    public static void safeInsertSubUbiImage(boolean z, ContentValues contentValues) {
        Uri uri;
        if (z) {
            uri = GalleryCloudUtils.SHARE_SUB_UBIFOCUS_URI;
        } else {
            uri = GalleryCloudUtils.OWNER_SUB_UBIFOCUS_URI;
        }
        GalleryUtils.safeInsert(uri, contentValues);
    }

    public static void safeUpdateSubUbiImage(boolean z, ContentValues contentValues, String str, String str2, int i) {
        Uri uri;
        SubUbiQuery subUbiQuery = new SubUbiQuery(str, str2, String.valueOf(i));
        if (!subUbiQuery.isValid()) {
            DefaultLogger.e("UbiFocusUtils", "safeUpdateSubUbiImage, subUbiQuery.isValid.");
            return;
        }
        if (z) {
            uri = GalleryCloudUtils.SHARE_SUB_UBIFOCUS_URI;
        } else {
            uri = GalleryCloudUtils.OWNER_SUB_UBIFOCUS_URI;
        }
        GalleryUtils.safeUpdate(uri, contentValues, subUbiQuery.queryString, subUbiQuery.queryArgs);
    }

    public static String getUbiServerIdByUbiLocalId(String str, boolean z) {
        Uri uri;
        if (z) {
            uri = GalleryCloudUtils.SHARE_IMAGE_URI;
        } else {
            uri = GalleryCloudUtils.CLOUD_URI;
        }
        return CloudUtils.getStringColumnValue(uri, z ? "shareId" : "serverId", j.c, str);
    }

    public static DBImage getSubUbiImage(final boolean z, String str, String str2, int i) {
        Uri uri;
        SubUbiQuery subUbiQuery = new SubUbiQuery(str, str2, String.valueOf(i));
        if (!subUbiQuery.isValid()) {
            DefaultLogger.e("UbiFocusUtils", "getSubUbiImage, subUbiQuery.isValid.");
            return null;
        }
        if (z) {
            uri = GalleryCloudUtils.SHARE_SUB_UBIFOCUS_URI;
        } else {
            uri = GalleryCloudUtils.OWNER_SUB_UBIFOCUS_URI;
        }
        return (DBImage) GalleryUtils.safeQuery(uri, CloudUtils.getProjectionAll(), subUbiQuery.queryString, subUbiQuery.queryArgs, (String) null, new GalleryUtils.QueryHandler<DBImage>() { // from class: com.miui.gallery.util.UbiFocusUtils.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
            /* renamed from: handle */
            public DBImage mo1712handle(Cursor cursor) {
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                if (z) {
                    return new DBShareSubUbiImage(cursor);
                }
                return new DBOwnerSubUbiImage(cursor);
            }
        });
    }

    public static <T> T queryForSubUbiImages(boolean z, String str, String str2, GalleryUtils.QueryHandler<T> queryHandler) {
        Uri uri;
        SubUbiQuery subUbiQuery = new SubUbiQuery(str, str2, null);
        if (!subUbiQuery.isValid()) {
            return null;
        }
        if (z) {
            uri = GalleryCloudUtils.SHARE_SUB_UBIFOCUS_URI;
        } else {
            uri = GalleryCloudUtils.OWNER_SUB_UBIFOCUS_URI;
        }
        return (T) GalleryUtils.safeQuery(uri, CloudUtils.getProjectionAll(), subUbiQuery.queryString, subUbiQuery.queryArgs, (String) null, queryHandler);
    }

    public static List<DBImage> getSubUbiImages(final boolean z, String str, String str2) {
        return (List) queryForSubUbiImages(z, str, str2, new GalleryUtils.QueryHandler<List<DBImage>>() { // from class: com.miui.gallery.util.UbiFocusUtils.3
            @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public List<DBImage> mo1712handle(Cursor cursor) {
                if (cursor != null) {
                    ArrayList newArrayList = Lists.newArrayList();
                    while (cursor.moveToNext()) {
                        if (z) {
                            newArrayList.add(new DBShareSubUbiImage(cursor));
                        } else {
                            newArrayList.add(new DBOwnerSubUbiImage(cursor));
                        }
                    }
                    return newArrayList;
                }
                return null;
            }
        });
    }

    public static void safeDeleteSubUbi(DBImage dBImage) {
        if (!dBImage.isUbiFocus()) {
            return;
        }
        SubUbiQuery subUbiQuery = new SubUbiQuery(dBImage.getRequestId(), dBImage.getId(), null);
        if (!subUbiQuery.isValid()) {
            DefaultLogger.e("UbiFocusUtils", "safeDeleteSubUbiImage, subUbiQuery.isValid.");
        } else {
            GalleryUtils.safeDelete(dBImage.isShareItem() ? GalleryCloudUtils.SHARE_SUB_UBIFOCUS_URI : GalleryCloudUtils.OWNER_SUB_UBIFOCUS_URI, subUbiQuery.queryString, subUbiQuery.queryArgs);
        }
    }

    public static DBImage getMainDBImage(final boolean z, String str, String str2) {
        Uri uri;
        MainUbiQuery mainUbiQuery = new MainUbiQuery(z, str, str2);
        if (!mainUbiQuery.isValid()) {
            return null;
        }
        if (z) {
            uri = GalleryCloudUtils.SHARE_IMAGE_URI;
        } else {
            uri = GalleryCloudUtils.CLOUD_URI;
        }
        return (DBImage) GalleryUtils.safeQuery(uri, CloudUtils.getProjectionAll(), mainUbiQuery.queryString, mainUbiQuery.queryArgs, (String) null, new GalleryUtils.QueryHandler<DBImage>() { // from class: com.miui.gallery.util.UbiFocusUtils.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
            /* renamed from: handle */
            public DBImage mo1712handle(Cursor cursor) {
                if (cursor == null || !cursor.moveToNext()) {
                    return null;
                }
                return z ? new DBShareImage(cursor) : new DBCloud(cursor);
            }
        });
    }

    public static int getSubUbiCount(boolean z, String str, String str2) {
        DBImage mainDBImage = getMainDBImage(z, str, str2);
        if (mainDBImage != null) {
            return mainDBImage.getSubUbiImageCount();
        }
        return 0;
    }
}
