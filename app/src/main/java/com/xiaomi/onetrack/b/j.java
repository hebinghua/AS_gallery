package com.xiaomi.onetrack.b;

import java.util.List;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public final class j implements Runnable {
    public final /* synthetic */ com.xiaomi.onetrack.api.d a;

    public j(com.xiaomi.onetrack.api.d dVar) {
        this.a = dVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            List<JSONObject> c = h.c();
            if (c != null && c.size() > 0) {
                for (JSONObject jSONObject : c) {
                    this.a.a(jSONObject.optString("eventName"), jSONObject.optString("data"));
                }
            }
            h.c(true);
        } catch (Exception e) {
            com.xiaomi.onetrack.util.p.b("NetworkAccessManager", "cta event error: " + e.toString());
        }
        boolean unused = h.l = false;
    }
}
