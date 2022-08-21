package com.miui.gallery.provider.updater;

/* loaded from: classes2.dex */
public class GalleryDBUpdater98 extends GalleryDBUpdater {
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x003e, code lost:
        if (r9.equals("create_time_asc") == false) goto L5;
     */
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.miui.gallery.provider.updater.UpdateResult doUpdate(androidx.sqlite.db.SupportSQLiteDatabase r8, com.miui.gallery.provider.updater.UpdateResult r9) {
        /*
            r7 = this;
            java.lang.String r8 = "key_album_sort_type"
            r9 = 0
            java.lang.String r9 = com.miui.gallery.preference.PreferenceHelper.getString(r8, r9)
            if (r9 == 0) goto L73
            com.miui.gallery.preference.PreferenceHelper.removeKey(r8)
            r0 = 1
            r1 = -2147483648(0xffffffff80000000, float:-0.0)
            int r2 = com.miui.gallery.util.AlbumSortHelper.SortSpec.makeSortSpec(r0, r1)
            r9.hashCode()
            r3 = -1
            int r4 = r9.hashCode()
            r5 = 3
            r6 = 2
            switch(r4) {
                case -1620660928: goto L41;
                case -1160660510: goto L38;
                case 1233394629: goto L2d;
                case 1840899773: goto L22;
                default: goto L20;
            }
        L20:
            r0 = r3
            goto L4b
        L22:
            java.lang.String r0 = "name_asc"
            boolean r9 = r9.equals(r0)
            if (r9 != 0) goto L2b
            goto L20
        L2b:
            r0 = r5
            goto L4b
        L2d:
            java.lang.String r0 = "name_desc"
            boolean r9 = r9.equals(r0)
            if (r9 != 0) goto L36
            goto L20
        L36:
            r0 = r6
            goto L4b
        L38:
            java.lang.String r4 = "create_time_asc"
            boolean r9 = r9.equals(r4)
            if (r9 != 0) goto L4b
            goto L20
        L41:
            java.lang.String r0 = "create_time_desc"
            boolean r9 = r9.equals(r0)
            if (r9 != 0) goto L4a
            goto L20
        L4a:
            r0 = 0
        L4b:
            r9 = 1073741824(0x40000000, float:2.0)
            switch(r0) {
                case 0: goto L60;
                case 1: goto L5b;
                case 2: goto L56;
                case 3: goto L51;
                default: goto L50;
            }
        L50:
            goto L64
        L51:
            int r2 = com.miui.gallery.util.AlbumSortHelper.SortSpec.makeSortSpec(r6, r9)
            goto L64
        L56:
            int r2 = com.miui.gallery.util.AlbumSortHelper.SortSpec.makeSortSpec(r6, r1)
            goto L64
        L5b:
            int r2 = com.miui.gallery.util.AlbumSortHelper.SortSpec.makeSortSpec(r5, r9)
            goto L64
        L60:
            int r2 = com.miui.gallery.util.AlbumSortHelper.SortSpec.makeSortSpec(r5, r1)
        L64:
            android.content.SharedPreferences r9 = com.miui.gallery.util.deprecated.BaseDeprecatedPreference.sGetDefaultPreferences()
            android.content.SharedPreferences$Editor r9 = r9.edit()
            android.content.SharedPreferences$Editor r8 = r9.putInt(r8, r2)
            r8.apply()
        L73:
            com.miui.gallery.provider.updater.UpdateResult r8 = com.miui.gallery.provider.updater.UpdateResult.defaultResult()
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.updater.GalleryDBUpdater98.doUpdate(androidx.sqlite.db.SupportSQLiteDatabase, com.miui.gallery.provider.updater.UpdateResult):com.miui.gallery.provider.updater.UpdateResult");
    }
}
