package com.xiaomi.onetrack;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import ch.qos.logback.core.CoreConstants;
import com.xiaomi.onetrack.api.g;
import com.xiaomi.onetrack.c.d;
import com.xiaomi.onetrack.e.a;
import com.xiaomi.onetrack.util.aa;
import com.xiaomi.onetrack.util.ac;
import com.xiaomi.onetrack.util.b;
import com.xiaomi.onetrack.util.i;
import com.xiaomi.onetrack.util.k;
import com.xiaomi.onetrack.util.p;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import xcrash.XCrash;

/* loaded from: classes3.dex */
public class CrashAnalysis {
    public static final AtomicBoolean t = new AtomicBoolean(false);
    public final FileProcessor[] u;
    public final g v;

    public CrashAnalysis(Context context, g gVar) {
        try {
            Object newInstance = XCrash.InitParameters.class.getConstructor(new Class[0]).newInstance(new Object[0]);
            Boolean bool = Boolean.FALSE;
            a(newInstance, "setNativeDumpAllThreads", bool);
            a(newInstance, "setLogDir", a());
            a(newInstance, "setNativeDumpMap", bool);
            a(newInstance, "setNativeDumpFds", bool);
            a(newInstance, "setJavaDumpAllThreads", bool);
            a(newInstance, "setAnrRethrow", bool);
            boolean z = XCrash.initialized;
            XCrash.class.getDeclaredMethod("init", Context.class, newInstance.getClass()).invoke(null, context.getApplicationContext(), newInstance);
            p.a("CrashAnalysis", "XCrash init success");
        } catch (Throwable th) {
            p.a("CrashAnalysis", "XCrash init failed: " + th.toString());
        }
        this.v = gVar;
        this.u = new FileProcessor[]{new FileProcessor("java"), new FileProcessor("anr"), new FileProcessor("native")};
    }

    public static boolean isSupport() {
        try {
            boolean z = XCrash.initialized;
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static void a(Context context) {
        try {
            a.a(context);
            boolean z = XCrash.initialized;
            XCrash.class.getDeclaredMethod("initHooker", Context.class, String.class).invoke(null, context.getApplicationContext(), a());
            Log.d("CrashAnalysis", "registerHook succeeded");
        } catch (Throwable th) {
            Log.d("CrashAnalysis", "registerHook failed: " + th.toString());
        }
    }

    public static void start(final Context context, final g gVar) {
        if (t.compareAndSet(false, true)) {
            i.a(new Runnable() { // from class: com.xiaomi.onetrack.CrashAnalysis.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        CrashAnalysis crashAnalysis = new CrashAnalysis(context, gVar);
                        if (crashAnalysis.d()) {
                            crashAnalysis.e();
                        } else {
                            p.a("CrashAnalysis", "no crash file found");
                        }
                    } catch (Throwable th) {
                        p.b("CrashAnalysis", "processCrash error: " + th.toString());
                    }
                }
            });
        } else {
            p.b("CrashAnalysis", "run method has been invoked more than once");
        }
    }

    public static String c(String str, String str2) {
        int i;
        int indexOf;
        String substring;
        int indexOf2;
        int indexOf3;
        if (!TextUtils.isEmpty(str)) {
            try {
                if (str2.equals("anr")) {
                    int indexOf4 = str.indexOf(" tid=1 ");
                    if (indexOf4 == -1 || (indexOf2 = str.indexOf("\n  at ", indexOf4)) == -1 || (indexOf3 = str.indexOf(10, indexOf2 + 6)) == -1) {
                        return "uncategoried";
                    }
                    substring = str.substring(indexOf2 + 2, indexOf3);
                } else {
                    int indexOf5 = str.indexOf("error reason:\n\t");
                    if (indexOf5 == -1 || (indexOf = str.indexOf("\n\n", (i = indexOf5 + 15))) == -1) {
                        return "uncategoried";
                    }
                    substring = str.substring(i, indexOf);
                }
                return substring;
            } catch (Exception e) {
                p.b("CrashAnalysis", "getErrorReasonString error: " + e.toString());
                return "uncategoried";
            }
        }
        return "uncategoried";
    }

    public static String d(String str, String str2) {
        int i;
        int indexOf;
        String substring;
        int indexOf2;
        if (!TextUtils.isEmpty(str)) {
            try {
                if (str2.equals("anr")) {
                    int indexOf3 = str.indexOf(" tid=1 ");
                    if (indexOf3 == -1 || (indexOf2 = str.indexOf("\n\n", indexOf3)) == -1) {
                        return "";
                    }
                    substring = calculateJavaDigest(str.substring(indexOf3, indexOf2));
                } else {
                    int indexOf4 = str.indexOf("backtrace feature id:\n\t");
                    if (indexOf4 == -1 || (indexOf = str.indexOf("\n\n", (i = indexOf4 + 23))) == -1) {
                        return "";
                    }
                    substring = str.substring(i, indexOf);
                }
                return substring;
            } catch (Exception e) {
                p.b("CrashAnalysis", "calculateFeatureId error: " + e.toString());
                return "";
            }
        }
        return "";
    }

    public static long b(String str) {
        int i;
        int indexOf;
        if (!TextUtils.isEmpty(str)) {
            try {
                int indexOf2 = str.indexOf("Crash time: '");
                if (indexOf2 != -1 && (indexOf = str.indexOf("'\n", (i = indexOf2 + 13))) != -1) {
                    return b.a(str.substring(i, indexOf));
                }
                return 0L;
            } catch (Exception e) {
                p.b("CrashAnalysis", "getCrashTimeStamp error: " + e.toString());
                return 0L;
            }
        }
        return 0L;
    }

    public final void a(Object obj, String str, Object obj2) throws Exception {
        obj.getClass().getDeclaredMethod(str, obj2.getClass() == Boolean.class ? Boolean.TYPE : obj2.getClass()).invoke(obj, obj2);
    }

    public static String a() {
        return k.a();
    }

    public final long b() {
        long c = aa.c();
        if (c == 0) {
            p.a("CrashAnalysis", "no ticket data found, return max count");
            return 10L;
        }
        long b = ac.b();
        if (c / 100 != b) {
            p.a("CrashAnalysis", "no today's ticket, return max count");
            return 10L;
        }
        long j = c - (b * 100);
        p.a("CrashAnalysis", "today's remain ticket is " + j);
        return j;
    }

    public final void a(long j) {
        aa.d((ac.b() * 100) + j);
    }

    public final List<File> c() {
        File[] listFiles = new File(a()).listFiles();
        if (listFiles == null) {
            p.a("CrashAnalysis", "this path does not denote a directory, or if an I/O error occurs.");
            return null;
        }
        List<File> asList = Arrays.asList(listFiles);
        Collections.sort(asList, new Comparator<File>() { // from class: com.xiaomi.onetrack.CrashAnalysis.2
            @Override // java.util.Comparator
            public int compare(File file, File file2) {
                return (int) (file.lastModified() - file2.lastModified());
            }
        });
        int size = asList.size();
        if (size <= 20) {
            return asList;
        }
        int i = size - 20;
        for (int i2 = 0; i2 < i; i2++) {
            k.a(asList.get(i2));
        }
        return asList.subList(i, size);
    }

    public final boolean d() {
        boolean z;
        Iterator<File> it;
        List<File> c = c();
        long b = b();
        if (c == null || c.size() <= 0) {
            z = false;
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            long b2 = aa.b();
            int i = (b2 > currentTimeMillis ? 1 : (b2 == currentTimeMillis ? 0 : -1));
            long j = CoreConstants.MILLIS_IN_ONE_WEEK;
            if (i > 0) {
                b2 = currentTimeMillis - CoreConstants.MILLIS_IN_ONE_WEEK;
            }
            Iterator<File> it2 = c.iterator();
            long j2 = 0;
            long j3 = 0;
            boolean z2 = false;
            while (it2.hasNext()) {
                File next = it2.next();
                long lastModified = next.lastModified();
                if (lastModified < currentTimeMillis - j || lastModified > currentTimeMillis) {
                    it = it2;
                    p.a("CrashAnalysis", "remove obsolete crash files: " + next.getName());
                    k.a(next);
                } else {
                    if (lastModified <= b2) {
                        p.a("CrashAnalysis", "found already reported crash file, ignore");
                    } else if (b > j2) {
                        FileProcessor[] fileProcessorArr = this.u;
                        int length = fileProcessorArr.length;
                        int i2 = 0;
                        while (i2 < length) {
                            Iterator<File> it3 = it2;
                            if (fileProcessorArr[i2].a(next)) {
                                p.a("CrashAnalysis", "find crash file:" + next.getName());
                                b--;
                                if (j3 < lastModified) {
                                    j3 = lastModified;
                                }
                                z2 = true;
                            }
                            i2++;
                            it2 = it3;
                        }
                    }
                    it = it2;
                }
                it2 = it;
                j = CoreConstants.MILLIS_IN_ONE_WEEK;
                j2 = 0;
            }
            if (j3 > j2) {
                aa.c(j3);
            }
            z = z2;
        }
        if (z) {
            a(b);
        }
        return z;
    }

    public final void e() {
        for (FileProcessor fileProcessor : this.u) {
            fileProcessor.a();
        }
    }

    /* loaded from: classes3.dex */
    public class FileProcessor {
        public final List<File> a = new ArrayList();
        public final String b;
        public final String c;

        public FileProcessor(String str) {
            this.c = str;
            this.b = str + ".xcrash";
        }

        public boolean a(File file) {
            if (file.getName().contains(this.b)) {
                this.a.add(file);
                return true;
            }
            return false;
        }

        public final String a(String str) {
            if (!TextUtils.isEmpty(str)) {
                String[] split = str.split("__");
                if (split.length != 2) {
                    return null;
                }
                String[] split2 = split[0].split("_");
                if (split2.length != 3) {
                    return null;
                }
                return split2[2];
            }
            return null;
        }

        public void a() {
            for (int i = 0; i < this.a.size(); i++) {
                String absolutePath = this.a.get(i).getAbsoluteFile().getAbsolutePath();
                String a = a(absolutePath);
                String a2 = k.a(absolutePath, 102400);
                if (!TextUtils.isEmpty(a2) && CrashAnalysis.this.v != null) {
                    String d = CrashAnalysis.d(a2, this.c);
                    String c = CrashAnalysis.c(a2, this.c);
                    long b = CrashAnalysis.b(a2);
                    p.a("CrashAnalysis", "fileName: " + absolutePath);
                    p.a("CrashAnalysis", "feature id: " + d);
                    p.a("CrashAnalysis", "error: " + c);
                    p.a("CrashAnalysis", "crashTimeStamp: " + b);
                    CrashAnalysis.this.v.a(a2, c, this.c, a, d, b);
                    k.a(new File(absolutePath));
                    p.a("CrashAnalysis", "remove reported crash file");
                }
            }
        }
    }

    public static String calculateJavaDigest(String str) {
        String[] split = str.replaceAll("\\t", "").split("\\n");
        StringBuilder sb = new StringBuilder();
        int min = Math.min(split.length, 20);
        for (int i = 0; i < min; i++) {
            split[i] = split[i].replaceAll("((java:)|(length=)|(index=)|(Index:)|(Size:))\\d+", "$1XX").replaceAll("\\$[0-9a-fA-F]{1,10}@[0-9a-fA-F]{1,10}|@[0-9a-fA-F]{1,10}|0x[0-9a-fA-F]{1,10}", "XX").replaceAll("\\d+[B,KB,MB]*", "");
        }
        for (int i2 = 0; i2 < min && (!split[i2].contains("...") || !split[i2].contains("more")); i2++) {
            sb.append(split[i2]);
            sb.append('\n');
        }
        return d.h(sb.toString());
    }
}
