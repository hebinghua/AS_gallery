package com.baidu.b.b;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import com.baidu.b.a.e;
import com.baidu.b.b.a;
import com.baidu.b.h;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public class e extends com.baidu.b.b.a {
    private Context d;
    private f e;

    /* loaded from: classes.dex */
    public static final class a implements Comparable<a> {
        private static final String[] a = {"read0", "read1", "read2", "read3", "access0", "access1", "access2", "access3", "sync0", "sync1", "sync2", "sync3", "open0", "open1", "open2", "open3"};
        private final int b;

        private a(int i) {
            this.b = i;
        }

        public static a a(byte b, boolean z) {
            int i = b & 255;
            return a(z ? i >> 4 : i & 15);
        }

        public static a a(int i) {
            if (i < 0 || i >= 16) {
                throw new IllegalArgumentException("invalid idx " + i);
            }
            return new a(i);
        }

        @Override // java.lang.Comparable
        /* renamed from: a */
        public int compareTo(a aVar) {
            return this.b - aVar.b;
        }

        public String a() {
            return a[this.b];
        }

        public byte b() {
            return (byte) this.b;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && a.class == obj.getClass() && this.b == ((a) obj).b;
        }

        public int hashCode() {
            return this.b;
        }
    }

    /* loaded from: classes.dex */
    public class b {
        private int b = 33;
        private a[] c = new a[33];
        private int d;

        public b() {
        }

        private void b(int i) {
            a[] aVarArr = this.c;
            if (i - aVarArr.length > 0) {
                int length = aVarArr.length;
                int i2 = length + (length >> 1);
                if (i2 - i >= 0) {
                    i = i2;
                }
                this.c = (a[]) Arrays.copyOf(aVarArr, i);
            }
        }

        public int a() {
            return this.d;
        }

        public a a(int i) {
            if (i < this.d) {
                return this.c[i];
            }
            throw new IndexOutOfBoundsException("idx " + i + " size " + this.d);
        }

        public void a(a aVar) {
            b(this.d + 1);
            a[] aVarArr = this.c;
            int i = this.d;
            this.d = i + 1;
            aVarArr[i] = aVar;
        }

        public byte[] b() {
            int i;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i2 = 0;
            while (true) {
                i = this.d;
                if (i2 >= i / 2) {
                    break;
                }
                int i3 = i2 * 2;
                byteArrayOutputStream.write((byte) (((a(i3 + 1).b() & 255) << 4) | (a(i3).b() & 255)));
                i2++;
            }
            if (i % 2 != 0) {
                byteArrayOutputStream.write((byte) (a(i - 1).b() & 255));
            }
            return byteArrayOutputStream.toByteArray();
        }
    }

    /* loaded from: classes.dex */
    public static class c {
        private List<a> a = new ArrayList();

        /* loaded from: classes.dex */
        public static class a {
            private int a;
            private a b;

            public a(a aVar) {
                this.b = aVar;
            }

            public void a() {
                this.a++;
            }
        }

        public List<a> a() {
            ArrayList arrayList = new ArrayList(this.a);
            Collections.sort(arrayList, new com.baidu.b.b.f(this));
            return arrayList;
        }

        public void a(a aVar) {
            this.a.add(new a(aVar));
        }
    }

    /* loaded from: classes.dex */
    public static class d {
        public byte[] a;
        public byte b;
        public byte[] c;

        public d(byte[] bArr, byte b, byte[] bArr2) {
            this.a = bArr;
            this.b = b;
            this.c = bArr2;
        }

        public h.a a() {
            try {
                String a = com.baidu.b.d.b.a(this.a, "", true);
                String str = new String(new byte[]{this.b}, Keyczar.DEFAULT_ENCODING);
                byte[] bArr = this.c;
                return h.a(a, str, bArr != null ? new String(bArr, Keyczar.DEFAULT_ENCODING) : null);
            } catch (Exception unused) {
                return null;
            }
        }
    }

    /* renamed from: com.baidu.b.b.e$e  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static class C0003e {
        public int a;
        public int b;
        public int c = 16;

        public String toString() {
            return "";
        }
    }

    /* loaded from: classes.dex */
    public static class f {
        private Method a;
        private Method b;
        private Method c;
        private Method d;
        private Method e;

        public int a(Context context, Uri uri, int i, int i2, int i3) {
            try {
                return ((Integer) this.a.invoke(context, uri, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3))).intValue();
            } catch (Exception e) {
                throw new e.a(e);
            }
        }

        public void a() {
            try {
                String a = com.baidu.b.a.e.a(com.baidu.b.a.d.d());
                Class cls = Integer.TYPE;
                this.a = com.baidu.b.a.e.a(Context.class, a, new Class[]{Uri.class, cls, cls, cls});
                this.b = com.baidu.b.a.e.a(Context.class, com.baidu.b.a.e.a(com.baidu.b.a.d.e()), new Class[]{String.class, Uri.class, cls});
                this.c = com.baidu.b.a.e.a(ContentResolver.class, com.baidu.b.a.e.a(com.baidu.b.a.d.f()), new Class[]{Uri.class, cls});
                this.d = com.baidu.b.a.e.a(Context.class, com.baidu.b.a.e.a(com.baidu.b.a.d.g()), new Class[]{Uri.class, cls});
                this.e = com.baidu.b.a.e.a(ContentResolver.class, com.baidu.b.a.e.a(com.baidu.b.a.d.h()), new Class[]{Uri.class, cls});
            } catch (Exception unused) {
            }
        }
    }

    public e() {
        super("upc", 9000000L);
        f fVar = new f();
        this.e = fVar;
        fVar.a();
    }

    private a a(String str, int i, List<c.a> list, int i2, C0003e c0003e) {
        for (c.a aVar : list) {
            if (a(str, i, aVar.b, i2, c0003e)) {
                aVar.a();
                return aVar.b;
            }
        }
        return null;
    }

    private String a(String str) {
        return str + ".cesium";
    }

    private String a(String str, int i, a aVar) {
        return String.format("content://%s/dat/v1/i%d/%s", a(str), Integer.valueOf(i), aVar.a());
    }

    private String a(String str, a aVar) {
        return String.format("content://%s/dic/v1/%s", a(str), aVar.a());
    }

    private boolean a(String str, int i, a aVar, int i2, C0003e c0003e) {
        int i3;
        Uri parse = Uri.parse(a(str, i, aVar));
        int i4 = 0;
        while (true) {
            if (i4 >= 2) {
                i3 = -1;
                break;
            }
            if (c0003e != null) {
                try {
                    c0003e.a++;
                } catch (Throwable unused) {
                    try {
                        Thread.sleep(5L);
                    } catch (Exception unused2) {
                    }
                    i4++;
                }
            }
            i3 = this.e.a(this.d, parse, 0, i2, 1);
            break;
        }
        if (i3 == 0) {
            return true;
        }
        if (c0003e != null) {
            c0003e.b++;
        }
        return false;
    }

    private boolean a(String str, a aVar, int i) {
        int i2;
        Uri parse = Uri.parse(a(str, aVar));
        int i3 = 0;
        while (true) {
            if (i3 >= 2) {
                i2 = -1;
                break;
            }
            try {
                i2 = this.e.a(this.d, parse, 0, i, 1);
                break;
            } catch (Throwable unused) {
                try {
                    Thread.sleep(5L);
                } catch (Exception unused2) {
                }
                i3++;
            }
        }
        return i2 == 0;
    }

    @Override // com.baidu.b.b.a
    public a.e a(String str, a.d dVar) {
        byte[] bArr;
        boolean z;
        Byte b2;
        if (Build.VERSION.SDK_INT < 26) {
            return a.e.b();
        }
        int i = -1;
        try {
            i = this.d.getPackageManager().getPackageUid(str, 0);
        } catch (PackageManager.NameNotFoundException unused) {
        }
        int i2 = i;
        if (i2 < 0) {
            return a.e.b();
        }
        C0003e c0003e = new C0003e();
        b bVar = new b();
        c cVar = new c();
        c cVar2 = new c();
        for (int i3 = 0; i3 < 16; i3++) {
            a a2 = a.a(i3);
            if (a(str, a2, i2)) {
                cVar.a(a2);
            } else {
                cVar2.a(a2);
            }
        }
        for (int i4 = 0; i4 < 32; i4++) {
            a a3 = a(str, i4, cVar.a(), i2, c0003e);
            if (a3 == null) {
                a3 = a(str, i4, cVar2.a(), i2, c0003e);
            }
            if (a3 == null) {
                return a.e.b();
            }
            bVar.a(a3);
        }
        byte[] b3 = bVar.b();
        int i5 = 3;
        boolean z2 = true;
        byte[] bArr2 = {"0".getBytes()[0], "O".getBytes()[0], "V".getBytes()[0]};
        int i6 = 0;
        while (true) {
            bArr = null;
            if (i6 >= i5) {
                z = z2;
                b2 = null;
                break;
            }
            byte b4 = bArr2[i6];
            a a4 = a.a(b4, false);
            int i7 = i6;
            z = z2;
            byte[] bArr3 = bArr2;
            if (a(str, 32, a4, i2, c0003e)) {
                a a5 = a.a(b4, z);
                if (a(str, 33, a5, i2, c0003e)) {
                    b bVar2 = new b();
                    bVar2.a(a4);
                    bVar2.a(a5);
                    b2 = Byte.valueOf(bVar2.b()[0]);
                    break;
                }
            }
            i6 = i7 + 1;
            z2 = z;
            bArr2 = bArr3;
            i5 = 3;
        }
        if (b2 == null) {
            b bVar3 = new b();
            int i8 = 32;
            for (int i9 = 34; i8 < i9; i9 = 34) {
                int i10 = i8;
                b bVar4 = bVar3;
                a a6 = a(str, i8, cVar.a(), i2, c0003e);
                if (a6 == null) {
                    a6 = a(str, i10, cVar2.a(), i2, c0003e);
                }
                if (a6 == null) {
                    return a.e.b();
                }
                bVar4.a(a6);
                i8 = i10 + 1;
                bVar3 = bVar4;
            }
            b2 = Byte.valueOf(bVar3.b()[0]);
        } else {
            z = false;
        }
        Byte b5 = b2;
        if (z) {
            b bVar5 = new b();
            for (int i11 = 34; i11 < 94; i11++) {
                a a7 = a(str, i11, cVar.a(), i2, c0003e);
                if (a7 == null) {
                    a7 = a(str, i11, cVar2.a(), i2, c0003e);
                }
                if (a7 == null) {
                    break;
                }
                bVar5.a(a7);
            }
            if (bVar5.a() > 0) {
                bArr = bVar5.b();
            }
        }
        return a.e.a(new d(b3, b5.byteValue(), bArr).a());
    }

    @Override // com.baidu.b.b.a
    public void a(a.c cVar) {
        this.d = this.a.a;
    }
}
