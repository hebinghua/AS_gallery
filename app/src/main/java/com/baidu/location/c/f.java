package com.baidu.location.c;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.OnNmeaMessageListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import com.baidu.location.BDLocation;
import com.baidu.location.Jni;
import com.baidu.location.b.s;
import com.baidu.location.b.w;
import com.baidu.location.b.x;
import com.baidu.platform.comapi.UIMsg;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.xiaomi.miai.api.StatusCode;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import org.slf4j.Marker;

/* loaded from: classes.dex */
public class f {
    private static String I = null;
    private static double K = 100.0d;
    private static float N = -1.0f;
    public static int a = 0;
    public static String b = "";
    private static f d;
    private static int q;
    private static int r;
    private static int s;
    private static int t;
    private static int u;
    private static long v;
    private BDLocation ac;
    private String ag;
    private Context e;
    private Location g;
    private GpsStatus j;
    private c k;
    private boolean l;
    private boolean n;
    private LocationManager f = null;
    private C0009f h = null;
    private h i = null;
    private d m = null;
    private GpsStatus.NmeaListener o = null;
    private OnNmeaMessageListener p = null;
    private long w = 0;
    private boolean x = false;
    private boolean y = false;
    private String z = null;
    private boolean A = false;
    private long B = 0;
    private double C = -1.0d;
    private double D = SearchStatUtils.POW;
    private double E = SearchStatUtils.POW;
    private long F = 0;
    private long G = 0;
    private long H = 0;
    private e J = null;
    private long L = 0;
    private long M = 0;
    private a O = null;
    private b P = null;
    private ArrayList<ArrayList<Float>> Q = new ArrayList<>();
    private ArrayList<ArrayList<Float>> R = new ArrayList<>();
    private ArrayList<ArrayList<Float>> S = new ArrayList<>();
    private ArrayList<ArrayList<Float>> T = new ArrayList<>();
    private ArrayList<ArrayList<Float>> U = new ArrayList<>();
    private String V = null;
    private long W = 0;
    private ArrayList<Integer> X = new ArrayList<>();
    private String Y = null;
    private long Z = 0;
    private long aa = -1;
    private long ab = -1;
    private boolean ad = false;
    private boolean ae = false;
    private long af = 0;
    public long c = 0;

    @TargetApi(24)
    /* loaded from: classes.dex */
    public class a extends GnssMeasurementsEvent.Callback {
        public int a;
        public String b;

        @Override // android.location.GnssMeasurementsEvent.Callback
        public void onGnssMeasurementsReceived(GnssMeasurementsEvent gnssMeasurementsEvent) {
            if (this.a != 1 || gnssMeasurementsEvent == null) {
                return;
            }
            this.b = gnssMeasurementsEvent.toString();
        }

        @Override // android.location.GnssMeasurementsEvent.Callback
        public void onStatusChanged(int i) {
            this.a = i;
        }
    }

    @TargetApi(24)
    /* loaded from: classes.dex */
    public class b extends GnssNavigationMessage.Callback {
        public int a;

        private b() {
            this.a = 0;
        }

        public /* synthetic */ b(f fVar, com.baidu.location.c.g gVar) {
            this();
        }

        @Override // android.location.GnssNavigationMessage.Callback
        public void onGnssNavigationMessageReceived(GnssNavigationMessage gnssNavigationMessage) {
            x a;
            long currentTimeMillis;
            if (f.this.H != 0) {
                a = x.a();
                currentTimeMillis = f.this.H;
            } else {
                a = x.a();
                currentTimeMillis = System.currentTimeMillis() / 1000;
            }
            a.a(gnssNavigationMessage, currentTimeMillis);
        }

        @Override // android.location.GnssNavigationMessage.Callback
        public void onStatusChanged(int i) {
            this.a = i;
        }
    }

    @TargetApi(24)
    /* loaded from: classes.dex */
    public class c extends GnssStatus.Callback {
        private c() {
        }

        public /* synthetic */ c(f fVar, com.baidu.location.c.g gVar) {
            this();
        }

        @Override // android.location.GnssStatus.Callback
        public void onFirstFix(int i) {
        }

        @Override // android.location.GnssStatus.Callback
        public void onSatelliteStatusChanged(GnssStatus gnssStatus) {
            ArrayList arrayList;
            if (f.this.f == null) {
                return;
            }
            f.this.M = System.currentTimeMillis();
            int satelliteCount = gnssStatus.getSatelliteCount();
            f.this.R.clear();
            f.this.S.clear();
            f.this.T.clear();
            f.this.U.clear();
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            for (int i4 = 0; i4 < satelliteCount; i4++) {
                i3++;
                ArrayList arrayList2 = new ArrayList();
                int constellationType = gnssStatus.getConstellationType(i4);
                arrayList2.add(Float.valueOf(gnssStatus.getAzimuthDegrees(i4)));
                arrayList2.add(Float.valueOf(gnssStatus.getElevationDegrees(i4)));
                arrayList2.add(Float.valueOf(gnssStatus.getCn0DbHz(i4)));
                if (gnssStatus.usedInFix(i4)) {
                    i++;
                    arrayList2.add(Float.valueOf(1.0f));
                    if (constellationType == 1) {
                        i2++;
                    }
                } else {
                    arrayList2.add(Float.valueOf(0.0f));
                }
                arrayList2.add(Float.valueOf(gnssStatus.getSvid(i4)));
                if (constellationType == 1) {
                    arrayList2.add(Float.valueOf(1.0f));
                    arrayList = f.this.R;
                } else if (constellationType == 5) {
                    arrayList2.add(Float.valueOf(2.0f));
                    arrayList = f.this.S;
                } else if (constellationType == 3) {
                    arrayList2.add(Float.valueOf(3.0f));
                    arrayList = f.this.T;
                } else if (constellationType == 6) {
                    arrayList2.add(Float.valueOf(4.0f));
                    arrayList = f.this.U;
                }
                arrayList.add(arrayList2);
            }
            ArrayList arrayList3 = new ArrayList();
            arrayList3.addAll(f.this.R);
            arrayList3.addAll(f.this.S);
            arrayList3.addAll(f.this.T);
            arrayList3.addAll(f.this.U);
            f.this.b(arrayList3);
            f fVar = f.this;
            fVar.Q = fVar.a(true, false, false, false, true, -1.0f);
            f fVar2 = f.this;
            f.b = fVar2.a(fVar2.Q);
            f.a = i;
            int unused = f.q = i2;
            int unused2 = f.u = i3;
            long unused3 = f.v = System.currentTimeMillis();
            f fVar3 = f.this;
            int unused4 = f.r = fVar3.a((ArrayList<ArrayList<Float>>) fVar3.T, true, -1.0f).size();
            f fVar4 = f.this;
            int unused5 = f.s = fVar4.a((ArrayList<ArrayList<Float>>) fVar4.U, true, -1.0f).size();
            f fVar5 = f.this;
            int unused6 = f.t = fVar5.a((ArrayList<ArrayList<Float>>) fVar5.S, true, -1.0f).size();
        }

        @Override // android.location.GnssStatus.Callback
        public void onStarted() {
        }

        @Override // android.location.GnssStatus.Callback
        public void onStopped() {
            f.this.d((Location) null);
            f.this.b(false);
            f.a = 0;
            int unused = f.q = 0;
            int unused2 = f.r = 0;
            int unused3 = f.s = 0;
            int unused4 = f.t = 0;
        }
    }

    /* loaded from: classes.dex */
    public class d implements GpsStatus.Listener {
        private long b;

        private d() {
            this.b = 0L;
        }

        public /* synthetic */ d(f fVar, com.baidu.location.c.g gVar) {
            this();
        }

        @Override // android.location.GpsStatus.Listener
        public void onGpsStatusChanged(int i) {
            long currentTimeMillis;
            ArrayList arrayList;
            if (f.this.f == null) {
                return;
            }
            int i2 = 0;
            if (i == 2) {
                f.this.d((Location) null);
                f.this.b(false);
                f.a = 0;
                int unused = f.q = 0;
                int unused2 = f.r = 0;
                int unused3 = f.s = 0;
                int unused4 = f.t = 0;
            } else if (i == 4 && f.this.y) {
                try {
                    if (f.this.j == null) {
                        f fVar = f.this;
                        fVar.j = fVar.f.getGpsStatus(null);
                    } else {
                        f.this.f.getGpsStatus(f.this.j);
                    }
                    f.this.M = System.currentTimeMillis();
                    f.this.R.clear();
                    f.this.S.clear();
                    f.this.T.clear();
                    f.this.U.clear();
                    int i3 = 0;
                    for (GpsSatellite gpsSatellite : f.this.j.getSatellites()) {
                        ArrayList arrayList2 = new ArrayList();
                        int prn = gpsSatellite.getPrn();
                        arrayList2.add(Float.valueOf(gpsSatellite.getAzimuth()));
                        arrayList2.add(Float.valueOf(gpsSatellite.getElevation()));
                        arrayList2.add(Float.valueOf(gpsSatellite.getSnr()));
                        if (gpsSatellite.usedInFix()) {
                            i2++;
                            arrayList2.add(Float.valueOf(1.0f));
                            if (prn >= 1 && prn <= 32) {
                                i3++;
                            }
                        } else {
                            arrayList2.add(Float.valueOf(0.0f));
                        }
                        arrayList2.add(Float.valueOf(prn));
                        if (prn >= 1 && prn <= 32) {
                            arrayList2.add(Float.valueOf(1.0f));
                            arrayList = f.this.R;
                        } else if (prn >= 201 && prn <= 235) {
                            arrayList2.add(Float.valueOf(2.0f));
                            arrayList = f.this.S;
                        } else if (prn >= 65 && prn <= 96) {
                            arrayList2.add(Float.valueOf(3.0f));
                            arrayList = f.this.T;
                        } else if (prn >= 301 && prn <= 336) {
                            arrayList2.add(Float.valueOf(4.0f));
                            arrayList = f.this.U;
                        }
                        arrayList.add(arrayList2);
                    }
                    ArrayList arrayList3 = new ArrayList();
                    arrayList3.addAll(f.this.R);
                    arrayList3.addAll(f.this.S);
                    arrayList3.addAll(f.this.T);
                    arrayList3.addAll(f.this.U);
                    f.this.b(arrayList3);
                    f fVar2 = f.this;
                    fVar2.Q = fVar2.a(true, false, false, false, true, -1.0f);
                    f fVar3 = f.this;
                    f.b = fVar3.a(fVar3.Q);
                    if (i3 > 0) {
                        int unused5 = f.q = i3;
                    }
                    if (i2 <= 0) {
                        if (System.currentTimeMillis() - this.b > 100) {
                            currentTimeMillis = System.currentTimeMillis();
                        }
                        long unused6 = f.v = System.currentTimeMillis();
                    }
                    currentTimeMillis = System.currentTimeMillis();
                    this.b = currentTimeMillis;
                    f.a = i2;
                    long unused62 = f.v = System.currentTimeMillis();
                } catch (Exception unused7) {
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class e extends Handler {
        public WeakReference<f> a;
        public f b;

        public e(f fVar) {
            this.a = new WeakReference<>(fVar);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Location location;
            String str;
            if (!com.baidu.location.f.isServing) {
                return;
            }
            f fVar = this.a.get();
            this.b = fVar;
            if (fVar == null) {
                return;
            }
            int i = message.what;
            if (i == 1) {
                fVar.f((Location) message.obj);
                return;
            }
            if (i == 3) {
                location = (Location) message.obj;
                str = "&og=1";
            } else if (i != 4) {
                if (i != 5) {
                    return;
                }
                fVar.a((String) message.obj);
                return;
            } else {
                location = (Location) message.obj;
                str = "&og=2";
            }
            fVar.a(str, location);
        }
    }

    /* renamed from: com.baidu.location.c.f$f  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class C0009f implements LocationListener {
        private C0009f() {
        }

        public /* synthetic */ C0009f(f fVar, com.baidu.location.c.g gVar) {
            this();
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (location != null && Math.abs(location.getLatitude()) <= 360.0d && Math.abs(location.getLongitude()) <= 360.0d) {
                f.this.H = location.getTime() / 1000;
                f.this.aa = System.currentTimeMillis();
                if (f.this.G != 0) {
                    f.this.F = System.currentTimeMillis() - f.this.G;
                }
                f.this.G = System.currentTimeMillis();
                int i = f.a;
                if (i == 0) {
                    try {
                        i = location.getExtras().getInt("satellites");
                    } catch (Exception unused) {
                    }
                }
                if (i == 0) {
                    System.currentTimeMillis();
                    long unused2 = f.this.M;
                }
                f.this.b(true);
                f.this.d(location);
                f.this.x = false;
            }
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
            f.this.d((Location) null);
            f.this.b(false);
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
            if (i == 0) {
                f.this.d((Location) null);
            } else if (i != 1) {
                if (i != 2) {
                    return;
                }
                f.this.x = false;
                return;
            } else {
                f.this.w = System.currentTimeMillis();
                f.this.x = true;
            }
            f.this.b(false);
        }
    }

    /* loaded from: classes.dex */
    public class g implements GpsStatus.NmeaListener {
        private g() {
        }

        public /* synthetic */ g(f fVar, com.baidu.location.c.g gVar) {
            this();
        }

        @Override // android.location.GpsStatus.NmeaListener
        public void onNmeaReceived(long j, String str) {
            if (f.this.J != null) {
                f.this.J.sendMessage(f.this.J.obtainMessage(5, str));
            }
        }
    }

    /* loaded from: classes.dex */
    public class h implements LocationListener {
        private long b;

        private h() {
            this.b = 0L;
        }

        public /* synthetic */ h(f fVar, com.baidu.location.c.g gVar) {
            this();
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (!f.this.y && location != null && TextUtils.equals(location.getProvider(), "gps") && System.currentTimeMillis() - this.b >= AbstractComponentTracker.LINGERING_TIMEOUT && Math.abs(location.getLatitude()) <= 360.0d && Math.abs(location.getLongitude()) <= 360.0d && w.a(location, false)) {
                this.b = System.currentTimeMillis();
                if (f.this.J == null) {
                    return;
                }
                f.this.c = System.currentTimeMillis();
                f.this.J.sendMessage(f.this.J.obtainMessage(4, location));
            }
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
    }

    private f() {
        this.l = false;
        this.n = false;
        this.ag = null;
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Class.forName("android.location.GnssStatus");
                this.l = true;
            } catch (ClassNotFoundException unused) {
                this.l = false;
            }
        }
        if (Build.VERSION.SDK_INT >= 28) {
            try {
                this.ag = Build.MANUFACTURER;
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        this.n = false;
    }

    public static synchronized f a() {
        f fVar;
        synchronized (f.class) {
            if (d == null) {
                d = new f();
            }
            fVar = d;
        }
        return fVar;
    }

    public static String a(Location location) {
        if (location == null) {
            return null;
        }
        float speed = (float) (location.getSpeed() * 3.6d);
        float f = -1.0f;
        if (!location.hasSpeed()) {
            speed = -1.0f;
        }
        int accuracy = (int) (location.hasAccuracy() ? location.getAccuracy() : -1.0f);
        double altitude = location.hasAltitude() ? location.getAltitude() : 555.0d;
        if (location.hasBearing()) {
            f = location.getBearing();
        }
        return N < -0.01f ? String.format(Locale.CHINA, "&ll=%.5f|%.5f&s=%.1f&d=%.1f&ll_r=%d&ll_n=%d&ll_h=%.2f&ll_t=%d&ll_sn=%d|%d|%d|%d|%d&ll_snr=%.1f", Double.valueOf(location.getLongitude()), Double.valueOf(location.getLatitude()), Float.valueOf(speed), Float.valueOf(f), Integer.valueOf(accuracy), Integer.valueOf(a), Double.valueOf(altitude), Long.valueOf(location.getTime() / 1000), Integer.valueOf(a), Integer.valueOf(q), Integer.valueOf(r), Integer.valueOf(s), Integer.valueOf(t), Double.valueOf(K)) : String.format(Locale.CHINA, "&ll=%.5f|%.5f&s=%.1f&d=%.1f&ll_r=%d&ll_n=%d&ll_h=%.2f&ll_t=%d&ll_sn=%d|%d|%d|%d|%d&ll_snr=%.1f&ll_bp=%.2f", Double.valueOf(location.getLongitude()), Double.valueOf(location.getLatitude()), Float.valueOf(speed), Float.valueOf(f), Integer.valueOf(accuracy), Integer.valueOf(a), Double.valueOf(altitude), Long.valueOf(location.getTime() / 1000), Integer.valueOf(a), Integer.valueOf(q), Integer.valueOf(r), Integer.valueOf(s), Integer.valueOf(t), Double.valueOf(K), Float.valueOf(N));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(ArrayList<ArrayList<Float>> arrayList) {
        StringBuilder sb = new StringBuilder();
        if (arrayList.size() == 0) {
            return sb.toString();
        }
        Iterator<ArrayList<Float>> it = arrayList.iterator();
        boolean z = true;
        while (it.hasNext()) {
            ArrayList<Float> next = it.next();
            if (next.size() == 6) {
                if (z) {
                    z = false;
                } else {
                    sb.append("|");
                }
                sb.append(String.format("%.1f;", next.get(0)));
                sb.append(String.format("%.1f;", next.get(1)));
                sb.append(String.format("%.1f;", next.get(2)));
                sb.append(String.format("%.0f;", next.get(3)));
                sb.append(String.format("%.0f", next.get(4)));
                sb.append(String.format("%.0f", next.get(5)));
            }
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<ArrayList<Float>> a(ArrayList<ArrayList<Float>> arrayList, boolean z, float f) {
        ArrayList<ArrayList<Float>> arrayList2 = new ArrayList<>();
        if (arrayList.size() <= 40 && arrayList.size() != 0) {
            Iterator<ArrayList<Float>> it = arrayList.iterator();
            while (it.hasNext()) {
                ArrayList<Float> next = it.next();
                if (next.size() == 6) {
                    float floatValue = next.get(3).floatValue();
                    float floatValue2 = next.get(2).floatValue();
                    if (!z || floatValue >= 1.0f) {
                        if (f <= 0.0f || floatValue2 >= f) {
                            arrayList2.add(next);
                        }
                    }
                }
            }
        }
        return arrayList2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<ArrayList<Float>> a(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, float f) {
        ArrayList<ArrayList<Float>> arrayList = new ArrayList<>();
        if (z) {
            arrayList.addAll(a(this.R, z5, f));
        }
        if (z2) {
            arrayList.addAll(a(this.S, z5, f));
        }
        if (z3) {
            arrayList.addAll(a(this.T, z5, f));
        }
        if (z4) {
            arrayList.addAll(a(this.U, z5, f));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        if (TextUtils.isEmpty(str) || !b(str)) {
            return;
        }
        if (str.startsWith("$GPGGA,")) {
            a(str, 2, 4, 6);
        } else if (!str.startsWith("$GPRMC,")) {
        } else {
            a(str, 3, 5, 2);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x00e9, code lost:
        if (android.text.TextUtils.equals(r0[r14], "A") != false) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00f6, code lost:
        if (android.text.TextUtils.equals(r0[r14], "0") != false) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00f9, code lost:
        r10.ad = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void a(java.lang.String r11, int r12, int r13, int r14) {
        /*
            Method dump skipped, instructions count: 264
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.c.f.a(java.lang.String, int, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, Location location) {
        if (location == null) {
            return;
        }
        String str2 = str + com.baidu.location.b.b.a().c();
        boolean e2 = i.a().e();
        s.a(new com.baidu.location.c.a(com.baidu.location.c.b.a().f()));
        s.a(System.currentTimeMillis());
        s.a(new Location(location));
        s.a(str2);
        if (e2) {
            return;
        }
        w.a(s.c(), null, s.d(), str2);
    }

    public static boolean a(Location location, Location location2, boolean z) {
        if (location == location2) {
            return false;
        }
        if (location == null || location2 == null) {
            return true;
        }
        float speed = location2.getSpeed();
        if (z && ((com.baidu.location.e.j.v == 3 || !com.baidu.location.e.d.a().a(location2.getLongitude(), location2.getLatitude())) && speed < 5.0f)) {
            return true;
        }
        float distanceTo = location2.distanceTo(location);
        return speed > com.baidu.location.e.j.L ? distanceTo > com.baidu.location.e.j.N : speed > com.baidu.location.e.j.K ? distanceTo > com.baidu.location.e.j.M : distanceTo > 5.0f;
    }

    public static String b(Location location) {
        String a2 = a(location);
        if (a2 != null) {
            return a2 + "&g_tp=0";
        }
        return a2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(ArrayList<ArrayList<Float>> arrayList) {
        String str;
        if (arrayList == null || arrayList.size() <= 0) {
            str = null;
        } else {
            StringBuilder sb = new StringBuilder(100);
            sb.append(com.baidu.location.e.c.g(this.R));
            sb.append("|");
            sb.append(com.baidu.location.e.c.f(this.R));
            sb.append("|");
            sb.append(com.baidu.location.e.c.a(this.R));
            sb.append("|");
            sb.append(com.baidu.location.e.c.h(this.R));
            sb.append("|");
            sb.append(com.baidu.location.e.c.b(this.R));
            sb.append("|");
            sb.append(com.baidu.location.e.c.c(this.R));
            sb.append("|");
            sb.append(com.baidu.location.e.c.e(this.R));
            sb.append("|");
            sb.append(com.baidu.location.e.c.d(this.R));
            str = sb.toString();
        }
        this.V = str;
        this.W = System.currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(boolean z) {
        this.A = z;
        N = -1.0f;
    }

    private boolean b(String str) {
        int i;
        if (str.indexOf(Marker.ANY_MARKER) != -1 && str.indexOf("$") != -1 && str.indexOf("$") <= str.indexOf(Marker.ANY_MARKER) && str.length() >= str.indexOf(Marker.ANY_MARKER)) {
            byte[] bytes = str.substring(0, str.indexOf(Marker.ANY_MARKER)).getBytes();
            int i2 = bytes[1];
            for (int i3 = 2; i3 < bytes.length; i3++) {
                i2 ^= bytes[i3];
            }
            String format = String.format("%02x", Integer.valueOf(i2));
            int indexOf = str.indexOf(Marker.ANY_MARKER);
            if (indexOf != -1 && str.length() >= (i = indexOf + 3) && format.equalsIgnoreCase(str.substring(indexOf + 1, i))) {
                return true;
            }
        }
        return false;
    }

    public static String c(Location location) {
        String a2 = a(location);
        if (a2 != null) {
            return a2 + I;
        }
        return a2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(Location location) {
        e eVar = this.J;
        if (eVar != null) {
            this.J.sendMessage(eVar.obtainMessage(1, location));
        }
    }

    private int e(Location location) {
        if (location == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT > 17 && location.isFromMockProvider()) {
            return 100;
        }
        if (Math.abs(this.aa - this.ab) >= 3000) {
            this.ab = -1L;
            this.ae = false;
            this.ad = false;
            this.ac = null;
        } else if (this.ac == null) {
            if (!this.ad) {
                return 200;
            }
            if (this.ae) {
                return UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME;
            }
        } else if (!this.ae && this.ad) {
            return StatusCode.BAD_REQUEST;
        }
        if (this.aa > 0) {
            if (this.ab == -1) {
                return 500;
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f(Location location) {
        if (location == null) {
            this.g = null;
            return;
        }
        if (a == 0) {
            try {
                location.getExtras().getInt("satellites");
            } catch (Exception unused) {
            }
        }
        if (this.n && com.baidu.location.e.j.a(location.getSpeed(), 0.0f) && !com.baidu.location.e.j.a(this.D, (double) SearchStatUtils.POW) && System.currentTimeMillis() - this.E < 2000.0d) {
            location.setSpeed((float) this.D);
        }
        Location location2 = new Location(location);
        this.B = System.currentTimeMillis();
        this.g = location;
        int i = a;
        long currentTimeMillis = System.currentTimeMillis();
        this.g.setTime(currentTimeMillis);
        float speed = (float) (this.g.getSpeed() * 3.6d);
        if (!this.g.hasSpeed()) {
            speed = -1.0f;
        }
        if (i == 0) {
            try {
                i = this.g.getExtras().getInt("satellites");
            } catch (Exception unused2) {
            }
        }
        this.z = String.format(Locale.CHINA, "&ll=%.5f|%.5f&s=%.1f&d=%.1f&ll_n=%d&ll_t=%d", Double.valueOf(this.g.getLongitude()), Double.valueOf(this.g.getLatitude()), Float.valueOf(speed), Float.valueOf(this.g.getBearing()), Integer.valueOf(i), Long.valueOf(currentTimeMillis));
        if (this.g != null) {
            n();
            if (a > 2 && w.a(this.g, true)) {
                boolean e2 = i.a().e();
                s.a(new com.baidu.location.c.a(com.baidu.location.c.b.a().f()));
                s.a(System.currentTimeMillis());
                s.a(new Location(this.g));
                s.a(com.baidu.location.b.b.a().c());
                if (!e2) {
                    x.a().b();
                }
            }
        }
        x.a().a(location2, a);
    }

    public static String l() {
        long currentTimeMillis = System.currentTimeMillis() - v;
        if (currentTimeMillis < 0 || currentTimeMillis >= 3000) {
            return null;
        }
        return String.format(Locale.US, "&gsvn=%d&gsfn=%d", Integer.valueOf(u), Integer.valueOf(a));
    }

    public void a(boolean z) {
        if (z) {
            c();
        } else {
            d();
        }
    }

    public synchronized void b() {
        if (!com.baidu.location.f.isServing) {
            return;
        }
        Context serviceContext = com.baidu.location.f.getServiceContext();
        this.e = serviceContext;
        try {
            this.f = (LocationManager) serviceContext.getSystemService("location");
            if (!this.l) {
                d dVar = new d(this, null);
                this.m = dVar;
                this.f.addGpsStatusListener(dVar);
            } else {
                c cVar = new c(this, null);
                this.k = cVar;
                this.f.registerGnssStatusCallback(cVar);
            }
            h hVar = new h(this, null);
            this.i = hVar;
            this.f.requestLocationUpdates("passive", 9000L, 0.0f, hVar);
        } catch (Exception unused) {
        }
        this.J = new e(this);
    }

    public void c() {
        Log.d(com.baidu.location.e.a.a, "start gps...");
        if (this.y) {
            return;
        }
        try {
            this.h = new C0009f(this, null);
            try {
                this.f.sendExtraCommand("gps", "force_xtra_injection", new Bundle());
            } catch (Exception unused) {
            }
            this.f.requestLocationUpdates("gps", 1000L, 0.0f, this.h);
            if (this.l && this.P == null && com.baidu.location.e.j.aC == 1 && new Random().nextDouble() < com.baidu.location.e.j.aB) {
                this.P = new b(this, null);
            }
            b bVar = this.P;
            if (bVar != null) {
                this.f.registerGnssNavigationMessageCallback(bVar);
            }
            this.L = System.currentTimeMillis();
            if (!com.baidu.location.e.j.m && com.baidu.location.e.j.aL == 1) {
                if (Build.VERSION.SDK_INT >= 24) {
                    com.baidu.location.c.g gVar = new com.baidu.location.c.g(this);
                    this.p = gVar;
                    this.f.addNmeaListener(gVar);
                } else {
                    this.o = new g(this, null);
                    Class.forName("android.location.LocationManager").getMethod("addNmeaListener", GpsStatus.NmeaListener.class).invoke(this.f, this.o);
                }
            }
            this.y = true;
        } catch (Exception unused2) {
        }
    }

    public void d() {
        if (!this.y) {
            return;
        }
        LocationManager locationManager = this.f;
        if (locationManager != null) {
            try {
                C0009f c0009f = this.h;
                if (c0009f != null) {
                    locationManager.removeUpdates(c0009f);
                }
                OnNmeaMessageListener onNmeaMessageListener = this.p;
                if (onNmeaMessageListener != null) {
                    this.f.removeNmeaListener(onNmeaMessageListener);
                }
                if (this.o != null) {
                    Class.forName("android.location.LocationManager").getMethod("removeNmeaListener", GpsStatus.NmeaListener.class).invoke(this.f, this.o);
                }
                b bVar = this.P;
                if (bVar != null) {
                    this.f.unregisterGnssNavigationMessageCallback(bVar);
                }
                k();
            } catch (Exception unused) {
            }
        }
        com.baidu.location.e.j.d = 0;
        com.baidu.location.e.j.v = 0;
        this.h = null;
        this.y = false;
        b(false);
    }

    public synchronized void e() {
        c cVar;
        d();
        LocationManager locationManager = this.f;
        if (locationManager == null) {
            return;
        }
        try {
            d dVar = this.m;
            if (dVar != null) {
                locationManager.removeGpsStatusListener(dVar);
            }
            if (this.l && (cVar = this.k) != null) {
                this.f.unregisterGnssStatusCallback(cVar);
            }
            h hVar = this.i;
            if (hVar != null) {
                this.f.removeUpdates(hVar);
                this.i = null;
            }
        } catch (Exception unused) {
        }
        try {
            e eVar = this.J;
            if (eVar != null) {
                eVar.removeCallbacksAndMessages(null);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.m = null;
        this.f = null;
    }

    public String f() {
        boolean z;
        StringBuilder sb;
        String str;
        if (this.g != null) {
            String str2 = "{\"result\":{\"time\":\"" + com.baidu.location.e.j.a() + "\",\"error\":\"61\"},\"content\":{\"point\":{\"x\":\"%f\",\"y\":\"%f\"},\"radius\":\"%d\",\"d\":\"%f\",\"s\":\"%f\",\"n\":\"%d\"";
            int accuracy = (int) (this.g.hasAccuracy() ? this.g.getAccuracy() : 10.0f);
            float speed = (float) (this.g.getSpeed() * 3.6d);
            if (!this.g.hasSpeed()) {
                speed = -1.0f;
            }
            double[] dArr = new double[2];
            if (com.baidu.location.e.d.a().a(this.g.getLongitude(), this.g.getLatitude())) {
                dArr = Jni.coorEncrypt(this.g.getLongitude(), this.g.getLatitude(), BDLocation.BDLOCATION_WGS84_TO_GCJ02);
                if (dArr[0] <= SearchStatUtils.POW && dArr[1] <= SearchStatUtils.POW) {
                    dArr[0] = this.g.getLongitude();
                    dArr[1] = this.g.getLatitude();
                }
                z = true;
            } else {
                dArr[0] = this.g.getLongitude();
                dArr[1] = this.g.getLatitude();
                if (dArr[0] <= SearchStatUtils.POW && dArr[1] <= SearchStatUtils.POW) {
                    dArr[0] = this.g.getLongitude();
                    dArr[1] = this.g.getLatitude();
                }
                z = false;
            }
            String format = String.format(Locale.CHINA, str2, Double.valueOf(dArr[0]), Double.valueOf(dArr[1]), Integer.valueOf(accuracy), Float.valueOf(this.g.getBearing()), Float.valueOf(speed), Integer.valueOf(a));
            if (!z) {
                format = format + ",\"in_cn\":\"0\"";
            }
            if (!com.baidu.location.e.j.m) {
                format = format + String.format(Locale.CHINA, ",\"is_mock\":%d", Integer.valueOf(e(this.g)));
            }
            if (this.g.hasAltitude()) {
                sb = new StringBuilder();
                sb.append(format);
                str = String.format(Locale.CHINA, ",\"h\":%.2f}}", Double.valueOf(this.g.getAltitude()));
            } else {
                sb = new StringBuilder();
                sb.append(format);
                str = "}}";
            }
            sb.append(str);
            return sb.toString();
        }
        return null;
    }

    public Location g() {
        if (this.g != null && Math.abs(System.currentTimeMillis() - this.g.getTime()) <= 60000) {
            return this.g;
        }
        return null;
    }

    public BDLocation h() {
        if (this.ac != null && Math.abs(System.currentTimeMillis() - this.ab) <= 3000) {
            return this.ac;
        }
        return null;
    }

    public boolean i() {
        try {
            System.currentTimeMillis();
            if (a == 0) {
                try {
                    this.g.getExtras().getInt("satellites");
                } catch (Exception unused) {
                }
            }
            Location location = this.g;
            if (location != null && location.getLatitude() != SearchStatUtils.POW) {
                if (this.g.getLongitude() != SearchStatUtils.POW) {
                    return true;
                }
            }
            return false;
        } catch (Exception unused2) {
            Location location2 = this.g;
            return (location2 == null || location2.getLatitude() == SearchStatUtils.POW || this.g.getLongitude() == SearchStatUtils.POW) ? false : true;
        }
    }

    public boolean j() {
        if (i() && System.currentTimeMillis() - this.B <= AbstractComponentTracker.LINGERING_TIMEOUT) {
            long currentTimeMillis = System.currentTimeMillis();
            if (this.x && currentTimeMillis - this.w < 3000) {
                return true;
            }
            return this.A;
        }
        return false;
    }

    public void k() {
        a aVar;
        LocationManager locationManager;
        if (!this.l || (aVar = this.O) == null || (locationManager = this.f) == null) {
            return;
        }
        locationManager.unregisterGnssMeasurementsCallback(aVar);
        this.O = null;
    }

    public synchronized String m() {
        String str;
        if (Math.abs(System.currentTimeMillis() - this.W) < 3000) {
            String str2 = this.V;
            str = str2 == null ? "0" : str2;
        } else {
            str = "-1";
        }
        return "&gnsf=" + str;
    }

    public void n() {
        if (com.baidu.location.e.j.m || e(this.g) <= 0) {
            com.baidu.location.b.b.a().a(f());
        } else {
            com.baidu.location.b.b.a().c(new BDLocation(f()));
        }
    }
}
