package com.baidu.lbsapi.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import com.baidu.platform.comapi.map.MapBundleKey;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class LBSAuthManager {
    public static final int CODE_AUTHENTICATE_SUCC = 0;
    public static final int CODE_AUTHENTICATING = 602;
    public static final int CODE_INNER_ERROR = -1;
    public static final int CODE_KEY_NOT_EXIST = 101;
    public static final int CODE_NETWORK_FAILED = -11;
    public static final int CODE_NETWORK_INVALID = -10;
    public static final int CODE_UNAUTHENTICATE = 601;
    public static final String VERSION = "1.0.25";
    private static Context a;
    private static m d;
    private static int e;
    private static LBSAuthManager g;
    private c b = null;
    private e c = null;
    private boolean h = false;
    private final Handler n = new i(this, Looper.getMainLooper());
    private static Hashtable<String, LBSAuthManagerListener> f = new Hashtable<>();
    private static String i = "";
    private static String j = "";
    private static String k = "";
    private static String[] l = null;
    private static boolean m = true;

    private LBSAuthManager(Context context) {
        a = context;
        m mVar = d;
        if (mVar != null && !mVar.isAlive()) {
            d = null;
        }
        a.b("BaiduApiAuth SDK Version:1.0.25");
        e();
    }

    private int a(String str) {
        int i2 = -1;
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.has("status")) {
                jSONObject.put("status", -1);
            }
            i2 = jSONObject.getInt("status");
            if (jSONObject.has("current") && i2 == 0) {
                long j2 = jSONObject.getLong("current");
                long currentTimeMillis = System.currentTimeMillis();
                if ((currentTimeMillis - j2) / 3600000.0d < 24.0d) {
                    if (this.h) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        if (!simpleDateFormat.format(Long.valueOf(currentTimeMillis)).equals(simpleDateFormat.format(Long.valueOf(j2)))) {
                        }
                    }
                }
                i2 = 601;
            }
            if (jSONObject.has("current") && i2 == 602) {
                if ((System.currentTimeMillis() - jSONObject.getLong("current")) / 1000 > 180.0d) {
                    return 601;
                }
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return i2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x0070, code lost:
        if (r6 == null) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0080, code lost:
        if (r6 == null) goto L12;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x007d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String a(int r6) throws java.io.IOException {
        /*
            r5 = this;
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch: java.lang.Throwable -> L4e java.io.IOException -> L63 java.io.FileNotFoundException -> L73
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4e java.io.IOException -> L63 java.io.FileNotFoundException -> L73
            r2.<init>()     // Catch: java.lang.Throwable -> L4e java.io.IOException -> L63 java.io.FileNotFoundException -> L73
            java.lang.String r3 = "/proc/"
            r2.append(r3)     // Catch: java.lang.Throwable -> L4e java.io.IOException -> L63 java.io.FileNotFoundException -> L73
            r2.append(r6)     // Catch: java.lang.Throwable -> L4e java.io.IOException -> L63 java.io.FileNotFoundException -> L73
            java.lang.String r6 = "/cmdline"
            r2.append(r6)     // Catch: java.lang.Throwable -> L4e java.io.IOException -> L63 java.io.FileNotFoundException -> L73
            java.lang.String r6 = r2.toString()     // Catch: java.lang.Throwable -> L4e java.io.IOException -> L63 java.io.FileNotFoundException -> L73
            r1.<init>(r6)     // Catch: java.lang.Throwable -> L4e java.io.IOException -> L63 java.io.FileNotFoundException -> L73
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L4e java.io.IOException -> L63 java.io.FileNotFoundException -> L73
            r6.<init>(r1)     // Catch: java.lang.Throwable -> L4e java.io.IOException -> L63 java.io.FileNotFoundException -> L73
            java.io.InputStreamReader r1 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L45 java.io.IOException -> L4a java.io.FileNotFoundException -> L4c
            r1.<init>(r6)     // Catch: java.lang.Throwable -> L45 java.io.IOException -> L4a java.io.FileNotFoundException -> L4c
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L3c java.io.IOException -> L41 java.io.FileNotFoundException -> L43
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L3c java.io.IOException -> L41 java.io.FileNotFoundException -> L43
            java.lang.String r0 = r2.readLine()     // Catch: java.lang.Throwable -> L3a java.io.IOException -> L66 java.io.FileNotFoundException -> L76
            r2.close()
            r1.close()
        L35:
            r6.close()
            goto L83
        L3a:
            r0 = move-exception
            goto L53
        L3c:
            r2 = move-exception
            r4 = r2
            r2 = r0
            r0 = r4
            goto L53
        L41:
            r2 = r0
            goto L66
        L43:
            r2 = r0
            goto L76
        L45:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
            goto L53
        L4a:
            r1 = r0
            goto L65
        L4c:
            r1 = r0
            goto L75
        L4e:
            r6 = move-exception
            r1 = r0
            r2 = r1
            r0 = r6
            r6 = r2
        L53:
            if (r2 == 0) goto L58
            r2.close()
        L58:
            if (r1 == 0) goto L5d
            r1.close()
        L5d:
            if (r6 == 0) goto L62
            r6.close()
        L62:
            throw r0
        L63:
            r6 = r0
            r1 = r6
        L65:
            r2 = r1
        L66:
            if (r2 == 0) goto L6b
            r2.close()
        L6b:
            if (r1 == 0) goto L70
            r1.close()
        L70:
            if (r6 == 0) goto L83
            goto L35
        L73:
            r6 = r0
            r1 = r6
        L75:
            r2 = r1
        L76:
            if (r2 == 0) goto L7b
            r2.close()
        L7b:
            if (r1 == 0) goto L80
            r1.close()
        L80:
            if (r6 == 0) goto L83
            goto L35
        L83:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.lbsapi.auth.LBSAuthManager.a(int):java.lang.String");
    }

    private String a(Context context) {
        String str;
        try {
            str = a(Process.myPid());
        } catch (IOException unused) {
            str = null;
        }
        return str != null ? str : a.getPackageName();
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0044, code lost:
        if (r6.equals(r1) != false) goto L18;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String a(android.content.Context r6, java.lang.String r7) {
        /*
            r5 = this;
            java.lang.String r0 = "无法在AndroidManifest.xml中获取com.baidu.android.lbs.API_KEY的值"
            java.lang.String r1 = ""
            java.lang.String r2 = com.baidu.lbsapi.auth.LBSAuthManager.i
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L10
            java.lang.String r6 = com.baidu.lbsapi.auth.LBSAuthManager.i
            return r6
        L10:
            java.lang.String r2 = r6.getPackageName()
            r3 = 101(0x65, float:1.42E-43)
            android.content.pm.PackageManager r6 = r6.getPackageManager()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            r4 = 128(0x80, float:1.794E-43)
            android.content.pm.ApplicationInfo r6 = r6.getApplicationInfo(r2, r4)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            android.os.Bundle r6 = r6.metaData     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            if (r6 != 0) goto L38
            java.util.Hashtable<java.lang.String, com.baidu.lbsapi.auth.LBSAuthManagerListener> r6 = com.baidu.lbsapi.auth.LBSAuthManager.f     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            java.lang.Object r6 = r6.get(r7)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            com.baidu.lbsapi.auth.LBSAuthManagerListener r6 = (com.baidu.lbsapi.auth.LBSAuthManagerListener) r6     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            if (r6 == 0) goto L6b
            java.lang.String r2 = "AndroidManifest.xml的application中没有meta-data标签"
            java.lang.String r2 = com.baidu.lbsapi.auth.ErrorMessage.a(r3, r2)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            r6.onAuthResult(r3, r2)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            goto L6b
        L38:
            java.lang.String r2 = "com.baidu.lbsapi.API_KEY"
            java.lang.String r6 = r6.getString(r2)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L5a
            if (r6 == 0) goto L46
            boolean r1 = r6.equals(r1)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L59
            if (r1 == 0) goto L57
        L46:
            java.util.Hashtable<java.lang.String, com.baidu.lbsapi.auth.LBSAuthManagerListener> r1 = com.baidu.lbsapi.auth.LBSAuthManager.f     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L59
            java.lang.Object r1 = r1.get(r7)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L59
            com.baidu.lbsapi.auth.LBSAuthManagerListener r1 = (com.baidu.lbsapi.auth.LBSAuthManagerListener) r1     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L59
            if (r1 == 0) goto L57
            java.lang.String r2 = com.baidu.lbsapi.auth.ErrorMessage.a(r3, r0)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L59
            r1.onAuthResult(r3, r2)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L59
        L57:
            r1 = r6
            goto L6b
        L59:
            r1 = r6
        L5a:
            java.util.Hashtable<java.lang.String, com.baidu.lbsapi.auth.LBSAuthManagerListener> r6 = com.baidu.lbsapi.auth.LBSAuthManager.f
            java.lang.Object r6 = r6.get(r7)
            com.baidu.lbsapi.auth.LBSAuthManagerListener r6 = (com.baidu.lbsapi.auth.LBSAuthManagerListener) r6
            if (r6 == 0) goto L6b
            java.lang.String r7 = com.baidu.lbsapi.auth.ErrorMessage.a(r3, r0)
            r6.onAuthResult(r3, r7)
        L6b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.lbsapi.auth.LBSAuthManager.a(android.content.Context, java.lang.String):java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a(String str, String str2) {
        m mVar;
        if (str == null) {
            str = g();
        }
        Message obtainMessage = this.n.obtainMessage();
        int i2 = -1;
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.has("status")) {
                jSONObject.put("status", -1);
            }
            if (!jSONObject.has("current")) {
                jSONObject.put("current", System.currentTimeMillis());
            }
            c(jSONObject.toString());
            if (jSONObject.has("current")) {
                jSONObject.remove("current");
            }
            i2 = jSONObject.getInt("status");
            obtainMessage.what = i2;
            obtainMessage.obj = jSONObject.toString();
            Bundle bundle = new Bundle();
            bundle.putString("listenerKey", str2);
            obtainMessage.setData(bundle);
            this.n.sendMessage(obtainMessage);
        } catch (JSONException e2) {
            e2.printStackTrace();
            obtainMessage.what = i2;
            obtainMessage.obj = new JSONObject();
            Bundle bundle2 = new Bundle();
            bundle2.putString("listenerKey", str2);
            obtainMessage.setData(bundle2);
            this.n.sendMessage(obtainMessage);
        }
        m mVar2 = d;
        if (mVar2 != null) {
            mVar2.c();
        }
        e--;
        a.a("httpRequest called mAuthCounter-- = " + e);
        if (e == 0 && (mVar = d) != null) {
            mVar.a();
            d = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(boolean z, String str, Hashtable<String, String> hashtable, String str2) {
        String str3;
        String a2 = a(a, str2);
        if (a2 == null || a2.equals("")) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapBundleKey.MapObjKey.OBJ_URL, "https://api.map.baidu.com/sdkcs/verify");
        a.a("url:https://api.map.baidu.com/sdkcs/verify");
        hashMap.put("output", "json");
        hashMap.put("ak", a2);
        a.a("ak:" + a2);
        hashMap.put("mcode", !TextUtils.isEmpty(k) ? k : b.a(a));
        hashMap.put("from", "lbs_yunsdk");
        if (hashtable != null && hashtable.size() > 0) {
            for (Map.Entry<String, String> entry : hashtable.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    hashMap.put(key, value);
                }
            }
        }
        String cuid = getCUID();
        a.a("cuid:" + cuid);
        if (!TextUtils.isEmpty(cuid)) {
            hashMap.put("cuid", cuid);
        } else {
            hashMap.put("cuid", "");
        }
        hashMap.put("pcn", a.getPackageName());
        hashMap.put("version", VERSION);
        hashMap.put("macaddr", "");
        try {
            str3 = b.a();
        } catch (Exception unused) {
            str3 = "";
        }
        if (!TextUtils.isEmpty(str3)) {
            hashMap.put("language", str3);
        } else {
            hashMap.put("language", "");
        }
        if (z) {
            hashMap.put("force", z ? "1" : "0");
        }
        if (str == null) {
            hashMap.put("from_service", "");
        } else {
            hashMap.put("from_service", str);
        }
        String f2 = f();
        if (!TextUtils.isEmpty(f2)) {
            hashMap.put("extend", f2);
        }
        c cVar = new c(a);
        this.b = cVar;
        cVar.a(hashMap, new k(this, str2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(boolean z, String str, Hashtable<String, String> hashtable, String[] strArr, String str2) {
        String str3;
        String a2 = a(a, str2);
        if (a2 == null || a2.equals("")) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(MapBundleKey.MapObjKey.OBJ_URL, "https://api.map.baidu.com/sdkcs/verify");
        hashMap.put("output", "json");
        hashMap.put("ak", a2);
        hashMap.put("from", "lbs_yunsdk");
        if (hashtable != null && hashtable.size() > 0) {
            for (Map.Entry<String, String> entry : hashtable.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    hashMap.put(key, value);
                }
            }
        }
        String cuid = getCUID();
        a.a("sendAuthRequests : cuid: " + cuid);
        if (!TextUtils.isEmpty(cuid)) {
            hashMap.put("cuid", cuid);
        } else {
            hashMap.put("cuid", "");
        }
        hashMap.put("pcn", a.getPackageName());
        hashMap.put("version", VERSION);
        hashMap.put("macaddr", "");
        try {
            str3 = b.a();
        } catch (Exception unused) {
            str3 = "";
        }
        if (!TextUtils.isEmpty(str3)) {
            hashMap.put("language", str3);
        } else {
            hashMap.put("language", "");
        }
        if (z) {
            hashMap.put("force", z ? "1" : "0");
        }
        if (str == null) {
            hashMap.put("from_service", "");
        } else {
            hashMap.put("from_service", str);
        }
        String f2 = f();
        if (!TextUtils.isEmpty(f2)) {
            hashMap.put("extend", f2);
        }
        e eVar = new e(a);
        this.c = eVar;
        eVar.a(hashMap, strArr, new l(this, str2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean b(String str) {
        String str2;
        JSONObject jSONObject;
        String a2 = a(a, str);
        try {
            jSONObject = new JSONObject(g());
        } catch (JSONException e2) {
            e2.printStackTrace();
            str2 = "";
        }
        if (!jSONObject.has("ak")) {
            return true;
        }
        str2 = jSONObject.getString("ak");
        return (a2 == null || str2 == null || a2.equals(str2)) ? false : true;
    }

    private void c(String str) {
        Context context = a;
        context.getSharedPreferences("authStatus_" + a(a), 0).edit().putString("status", str).commit();
    }

    private void e() {
        synchronized (LBSAuthManager.class) {
            if (d == null) {
                m mVar = new m("auth");
                d = mVar;
                mVar.start();
                while (d.a == null) {
                    try {
                        a.a("wait for create auth thread.");
                        Thread.sleep(3L);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    private String f() {
        try {
            JSONObject jSONObject = new JSONObject(g());
            return !jSONObject.has("extend") ? "" : jSONObject.getString("extend");
        } catch (JSONException unused) {
            return "";
        }
    }

    private String g() {
        Context context = a;
        return context.getSharedPreferences("authStatus_" + a(a), 0).getString("status", "{\"status\":601}");
    }

    public static LBSAuthManager getInstance(Context context) {
        if (g == null) {
            synchronized (LBSAuthManager.class) {
                if (g == null) {
                    g = new LBSAuthManager(context);
                }
            }
        } else if (context != null) {
            a = context;
        } else if (a.a) {
            a.c("input context is null");
            new RuntimeException("here").printStackTrace();
        }
        return g;
    }

    public int authenticate(boolean z, String str, Hashtable<String, String> hashtable, LBSAuthManagerListener lBSAuthManagerListener) {
        synchronized (LBSAuthManager.class) {
            boolean z2 = false;
            if (hashtable != null) {
                String str2 = hashtable.get("zero_auth");
                if (str2 != null && Integer.valueOf(str2).intValue() == 1) {
                    z2 = true;
                }
            }
            this.h = z2;
            String str3 = System.currentTimeMillis() + "";
            if (lBSAuthManagerListener != null) {
                f.put(str3, lBSAuthManagerListener);
            }
            String a2 = a(a, str3);
            if (a2 != null && !a2.equals("")) {
                e++;
                a.a(" mAuthCounter  ++ = " + e);
                String g2 = g();
                a.a("getAuthMessage from cache:" + g2);
                int a3 = a(g2);
                if (a3 == 601) {
                    try {
                        c(new JSONObject().put("status", CODE_AUTHENTICATING).toString());
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
                e();
                m mVar = d;
                if (mVar != null && mVar.a != null) {
                    a.a("mThreadLooper.mHandler = " + d.a);
                    d.a.post(new j(this, a3, z, str3, str, hashtable));
                    return a3;
                }
                return -1;
            }
            return 101;
        }
    }

    public String getCUID() {
        String str = "";
        if (a == null) {
            return str;
        }
        try {
            a.a("mIsPrivacyMode " + m);
            if (m) {
                str = com.baidu.a.a.a.a.a.a(a);
                a.a("getCUID: " + str);
            } else {
                SharedPreferences sharedPreferences = a.getSharedPreferences("Map_Privacy", 0);
                if (sharedPreferences.contains("cuid")) {
                    str = sharedPreferences.getString("cuid", str);
                } else {
                    str = n.a(UUID.randomUUID().toString().getBytes(), true) + "|MAPSDK001";
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("cuid", str);
                    edit.apply();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return str;
    }

    public String getKey() {
        Context context = a;
        if (context == null) {
            return "";
        }
        try {
            return getPublicKey(context);
        } catch (PackageManager.NameNotFoundException e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public String getMCode() {
        return a == null ? "" : !TextUtils.isEmpty(k) ? k : b.a(a);
    }

    public boolean getPrivacyMode() {
        return m;
    }

    public String getPublicKey(Context context) throws PackageManager.NameNotFoundException {
        if (!TextUtils.isEmpty(i)) {
            return i;
        }
        return context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getString("com.baidu.lbsapi.API_KEY");
    }

    public void setKey(String str) {
        if (a == null || TextUtils.isEmpty(str)) {
            return;
        }
        i = str;
    }

    public void setMCode(String str) {
        k = str;
    }

    public void setMCodes(String[] strArr) {
        l = strArr;
    }

    public void setPackageName(String str) {
        j = str;
    }

    public void setPrivacyMode(boolean z) {
        Context context = a;
        if (context == null) {
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("Map_Privacy", 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (z) {
            edit.putBoolean("privacyMode", z);
            edit.apply();
        } else {
            z = sharedPreferences.getBoolean("privacyMode", false);
        }
        m = z;
    }
}
