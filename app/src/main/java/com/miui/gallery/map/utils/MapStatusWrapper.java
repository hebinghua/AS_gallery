package com.miui.gallery.map.utils;

import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.model.LatLng;
import com.miui.gallery.map.cluster.LatLngBounds;
import com.miui.gallery.map.cluster.MapLatLng;

/* loaded from: classes2.dex */
public class MapStatusWrapper implements IMapStatus {
    public MapStatus mMapStatus;

    public void setMapStatus(MapStatus mapStatus) {
        this.mMapStatus = mapStatus;
    }

    @Override // com.miui.gallery.map.utils.IMapStatus
    public float getZoomLevel() {
        return this.mMapStatus.zoom;
    }

    @Override // com.miui.gallery.map.utils.IMapStatus
    public MapLatLng getTarget() {
        LatLng latLng = this.mMapStatus.target;
        return new MapLatLng(latLng.latitude, latLng.longitude);
    }

    @Override // com.miui.gallery.map.utils.IMapStatus
    public LatLngBounds getBound() {
        LatLng latLng = this.mMapStatus.bound.northeast;
        MapLatLng mapLatLng = new MapLatLng(latLng.latitude, latLng.longitude);
        LatLng latLng2 = this.mMapStatus.bound.southwest;
        return new LatLngBounds(mapLatLng, new MapLatLng(latLng2.latitude, latLng2.longitude));
    }
}
