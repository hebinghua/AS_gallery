package com.miui.gallery.provider;

import android.database.Cursor;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.photosapi.PhotosOemApi;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class ProcessingMediaManager {
    public static LazyValue<Void, Integer> CAMERA_PROVIDER_VERSION = new LazyValue<Void, Integer>() { // from class: com.miui.gallery.provider.ProcessingMediaManager.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Integer mo1272onInit(Void r1) {
            return Integer.valueOf(PhotosOemApi.getVersion(GalleryApp.sGetAndroidContext()));
        }
    };

    public static boolean isProcessingTimeout(Cursor cursor, int i) {
        if (i == -1) {
            return false;
        }
        if (System.currentTimeMillis() - cursor.getLong(i) <= 40000) {
            return false;
        }
        DefaultLogger.w("ProcessingMediaManager", "Media process timeout: [%s]", cursor.getString(cursor.getColumnIndex("media_path")));
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0087 A[Catch: all -> 0x00b7, Exception -> 0x00ba, TryCatch #4 {Exception -> 0x00ba, all -> 0x00b7, blocks: (B:5:0x0032, B:6:0x005c, B:8:0x0062, B:11:0x0069, B:13:0x007b, B:19:0x0087, B:21:0x0092, B:20:0x008a), top: B:42:0x0032 }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x008a A[Catch: all -> 0x00b7, Exception -> 0x00ba, TryCatch #4 {Exception -> 0x00ba, all -> 0x00b7, blocks: (B:5:0x0032, B:6:0x005c, B:8:0x0062, B:11:0x0069, B:13:0x007b, B:19:0x0087, B:21:0x0092, B:20:0x008a), top: B:42:0x0032 }] */
    /* JADX WARN: Type inference failed for: r7v10, types: [boolean] */
    /* JADX WARN: Type inference failed for: r7v12 */
    /* JADX WARN: Type inference failed for: r7v7 */
    /* JADX WARN: Type inference failed for: r7v8 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.List<com.miui.gallery.provider.ProcessingMedia> queryProcessingMedias() {
        /*
            android.content.Context r0 = com.miui.gallery.GalleryApp.sGetAndroidContext()
            android.content.ContentResolver r1 = r0.getContentResolver()
            com.miui.gallery.provider.ProcessingMedia$Factory r10 = new com.miui.gallery.provider.ProcessingMedia$Factory
            r10.<init>(r0)
            java.util.ArrayList r11 = new java.util.ArrayList
            r11.<init>()
            r7 = 0
            android.net.Uri r2 = com.miui.gallery.photosapi.PhotosOemApi.getQueryProcessingUri(r0)     // Catch: java.lang.Throwable -> Lc3 java.lang.Exception -> Lc5
            java.lang.String r12 = "media_store_id"
            java.lang.String r13 = "media_path"
            java.lang.String r14 = "progress_status"
            java.lang.String r15 = "progress_percentage"
            java.lang.String r16 = "start_time"
            java.lang.String r17 = "no_gaussian"
            java.lang.String r18 = "progress_anim_type"
            java.lang.String[] r3 = new java.lang.String[]{r12, r13, r14, r15, r16, r17, r18}     // Catch: java.lang.Throwable -> Lc3 java.lang.Exception -> Lc5
            r4 = 0
            r5 = 0
            r6 = 0
            android.database.Cursor r1 = r1.query(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> Lc3 java.lang.Exception -> Lc5
            if (r1 == 0) goto Lbd
            java.lang.String r0 = "media_store_id"
            int r0 = r1.getColumnIndexOrThrow(r0)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            java.lang.String r2 = "media_path"
            int r12 = r1.getColumnIndexOrThrow(r2)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            java.lang.String r2 = "progress_status"
            int r13 = r1.getColumnIndexOrThrow(r2)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            java.lang.String r2 = "progress_percentage"
            int r14 = r1.getColumnIndexOrThrow(r2)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            java.lang.String r2 = "start_time"
            int r15 = r1.getColumnIndex(r2)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            java.lang.String r2 = "no_gaussian"
            int r9 = r1.getColumnIndex(r2)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            java.lang.String r2 = "progress_anim_type"
            int r8 = r1.getColumnIndex(r2)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
        L5c:
            boolean r2 = r1.moveToNext()     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            if (r2 == 0) goto Lbd
            boolean r2 = isProcessingTimeout(r1, r15)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            if (r2 == 0) goto L69
            goto L5c
        L69:
            java.lang.String r5 = r1.getString(r12)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            long r3 = r1.getLong(r0)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            int r2 = r1.getInt(r13)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            int r6 = r1.getInt(r14)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            if (r9 < 0) goto L84
            int r7 = r1.getInt(r9)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            if (r7 != 0) goto L82
            goto L84
        L82:
            r7 = 0
            goto L85
        L84:
            r7 = 1
        L85:
            if (r8 >= 0) goto L8a
            com.miui.gallery.photosapi.ProcessingMetadataQuery$ProcessingUI r16 = com.miui.gallery.photosapi.ProcessingMetadataQuery$ProcessingUI.CIRCLE     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            goto L92
        L8a:
            int r16 = r1.getInt(r8)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            com.miui.gallery.photosapi.ProcessingMetadataQuery$ProcessingUI r16 = com.miui.gallery.photosapi.ProcessingMetadataQuery$ProcessingUI.fromIdentifier(r16)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
        L92:
            r17 = 1
            r18 = r0
            com.miui.gallery.provider.ProcessingMedia$ProcessingMetadata r0 = new com.miui.gallery.provider.ProcessingMedia$ProcessingMetadata     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            com.miui.gallery.photosapi.ProcessingMetadataQuery$ProgressStatus r2 = com.miui.gallery.photosapi.ProcessingMetadataQuery$ProgressStatus.fromIdentifier(r2)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            r0.<init>(r2, r6)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            r2 = r10
            r6 = r17
            r17 = r8
            r8 = r16
            r16 = r9
            r9 = r0
            com.miui.gallery.provider.ProcessingMedia r0 = r2.build(r3, r5, r6, r7, r8, r9)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            r11.add(r0)     // Catch: java.lang.Throwable -> Lb7 java.lang.Exception -> Lba
            r9 = r16
            r8 = r17
            r0 = r18
            goto L5c
        Lb7:
            r0 = move-exception
            r7 = r1
            goto Ld1
        Lba:
            r0 = move-exception
            r7 = r1
            goto Lc6
        Lbd:
            if (r1 == 0) goto Ld0
            r1.close()
            goto Ld0
        Lc3:
            r0 = move-exception
            goto Ld1
        Lc5:
            r0 = move-exception
        Lc6:
            java.lang.String r1 = "ProcessingMediaManager"
            com.miui.gallery.util.logger.DefaultLogger.d(r1, r0)     // Catch: java.lang.Throwable -> Lc3
            if (r7 == 0) goto Ld0
            r7.close()
        Ld0:
            return r11
        Ld1:
            if (r7 == 0) goto Ld6
            r7.close()
        Ld6:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.ProcessingMediaManager.queryProcessingMedias():java.util.List");
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0046, code lost:
        if (r10 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0051, code lost:
        if (r10 == null) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0053, code lost:
        r10.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0056, code lost:
        return r9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.List<java.lang.String> queryProcessingMediaPaths() {
        /*
            java.lang.String r0 = "start_time"
            java.lang.String r1 = "media_path"
            android.content.Context r2 = com.miui.gallery.GalleryApp.sGetAndroidContext()
            android.content.ContentResolver r3 = r2.getContentResolver()
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            r10 = 0
            android.net.Uri r4 = com.miui.gallery.photosapi.PhotosOemApi.getQueryProcessingUri(r2)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            java.lang.String[] r5 = new java.lang.String[]{r1, r0}     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r10 = r3.query(r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            if (r10 == 0) goto L46
            int r1 = r10.getColumnIndexOrThrow(r1)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            int r0 = r10.getColumnIndex(r0)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
        L2b:
            boolean r2 = r10.moveToNext()     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            if (r2 == 0) goto L46
            boolean r2 = isProcessingTimeout(r10, r0)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            if (r2 == 0) goto L38
            goto L2b
        L38:
            java.lang.String r2 = r10.getString(r1)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            if (r3 != 0) goto L2b
            r9.add(r2)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            goto L2b
        L46:
            if (r10 == 0) goto L56
            goto L53
        L49:
            r0 = move-exception
            goto L57
        L4b:
            r0 = move-exception
            java.lang.String r1 = "ProcessingMediaManager"
            com.miui.gallery.util.logger.DefaultLogger.d(r1, r0)     // Catch: java.lang.Throwable -> L49
            if (r10 == 0) goto L56
        L53:
            r10.close()
        L56:
            return r9
        L57:
            if (r10 == 0) goto L5c
            r10.close()
        L5c:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.ProcessingMediaManager.queryProcessingMediaPaths():java.util.List");
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0046, code lost:
        if (r10 != null) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0051, code lost:
        if (r10 == null) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0053, code lost:
        r10.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0056, code lost:
        return r9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.List<java.lang.Long> queryProcessingMediaIds() {
        /*
            java.lang.String r0 = "start_time"
            java.lang.String r1 = "media_store_id"
            android.content.Context r2 = com.miui.gallery.GalleryApp.sGetAndroidContext()
            android.content.ContentResolver r3 = r2.getContentResolver()
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            r10 = 0
            android.net.Uri r4 = com.miui.gallery.photosapi.PhotosOemApi.getQueryProcessingUri(r2)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            java.lang.String r2 = "media_path"
            java.lang.String[] r5 = new java.lang.String[]{r1, r2, r0}     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r10 = r3.query(r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            if (r10 == 0) goto L46
            int r1 = r10.getColumnIndexOrThrow(r1)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            int r0 = r10.getColumnIndex(r0)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
        L2d:
            boolean r2 = r10.moveToNext()     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            if (r2 == 0) goto L46
            boolean r2 = isProcessingTimeout(r10, r0)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            if (r2 == 0) goto L3a
            goto L2d
        L3a:
            long r2 = r10.getLong(r1)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            java.lang.Long r2 = java.lang.Long.valueOf(r2)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            r9.add(r2)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4b
            goto L2d
        L46:
            if (r10 == 0) goto L56
            goto L53
        L49:
            r0 = move-exception
            goto L57
        L4b:
            r0 = move-exception
            java.lang.String r1 = "ProcessingMediaManager"
            com.miui.gallery.util.logger.DefaultLogger.d(r1, r0)     // Catch: java.lang.Throwable -> L49
            if (r10 == 0) goto L56
        L53:
            r10.close()
        L56:
            return r9
        L57:
            if (r10 == 0) goto L5c
            r10.close()
        L5c:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.ProcessingMediaManager.queryProcessingMediaIds():java.util.List");
    }
}
