package cn.kuaipan.android.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import java.util.Locale;

/* loaded from: classes.dex */
public class ContextUtils {
    public static volatile Context sMyContext;

    public static void init(Context context) {
        if (context != null) {
            sMyContext = context;
        }
    }

    public static Context getContext() {
        Context context = sMyContext;
        if (context == null) {
            synchronized (ContextUtils.class) {
                context = sMyContext;
                if (context == null) {
                    try {
                        context = (Context) JavaCalls.callMethod(JavaCalls.callStaticMethod("android.app.ActivityThread", "currentActivityThread", new Object[0]), "getApplication", new Object[0]);
                    } catch (Exception unused) {
                    }
                    if (context == null) {
                        throw new RuntimeException("My Application havn't be call onCreate by Framework.");
                    }
                    sMyContext = context;
                }
            }
        }
        return context;
    }

    public static String getAppVersion(Context context) {
        String packageName = context.getPackageName();
        try {
            return context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.w("ContextUtils", "Exception when retrieving package:" + packageName);
            return null;
        }
    }

    public static String getFrameworkVersion() {
        StringBuilder sb = new StringBuilder();
        String str = Build.VERSION.RELEASE;
        if (str.length() > 0) {
            sb.append(str);
        } else {
            sb.append("1.0");
        }
        sb.append("; ");
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        String language = locale.getLanguage();
        if (language != null) {
            sb.append(language.toLowerCase());
            String country = locale.getCountry();
            if (country != null) {
                sb.append("-");
                sb.append(country.toLowerCase());
            }
        } else {
            sb.append("en");
        }
        if ("REL".equals(Build.VERSION.CODENAME)) {
            String str2 = Build.MODEL;
            if (str2.length() > 0) {
                sb.append("; ");
                sb.append(str2);
            }
        }
        String str3 = Build.ID;
        if (str3.length() > 0) {
            sb.append(" Build/");
            sb.append(str3);
        }
        return sb.toString();
    }
}
