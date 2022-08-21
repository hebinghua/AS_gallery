package com.xiaomi.push;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/* loaded from: classes3.dex */
public class gk implements go {
    public String a;

    /* renamed from: a  reason: collision with other field name */
    public List<gk> f414a;

    /* renamed from: a  reason: collision with other field name */
    public String[] f415a;
    public String b;

    /* renamed from: b  reason: collision with other field name */
    public String[] f416b;
    public String c;

    public gk(String str, String str2, String[] strArr, String[] strArr2) {
        this.f415a = null;
        this.f416b = null;
        this.f414a = null;
        this.a = str;
        this.b = str2;
        this.f415a = strArr;
        this.f416b = strArr2;
    }

    public gk(String str, String str2, String[] strArr, String[] strArr2, String str3, List<gk> list) {
        this.f415a = null;
        this.f416b = null;
        this.f414a = null;
        this.a = str;
        this.b = str2;
        this.f415a = strArr;
        this.f416b = strArr2;
        this.c = str3;
        this.f414a = list;
    }

    public static gk a(Bundle bundle) {
        ArrayList arrayList;
        String string = bundle.getString("ext_ele_name");
        String string2 = bundle.getString("ext_ns");
        String string3 = bundle.getString("ext_text");
        Bundle bundle2 = bundle.getBundle("attributes");
        Set<String> keySet = bundle2.keySet();
        String[] strArr = new String[keySet.size()];
        String[] strArr2 = new String[keySet.size()];
        int i = 0;
        for (String str : keySet) {
            strArr[i] = str;
            strArr2[i] = bundle2.getString(str);
            i++;
        }
        if (bundle.containsKey("children")) {
            Parcelable[] parcelableArray = bundle.getParcelableArray("children");
            ArrayList arrayList2 = new ArrayList(parcelableArray.length);
            for (Parcelable parcelable : parcelableArray) {
                arrayList2.add(a((Bundle) parcelable));
            }
            arrayList = arrayList2;
        } else {
            arrayList = null;
        }
        return new gk(string, string2, strArr, strArr2, string3, arrayList);
    }

    public static Parcelable[] a(List<gk> list) {
        return a((gk[]) list.toArray(new gk[list.size()]));
    }

    public static Parcelable[] a(gk[] gkVarArr) {
        if (gkVarArr == null) {
            return null;
        }
        Parcelable[] parcelableArr = new Parcelable[gkVarArr.length];
        for (int i = 0; i < gkVarArr.length; i++) {
            parcelableArr[i] = gkVarArr[i].m2193a();
        }
        return parcelableArr;
    }

    public Bundle a() {
        Bundle bundle = new Bundle();
        bundle.putString("ext_ele_name", this.a);
        bundle.putString("ext_ns", this.b);
        bundle.putString("ext_text", this.c);
        Bundle bundle2 = new Bundle();
        String[] strArr = this.f415a;
        if (strArr != null && strArr.length > 0) {
            int i = 0;
            while (true) {
                String[] strArr2 = this.f415a;
                if (i >= strArr2.length) {
                    break;
                }
                bundle2.putString(strArr2[i], this.f416b[i]);
                i++;
            }
        }
        bundle.putBundle("attributes", bundle2);
        List<gk> list = this.f414a;
        if (list != null && list.size() > 0) {
            bundle.putParcelableArray("children", a(this.f414a));
        }
        return bundle;
    }

    /* renamed from: a  reason: collision with other method in class */
    public Parcelable m2193a() {
        return a();
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2194a() {
        return this.a;
    }

    public String a(String str) {
        if (str != null) {
            if (this.f415a == null) {
                return null;
            }
            int i = 0;
            while (true) {
                String[] strArr = this.f415a;
                if (i >= strArr.length) {
                    return null;
                }
                if (str.equals(strArr[i])) {
                    return this.f416b[i];
                }
                i++;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2195a(String str) {
        if (!TextUtils.isEmpty(str)) {
            str = gy.a(str);
        }
        this.c = str;
    }

    public String b() {
        return this.b;
    }

    public String c() {
        return !TextUtils.isEmpty(this.c) ? gy.b(this.c) : this.c;
    }

    @Override // com.xiaomi.push.go
    public String d() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(this.a);
        if (!TextUtils.isEmpty(this.b)) {
            sb.append(" ");
            sb.append("xmlns=");
            sb.append("\"");
            sb.append(this.b);
            sb.append("\"");
        }
        String[] strArr = this.f415a;
        if (strArr != null && strArr.length > 0) {
            for (int i = 0; i < this.f415a.length; i++) {
                if (!TextUtils.isEmpty(this.f416b[i])) {
                    sb.append(" ");
                    sb.append(this.f415a[i]);
                    sb.append("=\"");
                    sb.append(gy.a(this.f416b[i]));
                    sb.append("\"");
                }
            }
        }
        if (!TextUtils.isEmpty(this.c)) {
            sb.append(">");
            sb.append(this.c);
        } else {
            List<gk> list = this.f414a;
            if (list == null || list.size() <= 0) {
                sb.append("/>");
                return sb.toString();
            }
            sb.append(">");
            for (gk gkVar : this.f414a) {
                sb.append(gkVar.d());
            }
        }
        sb.append("</");
        sb.append(this.a);
        sb.append(">");
        return sb.toString();
    }

    public String toString() {
        return d();
    }
}
