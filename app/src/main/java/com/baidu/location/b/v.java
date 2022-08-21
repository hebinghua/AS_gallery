package com.baidu.location.b;

import android.annotation.TargetApi;
import android.location.GnssNavigationMessage;
import android.text.TextUtils;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class v {
    private b a;
    private long b = 0;
    private long c = 0;

    /* loaded from: classes.dex */
    public static class a {
        private static v a = new v();
    }

    /* loaded from: classes.dex */
    public class b extends com.baidu.location.e.e {
        private boolean d = false;
        private String e = null;
        public boolean a = false;
        public long b = 0;

        public b() {
            this.j = new HashMap();
        }

        @Override // com.baidu.location.e.e
        public void a() {
            String b = com.baidu.location.e.b.a().b();
            if (b != null) {
                b = b + "&gnsst=" + this.b;
            }
            String a = m.a().a(b);
            String str = "null";
            String replaceAll = !TextUtils.isEmpty(a) ? a.trim().replaceAll("\r|\n", "") : str;
            String a2 = m.a().a(this.e);
            if (!TextUtils.isEmpty(a2)) {
                str = a2.trim().replaceAll("\r|\n", "");
            }
            try {
                this.j.put("info", URLEncoder.encode(replaceAll, "utf-8"));
                this.j.put("enl", URLEncoder.encode(str, "utf-8"));
            } catch (Exception unused) {
            }
        }

        public void a(String str, long j) {
            if (this.d) {
                return;
            }
            this.d = true;
            this.e = str;
            this.b = j;
            ExecutorService c = u.a().c();
            if (c != null) {
                a(c, "https://ofloc.map.baidu.com/locnu");
            } else {
                b("https://ofloc.map.baidu.com/locnu");
            }
        }

        @Override // com.baidu.location.e.e
        public void a(boolean z) {
            if (z && this.i != null) {
                try {
                    new JSONObject(this.i);
                    this.a = true;
                } catch (Throwable unused) {
                }
            }
            Map<String, Object> map = this.j;
            if (map != null) {
                map.clear();
            }
            this.d = false;
        }

        public boolean b() {
            return this.d;
        }
    }

    public static v a() {
        return a.a;
    }

    @TargetApi(24)
    public void a(GnssNavigationMessage gnssNavigationMessage, long j) {
        q.a().a(gnssNavigationMessage, j);
        this.b = System.currentTimeMillis();
        this.c = j;
    }

    public void b() {
        ArrayList<String> b2;
        if (this.b == 0 || Math.abs(System.currentTimeMillis() - this.b) >= 20000) {
            return;
        }
        if (this.a == null) {
            this.a = new b();
        }
        b bVar = this.a;
        if (bVar == null || bVar.b() || (b2 = q.a().b()) == null || b2.size() <= 0) {
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        Iterator<String> it = b2.iterator();
        while (it.hasNext()) {
            stringBuffer.append(it.next());
            i++;
            if (i != b2.size()) {
                stringBuffer.append(";");
            }
        }
        this.a.a(stringBuffer.toString(), this.c);
    }
}
