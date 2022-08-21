package ch.qos.logback.core.helpers;

import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class Transform {
    private static final String CDATA_EMBEDED_END = "]]>]]&gt;<![CDATA[";
    private static final String CDATA_END = "]]>";
    private static final int CDATA_END_LEN = 3;
    private static final String CDATA_PSEUDO_END = "]]&gt;";
    private static final String CDATA_START = "<![CDATA[";
    private static final Pattern UNSAFE_XML_CHARS = Pattern.compile("[\u0000-\b\u000b\f\u000e-\u001f<>&'\"]");

    public static String escapeTags(String str) {
        return (str == null || str.length() == 0 || !UNSAFE_XML_CHARS.matcher(str).find()) ? str : escapeTags(new StringBuffer(str));
    }

    public static String escapeTags(StringBuffer stringBuffer) {
        for (int i = 0; i < stringBuffer.length(); i++) {
            char charAt = stringBuffer.charAt(i);
            if (charAt != '\t' && charAt != '\n' && charAt != '\r') {
                if (charAt == '\"') {
                    stringBuffer.replace(i, i + 1, "&quot;");
                } else if (charAt == '<') {
                    stringBuffer.replace(i, i + 1, "&lt;");
                } else if (charAt == '>') {
                    stringBuffer.replace(i, i + 1, "&gt;");
                } else if (charAt == '&') {
                    stringBuffer.replace(i, i + 1, "&amp;");
                } else if (charAt == '\'') {
                    stringBuffer.replace(i, i + 1, "&#39;");
                } else if (charAt < ' ') {
                    stringBuffer.replace(i, i + 1, "�");
                }
            }
        }
        return stringBuffer.toString();
    }

    public static void appendEscapingCDATA(StringBuilder sb, String str) {
        if (str == null) {
            return;
        }
        int indexOf = str.indexOf(CDATA_END);
        if (indexOf < 0) {
            sb.append(str);
            return;
        }
        int i = 0;
        while (indexOf > -1) {
            sb.append(str.substring(i, indexOf));
            sb.append(CDATA_EMBEDED_END);
            i = CDATA_END_LEN + indexOf;
            if (i >= str.length()) {
                return;
            }
            indexOf = str.indexOf(CDATA_END, i);
        }
        sb.append(str.substring(i));
    }
}
