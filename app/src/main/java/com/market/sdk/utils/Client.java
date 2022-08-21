package com.market.sdk.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.market.sdk.SystemProperties;
import com.market.sdk.utils.PrefUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;
import org.slf4j.Marker;

/* loaded from: classes.dex */
public class Client {
    public static String ANDROID_ID = null;
    public static int DISPLAY_DENSITY = 0;
    public static int DISPLAY_HEIGHT = 0;
    public static String DISPLAY_RESOLUTION = null;
    public static int DISPLAY_WIDTH = 0;
    public static ArrayList<String> FEATURE = null;
    public static String GLES_VERSION = null;
    public static ArrayList<String> GL_EXTENSION = null;
    public static ArrayList<String> LIBRARY = null;
    public static String RELEASE = null;
    public static int SDK_VERSION = 0;
    public static String SYSTEM_VERSION = null;
    public static int TOUCH_SCREEN = 0;
    public static boolean mIsInited = false;
    public static final Object mLock = new Object();

    public static void init(Context context) {
        if (mIsInited) {
            return;
        }
        acquireScreenAttr(context);
        acquireDeviceConfig(context);
        acquireFeature(context);
        acquireLibrary(context);
        acquireGLExtensions();
        acquireSystemInfo(context);
        acquireIdentity(context);
        mIsInited = true;
    }

    public static void acquireScreenAttr(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        DISPLAY_HEIGHT = displayMetrics.heightPixels;
        DISPLAY_WIDTH = displayMetrics.widthPixels;
        DISPLAY_RESOLUTION = DISPLAY_HEIGHT + Marker.ANY_MARKER + DISPLAY_WIDTH;
        DISPLAY_DENSITY = displayMetrics.densityDpi;
    }

    public static void acquireDeviceConfig(Context context) {
        ConfigurationInfo deviceConfigurationInfo = ((ActivityManager) context.getSystemService("activity")).getDeviceConfigurationInfo();
        TOUCH_SCREEN = deviceConfigurationInfo.reqTouchScreen;
        GLES_VERSION = deviceConfigurationInfo.getGlEsVersion();
    }

    public static void acquireFeature(Context context) {
        FeatureInfo[] systemAvailableFeatures = context.getPackageManager().getSystemAvailableFeatures();
        synchronized (mLock) {
            FEATURE = new ArrayList<>();
            if (systemAvailableFeatures != null) {
                for (FeatureInfo featureInfo : systemAvailableFeatures) {
                    if (!TextUtils.isEmpty(featureInfo.name)) {
                        FEATURE.add(featureInfo.name);
                    }
                }
            }
            Collections.sort(FEATURE);
        }
    }

    public static void acquireLibrary(Context context) {
        String[] systemSharedLibraryNames = context.getPackageManager().getSystemSharedLibraryNames();
        synchronized (mLock) {
            LIBRARY = new ArrayList<>();
            if (systemSharedLibraryNames != null) {
                for (String str : systemSharedLibraryNames) {
                    if (!TextUtils.isEmpty(str)) {
                        LIBRARY.add(str);
                    }
                }
            }
            Collections.sort(LIBRARY);
        }
    }

    public static void acquireGLExtensions() {
        String[] split;
        String gLExtensions = getGLExtensions();
        synchronized (mLock) {
            GL_EXTENSION = new ArrayList<>();
            if (!TextUtils.isEmpty(gLExtensions)) {
                for (String str : TextUtils.split(gLExtensions, " ")) {
                    if (!TextUtils.isEmpty(str)) {
                        GL_EXTENSION.add(str);
                    }
                }
            }
            Collections.sort(GL_EXTENSION);
        }
    }

    public static void acquireSystemInfo(Context context) {
        RELEASE = Build.VERSION.RELEASE;
        SYSTEM_VERSION = Build.VERSION.INCREMENTAL;
        SDK_VERSION = Build.VERSION.SDK_INT;
    }

    public static String getCountry() {
        return Locale.getDefault().getCountry();
    }

    public static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static String getRegion() {
        return SystemProperties.getString("ro.miui.region", "CN");
    }

    public static String getMiuiBigVersionCode() {
        return SystemProperties.getString("ro.miui.ui.version.code", "-1");
    }

    public static boolean isAlphaBuild() {
        return SystemProperties.getString("ro.product.mod_device", "").endsWith("_alpha") || SystemProperties.getString("ro.product.mod_device", "").endsWith("_alpha_global");
    }

    public static boolean isDevBuild() {
        return !TextUtils.isEmpty(Build.VERSION.INCREMENTAL) && Build.VERSION.INCREMENTAL.matches("\\d+.\\d+.\\d+(-internal)?");
    }

    public static String getMiuiBigVersionName() {
        String string = SystemProperties.getString("ro.miui.ui.version.name", "");
        if (isAlphaBuild()) {
            return string + "-alpha";
        } else if (!isDevBuild()) {
            return string;
        } else {
            return string + "-dev";
        }
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getDevice() {
        return Build.DEVICE;
    }

    public static int getDeviceType() {
        return isTablet() ? 1 : 0;
    }

    public static boolean isTablet() {
        return SystemProperties.getString("ro.build.characteristics", "").contains("tablet");
    }

    public static String getCpuArch() {
        ArrayList arrayList = new ArrayList();
        if (isLaterThanLollipop()) {
            String string = SystemProperties.getString("ro.product.cpu.abilist", "");
            if (!TextUtils.isEmpty(string)) {
                arrayList = new ArrayList(Arrays.asList(TextUtils.split(string, ",")));
            }
        }
        if (arrayList.isEmpty()) {
            arrayList.add(Build.CPU_ABI);
            arrayList.add(Build.CPU_ABI2);
        }
        return TextUtils.join(",", arrayList);
    }

    public static void acquireIdentity(Context context) {
        ANDROID_ID = Settings.Secure.getString(context.getContentResolver(), "android_id");
    }

    public static String getImeiMd5() {
        String deviceId = ((TelephonyManager) AppGlobal.getContext().getSystemService("phone")).getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getUUid();
        }
        return Coder.encodeMD5(deviceId);
    }

    public static String getUUid() {
        String string = PrefUtils.getString(nexExportFormat.TAG_FORMAT_UUID, "", new PrefUtils.PrefFile[0]);
        if (TextUtils.isEmpty(string)) {
            String valueOf = String.valueOf(UUID.randomUUID());
            PrefUtils.setString(nexExportFormat.TAG_FORMAT_UUID, valueOf, new PrefUtils.PrefFile[0]);
            return valueOf;
        }
        return string;
    }

    public static String getGLExtensions() {
        EGL10 egl10 = (EGL10) EGLContext.getEGL();
        EGLDisplay eglGetDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if (eglGetDisplay != EGL10.EGL_NO_DISPLAY && egl10.eglInitialize(eglGetDisplay, new int[2])) {
            int[] iArr = new int[1];
            EGLConfig[] eGLConfigArr = new EGLConfig[1];
            if (!egl10.eglChooseConfig(eglGetDisplay, new int[]{12339, 1, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12344}, eGLConfigArr, 1, iArr)) {
                return null;
            }
            EGLConfig eGLConfig = iArr[0] > 0 ? eGLConfigArr[0] : null;
            EGLContext eglCreateContext = egl10.eglCreateContext(eglGetDisplay, eGLConfig, EGL10.EGL_NO_CONTEXT, null);
            EGLSurface eglCreatePbufferSurface = egl10.eglCreatePbufferSurface(eglGetDisplay, eGLConfig, new int[]{12375, 480, 12374, 800, 12344});
            if (eglCreatePbufferSurface == null || eglCreatePbufferSurface == EGL10.EGL_NO_SURFACE) {
                return null;
            }
            egl10.eglMakeCurrent(eglGetDisplay, eglCreatePbufferSurface, eglCreatePbufferSurface, eglCreateContext);
            if (!egl10.eglMakeCurrent(eglGetDisplay, eglCreatePbufferSurface, eglCreatePbufferSurface, eglCreateContext)) {
                return null;
            }
            String glGetString = ((GL10) eglCreateContext.getGL()).glGetString(7939);
            egl10.eglDestroySurface(eglGetDisplay, eglCreatePbufferSurface);
            egl10.eglDestroyContext(eglGetDisplay, eglCreateContext);
            egl10.eglTerminate(eglGetDisplay);
            if (glGetString == null) {
                return null;
            }
            return glGetString.trim();
        }
        return null;
    }

    public static boolean isLaterThanHoneycomb() {
        return SDK_VERSION >= 11;
    }

    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean isLaterThanLollipop() {
        return getSdkVersion() >= 21;
    }

    public static boolean isLaterThanN() {
        return SDK_VERSION >= 24;
    }

    public static boolean isMiui() {
        return new File("/system/app/miui.apk").exists() || new File("/system/app/miui/miui.apk").exists();
    }

    public static boolean isInternationalMiui() {
        return isMiui() && SystemProperties.getString("ro.product.mod_device", "").contains("_global");
    }
}
