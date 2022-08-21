package com.xiaomi.milab.videosdk.utils;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import ch.qos.logback.core.joran.action.Action;
import com.nexstreaming.nexeditorsdk.nexClip;
import com.xiaomi.stat.MiStat;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/* loaded from: classes3.dex */
public class BitmapUtils {
    public static final String[] EXIF_TAGS = {"FNumber", "DateTime", "ExposureTime", "Flash", "FocalLength", "GPSAltitude", "GPSAltitudeRef", "GPSDateStamp", "GPSLatitude", "GPSLatitudeRef", "GPSLongitude", "GPSLongitudeRef", "GPSProcessingMethod", "GPSTimeStamp", "ImageLength", "ImageWidth", "ISOSpeedRatings", "Make", "Model", "WhiteBalance"};
    private static final String TAG = "BitmapUtils";

    public static Bitmap getBitmapFromBuffer(ByteBuffer byteBuffer, int i, int i2) {
        return getBitmapFromBuffer(byteBuffer, i, i2, false, false);
    }

    public static Bitmap getBitmapFromBuffer(ByteBuffer byteBuffer, int i, int i2, boolean z, boolean z2) {
        if (byteBuffer == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        createBitmap.copyPixelsFromBuffer(byteBuffer);
        return (z || z2) ? flipBitmap(createBitmap, z, z2, true) : createBitmap;
    }

    public static Bitmap getBitmapFromFile(String str) {
        if (!new File(str).exists()) {
            return null;
        }
        try {
            return BitmapFactory.decodeFile(str);
        } catch (Exception e) {
            Log.e(TAG, "getBitmapFromFile: ", e);
            return null;
        }
    }

    public static Bitmap getImageFromAssetsFile(Context context, String str) {
        Bitmap bitmap = null;
        try {
            InputStream open = context.getResources().getAssets().open(str);
            bitmap = BitmapFactory.decodeStream(open);
            open.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public static Bitmap getImageFromAssetsFile(Context context, String str, Bitmap bitmap) {
        Bitmap decodeStream;
        Bitmap bitmap2 = null;
        try {
            InputStream open = context.getResources().getAssets().open(str);
            if (bitmap != null && !bitmap.isRecycled()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                options.inMutable = true;
                options.inBitmap = bitmap;
                decodeStream = BitmapFactory.decodeStream(open, null, options);
            } else {
                decodeStream = BitmapFactory.decodeStream(open);
            }
            bitmap2 = decodeStream;
            open.close();
            return bitmap2;
        } catch (IOException e) {
            e.printStackTrace();
            return bitmap2;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            int i6 = i3 / 2;
            int i7 = i4 / 2;
            while (i6 / i5 > i2 && i7 / i5 > i) {
                i5 *= 2;
            }
            for (long j = (i4 * i3) / i5; j > i * i2 * 2; j /= 2) {
                i5 *= 2;
            }
        }
        return i5;
    }

    public static Bitmap getBitmapFromFile(File file, int i, int i2) {
        BitmapFactory.Options options;
        if (file != null && file.exists()) {
            if (i <= 0 || i2 <= 0) {
                options = null;
            } else {
                options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getPath(), options);
                options.inSampleSize = calculateInSampleSize(options, i, i2);
                options.inJustDecodeBounds = false;
                options.inInputShareable = true;
                options.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(file.getPath(), options);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Bitmap getBitmapFromFile(File file, int i, int i2, boolean z) {
        BitmapFactory.Options options;
        int orientation;
        if (file != null && file.exists()) {
            if (i <= 0 || i2 <= 0) {
                options = null;
            } else {
                options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getPath(), options);
                options.inSampleSize = calculateInSampleSize(options, i, i2);
                options.inJustDecodeBounds = false;
                options.inInputShareable = true;
                options.inPurgeable = true;
            }
            try {
                Bitmap decodeFile = BitmapFactory.decodeFile(file.getPath(), options);
                if (!z || (orientation = getOrientation(file.getPath())) == 0) {
                    return decodeFile;
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(orientation);
                return Bitmap.createBitmap(decodeFile, 0, 0, decodeFile.getWidth(), decodeFile.getHeight(), matrix, true);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return createBitmap;
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int i, int i2, boolean z) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float f = i / width;
        float f2 = i2 / height;
        Matrix matrix = new Matrix();
        if (f < f2) {
            matrix.postScale(f, f);
        } else {
            matrix.postScale(f2, f2);
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        if (!bitmap.isRecycled() && z) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public static void saveBitmap(Context context, String str, Bitmap bitmap) {
        saveBitmap(context, str, bitmap, true);
    }

    public static void saveBitmap(Context context, String str, Bitmap bitmap, boolean z) {
        File file = new File(str);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean z2 = true;
        if (str.endsWith(".png")) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, fileOutputStream);
        } else if (str.endsWith(".jpeg") || str.endsWith(".jpg")) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fileOutputStream);
        } else {
            z2 = false;
        }
        try {
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        if (!z || !z2) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("_data", str);
        contentValues.put("_display_name", file.getName());
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    public static void saveBitmap(String str, Bitmap bitmap) {
        File file = new File(str);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (str.endsWith(".png")) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, fileOutputStream);
        } else if (str.endsWith(".jpeg") || str.endsWith(".jpg")) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fileOutputStream);
        }
        try {
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static void saveBitmap(String str, ByteBuffer byteBuffer, int i, int i2) {
        BufferedOutputStream bufferedOutputStream;
        BufferedOutputStream bufferedOutputStream2 = null;
        try {
            try {
                try {
                    bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(str));
                } catch (Throwable th) {
                    th = th;
                }
            } catch (FileNotFoundException e) {
                e = e;
            }
            try {
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
                createBitmap.copyPixelsFromBuffer(byteBuffer);
                Bitmap flipBitmap = flipBitmap(rotateBitmap(createBitmap, nexClip.kClip_Rotate_180, true), true);
                flipBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
                flipBitmap.recycle();
                bufferedOutputStream.close();
            } catch (FileNotFoundException e2) {
                e = e2;
                bufferedOutputStream2 = bufferedOutputStream;
                e.printStackTrace();
                if (bufferedOutputStream2 != null) {
                    bufferedOutputStream2.close();
                }
            } catch (Throwable th2) {
                th = th2;
                bufferedOutputStream2 = bufferedOutputStream;
                if (bufferedOutputStream2 != null) {
                    try {
                        bufferedOutputStream2.close();
                    } catch (IOException unused) {
                    }
                }
                throw th;
            }
        } catch (IOException unused2) {
        }
    }

    public static int getOrientation(String str) {
        int i;
        try {
            int attributeInt = new ExifInterface(new File(str).getAbsolutePath()).getAttributeInt("Orientation", 1);
            if (attributeInt == 3) {
                i = nexClip.kClip_Rotate_180;
            } else if (attributeInt == 6) {
                i = 90;
            } else if (attributeInt != 8) {
                return 0;
            } else {
                i = nexClip.kClip_Rotate_270;
            }
            return i;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getOrientation(Context context, Uri uri) {
        String scheme = uri.getScheme();
        if (scheme == null || Action.FILE_ATTRIBUTE.equals(scheme)) {
            return getOrientation(uri.getPath());
        }
        int i = 0;
        if (scheme.equals(MiStat.Param.CONTENT)) {
            try {
                ContentProviderClient acquireContentProviderClient = context.getContentResolver().acquireContentProviderClient(uri);
                if (acquireContentProviderClient != null) {
                    try {
                        Cursor query = acquireContentProviderClient.query(uri, new String[]{"orientation", "_data"}, null, null, null);
                        if (query == null) {
                            return 0;
                        }
                        int columnIndex = query.getColumnIndex("orientation");
                        int columnIndex2 = query.getColumnIndex("_data");
                        try {
                            if (query.getCount() > 0) {
                                query.moveToFirst();
                                if (columnIndex > -1) {
                                    i = query.getInt(columnIndex);
                                }
                                if (columnIndex2 > -1) {
                                    i |= getOrientation(query.getString(columnIndex2));
                                }
                                return i;
                            }
                        } finally {
                            query.close();
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SecurityException unused) {
            }
        }
        return 0;
    }

    public static Point getBitmapSize(String str) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        return new Point(options.outWidth, options.outHeight);
    }

    public static Bitmap rotateBitmap(byte[] bArr) {
        return rotateBitmap(bArr, 90);
    }

    public static Bitmap rotateBitmap(byte[] bArr, int i) {
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postRotate(i);
        Bitmap createBitmap = Bitmap.createBitmap(decodeByteArray, 0, 0, decodeByteArray.getWidth(), decodeByteArray.getHeight(), matrix, true);
        decodeByteArray.recycle();
        System.gc();
        return createBitmap;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, boolean z) {
        return rotateBitmap(bitmap, 90, z);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int i, boolean z) {
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postRotate(i);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (!bitmap.isRecycled() && z) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public static Bitmap flipBitmap(Bitmap bitmap, boolean z) {
        return flipBitmap(bitmap, true, false, z);
    }

    public static Bitmap flipBitmap(Bitmap bitmap, boolean z, boolean z2, boolean z3) {
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        float f = -1.0f;
        float f2 = z ? -1.0f : 1.0f;
        if (!z2) {
            f = 1.0f;
        }
        matrix.setScale(f2, f);
        matrix.postTranslate(bitmap.getWidth(), 0.0f);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        if (z3 && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public static Bitmap cropBitmap(Bitmap bitmap, int i, int i2, int i3, int i4, boolean z) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width - i < i3 || height - i2 < i4) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, i, i2, i3, i4, (Matrix) null, false);
        if (!bitmap.isRecycled() && z) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public static boolean loadExifAttributes(String str, Bundle bundle) {
        String[] strArr;
        try {
            ExifInterface exifInterface = new ExifInterface(str);
            for (String str2 : EXIF_TAGS) {
                bundle.putString(str2, exifInterface.getAttribute(str2));
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveExifAttributes(String str, Bundle bundle) {
        String[] strArr;
        try {
            ExifInterface exifInterface = new ExifInterface(str);
            for (String str2 : EXIF_TAGS) {
                if (bundle.containsKey(str2)) {
                    exifInterface.setAttribute(str2, bundle.getString(str2));
                }
            }
            try {
                exifInterface.saveAttributes();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static Bitmap textAsBitmap(Context context, String str) {
        TextView textView = new TextView(context);
        textView.setText(str);
        textView.setTextSize(28.0f);
        textView.setTextColor(-16776961);
        textView.setDrawingCacheEnabled(true);
        textView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
        Bitmap createBitmap = Bitmap.createBitmap(textView.getDrawingCache());
        textView.destroyDrawingCache();
        return createBitmap;
    }

    public static Bitmap textAsBitmap(String str, TextPaint textPaint, float f) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new StyleSpan(0), 0, str.length(), 33);
        StaticLayout staticLayout = new StaticLayout(spannableString, textPaint, (int) f, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        Bitmap createBitmap = Bitmap.createBitmap(staticLayout.getWidth(), staticLayout.getHeight(), Bitmap.Config.ARGB_8888);
        staticLayout.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public static Bitmap scaleAndRotateBitmap(Bitmap bitmap, float f, float f2) {
        if (bitmap == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), (Matrix) null, true);
        createBitmap.equals(bitmap);
        return createBitmap;
    }
}
