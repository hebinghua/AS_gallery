package com.baidu.location.c;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import java.util.List;

/* loaded from: classes.dex */
public class i {
    public static long a;
    private static i b;
    private WifiManager c = null;
    private a d = null;
    private h e = null;
    private long f = 0;
    private long g = 0;
    private boolean h = false;
    private ConnectivityManager i = null;
    private Handler j = new Handler();
    private boolean k = false;
    private long l = 0;
    private long m = 0;

    /* loaded from: classes.dex */
    public class a extends BroadcastReceiver {
        private long b;
        private boolean c;

        private a() {
            this.b = 0L;
            this.c = false;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (context == null) {
                return;
            }
            String action = intent.getAction();
            if (action.equals("android.net.wifi.SCAN_RESULTS")) {
                i.a = System.currentTimeMillis() / 1000;
                i.this.j.post(new j(this, intent.getBooleanExtra("resultsUpdated", true)));
            } else if (!action.equals("android.net.wifi.STATE_CHANGE") || !((NetworkInfo) intent.getParcelableExtra("networkInfo")).getState().equals(NetworkInfo.State.CONNECTED) || System.currentTimeMillis() - this.b < 5000) {
            } else {
                this.b = System.currentTimeMillis();
                if (this.c) {
                    return;
                }
                this.c = true;
            }
        }
    }

    private i() {
    }

    public static synchronized i a() {
        i iVar;
        synchronized (i.class) {
            if (b == null) {
                b = new i();
            }
            iVar = b;
        }
        return iVar;
    }

    private String a(long j) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.valueOf((int) (j & 255)));
        stringBuffer.append(CoreConstants.DOT);
        stringBuffer.append(String.valueOf((int) ((j >> 8) & 255)));
        stringBuffer.append(CoreConstants.DOT);
        stringBuffer.append(String.valueOf((int) ((j >> 16) & 255)));
        stringBuffer.append(CoreConstants.DOT);
        stringBuffer.append(String.valueOf((int) ((j >> 24) & 255)));
        return stringBuffer.toString();
    }

    public static boolean a(h hVar, h hVar2) {
        boolean a2 = a(hVar, hVar2, com.baidu.location.e.j.aA);
        long currentTimeMillis = System.currentTimeMillis() - com.baidu.location.b.b.c;
        if (currentTimeMillis <= 0 || currentTimeMillis >= 30000 || !a2 || hVar2.g() - hVar.g() <= 30) {
            return a2;
        }
        return false;
    }

    public static boolean a(h hVar, h hVar2, float f) {
        if (hVar != null && hVar2 != null) {
            List<ScanResult> list = hVar.a;
            List<ScanResult> list2 = hVar2.a;
            if (list == list2) {
                return true;
            }
            if (list != null && list2 != null) {
                int size = list.size();
                int size2 = list2.size();
                if (size == 0 && size2 == 0) {
                    return true;
                }
                if (size != 0 && size2 != 0) {
                    int i = 0;
                    for (int i2 = 0; i2 < size; i2++) {
                        String str = list.get(i2) != null ? list.get(i2).BSSID : null;
                        if (str != null) {
                            int i3 = 0;
                            while (true) {
                                if (i3 >= size2) {
                                    break;
                                }
                                String str2 = list2.get(i3) != null ? list2.get(i3).BSSID : null;
                                if (str2 != null && str.equals(str2)) {
                                    i++;
                                    break;
                                }
                                i3++;
                            }
                        }
                    }
                    if (i >= size * f) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q() {
        WifiManager wifiManager = this.c;
        if (wifiManager == null) {
            return;
        }
        try {
            List<ScanResult> scanResults = wifiManager.getScanResults();
            if (scanResults == null) {
                return;
            }
            h hVar = new h(scanResults, System.currentTimeMillis());
            h hVar2 = this.e;
            if (hVar2 != null && hVar.a(hVar2)) {
                return;
            }
            this.e = hVar;
        } catch (Exception unused) {
        }
    }

    public void b() {
        this.l = 0L;
    }

    public synchronized void c() {
        if (this.h) {
            return;
        }
        if (!com.baidu.location.f.isServing) {
            return;
        }
        this.c = (WifiManager) com.baidu.location.f.getServiceContext().getApplicationContext().getSystemService("wifi");
        this.d = new a();
        try {
            com.baidu.location.f.getServiceContext().registerReceiver(this.d, new IntentFilter("android.net.wifi.SCAN_RESULTS"));
        } catch (Exception unused) {
        }
        this.h = true;
    }

    public synchronized void d() {
        if (!this.h) {
            return;
        }
        try {
            com.baidu.location.f.getServiceContext().unregisterReceiver(this.d);
            a = 0L;
        } catch (Exception unused) {
        }
        this.d = null;
        this.c = null;
        this.h = false;
    }

    public boolean e() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = this.g;
        if (currentTimeMillis - j <= 0 || currentTimeMillis - j > 5000) {
            this.g = currentTimeMillis;
            b();
            return f();
        }
        return false;
    }

    public boolean f() {
        if (this.c == null) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long j = this.f;
        if (currentTimeMillis - j > 0) {
            long j2 = this.l;
            if (currentTimeMillis - j <= j2 + 5000 || currentTimeMillis - (a * 1000) <= j2 + 5000) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= 28 && currentTimeMillis - j < 25000) {
                return false;
            }
            if (i() && currentTimeMillis - this.f <= this.l + AbstractComponentTracker.LINGERING_TIMEOUT) {
                return false;
            }
        }
        return h();
    }

    @SuppressLint({"NewApi"})
    public String g() {
        WifiManager wifiManager = this.c;
        if (wifiManager != null) {
            try {
                if (!wifiManager.isWifiEnabled()) {
                    if (Build.VERSION.SDK_INT <= 17) {
                        return "";
                    }
                    if (!this.c.isScanAlwaysAvailable()) {
                        return "";
                    }
                }
                return "&wifio=1";
            } catch (Exception | NoSuchMethodError unused) {
                return "";
            }
        }
        return "";
    }

    @SuppressLint({"NewApi"})
    public boolean h() {
        long currentTimeMillis = System.currentTimeMillis() - this.m;
        if (currentTimeMillis < 0 || currentTimeMillis > 2000) {
            this.m = System.currentTimeMillis();
            try {
                if (!this.c.isWifiEnabled() && (Build.VERSION.SDK_INT <= 17 || !this.c.isScanAlwaysAvailable())) {
                    return false;
                }
                this.c.startScan();
                this.f = System.currentTimeMillis();
                return true;
            } catch (Exception | NoSuchMethodError unused) {
                return false;
            }
        }
        return false;
    }

    public boolean i() {
        try {
            if (this.i == null) {
                this.i = (ConnectivityManager) com.baidu.location.f.getServiceContext().getSystemService("connectivity");
            }
            ConnectivityManager connectivityManager = this.i;
            if (connectivityManager == null) {
                return false;
            }
            return connectivityManager.getNetworkInfo(1).isConnected();
        } catch (Error | Exception unused) {
            return false;
        }
    }

    @SuppressLint({"NewApi"})
    public boolean j() {
        try {
            if ((!this.c.isWifiEnabled() && (Build.VERSION.SDK_INT <= 17 || !this.c.isScanAlwaysAvailable())) || i()) {
                return false;
            }
            return new h(this.c.getScanResults(), 0L).e();
        } catch (Exception | NoSuchMethodError unused) {
            return false;
        }
    }

    public WifiInfo k() {
        WifiManager wifiManager = this.c;
        if (wifiManager == null) {
            return null;
        }
        try {
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && connectionInfo.getBSSID() != null && connectionInfo.getRssi() > -100) {
                String bssid = connectionInfo.getBSSID();
                if (bssid != null) {
                    String replace = bssid.replace(":", "");
                    if (!"000000000000".equals(replace) && !"".equals(replace)) {
                        if (replace.equals("020000000000")) {
                        }
                    }
                    return null;
                }
                return connectionInfo;
            }
        } catch (Error | Exception unused) {
        }
        return null;
    }

    public String l() {
        StringBuffer stringBuffer = new StringBuffer();
        WifiInfo k = a().k();
        if (k != null && k.getBSSID() != null) {
            String replace = k.getBSSID().replace(":", "");
            int rssi = k.getRssi();
            String m = a().m();
            if (rssi < 0) {
                rssi = -rssi;
            }
            if (replace != null && rssi < 100 && !replace.equals("020000000000")) {
                stringBuffer.append("&wf=");
                stringBuffer.append(replace);
                stringBuffer.append(";");
                stringBuffer.append("" + rssi + ";");
                String ssid = k.getSSID();
                if (ssid != null && (ssid.contains("&") || ssid.contains(";"))) {
                    ssid = ssid.replace("&", "_");
                }
                stringBuffer.append(ssid);
                stringBuffer.append("&wf_n=1");
                if (m != null) {
                    stringBuffer.append("&wf_gw=");
                    stringBuffer.append(m);
                }
                return stringBuffer.toString();
            }
        }
        return null;
    }

    public String m() {
        DhcpInfo dhcpInfo;
        WifiManager wifiManager = this.c;
        if (wifiManager == null || (dhcpInfo = wifiManager.getDhcpInfo()) == null) {
            return null;
        }
        return a(dhcpInfo.gateway);
    }

    public h n() {
        h hVar = this.e;
        return (hVar == null || !hVar.j()) ? p() : this.e;
    }

    public h o() {
        h hVar = this.e;
        return (hVar == null || !hVar.k()) ? p() : this.e;
    }

    public h p() {
        WifiManager wifiManager = this.c;
        if (wifiManager != null) {
            try {
                return new h(wifiManager.getScanResults(), this.f);
            } catch (Exception unused) {
            }
        }
        return new h(null, 0L);
    }
}
