package com.miui.gallery.util;

import android.content.Context;
import android.net.Uri;
import ch.qos.logback.core.CoreConstants;

/* loaded from: classes2.dex */
public class UriUtils {
    public static String toSafeString(Uri uri) {
        String scheme = uri.getScheme();
        String schemeSpecificPart = uri.getSchemeSpecificPart();
        if (scheme != null) {
            if (scheme.equalsIgnoreCase("tel") || scheme.equalsIgnoreCase("sip") || scheme.equalsIgnoreCase("sms") || scheme.equalsIgnoreCase("smsto") || scheme.equalsIgnoreCase("mailto")) {
                StringBuilder sb = new StringBuilder(64);
                sb.append(scheme);
                sb.append(CoreConstants.COLON_CHAR);
                if (schemeSpecificPart != null) {
                    for (int i = 0; i < schemeSpecificPart.length(); i++) {
                        char charAt = schemeSpecificPart.charAt(i);
                        if (charAt == '-' || charAt == '@' || charAt == '.') {
                            sb.append(charAt);
                        } else {
                            sb.append('x');
                        }
                    }
                }
                return sb.toString();
            } else if (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https") || scheme.equalsIgnoreCase("ftp")) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("//");
                String str = "";
                sb2.append(uri.getHost() != null ? uri.getHost() : str);
                if (uri.getPort() != -1) {
                    str = ":" + uri.getPort();
                }
                sb2.append(str);
                sb2.append("/...");
                schemeSpecificPart = sb2.toString();
            }
        }
        StringBuilder sb3 = new StringBuilder(64);
        if (scheme != null) {
            sb3.append(scheme);
            sb3.append(CoreConstants.COLON_CHAR);
        }
        if (schemeSpecificPart != null) {
            sb3.append(schemeSpecificPart);
        }
        return sb3.toString();
    }

    public static String getFilePathWithUri(Context context, Uri uri) {
        if (uri == null) {
            return "";
        }
        Scheme ofUri = Scheme.ofUri(uri.toString());
        Scheme scheme = Scheme.FILE;
        if (ofUri == scheme) {
            return scheme.crop(uri.toString());
        }
        return ofUri == Scheme.CONTENT ? ContentUtils.getValidFilePathForContentUri(context, uri) : "";
    }
}
