package com.baidu.location.b;

import android.location.Location;
import android.net.wifi.ScanResult;
import android.os.Build;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class w {
    private int B;
    public long a = 0;
    private a z;
    private static ArrayList<String> b = new ArrayList<>();
    private static ArrayList<String> c = new ArrayList<>();
    private static ArrayList<String> d = new ArrayList<>();
    private static String e = com.baidu.location.e.i.a + "/yo.dat";
    private static String f = com.baidu.location.e.i.a + "/yoh.dat";
    private static String g = com.baidu.location.e.i.a + "/yom.dat";
    private static String h = com.baidu.location.e.i.a + "/yol.dat";
    private static String i = com.baidu.location.e.i.a + "/yor.dat";
    private static File j = null;
    private static int k = 8;
    private static int l = 8;
    private static int m = 16;
    private static int n = 2048;
    private static double o = SearchStatUtils.POW;
    private static double p = 0.1d;
    private static double q = 30.0d;
    private static double r = 100.0d;
    private static int s = 0;
    private static int t = 64;
    private static int u = 128;
    private static Location v = null;
    private static Location w = null;
    private static Location x = null;
    private static com.baidu.location.c.h y = null;
    private static w A = null;
    private static long C = 0;

    /* loaded from: classes.dex */
    public class a extends com.baidu.location.e.e {
        public boolean a = false;
        public int b = 0;
        public int c = 0;
        private ArrayList<String> e = new ArrayList<>();
        private boolean p = true;

        public a() {
            this.j = new HashMap();
        }

        @Override // com.baidu.location.e.e
        public void a() {
            Map<String, Object> map;
            StringBuilder sb;
            String str;
            this.g = com.baidu.location.e.j.d();
            if (this.b != 1) {
                this.g = com.baidu.location.e.j.e();
            }
            this.h = 2;
            if (this.e != null) {
                for (int i = 0; i < this.e.size(); i++) {
                    if (this.b == 1) {
                        map = this.j;
                        sb = new StringBuilder();
                        str = "cldc[";
                    } else {
                        map = this.j;
                        sb = new StringBuilder();
                        str = "cltr[";
                    }
                    sb.append(str);
                    sb.append(i);
                    sb.append("]");
                    map.put(sb.toString(), this.e.get(i));
                }
                this.j.put("trtm", String.format(Locale.CHINA, "%d", Long.valueOf(System.currentTimeMillis())));
                if (this.b == 1) {
                    return;
                }
                this.j.put("qt", "cltrg");
            }
        }

        @Override // com.baidu.location.e.e
        public void a(boolean z) {
            if (z && this.i != null) {
                ArrayList<String> arrayList = this.e;
                if (arrayList != null) {
                    arrayList.clear();
                }
                try {
                    JSONObject jSONObject = new JSONObject(this.i);
                    if (jSONObject.has("ison") && jSONObject.getInt("ison") == 0) {
                        this.p = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Map<String, Object> map = this.j;
            if (map != null) {
                map.clear();
            }
            this.a = false;
        }

        public synchronized void b() {
            ExecutorService c;
            String str;
            String str2;
            int i;
            if (this.a) {
                return;
            }
            int i2 = com.baidu.location.e.e.o;
            if (i2 > 4 && (i = this.c) < i2) {
                this.c = i + 1;
                return;
            }
            this.c = 0;
            this.a = true;
            this.b = 0;
            try {
                ArrayList<String> arrayList = this.e;
                if (arrayList == null || arrayList.size() < 1) {
                    if (this.e == null) {
                        this.e = new ArrayList<>();
                    }
                    this.b = 0;
                    int i3 = 0;
                    while (true) {
                        String str3 = null;
                        String b = this.b < 2 ? w.b() : null;
                        if (b != null || this.b == 1 || !this.p) {
                            this.b = 1;
                            str3 = b;
                        } else {
                            this.b = 2;
                            try {
                                str3 = j.a();
                            } catch (Exception unused) {
                            }
                        }
                        if (str3 == null) {
                            break;
                        } else if (!str3.contains("err!")) {
                            this.e.add(str3);
                            i3 += str3.length();
                            if (i3 >= com.baidu.location.e.a.i) {
                                break;
                            }
                        }
                    }
                }
                ArrayList<String> arrayList2 = this.e;
                if (arrayList2 == null || arrayList2.size() < 1) {
                    ArrayList<String> arrayList3 = this.e;
                    if (arrayList3 != null) {
                        arrayList3.clear();
                    }
                    this.a = false;
                    return;
                }
                if (this.b != 1) {
                    c = u.a().c();
                    if (c != null) {
                        str2 = com.baidu.location.e.j.e();
                        a(c, str2);
                    } else {
                        str = com.baidu.location.e.j.e();
                        b(str);
                    }
                } else {
                    c = u.a().c();
                    if (c != null) {
                        str2 = com.baidu.location.e.j.f;
                        a(c, str2);
                    } else {
                        str = com.baidu.location.e.j.f;
                        b(str);
                    }
                }
            } catch (Exception unused2) {
                ArrayList<String> arrayList4 = this.e;
                if (arrayList4 == null) {
                    return;
                }
                arrayList4.clear();
            }
        }
    }

    private w() {
        String j2;
        this.z = null;
        this.B = 0;
        this.z = new a();
        this.B = 0;
        if (Build.VERSION.SDK_INT <= 28 || (j2 = com.baidu.location.e.j.j()) == null) {
            return;
        }
        e = j2 + "/yo1.dat";
        f = j2 + "/yoh1.dat";
        g = j2 + "/yom1.dat";
        h = j2 + "/yol1.dat";
        i = j2 + "/yor1.dat";
    }

    private static synchronized int a(List<String> list, int i2) {
        synchronized (w.class) {
            if (list != null && i2 <= 256) {
                if (i2 >= 0) {
                    try {
                        if (j == null) {
                            File file = new File(e);
                            j = file;
                            if (!file.exists()) {
                                j = null;
                                return -2;
                            }
                        }
                        RandomAccessFile randomAccessFile = new RandomAccessFile(j, "rw");
                        if (randomAccessFile.length() < 1) {
                            randomAccessFile.close();
                            return -3;
                        }
                        long j2 = i2;
                        randomAccessFile.seek(j2);
                        int readInt = randomAccessFile.readInt();
                        int readInt2 = randomAccessFile.readInt();
                        int readInt3 = randomAccessFile.readInt();
                        int readInt4 = randomAccessFile.readInt();
                        long readLong = randomAccessFile.readLong();
                        long j3 = readLong;
                        if (a(readInt, readInt2, readInt3, readInt4, readLong)) {
                            int i3 = 1;
                            if (readInt2 >= 1) {
                                byte[] bArr = new byte[n];
                                int i4 = k;
                                while (i4 > 0 && readInt2 > 0) {
                                    byte[] bArr2 = bArr;
                                    long j4 = j3;
                                    randomAccessFile.seek(((((readInt + readInt2) - i3) % readInt3) * readInt4) + j4);
                                    int readInt5 = randomAccessFile.readInt();
                                    if (readInt5 > 0 && readInt5 < readInt4) {
                                        randomAccessFile.read(bArr2, 0, readInt5);
                                        int i5 = readInt5 - 1;
                                        if (bArr2[i5] == 0) {
                                            list.add(new String(bArr2, 0, i5));
                                        }
                                    }
                                    i4--;
                                    readInt2--;
                                    j3 = j4;
                                    bArr = bArr2;
                                    i3 = 1;
                                }
                                randomAccessFile.seek(j2);
                                randomAccessFile.writeInt(readInt);
                                randomAccessFile.writeInt(readInt2);
                                randomAccessFile.writeInt(readInt3);
                                randomAccessFile.writeInt(readInt4);
                                randomAccessFile.writeLong(j3);
                                randomAccessFile.close();
                                return k - i4;
                            }
                        }
                        randomAccessFile.close();
                        return -4;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return -5;
                    }
                }
            }
            return -1;
        }
    }

    public static synchronized w a() {
        w wVar;
        synchronized (w.class) {
            if (A == null) {
                A = new w();
            }
            wVar = A;
        }
        return wVar;
    }

    private static String a(int i2) {
        String str;
        ArrayList<String> arrayList;
        String str2 = null;
        if (i2 == 1) {
            str = f;
            arrayList = b;
        } else if (i2 == 2) {
            str = g;
            arrayList = c;
        } else {
            if (i2 == 3) {
                str = h;
            } else if (i2 != 4) {
                return null;
            } else {
                str = i;
            }
            arrayList = d;
        }
        if (arrayList == null) {
            return null;
        }
        if (arrayList.size() < 1) {
            a(str, arrayList);
        }
        synchronized (w.class) {
            int size = arrayList.size();
            if (size > 0) {
                int i3 = size - 1;
                try {
                    String str3 = arrayList.get(i3);
                    try {
                        arrayList.remove(i3);
                    } catch (Exception unused) {
                    }
                    str2 = str3;
                } catch (Exception unused2) {
                }
            }
        }
        return str2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0011, code lost:
        if (r15 != false) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x001c, code lost:
        if (r15 != false) goto L45;
     */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0034  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00e1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00ce A[EDGE_INSN: B:51:0x00ce->B:42:0x00ce ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:56:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void a(int r14, boolean r15) {
        /*
            Method dump skipped, instructions count: 232
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.b.w.a(int, boolean):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:100:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:102:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0149  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0150  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void a(com.baidu.location.c.a r8, com.baidu.location.c.h r9, android.location.Location r10, java.lang.String r11) {
        /*
            Method dump skipped, instructions count: 373
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.b.w.a(com.baidu.location.c.a, com.baidu.location.c.h, android.location.Location, java.lang.String):void");
    }

    private static void a(String str) {
        e(str);
    }

    private static boolean a(int i2, int i3, int i4, int i5, long j2) {
        return i2 >= 0 && i2 < i4 && i3 >= 0 && i3 <= i4 && i4 >= 0 && i4 <= 1024 && i5 >= 128 && i5 <= 1024;
    }

    private static boolean a(Location location) {
        if (location == null) {
            return false;
        }
        Location location2 = w;
        if (location2 == null || v == null) {
            w = location;
            return true;
        }
        double distanceTo = location.distanceTo(location2);
        return ((double) location.distanceTo(v)) > (((((double) com.baidu.location.e.j.T) * distanceTo) * distanceTo) + (((double) com.baidu.location.e.j.U) * distanceTo)) + ((double) com.baidu.location.e.j.V);
    }

    private static boolean a(Location location, com.baidu.location.c.h hVar) {
        List<ScanResult> list;
        boolean z = false;
        if (location != null && hVar != null && (list = hVar.a) != null && !list.isEmpty()) {
            if (hVar.b(y)) {
                return false;
            }
            z = true;
            if (x == null) {
                x = location;
            }
        }
        return z;
    }

    public static boolean a(Location location, boolean z) {
        return com.baidu.location.c.f.a(v, location, z);
    }

    private static boolean a(String str, List<String> list) {
        File file = new File(str);
        if (!file.exists()) {
            return false;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(8L);
            int readInt = randomAccessFile.readInt();
            int readInt2 = randomAccessFile.readInt();
            int readInt3 = randomAccessFile.readInt();
            byte[] bArr = new byte[n];
            int i2 = l + 1;
            boolean z = false;
            while (i2 > 0 && readInt2 > 0) {
                if (readInt2 < readInt3) {
                    readInt3 = 0;
                }
                try {
                    randomAccessFile.seek(((readInt2 - 1) * readInt) + 128);
                    int readInt4 = randomAccessFile.readInt();
                    if (readInt4 > 0 && readInt4 < readInt) {
                        randomAccessFile.read(bArr, 0, readInt4);
                        int i3 = readInt4 - 1;
                        if (bArr[i3] == 0) {
                            list.add(0, new String(bArr, 0, i3));
                            z = true;
                        }
                    }
                    i2--;
                    readInt2--;
                } catch (Exception unused) {
                    return z;
                }
            }
            randomAccessFile.seek(12L);
            randomAccessFile.writeInt(readInt2);
            randomAccessFile.writeInt(readInt3);
            randomAccessFile.close();
            return z;
        } catch (Exception unused2) {
            return false;
        }
    }

    public static String b() {
        return f();
    }

    private static void b(String str) {
        e(str);
    }

    private static void c(String str) {
        e(str);
    }

    public static void d() {
        l = 0;
        a(1, false);
        a(2, false);
        a(3, false);
        l = 8;
    }

    private static void d(String str) {
        try {
            File file = new File(str);
            if (file.exists()) {
                return;
            }
            File file2 = new File(com.baidu.location.e.i.a);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            if (!file.createNewFile()) {
                file = null;
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(0L);
            randomAccessFile.writeInt(32);
            randomAccessFile.writeInt(2048);
            randomAccessFile.writeInt(2060);
            randomAccessFile.writeInt(0);
            randomAccessFile.writeInt(0);
            randomAccessFile.writeInt(0);
            randomAccessFile.close();
        } catch (Exception unused) {
        }
    }

    public static String e() {
        File file = new File(g);
        String str = null;
        if (file.exists()) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(20L);
                int readInt = randomAccessFile.readInt();
                if (readInt > 128) {
                    String str2 = "&p1=" + readInt;
                    try {
                        randomAccessFile.seek(20L);
                        randomAccessFile.writeInt(0);
                        randomAccessFile.close();
                        return str2;
                    } catch (Exception unused) {
                        str = str2;
                    }
                } else {
                    randomAccessFile.close();
                }
            } catch (Exception unused2) {
            }
        }
        File file2 = new File(h);
        if (file2.exists()) {
            try {
                RandomAccessFile randomAccessFile2 = new RandomAccessFile(file2, "rw");
                randomAccessFile2.seek(20L);
                int readInt2 = randomAccessFile2.readInt();
                if (readInt2 > 256) {
                    String str3 = "&p2=" + readInt2;
                    try {
                        randomAccessFile2.seek(20L);
                        randomAccessFile2.writeInt(0);
                        randomAccessFile2.close();
                        return str3;
                    } catch (Exception unused3) {
                        str = str3;
                    }
                } else {
                    randomAccessFile2.close();
                }
            } catch (Exception unused4) {
            }
        }
        File file3 = new File(i);
        if (file3.exists()) {
            try {
                RandomAccessFile randomAccessFile3 = new RandomAccessFile(file3, "rw");
                randomAccessFile3.seek(20L);
                int readInt3 = randomAccessFile3.readInt();
                if (readInt3 > 512) {
                    String str4 = "&p3=" + readInt3;
                    try {
                        randomAccessFile3.seek(20L);
                        randomAccessFile3.writeInt(0);
                        randomAccessFile3.close();
                        return str4;
                    } catch (Exception unused5) {
                        str = str4;
                    }
                } else {
                    randomAccessFile3.close();
                }
            } catch (Exception unused6) {
            }
        }
        return str;
    }

    private static synchronized void e(String str) {
        ArrayList<String> arrayList;
        synchronized (w.class) {
            if (str.contains("err!")) {
                return;
            }
            int i2 = com.baidu.location.e.j.q;
            if (i2 == 1) {
                arrayList = b;
            } else if (i2 == 2) {
                arrayList = c;
            } else if (i2 != 3) {
                return;
            } else {
                arrayList = d;
            }
            if (arrayList == null) {
                return;
            }
            if (arrayList.size() <= m) {
                arrayList.add(str);
            }
            if (arrayList.size() >= m) {
                a(i2, false);
            }
            while (arrayList.size() > m) {
                arrayList.remove(0);
            }
        }
    }

    private static String f() {
        String str = null;
        for (int i2 = 1; i2 < 5; i2++) {
            str = a(i2);
            if (str != null) {
                return str;
            }
        }
        a(d, t);
        if (d.size() > 0) {
            str = d.get(0);
            d.remove(0);
        }
        if (str != null) {
            return str;
        }
        a(d, s);
        if (d.size() > 0) {
            str = d.get(0);
            d.remove(0);
        }
        if (str != null) {
            return str;
        }
        a(d, u);
        if (d.size() <= 0) {
            return str;
        }
        String str2 = d.get(0);
        d.remove(0);
        return str2;
    }

    public void c() {
        if (com.baidu.location.c.i.a().i() && !com.baidu.location.e.j.b()) {
            this.z.b();
        }
    }
}
