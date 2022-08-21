package com.cdv.io;

import android.content.Context;
import android.view.OrientationEventListener;

/* loaded from: classes.dex */
public class NvOrientationEventListener {
    private static final String TAG = "OrientationEventListener";
    private int m_cameraId;
    private OrientationEventListener m_orientationEventListener;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void notifyOrientation(int i, int i2);

    private NvOrientationEventListener(int i, Context context) {
        this.m_cameraId = -1;
        this.m_cameraId = i;
        this.m_orientationEventListener = new OrientationEventListener(context, 3) { // from class: com.cdv.io.NvOrientationEventListener.1
            @Override // android.view.OrientationEventListener
            public void onOrientationChanged(int i2) {
                NvOrientationEventListener.notifyOrientation(NvOrientationEventListener.this.m_cameraId, i2);
            }
        };
    }

    public void enableListener() {
        if (this.m_orientationEventListener.canDetectOrientation()) {
            this.m_orientationEventListener.enable();
        }
    }

    public void disableListener() {
        if (this.m_orientationEventListener.canDetectOrientation()) {
            this.m_orientationEventListener.disable();
        }
    }
}
