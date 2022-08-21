package com.baidu.location.c;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.telephony.CellIdentityNr;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint({"NewApi"})
/* loaded from: classes.dex */
public class b {
    public static int a;
    public static int b;
    private static b c;
    private static Class<?> k;
    private a l;
    private boolean o;
    private TelephonyManager d = null;
    private com.baidu.location.c.a e = new com.baidu.location.c.a();
    private com.baidu.location.c.a f = null;
    private List<com.baidu.location.c.a> g = null;
    private C0008b h = null;
    private boolean i = false;
    private boolean j = false;
    private long m = 0;
    private Handler n = new Handler();

    /* loaded from: classes.dex */
    public class a extends TelephonyManager.CellInfoCallback {
        private a() {
        }

        @Override // android.telephony.TelephonyManager.CellInfoCallback
        public void onCellInfo(List<CellInfo> list) {
            if (list == null) {
                return;
            }
            b.this.n.post(new c(this));
        }

        @Override // android.telephony.TelephonyManager.CellInfoCallback
        public void onError(int i, Throwable th) {
            if (th != null) {
                th.printStackTrace();
            }
        }
    }

    /* renamed from: com.baidu.location.c.b$b  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class C0008b extends PhoneStateListener {
        public C0008b() {
        }

        @Override // android.telephony.PhoneStateListener
        public void onCellInfoChanged(List<CellInfo> list) {
            if (list == null) {
                return;
            }
            b.this.n.post(new d(this));
        }

        @Override // android.telephony.PhoneStateListener
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            com.baidu.location.c.a aVar;
            int cdmaDbm;
            if (b.this.e != null) {
                if (b.this.e.i == 'g') {
                    aVar = b.this.e;
                    cdmaDbm = signalStrength.getGsmSignalStrength();
                } else if (b.this.e.i != 'c') {
                    return;
                } else {
                    aVar = b.this.e;
                    cdmaDbm = signalStrength.getCdmaDbm();
                }
                aVar.h = cdmaDbm;
            }
        }
    }

    private b() {
        this.o = false;
        if (Build.VERSION.SDK_INT >= 30) {
            this.o = com.baidu.location.e.j.b("android.telephony.TelephonyManager$CellInfoCallback");
        }
    }

    private static int a(int i) {
        if (i == Integer.MAX_VALUE) {
            return -1;
        }
        return i;
    }

    public static int a(CellIdentityNr cellIdentityNr) {
        try {
            return com.baidu.location.e.j.a(cellIdentityNr, "getHwTac");
        } catch (Throwable unused) {
            return -1;
        }
    }

    public static int a(String str) {
        if (str == null || !str.contains("mNrTac")) {
            return -1;
        }
        Matcher matcher = Pattern.compile("mNrTac=(.+?)\\}").matcher(str.replace(" ", ""));
        while (true) {
            int i = -1;
            while (matcher.find()) {
                if (matcher.groupCount() >= 1) {
                    try {
                        i = Integer.parseInt(matcher.group(1));
                    } catch (Throwable unused) {
                    }
                }
            }
            return i;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x00e4, code lost:
        if (r0 < 0) goto L103;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00e7, code lost:
        if (r0 > 0) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x012c, code lost:
        if (r2 >= 28) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x017f, code lost:
        if (android.os.Build.VERSION.SDK_INT >= 28) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0064, code lost:
        if (r2 >= 28) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0066, code lost:
        r5.j = r18.getCellConnectionStatus();
     */
    @android.annotation.SuppressLint({"NewApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.baidu.location.c.a a(android.telephony.CellInfo r18, com.baidu.location.c.a r19, android.telephony.TelephonyManager r20) {
        /*
            Method dump skipped, instructions count: 773
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.c.b.a(android.telephony.CellInfo, com.baidu.location.c.a, android.telephony.TelephonyManager):com.baidu.location.c.a");
    }

    private com.baidu.location.c.a a(CellLocation cellLocation) {
        return a(cellLocation, false);
    }

    private com.baidu.location.c.a a(CellLocation cellLocation, boolean z) {
        GsmCellLocation gsmCellLocation;
        if (cellLocation == null || this.d == null) {
            return null;
        }
        com.baidu.location.c.a aVar = new com.baidu.location.c.a();
        if (z) {
            aVar.f();
        }
        aVar.l = 1;
        aVar.g = System.currentTimeMillis();
        try {
            String networkOperator = this.d.getNetworkOperator();
            if (networkOperator != null && networkOperator.length() > 0) {
                int i = -1;
                if (networkOperator.length() >= 3) {
                    i = Integer.valueOf(networkOperator.substring(0, 3)).intValue();
                    aVar.c = i < 0 ? this.e.c : i;
                }
                String substring = networkOperator.substring(3);
                if (substring != null) {
                    char[] charArray = substring.toCharArray();
                    int i2 = 0;
                    while (i2 < charArray.length && Character.isDigit(charArray[i2])) {
                        i2++;
                    }
                    i = Integer.valueOf(substring.substring(0, i2)).intValue();
                }
                if (i < 0) {
                    i = this.e.d;
                }
                aVar.d = i;
            }
            a = this.d.getSimState();
        } catch (Exception unused) {
            b = 1;
        }
        if (cellLocation instanceof GsmCellLocation) {
            aVar.a = ((GsmCellLocation) cellLocation).getLac();
            aVar.b = gsmCellLocation.getCid();
            aVar.i = 'g';
        } else if (cellLocation instanceof CdmaCellLocation) {
            aVar.i = 'c';
            if (k == null) {
                try {
                    k = Class.forName("android.telephony.cdma.CdmaCellLocation");
                } catch (Exception unused2) {
                    k = null;
                    return aVar;
                }
            }
            Class<?> cls = k;
            if (cls != null && cls.isInstance(cellLocation)) {
                try {
                    int systemId = ((CdmaCellLocation) cellLocation).getSystemId();
                    if (systemId < 0) {
                        systemId = this.e.d;
                    }
                    aVar.d = systemId;
                    aVar.b = ((CdmaCellLocation) cellLocation).getBaseStationId();
                    aVar.a = ((CdmaCellLocation) cellLocation).getNetworkId();
                    int baseStationLatitude = ((CdmaCellLocation) cellLocation).getBaseStationLatitude();
                    if (baseStationLatitude < Integer.MAX_VALUE) {
                        aVar.e = baseStationLatitude;
                    }
                    int baseStationLongitude = ((CdmaCellLocation) cellLocation).getBaseStationLongitude();
                    if (baseStationLongitude < Integer.MAX_VALUE) {
                        aVar.f = baseStationLongitude;
                    }
                } catch (Exception unused3) {
                    b = 3;
                    return aVar;
                }
            }
        }
        c(aVar);
        return aVar;
    }

    @SuppressLint({"NewApi"})
    public static com.baidu.location.c.a a(com.baidu.location.c.a aVar, TelephonyManager telephonyManager) {
        if (Integer.valueOf(Build.VERSION.SDK_INT).intValue() < 17) {
            return null;
        }
        try {
            a = telephonyManager.getSimState();
            List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
            if (allCellInfo == null || allCellInfo.size() <= 0) {
                return null;
            }
            com.baidu.location.c.a aVar2 = null;
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo.isRegistered()) {
                    boolean z = false;
                    if (aVar2 != null) {
                        z = true;
                    }
                    com.baidu.location.c.a a2 = a(cellInfo, aVar, telephonyManager);
                    if (a2 != null) {
                        if (!a2.b()) {
                            a2 = null;
                        } else if (z && aVar2 != null) {
                            aVar2.m = a2.i();
                            return aVar2;
                        }
                        if (aVar2 == null) {
                            aVar2 = a2;
                        }
                    }
                }
            }
            return aVar2;
        } catch (Throwable unused) {
            return null;
        }
    }

    public static synchronized b a() {
        b bVar;
        synchronized (b.class) {
            if (c == null) {
                c = new b();
            }
            bVar = c;
        }
        return bVar;
    }

    private void c(com.baidu.location.c.a aVar) {
        if (aVar.b()) {
            com.baidu.location.c.a aVar2 = this.e;
            if (aVar2 != null && aVar2.a(aVar)) {
                return;
            }
            this.e = aVar;
            if (!aVar.b()) {
                List<com.baidu.location.c.a> list = this.g;
                if (list == null) {
                    return;
                }
                list.clear();
                return;
            }
            int size = this.g.size();
            com.baidu.location.c.a aVar3 = size == 0 ? null : this.g.get(size - 1);
            if (aVar3 != null) {
                long j = aVar3.b;
                com.baidu.location.c.a aVar4 = this.e;
                if (j == aVar4.b && aVar3.a == aVar4.a) {
                    return;
                }
            }
            this.g.add(this.e);
            if (this.g.size() > 3) {
                this.g.remove(0);
            }
            j();
            this.j = false;
        }
    }

    @SuppressLint({"NewApi"})
    private String d(com.baidu.location.c.a aVar) {
        com.baidu.location.c.a a2;
        StringBuilder sb;
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = null;
        if (Integer.valueOf(Build.VERSION.SDK_INT).intValue() >= 17) {
            try {
                List<CellInfo> allCellInfo = this.d.getAllCellInfo();
                if (allCellInfo != null && allCellInfo.size() > 0) {
                    sb2.append("&nc=");
                    for (CellInfo cellInfo : allCellInfo) {
                        if (!cellInfo.isRegistered() && (a2 = a(cellInfo, this.e, this.d)) != null) {
                            int i = a2.a;
                            if (i != -1 && a2.b != -1) {
                                if (aVar != null && aVar.a == i) {
                                    sb = new StringBuilder();
                                    sb.append("|");
                                    sb.append(a2.b);
                                    sb.append("|");
                                    sb.append(a2.h);
                                    sb.append(";");
                                    sb2.append(sb.toString());
                                }
                                sb = new StringBuilder();
                                sb.append(a2.a);
                                sb.append("|");
                                sb.append(a2.b);
                                sb.append("|");
                                sb.append(a2.h);
                                sb.append(";");
                                sb2.append(sb.toString());
                            }
                            if (Build.VERSION.SDK_INT > 28 && a2.k == 6 && a2.n != null && a2.b()) {
                                if (sb3 == null) {
                                    StringBuilder sb4 = new StringBuilder();
                                    try {
                                        sb4.append("&ncnr=");
                                        sb3 = sb4;
                                    } catch (Throwable unused) {
                                        sb3 = sb4;
                                    }
                                }
                                sb3.append(a2.g());
                                sb3.append("_");
                                sb3.append(a2.n);
                                sb3.append(";");
                            }
                        }
                    }
                }
            } catch (Throwable unused2) {
            }
        }
        if (sb3 != null) {
            return sb2.toString() + sb3.toString();
        }
        return sb2.toString();
    }

    private void i() {
        String g = com.baidu.location.e.j.g();
        if (g == null) {
            return;
        }
        File file = new File(g + File.separator + "lcvif2.dat");
        if (!file.exists()) {
            return;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            long j = 0;
            randomAccessFile.seek(0L);
            if (System.currentTimeMillis() - randomAccessFile.readLong() > 60000) {
                randomAccessFile.close();
                file.delete();
                return;
            }
            randomAccessFile.readInt();
            int i = 0;
            while (i < 3) {
                long readLong = randomAccessFile.readLong();
                int readInt = randomAccessFile.readInt();
                int readInt2 = randomAccessFile.readInt();
                int readInt3 = randomAccessFile.readInt();
                long readLong2 = randomAccessFile.readLong();
                int readInt4 = randomAccessFile.readInt();
                char c2 = readInt4 == 1 ? 'g' : (char) 0;
                if (readInt4 == 2) {
                    c2 = 'c';
                }
                if (readLong != j) {
                    com.baidu.location.c.a aVar = new com.baidu.location.c.a(readInt3, readLong2, readInt, readInt2, 0, c2, -1);
                    aVar.g = readLong;
                    if (aVar.b()) {
                        this.j = true;
                        this.g.add(aVar);
                    }
                }
                i++;
                j = 0;
            }
            randomAccessFile.close();
        } catch (Exception unused) {
            file.delete();
        }
    }

    private void j() {
        List<com.baidu.location.c.a> list = this.g;
        if (list == null && this.f == null) {
            return;
        }
        if (list == null && this.f != null) {
            LinkedList linkedList = new LinkedList();
            this.g = linkedList;
            linkedList.add(this.f);
        }
        String g = com.baidu.location.e.j.g();
        if (g == null || this.g == null) {
            return;
        }
        File file = new File(g + File.separator + "lcvif2.dat");
        int size = this.g.size();
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(0L);
            randomAccessFile.writeLong(this.g.get(size - 1).g);
            randomAccessFile.writeInt(size);
            for (int i = 0; i < 3 - size; i++) {
                randomAccessFile.writeLong(0L);
                randomAccessFile.writeInt(-1);
                randomAccessFile.writeInt(-1);
                randomAccessFile.writeInt(-1);
                randomAccessFile.writeLong(-1L);
                randomAccessFile.writeInt(2);
            }
            for (int i2 = 0; i2 < size; i2++) {
                randomAccessFile.writeLong(this.g.get(i2).g);
                randomAccessFile.writeInt(this.g.get(i2).c);
                randomAccessFile.writeInt(this.g.get(i2).d);
                randomAccessFile.writeInt(this.g.get(i2).a);
                randomAccessFile.writeLong(this.g.get(i2).b);
                if (this.g.get(i2).i == 'g') {
                    randomAccessFile.writeInt(1);
                } else if (this.g.get(i2).i == 'c') {
                    randomAccessFile.writeInt(2);
                } else {
                    randomAccessFile.writeInt(3);
                }
            }
            randomAccessFile.close();
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k() {
        CellLocation cellLocation;
        com.baidu.location.c.a a2 = a(this.e, this.d);
        if (a2 != null) {
            c(a2);
        }
        if (Build.VERSION.SDK_INT <= 28) {
            if (a2 != null && a2.b()) {
                return;
            }
            try {
                cellLocation = this.d.getCellLocation();
            } catch (Throwable unused) {
                cellLocation = null;
            }
            if (cellLocation == null) {
                return;
            }
            a(cellLocation);
        }
    }

    public String a(com.baidu.location.c.a aVar) {
        String d;
        int intValue;
        String str = "";
        try {
            d = d(aVar);
            intValue = Integer.valueOf(Build.VERSION.SDK_INT).intValue();
            if (d != null && !d.equals(str)) {
                if (!d.equals("&nc=")) {
                    return d;
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        if (intValue >= 17) {
            return d;
        }
        str = d;
        if (str != null && str.equals("&nc=")) {
            return null;
        }
        return str;
    }

    public String b(com.baidu.location.c.a aVar) {
        int i;
        StringBuffer stringBuffer = new StringBuffer(128);
        stringBuffer.append("&nw=");
        stringBuffer.append(aVar.i);
        stringBuffer.append(String.format(Locale.CHINA, "&cl=%d|%d|%d|%d&cl_s=%d", Integer.valueOf(aVar.c), Integer.valueOf(aVar.d), Integer.valueOf(aVar.a), Long.valueOf(aVar.b), Integer.valueOf(aVar.h)));
        if (aVar.e < Integer.MAX_VALUE && (i = aVar.f) < Integer.MAX_VALUE) {
            stringBuffer.append(String.format(Locale.CHINA, "&cdmall=%.6f|%.6f", Double.valueOf(i / 14400.0d), Double.valueOf(aVar.e / 14400.0d)));
        }
        stringBuffer.append("&cl_t=");
        stringBuffer.append(aVar.g);
        stringBuffer.append("&clp=");
        stringBuffer.append(aVar.k);
        stringBuffer.append("&cl_api=");
        stringBuffer.append(aVar.l);
        if (aVar.n != null) {
            stringBuffer.append("&clnrs=");
            stringBuffer.append(aVar.n);
        }
        if (Build.VERSION.SDK_INT >= 28 && aVar.j != Integer.MAX_VALUE) {
            stringBuffer.append("&cl_cs=");
            stringBuffer.append(aVar.j);
        }
        try {
            List<com.baidu.location.c.a> list = this.g;
            if (list != null && list.size() > 0) {
                int size = this.g.size();
                stringBuffer.append("&clt=");
                for (int i2 = 0; i2 < size; i2++) {
                    com.baidu.location.c.a aVar2 = this.g.get(i2);
                    if (aVar2 != null) {
                        int i3 = aVar2.c;
                        if (i3 != aVar.c) {
                            stringBuffer.append(i3);
                        }
                        stringBuffer.append("|");
                        int i4 = aVar2.d;
                        if (i4 != aVar.d) {
                            stringBuffer.append(i4);
                        }
                        stringBuffer.append("|");
                        int i5 = aVar2.a;
                        if (i5 != aVar.a) {
                            stringBuffer.append(i5);
                        }
                        stringBuffer.append("|");
                        long j = aVar2.b;
                        if (j != aVar.b) {
                            stringBuffer.append(j);
                        }
                        stringBuffer.append("|");
                        stringBuffer.append((System.currentTimeMillis() - aVar2.g) / 1000);
                        stringBuffer.append(";");
                    }
                }
            }
        } catch (Exception unused) {
        }
        if (a > 100) {
            a = 0;
        }
        stringBuffer.append("&cs=" + (a + (b << 8)));
        String str = aVar.m;
        if (str != null) {
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    public synchronized void b() {
        C0008b c0008b;
        if (this.i) {
            return;
        }
        if (!com.baidu.location.f.isServing) {
            return;
        }
        this.d = (TelephonyManager) com.baidu.location.f.getServiceContext().getSystemService("phone");
        this.g = new LinkedList();
        this.h = new C0008b();
        i();
        TelephonyManager telephonyManager = this.d;
        if (telephonyManager != null && (c0008b = this.h) != null) {
            if (Build.VERSION.SDK_INT < 30 || !this.o) {
                try {
                    telephonyManager.listen(c0008b, 1280);
                } catch (Exception unused) {
                }
            }
            this.i = true;
        }
    }

    public synchronized void c() {
        TelephonyManager telephonyManager;
        if (!this.i) {
            return;
        }
        C0008b c0008b = this.h;
        if (c0008b != null && (telephonyManager = this.d) != null) {
            telephonyManager.listen(c0008b, 0);
        }
        this.h = null;
        this.d = null;
        this.g.clear();
        this.g = null;
        j();
        this.i = false;
    }

    public boolean d() {
        return this.j;
    }

    public int e() {
        TelephonyManager telephonyManager = this.d;
        if (telephonyManager == null) {
            return 0;
        }
        try {
            return telephonyManager.getNetworkType();
        } catch (Exception unused) {
            return 0;
        }
    }

    public com.baidu.location.c.a f() {
        com.baidu.location.c.a aVar;
        com.baidu.location.c.a aVar2 = this.e;
        if ((aVar2 == null || !aVar2.a() || !this.e.b()) && this.d != null) {
            try {
                k();
                if (Build.VERSION.SDK_INT >= 30 && this.o && System.currentTimeMillis() - this.m > 30000) {
                    this.m = System.currentTimeMillis();
                    if (this.l == null) {
                        this.l = new a();
                    }
                    this.d.requestCellInfoUpdate(com.baidu.location.f.getServiceContext().getMainExecutor(), this.l);
                }
            } catch (Exception unused) {
            }
        }
        com.baidu.location.c.a aVar3 = this.e;
        if (aVar3 != null && aVar3.e()) {
            this.f = null;
            this.f = new com.baidu.location.c.a(this.e);
        }
        com.baidu.location.c.a aVar4 = this.e;
        if (aVar4 != null && aVar4.d() && (aVar = this.f) != null) {
            com.baidu.location.c.a aVar5 = this.e;
            if (aVar5.i == 'g') {
                aVar5.d = aVar.d;
                aVar5.c = aVar.c;
            }
        }
        return this.e;
    }

    public String g() {
        int i = -1;
        try {
            TelephonyManager telephonyManager = this.d;
            if (telephonyManager != null) {
                i = telephonyManager.getSimState();
            }
        } catch (Exception unused) {
        }
        return "&sim=" + i;
    }

    public int h() {
        return 0;
    }
}
