package com.miui.gallery.cloud.syncstate;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.deviceprovider.UploadStatusController;
import java.util.Locale;
import miui.cloud.MiCloudCompat;
import miui.cloud.sync.MiCloudStatusInfo;
import miui.cloud.util.SyncAutoSettingUtil;

/* loaded from: classes.dex */
public class SyncStateUtil {
    public static int[] getSyncedCount(Context context) {
        return querySyncedCount(context, GalleryContract.Cloud.CLOUD_URI, String.format(Locale.US, "%s AND (%s = %s OR %s = %s) GROUP BY %s", "(localFlag = 0 AND serverStatus = 'custom')", "serverType", 1, "serverType", 2, "serverType"));
    }

    public static long[] getSyncedSize(Context context) {
        return querySize(context, GalleryContract.Cloud.CLOUD_URI, String.format(Locale.US, "%s AND (%s = %s OR %s = %s) GROUP BY %s", "(localFlag = 0 AND serverStatus = 'custom')", "serverType", 1, "serverType", 2, "serverType"));
    }

    public static DirtyCount getDirtyCount(Context context) {
        Locale locale = Locale.US;
        String format = String.format(locale, "(%s) AND (%s = %s OR %s = %s) AND (%s = %s OR %s = %s) GROUP BY %s, %s", CloudUtils.SELECTION_OWNER_NEED_SYNC, "serverType", 1, "serverType", 2, "localFlag", 7, "localFlag", 8, "serverType", "oversized");
        String format2 = String.format(locale, "(%s = %d OR %s = %d)  GROUP BY %s, %s", "localFlag", 7, "localFlag", 8, "serverType", "oversized");
        String[] strArr = {"count(*)", "serverType", String.format(locale, " CASE WHEN ((serverType = 1 AND size < %s) OR (serverType = 2 AND size < %s)) THEN 0 ELSE 1 END AS oversized", Long.valueOf(CloudUtils.getMaxImageSizeLimit()), Long.valueOf(CloudUtils.getMaxVideoSizeLimit()))};
        return queryDirtyCount(context, GalleryCloudUtils.CLOUD_URI, strArr, format).plus(queryDirtyCount(context, GalleryCloudUtils.SHARE_IMAGE_URI, strArr, format2));
    }

    public static long[] getDirtySize(Context context) {
        Locale locale = Locale.US;
        String format = String.format(locale, "(%s) AND (%s = %s OR %s = %s) AND (%s = %s OR %s = %s) GROUP BY %s", CloudUtils.SELECTION_OWNER_NEED_SYNC, "serverType", 1, "serverType", 2, "localFlag", 7, "localFlag", 8, "serverType");
        String format2 = String.format(locale, "(%s = %d OR %s = %d) GROUP BY %s", "localFlag", 7, "localFlag", 8, "serverType");
        long[] querySize = querySize(context, GalleryContract.Cloud.CLOUD_URI, format);
        long[] querySize2 = querySize(context, GalleryContract.ShareImage.SHARE_URI, format2);
        return new long[]{querySize[0] + querySize2[0], querySize[1] + querySize2[1]};
    }

    public static long[] querySize(Context context, Uri uri, String str) {
        return (long[]) SafeDBUtil.safeQuery(context, uri, new String[]{"sum(size)", "serverType"}, str, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<long[]>() { // from class: com.miui.gallery.cloud.syncstate.SyncStateUtil.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public long[] mo1808handle(Cursor cursor) {
                long[] jArr = {0, 0};
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        long j = cursor.getLong(0);
                        if (cursor.getInt(1) == 1) {
                            jArr[0] = j;
                        } else {
                            jArr[1] = j;
                        }
                    }
                }
                return jArr;
            }
        });
    }

    public static DirtyCount queryDirtyCount(Context context, Uri uri, String[] strArr, String str) {
        return (DirtyCount) SafeDBUtil.safeQuery(context, uri, strArr, str, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<DirtyCount>() { // from class: com.miui.gallery.cloud.syncstate.SyncStateUtil.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public DirtyCount mo1808handle(Cursor cursor) {
                DirtyCount dirtyCount = new DirtyCount();
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        int i = cursor.getInt(0);
                        int i2 = cursor.getInt(1);
                        int i3 = cursor.getInt(2);
                        if (i2 == 1) {
                            if (i3 == 1) {
                                dirtyCount.setOversizedImageCount(i);
                            } else {
                                dirtyCount.setSyncableImageCount(i);
                            }
                        } else if (i3 == 1) {
                            dirtyCount.setOversizedVideoCount(i);
                        } else if (i3 == 0) {
                            dirtyCount.setSyncableVideoCount(i);
                        }
                    }
                }
                return dirtyCount;
            }
        });
    }

    public static int[] querySyncedCount(Context context, Uri uri, String str) {
        return (int[]) SafeDBUtil.safeQuery(context, uri, new String[]{"count(*)", "serverType"}, str, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<int[]>() { // from class: com.miui.gallery.cloud.syncstate.SyncStateUtil.3
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public int[] mo1808handle(Cursor cursor) {
                int[] iArr = {0, 0};
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        int i = cursor.getInt(0);
                        if (cursor.getInt(1) == 1) {
                            iArr[0] = i;
                        } else {
                            iArr[1] = i;
                        }
                    }
                }
                return iArr;
            }
        });
    }

    public static CloudSpaceInfo getCloudSpaceInfo(Context context) {
        return new CloudSpaceInfo(context);
    }

    /* loaded from: classes.dex */
    public static class CloudSpaceInfo {
        public MiCloudStatusInfo.QuotaInfo mInfo;

        /* JADX WARN: Removed duplicated region for block: B:24:0x003c  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x003f  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public CloudSpaceInfo(android.content.Context r4) {
            /*
                r3 = this;
                r3.<init>()
                r0 = 0
                if (r4 == 0) goto L34
                boolean r1 = com.miui.gallery.util.BaseNetworkUtils.isNetworkConnected()     // Catch: java.lang.Throwable -> L29 java.lang.Exception -> L2b
                if (r1 == 0) goto L34
                boolean r1 = com.miui.gallery.preference.BaseGalleryPreferences.CTA.canConnectNetwork()     // Catch: java.lang.Throwable -> L29 java.lang.Exception -> L2b
                if (r1 == 0) goto L34
                android.accounts.Account r1 = com.miui.gallery.cloud.AccountCache.getAccount()     // Catch: java.lang.Throwable -> L29 java.lang.Exception -> L2b
                if (r1 == 0) goto L34
                java.lang.String r1 = r1.name     // Catch: java.lang.Throwable -> L29 java.lang.Exception -> L2b
                java.util.Locale r2 = com.miui.gallery.util.FileSizeFormatter.localeFromContext(r4)     // Catch: java.lang.Throwable -> L29 java.lang.Exception -> L2b
                if (r2 == 0) goto L34
                java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L29 java.lang.Exception -> L2b
                miui.cloud.sync.MiCloudStatusInfo r1 = com.xiaomi.micloudsdk.cloudinfo.utils.CloudInfoUtils.getMiCloudStatusInfo(r1, r0, r2)     // Catch: java.lang.Throwable -> L29 java.lang.Exception -> L2b
                goto L35
            L29:
                r0 = move-exception
                goto L30
            L2b:
                r1 = move-exception
                r1.printStackTrace()     // Catch: java.lang.Throwable -> L29
                goto L37
            L30:
                miui.cloud.sync.MiCloudStatusInfo.fromUserData(r4)
                throw r0
            L34:
                r1 = r0
            L35:
                if (r1 != 0) goto L3c
            L37:
                miui.cloud.sync.MiCloudStatusInfo r4 = miui.cloud.sync.MiCloudStatusInfo.fromUserData(r4)
                goto L3d
            L3c:
                r4 = r1
            L3d:
                if (r4 == 0) goto L43
                miui.cloud.sync.MiCloudStatusInfo$QuotaInfo r0 = r4.getQuotaInfo()
            L43:
                r3.mInfo = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloud.syncstate.SyncStateUtil.CloudSpaceInfo.<init>(android.content.Context):void");
        }

        public boolean isSpaceLow() {
            MiCloudStatusInfo.QuotaInfo quotaInfo = this.mInfo;
            return quotaInfo == null || quotaInfo.isSpaceFull() || this.mInfo.isSpaceLowPercent();
        }

        public long getTotal() {
            MiCloudStatusInfo.QuotaInfo quotaInfo = this.mInfo;
            if (quotaInfo != null) {
                return quotaInfo.getTotal();
            }
            return 0L;
        }

        public long getUsed() {
            MiCloudStatusInfo.QuotaInfo quotaInfo = this.mInfo;
            if (quotaInfo != null) {
                return quotaInfo.getUsed();
            }
            return 0L;
        }
    }

    public static String getQuantityStringWithUnit(long j) {
        return MiCloudCompat.getQuantityStringWithUnit(j);
    }

    public static boolean isSyncActive() {
        Account account = AccountCache.getAccount();
        if (account == null) {
            return false;
        }
        return ContentResolver.isSyncActive(account, "com.miui.gallery.cloud.provider");
    }

    public static boolean isUploading() {
        return UploadStatusController.getInstance().isUploading();
    }

    public static boolean isSyncPending() {
        Account account = AccountCache.getAccount();
        if (account == null) {
            return false;
        }
        return ContentResolver.isSyncPending(account, "com.miui.gallery.cloud.provider");
    }

    public static boolean isMasterSyncAutomatically() {
        return SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically();
    }

    public static boolean isSyncAutomatically() {
        Account account = AccountCache.getAccount();
        if (account == null) {
            return false;
        }
        return ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider");
    }

    public static boolean hasSyncedData(Context context) {
        return ((Integer) SafeDBUtil.safeQuery(context, GalleryContract.Cloud.CLOUD_URI, new String[]{"count(*)"}, String.format(Locale.US, "%s AND %s", "(localFlag = 0 AND serverStatus = 'custom')", "serverType IN (1,2)"), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Integer>() { // from class: com.miui.gallery.cloud.syncstate.SyncStateUtil.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Integer mo1808handle(Cursor cursor) {
                if (cursor != null && cursor.moveToFirst()) {
                    return Integer.valueOf(cursor.getInt(0));
                }
                return 0;
            }
        })).intValue() > 0;
    }
}
