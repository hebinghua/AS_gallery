package xcrash;

import android.annotation.SuppressLint;
import android.os.Process;
import android.text.TextUtils;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

@SuppressLint({"StaticFieldLeak"})
/* loaded from: classes3.dex */
public class JavaCrashHandler implements Thread.UncaughtExceptionHandler {
    public static final JavaCrashHandler instance = new JavaCrashHandler();
    public String appId;
    public String appVersion;
    public ICrashCallback callback;
    public boolean dumpAllThreads;
    public int dumpAllThreadsCountMax;
    public String[] dumpAllThreadsWhiteList;
    public boolean dumpFds;
    public boolean dumpNetworkInfo;
    public String logDir;
    public int logcatEventsLines;
    public int logcatMainLines;
    public int logcatSystemLines;
    public int pid;
    public String processName;
    public boolean rethrow;
    public final Date startTime = new Date();
    public Thread.UncaughtExceptionHandler defaultHandler = null;

    public static JavaCrashHandler getInstance() {
        return instance;
    }

    public void initialize(int i, String str, boolean z, String str2, String str3, String str4, boolean z2, int i2, int i3, int i4, boolean z3, boolean z4, boolean z5, int i5, String[] strArr, ICrashCallback iCrashCallback) {
        this.pid = i;
        this.processName = TextUtils.isEmpty(str) ? "unknown" : str;
        this.appId = str2;
        this.appVersion = str3;
        this.rethrow = z2;
        this.logDir = str4;
        this.logcatSystemLines = i2;
        this.logcatEventsLines = i3;
        this.logcatMainLines = i4;
        this.dumpFds = z3;
        this.dumpNetworkInfo = z4;
        this.dumpAllThreads = z5;
        this.dumpAllThreadsCountMax = i5;
        this.dumpAllThreadsWhiteList = strArr;
        this.callback = iCrashCallback;
        if (!z) {
            this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            try {
                Thread.setDefaultUncaughtExceptionHandler(this);
            } catch (Exception e) {
                XCrash.getLogger().e("xcrash", "JavaCrashHandler setDefaultUncaughtExceptionHandler failed", e);
            }
        }
    }

    public void setDefaultHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.defaultHandler = uncaughtExceptionHandler;
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.defaultHandler;
        if (uncaughtExceptionHandler != null) {
            Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
        }
        try {
            handleException(thread, th);
        } catch (Exception e) {
            XCrash.getLogger().e("xcrash", "JavaCrashHandler handleException failed", e);
        }
        if (this.rethrow) {
            Thread.UncaughtExceptionHandler uncaughtExceptionHandler2 = this.defaultHandler;
            if (uncaughtExceptionHandler2 == null) {
                return;
            }
            uncaughtExceptionHandler2.uncaughtException(thread, th);
            return;
        }
        ActivityMonitor.getInstance().finishAllActivities();
        Process.killProcess(this.pid);
        System.exit(10);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:61:0x017c  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0088 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:91:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void handleException(java.lang.Thread r14, java.lang.Throwable r15) {
        /*
            Method dump skipped, instructions count: 391
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: xcrash.JavaCrashHandler.handleException(java.lang.Thread, java.lang.Throwable):void");
    }

    public final String getEmergency(Date date, Thread thread, String str) {
        return Util.getLogHeader(this.startTime, date, "java", this.appId, this.appVersion) + "pid: " + this.pid + ", tid: " + Process.myTid() + ", name: " + thread.getName() + "  >>> " + this.processName + " <<<\n\njava stacktrace:\n" + str + "\n";
    }

    public final String getOtherThreadsInfo(Thread thread) {
        ArrayList<Pattern> arrayList;
        if (this.dumpAllThreadsWhiteList != null) {
            arrayList = new ArrayList<>();
            for (String str : this.dumpAllThreadsWhiteList) {
                try {
                    arrayList.add(Pattern.compile(str));
                } catch (Exception e) {
                    XCrash.getLogger().w("xcrash", "JavaCrashHandler pattern compile failed", e);
                }
            }
        } else {
            arrayList = null;
        }
        StringBuilder sb = new StringBuilder();
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        for (Map.Entry<Thread, StackTraceElement[]> entry : allStackTraces.entrySet()) {
            Thread key = entry.getKey();
            StackTraceElement[] value = entry.getValue();
            if (!key.getName().equals(thread.getName()) && (arrayList == null || matchThreadName(arrayList, key.getName()))) {
                i2++;
                int i4 = this.dumpAllThreadsCountMax;
                if (i4 <= 0 || i < i4) {
                    sb.append("--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\n");
                    sb.append("pid: ");
                    sb.append(this.pid);
                    sb.append(", tid: ");
                    sb.append(key.getId());
                    sb.append(", name: ");
                    sb.append(key.getName());
                    sb.append("  >>> ");
                    sb.append(this.processName);
                    sb.append(" <<<\n");
                    sb.append("\n");
                    sb.append("java stacktrace:\n");
                    for (StackTraceElement stackTraceElement : value) {
                        sb.append("    at ");
                        sb.append(stackTraceElement.toString());
                        sb.append("\n");
                    }
                    sb.append("\n");
                    i++;
                } else {
                    i3++;
                }
            }
        }
        if (allStackTraces.size() > 1) {
            if (i == 0) {
                sb.append("--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\n");
            }
            sb.append("total JVM threads (exclude the crashed thread): ");
            sb.append(allStackTraces.size() - 1);
            sb.append("\n");
            if (arrayList != null) {
                sb.append("JVM threads matched whitelist: ");
                sb.append(i2);
                sb.append("\n");
            }
            if (this.dumpAllThreadsCountMax > 0) {
                sb.append("JVM threads ignored by max count limit: ");
                sb.append(i3);
                sb.append("\n");
            }
            sb.append("dumped JVM threads:");
            sb.append(i);
            sb.append("\n");
            sb.append("+++ +++ +++ +++ +++ +++ +++ +++ +++ +++ +++ +++ +++ +++ +++ +++\n");
        }
        return sb.toString();
    }

    public final boolean matchThreadName(ArrayList<Pattern> arrayList, String str) {
        Iterator<Pattern> it = arrayList.iterator();
        while (it.hasNext()) {
            if (it.next().matcher(str).matches()) {
                return true;
            }
        }
        return false;
    }
}
