package xcrash;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.os.Process;
import android.system.Os;
import android.text.TextUtils;
import com.xiaomi.stat.a;
import com.xiaomi.stat.a.j;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes3.dex */
public class Util {
    public static final String[] suPathname = {"/data/local/su", "/data/local/bin/su", "/data/local/xbin/su", "/system/xbin/su", "/system/bin/su", "/system/bin/.ext/su", "/system/bin/failsafe/su", "/system/sd/xbin/su", "/system/usr/we-need-root/su", "/sbin/su", "/su/bin/su"};

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0078, code lost:
        if (r0 == null) goto L41;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String getProcessName(android.content.Context r4, int r5) {
        /*
            java.lang.String r0 = "activity"
            java.lang.Object r4 = r4.getSystemService(r0)     // Catch: java.lang.Exception -> L2f
            android.app.ActivityManager r4 = (android.app.ActivityManager) r4     // Catch: java.lang.Exception -> L2f
            if (r4 == 0) goto L2f
            java.util.List r4 = r4.getRunningAppProcesses()     // Catch: java.lang.Exception -> L2f
            if (r4 == 0) goto L2f
            java.util.Iterator r4 = r4.iterator()     // Catch: java.lang.Exception -> L2f
        L14:
            boolean r0 = r4.hasNext()     // Catch: java.lang.Exception -> L2f
            if (r0 == 0) goto L2f
            java.lang.Object r0 = r4.next()     // Catch: java.lang.Exception -> L2f
            android.app.ActivityManager$RunningAppProcessInfo r0 = (android.app.ActivityManager.RunningAppProcessInfo) r0     // Catch: java.lang.Exception -> L2f
            int r1 = r0.pid     // Catch: java.lang.Exception -> L2f
            if (r1 != r5) goto L14
            java.lang.String r1 = r0.processName     // Catch: java.lang.Exception -> L2f
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Exception -> L2f
            if (r1 != 0) goto L14
            java.lang.String r4 = r0.processName     // Catch: java.lang.Exception -> L2f
            return r4
        L2f:
            r4 = 0
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L77
            java.io.FileReader r1 = new java.io.FileReader     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L77
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L77
            r2.<init>()     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L77
            java.lang.String r3 = "/proc/"
            r2.append(r3)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L77
            r2.append(r5)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L77
            java.lang.String r5 = "/cmdline"
            r2.append(r5)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L77
            java.lang.String r5 = r2.toString()     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L77
            r1.<init>(r5)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L77
            r0.<init>(r1)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L77
            java.lang.String r5 = r0.readLine()     // Catch: java.lang.Throwable -> L6c java.lang.Exception -> L78
            boolean r1 = android.text.TextUtils.isEmpty(r5)     // Catch: java.lang.Throwable -> L6c java.lang.Exception -> L78
            if (r1 != 0) goto L68
            java.lang.String r5 = r5.trim()     // Catch: java.lang.Throwable -> L6c java.lang.Exception -> L78
            boolean r1 = android.text.TextUtils.isEmpty(r5)     // Catch: java.lang.Throwable -> L6c java.lang.Exception -> L78
            if (r1 != 0) goto L68
            r0.close()     // Catch: java.lang.Exception -> L67
        L67:
            return r5
        L68:
            r0.close()     // Catch: java.lang.Exception -> L7b
            goto L7b
        L6c:
            r4 = move-exception
            goto L71
        L6e:
            r5 = move-exception
            r0 = r4
            r4 = r5
        L71:
            if (r0 == 0) goto L76
            r0.close()     // Catch: java.lang.Exception -> L76
        L76:
            throw r4
        L77:
            r0 = r4
        L78:
            if (r0 == 0) goto L7b
            goto L68
        L7b:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: xcrash.Util.getProcessName(android.content.Context, int):java.lang.String");
    }

    public static boolean isRoot() {
        try {
            for (String str : suPathname) {
                if (new File(str).exists()) {
                    return true;
                }
            }
        } catch (Exception unused) {
        }
        return false;
    }

    public static String getAbiList() {
        if (Build.VERSION.SDK_INT >= 21) {
            return TextUtils.join(",", Build.SUPPORTED_ABIS);
        }
        String str = Build.CPU_ABI;
        String str2 = Build.CPU_ABI2;
        if (TextUtils.isEmpty(str2)) {
            return str;
        }
        return str + "," + str2;
    }

    public static String getAppVersion(Context context) {
        String str;
        try {
            str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception unused) {
            str = null;
        }
        return TextUtils.isEmpty(str) ? "unknown" : str;
    }

    public static String getProcessMemoryInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(" Process Summary (From: android.os.Debug.MemoryInfo)\n");
        Locale locale = Locale.US;
        sb.append(String.format(locale, "%21s %8s\n", "", "Pss(KB)"));
        sb.append(String.format(locale, "%21s %8s\n", "", "------"));
        try {
            Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
            Debug.getMemoryInfo(memoryInfo);
            int i = Build.VERSION.SDK_INT;
            if (i >= 23) {
                sb.append(String.format(locale, "%21s %8s\n", "Java Heap:", memoryInfo.getMemoryStat("summary.java-heap")));
                sb.append(String.format(locale, "%21s %8s\n", "Native Heap:", memoryInfo.getMemoryStat("summary.native-heap")));
                sb.append(String.format(locale, "%21s %8s\n", "Code:", memoryInfo.getMemoryStat("summary.code")));
                sb.append(String.format(locale, "%21s %8s\n", "Stack:", memoryInfo.getMemoryStat("summary.stack")));
                sb.append(String.format(locale, "%21s %8s\n", "Graphics:", memoryInfo.getMemoryStat("summary.graphics")));
                sb.append(String.format(locale, "%21s %8s\n", "Private Other:", memoryInfo.getMemoryStat("summary.private-other")));
                sb.append(String.format(locale, "%21s %8s\n", "System:", memoryInfo.getMemoryStat("summary.system")));
                sb.append(String.format(locale, "%21s %8s %21s %8s\n", "TOTAL:", memoryInfo.getMemoryStat("summary.total-pss"), "TOTAL SWAP:", memoryInfo.getMemoryStat("summary.total-swap")));
            } else {
                sb.append(String.format(locale, "%21s %8s\n", "Java Heap:", "~ " + memoryInfo.dalvikPrivateDirty));
                sb.append(String.format(locale, "%21s %8s\n", "Native Heap:", String.valueOf(memoryInfo.nativePrivateDirty)));
                sb.append(String.format(locale, "%21s %8s\n", "Private Other:", "~ " + memoryInfo.otherPrivateDirty));
                if (i >= 19) {
                    sb.append(String.format(locale, "%21s %8s\n", "System:", String.valueOf((memoryInfo.getTotalPss() - memoryInfo.getTotalPrivateDirty()) - memoryInfo.getTotalPrivateClean())));
                } else {
                    sb.append(String.format(locale, "%21s %8s\n", "System:", "~ " + (memoryInfo.getTotalPss() - memoryInfo.getTotalPrivateDirty())));
                }
                sb.append(String.format(locale, "%21s %8s\n", "TOTAL:", String.valueOf(memoryInfo.getTotalPss())));
            }
        } catch (Exception e) {
            XCrash.getLogger().i("xcrash", "Util getProcessMemoryInfo failed", e);
        }
        return sb.toString();
    }

    public static String getFileContent(String str) {
        return getFileContent(str, 0);
    }

    public static String getFileContent(String str, int i) {
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            try {
                try {
                    BufferedReader bufferedReader2 = new BufferedReader(new FileReader(str));
                    int i2 = 0;
                    while (true) {
                        try {
                            String readLine = bufferedReader2.readLine();
                            if (readLine == null) {
                                break;
                            }
                            String trim = readLine.trim();
                            if (trim.length() > 0) {
                                i2++;
                                if (i == 0 || i2 <= i) {
                                    sb.append("  ");
                                    sb.append(trim);
                                    sb.append("\n");
                                }
                            }
                        } catch (Exception e) {
                            e = e;
                            bufferedReader = bufferedReader2;
                            ILogger logger = XCrash.getLogger();
                            logger.i("xcrash", "Util getInfo(" + str + ") failed", e);
                            if (bufferedReader != null) {
                                bufferedReader.close();
                            }
                            return sb.toString();
                        } catch (Throwable th) {
                            th = th;
                            bufferedReader = bufferedReader2;
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (Exception unused) {
                                }
                            }
                            throw th;
                        }
                    }
                    if (i > 0 && i2 > i) {
                        sb.append("  ......\n");
                        sb.append("  (number of records: ");
                        sb.append(i2);
                        sb.append(")\n");
                    }
                    bufferedReader2.close();
                } catch (Exception unused2) {
                    return sb.toString();
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static boolean checkAndCreateDir(String str) {
        File file = new File(str);
        try {
            if (!file.exists()) {
                file.mkdirs();
                return file.exists() && file.isDirectory();
            }
            return file.isDirectory();
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean checkProcessAnrState(Context context, long j) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager == null) {
            return false;
        }
        int myPid = Process.myPid();
        long j2 = j / 500;
        for (int i = 0; i < j2; i++) {
            List<ActivityManager.ProcessErrorStateInfo> processesInErrorState = activityManager.getProcessesInErrorState();
            if (processesInErrorState != null) {
                for (ActivityManager.ProcessErrorStateInfo processErrorStateInfo : processesInErrorState) {
                    if (processErrorStateInfo.pid == myPid && processErrorStateInfo.condition == 2) {
                        return true;
                    }
                }
            }
            try {
                Thread.sleep(500L);
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static String getLogHeader(Date date, Date date2, String str, String str2, String str3) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        StringBuilder sb = new StringBuilder();
        sb.append("*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***\nTombstone maker: 'xCrash 2.4.9'\nCrash type: '");
        sb.append(str);
        sb.append("'\nStart time: '");
        sb.append(simpleDateFormat.format(date));
        sb.append("'\nCrash time: '");
        sb.append(simpleDateFormat.format(date2));
        sb.append("'\nApp ID: '");
        sb.append(str2);
        sb.append("'\nApp version: '");
        sb.append(str3);
        sb.append("'\nRooted: '");
        sb.append(isRoot() ? "Yes" : "No");
        sb.append("'\nAPI level: '");
        sb.append(Build.VERSION.SDK_INT);
        sb.append("'\nOS version: '");
        sb.append(Build.VERSION.RELEASE);
        sb.append("'\nABI list: '");
        sb.append(getAbiList());
        sb.append("'\nManufacturer: '");
        sb.append(Build.MANUFACTURER);
        sb.append("'\nBrand: '");
        sb.append(Build.BRAND);
        sb.append("'\nModel: '");
        sb.append(Build.MODEL);
        sb.append("'\nBuild fingerprint: '");
        sb.append(Build.FINGERPRINT);
        sb.append("'\n");
        return sb.toString();
    }

    public static String getMemoryInfo() {
        return "memory info:\n System Summary (From: /proc/meminfo)\n" + getFileContent("/proc/meminfo") + "-\n Process Status (From: /proc/PID/status)\n" + getFileContent("/proc/self/status") + "-\n Process Limits (From: /proc/PID/limits)\n" + getFileContent("/proc/self/limits") + "-\n" + getProcessMemoryInfo() + "\n";
    }

    public static String getNetworkInfo() {
        if (Build.VERSION.SDK_INT >= 29) {
            return "network info:\nNot supported on Android Q (API level 29) and later.\n\n";
        }
        return "network info:\n TCP over IPv4 (From: /proc/PID/net/tcp)\n" + getFileContent("/proc/self/net/tcp", 1024) + "-\n TCP over IPv6 (From: /proc/PID/net/tcp6)\n" + getFileContent("/proc/self/net/tcp6", 1024) + "-\n UDP over IPv4 (From: /proc/PID/net/udp)\n" + getFileContent("/proc/self/net/udp", 1024) + "-\n UDP over IPv6 (From: /proc/PID/net/udp6)\n" + getFileContent("/proc/self/net/udp6", 1024) + "-\n ICMP in IPv4 (From: /proc/PID/net/icmp)\n" + getFileContent("/proc/self/net/icmp", 256) + "-\n ICMP in IPv6 (From: /proc/PID/net/icmp6)\n" + getFileContent("/proc/self/net/icmp6", 256) + "-\n UNIX domain (From: /proc/PID/net/unix)\n" + getFileContent("/proc/self/net/unix", 256) + "\n";
    }

    public static String getFds() {
        StringBuilder sb = new StringBuilder("open files:\n");
        try {
            File[] listFiles = new File("/proc/self/fd").listFiles(new FilenameFilter() { // from class: xcrash.Util.1
                @Override // java.io.FilenameFilter
                public boolean accept(File file, String str) {
                    return TextUtils.isDigitsOnly(str);
                }
            });
            if (listFiles != null) {
                int i = 0;
                for (File file : listFiles) {
                    String str = null;
                    try {
                        if (Build.VERSION.SDK_INT >= 21) {
                            str = Os.readlink(file.getAbsolutePath());
                        } else {
                            str = file.getCanonicalPath();
                        }
                    } catch (Exception unused) {
                    }
                    sb.append("    fd ");
                    sb.append(file.getName());
                    sb.append(": ");
                    sb.append(TextUtils.isEmpty(str) ? "???" : str.trim());
                    sb.append('\n');
                    i++;
                    if (i > 1024) {
                        break;
                    }
                }
                if (listFiles.length > 1024) {
                    sb.append("    ......\n");
                }
                sb.append("    (number of FDs: ");
                sb.append(listFiles.length);
                sb.append(")\n");
            }
        } catch (Exception unused2) {
        }
        sb.append('\n');
        return sb.toString();
    }

    public static String getLogcat(int i, int i2, int i3) {
        int myPid = Process.myPid();
        StringBuilder sb = new StringBuilder();
        sb.append("logcat:\n");
        if (i > 0) {
            getLogcatByBufferName(myPid, sb, a.d, i, 'D');
        }
        if (i2 > 0) {
            getLogcatByBufferName(myPid, sb, "system", i2, 'W');
        }
        if (i3 > 0) {
            getLogcatByBufferName(myPid, sb, j.b, i2, 'I');
        }
        sb.append("\n");
        return sb.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x00e9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void getLogcatByBufferName(int r9, java.lang.StringBuilder r10, java.lang.String r11, int r12, char r13) {
        /*
            Method dump skipped, instructions count: 237
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: xcrash.Util.getLogcatByBufferName(int, java.lang.StringBuilder, java.lang.String, int, char):void");
    }
}
