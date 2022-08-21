package com.baidu.location.a;

import android.content.Context;
import android.util.Log;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import com.baidu.lbsapi.auth.LBSAuthManager;
import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class a implements LBSAuthManagerListener {
    private static Object b = new Object();
    private static a c;
    private int d = 0;
    private Context e = null;
    private long f = 0;
    private String g = null;
    public int a = 0;

    public static a a() {
        a aVar;
        synchronized (b) {
            if (c == null) {
                c = new a();
            }
            aVar = c;
        }
        return aVar;
    }

    public static String b(Context context) {
        try {
            return LBSAuthManager.getInstance(context).getPublicKey(context);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String c(Context context) {
        try {
            return LBSAuthManager.getInstance(context).getMCode();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void a(Context context) {
        this.e = context;
        LBSAuthManager.getInstance(context).authenticate(false, "lbs_locsdk", null, this);
        this.f = System.currentTimeMillis();
    }

    public boolean b() {
        int i = this.d;
        boolean z = i == 0 || i == 602 || i == 601 || i == -10 || i == -11;
        if (this.e != null) {
            long currentTimeMillis = System.currentTimeMillis() - this.f;
            if (!z ? currentTimeMillis < 0 || currentTimeMillis > AbstractComponentTracker.LINGERING_TIMEOUT : currentTimeMillis > 86400000) {
                LBSAuthManager.getInstance(this.e).authenticate(false, "lbs_locsdk", null, this);
                this.f = System.currentTimeMillis();
            }
        }
        return z;
    }

    @Override // com.baidu.lbsapi.auth.LBSAuthManagerListener
    public void onAuthResult(int i, String str) {
        this.d = i;
        if (i == 0) {
            Log.i(com.baidu.location.e.a.a, "LocationAuthManager Authentication AUTHENTICATE_SUCC");
        } else {
            String str2 = com.baidu.location.e.a.a;
            Log.i(str2, "LocationAuthManager Authentication Error errorcode = " + i + " , msg = " + str);
        }
        if (str != null) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has("token") && jSONObject.getString("token") != null) {
                    this.g = jSONObject.getString("token");
                }
                if (!jSONObject.has("ak_permission") || jSONObject.getInt("ak_permission") == 0) {
                    return;
                }
                this.a = jSONObject.getInt("ak_permission");
                String str3 = com.baidu.location.e.a.a;
                Log.i(str3, "LocationAuthManager ak_permission = " + this.a);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
