package com.miui.gallery.map.view;

import android.content.Context;
import android.view.View;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.map.cluster.MapLatLng;
import com.miui.gallery.map.utils.LocationConverter;

/* loaded from: classes2.dex */
public class StaticMapContainer extends MapContainer {
    public View.OnClickListener mClickListener;

    /* renamed from: $r8$lambda$rbpUR9QiZ-tc2RVBqkIloLCsZIg */
    public static /* synthetic */ boolean m1088$r8$lambda$rbpUR9QiZtc2RVBqkIloLCsZIg(StaticMapContainer staticMapContainer, Marker marker) {
        return staticMapContainer.lambda$init$0(marker);
    }

    public StaticMapContainer(Context context) {
        super(context);
    }

    public static IMapContainer newInstance(Context context, Float f, double d, double d2) {
        return newInstance(context, f, LocationConverter.convertToMapLatLng(d, d2));
    }

    public static IMapContainer newInstance(Context context, Float f, MapLatLng mapLatLng) {
        StaticMapContainer staticMapContainer = new StaticMapContainer(context);
        staticMapContainer.init(context, Float.valueOf(MapContainer.validZoomLevel(f)), mapLatLng);
        return staticMapContainer;
    }

    @Override // com.miui.gallery.map.view.MapContainer
    public void init(Context context, Float f, MapLatLng mapLatLng) {
        if (context == null) {
            return;
        }
        this.mContext = context;
        this.mMapView = new TextureMapView(GalleryApp.sGetAndroidContext(), buildOptions(f, mapLatLng));
        configMap();
        this.mMap.getUiSettings().setAllGesturesEnabled(false);
        this.mMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() { // from class: com.miui.gallery.map.view.StaticMapContainer.1
            @Override // com.baidu.mapapi.map.BaiduMap.OnMapClickListener
            public void onMapPoiClick(MapPoi mapPoi) {
            }

            {
                StaticMapContainer.this = this;
            }

            @Override // com.baidu.mapapi.map.BaiduMap.OnMapClickListener
            public void onMapClick(LatLng latLng) {
                StaticMapContainer staticMapContainer = StaticMapContainer.this;
                View.OnClickListener onClickListener = staticMapContainer.mClickListener;
                if (onClickListener != null) {
                    onClickListener.onClick(staticMapContainer.getRootView());
                }
            }
        });
        this.mMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() { // from class: com.miui.gallery.map.view.StaticMapContainer$$ExternalSyntheticLambda0
            @Override // com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener
            public final boolean onMarkerClick(Marker marker) {
                return StaticMapContainer.m1088$r8$lambda$rbpUR9QiZtc2RVBqkIloLCsZIg(StaticMapContainer.this, marker);
            }
        });
    }

    public /* synthetic */ boolean lambda$init$0(Marker marker) {
        View.OnClickListener onClickListener = this.mClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(getRootView());
            return false;
        }
        return false;
    }

    @Override // com.miui.gallery.map.view.MapContainer
    public void configMap() {
        addView(this.mMapView);
        BaiduMap map = this.mMapView.getMap();
        this.mMap = map;
        if (map != null) {
            map.setOnMapLoadedCallback(this.mBDMapLoadedCallback);
        }
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mClickListener = onClickListener;
    }

    @Override // com.miui.gallery.map.view.MapContainer, com.miui.gallery.map.view.IMapContainer
    public void onResume() {
        TextureMapView textureMapView = this.mMapView;
        if (textureMapView != null) {
            removeView(textureMapView);
            configMap();
        }
    }

    @Override // com.miui.gallery.map.view.MapContainer, com.miui.gallery.map.view.IMapContainer
    public void onPause() {
        TextureMapView textureMapView = this.mMapView;
        if (textureMapView != null) {
            removeView(textureMapView);
            this.mMap.setOnMapStatusChangeListener(null);
            this.mMap.setOnMapLoadedCallback(null);
        }
    }

    @Override // com.miui.gallery.map.view.MapContainer, com.miui.gallery.map.view.IMapContainer
    public void onDestroy() {
        this.mClickListener = null;
        super.onDestroy();
    }
}
