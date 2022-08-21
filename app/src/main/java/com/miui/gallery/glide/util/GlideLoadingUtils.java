package com.miui.gallery.glide.util;

/* loaded from: classes2.dex */
public class GlideLoadingUtils {
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:18:0x002f  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v0, types: [com.bumptech.glide.RequestManager] */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.util.concurrent.Future] */
    /* JADX WARN: Type inference failed for: r2v7, types: [java.util.concurrent.Future, com.bumptech.glide.request.FutureTarget] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.graphics.Bitmap blockingLoad(com.bumptech.glide.RequestManager r2, java.lang.Object r3, com.bumptech.glide.request.RequestOptions r4) {
        /*
            com.bumptech.glide.util.Util.assertBackgroundThread()
            r0 = 0
            r1 = 1
            com.bumptech.glide.RequestBuilder r2 = r2.mo985asBitmap()     // Catch: java.lang.Throwable -> L24 java.lang.InterruptedException -> L26 java.util.concurrent.ExecutionException -> L28
            com.bumptech.glide.RequestBuilder r2 = r2.mo962load(r3)     // Catch: java.lang.Throwable -> L24 java.lang.InterruptedException -> L26 java.util.concurrent.ExecutionException -> L28
            com.bumptech.glide.RequestBuilder r2 = r2.mo946apply(r4)     // Catch: java.lang.Throwable -> L24 java.lang.InterruptedException -> L26 java.util.concurrent.ExecutionException -> L28
            com.bumptech.glide.request.FutureTarget r2 = r2.submit()     // Catch: java.lang.Throwable -> L24 java.lang.InterruptedException -> L26 java.util.concurrent.ExecutionException -> L28
            java.lang.Object r3 = r2.get()     // Catch: java.lang.InterruptedException -> L20 java.util.concurrent.ExecutionException -> L22 java.lang.Throwable -> L33
            android.graphics.Bitmap r3 = (android.graphics.Bitmap) r3     // Catch: java.lang.InterruptedException -> L20 java.util.concurrent.ExecutionException -> L22 java.lang.Throwable -> L33
            r2.cancel(r1)
            r0 = r3
            goto L32
        L20:
            r3 = move-exception
            goto L2a
        L22:
            r3 = move-exception
            goto L2a
        L24:
            r3 = move-exception
            goto L35
        L26:
            r3 = move-exception
            goto L29
        L28:
            r3 = move-exception
        L29:
            r2 = r0
        L2a:
            r3.printStackTrace()     // Catch: java.lang.Throwable -> L33
            if (r2 == 0) goto L32
            r2.cancel(r1)
        L32:
            return r0
        L33:
            r3 = move-exception
            r0 = r2
        L35:
            if (r0 == 0) goto L3a
            r0.cancel(r1)
        L3a:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.glide.util.GlideLoadingUtils.blockingLoad(com.bumptech.glide.RequestManager, java.lang.Object, com.bumptech.glide.request.RequestOptions):android.graphics.Bitmap");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:18:0x002b  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0033  */
    /* JADX WARN: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v0, types: [com.bumptech.glide.RequestManager] */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.util.concurrent.Future] */
    /* JADX WARN: Type inference failed for: r2v6, types: [java.util.concurrent.Future, com.bumptech.glide.request.FutureTarget] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.graphics.Bitmap blockingLoad(com.bumptech.glide.RequestManager r2, java.lang.Object r3) {
        /*
            com.bumptech.glide.util.Util.assertBackgroundThread()
            r0 = 0
            r1 = 1
            com.bumptech.glide.RequestBuilder r2 = r2.mo985asBitmap()     // Catch: java.lang.Throwable -> L20 java.lang.InterruptedException -> L22 java.util.concurrent.ExecutionException -> L24
            com.bumptech.glide.RequestBuilder r2 = r2.mo962load(r3)     // Catch: java.lang.Throwable -> L20 java.lang.InterruptedException -> L22 java.util.concurrent.ExecutionException -> L24
            com.bumptech.glide.request.FutureTarget r2 = r2.submit()     // Catch: java.lang.Throwable -> L20 java.lang.InterruptedException -> L22 java.util.concurrent.ExecutionException -> L24
            java.lang.Object r3 = r2.get()     // Catch: java.lang.InterruptedException -> L1c java.util.concurrent.ExecutionException -> L1e java.lang.Throwable -> L2f
            android.graphics.Bitmap r3 = (android.graphics.Bitmap) r3     // Catch: java.lang.InterruptedException -> L1c java.util.concurrent.ExecutionException -> L1e java.lang.Throwable -> L2f
            r2.cancel(r1)
            r0 = r3
            goto L2e
        L1c:
            r3 = move-exception
            goto L26
        L1e:
            r3 = move-exception
            goto L26
        L20:
            r3 = move-exception
            goto L31
        L22:
            r3 = move-exception
            goto L25
        L24:
            r3 = move-exception
        L25:
            r2 = r0
        L26:
            r3.printStackTrace()     // Catch: java.lang.Throwable -> L2f
            if (r2 == 0) goto L2e
            r2.cancel(r1)
        L2e:
            return r0
        L2f:
            r3 = move-exception
            r0 = r2
        L31:
            if (r0 == 0) goto L36
            r0.cancel(r1)
        L36:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.glide.util.GlideLoadingUtils.blockingLoad(com.bumptech.glide.RequestManager, java.lang.Object):android.graphics.Bitmap");
    }
}
