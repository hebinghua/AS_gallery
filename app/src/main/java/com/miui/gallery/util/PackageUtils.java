package com.miui.gallery.util;

import android.content.pm.PackageManager;
import android.text.TextUtils;
import androidx.core.util.Pair;
import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public class PackageUtils {
    public static final Object CACHE_LOCK = new Object();
    public static volatile LoadingCache<String, Pair<String, Locale>> sAppNamesCache;

    public static String getAppNameForScreenshot(String str) {
        int lastIndexOf;
        int lastIndexOf2;
        if (TextUtils.isEmpty(str) || !str.startsWith("Screenshot") || (lastIndexOf2 = str.lastIndexOf(46)) <= (lastIndexOf = str.lastIndexOf(95))) {
            return null;
        }
        String substring = str.substring(lastIndexOf + 1, lastIndexOf2);
        ensureAppNamesCache();
        Pair<String, Locale> unchecked = sAppNamesCache.getUnchecked(substring);
        String str2 = unchecked.first;
        if ("MiuiGallery:Absent" == str2) {
            DefaultLogger.d("PackageUtils", "App name of package is absent");
            return null;
        }
        DefaultLogger.d("PackageUtils", "App name of package is [%s]", str2);
        return unchecked.first;
    }

    public static String gePackageNameForScreenshot(String str) {
        int lastIndexOf;
        int lastIndexOf2;
        if (TextUtils.isEmpty(str) || !str.startsWith("Screenshot") || (lastIndexOf2 = str.lastIndexOf(46)) <= (lastIndexOf = str.lastIndexOf(95))) {
            return null;
        }
        return str.substring(lastIndexOf + 1, lastIndexOf2);
    }

    public static void ensureAppNamesCache() {
        if (sAppNamesCache == null) {
            synchronized (CACHE_LOCK) {
                if (sAppNamesCache == null) {
                    CacheBuilder<Object, Object> newBuilder = CacheBuilder.newBuilder();
                    TimeUnit timeUnit = TimeUnit.MINUTES;
                    sAppNamesCache = newBuilder.refreshAfterWrite(2L, timeUnit).expireAfterWrite(15L, timeUnit).maximumSize(200L).build(new CacheLoader<String, Pair<String, Locale>>() { // from class: com.miui.gallery.util.PackageUtils.1
                        @Override // com.google.common.cache.CacheLoader
                        public Pair<String, Locale> load(String str) {
                            return Pair.create(PackageUtils.internalGetAppNameByPackage(str), Locale.getDefault());
                        }

                        @Override // com.google.common.cache.CacheLoader
                        public ListenableFuture<Pair<String, Locale>> reload(String str, Pair<String, Locale> pair) throws Exception {
                            Preconditions.checkNotNull(str);
                            Preconditions.checkNotNull(pair);
                            Locale locale = Locale.getDefault();
                            if ("MiuiGallery:Absent".equals(pair.first) || !Objects.equals(locale, pair.second)) {
                                Pair<String, Locale> load = load(str);
                                DefaultLogger.d("PackageUtils", "reload for pkg [%s], oldValue: %s, newValue: %s", str, pair, load);
                                pair = load;
                            }
                            return Futures.immediateFuture(pair);
                        }
                    });
                }
            }
        }
    }

    public static String getAppNameByPackage(String str) {
        ensureAppNamesCache();
        try {
            Pair<String, Locale> pair = sAppNamesCache.get(str);
            if (pair == null) {
                return null;
            }
            if (TextUtils.equals(pair.second.getLanguage(), Locale.getDefault().getLanguage())) {
                return "MiuiGallery:Absent".equals(pair.first) ? "" : pair.first;
            }
            sAppNamesCache.refresh(str);
            return getAppNameByPackage(str);
        } catch (Exception e) {
            DefaultLogger.e("PackageUtils", e);
            return null;
        }
    }

    public static String internalGetAppNameByPackage(String str) {
        try {
            PackageManager packageManager = StaticContext.sGetAndroidContext().getPackageManager();
            return packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 128)).toString();
        } catch (PackageManager.NameNotFoundException unused) {
            DefaultLogger.d("PackageUtils", "Package not found: %s", str);
            return "MiuiGallery:Absent";
        } catch (Exception e) {
            DefaultLogger.e("PackageUtils", e);
            return "MiuiGallery:Absent";
        }
    }

    public static String getAppVersionName(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                return StaticContext.sGetAndroidContext().getPackageManager().getPackageInfo(str, 16384).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
