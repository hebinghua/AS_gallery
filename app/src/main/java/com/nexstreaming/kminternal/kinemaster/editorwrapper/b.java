package com.nexstreaming.kminternal.kinemaster.editorwrapper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;
import com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemCategory;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemType;
import com.nexstreaming.app.common.nexasset.assetpackage.f;
import com.nexstreaming.kminternal.kinemaster.config.EditorGlobal;
import com.nexstreaming.nexeditorsdk.nexEngine;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: LookUpTable.java */
/* loaded from: classes3.dex */
public class b {
    private static int a = 100;
    private static b b = null;
    private static int i = 100;
    private LruCache<String, C0105b> c;
    private List<c> e;
    private Context f;
    private transient WeakReference<Bitmap> g;
    private boolean h;
    private Map<String, a> d = new HashMap();
    private Object j = new Object();
    private ArrayList<Integer> k = null;

    /* compiled from: LookUpTable.java */
    /* loaded from: classes3.dex */
    public class a {
        private int b;
        private C0105b c;

        public a(int i, C0105b c0105b) {
            this.b = i;
            this.c = c0105b;
        }

        public int a() {
            return this.b;
        }

        public C0105b b() {
            return this.c;
        }
    }

    /* compiled from: LookUpTable.java */
    /* loaded from: classes3.dex */
    public class c {
        private int b;
        private String c;
        private String d;
        private String e;
        private boolean f;

        private c(int i, String str, long j, boolean z) {
            this.b = i;
            this.c = str;
            int lastIndexOf = str.lastIndexOf(".");
            this.d = str.substring(lastIndexOf < 0 ? 0 : lastIndexOf + 1);
            this.f = z;
            f();
        }

        private void f() {
            this.e = "LUT_" + this.d.toUpperCase();
            if (this.d.compareTo("disney") == 0) {
                this.e = "LUT_DBRIGHT";
            }
        }

        public String a() {
            return this.d;
        }

        public int b() {
            return this.b;
        }

        public boolean c() {
            return this.f;
        }

        public String d() {
            return this.e;
        }

        public String e() {
            return this.c;
        }
    }

    public static b a(Context context) {
        Context applicationContext = context.getApplicationContext();
        b bVar = b;
        if (bVar != null && !bVar.f.getPackageName().equals(applicationContext.getPackageName())) {
            b = null;
        }
        if (b == null) {
            b bVar2 = new b(applicationContext);
            b = bVar2;
            bVar2.d();
        }
        return b;
    }

    public void a() {
        Log.d("LookUpTable", "releaseResource2LookUpTable()");
        LruCache<String, C0105b> lruCache = this.c;
        if (lruCache != null) {
            lruCache.evictAll();
            this.c = null;
        }
        List<c> list = this.e;
        if (list != null) {
            list.clear();
            this.e = null;
        }
        b = null;
    }

    public void b() {
        synchronized (this.j) {
            LruCache<String, C0105b> lruCache = this.c;
            if (lruCache != null && lruCache.size() > 0) {
                this.c.evictAll();
            }
        }
    }

    public static b c() {
        return b;
    }

    private b(Context context) {
        this.f = context;
    }

    public int d() {
        List<c> list = this.e;
        if (list != null) {
            list.clear();
        } else {
            this.e = new ArrayList();
        }
        int i2 = 1;
        for (f fVar : com.nexstreaming.app.common.nexasset.assetpackage.c.a().a(ItemCategory.filter)) {
            if (fVar.getType() == ItemType.lut) {
                this.e.add(new c(i2, fVar.getId(), 0L, fVar.isHidden()));
                i2++;
            }
        }
        for (Map.Entry<String, a> entry : this.d.entrySet()) {
            this.e.add(new c(entry.getValue().a(), entry.getKey(), 0L, false));
            i2++;
        }
        return i2 - 1;
    }

    private int i() {
        int intValue;
        synchronized (this) {
            if (this.k == null) {
                this.k = new ArrayList<>();
                for (int i2 = i; i2 < i + 10; i2++) {
                    this.k.add(Integer.valueOf(i2));
                }
            }
            intValue = this.k.get(0).intValue();
            this.k.remove(0);
        }
        return intValue;
    }

    private void b(int i2) {
        synchronized (this) {
            this.k.add(Integer.valueOf(i2));
        }
    }

    public int a(String str, C0105b c0105b) throws Exception {
        if (this.d.size() >= 10) {
            throw new Exception("Exceed the capacity of custom lut - it is 10");
        }
        j();
        a aVar = new a(i(), c0105b);
        this.d.put(str, aVar);
        h();
        return aVar.a();
    }

    public int a(String str) {
        a aVar = this.d.get(str);
        if (aVar != null) {
            this.d.remove(str);
            EditorGlobal.a().m(aVar.a());
            b(aVar.a());
            h();
            return 0;
        }
        return 0;
    }

    public void e() {
        for (a aVar : this.d.values()) {
            EditorGlobal.a().m(aVar.a());
            b(aVar.a());
        }
        this.d.clear();
        h();
    }

    public boolean b(String str) {
        return this.d.get(str) != null;
    }

    public int c(String str) {
        k();
        List<c> list = this.e;
        if (list == null || list.size() == 0) {
            return 0;
        }
        a aVar = this.d.get(str);
        if (aVar != null) {
            return aVar.a();
        }
        for (int i2 = 0; i2 < this.e.size(); i2++) {
            if (this.e.get(i2).d.compareTo(str) == 0) {
                return this.e.get(i2).b;
            }
        }
        return 0;
    }

    private int c(int i2) {
        k();
        List<c> list = this.e;
        if (list == null || list.size() == 0) {
            return -1;
        }
        for (int i3 = 0; i3 < this.e.size(); i3++) {
            if (this.e.get(i3).b == i2) {
                return i3;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int e(String str) {
        k();
        List<c> list = this.e;
        if (list == null || list.size() == 0) {
            return -1;
        }
        for (int i2 = 0; i2 < this.e.size(); i2++) {
            if (this.e.get(i2).d.compareTo(str) == 0) {
                return i2;
            }
        }
        return -1;
    }

    /* compiled from: LookUpTable.java */
    /* renamed from: com.nexstreaming.kminternal.kinemaster.editorwrapper.b$b  reason: collision with other inner class name */
    /* loaded from: classes3.dex */
    public class C0105b {
        private int b;
        private int c;
        private IntBuffer d;
        private Bitmap e;

        private Bitmap a(int[] iArr) {
            int[] array = IntBuffer.allocate(nexEngine.ExportHEVCMainTierLevel52).array();
            for (int i = 0; i < 8; i++) {
                for (int i2 = 0; i2 < 8; i2++) {
                    for (int i3 = 0; i3 < 64; i3++) {
                        for (int i4 = 0; i4 < 64; i4++) {
                            array[(i * 64) + (i2 * 512 * 64) + i3 + (i4 * 512)] = iArr[(((262080 - ((i * 512) * 64)) - ((i2 * 64) * 64)) + i3) - (i4 * 64)];
                        }
                    }
                }
            }
            return Bitmap.createBitmap(array, 512, 512, Bitmap.Config.ARGB_8888);
        }

        public C0105b(byte[] bArr, int i, int i2, int i3) {
            if (i3 == 2) {
                int[] b = EditorGlobal.a().b(bArr, i, i2);
                if (b != null) {
                    this.e = a(b);
                }
            } else if (i3 == 1) {
                int[] a = EditorGlobal.a().a(bArr, i, i2);
                if (a != null) {
                    this.e = a(a);
                }
            } else if (i3 == 0) {
                BitmapFactory.decodeByteArray(bArr, i, i2);
            }
            Bitmap bitmap = this.e;
            if (bitmap != null) {
                this.b = bitmap.getWidth();
                this.c = this.e.getHeight();
            }
        }

        public C0105b(Bitmap bitmap) {
            IntBuffer allocate = IntBuffer.allocate(nexEngine.ExportHEVCMainTierLevel52);
            this.d = IntBuffer.allocate(nexEngine.ExportHEVCMainTierLevel52);
            bitmap.getPixels(allocate.array(), 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            int[] array = allocate.array();
            int[] array2 = this.d.array();
            for (int i = 0; i < 8; i++) {
                for (int i2 = 0; i2 < 8; i2++) {
                    for (int i3 = 0; i3 < 64; i3++) {
                        for (int i4 = 0; i4 < 64; i4++) {
                            array2[(i * 64) + (i2 * 512 * 64) + i3 + (i4 * 512)] = array[(((262080 - ((i * 512) * 64)) - ((i2 * 64) * 64)) + i3) - (i4 * 64)];
                        }
                    }
                }
            }
            Bitmap createBitmap = Bitmap.createBitmap(array2, 512, 512, bitmap.getConfig());
            this.e = createBitmap;
            this.b = createBitmap.getWidth();
            this.c = this.e.getHeight();
        }

        public int[] a() {
            return this.d.array();
        }

        public Bitmap b() {
            return this.e;
        }
    }

    public List<c> f() {
        k();
        return this.e;
    }

    private void j() {
        synchronized (this.j) {
            if (this.c == null) {
                this.c = new LruCache(a) { // from class: com.nexstreaming.kminternal.kinemaster.editorwrapper.b.1
                    @Override // android.util.LruCache
                    public void entryRemoved(boolean z, Object obj, Object obj2, Object obj3) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("The entry is being removed / key:");
                        String str = (String) obj;
                        sb.append(str);
                        Log.d("LookUpTable", sb.toString());
                        ((C0105b) obj2).b().recycle();
                        int e = b.this.e(str);
                        if (e < 0) {
                            Log.d("LookUpTable", "The entry is not found");
                            return;
                        }
                        Log.d("LookUpTable", "The entry index =" + e + ", id=" + ((c) b.this.e.get(e)).b);
                        EditorGlobal.a().m(((c) b.this.e.get(e)).b);
                    }
                };
            }
        }
    }

    public C0105b a(int i2) {
        C0105b c0105b;
        LruCache<String, C0105b> lruCache;
        String str;
        C0105b c0105b2;
        int c2 = c(i2);
        AssetPackageReader assetPackageReader = null;
        if (c2 < 0) {
            return null;
        }
        j();
        synchronized (this.j) {
            c0105b = this.c.get(this.e.get(c2).c);
            if (c0105b == null) {
                a aVar = this.d.get(this.e.get(c2).c);
                if (aVar != null) {
                    c0105b = aVar.b();
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inScaled = false;
                    f c3 = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(this.e.get(c2).c);
                    if (c3 != null) {
                        try {
                            try {
                                AssetPackageReader a2 = AssetPackageReader.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), c3.getPackageURI(), c3.getAssetPackage().getAssetId());
                                if (a2 != null) {
                                    try {
                                        try {
                                            InputStream a3 = a2.a(c3.getFilePath());
                                            Bitmap decodeStream = BitmapFactory.decodeStream(a3, null, options);
                                            a3.close();
                                            lruCache = this.c;
                                            str = this.e.get(c2).c;
                                            c0105b2 = new C0105b(decodeStream);
                                        } catch (Throwable th) {
                                            th = th;
                                            assetPackageReader = a2;
                                            com.nexstreaming.app.common.util.b.a(assetPackageReader);
                                            throw th;
                                        }
                                    } catch (IOException e) {
                                        e = e;
                                    }
                                    try {
                                        lruCache.put(str, c0105b2);
                                        c0105b = c0105b2;
                                    } catch (IOException e2) {
                                        e = e2;
                                        c0105b = c0105b2;
                                        assetPackageReader = a2;
                                        e.printStackTrace();
                                        com.nexstreaming.app.common.util.b.a(assetPackageReader);
                                        return c0105b;
                                    }
                                }
                                com.nexstreaming.app.common.util.b.a(a2);
                            } catch (Throwable th2) {
                                th = th2;
                            }
                        } catch (IOException e3) {
                            e = e3;
                        }
                    }
                }
            }
        }
        return c0105b;
    }

    public Bitmap a(Bitmap bitmap, int i2) {
        C0105b a2 = a(i2);
        if (a2 == null) {
            return null;
        }
        return a(bitmap, a2);
    }

    private Bitmap a(Bitmap bitmap, C0105b c0105b) {
        int i2;
        int[] a2 = c0105b.a();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        IntBuffer allocate = IntBuffer.allocate(width * height);
        int i3 = 0;
        bitmap.getPixels(allocate.array(), 0, width, 0, 0, width, height);
        int[] array = allocate.array();
        int length = array.length;
        while (i3 < length) {
            int i4 = array[i3];
            float f = (((i4 >> 16) & 255) / 256.0f) * 63.0f;
            float f2 = (((i4 >> 8) & 255) / 256.0f) * 63.0f;
            double d = ((i4 & 255) / 256.0f) * 63.0f;
            int floor = (int) Math.floor(d);
            int ceil = (int) Math.ceil(d);
            int i5 = floor % 8;
            int i6 = ceil % 8;
            int i7 = ((floor - i5) / 8) * 64;
            int i8 = i5 * 64;
            int i9 = ((ceil - i6) / 8) * 64;
            int i10 = i6 * 64;
            double d2 = f;
            int floor2 = (int) Math.floor(d2);
            int[] iArr = array;
            int i11 = length;
            double d3 = f2;
            int floor3 = (int) Math.floor(d3);
            int ceil2 = (int) Math.ceil(d2);
            int ceil3 = (int) Math.ceil(d3);
            int i12 = a2[((i8 + floor2) * 512) + i7 + floor3];
            int i13 = a2[((i8 + ceil2) * 512) + i7 + floor3];
            float f3 = f - floor2;
            float f4 = f2 - floor3;
            Math.floor(d);
            int i14 = (int) ((((i12 >> 8) & 255) * (1.0f - f4)) + (((a2[i2 + ceil3] >> 8) & 255) * f4));
            int i15 = ((floor2 + i10) * 512) + i9;
            int i16 = a2[i15 + floor3];
            int i17 = a2[((i10 + ceil2) * 512) + i9 + floor3];
            int i18 = a2[i15 + ceil3];
            iArr[i3] = (-16777216) | (((int) ((((i12 >> 16) & 255) * (1.0f - f3)) + (((i13 >> 16) & 255) * f3))) << 16) | (i14 << 8) | (i12 & 255);
            i3++;
            array = iArr;
            length = i11;
        }
        int[] iArr2 = array;
        if (bitmap.getConfig() != null) {
            return Bitmap.createBitmap(iArr2, bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        }
        return Bitmap.createBitmap(iArr2, bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
    }

    public final Bitmap g() {
        WeakReference<Bitmap> weakReference = this.g;
        Bitmap bitmap = weakReference != null ? weakReference.get() : null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        if (bitmap == null) {
            File file = new File(EditorGlobal.f().getAbsolutePath() + File.separator + "vignette.webp");
            if (file.isFile()) {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            } else {
                try {
                    bitmap = BitmapFactory.decodeStream(this.f.getResources().getAssets().open("vignette.webp"), null, options);
                } catch (IOException e) {
                    e.printStackTrace();
                    bitmap = null;
                }
            }
        }
        if (bitmap == null) {
            return null;
        }
        this.g = new WeakReference<>(bitmap);
        return bitmap;
    }

    public c d(String str) {
        k();
        for (c cVar : this.e) {
            if (cVar.c.compareTo(str) == 0) {
                return cVar;
            }
        }
        return null;
    }

    public String[] a(boolean z) {
        k();
        ArrayList arrayList = new ArrayList();
        for (c cVar : this.e) {
            if (z) {
                arrayList.add(cVar.c);
            } else if (!cVar.c()) {
                arrayList.add(cVar.c);
            }
        }
        int size = arrayList.size();
        String[] strArr = new String[size];
        for (int i2 = 0; i2 < size; i2++) {
            strArr[i2] = (String) arrayList.get(i2);
        }
        return strArr;
    }

    public void h() {
        this.h = true;
    }

    private void k() {
        if (this.h) {
            this.h = false;
            d();
        }
    }
}
