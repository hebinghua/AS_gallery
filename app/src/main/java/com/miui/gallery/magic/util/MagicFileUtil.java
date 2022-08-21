package com.miui.gallery.magic.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.magic.tools.MagicUtils;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexClip;
import com.xiaomi.milab.videosdk.utils.BitmapUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: classes2.dex */
public class MagicFileUtil {
    public static String generateFilePath() {
        String str = getExternalStoragePath("magic_editor_image/") + System.currentTimeMillis() + ".jpg";
        File parentFile = new File(str).getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        return str;
    }

    public static String getExternalStoragePath(String str) {
        return MagicUtils.getGalleryApp().getFilesDir().getPath() + File.separator + str;
    }

    public static String getTempVideoPath() {
        String str = getExternalStoragePath("magic_editor_video/") + File.separator + "temp";
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return str;
    }

    public static void clearTempVideoFiles() {
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(getTempVideoPath(), IStoragePermissionStrategy.Permission.DELETE_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("MagicFileUtil", "clearTempVideoFiles"));
        if (documentFile == null) {
            return;
        }
        documentFile.delete();
    }

    public static Uri getImageUri(File file) {
        return FileProvider.getUriForFile(MagicUtils.getGalleryApp(), MagicUtils.getFileProviderAuthority(), file);
    }

    public static Intent getTakePhotoIntentNew(Context context, Uri uri) {
        Intent intent = new Intent();
        if (isIntentAvailable(context, "com.android.camera.action.IDPHOTO")) {
            intent.setAction("com.android.camera.action.IDPHOTO");
            for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(intent, 65536)) {
                context.grantUriPermission(resolveInfo.activityInfo.packageName, uri, 3);
            }
        } else {
            intent.setAction("android.media.action.IMAGE_CAPTURE");
        }
        intent.setPackage("com.android.camera");
        intent.putExtra("output", uri);
        return intent;
    }

    public static boolean isIntentAvailable(Context context, String str) {
        return context.getPackageManager().queryIntentActivities(new Intent(str), 65536).size() > 0;
    }

    public static Intent getSelectImageIntent() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setPackage("com.miui.gallery");
        intent.setType("image/*");
        return intent;
    }

    public static void deleteImage(Context context, Uri uri) {
        context.getContentResolver().delete(uri, null, null);
    }

    public static Uri saveBitmap(Context context, Bitmap bitmap, String str) {
        return saveBitmap(context, bitmap, -1, str);
    }

    public static Uri saveBitmap(Context context, Bitmap bitmap, int i, String str) {
        if (bitmap == null) {
            return null;
        }
        return saveSignImage(context, getFileNameByMode(str, ".jpg"), bitmap, i);
    }

    public static String getFileNameByMode(String str, String str2) {
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "IMG_" + format + str + str2;
    }

    public static Uri saveSignImage(Context context, String str, Bitmap bitmap, int i) {
        OutputStream openOutputStream;
        try {
            Uri uriByType = getUriByType(context, str, "image/JPEG");
            if (uriByType != null && (openOutputStream = context.getContentResolver().openOutputStream(uriByType)) != null) {
                storeImage(openOutputStream, bitmap, i);
            }
            return uriByType;
        } catch (Exception unused) {
            return null;
        }
    }

    public static Uri saveSignImagePng(Context context, String str, Bitmap bitmap) {
        OutputStream openOutputStream;
        try {
            Uri uriByType = getUriByType(context, str, "image/PNG");
            if (uriByType != null && (openOutputStream = context.getContentResolver().openOutputStream(uriByType)) != null) {
                storeImage(openOutputStream, bitmap, -1, Bitmap.CompressFormat.PNG);
            }
            return uriByType;
        } catch (Exception unused) {
            return null;
        }
    }

    public static void storeImage(OutputStream outputStream, Bitmap bitmap, int i) {
        storeImage(outputStream, bitmap, i, Bitmap.CompressFormat.JPEG);
    }

    public static void storeImage(OutputStream outputStream, Bitmap bitmap, int i, Bitmap.CompressFormat compressFormat) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(compressFormat, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            if (i > 0) {
                setDpi(byteArray, i);
            }
            outputStream.write(byteArray);
            outputStream.flush();
            outputStream.close();
            if (bitmap.isRecycled()) {
                return;
            }
            bitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static void setDpi(byte[] bArr, int i) {
        bArr[13] = 1;
        byte b = (byte) (i >> 8);
        bArr[14] = b;
        byte b2 = (byte) (i & 255);
        bArr[15] = b2;
        bArr[16] = b;
        bArr[17] = b2;
    }

    public static Uri getUriByType(Context context, String str, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", str);
        if (Build.VERSION.SDK_INT >= 29) {
            contentValues.put("relative_path", StorageConstants.RELATIVE_DIRECTORY_CREATIVE);
        } else {
            contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
        }
        contentValues.put("mime_type", str2);
        return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    public static Bitmap getBitmap(Bitmap bitmap, int i, int i2) {
        float f;
        float f2;
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        if (width > height) {
            if (i > width) {
                i = width;
            }
            f = i;
            f2 = width;
        } else {
            if (i > height) {
                i = height;
            }
            f = i;
            f2 = height;
        }
        float f3 = f / f2;
        matrix.postScale(f3, f3);
        matrix.postRotate(i2);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        if (createBitmap != bitmap) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        Log.e("MagicFileUtil", "calculateInSampleSize: width:" + i4 + " height:" + i3);
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            int i6 = i3 / 2;
            int i7 = i4 / 2;
            while (i6 / i5 >= i2 && i7 / i5 >= i) {
                i5 *= 2;
            }
        }
        Log.e("MagicFileUtil", "calculateInSampleSize: inSampleSize:" + i5);
        return i5;
    }

    public static Bitmap decodeSampledBitmapFromUri(Context context, Uri uri, int i, int i2) throws FileNotFoundException {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(openInputStream, null, options);
            if (openInputStream != null) {
                openInputStream.close();
            }
            InputStream openInputStream2 = context.getContentResolver().openInputStream(uri);
            options.inSampleSize = calculateInSampleSize(options, i, i2);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(openInputStream2, null, options);
            if (openInputStream2 != null) {
                openInputStream2.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap getBitmap(Context context, Uri uri, int i) throws IOException {
        return getBitmap(decodeSampledBitmapFromUri(context, uri, 4000, 4000), i, BitmapUtils.getOrientation(context, uri));
    }

    public static Bitmap getBitmap(Context context, Uri uri, int i, String str) throws IOException {
        int orientation;
        if (TextUtils.isEmpty(str)) {
            return getBitmap(context, uri, i);
        }
        if (Build.VERSION.SDK_INT >= 28) {
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            orientation = getOrientation(openInputStream);
            openInputStream.close();
        } else {
            orientation = BitmapUtils.getOrientation(str);
        }
        return getBitmap(decodeSampledBitmapFromUri(context, uri, 4000, 4000), i, orientation);
    }

    public static int getOrientation(InputStream inputStream) {
        int i;
        try {
            int attributeInt = new ExifInterface(inputStream).getAttributeInt("Orientation", 1);
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

    public static void openPreviewImage(Context context, Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setComponent(new ComponentName("com.miui.gallery", "com.miui.gallery.activity.ExternalPhotoPageActivity"));
        intent.setData(uri);
        intent.putExtra("com.miui.gallery.extra.deleting_include_cloud", true);
        context.startActivity(intent);
    }

    public static void openPreviewVideo(Context context, Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setComponent(new ComponentName("com.miui.gallery", "com.miui.gallery.activity.ExternalPhotoPageActivity"));
        intent.setData(uri);
        intent.putExtra("com.miui.gallery.extra.deleting_include_cloud", true);
        context.startActivity(intent);
    }

    public static void selectLocalAudio(Activity activity, int i) {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.setPackage("com.miui.player");
        try {
            activity.startActivityForResult(intent, i);
        } catch (ActivityNotFoundException unused) {
            DefaultLogger.e("MagicFileUtil", "com.miui.player not found,try all picker");
            try {
                Intent intent2 = new Intent();
                intent2.setType("audio/*");
                intent2.setAction("android.intent.action.GET_CONTENT");
                activity.startActivityForResult(intent2, i);
            } catch (ActivityNotFoundException unused2) {
                DefaultLogger.e("MagicFileUtil", "picker not found");
            }
        }
    }

    public static Bitmap getBitmapBackground(Bitmap bitmap, int i, int i2) {
        float f = i;
        float width = f / bitmap.getWidth();
        float f2 = i2;
        float height = f2 / bitmap.getHeight();
        Matrix matrix = new Matrix();
        if (width > height) {
            matrix.postScale(width, width);
        } else {
            matrix.postScale(height, height);
            width = height;
        }
        return Bitmap.createBitmap(bitmap, 0, 0, Math.round(f / width), Math.round(f2 / width), matrix, true);
    }

    public static boolean checkMinPX(Bitmap bitmap) {
        return bitmap.getHeight() < 50 || bitmap.getWidth() < 50;
    }

    public static boolean checkIdPhotoMinPX(Context context, Uri uri) {
        InputStream openInputStream;
        InputStream inputStream = null;
        try {
            try {
                openInputStream = context.getContentResolver().openInputStream(uri);
            } catch (Throwable th) {
                th = th;
            }
        } catch (FileNotFoundException e) {
            e = e;
        } catch (IOException e2) {
            e = e2;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(openInputStream, null, options);
            openInputStream.close();
            if (options.outWidth >= 260) {
                if (options.outHeight >= 260) {
                    try {
                        openInputStream.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    return false;
                }
            }
            try {
                openInputStream.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            return true;
        } catch (FileNotFoundException e5) {
            e = e5;
            inputStream = openInputStream;
            e.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
            }
            return true;
        } catch (IOException e7) {
            e = e7;
            inputStream = openInputStream;
            e.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e8) {
                    e8.printStackTrace();
                    return true;
                }
            }
            return true;
        } catch (Throwable th2) {
            th = th2;
            inputStream = openInputStream;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e9) {
                    e9.printStackTrace();
                    throw th;
                }
            }
            throw th;
        }
    }

    public static boolean checkIsBitmap(Context context, Uri uri) {
        InputStream openInputStream;
        InputStream inputStream = null;
        try {
            try {
                openInputStream = context.getContentResolver().openInputStream(uri);
            } catch (Throwable th) {
                th = th;
            }
        } catch (FileNotFoundException e) {
            e = e;
        } catch (IOException e2) {
            e = e2;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(openInputStream, null, options);
            openInputStream.close();
            if (options.outWidth > -1) {
                if (options.outHeight > -1) {
                    try {
                        openInputStream.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    return true;
                }
            }
            try {
                openInputStream.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            return false;
        } catch (FileNotFoundException e5) {
            e = e5;
            inputStream = openInputStream;
            e.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
            }
            return false;
        } catch (IOException e7) {
            e = e7;
            inputStream = openInputStream;
            e.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e8) {
                    e8.printStackTrace();
                    return false;
                }
            }
            return false;
        } catch (Throwable th2) {
            th = th2;
            inputStream = openInputStream;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e9) {
                    e9.printStackTrace();
                    throw th;
                }
            }
            throw th;
        }
    }

    public static boolean checkMaxPX(Context context, Uri uri) {
        InputStream openInputStream;
        InputStream inputStream = null;
        try {
            try {
                openInputStream = context.getContentResolver().openInputStream(uri);
            } catch (Throwable th) {
                th = th;
            }
        } catch (FileNotFoundException e) {
            e = e;
        } catch (IOException e2) {
            e = e2;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(openInputStream, null, options);
            openInputStream.close();
            if (options.outWidth <= 12032) {
                if (options.outHeight <= 12032) {
                    try {
                        openInputStream.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    return false;
                }
            }
            try {
                openInputStream.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            return true;
        } catch (FileNotFoundException e5) {
            e = e5;
            inputStream = openInputStream;
            e.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
            }
            return true;
        } catch (IOException e7) {
            e = e7;
            inputStream = openInputStream;
            e.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e8) {
                    e8.printStackTrace();
                    return true;
                }
            }
            return true;
        } catch (Throwable th2) {
            th = th2;
            inputStream = openInputStream;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e9) {
                    e9.printStackTrace();
                    throw th;
                }
            }
            throw th;
        }
    }

    public static boolean checkMattingSelectImage(Context context, Uri uri) {
        InputStream openInputStream;
        InputStream inputStream = null;
        try {
            try {
                openInputStream = context.getContentResolver().openInputStream(uri);
            } catch (Throwable th) {
                th = th;
            }
        } catch (FileNotFoundException e) {
            e = e;
        } catch (IOException e2) {
            e = e2;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(openInputStream, null, options);
            openInputStream.close();
            int i = options.outWidth;
            int i2 = options.outHeight;
            if (i > 12032 || i2 > 12032 || i < 500 || i2 < 500) {
                try {
                    openInputStream.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
                return true;
            }
            try {
                openInputStream.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            return false;
        } catch (FileNotFoundException e5) {
            e = e5;
            inputStream = openInputStream;
            e.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
            }
            return true;
        } catch (IOException e7) {
            e = e7;
            inputStream = openInputStream;
            e.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e8) {
                    e8.printStackTrace();
                    return true;
                }
            }
            return true;
        } catch (Throwable th2) {
            th = th2;
            inputStream = openInputStream;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e9) {
                    e9.printStackTrace();
                    throw th;
                }
            }
            throw th;
        }
    }

    public static Bitmap getResizeBitmap(Bitmap bitmap, int i, int i2) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(0, 0, createBitmap.getWidth(), createBitmap.getHeight()), new Paint());
        bitmap.recycle();
        return createBitmap;
    }
}
