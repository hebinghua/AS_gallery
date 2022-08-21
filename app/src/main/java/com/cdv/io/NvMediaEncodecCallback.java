package com.cdv.io;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class NvMediaEncodecCallback {
    private static final String TAG = "NvMediaEncodecCallback";
    private HandlerThread mCallbackThread = null;
    private long m_contextInterface;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeOnError(long j, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeOnOutputBufferAvailable(long j, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeOnOutputFormatChanged(long j, MediaFormat mediaFormat);

    public NvMediaEncodecCallback(long j) {
        this.m_contextInterface = -1L;
        this.m_contextInterface = j;
    }

    public boolean setCallbackToCodec(MediaCodec mediaCodec) {
        if (mediaCodec != null && Build.VERSION.SDK_INT >= 21) {
            if (this.mCallbackThread == null) {
                HandlerThread handlerThread = new HandlerThread("callback handler");
                this.mCallbackThread = handlerThread;
                handlerThread.start();
            }
            Looper looper = this.mCallbackThread.getLooper();
            if (looper == null) {
                closeCallbackThread();
                Log.e(TAG, "Failed to getLooper of the background thread!");
                return false;
            }
            mediaCodec.setCallback(new MediaCodec.Callback() { // from class: com.cdv.io.NvMediaEncodecCallback.1
                @Override // android.media.MediaCodec.Callback
                public void onInputBufferAvailable(MediaCodec mediaCodec2, int i) {
                    Log.d(NvMediaEncodecCallback.TAG, "onInputBufferAvailable");
                }

                @Override // android.media.MediaCodec.Callback
                public void onOutputBufferAvailable(MediaCodec mediaCodec2, int i, MediaCodec.BufferInfo bufferInfo) {
                    if (NvMediaEncodecCallback.this.m_contextInterface > 0 && bufferInfo != null) {
                        try {
                            NvMediaEncodecCallback.nativeOnOutputBufferAvailable(NvMediaEncodecCallback.this.m_contextInterface, mediaCodec2.getOutputBuffer(i), bufferInfo);
                            mediaCodec2.releaseOutputBuffer(i, false);
                        } catch (Exception e) {
                            Log.e(NvMediaEncodecCallback.TAG, "MediaCodec.releaseOutputBuffer failed!");
                            e.printStackTrace();
                        }
                    }
                }

                @Override // android.media.MediaCodec.Callback
                public void onError(MediaCodec mediaCodec2, MediaCodec.CodecException codecException) {
                    if (NvMediaEncodecCallback.this.m_contextInterface <= 0) {
                        return;
                    }
                    int i = -1;
                    if (codecException != null) {
                        i = codecException.getErrorCode();
                    }
                    Log.d(NvMediaEncodecCallback.TAG, "onErrorCode:" + i);
                    NvMediaEncodecCallback.nativeOnError(NvMediaEncodecCallback.this.m_contextInterface, i);
                }

                @Override // android.media.MediaCodec.Callback
                public void onOutputFormatChanged(MediaCodec mediaCodec2, MediaFormat mediaFormat) {
                    if (NvMediaEncodecCallback.this.m_contextInterface <= 0) {
                        return;
                    }
                    Log.d(NvMediaEncodecCallback.TAG, "onOutputFormatChanged");
                    NvMediaEncodecCallback.nativeOnOutputFormatChanged(NvMediaEncodecCallback.this.m_contextInterface, mediaFormat);
                }
            }, new Handler(looper));
            return true;
        }
        return false;
    }

    public void cleanUp() {
        closeCallbackThread();
    }

    private void closeCallbackThread() {
        HandlerThread handlerThread = this.mCallbackThread;
        if (handlerThread != null && Build.VERSION.SDK_INT >= 21) {
            if (handlerThread.isAlive()) {
                this.mCallbackThread.quitSafely();
            }
            try {
                this.mCallbackThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mCallbackThread = null;
        }
    }
}
