package com.xiaomi.push.service;

import android.content.Context;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import com.xiaomi.push.service.XMPushService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes3.dex */
public class bg {
    public static bg a;

    /* renamed from: a  reason: collision with other field name */
    public ConcurrentHashMap<String, HashMap<String, b>> f904a = new ConcurrentHashMap<>();

    /* renamed from: a  reason: collision with other field name */
    public List<a> f903a = new ArrayList();

    /* loaded from: classes3.dex */
    public interface a {
        void a();
    }

    /* loaded from: classes3.dex */
    public static class b {

        /* renamed from: a  reason: collision with other field name */
        public Context f905a;

        /* renamed from: a  reason: collision with other field name */
        public Messenger f907a;

        /* renamed from: a  reason: collision with other field name */
        public XMPushService f909a;

        /* renamed from: a  reason: collision with other field name */
        public j f912a;

        /* renamed from: a  reason: collision with other field name */
        public String f913a;

        /* renamed from: a  reason: collision with other field name */
        public boolean f915a;

        /* renamed from: b  reason: collision with other field name */
        public String f916b;
        public String c;
        public String d;
        public String e;
        public String f;
        public String g;
        public String h;
        public String i;

        /* renamed from: a  reason: collision with other field name */
        public c f911a = c.unbind;
        public int a = 0;

        /* renamed from: a  reason: collision with other field name */
        public List<a> f914a = new ArrayList();
        public c b = null;

        /* renamed from: b  reason: collision with other field name */
        public boolean f917b = false;

        /* renamed from: a  reason: collision with other field name */
        public XMPushService.c f908a = new XMPushService.c(this);

        /* renamed from: a  reason: collision with other field name */
        public IBinder.DeathRecipient f906a = null;

        /* renamed from: a  reason: collision with other field name */
        public final C0113b f910a = new C0113b();

        /* loaded from: classes3.dex */
        public interface a {
            void a(c cVar, c cVar2, int i);
        }

        /* renamed from: com.xiaomi.push.service.bg$b$b  reason: collision with other inner class name */
        /* loaded from: classes3.dex */
        public class C0113b extends XMPushService.j {

            /* renamed from: a  reason: collision with other field name */
            public String f918a;
            public int b;

            /* renamed from: b  reason: collision with other field name */
            public String f919b;
            public int c;

            public C0113b() {
                super(0);
            }

            public XMPushService.j a(int i, int i2, String str, String str2) {
                this.b = i;
                this.c = i2;
                this.f919b = str2;
                this.f918a = str;
                return this;
            }

            @Override // com.xiaomi.push.service.XMPushService.j
            /* renamed from: a */
            public String mo2550a() {
                return "notify job";
            }

            @Override // com.xiaomi.push.service.XMPushService.j
            /* renamed from: a  reason: collision with other method in class */
            public void mo2550a() {
                if (b.this.a(this.b, this.c, this.f919b)) {
                    b.this.a(this.b, this.c, this.f918a, this.f919b);
                    return;
                }
                com.xiaomi.channel.commonutils.logger.b.b(" ignore notify client :" + b.this.g);
            }
        }

        /* loaded from: classes3.dex */
        public class c implements IBinder.DeathRecipient {
            public final Messenger a;

            /* renamed from: a  reason: collision with other field name */
            public final b f920a;

            public c(b bVar, Messenger messenger) {
                this.f920a = bVar;
                this.a = messenger;
            }

            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                com.xiaomi.channel.commonutils.logger.b.b("peer died, chid = " + this.f920a.g);
                b.this.f909a.a(new bi(this, 0), 0L);
                if (!"9".equals(this.f920a.g) || !com.xiaomi.stat.c.c.a.equals(b.this.f909a.getPackageName())) {
                    return;
                }
                b.this.f909a.a(new bj(this, 0), 60000L);
            }
        }

        public b() {
        }

        public b(XMPushService xMPushService) {
            this.f909a = xMPushService;
            a(new bh(this));
        }

        public static String a(String str) {
            int lastIndexOf;
            return (!TextUtils.isEmpty(str) && (lastIndexOf = str.lastIndexOf(com.xiaomi.stat.b.h.g)) != -1) ? str.substring(lastIndexOf + 1) : "";
        }

        public long a() {
            return (((long) ((Math.random() * 20.0d) - 10.0d)) + ((this.a + 1) * 15)) * 1000;
        }

        public String a(int i) {
            return i != 1 ? i != 2 ? i != 3 ? "unknown" : "KICK" : "CLOSE" : "OPEN";
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m2496a() {
            try {
                Messenger messenger = this.f907a;
                if (messenger != null && this.f906a != null) {
                    messenger.getBinder().unlinkToDeath(this.f906a, 0);
                }
            } catch (Exception unused) {
            }
            this.b = null;
        }

        public final void a(int i, int i2, String str, String str2) {
            c cVar = this.f911a;
            this.b = cVar;
            if (i == 2) {
                this.f912a.a(this.f905a, this, i2);
            } else if (i == 3) {
                this.f912a.a(this.f905a, this, str2, str);
            } else if (i != 1) {
            } else {
                boolean z = cVar == c.binded;
                if (!z && "wait".equals(str2)) {
                    this.a++;
                } else if (z) {
                    this.a = 0;
                    if (this.f907a != null) {
                        try {
                            this.f907a.send(Message.obtain(null, 16, this.f909a.f823a));
                        } catch (RemoteException unused) {
                        }
                    }
                }
                this.f912a.a(this.f909a, this, z, i2, str);
            }
        }

        public void a(Messenger messenger) {
            m2496a();
            try {
                if (messenger != null) {
                    this.f907a = messenger;
                    this.f917b = true;
                    this.f906a = new c(this, messenger);
                    messenger.getBinder().linkToDeath(this.f906a, 0);
                } else {
                    com.xiaomi.channel.commonutils.logger.b.b("peer linked with old sdk chid = " + this.g);
                }
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.b("peer linkToDeath err: " + e.getMessage());
                this.f907a = null;
                this.f917b = false;
            }
        }

        public void a(a aVar) {
            synchronized (this.f914a) {
                this.f914a.add(aVar);
            }
        }

        public void a(c cVar, int i, int i2, String str, String str2) {
            boolean z;
            synchronized (this.f914a) {
                for (a aVar : this.f914a) {
                    aVar.a(this.f911a, cVar, i2);
                }
            }
            c cVar2 = this.f911a;
            int i3 = 0;
            if (cVar2 != cVar) {
                com.xiaomi.channel.commonutils.logger.b.m1859a(String.format("update the client %7$s status. %1$s->%2$s %3$s %4$s %5$s %6$s", cVar2, cVar, a(i), bk.a(i2), str, str2, this.g));
                this.f911a = cVar;
            }
            if (this.f912a == null) {
                com.xiaomi.channel.commonutils.logger.b.d("status changed while the client dispatcher is missing");
            } else if (cVar != c.binding) {
                if (this.b != null && (z = this.f917b)) {
                    i3 = (this.f907a == null || !z) ? 10100 : 1000;
                }
                this.f909a.b(this.f910a);
                if (b(i, i2, str2)) {
                    a(i, i2, str, str2);
                } else {
                    this.f909a.a(this.f910a.a(i, i2, str, str2), i3);
                }
            }
        }

        public final boolean a(int i, int i2, String str) {
            boolean z;
            StringBuilder sb;
            String str2;
            c cVar = this.b;
            if (cVar == null || !(z = this.f917b)) {
                return true;
            }
            if (cVar == this.f911a) {
                sb = new StringBuilder();
                str2 = " status recovered, don't notify client:";
            } else if (this.f907a != null && z) {
                com.xiaomi.channel.commonutils.logger.b.b("Peer alive notify status to client:" + this.g);
                return true;
            } else {
                sb = new StringBuilder();
                str2 = "peer died, ignore notify ";
            }
            sb.append(str2);
            sb.append(this.g);
            com.xiaomi.channel.commonutils.logger.b.b(sb.toString());
            return false;
        }

        public void b(a aVar) {
            synchronized (this.f914a) {
                this.f914a.remove(aVar);
            }
        }

        public final boolean b(int i, int i2, String str) {
            if (i == 1) {
                return this.f911a != c.binded && this.f909a.m2428c() && i2 != 21 && (i2 != 7 || !"wait".equals(str));
            } else if (i == 2) {
                return this.f909a.m2428c();
            } else {
                if (i == 3) {
                    return !"wait".equals(str);
                }
                return false;
            }
        }
    }

    /* loaded from: classes3.dex */
    public enum c {
        unbind,
        binding,
        binded
    }

    public static synchronized bg a() {
        bg bgVar;
        synchronized (bg.class) {
            if (a == null) {
                a = new bg();
            }
            bgVar = a;
        }
        return bgVar;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized int m2488a() {
        return this.f904a.size();
    }

    public synchronized b a(String str, String str2) {
        HashMap<String, b> hashMap = this.f904a.get(str);
        if (hashMap == null) {
            return null;
        }
        return hashMap.get(a(str2));
    }

    public final String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int indexOf = str.indexOf("@");
        return indexOf > 0 ? str.substring(0, indexOf) : str;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized ArrayList<b> m2489a() {
        ArrayList<b> arrayList;
        arrayList = new ArrayList<>();
        for (HashMap<String, b> hashMap : this.f904a.values()) {
            arrayList.addAll(hashMap.values());
        }
        return arrayList;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized Collection<b> m2490a(String str) {
        if (!this.f904a.containsKey(str)) {
            return new ArrayList();
        }
        return ((HashMap) this.f904a.get(str).clone()).values();
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized List<String> m2491a(String str) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        for (HashMap<String, b> hashMap : this.f904a.values()) {
            for (b bVar : hashMap.values()) {
                if (str.equals(bVar.f913a)) {
                    arrayList.add(bVar.g);
                }
            }
        }
        return arrayList;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m2492a() {
        Iterator<b> it = m2489a().iterator();
        while (it.hasNext()) {
            it.next().m2496a();
        }
        this.f904a.clear();
    }

    public synchronized void a(Context context) {
        for (HashMap<String, b> hashMap : this.f904a.values()) {
            for (b bVar : hashMap.values()) {
                bVar.a(c.unbind, 1, 3, (String) null, (String) null);
            }
        }
    }

    public synchronized void a(Context context, int i) {
        for (HashMap<String, b> hashMap : this.f904a.values()) {
            for (b bVar : hashMap.values()) {
                bVar.a(c.unbind, 2, i, (String) null, (String) null);
            }
        }
    }

    public synchronized void a(a aVar) {
        this.f903a.add(aVar);
    }

    public synchronized void a(b bVar) {
        HashMap<String, b> hashMap = this.f904a.get(bVar.g);
        if (hashMap == null) {
            hashMap = new HashMap<>();
            this.f904a.put(bVar.g, hashMap);
        }
        hashMap.put(a(bVar.f916b), bVar);
        com.xiaomi.channel.commonutils.logger.b.m1859a("add active client. " + bVar.f913a);
        for (a aVar : this.f903a) {
            aVar.a();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m2493a(String str) {
        HashMap<String, b> hashMap = this.f904a.get(str);
        if (hashMap != null) {
            for (b bVar : hashMap.values()) {
                bVar.m2496a();
            }
            hashMap.clear();
            this.f904a.remove(str);
        }
        for (a aVar : this.f903a) {
            aVar.a();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m2494a(String str, String str2) {
        HashMap<String, b> hashMap = this.f904a.get(str);
        if (hashMap != null) {
            b bVar = hashMap.get(a(str2));
            if (bVar != null) {
                bVar.m2496a();
            }
            hashMap.remove(a(str2));
            if (hashMap.isEmpty()) {
                this.f904a.remove(str);
            }
        }
        for (a aVar : this.f903a) {
            aVar.a();
        }
    }

    public synchronized void b() {
        this.f903a.clear();
    }
}
