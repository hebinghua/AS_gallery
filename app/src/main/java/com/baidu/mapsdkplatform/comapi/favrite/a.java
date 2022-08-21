package com.baidu.mapsdkplatform.comapi.favrite;

import android.os.Bundle;
import android.text.TextUtils;
import ch.qos.logback.core.joran.action.Action;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.platform.comapi.basestruct.Point;
import com.baidu.platform.comjni.map.favorite.NAFavorite;
import com.xiaomi.stat.b.h;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class a {
    private static a b;
    private NAFavorite a = null;
    private boolean c = false;
    private boolean d = false;
    private Vector<String> e = null;
    private Vector<String> f = null;
    private boolean g = false;
    private c h = new c();
    private b i = new b();

    /* renamed from: com.baidu.mapsdkplatform.comapi.favrite.a$a  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class C0018a implements Comparator<String> {
        public C0018a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(String str, String str2) {
            return str2.compareTo(str);
        }
    }

    /* loaded from: classes.dex */
    public class b {
        private long b;
        private long c;

        private b() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a() {
            this.b = System.currentTimeMillis();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void b() {
            this.c = System.currentTimeMillis();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean c() {
            return this.c - this.b > 1000;
        }
    }

    /* loaded from: classes.dex */
    public class c {
        private String b;
        private long c;
        private long d;

        private c() {
            this.c = 5000L;
            this.d = 0L;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String a() {
            return this.b;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(String str) {
            this.b = str;
            this.d = System.currentTimeMillis();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean b() {
            return TextUtils.isEmpty(this.b);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean c() {
            return true;
        }
    }

    private a() {
    }

    public static a a() {
        if (b == null) {
            synchronized (a.class) {
                if (b == null) {
                    a aVar = new a();
                    b = aVar;
                    aVar.h();
                }
            }
        }
        return b;
    }

    public static boolean g() {
        NAFavorite nAFavorite;
        a aVar = b;
        return (aVar == null || (nAFavorite = aVar.a) == null || !nAFavorite.d()) ? false : true;
    }

    private boolean h() {
        if (this.a == null) {
            NAFavorite nAFavorite = new NAFavorite();
            this.a = nAFavorite;
            if (nAFavorite.a() == 0) {
                this.a = null;
                return false;
            }
            j();
            i();
        }
        return true;
    }

    private boolean i() {
        if (this.a == null) {
            return false;
        }
        String str = SysOSUtil.getModuleFileName() + h.g;
        this.a.a(1);
        return this.a.a(str, "fav_poi", "fifo", 10, 501, -1);
    }

    private void j() {
        this.c = false;
        this.d = false;
    }

    public synchronized int a(String str, FavSyncPoi favSyncPoi) {
        if (this.a == null) {
            return 0;
        }
        if (str != null && !str.equals("") && favSyncPoi != null) {
            j();
            ArrayList<String> e = e();
            if ((e != null ? e.size() : 0) + 1 > 500) {
                return -2;
            }
            if (e != null && e.size() > 0) {
                Iterator<String> it = e.iterator();
                while (it.hasNext()) {
                    FavSyncPoi b2 = b(it.next());
                    if (b2 != null && str.equals(b2.b)) {
                        return -1;
                    }
                }
            }
            try {
                JSONObject jSONObject = new JSONObject();
                favSyncPoi.b = str;
                String valueOf = String.valueOf(System.currentTimeMillis());
                String str2 = valueOf + "_" + favSyncPoi.hashCode();
                favSyncPoi.h = valueOf;
                favSyncPoi.a = str2;
                jSONObject.put("bdetail", favSyncPoi.i);
                jSONObject.put("uspoiname", favSyncPoi.b);
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("x", favSyncPoi.c.getDoubleX());
                jSONObject2.put("y", favSyncPoi.c.getDoubleY());
                jSONObject.put("pt", jSONObject2);
                jSONObject.put("ncityid", favSyncPoi.e);
                jSONObject.put("npoitype", favSyncPoi.g);
                jSONObject.put("uspoiuid", favSyncPoi.f);
                jSONObject.put("addr", favSyncPoi.d);
                jSONObject.put("addtimesec", favSyncPoi.h);
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("Fav_Sync", jSONObject);
                jSONObject3.put("Fav_Content", favSyncPoi.j);
                if (!this.a.a(str2, jSONObject3.toString())) {
                    return 0;
                }
                j();
                return 1;
            } catch (JSONException unused) {
                return 0;
            } finally {
                g();
            }
        }
        return -1;
    }

    public synchronized boolean a(String str) {
        if (this.a == null) {
            return false;
        }
        if (str != null && !str.equals("")) {
            if (!c(str)) {
                return false;
            }
            j();
            return this.a.a(str);
        }
        return false;
    }

    public FavSyncPoi b(String str) {
        if (this.a != null && str != null && !str.equals("")) {
            try {
                if (!c(str)) {
                    return null;
                }
                FavSyncPoi favSyncPoi = new FavSyncPoi();
                String b2 = this.a.b(str);
                if (b2 != null && !b2.equals("")) {
                    JSONObject jSONObject = new JSONObject(b2);
                    JSONObject optJSONObject = jSONObject.optJSONObject("Fav_Sync");
                    String optString = jSONObject.optString("Fav_Content");
                    favSyncPoi.b = optJSONObject.optString("uspoiname");
                    JSONObject optJSONObject2 = optJSONObject.optJSONObject("pt");
                    favSyncPoi.c = new Point(optJSONObject2.optInt("x"), optJSONObject2.optInt("y"));
                    favSyncPoi.e = optJSONObject.optString("ncityid");
                    favSyncPoi.f = optJSONObject.optString("uspoiuid");
                    favSyncPoi.g = optJSONObject.optInt("npoitype");
                    favSyncPoi.d = optJSONObject.optString("addr");
                    favSyncPoi.h = optJSONObject.optString("addtimesec");
                    favSyncPoi.i = optJSONObject.optBoolean("bdetail");
                    favSyncPoi.j = optString;
                    favSyncPoi.a = str;
                    return favSyncPoi;
                }
                return null;
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (JSONException e2) {
                e2.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public void b() {
        a aVar = b;
        if (aVar != null) {
            NAFavorite nAFavorite = aVar.a;
            if (nAFavorite != null) {
                nAFavorite.b();
                b.a = null;
            }
            b = null;
        }
    }

    public synchronized boolean b(String str, FavSyncPoi favSyncPoi) {
        boolean z = false;
        if (this.a != null && str != null && !str.equals("") && favSyncPoi != null) {
            if (!c(str)) {
                return false;
            }
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("uspoiname", favSyncPoi.b);
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("x", favSyncPoi.c.getDoubleX());
                jSONObject2.put("y", favSyncPoi.c.getDoubleY());
                jSONObject.put("pt", jSONObject2);
                jSONObject.put("ncityid", favSyncPoi.e);
                jSONObject.put("npoitype", favSyncPoi.g);
                jSONObject.put("uspoiuid", favSyncPoi.f);
                jSONObject.put("addr", favSyncPoi.d);
                String valueOf = String.valueOf(System.currentTimeMillis());
                favSyncPoi.h = valueOf;
                jSONObject.put("addtimesec", valueOf);
                jSONObject.put("bdetail", false);
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("Fav_Sync", jSONObject);
                jSONObject3.put("Fav_Content", favSyncPoi.j);
                j();
                NAFavorite nAFavorite = this.a;
                if (nAFavorite != null) {
                    if (nAFavorite.b(str, jSONObject3.toString())) {
                        z = true;
                    }
                }
                return z;
            } catch (JSONException unused) {
                return false;
            }
        }
        return false;
    }

    public synchronized boolean c() {
        if (this.a == null) {
            return false;
        }
        j();
        boolean c2 = this.a.c();
        g();
        return c2;
    }

    public boolean c(String str) {
        return this.a != null && str != null && !str.equals("") && this.a.c(str);
    }

    public ArrayList<String> d() {
        String b2;
        if (this.a == null) {
            return null;
        }
        if (this.d && this.f != null) {
            return new ArrayList<>(this.f);
        }
        try {
            Bundle bundle = new Bundle();
            this.a.a(bundle);
            String[] stringArray = bundle.getStringArray("rstString");
            if (stringArray != null) {
                Vector<String> vector = this.f;
                if (vector == null) {
                    this.f = new Vector<>();
                } else {
                    vector.clear();
                }
                for (int i = 0; i < stringArray.length; i++) {
                    if (!stringArray[i].equals("data_version") && (b2 = this.a.b(stringArray[i])) != null && !b2.equals("")) {
                        this.f.add(stringArray[i]);
                    }
                }
                if (this.f.size() > 0) {
                    try {
                        Collections.sort(this.f, new C0018a());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.d = true;
                }
            } else {
                Vector<String> vector2 = this.f;
                if (vector2 != null) {
                    vector2.clear();
                    this.f = null;
                }
            }
            Vector<String> vector3 = this.f;
            if (vector3 != null && !vector3.isEmpty()) {
                return new ArrayList<>(this.f);
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public ArrayList<String> e() {
        if (this.a == null) {
            return null;
        }
        if (this.c && this.e != null) {
            return new ArrayList<>(this.e);
        }
        try {
            Bundle bundle = new Bundle();
            this.a.a(bundle);
            String[] stringArray = bundle.getStringArray("rstString");
            if (stringArray != null) {
                Vector<String> vector = this.e;
                if (vector == null) {
                    this.e = new Vector<>();
                } else {
                    vector.clear();
                }
                for (String str : stringArray) {
                    if (!str.equals("data_version")) {
                        this.e.add(str);
                    }
                }
                if (this.e.size() > 0) {
                    try {
                        Collections.sort(this.e, new C0018a());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.c = true;
                }
            } else {
                Vector<String> vector2 = this.e;
                if (vector2 != null) {
                    vector2.clear();
                    this.e = null;
                }
            }
            Vector<String> vector3 = this.e;
            if (vector3 != null && vector3.size() != 0) {
                return new ArrayList<>(this.e);
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public String f() {
        String b2;
        if (!this.i.c() || this.h.c() || this.h.b()) {
            this.i.a();
            if (this.a == null) {
                return null;
            }
            ArrayList<String> d = d();
            JSONObject jSONObject = new JSONObject();
            if (d != null) {
                try {
                    JSONArray jSONArray = new JSONArray();
                    int i = 0;
                    Iterator<String> it = d.iterator();
                    while (it.hasNext()) {
                        String next = it.next();
                        if (next != null && !next.equals("data_version") && (b2 = this.a.b(next)) != null && !b2.equals("")) {
                            JSONObject optJSONObject = new JSONObject(b2).optJSONObject("Fav_Sync");
                            optJSONObject.put(Action.KEY_ATTRIBUTE, next);
                            jSONArray.put(i, optJSONObject);
                            i++;
                        }
                    }
                    if (i > 0) {
                        jSONObject.put("favcontents", jSONArray);
                        jSONObject.put("favpoinum", i);
                    }
                } catch (JSONException unused) {
                    return null;
                }
            }
            this.i.b();
            this.h.a(jSONObject.toString());
            return this.h.a();
        }
        return this.h.a();
    }
}
