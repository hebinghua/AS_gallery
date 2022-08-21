package com.baidu.location.b;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.Base64;
import com.baidu.lbsapi.auth.LBSAuthManager;
import com.baidu.location.BDLocation;
import com.baidu.location.Jni;
import com.baidu.location.LocationClientOption;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public class a {
    private static String o = "BDLocConfigManager";
    public boolean a;
    public int b;
    public double c;
    public int d;
    public int e;
    public double f;
    public int g;
    public int h;
    public int i;
    public int j;
    public int k;
    public int l;
    public double[] m;
    public int n;
    private SharedPreferences p;
    private long q;
    private String r;
    private C0005a s;
    private boolean t;
    private String u;
    private String v;
    private String w;

    /* renamed from: com.baidu.location.b.a$a  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class C0005a extends com.baidu.location.e.e {
        public String a = null;
        public boolean b = false;

        public C0005a() {
            this.j = new HashMap();
        }

        @Override // com.baidu.location.e.e
        public void a() {
            this.h = 2;
            String encode = Jni.encode(this.a);
            this.a = null;
            this.j.put("qt", "conf");
            this.j.put("req", encode);
        }

        public void a(String str) {
            if (this.b) {
                return;
            }
            this.b = true;
            this.a = str;
            b("https://loc.map.baidu.com/cfgs/loc/commcfgs");
        }

        @Override // com.baidu.location.e.e
        public void a(boolean z) {
            if (z && this.i != null) {
                try {
                    new JSONObject(this.i);
                    if (a.this.p != null) {
                        SharedPreferences.Editor edit = a.this.p.edit();
                        edit.putString(a.o + "_config", this.i);
                        edit.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Map<String, Object> map = this.j;
            if (map != null) {
                map.clear();
            }
            this.b = false;
        }
    }

    /* loaded from: classes.dex */
    public static class b {
        public static final a a = new a();
    }

    private a() {
        this.p = null;
        this.a = false;
        this.b = 16;
        this.q = 300L;
        this.c = 0.75d;
        this.d = 0;
        this.e = 1;
        this.f = -0.10000000149011612d;
        this.g = 0;
        this.h = 1;
        this.i = 1;
        this.j = 10;
        this.k = 3;
        this.l = 40;
        this.n = 1;
        this.r = null;
        this.s = null;
        this.t = false;
        this.u = null;
        this.v = null;
        this.w = null;
    }

    public static a a() {
        return b.a;
    }

    private String a(Context context) {
        int myPid = Process.myPid();
        String str = "";
        try {
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
            if (runningAppProcesses != null) {
                for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                    if (runningAppProcessInfo.pid == myPid) {
                        str = runningAppProcessInfo.processName;
                    }
                }
            }
        } catch (Exception unused) {
        }
        return str;
    }

    private void a(LocationClientOption locationClientOption) {
        String string;
        String str = "&ver=" + com.baidu.location.e.j.y + "&usr=" + c() + "&app=" + this.u + "&prod=" + locationClientOption.prodName + "&newwf=1";
        String str2 = Build.VERSION.RELEASE;
        if (str2 != null && str2.length() > 6) {
            str2 = str2.substring(0, 6);
        }
        String str3 = str + "&sv=" + str2;
        String c = com.baidu.location.e.j.c("ro.miui.ui.version.name");
        if (!TextUtils.isEmpty(c)) {
            str3 = str3 + "&miui=" + c;
        }
        String k = com.baidu.location.e.j.k();
        if (!TextUtils.isEmpty(k)) {
            str3 = str3 + "&mtk=" + k;
        }
        if (!TextUtils.isEmpty(this.p.getString(o + "_loc", null))) {
            try {
                str3 = str3 + "&loc=" + new String(Base64.decode(string, 0), Keyczar.DEFAULT_ENCODING);
            } catch (Exception unused) {
            }
        }
        if (this.s == null) {
            this.s = new C0005a();
        }
        this.s.a(str3);
    }

    private void a(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("is_check_Per") && jSONObject.getInt("is_check_Per") > 0) {
                this.a = true;
            }
            if (jSONObject.has("wfnum")) {
                this.b = jSONObject.getInt("wfnum");
            }
            if (jSONObject.has("freq")) {
                this.q = jSONObject.getLong("freq");
            }
            if (jSONObject.has("wfsm")) {
                this.c = jSONObject.getDouble("wfsm");
            }
            if (jSONObject.has("idmoc")) {
                this.d = jSONObject.getInt("idmoc");
            }
            if (jSONObject.has("gnmcrm")) {
                this.f = jSONObject.getDouble("gnmcrm");
            }
            if (jSONObject.has("gnmcon")) {
                this.g = jSONObject.getInt("gnmcon");
            }
            if (jSONObject.has("lpcs")) {
                this.e = jSONObject.getInt("lpcs");
            }
            if (jSONObject.has("iupl")) {
                this.h = jSONObject.getInt("iupl");
            }
            if (jSONObject.has("opetco")) {
                this.i = jSONObject.getInt("opetco");
            }
            if (jSONObject.has("ct")) {
                this.j = jSONObject.getInt("ct");
            }
            if (jSONObject.has("suci")) {
                this.k = jSONObject.getInt("suci");
            }
            if (jSONObject.has("smn")) {
                this.l = jSONObject.getInt("smn");
            }
            if (jSONObject.has("bcar")) {
                a(jSONObject);
            }
            if (jSONObject.has("ums")) {
                this.n = jSONObject.getInt("ums");
            }
            this.r = str;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String c() {
        StringBuilder sb = new StringBuilder();
        sb.append("v9.16|");
        sb.append(this.v);
        sb.append("|");
        String str = Build.MODEL;
        sb.append(str);
        sb.append("&cu=");
        sb.append(this.v);
        sb.append("&mb=");
        sb.append(str);
        return sb.toString();
    }

    public synchronized void a(double d, double d2, String str) {
        SharedPreferences sharedPreferences;
        if (this.w == null && str != null) {
            try {
                if (str.equals(BDLocation.BDLOCATION_GCJ02_TO_BD09) || str.equals("wgs84mc")) {
                    double[] coorEncrypt = Jni.coorEncrypt(d2, d, BDLocation.BDLOCATION_BD09_TO_GCJ02);
                    double d3 = coorEncrypt[1];
                    double d4 = coorEncrypt[0];
                    d = d3;
                    d2 = d4;
                }
                String format = String.format(Locale.US, "%.5f|%.5f", Double.valueOf(d2), Double.valueOf(d));
                this.w = format;
                String encodeToString = Base64.encodeToString(format.getBytes(Keyczar.DEFAULT_ENCODING), 0);
                if (encodeToString != null && (sharedPreferences = this.p) != null) {
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString(o + "_loc", encodeToString);
                    edit.commit();
                }
            } catch (Exception unused) {
                this.w = null;
            }
        }
    }

    public synchronized void a(Context context, LocationClientOption locationClientOption, String str) {
        if (!this.t && context != null) {
            this.t = true;
            if (locationClientOption == null) {
                locationClientOption = new LocationClientOption();
            }
            o += "_" + a(context);
            this.u = context.getPackageName();
            this.v = LBSAuthManager.getInstance(context).getCUID();
            if (this.p == null) {
                this.p = context.getSharedPreferences(o + "BDLocConfig", 0);
            }
            SharedPreferences sharedPreferences = this.p;
            if (sharedPreferences != null) {
                long j = sharedPreferences.getLong(o + "_lastCheckTime", 0L);
                String string = this.p.getString(o + "_config", "");
                if (!TextUtils.isEmpty(string)) {
                    a(string);
                }
                if (Math.abs((System.currentTimeMillis() / 1000) - j) > this.q) {
                    SharedPreferences.Editor edit = this.p.edit();
                    edit.putLong(o + "_lastCheckTime", System.currentTimeMillis() / 1000);
                    edit.commit();
                    a(locationClientOption);
                }
            }
        }
    }

    public void a(JSONObject jSONObject) {
        JSONArray jSONArray;
        if (jSONObject != null) {
            double[] dArr = this.m;
            if (dArr != null && dArr.length > 0) {
                this.m = null;
            }
            try {
                if (!jSONObject.has("bcar") || (jSONArray = jSONObject.getJSONArray("bcar")) == null || jSONArray.length() <= 0) {
                    return;
                }
                if (this.m == null) {
                    this.m = new double[jSONArray.length() * 4];
                }
                int i = 0;
                int i2 = 0;
                while (i < jSONArray.length()) {
                    int i3 = i2 + 1;
                    this.m[i2] = jSONArray.getJSONObject(i).getDouble("x1");
                    int i4 = i3 + 1;
                    this.m[i3] = jSONArray.getJSONObject(i).getDouble("y1");
                    int i5 = i4 + 1;
                    this.m[i4] = jSONArray.getJSONObject(i).getDouble("x2");
                    int i6 = i5 + 1;
                    this.m[i5] = jSONArray.getJSONObject(i).getDouble("y2");
                    i++;
                    i2 = i6;
                }
            } catch (Exception unused) {
            }
        }
    }
}
