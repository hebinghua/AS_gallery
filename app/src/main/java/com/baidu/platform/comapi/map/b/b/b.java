package com.baidu.platform.comapi.map.b.b;

import android.util.Pair;
import com.baidu.platform.comapi.map.MapController;
import com.baidu.platform.comapi.map.b.a;
import com.baidu.platform.comapi.map.b.a.b;
import com.baidu.platform.comapi.map.b.f;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class b implements b.a {
    private LinkedList<a.C0020a> a = new LinkedList<>();
    private f b;
    private MapController c;
    private boolean d;
    private a e;
    private int f;

    public b(MapController mapController) {
        f fVar = new f();
        this.b = fVar;
        this.d = false;
        this.c = mapController;
        this.f = fVar.b / 3;
    }

    private boolean a() {
        a.d c;
        a.C0020a c0020a;
        this.d = true;
        Iterator<a.C0020a> it = this.a.iterator();
        while (it.hasNext()) {
            Double valueOf = Double.valueOf(a.d.a(com.baidu.platform.comapi.map.b.a.a.c(), it.next().c()));
            if (Math.abs(valueOf.doubleValue()) > 45.0d && Math.abs(valueOf.doubleValue()) < 135.0d) {
                return false;
            }
        }
        Pair<a.d, a.d> c2 = this.b.c();
        a.d dVar = (a.d) c2.first;
        a.d dVar2 = (a.d) c2.second;
        boolean z = Math.abs(dVar.b) > ((double) this.f) && Math.abs(dVar2.b) > ((double) this.f);
        a.C0020a first = this.a.getFirst();
        a.C0020a last = this.a.getLast();
        a.C0020a c0020a2 = new a.C0020a(last.a, first.a);
        a.C0020a c0020a3 = new a.C0020a(last.b, first.b);
        if (dVar.b <= SearchStatUtils.POW || dVar2.b <= SearchStatUtils.POW) {
            c = c0020a2.c();
            c0020a = com.baidu.platform.comapi.map.b.a.b;
        } else {
            c = c0020a2.c();
            c0020a = com.baidu.platform.comapi.map.b.a.c;
        }
        return z && (Math.abs((int) a.d.a(c, c0020a.c())) < 40 && Math.abs((int) a.d.a(c0020a3.c(), c0020a.c())) < 40);
    }

    private void d(com.baidu.platform.comapi.map.b.a.b bVar) {
        if (this.a.size() < 5) {
            this.a.addLast(bVar.c);
            this.b.a(bVar.d);
        } else if (this.d || this.a.size() != 5 || !a()) {
        } else {
            e(bVar);
        }
    }

    private void e(com.baidu.platform.comapi.map.b.a.b bVar) {
        if (this.c.isOverlookGestureEnable()) {
            this.e.a(bVar, null);
            c cVar = new c(this.c);
            this.e = cVar;
            cVar.a(bVar);
        }
    }

    @Override // com.baidu.platform.comapi.map.b.a.b.a
    public boolean a(com.baidu.platform.comapi.map.b.a.b bVar) {
        this.a.clear();
        this.b.a();
        this.e = new d(this.c);
        this.d = false;
        return true;
    }

    @Override // com.baidu.platform.comapi.map.b.a.b.a
    public boolean b(com.baidu.platform.comapi.map.b.a.b bVar) {
        d(bVar);
        if (this.a.size() == 1) {
            this.e.a(bVar);
        }
        this.e.b(bVar);
        return true;
    }

    @Override // com.baidu.platform.comapi.map.b.a.b.a
    public boolean c(com.baidu.platform.comapi.map.b.a.b bVar) {
        Pair<a.d, a.d> c = this.b.c();
        this.b.b();
        this.e.a(bVar, c);
        return true;
    }
}
