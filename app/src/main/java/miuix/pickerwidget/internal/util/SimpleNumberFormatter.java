package miuix.pickerwidget.internal.util;

import ch.qos.logback.core.CoreConstants;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Objects;
import miuix.core.util.Pools;

/* loaded from: classes3.dex */
public class SimpleNumberFormatter {
    public static Locale sLocale = Locale.getDefault();
    public static char sZeroDigit = new DecimalFormatSymbols(sLocale).getZeroDigit();

    public static String format(int i) {
        return format(-1, i);
    }

    public static String format(int i, int i2) {
        char zeroDigit = getZeroDigit(Locale.getDefault());
        String convertInt = convertInt(i, i2);
        return zeroDigit != '0' ? localizeDigits(zeroDigit, convertInt) : convertInt;
    }

    public static String convertInt(int i, int i2) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        if (i2 < 0) {
            i2 = -i2;
            i--;
            acquire.append(CoreConstants.DASH_CHAR);
        }
        if (i2 >= 10000) {
            String num = Integer.toString(i2);
            for (int length = num.length(); length < i; length++) {
                acquire.append('0');
            }
            acquire.append(num);
        } else {
            for (int i3 = i2 >= 1000 ? 4 : i2 >= 100 ? 3 : i2 >= 10 ? 2 : 1; i3 < i; i3++) {
                acquire.append('0');
            }
            acquire.append(i2);
        }
        String sb = acquire.toString();
        Pools.getStringBuilderPool().release(acquire);
        return sb;
    }

    public static String localizeDigits(char c, String str) {
        int length = str.length();
        int i = c - '0';
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str.charAt(i2);
            if (charAt >= '0' && charAt <= '9') {
                charAt = (char) (charAt + i);
            }
            acquire.append(charAt);
        }
        String sb = acquire.toString();
        Pools.getStringBuilderPool().release(acquire);
        return sb;
    }

    public static char getZeroDigit(Locale locale) {
        Objects.requireNonNull(locale, "locale == null");
        if (!locale.equals(sLocale)) {
            sZeroDigit = new DecimalFormatSymbols(locale).getZeroDigit();
            sLocale = locale;
        }
        return sZeroDigit;
    }
}
