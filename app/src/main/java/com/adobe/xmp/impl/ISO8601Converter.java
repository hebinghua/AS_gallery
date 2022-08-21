package com.adobe.xmp.impl;

import ch.qos.logback.core.CoreConstants;
import com.adobe.xmp.XMPDateTime;
import com.adobe.xmp.XMPException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/* loaded from: classes.dex */
public final class ISO8601Converter {
    public static XMPDateTime parse(String str) throws XMPException {
        return parse(str, new XMPDateTimeImpl());
    }

    /* JADX WARN: Removed duplicated region for block: B:127:0x0209 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:128:0x020a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.adobe.xmp.XMPDateTime parse(java.lang.String r13, com.adobe.xmp.XMPDateTime r14) throws com.adobe.xmp.XMPException {
        /*
            Method dump skipped, instructions count: 546
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.adobe.xmp.impl.ISO8601Converter.parse(java.lang.String, com.adobe.xmp.XMPDateTime):com.adobe.xmp.XMPDateTime");
    }

    public static String render(XMPDateTime xMPDateTime) {
        StringBuffer stringBuffer = new StringBuffer();
        DecimalFormat decimalFormat = new DecimalFormat("0000", new DecimalFormatSymbols(Locale.ENGLISH));
        stringBuffer.append(decimalFormat.format(xMPDateTime.getYear()));
        if (xMPDateTime.getMonth() == 0) {
            return stringBuffer.toString();
        }
        decimalFormat.applyPattern("'-'00");
        stringBuffer.append(decimalFormat.format(xMPDateTime.getMonth()));
        if (xMPDateTime.getDay() == 0) {
            return stringBuffer.toString();
        }
        stringBuffer.append(decimalFormat.format(xMPDateTime.getDay()));
        if (xMPDateTime.getHour() != 0 || xMPDateTime.getMinute() != 0 || xMPDateTime.getSecond() != 0 || xMPDateTime.getNanoSecond() != 0 || (xMPDateTime.getTimeZone() != null && xMPDateTime.getTimeZone().getRawOffset() != 0)) {
            stringBuffer.append('T');
            decimalFormat.applyPattern("00");
            stringBuffer.append(decimalFormat.format(xMPDateTime.getHour()));
            stringBuffer.append(CoreConstants.COLON_CHAR);
            stringBuffer.append(decimalFormat.format(xMPDateTime.getMinute()));
            if (xMPDateTime.getSecond() != 0 || xMPDateTime.getNanoSecond() != 0) {
                decimalFormat.applyPattern(":00.#########");
                stringBuffer.append(decimalFormat.format(xMPDateTime.getSecond() + (xMPDateTime.getNanoSecond() / 1.0E9d)));
            }
            if (xMPDateTime.getTimeZone() != null) {
                int offset = xMPDateTime.getTimeZone().getOffset(xMPDateTime.getCalendar().getTimeInMillis());
                if (offset == 0) {
                    stringBuffer.append('Z');
                } else {
                    int i = offset / 3600000;
                    int abs = Math.abs((offset % 3600000) / 60000);
                    decimalFormat.applyPattern("+00;-00");
                    stringBuffer.append(decimalFormat.format(i));
                    decimalFormat.applyPattern(":00");
                    stringBuffer.append(decimalFormat.format(abs));
                }
            }
        }
        return stringBuffer.toString();
    }
}
