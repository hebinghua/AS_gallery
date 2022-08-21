package com.miui.gallery.ui.share;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

/* loaded from: classes2.dex */
public class ConvertFunc implements PrepareFunc<ConvertItem> {
    public File mCacheFolder;
    public SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS", Locale.US);

    @Override // com.miui.gallery.ui.share.PrepareFunc
    public void release() {
    }

    public ConvertFunc(File file) {
        this.mCacheFolder = file;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x006b, code lost:
        if (r13 != null) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x006d, code lost:
        r13.onPreparing(r12, 1.0f);
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x007d, code lost:
        if (r13 != null) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0080, code lost:
        return r1;
     */
    /* JADX WARN: Not initialized variable reg: 3, insn: 0x0082: MOVE  (r1 I:??[OBJECT, ARRAY]) = (r3 I:??[OBJECT, ARRAY]), block:B:33:0x0082 */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x008a  */
    @Override // com.miui.gallery.ui.share.PrepareFunc
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public android.net.Uri prepare(com.miui.gallery.ui.share.ConvertItem r12, com.miui.gallery.ui.share.PrepareProgressCallback<com.miui.gallery.ui.share.ConvertItem> r13) {
        /*
            r11 = this;
            java.io.File r0 = r11.mCacheFolder
            boolean r0 = r0.exists()
            r1 = 0
            if (r0 != 0) goto L12
            java.io.File r0 = r11.mCacheFolder
            boolean r0 = r0.mkdirs()
            if (r0 != 0) goto L12
            return r1
        L12:
            android.content.Context r0 = com.miui.gallery.util.StaticContext.sGetAndroidContext()
            r2 = 1065353216(0x3f800000, float:1.0)
            android.net.Uri r3 = r12.getPreparedUriInLastStep()     // Catch: java.lang.Throwable -> L71 java.io.IOException -> L73
            android.graphics.Bitmap r3 = com.miui.gallery.ui.share.PrepareUtils.decodeOrigin(r0, r3)     // Catch: java.lang.Throwable -> L71 java.io.IOException -> L73
            if (r3 == 0) goto L66
            java.lang.String r4 = r12.getFileTitle()     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            if (r5 == 0) goto L3b
            java.text.SimpleDateFormat r4 = r11.mFormat     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            java.util.Date r5 = new java.util.Date     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            long r6 = java.lang.System.currentTimeMillis()     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            r5.<init>(r6)     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            java.lang.String r4 = r4.format(r5)     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
        L3b:
            java.io.File r5 = new java.io.File     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            java.io.File r6 = r11.mCacheFolder     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            java.util.Locale r7 = java.util.Locale.US     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            java.lang.String r8 = "%s.jpg"
            r9 = 1
            java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            r10 = 0
            r9[r10] = r4     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            java.lang.String r4 = java.lang.String.format(r7, r8, r9)     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            r5.<init>(r6, r4)     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            android.net.Uri r4 = android.net.Uri.fromFile(r5)     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            android.net.Uri r5 = r12.getPreparedUriInLastStep()     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            androidx.exifinterface.media.ExifInterface r5 = com.miui.gallery.util.Bitmaps.readExif(r0, r5)     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            boolean r0 = com.miui.gallery.ui.share.PrepareUtils.saveBitmapWithExif(r0, r3, r5, r4)     // Catch: java.io.IOException -> L64 java.lang.Throwable -> L81
            if (r0 == 0) goto L66
            r1 = r4
            goto L66
        L64:
            r0 = move-exception
            goto L75
        L66:
            if (r3 == 0) goto L6b
            r3.recycle()
        L6b:
            if (r13 == 0) goto L80
        L6d:
            r13.onPreparing(r12, r2)
            goto L80
        L71:
            r0 = move-exception
            goto L83
        L73:
            r0 = move-exception
            r3 = r1
        L75:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L81
            if (r3 == 0) goto L7d
            r3.recycle()
        L7d:
            if (r13 == 0) goto L80
            goto L6d
        L80:
            return r1
        L81:
            r0 = move-exception
            r1 = r3
        L83:
            if (r1 == 0) goto L88
            r1.recycle()
        L88:
            if (r13 == 0) goto L8d
            r13.onPreparing(r12, r2)
        L8d:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.share.ConvertFunc.prepare(com.miui.gallery.ui.share.ConvertItem, com.miui.gallery.ui.share.PrepareProgressCallback):android.net.Uri");
    }
}
