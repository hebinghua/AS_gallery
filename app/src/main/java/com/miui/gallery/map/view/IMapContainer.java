package com.miui.gallery.map.view;

import android.view.View;
import com.miui.gallery.map.cluster.LatLngBounds;
import com.miui.gallery.map.cluster.MapLatLng;
import com.miui.gallery.map.utils.IMapStatus;
import com.miui.gallery.map.utils.IMarker;
import com.miui.gallery.map.utils.IMarkerOptions;
import com.miui.gallery.map.utils.OnMapLoadedCallback;
import com.miui.gallery.map.utils.OnMapStatusChangeListener;
import com.miui.gallery.map.utils.OnMarkerClickListener;

/* loaded from: classes2.dex */
public interface IMapContainer {
    void addMarker(String str, int i, double d, double d2);

    void addMarkerAndFocus(String str, int i, double d, double d2, float f);

    IMarker addOverlay(IMarkerOptions iMarkerOptions);

    void clearOverlays();

    LatLngBounds getBound();

    IMapStatus getMapStatus();

    View getView();

    float getZoomLevel();

    boolean hasMapLoaded();

    void moveTo(double d, double d2, float f);

    void onDestroy();

    void onPause();

    void onResume();

    void setClusterClickListener(OnMarkerClickListener onMarkerClickListener);

    void setMapLoadedCallback(OnMapLoadedCallback onMapLoadedCallback);

    void setMapStatusChangeListener(OnMapStatusChangeListener onMapStatusChangeListener);

    void showLocationIcon(MapLatLng mapLatLng);
}
