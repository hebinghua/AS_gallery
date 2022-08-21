package com.miui.gallery.provider;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import androidx.sqlite.db.SupportSQLiteDatabase;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Pattern;
import miuix.core.util.Pools;

/* loaded from: classes2.dex */
public class RecentDiscoveryMediaManager {
    public static boolean isInShowHiddenMode() {
        return GalleryPreferences.HiddenAlbum.isShowHiddenAlbum();
    }

    public static String getNotInHiddenAlbumSelection(long j) {
        return !isInShowHiddenMode() ? String.format(Locale.US, " AND (%s in (SELECT _id FROM album WHERE attributes&16=0))", Long.valueOf(j)) : "";
    }

    public static String getNotSecretSelection(long j) {
        return String.format(Locale.US, "(%s !=-1000)", Long.valueOf(j));
    }

    public static boolean isMediaCanShowInMessage(long j) {
        Cursor query = GalleryDBHelper.getInstance().getReadableDatabase().query("SELECT " + getNotSecretSelection(j) + getNotInHiddenAlbumSelection(j));
        boolean z = false;
        if (query != null) {
            try {
                if (query.moveToFirst()) {
                    if (query.getInt(0) != 0) {
                        z = true;
                    }
                    return z;
                }
            } finally {
                query.close();
            }
        }
        return false;
    }

    public static void cleanupInvalidRecords() {
        try {
            GalleryDBHelper.getInstance().getWritableDatabase().execSQL("DELETE FROM recentDiscoveredMedia WHERE mediaId IN  (SELECT mediaId FROM (SELECT mediaId, cloud._id AS _id, localFlag, serverStatus FROM (recentDiscoveredMedia LEFT OUTER JOIN cloud ON cloud._id = mediaId)) WHERE (localFlag IN (11, -1, 2) OR (localFlag=0 AND (serverStatus<>'custom' AND serverStatus <> 'recovery'))) OR (_id IS NULL))");
        } catch (SQLException e) {
            DefaultLogger.e("RecentDiscoveryMediaManager", "Something wrong happened when cleanup recent table: %s", e.getMessage());
            e.printStackTrace();
        }
    }

    public static void insertToRecentUnchecked(Context context, RecentMediaEntry... recentMediaEntryArr) {
        doInsertToRecent(context, false, recentMediaEntryArr);
    }

    public static void insertToRecent(Context context, RecentMediaEntry... recentMediaEntryArr) {
        doInsertToRecent(context, true, recentMediaEntryArr);
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x00ef, code lost:
        recordNotInWhiteListAlbum((java.lang.String) r3.getKey());
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void doInsertToRecent(android.content.Context r10, boolean r11, com.miui.gallery.provider.RecentDiscoveryMediaManager.RecentMediaEntry... r12) {
        /*
            Method dump skipped, instructions count: 346
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.RecentDiscoveryMediaManager.doInsertToRecent(android.content.Context, boolean, com.miui.gallery.provider.RecentDiscoveryMediaManager$RecentMediaEntry[]):void");
    }

    public static boolean isNotRubbishAlbum(Context context, String str) {
        return !Album.isRubbishAlbum(CloudUtils.queryAlbumAttributesByAlbumLocalPath(context, str));
    }

    public static void recordNotInWhiteListAlbum(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(Action.FILE_ATTRIBUTE, str);
        SamplingStatHelper.recordCountEvent("media_scanner", "scanner_directory_not_in_white_list", hashMap);
    }

    public static boolean isAlbumInWhiteList(String str) {
        ArrayList<String> albumsInWhiteList = CloudControlStrategyHelper.getAlbumsInWhiteList();
        if (BaseMiscUtil.isValid(albumsInWhiteList)) {
            Iterator<String> it = albumsInWhiteList.iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (next != null && next.equalsIgnoreCase(str)) {
                    return true;
                }
            }
        }
        ArrayList<Pattern> albumWhiteListPatterns = CloudControlStrategyHelper.getAlbumWhiteListPatterns();
        if (BaseMiscUtil.isValid(albumWhiteListPatterns)) {
            for (Pattern pattern : albumWhiteListPatterns) {
                if (pattern.matcher(str).find()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean deleteGroupByAlbumIds(SupportSQLiteDatabase supportSQLiteDatabase, long[] jArr) {
        boolean z = false;
        if (jArr == null || jArr.length == 0) {
            return false;
        }
        StringBuilder sb = null;
        try {
            StringBuilder acquire = Pools.getStringBuilderPool().acquire();
            try {
                acquire.append(jArr[0]);
                for (int i = 1; i < jArr.length; i++) {
                    acquire.append(",");
                    acquire.append(jArr[i]);
                }
                if (delete(supportSQLiteDatabase, String.format("mediaId IN (%s)", String.format(" SELECT _id FROM cloud WHERE %s ", String.format("localGroupId IN (%s)", acquire.toString()))), null) > 0) {
                    z = true;
                }
                Pools.getStringBuilderPool().release(acquire);
                return z;
            } catch (Throwable th) {
                th = th;
                sb = acquire;
                Pools.getStringBuilderPool().release(sb);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static int delete(SupportSQLiteDatabase supportSQLiteDatabase, String str, String[] strArr) {
        return supportSQLiteDatabase.delete("recentDiscoveredMedia", str, strArr);
    }

    /* loaded from: classes2.dex */
    public static class RecentMediaEntry {
        public final long mAlbumId;
        public final long mDateModified;
        public final long mMediaId;
        public final String mThumbPath;

        public RecentMediaEntry(long j, long j2, String str, long j3) {
            this.mAlbumId = j;
            this.mMediaId = j2;
            this.mThumbPath = str;
            this.mDateModified = j3;
        }

        public long getMediaId() {
            return this.mMediaId;
        }

        public String getThumbPath() {
            return this.mThumbPath;
        }

        public long getDateModified() {
            return this.mDateModified;
        }
    }
}
