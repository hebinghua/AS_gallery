package com.baidu.location.b;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.Jni;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.location.PoiRegion;
import com.baidu.platform.comapi.location.CoordinateType;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class b {
    public static long c = 0;
    public static int d = -1;
    private static b f;
    private ArrayList<a> g;
    private boolean h = false;
    public boolean a = false;
    public boolean b = false;
    private BDLocation i = null;
    private BDLocation j = null;
    private Object k = new Object();
    public int e = 0;
    private BDLocation l = null;
    private boolean m = false;
    private boolean n = false;
    private RunnableC0006b o = null;

    /* loaded from: classes.dex */
    public class a {
        public String a;
        public Messenger b;
        public LocationClientOption c = new LocationClientOption();
        public int d = 0;

        public a(Message message) {
            this.a = null;
            this.b = null;
            boolean z = false;
            this.b = message.replyTo;
            this.a = message.getData().getString("packName");
            this.c.prodName = message.getData().getString("prodName");
            com.baidu.location.e.b.a().a(this.c.prodName, this.a);
            this.c.coorType = message.getData().getString("coorType");
            this.c.addrType = message.getData().getString("addrType");
            this.c.enableSimulateGps = message.getData().getBoolean("enableSimulateGps", false);
            com.baidu.location.e.j.m = com.baidu.location.e.j.m || this.c.enableSimulateGps;
            if (!com.baidu.location.e.j.g.equals("all")) {
                com.baidu.location.e.j.g = this.c.addrType;
            }
            this.c.openGps = message.getData().getBoolean("openGPS");
            this.c.scanSpan = message.getData().getInt("scanSpan");
            this.c.timeOut = message.getData().getInt("timeOut");
            this.c.priority = message.getData().getInt(com.xiaomi.stat.a.j.k);
            this.c.location_change_notify = message.getData().getBoolean("location_change_notify");
            this.c.mIsNeedDeviceDirect = message.getData().getBoolean("needDirect", false);
            this.c.isNeedAltitude = message.getData().getBoolean("isneedaltitude", false);
            this.c.isNeedNewVersionRgc = message.getData().getBoolean("isneednewrgc", false);
            com.baidu.location.e.j.i = com.baidu.location.e.j.i || this.c.isNeedNewVersionRgc;
            com.baidu.location.e.j.h = com.baidu.location.e.j.h || message.getData().getBoolean("isneedaptag", false);
            com.baidu.location.e.j.j = com.baidu.location.e.j.j || message.getData().getBoolean("isneedaptagd", false);
            com.baidu.location.e.j.S = message.getData().getFloat("autoNotifyLocSensitivity", 0.5f);
            int i = message.getData().getInt("wfnum", com.baidu.location.e.j.az);
            float f = message.getData().getFloat("wfsm", com.baidu.location.e.j.aA);
            int i2 = message.getData().getInt("gnmcon", com.baidu.location.e.j.aC);
            double d = message.getData().getDouble("gnmcrm", com.baidu.location.e.j.aB);
            int i3 = message.getData().getInt("iupl", 1);
            com.baidu.location.e.j.aF = message.getData().getInt("ct", 10);
            com.baidu.location.e.j.aG = message.getData().getInt("suci", 3);
            com.baidu.location.e.j.aI = message.getData().getDoubleArray("cgs");
            com.baidu.location.e.j.aJ = message.getData().getInt("ums", 1);
            com.baidu.location.e.j.aH = message.getData().getInt("smn", 40);
            if (i3 <= 0) {
                com.baidu.location.e.j.aE = 0;
            } else if (com.baidu.location.e.j.aE == -1) {
                com.baidu.location.e.j.aE = 1;
            }
            if (message.getData().getInt("opetco", 1) == 0) {
                com.baidu.location.e.j.aK = 0;
            }
            if (message.getData().getInt("lpcs", com.baidu.location.e.j.aL) == 0) {
                com.baidu.location.e.j.aL = 0;
            }
            if (i2 == 1) {
                com.baidu.location.e.j.aC = 1;
            }
            if (d > com.baidu.location.e.j.aB) {
                com.baidu.location.e.j.aB = d;
            }
            com.baidu.location.e.j.ay = com.baidu.location.e.j.ay || message.getData().getBoolean("ischeckper", false);
            if (i > com.baidu.location.e.j.az) {
                com.baidu.location.e.j.az = i;
            }
            if (f > com.baidu.location.e.j.aA) {
                com.baidu.location.e.j.aA = f;
            }
            int i4 = message.getData().getInt("wifitimeout", Integer.MAX_VALUE);
            if (i4 < com.baidu.location.e.j.ag) {
                com.baidu.location.e.j.ag = i4;
            }
            int i5 = message.getData().getInt("autoNotifyMaxInterval", 0);
            if (i5 >= com.baidu.location.e.j.X) {
                com.baidu.location.e.j.X = i5;
            }
            int i6 = message.getData().getInt("autoNotifyMinDistance", 0);
            if (i6 >= com.baidu.location.e.j.Z) {
                com.baidu.location.e.j.Z = i6;
            }
            int i7 = message.getData().getInt("autoNotifyMinTimeInterval", 0);
            if (i7 >= com.baidu.location.e.j.Y) {
                com.baidu.location.e.j.Y = i7;
            }
            LocationClientOption locationClientOption = this.c;
            if (locationClientOption.mIsNeedDeviceDirect || locationClientOption.isNeedAltitude) {
                r.a().a(this.c.mIsNeedDeviceDirect);
                r.a().b();
            }
            b.this.b = (b.this.b || this.c.isNeedAltitude) ? true : z;
        }

        private double a(boolean z, BDLocation bDLocation, BDLocation bDLocation2) {
            double d;
            double latitude;
            double longitude;
            double latitude2;
            double longitude2;
            double a;
            double[] dArr;
            if (z) {
                if (TextUtils.equals(bDLocation2.getCoorType(), bDLocation.getCoorType())) {
                    if (TextUtils.equals(BDLocation.BDLOCATION_GCJ02_TO_BD09, bDLocation2.getCoorType())) {
                        double[] coorEncrypt = Jni.coorEncrypt(bDLocation2.getLongitude(), bDLocation2.getLatitude(), BDLocation.BDLOCATION_BD09_TO_GCJ02);
                        double[] coorEncrypt2 = Jni.coorEncrypt(bDLocation.getLongitude(), bDLocation.getLatitude(), BDLocation.BDLOCATION_BD09_TO_GCJ02);
                        latitude = coorEncrypt[1];
                        longitude = coorEncrypt[0];
                        latitude2 = coorEncrypt2[1];
                        longitude2 = coorEncrypt2[0];
                        a = com.baidu.location.e.j.a(latitude, longitude, latitude2, longitude2);
                    }
                    a = com.baidu.location.e.j.a(bDLocation2.getLatitude(), bDLocation2.getLongitude(), bDLocation.getLatitude(), bDLocation.getLongitude());
                } else {
                    if (!TextUtils.equals(CoordinateType.WGS84, bDLocation.getCoorType())) {
                        double[] coorEncrypt3 = TextUtils.equals(BDLocation.BDLOCATION_GCJ02_TO_BD09, bDLocation.getCoorType()) ? Jni.coorEncrypt(bDLocation.getLongitude(), bDLocation.getLatitude(), BDLocation.BDLOCATION_BD09_TO_GCJ02) : TextUtils.equals("bd09ll", bDLocation.getCoorType()) ? Jni.coorEncrypt(bDLocation.getLongitude(), bDLocation.getLatitude(), BDLocation.BDLOCATION_BD09LL_TO_GCJ02) : new double[]{bDLocation.getLongitude(), bDLocation.getLatitude()};
                        dArr = Jni.coorEncrypt(coorEncrypt3[0], coorEncrypt3[1], "gcj2wgs");
                    } else {
                        dArr = new double[]{bDLocation.getLongitude(), bDLocation.getLatitude()};
                    }
                    bDLocation.setLatitude(dArr[1]);
                    d = dArr[0];
                    bDLocation.setLongitude(d);
                    bDLocation.setTime(com.baidu.location.e.j.a());
                    bDLocation.setCoorType(CoordinateType.WGS84);
                    a = com.baidu.location.e.j.a(bDLocation2.getLatitude(), bDLocation2.getLongitude(), bDLocation.getLatitude(), bDLocation.getLongitude());
                }
            } else if (TextUtils.equals(bDLocation2.getCoorType(), bDLocation.getCoorType())) {
                latitude = bDLocation2.getLatitude();
                longitude = bDLocation2.getLongitude();
                latitude2 = bDLocation.getLatitude();
                longitude2 = bDLocation.getLongitude();
                a = com.baidu.location.e.j.a(latitude, longitude, latitude2, longitude2);
            } else {
                double[] coorEncrypt4 = Jni.coorEncrypt(bDLocation.getLongitude(), bDLocation.getLatitude(), "gcj2wgs");
                bDLocation.setLatitude(coorEncrypt4[1]);
                d = coorEncrypt4[0];
                bDLocation.setLongitude(d);
                bDLocation.setTime(com.baidu.location.e.j.a());
                bDLocation.setCoorType(CoordinateType.WGS84);
                a = com.baidu.location.e.j.a(bDLocation2.getLatitude(), bDLocation2.getLongitude(), bDLocation.getLatitude(), bDLocation.getLongitude());
            }
            bDLocation2.setDisToRealLocation(a);
            if (bDLocation != null) {
                bDLocation2.setReallLocation(bDLocation);
            }
            return a;
        }

        private int a(double d) {
            if (d < SearchStatUtils.POW || d > 10.0d) {
                if (d > 10.0d && d <= 100.0d) {
                    return 1;
                }
                return (d <= 100.0d || d > 200.0d) ? 3 : 2;
            }
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(int i) {
            Message obtain = Message.obtain((Handler) null, i);
            try {
                Messenger messenger = this.b;
                if (messenger != null) {
                    messenger.send(obtain);
                }
                this.d = 0;
            } catch (Exception e) {
                if (!(e instanceof DeadObjectException)) {
                    return;
                }
                this.d++;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(int i, Bundle bundle) {
            Message obtain = Message.obtain((Handler) null, i);
            obtain.setData(bundle);
            try {
                Messenger messenger = this.b;
                if (messenger != null) {
                    messenger.send(obtain);
                }
                this.d = 0;
            } catch (Exception e) {
                if (e instanceof DeadObjectException) {
                    this.d++;
                }
                e.printStackTrace();
            }
        }

        private void a(int i, String str, BDLocation bDLocation) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(str, bDLocation);
            bundle.setClassLoader(BDLocation.class.getClassLoader());
            Message obtain = Message.obtain((Handler) null, i);
            obtain.setData(bundle);
            try {
                Messenger messenger = this.b;
                if (messenger != null) {
                    messenger.send(obtain);
                }
                this.d = 0;
            } catch (Exception e) {
                if (!(e instanceof DeadObjectException)) {
                    return;
                }
                this.d++;
            }
        }

        private BDLocation b() {
            BDLocation h = com.baidu.location.c.f.a().h();
            if (h != null) {
                double[] coorEncrypt = Jni.coorEncrypt(h.getLongitude(), h.getLatitude(), BDLocation.BDLOCATION_WGS84_TO_GCJ02);
                double[] coorEncrypt2 = Jni.coorEncrypt(coorEncrypt[0], coorEncrypt[1], this.c.coorType);
                BDLocation bDLocation = new BDLocation();
                bDLocation.setLongitude(coorEncrypt2[0]);
                bDLocation.setLatitude(coorEncrypt2[1]);
                bDLocation.setTime(com.baidu.location.e.j.a());
                bDLocation.setLocType(61);
                bDLocation.setCoorType(this.c.coorType);
                return bDLocation;
            }
            return null;
        }

        private BDLocation c() {
            BDLocation h = com.baidu.location.c.f.a().h();
            if (h != null) {
                double[] coorEncrypt = Jni.coorEncrypt(h.getLongitude(), h.getLatitude(), BDLocation.BDLOCATION_WGS84_TO_GCJ02);
                BDLocation bDLocation = new BDLocation();
                bDLocation.setLongitude(coorEncrypt[0]);
                bDLocation.setLatitude(coorEncrypt[1]);
                bDLocation.setTime(com.baidu.location.e.j.a());
                bDLocation.setLocType(61);
                bDLocation.setCoorType(CoordinateType.GCJ02);
                return bDLocation;
            }
            return null;
        }

        public int a(int i, boolean z, BDLocation bDLocation) {
            double a;
            if (i == 100) {
                if (z) {
                    BDLocation b = b();
                    if (b == null) {
                        return 3;
                    }
                    a(true, b, bDLocation);
                    return 3;
                }
                BDLocation c = c();
                if (c == null) {
                    return 3;
                }
                a(false, c, bDLocation);
                return 3;
            } else if (i == 200 || i == 300) {
                return 1;
            } else {
                if (i != 400) {
                    return i == 500 ? 1 : 0;
                }
                if (z) {
                    BDLocation b2 = b();
                    if (b2 == null) {
                        return -1;
                    }
                    a = a(true, b2, bDLocation);
                } else {
                    BDLocation c2 = c();
                    if (c2 == null) {
                        return -1;
                    }
                    a = a(false, c2, bDLocation);
                }
                return a(a);
            }
        }

        public void a() {
            if (this.c.location_change_notify) {
                a(com.baidu.location.e.j.b ? 54 : 55);
            }
        }

        public void a(BDLocation bDLocation) {
            a(bDLocation, 21);
        }

        public void a(BDLocation bDLocation, int i) {
            int a;
            String str;
            BDLocation bDLocation2 = new BDLocation(bDLocation);
            if (i == 21) {
                a(27, "locStr", bDLocation2);
            }
            String str2 = this.c.coorType;
            if (str2 != null && !str2.equals(CoordinateType.GCJ02)) {
                double longitude = bDLocation2.getLongitude();
                double latitude = bDLocation2.getLatitude();
                if (longitude != Double.MIN_VALUE && latitude != Double.MIN_VALUE) {
                    if ((bDLocation2.getCoorType() != null && bDLocation2.getCoorType().equals(CoordinateType.GCJ02)) || bDLocation2.getCoorType() == null) {
                        double[] coorEncrypt = Jni.coorEncrypt(longitude, latitude, this.c.coorType);
                        bDLocation2.setLongitude(coorEncrypt[0]);
                        bDLocation2.setLatitude(coorEncrypt[1]);
                        str = this.c.coorType;
                    } else if (bDLocation2.getCoorType() != null && bDLocation2.getCoorType().equals(CoordinateType.WGS84) && !this.c.coorType.equals("bd09ll")) {
                        double[] coorEncrypt2 = Jni.coorEncrypt(longitude, latitude, "wgs842mc");
                        bDLocation2.setLongitude(coorEncrypt2[0]);
                        bDLocation2.setLatitude(coorEncrypt2[1]);
                        str = "wgs84mc";
                    }
                    bDLocation2.setCoorType(str);
                }
                if (!com.baidu.location.e.j.m && bDLocation2.getMockGpsStrategy() > 0) {
                    a = a(bDLocation2.getMockGpsStrategy(), true, bDLocation2);
                    bDLocation2.setMockGpsProbability(a);
                }
            } else if (!com.baidu.location.e.j.m && bDLocation2.getMockGpsStrategy() > 0) {
                a = a(bDLocation2.getMockGpsStrategy(), false, bDLocation2);
                bDLocation2.setMockGpsProbability(a);
            }
            a(i, "locStr", bDLocation2);
        }
    }

    /* renamed from: com.baidu.location.b.b$b  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class RunnableC0006b implements Runnable {
        public final /* synthetic */ b a;
        private int b;
        private boolean c;

        @Override // java.lang.Runnable
        public void run() {
            if (this.c) {
                return;
            }
            this.b++;
            this.a.n = false;
        }
    }

    private b() {
        this.g = null;
        this.g = new ArrayList<>();
    }

    private a a(Messenger messenger) {
        ArrayList<a> arrayList = this.g;
        if (arrayList == null) {
            return null;
        }
        Iterator<a> it = arrayList.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (next.b.equals(messenger)) {
                return next;
            }
        }
        return null;
    }

    public static b a() {
        if (f == null) {
            f = new b();
        }
        return f;
    }

    private void a(a aVar) {
        if (aVar == null) {
            return;
        }
        synchronized (this.k) {
            if (a(aVar.b) != null) {
                aVar.a(14);
            } else {
                this.g.add(aVar);
                aVar.a(13);
            }
        }
    }

    private void b(String str) {
        Intent intent = new Intent("com.baidu.location.flp.log");
        intent.setPackage("com.baidu.baidulocationdemo");
        intent.putExtra("data", str);
        intent.putExtra("pack", com.baidu.location.e.b.e);
        intent.putExtra(nexExportFormat.TAG_FORMAT_TAG, "state");
        com.baidu.location.f.getServiceContext().sendBroadcast(intent);
    }

    private void e() {
        f();
        d();
    }

    private void f() {
        Iterator<a> it = this.g.iterator();
        boolean z = false;
        boolean z2 = false;
        while (it.hasNext()) {
            LocationClientOption locationClientOption = it.next().c;
            if (locationClientOption.openGps) {
                z2 = true;
            }
            if (locationClientOption.location_change_notify) {
                z = true;
            }
        }
        com.baidu.location.e.j.a = z;
        if (this.h != z2) {
            this.h = z2;
            com.baidu.location.c.f.a().a(this.h);
        }
    }

    public void a(Bundle bundle, int i) {
        synchronized (this.k) {
            Iterator<a> it = this.g.iterator();
            while (it.hasNext()) {
                try {
                    a next = it.next();
                    next.a(i, bundle);
                    if (next.d > 4) {
                        it.remove();
                    }
                } catch (Exception unused) {
                }
            }
        }
    }

    public void a(Message message) {
        if (message == null || message.replyTo == null) {
            return;
        }
        c = System.currentTimeMillis();
        this.a = true;
        com.baidu.location.c.i.a().b();
        a(new a(message));
        e();
        if (!this.m) {
            return;
        }
        b("start");
        this.e = 0;
    }

    public void a(BDLocation bDLocation) {
        b(bDLocation);
    }

    public void a(String str) {
        c(new BDLocation(str));
    }

    public void a(boolean z) {
        this.a = z;
        d = z ? 1 : 0;
    }

    public void b() {
        synchronized (this.k) {
            try {
                ArrayList<a> arrayList = this.g;
                if (arrayList != null) {
                    arrayList.clear();
                }
            } catch (Throwable unused) {
            }
        }
        this.i = null;
        e();
    }

    public void b(Message message) {
        synchronized (this.k) {
            a a2 = a(message.replyTo);
            if (a2 != null) {
                this.g.remove(a2);
            }
        }
        r.a().c();
        e();
        if (this.m) {
            b("stop");
            this.e = 0;
        }
    }

    public void b(BDLocation bDLocation) {
        BDLocation bDLocation2;
        if (bDLocation == null || bDLocation.getLocType() != 161 || com.baidu.location.a.a.a().b()) {
            synchronized (this.k) {
                Iterator<a> it = this.g.iterator();
                while (it.hasNext()) {
                    try {
                        a next = it.next();
                        next.a(bDLocation);
                        if (next.d > 4) {
                            it.remove();
                        }
                    } catch (Exception unused) {
                    }
                }
            }
        } else {
            if (this.j == null) {
                BDLocation bDLocation3 = new BDLocation();
                this.j = bDLocation3;
                bDLocation3.setLocType(505);
            }
            synchronized (this.k) {
                Iterator<a> it2 = this.g.iterator();
                while (it2.hasNext()) {
                    try {
                        a next2 = it2.next();
                        next2.a(this.j);
                        if (next2.d > 4) {
                            it2.remove();
                        }
                    } catch (Exception unused2) {
                    }
                }
            }
        }
        boolean z = o.h;
        if (z) {
            o.h = false;
        }
        if (com.baidu.location.e.j.X >= 10000) {
            if (bDLocation.getLocType() != 61 && bDLocation.getLocType() != 161 && bDLocation.getLocType() != 66) {
                return;
            }
            BDLocation bDLocation4 = this.i;
            if (bDLocation4 != null) {
                float[] fArr = new float[1];
                Location.distanceBetween(bDLocation4.getLatitude(), this.i.getLongitude(), bDLocation.getLatitude(), bDLocation.getLongitude(), fArr);
                if (fArr[0] <= com.baidu.location.e.j.Z && !z) {
                    return;
                }
                this.i = null;
                bDLocation2 = new BDLocation(bDLocation);
            } else {
                bDLocation2 = new BDLocation(bDLocation);
            }
            this.i = bDLocation2;
        }
    }

    public String c() {
        StringBuilder sb;
        StringBuffer stringBuffer = new StringBuffer(256);
        if (this.g.isEmpty()) {
            return "&prod=" + com.baidu.location.e.b.f + ":" + com.baidu.location.e.b.e;
        }
        String stringBuffer2 = stringBuffer.toString();
        try {
            a aVar = this.g.get(0);
            String str = aVar.c.prodName;
            if (str != null) {
                stringBuffer.append(str);
            }
            if (aVar.a != null) {
                stringBuffer.append(":");
                stringBuffer.append(aVar.a);
                stringBuffer.append("|");
            }
            if (stringBuffer2 == null || stringBuffer2.equals("")) {
                sb = new StringBuilder();
                sb.append("&prod=");
                sb.append(com.baidu.location.e.b.f);
                sb.append(":");
                stringBuffer2 = com.baidu.location.e.b.e;
            } else {
                sb = new StringBuilder();
                sb.append("&prod=");
            }
            sb.append(stringBuffer2);
            return sb.toString();
        } catch (Exception unused) {
            return "&prod=" + com.baidu.location.e.b.f + ":" + com.baidu.location.e.b.e;
        }
    }

    public void c(BDLocation bDLocation) {
        Address a2 = o.c().a(bDLocation);
        String f2 = o.c().f();
        List<Poi> g = o.c().g();
        PoiRegion h = o.c().h();
        if (a2 != null) {
            bDLocation.setAddr(a2);
        }
        if (f2 != null) {
            bDLocation.setLocationDescribe(f2);
        }
        if (g != null) {
            bDLocation.setPoiList(g);
        }
        if (h != null) {
            bDLocation.setPoiRegion(h);
        }
        a(bDLocation);
        o.c().c(bDLocation);
    }

    public boolean c(Message message) {
        a a2 = a(message.replyTo);
        boolean z = false;
        if (a2 == null) {
            return false;
        }
        LocationClientOption locationClientOption = a2.c;
        int i = locationClientOption.scanSpan;
        locationClientOption.scanSpan = message.getData().getInt("scanSpan", a2.c.scanSpan);
        if (a2.c.scanSpan < 1000) {
            r.a().c();
            this.a = false;
        } else {
            this.a = true;
        }
        LocationClientOption locationClientOption2 = a2.c;
        if (locationClientOption2.scanSpan > 999 && i < 1000) {
            if (locationClientOption2.mIsNeedDeviceDirect || locationClientOption2.isNeedAltitude) {
                r.a().a(a2.c.mIsNeedDeviceDirect);
                r.a().b();
            }
            if (this.b || a2.c.isNeedAltitude) {
                z = true;
            }
            this.b = z;
            z = true;
        }
        a2.c.openGps = message.getData().getBoolean("openGPS", a2.c.openGps);
        String string = message.getData().getString("coorType");
        LocationClientOption locationClientOption3 = a2.c;
        if (string == null || string.equals("")) {
            string = a2.c.coorType;
        }
        locationClientOption3.coorType = string;
        String string2 = message.getData().getString("addrType");
        LocationClientOption locationClientOption4 = a2.c;
        if (string2 == null || string2.equals("")) {
            string2 = a2.c.addrType;
        }
        locationClientOption4.addrType = string2;
        if (!com.baidu.location.e.j.g.equals(a2.c.addrType)) {
            o.c().j();
        }
        a2.c.timeOut = message.getData().getInt("timeOut", a2.c.timeOut);
        a2.c.location_change_notify = message.getData().getBoolean("location_change_notify", a2.c.location_change_notify);
        a2.c.priority = message.getData().getInt(com.xiaomi.stat.a.j.k, a2.c.priority);
        int i2 = message.getData().getInt("wifitimeout", Integer.MAX_VALUE);
        if (i2 < com.baidu.location.e.j.ag) {
            com.baidu.location.e.j.ag = i2;
        }
        e();
        return z;
    }

    public int d(Message message) {
        Messenger messenger;
        a a2;
        LocationClientOption locationClientOption;
        if (message == null || (messenger = message.replyTo) == null || (a2 = a(messenger)) == null || (locationClientOption = a2.c) == null) {
            return 1;
        }
        return locationClientOption.priority;
    }

    public void d() {
        Iterator<a> it = this.g.iterator();
        while (it.hasNext()) {
            it.next().a();
        }
    }

    public int e(Message message) {
        Messenger messenger;
        a a2;
        LocationClientOption locationClientOption;
        if (message == null || (messenger = message.replyTo) == null || (a2 = a(messenger)) == null || (locationClientOption = a2.c) == null) {
            return 1000;
        }
        return locationClientOption.scanSpan;
    }
}
