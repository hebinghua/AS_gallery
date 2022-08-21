package com.baidu.platform.comapi.map;

import android.content.Context;
import android.view.SurfaceView;

/* loaded from: classes.dex */
public class ah {

    /* loaded from: classes.dex */
    public enum a {
        OPENGL_ES,
        VULKAN,
        AUTO
    }

    public static ag a(SurfaceView surfaceView, a aVar, Context context) {
        f fVar = new f(surfaceView);
        fVar.c(2);
        try {
            if (com.baidu.platform.comapi.util.g.a(8, 8, 8, 8, 24, 8)) {
                fVar.a(8, 8, 8, 8, 24, 8);
            } else {
                fVar.b(true);
            }
        } catch (IllegalArgumentException unused) {
            fVar.b(true);
        }
        fVar.a(true);
        return fVar;
    }
}
