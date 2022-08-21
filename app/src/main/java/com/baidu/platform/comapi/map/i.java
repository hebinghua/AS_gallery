package com.baidu.platform.comapi.map;

import android.view.View;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class i implements View.OnLayoutChangeListener {
    public final /* synthetic */ h a;

    public i(h hVar) {
        this.a = hVar;
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        h hVar = this.a;
        hVar.onSurfaceTextureSizeChanged(hVar.getSurfaceTexture(), i3 - i, i4 - i2);
    }
}
