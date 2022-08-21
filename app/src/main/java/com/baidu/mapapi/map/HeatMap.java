package com.baidu.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.util.SparseIntArray;
import androidx.collection.LongSparseArray;
import com.baidu.mapapi.model.LatLng;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.nexstreaming.nexeditorsdk.nexEngine;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/* loaded from: classes.dex */
public class HeatMap {
    public static final Gradient DEFAULT_GRADIENT;
    public static final double DEFAULT_OPACITY = 0.6d;
    public static final int DEFAULT_RADIUS = 12;
    private static final String b = "HeatMap";
    private static final SparseIntArray c;
    private static final int[] d;
    private static final float[] e;
    private static int r;
    public BaiduMap a;
    private v<WeightedLatLng> f;
    private Collection<WeightedLatLng> g;
    private int h;
    private Gradient i;
    private double j;
    private m k;
    private int[] l;
    private double[] m;
    private double[] n;
    private HashMap<String, Tile> o;
    private ExecutorService p;
    private HashSet<String> q;

    /* loaded from: classes.dex */
    public static class Builder {
        private Collection<WeightedLatLng> a;
        private int b = 12;
        private Gradient c = HeatMap.DEFAULT_GRADIENT;
        private double d = 0.6d;

        public HeatMap build() {
            if (this.a != null) {
                return new HeatMap(this, null);
            }
            throw new IllegalStateException("BDMapSDKException: No input data: you must use either .data or .weightedData before building");
        }

        public Builder data(Collection<LatLng> collection) {
            if (collection == null || collection.isEmpty()) {
                throw new IllegalArgumentException("BDMapSDKException: No input points.");
            }
            if (collection.contains(null)) {
                throw new IllegalArgumentException("BDMapSDKException: input points can not contain null.");
            }
            return weightedData(HeatMap.c(collection));
        }

        public Builder gradient(Gradient gradient) {
            if (gradient != null) {
                this.c = gradient;
                return this;
            }
            throw new IllegalArgumentException("BDMapSDKException: gradient can not be null");
        }

        public Builder opacity(double d) {
            this.d = d;
            if (d < SearchStatUtils.POW || d > 1.0d) {
                throw new IllegalArgumentException("BDMapSDKException: Opacity must be in range [0, 1]");
            }
            return this;
        }

        public Builder radius(int i) {
            this.b = i;
            if (i < 10 || i > 50) {
                throw new IllegalArgumentException("BDMapSDKException: Radius not within bounds.");
            }
            return this;
        }

        public Builder weightedData(Collection<WeightedLatLng> collection) {
            if (collection == null || collection.isEmpty()) {
                throw new IllegalArgumentException("BDMapSDKException: No input points.");
            }
            if (collection.contains(null)) {
                throw new IllegalArgumentException("BDMapSDKException: input points can not contain null.");
            }
            ArrayList arrayList = new ArrayList();
            for (WeightedLatLng weightedLatLng : collection) {
                LatLng latLng = weightedLatLng.latLng;
                double d = latLng.latitude;
                if (d >= 0.37532d && d <= 54.562495d) {
                    double d2 = latLng.longitude;
                    if (d2 >= 72.508319d && d2 <= 135.942198d) {
                    }
                }
                arrayList.add(weightedLatLng);
            }
            collection.removeAll(arrayList);
            this.a = collection;
            return this;
        }
    }

    static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        c = sparseIntArray;
        sparseIntArray.put(3, nexEngine.ExportHEVCHighTierLevel61);
        sparseIntArray.put(4, nexEngine.ExportHEVCMainTierLevel61);
        sparseIntArray.put(5, nexEngine.ExportHEVCHighTierLevel6);
        sparseIntArray.put(6, nexEngine.ExportHEVCMainTierLevel6);
        sparseIntArray.put(7, nexEngine.ExportHEVCHighTierLevel52);
        sparseIntArray.put(8, nexEngine.ExportHEVCMainTierLevel52);
        sparseIntArray.put(9, 131072);
        sparseIntArray.put(10, 65536);
        sparseIntArray.put(11, 32768);
        sparseIntArray.put(12, 16384);
        sparseIntArray.put(13, 8192);
        sparseIntArray.put(14, 4096);
        sparseIntArray.put(15, 2048);
        sparseIntArray.put(16, 1024);
        sparseIntArray.put(17, 512);
        sparseIntArray.put(18, 256);
        sparseIntArray.put(19, 128);
        sparseIntArray.put(20, 64);
        int[] iArr = {Color.rgb(0, 0, 200), Color.rgb(0, 225, 0), Color.rgb(255, 0, 0)};
        d = iArr;
        float[] fArr = {0.08f, 0.4f, 1.0f};
        e = fArr;
        DEFAULT_GRADIENT = new Gradient(iArr, fArr);
        r = 0;
    }

    private HeatMap(Builder builder) {
        this.o = new HashMap<>();
        this.p = Executors.newFixedThreadPool(1);
        this.q = new HashSet<>();
        this.g = builder.a;
        this.h = builder.b;
        this.i = builder.c;
        this.j = builder.d;
        int i = this.h;
        this.m = a(i, i / 3.0d);
        a(this.i);
        b(this.g);
    }

    public /* synthetic */ HeatMap(Builder builder, o oVar) {
        this(builder);
    }

    private static double a(Collection<WeightedLatLng> collection, m mVar, int i, int i2) {
        double d2 = mVar.a;
        double d3 = mVar.c;
        double d4 = mVar.b;
        double d5 = d3 - d2;
        double d6 = mVar.d - d4;
        if (d5 <= d6) {
            d5 = d6;
        }
        double d7 = ((int) ((i2 / (i * 2)) + 0.5d)) / d5;
        LongSparseArray longSparseArray = new LongSparseArray();
        double d8 = SearchStatUtils.POW;
        for (WeightedLatLng weightedLatLng : collection) {
            int i3 = (int) ((weightedLatLng.a().y - d4) * d7);
            long j = (int) ((weightedLatLng.a().x - d2) * d7);
            LongSparseArray longSparseArray2 = (LongSparseArray) longSparseArray.get(j);
            if (longSparseArray2 == null) {
                longSparseArray2 = new LongSparseArray();
                longSparseArray.put(j, longSparseArray2);
            }
            long j2 = i3;
            Double d9 = (Double) longSparseArray2.get(j2);
            if (d9 == null) {
                d9 = Double.valueOf((double) SearchStatUtils.POW);
            }
            LongSparseArray longSparseArray3 = longSparseArray;
            double d10 = d2;
            Double valueOf = Double.valueOf(d9.doubleValue() + weightedLatLng.intensity);
            longSparseArray2.put(j2, valueOf);
            if (valueOf.doubleValue() > d8) {
                d8 = valueOf.doubleValue();
            }
            longSparseArray = longSparseArray3;
            d2 = d10;
        }
        return d8;
    }

    private static Bitmap a(double[][] dArr, int[] iArr, double d2) {
        int i = iArr[iArr.length - 1];
        double length = (iArr.length - 1) / d2;
        int length2 = dArr.length;
        int[] iArr2 = new int[length2 * length2];
        for (int i2 = 0; i2 < length2; i2++) {
            for (int i3 = 0; i3 < length2; i3++) {
                double d3 = dArr[i3][i2];
                int i4 = (i2 * length2) + i3;
                int i5 = (int) (d3 * length);
                if (d3 == SearchStatUtils.POW) {
                    iArr2[i4] = 0;
                } else if (i5 < iArr.length) {
                    iArr2[i4] = iArr[i5];
                } else {
                    iArr2[i4] = i;
                }
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(length2, length2, Bitmap.Config.ARGB_8888);
        createBitmap.setPixels(iArr2, 0, length2, 0, 0, length2, length2);
        return createBitmap;
    }

    private static Tile a(Bitmap bitmap) {
        ByteBuffer allocate = ByteBuffer.allocate(bitmap.getWidth() * bitmap.getHeight() * 4);
        bitmap.copyPixelsToBuffer(allocate);
        return new Tile(256, 256, allocate.array());
    }

    private void a(Gradient gradient) {
        this.i = gradient;
        this.l = gradient.a(this.j);
    }

    private synchronized void a(String str, Tile tile) {
        this.o.put(str, tile);
    }

    private synchronized boolean a(String str) {
        return this.q.contains(str);
    }

    private double[] a(int i) {
        int i2;
        double[] dArr = new double[20];
        int i3 = 5;
        while (true) {
            if (i3 >= 11) {
                break;
            }
            dArr[i3] = a(this.g, this.k, i, (int) (Math.pow(2.0d, i3 - 3) * 1280.0d));
            if (i3 == 5) {
                for (int i4 = 0; i4 < i3; i4++) {
                    dArr[i4] = dArr[i3];
                }
            }
            i3++;
        }
        for (i2 = 11; i2 < 20; i2++) {
            dArr[i2] = dArr[10];
        }
        return dArr;
    }

    private static double[] a(int i, double d2) {
        double[] dArr = new double[(i * 2) + 1];
        for (int i2 = -i; i2 <= i; i2++) {
            dArr[i2 + i] = Math.exp(((-i2) * i2) / ((2.0d * d2) * d2));
        }
        return dArr;
    }

    private static double[][] a(double[][] dArr, double[] dArr2) {
        int floor = (int) Math.floor(dArr2.length / 2.0d);
        int length = dArr.length;
        int i = length - (floor * 2);
        int i2 = 1;
        int i3 = (floor + i) - 1;
        double[][] dArr3 = (double[][]) Array.newInstance(double.class, length, length);
        int i4 = 0;
        while (true) {
            double d2 = SearchStatUtils.POW;
            if (i4 >= length) {
                break;
            }
            int i5 = 0;
            while (i5 < length) {
                double d3 = dArr[i4][i5];
                if (d3 != d2) {
                    int i6 = i4 + floor;
                    if (i3 < i6) {
                        i6 = i3;
                    }
                    int i7 = i6 + 1;
                    int i8 = i4 - floor;
                    for (int i9 = floor > i8 ? floor : i8; i9 < i7; i9++) {
                        double[] dArr4 = dArr3[i9];
                        dArr4[i5] = dArr4[i5] + (dArr2[i9 - i8] * d3);
                    }
                }
                i5++;
                d2 = SearchStatUtils.POW;
            }
            i4++;
        }
        double[][] dArr5 = (double[][]) Array.newInstance(double.class, i, i);
        int i10 = floor;
        while (i10 < i3 + 1) {
            int i11 = 0;
            while (i11 < length) {
                double d4 = dArr3[i10][i11];
                if (d4 != SearchStatUtils.POW) {
                    int i12 = i11 + floor;
                    if (i3 < i12) {
                        i12 = i3;
                    }
                    int i13 = i12 + i2;
                    int i14 = i11 - floor;
                    for (int i15 = floor > i14 ? floor : i14; i15 < i13; i15++) {
                        double[] dArr6 = dArr5[i10 - floor];
                        int i16 = i15 - floor;
                        dArr6[i16] = dArr6[i16] + (dArr2[i15 - i14] * d4);
                    }
                }
                i11++;
                i2 = 1;
            }
            i10++;
            i2 = 1;
        }
        return dArr5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(int i, int i2, int i3) {
        int i4;
        double d2 = c.get(i3);
        double d3 = (this.h * d2) / 256.0d;
        double d4 = ((2.0d * d3) + d2) / ((i4 * 2) + 256);
        if (i < 0 || i2 < 0) {
            return;
        }
        double d5 = (i * d2) - d3;
        double d6 = ((i + 1) * d2) + d3;
        double d7 = (i2 * d2) - d3;
        double d8 = ((i2 + 1) * d2) + d3;
        m mVar = new m(d5, d6, d7, d8);
        m mVar2 = this.k;
        if (!mVar.a(new m(mVar2.a - d3, mVar2.c + d3, mVar2.b - d3, mVar2.d + d3))) {
            return;
        }
        Collection<WeightedLatLng> a = this.f.a(mVar);
        if (a.isEmpty()) {
            return;
        }
        int i5 = this.h;
        double[][] dArr = (double[][]) Array.newInstance(double.class, (i5 * 2) + 256, (i5 * 2) + 256);
        for (WeightedLatLng weightedLatLng : a) {
            Point a2 = weightedLatLng.a();
            int i6 = (int) ((a2.x - d5) / d4);
            int i7 = (int) ((d8 - a2.y) / d4);
            int i8 = this.h;
            if (i6 >= (i8 * 2) + 256) {
                i6 = ((i8 * 2) + 256) - 1;
            }
            if (i7 >= (i8 * 2) + 256) {
                i7 = ((i8 * 2) + 256) - 1;
            }
            double[] dArr2 = dArr[i6];
            dArr2[i7] = dArr2[i7] + weightedLatLng.intensity;
            d8 = d8;
        }
        Bitmap a3 = a(a(dArr, this.m), this.l, this.n[i3 - 1]);
        Tile a4 = a(a3);
        a3.recycle();
        a(i + "_" + i2 + "_" + i3, a4);
        if (this.o.size() > r) {
            a();
        }
        BaiduMap baiduMap = this.a;
        if (baiduMap == null) {
            return;
        }
        baiduMap.a();
    }

    private synchronized void b(String str) {
        this.q.add(str);
    }

    private void b(Collection<WeightedLatLng> collection) {
        this.g = collection;
        if (!collection.isEmpty()) {
            m d2 = d(this.g);
            this.k = d2;
            this.f = new v<>(d2);
            for (WeightedLatLng weightedLatLng : this.g) {
                this.f.a((v<WeightedLatLng>) weightedLatLng);
            }
            this.n = a(this.h);
            return;
        }
        throw new IllegalArgumentException("BDMapSDKException: No input points.");
    }

    private synchronized Tile c(String str) {
        if (this.o.containsKey(str)) {
            Tile tile = this.o.get(str);
            this.o.remove(str);
            return tile;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Collection<WeightedLatLng> c(Collection<LatLng> collection) {
        ArrayList arrayList = new ArrayList();
        for (LatLng latLng : collection) {
            arrayList.add(new WeightedLatLng(latLng));
        }
        return arrayList;
    }

    private static m d(Collection<WeightedLatLng> collection) {
        Iterator<WeightedLatLng> it = collection.iterator();
        WeightedLatLng next = it.next();
        double d2 = next.a().x;
        double d3 = next.a().x;
        double d4 = next.a().y;
        double d5 = next.a().y;
        while (it.hasNext()) {
            WeightedLatLng next2 = it.next();
            double d6 = next2.a().x;
            double d7 = next2.a().y;
            if (d6 < d2) {
                d2 = d6;
            }
            if (d6 > d3) {
                d3 = d6;
            }
            if (d7 < d4) {
                d4 = d7;
            }
            if (d7 > d5) {
                d5 = d7;
            }
        }
        return new m(d2, d3, d4, d5);
    }

    private synchronized void d() {
        this.o.clear();
    }

    public Tile a(int i, int i2, int i3) {
        String str = i + "_" + i2 + "_" + i3;
        Tile c2 = c(str);
        if (c2 != null) {
            return c2;
        }
        if (a(str)) {
            return null;
        }
        BaiduMap baiduMap = this.a;
        if (baiduMap != null && r == 0) {
            WinRound winRound = baiduMap.getMapStatus().a.j;
            r = (((winRound.right - winRound.left) / 256) + 2) * (((winRound.bottom - winRound.top) / 256) + 2) * 4;
        }
        if (this.o.size() > r) {
            a();
        }
        if (this.p.isShutdown()) {
            return null;
        }
        try {
            this.p.execute(new o(this, i, i2, i3));
            b(str);
            return null;
        } catch (RejectedExecutionException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public synchronized void a() {
        this.q.clear();
        this.o.clear();
    }

    public void b() {
        d();
    }

    public void c() {
        this.p.shutdownNow();
    }

    public void removeHeatMap() {
        BaiduMap baiduMap = this.a;
        if (baiduMap != null) {
            baiduMap.a(this);
        }
    }
}
