package com.baidu.platform.comapi.map;

import android.graphics.Bitmap;
import android.opengl.GLDebugHelper;
import android.opengl.GLException;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.baidu.platform.comapi.map.ah;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.nio.IntBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class f implements ag {
    private static final j c = new j();
    private WeakReference<SurfaceView> a;
    private int b = 60;
    private final WeakReference<f> d = new WeakReference<>(this);
    private i e;
    private ap f;
    private boolean g;
    private e h;
    private InterfaceC0022f i;
    private g j;
    private k k;
    private int l;
    private int m;
    private boolean n;

    /* loaded from: classes.dex */
    public abstract class a implements e {
        public int[] a;

        public a(int[] iArr) {
            this.a = a(iArr);
        }

        private int[] a(int[] iArr) {
            if (f.this.m == 2 || f.this.m == 3) {
                int length = iArr.length;
                int[] iArr2 = new int[length + 2];
                int i = length - 1;
                System.arraycopy(iArr, 0, iArr2, 0, i);
                iArr2[i] = 12352;
                if (f.this.m == 2) {
                    iArr2[length] = 4;
                } else {
                    iArr2[length] = 64;
                }
                iArr2[length + 1] = 12344;
                return iArr2;
            }
            return iArr;
        }

        @Override // com.baidu.platform.comapi.map.f.e
        public EGLConfig a(EGL10 egl10, EGLDisplay eGLDisplay) {
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

        public abstract EGLConfig a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr);
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

        @Override // com.baidu.platform.comapi.map.f.a
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
    public class c implements InterfaceC0022f {
        private int b;

        private c() {
            this.b = 12440;
        }

        @Override // com.baidu.platform.comapi.map.f.InterfaceC0022f
        public EGLContext a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
            int[] iArr = {this.b, f.this.m, 12344};
            EGLContext eGLContext = EGL10.EGL_NO_CONTEXT;
            if (f.this.m == 0) {
                iArr = null;
            }
            return egl10.eglCreateContext(eGLDisplay, eGLConfig, eGLContext, iArr);
        }

        @Override // com.baidu.platform.comapi.map.f.InterfaceC0022f
        public void a(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext) {
            if (!egl10.eglDestroyContext(eGLDisplay, eGLContext)) {
                Log.e("DefaultContextFactory", "display:" + eGLDisplay + " context: " + eGLContext);
                h.a("eglDestroyContex", egl10.eglGetError());
            }
        }
    }

    /* loaded from: classes.dex */
    public static class d implements g {
        private d() {
        }

        @Override // com.baidu.platform.comapi.map.f.g
        public EGLSurface a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj) {
            try {
                return egl10.eglCreateWindowSurface(eGLDisplay, eGLConfig, obj, null);
            } catch (IllegalArgumentException e) {
                Log.e("GLRenderControl", "eglCreateWindowSurface", e);
                return null;
            }
        }

        @Override // com.baidu.platform.comapi.map.f.g
        public void a(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface) {
            egl10.eglDestroySurface(eGLDisplay, eGLSurface);
        }
    }

    /* loaded from: classes.dex */
    public interface e {
        EGLConfig a(EGL10 egl10, EGLDisplay eGLDisplay);
    }

    /* renamed from: com.baidu.platform.comapi.map.f$f  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public interface InterfaceC0022f {
        EGLContext a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig);

        void a(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext);
    }

    /* loaded from: classes.dex */
    public interface g {
        EGLSurface a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj);

        void a(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface);
    }

    /* loaded from: classes.dex */
    public static class h {
        public EGL10 a;
        public EGLDisplay b;
        public EGLSurface c;
        public EGLConfig d;
        public EGLContext e;
        private WeakReference<f> f;

        public h(WeakReference<f> weakReference) {
            this.f = weakReference;
        }

        private static String a(int i) {
            switch (i) {
                case 12288:
                    return "EGL_SUCCESS";
                case 12289:
                    return "EGL_NOT_INITIALIZED";
                case 12290:
                    return "EGL_BAD_ACCESS";
                case 12291:
                    return "EGL_BAD_ALLOC";
                case 12292:
                    return "EGL_BAD_ATTRIBUTE";
                case 12293:
                    return "EGL_BAD_CONFIG";
                case 12294:
                    return "EGL_BAD_CONTEXT";
                case 12295:
                    return "EGL_BAD_CURRENT_SURFACE";
                case 12296:
                    return "EGL_BAD_DISPLAY";
                case 12297:
                    return "EGL_BAD_MATCH";
                case 12298:
                    return "EGL_BAD_NATIVE_PIXMAP";
                case 12299:
                    return "EGL_BAD_NATIVE_WINDOW";
                case 12300:
                    return "EGL_BAD_PARAMETER";
                case 12301:
                    return "EGL_BAD_SURFACE";
                case 12302:
                    return "EGL_CONTEXT_LOST";
                default:
                    return b(i);
            }
        }

        private void a(String str) {
            a(str, this.a.eglGetError());
        }

        public static void a(String str, int i) {
            throw new RuntimeException(b(str, i));
        }

        public static void a(String str, String str2, int i) {
            Log.w(str, b(str2, i));
        }

        private static String b(int i) {
            return "0x" + Integer.toHexString(i);
        }

        public static String b(String str, int i) {
            return str + " failed: " + a(i);
        }

        private void g() {
            EGLSurface eGLSurface;
            EGLSurface eGLSurface2 = this.c;
            if (eGLSurface2 == null || eGLSurface2 == (eGLSurface = EGL10.EGL_NO_SURFACE)) {
                return;
            }
            this.a.eglMakeCurrent(this.b, eGLSurface, eGLSurface, EGL10.EGL_NO_CONTEXT);
            f fVar = this.f.get();
            if (fVar != null) {
                fVar.j.a(this.a, this.b, this.c);
            }
            this.c = null;
        }

        public void a() {
            EGL10 egl10 = (EGL10) EGLContext.getEGL();
            this.a = egl10;
            EGLDisplay eglGetDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            this.b = eglGetDisplay;
            if (eglGetDisplay != EGL10.EGL_NO_DISPLAY) {
                if (!this.a.eglInitialize(eglGetDisplay, new int[2])) {
                    throw new RuntimeException("eglInitialize failed");
                }
                f fVar = this.f.get();
                if (fVar == null) {
                    this.d = null;
                    this.e = null;
                } else {
                    this.d = fVar.h.a(this.a, this.b);
                    this.e = fVar.i.a(this.a, this.b, this.d);
                }
                EGLContext eGLContext = this.e;
                if (eGLContext == null || eGLContext == EGL10.EGL_NO_CONTEXT) {
                    this.e = null;
                    a("createContext");
                }
                this.c = null;
                return;
            }
            throw new RuntimeException("eglGetDisplay failed");
        }

        public boolean b() {
            if (this.a != null) {
                if (this.b == null) {
                    throw new RuntimeException("eglDisplay not initialized");
                }
                if (this.d == null) {
                    throw new RuntimeException("mEglConfig not initialized");
                }
                g();
                f fVar = this.f.get();
                this.c = fVar != null ? fVar.j.a(this.a, this.b, this.d, fVar.a()) : null;
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
            f fVar = this.f.get();
            if (fVar != null) {
                if (fVar.k != null) {
                    gl = fVar.k.a(gl);
                }
                if ((fVar.l & 3) == 0) {
                    return gl;
                }
                int i = 0;
                l lVar = null;
                if ((fVar.l & 1) != 0) {
                    i = 1;
                }
                if ((fVar.l & 2) != 0) {
                    lVar = new l();
                }
                return GLDebugHelper.wrap(gl, i, lVar);
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
            g();
        }

        public void f() {
            if (this.e != null) {
                f fVar = this.f.get();
                if (fVar != null) {
                    fVar.i.a(this.a, this.b, this.e);
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
    public static class i extends Thread {
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
        private boolean k;
        private boolean q;
        private h u;
        private WeakReference<f> v;
        private ArrayList<Runnable> r = new ArrayList<>();
        private boolean s = true;
        private Runnable t = null;
        private int l = 0;
        private int m = 0;
        private boolean o = true;
        private int n = 1;
        private boolean p = false;

        public i(WeakReference<f> weakReference) {
            this.v = weakReference;
            setPriority(10);
        }

        private void i() {
            if (this.i) {
                this.i = false;
                this.u.e();
            }
        }

        private void j() {
            if (this.h) {
                this.u.f();
                this.h = false;
                f.c.b(this);
            }
        }

        private void k() throws InterruptedException {
            boolean z;
            boolean z2;
            boolean z3;
            int i;
            boolean z4;
            boolean z5;
            i iVar = this;
            iVar.u = new h(iVar.v);
            iVar.h = false;
            iVar.i = false;
            iVar.p = false;
            boolean z6 = false;
            boolean z7 = false;
            boolean z8 = false;
            boolean z9 = false;
            boolean z10 = false;
            boolean z11 = false;
            boolean z12 = false;
            boolean z13 = false;
            int i2 = 0;
            int i3 = 0;
            Runnable runnable = null;
            GL10 gl10 = null;
            while (true) {
                Runnable runnable2 = null;
                while (true) {
                    try {
                        synchronized (f.c) {
                            while (!iVar.a) {
                                if (!iVar.r.isEmpty()) {
                                    runnable2 = iVar.r.remove(0);
                                } else {
                                    boolean z14 = iVar.d;
                                    boolean z15 = iVar.c;
                                    if (z14 != z15) {
                                        iVar.d = z15;
                                        f.c.notifyAll();
                                    } else {
                                        z15 = false;
                                    }
                                    if (iVar.k) {
                                        i();
                                        j();
                                        iVar.k = false;
                                        z8 = true;
                                    }
                                    if (z6) {
                                        i();
                                        j();
                                        z6 = false;
                                    }
                                    if (z15 && iVar.i) {
                                        i();
                                    }
                                    if (z15 && iVar.h) {
                                        f fVar = iVar.v.get();
                                        if (!(fVar == null ? false : fVar.n)) {
                                            j();
                                        }
                                    }
                                    if (!iVar.e && !iVar.g) {
                                        if (iVar.i) {
                                            i();
                                        }
                                        iVar.g = true;
                                        iVar.f = false;
                                        f.c.notifyAll();
                                    }
                                    if (iVar.e && iVar.g) {
                                        iVar.g = false;
                                        f.c.notifyAll();
                                    }
                                    if (z7) {
                                        iVar.p = false;
                                        iVar.q = true;
                                        f.c.notifyAll();
                                        z7 = false;
                                    }
                                    Runnable runnable3 = iVar.t;
                                    if (runnable3 != null) {
                                        iVar.t = null;
                                        runnable = runnable3;
                                    }
                                    if (l()) {
                                        if (!iVar.h) {
                                            if (z8) {
                                                z8 = false;
                                            } else {
                                                try {
                                                    iVar.u.a();
                                                    iVar.h = true;
                                                    f.c.notifyAll();
                                                    z9 = true;
                                                } catch (RuntimeException e) {
                                                    f.c.b(iVar);
                                                    throw e;
                                                }
                                            }
                                        }
                                        if (iVar.h && !iVar.i) {
                                            iVar.i = true;
                                            z10 = true;
                                            z11 = true;
                                            z12 = true;
                                        }
                                        if (iVar.i) {
                                            if (iVar.s) {
                                                i2 = iVar.l;
                                                i3 = iVar.m;
                                                iVar.p = true;
                                                iVar.s = false;
                                                z5 = false;
                                                z10 = true;
                                                z12 = true;
                                            } else {
                                                z5 = false;
                                            }
                                            iVar.o = z5;
                                            f.c.notifyAll();
                                            if (iVar.p) {
                                                z13 = true;
                                            }
                                        }
                                    } else if (runnable != null) {
                                        Log.w("GLRenderControl", "Warning, !readyToDraw() but waiting for draw finished! Early reporting draw finished.");
                                        runnable.run();
                                        runnable = null;
                                    }
                                    f.c.wait();
                                    iVar = this;
                                }
                            }
                            synchronized (f.c) {
                                i();
                                j();
                            }
                            return;
                        }
                        if (runnable2 != null) {
                            break;
                        }
                        if (z10) {
                            if (iVar.u.b()) {
                                synchronized (f.c) {
                                    iVar.j = true;
                                    f.c.notifyAll();
                                }
                                z10 = false;
                            } else {
                                synchronized (f.c) {
                                    iVar.j = true;
                                    iVar.f = true;
                                    f.c.notifyAll();
                                }
                            }
                        }
                        if (z11) {
                            gl10 = (GL10) iVar.u.c();
                            z11 = false;
                        }
                        if (z9) {
                            f fVar2 = iVar.v.get();
                            if (fVar2 != null) {
                                z = z6;
                                z2 = z7;
                                z3 = false;
                                fVar2.f.a(null, fVar2.c(), fVar2.d(), 0);
                                Log.d("GLRenderControl", "mRenderer.onSurfaceCreated");
                            } else {
                                z = z6;
                                z2 = z7;
                                z3 = false;
                            }
                            z9 = z3;
                        } else {
                            z = z6;
                            z2 = z7;
                            z3 = false;
                        }
                        if (z12) {
                            f fVar3 = iVar.v.get();
                            if (fVar3 != null) {
                                fVar3.f.a(i2, i3);
                                Log.d("GLRenderControl", "mRenderer.onSurfaceChanged");
                            }
                            z12 = z3;
                        }
                        long currentTimeMillis = System.currentTimeMillis();
                        f fVar4 = iVar.v.get();
                        if (fVar4 != null) {
                            fVar4.f.a(gl10);
                            if (runnable != null) {
                                runnable.run();
                                runnable = null;
                            }
                            i = fVar4.e();
                        } else {
                            i = 60;
                        }
                        int d = iVar.u.d();
                        if (d == 12288) {
                            z4 = true;
                        } else if (d != 12302) {
                            h.a("GLThread", "eglSwapBuffers", d);
                            synchronized (f.c) {
                                z4 = true;
                                iVar.f = true;
                                f.c.notifyAll();
                            }
                        } else {
                            z4 = true;
                            z = true;
                        }
                        if (z13) {
                            z7 = z4;
                            z13 = false;
                        } else {
                            z7 = z2;
                        }
                        long currentTimeMillis2 = System.currentTimeMillis();
                        if (i < 60 && i > 0) {
                            long j = (1000 / i) - (currentTimeMillis2 - currentTimeMillis);
                            if (j > 1) {
                                synchronized (f.c) {
                                    f.c.wait(j);
                                }
                            }
                        }
                        iVar = this;
                        z6 = z;
                    } catch (Throwable th) {
                        synchronized (f.c) {
                            i();
                            j();
                            throw th;
                        }
                    }
                }
                runnable2.run();
            }
        }

        private boolean l() {
            return !this.d && this.e && !this.f && this.l > 0 && this.m > 0 && (this.o || this.n == 1);
        }

        public void a(int i) {
            if (i < 0 || i > 1) {
                throw new IllegalArgumentException("renderMode");
            }
            synchronized (f.c) {
                this.n = i;
                f.c.notifyAll();
            }
        }

        public void a(int i, int i2) {
            synchronized (f.c) {
                this.l = i;
                this.m = i2;
                this.s = true;
                this.o = true;
                this.q = false;
                if (Thread.currentThread() == this) {
                    return;
                }
                f.c.notifyAll();
                while (!this.b && !this.d && !this.q && a()) {
                    try {
                        f.c.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void a(Runnable runnable) {
            synchronized (f.c) {
                if (Thread.currentThread() == this) {
                    return;
                }
                this.p = true;
                this.o = true;
                this.q = false;
                this.t = runnable;
                f.c.notifyAll();
            }
        }

        public boolean a() {
            return this.h && this.i && l();
        }

        public int b() {
            int i;
            synchronized (f.c) {
                i = this.n;
            }
            return i;
        }

        public void b(Runnable runnable) {
            if (runnable != null) {
                synchronized (f.c) {
                    this.r.add(runnable);
                    f.c.notifyAll();
                }
                return;
            }
            throw new IllegalArgumentException("r must not be null");
        }

        public void c() {
            synchronized (f.c) {
                this.o = true;
                f.c.notifyAll();
            }
        }

        public void d() {
            synchronized (f.c) {
                this.e = true;
                this.j = false;
                f.c.notifyAll();
                while (this.g && !this.j && !this.b) {
                    try {
                        f.c.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void e() {
            synchronized (f.c) {
                this.e = false;
                f.c.notifyAll();
                while (!this.g && !this.b) {
                    try {
                        f.c.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void f() {
            synchronized (f.c) {
                this.c = true;
                f.c.notifyAll();
                while (!this.b && !this.d) {
                    try {
                        f.c.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void g() {
            synchronized (f.c) {
                this.c = false;
                this.o = true;
                this.q = false;
                f.c.notifyAll();
                while (!this.b && this.d && !this.q) {
                    try {
                        f.c.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void h() {
            synchronized (f.c) {
                this.a = true;
                f.c.notifyAll();
                while (!this.b) {
                    try {
                        f.c.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            setName("GLThread " + getId());
            try {
                k();
            } catch (InterruptedException unused) {
            } catch (Throwable th) {
                f.c.a(this);
                throw th;
            }
            f.c.a(this);
        }
    }

    /* loaded from: classes.dex */
    public static class j {
        private static String a = "GLThreadManager";

        private j() {
        }

        public synchronized void a(i iVar) {
            iVar.b = true;
            notifyAll();
        }

        public void b(i iVar) {
            notifyAll();
        }
    }

    /* loaded from: classes.dex */
    public interface k {
        GL a(GL gl);
    }

    /* loaded from: classes.dex */
    public static class l extends Writer {
        private StringBuilder a = new StringBuilder();

        private void a() {
            if (this.a.length() > 0) {
                Log.v("GLSurfaceView26", this.a.toString());
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
    public class m extends b {
        public m(boolean z) {
            super(8, 8, 8, 0, z ? 16 : 0, 0);
        }
    }

    public f(SurfaceView surfaceView) {
        this.a = new WeakReference<>(surfaceView);
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

    private void n() {
        if (this.e == null) {
            return;
        }
        throw new IllegalStateException("setRenderer has already been called for this instance.");
    }

    @Override // com.baidu.platform.comapi.map.ag
    public Bitmap a(int i2, int i3, int i4, int i5, Object obj, Bitmap.Config config) {
        return a(i2, i3, i4, i5, (GL10) obj, config);
    }

    public SurfaceHolder a() {
        SurfaceView surfaceView = this.a.get();
        if (surfaceView != null) {
            return surfaceView.getHolder();
        }
        return null;
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void a(int i2) {
        if (i2 <= 0) {
            return;
        }
        if (i2 > 60) {
            i2 = 60;
        }
        this.b = i2;
    }

    public void a(int i2, int i3, int i4, int i5, int i6, int i7) {
        a(new b(i2, i3, i4, i5, i6, i7));
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void a(ap apVar) {
        n();
        if (this.h == null) {
            this.h = new m(true);
        }
        if (this.i == null) {
            this.i = new c();
        }
        if (this.j == null) {
            this.j = new d();
        }
        this.f = apVar;
        i iVar = new i(this.d);
        this.e = iVar;
        iVar.start();
    }

    public void a(e eVar) {
        n();
        this.h = eVar;
    }

    public void a(InterfaceC0022f interfaceC0022f) {
        n();
        this.i = interfaceC0022f;
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void a(Runnable runnable) {
        this.e.b(runnable);
    }

    public void a(boolean z) {
        this.n = z;
    }

    @Override // com.baidu.platform.comapi.map.ag
    public ah.a b() {
        return ah.a.OPENGL_ES;
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void b(int i2) {
        this.l = i2;
    }

    public void b(boolean z) {
        a(new m(z));
    }

    public int c() {
        SurfaceView surfaceView = this.a.get();
        if (surfaceView != null) {
            return surfaceView.getWidth();
        }
        return 0;
    }

    public void c(int i2) {
        n();
        this.m = i2;
    }

    public int d() {
        SurfaceView surfaceView = this.a.get();
        if (surfaceView != null) {
            return surfaceView.getHeight();
        }
        return 0;
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void d(int i2) {
        this.e.a(i2);
    }

    @Override // com.baidu.platform.comapi.map.ag
    public int e() {
        return this.b;
    }

    @Override // com.baidu.platform.comapi.map.ag
    public int f() {
        return this.l;
    }

    public void finalize() throws Throwable {
        try {
            i iVar = this.e;
            if (iVar != null) {
                iVar.h();
            }
        } finally {
            super.finalize();
        }
    }

    @Override // com.baidu.platform.comapi.map.ag
    public int g() {
        return this.e.b();
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void h() {
        this.e.c();
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void i() {
        this.e.f();
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void j() {
        this.e.g();
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void k() {
        if (this.g && this.f != null) {
            i iVar = this.e;
            int b2 = iVar != null ? iVar.b() : 1;
            i iVar2 = new i(this.d);
            this.e = iVar2;
            if (b2 != 1) {
                iVar2.a(b2);
            }
            this.e.start();
        }
        this.g = false;
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void l() {
        i iVar = this.e;
        if (iVar != null) {
            iVar.h();
        }
        this.g = true;
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i2, int i3, int i4) {
        this.e.a(i3, i4);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.e.d();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.e.e();
    }

    @Override // android.view.SurfaceHolder.Callback2
    @Deprecated
    public void surfaceRedrawNeeded(SurfaceHolder surfaceHolder) {
    }

    @Override // android.view.SurfaceHolder.Callback2
    public void surfaceRedrawNeededAsync(SurfaceHolder surfaceHolder, Runnable runnable) {
        i iVar = this.e;
        if (iVar != null) {
            iVar.a(runnable);
        }
    }
}
