package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class GalleryDBUpdater111 extends GalleryDBUpdater {
    public final String SQL_FORMAT = "update %s set %s=%s";

    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public boolean handle(int i) {
        return i == 109 || i == 108;
    }

    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        DefaultLogger.fd("GalleryDBUpdater111", "------------------------upgrade 111 start");
        upgradeTable(supportSQLiteDatabase, true);
        upgradeTable(supportSQLiteDatabase, false);
        DefaultLogger.fd("GalleryDBUpdater111", "------------------------upgrade 111 end");
        return UpdateResult.defaultResult();
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void upgradeTable(androidx.sqlite.db.SupportSQLiteDatabase r9, boolean r10) {
        /*
            r8 = this;
            if (r10 == 0) goto L5
            java.lang.String r10 = "shareAlbum"
            goto L7
        L5:
            java.lang.String r10 = "album"
        L7:
            java.lang.String r0 = "select name from pragma_table_info('album')"
            android.database.Cursor r0 = r9.query(r0)
            java.lang.String r1 = "sortInfo"
            r2 = 1
            r3 = 0
            java.lang.String r4 = "sort_info"
            if (r0 == 0) goto L5a
            boolean r5 = r0.moveToFirst()     // Catch: java.lang.Throwable -> L4e
            if (r5 == 0) goto L5a
            java.util.HashMap r5 = new java.util.HashMap     // Catch: java.lang.Throwable -> L4e
            int r6 = r0.getCount()     // Catch: java.lang.Throwable -> L4e
            float r6 = (float) r6     // Catch: java.lang.Throwable -> L4e
            r7 = 1061158912(0x3f400000, float:0.75)
            float r6 = r6 / r7
            int r6 = (int) r6     // Catch: java.lang.Throwable -> L4e
            int r6 = r6 + r2
            r5.<init>(r6)     // Catch: java.lang.Throwable -> L4e
        L2a:
            java.lang.String r6 = r0.getString(r3)     // Catch: java.lang.Throwable -> L4e
            r7 = 0
            r5.put(r6, r7)     // Catch: java.lang.Throwable -> L4e
            boolean r6 = r0.moveToNext()     // Catch: java.lang.Throwable -> L4e
            if (r6 != 0) goto L2a
            boolean r6 = r5.containsKey(r4)     // Catch: java.lang.Throwable -> L4e
            java.lang.String r7 = "sort_position"
            if (r6 == 0) goto L41
            goto L48
        L41:
            boolean r6 = r5.containsKey(r7)     // Catch: java.lang.Throwable -> L4e
            if (r6 == 0) goto L48
            r4 = r7
        L48:
            boolean r5 = r5.containsKey(r1)     // Catch: java.lang.Throwable -> L4e
            r5 = r5 ^ r2
            goto L5b
        L4e:
            r9 = move-exception
            if (r0 == 0) goto L59
            r0.close()     // Catch: java.lang.Throwable -> L55
            goto L59
        L55:
            r10 = move-exception
            r9.addSuppressed(r10)
        L59:
            throw r9
        L5a:
            r5 = r3
        L5b:
            if (r0 == 0) goto L60
            r0.close()
        L60:
            if (r5 == 0) goto L78
            com.miui.gallery.dao.base.TableColumn$Builder r0 = new com.miui.gallery.dao.base.TableColumn$Builder
            r0.<init>()
            com.miui.gallery.dao.base.TableColumn$Builder r0 = r0.setName(r1)
            java.lang.String r5 = "TEXT"
            com.miui.gallery.dao.base.TableColumn$Builder r0 = r0.setType(r5)
            com.miui.gallery.dao.base.TableColumn r0 = r0.build()
            com.miui.gallery.provider.GalleryDBHelper.addColumn(r9, r10, r0)
        L78:
            java.util.Locale r0 = java.util.Locale.US
            r5 = 3
            java.lang.Object[] r5 = new java.lang.Object[r5]
            r5[r3] = r10
            r5[r2] = r1
            r10 = 2
            r5[r10] = r4
            java.lang.String r10 = "update %s set %s=%s"
            java.lang.String r10 = java.lang.String.format(r0, r10, r5)
            java.lang.String r0 = "GalleryDBUpdater111"
            java.lang.String r1 = "109版本用户执行数据升级操作，sql: %s"
            com.miui.gallery.util.logger.DefaultLogger.fd(r0, r1, r10)
            r9.execSQL(r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.updater.GalleryDBUpdater111.upgradeTable(androidx.sqlite.db.SupportSQLiteDatabase, boolean):void");
    }
}
