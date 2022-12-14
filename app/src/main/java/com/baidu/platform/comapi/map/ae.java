package com.baidu.platform.comapi.map;

import android.os.Bundle;
import com.baidu.platform.comjni.map.basemap.AppBaseMap;
import java.util.HashMap;

/* loaded from: classes.dex */
public final class ae implements com.baidu.platform.comjni.map.basemap.a {
    public static final String a = "com.baidu.platform.comapi.map.ae";
    public HashMap<Long, InnerOverlay> b = new HashMap<>();
    public AppBaseMap c;

    public ae(AppBaseMap appBaseMap) {
        this.c = null;
        this.c = appBaseMap;
    }

    @Override // com.baidu.platform.comjni.map.basemap.a
    public int a(Bundle bundle, long j, int i) {
        long currentTimeMillis = y.a ? System.currentTimeMillis() : 0L;
        InnerOverlay innerOverlay = this.b.get(Long.valueOf(j));
        if (innerOverlay != null) {
            String data = innerOverlay.getData();
            if (this.c.LayersIsShow(j)) {
                bundle.putString("jsondata", data);
                Bundle param = innerOverlay.getParam();
                if (param != null) {
                    bundle.putBundle("param", param);
                }
            } else {
                bundle.putString("jsondata", null);
            }
            if (y.a) {
                String str = a;
                y.a(str, "MapLayerDataReq:" + j + " tag:" + innerOverlay.getLayerTag() + " [" + (System.currentTimeMillis() - currentTimeMillis) + "ms] LayerData:" + data);
            }
            return innerOverlay.getType();
        }
        return 0;
    }

    public void a() {
        if (this.c != null) {
            for (Long l : this.b.keySet()) {
                if (l.longValue() > 0) {
                    this.c.ClearLayer(l.longValue());
                    this.c.RemoveLayer(l.longValue());
                }
            }
        }
        this.b.clear();
    }

    public void a(InnerOverlay innerOverlay) {
        this.b.put(Long.valueOf(innerOverlay.mLayerID), innerOverlay);
        innerOverlay.SetMapParam(innerOverlay.mLayerID, this.c);
    }

    public void a(Overlay overlay) {
        this.b.remove(Long.valueOf(overlay.mLayerID));
    }

    @Override // com.baidu.platform.comjni.map.basemap.a
    public boolean a(long j) {
        return this.b.containsKey(Long.valueOf(j));
    }
}
