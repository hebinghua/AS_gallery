package com.miui.gallery.map.view;

import com.baidu.mapapi.map.Marker;
import com.miui.gallery.map.utils.IMarker;
import java.util.Objects;

/* loaded from: classes2.dex */
public class MarkerWrapper implements IMarker {
    public Marker mMarker;

    public MarkerWrapper(Marker marker) {
        this.mMarker = marker;
    }

    @Override // com.miui.gallery.map.utils.IMarker
    public void remove() {
        this.mMarker.remove();
    }

    @Override // com.miui.gallery.map.utils.IMarker
    public void setIcon(BitmapDescriptorWrapper bitmapDescriptorWrapper) {
        if (bitmapDescriptorWrapper != null) {
            this.mMarker.setIcon(bitmapDescriptorWrapper.getBitmapDescriptor());
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            return Objects.equals(this.mMarker, ((MarkerWrapper) obj).mMarker);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mMarker);
    }
}
