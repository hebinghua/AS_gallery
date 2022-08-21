package com.xiaomi.onetrack.api;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.CrashAnalysis;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.util.p;
import com.xiaomi.onetrack.util.q;
import com.xiaomi.onetrack.util.r;
import com.xiaomi.onetrack.util.v;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class g {
    public static ExecutorService c;
    public d b;
    public Context d;
    public e e;
    public Configuration f;
    public OneTrack.ICommonPropertyProvider g;
    public OneTrack.IEventHook h;
    public v i;
    public BroadcastReceiver j = new x(this);

    public g(Context context, Configuration configuration) {
        Context applicationContext = context.getApplicationContext();
        this.d = applicationContext;
        this.f = configuration;
        b(applicationContext);
        Log.d("OneTrackImp", "OneTrackImp init : " + configuration.toString());
    }

    public final void b(Context context) {
        p.a();
        q.a(this.f.isInternational(), this.f.getRegion(), this.f.getMode());
        if (c == null) {
            c = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        }
        this.i = new v(this.f);
        if (q.a() && e() && c()) {
            com.xiaomi.onetrack.util.o.a().a(Boolean.TRUE);
            this.b = new aj(this.f, this.i);
        } else {
            com.xiaomi.onetrack.util.o.a().a(Boolean.FALSE);
            this.b = new ah(context, this.f, this.i);
        }
        if (this.f.getMode() == OneTrack.Mode.APP) {
            q.a(this.f.isOverrideMiuiRegionSetting());
            c(context);
            if (this.f.isExceptionCatcherEnable()) {
                CrashAnalysis.start(context, this);
                if (!CrashAnalysis.isSupport()) {
                    e eVar = new e();
                    this.e = eVar;
                    eVar.a();
                }
            }
        }
        d(context);
        c.execute(new h(this));
    }

    public final boolean c() {
        if (!this.f.isOverrideMiuiRegionSetting()) {
            return true;
        }
        return TextUtils.equals(q.z(), this.f.getRegion());
    }

    public void a(String str, Map<String, Object> map) {
        c.execute(new aa(this, str, map));
    }

    public void a(String str, String str2, String str3, String str4, String str5, long j) {
        c.execute(new ac(this, str, str2, str3, str5, str4, j));
    }

    public final void c(Context context) {
        ((Application) context).registerActivityLifecycleCallbacks(new k(this));
    }

    public final void d() {
        c.execute(new l(this));
    }

    public final void a(String str, boolean z) {
        c.execute(new m(this, str, z));
    }

    public final void a(String str, long j) {
        c.execute(new n(this, str, j));
    }

    public final void c(boolean z) {
        c.execute(new o(this, z));
    }

    public final boolean e() {
        if (p.a) {
            p.a("OneTrackImp", "enable:" + f() + " isSupportEmptyEvent: " + g());
        }
        return f() && g();
    }

    public final boolean f() {
        try {
            int componentEnabledSetting = com.xiaomi.onetrack.e.a.b().getPackageManager().getComponentEnabledSetting(new ComponentName("com.miui.analytics", "com.miui.analytics.onetrack.OneTrackService"));
            return componentEnabledSetting == 1 || componentEnabledSetting == 0;
        } catch (Exception e) {
            p.b("OneTrackImp", "enable error:" + e.toString());
            return false;
        }
    }

    public static boolean g() {
        int i;
        try {
            i = com.xiaomi.onetrack.e.a.b().getPackageManager().getPackageInfo("com.miui.analytics", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (i >= 2020062900) {
            return true;
        }
        p.a("OneTrackImp", "system analytics version: " + i);
        return false;
    }

    public final boolean c(String str) {
        boolean a = r.a(str);
        if (!a) {
            p.b("OneTrackImp", String.format("Invalid eventname: %s. Eventname can only consist of numbers, letters, underscores ,and can not start with a number or \"onetrack_\" or \"ot_\"", str));
        }
        return !a;
    }

    public final JSONObject d(String str) {
        try {
            OneTrack.ICommonPropertyProvider iCommonPropertyProvider = this.g;
            JSONObject a = r.a(iCommonPropertyProvider != null ? iCommonPropertyProvider.getDynamicProperty(str) : null, false);
            String a2 = com.xiaomi.onetrack.util.k.a(r.a(this.f));
            return r.a(a, !TextUtils.isEmpty(a2) ? new JSONObject(a2) : null);
        } catch (Exception e) {
            p.b("OneTrackImp", "getCommonProperty: " + e.toString());
            return null;
        }
    }

    public final void i() {
        if (com.xiaomi.onetrack.b.h.d()) {
            c.execute(new w(this));
        }
    }

    public final void j() {
        try {
            if (this.f.getMode() != OneTrack.Mode.APP) {
                return;
            }
            long d = com.xiaomi.onetrack.e.a.d();
            String a = a(d, com.xiaomi.onetrack.e.a.c());
            String A = com.xiaomi.onetrack.util.aa.A();
            if (TextUtils.isEmpty(A)) {
                com.xiaomi.onetrack.util.aa.j(a);
                return;
            }
            JSONObject jSONObject = new JSONObject(A);
            long optLong = jSONObject.optLong("last_ver_code");
            String optString = jSONObject.optString("last_ver_name");
            if (optLong == d) {
                return;
            }
            com.xiaomi.onetrack.util.aa.j(a);
            this.b.a("onetrack_upgrade", c.a(optLong, optString, d, com.xiaomi.onetrack.e.a.f(), this.f, this.h, this.i));
        } catch (Exception e) {
            p.b("OneTrackImp", "trackUpgradeEvent error: " + e.toString());
        }
    }

    public final String a(long j, String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("last_ver_name", str);
        jSONObject.put("last_ver_code", j);
        return jSONObject.toString();
    }

    public void a(OneTrack.IEventHook iEventHook) {
        this.h = iEventHook;
        this.i.a(iEventHook);
    }

    public final void d(Context context) {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            context.registerReceiver(this.j, intentFilter);
        } catch (Exception unused) {
        }
    }
}
