package com.baidu.mapapi.map;

import android.view.View;
import com.baidu.platform.comapi.UIMsg;
import com.baidu.platform.comapi.map.MapSurfaceView;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ah implements View.OnClickListener {
    public final /* synthetic */ WearMapView a;

    public ah(WearMapView wearMapView) {
        this.a = wearMapView;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        MapSurfaceView mapSurfaceView;
        MapSurfaceView mapSurfaceView2;
        mapSurfaceView = this.a.f;
        com.baidu.mapsdkplatform.comapi.map.w C = mapSurfaceView.getBaseMap().C();
        C.a -= 1.0f;
        mapSurfaceView2 = this.a.f;
        mapSurfaceView2.getBaseMap().a(C, UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME);
    }
}
