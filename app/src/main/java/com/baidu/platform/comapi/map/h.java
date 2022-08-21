package com.baidu.platform.comapi.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLDebugHelper;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.nio.IntBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class h extends TextureView implements TextureView.SurfaceTextureListener {
    public static final int DEBUG_CHECK_GL_ERROR = 1;
    public static final int DEBUG_LOG_GL_CALLS = 2;
    public static final int RENDERMODE_CONTINUOUSLY = 1;
    public static final int RENDERMODE_WHEN_DIRTY = 0;
    private static final g c = new g(null);
    private int a;
    private final View.OnLayoutChangeListener b;
    private final WeakReference<h> d;
    private f e;
    private ap f;
    private boolean g;
    private GLSurfaceView.EGLConfigChooser h;
    private GLSurfaceView.EGLContextFactory i;
    private GLSurfaceView.EGLWindowSurfaceFactory j;
    private GLSurfaceView.GLWrapper k;
    private int l;
    private int m;
    private boolean n;

    /* loaded from: classes.dex */
    public abstract class a implements GLSurfaceView.EGLConfigChooser {
        public int[] a;

        public a(int[] iArr) {
            this.a = a(iArr);
        }

        private int[] a(int[] iArr) {
            if (h.this.m != 2) {
                return iArr;
            }
            int length = iArr.length;
            int[] iArr2 = new int[length + 2];
            int i = length - 1;
            System.arraycopy(iArr, 0, iArr2, 0, i);
            iArr2[i] = 12352;
            iArr2[length] = 4;
            iArr2[length + 1] = 12344;
            return iArr2;
        }

        public abstract EGLConfig a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr);

        @Override // android.opengl.GLSurfaceView.EGLConfigChooser
        public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay) {
            int[] iArr = new int[1];
            if (egl10.eglChooseConfig(eGLDisplay, this.a, null, 0, iArr)) {
                int i = iArr[0];
                if (i <= 0) {
                    throw new IllegalArgumentException("No configs match configSpec");
                }
                EGLConfig[] eGLConfigArr = new EGLConfig[i];
                if (!egl10.eglChooseConfig(eGLDisplay, this.a, eGLConfigArr, i, iArr)) {
                    throw new IllegalArgumentException("eglChooseConfig#2 failed");
                }
                EGLConfig a = a(egl10, eGLDisplay, eGLConfigArr);
                if (a == null) {
                    throw new IllegalArgumentException("No config chosen");
                }
                return a;
            }
            throw new IllegalArgumentException("eglChooseConfig failed");
        }
    }

    /* loaded from: classes.dex */
    public class b extends a {
        public int c;
        public int d;
        public int e;
        public int f;
        public int g;
        public int h;
        private int[] j;

        public b(int i, int i2, int i3, int i4, int i5, int i6) {
            super(new int[]{12324, i, 12323, i2, 12322, i3, 12321, i4, 12325, i5, 12326, i6, 12344});
            this.j = new int[1];
            this.c = i;
            this.d = i2;
            this.e = i3;
            this.f = i4;
            this.g = i5;
            this.h = i6;
        }

        private int a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int i2) {
            return egl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, i, this.j) ? this.j[0] : i2;
        }

        @Override // com.baidu.platform.comapi.map.h.a
        public EGLConfig a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr) {
            for (EGLConfig eGLConfig : eGLConfigArr) {
                int a = a(egl10, eGLDisplay, eGLConfig, 12325, 0);
                int a2 = a(egl10, eGLDisplay, eGLConfig, 12326, 0);
                if (a >= this.g && a2 >= this.h) {
                    int a3 = a(egl10, eGLDisplay, eGLConfig, 12324, 0);
                    int a4 = a(egl10, eGLDisplay, eGLConfig, 12323, 0);
                    int a5 = a(egl10, eGLDisplay, eGLConfig, 12322, 0);
                    int a6 = a(egl10, eGLDisplay, eGLConfig, 12321, 0);
                    if (a3 == this.c && a4 == this.d && a5 == this.e && a6 == this.f) {
                        return eGLConfig;
                    }
                }
            }
            return null;
        }
    }

    /* loaded from: classes.dex */
    public class c implements GLSurfaceView.EGLContextFactory {
        private int b;

        private c() {
            this.b = 12440;
        }

        public /* synthetic */ c(h hVar, com.baidu.platform.comapi.map.i iVar) {
            this();
        }

        @Override // android.opengl.GLSurfaceView.EGLContextFactory
        public EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
            int[] iArr = {this.b, h.this.m, 12344};
            EGLContext eGLContext = EGL10.EGL_NO_CONTEXT;
            if (h.this.m == 0) {
                iArr = null;
            }
            return egl10.eglCreateContext(eGLDisplay, eGLConfig, eGLContext, iArr);
        }

        @Override // android.opengl.GLSurfaceView.EGLContextFactory
        public void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext) {
            if (!egl10.eglDestroyContext(eGLDisplay, eGLContext)) {
                Log.i("DefaultContextFactory", "tid=" + Thread.currentThread().getId());
                e.a("eglDestroyContex", egl10.eglGetError());
            }
        }
    }

    /* loaded from: classes.dex */
    public static class d implements GLSurfaceView.EGLWindowSurfaceFactory {
        private d() {
        }

        public /* synthetic */ d(com.baidu.platform.comapi.map.i iVar) {
            this();
        }

        @Override // android.opengl.GLSurfaceView.EGLWindowSurfaceFactory
        public EGLSurface createWindowSurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj) {
            try {
                return egl10.eglCreateWindowSurface(eGLDisplay, eGLConfig, obj, null);
            } catch (IllegalArgumentException e) {
                Log.e("GLTextureView", "eglCreateWindowSurface", e);
                return null;
            }
        }

        @Override // android.opengl.GLSurfaceView.EGLWindowSurfaceFactory
        public void destroySurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface) {
            egl10.eglDestroySurface(eGLDisplay, eGLSurface);
        }
    }

    /* loaded from: classes.dex */
    public static class e {
        public EGL10 a;
        public EGLDisplay b;
        public EGLSurface c;
        public EGLConfig d;
        public EGLContext e;
        private WeakReference<h> f;

        public e(WeakReference<h> weakReference) {
            this.f = weakReference;
        }

        private void a(String str) {
            a(str, this.a.eglGetError());
        }

        public static void a(String str, int i) {
            String b = b(str, i);
            Log.e("EglHelper", "throwEglException tid=" + Thread.currentThread().getId() + " " + b);
            throw new RuntimeException(b);
        }

        public static void a(String str, String str2, int i) {
        }

        public static String b(String str, int i) {
            return str + " EGL failed code: " + i;
        }

        private void g() {
            EGLSurface eGLSurface;
            EGLSurface eGLSurface2 = this.c;
            if (eGLSurface2 == null || eGLSurface2 == (eGLSurface = EGL10.EGL_NO_SURFACE)) {
                return;
            }
            this.a.eglMakeCurrent(this.b, eGLSurface, eGLSurface, EGL10.EGL_NO_CONTEXT);
            h hVar = this.f.get();
            if (hVar != null) {
                hVar.j.destroySurface(this.a, this.b, this.c);
            }
            this.c = null;
        }

        public void a() {
            Log.w("EglHelper", "start() tid=" + Thread.currentThread().getId());
            EGL10 egl10 = (EGL10) EGLContext.getEGL();
            this.a = egl10;
            EGLDisplay eglGetDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            this.b = eglGetDisplay;
            if (eglGetDisplay != EGL10.EGL_NO_DISPLAY) {
                if (!this.a.eglInitialize(eglGetDisplay, new int[2])) {
                    throw new RuntimeException("eglInitialize failed");
                }
                h hVar = this.f.get();
                if (hVar == null) {
                    this.d = null;
                    this.e = null;
                } else {
                    this.d = hVar.h.chooseConfig(this.a, this.b);
                    this.e = hVar.i.createContext(this.a, this.b, this.d);
                }
                EGLContext eGLContext = this.e;
                if (eGLContext == null || eGLContext == EGL10.EGL_NO_CONTEXT) {
                    this.e = null;
                    a("createContext");
                }
                Log.w("EglHelper", "createContext " + this.e + " tid=" + Thread.currentThread().getId());
                this.c = null;
                return;
            }
            throw new RuntimeException("eglGetDisplay failed");
        }

        public boolean b() {
            Log.w("EglHelper", "createSurface()  tid=" + Thread.currentThread().getId());
            if (this.a != null) {
                if (this.b == null) {
                    throw new RuntimeException("eglDisplay not initialized");
                }
                if (this.d == null) {
                    throw new RuntimeException("mEglConfig not initialized");
                }
                g();
                h hVar = this.f.get();
                this.c = hVar != null ? hVar.j.createWindowSurface(this.a, this.b, this.d, hVar.getSurfaceTexture()) : null;
                EGLSurface eGLSurface = this.c;
                if (eGLSurface == null || eGLSurface == EGL10.EGL_NO_SURFACE) {
                    if (this.a.eglGetError() == 12299) {
                        Log.e("EglHelper", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
                    }
                    return false;
                } else if (this.a.eglMakeCurrent(this.b, eGLSurface, eGLSurface, this.e)) {
                    return true;
                } else {
                    a("EGLHelper", "eglMakeCurrent", this.a.eglGetError());
                    return false;
                }
            }
            throw new RuntimeException("egl not initialized");
        }

        public GL c() {
            GL gl = this.e.getGL();
            h hVar = this.f.get();
            if (hVar != null) {
                if (hVar.k != null) {
                    gl = hVar.k.wrap(gl);
                }
                if ((hVar.l & 3) == 0) {
                    return gl;
                }
                int i = 0;
                C0023h c0023h = null;
                if ((hVar.l & 1) != 0) {
                    i = 1;
                }
                if ((hVar.l & 2) != 0) {
                    c0023h = new C0023h();
                }
                return GLDebugHelper.wrap(gl, i, c0023h);
            }
            return gl;
        }

        public int d() {
            if (!this.a.eglSwapBuffers(this.b, this.c)) {
                return this.a.eglGetError();
            }
            return 12288;
        }

        public void e() {
            Log.w("EglHelper", "destroySurface()  tid=" + Thread.currentThread().getId());
            g();
        }

        public void f() {
            Log.w("EglHelper", "finish() tid=" + Thread.currentThread().getId());
            if (this.e != null) {
                h hVar = this.f.get();
                if (hVar != null) {
                    hVar.i.destroyContext(this.a, this.b, this.e);
                }
                this.e = null;
            }
            EGLDisplay eGLDisplay = this.b;
            if (eGLDisplay != null) {
                this.a.eglTerminate(eGLDisplay);
                this.b = null;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class f extends Thread {
        private boolean a;
        private boolean b;
        private boolean c;
        private boolean d;
        private boolean e;
        private boolean f;
        private boolean g;
        private boolean h;
        private boolean i;
        private boolean j;
        private boolean o;
        private e r;
        private WeakReference<h> s;
        private ArrayList<Runnable> p = new ArrayList<>();
        private boolean q = true;
        private int k = 0;
        private int l = 0;
        private boolean n = true;
        private int m = 1;

        public f(WeakReference<h> weakReference) {
            this.s = weakReference;
        }

        private void j() {
            if (this.i) {
                this.i = false;
                this.r.e();
            }
        }

        private void k() {
            if (this.h) {
                this.r.f();
                this.h = false;
                h.c.c(this);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:144:0x036e  */
        /* JADX WARN: Removed duplicated region for block: B:160:0x039f  */
        /* JADX WARN: Removed duplicated region for block: B:186:0x021f A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private void l() throws java.lang.InterruptedException {
            /*
                Method dump skipped, instructions count: 1099
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.map.h.f.l():void");
        }

        private boolean m() {
            return !this.d && this.e && !this.f && this.k > 0 && this.l > 0 && (this.n || this.m == 1);
        }

        public void a(int i) {
            if (i < 0 || i > 1) {
                throw new IllegalArgumentException("renderMode");
            }
            synchronized (h.c) {
                this.m = i;
                h.c.notifyAll();
            }
        }

        public void a(int i, int i2) {
            synchronized (h.c) {
                this.k = i;
                this.l = i2;
                this.q = true;
                this.n = true;
                this.o = false;
                h.c.notifyAll();
                while (!this.b && !this.d && !this.o && a()) {
                    Log.i("Main thread", "onWindowResize waiting for render complete from tid=" + getId());
                    try {
                        h.c.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void a(Runnable runnable) {
            if (runnable != null) {
                synchronized (h.c) {
                    this.p.add(runnable);
                    h.c.notifyAll();
                }
                return;
            }
            throw new IllegalArgumentException("r must not be null");
        }

        public boolean a() {
            return this.h && this.i && m();
        }

        public int b() {
            int i;
            synchronized (h.c) {
                i = this.m;
            }
            return i;
        }

        public void c() {
            synchronized (h.c) {
                this.n = true;
                h.c.notifyAll();
            }
        }

        public void d() {
            synchronized (h.c) {
                Log.i("GLThread", "surfaceCreated tid=" + getId());
                this.e = true;
                h.c.notifyAll();
                while (this.g && !this.b) {
                    try {
                        h.c.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void e() {
            synchronized (h.c) {
                Log.i("GLThread", "surfaceDestroyed tid=" + getId());
                this.e = false;
                h.c.notifyAll();
                while (!this.g && !this.b) {
                    try {
                        h.c.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void f() {
            synchronized (h.c) {
                Log.i("GLThread", "onPause tid=" + getId());
                this.c = true;
                h.c.notifyAll();
                while (!this.b && !this.d) {
                    Log.i("Main thread", "onPause waiting for mPaused.");
                    try {
                        h.c.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void g() {
            synchronized (h.c) {
                Log.i("GLThread", "onResume tid=" + getId());
                this.c = false;
                this.n = true;
                this.o = false;
                h.c.notifyAll();
                while (!this.b && this.d && !this.o) {
                    Log.i("Main thread", "onResume waiting for !mPaused.");
                    try {
                        h.c.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void h() {
            synchronized (h.c) {
                this.a = true;
                h.c.notifyAll();
                while (!this.b) {
                    try {
                        h.c.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void i() {
            this.j = true;
            h.c.notifyAll();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            setName("GLThread " + getId());
            Log.i("GLThread", "starting tid=" + getId());
            try {
                l();
            } catch (InterruptedException unused) {
            } catch (Throwable th) {
                h.c.a(this);
                throw th;
            }
            h.c.a(this);
        }
    }

    /* loaded from: classes.dex */
    public static class g {
        private static String a = "GLThreadManager";
        private static final Class b;
        private static final Method c;
        private boolean d;
        private int e;
        private boolean f;
        private boolean g;
        private boolean h;
        private f i;

        static {
            try {
                Class<?> cls = Class.forName("android.os.SystemProperties");
                b = cls;
                Method declaredMethod = cls.getDeclaredMethod("getInt", String.class, Integer.TYPE);
                c = declaredMethod;
                declaredMethod.setAccessible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private g() {
        }

        public /* synthetic */ g(com.baidu.platform.comapi.map.i iVar) {
            this();
        }

        private void c() {
            if (!this.d) {
                try {
                    this.e = ((Integer) c.invoke(null, "ro.opengles.version", 0)).intValue();
                } catch (Exception unused) {
                    this.e = 65536;
                }
                if (this.e >= 131072) {
                    this.g = true;
                }
                String str = a;
                Log.w(str, "checkGLESVersion mGLESVersion = " + this.e + " mMultipleGLESContextsAllowed = " + this.g);
                this.d = true;
            }
        }

        public synchronized void a(f fVar) {
            Log.i("GLThread", "exiting tid=" + fVar.getId());
            fVar.b = true;
            if (this.i == fVar) {
                this.i = null;
            }
            notifyAll();
        }

        public synchronized void a(GL10 gl10) {
            if (!this.f) {
                c();
                String glGetString = gl10.glGetString(7937);
                boolean z = false;
                if (this.e < 131072) {
                    this.g = !glGetString.startsWith("Q3Dimension MSM7500 ");
                    notifyAll();
                }
                if (!this.g) {
                    z = true;
                }
                this.h = z;
                Log.w(a, "checkGLDriver renderer = \"" + glGetString + "\" multipleContextsAllowed = " + this.g + " mLimitedGLESContexts = " + this.h);
                this.f = true;
            }
        }

        public synchronized boolean a() {
            return this.h;
        }

        public synchronized boolean b() {
            c();
            return !this.g;
        }

        public boolean b(f fVar) {
            f fVar2 = this.i;
            if (fVar2 == fVar || fVar2 == null) {
                this.i = fVar;
                notifyAll();
                return true;
            }
            c();
            if (this.g) {
                return true;
            }
            f fVar3 = this.i;
            if (fVar3 == null) {
                return false;
            }
            fVar3.i();
            return false;
        }

        public void c(f fVar) {
            if (this.i == fVar) {
                this.i = null;
            }
            notifyAll();
        }
    }

    /* renamed from: com.baidu.platform.comapi.map.h$h  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static class C0023h extends Writer {
        private StringBuilder a = new StringBuilder();

        private void a() {
            if (this.a.length() > 0) {
                StringBuilder sb = this.a;
                sb.delete(0, sb.length());
            }
        }

        @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            a();
        }

        @Override // java.io.Writer, java.io.Flushable
        public void flush() {
            a();
        }

        @Override // java.io.Writer
        public void write(char[] cArr, int i, int i2) {
            for (int i3 = 0; i3 < i2; i3++) {
                char c = cArr[i + i3];
                if (c == '\n') {
                    a();
                } else {
                    this.a.append(c);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class i extends b {
        public i(boolean z) {
            super(8, 8, 8, 0, z ? 16 : 0, 0);
        }
    }

    public h(Context context) {
        super(context);
        this.a = 60;
        this.b = new com.baidu.platform.comapi.map.i(this);
        this.d = new WeakReference<>(this);
        b();
    }

    public h(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.a = 60;
        this.b = new com.baidu.platform.comapi.map.i(this);
        this.d = new WeakReference<>(this);
        b();
    }

    public h(Context context, AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
        this.a = 60;
        this.b = new com.baidu.platform.comapi.map.i(this);
        this.d = new WeakReference<>(this);
        b();
    }

    private Bitmap a(int i2, int i3, int i4, int i5, GL10 gl10, Bitmap.Config config) {
        int i6 = i4 * i5;
        int[] iArr = new int[i6];
        int[] iArr2 = new int[i6];
        IntBuffer wrap = IntBuffer.wrap(iArr);
        wrap.position(0);
        try {
            gl10.glReadPixels(i2, i3, i4, i5, 6408, 5121, wrap);
            for (int i7 = 0; i7 < i5; i7++) {
                int i8 = i7 * i4;
                int i9 = ((i5 - i7) - 1) * i4;
                for (int i10 = 0; i10 < i4; i10++) {
                    int i11 = iArr[i8 + i10];
                    iArr2[i9 + i10] = (i11 & (-16711936)) | ((i11 << 16) & 16711680) | ((i11 >> 16) & 255);
                }
            }
            return config == null ? Bitmap.createBitmap(iArr2, i4, i5, Bitmap.Config.ARGB_8888) : Bitmap.createBitmap(iArr2, i4, i5, config);
        } catch (GLException unused) {
            return null;
        }
    }

    private void b() {
        setSurfaceTextureListener(this);
        addOnLayoutChangeListener(this.b);
    }

    private void c() {
        if (this.e == null) {
            return;
        }
        throw new IllegalStateException("setRenderer has already been called for this instance.");
    }

    public Bitmap captureImageFromSurface(int i2, int i3, int i4, int i5, Object obj, Bitmap.Config config) {
        return a(i2, i3, i4, i5, (GL10) obj, config);
    }

    public void finalize() throws Throwable {
        try {
            f fVar = this.e;
            if (fVar != null) {
                fVar.h();
            }
        } finally {
            super.finalize();
        }
    }

    public int getDebugFlags() {
        return this.l;
    }

    public int getFPS() {
        return this.a;
    }

    public boolean getPreserveEGLContextOnPause() {
        return this.n;
    }

    public int getRenderMode() {
        return this.e.b();
    }

    @Override // android.view.TextureView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d("GLTextureView", "onAttachedToWindow reattach =" + this.g);
        if (this.g && this.f != null) {
            f fVar = this.e;
            int b2 = fVar != null ? fVar.b() : 1;
            f fVar2 = new f(this.d);
            this.e = fVar2;
            if (b2 != 1) {
                fVar2.a(b2);
            }
            this.e.start();
        }
        this.g = false;
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        Log.d("GLTextureView", "onDetachedFromWindow");
        f fVar = this.e;
        if (fVar != null) {
            fVar.h();
        }
        this.g = true;
        super.onDetachedFromWindow();
    }

    public void onPause() {
        f fVar = this.e;
        if (fVar != null) {
            fVar.f();
        }
    }

    public void onResume() {
        f fVar = this.e;
        if (fVar != null) {
            fVar.g();
        }
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i2, int i3) {
        surfaceCreated(surfaceTexture);
        surfaceChanged(surfaceTexture, 0, i2, i3);
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        surfaceDestroyed(surfaceTexture);
        return true;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i2, int i3) {
        surfaceChanged(surfaceTexture, 0, i2, i3);
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public void queueEvent(Runnable runnable) {
        f fVar = this.e;
        if (fVar != null) {
            fVar.a(runnable);
        }
    }

    public void requestRender() {
        f fVar = this.e;
        if (fVar != null) {
            fVar.c();
        }
    }

    public void setDebugFlags(int i2) {
        this.l = i2;
    }

    public void setEGLConfigChooser(int i2, int i3, int i4, int i5, int i6, int i7) {
        setEGLConfigChooser(new b(i2, i3, i4, i5, i6, i7));
    }

    public void setEGLConfigChooser(GLSurfaceView.EGLConfigChooser eGLConfigChooser) {
        c();
        this.h = eGLConfigChooser;
    }

    public void setEGLConfigChooser(boolean z) {
        setEGLConfigChooser(new i(z));
    }

    public void setEGLContextClientVersion(int i2) {
        c();
        this.m = i2;
    }

    public void setEGLContextFactory(GLSurfaceView.EGLContextFactory eGLContextFactory) {
        c();
        this.i = eGLContextFactory;
    }

    public void setEGLWindowSurfaceFactory(GLSurfaceView.EGLWindowSurfaceFactory eGLWindowSurfaceFactory) {
        c();
        this.j = eGLWindowSurfaceFactory;
    }

    public void setFPS(int i2) {
        this.a = i2;
    }

    public void setGLWrapper(GLSurfaceView.GLWrapper gLWrapper) {
        this.k = gLWrapper;
    }

    public void setPreserveEGLContextOnPause(boolean z) {
        this.n = z;
    }

    public void setRenderMode(int i2) {
        this.e.a(i2);
    }

    public void setRenderer(ap apVar) {
        c();
        if (this.h == null) {
            this.h = new i(true);
        }
        if (this.i == null) {
            this.i = new c(this, null);
        }
        if (this.j == null) {
            this.j = new d(null);
        }
        this.f = apVar;
        f fVar = new f(this.d);
        this.e = fVar;
        fVar.start();
    }

    public void surfaceChanged(SurfaceTexture surfaceTexture, int i2, int i3, int i4) {
        f fVar = this.e;
        if (fVar != null) {
            fVar.a(i3, i4);
        }
    }

    public void surfaceCreated(SurfaceTexture surfaceTexture) {
        f fVar = this.e;
        if (fVar != null) {
            fVar.d();
        }
    }

    public void surfaceDestroyed(SurfaceTexture surfaceTexture) {
        f fVar = this.e;
        if (fVar != null) {
            fVar.e();
        }
    }
}
