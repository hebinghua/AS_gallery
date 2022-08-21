package com.baidu.mapsdkplatform.comapi.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import com.baidu.lbsapi.auth.LBSAuthManager;
import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import java.util.Hashtable;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class PermissionCheck {
    public static int a = 200;
    public static int b = 202;
    public static int c = 252;
    private static final String d = "PermissionCheck";
    private static Context e = null;
    private static String f = null;
    private static Hashtable<String, String> g = null;
    private static LBSAuthManager h = null;
    private static LBSAuthManagerListener i = null;
    private static c j = null;
    private static int k = 601;

    /* loaded from: classes.dex */
    public static class a implements LBSAuthManagerListener {
        private a() {
        }

        @Override // com.baidu.lbsapi.auth.LBSAuthManagerListener
        public void onAuthResult(int i, String str) {
            if (str == null) {
                Log.e(PermissionCheck.d, "The result is null");
                int permissionCheck = PermissionCheck.permissionCheck();
                String str2 = PermissionCheck.d;
                Log.d(str2, "onAuthResult try permissionCheck result is: " + permissionCheck);
                return;
            }
            b bVar = new b();
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has("status")) {
                    bVar.a = jSONObject.optInt("status");
                }
                if (jSONObject.has("appid")) {
                    bVar.c = jSONObject.optString("appid");
                }
                if (jSONObject.has("uid")) {
                    bVar.b = jSONObject.optString("uid");
                }
                if (jSONObject.has("message")) {
                    bVar.d = jSONObject.optString("message");
                }
                if (jSONObject.has("token")) {
                    bVar.e = jSONObject.optString("token");
                }
                if (jSONObject.has("ak_permission")) {
                    bVar.f = jSONObject.optInt("ak_permission");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int unused = PermissionCheck.k = bVar.a;
            if (PermissionCheck.j == null) {
                return;
            }
            PermissionCheck.j.a(bVar);
        }
    }

    /* loaded from: classes.dex */
    public static class b {
        public int a = 0;
        public String b = "-1";
        public String c = "-1";
        public String d = "";
        public String e;
        public int f;

        public String toString() {
            return String.format("=============================================\n----------------- 鉴权错误信息 ------------\nsha1;package:%s\nkey:%s\nerrorcode: %d uid: %s appid %s msg: %s\n请仔细核查 SHA1、package与key申请信息是否对应，key是否删除，平台是否匹配\nerrorcode为230时，请参考论坛链接：\nhttp://bbs.lbsyun.baidu.com/forum.php?mod=viewthread&tid=106461\n=============================================\n", com.baidu.mapsdkplatform.comapi.util.a.a(PermissionCheck.e), PermissionCheck.f, Integer.valueOf(this.a), this.b, this.c, this.d);
        }
    }

    /* loaded from: classes.dex */
    public interface c {
        void a(b bVar);
    }

    public static void destory() {
        j = null;
        e = null;
        i = null;
    }

    public static int getPermissionResult() {
        return k;
    }

    public static void init(Context context) {
        ApplicationInfo applicationInfo;
        String str;
        e = context;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(e.getPackageName(), 128);
        } catch (PackageManager.NameNotFoundException e2) {
            e2.printStackTrace();
            applicationInfo = null;
        }
        if (applicationInfo != null && TextUtils.isEmpty(f)) {
            f = applicationInfo.metaData.getString("com.baidu.lbsapi.API_KEY");
        }
        if (g == null) {
            g = new Hashtable<>();
        }
        if (h == null) {
            h = LBSAuthManager.getInstance(e);
        }
        if (i == null) {
            i = new a();
        }
        try {
            str = context.getPackageManager().getPackageInfo(e.getPackageName(), 0).applicationInfo.loadLabel(e.getPackageManager()).toString();
        } catch (Exception e3) {
            e3.printStackTrace();
            str = "";
        }
        try {
            JSONObject jSONObject = new JSONObject(h.b());
            g.put("mb", jSONObject.optString("mb"));
            g.put(com.xiaomi.stat.d.l, jSONObject.optString(com.xiaomi.stat.d.l));
            g.put(com.xiaomi.stat.d.b, jSONObject.optString(com.xiaomi.stat.d.b));
            g.put("imt", "1");
            g.put("net", jSONObject.optString("net"));
            g.put("cpu", jSONObject.optString("cpu"));
            g.put("glr", jSONObject.optString("glr"));
            g.put("glv", jSONObject.optString("glv"));
            g.put("resid", jSONObject.optString("resid"));
            g.put("appid", "-1");
            g.put("ver", "1");
            g.put("screen", String.format("(%d,%d)", Integer.valueOf(jSONObject.optInt("screen_x")), Integer.valueOf(jSONObject.optInt("screen_y"))));
            g.put("dpi", String.format("(%d,%d)", Integer.valueOf(jSONObject.optInt("dpi_x")), Integer.valueOf(jSONObject.optInt("dpi_y"))));
            g.put("pcn", jSONObject.optString("pcn"));
            g.put("cuid", jSONObject.optString("cuid"));
            g.put("name", str);
        } catch (Exception unused) {
        }
    }

    public static synchronized int permissionCheck() {
        synchronized (PermissionCheck.class) {
            LBSAuthManager lBSAuthManager = h;
            if (lBSAuthManager != null && i != null && e != null) {
                lBSAuthManager.setKey(f);
                int authenticate = h.authenticate(false, "lbs_androidmapsdk", g, i);
                if (authenticate != 0) {
                    String str = d;
                    Log.e(str, "permission check result is: " + authenticate);
                }
                return authenticate;
            }
            String str2 = d;
            Log.e(str2, "The authManager is: " + h + "; the authCallback is: " + i + "; the mContext is: " + e);
            return 0;
        }
    }

    public static void setApiKey(String str) {
        if (str == null || str.trim().length() <= 0) {
            return;
        }
        f = str;
    }

    public static void setPermissionCheckResultListener(c cVar) {
        j = cVar;
    }
}
