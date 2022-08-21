package com.baidu.mapapi.map;

import android.text.TextUtils;
import com.baidu.mapsdkplatform.comapi.map.e;

/* loaded from: classes.dex */
class aa implements e.a {
    public final /* synthetic */ CustomMapStyleCallBack a;
    public final /* synthetic */ MapCustomStyleOptions b;
    public final /* synthetic */ TextureMapView c;

    public aa(TextureMapView textureMapView, CustomMapStyleCallBack customMapStyleCallBack, MapCustomStyleOptions mapCustomStyleOptions) {
        this.c = textureMapView;
        this.a = customMapStyleCallBack;
        this.b = mapCustomStyleOptions;
    }

    @Override // com.baidu.mapsdkplatform.comapi.map.e.a
    public void a(int i, String str, String str2) {
        boolean z;
        CustomMapStyleCallBack customMapStyleCallBack = this.a;
        if (customMapStyleCallBack == null || !customMapStyleCallBack.onCustomMapStyleLoadFailed(i, str, str2)) {
            z = this.c.B;
            if (z) {
                return;
            }
            this.c.a(str2, this.b);
        }
    }

    @Override // com.baidu.mapsdkplatform.comapi.map.e.a
    public void a(String str) {
        CustomMapStyleCallBack customMapStyleCallBack = this.a;
        if (customMapStyleCallBack == null || !customMapStyleCallBack.onPreLoadLastCustomMapStyle(str)) {
            this.c.a(str, this.b);
            this.c.B = true;
        }
    }

    @Override // com.baidu.mapsdkplatform.comapi.map.e.a
    public void a(boolean z, String str) {
        CustomMapStyleCallBack customMapStyleCallBack = this.a;
        if ((customMapStyleCallBack == null || !customMapStyleCallBack.onCustomMapStyleLoadSuccess(z, str)) && !TextUtils.isEmpty(str)) {
            this.c.a(str, "");
            this.c.setMapCustomStyleEnable(true);
        }
    }
}
