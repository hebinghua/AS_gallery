package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.card.SyncTagUtil;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SyncLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class GalleryCloudSyncTagUtils {
    public static HashMap<String, String> sColumnNameToPushNameMap;
    public static final Object sSyncTagLock = new Object();
    public static HashMap<Integer, SyncTagConstant> sSyncTagConstantsMap = new HashMap<>();

    static {
        HashMap<String, String> hashMap = new HashMap<>();
        sColumnNameToPushNameMap = hashMap;
        hashMap.put("syncTag", "micloud.gallery.sync");
        sColumnNameToPushNameMap.put("shareSyncTagAlbumList", "micloud.gallery.albumlist.sync");
        sColumnNameToPushNameMap.put("shareSyncTagAlbumInfo", "micloud.gallery.albuminfo.sync");
        sColumnNameToPushNameMap.put("ownerSyncTagUserList", "micloud.gallery.sharerlist.sync");
        sColumnNameToPushNameMap.put("shareSyncTagImageList", "micloud.gallery.imagelist.sync");
        sColumnNameToPushNameMap.put("faceWatermark", "gallery-facerecognition");
        HashMap<Integer, SyncTagConstant> hashMap2 = sSyncTagConstantsMap;
        Uri uri = GalleryCloudUtils.CLOUD_SETTING_URI;
        hashMap2.put(1, new SyncTagConstant("syncTag", "syncTag", "syncTag", "micloud.gallery.sync", 0, true, false, uri, "syncInfo"));
        sSyncTagConstantsMap.put(2, new SyncTagConstant("ownerSyncTagUserList", "mySharerListsTag", "sharerlist", "micloud.gallery.sharerlist.sync", 0, true, false, uri, "shareSyncInfo"));
        sSyncTagConstantsMap.put(3, new SyncTagConstant("shareSyncTagAlbumList", "albumListTag", "albumlist", "micloud.gallery.albumlist.sync", 0, true, true, uri, "shareSyncInfo"));
        sSyncTagConstantsMap.put(4, new SyncTagConstant("shareSyncTagAlbumInfo", "albumInfoTag", "albuminfo", "micloud.gallery.albuminfo.sync", 0, true, true, uri, "shareSyncInfo"));
        sSyncTagConstantsMap.put(5, new SyncTagConstant("shareSyncTagImageList", "imageListTag", "imagelist", "micloud.gallery.imagelist.sync", 0, true, true, uri, "shareSyncInfo"));
        HashMap<Integer, SyncTagConstant> hashMap3 = sSyncTagConstantsMap;
        Uri uri2 = GalleryCloudUtils.SHARE_ALBUM_URI;
        hashMap3.put(8, new SyncTagConstant("albumImageTag", "syncTag", "syncTag", null, 0, false, false, uri2, "syncInfo"));
        sSyncTagConstantsMap.put(9, new SyncTagConstant("albumUserTag", "updateTag", "syncTag", null, 0, false, false, uri2, null));
        sSyncTagConstantsMap.put(10, new SyncTagConstant("albumUserTag", "updateTag", "syncTag", null, 0, false, false, GalleryCloudUtils.CLOUD_URI, null));
        sSyncTagConstantsMap.put(11, new SyncTagConstant("faceWatermark", null, null, "gallery-facerecognition", 0, false, false, uri, null));
    }

    /* loaded from: classes.dex */
    public static class SyncTagConstant {
        public final String columnName;
        public final int initValue;
        public final String jsonTagInput;
        public final String jsonTagOutput;
        public final String pushName;
        public final boolean shouldCheckInit;
        public final boolean shouldInsertCloudSetting;
        public final String syncInfoColumnName;
        public final Uri uri;

        public SyncTagConstant(String str, String str2, String str3, String str4, int i, boolean z, boolean z2, Uri uri, String str5) {
            this.columnName = str;
            this.jsonTagInput = str2;
            this.jsonTagOutput = str3;
            this.pushName = str4;
            this.initValue = i;
            this.shouldInsertCloudSetting = z;
            this.shouldCheckInit = z2;
            this.syncInfoColumnName = str5;
            this.uri = uri;
        }

        public boolean hasSyncInfo() {
            return this.syncInfoColumnName != null;
        }
    }

    /* loaded from: classes.dex */
    public static class SyncTagItem {
        public long currentValue;
        public long serverValue;
        public boolean shouldSync = true;
        public final int syncTagType;

        public SyncTagItem(int i) {
            this.syncTagType = i;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0065, code lost:
        if (r1 != null) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0067, code lost:
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0071, code lost:
        if (0 == 0) goto L16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.ArrayList<com.miui.gallery.cloud.GalleryCloudSyncTagUtils.SyncTagItem> getSyncTag(android.content.Context r6, android.accounts.Account r7, java.util.ArrayList<com.miui.gallery.cloud.GalleryCloudSyncTagUtils.SyncTagItem> r8) {
        /*
            java.lang.Object r0 = com.miui.gallery.cloud.GalleryCloudSyncTagUtils.sSyncTagLock
            monitor-enter(r0)
            r1 = 0
            android.database.Cursor r1 = getSyncTagCursorByAccount(r6, r7, r8)     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            if (r1 == 0) goto L41
            boolean r6 = r1.moveToNext()     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            if (r6 == 0) goto L41
            r6 = 0
        L11:
            int r7 = r8.size()     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            if (r6 >= r7) goto L65
            long r2 = r1.getLong(r6)     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            java.lang.Object r7 = r8.get(r6)     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            com.miui.gallery.cloud.GalleryCloudSyncTagUtils$SyncTagItem r7 = (com.miui.gallery.cloud.GalleryCloudSyncTagUtils.SyncTagItem) r7     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            java.util.HashMap<java.lang.Integer, com.miui.gallery.cloud.GalleryCloudSyncTagUtils$SyncTagConstant> r4 = com.miui.gallery.cloud.GalleryCloudSyncTagUtils.sSyncTagConstantsMap     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            java.lang.Object r5 = r8.get(r6)     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            com.miui.gallery.cloud.GalleryCloudSyncTagUtils$SyncTagItem r5 = (com.miui.gallery.cloud.GalleryCloudSyncTagUtils.SyncTagItem) r5     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            int r5 = r5.syncTagType     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            java.lang.Object r4 = r4.get(r5)     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            com.miui.gallery.cloud.GalleryCloudSyncTagUtils$SyncTagConstant r4 = (com.miui.gallery.cloud.GalleryCloudSyncTagUtils.SyncTagConstant) r4     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            int r4 = r4.initValue     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            long r4 = (long) r4     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            long r2 = java.lang.Math.max(r2, r4)     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            r7.currentValue = r2     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            int r6 = r6 + 1
            goto L11
        L41:
            java.util.Iterator r6 = r8.iterator()     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
        L45:
            boolean r7 = r6.hasNext()     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            if (r7 == 0) goto L65
            java.lang.Object r7 = r6.next()     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            com.miui.gallery.cloud.GalleryCloudSyncTagUtils$SyncTagItem r7 = (com.miui.gallery.cloud.GalleryCloudSyncTagUtils.SyncTagItem) r7     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            java.util.HashMap<java.lang.Integer, com.miui.gallery.cloud.GalleryCloudSyncTagUtils$SyncTagConstant> r2 = com.miui.gallery.cloud.GalleryCloudSyncTagUtils.sSyncTagConstantsMap     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            int r3 = r7.syncTagType     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            java.lang.Object r2 = r2.get(r3)     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            com.miui.gallery.cloud.GalleryCloudSyncTagUtils$SyncTagConstant r2 = (com.miui.gallery.cloud.GalleryCloudSyncTagUtils.SyncTagConstant) r2     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            int r2 = r2.initValue     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            long r2 = (long) r2     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            r7.currentValue = r2     // Catch: java.lang.Throwable -> L6b java.lang.Exception -> L6d
            goto L45
        L65:
            if (r1 == 0) goto L74
        L67:
            r1.close()     // Catch: java.lang.Throwable -> L76
            goto L74
        L6b:
            r6 = move-exception
            goto L78
        L6d:
            r6 = move-exception
            r6.printStackTrace()     // Catch: java.lang.Throwable -> L6b
            if (r1 == 0) goto L74
            goto L67
        L74:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L76
            return r8
        L76:
            r6 = move-exception
            goto L7e
        L78:
            if (r1 == 0) goto L7d
            r1.close()     // Catch: java.lang.Throwable -> L76
        L7d:
            throw r6     // Catch: java.lang.Throwable -> L76
        L7e:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L76
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloud.GalleryCloudSyncTagUtils.getSyncTag(android.content.Context, android.accounts.Account, java.util.ArrayList):java.util.ArrayList");
    }

    public static String getFaceSyncToken(Account account) {
        return (String) GalleryUtils.safeQuery(CloudUtils.getLimitUri(GalleryCloudUtils.CLOUD_SETTING_URI, 1), new String[]{"faceSyncToken"}, getAccountSelections(account), (String[]) null, (String) null, new GalleryUtils.QueryHandler<String>() { // from class: com.miui.gallery.cloud.GalleryCloudSyncTagUtils.2
            @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public String mo1712handle(Cursor cursor) {
                return (cursor == null || !cursor.moveToNext()) ? "" : cursor.getString(0);
            }
        });
    }

    public static void setFaceSyncToken(Account account, String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("faceSyncToken", str);
        GalleryUtils.safeUpdate(GalleryCloudUtils.CLOUD_SETTING_URI, contentValues, getAccountSelections(account), null);
    }

    public static void setFaceSyncWatermark(Account account, long j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("faceWatermark", Long.valueOf(j));
        GalleryUtils.safeUpdate(GalleryCloudUtils.CLOUD_SETTING_URI, contentValues, getAccountSelections(account), null);
    }

    public static long getCardSyncTag(Account account) {
        return SyncTagUtil.getCardSyncTag(account);
    }

    public static void setCardSyncTag(Account account, long j) {
        SyncTagUtil.setCardSyncTag(account, j);
    }

    public static String getCardSyncInfo(Account account) {
        return SyncTagUtil.getCardSyncInfo(account);
    }

    public static void setCardSyncInfo(Account account, String str) {
        SyncTagUtil.setCardSyncInfo(account, str);
    }

    public static void insertAccountToDB(Context context, Account account) {
        if (account == null || TextUtils.isEmpty(account.name) || TextUtils.isEmpty(account.type)) {
            return;
        }
        synchronized (sSyncTagLock) {
            ContentValues contentValues = new ContentValues();
            for (SyncTagConstant syncTagConstant : sSyncTagConstantsMap.values()) {
                if (syncTagConstant.shouldInsertCloudSetting) {
                    contentValues.put(syncTagConstant.columnName, Integer.valueOf(syncTagConstant.initValue));
                }
            }
            internalUpdateAccount(context, account, contentValues, null);
        }
        SyncTagUtil.ensureAccount(account);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0039  */
    /* JADX WARN: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void internalUpdateAccount(android.content.Context r3, android.accounts.Account r4, android.content.ContentValues r5, java.util.ArrayList<com.miui.gallery.cloud.GalleryCloudSyncTagUtils.SyncTagItem> r6) {
        /*
            r0 = 0
            android.database.Cursor r1 = getSyncTagCursorByAccount(r3, r4, r6)     // Catch: java.lang.Throwable -> L40
            if (r1 == 0) goto L21
            boolean r2 = r1.moveToNext()     // Catch: java.lang.Throwable -> L3d
            if (r2 != 0) goto Le
            goto L21
        Le:
            if (r6 == 0) goto L1d
            boolean r4 = r6.isEmpty()     // Catch: java.lang.Throwable -> L3d
            if (r4 == 0) goto L17
            goto L1d
        L17:
            android.net.Uri r4 = com.miui.gallery.cloud.GalleryCloudUtils.CLOUD_SETTING_URI     // Catch: java.lang.Throwable -> L3d
            com.miui.gallery.util.GalleryUtils.safeUpdate(r4, r5, r0, r0)     // Catch: java.lang.Throwable -> L3d
            goto L34
        L1d:
            r1.close()
            return
        L21:
            java.lang.String r6 = "accountName"
            java.lang.String r0 = r4.name     // Catch: java.lang.Throwable -> L3d
            r5.put(r6, r0)     // Catch: java.lang.Throwable -> L3d
            java.lang.String r6 = "accountType"
            java.lang.String r4 = r4.type     // Catch: java.lang.Throwable -> L3d
            r5.put(r6, r4)     // Catch: java.lang.Throwable -> L3d
            android.net.Uri r4 = com.miui.gallery.cloud.GalleryCloudUtils.CLOUD_SETTING_URI     // Catch: java.lang.Throwable -> L3d
            com.miui.gallery.util.GalleryUtils.safeInsert(r4, r5)     // Catch: java.lang.Throwable -> L3d
        L34:
            postUpdateSyncTag(r3, r5)     // Catch: java.lang.Throwable -> L3d
            if (r1 == 0) goto L3c
            r1.close()
        L3c:
            return
        L3d:
            r3 = move-exception
            r0 = r1
            goto L41
        L40:
            r3 = move-exception
        L41:
            if (r0 == 0) goto L46
            r0.close()
        L46:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloud.GalleryCloudSyncTagUtils.internalUpdateAccount(android.content.Context, android.accounts.Account, android.content.ContentValues, java.util.ArrayList):void");
    }

    public static void postUpdateSyncTag(Context context, ContentValues contentValues) {
        for (Map.Entry<String, Object> entry : contentValues.valueSet()) {
            String str = sColumnNameToPushNameMap.get(entry.getKey());
            if (!TextUtils.isEmpty(str)) {
                SyncLogger.d("GalleryCloudSyncTagUtils", "pushName:" + str + ", pushData:" + entry.getValue());
            }
        }
    }

    public static String[] getSyncTagSelection(ArrayList<SyncTagItem> arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return new String[]{" * "};
        }
        String[] strArr = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            strArr[i] = sSyncTagConstantsMap.get(Integer.valueOf(arrayList.get(i).syncTagType)).columnName;
        }
        return strArr;
    }

    public static String getAccountSelections(Account account) {
        return "accountName = '" + account.name + "' and accountType = '" + account.type + "'";
    }

    public static Cursor getSyncTagCursorByAccount(Context context, Account account, ArrayList<SyncTagItem> arrayList) {
        if (account == null) {
            SyncLogger.e("GalleryCloudSyncTagUtils", "account is null");
            return null;
        }
        return context.getContentResolver().query(GalleryCloudUtils.CLOUD_SETTING_URI, getSyncTagSelection(arrayList), getAccountSelections(account), null, null);
    }

    public static int getInitSyncTagValue(int i) {
        SyncTagConstant syncTagConstant = sSyncTagConstantsMap.get(Integer.valueOf(i));
        if (syncTagConstant != null) {
            return syncTagConstant.initValue;
        }
        return 0;
    }

    public static boolean shouldSyncTagValue(int i) {
        SyncTagConstant syncTagConstant = sSyncTagConstantsMap.get(Integer.valueOf(i));
        if (syncTagConstant != null) {
            return syncTagConstant.shouldCheckInit;
        }
        return false;
    }

    public static String getJsonTagInput(int i) {
        SyncTagConstant syncTagConstant = sSyncTagConstantsMap.get(Integer.valueOf(i));
        if (syncTagConstant != null) {
            return syncTagConstant.jsonTagInput;
        }
        return null;
    }

    public static String getJsonTagOutput(int i) {
        SyncTagConstant syncTagConstant = sSyncTagConstantsMap.get(Integer.valueOf(i));
        if (syncTagConstant != null) {
            return syncTagConstant.jsonTagOutput;
        }
        return null;
    }

    public static String getColumnName(int i) {
        SyncTagConstant syncTagConstant = sSyncTagConstantsMap.get(Integer.valueOf(i));
        if (syncTagConstant != null) {
            return syncTagConstant.columnName;
        }
        return null;
    }

    public static Uri getUri(int i) {
        SyncTagConstant syncTagConstant = sSyncTagConstantsMap.get(Integer.valueOf(i));
        if (syncTagConstant != null) {
            return syncTagConstant.uri;
        }
        return null;
    }

    public static String getSyncInfoColumnName(int i) {
        SyncTagConstant syncTagConstant = sSyncTagConstantsMap.get(Integer.valueOf(i));
        if (syncTagConstant != null) {
            return syncTagConstant.syncInfoColumnName;
        }
        return null;
    }

    public static boolean hasSyncInfo(int i) {
        SyncTagConstant syncTagConstant = sSyncTagConstantsMap.get(Integer.valueOf(i));
        if (syncTagConstant != null) {
            return syncTagConstant.hasSyncInfo();
        }
        return false;
    }
}
