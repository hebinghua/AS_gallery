package com.baidu.platform.comapi.map.b;

import android.view.MotionEvent;
import com.miui.gallery.search.statistics.SearchStatUtils;

/* loaded from: classes.dex */
public class a {
    public static final C0020a a = new C0020a(new b(SearchStatUtils.POW, SearchStatUtils.POW), new b(1.0d, SearchStatUtils.POW));
    public static final C0020a b = new C0020a(new b(SearchStatUtils.POW, SearchStatUtils.POW), new b(SearchStatUtils.POW, 1.0d));
    public static final C0020a c = new C0020a(new b(SearchStatUtils.POW, 1.0d), new b(SearchStatUtils.POW, SearchStatUtils.POW));

    /* renamed from: com.baidu.platform.comapi.map.b.a$a  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static class C0020a {
        public b a;
        public b b;

        public C0020a(b bVar, b bVar2) {
            this.a = bVar;
            this.b = bVar2;
        }

        public static C0020a a(MotionEvent motionEvent) {
            return new C0020a(new b(motionEvent.getX(0), motionEvent.getY(0)), new b(motionEvent.getX(1), motionEvent.getY(1)));
        }

        public b a() {
            b bVar = this.a;
            double d = bVar.a;
            b bVar2 = this.b;
            return new b((d + bVar2.a) / 2.0d, (bVar.b + bVar2.b) / 2.0d);
        }

        public double b() {
            b bVar = this.a;
            double d = bVar.a;
            b bVar2 = this.b;
            double d2 = bVar2.a;
            double d3 = bVar.b;
            double d4 = bVar2.b;
            return Math.sqrt(((d - d2) * (d - d2)) + ((d3 - d4) * (d3 - d4)));
        }

        public d c() {
            b bVar = this.b;
            double d = bVar.a;
            b bVar2 = this.a;
            return new d(d - bVar2.a, bVar.b - bVar2.b);
        }

        public String toString() {
            return getClass().getSimpleName() + "  a : " + this.a.toString() + " b : " + this.b.toString();
        }
    }

    /* loaded from: classes.dex */
    public static class b {
        public double a;
        public double b;

        public b(double d, double d2) {
            this.a = d;
            this.b = d2;
        }

        public String toString() {
            return getClass().getSimpleName() + " x : " + this.a + " y : " + this.b;
        }
    }

    /* loaded from: classes.dex */
    public static class c {
        public final double a;
        public final double b;
        public final d c;

        public c(C0020a c0020a, C0020a c0020a2) {
            this.c = new d(c0020a.a(), c0020a2.a());
            this.b = c0020a2.b() / c0020a.b();
            this.a = d.a(c0020a.c(), c0020a2.c());
        }

        public String toString() {
            return getClass().getSimpleName() + " rotate : " + this.a + " scale : " + (this.b * 100.0d) + " move : " + this.c.toString();
        }
    }

    /* loaded from: classes.dex */
    public static class d {
        public double a;
        public double b;

        public d(double d, double d2) {
            this.a = d;
            this.b = d2;
        }

        public d(b bVar, b bVar2) {
            this.a = bVar2.a - bVar.a;
            this.b = bVar2.b - bVar.b;
        }

        public static double a(d dVar, d dVar2) {
            double atan2 = Math.atan2(dVar.b, dVar.a) - Math.atan2(dVar2.b, dVar2.a);
            if (atan2 > 3.141592653589793d) {
                atan2 -= 6.283185307179586d;
            } else if (atan2 < -3.141592653589793d) {
                atan2 += 6.283185307179586d;
            }
            return (atan2 * 180.0d) / 3.141592653589793d;
        }

        public String toString() {
            return getClass().getSimpleName() + " x : " + this.a + " y : " + this.b;
        }
    }
}
