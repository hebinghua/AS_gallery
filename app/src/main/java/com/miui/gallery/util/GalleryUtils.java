package com.miui.gallery.util;

import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.data.DecodeUtils;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.deprecated.GalleryCloudProvider;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.FeatureHelper;
import com.xiaomi.stat.a.j;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Locale;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class GalleryUtils {
    public static Handler sHandler;

    /* loaded from: classes2.dex */
    public interface ConcatConverter<T> {
        String convertToString(T t);
    }

    /* loaded from: classes2.dex */
    public interface QueryHandler<T> {
        /* renamed from: handle */
        T mo1712handle(Cursor cursor);
    }

    public static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean copyFile(InputStream inputStream, OutputStream outputStream) {
        byte[] bArr = new byte[102400];
        while (true) {
            try {
                int read = inputStream.read(bArr);
                if (read != -1) {
                    outputStream.write(bArr, 0, read);
                } else {
                    outputStream.flush();
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                closeStream(inputStream);
                closeStream(outputStream);
            }
        }
    }

    public static boolean saveBitmapToOutputStream(Bitmap bitmap, Bitmap.CompressFormat compressFormat, OutputStream outputStream) {
        if (bitmap == null) {
            return false;
        }
        try {
            if (!bitmap.compress(compressFormat, 90, outputStream)) {
                return false;
            }
            Utils.closeSilently(outputStream);
            return true;
        } finally {
            Utils.closeSilently(outputStream);
        }
    }

    public static Bitmap.CompressFormat convertExtensionToCompressFormat(String str) {
        if (str.equals("png")) {
            return Bitmap.CompressFormat.PNG;
        }
        return Bitmap.CompressFormat.JPEG;
    }

    public static Uri safeInsert(Uri uri, ContentValues contentValues) {
        try {
            return GalleryApp.sGetAndroidContext().getContentResolver().insert(uri, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void putMixedDateTime(ContentValues contentValues) {
        String asString = contentValues.getAsString("exifGPSDateStamp");
        String asString2 = contentValues.getAsString("exifGPSTimeStamp");
        String asString3 = contentValues.getAsString("exifDateTime");
        Long asLong = contentValues.getAsLong("dateTaken");
        Long asLong2 = contentValues.getAsLong("dateModified");
        contentValues.put("mixedDateTime", Long.valueOf(GalleryTimeUtils.getTakenDateTime(asString, asString2, asString3, asLong != null ? asLong.longValue() : 0L, asLong2 != null ? asLong2.longValue() : 0L)));
    }

    public static Uri safeInsertImage(Uri uri, ContentValues contentValues) {
        putMixedDateTime(contentValues);
        return safeInsert(uri, contentValues);
    }

    public static int safeUpdateImage(ContentValues contentValues, DBImage dBImage) {
        putMixedDateTime(contentValues);
        return safeUpdate(dBImage.getBaseUri(), contentValues, String.format(Locale.US, "%s=?", j.c), new String[]{dBImage.getId()});
    }

    public static int safeUpdate(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        try {
            return GalleryApp.sGetAndroidContext().getContentResolver().update(uri, contentValues, str, strArr);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int safeDelete(Uri uri, String str, String[] strArr) {
        try {
            return GalleryApp.sGetAndroidContext().getContentResolver().delete(uri, str, strArr);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean safeExec(String str) {
        try {
            return GalleryDBHelper.getInstance().execSQL(str);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Cursor queryToCursor(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        final ContentProviderClient contentProviderClient;
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        if (sGetAndroidContext == null) {
            return null;
        }
        try {
            if (GalleryCloudProvider.isCloudUri(uri)) {
                contentProviderClient = GalleryApp.sGetAndroidContext().getContentResolver().acquireContentProviderClient("com.miui.gallery.cloud.provider");
                try {
                    ContentProvider localContentProvider = contentProviderClient.getLocalContentProvider();
                    if (localContentProvider != null) {
                        Cursor query = localContentProvider.query(uri, strArr, str, strArr2, str2);
                        if (query == null) {
                            releaseSilently(contentProviderClient);
                            return null;
                        }
                        return new CursorWrapper(query) { // from class: com.miui.gallery.util.GalleryUtils.1
                            @Override // android.database.CursorWrapper, android.database.Cursor, java.io.Closeable, java.lang.AutoCloseable
                            public void close() {
                                super.close();
                                GalleryUtils.releaseSilently(contentProviderClient);
                            }
                        };
                    }
                    releaseSilently(contentProviderClient);
                } catch (Exception e) {
                    e = e;
                    releaseSilently(contentProviderClient);
                    e.printStackTrace();
                    return null;
                }
            }
            return sGetAndroidContext.getContentResolver().query(uri, strArr, str, strArr2, str2);
        } catch (Exception e2) {
            e = e2;
            contentProviderClient = null;
        }
    }

    public static void releaseSilently(ContentProviderClient contentProviderClient) {
        if (contentProviderClient != null) {
            try {
                contentProviderClient.release();
            } catch (Exception unused) {
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0023  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static <T> T safeQuery(android.net.Uri r1, java.lang.String[] r2, java.lang.String r3, java.lang.String[] r4, java.lang.String r5, com.miui.gallery.util.GalleryUtils.QueryHandler<T> r6) {
        /*
            r0 = 0
            android.database.Cursor r1 = queryToCursor(r1, r2, r3, r4, r5)     // Catch: java.lang.Throwable -> L14 java.lang.Exception -> L16
            java.lang.Object r2 = r6.mo1712handle(r1)     // Catch: java.lang.Throwable -> Lf java.lang.Exception -> L12
            if (r1 == 0) goto Le
            r1.close()
        Le:
            return r2
        Lf:
            r2 = move-exception
            r0 = r1
            goto L21
        L12:
            r2 = move-exception
            goto L18
        L14:
            r2 = move-exception
            goto L21
        L16:
            r2 = move-exception
            r1 = r0
        L18:
            r2.printStackTrace()     // Catch: java.lang.Throwable -> Lf
            if (r1 == 0) goto L20
            r1.close()
        L20:
            return r0
        L21:
            if (r0 == 0) goto L26
            r0.close()
        L26:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.GalleryUtils.safeQuery(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String, com.miui.gallery.util.GalleryUtils$QueryHandler):java.lang.Object");
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x003e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static <T> T safeQuery(java.lang.String r2, java.lang.String[] r3, java.lang.String r4, java.lang.String[] r5, java.lang.String r6, com.miui.gallery.util.GalleryUtils.QueryHandler<T> r7) {
        /*
            r0 = 0
            com.miui.gallery.provider.GalleryDBHelper r1 = com.miui.gallery.provider.GalleryDBHelper.getInstance()     // Catch: java.lang.Throwable -> L2d java.lang.Exception -> L2f
            androidx.sqlite.db.SupportSQLiteDatabase r1 = r1.getReadableDatabase()     // Catch: java.lang.Throwable -> L2d java.lang.Exception -> L2f
            androidx.sqlite.db.SupportSQLiteQueryBuilder r2 = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(r2)     // Catch: java.lang.Throwable -> L2d java.lang.Exception -> L2f
            androidx.sqlite.db.SupportSQLiteQueryBuilder r2 = r2.columns(r3)     // Catch: java.lang.Throwable -> L2d java.lang.Exception -> L2f
            androidx.sqlite.db.SupportSQLiteQueryBuilder r2 = r2.selection(r4, r5)     // Catch: java.lang.Throwable -> L2d java.lang.Exception -> L2f
            androidx.sqlite.db.SupportSQLiteQueryBuilder r2 = r2.orderBy(r6)     // Catch: java.lang.Throwable -> L2d java.lang.Exception -> L2f
            androidx.sqlite.db.SupportSQLiteQuery r2 = r2.create()     // Catch: java.lang.Throwable -> L2d java.lang.Exception -> L2f
            android.database.Cursor r2 = r1.query(r2)     // Catch: java.lang.Throwable -> L2d java.lang.Exception -> L2f
            java.lang.Object r3 = r7.mo1712handle(r2)     // Catch: java.lang.Exception -> L2b java.lang.Throwable -> L3a
            if (r2 == 0) goto L2a
            r2.close()
        L2a:
            return r3
        L2b:
            r3 = move-exception
            goto L31
        L2d:
            r3 = move-exception
            goto L3c
        L2f:
            r3 = move-exception
            r2 = r0
        L31:
            r3.printStackTrace()     // Catch: java.lang.Throwable -> L3a
            if (r2 == 0) goto L39
            r2.close()
        L39:
            return r0
        L3a:
            r3 = move-exception
            r0 = r2
        L3c:
            if (r0 == 0) goto L41
            r0.close()
        L41:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.GalleryUtils.safeQuery(java.lang.String, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String, com.miui.gallery.util.GalleryUtils$QueryHandler):java.lang.Object");
    }

    public static <T> String concatAll(Collection<T> collection, String str) {
        return concatAll(collection, str, null);
    }

    public static <T> String concatAll(Collection<T> collection, String str, ConcatConverter<T> concatConverter) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (T t : collection) {
            if (t != null) {
                String convertToString = concatConverter != null ? concatConverter.convertToString(t) : t.toString();
                if (!TextUtils.isEmpty(convertToString)) {
                    if (sb.length() > 0) {
                        sb.append(str);
                    }
                    sb.append(convertToString);
                }
            }
        }
        return sb.toString();
    }

    public static Bitmap safeDecodeFileDescriptor(FileDescriptor fileDescriptor, Rect rect, DecodeUtils.GalleryOptions galleryOptions) {
        try {
            try {
                return BitmapFactory.decodeFileDescriptor(fileDescriptor, rect, galleryOptions);
            } catch (Exception e) {
                DefaultLogger.e("GalleryUtils", "safeDecodeFileDescriptor() failed: ", e);
                return null;
            } catch (OutOfMemoryError e2) {
                DefaultLogger.e("GalleryUtils", "safeDecodeFileDescriptor() failed OOM: ", e2);
                return null;
            }
        } finally {
            closeStream(null);
        }
    }

    public static ProgressDialog showProgressDialog(Context context, int i, int i2, int i3, boolean z) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(i == 0 ? null : context.getResources().getString(i));
        progressDialog.setMessage(i2 == 0 ? null : context.getResources().getString(i2));
        progressDialog.setCancelable(z);
        progressDialog.setCanceledOnTouchOutside(false);
        if (z) {
            progressDialog.setButton(-2, context.getString(17039360), (Message) null);
        }
        if (i3 > 1) {
            progressDialog.setMax(i3);
            progressDialog.setProgressStyle(1);
        }
        progressDialog.show();
        return progressDialog;
    }

    public static boolean safeUnregisterReceiver(Context context, BroadcastReceiver broadcastReceiver) {
        return ReceiverUtils.safeUnregisterReceiver(context, broadcastReceiver);
    }

    public static boolean needImpunityDeclaration() {
        return !FeatureHelper.isPad();
    }

    public static void runOnMainThreadPostDelay(Runnable runnable, int i) {
        if (runnable != null) {
            if (sHandler == null) {
                sHandler = new Handler(Looper.getMainLooper());
            }
            sHandler.removeCallbacks(runnable);
            sHandler.postDelayed(runnable, i);
        }
    }
}
