package miui.cloud.common;

import android.os.Environment;
import android.util.Log;
import ch.qos.logback.core.joran.action.ActionConst;
import java.io.File;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Locale;

/* loaded from: classes3.dex */
public class XLogger {
    public static final String DEFAULT_LOGGER_CLASS = "miui.cloud.common.XLogger";
    public static volatile LogSender sLogSender = new LogSender() { // from class: miui.cloud.common.XLogger.1
        public final boolean sLogOnAdb = new File(Environment.getExternalStorageDirectory(), "log_on_adb").exists();

        @Override // miui.cloud.common.XLogger.LogSender
        public void sendLog(int i, String str, String str2) {
            if (this.sLogOnAdb) {
                Log.println(i, str, str2);
            }
        }
    };
    public static volatile int sDefLogLevel = 4;
    public static volatile int sOutputLogLevel = 2;
    public static volatile boolean sEnableCallStacktrace = true;

    /* loaded from: classes3.dex */
    public interface LogSender {
        void sendLog(int i, String str, String str2);
    }

    public static void logAtLevel(int i, Object... objArr) {
        logAtLevelImp(i, null, DEFAULT_LOGGER_CLASS, objArr);
    }

    public static void logAtLevelImp(int i, String str, String str2, Object... objArr) {
        LogSender logSender;
        Object obj;
        if (i >= sOutputLogLevel && (logSender = sLogSender) != null) {
            String str3 = sEnableCallStacktrace ? getCallerInfo(str2) + "--" : "";
            if (objArr.length == 1) {
                obj = getObjectString(objArr[0]);
            } else {
                StringBuilder sb = new StringBuilder();
                for (Object obj2 : objArr) {
                    if (sb.length() != 0) {
                        sb.append(", ");
                    }
                    sb.append(getObjectString(obj2));
                }
                obj = sb;
            }
            if (str == null) {
                str = "##XLogger##";
            }
            logSender.sendLog(i, str, str3 + obj);
        }
    }

    public static void loge(Object... objArr) {
        logAtLevel(6, objArr);
    }

    public static void error(String str, String str2, Object... objArr) {
        logAtLevelImp(6, str, DEFAULT_LOGGER_CLASS, formatStr(str2, objArr));
    }

    public static void info(String str, String str2, Object... objArr) {
        logAtLevelImp(4, str, DEFAULT_LOGGER_CLASS, formatStr(str2, objArr));
    }

    public static String formatStr(String str, Object... objArr) {
        try {
            String format = String.format(Locale.ENGLISH, str, objArr);
            if (objArr == null || objArr.length == 0) {
                return format;
            }
            Object obj = objArr[objArr.length - 1];
            if (!(obj instanceof Throwable)) {
                return format;
            }
            return format + getThrowableString((Throwable) obj);
        } catch (IllegalFormatException e) {
            return str + " " + Arrays.toString(objArr) + " : " + getThrowableString(e);
        }
    }

    public static String getObjectString(Object obj) {
        if (obj == null) {
            return ActionConst.NULL;
        }
        if (obj instanceof Throwable) {
            return getThrowableString((Throwable) obj);
        }
        return obj.toString();
    }

    public static String getThrowableString(Throwable th) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10 && th != null; i++) {
            if (sb.length() != 0) {
                sb.append(" *Caused by* ");
            }
            sb.append(th.toString());
            th = th.getCause();
        }
        if (th != null) {
            sb.append(" *and more...*");
        }
        return sb.toString();
    }

    public static String getCallerInfo(String str) {
        StackTraceElement[] stackTrace;
        boolean z = false;
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            if (stackTraceElement.getClassName().equals(str)) {
                z = true;
            } else if (z) {
                return String.format("%s::%s@%s:%s, thread:%s", stackTraceElement.getClassName(), stackTraceElement.getMethodName(), stackTraceElement.getFileName(), Integer.valueOf(stackTraceElement.getLineNumber()), Long.valueOf(Thread.currentThread().getId()));
            }
        }
        return "";
    }
}
