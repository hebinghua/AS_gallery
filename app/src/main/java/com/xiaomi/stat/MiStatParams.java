package com.xiaomi.stat;

import java.io.Reader;
import java.io.StringReader;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class MiStatParams {
    private static final String a = "MiStatParams";
    private JSONObject b;

    public boolean a() {
        return true;
    }

    public MiStatParams() {
        this.b = new JSONObject();
    }

    public MiStatParams(MiStatParams miStatParams) {
        JSONObject jSONObject;
        if (miStatParams != null && (jSONObject = miStatParams.b) != null) {
            this.b = a(jSONObject);
        } else {
            this.b = new JSONObject();
        }
    }

    /* JADX WARN: Not initialized variable reg: 1, insn: 0x0049: MOVE  (r0 I:??[OBJECT, ARRAY]) = (r1 I:??[OBJECT, ARRAY]), block:B:21:0x0049 */
    private JSONObject a(JSONObject jSONObject) {
        StringReader stringReader;
        Exception e;
        Reader reader;
        Reader reader2 = null;
        try {
            try {
                stringReader = new StringReader(jSONObject.toString());
                try {
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        int read = stringReader.read();
                        if (read != -1) {
                            sb.append((char) read);
                        } else {
                            JSONObject jSONObject2 = new JSONObject(sb.toString());
                            com.xiaomi.stat.d.j.a((Reader) stringReader);
                            return jSONObject2;
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                    com.xiaomi.stat.d.k.e(" deepCopy " + e);
                    com.xiaomi.stat.d.j.a((Reader) stringReader);
                    return jSONObject;
                }
            } catch (Throwable th) {
                th = th;
                reader2 = reader;
                com.xiaomi.stat.d.j.a(reader2);
                throw th;
            }
        } catch (Exception e3) {
            stringReader = null;
            e = e3;
        } catch (Throwable th2) {
            th = th2;
            com.xiaomi.stat.d.j.a(reader2);
            throw th;
        }
    }

    public void putInt(String str, int i) {
        if (!a(str)) {
            com.xiaomi.stat.d.n.e(str);
        } else if (c(str)) {
            com.xiaomi.stat.d.n.a();
        } else {
            try {
                this.b.put(str, i);
            } catch (JSONException e) {
                com.xiaomi.stat.d.k.c(a, "put value error " + e);
            }
        }
    }

    public void putLong(String str, long j) {
        if (!a(str)) {
            com.xiaomi.stat.d.n.e(str);
        } else if (c(str)) {
            com.xiaomi.stat.d.n.a();
        } else {
            try {
                this.b.put(str, j);
            } catch (JSONException e) {
                com.xiaomi.stat.d.k.c(a, "put value error " + e);
            }
        }
    }

    public void putDouble(String str, double d) {
        if (!a(str)) {
            com.xiaomi.stat.d.n.e(str);
        } else if (c(str)) {
            com.xiaomi.stat.d.n.a();
        } else {
            try {
                this.b.put(str, d);
            } catch (JSONException e) {
                com.xiaomi.stat.d.k.c(a, "put value error " + e);
            }
        }
    }

    public void putString(String str, String str2) {
        if (!a(str)) {
            com.xiaomi.stat.d.n.e(str);
        } else if (!b(str2)) {
            com.xiaomi.stat.d.n.f(str2);
        } else if (c(str)) {
            com.xiaomi.stat.d.n.a();
        } else {
            try {
                this.b.put(str, com.xiaomi.stat.d.n.c(str2));
            } catch (JSONException e) {
                com.xiaomi.stat.d.k.c(a, "put value error " + e);
            }
        }
    }

    public void putBoolean(String str, boolean z) {
        if (!a(str)) {
            com.xiaomi.stat.d.n.e(str);
        } else if (c(str)) {
            com.xiaomi.stat.d.n.a();
        } else {
            try {
                this.b.put(str, z);
            } catch (JSONException e) {
                com.xiaomi.stat.d.k.c(a, "put value error " + e);
            }
        }
    }

    public String toJsonString() {
        return this.b.toString();
    }

    public boolean isEmpty() {
        return this.b.length() == 0;
    }

    public int getParamsNumber() {
        return this.b.length();
    }

    private boolean c(String str) {
        return a() && !this.b.has(str) && this.b.length() == 30;
    }

    public boolean a(String str) {
        return com.xiaomi.stat.d.n.a(str);
    }

    public boolean b(String str) {
        return com.xiaomi.stat.d.n.b(str);
    }
}
