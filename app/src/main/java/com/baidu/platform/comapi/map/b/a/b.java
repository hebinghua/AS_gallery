package com.baidu.platform.comapi.map.b.a;

import android.util.Pair;
import android.view.MotionEvent;
import com.baidu.platform.comapi.map.b.a;
import com.baidu.platform.comapi.map.b.f;
import com.miui.gallery.search.statistics.SearchStatUtils;

/* loaded from: classes.dex */
public class b {
    public a.C0020a a;
    public a.C0020a b;
    public a.C0020a c;
    public MotionEvent d;
    private a f;
    public f e = new f();
    private boolean g = false;

    /* loaded from: classes.dex */
    public interface a {
        boolean a(b bVar);

        boolean b(b bVar);

        boolean c(b bVar);
    }

    public b(a aVar) {
        this.f = aVar;
    }

    private void a() {
        this.e.a();
        this.a = null;
        this.b = null;
        this.c = null;
        this.g = true;
        this.f.a(this);
    }

    private void b() {
        this.e.b();
        this.g = false;
        this.f.c(this);
    }

    private void b(MotionEvent motionEvent) {
        this.e.a(motionEvent);
        Pair<a.d, a.d> c = this.e.c();
        if (motionEvent.getPointerCount() == 2) {
            if (Math.abs(((a.d) c.first).a) <= SearchStatUtils.POW && Math.abs(((a.d) c.first).b) <= SearchStatUtils.POW && Math.abs(((a.d) c.second).a) <= SearchStatUtils.POW && Math.abs(((a.d) c.second).b) <= SearchStatUtils.POW) {
                return;
            }
            c(motionEvent);
            this.f.b(this);
        }
    }

    private void c(MotionEvent motionEvent) {
        a.C0020a a2 = a.C0020a.a(motionEvent);
        a.C0020a c0020a = this.c;
        if (c0020a == null) {
            c0020a = a2;
        }
        this.b = c0020a;
        this.c = a2;
        if (this.a == null) {
            this.a = a2;
        }
    }

    public void a(MotionEvent motionEvent) {
        this.d = motionEvent;
        int action = motionEvent.getAction();
        if (action != 2) {
            if (action != 5) {
                if (action != 6) {
                    if (action != 261) {
                        if (action != 262) {
                            return;
                        }
                    }
                }
                if (!this.g) {
                    return;
                }
                b();
                return;
            }
            if (this.g) {
                return;
            }
        } else if (this.g) {
            b(motionEvent);
            return;
        } else if (motionEvent.getPointerCount() != 2) {
            return;
        }
        a();
    }
}
