package com.baidu.platform.comapi.map.b.b;

import android.util.Pair;
import com.baidu.platform.comapi.map.MapController;
import com.baidu.platform.comapi.map.MapStatus;
import com.baidu.platform.comapi.map.b.a;
import com.miui.gallery.search.statistics.SearchStatUtils;

/* loaded from: classes.dex */
public class c extends a {
    private boolean b;
    private long c;

    public c(MapController mapController) {
        super(mapController);
        this.b = true;
    }

    private void a(double d, MapStatus mapStatus) {
        if (this.c == 0) {
            this.c = System.currentTimeMillis();
        }
        long currentTimeMillis = System.currentTimeMillis();
        this.c = currentTimeMillis;
        if (currentTimeMillis - currentTimeMillis > 50 || Math.abs(d) < 4.0d) {
            return;
        }
        mapStatus.overlooking = d > SearchStatUtils.POW ? mapStatus.overlooking - 4 : (int) (mapStatus.overlooking + 2.0d);
    }

    @Override // com.baidu.platform.comapi.map.b.b.a
    public void a(com.baidu.platform.comapi.map.b.a.b bVar, Pair<a.d, a.d> pair) {
        MapStatus mapStatus = this.a.getMapStatus();
        if (mapStatus.bOverlookSpringback) {
            mapStatus.overlooking = mapStatus.overlooking > 0 ? 0 : mapStatus.minOverlooking;
            this.a.setMapStatusWithAnimation(mapStatus, 200);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0031, code lost:
        if (r4 != com.miui.gallery.search.statistics.SearchStatUtils.POW) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x003e, code lost:
        if (java.lang.Math.abs(r2) > java.lang.Math.abs(r4)) goto L3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0041, code lost:
        a(r4, r1);
     */
    /* JADX WARN: Removed duplicated region for block: B:18:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:20:? A[RETURN, SYNTHETIC] */
    @Override // com.baidu.platform.comapi.map.b.b.a
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void b(com.baidu.platform.comapi.map.b.a.b r11) {
        /*
            r10 = this;
            com.baidu.platform.comapi.map.b.a$a r0 = r11.b
            com.baidu.platform.comapi.map.b.a$a r11 = r11.c
            com.baidu.platform.comapi.map.MapController r1 = r10.a
            com.baidu.platform.comapi.map.MapStatus r1 = r1.getMapStatus()
            com.baidu.platform.comapi.map.b.a$b r2 = r11.a
            double r2 = r2.b
            com.baidu.platform.comapi.map.b.a$b r4 = r0.a
            double r4 = r4.b
            double r2 = r2 - r4
            com.baidu.platform.comapi.map.b.a$b r11 = r11.b
            double r4 = r11.b
            com.baidu.platform.comapi.map.b.a$b r11 = r0.b
            double r6 = r11.b
            double r4 = r4 - r6
            double r6 = r2 * r4
            r8 = 0
            int r11 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r11 <= 0) goto L28
        L24:
            r10.a(r2, r1)
            goto L44
        L28:
            if (r11 != 0) goto L34
            int r11 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
            if (r11 == 0) goto L2f
            goto L24
        L2f:
            int r11 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r11 == 0) goto L44
            goto L41
        L34:
            double r6 = java.lang.Math.abs(r2)
            double r8 = java.lang.Math.abs(r4)
            int r11 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r11 <= 0) goto L41
            goto L24
        L41:
            r10.a(r4, r1)
        L44:
            com.baidu.platform.comapi.map.MapController r11 = r10.a
            r11.setMapStatus(r1)
            boolean r11 = r10.b
            if (r11 == 0) goto L59
            r11 = 0
            r10.b = r11
            com.baidu.platform.comapi.map.MapController r11 = r10.a
            com.baidu.platform.comapi.map.b.d r11 = r11.getGestureMonitor()
            r11.d()
        L59:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.map.b.b.c.b(com.baidu.platform.comapi.map.b.a.b):void");
    }
}
