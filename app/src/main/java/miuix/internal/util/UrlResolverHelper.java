package miuix.internal.util;

import android.net.Uri;
import android.text.TextUtils;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes3.dex */
public class UrlResolverHelper {
    public static final String[] MI_LIST = {".xiaomi.com", ".mi.com", ".miui.com", ".mipay.com"};
    public static final String[] WHITE_LIST = {".duokan.com", ".duokanbox.com", ".mijiayoupin.com"};
    public static final String[] WHITE_PACKAGE_LIST = {"com.xiaomi.channel", "com.duokan.reader", "com.duokan.hdreader", "com.duokan.fiction", "com.duokan.free", "com.xiaomi.router", "com.xiaomi.smarthome", "com.xiaomi.o2o", "com.xiaomi.shop", "com.xiaomi.jr", "com.xiaomi.jr.security", "com.xiaomi.mifisecurity", "com.xiaomi.loan", "com.xiaomi.loanx", "com.mi.credit.in", "com.mi.credit.id", "com.miui.miuibbs", "com.wali.live", "com.mi.live", "com.xiaomi.ab", "com.mfashiongallery.emag", "com.xiaomi.pass", "com.xiaomi.youpin", "com.mi.liveassistant", "com.xiangkan.android", "com.xiaomi.gamecenter", "com.xiaomi.vipaccount"};
    public static Set<String> sBrowserFallbackSchemeSet;
    public static Set<String> sFallbackSchemeSet;

    static {
        HashSet hashSet = new HashSet();
        sBrowserFallbackSchemeSet = hashSet;
        hashSet.add("mihttp");
        sBrowserFallbackSchemeSet.add("mihttps");
        HashSet hashSet2 = new HashSet();
        sFallbackSchemeSet = hashSet2;
        hashSet2.add("http");
        sFallbackSchemeSet.add("https");
        sFallbackSchemeSet.addAll(sBrowserFallbackSchemeSet);
    }

    public static boolean isMiUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Uri parse = Uri.parse(str);
        if (!"http".equals(parse.getScheme()) && !"https".equals(parse.getScheme())) {
            return false;
        }
        return isMiHost(parse.getHost());
    }

    public static boolean isMiHost(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (String str2 : MI_LIST) {
            if (str.endsWith(str2)) {
                return true;
            }
        }
        for (String str3 : WHITE_LIST) {
            if (str.endsWith(str3)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWhiteListPackage(String str) {
        for (String str2 : WHITE_PACKAGE_LIST) {
            if (str2.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBrowserFallbackScheme(String str) {
        return sBrowserFallbackSchemeSet.contains(str);
    }

    public static Uri getBrowserFallbackUri(String str) {
        return Uri.parse(str.substring(2));
    }

    public static String getFallbackParameter(Uri uri) {
        String fallbackParameter = getFallbackParameter(uri, 0, null);
        if (fallbackParameter != null) {
            if (sFallbackSchemeSet.contains(Uri.parse(fallbackParameter).getScheme())) {
                return fallbackParameter;
            }
        }
        return null;
    }

    public static String getFallbackParameter(Uri uri, int i, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("mifb");
        sb.append(i == 0 ? "" : Integer.valueOf(i));
        String queryParameter = uri.getQueryParameter(sb.toString());
        return queryParameter != null ? getFallbackParameter(uri, i + 1, queryParameter) : str;
    }
}
