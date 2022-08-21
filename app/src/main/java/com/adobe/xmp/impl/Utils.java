package com.adobe.xmp.impl;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes.dex */
public class Utils {
    public static boolean[] xmlNameChars;
    public static boolean[] xmlNameStartChars;

    public static boolean isControlChar(char c) {
        return ((c > 31 && c != 127) || c == '\t' || c == '\n' || c == '\r') ? false : true;
    }

    static {
        initCharTables();
    }

    public static String normalizeLangValue(String str) {
        if ("x-default".equals(str)) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer();
        int i = 1;
        for (int i2 = 0; i2 < str.length(); i2++) {
            char charAt = str.charAt(i2);
            if (charAt != ' ') {
                if (charAt == '-' || charAt == '_') {
                    stringBuffer.append(CoreConstants.DASH_CHAR);
                    i++;
                } else if (i != 2) {
                    stringBuffer.append(Character.toLowerCase(str.charAt(i2)));
                } else {
                    stringBuffer.append(Character.toUpperCase(str.charAt(i2)));
                }
            }
        }
        return stringBuffer.toString();
    }

    public static String[] splitNameAndValue(String str) {
        int indexOf = str.indexOf(61);
        String substring = str.substring(str.charAt(1) == '?' ? 2 : 1, indexOf);
        int i = indexOf + 1;
        char charAt = str.charAt(i);
        int i2 = i + 1;
        int length = str.length() - 2;
        StringBuffer stringBuffer = new StringBuffer(length - indexOf);
        while (i2 < length) {
            stringBuffer.append(str.charAt(i2));
            i2++;
            if (str.charAt(i2) == charAt) {
                i2++;
            }
        }
        return new String[]{substring, stringBuffer.toString()};
    }

    public static boolean checkUUIDFormat(String str) {
        if (str == null) {
            return false;
        }
        int i = 0;
        int i2 = 0;
        boolean z = true;
        while (i < str.length()) {
            if (str.charAt(i) == '-') {
                i2++;
                z = z && (i == 8 || i == 13 || i == 18 || i == 23);
            }
            i++;
        }
        return z && 4 == i2 && 36 == i;
    }

    public static boolean isXMLName(String str) {
        if (str.length() <= 0 || isNameStartChar(str.charAt(0))) {
            for (int i = 1; i < str.length(); i++) {
                if (!isNameChar(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isXMLNameNS(String str) {
        if (str.length() <= 0 || (isNameStartChar(str.charAt(0)) && str.charAt(0) != ':')) {
            for (int i = 1; i < str.length(); i++) {
                if (!isNameChar(str.charAt(i)) || str.charAt(i) == ':') {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isNameStartChar(char c) {
        return c > 255 || xmlNameStartChars[c];
    }

    public static boolean isNameChar(char c) {
        return c > 255 || xmlNameChars[c];
    }

    public static void initCharTables() {
        xmlNameChars = new boolean[256];
        xmlNameStartChars = new boolean[256];
        char c = 0;
        while (true) {
            boolean[] zArr = xmlNameChars;
            if (c < zArr.length) {
                boolean z = true;
                xmlNameStartChars[c] = ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || c == ':' || c == '_' || ((192 <= c && c <= 214) || (216 <= c && c <= 246));
                if (('a' > c || c > 'z') && (('A' > c || c > 'Z') && (('0' > c || c > '9') && c != ':' && c != '_' && c != '-' && c != '.' && c != 183 && ((192 > c || c > 214) && (216 > c || c > 246))))) {
                    z = false;
                }
                zArr[c] = z;
                c = (char) (c + 1);
            } else {
                return;
            }
        }
    }
}
