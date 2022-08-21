package com.miui.gallery.map.location;

import android.content.Context;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.miui.gallery.map.cluster.MapLatLng;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class BDLocationClientImpl implements ILocationClient {
    public BDAbstractLocationListener mBDLocationListener;
    public LocationClient mLocationClient;
    public ILocationListener mLocationListener;

    @Override // com.miui.gallery.map.location.ILocationClient
    public void init(Context context) {
        this.mLocationClient = new LocationClient(context);
        LocationClientOption locationClientOption = new LocationClientOption();
        locationClientOption.setCoorType("bd09ll");
        locationClientOption.setScanSpan(0);
        locationClientOption.setOnceLocation(true);
        locationClientOption.setOpenGps(true);
        locationClientOption.setIsNeedAddress(false);
        this.mLocationClient.setLocOption(locationClientOption);
        this.mBDLocationListener = new BDAbstractLocationListener() { // from class: com.miui.gallery.map.location.BDLocationClientImpl.1
            @Override // com.baidu.location.BDAbstractLocationListener
            public void onReceiveLocation(BDLocation bDLocation) {
                if (bDLocation != null) {
                    if (BDLocationClientImpl.this.mLocationListener == null) {
                        return;
                    }
                    DefaultLogger.d("LocationClientImpl", "onReceiveLocation: loctype->%s", Integer.valueOf(bDLocation.getLocType()));
                    if (bDLocation.getLocType() == 61 || bDLocation.getLocType() == 161) {
                        BDLocationClientImpl.this.mLocationListener.onReceiveLocationSuccess(new MapLatLng(bDLocation.getLatitude(), bDLocation.getLongitude(), bDLocation.getDirection()));
                        return;
                    } else {
                        BDLocationClientImpl.this.mLocationListener.onReceiveLocationFailed(bDLocation.getLocType());
                        return;
                    }
                }
                DefaultLogger.d("LocationClientImpl", "onReceiveLocation failed");
            }
        };
    }

    @Override // com.miui.gallery.map.location.ILocationClient
    public void start() {
        this.mLocationClient.registerLocationListener(this.mBDLocationListener);
        this.mLocationClient.start();
    }

    @Override // com.miui.gallery.map.location.ILocationClient
    public void stop() {
        this.mLocationClient.unRegisterLocationListener(this.mBDLocationListener);
        this.mLocationClient.stop();
    }

    @Override // com.miui.gallery.map.location.ILocationClient
    public void registerLocationListener(ILocationListener iLocationListener) {
        this.mLocationListener = iLocationListener;
    }

    @Override // com.miui.gallery.map.location.ILocationClient
    public void unregisterLocationListener() {
        LocationClient locationClient;
        this.mLocationListener = null;
        BDAbstractLocationListener bDAbstractLocationListener = this.mBDLocationListener;
        if (bDAbstractLocationListener == null || (locationClient = this.mLocationClient) == null) {
            return;
        }
        locationClient.unRegisterLocationListener(bDAbstractLocationListener);
    }
}
