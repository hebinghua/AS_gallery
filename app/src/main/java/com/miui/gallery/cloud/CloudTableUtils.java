package com.miui.gallery.cloud;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Locale;

/* loaded from: classes.dex */
public class CloudTableUtils {
    public static final ArrayList<Long> sIdsForGroupWithoutRecord;
    public static final String sItemIsNotGroup;
    public static final ArrayList<Long> sKeepServerIdsAlbumsWhenReset;
    public static String sPhotoLocalFlag_Create_ForceCreate_Move_Copy;
    public static final String sPhotoLocalFlag_Synced;
    public static final ArrayList<Long> sSystemAlbumGroupIds;

    static {
        Locale locale = Locale.US;
        sPhotoLocalFlag_Synced = String.format(locale, "(%s = %d AND %s = '%s')", "localFlag", 0, "serverStatus", "custom");
        sPhotoLocalFlag_Create_ForceCreate_Move_Copy = String.format(locale, "(%s = %d OR %s = %d OR %s = %d OR %s = %d)", "localFlag", 8, "localFlag", 5, "localFlag", 6, "localFlag", 9);
        sItemIsNotGroup = String.format(locale, "(%s = %d OR %s = %d)", "serverType", 1, "serverType", 2);
        ArrayList<Long> arrayList = new ArrayList<>();
        sSystemAlbumGroupIds = arrayList;
        ArrayList<Long> arrayList2 = new ArrayList<>();
        sIdsForGroupWithoutRecord = arrayList2;
        ArrayList<Long> arrayList3 = new ArrayList<>();
        sKeepServerIdsAlbumsWhenReset = arrayList3;
        arrayList.add(1L);
        arrayList.add(2L);
        arrayList.add(3L);
        arrayList.add(4L);
        arrayList.add(1000L);
        arrayList.add(1001L);
        arrayList2.add(3L);
        arrayList2.add(4L);
        arrayList2.add(1000L);
        arrayList2.add(1001L);
        arrayList3.add(1L);
        arrayList3.add(2L);
    }

    public static String sGetWhereClauseAll(String str, String str2, int i) {
        Locale locale = Locale.US;
        return String.format(locale, "( (%s) AND ((%s AND (+%s = %s AND %s)) OR (%s AND (%s = %s AND %s))) )", sItemIsNotGroup, sPhotoLocalFlag_Synced, "groupId", str2, String.format(locale, "(%d = %d OR %d = %d)", 0, Integer.valueOf(i), 10, Integer.valueOf(i)), sPhotoLocalFlag_Create_ForceCreate_Move_Copy, "localGroupId", str, String.format(locale, "(%d = %d OR %d = %d OR %d = %d)", 0, Integer.valueOf(i), 8, Integer.valueOf(i), 10, Integer.valueOf(i)));
    }

    public static long getServerIdForGroupWithoutRecord(long j) {
        if (isGroupWithoutRecordByCloudId(j)) {
            return j * (-1);
        }
        return 0L;
    }

    public static long getCloudIdForGroupWithoutRecord(long j) {
        if (isGroupWithoutRecord(j)) {
            return j * (-1);
        }
        return 0L;
    }

    public static final boolean isGroupWithoutRecord(long j) {
        return sIdsForGroupWithoutRecord.contains(Long.valueOf(j));
    }

    public static final boolean isGroupWithoutRecordByCloudId(long j) {
        return sIdsForGroupWithoutRecord.contains(Long.valueOf(j * (-1)));
    }

    public static final ArrayList<Long> getKeepServerIdAlbums() {
        return new ArrayList<>(sKeepServerIdsAlbumsWhenReset);
    }

    public static boolean isCameraGroup(String str) {
        return String.valueOf(1L).equals(str);
    }

    public static boolean isScreenshotGroup(String str) {
        return String.valueOf(2L).equals(str);
    }

    public static boolean isSecretAlbum(String str, String str2) {
        long serverIdForGroupWithoutRecord;
        if (!TextUtils.isEmpty(str) && !TextUtils.equals(str, "0")) {
            serverIdForGroupWithoutRecord = Long.parseLong(str);
        } else {
            serverIdForGroupWithoutRecord = (TextUtils.isEmpty(str2) || TextUtils.equals(str2, "0")) ? 0L : getServerIdForGroupWithoutRecord(Long.parseLong(str2));
        }
        return serverIdForGroupWithoutRecord == 1000;
    }

    public static boolean isSystemAlbum(long j) {
        return sSystemAlbumGroupIds.contains(Long.valueOf(j));
    }
}
