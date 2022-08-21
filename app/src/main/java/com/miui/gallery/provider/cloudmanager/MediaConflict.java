package com.miui.gallery.provider.cloudmanager;

import android.database.Cursor;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class MediaConflict {
    public static long verify(String str, long j, long j2, String str2, SupportSQLiteDatabase supportSQLiteDatabase) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            DefaultLogger.w("MediaConflict", "%s's fileName is empty, so no conflict", Long.valueOf(j2));
            return -102L;
        }
        VerifyParams prepare = prepare(str, j, j2, str2, supportSQLiteDatabase);
        if (prepare == null) {
            DefaultLogger.d("MediaConflict", "cursor for %s is null, abort", Long.valueOf(j2));
            return -101L;
        } else if (prepare.mExist.size() > 0) {
            DefaultLogger.w("MediaConflict", "目标目录存在文件");
            return -103L;
        } else if (prepare.mAllSame.size() > 0) {
            DefaultLogger.w("MediaConflict", "文件名 与 sha1 冲突");
            return -118L;
        } else if (prepare.mFileNameSame.size() > 0) {
            DefaultLogger.w("MediaConflict", "文件名 冲突");
            return -116L;
        } else if (prepare.mSha1Same.size() <= 0) {
            return -102L;
        } else {
            DefaultLogger.w("MediaConflict", "sha1 冲突");
            return -117L;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x00f1, code lost:
        r15.mAllSame.add(java.lang.Long.valueOf(r0));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.miui.gallery.provider.cloudmanager.MediaConflict.VerifyParams prepare(java.lang.String r9, long r10, long r12, java.lang.String r14, androidx.sqlite.db.SupportSQLiteDatabase r15) {
        /*
            boolean r0 = com.miui.gallery.provider.ShareAlbumHelper.isOtherShareAlbumId(r10)
            r1 = 3
            java.lang.String r2 = "(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND localGroupId = ? AND (fileName = ? OR sha1 = ? )"
            java.lang.String r3 = "fileName"
            java.lang.String r4 = "sha1"
            java.lang.String r5 = "_id"
            r6 = 2
            r7 = 1
            r8 = 0
            if (r0 == 0) goto L3d
            long r10 = com.miui.gallery.provider.ShareAlbumHelper.getOriginalAlbumId(r10)
            java.lang.String r0 = "shareImage"
            androidx.sqlite.db.SupportSQLiteQueryBuilder r0 = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(r0)
            java.lang.String[] r3 = new java.lang.String[]{r5, r4, r3}
            androidx.sqlite.db.SupportSQLiteQueryBuilder r0 = r0.columns(r3)
            java.lang.String[] r1 = new java.lang.String[r1]
            java.lang.String r10 = java.lang.String.valueOf(r10)
            r1[r8] = r10
            r1[r7] = r9
            r1[r6] = r14
            androidx.sqlite.db.SupportSQLiteQueryBuilder r10 = r0.selection(r2, r1)
            androidx.sqlite.db.SupportSQLiteQuery r10 = r10.create()
            android.database.Cursor r10 = r15.query(r10)
            goto L63
        L3d:
            java.lang.String r0 = "cloud"
            androidx.sqlite.db.SupportSQLiteQueryBuilder r0 = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(r0)
            java.lang.String[] r3 = new java.lang.String[]{r5, r4, r3}
            androidx.sqlite.db.SupportSQLiteQueryBuilder r0 = r0.columns(r3)
            java.lang.String[] r1 = new java.lang.String[r1]
            java.lang.String r10 = java.lang.String.valueOf(r10)
            r1[r8] = r10
            r1[r7] = r9
            r1[r6] = r14
            androidx.sqlite.db.SupportSQLiteQueryBuilder r10 = r0.selection(r2, r1)
            androidx.sqlite.db.SupportSQLiteQuery r10 = r10.create()
            android.database.Cursor r10 = r15.query(r10)
        L63:
            r11 = 0
            if (r10 != 0) goto L67
            return r11
        L67:
            com.miui.gallery.provider.cloudmanager.MediaConflict$VerifyParams r15 = new com.miui.gallery.provider.cloudmanager.MediaConflict$VerifyParams
            r15.<init>()
        L6c:
            boolean r11 = r10.moveToNext()
            if (r11 == 0) goto Lfa
            long r0 = r10.getLong(r8)
            boolean r11 = com.miui.gallery.provider.cache.ShareMediaManager.isOtherShareMediaId(r12)
            if (r11 == 0) goto L81
            long r2 = com.miui.gallery.provider.cache.ShareMediaManager.getOriginalMediaId(r12)
            goto L82
        L81:
            r2 = r12
        L82:
            int r11 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r11 != 0) goto L91
            java.util.ArrayList<java.lang.Long> r9 = r15.mExist
            java.lang.Long r11 = java.lang.Long.valueOf(r0)
            r9.add(r11)
            goto Lfa
        L91:
            java.lang.String r11 = r10.getString(r7)
            java.lang.String r2 = r10.getString(r6)
            boolean r3 = android.text.TextUtils.isEmpty(r11)
            if (r3 != 0) goto La7
            boolean r11 = android.text.TextUtils.equals(r14, r11)
            if (r11 == 0) goto La7
            r11 = r7
            goto La8
        La7:
            r11 = r8
        La8:
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 != 0) goto Lb6
            boolean r3 = android.text.TextUtils.equals(r9, r2)
            if (r3 == 0) goto Lb6
            r3 = r7
            goto Lb7
        Lb6:
            r3 = r8
        Lb7:
            if (r11 == 0) goto Lc5
            if (r3 == 0) goto Lc5
            java.util.ArrayList<java.lang.Long> r9 = r15.mAllSame
            java.lang.Long r11 = java.lang.Long.valueOf(r0)
            r9.add(r11)
            goto Lfa
        Lc5:
            if (r3 == 0) goto Ld1
            java.util.ArrayList<java.lang.Long> r9 = r15.mFileNameSame
            java.lang.Long r11 = java.lang.Long.valueOf(r0)
            r9.add(r11)
            goto Lfa
        Ld1:
            java.lang.String r11 = com.miui.gallery.provider.cloudmanager.Util.generatedNewName(r9, r14)
            boolean r11 = android.text.TextUtils.equals(r2, r11)
            if (r11 != 0) goto Lf1
            java.lang.String r11 = com.miui.gallery.provider.cloudmanager.Util.generatedNewName(r2, r14)
            boolean r11 = android.text.TextUtils.equals(r9, r11)
            if (r11 == 0) goto Le6
            goto Lf1
        Le6:
            java.util.ArrayList<java.lang.Long> r11 = r15.mSha1Same
            java.lang.Long r0 = java.lang.Long.valueOf(r0)
            r11.add(r0)
            goto L6c
        Lf1:
            java.util.ArrayList<java.lang.Long> r9 = r15.mAllSame
            java.lang.Long r11 = java.lang.Long.valueOf(r0)
            r9.add(r11)
        Lfa:
            r10.close()
            return r15
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.cloudmanager.MediaConflict.prepare(java.lang.String, long, long, java.lang.String, androidx.sqlite.db.SupportSQLiteDatabase):com.miui.gallery.provider.cloudmanager.MediaConflict$VerifyParams");
    }

    public static long verifyNewFileName(String str, long j, SupportSQLiteDatabase supportSQLiteDatabase) {
        Cursor query;
        if (ShareAlbumHelper.isOtherShareAlbumId(j)) {
            query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("shareImage").columns(new String[]{j.c}).selection("(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND localGroupId = ? AND fileName = ?  ", new String[]{String.valueOf(ShareAlbumHelper.getOriginalAlbumId(j)), str}).create());
        } else {
            query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(new String[]{j.c}).selection("(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND localGroupId = ? AND fileName = ? ", new String[]{String.valueOf(j), str}).create());
        }
        long j2 = -119;
        if (query == null) {
            return -119L;
        }
        if (query.getCount() > 0) {
            DefaultLogger.d("MediaConflict", "%s 拼接sha1后的新名称冲突，需要再拼接唯一关键词", str);
            j2 = -120;
        }
        query.close();
        return j2;
    }

    /* loaded from: classes2.dex */
    public static class VerifyParams {
        public ArrayList<Long> mAllSame;
        public ArrayList<Long> mExist;
        public ArrayList<Long> mFileNameSame;
        public ArrayList<Long> mSha1Same;

        public VerifyParams() {
            this.mExist = new ArrayList<>();
            this.mAllSame = new ArrayList<>();
            this.mSha1Same = new ArrayList<>();
            this.mFileNameSame = new ArrayList<>();
        }
    }
}
