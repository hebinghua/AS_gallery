package ch.qos.logback.classic.spi;

import ch.qos.logback.core.CoreConstants;
import java.util.List;

/* loaded from: classes.dex */
public class CallerData {
    public static final String CALLER_DATA_NA = "?#?:?" + CoreConstants.LINE_SEPARATOR;
    public static final StackTraceElement[] EMPTY_CALLER_DATA_ARRAY = new StackTraceElement[0];
    public static final int LINE_NA = -1;
    private static final String LOG4J_CATEGORY = "org.apache.log4j.Category";
    public static final String NA = "?";
    private static final String SLF4J_BOUNDARY = "org.slf4j.Logger";

    public static StackTraceElement[] extract(Throwable th, String str, int i, List<String> list) {
        if (th == null) {
            return null;
        }
        StackTraceElement[] stackTrace = th.getStackTrace();
        int i2 = -1;
        for (int i3 = 0; i3 < stackTrace.length; i3++) {
            if (isInFrameworkSpace(stackTrace[i3].getClassName(), str, list)) {
                i2 = i3 + 1;
            } else if (i2 != -1) {
                break;
            }
        }
        if (i2 == -1) {
            return EMPTY_CALLER_DATA_ARRAY;
        }
        int length = stackTrace.length - i2;
        if (i >= length) {
            i = length;
        }
        StackTraceElement[] stackTraceElementArr = new StackTraceElement[i];
        for (int i4 = 0; i4 < i; i4++) {
            stackTraceElementArr[i4] = stackTrace[i2 + i4];
        }
        return stackTraceElementArr;
    }

    public static boolean isInFrameworkSpace(String str, String str2, List<String> list) {
        return str.equals(str2) || str.equals(LOG4J_CATEGORY) || str.startsWith(SLF4J_BOUNDARY) || isInFrameworkSpaceList(str, list);
    }

    private static boolean isInFrameworkSpaceList(String str, List<String> list) {
        if (list == null) {
            return false;
        }
        for (String str2 : list) {
            if (str.startsWith(str2)) {
                return true;
            }
        }
        return false;
    }

    public static StackTraceElement naInstance() {
        return new StackTraceElement(NA, NA, NA, -1);
    }
}
