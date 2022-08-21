package com.miui.gallery.util;

import android.text.TextUtils;
import android.util.Pair;
import ch.qos.logback.classic.spi.CallerData;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.keyczar.Keyczar;

/* loaded from: classes2.dex */
public class HttpUtils {
    public static String appendUrl(String str, Pair<String, String>... pairArr) throws UnsupportedEncodingException {
        if (!TextUtils.isEmpty(str)) {
            if (pairArr == null) {
                return str;
            }
            StringBuilder sb = new StringBuilder(str);
            if (str.contains(CallerData.NA)) {
                if (!str.endsWith(CallerData.NA) && !str.endsWith("&")) {
                    sb.append("&");
                }
            } else {
                sb.append(CallerData.NA);
            }
            for (int i = 0; i < pairArr.length; i++) {
                if (i != 0) {
                    sb.append("&");
                }
                Pair<String, String> pair = pairArr[i];
                sb.append((String) pair.first);
                sb.append("=");
                sb.append(URLEncoder.encode((String) pair.second, Keyczar.DEFAULT_ENCODING));
            }
            return sb.toString();
        }
        throw new NullPointerException("url is not allowed empty");
    }

    public static String appendAppLifecycleParameter(String str) {
        try {
            return appendUrl(str, getAppLifecycleParameter());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    public static Pair<String, String> getAppLifecycleParameter() {
        return new Pair<>("_backend", String.valueOf(!ProcessUtils.isAppInForeground()));
    }

    public static Pair<String, String> getApkVersionParameter() {
        return new Pair<>("_apkversion", PackageUtils.getAppVersionName("com.miui.gallery"));
    }
}
