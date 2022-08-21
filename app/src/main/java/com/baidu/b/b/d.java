package com.baidu.b.b;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.baidu.b.b.a;
import com.baidu.b.e.a;
import com.baidu.b.h;
import java.io.File;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class d extends com.baidu.b.b.a {
    public a.C0004a d;
    private a e;

    /* loaded from: classes.dex */
    public class a {
        private long c;
        private h.a d;
        private boolean e;
        private int g;
        private com.baidu.b.f.b b = new com.baidu.b.f.b();
        private boolean f = true;

        public a() {
        }

        private boolean a(String str) {
            if (!TextUtils.isEmpty(str)) {
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    this.c = jSONObject.getLong("pub_lst_ts");
                    this.d = h.a(jSONObject.getString("pub_info"));
                    this.g = jSONObject.getInt("d_form_ver");
                    this.e = false;
                    return true;
                } catch (Exception unused) {
                }
            }
            return false;
        }

        public long a() {
            return this.c;
        }

        public boolean a(PackageInfo packageInfo) {
            String a = d.this.d.a(new File(packageInfo.applicationInfo.dataDir)).a("pub.dat", true);
            this.f = false;
            return a(a);
        }

        public h.a b() {
            return this.d;
        }
    }

    /* loaded from: classes.dex */
    public class b extends a.b {
        private int b;
        private String c;
        private long d;
        private long e;
        private long f;
        private h.a g;

        public b(String str) {
            super(d.this.d, str);
        }

        public void a(a aVar) {
            a(aVar.b());
            b(aVar.a());
        }

        @Override // com.baidu.b.b.a.b
        public void a(JSONObject jSONObject) {
            this.c = jSONObject.getString("pkg");
            this.e = jSONObject.getInt("tar_pkg_lst_pub_ts");
            this.d = jSONObject.getLong("last_fe_ts");
            this.g = h.a(jSONObject.getString("info"));
            this.f = jSONObject.getLong("tar_pkg_lst_up_ts");
            this.b = jSONObject.getInt("d_form_ver");
        }

        public boolean a(long j) {
            if (this.d != j) {
                this.d = j;
                a(true);
                return true;
            }
            return false;
        }

        public boolean a(h.a aVar) {
            if (!aVar.equals(this.g)) {
                this.g = aVar;
                a(true);
                return true;
            }
            return false;
        }

        public boolean a(String str) {
            if (!str.equals(this.c)) {
                this.c = str;
                a(true);
                return true;
            }
            return false;
        }

        @Override // com.baidu.b.b.a.b
        public void b(JSONObject jSONObject) {
            jSONObject.put("pkg", this.c);
            jSONObject.put("last_fe_ts", this.d);
            jSONObject.put("tar_pkg_lst_pub_ts", this.e);
            jSONObject.put("info", this.g.a());
            jSONObject.put("tar_pkg_lst_up_ts", this.f);
            jSONObject.put("d_form_ver", 1);
        }

        public boolean b(long j) {
            if (this.e != j) {
                this.e = j;
                a(true);
                return true;
            }
            return false;
        }

        public String c() {
            return this.c;
        }

        public boolean c(long j) {
            if (this.f != j) {
                this.f = j;
                a(true);
                return true;
            }
            return false;
        }

        public h.a d() {
            return this.g;
        }

        public long e() {
            return this.f;
        }
    }

    public d() {
        super("isc", 8000000L);
        this.e = new a();
    }

    @Override // com.baidu.b.b.a
    public a.e a(String str, a.d dVar) {
        PackageInfo packageInfo;
        h.a b2;
        b bVar = null;
        try {
            packageInfo = this.a.a.getPackageManager().getPackageInfo(str, 0);
        } catch (PackageManager.NameNotFoundException unused) {
            packageInfo = null;
        }
        if (packageInfo == null) {
            return a.e.a(-2);
        }
        if (dVar.a) {
            bVar = new b(str);
            bVar.a();
            if (str.equals(bVar.c()) && packageInfo.lastUpdateTime == bVar.e()) {
                b2 = bVar.d();
                return a.e.a(b2);
            }
        }
        a aVar = new a();
        if (!aVar.a(packageInfo)) {
            return a.e.a(-2);
        }
        if (dVar.a && bVar != null) {
            bVar.a(aVar);
            bVar.a(System.currentTimeMillis());
            bVar.c(packageInfo.lastUpdateTime);
            bVar.a(str);
            bVar.b();
        }
        b2 = aVar.b();
        return a.e.a(b2);
    }

    @Override // com.baidu.b.b.a
    public void a(a.c cVar) {
        this.d = this.b.a("isc");
    }
}
