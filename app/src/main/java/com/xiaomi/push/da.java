package com.xiaomi.push;

import java.util.Iterator;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class da implements Comparable<da> {
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public long f186a;

    /* renamed from: a  reason: collision with other field name */
    public String f187a;

    /* renamed from: a  reason: collision with other field name */
    public final LinkedList<cq> f188a;

    public da() {
        this(null, 0);
    }

    public da(String str) {
        this(str, 0);
    }

    public da(String str, int i) {
        this.f188a = new LinkedList<>();
        this.f186a = 0L;
        this.f187a = str;
        this.a = i;
    }

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(da daVar) {
        if (daVar == null) {
            return 1;
        }
        return daVar.a - this.a;
    }

    public synchronized da a(JSONObject jSONObject) {
        this.f186a = jSONObject.getLong("tt");
        this.a = jSONObject.getInt("wt");
        this.f187a = jSONObject.getString("host");
        JSONArray jSONArray = jSONObject.getJSONArray("ah");
        for (int i = 0; i < jSONArray.length(); i++) {
            this.f188a.add(new cq().a(jSONArray.getJSONObject(i)));
        }
        return this;
    }

    public synchronized JSONObject a() {
        JSONObject jSONObject;
        jSONObject = new JSONObject();
        jSONObject.put("tt", this.f186a);
        jSONObject.put("wt", this.a);
        jSONObject.put("host", this.f187a);
        JSONArray jSONArray = new JSONArray();
        Iterator<cq> it = this.f188a.iterator();
        while (it.hasNext()) {
            jSONArray.put(it.next().m2015a());
        }
        jSONObject.put("ah", jSONArray);
        return jSONObject;
    }

    public synchronized void a(cq cqVar) {
        if (cqVar != null) {
            this.f188a.add(cqVar);
            int a = cqVar.a();
            if (a > 0) {
                this.a += cqVar.a();
            } else {
                int i = 0;
                for (int size = this.f188a.size() - 1; size >= 0 && this.f188a.get(size).a() < 0; size--) {
                    i++;
                }
                this.a += a * i;
            }
            if (this.f188a.size() > 30) {
                this.a -= this.f188a.remove().a();
            }
        }
    }

    public String toString() {
        return this.f187a + ":" + this.a;
    }
}
