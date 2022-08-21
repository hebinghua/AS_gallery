package com.miui.gallery.util.photoview;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;
import com.miui.gallery.Config$TileConfig;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.util.BaseBitmapUtils;
import com.miui.gallery.util.BitmapUtils;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class TileBitProviderRegion implements TileBitProvider {
    public int mImageHeight;
    public int mImageWidth;
    public boolean mIsFlip;
    public final Object mLock = new Object();
    public String mMimeType;
    public BitmapRegionDecoder mRegionDecoder;
    public int mRotation;

    @Override // com.miui.gallery.util.photoview.TileBitProvider
    public ThreadPool customDecodePool() {
        return null;
    }

    @Override // com.miui.gallery.util.photoview.TileBitProvider
    public int getParallelism() {
        return 1;
    }

    public TileBitProviderRegion(ContentResolver contentResolver, Uri uri, byte[] bArr, String str) {
        BitmapRegionDecoder safeCreateBitmapRegionDecoder = BitmapUtils.safeCreateBitmapRegionDecoder(contentResolver, uri, false, bArr);
        this.mRegionDecoder = safeCreateBitmapRegionDecoder;
        this.mMimeType = str;
        if (BaseBitmapUtils.isValid(safeCreateBitmapRegionDecoder)) {
            this.mImageWidth = this.mRegionDecoder.getWidth();
            this.mImageHeight = this.mRegionDecoder.getHeight();
            ExifUtil.ExifInfo parseRotationInfo = ExifUtil.parseRotationInfo(contentResolver, uri, bArr);
            if (parseRotationInfo == null) {
                this.mRotation = 0;
                this.mIsFlip = false;
            } else {
                this.mRotation = parseRotationInfo.rotation;
                this.mIsFlip = parseRotationInfo.flip;
            }
        }
        DefaultLogger.d("TileBitProviderRegion", "create");
    }

    @Override // com.miui.gallery.util.photoview.TileBitProvider
    public void release() {
        synchronized (this.mLock) {
            if (BaseBitmapUtils.isValid(this.mRegionDecoder)) {
                this.mRegionDecoder.recycle();
            }
            this.mRegionDecoder = null;
            DefaultLogger.d("TileBitProviderRegion", "release");
        }
    }

    @Override // com.miui.gallery.util.photoview.TileBitProvider
    public TileBit getTileBit(Rect rect, int i) {
        Bitmap safeDecodeRegion;
        if (rect == null) {
            return null;
        }
        Rect rect2 = new Rect(0, 0, this.mImageWidth, this.mImageHeight);
        if (!rect2.intersect(rect)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config$TileConfig.getBitmapConfig();
        options.inSampleSize = i;
        options.outWidth = rect2.width();
        options.outHeight = rect2.height();
        options.inBitmap = TileReusedBitCache.getInstance().get(options);
        synchronized (this.mLock) {
            safeDecodeRegion = BaseBitmapUtils.isValid(this.mRegionDecoder) ? BitmapUtils.safeDecodeRegion(this.mRegionDecoder, rect2, options) : null;
        }
        if (safeDecodeRegion == null) {
            synchronized (this.mLock) {
                if (BaseBitmapUtils.isValid(options.inBitmap)) {
                    DefaultLogger.w("TileBitProviderRegion", "fail in decoding region use inBitmap [width %d, height %d]", Integer.valueOf(options.inBitmap.getWidth()), Integer.valueOf(options.inBitmap.getHeight()));
                    options.inBitmap.recycle();
                    options.inBitmap = null;
                    safeDecodeRegion = BitmapUtils.safeDecodeRegion(this.mRegionDecoder, rect2, options);
                } else {
                    DefaultLogger.w("TileBitProviderRegion", "fail in decoding region %s", rect.toString());
                }
            }
        }
        if (!BaseBitmapUtils.isValid(safeDecodeRegion)) {
            return null;
        }
        return new TileBit(safeDecodeRegion, rect2.width() / i, rect2.height() / i);
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
}
