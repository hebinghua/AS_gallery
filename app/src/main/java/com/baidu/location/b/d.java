package com.baidu.location.b;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import com.baidu.lbsapi.auth.LBSAuthManager;
import com.baidu.location.BDLocation;
import com.baidu.location.Jni;
import com.baidu.location.LocationClientOption;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class d {
    private static char[] q = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.".toCharArray();
    public String a;
    public String b;
    private Context d;
    private TelephonyManager e;
    private WifiManager g;
    private String i;
    private String j;
    private LocationClientOption k;
    private a l;
    private String n;
    private String o;
    private boolean p;
    private com.baidu.location.c.a f = new com.baidu.location.c.a();
    private C0007d h = null;
    private String m = null;
    public b c = new b();
    private boolean r = false;
    private long s = 0;
    private boolean t = false;

    /* loaded from: classes.dex */
    public interface a {
        void onReceiveLocation(BDLocation bDLocation);
    }

    /* loaded from: classes.dex */
    public class b extends com.baidu.location.e.e {
        public LocationManager b;
        public a c;
        public String a = null;
        public boolean d = false;

        /* loaded from: classes.dex */
        public class a implements LocationListener {
            private a() {
            }

            public /* synthetic */ a(b bVar, e eVar) {
                this();
            }

            @Override // android.location.LocationListener
            public void onLocationChanged(Location location) {
                b.this.c();
                b.this.d = true;
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

        public b() {
            this.j = new HashMap();
        }

        private void b() {
            try {
                this.b = (LocationManager) d.this.d.getSystemService("location");
                a aVar = new a(this, null);
                this.c = aVar;
                LocationManager locationManager = this.b;
                if (locationManager == null) {
                    return;
                }
                try {
                    locationManager.requestLocationUpdates("network", 1000L, 0.0f, aVar, Looper.getMainLooper());
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            } catch (Throwable unused) {
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void c() {
            LocationManager locationManager;
            a aVar = this.c;
            if (aVar == null || (locationManager = this.b) == null) {
                return;
            }
            try {
                locationManager.removeUpdates(aVar);
            } catch (Exception unused) {
            }
        }

        @Override // com.baidu.location.e.e
        public void a() {
            this.g = com.baidu.location.e.j.d();
            if (d.this.n != null && d.this.o != null) {
                this.a += String.format(Locale.CHINA, "&ki=%s&sn=%s", d.this.n, d.this.o);
            }
            String str = this.a + "&enc=2";
            this.a = str;
            String encodeTp4 = Jni.encodeTp4(str);
            this.a = null;
            this.j.put("bloc", encodeTp4);
            this.j.put("trtm", String.format(Locale.CHINA, "%d", Long.valueOf(System.currentTimeMillis())));
        }

        public void a(String str) {
            this.a = str;
            b(com.baidu.location.e.j.f);
            if (d.this.r) {
                b();
                Timer timer = new Timer();
                timer.schedule(new f(this, timer), AbstractComponentTracker.LINGERING_TIMEOUT);
                SharedPreferences.Editor edit = d.this.d.getSharedPreferences("cuidRelate", 0).edit();
                edit.putLong("reqtime", System.currentTimeMillis());
                edit.apply();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:4:0x0008, code lost:
            r6 = r5.i;
         */
        @Override // com.baidu.location.e.e
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void a(boolean r6) {
            /*
                r5 = this;
                java.lang.String r0 = ";"
                java.lang.String r1 = "enc"
                r2 = 63
                if (r6 == 0) goto La9
                java.lang.String r6 = r5.i
                if (r6 == 0) goto La9
                java.lang.String r3 = "\"enc\""
                boolean r3 = r6.contains(r3)     // Catch: java.lang.Exception -> L9f
                if (r3 == 0) goto L30
                org.json.JSONObject r3 = new org.json.JSONObject     // Catch: java.lang.Exception -> L2c
                r3.<init>(r6)     // Catch: java.lang.Exception -> L2c
                boolean r4 = r3.has(r1)     // Catch: java.lang.Exception -> L2c
                if (r4 == 0) goto L30
                java.lang.String r1 = r3.getString(r1)     // Catch: java.lang.Exception -> L2c
                com.baidu.location.b.m r3 = com.baidu.location.b.m.a()     // Catch: java.lang.Exception -> L2c
                java.lang.String r6 = r3.b(r1)     // Catch: java.lang.Exception -> L2c
                goto L30
            L2c:
                r1 = move-exception
                r1.printStackTrace()     // Catch: java.lang.Exception -> L9f
            L30:
                com.baidu.location.BDLocation r1 = new com.baidu.location.BDLocation     // Catch: java.lang.Exception -> L3b
                r1.<init>(r6)     // Catch: java.lang.Exception -> L3b
                com.baidu.location.b.d r3 = com.baidu.location.b.d.this     // Catch: java.lang.Exception -> L3b
                com.baidu.location.b.d.a(r3, r6)     // Catch: java.lang.Exception -> L3b
                goto L43
            L3b:
                com.baidu.location.BDLocation r1 = new com.baidu.location.BDLocation     // Catch: java.lang.Exception -> L9f
                r1.<init>()     // Catch: java.lang.Exception -> L9f
                r1.setLocType(r2)     // Catch: java.lang.Exception -> L9f
            L43:
                int r6 = r1.getLocType()     // Catch: java.lang.Exception -> L9f
                r3 = 161(0xa1, float:2.26E-43)
                if (r6 != r3) goto L95
                com.baidu.location.b.d r6 = com.baidu.location.b.d.this     // Catch: java.lang.Exception -> L9f
                com.baidu.location.LocationClientOption r6 = com.baidu.location.b.d.c(r6)     // Catch: java.lang.Exception -> L9f
                java.lang.String r6 = r6.coorType     // Catch: java.lang.Exception -> L9f
                r1.setCoorType(r6)     // Catch: java.lang.Exception -> L9f
                java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L9f
                r6.<init>()     // Catch: java.lang.Exception -> L9f
                com.baidu.location.b.d r3 = com.baidu.location.b.d.this     // Catch: java.lang.Exception -> L9f
                java.lang.String r3 = r3.a     // Catch: java.lang.Exception -> L9f
                r6.append(r3)     // Catch: java.lang.Exception -> L9f
                r6.append(r0)     // Catch: java.lang.Exception -> L9f
                com.baidu.location.b.d r3 = com.baidu.location.b.d.this     // Catch: java.lang.Exception -> L9f
                java.lang.String r3 = r3.b     // Catch: java.lang.Exception -> L9f
                r6.append(r3)     // Catch: java.lang.Exception -> L9f
                r6.append(r0)     // Catch: java.lang.Exception -> L9f
                java.lang.String r0 = r1.getTime()     // Catch: java.lang.Exception -> L9f
                r6.append(r0)     // Catch: java.lang.Exception -> L9f
                java.lang.String r6 = r6.toString()     // Catch: java.lang.Exception -> L9f
                java.lang.String r6 = com.baidu.location.Jni.en1(r6)     // Catch: java.lang.Exception -> L9f
                r1.setLocationID(r6)     // Catch: java.lang.Exception -> L9f
                r6 = 0
                r1.setRoadLocString(r6, r6)     // Catch: java.lang.Exception -> L9f
                com.baidu.location.b.d r6 = com.baidu.location.b.d.this     // Catch: java.lang.Exception -> L9f
                r0 = 1
                com.baidu.location.b.d.a(r6, r0)     // Catch: java.lang.Exception -> L9f
                com.baidu.location.b.d r6 = com.baidu.location.b.d.this     // Catch: java.lang.Exception -> L9f
                com.baidu.location.b.d$a r6 = com.baidu.location.b.d.d(r6)     // Catch: java.lang.Exception -> L9f
                r6.onReceiveLocation(r1)     // Catch: java.lang.Exception -> L9f
                goto Lae
            L95:
                com.baidu.location.b.d r6 = com.baidu.location.b.d.this     // Catch: java.lang.Exception -> L9f
                int r0 = r1.getLocType()     // Catch: java.lang.Exception -> L9f
                com.baidu.location.b.d.a(r6, r0)     // Catch: java.lang.Exception -> L9f
                goto Lae
            L9f:
                r6 = move-exception
                com.baidu.location.b.d r0 = com.baidu.location.b.d.this
                com.baidu.location.b.d.a(r0, r2)
                r6.printStackTrace()
                goto Lae
            La9:
                com.baidu.location.b.d r6 = com.baidu.location.b.d.this
                com.baidu.location.b.d.a(r6, r2)
            Lae:
                java.util.Map<java.lang.String, java.lang.Object> r6 = r5.j
                if (r6 == 0) goto Lb5
                r6.clear()
            Lb5:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.b.d.b.a(boolean):void");
        }
    }

    /* loaded from: classes.dex */
    public class c {
        public String a;
        public int b;

        public c(String str, int i) {
            this.a = str;
            this.b = i;
        }
    }

    /* renamed from: com.baidu.location.b.d$d  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static class C0007d {
        public List<ScanResult> a;
        private long c;
        public String b = null;
        private String d = null;
        private int e = 16;

        public C0007d(List<ScanResult> list) {
            this.a = null;
            this.c = 0L;
            this.a = list;
            this.c = System.currentTimeMillis();
            try {
                b();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*  JADX ERROR: JadxOverflowException in pass: LoopRegionVisitor
            jadx.core.utils.exceptions.JadxOverflowException: LoopRegionVisitor.assignOnlyInLoop endless recursion
            	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:56)
            	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:30)
            	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:18)
            */
        private void b() {
            /*
                r7 = this;
                int r0 = r7.a()
                r1 = 1
                if (r0 >= r1) goto L8
                return
            L8:
                java.util.List<android.net.wifi.ScanResult> r0 = r7.a
                int r0 = r0.size()
                int r0 = r0 - r1
                r2 = r1
            L10:
                if (r0 < r1) goto L5e
                if (r2 == 0) goto L5e
                r2 = 0
                r3 = r2
            L16:
                if (r2 >= r0) goto L5a
                java.util.List<android.net.wifi.ScanResult> r4 = r7.a
                java.lang.Object r4 = r4.get(r2)
                if (r4 == 0) goto L57
                java.util.List<android.net.wifi.ScanResult> r4 = r7.a
                int r5 = r2 + 1
                java.lang.Object r4 = r4.get(r5)
                if (r4 == 0) goto L57
                java.util.List<android.net.wifi.ScanResult> r4 = r7.a
                java.lang.Object r4 = r4.get(r2)
                android.net.wifi.ScanResult r4 = (android.net.wifi.ScanResult) r4
                int r4 = r4.level
                java.util.List<android.net.wifi.ScanResult> r6 = r7.a
                java.lang.Object r6 = r6.get(r5)
                android.net.wifi.ScanResult r6 = (android.net.wifi.ScanResult) r6
                int r6 = r6.level
                if (r4 >= r6) goto L57
                java.util.List<android.net.wifi.ScanResult> r3 = r7.a
                java.lang.Object r3 = r3.get(r5)
                android.net.wifi.ScanResult r3 = (android.net.wifi.ScanResult) r3
                java.util.List<android.net.wifi.ScanResult> r4 = r7.a
                java.lang.Object r6 = r4.get(r2)
                r4.set(r5, r6)
                java.util.List<android.net.wifi.ScanResult> r4 = r7.a
                r4.set(r2, r3)
                r3 = r1
            L57:
                int r2 = r2 + 1
                goto L16
            L5a:
                int r0 = r0 + (-1)
                r2 = r3
                goto L10
            L5e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.b.d.C0007d.b():void");
        }

        public int a() {
            List<ScanResult> list = this.a;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        /* JADX WARN: Removed duplicated region for block: B:18:0x0053  */
        /* JADX WARN: Removed duplicated region for block: B:50:0x00f3  */
        /* JADX WARN: Removed duplicated region for block: B:52:0x00f8  */
        /* JADX WARN: Removed duplicated region for block: B:96:0x00eb A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.lang.String a(int r26, java.lang.String r27, boolean r28, int r29) {
            /*
                Method dump skipped, instructions count: 473
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.b.d.C0007d.a(int, java.lang.String, boolean, int):java.lang.String");
        }
    }

    public d(Context context, LocationClientOption locationClientOption, a aVar, String str) {
        StringBuilder sb;
        String str2 = null;
        this.d = null;
        this.e = null;
        this.g = null;
        this.i = null;
        this.j = null;
        this.n = null;
        this.o = null;
        this.a = null;
        this.b = null;
        this.p = false;
        Context applicationContext = context.getApplicationContext();
        this.d = applicationContext;
        try {
            com.baidu.location.e.j.ax = applicationContext.getPackageName();
        } catch (Exception unused) {
        }
        this.p = true;
        this.k = new LocationClientOption(locationClientOption);
        this.l = aVar;
        this.a = this.d.getPackageName();
        this.b = null;
        try {
            this.e = (TelephonyManager) this.d.getSystemService("phone");
            this.g = (WifiManager) this.d.getApplicationContext().getSystemService("wifi");
        } catch (Exception unused2) {
        }
        this.j = "&" + this.a + "&" + ((String) null);
        try {
            this.b = LBSAuthManager.getInstance(this.d).getCUID();
        } catch (Throwable unused3) {
            this.b = null;
            this.e = null;
            this.g = null;
        }
        if (this.b != null) {
            com.baidu.location.e.j.o = "" + this.b;
            sb = new StringBuilder();
            sb.append("&prod=");
            sb.append(this.k.prodName);
            sb.append(":");
            sb.append(this.a);
            sb.append("|&cu=");
            str2 = this.b;
        } else {
            sb = new StringBuilder();
            sb.append("&prod=");
            sb.append(this.k.prodName);
            sb.append(":");
            sb.append(this.a);
            sb.append("|&im=");
        }
        sb.append(str2);
        sb.append("&coor=");
        sb.append(locationClientOption.getCoorType());
        this.i = sb.toString();
        StringBuffer stringBuffer = new StringBuffer(256);
        stringBuffer.append("&fw=");
        stringBuffer.append("9.16");
        stringBuffer.append("&sdk=");
        stringBuffer.append("9.16");
        stringBuffer.append("&lt=1");
        stringBuffer.append("&mb=");
        stringBuffer.append(Build.MODEL);
        stringBuffer.append("&resid=");
        stringBuffer.append("12");
        locationClientOption.getAddrType();
        if (locationClientOption.getAddrType() != null && locationClientOption.getAddrType().equals("all")) {
            this.i += "&addr=allj2";
            if (locationClientOption.isNeedNewVersionRgc) {
                stringBuffer.append("&adtp=n2");
            }
        }
        if (locationClientOption.isNeedAptag || locationClientOption.isNeedAptagd) {
            this.i += "&sema=";
            if (locationClientOption.isNeedAptag) {
                this.i += "aptag|";
            }
            if (locationClientOption.isNeedAptagd) {
                this.i += "aptagd2|";
            }
            this.n = com.baidu.location.a.a.b(this.d);
            this.o = com.baidu.location.a.a.c(this.d);
        }
        stringBuffer.append("&first=1");
        stringBuffer.append("&os=A");
        stringBuffer.append(Build.VERSION.SDK);
        this.i += stringBuffer.toString();
    }

    public static com.baidu.location.c.a a(CellLocation cellLocation, TelephonyManager telephonyManager, com.baidu.location.c.a aVar) {
        GsmCellLocation gsmCellLocation;
        if (cellLocation == null || telephonyManager == null) {
            return null;
        }
        com.baidu.location.c.a aVar2 = new com.baidu.location.c.a();
        aVar2.l = 1;
        String networkOperator = telephonyManager.getNetworkOperator();
        if (networkOperator != null && networkOperator.length() > 0) {
            try {
                if (networkOperator.length() >= 3) {
                    int intValue = Integer.valueOf(networkOperator.substring(0, 3)).intValue();
                    if (intValue < 0) {
                        intValue = aVar.c;
                    }
                    aVar2.c = intValue;
                }
                String substring = networkOperator.substring(3);
                if (substring != null) {
                    char[] charArray = substring.toCharArray();
                    int i = 0;
                    while (i < charArray.length && Character.isDigit(charArray[i])) {
                        i++;
                    }
                    int intValue2 = Integer.valueOf(substring.substring(0, i)).intValue();
                    if (intValue2 < 0) {
                        intValue2 = aVar.d;
                    }
                    aVar2.d = intValue2;
                }
            } catch (Exception unused) {
            }
        }
        if (cellLocation instanceof GsmCellLocation) {
            aVar2.a = ((GsmCellLocation) cellLocation).getLac();
            aVar2.b = gsmCellLocation.getCid();
            aVar2.i = 'g';
        } else if (cellLocation instanceof CdmaCellLocation) {
            aVar2.i = 'c';
            try {
                if (Class.forName("android.telephony.cdma.CdmaCellLocation").isInstance(cellLocation)) {
                    try {
                        int systemId = ((CdmaCellLocation) cellLocation).getSystemId();
                        if (systemId < 0) {
                            systemId = -1;
                        }
                        aVar2.d = systemId;
                        aVar2.b = ((CdmaCellLocation) cellLocation).getBaseStationId();
                        aVar2.a = ((CdmaCellLocation) cellLocation).getNetworkId();
                        int baseStationLatitude = ((CdmaCellLocation) cellLocation).getBaseStationLatitude();
                        if (baseStationLatitude < Integer.MAX_VALUE) {
                            aVar2.e = baseStationLatitude;
                        }
                        int baseStationLongitude = ((CdmaCellLocation) cellLocation).getBaseStationLongitude();
                        if (baseStationLongitude < Integer.MAX_VALUE) {
                            aVar2.f = baseStationLongitude;
                        }
                    } catch (Exception unused2) {
                    }
                }
            } catch (Exception unused3) {
                return null;
            }
        }
        if (aVar2.b()) {
            return aVar2;
        }
        return null;
    }

    private Object a(Object obj, String str) throws Exception {
        return obj.getClass().getField(str).get(obj);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(18:1|(2:2|3)|(15:5|(1:7)|8|9|(1:56)(1:13)|15|16|(1:22)|24|25|(5:27|28|29|(1:33)|(2:(1:(1:42)(1:43))|(1:45)(4:46|(1:48)|49|50))(2:37|38))|53|(0)|(0)|(0)(0))|57|(2:59|8)|9|(1:11)|56|15|16|(3:18|20|22)|24|25|(0)|53|(0)|(0)|(0)(0)) */
    /* JADX WARN: Can't wrap try/catch for region: R(19:1|2|3|(15:5|(1:7)|8|9|(1:56)(1:13)|15|16|(1:22)|24|25|(5:27|28|29|(1:33)|(2:(1:(1:42)(1:43))|(1:45)(4:46|(1:48)|49|50))(2:37|38))|53|(0)|(0)|(0)(0))|57|(2:59|8)|9|(1:11)|56|15|16|(3:18|20|22)|24|25|(0)|53|(0)|(0)|(0)(0)) */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0069 A[Catch: Exception -> 0x0098, TRY_LEAVE, TryCatch #1 {Exception -> 0x0098, blocks: (B:28:0x005f, B:30:0x0069), top: B:58:0x005f }] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x009b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00b7 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00b8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String a(int r7) {
        /*
            r6 = this;
            r0 = 0
            com.baidu.location.c.a r1 = r6.f     // Catch: java.lang.Throwable -> L5e
            android.telephony.TelephonyManager r2 = r6.e     // Catch: java.lang.Throwable -> L5e
            com.baidu.location.c.a r1 = com.baidu.location.c.b.a(r1, r2)     // Catch: java.lang.Throwable -> L5e
            if (r1 == 0) goto L15
            boolean r2 = r1.b()     // Catch: java.lang.Throwable -> L5e
            if (r2 != 0) goto L12
            goto L15
        L12:
            r6.f = r1     // Catch: java.lang.Throwable -> L5e
            goto L2a
        L15:
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.Throwable -> L5e
            r2 = 28
            if (r1 > r2) goto L2a
            android.telephony.TelephonyManager r1 = r6.e     // Catch: java.lang.Throwable -> L5e
            android.telephony.CellLocation r1 = r1.getCellLocation()     // Catch: java.lang.Throwable -> L5e
            android.telephony.TelephonyManager r2 = r6.e     // Catch: java.lang.Throwable -> L5e
            com.baidu.location.c.a r3 = r6.f     // Catch: java.lang.Throwable -> L5e
            com.baidu.location.c.a r1 = a(r1, r2, r3)     // Catch: java.lang.Throwable -> L5e
            goto L12
        L2a:
            com.baidu.location.c.a r1 = r6.f     // Catch: java.lang.Throwable -> L5e
            if (r1 == 0) goto L3b
            boolean r1 = r1.b()     // Catch: java.lang.Throwable -> L5e
            if (r1 == 0) goto L3b
            com.baidu.location.c.a r1 = r6.f     // Catch: java.lang.Throwable -> L5e
            java.lang.String r1 = r1.h()     // Catch: java.lang.Throwable -> L5e
            goto L3c
        L3b:
            r1 = r0
        L3c:
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Throwable -> L5f
            if (r2 != 0) goto L5f
            com.baidu.location.c.a r2 = r6.f     // Catch: java.lang.Throwable -> L5f
            if (r2 == 0) goto L5f
            java.lang.String r2 = r2.m     // Catch: java.lang.Throwable -> L5f
            if (r2 == 0) goto L5f
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L5f
            r2.<init>()     // Catch: java.lang.Throwable -> L5f
            r2.append(r1)     // Catch: java.lang.Throwable -> L5f
            com.baidu.location.c.a r3 = r6.f     // Catch: java.lang.Throwable -> L5f
            java.lang.String r3 = r3.m     // Catch: java.lang.Throwable -> L5f
            r2.append(r3)     // Catch: java.lang.Throwable -> L5f
            java.lang.String r1 = r2.toString()     // Catch: java.lang.Throwable -> L5f
            goto L5f
        L5e:
            r1 = r0
        L5f:
            r6.h = r0     // Catch: java.lang.Exception -> L98
            android.net.wifi.WifiManager r2 = r6.g     // Catch: java.lang.Exception -> L98
            boolean r2 = a(r2)     // Catch: java.lang.Exception -> L98
            if (r2 == 0) goto L98
            com.baidu.location.b.d$d r2 = new com.baidu.location.b.d$d     // Catch: java.lang.Exception -> L98
            android.net.wifi.WifiManager r3 = r6.g     // Catch: java.lang.Exception -> L98
            java.util.List r3 = r3.getScanResults()     // Catch: java.lang.Exception -> L98
            r2.<init>(r3)     // Catch: java.lang.Exception -> L98
            r6.h = r2     // Catch: java.lang.Exception -> L98
            android.net.wifi.WifiManager r3 = r6.g     // Catch: java.lang.Exception -> L98
            java.lang.String r3 = b(r3)     // Catch: java.lang.Exception -> L98
            boolean r4 = r6.r     // Catch: java.lang.Exception -> L98
            com.baidu.location.b.a r5 = com.baidu.location.b.a.a()     // Catch: java.lang.Exception -> L98
            int r5 = r5.b     // Catch: java.lang.Exception -> L98
            java.lang.String r7 = r2.a(r7, r3, r4, r5)     // Catch: java.lang.Exception -> L98
            com.baidu.location.LocationClientOption r2 = r6.k     // Catch: java.lang.Exception -> L99
            if (r2 == 0) goto L99
            boolean r2 = r2.isOnceLocation()     // Catch: java.lang.Exception -> L99
            if (r2 == 0) goto L99
            android.net.wifi.WifiManager r2 = r6.g     // Catch: java.lang.Exception -> L99
            r2.startScan()     // Catch: java.lang.Exception -> L99
            goto L99
        L98:
            r7 = r0
        L99:
            if (r1 != 0) goto La0
            if (r7 != 0) goto La0
            r6.m = r0
            return r0
        La0:
            if (r7 == 0) goto Lb5
            if (r1 != 0) goto La6
            r1 = r7
            goto Lb5
        La6:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r1)
            r2.append(r7)
            java.lang.String r1 = r2.toString()
        Lb5:
            if (r1 != 0) goto Lb8
            return r0
        Lb8:
            r6.m = r1
            java.lang.String r7 = r6.i
            if (r7 == 0) goto Ld3
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r0 = r6.m
            r7.append(r0)
            java.lang.String r0 = r6.i
            r7.append(r0)
            java.lang.String r7 = r7.toString()
            r6.m = r7
        Ld3:
            r6.e()
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            r7.append(r1)
            java.lang.String r0 = r6.i
            r7.append(r0)
            java.lang.String r7 = r7.toString()
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.b.d.a(int):java.lang.String");
    }

    private String a(List<WifiConfiguration> list) {
        ArrayList<c> arrayList;
        int i;
        int i2 = 0;
        if (list == null || list.size() <= 0) {
            arrayList = null;
        } else {
            arrayList = new ArrayList();
            for (WifiConfiguration wifiConfiguration : list) {
                String str = wifiConfiguration.SSID;
                try {
                    i = ((Integer) a(wifiConfiguration, "numAssociation")).intValue();
                } catch (Throwable unused) {
                    i = 0;
                }
                if (i > 0 && !TextUtils.isEmpty(str)) {
                    arrayList.add(new c(str, i));
                }
            }
        }
        if (arrayList == null || arrayList.isEmpty()) {
            return null;
        }
        if (arrayList.size() > 1) {
            Collections.sort(arrayList, new e(this));
        }
        StringBuffer stringBuffer = new StringBuffer(200);
        for (c cVar : arrayList) {
            stringBuffer.append(cVar.a);
            stringBuffer.append(",");
            stringBuffer.append(cVar.b);
            stringBuffer.append("|");
            i2++;
            if (i2 == 4) {
                break;
            }
        }
        if (arrayList.size() >= 5) {
            stringBuffer.append(((c) arrayList.get(4)).a);
            stringBuffer.append(",");
            stringBuffer.append(((c) arrayList.get(4)).b);
        }
        return stringBuffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        String[] split;
        try {
            JSONObject jSONObject = new JSONObject(str).getJSONObject(MiStat.Param.CONTENT);
            String str2 = null;
            if (jSONObject.has("ideocfre")) {
                str2 = jSONObject.getString("ideocfre");
            }
            if (TextUtils.isEmpty(str2) || !str2.contains("|") || (split = str2.split("\\|")) == null || split.length < 2) {
                return;
            }
            int parseInt = Integer.parseInt(split[0]);
            long parseLong = Long.parseLong(split[1]);
            SharedPreferences.Editor edit = this.d.getSharedPreferences("cuidRelate", 0).edit();
            edit.putInt("cuidoc", parseInt);
            edit.putLong("cuidfreq", parseLong);
            edit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean a(WifiManager wifiManager) {
        try {
            if (!wifiManager.isWifiEnabled()) {
                if (Build.VERSION.SDK_INT <= 17) {
                    return false;
                }
                if (!wifiManager.isScanAlwaysAvailable()) {
                    return false;
                }
            }
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static String b(WifiManager wifiManager) {
        if (wifiManager == null) {
            return null;
        }
        try {
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo == null) {
                return null;
            }
            String bssid = connectionInfo.getBSSID();
            String replace = bssid != null ? bssid.replace(":", "") : null;
            if (replace != null && replace.length() != 12) {
                return null;
            }
            return new String(replace);
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(int i) {
        if (this.k.isOnceLocation()) {
            BDLocation bDLocation = new BDLocation();
            bDLocation.setLocType(i);
            a aVar = this.l;
            if (aVar == null) {
                return;
            }
            aVar.onReceiveLocation(bDLocation);
        }
    }

    private boolean d() {
        if (com.baidu.location.b.a.a().d == 0) {
            return false;
        }
        SharedPreferences sharedPreferences = this.d.getApplicationContext().getSharedPreferences("cuidRelate", 0);
        if (!sharedPreferences.contains("isInstalled")) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            if (!com.baidu.location.e.j.b(this.d, "com.baidu.map.location")) {
                edit.putInt("isInstalled", 0);
                return false;
            }
            edit.putInt("isInstalled", 1);
            edit.apply();
        } else if (sharedPreferences.getInt("isInstalled", -1) == 0) {
            return false;
        }
        return sharedPreferences.getInt("cuidoc", 1) != 0 && (System.currentTimeMillis() - sharedPreferences.getLong("reqtime", 0L)) / 1000 >= sharedPreferences.getLong("cuidfreq", 60L) && com.baidu.location.e.j.b(this.d) >= 2 && a(this.g) && this.h.a() > 3;
    }

    private void e() {
        String a2;
        if (!d()) {
            this.r = false;
            return;
        }
        this.r = true;
        String str = null;
        if (this.h.a() >= 10) {
            String a3 = this.h.a(9, b(this.g), this.r, com.baidu.location.b.a.a().b);
            if (!TextUtils.isEmpty(a3)) {
                a2 = com.baidu.location.e.j.a(a3.getBytes(), false);
            }
            a2 = null;
        } else {
            C0007d c0007d = this.h;
            String a4 = c0007d.a(c0007d.a(), b(this.g), this.r, com.baidu.location.b.a.a().b);
            if (!TextUtils.isEmpty(a4)) {
                a2 = com.baidu.location.e.j.a(a4.getBytes(), false);
            }
            a2 = null;
        }
        String a5 = a(f());
        if (!TextUtils.isEmpty(a5)) {
            str = com.baidu.location.e.j.a(a5.getBytes(), false);
        }
        if (!TextUtils.isEmpty(a2)) {
            this.m += "&swf5=" + a2;
            this.r = true;
        } else {
            this.r = false;
        }
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.m += "&hwf5=" + str;
        this.r = true;
    }

    private List<WifiConfiguration> f() {
        try {
            WifiManager wifiManager = this.g;
            if (wifiManager == null) {
                return null;
            }
            return wifiManager.getConfiguredNetworks();
        } catch (Exception unused) {
            return null;
        }
    }

    public void a() {
        b();
    }

    public String b() {
        try {
            return a(15);
        } catch (Exception unused) {
            return null;
        }
    }

    public void c() {
        String str = this.m;
        if (str == null) {
            b(62);
        } else if (!this.p) {
        } else {
            this.c.a(str);
        }
    }
}
