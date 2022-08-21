package com.baidu.location.b;

import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.baidu.location.PoiRegion;
import com.baidu.location.b.l;
import java.util.List;

/* loaded from: classes.dex */
public class o extends l {
    public static String f = "0";
    public static boolean h = false;
    private static o i;
    private double B;
    private double C;
    public l.b e;
    private long x;
    private boolean j = true;
    private String k = null;
    private BDLocation l = null;
    private BDLocation m = null;
    private com.baidu.location.c.h n = null;
    private com.baidu.location.c.a o = null;
    private com.baidu.location.c.h p = null;
    private com.baidu.location.c.a q = null;
    private boolean r = true;
    private volatile boolean s = false;
    private boolean t = false;
    private long u = 0;
    private long v = 0;
    private Address w = null;
    private String y = null;
    private List<Poi> z = null;
    private PoiRegion A = null;
    private boolean D = false;
    private long E = 0;
    private long F = 0;
    private a G = null;
    private boolean H = false;
    private boolean I = false;
    private boolean J = true;
    public final Handler g = new l.a();
    private boolean K = false;
    private boolean L = false;
    private b M = null;
    private boolean N = false;
    private int O = 0;
    private long P = 0;
    private boolean Q = false;
    private String R = null;
    private boolean S = false;
    private boolean T = true;

    /* loaded from: classes.dex */
    public class a implements Runnable {
        public final /* synthetic */ o a;

        @Override // java.lang.Runnable
        public void run() {
            if (this.a.H) {
                this.a.H = false;
                boolean unused = this.a.I;
            }
        }
    }

    /* loaded from: classes.dex */
    public class b implements Runnable {
        private b() {
        }

        public /* synthetic */ b(o oVar, p pVar) {
            this();
        }

        @Override // java.lang.Runnable
        public void run() {
            if (o.this.N) {
                o.this.N = false;
            }
            if (o.this.t) {
                o.this.t = false;
                o.this.h(null);
            }
        }
    }

    private o() {
        this.e = null;
        this.e = new l.b();
    }

    private boolean a(com.baidu.location.c.a aVar) {
        com.baidu.location.c.a f2 = com.baidu.location.c.b.a().f();
        this.b = f2;
        if (f2 == aVar) {
            return false;
        }
        if (f2 != null && aVar != null) {
            return !aVar.a(f2);
        }
        return true;
    }

    private boolean a(com.baidu.location.c.h hVar) {
        com.baidu.location.c.h o = com.baidu.location.c.i.a().o();
        this.a = o;
        if (hVar == o) {
            return false;
        }
        if (o != null && hVar != null) {
            return !hVar.c(o);
        }
        return true;
    }

    public static synchronized o c() {
        o oVar;
        synchronized (o.class) {
            if (i == null) {
                i = new o();
            }
            oVar = i;
        }
        return oVar;
    }

    private void c(Message message) {
        if (!com.baidu.location.e.j.c(com.baidu.location.f.getServiceContext())) {
            BDLocation bDLocation = new BDLocation();
            bDLocation.setLocType(62);
            com.baidu.location.b.b.a().a(bDLocation);
            return;
        }
        if (com.baidu.location.e.j.b()) {
            Log.d(com.baidu.location.e.a.a, "isInforbiddenTime on request location ...");
        }
        if (message.getData().getBoolean("isWaitingLocTag", false)) {
            h = true;
        }
        int d = com.baidu.location.b.b.a().d(message);
        if (d == 1) {
            d(message);
        } else if (d == 2) {
            g(message);
        } else if (d != 3) {
            throw new IllegalArgumentException(String.format("this type %d is illegal", Integer.valueOf(d)));
        } else {
            if (!com.baidu.location.c.f.a().j()) {
                return;
            }
            e(message);
        }
    }

    private void d(Message message) {
        if (com.baidu.location.c.f.a().j()) {
            e(message);
            r.a().c();
            return;
        }
        g(message);
        r.a().b();
    }

    private void d(BDLocation bDLocation) {
        if (com.baidu.location.e.j.m || bDLocation.getMockGpsStrategy() <= 0) {
            com.baidu.location.b.b.a().a(bDLocation);
        } else {
            com.baidu.location.b.b.a().c(bDLocation);
        }
    }

    private void e(Message message) {
        BDLocation bDLocation = new BDLocation(com.baidu.location.c.f.a().f());
        if (com.baidu.location.e.j.g.equals("all") || com.baidu.location.e.j.h || com.baidu.location.e.j.j) {
            float[] fArr = new float[2];
            Location.distanceBetween(this.C, this.B, bDLocation.getLatitude(), bDLocation.getLongitude(), fArr);
            if (fArr[0] < 100.0f) {
                Address address = this.w;
                if (address != null) {
                    bDLocation.setAddr(address);
                }
                String str = this.y;
                if (str != null) {
                    bDLocation.setLocationDescribe(str);
                }
                List<Poi> list = this.z;
                if (list != null) {
                    bDLocation.setPoiList(list);
                }
                PoiRegion poiRegion = this.A;
                if (poiRegion != null) {
                    bDLocation.setPoiRegion(poiRegion);
                }
            } else {
                this.D = true;
                g(null);
            }
        }
        this.l = bDLocation;
        this.m = null;
        d(bDLocation);
    }

    private void f(Message message) {
        b bVar;
        if (!com.baidu.location.c.i.a().f()) {
            h(message);
            return;
        }
        this.t = true;
        if (this.M == null) {
            this.M = new b(this, null);
        }
        if (this.N && (bVar = this.M) != null) {
            this.g.removeCallbacks(bVar);
        }
        this.g.postDelayed(this.M, 3500L);
        this.N = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g(Message message) {
        this.O = 0;
        if (!this.r) {
            f(message);
            this.F = SystemClock.uptimeMillis();
            return;
        }
        this.O = 1;
        this.F = SystemClock.uptimeMillis();
        if (com.baidu.location.c.i.a().j()) {
            f(message);
        } else {
            h(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x009e, code lost:
        if (r6 <= 0) goto L46;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void h(android.os.Message r14) {
        /*
            Method dump skipped, instructions count: 462
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.b.o.h(android.os.Message):void");
    }

    private String[] k() {
        boolean z;
        c a2;
        int i2;
        String[] strArr = {"", "Location failed beacuse we can not get any loc information!"};
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("&apl=");
        int a3 = com.baidu.location.e.j.a(com.baidu.location.f.getServiceContext());
        String str = "Location failed beacuse we can not get any loc information in airplane mode, you can turn it off and try again!!";
        if (a3 == 1) {
            strArr[1] = str;
        }
        stringBuffer.append(a3);
        String d = com.baidu.location.e.j.d(com.baidu.location.f.getServiceContext());
        if (d.contains("0|0|")) {
            strArr[1] = "Location failed beacuse we can not get any loc information without any location permission!";
        }
        stringBuffer.append(d);
        int i3 = Build.VERSION.SDK_INT;
        if (i3 >= 23) {
            stringBuffer.append("&loc=");
            int b2 = com.baidu.location.e.j.b(com.baidu.location.f.getServiceContext());
            if (b2 == 0) {
                strArr[1] = "Location failed beacuse we can not get any loc information with the phone loc mode is off, you can turn it on and try again!";
                z = true;
            } else {
                z = false;
            }
            stringBuffer.append(b2);
        } else {
            z = false;
        }
        if (i3 >= 19) {
            stringBuffer.append("&lmd=");
            int b3 = com.baidu.location.e.j.b(com.baidu.location.f.getServiceContext());
            if (b3 >= 0) {
                stringBuffer.append(b3);
            }
        }
        String g = com.baidu.location.c.b.a().g();
        String g2 = com.baidu.location.c.i.a().g();
        stringBuffer.append(g2);
        stringBuffer.append(g);
        stringBuffer.append(com.baidu.location.e.j.e(com.baidu.location.f.getServiceContext()));
        if (a3 != 1) {
            if (d.contains("0|0|")) {
                c.a().a(62, 4, "Location failed beacuse we can not get any loc information without any location permission!");
            } else if (z) {
                c.a().a(62, 5, "Location failed beacuse we can not get any loc information with the phone loc mode is off, you can turn it on and try again!");
            } else if (g == null || g2 == null || !g.equals("&sim=1") || g2.equals("&wifio=1")) {
                c.a().a(62, 9, "Location failed beacuse we can not get any loc information!");
            } else {
                a2 = c.a();
                i2 = 6;
                str = "Location failed beacuse we can not get any loc information , you can insert a sim card or open wifi and try again!";
            }
            strArr[0] = stringBuffer.toString();
            return strArr;
        }
        a2 = c.a();
        i2 = 7;
        a2.a(62, i2, str);
        strArr[0] = stringBuffer.toString();
        return strArr;
    }

    private void l() {
        this.s = false;
        this.I = false;
        this.J = false;
        this.D = false;
        m();
        if (this.T) {
            this.T = false;
        }
    }

    private void m() {
        if (this.l == null || !com.baidu.location.c.i.a().i()) {
            return;
        }
        x.a().d();
    }

    public Address a(BDLocation bDLocation) {
        if (com.baidu.location.e.j.g.equals("all") || com.baidu.location.e.j.h || com.baidu.location.e.j.j) {
            float[] fArr = new float[2];
            Location.distanceBetween(this.C, this.B, bDLocation.getLatitude(), bDLocation.getLongitude(), fArr);
            if (fArr[0] < 100.0d) {
                Address address = this.w;
                if (address != null) {
                    return address;
                }
            } else {
                this.y = null;
                this.z = null;
                this.A = null;
                this.D = true;
                this.g.post(new p(this));
            }
        }
        return null;
    }

    @Override // com.baidu.location.b.l
    public void a() {
        a aVar = this.G;
        if (aVar != null && this.H) {
            this.H = false;
            this.g.removeCallbacks(aVar);
        }
        if (com.baidu.location.c.f.a().j()) {
            BDLocation bDLocation = new BDLocation(com.baidu.location.c.f.a().f());
            if (com.baidu.location.e.j.g.equals("all") || com.baidu.location.e.j.h || com.baidu.location.e.j.j) {
                float[] fArr = new float[2];
                Location.distanceBetween(this.C, this.B, bDLocation.getLatitude(), bDLocation.getLongitude(), fArr);
                if (fArr[0] < 100.0f) {
                    Address address = this.w;
                    if (address != null) {
                        bDLocation.setAddr(address);
                    }
                    String str = this.y;
                    if (str != null) {
                        bDLocation.setLocationDescribe(str);
                    }
                    List<Poi> list = this.z;
                    if (list != null) {
                        bDLocation.setPoiList(list);
                    }
                    PoiRegion poiRegion = this.A;
                    if (poiRegion != null) {
                        bDLocation.setPoiRegion(poiRegion);
                    }
                }
            }
            com.baidu.location.b.b.a().a(bDLocation);
        } else if (this.I) {
            l();
            return;
        } else {
            if (this.j || this.l == null) {
                BDLocation bDLocation2 = new BDLocation();
                bDLocation2.setLocType(63);
                this.l = null;
                com.baidu.location.b.b.a().a(bDLocation2);
            } else {
                com.baidu.location.b.b.a().a(this.l);
            }
            this.m = null;
        }
        l();
    }

    @Override // com.baidu.location.b.l
    public void a(Message message) {
        a aVar = this.G;
        if (aVar != null && this.H) {
            this.H = false;
            this.g.removeCallbacks(aVar);
        }
        BDLocation bDLocation = (BDLocation) message.obj;
        if (bDLocation != null && bDLocation.getLocType() == 167 && this.L) {
            bDLocation.setLocType(62);
        }
        b(bDLocation);
    }

    public void b(Message message) {
        if (!this.K) {
            return;
        }
        c(message);
    }

    public void b(BDLocation bDLocation) {
        String g;
        int b2;
        com.baidu.location.c.h hVar;
        BDLocation bDLocation2;
        String str;
        new BDLocation(bDLocation);
        if (bDLocation.hasAddr()) {
            Address address = bDLocation.getAddress();
            this.w = address;
            if (address != null && (str = address.cityCode) != null) {
                f = str;
                this.x = System.currentTimeMillis();
            }
            this.B = bDLocation.getLongitude();
            this.C = bDLocation.getLatitude();
        }
        if (bDLocation.getLocationDescribe() != null) {
            this.y = bDLocation.getLocationDescribe();
            this.B = bDLocation.getLongitude();
            this.C = bDLocation.getLatitude();
        }
        if (bDLocation.getPoiList() != null) {
            this.z = bDLocation.getPoiList();
            this.B = bDLocation.getLongitude();
            this.C = bDLocation.getLatitude();
        }
        if (bDLocation.getPoiRegion() != null) {
            this.A = bDLocation.getPoiRegion();
            this.B = bDLocation.getLongitude();
            this.C = bDLocation.getLatitude();
        }
        boolean z = false;
        if (com.baidu.location.c.f.a().j()) {
            BDLocation bDLocation3 = new BDLocation(com.baidu.location.c.f.a().f());
            if (com.baidu.location.e.j.g.equals("all") || com.baidu.location.e.j.h || com.baidu.location.e.j.j) {
                float[] fArr = new float[2];
                Location.distanceBetween(this.C, this.B, bDLocation3.getLatitude(), bDLocation3.getLongitude(), fArr);
                if (fArr[0] < 100.0f) {
                    Address address2 = this.w;
                    if (address2 != null) {
                        bDLocation3.setAddr(address2);
                    }
                    String str2 = this.y;
                    if (str2 != null) {
                        bDLocation3.setLocationDescribe(str2);
                    }
                    List<Poi> list = this.z;
                    if (list != null) {
                        bDLocation3.setPoiList(list);
                    }
                    PoiRegion poiRegion = this.A;
                    if (poiRegion != null) {
                        bDLocation3.setPoiRegion(poiRegion);
                    }
                }
            }
            d(bDLocation3);
            l();
        } else if (this.I) {
            float[] fArr2 = new float[2];
            BDLocation bDLocation4 = this.l;
            if (bDLocation4 != null) {
                Location.distanceBetween(bDLocation4.getLatitude(), this.l.getLongitude(), bDLocation.getLatitude(), bDLocation.getLongitude(), fArr2);
            }
            if (fArr2[0] <= 10.0f) {
                if (bDLocation.getUserIndoorState() > -1) {
                    this.l = bDLocation;
                    com.baidu.location.b.b.a().a(bDLocation);
                }
                l();
            }
            this.l = bDLocation;
            if (!this.J) {
                this.J = false;
                com.baidu.location.b.b.a().a(bDLocation);
            }
            l();
        } else {
            if (bDLocation.getLocType() == 167) {
                c.a().a(BDLocation.TypeServerError, 8, "NetWork location failed because baidu location service can not caculate the location!");
            } else if (bDLocation.getLocType() == 161) {
                if (Build.VERSION.SDK_INT >= 19 && ((b2 = com.baidu.location.e.j.b(com.baidu.location.f.getServiceContext())) == 0 || b2 == 2)) {
                    c.a().a(BDLocation.TypeNetWorkLocation, 1, "NetWork location successful, open gps will be better!");
                } else if (bDLocation.getRadius() >= 100.0f && bDLocation.getNetworkLocationType() != null && bDLocation.getNetworkLocationType().equals("cl") && (g = com.baidu.location.c.i.a().g()) != null && !g.equals("&wifio=1")) {
                    c.a().a(BDLocation.TypeNetWorkLocation, 2, "NetWork location successful, open wifi will be better!");
                }
            }
            this.m = null;
            if (bDLocation.getLocType() == 161 && "cl".equals(bDLocation.getNetworkLocationType()) && (bDLocation2 = this.l) != null && bDLocation2.getLocType() == 161 && "wf".equals(this.l.getNetworkLocationType()) && System.currentTimeMillis() - this.v < 30000) {
                this.m = bDLocation;
                z = true;
            }
            com.baidu.location.b.b a2 = com.baidu.location.b.b.a();
            if (z) {
                a2.a(this.l);
            } else {
                a2.a(bDLocation);
                this.v = System.currentTimeMillis();
            }
            if (!com.baidu.location.e.j.a(bDLocation)) {
                this.l = null;
            } else if (!z) {
                this.l = bDLocation;
            }
            int a3 = com.baidu.location.e.j.a(l.c, "ssid\":\"", "\"");
            if (a3 == Integer.MIN_VALUE || (hVar = this.n) == null) {
                this.k = null;
            } else {
                this.k = hVar.b(a3);
            }
            com.baidu.location.c.i.a().i();
            l();
        }
    }

    public void c(BDLocation bDLocation) {
        this.l = new BDLocation(bDLocation);
    }

    public void d() {
        this.r = true;
        this.s = false;
        this.K = true;
    }

    public void e() {
        this.s = false;
        this.t = false;
        this.I = false;
        this.J = true;
        j();
        this.K = false;
    }

    public String f() {
        return this.y;
    }

    public List<Poi> g() {
        return this.z;
    }

    public PoiRegion h() {
        return this.A;
    }

    public void i() {
        if (this.t) {
            h(null);
            this.t = false;
        }
    }

    public void j() {
        this.l = null;
    }
}
