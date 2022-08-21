package com.miui.gallery.xmstreaming;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public class XmsContext {
    public static int MEDIA_INFO_STARTED_AS_NEXT = 0;
    private static final int PLAYER_EVENT_COMPILE_PROGRESS = 203;
    private static final int PLAYER_EVENT_COMPILE_RESULT = 204;
    private static final int PLAYER_EVENT_PLAY_EOF = 202;
    private static final int PLAYER_EVENT_PROGRESS_CHANGED = 201;
    private static final int PLAYER_EVENT_STATE_CHANGED = 200;
    public static final int STATE_PAUSING = 2;
    public static final int STATE_PLAYING = 1;
    public static final int STATE_SEEKING = 3;
    private static final String TAG = "XMSContext";
    private static volatile boolean mIsNativeInitialized;
    private static XmsContext mXmsContext = new XmsContext();
    private CompileCallback mCompileCallback;
    private EventHandler mEventHandler;
    private PlaybackCallback mPlaybackCallback;
    private StreamingEngineCallback mStreamingEngineCallback;

    /* loaded from: classes3.dex */
    public interface CompileCallback {
        void onCompileFailed(boolean z);

        void onCompileFinished();

        void onCompileProgress(int i);
    }

    /* loaded from: classes3.dex */
    public interface PlaybackCallback {
        void onPlaybackEOF();

        void onPlaybackPreloadingCompletion();

        void onPlaybackTimelinePosition(int i);
    }

    /* loaded from: classes3.dex */
    public interface StreamingEngineCallback {
        void onFirstVideoFramePresented();

        void onStreamingEngineStateChanged(int i);
    }

    private native boolean nativeApplyTimeline(long j);

    private native boolean nativeCancelCompile(long j);

    private native boolean nativeCompile(long j, int i, int i2, String str);

    private native boolean nativeConnectTimelineWithSurface(long j, Surface surface);

    private native XmsTimeline nativeCreateTimeline(int i, int i2);

    private static native void nativeInit(boolean z);

    private static native boolean nativeIsPlaying();

    private static native boolean nativeIsReadyForExport();

    private static native boolean nativeIsReadyForSwitch();

    private static native void nativePause();

    private static native void nativePauseToBackground();

    private static native void nativeRelease();

    private static native void nativeResume();

    private static native int nativeResumeToForeground(long j);

    private static native void nativeSeekTo(int i);

    private static native void nativeSet(Object obj);

    private static native void nativeSkipPause();

    private static native void nativeStartPreview();

    private static native void nativeStop();

    public native void nativeSurfaceChanged(Surface surface, int i, int i2);

    static {
        System.loadLibrary("XmStreamingSDK");
        mIsNativeInitialized = false;
        MEDIA_INFO_STARTED_AS_NEXT = 2;
    }

    private static void initNativeOnce() {
        synchronized (XmsContext.class) {
            if (!mIsNativeInitialized) {
                mIsNativeInitialized = true;
            }
        }
    }

    public static XmsContext getInstance() {
        return mXmsContext;
    }

    public void initPlayer(boolean z) {
        initNativeOnce();
        Looper myLooper = Looper.myLooper();
        if (myLooper != null) {
            this.mEventHandler = new EventHandler(this, myLooper);
        } else {
            Looper mainLooper = Looper.getMainLooper();
            if (mainLooper != null) {
                this.mEventHandler = new EventHandler(this, mainLooper);
            } else {
                this.mEventHandler = null;
            }
        }
        nativeSet(new WeakReference(this));
        nativeInit(z);
    }

    public void release() {
        Log.d(TAG, "release");
        this.mEventHandler.removeCallbacksAndMessages(null);
        synchronized (XmsContext.class) {
            nativeRelease();
        }
    }

    public void compileTimeline(XmsTimeline xmsTimeline, String str, CompileCallback compileCallback) {
        this.mCompileCallback = compileCallback;
        synchronized (XmsContext.class) {
            nativeCompile(xmsTimeline.m_internalObject, xmsTimeline.getPreferWidth(), xmsTimeline.getPreferHeight(), str);
        }
    }

    public boolean cancelCompile(long j) {
        boolean nativeCancelCompile;
        synchronized (XmsContext.class) {
            nativeCancelCompile = nativeCancelCompile(j);
        }
        return nativeCancelCompile;
    }

    public void startPreview() {
        synchronized (XmsContext.class) {
            nativeStartPreview();
        }
    }

    public boolean isPlaying() {
        boolean nativeIsPlaying;
        synchronized (XmsContext.class) {
            nativeIsPlaying = nativeIsPlaying();
        }
        return nativeIsPlaying;
    }

    public void pause() {
        synchronized (XmsContext.class) {
            nativePause();
        }
    }

    public void pauseToBackground() {
        synchronized (XmsContext.class) {
            nativePauseToBackground();
        }
    }

    public int resumeToForeground(long j) {
        int nativeResumeToForeground;
        synchronized (XmsContext.class) {
            nativeResumeToForeground = nativeResumeToForeground(j);
        }
        return nativeResumeToForeground;
    }

    public void resume() {
        synchronized (XmsContext.class) {
            nativeResume();
        }
    }

    public void stop() {
        synchronized (XmsContext.class) {
            nativeStop();
        }
    }

    public boolean isReadyForSwitch() {
        boolean nativeIsReadyForSwitch;
        synchronized (XmsContext.class) {
            nativeIsReadyForSwitch = nativeIsReadyForSwitch();
        }
        return nativeIsReadyForSwitch;
    }

    public boolean isReadyForExport() {
        boolean nativeIsReadyForExport;
        synchronized (XmsContext.class) {
            nativeIsReadyForExport = nativeIsReadyForExport();
        }
        return nativeIsReadyForExport;
    }

    public void skipPause() {
        synchronized (XmsContext.class) {
            nativeSkipPause();
        }
    }

    public XmsTimeline createTimeline(int i, int i2) {
        XmsTimeline nativeCreateTimeline;
        synchronized (XmsContext.class) {
            nativeCreateTimeline = nativeCreateTimeline(i, i2);
        }
        return nativeCreateTimeline;
    }

    public boolean connectTimelineWithSurface(XmsTimeline xmsTimeline, Surface surface) {
        boolean nativeConnectTimelineWithSurface;
        synchronized (XmsContext.class) {
            nativeConnectTimelineWithSurface = nativeConnectTimelineWithSurface(xmsTimeline.getInternalObject(), surface);
        }
        return nativeConnectTimelineWithSurface;
    }

    public boolean applyTimeline(XmsTimeline xmsTimeline) {
        boolean nativeApplyTimeline;
        synchronized (XmsContext.class) {
            nativeApplyTimeline = nativeApplyTimeline(xmsTimeline.getInternalObject());
            if (nativeApplyTimeline) {
                nativeStartPreview();
            }
        }
        return nativeApplyTimeline;
    }

    public void seekToPos(int i) {
        synchronized (XmsContext.class) {
            nativeSeekTo(i);
        }
    }

    public void setPlaybackCallback(PlaybackCallback playbackCallback) {
        this.mPlaybackCallback = playbackCallback;
    }

    public void setStreamingEngineCallback(StreamingEngineCallback streamingEngineCallback) {
        this.mStreamingEngineCallback = streamingEngineCallback;
    }

    private static void postEventFromNative(Object obj, int i, int i2, int i3, Object obj2) {
        XmsContext xmsContext;
        EventHandler eventHandler;
        if (obj == null || (xmsContext = (XmsContext) ((WeakReference) obj).get()) == null || (eventHandler = xmsContext.mEventHandler) == null) {
            return;
        }
        xmsContext.mEventHandler.sendMessage(eventHandler.obtainMessage(i, i2, i3, obj2));
    }

    /* loaded from: classes3.dex */
    public static class EventHandler extends Handler {
        private final WeakReference<XmsContext> mWeakPlayer;

        public EventHandler(XmsContext xmsContext, Looper looper) {
            super(looper);
            this.mWeakPlayer = new WeakReference<>(xmsContext);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            XmsContext xmsContext = this.mWeakPlayer.get();
            if (xmsContext == null) {
                Log.w(XmsContext.TAG, "XmsContext went away with unhandled events");
                return;
            }
            Log.d(XmsContext.TAG, "get msg " + message.what + message.arg1);
            switch (message.what) {
                case 200:
                    xmsContext.notifyStateChanged(message.arg1);
                    return;
                case 201:
                    xmsContext.notifyProgressChanged(message.arg1);
                    return;
                case 202:
                    xmsContext.notifyPlayEOF();
                    return;
                case 203:
                    xmsContext.notifyCompileProgress(message.arg1);
                    return;
                case 204:
                    xmsContext.notifyCompileResult(message.arg1);
                    return;
                default:
                    Log.e(XmsContext.TAG, "Unknown message type " + message.what);
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyStateChanged(int i) {
        StreamingEngineCallback streamingEngineCallback = this.mStreamingEngineCallback;
        if (streamingEngineCallback != null) {
            streamingEngineCallback.onStreamingEngineStateChanged(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyProgressChanged(int i) {
        PlaybackCallback playbackCallback;
        if (i == 0 && (playbackCallback = this.mPlaybackCallback) != null) {
            playbackCallback.onPlaybackPreloadingCompletion();
        }
        PlaybackCallback playbackCallback2 = this.mPlaybackCallback;
        if (playbackCallback2 != null) {
            playbackCallback2.onPlaybackTimelinePosition(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyPlayEOF() {
        PlaybackCallback playbackCallback = this.mPlaybackCallback;
        if (playbackCallback != null) {
            playbackCallback.onPlaybackEOF();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyCompileProgress(int i) {
        CompileCallback compileCallback = this.mCompileCallback;
        if (compileCallback != null) {
            compileCallback.onCompileProgress(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyCompileResult(int i) {
        CompileCallback compileCallback = this.mCompileCallback;
        if (compileCallback != null) {
            if (i >= 0) {
                compileCallback.onCompileFinished();
            } else if (i == -1) {
                compileCallback.onCompileFailed(true);
            } else {
                compileCallback.onCompileFailed(false);
            }
        }
    }
}
