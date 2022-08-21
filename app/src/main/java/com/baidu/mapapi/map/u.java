package com.baidu.mapapi.map;

import android.view.View;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class u implements Runnable {
    public final /* synthetic */ View a;
    public final /* synthetic */ MapView b;

    public u(MapView mapView, View view) {
        this.b = mapView;
        this.a = view;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.b.removeView(this.a);
    }
}
