package com.nexstreaming.kminternal.nexvideoeditor;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.Image;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader;
import com.nexstreaming.app.common.task.ResultTask;
import com.nexstreaming.app.common.task.Task;
import com.nexstreaming.kminternal.kinemaster.codeccolorformat.ColorFormatChecker;
import com.nexstreaming.kminternal.kinemaster.config.NexEditorDeviceProfile;
import com.nexstreaming.kminternal.nexvideoeditor.NexImageLoader;
import com.nexstreaming.nexeditorsdk.nexEngine;
import com.xiaomi.miai.api.StatusCode;
import dalvik.system.BaseDexClassLoader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Deque;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/* loaded from: classes3.dex */
public class NexEditor {
    private static File D = null;
    private static boolean O = false;
    private static int P = 1280;
    private static int Q = 720;
    private static int R = 0;
    private static NexEditor W = null;
    private static boolean ae = false;
    private int E;
    private int F;
    private int G;
    private int K;
    private Surface L;
    private int S;
    private m X;
    private h Y;
    private r Z;
    private j aG;
    private k ab;
    private l ac;
    private s ad;
    private ResultTask<Rect> ag;
    private int al;
    private String ap;
    private int ar;
    private NexEditorEventListener d;
    private NexThemeView e;
    private static final ExecutorService at = Executors.newSingleThreadExecutor();
    private static final f aF = new f() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.9
        @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.f
        public void a(ErrorCode errorCode) {
        }
    };
    private int a = 1;
    private boolean b = false;
    private boolean c = false;
    private com.nexstreaming.kminternal.nexvideoeditor.c f = null;
    private t[] g = null;
    private a[] h = null;
    private a[] i = null;
    private boolean j = false;
    private Deque<d> k = new LinkedList();
    private Deque<d> l = new LinkedList();
    private Deque<g> m = new LinkedList();
    private Deque<n> n = new LinkedList();
    private Deque<p> o = new LinkedList();
    private Deque<f> p = new LinkedList();
    private Deque<o> q = new LinkedList();
    private Deque<o> r = new LinkedList();
    private Deque<f> s = new LinkedList();
    private Deque<f> t = new LinkedList();
    private Deque<Integer> u = new LinkedList();
    private Deque<e> v = new LinkedList();
    private Deque<i> w = new LinkedList();
    private q x = null;
    private int y = 1;
    private int z = 0;
    private int A = 0;
    private int B = 0;
    private int C = 0;
    private ColorFormatChecker.ColorFormat H = null;
    private int I = 0;
    private String J = null;
    private int M = 1;
    private int N = 0;
    private Thread T = null;
    private com.nexstreaming.kminternal.nexvideoeditor.d U = null;
    private Task V = null;
    private Task aa = null;
    private boolean af = false;
    private SurfaceHolder.Callback ah = new SurfaceHolder.Callback() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.13
        @Override // android.view.SurfaceHolder.Callback
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i2, int i3, int i4) {
            if (NexEditor.this.ag != null) {
                NexEditor.this.ag.sendResult(new Rect(0, 0, i3, i4));
                NexEditor.this.ag = null;
            }
        }
    };
    private boolean ai = false;
    private boolean aj = false;
    private boolean ak = false;
    private int am = 0;
    private boolean an = false;
    private boolean ao = false;
    private Task aq = null;
    private ImageReader as = null;
    private BlockingQueue<Integer> au = new ArrayBlockingQueue(3);
    private boolean av = false;
    private v aw = null;
    private Object ax = new Object();
    private boolean ay = false;
    private boolean az = false;
    private int aA = 0;
    private int aB = 0;
    private boolean aC = false;
    private int aD = 0;
    private int aE = 0;
    private float aH = 1.0f;

    /* loaded from: classes3.dex */
    public enum FastPreviewOption {
        normal,
        brightness,
        contrast,
        saturation,
        adj_brightness,
        adj_contrast,
        adj_saturation,
        tintColor,
        left,
        top,
        right,
        bottom,
        nofx,
        cts,
        swapv,
        video360flag,
        video360_horizontal,
        video360_vertical,
        adj_vignette,
        adj_vignetteRange,
        adj_sharpness,
        customlut_clip,
        customlut_power
    }

    /* loaded from: classes3.dex */
    public static class a extends u {
    }

    /* loaded from: classes3.dex */
    public interface b {
        b a(int i);

        b a(int i, int i2);

        b a(Rect rect);

        b a(FastPreviewOption fastPreviewOption, int i);

        b a(boolean z);

        void a();
    }

    /* loaded from: classes3.dex */
    public interface c {
        void a(LayerRenderer layerRenderer);
    }

    /* loaded from: classes3.dex */
    public static abstract class d {
        public int a;

        public abstract void a(int i, int i2, int i3);

        public abstract void a(ErrorCode errorCode);
    }

    /* loaded from: classes3.dex */
    public static abstract class e {
        public abstract void a(Bitmap bitmap);

        public abstract void a(ErrorCode errorCode);
    }

    /* loaded from: classes3.dex */
    public static abstract class g {
        public abstract void a();

        public abstract void a(ErrorCode errorCode);
    }

    /* loaded from: classes3.dex */
    public interface h {
        void a();
    }

    /* loaded from: classes3.dex */
    public static abstract class i {
        public abstract void a();

        public abstract void a(int i);

        public abstract void a(ErrorCode errorCode);
    }

    /* loaded from: classes3.dex */
    public interface j {
        void a(ErrorCode errorCode, int i);
    }

    /* loaded from: classes3.dex */
    public interface k {
        void a(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6, int i7, int i8);
    }

    /* loaded from: classes3.dex */
    public interface l {
        int a(int i);

        int a(int i, int[] iArr);
    }

    /* loaded from: classes3.dex */
    public interface m {
        void a();
    }

    /* loaded from: classes3.dex */
    public static abstract class n {
        public abstract void a(int i);

        public abstract void a(ErrorCode errorCode);
    }

    /* loaded from: classes3.dex */
    public static abstract class o {
        public abstract void a();

        public abstract void a(ErrorCode errorCode);
    }

    /* loaded from: classes3.dex */
    public static abstract class q {
        public abstract void a();
    }

    /* loaded from: classes3.dex */
    public interface r {
        void a(int i, int i2, int i3);

        void a(ErrorCode errorCode, int i);
    }

    /* loaded from: classes3.dex */
    public interface s {
        int a(int i);

        int a(int i, int[] iArr, int[] iArr2);
    }

    /* loaded from: classes3.dex */
    public static class t extends u {
    }

    /* loaded from: classes3.dex */
    public static abstract class u {
    }

    /* loaded from: classes3.dex */
    public static abstract class v {
        public abstract void a();

        public abstract boolean a(int i);

        public abstract boolean a(byte[] bArr, int i);

        public abstract int b();

        public abstract void b(int i);
    }

    private native int asyncLoadList(NexVisualClip[] nexVisualClipArr, NexAudioClip[] nexAudioClipArr, int i2);

    private native int captureCurrentFrame();

    private native int clearRenderItems(int i2);

    private native int clearScreen(int i2);

    private native int closeInputFile(int i2, int i3);

    private native void closeOutputFile(int i2);

    private native int createEditor(String str, String str2, int i2, int i3, int[] iArr);

    private native int createRenderItem(String str, int i2);

    private native int deleteClipID(int i2);

    private native int destroyEditor();

    private native int drawRenderItemOverlay(int i2, String str, int i3, int i4, int i5, int i6, float[] fArr, float f2, float f3, float f4, float f5, float f6);

    private native int encodeProject(String str, int i2, int i3, int i4, long j2, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14);

    private native int encodeProjectJpeg(Surface surface, String str, int i2, int i3, int i4, int i5);

    private native int fastOptionPreview(String str, int i2);

    private native int getAudioSessionID();

    private native int getClipAudioThumb(String str, String str2, int i2);

    private native int getClipInfoSync(String str, NexClipInfo nexClipInfo, int i2, int i3);

    private native int getClipStopThumb(int i2);

    private native int getClipVideoThumb(String str, String str2, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    private native int getClipVideoThumbWithTimeTable(String str, String str2, int i2, int i3, int i4, int[] iArr, int i5, int i6);

    private native int getNativeSDKInfoWM();

    private static native String getSystemProperty(String str);

    private native int getTexNameForClipID(int i2, int i3);

    private native int getTexNameForMask(int i2);

    private native byte[] getTimeThumbData(int i2);

    private native int highlightStart(String str, int i2, int i3, int i4, int i5, String str2, int i6, int i7, int i8, long j2, int i9, int i10, int i11);

    private native int highlightStop();

    private native int initUserData();

    private native int loadList(NexVisualClip[] nexVisualClipArr, NexAudioClip[] nexAudioClipArr, int i2);

    private native int loadRenderItem(String str, String str2, int i2);

    private native int loadTheme(String str, String str2, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public native int prepareSurface(Surface surface);

    private native int pushLoadedBitmap(String str, int[] iArr, int i2, int i3, int i4);

    private native int releaseLUTTexture(int i2);

    private native int releaseRenderItem(int i2, int i3);

    private native int removeBitmap(String str);

    private native int resetFaceDetectInfo(int i2);

    private native int set360VideoTrackPosition(int i2, int i3, int i4);

    private static native void setEncInfo(String[] strArr);

    private native int setEventHandler(NexEditorEventListener nexEditorEventListener);

    /* JADX INFO: Access modifiers changed from: private */
    public native int setGIFMode(int i2);

    private native int setInputFile(FileDescriptor fileDescriptor, int i2, long j2, long j3);

    private static native void setPacakgeName4Protection(String str);

    private native int setPreviewScaleFactor(float f2);

    private native int setRenderToDefault(int i2);

    private native int setRenderToMask(int i2);

    private native int setThumbnailRoutine(int i2);

    private native int setTime(int i2, int i3, int i4);

    private native int setVideoTrackUUID(int i2, byte[] bArr);

    /* JADX INFO: Access modifiers changed from: private */
    public native int startPlay(int i2);

    private native int stopPlay(int i2);

    private native int transcodingStart(String str, String str2, int i2, int i3, int i4, int i5, int i6, long j2, int i7, int i8, String str3);

    private native int transcodingStop();

    private native int updateRenderInfo(int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15);

    public native int addUDTA(int i2, String str);

    public native int asyncDrawInfoList(NexDrawInfo[] nexDrawInfoArr, NexDrawInfo[] nexDrawInfoArr2);

    public native int checkDirectExport(int i2);

    public native int checkIDREnd();

    public native int checkIDRStart(String str);

    public native int checkIDRTime(int i2);

    public native int checkPFrameDirectExportSync(String str);

    public native int clearProject();

    public native int clearTrackCache();

    public native int clearUDTA();

    public native int closeProject();

    public native int[] createCubeLUT(byte[] bArr);

    public native int[] createLGLUT(byte[] bArr);

    public native int createProject();

    public native int directExport(String str, long j2, long j3, String str2, int i2);

    public native int encodePause();

    public native int encodeResume();

    public native int fastPreviewStart(int i2, int i3, int i4, int i5);

    public native int fastPreviewStop();

    public native int fastPreviewTime(int i2);

    public native int getBrightness();

    public native int getContrast();

    public native int getDuration();

    public native String getProperty(String str);

    public native int getSaturation();

    public native int getSharpness();

    public native int getVignette();

    public native int getVignetteRange();

    public native int reverseStart(String str, String str2, String str3, int i2, int i3, int i4, long j2, int i5, int i6, int i7);

    public native int reverseStop();

    public native int setBaseFilterRenderItem(String str);

    public native int setBrightness(int i2);

    public native int setContrast(int i2);

    public native int setDeviceGamma(float f2);

    public native int setDeviceLightLevel(int i2);

    public native int setProjectEffect(String str);

    public native int setProjectManualVolumeControl(int i2);

    public native int setProjectVolume(int i2);

    public native int setProjectVolumeFade(int i2, int i3);

    public native int setProperty(String str, String str2);

    public native int setSaturation(int i2);

    public native void setSharpness(int i2);

    public native int setTaskSleep(int i2);

    public native void setVignette(int i2);

    public native void setVignetteRange(int i2);

    public native int setVolumeWhilePlay(int i2, int i3);

    public native int updateDrawInfo(NexDrawInfo nexDrawInfo);

    public static void a(int i2, int i3, int i4) {
        P = i2;
        Q = i3;
        R = i4;
    }

    public static int a() {
        return P;
    }

    public static int b() {
        return Q;
    }

    public static int c() {
        return R;
    }

    public void a(int i2) {
        this.N = i2;
    }

    public int d() {
        return this.N;
    }

    public void a(Thread thread) {
        this.T = thread;
    }

    public void e() {
        Thread thread = this.T;
        if (thread == null) {
            return;
        }
        try {
            thread.join();
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        this.T = null;
    }

    /* loaded from: classes3.dex */
    public static class EditorInitException extends Exception {
        private static final long serialVersionUID = 1;

        public EditorInitException() {
        }

        public EditorInitException(String str, Throwable th) {
            super(str, th);
        }

        public EditorInitException(String str) {
            super(str);
        }

        public EditorInitException(Throwable th) {
            super(th);
        }
    }

    public Task a(Context context) {
        Log.d("NexEditor.java", "detectAndSetEditorColorFormat");
        if (this.V == null) {
            this.V = new Task();
            if (NexEditorDeviceProfile.getDeviceProfile().getNeedsColorFormatCheck()) {
                ColorFormatChecker.a(context).onResultAvailable(new ResultTask.OnResultAvailableListener<ColorFormatChecker.ColorFormat>() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.10
                    @Override // com.nexstreaming.app.common.task.ResultTask.OnResultAvailableListener
                    /* renamed from: a */
                    public void onResultAvailable(ResultTask<ColorFormatChecker.ColorFormat> resultTask, Task.Event event, ColorFormatChecker.ColorFormat colorFormat) {
                        NexEditor.this.H = colorFormat;
                        if (colorFormat == ColorFormatChecker.ColorFormat.UNKNOWN) {
                            NexEditor.this.V.signalEvent(Task.Event.FAIL);
                            return;
                        }
                        Log.d("NexEditor.java", "Setting color format: " + colorFormat.name());
                        NexEditor.this.setProperty("setExportColorFormat", colorFormat.name());
                        NexEditor.this.V.signalEvent(Task.Event.COMPLETE);
                    }
                }).mo1851onFailure(new Task.OnFailListener() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.1
                    @Override // com.nexstreaming.app.common.task.Task.OnFailListener
                    public void onFail(Task task, Task.Event event, Task.TaskError taskError) {
                        NexEditor.this.V.sendFailure(taskError);
                    }
                });
            } else {
                Log.d("NexEditor.java", "Skip checking color format (not needed)");
                this.V.signalEvent(Task.Event.COMPLETE);
            }
        }
        return this.V;
    }

    public NexEditor(Context context, NexThemeView nexThemeView, String str, int i2, NexImageLoader.d dVar, int[] iArr) throws EditorInitException {
        String str2;
        boolean z = true;
        this.d = null;
        this.e = null;
        this.S = -1;
        W = this;
        int i3 = i2 ^ 323655054;
        if (context.getFilesDir() == null) {
            throw new IllegalStateException("No files directory - cannot play video - relates to Android issue: 8886!");
        }
        String findLibrary = ((BaseDexClassLoader) context.getClassLoader()).findLibrary("nexcralbody_mc_jb");
        if (findLibrary != null) {
            String str3 = File.separator;
            int lastIndexOf = findLibrary.lastIndexOf(str3);
            str2 = findLibrary.substring(0, lastIndexOf) + str3;
            Log.d("NexEditor.java", "[nexlib] getApplicationInfo mc libarays in: " + str2);
        } else {
            String str4 = context.getApplicationInfo().nativeLibraryDir;
            String str5 = File.separator;
            str4 = !str4.endsWith(str5) ? str4 + str5 : str4;
            Log.d("NexEditor.java", "[nexlib] getApplicationInfo says libs are in: " + str4);
            Log.d("NexEditor.java", "[nexlib] sdk lib name: libnexeditorsdk.so");
            if (new File(str4, "libnexeditorsdk.so").exists()) {
                Log.d("NexEditor.java", "[nexlib] libs found in: " + str4);
            } else {
                String property = System.getProperty("java.library.path");
                if (property != null) {
                    String[] split = property.split(":");
                    for (int i4 = 0; i4 < split.length; i4++) {
                        if (!split[i4].endsWith(com.xiaomi.stat.b.h.g)) {
                            split[i4] = split[i4] + com.xiaomi.stat.b.h.g;
                        }
                        Log.d("NexEditor.java", "[nexlib] trying: " + split[i4]);
                        if (new File(split[i4], "libnexeditorsdk.so").exists()) {
                            Log.d("NexEditor.java", "[nexlib] libs found in: " + split[i4]);
                            str4 = split[i4];
                            break;
                        }
                        Log.d("NexEditor.java", "[nexlib] libs NOT FOUND!");
                    }
                }
                z = false;
                if (!z) {
                    String str6 = "/system/lib64/";
                    if (!str4.contains("/arm64") && !str4.contains("/x86_64")) {
                        str6 = "/system/lib/";
                    }
                    Log.d("NexEditor.java", "[nexlib]2 trying: " + str6);
                    if (new File(str6, "libnexeditorsdk.so").exists()) {
                        Log.d("NexEditor.java", "[nexlib]2 libs found in: " + str6);
                    } else {
                        Log.d("NexEditor.java", "[nexlib]2 libs NOT FOUND!");
                    }
                    str2 = str6;
                }
            }
            str2 = str4;
        }
        c(context.getPackageName());
        int createEditor = createEditor(str2, str, Build.VERSION.SDK_INT, initUserData() ^ i3, iArr == null ? null : Arrays.copyOf(iArr, iArr.length + 2));
        if (createEditor != 0) {
            throw new EditorInitException("Editor Initialization Failed (result=" + createEditor + ")");
        }
        if (NexEditorDeviceProfile.getDeviceProfile().getUserConfigSettings()) {
            setProperty("HardWareCodecMemSize", "" + NexEditorDeviceProfile.getDeviceProfile().getHardwareCodecMemSize());
            setProperty("HardWareDecMaxCount", "" + NexEditorDeviceProfile.getDeviceProfile().getHardwareDecMaxCount());
            setProperty("HardWareEncMaxCount", "" + NexEditorDeviceProfile.getDeviceProfile().getHardwareEncMaxCount());
            setProperty("FeatureVersion", "3");
            setProperty("useNexEditorSDK", "1");
            setProperty("DeviceExtendMode", "1");
            setProperty("forceDirectExport", "" + NexEditorDeviceProfile.getDeviceProfile().getForceDirectExport());
            setProperty("SupportedMaxFPS", "" + NexEditorDeviceProfile.getDeviceProfile().getMaxSupportedFPS());
            setProperty("InputMaxFPS", "" + NexEditorDeviceProfile.getDeviceProfile().getMaxSupportedFPS());
            setProperty("SupportFrameTimeChecker", "" + NexEditorDeviceProfile.getDeviceProfile().getSupportedTimeCheker());
            setProperty("DeviceMaxLightLevel", "" + NexEditorDeviceProfile.getDeviceProfile().getDeviceMaxLightLevel());
            setProperty("DeviceMaxGamma", "" + NexEditorDeviceProfile.getDeviceProfile().getDeviceMaxGamma());
        } else {
            setProperty("HardWareCodecMemSize", "8912896");
            setProperty("HardWareDecMaxCount", "4");
            setProperty("HardWareEncMaxCount", "1");
            setProperty("FeatureVersion", "3");
            setProperty("useNexEditorSDK", "1");
            setProperty("forceDirectExport", "" + NexEditorDeviceProfile.getDeviceProfile().getForceDirectExport());
            setProperty("DeviceExtendMode", "1");
            setProperty("InputMaxFPS", "120");
            setProperty("SupportFrameTimeChecker", "1");
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int max = Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
        int i5 = 1024;
        while (true) {
            int i6 = i5 * 2;
            if (i6 >= max) {
                break;
            }
            i5 = i6;
        }
        this.E = i5;
        this.F = i5;
        this.G = i5 * i5;
        setProperty("JpegMaxWidthFactor", "" + this.E);
        setProperty("JpegMaxHeightFactor", "" + this.E);
        setProperty("JpegMaxSizeFactor", "" + this.G);
        setProperty("UseAndroidJPEG", "1");
        setProperty("supportPeakMeter", "0");
        setProperty("skipPrefetchEffect", "0");
        Log.d("NexEditor.java", "largestDimension = " + max + " selectedSize=" + i5);
        NexThemeView nexThemeView2 = this.e;
        if (nexThemeView2 != nexThemeView) {
            if (nexThemeView2 != null) {
                nexThemeView2.linkToEditor(null);
            }
            nexThemeView.linkToEditor(this);
            this.e = nexThemeView;
        }
        NexEditorEventListener nexEditorEventListener = new NexEditorEventListener(this, context, com.nexstreaming.app.common.nexasset.assetpackage.c.a(context).e(), dVar);
        this.d = nexEditorEventListener;
        setEventHandler(nexEditorEventListener);
        try {
            AssetFileDescriptor openFd = context.getAssets().openFd("nexasset.jpg");
            this.S = setInputFile(openFd.getFileDescriptor(), 1, -1L, -1L);
            openFd.close();
            Log.d("NexEditor.java", "assetNativeFD is: " + this.S);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        String[] a2 = AssetPackageReader.a();
        if (a2.length <= 0) {
            return;
        }
        a(a2);
    }

    public int f() {
        return this.E;
    }

    public int g() {
        return this.F;
    }

    public int h() {
        return this.G;
    }

    public void i() {
        if (W == this) {
            Log.d("NexEditor.java", "destroy editor instance");
            m();
            destroyEditor();
        } else {
            Log.d("NexEditor.java", "skip destroying editor instance (not latest instance)");
        }
        int i2 = this.S;
        if (i2 >= 0) {
            closeInputFile(1, i2);
        }
        this.d = null;
        NexThemeView nexThemeView = this.e;
        if (nexThemeView != null) {
            nexThemeView.linkToEditor(null);
            this.e = null;
        }
        if (this.ac != null) {
            this.ac = null;
        }
    }

    public com.nexstreaming.kminternal.nexvideoeditor.d j() {
        if (this.U == null) {
            this.U = new com.nexstreaming.kminternal.nexvideoeditor.d(this);
        }
        return this.U;
    }

    public void a(com.nexstreaming.kminternal.nexvideoeditor.c cVar) {
        this.f = cVar;
        NexEditorEventListener nexEditorEventListener = this.d;
        if (nexEditorEventListener != null) {
            nexEditorEventListener.setUIListener(cVar);
        }
    }

    public void a(c cVar) {
        NexEditorEventListener nexEditorEventListener = this.d;
        if (nexEditorEventListener != null) {
            nexEditorEventListener.setCustomRenderCallback(cVar);
        }
    }

    public void a(NexThemeView nexThemeView) {
        NexThemeView nexThemeView2 = this.e;
        if (nexThemeView2 != nexThemeView) {
            Context context = null;
            if (nexThemeView2 != null) {
                nexThemeView2.linkToEditor(null);
            }
            if (nexThemeView != null) {
                nexThemeView.linkToEditor(this);
            }
            this.e = nexThemeView;
            NexEditorEventListener nexEditorEventListener = this.d;
            if (nexEditorEventListener == null) {
                return;
            }
            if (nexThemeView != null) {
                context = nexThemeView.getContext();
            }
            nexEditorEventListener.setContext(context);
        }
    }

    public NexThemeView k() {
        return this.e;
    }

    public synchronized int a(int i2, int i3, Rect rect, Rect rect2, Rect rect3) {
        return updateRenderInfo(i2, rect.left, rect.top, rect.right, rect.bottom, rect2.left, rect2.top, rect2.right, rect2.bottom, i3, rect3.left, rect3.top, rect3.right, rect3.bottom);
    }

    public int b(int i2) {
        return resetFaceDetectInfo(i2);
    }

    private String B() {
        return " m_seekSerial=" + this.aE + "; m_isSeeking=" + this.ay + "; m_isPendingSeek=" + this.az + "; m_pendingSeekLocation=" + this.aA + "; m_setTimeDoneListeners.size()=" + this.o.size();
    }

    private String C() {
        String str = "";
        int i2 = 0;
        for (p pVar : this.o) {
            i2++;
            String str2 = str + "\n     " + i2 + ": " + pVar + " m_reqTime=" + pVar.a + " m_serialNumber=" + pVar.b + " ";
            StackTraceElement[] stackTraceElementArr = pVar.e;
            if (stackTraceElementArr != null) {
                int i3 = 0;
                for (StackTraceElement stackTraceElement : stackTraceElementArr) {
                    String className = stackTraceElement.getClassName();
                    if (!className.equals("dalvik.system.VMStack") && !className.equals("java.lang.Thread") && !className.equals("dalvik.system.VMStack") && !className.equals("android.app.ActivityThread") && !className.equals("java.lang.reflect.Method") && !className.equals("com.android.internal.os.ZygoteInit") && !className.equals("com.android.internal.os.ZygoteInit$MethodAndArgsCaller:") && !className.equals("dalvik.system.NativeStart") && !className.equals("android.os.Looper")) {
                        str2 = str2 + "\n          " + i3 + ": " + className + "::" + stackTraceElement.getMethodName() + "(" + stackTraceElement.getFileName() + " " + stackTraceElement.getLineNumber() + ")";
                    }
                    i3++;
                }
            }
            str = str2;
        }
        return str;
    }

    public void a(m mVar) {
        this.X = mVar;
    }

    public void a(h hVar) {
        this.Y = hVar;
    }

    public void l() {
        m mVar = this.X;
        if (mVar != null) {
            mVar.a();
        }
    }

    public void m() {
        h hVar = this.Y;
        if (hVar != null) {
            hVar.a();
        }
    }

    public void b(int i2, int i3, int i4) {
        r rVar = this.Z;
        if (rVar != null) {
            rVar.a(i2, i3, i4);
        }
    }

    public void a(ErrorCode errorCode, int i2) {
        Log.d("NexEditor.java", "-  onTranscodingDone() result : " + errorCode + " mTranscoding =" + this.af);
        if (!this.af) {
            return;
        }
        this.af = false;
        r rVar = this.Z;
        if (rVar == null) {
            return;
        }
        rVar.a(errorCode, i2);
    }

    public void a(ErrorCode errorCode) {
        Task task;
        if (errorCode.isError()) {
            Log.d("NexEditor.java", "onFastOptionPreviewDone:" + errorCode.getMessage());
        }
        int i2 = this.I - 1;
        this.I = i2;
        String str = this.J;
        if (str == null || i2 >= 2) {
            if (i2 >= 1 || (task = this.aa) == null) {
                return;
            }
            task.signalEvent(Task.Event.SUCCESS, Task.Event.COMPLETE);
            this.aa = null;
        } else if (this.ay) {
            Log.d("NexEditor.java", "onFastOptionPreviewDone: stat is seeking");
            this.J = null;
        } else {
            int fastOptionPreview = fastOptionPreview(str, this.K);
            this.J = null;
            if (fastOptionPreview == 0) {
                this.I++;
                return;
            }
            Log.d("NexEditor.java", "onFastOptionPreviewDone: pending result=" + fastOptionPreview);
        }
    }

    public void b(ErrorCode errorCode) {
        Log.d("NexEditor.java", "onPlay:" + errorCode);
        this.b = true;
        if (this.q.size() < 1) {
            return;
        }
        o remove = this.q.remove();
        remove.a(errorCode);
        if (errorCode != ErrorCode.NONE) {
            return;
        }
        this.r.add(remove);
    }

    public void n() {
        this.ao = false;
        if (this.r.size() < 1) {
            return;
        }
        this.r.remove().a();
    }

    public void c(ErrorCode errorCode) {
        Log.d("NexEditor.java", "onStop : m_onStopListeners.size()=" + this.p.size());
        this.b = false;
        if (this.aq != null && errorCode.isError()) {
            this.aq.sendFailure(errorCode);
            this.aq = null;
        }
        while (true) {
            f poll = this.p.poll();
            if (poll != null) {
                poll.a(errorCode);
            } else {
                return;
            }
        }
    }

    public void d(ErrorCode errorCode) {
        if (this.aq != null) {
            if (errorCode.isError()) {
                this.aq.sendFailure(errorCode);
            } else {
                this.aq.signalEvent(Task.Event.SUCCESS, Task.Event.COMPLETE);
            }
            this.aq = null;
            E();
        }
    }

    public void b(ErrorCode errorCode, int i2) {
        this.z = i2;
        Log.d("NexEditor.java", "REACHED MARKER " + i2);
        while (true) {
            f peek = this.s.peek();
            if (peek == null) {
                Log.d("NexEditor.java", "    - onCommandMarker(" + errorCode + "," + i2 + "): Skipping because listener is null");
                return;
            } else if (peek.a > i2) {
                Log.d("NexEditor.java", "    - onCommandMarker(" + errorCode + "," + i2 + "): Skipping because " + peek.a + ">" + i2);
                return;
            } else {
                Log.d("NexEditor.java", "    - onCommandMarker(" + errorCode + "," + i2 + "): Notifying because " + peek.a + "<=" + i2);
                this.s.remove().a(errorCode);
            }
        }
    }

    public void a(k kVar) {
        this.ab = kVar;
    }

    public void a(int i2, int i3, int i4, int i5, int i6, byte[] bArr, int i7, int i8, int i9) {
        k kVar = this.ab;
        if (kVar != null) {
            kVar.a(i2, i3, i4, i5, i6, bArr, i7, i8, i9);
        }
    }

    public void a(l lVar) {
        this.ac = lVar;
    }

    public void a(int i2, int[] iArr) {
        l lVar = this.ac;
        if (lVar != null) {
            lVar.a(i2, iArr);
        }
    }

    public void c(int i2) {
        l lVar = this.ac;
        if (lVar != null) {
            lVar.a(i2);
        }
    }

    public void a(s sVar) {
        this.ad = sVar;
    }

    public void a(int i2, int[] iArr, int[] iArr2) {
        s sVar = this.ad;
        if (sVar != null) {
            sVar.a(i2, iArr, iArr2);
        }
    }

    public void d(int i2) {
        s sVar = this.ad;
        if (sVar != null) {
            sVar.a(i2);
        }
    }

    public void a(int i2, int i3, int i4, byte[] bArr, boolean z) {
        if (this.v.size() < 1) {
            return;
        }
        if (i2 < 1 || i3 < 1 || i4 < 1 || bArr == null) {
            e(ErrorCode.CAPTURE_FAILED);
        }
        try {
            e remove = this.v.remove();
            if (z) {
                int i5 = i2 * 4;
                byte[] bArr2 = new byte[i5];
                for (int i6 = 0; i6 < i3 / 2; i6++) {
                    int i7 = i2 * i6 * 4;
                    System.arraycopy(bArr, i7, bArr2, 0, i5);
                    int i8 = ((i3 - 1) - i6) * i2 * 4;
                    System.arraycopy(bArr, i8, bArr, i7, i5);
                    System.arraycopy(bArr2, 0, bArr, i8, i5);
                }
            }
            Bitmap createBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888);
            createBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bArr));
            remove.a(createBitmap);
        } catch (NoSuchElementException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public void e(ErrorCode errorCode) {
        if (this.v.size() < 1) {
            return;
        }
        this.v.remove().a(errorCode);
    }

    public boolean f(ErrorCode errorCode) {
        if (this.w.size() < 1) {
            return false;
        }
        this.w.remove().a(errorCode);
        this.aq = null;
        return true;
    }

    public boolean e(int i2) {
        if (this.w.size() < 1) {
            return false;
        }
        this.w.getFirst().a(i2);
        return true;
    }

    public boolean o() {
        if (this.w.size() < 1) {
            return false;
        }
        this.w.remove().a();
        this.aq = null;
        return true;
    }

    public void a(int i2, int i3, int i4, int i5) {
        if (i5 == 1 || i5 == 4 || i5 == 0) {
            Deque<d> deque = this.l;
            if (deque == null || deque.size() < 1) {
                Log.d("NexEditor.java", "Ignore onAddClipDone event -- no listeners");
                return;
            }
            for (d dVar : this.l) {
                if (dVar.a == i4) {
                    this.l.remove(dVar);
                    if (i2 == 0) {
                        dVar.a(i4, i3, i5);
                        return;
                    } else {
                        dVar.a(ErrorCode.fromValue(i2));
                        return;
                    }
                }
            }
        }
        if (i5 == 3 || i5 == 0) {
            Deque<d> deque2 = this.k;
            if (deque2 == null || deque2.size() < 1) {
                Log.d("NexEditor.java", "Ignore onAddClipDone event -- no listeners");
                return;
            }
            for (d dVar2 : this.k) {
                if (dVar2.a == i4) {
                    this.k.remove(dVar2);
                    if (i2 == 0) {
                        dVar2.a(i4, i3, i5);
                        return;
                    } else {
                        dVar2.a(ErrorCode.fromValue(i2));
                        return;
                    }
                }
            }
        }
    }

    public void f(int i2) {
        g remove = this.m.remove();
        if (i2 == 0) {
            remove.a();
        } else {
            remove.a(ErrorCode.fromValue(i2));
        }
    }

    public void a(int i2, int i3) {
        if (this.u.size() < 1 || this.n.size() < 1 || this.u.peek().intValue() != i3) {
            return;
        }
        this.u.remove();
        n remove = this.n.remove();
        if (i2 == 0) {
            remove.a(i3);
        } else {
            remove.a(ErrorCode.fromValue(i2));
        }
    }

    public int a(String str) {
        return removeBitmap(str);
    }

    public int a(String str, int[] iArr, int i2, int i3, int i4) {
        return pushLoadedBitmap(str, iArr, i2, i3, i4);
    }

    public int b(int i2, int i3) {
        return getTexNameForClipID(i2, i3);
    }

    public int g(int i2) {
        return getTexNameForMask(i2);
    }

    public int a(int i2, String str, int i3, int i4, int i5, int i6, float[] fArr, float f2, float f3, float f4, float f5, float f6) {
        return drawRenderItemOverlay(i2, str, i3, i4, i5, i6, fArr, f2, f3, f4, f5, f6);
    }

    public int a(String str, int i2) {
        return createRenderItem(str, i2);
    }

    public int c(int i2, int i3) {
        return releaseRenderItem(i2, i3);
    }

    public int h(int i2) {
        return setRenderToMask(i2);
    }

    public int i(int i2) {
        return setRenderToDefault(i2);
    }

    public int b(String str, int i2) {
        String property = getProperty(str);
        if (property == null) {
            return i2;
        }
        try {
            return Integer.parseInt(property);
        } catch (NumberFormatException unused) {
            return i2;
        }
    }

    public boolean a(String str, boolean z) {
        String property = getProperty(str);
        if (property == null) {
            return z;
        }
        if (property.trim().equalsIgnoreCase("true")) {
            return true;
        }
        if (!property.trim().equalsIgnoreCase("false")) {
            return z;
        }
        return false;
    }

    public void a(Surface surface) {
        this.L = surface;
        if (this.aj) {
            Log.d("NexEditor.java", "prepareSurface wait. image exporting...");
        } else if (this.ai) {
        } else {
            if (O) {
                prepareSurface(null);
            }
            if (surface != null) {
                setPreviewScaleFactor(this.aH);
            }
            prepareSurface(surface);
        }
    }

    public static String b(String str) {
        try {
            return getSystemProperty(str);
        } catch (UnsatisfiedLinkError unused) {
            ae = true;
            return "";
        }
    }

    public ErrorCode a(String str, String str2, int i2, int i3, int i4, int i5, int i6, long j2, int i7, int i8, String str3) {
        if (this.af) {
            return ErrorCode.TRANSCODING_BUSY;
        }
        ErrorCode fromValue = ErrorCode.fromValue(transcodingStart(str, str2, i2, i3, i4, i5, i6, j2, i7, i8, str3));
        if (!fromValue.isError()) {
            this.af = true;
        }
        return fromValue;
    }

    public ErrorCode p() {
        if (!this.af) {
            return ErrorCode.NO_ACTION;
        }
        ErrorCode fromValue = ErrorCode.fromValue(transcodingStop());
        fromValue.isError();
        return fromValue;
    }

    public void a(FastPreviewOption fastPreviewOption, int i2) {
        a(fastPreviewOption, i2, true);
    }

    public b q() {
        return new b() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.12
            private Map<FastPreviewOption, Integer> b = new EnumMap(FastPreviewOption.class);

            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.b
            public b a(FastPreviewOption fastPreviewOption, int i2) {
                this.b.put(fastPreviewOption, Integer.valueOf(i2));
                return this;
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.b
            public b a(Rect rect) {
                return a(FastPreviewOption.left, rect.left).a(FastPreviewOption.top, rect.bottom).a(FastPreviewOption.right, rect.right).a(FastPreviewOption.bottom, rect.top);
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.b
            public b a(boolean z) {
                return a(FastPreviewOption.swapv, z ? 1 : 0);
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.b
            public b a(int i2) {
                return a(FastPreviewOption.cts, i2);
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.b
            public b a(int i2, int i3) {
                return a(FastPreviewOption.video360flag, 1).a(FastPreviewOption.video360_horizontal, i2).a(FastPreviewOption.video360_vertical, i3);
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.b
            public void a() {
                b(true);
            }

            private void b(boolean z) {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<FastPreviewOption, Integer> entry : this.b.entrySet()) {
                    if (sb.length() > 0) {
                        sb.append(' ');
                    }
                    sb.append(entry.getKey().name());
                    sb.append('=');
                    sb.append(entry.getValue());
                }
                NexEditor.this.c(sb.toString(), z ? 1 : 0);
            }
        };
    }

    public void a(FastPreviewOption fastPreviewOption, int i2, boolean z) {
        if (this.aj) {
            return;
        }
        c(fastPreviewOption.name() + "=" + i2, z ? 1 : 0);
    }

    private void b(FastPreviewOption fastPreviewOption, int i2, boolean z) {
        c(fastPreviewOption.name() + "=" + i2, z ? 1 : 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int c(String str, int i2) {
        if (this.I >= 2) {
            this.J = str;
            this.K = i2;
            return 0;
        }
        int fastOptionPreview = fastOptionPreview(str, i2);
        if (fastOptionPreview == 0) {
            this.I++;
        }
        return fastOptionPreview;
    }

    public int r() {
        return clearScreen(1);
    }

    public ErrorCode a(e eVar) {
        return a(2147418113, eVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int b(e eVar) {
        int captureCurrentFrame = captureCurrentFrame();
        if (captureCurrentFrame == 0) {
            this.v.add(eVar);
        }
        return captureCurrentFrame;
    }

    public ErrorCode a(final int i2, final e eVar) {
        String str = "completing ";
        String str2 = "in-progress ";
        if (i2 == 2147418113) {
            StringBuilder sb = new StringBuilder();
            sb.append("Capture request : captureTime=CAPTURE_CURRENT t=");
            sb.append(this.A);
            sb.append(" ");
            if (!this.aj) {
                str2 = "ok ";
            }
            sb.append(str2);
            if (!this.ak) {
                str = "new ";
            }
            sb.append(str);
            Log.d("NexEditor.java", sb.toString());
            i2 = (this.az || this.ay) ? this.aA : this.A;
        } else if (i2 == 2147418114) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Capture request : captureTime=CAPTURE_CURRENT_NOFX ");
            if (!this.aj) {
                str2 = "ok ";
            }
            sb2.append(str2);
            if (!this.ak) {
                str = "new ";
            }
            sb2.append(str);
            Log.d("NexEditor.java", sb2.toString());
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Capture request : captureTime=");
            sb3.append(i2);
            sb3.append(" ");
            if (!this.aj) {
                str2 = "ok ";
            }
            sb3.append(str2);
            if (!this.ak) {
                str = "new ";
            }
            sb3.append(str);
            Log.d("NexEditor.java", sb3.toString());
        }
        if (this.aj) {
            return ErrorCode.INVALID_STATE;
        }
        this.aj = true;
        if (!this.ak) {
            this.al = (this.az || this.ay) ? this.aA : this.A;
        }
        Log.d("NexEditor.java", "  Capture mCaptureOriginalTime=" + this.al);
        final e eVar2 = new e() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.14
            public void a(ErrorCode errorCode, Bitmap bitmap) {
                NexEditor.this.ak = true;
                if (bitmap == null) {
                    Log.e("NexEditor.java", "  Capture failed; error=" + errorCode.getValue() + " (" + errorCode.name() + ")");
                    NexEditor.this.aj = false;
                    eVar.a(errorCode);
                } else {
                    Log.d("NexEditor.java", "  Capture success; notify listener");
                    NexEditor.this.aj = false;
                    eVar.a(bitmap);
                }
                NexEditor.this.ak = false;
                if (!NexEditor.this.aj) {
                    NexEditor.this.aj = true;
                    Log.d("NexEditor.java", "  Capture done; seek to original location");
                    NexEditor nexEditor = NexEditor.this;
                    nexEditor.a(nexEditor.al, false, new p() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.14.1
                        @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.p
                        public String a() {
                            return "seekToOriginalTimeAfterCapture";
                        }

                        @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.p
                        public void a(ErrorCode errorCode2) {
                            NexEditor.this.aj = false;
                        }

                        @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.p
                        public void a(int i3, int i4) {
                            NexEditor.this.aj = false;
                        }
                    });
                    return;
                }
                Log.d("NexEditor.java", "  Capture done; SKIP SEEK");
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.e
            public void a(ErrorCode errorCode) {
                Log.d("NexEditor.java", "  Capture onCaptureFail=" + errorCode.name());
                a(errorCode, null);
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.e
            public void a(Bitmap bitmap) {
                Log.d("NexEditor.java", "  Capture onCapture=" + bitmap);
                a(ErrorCode.NONE, bitmap);
            }
        };
        if (i2 == 2147418114) {
            Log.d("NexEditor.java", "  Capture CAPTURE_CURRENT_NOFX fastPreview");
            b(FastPreviewOption.nofx, 1, false);
            Log.d("NexEditor.java", "  Capture CAPTURE_CURRENT_NOFX captureCurrentFrame");
            b(eVar2);
            Log.d("NexEditor.java", "  Capture CAPTURE_CURRENT_NOFX out");
        } else {
            a(i2, false, new p() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.15
                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.p
                public String a() {
                    return "CAPTURE(" + i2 + ")";
                }

                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.p
                public void a(ErrorCode errorCode) {
                    Log.d("NexEditor.java", "  Capture onSetTimeFail=" + errorCode.name());
                    NexEditor.this.aj = false;
                    eVar.a(errorCode);
                }

                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.p
                public void a(int i3, int i4) {
                    Log.d("NexEditor.java", "  Capture onSetTimeDone=" + i3);
                    NexEditor.this.b(eVar2);
                }
            });
        }
        return ErrorCode.NONE;
    }

    public void s() {
        a(new f() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.16
            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.f
            public void a(ErrorCode errorCode) {
            }
        });
    }

    public void t() {
        a(0, new o() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.2
            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.o
            public void a() {
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.o
            public void a(ErrorCode errorCode) {
            }
        });
    }

    private void a(final int i2, final o oVar) {
        final int i3 = this.am + 1;
        this.am = i3;
        this.an = true;
        this.j = false;
        b(new f() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.3
            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.f
            public void a(ErrorCode errorCode) {
                if (!NexEditor.this.an) {
                    return;
                }
                NexEditor.this.an = false;
                if (NexEditor.this.am != i3) {
                    return;
                }
                NexEditor.this.ao = true;
                int startPlay = NexEditor.this.startPlay(i2);
                if (startPlay == 0) {
                    NexEditor.this.q.add(oVar);
                } else {
                    oVar.a(ErrorCode.fromValue(startPlay));
                }
            }
        });
    }

    public Task a(String str, int i2, int i3, int i4, long j2, int i5, boolean z, int i6, int i7) {
        Task task = new Task();
        if (this.aq != null) {
            task.sendFailure(ErrorCode.ALREADY_EXPORTING);
            return task;
        }
        int i8 = i5;
        this.ar = i8;
        if (!z) {
            i8 = 0;
        }
        int encodeProject = encodeProject(str, i2, i3, i4, j2, i8, 3000, 1920, 1080, i6, 131072, 0, 0, nexEngine.ExportCodec_AVC, i7);
        if (encodeProject != 0) {
            task.sendFailure(ErrorCode.fromValue(encodeProject));
            return task;
        }
        this.ap = null;
        this.aq = task;
        return task;
    }

    public Task a(String str, int i2, int i3, int i4, long j2, int i5, boolean z, int i6, int i7, int i8, int i9, int i10, int i11) {
        Task task = new Task();
        if (this.aq != null) {
            task.sendFailure(ErrorCode.ALREADY_EXPORTING);
            return task;
        }
        int i12 = i5;
        this.ar = i12;
        if (!z) {
            i12 = 0;
        }
        int encodeProject = encodeProject(str, i2, i3, i4, j2, i12, i9, 1920, 1080, i6, 131072, i7, i8, i10, i11);
        if (encodeProject != 0) {
            task.sendFailure(ErrorCode.fromValue(encodeProject));
            return task;
        }
        this.ap = null;
        this.aq = task;
        return task;
    }

    public void a(f fVar) {
        a(2, fVar);
    }

    public void a(int i2, f fVar) {
        if (this.an) {
            for (f fVar2 : this.t) {
                fVar2.a(ErrorCode.PLAY_SUPERCEEDED);
            }
            this.t.clear();
        }
        this.an = false;
        this.ao = false;
        if (!this.b && this.aq == null) {
            stopPlay(i2);
            fVar.a(ErrorCode.NONE);
            return;
        }
        Log.d("NexEditor.java", "stop flags(" + i2 + ") : " + fVar.toString());
        this.r.clear();
        this.p.add(fVar);
        int stopPlay = stopPlay(i2);
        Task task = this.aq;
        if (task != null) {
            task.signalEvent(Task.Event.CANCEL);
            this.aq = null;
        }
        if (stopPlay == 0) {
            return;
        }
        this.p.remove(fVar);
        fVar.a(ErrorCode.fromValue(stopPlay));
    }

    public ErrorCode a(int i2, int i3, int i4, final e eVar) {
        if (this.aj) {
            return ErrorCode.INVALID_STATE;
        }
        this.aj = true;
        if (!this.ak) {
            this.al = (this.az || this.ay) ? this.aA : this.A;
        }
        Log.d("NexEditor.java", "image Capture mCaptureOriginalTime=" + this.al);
        e eVar2 = new e() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.4
            public void a(ErrorCode errorCode, Bitmap bitmap) {
                if (NexEditor.this.as != null) {
                    NexEditor.this.as.close();
                    NexEditor.this.as = null;
                }
                NexEditor.this.ak = true;
                if (bitmap == null) {
                    Log.e("NexEditor.java", "image Capture failed; error=" + errorCode.getValue() + " (" + errorCode.name() + ")");
                    NexEditor.this.aj = false;
                    eVar.a(errorCode);
                } else {
                    Log.d("NexEditor.java", "image Capture success; notify listener");
                    NexEditor.this.aj = false;
                    eVar.a(bitmap);
                }
                NexEditor.this.ak = false;
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.e
            public void a(ErrorCode errorCode) {
                Log.d("NexEditor.java", "image Capture onCaptureFail=" + errorCode.name());
                NexEditor nexEditor = NexEditor.this;
                nexEditor.prepareSurface(nexEditor.L);
                if (NexEditor.this.as != null) {
                    NexEditor.this.as.close();
                    NexEditor.this.as = null;
                }
                a(errorCode, null);
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.e
            public void a(Bitmap bitmap) {
                Log.d("NexEditor.java", "image Capture onCapture=" + bitmap);
                a(ErrorCode.NONE, bitmap);
            }
        };
        if (this.as == null) {
            Log.d("NexEditor", "image Capture create ImageReader");
            this.as = ImageReader.newInstance(i2, i3, 1, 2);
        }
        this.as.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.5
            /* JADX WARN: Code restructure failed: missing block: B:17:0x008e, code lost:
                if (r2 != null) goto L6;
             */
            /* JADX WARN: Code restructure failed: missing block: B:18:0x0090, code lost:
                r2.close();
             */
            /* JADX WARN: Code restructure failed: missing block: B:19:0x0093, code lost:
                r13.close();
                r12.a.as = null;
             */
            /* JADX WARN: Code restructure failed: missing block: B:25:0x00ce, code lost:
                if (r2 == null) goto L7;
             */
            /* JADX WARN: Code restructure failed: missing block: B:27:0x00d1, code lost:
                return;
             */
            /* JADX WARN: Removed duplicated region for block: B:30:0x00d5  */
            @Override // android.media.ImageReader.OnImageAvailableListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void onImageAvailable(android.media.ImageReader r13) {
                /*
                    r12 = this;
                    java.lang.String r0 = "NexEditor"
                    java.lang.String r1 = "image Capture setOnImageAvailableListener jpeg == "
                    android.util.Log.d(r0, r1)
                    r1 = 0
                    android.media.Image r2 = r13.acquireLatestImage()     // Catch: java.lang.Throwable -> L9c java.lang.Exception -> L9f
                    if (r2 == 0) goto L8e
                    android.media.Image$Plane[] r3 = r2.getPlanes()     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    r4 = 0
                    r5 = r3[r4]     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    java.nio.ByteBuffer r5 = r5.getBuffer()     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    if (r5 != 0) goto L2e
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor r3 = com.nexstreaming.kminternal.nexvideoeditor.NexEditor.this     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor$ErrorCode r4 = com.nexstreaming.kminternal.nexvideoeditor.NexEditor.ErrorCode.UNKNOWN     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    r3.e(r4)     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    r2.close()
                    r13.close()
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor r13 = com.nexstreaming.kminternal.nexvideoeditor.NexEditor.this
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor.a(r13, r1)
                    return
                L2e:
                    int r5 = r2.getWidth()     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    int r6 = r2.getHeight()     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    r7 = r3[r4]     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    int r7 = r7.getRowStride()     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    int r8 = r5 * 4
                    int r9 = r5 * r6
                    int r9 = r9 * 4
                    byte[] r10 = new byte[r9]     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    r3 = r3[r4]     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    java.nio.ByteBuffer r3 = r3.getBuffer()     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                L4a:
                    if (r4 >= r6) goto L5b
                    int r11 = r5 * r4
                    int r11 = r11 * 4
                    r3.get(r10, r11, r8)     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    int r4 = r4 + 1
                    int r11 = r7 * r4
                    r3.position(r11)     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    goto L4a
                L5b:
                    java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    r3.<init>()     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    java.lang.String r4 = "image Capture prepareSurface = "
                    r3.append(r4)     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor r4 = com.nexstreaming.kminternal.nexvideoeditor.NexEditor.this     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    android.view.Surface r4 = com.nexstreaming.kminternal.nexvideoeditor.NexEditor.c(r4)     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    r3.append(r4)     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    java.lang.String r3 = r3.toString()     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    android.util.Log.d(r0, r3)     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor r3 = com.nexstreaming.kminternal.nexvideoeditor.NexEditor.this     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    android.view.Surface r4 = com.nexstreaming.kminternal.nexvideoeditor.NexEditor.c(r3)     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor.a(r3, r4)     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor r4 = com.nexstreaming.kminternal.nexvideoeditor.NexEditor.this     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    r3 = 0
                    r7 = r9
                    r8 = r10
                    r9 = r3
                    r4.a(r5, r6, r7, r8, r9)     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    r2.close()     // Catch: java.lang.Exception -> L8c java.lang.Throwable -> Ld2
                    r2 = r1
                    goto L8e
                L8c:
                    r3 = move-exception
                    goto La1
                L8e:
                    if (r2 == 0) goto L93
                L90:
                    r2.close()
                L93:
                    r13.close()
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor r13 = com.nexstreaming.kminternal.nexvideoeditor.NexEditor.this
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor.a(r13, r1)
                    goto Ld1
                L9c:
                    r0 = move-exception
                    r2 = r1
                    goto Ld3
                L9f:
                    r3 = move-exception
                    r2 = r1
                La1:
                    r3.printStackTrace()     // Catch: java.lang.Throwable -> Ld2
                    java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Ld2
                    r3.<init>()     // Catch: java.lang.Throwable -> Ld2
                    java.lang.String r4 = "image Capture prepareSurface(exception) = "
                    r3.append(r4)     // Catch: java.lang.Throwable -> Ld2
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor r4 = com.nexstreaming.kminternal.nexvideoeditor.NexEditor.this     // Catch: java.lang.Throwable -> Ld2
                    android.view.Surface r4 = com.nexstreaming.kminternal.nexvideoeditor.NexEditor.c(r4)     // Catch: java.lang.Throwable -> Ld2
                    r3.append(r4)     // Catch: java.lang.Throwable -> Ld2
                    java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> Ld2
                    android.util.Log.d(r0, r3)     // Catch: java.lang.Throwable -> Ld2
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor r0 = com.nexstreaming.kminternal.nexvideoeditor.NexEditor.this     // Catch: java.lang.Throwable -> Ld2
                    android.view.Surface r3 = com.nexstreaming.kminternal.nexvideoeditor.NexEditor.c(r0)     // Catch: java.lang.Throwable -> Ld2
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor.a(r0, r3)     // Catch: java.lang.Throwable -> Ld2
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor r0 = com.nexstreaming.kminternal.nexvideoeditor.NexEditor.this     // Catch: java.lang.Throwable -> Ld2
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor$ErrorCode r3 = com.nexstreaming.kminternal.nexvideoeditor.NexEditor.ErrorCode.UNKNOWN     // Catch: java.lang.Throwable -> Ld2
                    r0.e(r3)     // Catch: java.lang.Throwable -> Ld2
                    if (r2 == 0) goto L93
                    goto L90
                Ld1:
                    return
                Ld2:
                    r0 = move-exception
                Ld3:
                    if (r2 == 0) goto Ld8
                    r2.close()
                Ld8:
                    r13.close()
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor r13 = com.nexstreaming.kminternal.nexvideoeditor.NexEditor.this
                    com.nexstreaming.kminternal.nexvideoeditor.NexEditor.a(r13, r1)
                    throw r0
                */
                throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.AnonymousClass5.onImageAvailable(android.media.ImageReader):void");
            }
        }, null);
        if (encodeProjectJpeg(this.as.getSurface(), " ", i2, i3, 0, i4) == 0) {
            this.v.add(eVar2);
        }
        return ErrorCode.NONE;
    }

    static {
        StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            try {
                System.loadLibrary("nexeditorsdk");
            } catch (UnsatisfiedLinkError e2) {
                Log.e("NexEditor.java", "[NexEditor.java] nexeditor load failed : " + e2);
            }
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }

    public void u() {
        if (!this.aj || this.av) {
            return;
        }
        this.av = true;
        Log.d("NexEditor.java", "  exportImageFormatCancel...");
    }

    public ErrorCode a(int i2, int i3, final int i4, final int i5, final int i6, v vVar) {
        synchronized (this.ax) {
            if (this.aj) {
                Log.d("NexEditor.java", "exportImageFormat already exporting");
                return ErrorCode.INVALID_STATE;
            }
            this.aj = true;
            if (vVar == null) {
                Log.d("NexEditor.java", "exportImageFormat ExportImageCollback is null ");
                this.aj = false;
                return ErrorCode.INVALID_STATE;
            }
            this.aw = vVar;
            if (!this.ak) {
                this.al = (this.az || this.ay) ? this.aA : this.A;
            }
            Log.d("NexEditor.java", "  exportImageFormat mCaptureOriginalTime=" + this.al);
            this.au.poll();
            this.au.poll();
            this.au.poll();
            if (this.as == null) {
                this.as = ImageReader.newInstance(i2, i3, 1, 2);
            }
            HandlerThread handlerThread = new HandlerThread("exportImageFormat");
            handlerThread.start();
            Handler handler = new Handler(handlerThread.getLooper());
            Log.d("NexEditor.java", "  exportImageFormat 1");
            this.as.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.6
                public int a = 0;

                @Override // android.media.ImageReader.OnImageAvailableListener
                public void onImageAvailable(ImageReader imageReader) {
                    int i7;
                    Image acquireLatestImage;
                    try {
                        acquireLatestImage = imageReader.acquireLatestImage();
                    } catch (Exception e2) {
                        Log.d("exportImageFormat", "Exception");
                        e2.printStackTrace();
                        i7 = -5;
                    }
                    if (acquireLatestImage != null) {
                        Image.Plane[] planes = acquireLatestImage.getPlanes();
                        boolean z = false;
                        if (planes[0].getBuffer() != null) {
                            int width = acquireLatestImage.getWidth();
                            int height = acquireLatestImage.getHeight();
                            int rowStride = planes[0].getRowStride();
                            int i8 = width * 4;
                            int i9 = rowStride - i8;
                            byte[] bArr = new byte[width * height * 4];
                            ByteBuffer buffer = planes[0].getBuffer();
                            int i10 = 0;
                            while (i10 < height) {
                                buffer.get(bArr, width * i10 * 4, i8);
                                i10++;
                                buffer.position(rowStride * i10);
                            }
                            if (!NexEditor.this.av) {
                                z = NexEditor.this.aw.a(bArr, 0);
                            }
                            Log.d("exportImageFormat", "onImageAvailable(" + acquireLatestImage.getTimestamp() + ") width=" + width + ", height=" + height + ", rowStride=" + rowStride + ", padding=" + i9 + ", format=" + acquireLatestImage.getFormat());
                            acquireLatestImage.close();
                            i7 = !z ? -2 : 1;
                        } else {
                            i7 = -3;
                        }
                        if (NexEditor.this.av) {
                            i7 = -1;
                        }
                        try {
                            NexEditor.this.au.put(Integer.valueOf(i7));
                            return;
                        } catch (InterruptedException e3) {
                            e3.printStackTrace();
                            return;
                        }
                    }
                    Log.d("exportImageFormat", "Latest image is null. Ignore it.");
                }
            }, handler);
            Log.d("NexEditor.java", "  exportImageFormat 2");
            new AsyncTask<Void, Integer, Integer>() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.7
                public int a = 0;
                public int b = 0;

                private int a() {
                    boolean z = false;
                    int i7 = 0;
                    while (true) {
                        if (i7 >= 100) {
                            break;
                        } else if (!NexEditor.this.ay) {
                            z = true;
                            break;
                        } else {
                            try {
                                Thread.sleep(10L, 0);
                            } catch (InterruptedException e2) {
                                e2.printStackTrace();
                            }
                            i7++;
                        }
                    }
                    if (z || !NexEditor.this.ay) {
                        return 1;
                    }
                    Log.d("exportImageFormat", "wait waitSeekDone() fail!");
                    return -6;
                }

                @Override // android.os.AsyncTask
                /* renamed from: a */
                public Integer doInBackground(Void... voidArr) {
                    ErrorCode fromValue;
                    Log.d("NexEditor.java", "  exportImageFormat doInBackground");
                    NexEditor.this.aw.b(0);
                    int a2 = a();
                    if (a2 == 1) {
                        if (ErrorCode.fromValue(NexEditor.this.setGIFMode(1)) != ErrorCode.NONE) {
                            NexEditor.this.aj = false;
                            Log.d("NexEditor.java", "exportImageFormat setGIFMode failed width " + fromValue.toString());
                            return -7;
                        }
                        int b2 = NexEditor.this.aw.b();
                        int i7 = 0;
                        while (true) {
                            int b3 = NexEditor.this.aw.b();
                            Log.d("exportImageFormat", "waitCount=" + b3);
                            if (b3 != 0) {
                                try {
                                    Thread.sleep(500L, 0);
                                } catch (InterruptedException e2) {
                                    e2.printStackTrace();
                                }
                                if (b2 != b3) {
                                    this.b++;
                                    NexEditor.this.aw.b(this.b);
                                    i7 = 0;
                                    b2 = b3;
                                } else {
                                    i7++;
                                    if (i7 > 10) {
                                        Log.d("exportImageFormat", "waitCount Timeout=" + b2);
                                        return -6;
                                    }
                                }
                            } else {
                                int i8 = this.b;
                                int i9 = 100 - i8;
                                if (i9 <= 0) {
                                    i8 = 0;
                                    i9 = 100;
                                }
                                NexEditor.this.prepareSurface(NexEditor.this.as.getSurface());
                                int i10 = i5;
                                while (true) {
                                    if (i10 >= i6) {
                                        break;
                                    }
                                    Log.d("exportImageFormat", "seek start=" + i10);
                                    NexEditor.this.j(i10);
                                    try {
                                        Integer num = (Integer) NexEditor.this.au.poll(AbstractComponentTracker.LINGERING_TIMEOUT, TimeUnit.MILLISECONDS);
                                        a2 = num == null ? 0 : num.intValue();
                                    } catch (InterruptedException e3) {
                                        e3.printStackTrace();
                                    }
                                    if (a2 <= 0) {
                                        Log.d("exportImageFormat", "val=" + a2);
                                        break;
                                    }
                                    a2 = a();
                                    if (a2 != 1) {
                                        Log.d("exportImageFormat", "wait val=" + a2);
                                        break;
                                    }
                                    int i11 = i5;
                                    int i12 = (((i10 - i11) * i9) / (i6 - i11)) + i8;
                                    if (this.b != i12) {
                                        this.b = i12;
                                        NexEditor.this.aw.b(this.b);
                                    }
                                    i10 += i4;
                                }
                                if (NexEditor.this.as != null) {
                                    NexEditor.this.as.setOnImageAvailableListener(null, null);
                                }
                                NexEditor.this.aw.a();
                                if (this.b != 100) {
                                    NexEditor.this.aw.b(100);
                                }
                                return Integer.valueOf(a2);
                            }
                        }
                    } else {
                        return Integer.valueOf(a2);
                    }
                }

                @Override // android.os.AsyncTask
                /* renamed from: a */
                public void onPostExecute(Integer num) {
                    Log.d("NexEditor.java", "  exportImageFormat onPostExecute");
                    this.a = num.intValue() - 1;
                    NexEditor.this.setGIFMode(0);
                    NexEditor nexEditor = NexEditor.this;
                    nexEditor.prepareSurface(nexEditor.L);
                    if (NexEditor.this.as != null) {
                        NexEditor.this.as.close();
                        NexEditor.this.as = null;
                    }
                    NexEditor.this.aj = false;
                    NexEditor.this.av = false;
                    NexEditor.this.aw.a(this.a);
                }
            }.executeOnExecutor(at, new Void[0]);
            Log.d("NexEditor.java", "  exportImageFormat 3");
            return ErrorCode.NONE;
        }
    }

    public void j(int i2) {
        c(i2, (p) null);
    }

    private void b(f fVar) {
        Log.d("NexEditor.java", "setTimeCancel m_isPendingSeek: " + this.az + " -> false");
        this.az = false;
        this.aC = false;
        if (!this.ay) {
            fVar.a(ErrorCode.NONE);
        } else if (fVar == null) {
        } else {
            this.t.add(fVar);
        }
    }

    public void a(final int i2, final p pVar) {
        b(new f() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.8
            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.f
            public void a(ErrorCode errorCode) {
                NexEditor.this.b(i2, pVar);
            }
        });
    }

    public void b(int i2, p pVar) {
        a(i2, true, 4, pVar);
    }

    public void c(int i2, p pVar) {
        a(i2, true, 0, pVar);
    }

    public void d(int i2, p pVar) {
        a(i2, true, 1, pVar);
    }

    public void e(int i2, p pVar) {
        a(i2, true, 2, pVar);
    }

    public void a(int i2, boolean z, p pVar) {
        a(i2, z, 0, pVar);
    }

    private void a(int i2, boolean z, int i3, p pVar) {
        StringBuilder sb = new StringBuilder();
        sb.append("in seek(display=");
        sb.append(z);
        sb.append(",");
        sb.append(i2);
        sb.append(",");
        sb.append(i3);
        sb.append(",");
        sb.append(pVar == null ? "null" : pVar);
        sb.append(") m_seekSerial=");
        sb.append(this.aE);
        sb.append(" seeking=");
        sb.append(this.ay);
        sb.append("; pendingSeek=");
        sb.append(this.az);
        sb.append("; pendingSeekLocation=");
        sb.append(this.aA);
        Log.d("NexEditor.java", sb.toString());
        if (pVar != null) {
            pVar.a = i2;
            pVar.b = this.aE;
        }
        if (!this.ay) {
            this.j = true;
            f(true);
            int time = setTime(i2, z ? 1 : 0, i3);
            if (time != 0) {
                Log.d("NexEditor.java", "setTime ERROR RETURN: " + time);
                f(false);
                if (pVar == null) {
                    return;
                }
                pVar.a(ErrorCode.fromValue(time));
                return;
            }
            Log.d("NexEditor.java", "setTime SEEK STARTED: " + time);
            this.aA = i2;
            if (z) {
                Log.d("NexEditor.java", "seek_internal[display]: m_isPendingSeek=" + this.az + " -> false");
                this.az = false;
            } else {
                this.aC = false;
            }
            this.aE++;
        } else if (z) {
            Log.d("NexEditor.java", "seek_internal: m_isPendingSeek=" + this.az + " -> true, flag=" + i3);
            this.az = true;
            this.aA = i2;
            this.aB = i3;
        } else {
            this.aC = true;
            this.aD = i2;
        }
        if (pVar != null) {
            this.o.add(pVar);
            Log.d("NexEditor.java", "m_setTimeDoneListeners - Added listener");
            D();
        }
    }

    private void D() {
        int i2 = 0;
        for (p pVar : this.o) {
            Log.d("NexEditor.java", "     " + i2 + ": t=" + pVar.a + " sn=" + pVar.b + " " + pVar.a() + "    " + pVar);
            i2++;
        }
    }

    public void d(int i2, int i3) {
        a(i2, i3, ErrorCode.NONE);
    }

    public void k(int i2) {
        Task task = this.aq;
        if (task != null) {
            task.setProgress(Math.min(this.ar, i2), this.ar);
        }
        this.A = i2;
    }

    public void a(int i2, int i3, ErrorCode errorCode) {
        this.A = i2;
        this.B = i3;
        Log.d("NexEditor.java", "in onSetTimeDone(" + i2 + "," + i3 + ") " + B() + " " + C());
        LinkedList<p> linkedList = new LinkedList();
        for (p pVar : this.o) {
            if (pVar.b < this.aE) {
                linkedList.add(pVar);
            }
        }
        for (p pVar2 : linkedList) {
            Log.d("NexEditor.java", "  - START notify listener : " + pVar2 + pVar2.a() + pVar2);
            if (errorCode == ErrorCode.NONE) {
                pVar2.a(i2, i3);
            } else {
                pVar2.a(errorCode);
            }
            Log.d("NexEditor.java", "  - DONE notify listener : " + pVar2.a() + " " + pVar2);
        }
        Log.d("NexEditor.java", "(removing " + linkedList.size() + " listeners)");
        this.o.removeAll(linkedList);
        D();
        f(false);
        Log.d("NexEditor.java", "(seek state set to FALSE)");
        if (this.t.size() > 0) {
            Log.d("NexEditor.java", "onSetTimeDone [m_onSetTimeCancelListeners>0]: m_isPendingSeek=" + this.az + " -> false");
            this.az = false;
            this.aC = false;
            while (this.t.size() > 0) {
                this.t.remove().a(ErrorCode.NONE);
            }
        }
        if (this.aC) {
            Log.d("NexEditor.java", "execute pending non-display seek : " + this.aD);
            this.aC = false;
            a(this.aD, false, 0, (p) null);
        } else if (this.az) {
            Log.d("NexEditor.java", "execute pending seek : " + this.aA + ", m_pendingSeekIDR=" + this.aB);
            this.az = false;
            int i4 = this.aB;
            if (i4 == 0) {
                j(this.aA);
            } else if (i4 == 1) {
                d(this.aA, (p) null);
            } else if (i4 == 4) {
                b(this.aA, (p) null);
            } else {
                e(this.aA, (p) null);
            }
        }
        Log.d("NexEditor.java", "out onSetTimeDone(" + i2 + "," + i3 + ") " + B() + " " + this.o.size());
    }

    public void v() {
        a(0, 0, ErrorCode.SET_TIME_IGNORED);
    }

    private void f(boolean z) {
        if (z == this.ay) {
            return;
        }
        this.ay = z;
        com.nexstreaming.kminternal.nexvideoeditor.c cVar = this.f;
        if (cVar == null) {
            return;
        }
        cVar.a(z);
    }

    public int a(int i2, g gVar) {
        if (gVar == null) {
            gVar = new g() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditor.11
                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.g
                public void a() {
                }

                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.g
                public void a(ErrorCode errorCode) {
                }
            };
        }
        int deleteClipID = deleteClipID(i2);
        if (deleteClipID == 0) {
            this.m.add(gVar);
        }
        return deleteClipID;
    }

    public void b(String str, boolean z) {
        loadTheme(String.format("", new Object[0]), str, !z ? 1 : 0);
    }

    public void a(boolean z) {
        clearRenderItems(!z ? 1 : 0);
    }

    public void a(String str, String str2, boolean z) {
        loadRenderItem(str, str2, !z ? 1 : 0);
    }

    public ErrorCode a(String str, NexClipInfo nexClipInfo, boolean z, int i2) {
        return ErrorCode.fromValue(getClipInfoSync(str, nexClipInfo, z ? 1 : 0, i2));
    }

    public ErrorCode a(String str, NexClipInfo nexClipInfo, int i2, int i3) {
        return ErrorCode.fromValue(getClipInfoSync(str, nexClipInfo, i2, i3));
    }

    public void a(j jVar) {
        this.aG = jVar;
    }

    public void c(ErrorCode errorCode, int i2) {
        Log.d("NexEditor.java", "onGetClipInfoDone onGetClipInfoDone=" + errorCode + " tag=" + i2);
        j jVar = this.aG;
        if (jVar != null) {
            jVar.a(errorCode, i2);
        }
    }

    public void a(q qVar) {
        this.x = qVar;
    }

    public void w() {
        q qVar = this.x;
        if (qVar != null) {
            qVar.a();
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class p {
        private int a;
        private int b;
        public StackTraceElement[] e;

        public abstract String a();

        public abstract void a(int i, int i2);

        public abstract void a(ErrorCode errorCode);
    }

    /* loaded from: classes3.dex */
    public static abstract class f {
        private int a;

        public abstract void a(ErrorCode errorCode);
    }

    /* loaded from: classes3.dex */
    public enum PlayState {
        NONE(0),
        IDLE(1),
        RUN(2),
        RECORD(3),
        PAUSE(4),
        RESUME(5);
        
        private int mValue;

        PlayState(int i) {
            this.mValue = i;
        }

        public int getValue() {
            return this.mValue;
        }

        public static PlayState fromValue(int i) {
            PlayState[] values;
            for (PlayState playState : values()) {
                if (playState.getValue() == i) {
                    return playState;
                }
            }
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public enum ErrorCode implements Task.TaskError {
        NONE(0),
        GENERAL(1),
        UNKNOWN(2),
        NO_ACTION(3),
        INVALID_INFO(4),
        INVALID_STATE(5),
        VERSION_MISMATCH(6),
        CREATE_FAILED(7),
        MEMALLOC_FAILED(8, "Memory allocation failed", 0),
        ARGUMENT_FAILED(9),
        NOT_ENOUGH_NEMORY(10, "Insufficient memory", 0),
        EVENTHANDLER(11),
        FILE_IO_FAILED(12, "Error accessing file", 0),
        FILE_INVALID_SYNTAX(13),
        FILEREADER_CREATE_FAIL(14, "Could not open file", 0),
        FILEWRITER_CREATE_FAIL(15),
        AUDIORESAMPLER_CREATE_FAIL(16),
        UNSUPPORT_FORMAT(17, "Unsupported format", 0),
        FILEREADER_FAILED(18, "Error reading file format", 0),
        PLAYSTART_FAILED(19),
        PLAYSTOP_FAILED(20),
        PROJECT_NOT_CREATE(21),
        PROJECT_NOT_OPEN(22),
        CODEC_INIT(23, "Codec init failed", 0),
        RENDERER_INIT(24),
        THEMESET_CREATE_FAIL(25),
        ADD_CLIP_FAIL(26, "Unable to add clip", 0),
        ENCODE_VIDEO_FAIL(27),
        INPROGRESS_GETCLIPINFO(28),
        THUMBNAIL_BUSY(29),
        UNSUPPORT_MIN_DURATION(30),
        UNSUPPORT_MAX_RESOLUTION(31),
        UNSUPPORT_MIN_RESOLUTION(32),
        UNSUPPORT_VIDEIO_PROFILE(33),
        UNSUPPORT_VIDEO_LEVEL(34),
        UNSUPPORT_VIDEO_FPS(35),
        TRANSCODING_BUSY(36),
        TRANSCODING_NOT_SUPPORTED_FORMAT(37),
        TRANSCODING_USER_CANCEL(38),
        TRANSCODING_NOT_ENOUGHT_DISK_SPACE(39),
        TRANSCODING_CODEC_FAILED(40),
        EXPORT_WRITER_INVAILED_HANDLE(41),
        EXPORT_WRITER_INIT_FAIL(42),
        EXPORT_WRITER_START_FAIL(43),
        EXPORT_AUDIO_DEC_INIT_FAIL(44),
        EXPORT_VIDEO_DEC_INIT_FAIL(45),
        EXPORT_VIDEO_ENC_FAIL(46),
        EXPORT_VIDEO_RENDER_INIT_FAIL(47),
        EXPORT_NOT_ENOUGHT_DISK_SPACE(48, "Not enough space", 0),
        UNSUPPORT_AUDIO_PROFILE(49),
        THUMBNAIL_INIT_FAIL(50),
        UNSUPPORT_AUDIO_CODEC(51),
        UNSUPPORT_VIDEO_CODEC(52),
        HIGHLIGHT_FILEREADER_INIT_ERROR(53),
        HIGHLIGHT_TOO_SHORT_CONTENTS(54),
        HIGHLIGHT_CODEC_INIT_ERROR(55),
        HIGHLIGHT_CODEC_DECODE_ERROR(56),
        HIGHLIGHT_RENDER_INIT_ERROR(57),
        HIGHLIGHT_WRITER_INIT_ERROR(58),
        HIGHLIGHT_WRITER_WRITE_ERROR(59),
        HIGHLIGHT_GET_INDEX_ERROR(60),
        HIGHLIGHT_USER_CANCEL(61),
        GETCLIPINFO_USER_CANCEL(62),
        DIRECTEXPORT_CLIPLIST_ERROR(63),
        DIRECTEXPORT_CHECK_ERROR(64),
        DIRECTEXPORT_FILEREADER_INIT_ERROR(65),
        DIRECTEXPORT_FILEWRITER_INIT_ERROR(66),
        DIRECTEXPORT_DEC_INIT_ERROR(67),
        DIRECTEXPORT_DEC_INIT_SURFACE_ERROR(68),
        DIRECTEXPORT_DEC_DECODE_ERROR(69),
        DIRECTEXPORT_ENC_INIT_ERROR(70),
        DIRECTEXPORT_ENC_ENCODE_ERROR(71),
        DIRECTEXPORT_ENC_INPUT_SURFACE_ERROR(72),
        DIRECTEXPORT_ENC_FUNCTION_ERROR(73),
        DIRECTEXPORT_ENC_DSI_DIFF_ERROR(74),
        DIRECTEXPORT_ENC_FRAME_CONVERT_ERROR(75),
        DIRECTEXPORT_RENDER_INIT_ERROR(76),
        DIRECTEXPORT_WRITER_WRITE_ERROR(77),
        DIRECTEXPORT_WRITER_UNKNOWN_ERROR(78),
        FASTPREVIEW_USER_CANCEL(79),
        FASTPREVIEW_CLIPLIST_ERROR(80),
        FASTPREVIEW_FIND_CLIP_ERROR(81),
        FASTPREVIEW_FIND_READER_ERROR(82),
        FASTPREVIEW_VIDEO_RENDERER_ERROR(83),
        FASTPREVIEW_DEC_INIT_SURFACE_ERROR(84),
        HW_NOT_ENOUGH_MEMORY(85),
        EXPORT_USER_CANCEL(86),
        FASTPREVIEW_DEC_INIT_ERROR(87),
        FASTPREVIEW_FILEREADER_INIT_ERROR(88),
        FASTPREVIEW_TIME_ERROR(89),
        FASTPREVIEW_RENDER_INIT_ERROR(90),
        FASTPREVIEW_OUTPUTSURFACE_INIT_ERROR(91),
        FASTPREVIEW_BUSY(92),
        CODEC_DECODE(93, "Codec decode failed", 0),
        RENDERER_AUDIO(94),
        UNSUPPORT_AUDIO_SAMPLINGRATE(95),
        IMAGE_PROCESS(4097),
        SET_TIME_IGNORED("Set time ignored", 0),
        SET_TIME_CANCELED("Set time canceled", 0),
        CAPTURE_FAILED("Capture failed", 0),
        SOURCE_FILE_NOT_FOUND,
        TRANSCODING_ABORTED,
        DESTINATION_FILE_ALREADY_EXISTS,
        TEMP_FILE_ALREADY_EXISTS,
        NO_INSTANCE_AVAILABLE,
        EXPORT_NO_SUCCESS,
        PLAY_SUPERCEEDED,
        WRAPPER_BUSY,
        NOT_READY_TO_PLAY,
        SEEKING_LOCKED,
        NO_PROJECT_LOADED,
        ALREADY_EXPORTING,
        EMPTY_PROJECT,
        MISSING_RESOURCES,
        EXPORT_UNEXPECTED_STOP,
        RENAME_FAIL,
        CAPTURE_FAIL_ENOSPC("Not enough space", 0),
        CAPTURE_FAIL_OTHER("Capture failed", 0),
        CAPTURE_FAIL_SCANNING("Media scanner failed", 0),
        UNRECOGNIZED_ERROR_CODE("Unrecognized error code", 0),
        EDITOR_INSTANCE_DESTROYED,
        FILE_MISSING;
        
        private final String mDescription;
        private final int mDescriptionRsrcId;
        private final boolean mFromEngine;
        private final int mValue;

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public Exception getException() {
            return null;
        }

        public boolean isError() {
            return this != NONE;
        }

        ErrorCode(int i) {
            this.mValue = i;
            this.mDescription = null;
            this.mDescriptionRsrcId = 0;
            this.mFromEngine = true;
        }

        ErrorCode(int i, String str, int i2) {
            this.mValue = i;
            this.mDescription = str;
            this.mDescriptionRsrcId = i2;
            this.mFromEngine = true;
        }

        ErrorCode(String str, int i) {
            this.mValue = 0;
            this.mDescription = str;
            this.mDescriptionRsrcId = i;
            this.mFromEngine = false;
        }

        ErrorCode() {
            this.mValue = 0;
            this.mDescription = null;
            this.mDescriptionRsrcId = 0;
            this.mFromEngine = false;
        }

        public int getValue() {
            return this.mValue;
        }

        public String getDescription() {
            String str = this.mDescription;
            return str == null ? name() : str;
        }

        public String getLocalizedDescription(Context context) {
            if (this.mDescriptionRsrcId != 0) {
                return context.getResources().getString(this.mDescriptionRsrcId);
            }
            String str = this.mDescription;
            return str == null ? name() : str;
        }

        public static ErrorCode fromValue(int i) {
            ErrorCode[] values;
            for (ErrorCode errorCode : values()) {
                if (errorCode.mFromEngine && errorCode.getValue() == i) {
                    return errorCode;
                }
            }
            Log.e("NexEditor.java", "Unrecognized error code : " + i);
            return UNRECOGNIZED_ERROR_CODE;
        }

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public String getMessage() {
            return getDescription();
        }

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public String getLocalizedMessage(Context context) {
            return getLocalizedDescription(context);
        }

        @Override // java.lang.Enum
        public String toString() {
            if (this == NONE) {
                return "NONE(0)";
            }
            if (this.mValue != 0) {
                return name() + "(" + this.mValue + ")";
            }
            return name();
        }
    }

    public ErrorCode a(String str, File file, int i2, int i3, int i4, int i5, int i6, int i7, int i8) throws IOException {
        return ErrorCode.fromValue(getClipVideoThumb(str, file.getAbsolutePath(), i2, i3, i4, i5, i6, i7, i8));
    }

    public ErrorCode a(String str, File file, int i2, int i3, int i4, int[] iArr, int i5, int i6) throws IOException {
        return ErrorCode.fromValue(getClipVideoThumbWithTimeTable(str, file.getAbsolutePath(), i2, i3, i4, iArr, i5, i6));
    }

    public ErrorCode a(String str, File file, int i2) {
        return ErrorCode.fromValue(getClipAudioThumb(str, file.getAbsolutePath(), i2));
    }

    public ErrorCode l(int i2) {
        return ErrorCode.fromValue(getClipStopThumb(i2));
    }

    public int a(NexVisualClip[] nexVisualClipArr, NexAudioClip[] nexAudioClipArr, int i2) {
        int i3;
        int i4;
        int i5;
        if (nexVisualClipArr != null) {
            i3 = 0;
            for (NexVisualClip nexVisualClip : nexVisualClipArr) {
                if (nexVisualClip != null && (i5 = nexVisualClip.mClipID) > i3) {
                    i3 = i5;
                }
            }
        } else {
            i3 = 0;
        }
        if (nexAudioClipArr != null) {
            for (NexAudioClip nexAudioClip : nexAudioClipArr) {
                if (nexAudioClip != null && (i4 = nexAudioClip.mClipID) > i3) {
                    i3 = i4;
                }
            }
        }
        this.a = i3 + 1;
        return loadList(nexVisualClipArr, nexAudioClipArr, i2);
    }

    public int b(NexVisualClip[] nexVisualClipArr, NexAudioClip[] nexAudioClipArr, int i2) {
        int i3;
        int i4;
        int i5;
        if (nexVisualClipArr != null) {
            i3 = 0;
            for (NexVisualClip nexVisualClip : nexVisualClipArr) {
                if (nexVisualClip != null && (i5 = nexVisualClip.mClipID) > i3) {
                    i3 = i5;
                }
            }
        } else {
            i3 = 0;
        }
        if (nexAudioClipArr != null) {
            for (NexAudioClip nexAudioClip : nexAudioClipArr) {
                if (nexAudioClip != null && (i4 = nexAudioClip.mClipID) > i3) {
                    i3 = i4;
                }
            }
        }
        this.a = i3 + 1;
        return asyncLoadList(nexVisualClipArr, nexAudioClipArr, i2);
    }

    public boolean x() {
        return this.ay;
    }

    public void b(boolean z) {
        NexEditorEventListener nexEditorEventListener = this.d;
        if (nexEditorEventListener != null) {
            nexEditorEventListener.setSyncMode(z);
        }
    }

    public int y() {
        return getNativeSDKInfoWM();
    }

    public static void c(String str) {
        setPacakgeName4Protection(str);
    }

    public static void a(String[] strArr) {
        setEncInfo(strArr);
    }

    public int a(String str, int i2, int i3, int i4, int i5, int i6, int i7) {
        return highlightStart(str, i2, i3, i4, 0, "dummy", 0, 0, 0, Long.MAX_VALUE, i5, i6, i7);
    }

    public int z() {
        return highlightStop();
    }

    private void E() {
        String str = this.ap;
        if (str == null || !str.contains("writefd://")) {
            return;
        }
        closeOutputFile(Integer.parseInt(this.ap.substring(10)));
        this.ap = null;
    }

    public void a(int i2, byte[] bArr) {
        setVideoTrackUUID(i2, bArr);
    }

    public void a(i iVar) {
        if (iVar == null) {
            this.w.clear();
        } else {
            this.w.add(iVar);
        }
    }

    public static void c(boolean z) {
        O = z;
    }

    public int d(boolean z) {
        return getAudioSessionID();
    }

    public boolean e(int i2, int i3) {
        set360VideoTrackPosition(i2, i3, 1);
        return true;
    }

    public boolean A() {
        set360VideoTrackPosition(0, 0, 0);
        return true;
    }

    public int m(int i2) {
        return releaseLUTTexture(i2);
    }

    public int[] a(byte[] bArr, int i2, int i3) {
        return createLGLUT(bArr);
    }

    public int[] b(byte[] bArr, int i2, int i3) {
        return createCubeLUT(bArr);
    }

    public void e(boolean z) {
        NexEditorEventListener nexEditorEventListener = this.d;
        if (nexEditorEventListener != null) {
            nexEditorEventListener.setWatermark(z);
        }
    }

    public boolean a(float f2) {
        if (f2 <= 0.0f || f2 > 1.0f) {
            return false;
        }
        this.aH = f2;
        return true;
    }

    public int[] c(byte[] bArr, int i2, int i3) {
        int i4 = i2 * i3;
        int[] iArr = new int[i4];
        int i5 = 0;
        for (int i6 = 0; i6 < i3; i6++) {
            int i7 = ((i6 >> 1) * i2) + i4;
            int i8 = 0;
            int i9 = 0;
            int i10 = 0;
            while (i8 < i2) {
                int i11 = (bArr[i5] & 255) - 16;
                if (i11 < 0) {
                    i11 = 0;
                }
                if ((i8 & 1) == 0) {
                    int i12 = i7 + 1;
                    int i13 = i12 + 1;
                    int i14 = (bArr[i12] & 255) - 128;
                    i9 = (bArr[i7] & 255) - 128;
                    i7 = i13;
                    i10 = i14;
                }
                int i15 = i11 * 1192;
                int i16 = (i10 * 1634) + i15;
                int i17 = (i15 - (i10 * 833)) - (i9 * StatusCode.BAD_REQUEST);
                int i18 = i15 + (i9 * 2066);
                if (i16 < 0) {
                    i16 = 0;
                } else if (i16 > 262143) {
                    i16 = 262143;
                }
                if (i17 < 0) {
                    i17 = 0;
                } else if (i17 > 262143) {
                    i17 = 262143;
                }
                if (i18 < 0) {
                    i18 = 0;
                } else if (i18 > 262143) {
                    i18 = 262143;
                }
                iArr[i5] = ((i18 >> 10) & 255) | ((i16 << 6) & 16711680) | (-16777216) | ((i17 >> 2) & 65280);
                i8++;
                i5++;
            }
        }
        return iArr;
    }

    public Bitmap n(int i2) {
        byte[] timeThumbData = getTimeThumbData(i2);
        if (timeThumbData == null || timeThumbData.length == 0) {
            return null;
        }
        return Bitmap.createBitmap(c(timeThumbData, 320, 240), 320, 240, Bitmap.Config.ARGB_8888);
    }

    public int o(int i2) {
        return setThumbnailRoutine(i2);
    }
}
