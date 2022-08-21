package com.miui.gallery.request;

import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.CookieManager;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;

/* loaded from: classes2.dex */
public class HostManager {
    public static final String MICLOUD_GALLERY_WEB_URL_BASE;
    public static final String URL_SWITCH_FILE;

    static {
        String pathInPrimaryStorage = StorageUtils.getPathInPrimaryStorage(Environment.DIRECTORY_DOWNLOADS + "/url_daily");
        URL_SWITCH_FILE = pathInPrimaryStorage;
        MICLOUD_GALLERY_WEB_URL_BASE = new File(pathInPrimaryStorage).exists() ? "https://daily.i.mi.com/mobile/gallery" : "https://i.mi.com/mobile/gallery";
    }

    public static String getTrashBinUrl() {
        return MICLOUD_GALLERY_WEB_URL_BASE + "/trash";
    }

    public static void clearCookies() {
        try {
            if (!CookieManager.getInstance().hasCookies()) {
                return;
            }
            CookieManager.getInstance().removeAllCookie();
        } catch (Exception e) {
            DefaultLogger.e("HostManager", e);
        }
    }

    public static boolean isInternalUrl(String str) {
        Uri parse;
        String host;
        if (!TextUtils.isEmpty(str) && (host = (parse = Uri.parse(str)).getHost()) != null) {
            String scheme = parse.getScheme();
            if (!"http".equals(scheme) && !"https".equals(scheme)) {
                return false;
            }
            return host.endsWith(".miui.com") || host.endsWith(".mi.com") || host.endsWith(".xiaomi.com") || host.endsWith(".xiaomi.net");
        }
        return false;
    }

    public static boolean isGalleryUrl(String str) {
        if (isInternalUrl(str)) {
            String path = Uri.parse(str).getPath();
            if (TextUtils.isEmpty(path)) {
                return false;
            }
            return path.contains("/mobile/gallery");
        }
        return false;
    }
}
