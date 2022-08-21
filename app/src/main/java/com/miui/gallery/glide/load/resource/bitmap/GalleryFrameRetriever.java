package com.miui.gallery.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.net.Uri;
import com.miui.gallery.data.DecodeUtils;
import com.miui.gallery.util.ExifUtil;

/* loaded from: classes2.dex */
public class GalleryFrameRetriever implements AutoCloseable {
    public final long mNativeProvider = nativeCreate();
    public int mRotation;
    public String mUrl;

    private static native long nativeCreate();

    private static native Bitmap nativeGetFrameAtIndex(long j, int i, Bitmap.Config config);

    private static native int nativeGetRotation(long j);

    private static native void nativeRelease(long j);

    private static native void nativeSetDataSource(long j, String str) throws Exception;

    public void setSource(String str) throws Exception {
        String path = Uri.parse(str).getPath();
        this.mUrl = path;
        nativeSetDataSource(this.mNativeProvider, path);
        this.mRotation = nativeGetRotation(this.mNativeProvider);
    }

    public Bitmap getFrameAtIndex(int i, Bitmap.Config config) {
        Bitmap nativeGetFrameAtIndex = nativeGetFrameAtIndex(this.mNativeProvider, i, config);
        int i2 = this.mRotation;
        return i2 != 0 ? DecodeUtils.considerOrientation(nativeGetFrameAtIndex, new ExifUtil.ExifInfo(0, i2, false)) : nativeGetFrameAtIndex;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        nativeRelease(this.mNativeProvider);
    }
}
