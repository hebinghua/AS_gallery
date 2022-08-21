package com.baidu.vi;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.miui.gallery.search.statistics.SearchStatUtils;

/* loaded from: classes.dex */
class g implements LocationListener {
    public final /* synthetic */ VGps a;

    public g(VGps vGps) {
        this.a = vGps;
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(Location location) {
        int i;
        int i2;
        int i3;
        if (location != null) {
            float f = 0.0f;
            if (location.hasAccuracy()) {
                f = location.getAccuracy();
            }
            float f2 = f;
            i = this.a.f;
            i2 = VGps.e;
            if (i < i2) {
                this.a.b();
                return;
            }
            float bearing = location.getBearing();
            i3 = this.a.f;
            this.a.updateGps(location.getLongitude(), location.getLatitude(), (float) (location.getSpeed() * 3.6d), bearing, f2, i3);
        }
    }

    @Override // android.location.LocationListener
    public void onProviderDisabled(String str) {
        this.a.updateGps(SearchStatUtils.POW, SearchStatUtils.POW, 0.0f, 0.0f, 0.0f, 0);
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(String str) {
    }

    @Override // android.location.LocationListener
    public void onStatusChanged(String str, int i, Bundle bundle) {
        if (i == 0 || i == 1) {
            this.a.updateGps(SearchStatUtils.POW, SearchStatUtils.POW, 0.0f, 0.0f, 0.0f, 0);
        }
    }
}
