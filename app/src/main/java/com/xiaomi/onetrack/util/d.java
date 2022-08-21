package com.xiaomi.onetrack.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;
import com.xiaomi.onetrack.e.a;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class d {
    public static final String a = "d";
    public static volatile d f;
    public final Context h;
    public Handler i = new e(this, Looper.getMainLooper());
    public BroadcastReceiver j = new f(this);

    public static d a() {
        if (f == null) {
            synchronized (d.class) {
                if (f == null) {
                    f = new d();
                }
            }
        }
        return f;
    }

    public d() {
        Context b = a.b();
        this.h = b;
        a(b);
    }

    public final void a(Context context) {
        if (context == null) {
            return;
        }
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.xiaomi.onetrack.DEBUG");
            context.registerReceiver(this.j, intentFilter, "com.xiaomi.onetrack.permissions.DEBUG_MODE", null);
        } catch (Exception e) {
            String str = a;
            p.a(str, "registerDebugModeReceiver: " + e);
        }
    }

    public final void a(String str, String str2, String str3) {
        i.a(new g(this, str, str2, str3));
    }

    public final void a(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            int optInt = jSONObject.optInt("code");
            String optString = jSONObject.optString("message");
            String optString2 = jSONObject.optString("result");
            boolean optBoolean = jSONObject.optBoolean("success");
            Message obtain = Message.obtain();
            obtain.what = 100;
            Bundle bundle = new Bundle();
            if (optInt == 0 && optBoolean) {
                bundle.putString("hint", optString2);
            } else {
                bundle.putString("hint", optString);
            }
            obtain.setData(bundle);
            this.i.sendMessage(obtain);
        } catch (JSONException e) {
            p.b(a, e.getMessage());
        }
    }

    public final void b(String str) {
        Toast.makeText(this.h, str, 1).show();
    }
}
