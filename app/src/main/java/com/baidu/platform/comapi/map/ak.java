package com.baidu.platform.comapi.map;

import android.view.MotionEvent;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public interface ak {
    void a();

    void a(MotionEvent motionEvent);

    void a(com.baidu.mapsdkplatform.comapi.map.w wVar);

    void a(GeoPoint geoPoint);

    void a(String str);

    void a(GL10 gl10, com.baidu.mapsdkplatform.comapi.map.w wVar);

    void a(boolean z);

    void a(boolean z, int i);

    void b();

    void b(com.baidu.mapsdkplatform.comapi.map.w wVar);

    void b(GeoPoint geoPoint);

    boolean b(String str);

    void c();

    void c(com.baidu.mapsdkplatform.comapi.map.w wVar);

    void c(GeoPoint geoPoint);

    void d();

    void d(GeoPoint geoPoint);

    void e(GeoPoint geoPoint);
}
