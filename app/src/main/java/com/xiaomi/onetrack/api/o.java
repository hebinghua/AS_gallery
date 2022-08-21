package com.xiaomi.onetrack.api;

import android.text.TextUtils;
import com.xiaomi.onetrack.util.p;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class o implements Runnable {
    public final /* synthetic */ boolean a;
    public final /* synthetic */ g b;

    public o(g gVar, boolean z) {
        this.b = gVar;
        this.a = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        d dVar;
        try {
            String z = com.xiaomi.onetrack.util.aa.z();
            if (TextUtils.isEmpty(z)) {
                return;
            }
            JSONObject jSONObject = new JSONObject(z);
            JSONObject put = jSONObject.optJSONObject("B").put("app_end", this.a);
            dVar = this.b.b;
            dVar.a("onetrack_pa", jSONObject.put("B", put).toString());
            if (p.a) {
                p.a("OneTrackImp", "trackPageEndAuto");
            }
            com.xiaomi.onetrack.util.aa.i("");
        } catch (Exception e) {
            p.b("OneTrackImp", "trackPageEndAuto error:" + e.toString());
        }
    }
}
