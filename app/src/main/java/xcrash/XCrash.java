package xcrash;

import android.content.Context;
import java.lang.Thread;
import java.util.concurrent.Semaphore;

/* loaded from: classes3.dex */
public final class XCrash {
    public static String appId = null;
    public static String appVersion = null;
    public static String crashPath = null;
    public static Semaphore initSemaphore = null;
    public static boolean initialized = false;
    public static String logDir;
    public static Context tempContext;
    public static ILogger logger = new DefaultLogger();
    public static boolean hookSet = false;
    public static Thread.UncaughtExceptionHandler defaultHandler = null;
    public static Thread.UncaughtExceptionHandler fcHandler = new Thread.UncaughtExceptionHandler() { // from class: xcrash.XCrash.1
        @Override // java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread thread, Throwable th) {
            try {
                if (!XCrash.initGetCalled()) {
                    InitParameters initParameters = new InitParameters();
                    initParameters.setNativeDumpAllThreads(false);
                    initParameters.setLogDir(XCrash.crashPath);
                    initParameters.setNativeDumpMap(false);
                    initParameters.setNativeDumpFds(false);
                    initParameters.setJavaDumpAllThreads(false);
                    XCrash.init(XCrash.tempContext, initParameters);
                }
                XCrash.initSemaphore.acquire();
                JavaCrashHandler.getInstance().setDefaultHandler(XCrash.defaultHandler);
                JavaCrashHandler.getInstance().uncaughtException(thread, th);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public static void initHooker(Context context, String str) {
        initSemaphore = new Semaphore(0);
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        tempContext = context;
        crashPath = str;
        try {
            Thread.setDefaultUncaughtExceptionHandler(fcHandler);
            hookSet = true;
        } catch (Exception e) {
            getLogger().e("xcrash", "JavaCrashHandler setDefaultUncaughtExceptionHandler failed", e);
        }
    }

    public static int init(Context context) {
        return init(context, null);
    }

    public static synchronized boolean initGetCalled() {
        boolean z;
        synchronized (XCrash.class) {
            z = initialized;
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x00db A[Catch: all -> 0x01df, TryCatch #0 {, blocks: (B:4:0x0003, B:10:0x000b, B:15:0x0012, B:20:0x001d, B:22:0x0026, B:24:0x002a, B:25:0x002c, B:27:0x0038, B:28:0x003c, B:30:0x0044, B:31:0x004a, B:33:0x0056, B:34:0x006d, B:36:0x007b, B:48:0x009a, B:50:0x00c1, B:52:0x00c5, B:57:0x00d7, B:59:0x00db, B:61:0x011f, B:63:0x0125, B:65:0x0129, B:67:0x014e, B:69:0x0152, B:71:0x0156, B:83:0x01cd, B:85:0x01d8, B:86:0x01db, B:75:0x0160, B:77:0x0194, B:82:0x01a3, B:54:0x00c9, B:56:0x00cd, B:40:0x0083, B:42:0x008b, B:44:0x0091, B:46:0x0097), top: B:92:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x011d  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0194 A[Catch: all -> 0x01df, TryCatch #0 {, blocks: (B:4:0x0003, B:10:0x000b, B:15:0x0012, B:20:0x001d, B:22:0x0026, B:24:0x002a, B:25:0x002c, B:27:0x0038, B:28:0x003c, B:30:0x0044, B:31:0x004a, B:33:0x0056, B:34:0x006d, B:36:0x007b, B:48:0x009a, B:50:0x00c1, B:52:0x00c5, B:57:0x00d7, B:59:0x00db, B:61:0x011f, B:63:0x0125, B:65:0x0129, B:67:0x014e, B:69:0x0152, B:71:0x0156, B:83:0x01cd, B:85:0x01d8, B:86:0x01db, B:75:0x0160, B:77:0x0194, B:82:0x01a3, B:54:0x00c9, B:56:0x00cd, B:40:0x0083, B:42:0x008b, B:44:0x0091, B:46:0x0097), top: B:92:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x019f  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01d8 A[Catch: all -> 0x01df, TryCatch #0 {, blocks: (B:4:0x0003, B:10:0x000b, B:15:0x0012, B:20:0x001d, B:22:0x0026, B:24:0x002a, B:25:0x002c, B:27:0x0038, B:28:0x003c, B:30:0x0044, B:31:0x004a, B:33:0x0056, B:34:0x006d, B:36:0x007b, B:48:0x009a, B:50:0x00c1, B:52:0x00c5, B:57:0x00d7, B:59:0x00db, B:61:0x011f, B:63:0x0125, B:65:0x0129, B:67:0x014e, B:69:0x0152, B:71:0x0156, B:83:0x01cd, B:85:0x01d8, B:86:0x01db, B:75:0x0160, B:77:0x0194, B:82:0x01a3, B:54:0x00c9, B:56:0x00cd, B:40:0x0083, B:42:0x008b, B:44:0x0091, B:46:0x0097), top: B:92:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static synchronized int init(android.content.Context r34, xcrash.XCrash.InitParameters r35) {
        /*
            Method dump skipped, instructions count: 482
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: xcrash.XCrash.init(android.content.Context, xcrash.XCrash$InitParameters):int");
    }

    /* loaded from: classes3.dex */
    public static class InitParameters {
        public String appVersion = null;
        public String logDir = null;
        public int logFileMaintainDelayMs = 5000;
        public ILogger logger = null;
        public ILibLoader libLoader = null;
        public int placeholderCountMax = 0;
        public int placeholderSizeKb = 128;
        public boolean enableJavaCrashHandler = true;
        public boolean javaRethrow = true;
        public int javaLogCountMax = 10;
        public int javaLogcatSystemLines = 50;
        public int javaLogcatEventsLines = 50;
        public int javaLogcatMainLines = 200;
        public boolean javaDumpFds = true;
        public boolean javaDumpNetworkInfo = true;
        public boolean javaDumpAllThreads = true;
        public int javaDumpAllThreadsCountMax = 0;
        public String[] javaDumpAllThreadsWhiteList = null;
        public ICrashCallback javaCallback = null;
        public boolean enableNativeCrashHandler = true;
        public boolean nativeRethrow = true;
        public int nativeLogCountMax = 10;
        public int nativeLogcatSystemLines = 50;
        public int nativeLogcatEventsLines = 50;
        public int nativeLogcatMainLines = 200;
        public boolean nativeDumpElfHash = true;
        public boolean nativeDumpMap = true;
        public boolean nativeDumpFds = true;
        public boolean nativeDumpNetworkInfo = true;
        public boolean nativeDumpAllThreads = true;
        public int nativeDumpAllThreadsCountMax = 0;
        public String[] nativeDumpAllThreadsWhiteList = null;
        public ICrashCallback nativeCallback = null;
        public boolean enableAnrHandler = true;
        public boolean anrRethrow = true;
        public boolean anrCheckProcessState = true;
        public int anrLogCountMax = 10;
        public int anrLogcatSystemLines = 50;
        public int anrLogcatEventsLines = 50;
        public int anrLogcatMainLines = 200;
        public boolean anrDumpFds = true;
        public boolean anrDumpNetworkInfo = true;
        public ICrashCallback anrCallback = null;

        public InitParameters setAppVersion(String str) {
            this.appVersion = str;
            return this;
        }

        public InitParameters setLogDir(String str) {
            this.logDir = str;
            return this;
        }

        public InitParameters setLogFileMaintainDelayMs(int i) {
            if (i < 0) {
                i = 0;
            }
            this.logFileMaintainDelayMs = i;
            return this;
        }

        public InitParameters setLogger(ILogger iLogger) {
            this.logger = iLogger;
            return this;
        }

        public InitParameters setLibLoader(ILibLoader iLibLoader) {
            this.libLoader = iLibLoader;
            return this;
        }

        public InitParameters setPlaceholderCountMax(int i) {
            if (i < 0) {
                i = 0;
            }
            this.placeholderCountMax = i;
            return this;
        }

        public InitParameters setPlaceholderSizeKb(int i) {
            if (i < 0) {
                i = 0;
            }
            this.placeholderSizeKb = i;
            return this;
        }

        public InitParameters enableJavaCrashHandler() {
            this.enableJavaCrashHandler = true;
            return this;
        }

        public InitParameters disableJavaCrashHandler() {
            this.enableJavaCrashHandler = false;
            return this;
        }

        public InitParameters setJavaRethrow(boolean z) {
            this.javaRethrow = z;
            return this;
        }

        public InitParameters setJavaLogCountMax(int i) {
            if (i < 1) {
                i = 1;
            }
            this.javaLogCountMax = i;
            return this;
        }

        public InitParameters setJavaLogcatSystemLines(int i) {
            this.javaLogcatSystemLines = i;
            return this;
        }

        public InitParameters setJavaLogcatEventsLines(int i) {
            this.javaLogcatEventsLines = i;
            return this;
        }

        public InitParameters setJavaLogcatMainLines(int i) {
            this.javaLogcatMainLines = i;
            return this;
        }

        public InitParameters setJavaDumpFds(boolean z) {
            this.javaDumpFds = z;
            return this;
        }

        public InitParameters setJavaDumpNetworkInfo(boolean z) {
            this.javaDumpNetworkInfo = z;
            return this;
        }

        public InitParameters setJavaDumpAllThreads(boolean z) {
            this.javaDumpAllThreads = z;
            return this;
        }

        public InitParameters setJavaDumpAllThreadsCountMax(int i) {
            if (i < 0) {
                i = 0;
            }
            this.javaDumpAllThreadsCountMax = i;
            return this;
        }

        public InitParameters setJavaDumpAllThreadsWhiteList(String[] strArr) {
            this.javaDumpAllThreadsWhiteList = strArr;
            return this;
        }

        public InitParameters setJavaCallback(ICrashCallback iCrashCallback) {
            this.javaCallback = iCrashCallback;
            return this;
        }

        public InitParameters enableNativeCrashHandler() {
            this.enableNativeCrashHandler = true;
            return this;
        }

        public InitParameters disableNativeCrashHandler() {
            this.enableNativeCrashHandler = false;
            return this;
        }

        public InitParameters setNativeRethrow(boolean z) {
            this.nativeRethrow = z;
            return this;
        }

        public InitParameters setNativeLogCountMax(int i) {
            if (i < 1) {
                i = 1;
            }
            this.nativeLogCountMax = i;
            return this;
        }

        public InitParameters setNativeLogcatSystemLines(int i) {
            this.nativeLogcatSystemLines = i;
            return this;
        }

        public InitParameters setNativeLogcatEventsLines(int i) {
            this.nativeLogcatEventsLines = i;
            return this;
        }

        public InitParameters setNativeLogcatMainLines(int i) {
            this.nativeLogcatMainLines = i;
            return this;
        }

        public InitParameters setNativeDumpElfHash(boolean z) {
            this.nativeDumpElfHash = z;
            return this;
        }

        public InitParameters setNativeDumpMap(boolean z) {
            this.nativeDumpMap = z;
            return this;
        }

        public InitParameters setNativeDumpFds(boolean z) {
            this.nativeDumpFds = z;
            return this;
        }

        public InitParameters setNativeDumpNetwork(boolean z) {
            this.nativeDumpNetworkInfo = z;
            return this;
        }

        public InitParameters setNativeDumpAllThreads(boolean z) {
            this.nativeDumpAllThreads = z;
            return this;
        }

        public InitParameters setNativeDumpAllThreadsCountMax(int i) {
            if (i < 0) {
                i = 0;
            }
            this.nativeDumpAllThreadsCountMax = i;
            return this;
        }

        public InitParameters setNativeDumpAllThreadsWhiteList(String[] strArr) {
            this.nativeDumpAllThreadsWhiteList = strArr;
            return this;
        }

        public InitParameters setNativeCallback(ICrashCallback iCrashCallback) {
            this.nativeCallback = iCrashCallback;
            return this;
        }

        public InitParameters enableAnrCrashHandler() {
            this.enableAnrHandler = true;
            return this;
        }

        public InitParameters disableAnrCrashHandler() {
            this.enableAnrHandler = false;
            return this;
        }

        public InitParameters setAnrRethrow(boolean z) {
            this.anrRethrow = z;
            return this;
        }

        public InitParameters setAnrCheckProcessState(boolean z) {
            this.anrCheckProcessState = z;
            return this;
        }

        public InitParameters setAnrLogCountMax(int i) {
            if (i < 1) {
                i = 1;
            }
            this.anrLogCountMax = i;
            return this;
        }

        public InitParameters setAnrLogcatSystemLines(int i) {
            this.anrLogcatSystemLines = i;
            return this;
        }

        public InitParameters setAnrLogcatEventsLines(int i) {
            this.anrLogcatEventsLines = i;
            return this;
        }

        public InitParameters setAnrLogcatMainLines(int i) {
            this.anrLogcatMainLines = i;
            return this;
        }

        public InitParameters setAnrDumpFds(boolean z) {
            this.anrDumpFds = z;
            return this;
        }

        public InitParameters setAnrDumpNetwork(boolean z) {
            this.anrDumpNetworkInfo = z;
            return this;
        }

        public InitParameters setAnrCallback(ICrashCallback iCrashCallback) {
            this.anrCallback = iCrashCallback;
            return this;
        }
    }

    public static ILogger getLogger() {
        return logger;
    }

    public static void testJavaCrash(boolean z) throws RuntimeException {
        if (z) {
            Thread thread = new Thread() { // from class: xcrash.XCrash.2
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    throw new RuntimeException("test java exception");
                }
            };
            thread.setName("xcrash_test_java_thread");
            thread.start();
            return;
        }
        throw new RuntimeException("test java exception");
    }

    public static void testNativeCrash(boolean z) {
        NativeHandler.getInstance().testNativeCrash(z);
    }
}
