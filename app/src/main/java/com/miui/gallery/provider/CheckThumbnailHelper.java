package com.miui.gallery.provider;

import android.text.TextUtils;
import com.miui.gallery.util.ExifUtil;

/* loaded from: classes2.dex */
public class CheckThumbnailHelper {
    /* JADX WARN: Code restructure failed: missing block: B:16:0x003e, code lost:
        if (checkUserCommentSha1Exist(r10.getPath()) != false) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.database.Cursor checkThumbnail(android.content.Context r12, java.lang.String[] r13, boolean r14) {
        /*
            if (r13 != 0) goto L4
            r12 = 0
            return r12
        L4:
            long r0 = java.lang.System.currentTimeMillis()
            android.database.MatrixCursor r2 = new android.database.MatrixCursor
            java.lang.String r3 = "check_thumbnail_result"
            java.lang.String[] r3 = new java.lang.String[]{r3}
            int r4 = r13.length
            r2.<init>(r3, r4)
            java.util.HashMap r3 = new java.util.HashMap
            r3.<init>()
            int r4 = r13.length
            r5 = 0
            r6 = r5
        L1c:
            java.lang.String r7 = "CheckThumbnailHelper"
            if (r6 >= r4) goto L57
            r8 = r13[r6]
            r9 = 1
            android.net.Uri r10 = android.net.Uri.parse(r8)     // Catch: java.lang.Exception -> L42
            if (r10 == 0) goto L48
            if (r14 == 0) goto L36
            java.lang.String r10 = r10.getPath()     // Catch: java.lang.Exception -> L42
            boolean r7 = checkOriginalRecordExist(r12, r10, r3)     // Catch: java.lang.Exception -> L42
            if (r7 == 0) goto L48
            goto L40
        L36:
            java.lang.String r10 = r10.getPath()     // Catch: java.lang.Exception -> L42
            boolean r7 = checkUserCommentSha1Exist(r10)     // Catch: java.lang.Exception -> L42
            if (r7 == 0) goto L48
        L40:
            r7 = r5
            goto L49
        L42:
            r10 = move-exception
            java.lang.String r11 = "Failed checking file %s\n %s"
            com.miui.gallery.util.logger.DefaultLogger.e(r7, r11, r8, r10)
        L48:
            r7 = r9
        L49:
            java.lang.Integer[] r8 = new java.lang.Integer[r9]
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            r8[r5] = r7
            r2.addRow(r8)
            int r6 = r6 + 1
            goto L1c
        L57:
            int r12 = r13.length
            java.lang.Integer r12 = java.lang.Integer.valueOf(r12)
            long r13 = java.lang.System.currentTimeMillis()
            long r13 = r13 - r0
            java.lang.String r13 = java.lang.String.valueOf(r13)
            java.lang.String r14 = "Checked %d files, cost %ss"
            com.miui.gallery.util.logger.DefaultLogger.d(r7, r14, r12, r13)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.CheckThumbnailHelper.checkThumbnail(android.content.Context, java.lang.String[], boolean):android.database.Cursor");
    }

    public static boolean checkUserCommentSha1Exist(String str) {
        return !TextUtils.isEmpty(ExifUtil.getUserCommentSha1(str));
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x004a, code lost:
        if (r2.moveToFirst() != false) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x004c, code lost:
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00fb, code lost:
        if (r2.moveToFirst() != false) goto L14;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean checkOriginalRecordExist(android.content.Context r19, java.lang.String r20, java.util.Map<java.lang.String, java.lang.Long> r21) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 264
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.CheckThumbnailHelper.checkOriginalRecordExist(android.content.Context, java.lang.String, java.util.Map):boolean");
    }
}
