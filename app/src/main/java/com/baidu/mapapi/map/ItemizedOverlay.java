package com.baidu.mapapi.map;

import android.graphics.drawable.Drawable;

/* loaded from: classes.dex */
public class ItemizedOverlay extends Overlay {
    public MapView a;

    public ItemizedOverlay(Drawable drawable, MapView mapView) {
        this.type = com.baidu.mapsdkplatform.comapi.map.h.marker;
        this.a = mapView;
    }

    public void addItem(OverlayOptions overlayOptions) {
        if (overlayOptions != null) {
            this.a.getMap().addOverlay(overlayOptions);
        }
    }

    public void reAddAll() {
    }

    public void removeAll() {
        this.a.getMap().clear();
    }
}
