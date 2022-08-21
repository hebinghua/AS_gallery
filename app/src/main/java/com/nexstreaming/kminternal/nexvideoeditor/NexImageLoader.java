package com.nexstreaming.kminternal.nexvideoeditor;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import ch.qos.logback.classic.pattern.CallerDataConverter;
import com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader;
import com.nexstreaming.app.common.nexasset.assetpackage.f;
import com.nexstreaming.nexeditorsdk.nexClip;
import com.xiaomi.stat.a.j;
import com.xiaomi.stat.b.h;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: classes3.dex */
public final class NexImageLoader {
    private static final String LOG_TAG = "NexImageLoader";
    private static final int MAX_USERSTRINGS = 3;
    private static final int NXT_ALIGNMASK = 15;
    private static final int NXT_ALIGN_CENTER = 1;
    private static final int NXT_ALIGN_LEFT = 0;
    private static final int NXT_ALIGN_RIGHT = 2;
    private static final int NXT_BLUR_INNER = 2;
    private static final int NXT_BLUR_NORMAL = 0;
    private static final int NXT_BLUR_OUTER = 3;
    private static final int NXT_BLUR_SOLID = 1;
    private static final int NXT_LONGTEXT_CROP_END = 0;
    private static final int NXT_LONGTEXT_ELLIPSIZE_END = 4;
    private static final int NXT_LONGTEXT_ELLIPSIZE_MIDDLE = 3;
    private static final int NXT_LONGTEXT_ELLIPSIZE_START = 2;
    private static final int NXT_LONGTEXT_WRAP = 1;
    private static final int NXT_TEXTFLAG_AUTOSIZE = 1024;
    private static final int NXT_TEXTFLAG_BOLD = 1;
    private static final int NXT_TEXTFLAG_CUTOUT = 2048;
    private static final int NXT_TEXTFLAG_FILL = 4;
    private static final int NXT_TEXTFLAG_ITALIC = 2;
    private static final int NXT_TEXTFLAG_LINEAR = 512;
    private static final int NXT_TEXTFLAG_SHADOW = 256;
    private static final int NXT_TEXTFLAG_STRIKE = 32;
    private static final int NXT_TEXTFLAG_STROKE = 8;
    private static final int NXT_TEXTFLAG_STROKEBACK = 4096;
    private static final int NXT_TEXTFLAG_SUBPIXEL = 128;
    private static final int NXT_TEXTFLAG_UNDERLINE = 16;
    private static final int NXT_VALIGNMASK = 240;
    private static final int NXT_VALIGN_BOTTOM = 32;
    private static final int NXT_VALIGN_CENTER = 16;
    private static final int NXT_VALIGN_TOP = 0;
    private static final String TAG_Overlay = "[Overlay]";
    private static final String TAG_PreviewThemeImage = "[PvwThImage]";
    private static final String TAG_Text = "[Text]";
    private static final String TAG_ThemeImage = "[ThemeImage]";
    private static final String TYPEFACE_ASSET = "asset:";
    private static final String TYPEFACE_FILE = "file:";
    private static final String TYPEFACE_FONTFILE = "fontfile:";
    private static final String TYPEFACE_FONTID = "fontid:";
    private static final String TYPEFACE_SYSTEM = "android:";
    private static final String TYPEFACE_THEME = "theme:";
    private AssetManager m_assetManager;
    private com.nexstreaming.kminternal.nexvideoeditor.a m_effectResourceLoader;
    private int m_jpegMaxHeight;
    private int m_jpegMaxSize;
    private int m_jpegMaxWidth;
    private d m_overlayPathResolver;
    private static Map<a, WeakReference<Bitmap>> sBitmapCache = new HashMap();
    private static WeakHashMap<Bitmap, c> sLoadedBitmapCache = new WeakHashMap<>();
    private static int sCleanCacheCount = 0;
    private static final Object sBitmapCacheLock = new Object();

    /* loaded from: classes3.dex */
    public static abstract class d {
        public abstract String a(String str);
    }

    public NexImageLoader(Resources resources, com.nexstreaming.kminternal.nexvideoeditor.a aVar, d dVar, int i, int i2, int i3) {
        if (resources == null) {
            this.m_assetManager = null;
        } else {
            this.m_assetManager = resources.getAssets();
        }
        this.m_effectResourceLoader = aVar;
        this.m_overlayPathResolver = dVar;
        this.m_jpegMaxWidth = i;
        this.m_jpegMaxHeight = i2;
        this.m_jpegMaxSize = i3;
    }

    public void setResources(Resources resources) {
        if (resources == null) {
            this.m_assetManager = null;
        } else {
            this.m_assetManager = resources.getAssets();
        }
    }

    private String pdecode(String str) {
        int i;
        StringBuilder sb = new StringBuilder(str);
        int i2 = -1;
        while (true) {
            i2 = sb.indexOf("%", i2 + 1);
            if (i2 == -1 || (i = i2 + 2) >= sb.length()) {
                break;
            }
            int i3 = i2 + 1;
            int indexOf = "0123456789ABCDEF".indexOf(str.charAt(i3));
            int indexOf2 = "0123456789ABCDEF".indexOf(str.charAt(i));
            if (indexOf != -1 && indexOf2 != -1) {
                sb.setCharAt(i2, (char) ((indexOf << 4) | indexOf2));
                sb.delete(i3, i2 + 3);
                str = sb.toString();
            }
        }
        return sb.toString();
    }

    public byte[] openThemeFile(String str) {
        String str2;
        int indexOf = str.indexOf(47);
        int i = 0;
        if (indexOf >= 0) {
            str2 = str.substring(0, indexOf);
            str = str.substring(indexOf + 1);
        } else {
            str2 = "";
        }
        com.nexstreaming.kminternal.nexvideoeditor.a aVar = this.m_effectResourceLoader;
        if (aVar != null) {
            try {
                if (aVar.a(str2, str).exists()) {
                    InputStream b2 = this.m_effectResourceLoader.b(str2, str);
                    Log.e(LOG_TAG, "get size begin loading bitmap for effect(" + str2 + ") : " + str);
                    int i2 = 0;
                    while (true) {
                        int skip = (int) b2.skip(2147483647L);
                        if (skip <= 0) {
                            break;
                        }
                        i2 += skip;
                    }
                    b2.close();
                    InputStream b3 = this.m_effectResourceLoader.b(str2, str);
                    Log.e(LOG_TAG, "get size end loading bitmap for effect(" + str2 + ") : " + str + " size=" + i2);
                    byte[] bArr = new byte[i2];
                    int i3 = i2;
                    do {
                        int read = b3.read(bArr, i, i3);
                        if (-1 == read) {
                            break;
                        }
                        i += read;
                        i3 = i2 - i;
                    } while (i3 > 0);
                    return bArr;
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error loading bitmap for effect(" + str2 + ") : " + str);
                e.printStackTrace();
            }
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:199:0x06fe, code lost:
        if (r12 <= r9) goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x0701, code lost:
        r9 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x0702, code lost:
        r12 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:203:0x0703, code lost:
        if (r15 <= r11) goto L133;
     */
    /* JADX WARN: Removed duplicated region for block: B:119:0x059b  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x05a0  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x05ca  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x05f5  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x060e  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0615  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x061e  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x0625  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0631  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x063d  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0649  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x064e  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x0656  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0659  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x065c  */
    /* JADX WARN: Removed duplicated region for block: B:260:0x0853  */
    /* JADX WARN: Removed duplicated region for block: B:313:0x09df  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x09ed  */
    /* JADX WARN: Removed duplicated region for block: B:351:0x0afb  */
    /* JADX WARN: Removed duplicated region for block: B:353:0x0b09  */
    /* JADX WARN: Removed duplicated region for block: B:384:0x0bca  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.nexstreaming.kminternal.nexvideoeditor.NexImage openThemeImage(java.lang.String r49) {
        /*
            Method dump skipped, instructions count: 3053
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.kminternal.nexvideoeditor.NexImageLoader.openThemeImage(java.lang.String):com.nexstreaming.kminternal.nexvideoeditor.NexImage");
    }

    /* loaded from: classes3.dex */
    public static class c {
        private int a;
        private int b;
        private int c;

        private c(int i, int i2, int i3) {
            this.a = i;
            this.b = i2;
            this.c = i3;
        }

        private c(int i, int i2) {
            this.a = i;
            this.b = i2;
            this.c = 1;
        }

        public int a() {
            return this.a;
        }

        public int b() {
            return this.b;
        }

        public int c() {
            return this.c;
        }
    }

    /* loaded from: classes3.dex */
    public static class b {
        private Bitmap a;
        private int b;
        private int c;
        private int d;

        private b(Bitmap bitmap, int i, int i2, int i3) {
            this.b = i;
            this.c = i2;
            this.a = bitmap;
            this.d = i3;
        }

        private b(Bitmap bitmap, int i, int i2) {
            this.b = i;
            this.c = i2;
            this.a = bitmap;
            this.d = 1;
        }

        public Bitmap a() {
            return this.a;
        }

        public int b() {
            return this.b;
        }

        public int c() {
            return this.c;
        }

        public int d() {
            return this.d;
        }
    }

    public static b loadBitmap(String str, int i, int i2) {
        return loadBitmap(str, i, i2, Integer.MAX_VALUE, 0);
    }

    public static b loadBitmap(InputStream inputStream, int i, int i2, int i3) {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        BitmapFactory.Options options = new BitmapFactory.Options();
        int i4 = 1;
        options.inJustDecodeBounds = true;
        try {
            bufferedInputStream.mark(inputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.decodeStream(bufferedInputStream, null, options);
        try {
            bufferedInputStream.reset();
            options.inJustDecodeBounds = false;
            int i5 = options.outWidth;
            int i6 = options.outHeight;
            while (i4 < 8) {
                int i7 = options.outWidth;
                if (i7 / i4 <= i || options.outHeight / i4 <= i2) {
                    if (((i7 / i4) * options.outHeight) / i4 <= (i3 > 0 ? i3 : 1500000)) {
                        break;
                    }
                }
                i4 *= 2;
            }
            options.inSampleSize = i4;
            Log.d(LOG_TAG, "loadBitmap from stream width=" + options.outWidth + " height=" + options.outHeight + " sampleSize=" + i4);
            Bitmap decodeStream = BitmapFactory.decodeStream(bufferedInputStream, null, options);
            try {
                bufferedInputStream.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            if (decodeStream == null) {
                return new b((Bitmap) null, 0, 0);
            }
            return new b(decodeStream, i5, i6);
        } catch (IOException e3) {
            throw new RuntimeException("Failed to reset stream", e3);
        }
    }

    public static b loadBitmap(String str, int i, int i2, int i3, int i4) {
        return loadBitmap(str, i, i2, i3, null, i4);
    }

    /* loaded from: classes3.dex */
    public static class a {
        public final String a;
        public final int b;
        public final int c;
        public final int d;
        public final Bitmap.Config e;
        public final boolean f;

        private a(String str, int i, int i2, int i3, Bitmap.Config config, boolean z) {
            this.a = str == null ? "" : str;
            this.b = i;
            this.c = i2;
            this.d = i3;
            this.e = config;
            this.f = z;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof a)) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            a aVar = (a) obj;
            return this.b == aVar.b && this.c == aVar.c && this.d == aVar.d && this.e == aVar.e && this.a.equals(aVar.a) && this.f == aVar.f;
        }

        public int hashCode() {
            int hashCode = this.a.hashCode() + (this.b * 191) + (this.c * 61) + (this.d * 199);
            Bitmap.Config config = this.e;
            return hashCode + (config == null ? 0 : config.hashCode());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x023f  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0260 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.nexstreaming.kminternal.nexvideoeditor.NexImageLoader.b loadBitmap(java.lang.String r18, int r19, int r20, int r21, android.graphics.Bitmap.Config r22, int r23) {
        /*
            Method dump skipped, instructions count: 658
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.kminternal.nexvideoeditor.NexImageLoader.loadBitmap(java.lang.String, int, int, int, android.graphics.Bitmap$Config, int):com.nexstreaming.kminternal.nexvideoeditor.NexImageLoader$b");
    }

    public static b loadBitmapThumb(String str, int i, int i2, int i3, Bitmap.Config config) {
        b bVar;
        Bitmap bitmap;
        c cVar;
        Log.d(LOG_TAG, "loadBitmapThumb");
        a aVar = new a(str, i, i2, i3, config, true);
        synchronized (sBitmapCacheLock) {
            WeakReference<Bitmap> weakReference = sBitmapCache.get(aVar);
            b bVar2 = (weakReference == null || (bitmap = weakReference.get()) == null || (cVar = sLoadedBitmapCache.get(bitmap)) == null) ? null : new b(bitmap, cVar.a(), cVar.b(), cVar.c());
            int i4 = sCleanCacheCount + 1;
            sCleanCacheCount = i4;
            if (i4 > 30) {
                sCleanCacheCount = 0;
                ArrayList<a> arrayList = null;
                for (Map.Entry<a, WeakReference<Bitmap>> entry : sBitmapCache.entrySet()) {
                    if (entry.getValue().get() == null) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(entry.getKey());
                    }
                }
                if (arrayList != null) {
                    for (a aVar2 : arrayList) {
                        sBitmapCache.remove(aVar2);
                    }
                }
            }
            if (bVar2 != null) {
                return bVar2;
            }
            Bitmap thumbnail = getThumbnail(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), str);
            if (thumbnail == null) {
                return new b(null, 0, 0, 0);
            }
            int thumbnailOrientation = getThumbnailOrientation(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), str);
            if (thumbnailOrientation == 90) {
                bVar = new b(rotateImage(thumbnail, 90), 720, 1280, 2);
            } else if (thumbnailOrientation == 180) {
                bVar = new b(rotateImage(thumbnail, nexClip.kClip_Rotate_180), 1280, 720, 2);
            } else if (thumbnailOrientation == 270) {
                bVar = new b(rotateImage(thumbnail, nexClip.kClip_Rotate_270), 720, 1280, 2);
            } else {
                bVar = new b(thumbnail, 1280, 720, 2);
            }
            sBitmapCache.put(aVar, new WeakReference<>(thumbnail));
            sLoadedBitmapCache.put(thumbnail, new c(bVar.b(), bVar.c(), bVar.d()));
            return bVar;
        }
    }

    public static Bitmap rotateImage(Bitmap bitmap, int i) {
        if (i == 0 || bitmap == null) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(i, bitmap.getWidth() / 2.0f, bitmap.getHeight() / 2.0f);
        try {
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return bitmap != createBitmap ? createBitmap : bitmap;
        } catch (OutOfMemoryError e) {
            Log.e(LOG_TAG, "rotateImage Error : " + e);
            return bitmap;
        }
    }

    public static Bitmap rotateAndFlipImage(Bitmap bitmap, int i, boolean z, boolean z2) {
        if ((i != 0 || z || z2) && bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.preRotate(i, bitmap.getWidth() / 2.0f, bitmap.getHeight() / 2.0f);
            float f = -1.0f;
            float f2 = z ? -1.0f : 1.0f;
            if (!z2) {
                f = 1.0f;
            }
            matrix.preScale(f2, f, bitmap.getWidth() / 2.0f, bitmap.getHeight() / 2.0f);
            try {
                Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                return bitmap != createBitmap ? createBitmap : bitmap;
            } catch (OutOfMemoryError e) {
                Log.e(LOG_TAG, "rotateImage Error : " + e);
                return bitmap;
            }
        }
        return bitmap;
    }

    public static void calcSampleSize(BitmapFactory.Options options) {
        int i = 1;
        while (i < 8) {
            int i2 = options.outWidth;
            if ((i2 / i <= 1440 || options.outHeight / i <= 810) && ((i2 / i) * options.outHeight) / i <= 1500000) {
                break;
            }
            i *= 2;
        }
        options.inSampleSize = i;
    }

    public static void calcSampleSize(BitmapFactory.Options options, int i, int i2, int i3) {
        int i4 = 1;
        while (i4 < 8) {
            int i5 = options.outWidth;
            if (i5 / i4 <= i || options.outHeight / i4 <= i2) {
                if (((i5 / i4) * options.outHeight) / i4 <= (i3 > 0 ? i3 : 1500000)) {
                    break;
                }
            }
            i4 *= 2;
        }
        options.inSampleSize = i4;
    }

    public NexImage openFile(String str, int i) {
        if (str.startsWith("@solid:") && str.endsWith(".jpg")) {
            int parseLong = (int) Long.parseLong(str.substring(7, 15), 16);
            int[] iArr = new int[576];
            for (int i2 = 0; i2 < 576; i2++) {
                iArr[i2] = parseLong;
            }
            return new NexImage(Bitmap.createBitmap(iArr, 32, 18, Bitmap.Config.ARGB_8888), 32, 18);
        } else if (str.startsWith("@assetItem:")) {
            String substring = str.substring(11);
            com.nexstreaming.kminternal.nexvideoeditor.a aVar = this.m_effectResourceLoader;
            if (aVar != null) {
                try {
                    Bitmap a2 = loadBitmap(aVar.b(substring, null), this.m_jpegMaxWidth, this.m_jpegMaxHeight, this.m_jpegMaxSize).a();
                    if (a2 != null) {
                        Log.d(LOG_TAG, "@assetItem bitmap width=" + a2.getWidth() + " height=" + a2.getHeight());
                        return new NexImage(a2, a2.getWidth() & (-2), a2.getHeight() & (-2));
                    }
                } catch (IOException unused) {
                }
            }
            return null;
        } else {
            try {
                b loadBitmap = loadBitmap(str, this.m_jpegMaxWidth, this.m_jpegMaxHeight, this.m_jpegMaxSize, i);
                Bitmap a3 = loadBitmap.a();
                int d2 = loadBitmap.d();
                if (a3 != null) {
                    Log.d(LOG_TAG, "Actual bitmap width=" + a3.getWidth() + " height=" + a3.getHeight() + ", loadedtype=" + d2);
                    return new NexImage(a3, a3.getWidth() & (-2), a3.getHeight() & (-2), d2);
                }
            } catch (Exception unused2) {
            }
            return null;
        }
    }

    public byte[] callbackReadAssetItemFile(String str, String str2) {
        String filePath;
        f c2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(str);
        if (c2 == null) {
            Log.d(LOG_TAG, "Error get assetItem id=" + str);
            return null;
        }
        try {
            AssetPackageReader a2 = AssetPackageReader.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), c2.getPackageURI(), c2.getAssetPackage().getAssetId());
            if (str2 == null || str2.length() < 1) {
                filePath = c2.getFilePath();
            } else {
                filePath = relativePath(c2.getFilePath(), str2);
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                InputStream a3 = a2.a(filePath);
                copy(a3, byteArrayOutputStream);
                a3.close();
                return byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                Log.d(LOG_TAG, "Error reading file", e);
                return null;
            }
        } catch (IOException e2) {
            Log.d(LOG_TAG, "Error making reader", e2);
            return null;
        }
    }

    private static String relativePath(String str, String str2) {
        if (str2.startsWith(CallerDataConverter.DEFAULT_RANGE_DELIMITER) || str2.contains("/..")) {
            throw new SecurityException("Parent Path References Not Allowed");
        }
        if (str.endsWith(h.g)) {
            return str + str2;
        }
        int lastIndexOf = str.lastIndexOf(47);
        if (lastIndexOf < 0) {
            return str2;
        }
        return str.substring(0, lastIndexOf + 1) + str2;
    }

    private static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[4096];
        while (true) {
            int read = inputStream.read(bArr);
            if (-1 != read) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    private static Bitmap getThumbnail(Context context, String str) {
        Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{j.c}, "_data=?", new String[]{str}, null);
        if (query != null && query.moveToFirst()) {
            int i = query.getInt(query.getColumnIndex(j.c));
            query.close();
            return MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), i, 1, null);
        }
        query.close();
        return null;
    }

    private static int getThumbnailOrientation(Context context, String str) {
        Uri uri;
        Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{j.c}, "_data=?", new String[]{str}, null);
        if (query == null || !query.moveToFirst()) {
            uri = null;
        } else {
            uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, query.getInt(query.getColumnIndex(j.c)));
            query.close();
        }
        Uri uri2 = uri;
        if (uri2 != null) {
            String[] strArr = {"orientation"};
            Cursor query2 = context.getContentResolver().query(uri2, strArr, null, null, null);
            if (query2 != null && query2.moveToFirst()) {
                return query2.getInt(query2.getColumnIndex(strArr[0]));
            }
        }
        return -1;
    }
}
