package com.miui.gallery.ui.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.exifinterface.media.ExifInterface;
import com.miui.gallery.editor.photo.utils.BigBitmapLoadUtils;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.Bitmaps;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.HeifUtil;
import com.miui.gallery.util.IoUtils;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes2.dex */
public class PrepareUtils {
    public static Bitmap decodeOrigin(Context context, Uri uri) throws IOException {
        boolean z = false;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmaps.decodeUri(context, uri, options);
            boolean isHeifMimeType = BaseFileMimeUtil.isHeifMimeType(options.outMimeType);
            try {
                options.inJustDecodeBounds = false;
                options.inSampleSize = BigBitmapLoadUtils.calculateInSampleSize(context, options.outWidth, options.outHeight);
                Bitmap config = Bitmaps.setConfig(Bitmaps.joinExif(Bitmaps.decodeUri(context, uri, options), ExifUtil.getRotationDegrees(Bitmaps.readExif(context, uri)), null));
                if (isHeifMimeType) {
                    HeifUtil.releaseMemoryHeap();
                }
                return config;
            } catch (Throwable th) {
                th = th;
                z = isHeifMimeType;
                if (z) {
                    HeifUtil.releaseMemoryHeap();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static boolean saveBitmapWithExif(Context context, Bitmap bitmap, ExifInterface exifInterface, Uri uri) throws IOException {
        OutputStream outputStream;
        try {
            OutputStream openOutputStream = IoUtils.openOutputStream(context, uri);
            try {
                com.miui.gallery3d.exif.ExifInterface exifInterface2 = new com.miui.gallery3d.exif.ExifInterface();
                long dateTime = ExifUtil.getDateTime(exifInterface, true);
                if (dateTime > 0) {
                    ExifUtil.setDateTime(exifInterface2, dateTime, true);
                }
                exifInterface2.setTag(exifInterface2.buildTag(com.miui.gallery3d.exif.ExifInterface.TAG_ORIENTATION, Short.valueOf(com.miui.gallery3d.exif.ExifInterface.getOrientationValueForRotation(0))));
                exifInterface2.setTag(exifInterface2.buildTag(com.miui.gallery3d.exif.ExifInterface.TAG_IMAGE_WIDTH, Integer.valueOf(bitmap.getWidth())));
                exifInterface2.setTag(exifInterface2.buildTag(com.miui.gallery3d.exif.ExifInterface.TAG_IMAGE_LENGTH, Integer.valueOf(bitmap.getHeight())));
                double[] latLong = exifInterface.getLatLong();
                if (latLong != null && latLong.length == 2) {
                    exifInterface2.addGpsTags(latLong[0], latLong[1]);
                }
                outputStream = exifInterface2.getExifWriterStream(openOutputStream);
                boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream);
                if (compress) {
                    outputStream.flush();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                return compress;
            } catch (Throwable th) {
                th = th;
                if (outputStream != null) {
                    outputStream.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            outputStream = null;
        }
    }
}
