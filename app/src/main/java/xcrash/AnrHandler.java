package xcrash;

import android.content.Context;
import android.os.Build;
import android.os.FileObserver;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes3.dex */
public class AnrHandler {
    public static final AnrHandler instance = new AnrHandler();
    public String appId;
    public String appVersion;
    public ICrashCallback callback;
    public boolean checkProcessState;
    public Context ctx;
    public boolean dumpFds;
    public boolean dumpNetworkInfo;
    public String logDir;
    public int logcatEventsLines;
    public int logcatMainLines;
    public int logcatSystemLines;
    public int pid;
    public String processName;
    public final Date startTime = new Date();
    public final Pattern patPidTime = Pattern.compile("^-----\\spid\\s(\\d+)\\sat\\s(.*)\\s-----$");
    public final Pattern patProcessName = Pattern.compile("^Cmd\\sline:\\s+(.*)$");
    public final long anrTimeoutMs = 15000;
    public long lastTime = 0;
    public FileObserver fileObserver = null;

    public static AnrHandler getInstance() {
        return instance;
    }

    public void initialize(Context context, int i, String str, String str2, String str3, String str4, boolean z, int i2, int i3, int i4, boolean z2, boolean z3, ICrashCallback iCrashCallback) {
        if (Build.VERSION.SDK_INT >= 21) {
            return;
        }
        this.ctx = context;
        this.pid = i;
        if (TextUtils.isEmpty(str)) {
            str = "unknown";
        }
        this.processName = str;
        this.appId = str2;
        this.appVersion = str3;
        this.logDir = str4;
        this.checkProcessState = z;
        this.logcatSystemLines = i2;
        this.logcatEventsLines = i3;
        this.logcatMainLines = i4;
        this.dumpFds = z2;
        this.dumpNetworkInfo = z3;
        this.callback = iCrashCallback;
        FileObserver fileObserver = new FileObserver("/data/anr/", 8) { // from class: xcrash.AnrHandler.1
            @Override // android.os.FileObserver
            public void onEvent(int i5, String str5) {
                if (str5 != null) {
                    try {
                        String str6 = "/data/anr/" + str5;
                        if (!str6.contains("trace")) {
                            return;
                        }
                        AnrHandler.this.handleAnr(str6);
                    } catch (Exception e) {
                        XCrash.getLogger().e("xcrash", "AnrHandler fileObserver onEvent failed", e);
                    }
                }
            }
        };
        this.fileObserver = fileObserver;
        try {
            fileObserver.startWatching();
        } catch (Exception e) {
            this.fileObserver = null;
            XCrash.getLogger().e("xcrash", "AnrHandler fileObserver startWatching failed", e);
        }
    }

    public void notifyJavaCrashed() {
        FileObserver fileObserver = this.fileObserver;
        if (fileObserver != null) {
            try {
                try {
                    fileObserver.stopWatching();
                } catch (Exception e) {
                    XCrash.getLogger().e("xcrash", "AnrHandler fileObserver stopWatching failed", e);
                }
            } finally {
                this.fileObserver = null;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:66:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x010b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0113 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:94:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void handleAnr(java.lang.String r12) {
        /*
            Method dump skipped, instructions count: 294
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: xcrash.AnrHandler.handleAnr(java.lang.String):void");
    }

    public final String getEmergency(Date date, String str) {
        return Util.getLogHeader(this.startTime, date, "anr", this.appId, this.appVersion) + "pid: " + this.pid + "  >>> " + this.processName + " <<<\n\n--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\n" + str + "\n+++ +++ +++ +++ +++ +++ +++ +++ +++ +++ +++ +++ +++ +++ +++ +++\n\n";
    }

    public final String getTrace(String str, long j) {
        BufferedReader bufferedReader;
        Date parse;
        String group;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader2 = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(str));
            boolean z = false;
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    } else if (!z && readLine.startsWith("----- pid ")) {
                        Matcher matcher = this.patPidTime.matcher(readLine);
                        if (matcher.find() && matcher.groupCount() == 2) {
                            String group2 = matcher.group(1);
                            String group3 = matcher.group(2);
                            if (group2 != null && group3 != null && this.pid == Integer.parseInt(group2) && (parse = simpleDateFormat.parse(group3)) != null && Math.abs(parse.getTime() - j) <= 15000) {
                                String readLine2 = bufferedReader.readLine();
                                if (readLine2 == null) {
                                    break;
                                }
                                Matcher matcher2 = this.patProcessName.matcher(readLine2);
                                if (matcher2.find() && matcher2.groupCount() == 1 && (group = matcher2.group(1)) != null && group.equals(this.processName)) {
                                    sb.append(readLine2);
                                    sb.append('\n');
                                    sb.append("Mode: Watching /data/anr/*\n");
                                    z = true;
                                }
                            }
                        }
                    } else if (!z) {
                        continue;
                    } else if (readLine.startsWith("----- end ")) {
                        break;
                    } else {
                        sb.append(readLine);
                        sb.append('\n');
                    }
                } catch (Exception unused) {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (Exception unused2) {
                        }
                    }
                    return null;
                } catch (Throwable th) {
                    th = th;
                    bufferedReader2 = bufferedReader;
                    if (bufferedReader2 != null) {
                        try {
                            bufferedReader2.close();
                        } catch (Exception unused3) {
                        }
                    }
                    throw th;
                }
            }
            String sb2 = sb.toString();
            try {
                bufferedReader.close();
            } catch (Exception unused4) {
            }
            return sb2;
        } catch (Exception unused5) {
            bufferedReader = null;
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
