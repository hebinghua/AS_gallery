package com.baidu.mapapi.map;

import android.util.SparseArray;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.MapSurfaceView;
import com.baidu.platform.comapi.map.ak;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ag implements ak {
    public final /* synthetic */ WearMapView a;

    public ag(WearMapView wearMapView) {
        this.a = wearMapView;
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void a() {
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void a(MotionEvent motionEvent) {
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void a(com.baidu.mapsdkplatform.comapi.map.w wVar) {
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void a(GeoPoint geoPoint) {
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void a(String str) {
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void a(GL10 gl10, com.baidu.mapsdkplatform.comapi.map.w wVar) {
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void a(boolean z) {
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void a(boolean z, int i) {
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void b() {
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void b(com.baidu.mapsdkplatform.comapi.map.w wVar) {
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void b(GeoPoint geoPoint) {
    }

    @Override // com.baidu.platform.comapi.map.ak
    public boolean b(String str) {
        return false;
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void c() {
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void c(com.baidu.mapsdkplatform.comapi.map.w wVar) {
        MapSurfaceView mapSurfaceView;
        MapSurfaceView mapSurfaceView2;
        MapSurfaceView mapSurfaceView3;
        MapSurfaceView mapSurfaceView4;
        MapSurfaceView mapSurfaceView5;
        MapSurfaceView mapSurfaceView6;
        float f;
        SparseArray sparseArray;
        MapSurfaceView mapSurfaceView7;
        ImageView imageView;
        String format;
        TextView textView;
        TextView textView2;
        MapSurfaceView mapSurfaceView8;
        mapSurfaceView = this.a.f;
        if (mapSurfaceView != null) {
            mapSurfaceView2 = this.a.f;
            if (mapSurfaceView2.getBaseMap() == null) {
                return;
            }
            mapSurfaceView3 = this.a.f;
            float zoomLevel = mapSurfaceView3.getZoomLevel();
            mapSurfaceView4 = this.a.f;
            if (zoomLevel < mapSurfaceView4.getController().mMinZoomLevel) {
                mapSurfaceView8 = this.a.f;
                zoomLevel = mapSurfaceView8.getController().mMinZoomLevel;
            } else {
                mapSurfaceView5 = this.a.f;
                if (zoomLevel > mapSurfaceView5.getController().mMaxZoomLevel) {
                    mapSurfaceView6 = this.a.f;
                    zoomLevel = mapSurfaceView6.getController().mMaxZoomLevel;
                }
            }
            f = this.a.A;
            if (Math.abs(f - zoomLevel) > 0.0f) {
                sparseArray = WearMapView.x;
                int intValue = ((Integer) sparseArray.get(Math.round(zoomLevel))).intValue();
                mapSurfaceView7 = this.a.f;
                imageView = this.a.r;
                int zoomUnitsInMeter = ((int) (intValue / mapSurfaceView7.getController().getZoomUnitsInMeter())) / 2;
                imageView.setPadding(zoomUnitsInMeter, 0, zoomUnitsInMeter, 0);
                Object[] objArr = new Object[1];
                if (intValue >= 1000) {
                    objArr[0] = Integer.valueOf(intValue / 1000);
                    format = String.format(" %d公里 ", objArr);
                } else {
                    objArr[0] = Integer.valueOf(intValue);
                    format = String.format(" %d米 ", objArr);
                }
                textView = this.a.p;
                textView.setText(format);
                textView2 = this.a.q;
                textView2.setText(format);
                this.a.A = zoomLevel;
            }
            this.a.requestLayout();
        }
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void c(GeoPoint geoPoint) {
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void d() {
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void d(GeoPoint geoPoint) {
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void e(GeoPoint geoPoint) {
    }
}
