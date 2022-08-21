package com.baidu.mapsdkplatform.comapi;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.util.Log;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* loaded from: classes.dex */
public class NativeLoader {
    private static final String a = "NativeLoader";
    private static Context b;
    private static NativeLoader e;
    private static final Set<String> c = new HashSet();
    private static final Set<String> d = new HashSet();
    private static a f = a.ARMEABI;
    private static boolean g = false;
    private static String h = null;

    /* loaded from: classes.dex */
    public enum a {
        ARMEABI("armeabi"),
        ARMV7("armeabi-v7a"),
        ARM64("arm64-v8a"),
        X86("x86"),
        X86_64("x86_64");
        
        private String f;

        a(String str) {
            this.f = str;
        }

        public String a() {
            return this.f;
        }
    }

    private NativeLoader() {
    }

    @TargetApi(8)
    private String a() {
        Context context = b;
        return (context != null && 8 <= Build.VERSION.SDK_INT) ? context.getPackageCodePath() : "";
    }

    private String a(a aVar) {
        return "lib/" + aVar.a() + h.g;
    }

    private void a(InputStream inputStream, FileOutputStream fileOutputStream) throws IOException {
        byte[] bArr = new byte[4096];
        while (true) {
            try {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            } catch (Throwable th) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    Log.e(a, "Close InputStream error", e2);
                }
                try {
                    fileOutputStream.close();
                } catch (IOException e3) {
                    Log.e(a, "Close OutputStream error", e3);
                }
                throw th;
            }
        }
        fileOutputStream.flush();
        try {
            inputStream.close();
        } catch (IOException e4) {
            Log.e(a, "Close InputStream error", e4);
        }
        try {
            fileOutputStream.close();
        } catch (IOException e5) {
            Log.e(a, "Close OutputStream error", e5);
        }
    }

    private void a(Throwable th) {
        Log.e(a, "loadException", th);
        Iterator<String> it = d.iterator();
        while (it.hasNext()) {
            String str = a;
            Log.e(str, it.next() + " Failed to load.");
        }
    }

    public static void a(boolean z, String str) {
        g = z;
        h = str;
    }

    private boolean a(String str) {
        try {
            Set<String> set = c;
            synchronized (set) {
                if (set.contains(str)) {
                    return true;
                }
                System.loadLibrary(str);
                synchronized (set) {
                    set.add(str);
                }
                return true;
            }
        } catch (Throwable th) {
            th.printStackTrace();
            return b(str);
        }
    }

    private boolean a(String str, a aVar) {
        ZipFile zipFile;
        File file = new File(b(), str);
        if (!file.exists() || file.length() <= 0) {
            String str2 = a(aVar) + str;
            ZipFile zipFile2 = null;
            String a2 = !g ? a() : h;
            if (a2 != null) {
                try {
                    if (!a2.isEmpty()) {
                        try {
                            zipFile = new ZipFile(a2);
                        } catch (Exception e2) {
                            e = e2;
                        }
                        try {
                            ZipEntry entry = zipFile.getEntry(str2);
                            if (entry == null) {
                                try {
                                    zipFile.close();
                                } catch (IOException e3) {
                                    Log.e(a, "Release file failed", e3);
                                }
                                return false;
                            }
                            a(zipFile.getInputStream(entry), new FileOutputStream(new File(b(), str)));
                            try {
                                zipFile.close();
                            } catch (IOException e4) {
                                Log.e(a, "Release file failed", e4);
                            }
                            return true;
                        } catch (Exception e5) {
                            e = e5;
                            zipFile2 = zipFile;
                            Log.e(a, "Copy library file error", e);
                            if (zipFile2 != null) {
                                try {
                                    zipFile2.close();
                                } catch (IOException e6) {
                                    Log.e(a, "Release file failed", e6);
                                }
                            }
                            return false;
                        } catch (Throwable th) {
                            th = th;
                            zipFile2 = zipFile;
                            if (zipFile2 != null) {
                                try {
                                    zipFile2.close();
                                } catch (IOException e7) {
                                    Log.e(a, "Release file failed", e7);
                                }
                            }
                            throw th;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            return false;
        }
        return true;
    }

    private boolean a(String str, String str2) {
        return !a(str2, a.ARMV7) ? b(str, str2) : f(str2, str);
    }

    private String b() {
        if (b == null) {
            return "";
        }
        File file = new File(b.getFilesDir(), "libs" + File.separator + f.a());
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    private boolean b(String str) {
        String mapLibraryName = System.mapLibraryName(str);
        Set<String> set = c;
        synchronized (set) {
            if (set.contains(str)) {
                return true;
            }
            int i = e.a[f.ordinal()];
            boolean d2 = i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? false : d(str, mapLibraryName) : e(str, mapLibraryName) : b(str, mapLibraryName) : a(str, mapLibraryName) : c(str, mapLibraryName);
            synchronized (set) {
                set.add(str);
            }
            return d2;
        }
    }

    private boolean b(String str, String str2) {
        a aVar = a.ARMEABI;
        if (a(str2, aVar)) {
            return f(str2, str);
        }
        String str3 = a;
        Log.e(str3, "found lib " + aVar.a() + h.g + str + ".so error");
        return false;
    }

    @TargetApi(21)
    private static a c() {
        String str = Build.VERSION.SDK_INT < 21 ? Build.CPU_ABI : Build.SUPPORTED_ABIS[0];
        if (str == null) {
            return a.ARMEABI;
        }
        if (str.contains("arm") && str.contains("v7")) {
            f = a.ARMV7;
        }
        if (str.contains("arm") && str.contains("64") && d()) {
            f = a.ARM64;
        }
        if (str.contains("x86")) {
            f = str.contains("64") ? a.X86_64 : a.X86;
        }
        return f;
    }

    private boolean c(String str, String str2) {
        return !a(str2, a.ARM64) ? a(str, str2) : f(str2, str);
    }

    private static boolean d() {
        int i = Build.VERSION.SDK_INT;
        if (i >= 23) {
            return Process.is64Bit();
        }
        if (i < 21) {
            return false;
        }
        return Build.CPU_ABI.equals(Build.SUPPORTED_64_BIT_ABIS[0]);
    }

    private boolean d(String str, String str2) {
        return !a(str2, a.X86) ? a(str, str2) : f(str2, str);
    }

    private boolean e(String str, String str2) {
        return !a(str2, a.X86_64) ? d(str, str2) : f(str2, str);
    }

    private boolean f(String str, String str2) {
        try {
            System.load(new File(b(), str).getAbsolutePath());
            Set<String> set = c;
            synchronized (set) {
                set.add(str2);
            }
            g(str, str2);
            return true;
        } catch (Throwable th) {
            Set<String> set2 = d;
            synchronized (set2) {
                set2.add(str2);
                a(th);
                return false;
            }
        }
    }

    private void g(String str, String str2) {
        if (str == null || str.isEmpty() || !str.contains("libBaiduMapSDK_")) {
            return;
        }
        try {
            String[] split = str.split("_v");
            if (split.length <= 1) {
                return;
            }
            File[] listFiles = new File(b()).listFiles(new d(this, split[1]));
            if (listFiles == null || listFiles.length == 0) {
                return;
            }
            for (File file : listFiles) {
                file.delete();
            }
        } catch (Exception unused) {
        }
    }

    public static synchronized NativeLoader getInstance() {
        NativeLoader nativeLoader;
        synchronized (NativeLoader.class) {
            if (e == null) {
                e = new NativeLoader();
                f = c();
            }
            nativeLoader = e;
        }
        return nativeLoader;
    }

    public static void setContext(Context context) {
        b = context;
    }

    public synchronized boolean loadLibrary(String str) {
        if (!g) {
            return a(str);
        }
        String str2 = h;
        if (str2 == null || str2.isEmpty()) {
            Log.e(a, "Given custom so file path is null, please check!");
            return false;
        }
        return b(str);
    }
}
