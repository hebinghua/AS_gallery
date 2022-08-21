package com.xiaomi.milab.videosdk;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Looper;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import com.xiaomi.milab.videosdk.interfaces.AudioExtractCallback;
import com.xiaomi.milab.videosdk.interfaces.ExportCallback;
import com.xiaomi.milab.videosdk.interfaces.PlayCallback;
import com.xiaomi.milab.videosdk.interfaces.TimelineCallback;
import com.xiaomi.milab.videosdk.message.EventHandler;
import com.xiaomi.milab.videosdk.message.MsgProxy;
import com.xiaomi.milab.videosdk.message.MsgType;

/* loaded from: classes3.dex */
public class XmsContext extends XmsNativeObject {
    public static final String TAG = "XmsContext";
    private static XmsContext mInstance;
    private static Object mObj = new Object();
    private boolean bHasInit = false;
    private Context mContext;
    private Surface mCurrentSurface;
    private EventHandler mEventHandler;
    private int mHeight;
    private int mWidth;
    private XmsTimeline xmsTimeline;

    private static native void nativeCancelExport(long j, long j2);

    private static native long nativeCreateContext();

    private static native long nativeCreateTimeline(long j);

    private static native void nativeExportTimeline(long j, long j2, String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    private static native void nativeExportTimelineCbr(long j, long j2, String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    private static native String nativeGetSdkVersion(long j);

    private static native long nativeGetTimelineCurrentPosition(long j, long j2);

    private static native void nativeInitContext(long j);

    private static native void nativePauseTimeline(long j, long j2);

    private static native void nativePauseToBackground(long j, long j2);

    private static native void nativePlayTimeline(long j, long j2);

    private static native void nativePlayTimelineByStart(long j, long j2, int i);

    private static native void nativePlayTimelinePrepare(long j, long j2, int i);

    private static native void nativeRelease(long j);

    private static native void nativeRemoveTimeline(long j, long j2);

    private static native void nativeResumeTimeline(long j, long j2);

    private static native void nativeResumeToForeground(long j, long j2);

    private static native void nativeSeekTimeline(long j, long j2, long j3, int i);

    private static native void nativeSkipPauseTimeline(long j, long j2);

    private XmsContext() {
        createContext();
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return this.mContext;
    }

    public XmsTimeline getXmsTimeline() {
        return this.xmsTimeline;
    }

    public static XmsContext getInstance() {
        XmsContext xmsContext;
        synchronized (mObj) {
            if (mInstance == null) {
                mInstance = new XmsContext();
            }
            xmsContext = mInstance;
        }
        return xmsContext;
    }

    private void createContext() {
        this.xmsTimeline = null;
        this.mNativePtr = nativeCreateContext();
    }

    public void initContext() {
        nativeInitContext(this.mNativePtr);
    }

    public XmsTimeline createTimeline() {
        XmsTimeline createTimeline = XmsTimeline.createTimeline();
        this.xmsTimeline = createTimeline;
        return createTimeline;
    }

    public void removeTimeline(XmsTimeline xmsTimeline) {
        if (xmsTimeline == null || xmsTimeline.isNULL()) {
            return;
        }
        xmsTimeline.clearMap();
        nativeRemoveTimeline(this.mNativePtr, xmsTimeline.mNativePtr);
        xmsTimeline.mNativePtr = 0L;
        xmsTimeline.releaseAction();
    }

    public void release() {
        synchronized (mObj) {
            nativeRelease(this.mNativePtr);
            mInstance = null;
        }
        this.xmsTimeline = null;
        MsgProxy.destory();
    }

    public long getTimelineCurrentPosition(XmsTimeline xmsTimeline) {
        if (xmsTimeline == null || xmsTimeline.isNULL()) {
            return -1L;
        }
        return nativeGetTimelineCurrentPosition(this.mNativePtr, xmsTimeline.mNativePtr);
    }

    public String getSdkVersion() {
        return nativeGetSdkVersion(this.mNativePtr);
    }

    public void playTimeline(XmsTimeline xmsTimeline) {
        if (xmsTimeline == null || xmsTimeline.isNULL()) {
            return;
        }
        nativePlayTimeline(this.mNativePtr, xmsTimeline.mNativePtr);
    }

    public void prepareTimeline(XmsTimeline xmsTimeline, int i) {
        if (xmsTimeline == null || xmsTimeline.isNULL()) {
            return;
        }
        nativePlayTimelinePrepare(this.mNativePtr, xmsTimeline.mNativePtr, i);
    }

    public void playTimeline(XmsTimeline xmsTimeline, int i) {
        if (xmsTimeline == null || xmsTimeline.isNULL()) {
            return;
        }
        nativePlayTimelineByStart(this.mNativePtr, xmsTimeline.mNativePtr, i);
    }

    public void exportTimeline(XmsTimeline xmsTimeline, String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (xmsTimeline == null || xmsTimeline.isNULL()) {
            return;
        }
        nativeExportTimeline(this.mNativePtr, xmsTimeline.mNativePtr, str, i, i2, i3, i4, i5, i6, i7, i8);
    }

    public void exportTimelineCbr(XmsTimeline xmsTimeline, String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (xmsTimeline == null || xmsTimeline.isNULL()) {
            return;
        }
        nativeExportTimelineCbr(this.mNativePtr, xmsTimeline.mNativePtr, str, i, i2, i3, i4, i5, i6, i7, i8);
    }

    public void cancelExport(XmsTimeline xmsTimeline) {
        if (xmsTimeline == null || xmsTimeline.isNULL()) {
            return;
        }
        nativeCancelExport(this.mNativePtr, xmsTimeline.mNativePtr);
    }

    public void pause(XmsTimeline xmsTimeline) {
        if (xmsTimeline == null || xmsTimeline.isNULL()) {
            return;
        }
        nativePauseTimeline(this.mNativePtr, xmsTimeline.mNativePtr);
    }

    public void skipPause(XmsTimeline xmsTimeline) {
        if (xmsTimeline == null || xmsTimeline.isNULL()) {
            return;
        }
        nativeSkipPauseTimeline(this.mNativePtr, xmsTimeline.mNativePtr);
    }

    public void resume(XmsTimeline xmsTimeline) {
        if (xmsTimeline == null || xmsTimeline.isNULL()) {
            return;
        }
        nativeResumeTimeline(this.mNativePtr, xmsTimeline.mNativePtr);
    }

    public void stop(XmsTimeline xmsTimeline) {
        if (xmsTimeline == null || xmsTimeline.isNULL()) {
            return;
        }
        xmsTimeline.stop();
    }

    public void seekTimeline(XmsTimeline xmsTimeline, long j, int i) {
        if (xmsTimeline == null || xmsTimeline.isNULL()) {
            return;
        }
        nativeSeekTimeline(this.mNativePtr, xmsTimeline.mNativePtr, j, i);
    }

    public void pauseToBackground(XmsTimeline xmsTimeline) {
        nativePauseToBackground(this.mNativePtr, xmsTimeline.mNativePtr);
    }

    public void resumeToForeground(XmsTimeline xmsTimeline) {
        nativeResumeToForeground(this.mNativePtr, xmsTimeline.mNativePtr);
    }

    public void initLister() {
        initEventHandler();
    }

    private void initEventHandler() {
        Looper myLooper = Looper.myLooper();
        if (myLooper != null) {
            this.mEventHandler = new EventHandler(myLooper);
        } else {
            Looper mainLooper = Looper.getMainLooper();
            if (mainLooper != null) {
                this.mEventHandler = new EventHandler(mainLooper);
            } else {
                this.mEventHandler = null;
            }
        }
        MsgProxy.registerMessageHandler(MsgType.XMSCONTEXT, this.mEventHandler);
    }

    public void unRegisterMessageHandler() {
        MsgProxy.unRegisterMessageHandler(MsgType.XMSCONTEXT, this.mEventHandler);
        MsgProxy.unRegisterMessageHandler(MsgType.XMSEXPORT, this.mEventHandler);
        MsgProxy.unRegisterMessageHandler(MsgType.XMSTRANSCODE, this.mEventHandler);
    }

    public void attachSurface(final XmsTimeline xmsTimeline, final XmsSurface xmsSurface, final int i) {
        if (xmsTimeline == null || xmsTimeline.isNULL()) {
            return;
        }
        if (xmsSurface != null && xmsSurface.isCreated()) {
            xmsTimeline.attachSurface(xmsSurface.getSurface());
            xmsTimeline.setProfile(xmsSurface.getWidth(), xmsSurface.getHeight(), i);
            initContext();
        }
        xmsSurface.getHolder().addCallback(new SurfaceHolder.Callback() { // from class: com.xiaomi.milab.videosdk.XmsContext.1
            @Override // android.view.SurfaceHolder.Callback
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i2, int i3, int i4) {
                XmsContext.this.mCurrentSurface = surfaceHolder.getSurface();
                xmsTimeline.attachSurface(surfaceHolder.getSurface());
                xmsTimeline.setProfile(xmsSurface.getWidth(), xmsSurface.getHeight(), i);
                XmsContext.this.initContext();
                xmsTimeline.surfaceChanged(xmsSurface.getSurface(), i3, i4);
            }
        });
    }

    public void attachTexture(final XmsTimeline xmsTimeline, final XmsTextureView xmsTextureView, final int i) {
        if (xmsTimeline == null || xmsTimeline.isNULL()) {
            return;
        }
        if (xmsTextureView != null && xmsTextureView.isCreated()) {
            xmsTimeline.attachSurface(xmsTextureView.getSurface());
            this.mCurrentSurface = xmsTextureView.getSurface();
            xmsTimeline.setProfile(xmsTextureView.getWidth(), xmsTextureView.getHeight(), i);
            this.mWidth = xmsTextureView.getWidth();
            this.mHeight = xmsTextureView.getHeight();
            initContext();
            this.bHasInit = true;
        }
        xmsTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() { // from class: com.xiaomi.milab.videosdk.XmsContext.2
            @Override // android.view.TextureView.SurfaceTextureListener
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i2, int i3) {
                if (XmsContext.this.bHasInit) {
                    Surface surface = new Surface(surfaceTexture);
                    XmsContext.this.mCurrentSurface = surface;
                    xmsTextureView.setSurface(surface);
                    xmsTimeline.surfaceChanged(surface, i2, i3);
                    XmsContext.this.mWidth = i2;
                    XmsContext.this.mHeight = i3;
                    return;
                }
                Surface surface2 = new Surface(surfaceTexture);
                XmsContext.this.mCurrentSurface = surface2;
                xmsTextureView.setSurface(surface2);
                xmsTimeline.attachSurface(surface2);
                XmsContext.this.mWidth = i2;
                XmsContext.this.mHeight = i3;
                xmsTimeline.setProfile(i2, i3, i);
                XmsContext.this.initContext();
                XmsContext.this.bHasInit = true;
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i2, int i3) {
                Surface surface = new Surface(surfaceTexture);
                XmsContext.this.mCurrentSurface = surface;
                xmsTextureView.setSurface(surface);
                xmsTimeline.surfaceChanged(xmsTextureView.getSurface(), i2, i3);
                XmsContext.this.mWidth = i2;
                XmsContext.this.mHeight = i3;
            }
        });
    }

    public long creatTimeline() {
        return nativeCreateTimeline(this.mNativePtr);
    }

    public void flushTimeline(XmsTimeline xmsTimeline) {
        if (this.mCurrentSurface != null) {
            xmsTimeline.updateRender();
        }
    }

    public void setPlayCallback(PlayCallback playCallback) {
        EventHandler eventHandler = this.mEventHandler;
        if (eventHandler == null) {
            return;
        }
        eventHandler.setPlayCallback(playCallback);
    }

    public void setTimelineCallback(TimelineCallback timelineCallback) {
        EventHandler eventHandler = this.mEventHandler;
        if (eventHandler == null) {
            return;
        }
        MsgProxy.registerMessageHandler(MsgType.XMSTIMELINE, eventHandler);
        this.mEventHandler.setTimelineCallback(timelineCallback);
    }

    public void setExportCallback(ExportCallback exportCallback) {
        EventHandler eventHandler = this.mEventHandler;
        if (eventHandler == null) {
            return;
        }
        MsgProxy.registerMessageHandler(MsgType.XMSEXPORT, eventHandler);
        this.mEventHandler.setExportCallback(exportCallback);
    }

    public void setAudioExtractCallback(AudioExtractCallback audioExtractCallback) {
        EventHandler eventHandler = this.mEventHandler;
        if (eventHandler == null) {
            return;
        }
        MsgProxy.registerMessageHandler(MsgType.XMSAUDIOEXTRACT, eventHandler);
        this.mEventHandler.setAudioExtractCallback(audioExtractCallback);
    }
}
