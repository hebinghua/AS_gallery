package androidx.room.util;

import ch.qos.logback.classic.spi.CallerData;

/* loaded from: classes.dex */
public class StringUtil {
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static StringBuilder newStringBuilder() {
        return new StringBuilder();
    }

    public static void appendPlaceholders(StringBuilder sb, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            sb.append(CallerData.NA);
            if (i2 < i - 1) {
                sb.append(",");
            }
        }
    }
}
