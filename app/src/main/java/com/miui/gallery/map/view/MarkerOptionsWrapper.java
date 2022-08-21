package com.miui.gallery.map.view;

import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.miui.gallery.map.cluster.MapLatLng;
import com.miui.gallery.map.utils.IMarkerOptions;

/* loaded from: classes2.dex */
public class MarkerOptionsWrapper implements IMarkerOptions<MarkerOptions> {
    public final MarkerOptions mMarkerOptions = new MarkerOptions();

    @Override // com.miui.gallery.map.utils.IMarkerOptions
    public IMarkerOptions<MarkerOptions> icon(BitmapDescriptorWrapper bitmapDescriptorWrapper) {
        this.mMarkerOptions.icon(bitmapDescriptorWrapper.getBitmapDescriptor());
        return this;
    }

    public IMarkerOptions<MarkerOptions> position(MapLatLng mapLatLng) {
        this.mMarkerOptions.position(new LatLng(mapLatLng.latitude, mapLatLng.longitude));
        return this;
    }

    public IMarkerOptions<MarkerOptions> position(double d, double d2) {
        this.mMarkerOptions.position(new LatLng(d, d2));
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.map.utils.IMarkerOptions
    /* renamed from: getOptions */
    public MarkerOptions mo1087getOptions() {
        return this.mMarkerOptions;
    }

    @Override // com.miui.gallery.map.utils.IMarkerOptions
    public BitmapDescriptorWrapper getIcon() {
        return new BitmapDescriptorWrapper(this.mMarkerOptions.getIcon());
    }
}
