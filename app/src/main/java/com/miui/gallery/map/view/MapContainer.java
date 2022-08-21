package com.miui.gallery.map.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapLayer;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.map.cluster.LatLngBounds;
import com.miui.gallery.map.cluster.MapLatLng;
import com.miui.gallery.map.utils.AsyncFileOperations;
import com.miui.gallery.map.utils.BitmapDescriptorWrapperFactory;
import com.miui.gallery.map.utils.IMapStatus;
import com.miui.gallery.map.utils.IMarker;
import com.miui.gallery.map.utils.IMarkerOptions;
import com.miui.gallery.map.utils.IconGenerator;
import com.miui.gallery.map.utils.LocationConverter;
import com.miui.gallery.map.utils.MapBitmapTool;
import com.miui.gallery.map.utils.MapConfig;
import com.miui.gallery.map.utils.MapStatusWrapper;
import com.miui.gallery.map.utils.OnMapLoadedCallback;
import com.miui.gallery.map.utils.OnMapStatusChangeListener;
import com.miui.gallery.map.utils.OnMarkerClickListener;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.b.h;

/* loaded from: classes2.dex */
public class MapContainer extends FrameLayout implements IMapContainer {
    public BaiduMap.OnMapLoadedCallback mBDMapLoadedCallback;
    public OnMarkerClickListener mClusterClickListener;
    public Context mContext;
    public AsyncFileOperations.FileOperationCallback mFileOperationCallback;
    public BaiduMap mMap;
    public boolean mMapLoaded;
    public OnMapLoadedCallback mMapLoadedCallback;
    public OnMapStatusChangeListener mMapStatusChangeListener;
    public AsyncFileOperations mMapStyleFileLoader;
    public TextureMapView mMapView;

    public static /* synthetic */ void $r8$lambda$QiMRArzRXJOALPQ4rIibXgJNRBU(MapContainer mapContainer, boolean z, String str) {
        mapContainer.lambda$checkOrLoadMapStyleRes$1(z, str);
    }

    public static /* synthetic */ void $r8$lambda$YG3ve7qgTqO1si4mTMtWleuue2A(MapContainer mapContainer, int i, MapLatLng mapLatLng, Future future) {
        mapContainer.lambda$addMarker$3(i, mapLatLng, future);
    }

    /* renamed from: $r8$lambda$ryQ5FWYhyjSl-yq27_rz5a7m8Uo */
    public static /* synthetic */ boolean m1086$r8$lambda$ryQ5FWYhyjSlyq27_rz5a7m8Uo(MapContainer mapContainer, Marker marker) {
        return mapContainer.lambda$configMap$0(marker);
    }

    public static /* synthetic */ Bitmap $r8$lambda$zz78u8ZoSTm8gxS234UMBMItoLw(MapContainer mapContainer, String str, ThreadPool.JobContext jobContext) {
        return mapContainer.lambda$addMarker$2(str, jobContext);
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public View getView() {
        return this;
    }

    public static IMapContainer newInstance(Context context) {
        return newInstance(context, null, null);
    }

    public static IMapContainer newInstance(Context context, Float f, MapLatLng mapLatLng) {
        MapContainer mapContainer = new MapContainer(context);
        mapContainer.init(context, Float.valueOf(validZoomLevel(f)), mapLatLng);
        return mapContainer;
    }

    public MapContainer(Context context) {
        this(context, null);
    }

    public MapContainer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public MapContainer(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, -1);
    }

    public MapContainer(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mBDMapLoadedCallback = new BaiduMap.OnMapLoadedCallback() { // from class: com.miui.gallery.map.view.MapContainer.1
            {
                MapContainer.this = this;
            }

            @Override // com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback
            public void onMapLoaded() {
                DefaultLogger.d("MapContainer", "map loaded");
                MapContainer mapContainer = MapContainer.this;
                mapContainer.mMapLoaded = true;
                mapContainer.mMap.setViewPadding(mapContainer.getResources().getDimensionPixelSize(R.dimen.px_50), 0, 0, MapContainer.this.getResources().getDimensionPixelSize(R.dimen.px_25));
                OnMapLoadedCallback onMapLoadedCallback = MapContainer.this.mMapLoadedCallback;
                if (onMapLoadedCallback != null) {
                    onMapLoadedCallback.onMapLoaded();
                }
            }
        };
    }

    public BaiduMapOptions buildOptions(Float f, MapLatLng mapLatLng) {
        BaiduMapOptions baiduMapOptions = new BaiduMapOptions();
        baiduMapOptions.zoomControlsEnabled(false);
        baiduMapOptions.overlookingGesturesEnabled(false);
        baiduMapOptions.logoPosition(LogoPosition.logoPostionleftBottom);
        baiduMapOptions.scaleControlEnabled(false);
        MapStatus.Builder zoom = new MapStatus.Builder().zoom(f.floatValue());
        if (mapLatLng != null) {
            zoom.target(new LatLng(mapLatLng.latitude, mapLatLng.longitude));
        }
        baiduMapOptions.mapStatus(zoom.build());
        return baiduMapOptions;
    }

    public static float validZoomLevel(Float f) {
        if (f == null || f.floatValue() < MapConfig.MAP_MIN_SHOW_LEVEL.floatValue() || f.floatValue() > MapConfig.MAP_MAX_SHOW_LEVEL.floatValue()) {
            return MapConfig.OVERVIEW_ZOOM_LEVEL.floatValue();
        }
        return f.floatValue();
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public void onResume() {
        TextureMapView textureMapView = this.mMapView;
        if (textureMapView != null) {
            textureMapView.onResume();
        }
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public void onPause() {
        TextureMapView textureMapView = this.mMapView;
        if (textureMapView != null) {
            textureMapView.onPause();
        }
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public void onDestroy() {
        if (this.mMapView != null) {
            this.mMap.setOnMapStatusChangeListener(null);
            this.mMap.setOnMapLoadedCallback(null);
            this.mMap.setOnMapRenderCallbadk(null);
            this.mMap.setMyLocationEnabled(false);
            this.mMapView.onDestroy();
            removeView(this.mMapView);
            this.mMapLoaded = false;
        }
        AsyncFileOperations asyncFileOperations = this.mMapStyleFileLoader;
        if (asyncFileOperations != null) {
            asyncFileOperations.cancelLoad();
            this.mMapStyleFileLoader.cancel(true);
        }
        this.mContext = null;
        this.mMapView = null;
    }

    public void init(Context context, Float f, MapLatLng mapLatLng) {
        if (context == null) {
            return;
        }
        this.mContext = context;
        this.mMapView = new TextureMapView(GalleryApp.sGetAndroidContext(), buildOptions(f, mapLatLng));
        checkOrLoadMapStyleRes(getResources().getConfiguration());
        configMap();
    }

    public void configMap() {
        addView(this.mMapView);
        BaiduMap map = this.mMapView.getMap();
        this.mMap = map;
        if (map != null) {
            map.setOnMapLoadedCallback(this.mBDMapLoadedCallback);
            this.mMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() { // from class: com.miui.gallery.map.view.MapContainer.2
                @Override // com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener
                public void onMapStatusChange(MapStatus mapStatus) {
                }

                @Override // com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener
                public void onMapStatusChangeStart(MapStatus mapStatus) {
                }

                @Override // com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener
                public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
                }

                {
                    MapContainer.this = this;
                }

                @Override // com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener
                public void onMapStatusChangeFinish(MapStatus mapStatus) {
                    if (MapContainer.this.mMapStatusChangeListener != null) {
                        MapStatusWrapper mapStatusWrapper = new MapStatusWrapper();
                        mapStatusWrapper.setMapStatus(mapStatus);
                        MapContainer.this.mMapStatusChangeListener.onMapStatusChangeFinish(mapStatusWrapper);
                    }
                }
            });
            this.mMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() { // from class: com.miui.gallery.map.view.MapContainer$$ExternalSyntheticLambda0
                @Override // com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener
                public final boolean onMarkerClick(Marker marker) {
                    return MapContainer.m1086$r8$lambda$ryQ5FWYhyjSlyq27_rz5a7m8Uo(MapContainer.this, marker);
                }
            });
        }
    }

    public /* synthetic */ boolean lambda$configMap$0(Marker marker) {
        if (this.mClusterClickListener != null) {
            return this.mClusterClickListener.onMarkerClick(new MarkerWrapper(marker));
        }
        return false;
    }

    public final void checkOrLoadMapStyleRes(Configuration configuration) {
        final boolean z = (configuration.uiMode & 48) == 32;
        String absolutePath = this.mContext.getFilesDir().getAbsolutePath();
        StringBuilder sb = new StringBuilder();
        sb.append(absolutePath);
        sb.append(h.g);
        String str = "night_map_style.sty";
        sb.append(z ? str : "light_map_style.sty");
        String sb2 = sb.toString();
        if (MapConfig.checkMapCustomStyleAvailable(z)) {
            this.mMapView.setMapCustomStylePath(sb2);
            this.mMapView.setMapCustomStyleEnable(true);
            return;
        }
        if (this.mFileOperationCallback == null) {
            this.mFileOperationCallback = new AsyncFileOperations.FileOperationCallback() { // from class: com.miui.gallery.map.view.MapContainer$$ExternalSyntheticLambda3
                @Override // com.miui.gallery.map.utils.AsyncFileOperations.FileOperationCallback
                public final void onFinished(String str2) {
                    MapContainer.$r8$lambda$QiMRArzRXJOALPQ4rIibXgJNRBU(MapContainer.this, z, str2);
                }
            };
        }
        Context context = this.mContext;
        if (!z) {
            str = "light_map_style.sty";
        }
        AsyncFileOperations asyncFileOperations = new AsyncFileOperations(context, str, this.mFileOperationCallback);
        this.mMapStyleFileLoader = asyncFileOperations;
        asyncFileOperations.execute(new Void[0]);
    }

    public /* synthetic */ void lambda$checkOrLoadMapStyleRes$1(boolean z, String str) {
        if (!TextUtils.isEmpty(str)) {
            MapConfig.setMapCustomStyleAvailable(z);
            TextureMapView textureMapView = this.mMapView;
            if (textureMapView == null) {
                return;
            }
            textureMapView.setMapCustomStylePath(str);
            this.mMapView.setMapCustomStyleEnable(true);
            return;
        }
        DefaultLogger.d("MapContainer", "load custom map res failed");
        TextureMapView textureMapView2 = this.mMapView;
        if (textureMapView2 == null) {
            return;
        }
        textureMapView2.setMapCustomStyleEnable(false);
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public void setMapStatusChangeListener(OnMapStatusChangeListener onMapStatusChangeListener) {
        this.mMapStatusChangeListener = onMapStatusChangeListener;
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public void setMapLoadedCallback(OnMapLoadedCallback onMapLoadedCallback) {
        this.mMapLoadedCallback = onMapLoadedCallback;
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public void setClusterClickListener(OnMarkerClickListener onMarkerClickListener) {
        this.mClusterClickListener = onMarkerClickListener;
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public boolean hasMapLoaded() {
        return this.mMap != null && this.mMapLoaded;
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public void showLocationIcon(MapLatLng mapLatLng) {
        if (this.mMap == null) {
            return;
        }
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(null, false, null);
        MyLocationData build = new MyLocationData.Builder().accuracy(0.0f).direction(mapLatLng.direction).latitude(mapLatLng.latitude).longitude(mapLatLng.longitude).build();
        this.mMap.setMyLocationEnabled(true);
        this.mMap.setMyLocationConfiguration(myLocationConfiguration);
        this.mMap.setMyLocationData(build);
        this.mMap.setLayerClickable(MapLayer.MAP_LAYER_LOCATION, false);
        animateTo(mapLatLng, MapConfig.FOCUS_ZOOM_LEVEL.floatValue());
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public void addMarker(final String str, final int i, double d, double d2) {
        final MapLatLng convertToMapLatLng = LocationConverter.convertToMapLatLng(d, d2);
        ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.map.view.MapContainer$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public final Object mo1807run(ThreadPool.JobContext jobContext) {
                return MapContainer.$r8$lambda$zz78u8ZoSTm8gxS234UMBMItoLw(MapContainer.this, str, jobContext);
            }
        }, new FutureListener() { // from class: com.miui.gallery.map.view.MapContainer$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.concurrent.FutureListener
            public final void onFutureDone(Future future) {
                MapContainer.$r8$lambda$YG3ve7qgTqO1si4mTMtWleuue2A(MapContainer.this, i, convertToMapLatLng, future);
            }
        });
    }

    public /* synthetic */ Bitmap lambda$addMarker$2(String str, ThreadPool.JobContext jobContext) {
        return MapBitmapTool.getSourceBitmap(str, getResources().getDimensionPixelSize(R.dimen.custom_marker_image_width));
    }

    public /* synthetic */ void lambda$addMarker$3(int i, MapLatLng mapLatLng, Future future) {
        Context context;
        if (!future.isDone() || future.get() == null || this.mMap == null || (context = this.mContext) == null) {
            return;
        }
        IconGenerator iconGenerator = new IconGenerator(context);
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.map_marker_item, (ViewGroup) null);
        iconGenerator.setContentView(inflate);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.custom_marker_image);
        TextView textView = (TextView) inflate.findViewById(R.id.custom_marker_num);
        if (i > 1) {
            textView.setText(String.valueOf(i));
        } else {
            textView.setVisibility(8);
        }
        imageView.setImageBitmap((Bitmap) future.get());
        addOverlay(new MarkerOptionsWrapper().position(mapLatLng.latitude, mapLatLng.longitude).icon(BitmapDescriptorWrapperFactory.fromBitmap(iconGenerator.makeIcon())));
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public IMarker addOverlay(IMarkerOptions iMarkerOptions) {
        return new MarkerWrapper((Marker) this.mMap.addOverlay((OverlayOptions) iMarkerOptions.mo1087getOptions()));
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public void addMarkerAndFocus(String str, int i, double d, double d2, float f) {
        MapLatLng convertToMapLatLng = LocationConverter.convertToMapLatLng(d, d2);
        if (this.mMap != null) {
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(new LatLng(convertToMapLatLng.latitude, convertToMapLatLng.longitude));
            if (this.mMap.getMapStatus().zoom != f) {
                builder.zoom(f);
            }
            this.mMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
        addMarker(str, i, d, d2);
        setVisibility(0);
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public void clearOverlays() {
        BaiduMap baiduMap = this.mMap;
        if (baiduMap != null) {
            baiduMap.clear();
        }
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public void moveTo(double d, double d2, float f) {
        if (this.mMap != null) {
            MapStatus.Builder builder = new MapStatus.Builder();
            MapLatLng convertToMapLatLng = LocationConverter.convertToMapLatLng(d, d2);
            builder.target(new LatLng(convertToMapLatLng.latitude, convertToMapLatLng.longitude));
            builder.zoom(f);
            this.mMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    public void animateTo(MapLatLng mapLatLng, float f) {
        if (this.mMap != null) {
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(new LatLng(mapLatLng.latitude, mapLatLng.longitude));
            builder.zoom(f);
            this.mMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public float getZoomLevel() {
        return this.mMap.getMapStatus().zoom;
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public LatLngBounds getBound() {
        MapStatusWrapper mapStatusWrapper = new MapStatusWrapper();
        mapStatusWrapper.setMapStatus(this.mMap.getMapStatus());
        return mapStatusWrapper.getBound();
    }

    @Override // com.miui.gallery.map.view.IMapContainer
    public IMapStatus getMapStatus() {
        if (this.mMap != null) {
            MapStatusWrapper mapStatusWrapper = new MapStatusWrapper();
            mapStatusWrapper.setMapStatus(this.mMap.getMapStatus());
            return mapStatusWrapper;
        }
        return null;
    }
}
