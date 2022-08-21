package com.baidu.vi;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import com.miui.gallery.search.statistics.SearchStatUtils;

/* loaded from: classes.dex */
class f implements GpsStatus.Listener {
    public final /* synthetic */ VGps a;

    public f(VGps vGps) {
        this.a = vGps;
    }

    @Override // android.location.GpsStatus.Listener
    public void onGpsStatusChanged(int i) {
        LocationManager locationManager;
        GpsStatus gpsStatus;
        int i2;
        int i3;
        int i4;
        GpsStatus gpsStatus2;
        LocationManager locationManager2;
        GpsStatus gpsStatus3;
        LocationManager locationManager3;
        if (i == 2) {
            this.a.updateGps(SearchStatUtils.POW, SearchStatUtils.POW, 0.0f, 0.0f, 0.0f, 0);
        } else if (i == 4) {
            locationManager = this.a.c;
            if (locationManager != null) {
                gpsStatus2 = this.a.d;
                if (gpsStatus2 == null) {
                    VGps vGps = this.a;
                    locationManager3 = vGps.c;
                    vGps.d = locationManager3.getGpsStatus(null);
                } else {
                    locationManager2 = this.a.c;
                    gpsStatus3 = this.a.d;
                    locationManager2.getGpsStatus(gpsStatus3);
                }
            }
            gpsStatus = this.a.d;
            int i5 = 0;
            for (GpsSatellite gpsSatellite : gpsStatus.getSatellites()) {
                if (gpsSatellite.usedInFix()) {
                    i5++;
                }
            }
            i2 = VGps.e;
            if (i5 < i2) {
                i3 = this.a.f;
                i4 = VGps.e;
                if (i3 >= i4) {
                    this.a.b();
                }
            }
            this.a.f = i5;
        }
    }
}
