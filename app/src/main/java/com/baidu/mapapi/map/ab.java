package com.baidu.mapapi.map;

import android.util.SparseArray;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.MapTextureView;
import com.baidu.platform.comapi.map.ak;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ab implements ak {
    public final /* synthetic */ TextureMapView a;

    public ab(TextureMapView textureMapView) {
        this.a = textureMapView;
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
        MapTextureView mapTextureView;
        MapTextureView mapTextureView2;
        MapTextureView mapTextureView3;
        MapTextureView mapTextureView4;
        MapTextureView mapTextureView5;
        MapTextureView mapTextureView6;
        float f;
        SparseArray sparseArray;
        MapTextureView mapTextureView7;
        ImageView imageView;
        String format;
        TextView textView;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        ImageView imageView2;
        MapTextureView mapTextureView8;
        mapTextureView = this.a.b;
        if (mapTextureView != null) {
            mapTextureView2 = this.a.b;
            if (mapTextureView2.getController() == null) {
                return;
            }
            mapTextureView3 = this.a.b;
            float zoomLevel = mapTextureView3.getZoomLevel();
            mapTextureView4 = this.a.b;
            if (zoomLevel < mapTextureView4.getController().mMinZoomLevel) {
                mapTextureView8 = this.a.b;
                zoomLevel = mapTextureView8.getController().mMinZoomLevel;
            } else {
                mapTextureView5 = this.a.b;
                if (zoomLevel > mapTextureView5.getController().mMaxZoomLevel) {
                    mapTextureView6 = this.a.b;
                    zoomLevel = mapTextureView6.getController().mMaxZoomLevel;
                }
            }
            f = this.a.r;
            if (Math.abs(f - zoomLevel) > 0.0f) {
                sparseArray = TextureMapView.q;
                int intValue = ((Integer) sparseArray.get(Math.round(zoomLevel))).intValue();
                mapTextureView7 = this.a.b;
                int zoomUnitsInMeter = (int) (intValue / mapTextureView7.getController().getZoomUnitsInMeter());
                imageView = this.a.o;
                if (imageView != null) {
                    imageView2 = this.a.o;
                    int i = zoomUnitsInMeter / 2;
                    imageView2.setPadding(i, 0, i, 0);
                }
                Object[] objArr = new Object[1];
                if (intValue >= 1000) {
                    objArr[0] = Integer.valueOf(intValue / 1000);
                    format = String.format(" %d公里 ", objArr);
                } else {
                    objArr[0] = Integer.valueOf(intValue);
                    format = String.format(" %d米 ", objArr);
                }
                textView = this.a.m;
                if (textView != null) {
                    textView4 = this.a.m;
                    textView4.setText(format);
                }
                textView2 = this.a.n;
                if (textView2 != null) {
                    textView3 = this.a.n;
                    textView3.setText(format);
                }
                this.a.r = zoomLevel;
            }
            this.a.b();
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
