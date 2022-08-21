package com.miui.gallery.util;

import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class SafeDBUtil {

    /* loaded from: classes2.dex */
    public interface QueryHandler<T> {
        /* renamed from: handle */
        T mo1808handle(Cursor cursor);
    }

    public static Uri safeInsert(Context context, Uri uri, ContentValues contentValues) {
        try {
            return context.getContentResolver().insert(uri, contentValues);
        } catch (Exception e) {
            DefaultLogger.w("SafeDBUtil", e);
            return null;
        }
    }

    public static int safeDelete(Context context, Uri uri, String str, String[] strArr) {
        try {
            return context.getContentResolver().delete(uri, str, strArr);
        } catch (Exception e) {
            DefaultLogger.w("SafeDBUtil", e);
            return -1;
        }
    }

    public static int safeUpdate(Context context, Uri uri, ContentValues contentValues, String str, String[] strArr) {
        try {
            return context.getContentResolver().update(uri, contentValues, str, strArr);
        } catch (Exception e) {
            DefaultLogger.w("SafeDBUtil", e);
            return -1;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static <T> T safeQuery(android.content.Context r1, android.net.Uri r2, java.lang.String[] r3, java.lang.String r4, java.lang.String[] r5, java.lang.String r6, com.miui.gallery.util.SafeDBUtil.QueryHandler<T> r7) {
        /*
            r0 = 0
            android.database.Cursor r1 = queryToCursor(r1, r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> L15 java.lang.Exception -> L17
            if (r7 == 0) goto L11
            java.lang.Object r2 = r7.mo1808handle(r1)     // Catch: java.lang.Exception -> Lf java.lang.Throwable -> L26
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r1)
            return r2
        Lf:
            r2 = move-exception
            goto L19
        L11:
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r1)
            goto L1f
        L15:
            r2 = move-exception
            goto L28
        L17:
            r2 = move-exception
            r1 = r0
        L19:
            java.lang.String r3 = "SafeDBUtil"
            com.miui.gallery.util.logger.DefaultLogger.w(r3, r2)     // Catch: java.lang.Throwable -> L26
            goto L11
        L1f:
            if (r7 == 0) goto L25
            java.lang.Object r0 = r7.mo1808handle(r0)
        L25:
            return r0
        L26:
            r2 = move-exception
            r0 = r1
        L28:
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.SafeDBUtil.safeQuery(android.content.Context, android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String, com.miui.gallery.util.SafeDBUtil$QueryHandler):java.lang.Object");
    }

    public static long safeInsert(SupportSQLiteDatabase supportSQLiteDatabase, String str, ContentValues contentValues) {
        try {
            return supportSQLiteDatabase.insert(str, 0, contentValues);
        } catch (Exception e) {
            DefaultLogger.w("SafeDBUtil", e);
            return -1L;
        }
    }

    public static int safeUpdate(SupportSQLiteDatabase supportSQLiteDatabase, String str, ContentValues contentValues, String str2, String[] strArr) {
        try {
            return supportSQLiteDatabase.update(str, 0, contentValues, str2, strArr);
        } catch (Exception e) {
            DefaultLogger.w("SafeDBUtil", e);
            return -1;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0033  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static <T> T safeQuery(androidx.sqlite.db.SupportSQLiteDatabase r1, java.lang.String r2, java.lang.String[] r3, java.lang.String r4, java.lang.String[] r5, java.lang.String r6, com.miui.gallery.util.SafeDBUtil.QueryHandler<T> r7) {
        /*
            r0 = 0
            androidx.sqlite.db.SupportSQLiteQueryBuilder r2 = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(r2)     // Catch: java.lang.Throwable -> L25 java.lang.Exception -> L27
            androidx.sqlite.db.SupportSQLiteQueryBuilder r2 = r2.columns(r3)     // Catch: java.lang.Throwable -> L25 java.lang.Exception -> L27
            androidx.sqlite.db.SupportSQLiteQueryBuilder r2 = r2.selection(r4, r5)     // Catch: java.lang.Throwable -> L25 java.lang.Exception -> L27
            androidx.sqlite.db.SupportSQLiteQueryBuilder r2 = r2.orderBy(r6)     // Catch: java.lang.Throwable -> L25 java.lang.Exception -> L27
            androidx.sqlite.db.SupportSQLiteQuery r2 = r2.create()     // Catch: java.lang.Throwable -> L25 java.lang.Exception -> L27
            android.database.Cursor r1 = r1.query(r2)     // Catch: java.lang.Throwable -> L25 java.lang.Exception -> L27
            if (r7 == 0) goto L2e
            java.lang.Object r2 = r7.mo1808handle(r1)     // Catch: java.lang.Exception -> L23 java.lang.Throwable -> L38
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r1)
            return r2
        L23:
            r2 = move-exception
            goto L29
        L25:
            r2 = move-exception
            goto L3a
        L27:
            r2 = move-exception
            r1 = r0
        L29:
            java.lang.String r3 = "SafeDBUtil"
            com.miui.gallery.util.logger.DefaultLogger.w(r3, r2)     // Catch: java.lang.Throwable -> L38
        L2e:
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r1)
            if (r7 == 0) goto L37
            java.lang.Object r0 = r7.mo1808handle(r0)
        L37:
            return r0
        L38:
            r2 = move-exception
            r0 = r1
        L3a:
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.SafeDBUtil.safeQuery(androidx.sqlite.db.SupportSQLiteDatabase, java.lang.String, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String, com.miui.gallery.util.SafeDBUtil$QueryHandler):java.lang.Object");
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:28:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static <T> T safeQuery(androidx.sqlite.db.SupportSQLiteDatabase r1, java.lang.String r2, java.lang.String[] r3, java.lang.String r4, java.lang.String[] r5, java.lang.String r6, java.lang.String r7, com.miui.gallery.util.SafeDBUtil.QueryHandler<T> r8) {
        /*
            r0 = 0
            androidx.sqlite.db.SupportSQLiteQueryBuilder r2 = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(r2)     // Catch: java.lang.Throwable -> L29 java.lang.Exception -> L2b
            androidx.sqlite.db.SupportSQLiteQueryBuilder r2 = r2.columns(r3)     // Catch: java.lang.Throwable -> L29 java.lang.Exception -> L2b
            androidx.sqlite.db.SupportSQLiteQueryBuilder r2 = r2.selection(r4, r5)     // Catch: java.lang.Throwable -> L29 java.lang.Exception -> L2b
            androidx.sqlite.db.SupportSQLiteQueryBuilder r2 = r2.groupBy(r6)     // Catch: java.lang.Throwable -> L29 java.lang.Exception -> L2b
            androidx.sqlite.db.SupportSQLiteQueryBuilder r2 = r2.orderBy(r7)     // Catch: java.lang.Throwable -> L29 java.lang.Exception -> L2b
            androidx.sqlite.db.SupportSQLiteQuery r2 = r2.create()     // Catch: java.lang.Throwable -> L29 java.lang.Exception -> L2b
            android.database.Cursor r1 = r1.query(r2)     // Catch: java.lang.Throwable -> L29 java.lang.Exception -> L2b
            if (r8 == 0) goto L32
            java.lang.Object r2 = r8.mo1808handle(r1)     // Catch: java.lang.Exception -> L27 java.lang.Throwable -> L3c
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r1)
            return r2
        L27:
            r2 = move-exception
            goto L2d
        L29:
            r2 = move-exception
            goto L3e
        L2b:
            r2 = move-exception
            r1 = r0
        L2d:
            java.lang.String r3 = "SafeDBUtil"
            com.miui.gallery.util.logger.DefaultLogger.w(r3, r2)     // Catch: java.lang.Throwable -> L3c
        L32:
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r1)
            if (r8 == 0) goto L3b
            java.lang.Object r0 = r8.mo1808handle(r0)
        L3b:
            return r0
        L3c:
            r2 = move-exception
            r0 = r1
        L3e:
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.SafeDBUtil.safeQuery(androidx.sqlite.db.SupportSQLiteDatabase, java.lang.String, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String, java.lang.String, com.miui.gallery.util.SafeDBUtil$QueryHandler):java.lang.Object");
    }

    /* JADX WARN: Not initialized variable reg: 1, insn: 0x0052: MOVE  (r0 I:??[OBJECT, ARRAY]) = (r1 I:??[OBJECT, ARRAY]), block:B:27:0x0052 */
    public static Cursor queryToCursor(Context context, Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        final ContentProviderClient contentProviderClient;
        ContentProviderClient contentProviderClient2;
        ContentProviderClient contentProviderClient3 = null;
        try {
            try {
                contentProviderClient = context.getContentResolver().acquireContentProviderClient(uri.getAuthority());
                try {
                    ContentProvider localContentProvider = contentProviderClient.getLocalContentProvider();
                    if (localContentProvider == null) {
                        releaseSilently(contentProviderClient);
                        Cursor query = context.getContentResolver().query(uri, strArr, str, strArr2, str2);
                        releaseSilently(contentProviderClient);
                        return query;
                    }
                    Cursor query2 = localContentProvider.query(uri, strArr, str, strArr2, str2);
                    if (query2 == null) {
                        releaseSilently(contentProviderClient);
                        releaseSilently(contentProviderClient);
                        return null;
                    }
                    CursorWrapper cursorWrapper = new CursorWrapper(query2) { // from class: com.miui.gallery.util.SafeDBUtil.1
                        @Override // android.database.CursorWrapper, android.database.Cursor, java.io.Closeable, java.lang.AutoCloseable
                        public void close() {
                            super.close();
                            SafeDBUtil.releaseSilently(contentProviderClient);
                        }
                    };
                    releaseSilently(contentProviderClient);
                    return cursorWrapper;
                } catch (Exception e) {
                    e = e;
                    DefaultLogger.w("SafeDBUtil", e);
                    releaseSilently(contentProviderClient);
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                contentProviderClient3 = contentProviderClient2;
                releaseSilently(contentProviderClient3);
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            contentProviderClient = null;
        } catch (Throwable th2) {
            th = th2;
            releaseSilently(contentProviderClient3);
            throw th;
        }
    }

    public static void releaseSilently(ContentProviderClient contentProviderClient) {
        if (contentProviderClient != null) {
            try {
                contentProviderClient.release();
            } catch (Exception unused) {
            }
        }
    }
}
