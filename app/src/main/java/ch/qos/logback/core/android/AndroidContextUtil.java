package ch.qos.logback.core.android;

import android.annotation.TargetApi;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.CoreConstants;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/* loaded from: classes.dex */
public class AndroidContextUtil {
    private ContextWrapper context;

    public AndroidContextUtil() {
        this(getContext());
    }

    public AndroidContextUtil(ContextWrapper contextWrapper) {
        this.context = contextWrapper;
    }

    public void setupProperties(LoggerContext loggerContext) {
        Properties properties = new Properties();
        properties.setProperty(CoreConstants.DATA_DIR_KEY, getFilesDirectoryPath());
        String mountedExternalStorageDirectoryPath = getMountedExternalStorageDirectoryPath();
        if (mountedExternalStorageDirectoryPath != null) {
            properties.setProperty(CoreConstants.EXT_DIR_KEY, mountedExternalStorageDirectoryPath);
        }
        properties.setProperty(CoreConstants.PACKAGE_NAME_KEY, getPackageName());
        properties.setProperty(CoreConstants.VERSION_CODE_KEY, getVersionCode());
        properties.setProperty(CoreConstants.VERSION_NAME_KEY, getVersionName());
        loggerContext.putProperties(properties);
    }

    public static ContextWrapper getContext() {
        try {
            Class<?> cls = Class.forName("android.app.AppGlobals");
            return (ContextWrapper) cls.getDeclaredMethod("getInitialApplication", new Class[0]).invoke(cls, new Object[0]);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException unused) {
            return null;
        }
    }

    public String getMountedExternalStorageDirectoryPath() {
        String externalStorageState = Environment.getExternalStorageState();
        if (externalStorageState.equals("mounted") || externalStorageState.equals("mounted_ro")) {
            return getExternalStorageDirectoryPath();
        }
        return null;
    }

    @TargetApi(8)
    public String getExternalStorageDirectoryPath() {
        if (Build.VERSION.SDK_INT >= 29) {
            return getExternalFilesDirectoryPath();
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    @TargetApi(8)
    public String getExternalFilesDirectoryPath() {
        ContextWrapper contextWrapper = this.context;
        return contextWrapper != null ? absPath(contextWrapper.getExternalFilesDir(null)) : "";
    }

    public String getCacheDirectoryPath() {
        ContextWrapper contextWrapper = this.context;
        return contextWrapper != null ? absPath(contextWrapper.getCacheDir()) : "";
    }

    public String getExternalCacheDirectoryPath() {
        ContextWrapper contextWrapper = this.context;
        return contextWrapper != null ? absPath(contextWrapper.getExternalCacheDir()) : "";
    }

    public String getPackageName() {
        ContextWrapper contextWrapper = this.context;
        return contextWrapper != null ? contextWrapper.getPackageName() : "";
    }

    public String getFilesDirectoryPath() {
        ContextWrapper contextWrapper = this.context;
        return contextWrapper != null ? absPath(contextWrapper.getFilesDir()) : "";
    }

    @TargetApi(21)
    public String getNoBackupFilesDirectoryPath() {
        ContextWrapper contextWrapper;
        return (Build.VERSION.SDK_INT < 21 || (contextWrapper = this.context) == null) ? "" : absPath(contextWrapper.getNoBackupFilesDir());
    }

    public String getDatabaseDirectoryPath() {
        ContextWrapper contextWrapper = this.context;
        return (contextWrapper == null || contextWrapper.getDatabasePath("x") == null) ? "" : this.context.getDatabasePath("x").getParent();
    }

    public String getDatabasePath(String str) {
        ContextWrapper contextWrapper = this.context;
        return contextWrapper != null ? absPath(contextWrapper.getDatabasePath(str)) : "";
    }

    public String getVersionCode() {
        ContextWrapper contextWrapper = this.context;
        if (contextWrapper != null) {
            try {
                PackageInfo packageInfo = contextWrapper.getPackageManager().getPackageInfo(getPackageName(), 0);
                return "" + packageInfo.versionCode;
            } catch (PackageManager.NameNotFoundException unused) {
                return "";
            }
        }
        return "";
    }

    /* JADX WARN: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0019  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String getVersionName() {
        /*
            r4 = this;
            android.content.ContextWrapper r0 = r4.context
            java.lang.String r1 = ""
            if (r0 == 0) goto L16
            android.content.pm.PackageManager r0 = r0.getPackageManager()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L16
            java.lang.String r2 = r4.getPackageName()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L16
            r3 = 0
            android.content.pm.PackageInfo r0 = r0.getPackageInfo(r2, r3)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L16
            java.lang.String r0 = r0.versionName     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L16
            goto L17
        L16:
            r0 = r1
        L17:
            if (r0 == 0) goto L1a
            r1 = r0
        L1a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: ch.qos.logback.core.android.AndroidContextUtil.getVersionName():java.lang.String");
    }

    private String absPath(File file) {
        return file != null ? file.getAbsolutePath() : "";
    }
}
