package com.xiaomi.skyprocess;

import android.util.Log;

/* loaded from: classes3.dex */
public class RenderPlayerNotifierCallBack implements RenderPlayerNotifier {
    private static final String TAG = "RenderPlayerNotifier";
    private MiGLSurfaceViewRender mGLSurfaceViewRender;

    public void SetGLSurfaceViewRender(MiGLSurfaceViewRender miGLSurfaceViewRender) {
        Log.d(TAG, "SetGLSurfaceViewRender: " + miGLSurfaceViewRender);
        this.mGLSurfaceViewRender = miGLSurfaceViewRender;
    }

    @Override // com.xiaomi.skyprocess.RenderPlayerNotifier
    public void OnNotifyRender() {
        this.mGLSurfaceViewRender.ReadyRenderFrame();
        Log.d(TAG, "OnNotifyRender: end");
    }
}
