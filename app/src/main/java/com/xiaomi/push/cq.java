package com.xiaomi.push;

import com.baidu.platform.comapi.map.MapBundleKey;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class cq {
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public long f166a;

    /* renamed from: a  reason: collision with other field name */
    public String f167a;
    public long b;
    public long c;

    public cq() {
        this(0, 0L, 0L, null);
    }

    public cq(int i, long j, long j2, Exception exc) {
        this.a = i;
        this.f166a = j;
        this.c = j2;
        this.b = System.currentTimeMillis();
        if (exc != null) {
            this.f167a = exc.getClass().getSimpleName();
        }
    }

    public int a() {
        return this.a;
    }

    public cq a(JSONObject jSONObject) {
        this.f166a = jSONObject.getLong("cost");
        this.c = jSONObject.getLong(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE);
        this.b = jSONObject.getLong("ts");
        this.a = jSONObject.getInt("wt");
        this.f167a = jSONObject.optString("expt");
        return this;
    }

    /* renamed from: a  reason: collision with other method in class */
    public JSONObject m2015a() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("cost", this.f166a);
        jSONObject.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, this.c);
        jSONObject.put("ts", this.b);
        jSONObject.put("wt", this.a);
        jSONObject.put("expt", this.f167a);
        return jSONObject;
    }
}
