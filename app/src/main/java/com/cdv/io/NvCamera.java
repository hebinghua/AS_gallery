package com.cdv.io;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.OrientationEventListener;
import com.cdv.io.NvAndroidAudioRecorder;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;

/* loaded from: classes.dex */
public class NvCamera implements Camera.ErrorCallback, Camera.AutoFocusCallback, Camera.OnZoomChangeListener, Camera.PreviewCallback, NvAndroidAudioRecorder.RecordDataCallback {
    private static final int PREVIEW_BUFFER_COUNT = 3;
    private static final String TAG = "CDV Camera";
    private Camera m_camera;
    private int m_cameraId;
    private OrientationEventListener m_orientationEventListener;
    private byte[][] m_previewCallbackBuffer;
    private NvAndroidAudioRecorder m_audioRecorder = null;
    private Camera.Size m_previewSize = null;

    /* loaded from: classes.dex */
    public static class CameraOpenParam {
        public Camera m_cam;
        public Semaphore m_semaphore;
    }

    private static native void notifyAudioRecordData(int i, ByteBuffer byteBuffer, int i2);

    private static native void notifyAutoFocusComplete(int i, boolean z);

    private static native void notifyError(int i, int i2);

    private static native void notifyNewPreviewFrame(int i, byte[] bArr, int i2, int i3);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void notifyOrientationChange(int i, int i2);

    private static native void notifyZoomChange(int i, int i2, boolean z);

    private NvCamera(int i, Camera camera, Context context) {
        this.m_cameraId = -1;
        this.m_camera = null;
        this.m_cameraId = i;
        this.m_camera = camera;
        camera.setErrorCallback(this);
        camera.setZoomChangeListener(this);
        this.m_orientationEventListener = new OrientationEventListener(context, 3) { // from class: com.cdv.io.NvCamera.1
            @Override // android.view.OrientationEventListener
            public void onOrientationChanged(int i2) {
                NvCamera.notifyOrientationChange(NvCamera.this.m_cameraId, i2);
            }
        };
    }

    public static NvCamera open(final int i, Context context, Handler handler) {
        Camera open;
        try {
            if (handler != null) {
                final CameraOpenParam cameraOpenParam = new CameraOpenParam();
                cameraOpenParam.m_semaphore = new Semaphore(0);
                handler.post(new Runnable() { // from class: com.cdv.io.NvCamera.2
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            try {
                                CameraOpenParam.this.m_cam = Camera.open(i);
                            } catch (Exception e) {
                                Log.e(NvCamera.TAG, "" + e.getMessage());
                                e.printStackTrace();
                            }
                        } finally {
                            CameraOpenParam.this.m_semaphore.release();
                        }
                    }
                });
                cameraOpenParam.m_semaphore.acquire();
                open = cameraOpenParam.m_cam;
                if (open == null) {
                    return null;
                }
            } else {
                open = Camera.open(i);
            }
            return new NvCamera(i, open, context);
        } catch (Exception e) {
            Log.e(TAG, "Failed to open camera(index=" + i + ")!");
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(e.getMessage());
            Log.e(TAG, sb.toString());
            return null;
        }
    }

    public Camera.Parameters getParameters() {
        return this.m_camera.getParameters();
    }

    public void lock() {
        try {
            this.m_camera.lock();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public void unlock() {
        try {
            this.m_camera.unlock();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public void release() {
        this.m_camera.release();
    }

    public void reconnect() {
        try {
            this.m_camera.reconnect();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public void setDisplayOrientation(int i) {
        this.m_camera.setDisplayOrientation(i);
    }

    public void setParameters(Camera.Parameters parameters) {
        try {
            this.m_camera.setParameters(parameters);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public void setPreviewTexture(SurfaceTexture surfaceTexture) {
        try {
            this.m_camera.setPreviewTexture(surfaceTexture);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public int startPreview(boolean z, boolean z2) {
        if (z2) {
            NvAndroidAudioRecorder nvAndroidAudioRecorder = new NvAndroidAudioRecorder();
            this.m_audioRecorder = nvAndroidAudioRecorder;
            if (!nvAndroidAudioRecorder.startRecord(this)) {
                this.m_audioRecorder.releaseAudioRecorder();
                this.m_audioRecorder = null;
            }
        }
        if (this.m_orientationEventListener.canDetectOrientation()) {
            this.m_orientationEventListener.enable();
        }
        if (z) {
            try {
                Camera.Size previewSize = this.m_camera.getParameters().getPreviewSize();
                this.m_previewSize = previewSize;
                if (this.m_previewCallbackBuffer == null) {
                    this.m_previewCallbackBuffer = (byte[][]) Array.newInstance(byte.class, 3, ((previewSize.width * previewSize.height) * 3) / 2);
                }
                this.m_camera.setPreviewCallbackWithBuffer(this);
                for (int i = 0; i < 3; i++) {
                    this.m_camera.addCallbackBuffer(this.m_previewCallbackBuffer[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "" + e.getMessage());
                return 2;
            }
        }
        if (Build.VERSION.SDK_INT >= 24) {
            this.m_camera.setDisplayOrientation(0);
        }
        this.m_camera.startPreview();
        if (z2) {
            if (this.m_audioRecorder == null) {
                return 1;
            }
        }
        return 0;
    }

    public void stopPreview() {
        NvAndroidAudioRecorder nvAndroidAudioRecorder = this.m_audioRecorder;
        if (nvAndroidAudioRecorder != null) {
            nvAndroidAudioRecorder.stopRecord();
            this.m_audioRecorder.releaseAudioRecorder();
            this.m_audioRecorder = null;
        }
        if (this.m_orientationEventListener.canDetectOrientation()) {
            this.m_orientationEventListener.disable();
        }
        this.m_camera.stopPreview();
        this.m_camera.setPreviewCallbackWithBuffer(null);
        this.m_previewCallbackBuffer = null;
    }

    public void autoFocus() {
        this.m_camera.autoFocus(this);
    }

    public void cancelAutoFocus() {
        this.m_camera.cancelAutoFocus();
    }

    public void startSmoothZoom(int i) {
        try {
            this.m_camera.startSmoothZoom(i);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public void stopSmoothZoom() {
        try {
            this.m_camera.stopSmoothZoom();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    @Override // android.hardware.Camera.ErrorCallback
    public void onError(int i, Camera camera) {
        notifyError(this.m_cameraId, i);
    }

    @Override // android.hardware.Camera.AutoFocusCallback
    public void onAutoFocus(boolean z, Camera camera) {
        notifyAutoFocusComplete(this.m_cameraId, z);
    }

    @Override // android.hardware.Camera.OnZoomChangeListener
    public void onZoomChange(int i, boolean z, Camera camera) {
        notifyZoomChange(this.m_cameraId, i, z);
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] bArr, Camera camera) {
        if (bArr != null) {
            int i = this.m_cameraId;
            Camera.Size size = this.m_previewSize;
            notifyNewPreviewFrame(i, bArr, size.width, size.height);
            this.m_camera.addCallbackBuffer(bArr);
        }
    }

    @Override // com.cdv.io.NvAndroidAudioRecorder.RecordDataCallback
    public void onAudioRecordDataArrived(ByteBuffer byteBuffer, int i) {
        notifyAudioRecordData(this.m_cameraId, byteBuffer, i);
    }
}
