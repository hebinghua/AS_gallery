package com.miui.gallery.glide.load.data;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.XmExifInterface;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import miuix.io.ResettableInputStream;

/* loaded from: classes2.dex */
public class ImageBoundCoverFetcher implements IThumbFetcher<String, BoundCover> {
    @Override // com.miui.gallery.glide.load.data.IThumbFetcher
    public BoundCover load(String str) throws IOException {
        XmExifInterface buildExifInterface = buildExifInterface(str);
        byte[] xmThumbnailBytes = buildExifInterface != null ? getXmThumbnailBytes(buildExifInterface) : null;
        if (xmThumbnailBytes != null) {
            return BoundCover.obtain(xmThumbnailBytes);
        }
        return null;
    }

    public static byte[] getXmThumbnailBytes(XmExifInterface xmExifInterface) throws FileNotFoundException {
        ResettableInputStream resettableInputStream;
        Throwable th;
        byte[] xmThumbnail = xmExifInterface.getXmThumbnail();
        if (xmThumbnail == null || xmThumbnail.length == 0) {
            throw new IllegalArgumentException("invalid xm thumbnail");
        }
        String attribute = xmExifInterface.getAttribute("Software");
        if (!TextUtils.isEmpty(attribute)) {
            throw new IllegalArgumentException(String.format("the file may have been edited by %s, skip!!", attribute));
        }
        try {
            resettableInputStream = new ResettableInputStream(xmThumbnail);
            try {
                BitmapFactory.Options bitmapSize = miuix.graphics.BitmapFactory.getBitmapSize(resettableInputStream);
                BaseMiscUtil.closeSilently(resettableInputStream);
                if (Math.max(bitmapSize.outWidth, bitmapSize.outHeight) < 1080) {
                    throw new IllegalArgumentException(String.format(Locale.US, "illegal size: %dx%d", Integer.valueOf(bitmapSize.outWidth), Integer.valueOf(bitmapSize.outHeight)));
                }
                return xmThumbnail;
            } catch (Throwable th2) {
                th = th2;
                BaseMiscUtil.closeSilently(resettableInputStream);
                throw th;
            }
        } catch (Throwable th3) {
            resettableInputStream = null;
            th = th3;
        }
    }

    public final XmExifInterface buildExifInterface(String str) throws IOException {
        Scheme ofUri = Scheme.ofUri(str);
        Scheme scheme = Scheme.FILE;
        if (scheme == ofUri) {
            return new XmExifInterface(scheme.crop(str));
        }
        com.bumptech.glide.load.data.StreamLocalUriFetcher streamLocalUriFetcher = null;
        if (Scheme.CONTENT == ofUri) {
            try {
                com.bumptech.glide.load.data.StreamLocalUriFetcher streamLocalUriFetcher2 = new com.bumptech.glide.load.data.StreamLocalUriFetcher(StaticContext.sGetAndroidContext().getContentResolver(), Uri.parse(str));
                try {
                    MyDataCallback myDataCallback = new MyDataCallback();
                    streamLocalUriFetcher2.loadData(Priority.IMMEDIATE, myDataCallback);
                    if (myDataCallback.getResult() != null) {
                        XmExifInterface xmExifInterface = new XmExifInterface(myDataCallback.getResult());
                        streamLocalUriFetcher2.cleanup();
                        return xmExifInterface;
                    }
                    streamLocalUriFetcher2.cleanup();
                } catch (Throwable th) {
                    th = th;
                    streamLocalUriFetcher = streamLocalUriFetcher2;
                    if (streamLocalUriFetcher != null) {
                        streamLocalUriFetcher.cleanup();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public static class MyDataCallback implements DataFetcher.DataCallback<InputStream> {
        public InputStream mResult;

        @Override // com.bumptech.glide.load.data.DataFetcher.DataCallback
        public void onLoadFailed(Exception exc) {
        }

        public MyDataCallback() {
        }

        @Override // com.bumptech.glide.load.data.DataFetcher.DataCallback
        public void onDataReady(InputStream inputStream) {
            this.mResult = inputStream;
        }

        public InputStream getResult() {
            return this.mResult;
        }
    }
}
