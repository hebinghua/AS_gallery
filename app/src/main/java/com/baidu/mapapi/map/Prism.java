package com.baidu.mapapi.map;

import android.os.Bundle;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.BuildingInfo;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import java.util.List;

/* loaded from: classes.dex */
public final class Prism extends Overlay {
    public float a;
    public List<LatLng> b;
    public int c = -16777216;
    public int d = -16711936;
    public BuildingInfo e;
    public BitmapDescriptor f;

    public Prism() {
        this.type = com.baidu.mapsdkplatform.comapi.map.h.prism;
    }

    @Override // com.baidu.mapapi.map.Overlay
    public Bundle a(Bundle bundle) {
        super.a(bundle);
        Overlay.b(this.c, bundle);
        Overlay.c(this.d, bundle);
        BitmapDescriptor bitmapDescriptor = this.f;
        if (bitmapDescriptor != null) {
            bundle.putBundle("image_info", bitmapDescriptor.b());
        }
        BuildingInfo buildingInfo = this.e;
        int i = 0;
        if (buildingInfo != null) {
            bundle.putDouble("m_height", buildingInfo.a());
            com.baidu.mapsdkplatform.comapi.map.x xVar = new com.baidu.mapsdkplatform.comapi.map.x();
            Overlay.a(xVar.a(this.e.b()), bundle);
            GeoPoint ll2mc = CoordUtil.ll2mc(xVar.b(this.e.c()));
            bundle.putDouble("location_x", ll2mc.getLongitudeE6());
            bundle.putDouble("location_y", ll2mc.getLatitudeE6());
        } else {
            GeoPoint ll2mc2 = CoordUtil.ll2mc(this.b.get(0));
            bundle.putDouble("location_x", ll2mc2.getLongitudeE6());
            bundle.putDouble("location_y", ll2mc2.getLatitudeE6());
            Overlay.a(this.b, bundle);
            bundle.putDouble("m_height", this.a);
        }
        if (this.e != null) {
            i = 1;
        }
        bundle.putInt("m_isBuilding", i);
        return bundle;
    }

    public BuildingInfo getBuildingInfo() {
        return this.e;
    }

    public BitmapDescriptor getCustomSideImage() {
        return this.f;
    }

    public float getHeight() {
        return this.a;
    }

    public List<LatLng> getPoints() {
        return this.b;
    }

    public int getSideFaceColor() {
        return this.d;
    }

    public int getTopFaceColor() {
        return this.c;
    }

    public void setBuildingInfo(BuildingInfo buildingInfo) {
        this.e = buildingInfo;
        this.listener.b(this);
    }

    public void setCustomSideImage(BitmapDescriptor bitmapDescriptor) {
        this.f = bitmapDescriptor;
        this.listener.b(this);
    }

    public void setHeight(float f) {
        this.a = f;
        this.listener.b(this);
    }

    public void setPoints(List<LatLng> list) {
        if (list != null) {
            if (list.size() <= 3) {
                throw new IllegalArgumentException("BDMapSDKException: points count can not less than four");
            }
            if (list.contains(null)) {
                throw new IllegalArgumentException("BDMapSDKException: points list can not contains null");
            }
            int i = 0;
            while (i < list.size()) {
                int i2 = i + 1;
                for (int i3 = i2; i3 < list.size(); i3++) {
                    if (list.get(i) == list.get(i3)) {
                        throw new IllegalArgumentException("BDMapSDKException: points list can not has same points");
                    }
                }
                i = i2;
            }
            this.b = list;
            this.listener.b(this);
            return;
        }
        throw new IllegalArgumentException("BDMapSDKException: points list can not be null");
    }

    public void setSideFaceColor(int i) {
        this.d = i;
        this.listener.b(this);
    }

    public void setTopFaceColor(int i) {
        this.c = i;
        this.listener.b(this);
    }
}
