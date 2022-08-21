package com.baidu.platform.comapi.map;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class af extends j {
    private List<GeoPoint> k;

    public af(ao aoVar) {
        super(aoVar);
        this.k = new ArrayList();
        this.i = 0;
        this.j = 2;
    }

    private boolean b() {
        synchronized (this.k) {
            if (this.k.size() < 2) {
                return false;
            }
            int size = this.k.size();
            this.d = new double[(this.k.size() * 2) + 5];
            if (c()) {
                this.d[0] = this.e.getLongitude();
                this.d[1] = this.e.getLatitude();
                this.d[2] = this.f.getLongitude();
                this.d[3] = this.f.getLatitude();
            }
            double[] dArr = this.d;
            dArr[4] = 2.0d;
            dArr[5] = this.k.get(0).getLongitude();
            this.d[6] = this.k.get(0).getLatitude();
            for (int i = 1; i < size; i++) {
                int i2 = (i * 2) + 5;
                int i3 = i - 1;
                this.d[i2] = this.k.get(i).getLongitude() - this.k.get(i3).getLongitude();
                this.d[i2 + 1] = this.k.get(i).getLatitude() - this.k.get(i3).getLatitude();
            }
            return true;
        }
    }

    private boolean c() {
        synchronized (this.k) {
            if (this.k.size() < 2) {
                return false;
            }
            this.e.setLatitude(this.k.get(0).getLatitude());
            this.e.setLongitude(this.k.get(0).getLongitude());
            this.f.setLatitude(this.k.get(0).getLatitude());
            this.f.setLongitude(this.k.get(0).getLongitude());
            for (GeoPoint geoPoint : this.k) {
                if (this.e.getLatitude() >= geoPoint.getLatitude()) {
                    this.e.setLatitude(geoPoint.getLatitude());
                }
                if (this.e.getLongitude() >= geoPoint.getLongitude()) {
                    this.e.setLongitude(geoPoint.getLongitude());
                }
                if (this.f.getLatitude() <= geoPoint.getLatitude()) {
                    this.f.setLatitude(geoPoint.getLatitude());
                }
                if (this.f.getLongitude() <= geoPoint.getLongitude()) {
                    this.f.setLongitude(geoPoint.getLongitude());
                }
            }
            return true;
        }
    }

    @Override // com.baidu.platform.comapi.map.j
    public String a() {
        String a;
        synchronized (this.k) {
            if (this.g) {
                this.g = !b();
            }
            a = a(this.i);
        }
        return a;
    }

    public void a(ao aoVar) {
        this.a = aoVar;
    }

    public void a(List<GeoPoint> list) {
        if (list != null) {
            if (list.size() < 2) {
                throw new IllegalArgumentException("points count can not be less than two!");
            }
            synchronized (this.k) {
                this.k.clear();
                this.k.addAll(list);
                this.g = true;
            }
            return;
        }
        throw new IllegalArgumentException("points list can not be null!");
    }
}
