package com.baidu.platform.comapi.map.b.b;

import android.util.Pair;
import com.baidu.platform.comapi.UIMsg;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.MapController;
import com.baidu.platform.comapi.map.MapStatus;
import com.baidu.platform.comapi.map.MapViewInterface;
import com.baidu.platform.comapi.map.b.a;
import com.baidu.platform.comapi.util.SysOSUtil;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.util.LinkedList;
import java.util.Queue;

/* loaded from: classes.dex */
public class d extends a {
    private GeoPoint b;
    private int c;
    private float d;
    private Queue<a.c> e;
    private a.c f;
    private a.c g;
    private boolean h;
    private com.baidu.platform.comapi.map.b.a.b i;
    private boolean j;
    private double k;
    private boolean l;
    private long m;

    public d(MapController mapController) {
        super(mapController);
        this.e = new LinkedList();
        this.h = false;
        this.j = false;
        this.k = SearchStatUtils.POW;
        this.l = false;
        this.m = 0L;
    }

    private int a() {
        if (this.j) {
            LinkedList linkedList = new LinkedList();
            linkedList.addAll(this.e);
            if (linkedList.size() < 2) {
                return 0;
            }
            int i = (int) (((a.c) linkedList.get(linkedList.size() - 2)).a * 8.0d);
            if (i >= 180) {
                return 179;
            }
            if (i > -180) {
                return i;
            }
            return -179;
        }
        return 0;
    }

    private void a(MapStatus mapStatus) {
        float log = this.d + ((float) (Math.log(this.f.b) / Math.log(2.0d)));
        mapStatus.level = log;
        if (log < 4.0f) {
            log = 4.0f;
        }
        mapStatus.level = log;
    }

    private void a(MapStatus mapStatus, int i) {
        if (i != 0) {
            mapStatus.rotation = (mapStatus.rotation + i) % 360;
            this.a.setMapStatusWithAnimation(mapStatus, UIMsg.MSG_MAP_PANO_DATA);
        }
    }

    private void b(MapStatus mapStatus) {
        if (this.b != null) {
            if (Math.abs(this.g.c.a) <= SearchStatUtils.POW && Math.abs(this.g.c.b) <= SearchStatUtils.POW) {
                return;
            }
            a.b a = this.i.a.a();
            a.b a2 = this.i.c.a();
            double d = a2.a;
            double d2 = a.a;
            double d3 = a2.b;
            double d4 = a.b;
            double sqrt = Math.sqrt(((d - d2) * (d - d2)) + ((d3 - d4) * (d3 - d4)));
            boolean z = MapController.isCompass;
            if (z && sqrt < 100.0d) {
                return;
            }
            if (!z && !this.l) {
                mapStatus.centerPtX = this.b.getLongitude();
                mapStatus.centerPtY = this.b.getLatitude();
                a.b a3 = this.i.c.a();
                mapStatus.xOffset = (float) (a3.a - (this.a.getScreenWidth() / 2));
                mapStatus.yOffset = ((float) (a3.b - (this.a.getScreenHeight() / 2))) * (-1.0f);
                return;
            }
            this.l = false;
            com.baidu.platform.comapi.util.a.a().a(new com.baidu.platform.comapi.map.a.a());
            MapViewInterface mapView = this.a.getMapView();
            if (mapView == null) {
                return;
            }
            a.b a4 = this.i.c.a();
            this.b = mapView.getProjection().fromPixels((int) a4.a, (int) a4.b);
        }
    }

    private void c(MapStatus mapStatus) {
        com.baidu.platform.comapi.map.b.a.b bVar = this.i;
        double abs = Math.abs(new a.c(new a.C0020a(bVar.b.a, bVar.c.a), this.i.b).a);
        com.baidu.platform.comapi.map.b.a.b bVar2 = this.i;
        double abs2 = Math.abs(new a.c(new a.C0020a(bVar2.b.b, bVar2.c.b), this.i.b).a);
        double d = this.k;
        boolean z = false;
        if (d != SearchStatUtils.POW && d * this.g.b < SearchStatUtils.POW) {
            return;
        }
        if (this.j) {
            mapStatus.rotation = (int) ((this.c + this.f.a) % 360.0d);
        } else {
            double d2 = this.g.b;
            boolean z2 = (d2 < 1.0d && abs > 60.0d) || (d2 > 1.0d && Math.abs(abs - 180.0d) > 60.0d);
            double d3 = this.g.b;
            if ((d3 > 1.0d && abs2 > 60.0d) || (d3 < 1.0d && Math.abs(abs2 - 180.0d) > 60.0d)) {
                z = true;
            }
            if (z2 || z) {
                if (Math.abs(this.f.a) > (MapController.isCompass ? 30 : 10)) {
                    this.j = true;
                    this.a.getGestureMonitor().c();
                    this.c = (int) (this.c - this.f.a);
                    if (MapController.isCompass) {
                        this.l = true;
                        com.baidu.platform.comapi.util.a.a().a(new com.baidu.platform.comapi.map.a.a());
                    }
                }
            }
        }
        this.k = this.g.b;
    }

    public void a(MapStatus mapStatus, com.baidu.platform.comapi.map.b.a.b bVar, Pair<a.d, a.d> pair) {
        double d;
        int i;
        double d2;
        if (pair != null) {
            int a = a();
            Object obj = pair.first;
            double d3 = ((a.d) obj).a;
            Object obj2 = pair.second;
            if (d3 * ((a.d) obj2).a > SearchStatUtils.POW && ((a.d) obj).b * ((a.d) obj2).b > SearchStatUtils.POW) {
                a(mapStatus, a);
            } else if (Math.abs(((a.d) obj).a - ((a.d) obj2).a) < 1.0d || Math.abs(((a.d) pair.first).b - ((a.d) pair.second).b) < 1.0d) {
                a(mapStatus, a);
            } else {
                double abs = Math.abs(new a.c(new a.C0020a(bVar.b.a, bVar.c.a), bVar.b).a);
                double abs2 = Math.abs(new a.c(new a.C0020a(bVar.b.b, bVar.c.b), bVar.b).a);
                double d4 = this.k;
                if (d4 != SearchStatUtils.POW && d4 * this.g.b < SearchStatUtils.POW) {
                    a(mapStatus, a);
                    return;
                }
                Object obj3 = pair.first;
                Object obj4 = pair.second;
                float sqrt = ((float) Math.sqrt((((a.d) obj3).a * ((a.d) obj3).a) + (((a.d) obj4).a * ((a.d) obj4).a) + (((a.d) obj3).b * ((a.d) obj3).b) + (((a.d) obj4).b * ((a.d) obj4).b))) * 2.0f;
                if (sqrt <= (SysOSUtil.getInstance().getDensityDPI() * 100) / 320) {
                    return;
                }
                mapStatus.hasAnimation = 1;
                mapStatus.animationTime = UIMsg.MSG_MAP_PANO_DATA;
                a.c cVar = null;
                a.c cVar2 = null;
                int i2 = 0;
                int i3 = 0;
                int i4 = 0;
                while (i2 < this.e.size()) {
                    a.c poll = this.e.poll();
                    if (poll != null) {
                        if (this.e.isEmpty()) {
                            int i5 = a;
                            d2 = 1.0d;
                            if (Math.abs(poll.b - 1.0d) < 0.01d) {
                                a(mapStatus, i5);
                                return;
                            }
                            i = i5;
                        } else {
                            i = a;
                            d2 = 1.0d;
                        }
                        a.c cVar3 = cVar;
                        a.c cVar4 = cVar2;
                        if (poll.b > d2) {
                            i3++;
                            cVar = cVar3;
                            cVar2 = poll;
                        } else {
                            i4++;
                            cVar = poll;
                            cVar2 = cVar4;
                        }
                    } else {
                        i = a;
                    }
                    i2++;
                    a = i;
                }
                int i6 = a;
                a.c cVar5 = i3 >= i4 ? cVar2 : cVar;
                if (cVar5 != null) {
                    d = 1.0d;
                    if (Math.abs(cVar5.b - 1.0d) < 0.01d) {
                        a(mapStatus, i6);
                        return;
                    }
                } else {
                    d = 1.0d;
                }
                double d5 = cVar5.b;
                boolean z = (d5 < d && abs > 60.0d) || (d5 > d && Math.abs(abs - 180.0d) > 60.0d);
                double d6 = cVar5.b;
                boolean z2 = (d6 > 1.0d && abs2 > 60.0d) || (d6 < 1.0d && Math.abs(abs2 - 180.0d) > 60.0d);
                if (z || z2) {
                    if (Math.abs(this.f.a) > (MapController.isCompass ? 30 : 15)) {
                        a(mapStatus, i6);
                        return;
                    }
                }
                this.h = cVar5.b > 1.0d;
                float densityDPI = sqrt / (800000 / SysOSUtil.getInstance().getDensityDPI());
                if (densityDPI > 2.0f) {
                    densityDPI = 2.0f;
                }
                mapStatus.level = !this.h ? mapStatus.level - densityDPI : mapStatus.level + densityDPI;
                float f = mapStatus.level;
                if (f < 4.0f) {
                    f = 4.0f;
                }
                mapStatus.level = f;
                if (i6 != 0) {
                    mapStatus.rotation = (mapStatus.rotation + i6) % 360;
                }
                this.a.setMapStatus(mapStatus);
                this.a.mIsAnimating = true;
            }
        }
    }

    @Override // com.baidu.platform.comapi.map.b.b.a
    public void a(com.baidu.platform.comapi.map.b.a.b bVar) {
        MapViewInterface mapView = this.a.getMapView();
        if (mapView == null) {
            return;
        }
        MapStatus mapStatus = this.a.getMapStatus();
        a.b a = bVar.a.a();
        this.b = mapView.getProjection().fromPixels((int) a.a, (int) a.b);
        this.d = this.a.getZoomLevel();
        this.c = mapStatus.rotation;
        this.k = SearchStatUtils.POW;
    }

    @Override // com.baidu.platform.comapi.map.b.b.a
    public void a(com.baidu.platform.comapi.map.b.a.b bVar, Pair<a.d, a.d> pair) {
        double d;
        double d2;
        MapViewInterface mapView = this.a.getMapView();
        if (mapView == null) {
            return;
        }
        int x = (int) bVar.d.getX();
        int y = (int) bVar.d.getY();
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        GeoPoint fromPixels = mapView.getProjection().fromPixels(this.a.getScreenWidth() / 2, this.a.getScreenHeight() / 2);
        if (fromPixels != null) {
            d = fromPixels.getLongitude();
            d2 = fromPixels.getLatitude();
        } else {
            d = 0.0d;
            d2 = 0.0d;
        }
        this.a.MapMsgProc(5, 1, (y << 16) | x, 0, 0, d, d2, SearchStatUtils.POW, SearchStatUtils.POW);
        this.a.getGestureMonitor().a(this.a.getZoomLevel());
        if (System.currentTimeMillis() - this.m > 100 || !this.a.isEnableZoom()) {
            return;
        }
        a(this.a.getMapStatus(), bVar, pair);
    }

    @Override // com.baidu.platform.comapi.map.b.b.a
    public void b(com.baidu.platform.comapi.map.b.a.b bVar) {
        this.i = bVar;
        this.f = new a.c(bVar.a, bVar.c);
        this.g = new a.c(bVar.b, bVar.c);
        MapStatus mapStatus = this.a.getMapStatus();
        if (this.a.isEnableZoom()) {
            a(mapStatus);
        }
        if (this.a.is3DGestureEnable() && this.a.getMapControlMode() != MapController.MapControlMode.STREET) {
            if (mapStatus.overlooking == 0 && this.a.isCanTouchMove()) {
                b(mapStatus);
            }
            c(mapStatus);
        }
        this.a.setMapStatus(mapStatus);
        if (this.a.isNaviMode() && this.a.getNaviMapViewListener() != null) {
            this.a.getNaviMapViewListener().onAction(520, null);
        }
        this.a.mapStatusChangeStart();
        if (this.e.size() >= 10) {
            this.e.poll();
        }
        this.e.offer(this.g);
        com.baidu.platform.comapi.util.a.a().a(new com.baidu.platform.comapi.map.a.d());
        this.m = System.currentTimeMillis();
    }
}
