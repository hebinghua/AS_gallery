package com.baidu.mapapi.map;

import android.util.Log;
import com.baidu.mapapi.model.LatLng;

/* loaded from: classes.dex */
public class CircleHoleOptions extends HoleOptions {
    private LatLng a;
    private int b;

    public CircleHoleOptions() {
        this.mHoleType = "circle";
    }

    public CircleHoleOptions center(LatLng latLng) {
        if (latLng == null) {
            Log.e("baidumapsdk", "CircleHole center can not be null");
            return this;
        }
        this.a = latLng;
        return this;
    }

    public LatLng getHoleCenter() {
        return this.a;
    }

    public int getHoleRadius() {
        return this.b;
    }

    public CircleHoleOptions radius(int i) {
        if (i <= 0) {
            Log.e("baidumapsdk", "CircleHole's radius can not be less than zero");
            return this;
        }
        this.b = i;
        return this;
    }
}
