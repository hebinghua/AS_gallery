package com.nexstreaming.app.common.nexasset.preview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemCategory;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemType;
import com.nexstreaming.app.common.nexasset.assetpackage.c;
import com.nexstreaming.app.common.nexasset.assetpackage.f;
import com.nexstreaming.kminternal.kinemaster.config.NexEditorDeviceProfile;
import com.nexstreaming.kminternal.nexvideoeditor.NexImageLoader;
import com.nexstreaming.kminternal.nexvideoeditor.NexThemeRenderer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

/* compiled from: AssetPreviewView.java */
/* loaded from: classes3.dex */
public class a extends GLSurfaceView {
    private static final long FRAME_RATE = 30;
    private static final long FRAME_TIME = 33;
    private static String TAG = "NexThemePreviewView";
    private float mAspectRatio;
    private float mColor;
    private int m_absTime;
    private c m_assetPackageManager;
    public boolean m_done;
    private f m_effect;
    private String m_effectOptions;
    private long m_effectTime;
    private long m_frameEnd;
    private long m_frameStart;
    private Handler m_handler;
    private boolean m_isClipEffect;
    private boolean m_isRenderItem;
    private NexThemeRenderer m_nexThemeRenderer;
    private NexImageLoader.d m_overlayPathResolver;
    private long m_pauseTime;
    private Object m_renderLock;
    private f m_setEffect;
    private String m_setEffectOptions;
    private long m_setEffectTime;
    private boolean m_showOnRender;
    private long m_startTime;
    private boolean m_swapPlaceholders;

    public void deinitRenderer() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long miliTime() {
        return System.nanoTime() / 1000000;
    }

    public a(Context context) {
        super(context);
        this.mAspectRatio = 1.7777778f;
        this.m_showOnRender = false;
        this.m_handler = new Handler();
        this.m_swapPlaceholders = false;
        this.m_effectOptions = null;
        this.m_setEffectOptions = null;
        this.m_done = false;
        this.m_renderLock = new Object();
        this.m_effectTime = 2000L;
        this.m_setEffectTime = -1L;
        this.m_pauseTime = 1000L;
        this.m_absTime = 0;
        this.m_nexThemeRenderer = null;
        this.m_overlayPathResolver = null;
        this.mColor = 0.0f;
        init(false, 0, 1);
    }

    public a(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAspectRatio = 1.7777778f;
        this.m_showOnRender = false;
        this.m_handler = new Handler();
        this.m_swapPlaceholders = false;
        this.m_effectOptions = null;
        this.m_setEffectOptions = null;
        this.m_done = false;
        this.m_renderLock = new Object();
        this.m_effectTime = 2000L;
        this.m_setEffectTime = -1L;
        this.m_pauseTime = 1000L;
        this.m_absTime = 0;
        this.m_nexThemeRenderer = null;
        this.m_overlayPathResolver = null;
        this.mColor = 0.0f;
        init(false, 0, 1);
    }

    public a(Context context, boolean z, int i, int i2) {
        super(context);
        this.mAspectRatio = 1.7777778f;
        this.m_showOnRender = false;
        this.m_handler = new Handler();
        this.m_swapPlaceholders = false;
        this.m_effectOptions = null;
        this.m_setEffectOptions = null;
        this.m_done = false;
        this.m_renderLock = new Object();
        this.m_effectTime = 2000L;
        this.m_setEffectTime = -1L;
        this.m_pauseTime = 1000L;
        this.m_absTime = 0;
        this.m_nexThemeRenderer = null;
        this.m_overlayPathResolver = null;
        this.mColor = 0.0f;
        init(z, i, 1);
    }

    public void setAspectRatio(float f) {
        this.mAspectRatio = f;
    }

    public float getAspectRatio() {
        return this.mAspectRatio;
    }

    public void setOverlayPathResolver(NexImageLoader.d dVar) {
        this.m_overlayPathResolver = dVar;
    }

    public void setEffectOptions(String str) {
        String str2;
        if (str == null || (str2 = this.m_effectOptions) == null || !str.equals(str2)) {
            this.m_effectOptions = str;
        }
    }

    public String getEffectOptions() {
        return this.m_effectOptions;
    }

    public void setEffect(String str) {
        setEffect(c.a(getContext()).c(str));
    }

    public void setEffect(f fVar) {
        if (fVar == this.m_effect) {
            return;
        }
        this.m_startTime = miliTime();
        boolean z = true;
        this.m_isClipEffect = fVar.getCategory() == ItemCategory.effect;
        if (fVar.getType() != ItemType.renderitem) {
            z = false;
        }
        this.m_isRenderItem = z;
        this.m_effect = fVar;
    }

    public void setEffectTime(int i) {
        long j = i;
        if (j == this.m_effectTime) {
            return;
        }
        this.m_startTime = miliTime();
        this.m_effectTime = j;
    }

    public void setPauseTime(int i) {
        this.m_pauseTime = i;
    }

    public int getPauseTime() {
        return (int) this.m_pauseTime;
    }

    public void stepForward(int i) {
        int i2 = this.m_absTime + i;
        this.m_absTime = i2;
        if (i2 < 0) {
            this.m_absTime = 0;
        }
        if (this.m_absTime > 60) {
            this.m_absTime = 60;
        }
        this.m_effectTime = 0L;
    }

    public void stepBackward(int i) {
        int i2 = this.m_absTime - i;
        this.m_absTime = i2;
        if (i2 < 0) {
            this.m_absTime = 0;
        }
        if (this.m_absTime > 60) {
            this.m_absTime = 60;
        }
        this.m_effectTime = 0L;
    }

    public void showOnRender() {
        this.m_showOnRender = true;
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceView, android.view.View
    public void onDetachedFromWindow() {
        destroyRenderer();
        super.onDetachedFromWindow();
    }

    private void destroyRenderer() {
        synchronized (this.m_renderLock) {
            NexThemeRenderer nexThemeRenderer = this.m_nexThemeRenderer;
            if (nexThemeRenderer != null) {
                nexThemeRenderer.deinit(true);
                this.m_nexThemeRenderer = null;
            }
        }
    }

    private void makeRenderer() {
        if (this.m_nexThemeRenderer != null || this.m_done) {
            return;
        }
        this.m_nexThemeRenderer = new NexThemeRenderer();
        this.m_nexThemeRenderer.init(new NexImageLoader(getContext().getResources(), c.a(getContext()).e(), this.m_overlayPathResolver, 1440, 810, 1500000));
        if (this.m_swapPlaceholders && !this.m_isClipEffect) {
            this.m_nexThemeRenderer.setPlaceholders("placeholder2.jpg", "placeholder1.jpg");
        } else {
            this.m_nexThemeRenderer.setPlaceholders("placeholder1.jpg", "placeholder2.jpg");
        }
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceView, android.view.View
    public void onAttachedToWindow() {
        makeRenderer();
        super.onAttachedToWindow();
    }

    @Override // android.view.SurfaceView, android.view.View
    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        float f = this.mAspectRatio;
        if (f > 0.0f) {
            float f2 = size2;
            float f3 = size;
            if (f2 * f > f3) {
                size2 = (int) (f3 / f);
            } else {
                size = (int) (f2 * f);
            }
        }
        setMeasuredDimension(size, size2);
    }

    private void init(boolean z, int i, int i2) {
        this.m_assetPackageManager = c.a(getContext());
        setEGLContextClientVersion(2);
        setZOrderOnTop(true);
        getHolder().setFormat(1);
        final NexEditorDeviceProfile deviceProfile = NexEditorDeviceProfile.getDeviceProfile();
        setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() { // from class: com.nexstreaming.app.common.nexasset.preview.a.1
            @Override // android.opengl.GLSurfaceView.EGLConfigChooser
            public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay) {
                int[] iArr;
                int[] iArr2 = new int[19];
                iArr2[0] = 12352;
                iArr2[1] = 4;
                char c = 2;
                iArr2[2] = 12324;
                iArr2[3] = 8;
                iArr2[4] = 12323;
                iArr2[5] = 8;
                iArr2[6] = 12322;
                iArr2[7] = 8;
                iArr2[8] = 12321;
                iArr2[9] = 8;
                iArr2[10] = 12326;
                iArr2[11] = 1;
                iArr2[12] = 12338;
                iArr2[13] = deviceProfile.getGLMultisample() ? 1 : 0;
                iArr2[14] = 12337;
                if (!deviceProfile.getGLMultisample()) {
                    c = 0;
                }
                iArr2[15] = c;
                iArr2[16] = 12325;
                iArr2[17] = deviceProfile.getGLDepthBufferBits();
                iArr2[18] = 12344;
                int[] iArr3 = {12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12326, 8, 12344};
                int[] iArr4 = new int[1];
                egl10.eglChooseConfig(eGLDisplay, iArr2, null, 0, iArr4);
                if (iArr4[0] < 1) {
                    iArr = iArr4;
                    iArr2 = iArr3;
                    egl10.eglChooseConfig(eGLDisplay, iArr3, null, 0, iArr);
                } else {
                    iArr = iArr4;
                }
                int[] iArr5 = iArr2;
                int i3 = iArr[0];
                EGLConfig[] eGLConfigArr = new EGLConfig[i3];
                egl10.eglChooseConfig(eGLDisplay, iArr5, eGLConfigArr, i3, iArr);
                return eGLConfigArr[0];
            }
        });
        C0100a c0100a = new C0100a();
        setRenderer(c0100a);
        setRenderMode(1);
        String str = TAG;
        Log.d(str, "GL View Created " + c0100a);
        this.m_startTime = miliTime();
    }

    public void suspendRendering() {
        setRenderMode(0);
    }

    public void resumeRendering() {
        setRenderMode(1);
    }

    public int getRenderingMode() {
        return getRenderMode();
    }

    /* compiled from: AssetPreviewView.java */
    /* renamed from: com.nexstreaming.app.common.nexasset.preview.a$a  reason: collision with other inner class name */
    /* loaded from: classes3.dex */
    public class C0100a implements GLSurfaceView.Renderer {
        private C0100a() {
        }

        /* JADX WARN: Removed duplicated region for block: B:78:0x02a8  */
        /* JADX WARN: Removed duplicated region for block: B:79:0x02a9  */
        @Override // android.opengl.GLSurfaceView.Renderer
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void onDrawFrame(javax.microedition.khronos.opengles.GL10 r20) {
            /*
                Method dump skipped, instructions count: 763
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.app.common.nexasset.preview.a.C0100a.onDrawFrame(javax.microedition.khronos.opengles.GL10):void");
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onSurfaceChanged(GL10 gl10, int i, int i2) {
            Log.d(a.TAG, "onSurfaceChanged");
            gl10.glViewport(0, 0, i, i2);
            if (a.this.m_nexThemeRenderer != null) {
                a.this.m_nexThemeRenderer.surfaceChange(i, i2);
            }
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
            Log.d(a.TAG, "onSurfaceCreated");
        }
    }
}
