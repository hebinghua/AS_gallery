package com.baidu.platform.comapi.map.b.a;

import android.view.MotionEvent;
import com.baidu.platform.comapi.map.b.a;

/* loaded from: classes.dex */
public class a {
    private long a = 0;
    private boolean b = false;
    private a.C0020a c;
    private InterfaceC0021a d;

    /* renamed from: com.baidu.platform.comapi.map.b.a.a$a  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public interface InterfaceC0021a {
        boolean a(a aVar);
    }

    public a(InterfaceC0021a interfaceC0021a) {
        this.d = interfaceC0021a;
    }

    private void a() {
        this.b = false;
        this.c = null;
        this.a = 0L;
    }

    private void b(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() != 2 || this.c == null) {
            return;
        }
        a.C0020a a = a.C0020a.a(motionEvent);
        a.C0020a c0020a = new a.C0020a(this.c.a, a.a);
        a.C0020a c0020a2 = new a.C0020a(this.c.b, a.b);
        int i = (Math.abs(c0020a.b()) > 20.0d ? 1 : (Math.abs(c0020a.b()) == 20.0d ? 0 : -1));
        boolean z = true;
        boolean z2 = i < 0 && Math.abs(c0020a2.b()) < 20.0d;
        if (System.currentTimeMillis() - this.a >= 200) {
            z = false;
        }
        if (!z2 || !z || !this.b) {
            return;
        }
        this.d.a(this);
    }

    private void c(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() != 2) {
            return;
        }
        this.c = a.C0020a.a(motionEvent);
        this.b = true;
    }

    public void a(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.a = System.currentTimeMillis();
            return;
        }
        if (action != 5) {
            if (action != 6) {
                if (action != 261) {
                    if (action != 262) {
                        return;
                    }
                }
            }
            b(motionEvent);
            a();
            return;
        }
        c(motionEvent);
    }
}
