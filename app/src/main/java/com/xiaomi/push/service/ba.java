package com.xiaomi.push.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;
import com.xiaomi.push.ho;
import com.xiaomi.push.hp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes3.dex */
public class ba {
    public static volatile ba a;

    /* renamed from: a  reason: collision with other field name */
    public SharedPreferences f900a;

    /* renamed from: a  reason: collision with other field name */
    public HashSet<a> f901a = new HashSet<>();
    public SharedPreferences b;

    /* loaded from: classes3.dex */
    public static abstract class a implements Runnable {
        public String mDescription;
        public int mId;

        public a(int i, String str) {
            this.mId = i;
            this.mDescription = str;
        }

        public boolean equals(Object obj) {
            return (obj instanceof a) && this.mId == ((a) obj).mId;
        }

        public int hashCode() {
            return this.mId;
        }

        public abstract void onCallback();

        @Override // java.lang.Runnable
        public final void run() {
            onCallback();
        }
    }

    public ba(Context context) {
        this.f900a = context.getSharedPreferences("mipush_oc_normal", 0);
        this.b = context.getSharedPreferences("mipush_oc_custom", 0);
    }

    public static ba a(Context context) {
        if (a == null) {
            synchronized (ba.class) {
                if (a == null) {
                    a = new ba(context);
                }
            }
        }
        return a;
    }

    public int a(int i, int i2) {
        try {
            String a2 = a(i);
            return this.b.contains(a2) ? this.b.getInt(a2, 0) : this.f900a.contains(a2) ? this.f900a.getInt(a2, 0) : i2;
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.m1859a(i + " oc int error " + e);
            return i2;
        }
    }

    public int a(hp hpVar, int i) {
        try {
            return this.f900a.getInt(a(hpVar), i);
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.m1859a(hpVar + " version error " + e);
            return i;
        }
    }

    public long a(int i, long j) {
        try {
            String a2 = a(i);
            return this.b.contains(a2) ? this.b.getLong(a2, 0L) : this.f900a.contains(a2) ? this.f900a.getLong(a2, 0L) : j;
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.m1859a(i + " oc long error " + e);
            return j;
        }
    }

    public final String a(int i) {
        return "oc_" + i;
    }

    public String a(int i, String str) {
        try {
            String a2 = a(i);
            return this.b.contains(a2) ? this.b.getString(a2, null) : this.f900a.contains(a2) ? this.f900a.getString(a2, null) : str;
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.m1859a(i + " oc string error " + e);
            return str;
        }
    }

    public final String a(hp hpVar) {
        return "oc_version_" + hpVar.a();
    }

    public synchronized void a() {
        this.f901a.clear();
    }

    public final void a(SharedPreferences.Editor editor, Pair<Integer, Object> pair, String str) {
        Object obj = pair.second;
        if (obj instanceof Integer) {
            editor.putInt(str, ((Integer) obj).intValue());
        } else if (obj instanceof Long) {
            editor.putLong(str, ((Long) obj).longValue());
        } else if (!(obj instanceof String)) {
            if (!(obj instanceof Boolean)) {
                return;
            }
            editor.putBoolean(str, ((Boolean) obj).booleanValue());
        } else {
            String str2 = (String) obj;
            if (str.equals(a(ho.AppIsInstalledList.a()))) {
                str2 = com.xiaomi.push.bm.a(str2);
            }
            editor.putString(str, str2);
        }
    }

    public synchronized void a(a aVar) {
        if (!this.f901a.contains(aVar)) {
            this.f901a.add(aVar);
        }
    }

    public void a(List<Pair<Integer, Object>> list) {
        if (com.xiaomi.push.ag.a(list)) {
            return;
        }
        SharedPreferences.Editor edit = this.b.edit();
        for (Pair<Integer, Object> pair : list) {
            Object obj = pair.first;
            if (obj != null) {
                String a2 = a(((Integer) obj).intValue());
                if (pair.second == null) {
                    edit.remove(a2);
                } else {
                    a(edit, pair, a2);
                }
            }
        }
        edit.apply();
    }

    public void a(List<Pair<hp, Integer>> list, List<Pair<Integer, Object>> list2) {
        if (com.xiaomi.push.ag.a(list) || com.xiaomi.push.ag.a(list2)) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("not update oc, because versions or configs are empty");
            return;
        }
        SharedPreferences.Editor edit = this.f900a.edit();
        edit.clear();
        for (Pair<hp, Integer> pair : list) {
            Object obj = pair.first;
            if (obj != null && pair.second != null) {
                edit.putInt(a((hp) obj), ((Integer) pair.second).intValue());
            }
        }
        for (Pair<Integer, Object> pair2 : list2) {
            Object obj2 = pair2.first;
            if (obj2 != null && pair2.second != null) {
                a(edit, pair2, a(((Integer) obj2).intValue()));
            }
        }
        edit.apply();
    }

    public boolean a(int i, boolean z) {
        try {
            String a2 = a(i);
            return this.b.contains(a2) ? this.b.getBoolean(a2, false) : this.f900a.contains(a2) ? this.f900a.getBoolean(a2, false) : z;
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.m1859a(i + " oc boolean error " + e);
            return z;
        }
    }

    public void b() {
        com.xiaomi.channel.commonutils.logger.b.c("OC_Callback : receive new oc data");
        HashSet hashSet = new HashSet();
        synchronized (this) {
            hashSet.addAll(this.f901a);
        }
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            a aVar = (a) it.next();
            if (aVar != null) {
                aVar.run();
            }
        }
        hashSet.clear();
    }
}
