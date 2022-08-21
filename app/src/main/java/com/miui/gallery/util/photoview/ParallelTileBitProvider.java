package com.miui.gallery.util.photoview;

import android.content.ContentResolver;
import android.graphics.BitmapRegionDecoder;
import android.net.Uri;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.util.BaseBitmapUtils;
import com.miui.gallery.util.BitmapUtils;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/* loaded from: classes2.dex */
public class ParallelTileBitProvider implements TileBitProvider {
    public final int mDecoderNumber;
    public boolean[] mDecoderUsedMarkers;
    public BitmapRegionDecoder[] mDecoders;
    public int mImageHeight;
    public int mImageWidth;
    public boolean mIsFlip;
    public volatile boolean mIsReleasing;
    public String mMimeType;
    public int mRotation;
    public final Semaphore mSemaphore;

    @Override // com.miui.gallery.util.photoview.TileBitProvider
    public ThreadPool customDecodePool() {
        return null;
    }

    public ParallelTileBitProvider(ContentResolver contentResolver, Uri uri, byte[] bArr, String str, int i) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < i; i2++) {
            BitmapRegionDecoder safeCreateBitmapRegionDecoder = BitmapUtils.safeCreateBitmapRegionDecoder(contentResolver, uri, true, bArr);
            if (BaseBitmapUtils.isValid(safeCreateBitmapRegionDecoder)) {
                arrayList.add(safeCreateBitmapRegionDecoder);
            }
        }
        int size = arrayList.size();
        this.mDecoderNumber = size;
        this.mDecoders = new BitmapRegionDecoder[size];
        this.mDecoderUsedMarkers = new boolean[size];
        this.mSemaphore = new Semaphore(size, true);
        this.mMimeType = str;
        if (arrayList.size() > 0) {
            BitmapRegionDecoder bitmapRegionDecoder = (BitmapRegionDecoder) arrayList.get(0);
            this.mImageWidth = bitmapRegionDecoder.getWidth();
            this.mImageHeight = bitmapRegionDecoder.getHeight();
            ExifUtil.ExifInfo parseRotationInfo = ExifUtil.parseRotationInfo(contentResolver, uri, bArr);
            if (parseRotationInfo == null) {
                this.mRotation = 0;
                this.mIsFlip = false;
            } else {
                this.mRotation = parseRotationInfo.rotation;
                this.mIsFlip = parseRotationInfo.flip;
            }
            arrayList.toArray(this.mDecoders);
        }
        DefaultLogger.d("ParallelTileBitProvider", "create");
    }

    @Override // com.miui.gallery.util.photoview.TileBitProvider
    public void release() {
        BitmapRegionDecoder[] bitmapRegionDecoderArr;
        DefaultLogger.d("ParallelTileBitProvider", "start release");
        long currentTimeMillis = System.currentTimeMillis();
        this.mIsReleasing = true;
        int i = this.mDecoderNumber;
        if (i > 0) {
            this.mSemaphore.acquireUninterruptibly(i);
            for (BitmapRegionDecoder bitmapRegionDecoder : this.mDecoders) {
                if (BaseBitmapUtils.isValid(bitmapRegionDecoder)) {
                    bitmapRegionDecoder.recycle();
                    DefaultLogger.d("ParallelTileBitProvider", "release decoder [%s]", Integer.toHexString(bitmapRegionDecoder.hashCode()));
                }
            }
        }
        this.mDecoders = null;
        DefaultLogger.d("ParallelTileBitProvider", "release done, costs: %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00a0  */
    @Override // com.miui.gallery.util.photoview.TileBitProvider
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.miui.gallery.util.photoview.TileBit getTileBit(android.graphics.Rect r9, int r10) {
        /*
            r8 = this;
            java.lang.String r0 = "ParallelTileBitProvider"
            r1 = 0
            if (r9 == 0) goto Lb0
            int r2 = r8.mDecoderNumber
            if (r2 <= 0) goto Lb0
            boolean r2 = r8.mIsReleasing
            if (r2 == 0) goto Lf
            goto Lb0
        Lf:
            android.graphics.Rect r2 = new android.graphics.Rect
            int r3 = r8.mImageWidth
            int r4 = r8.mImageHeight
            r5 = 0
            r2.<init>(r5, r5, r3, r4)
            boolean r3 = r2.intersect(r9)
            if (r3 != 0) goto L20
            return r1
        L20:
            android.graphics.BitmapFactory$Options r3 = new android.graphics.BitmapFactory$Options
            r3.<init>()
            android.graphics.Bitmap$Config r4 = com.miui.gallery.Config$TileConfig.getBitmapConfig()
            r3.inPreferredConfig = r4
            r3.inSampleSize = r10
            int r4 = r2.width()
            r3.outWidth = r4
            int r4 = r2.height()
            r3.outHeight = r4
            com.miui.gallery.util.photoview.TileReusedBitCache r4 = com.miui.gallery.util.photoview.TileReusedBitCache.getInstance()
            android.graphics.Bitmap r4 = r4.get(r3)
            r3.inBitmap = r4
            java.lang.System.currentTimeMillis()     // Catch: java.lang.InterruptedException -> L95
            android.graphics.BitmapRegionDecoder r4 = r8.acquireDecoder()     // Catch: java.lang.InterruptedException -> L95
            boolean r5 = com.miui.gallery.util.BaseBitmapUtils.isValid(r4)     // Catch: java.lang.InterruptedException -> L95
            if (r5 == 0) goto L8e
            android.graphics.Bitmap r5 = com.miui.gallery.util.BitmapUtils.safeDecodeRegion(r4, r2, r3)     // Catch: java.lang.InterruptedException -> L95
            if (r5 != 0) goto L8f
            android.graphics.Bitmap r6 = r3.inBitmap     // Catch: java.lang.InterruptedException -> L93
            boolean r6 = com.miui.gallery.util.BaseBitmapUtils.isValid(r6)     // Catch: java.lang.InterruptedException -> L93
            if (r6 == 0) goto L84
            java.lang.String r9 = "fail in decoding region use inBitmap [width %d, height %d]"
            android.graphics.Bitmap r6 = r3.inBitmap     // Catch: java.lang.InterruptedException -> L93
            int r6 = r6.getWidth()     // Catch: java.lang.InterruptedException -> L93
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch: java.lang.InterruptedException -> L93
            android.graphics.Bitmap r7 = r3.inBitmap     // Catch: java.lang.InterruptedException -> L93
            int r7 = r7.getHeight()     // Catch: java.lang.InterruptedException -> L93
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch: java.lang.InterruptedException -> L93
            com.miui.gallery.util.logger.DefaultLogger.w(r0, r9, r6, r7)     // Catch: java.lang.InterruptedException -> L93
            android.graphics.Bitmap r9 = r3.inBitmap     // Catch: java.lang.InterruptedException -> L93
            r9.recycle()     // Catch: java.lang.InterruptedException -> L93
            r3.inBitmap = r1     // Catch: java.lang.InterruptedException -> L93
            android.graphics.Bitmap r9 = com.miui.gallery.util.BitmapUtils.safeDecodeRegion(r4, r2, r3)     // Catch: java.lang.InterruptedException -> L93
            r5 = r9
            goto L8f
        L84:
            java.lang.String r3 = "fail in decoding region %s"
            java.lang.String r9 = r9.toString()     // Catch: java.lang.InterruptedException -> L93
            com.miui.gallery.util.logger.DefaultLogger.w(r0, r3, r9)     // Catch: java.lang.InterruptedException -> L93
            goto L8f
        L8e:
            r5 = r1
        L8f:
            r8.releaseDecoder(r4)     // Catch: java.lang.InterruptedException -> L93
            goto L9a
        L93:
            r9 = move-exception
            goto L97
        L95:
            r9 = move-exception
            r5 = r1
        L97:
            com.miui.gallery.util.logger.DefaultLogger.w(r0, r9)
        L9a:
            boolean r9 = com.miui.gallery.util.BaseBitmapUtils.isValid(r5)
            if (r9 == 0) goto Lb0
            com.miui.gallery.util.photoview.TileBit r9 = new com.miui.gallery.util.photoview.TileBit
            int r0 = r2.width()
            int r0 = r0 / r10
            int r1 = r2.height()
            int r1 = r1 / r10
            r9.<init>(r5, r0, r1)
            return r9
        Lb0:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.photoview.ParallelTileBitProvider.getTileBit(android.graphics.Rect, int):com.miui.gallery.util.photoview.TileBit");
    }

    @Override // com.miui.gallery.util.photoview.TileBitProvider
    public int getImageWidth() {
        return this.mImageWidth;
    }

    @Override // com.miui.gallery.util.photoview.TileBitProvider
    public int getImageHeight() {
        return this.mImageHeight;
    }

    @Override // com.miui.gallery.util.photoview.TileBitProvider
    public String getImageMimeType() {
        return this.mMimeType;
    }

    @Override // com.miui.gallery.util.photoview.TileBitProvider
    public int getRotation() {
        return this.mRotation;
    }

    @Override // com.miui.gallery.util.photoview.TileBitProvider
    public boolean isFlip() {
        return this.mIsFlip;
    }

    @Override // com.miui.gallery.util.photoview.TileBitProvider
    public int getParallelism() {
        return this.mDecoderNumber;
    }

    public final BitmapRegionDecoder acquireDecoder() throws InterruptedException {
        this.mSemaphore.acquire();
        return getNextAvailableDecoder();
    }

    public final void releaseDecoder(BitmapRegionDecoder bitmapRegionDecoder) {
        if (markAsUnused(bitmapRegionDecoder)) {
            this.mSemaphore.release();
        }
    }

    public final synchronized BitmapRegionDecoder getNextAvailableDecoder() {
        for (int i = 0; i < this.mDecoderNumber; i++) {
            boolean[] zArr = this.mDecoderUsedMarkers;
            if (!zArr[i]) {
                zArr[i] = true;
                return this.mDecoders[i];
            }
        }
        return null;
    }

    public final synchronized boolean markAsUnused(BitmapRegionDecoder bitmapRegionDecoder) {
        for (int i = 0; i < this.mDecoderNumber; i++) {
            if (bitmapRegionDecoder == this.mDecoders[i]) {
                boolean[] zArr = this.mDecoderUsedMarkers;
                if (!zArr[i]) {
                    return false;
                }
                zArr[i] = false;
                return true;
            }
        }
        return false;
    }
}
