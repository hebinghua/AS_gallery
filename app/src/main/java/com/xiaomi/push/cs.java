package com.xiaomi.push;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class cs {
    public String a;

    /* renamed from: a  reason: collision with other field name */
    public final ArrayList<cr> f172a = new ArrayList<>();

    public cs() {
    }

    public cs(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.a = str;
            return;
        }
        throw new IllegalArgumentException("the host is empty");
    }

    public synchronized cr a() {
        for (int size = this.f172a.size() - 1; size >= 0; size--) {
            cr crVar = this.f172a.get(size);
            if (crVar.m2019a()) {
                cv.a().m2029a(crVar.a());
                return crVar;
            }
        }
        return null;
    }

    public synchronized cs a(JSONObject jSONObject) {
        this.a = jSONObject.getString("host");
        JSONArray jSONArray = jSONObject.getJSONArray("fbs");
        for (int i = 0; i < jSONArray.length(); i++) {
            this.f172a.add(new cr(this.a).a(jSONArray.getJSONObject(i)));
        }
        return this;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2020a() {
        return this.a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public ArrayList<cr> m2021a() {
        return this.f172a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized JSONObject m2022a() {
        JSONObject jSONObject;
        jSONObject = new JSONObject();
        jSONObject.put("host", this.a);
        JSONArray jSONArray = new JSONArray();
        Iterator<cr> it = this.f172a.iterator();
        while (it.hasNext()) {
            jSONArray.put(it.next().m2017a());
        }
        jSONObject.put("fbs", jSONArray);
        return jSONObject;
    }

    public synchronized void a(cr crVar) {
        int i = 0;
        while (true) {
            if (i >= this.f172a.size()) {
                break;
            } else if (this.f172a.get(i).a(crVar)) {
                this.f172a.set(i, crVar);
                break;
            } else {
                i++;
            }
        }
        if (i >= this.f172a.size()) {
            this.f172a.add(crVar);
        }
    }

    public synchronized void a(boolean z) {
        ArrayList<cr> arrayList;
        for (int size = this.f172a.size() - 1; size >= 0; size--) {
            cr crVar = this.f172a.get(size);
            if (z) {
                if (crVar.c()) {
                    arrayList = this.f172a;
                    arrayList.remove(size);
                }
            } else if (!crVar.b()) {
                arrayList = this.f172a;
                arrayList.remove(size);
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.a);
        sb.append("\n");
        Iterator<cr> it = this.f172a.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
        }
        return sb.toString();
    }
}
