package com.baidu.mapapi.map;

import android.graphics.Point;
import com.baidu.mapapi.map.v.a;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* loaded from: classes.dex */
class v<T extends a> {
    private final m a;
    private final int b;
    private List<T> c;
    private List<v<T>> d;

    /* loaded from: classes.dex */
    public static abstract class a {
        public abstract Point a();
    }

    private v(double d, double d2, double d3, double d4, int i) {
        this(new m(d, d2, d3, d4), i);
    }

    public v(m mVar) {
        this(mVar, 0);
    }

    private v(m mVar, int i) {
        this.d = null;
        this.a = mVar;
        this.b = i;
    }

    private void a() {
        ArrayList arrayList = new ArrayList(4);
        this.d = arrayList;
        m mVar = this.a;
        arrayList.add(new v(mVar.a, mVar.e, mVar.b, mVar.f, this.b + 1));
        List<v<T>> list = this.d;
        m mVar2 = this.a;
        list.add(new v<>(mVar2.e, mVar2.c, mVar2.b, mVar2.f, this.b + 1));
        List<v<T>> list2 = this.d;
        m mVar3 = this.a;
        list2.add(new v<>(mVar3.a, mVar3.e, mVar3.f, mVar3.d, this.b + 1));
        List<v<T>> list3 = this.d;
        m mVar4 = this.a;
        list3.add(new v<>(mVar4.e, mVar4.c, mVar4.f, mVar4.d, this.b + 1));
        List<T> list4 = this.c;
        this.c = null;
        for (T t : list4) {
            a(t.a().x, t.a().y, t);
        }
    }

    private void a(double d, double d2, T t) {
        List<v<T>> list = this.d;
        if (list != null) {
            m mVar = this.a;
            list.get(d2 < mVar.f ? d < mVar.e ? 0 : 1 : d < mVar.e ? 2 : 3).a(d, d2, t);
            return;
        }
        if (this.c == null) {
            this.c = new ArrayList();
        }
        this.c.add(t);
        if (this.c.size() <= 40 || this.b >= 40) {
            return;
        }
        a();
    }

    private void a(m mVar, Collection<T> collection) {
        if (!this.a.a(mVar)) {
            return;
        }
        List<v<T>> list = this.d;
        if (list != null) {
            for (v<T> vVar : list) {
                vVar.a(mVar, collection);
            }
        } else if (this.c != null) {
            if (mVar.b(this.a)) {
                collection.addAll(this.c);
                return;
            }
            for (T t : this.c) {
                if (mVar.a(t.a())) {
                    collection.add(t);
                }
            }
        }
    }

    public Collection<T> a(m mVar) {
        ArrayList arrayList = new ArrayList();
        a(mVar, arrayList);
        return arrayList;
    }

    public void a(T t) {
        Point a2 = t.a();
        if (this.a.a(a2.x, a2.y)) {
            a(a2.x, a2.y, t);
        }
    }
}
