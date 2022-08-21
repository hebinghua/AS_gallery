package com.xiaomi.push;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.push.service.module.PushChannelRegion;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public class cv {
    public static Context a;

    /* renamed from: a  reason: collision with other field name */
    public static a f174a;

    /* renamed from: a  reason: collision with other field name */
    public static cv f175a;
    public static String c;
    public static String d;

    /* renamed from: a  reason: collision with other field name */
    public long f177a;

    /* renamed from: a  reason: collision with other field name */
    public cu f178a;

    /* renamed from: a  reason: collision with other field name */
    public b f179a;

    /* renamed from: a  reason: collision with other field name */
    public String f180a;

    /* renamed from: a  reason: collision with other field name */
    public final Map<String, cs> f181a;

    /* renamed from: b  reason: collision with other field name */
    public final long f182b;

    /* renamed from: b  reason: collision with other field name */
    public String f183b;

    /* renamed from: c  reason: collision with other field name */
    public long f184c;
    public static final Map<String, cr> b = new HashMap();

    /* renamed from: a  reason: collision with other field name */
    public static boolean f176a = false;

    /* loaded from: classes3.dex */
    public interface a {
        cv a(Context context, cu cuVar, b bVar, String str);
    }

    /* loaded from: classes3.dex */
    public interface b {
        String a(String str);
    }

    public cv(Context context, cu cuVar, b bVar, String str) {
        this(context, cuVar, bVar, str, null, null);
    }

    public cv(Context context, cu cuVar, b bVar, String str, String str2, String str3) {
        this.f181a = new HashMap();
        this.f180a = "0";
        this.f177a = 0L;
        this.f182b = 15L;
        this.f184c = 0L;
        this.f183b = "isp_prov_city_country_ip";
        this.f179a = bVar;
        this.f178a = cuVar == null ? new cw(this) : cuVar;
        this.f180a = str;
        c = str2 == null ? context.getPackageName() : str2;
        d = str3 == null ? f() : str3;
    }

    public static synchronized cv a() {
        cv cvVar;
        synchronized (cv.class) {
            cvVar = f175a;
            if (cvVar == null) {
                throw new IllegalStateException("the host manager is not initialized yet.");
            }
        }
        return cvVar;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static String m2025a() {
        NetworkInfo activeNetworkInfo;
        Context context = a;
        if (context == null) {
            return "unknown";
        }
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null) {
                return "unknown";
            }
            if (activeNetworkInfo.getType() == 1) {
                return "WIFI-UNKNOWN";
            }
            return activeNetworkInfo.getTypeName() + "-" + activeNetworkInfo.getSubtypeName();
        } catch (Throwable unused) {
            return "unknown";
        }
    }

    public static String a(String str) {
        try {
            int length = str.length();
            byte[] bytes = str.getBytes(Keyczar.DEFAULT_ENCODING);
            for (int i = 0; i < bytes.length; i++) {
                byte b2 = bytes[i];
                int i2 = b2 & 240;
                if (i2 != 240) {
                    bytes[i] = (byte) (((b2 & 15) ^ ((byte) (((b2 >> 4) + length) & 15))) | i2);
                }
            }
            return new String(bytes);
        } catch (UnsupportedEncodingException unused) {
            return str;
        }
    }

    public static synchronized void a(Context context, cu cuVar, b bVar, String str, String str2, String str3) {
        synchronized (cv.class) {
            Context applicationContext = context.getApplicationContext();
            a = applicationContext;
            if (applicationContext == null) {
                a = context;
            }
            if (f175a == null) {
                a aVar = f174a;
                if (aVar == null) {
                    f175a = new cv(context, cuVar, bVar, str, str2, str3);
                } else {
                    f175a = aVar.a(context, cuVar, bVar, str);
                }
            }
        }
    }

    public static synchronized void a(a aVar) {
        synchronized (cv.class) {
            f174a = aVar;
            f175a = null;
        }
    }

    public static void a(String str, String str2) {
        Map<String, cr> map = b;
        cr crVar = map.get(str);
        synchronized (map) {
            if (crVar == null) {
                cr crVar2 = new cr(str);
                crVar2.a(CoreConstants.MILLIS_IN_ONE_WEEK);
                crVar2.m2018a(str2);
                map.put(str, crVar2);
            } else {
                crVar.m2018a(str2);
            }
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public cr m2026a(String str) {
        if (!TextUtils.isEmpty(str)) {
            return a(new URL(str).getHost(), true);
        }
        throw new IllegalArgumentException("the url is empty");
    }

    public cr a(String str, boolean z) {
        cr e;
        if (!TextUtils.isEmpty(str)) {
            if (!this.f178a.a(str)) {
                return null;
            }
            cr c2 = c(str);
            return (c2 == null || !c2.b()) ? (!z || !bj.b(a) || (e = e(str)) == null) ? new cx(this, str, c2) : e : c2;
        }
        throw new IllegalArgumentException("the host is empty");
    }

    public String a(ArrayList<String> arrayList, String str, String str2, boolean z) {
        ArrayList<String> arrayList2 = new ArrayList<>();
        ArrayList<bi> arrayList3 = new ArrayList();
        arrayList3.add(new bg(nexExportFormat.TAG_FORMAT_TYPE, str));
        if (str.equals("wap")) {
            arrayList3.add(new bg("conpt", a(bj.m1969a(a))));
        }
        if (z) {
            arrayList3.add(new bg("reserved", "1"));
        }
        arrayList3.add(new bg(nexExportFormat.TAG_FORMAT_UUID, str2));
        arrayList3.add(new bg("list", bp.a(arrayList, ",")));
        arrayList3.add(new bg("countrycode", com.xiaomi.push.service.a.a(a).b()));
        String b2 = b();
        cr c2 = c(b2);
        String format = String.format(Locale.US, "https://%1$s/gslb/?ver=4.0", b2);
        if (c2 == null) {
            arrayList2.add(format);
            Map<String, cr> map = b;
            synchronized (map) {
                cr crVar = map.get(b2);
                if (crVar != null) {
                    Iterator<String> it = crVar.a(true).iterator();
                    while (it.hasNext()) {
                        arrayList2.add(String.format(Locale.US, "https://%1$s/gslb/?ver=4.0", it.next()));
                    }
                }
            }
        } else {
            arrayList2 = c2.a(format);
        }
        Iterator<String> it2 = arrayList2.iterator();
        IOException e = null;
        while (it2.hasNext()) {
            Uri.Builder buildUpon = Uri.parse(it2.next()).buildUpon();
            for (bi biVar : arrayList3) {
                buildUpon.appendQueryParameter(biVar.a(), biVar.b());
            }
            try {
                b bVar = this.f179a;
                return bVar == null ? bj.a(a, new URL(buildUpon.toString())) : bVar.a(buildUpon.toString());
            } catch (IOException e2) {
                e = e2;
            }
        }
        if (e == null) {
            return null;
        }
        com.xiaomi.channel.commonutils.logger.b.m1859a("network exception: " + e.getMessage());
        throw e;
    }

    public final ArrayList<cr> a(ArrayList<String> arrayList) {
        JSONObject jSONObject;
        JSONObject jSONObject2;
        m2034d();
        synchronized (this.f181a) {
            m2030a();
            for (String str : this.f181a.keySet()) {
                if (!arrayList.contains(str)) {
                    arrayList.add(str);
                }
            }
        }
        Map<String, cr> map = b;
        synchronized (map) {
            for (Object obj : map.values().toArray()) {
                cr crVar = (cr) obj;
                if (!crVar.b()) {
                    b.remove(crVar.f171b);
                }
            }
        }
        if (!arrayList.contains(b())) {
            arrayList.add(b());
        }
        ArrayList<cr> arrayList2 = new ArrayList<>(arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(null);
        }
        try {
            String str2 = bj.e(a) ? "wifi" : "wap";
            String a2 = a(arrayList, str2, this.f180a, true);
            if (!TextUtils.isEmpty(a2)) {
                JSONObject jSONObject3 = new JSONObject(a2);
                com.xiaomi.channel.commonutils.logger.b.b(a2);
                if ("OK".equalsIgnoreCase(jSONObject3.getString("S"))) {
                    JSONObject jSONObject4 = jSONObject3.getJSONObject("R");
                    String string = jSONObject4.getString("province");
                    String string2 = jSONObject4.getString("city");
                    String string3 = jSONObject4.getString("isp");
                    String string4 = jSONObject4.getString("ip");
                    String string5 = jSONObject4.getString("country");
                    JSONObject jSONObject5 = jSONObject4.getJSONObject(str2);
                    com.xiaomi.channel.commonutils.logger.b.c("get bucket: net=" + string3 + ", hosts=" + jSONObject5.toString());
                    int i2 = 0;
                    while (i2 < arrayList.size()) {
                        String str3 = arrayList.get(i2);
                        JSONArray optJSONArray = jSONObject5.optJSONArray(str3);
                        if (optJSONArray == null) {
                            com.xiaomi.channel.commonutils.logger.b.m1859a("no bucket found for " + str3);
                            jSONObject = jSONObject5;
                        } else {
                            cr crVar2 = new cr(str3);
                            int i3 = 0;
                            while (i3 < optJSONArray.length()) {
                                String string6 = optJSONArray.getString(i3);
                                if (!TextUtils.isEmpty(string6)) {
                                    jSONObject2 = jSONObject5;
                                    crVar2.a(new da(string6, optJSONArray.length() - i3));
                                } else {
                                    jSONObject2 = jSONObject5;
                                }
                                i3++;
                                jSONObject5 = jSONObject2;
                            }
                            jSONObject = jSONObject5;
                            arrayList2.set(i2, crVar2);
                            crVar2.g = string5;
                            crVar2.c = string;
                            crVar2.e = string3;
                            crVar2.f = string4;
                            crVar2.d = string2;
                            if (jSONObject4.has("stat-percent")) {
                                crVar2.a(jSONObject4.getDouble("stat-percent"));
                            }
                            if (jSONObject4.has("stat-domain")) {
                                crVar2.b(jSONObject4.getString("stat-domain"));
                            }
                            if (jSONObject4.has("ttl")) {
                                crVar2.a(jSONObject4.getInt("ttl") * 1000);
                            }
                            m2029a(crVar2.a());
                        }
                        i2++;
                        jSONObject5 = jSONObject;
                    }
                    JSONObject optJSONObject = jSONObject4.optJSONObject("reserved");
                    if (optJSONObject != null) {
                        long j = CoreConstants.MILLIS_IN_ONE_WEEK;
                        if (jSONObject4.has("reserved-ttl")) {
                            j = jSONObject4.getInt("reserved-ttl") * 1000;
                        }
                        Iterator<String> keys = optJSONObject.keys();
                        while (keys.hasNext()) {
                            String next = keys.next();
                            JSONArray optJSONArray2 = optJSONObject.optJSONArray(next);
                            if (optJSONArray2 == null) {
                                com.xiaomi.channel.commonutils.logger.b.m1859a("no bucket found for " + next);
                            } else {
                                cr crVar3 = new cr(next);
                                crVar3.a(j);
                                for (int i4 = 0; i4 < optJSONArray2.length(); i4++) {
                                    String string7 = optJSONArray2.getString(i4);
                                    if (!TextUtils.isEmpty(string7)) {
                                        crVar3.a(new da(string7, optJSONArray2.length() - i4));
                                    }
                                }
                                Map<String, cr> map2 = b;
                                synchronized (map2) {
                                    if (this.f178a.a(next)) {
                                        map2.put(next, crVar3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("failed to get bucket " + e.getMessage());
        }
        for (int i5 = 0; i5 < arrayList.size(); i5++) {
            cr crVar4 = arrayList2.get(i5);
            if (crVar4 != null) {
                a(arrayList.get(i5), crVar4);
            }
        }
        m2033c();
        return arrayList2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public JSONObject m2027a() {
        JSONObject jSONObject;
        synchronized (this.f181a) {
            jSONObject = new JSONObject();
            jSONObject.put("ver", 2);
            JSONArray jSONArray = new JSONArray();
            for (cs csVar : this.f181a.values()) {
                jSONArray.put(csVar.m2022a());
            }
            jSONObject.put("data", jSONArray);
            JSONArray jSONArray2 = new JSONArray();
            for (cr crVar : b.values()) {
                jSONArray2.put(crVar.m2017a());
            }
            jSONObject.put("reserved", jSONArray2);
        }
        return jSONObject;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2028a() {
        synchronized (this.f181a) {
            this.f181a.clear();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2029a(String str) {
        this.f183b = str;
    }

    public void a(String str, cr crVar) {
        if (TextUtils.isEmpty(str) || crVar == null) {
            throw new IllegalArgumentException("the argument is invalid " + str + ", " + crVar);
        } else if (!this.f178a.a(str)) {
        } else {
            synchronized (this.f181a) {
                m2030a();
                if (this.f181a.containsKey(str)) {
                    this.f181a.get(str).a(crVar);
                } else {
                    cs csVar = new cs(str);
                    csVar.a(crVar);
                    this.f181a.put(str, csVar);
                }
            }
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2030a() {
        synchronized (this.f181a) {
            if (!f176a) {
                f176a = true;
                this.f181a.clear();
                String d2 = d();
                if (TextUtils.isEmpty(d2)) {
                    return false;
                }
                m2032b(d2);
                com.xiaomi.channel.commonutils.logger.b.b("loading the new hosts succeed");
                return true;
            }
            return true;
        }
    }

    public cr b(String str) {
        return a(str, true);
    }

    public String b() {
        String a2 = com.xiaomi.push.service.a.a(a).a();
        return (TextUtils.isEmpty(a2) || PushChannelRegion.China.name().equals(a2)) ? "resolver.msg.xiaomi.net" : "resolver.msg.global.xiaomi.net";
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m2031b() {
        ArrayList<String> arrayList;
        synchronized (this.f181a) {
            m2030a();
            arrayList = new ArrayList<>(this.f181a.keySet());
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                cs csVar = this.f181a.get(arrayList.get(size));
                if (csVar != null && csVar.a() != null) {
                    arrayList.remove(size);
                }
            }
        }
        ArrayList<cr> a2 = a(arrayList);
        for (int i = 0; i < arrayList.size(); i++) {
            if (a2.get(i) != null) {
                a(arrayList.get(i), a2.get(i));
            }
        }
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m2032b(String str) {
        synchronized (this.f181a) {
            this.f181a.clear();
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.optInt("ver") != 2) {
                throw new JSONException("Bad version");
            }
            JSONArray optJSONArray = jSONObject.optJSONArray("data");
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    cs a2 = new cs().a(optJSONArray.getJSONObject(i));
                    this.f181a.put(a2.m2020a(), a2);
                }
            }
            JSONArray optJSONArray2 = jSONObject.optJSONArray("reserved");
            if (optJSONArray2 != null) {
                for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                    JSONObject jSONObject2 = optJSONArray2.getJSONObject(i2);
                    String optString = jSONObject2.optString("host");
                    if (!TextUtils.isEmpty(optString)) {
                        try {
                            cr a3 = new cr(optString).a(jSONObject2);
                            b.put(a3.f171b, a3);
                            com.xiaomi.channel.commonutils.logger.b.m1859a("load local reserved host for " + a3.f171b);
                        } catch (JSONException unused) {
                            com.xiaomi.channel.commonutils.logger.b.m1859a("parse reserved host fail.");
                        }
                    }
                }
            }
        }
    }

    public cr c(String str) {
        cs csVar;
        cr a2;
        synchronized (this.f181a) {
            m2030a();
            csVar = this.f181a.get(str);
        }
        if (csVar == null || (a2 = csVar.a()) == null) {
            return null;
        }
        return a2;
    }

    public String c() {
        StringBuilder sb = new StringBuilder();
        synchronized (this.f181a) {
            for (Map.Entry<String, cs> entry : this.f181a.entrySet()) {
                sb.append(entry.getKey());
                sb.append(":\n");
                sb.append(entry.getValue().toString());
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /* renamed from: c  reason: collision with other method in class */
    public void m2033c() {
        synchronized (this.f181a) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(a.openFileOutput(e(), 0)));
                String jSONObject = m2027a().toString();
                if (!TextUtils.isEmpty(jSONObject)) {
                    bufferedWriter.write(jSONObject);
                }
                bufferedWriter.close();
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("persist bucket failure: " + e.getMessage());
            }
        }
    }

    public cr d(String str) {
        cr crVar;
        Map<String, cr> map = b;
        synchronized (map) {
            crVar = map.get(str);
        }
        return crVar;
    }

    public String d() {
        BufferedReader bufferedReader;
        File file;
        try {
            file = new File(a.getFilesDir(), e());
        } catch (Throwable th) {
            th = th;
            bufferedReader = null;
        }
        if (!file.isFile()) {
            ab.a((Closeable) null);
            return null;
        }
        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        try {
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    return sb.toString();
                }
                sb.append(readLine);
            }
        } catch (Throwable th2) {
            th = th2;
            try {
                com.xiaomi.channel.commonutils.logger.b.m1859a("load host exception " + th.getMessage());
                return null;
            } finally {
                ab.a(bufferedReader);
            }
        }
    }

    /* renamed from: d  reason: collision with other method in class */
    public void m2034d() {
        String next;
        synchronized (this.f181a) {
            for (cs csVar : this.f181a.values()) {
                csVar.a(true);
            }
            while (true) {
                for (boolean z = false; !z; z = true) {
                    Iterator<String> it = this.f181a.keySet().iterator();
                    while (it.hasNext()) {
                        next = it.next();
                        if (this.f181a.get(next).m2021a().isEmpty()) {
                            break;
                        }
                    }
                }
                this.f181a.remove(next);
            }
        }
    }

    public cr e(String str) {
        if (System.currentTimeMillis() - this.f184c > this.f177a * 60 * 1000) {
            this.f184c = System.currentTimeMillis();
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(str);
            cr crVar = a(arrayList).get(0);
            if (crVar != null) {
                this.f177a = 0L;
                return crVar;
            }
            long j = this.f177a;
            if (j >= 15) {
                return null;
            }
            this.f177a = j + 1;
            return null;
        }
        return null;
    }

    public String e() {
        if (com.xiaomi.stat.c.c.a.equals(c)) {
            return c;
        }
        return c + ":pushservice";
    }

    public final String f() {
        try {
            PackageInfo packageInfo = a.getPackageManager().getPackageInfo(a.getPackageName(), 16384);
            return packageInfo != null ? packageInfo.versionName : "0";
        } catch (Exception unused) {
            return "0";
        }
    }
}
