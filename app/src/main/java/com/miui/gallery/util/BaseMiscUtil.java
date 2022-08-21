package com.miui.gallery.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.RectF;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes2.dex */
public class BaseMiscUtil {
    public static long[] sCrcTable = new long[256];

    /* renamed from: $r8$lambda$M7R9_-VGn96ojS3Xw8ASalJXH1A */
    public static /* synthetic */ void m1669$r8$lambda$M7R9_VGn96ojS3Xw8ASalJXH1A(View view) {
        lambda$showInputMethod$0(view);
    }

    public static float clamp(float f, float f2, float f3) {
        return f > f3 ? f3 : f < f2 ? f2 : f;
    }

    public static int clamp(int i, int i2, int i3) {
        return i > i3 ? i3 : i < i2 ? i2 : i;
    }

    static {
        for (int i = 0; i < 256; i++) {
            long j = i;
            for (int i2 = 0; i2 < 8; i2++) {
                j = (j >> 1) ^ ((((int) j) & 1) != 0 ? -7661587058870466123L : 0L);
            }
            sCrcTable[i] = j;
        }
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            DefaultLogger.w("BaseMiscUtil", "close fail", e);
        }
    }

    public static boolean isValid(Collection collection) {
        return collection != null && collection.size() > 0;
    }

    public static boolean isValid(long[] jArr) {
        return jArr != null && jArr.length > 0;
    }

    public static boolean isValid(Map map) {
        return map != null && map.size() > 0;
    }

    public static final long crc64Long(byte[] bArr) {
        long j = -1;
        for (byte b : bArr) {
            j = (j >> 8) ^ sCrcTable[(((int) j) ^ b) & 255];
        }
        return j;
    }

    public static boolean floatEquals(float f, float f2) {
        return floatNear(f, f2, 1.0E-7f);
    }

    public static boolean doubleEquals(double d, double d2) {
        return doubleNear(d, d2, 1.0E-7d);
    }

    public static boolean floatNear(float f, float f2, float f3) {
        return Math.abs(f - f2) < Math.abs(f3);
    }

    public static boolean doubleNear(double d, double d2, double d3) {
        return Math.abs(d - d2) < Math.abs(d3);
    }

    public static Method getDeclaredMethod(Object obj, String str, Class<?>[] clsArr) {
        if (obj instanceof Class) {
            try {
                return ((Class) obj).getMethod(str, clsArr);
            } catch (Exception unused) {
                return null;
            }
        }
        for (Class<?> cls = obj.getClass(); cls != Object.class; cls = cls.getSuperclass()) {
            try {
                return cls.getDeclaredMethod(str, clsArr);
            } catch (Exception unused2) {
            }
        }
        return null;
    }

    public static Object invokeSafely(Object obj, String str, Class<?>[] clsArr, Object... objArr) {
        try {
            Method declaredMethod = getDeclaredMethod(obj, str, clsArr);
            if (declaredMethod == null) {
                return null;
            }
            if (!declaredMethod.isAccessible()) {
                declaredMethod.setAccessible(true);
            }
            return declaredMethod.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            DefaultLogger.e("BaseMiscUtil", e);
            return null;
        } catch (IllegalArgumentException e2) {
            DefaultLogger.e("BaseMiscUtil", e2);
            return null;
        } catch (SecurityException e3) {
            DefaultLogger.e("BaseMiscUtil", e3);
            return null;
        } catch (InvocationTargetException e4) {
            DefaultLogger.e("BaseMiscUtil", e4);
            return null;
        } catch (Exception e5) {
            DefaultLogger.e("BaseMiscUtil", e5);
            return null;
        }
    }

    public static boolean isNightMode(Context context) {
        return context != null && (context.getResources().getConfiguration().uiMode & 48) == 32;
    }

    public static boolean isRTLDirection() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
    }

    public static RectF scaleRectF(RectF rectF, float f) {
        if (f != 1.0f) {
            rectF.left *= f;
            rectF.top *= f;
            rectF.right *= f;
            rectF.bottom *= f;
        }
        return rectF;
    }

    public static String getActionStr(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                return "ACTION_UP";
            }
            if (actionMasked == 2) {
                return "ACTION_MOVE";
            }
            if (actionMasked == 3) {
                return "ACTION_CANCEL";
            }
            if (actionMasked == 5) {
                return "ACTION_POINTER_DOWN";
            }
            if (actionMasked == 6) {
                return "ACTION_POINTER_UP";
            }
            return "UNKNOWN " + motionEvent.getAction();
        }
        return "ACTION_DOWN";
    }

    public static int getDimensionPixelOffset(Resources resources, String str, String str2, String str3) {
        int identifier = resources.getIdentifier(str, str2, str3);
        if (identifier > 0) {
            return resources.getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public static void showInputMethod(final View view) {
        if (view == null || !view.isAttachedToWindow()) {
            return;
        }
        view.postDelayed(new Runnable() { // from class: com.miui.gallery.util.BaseMiscUtil$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BaseMiscUtil.m1669$r8$lambda$M7R9_VGn96ojS3Xw8ASalJXH1A(view);
            }
        }, 80L);
    }

    public static /* synthetic */ void lambda$showInputMethod$0(View view) {
        Context context = view.getContext();
        if (context != null) {
            view.requestFocus();
            ((InputMethodManager) context.getSystemService("input_method")).showSoftInput(view, 0);
        }
    }

    public static boolean isIntentSupported(Intent intent) {
        if (intent == null) {
            return false;
        }
        return !StaticContext.sGetAndroidContext().getPackageManager().queryIntentActivities(intent, 0).isEmpty();
    }

    public static boolean isServiceSupported(Intent intent) {
        if (intent == null) {
            return false;
        }
        return !StaticContext.sGetAndroidContext().getPackageManager().queryIntentServices(intent, 0).isEmpty();
    }

    public static boolean isPackageInstalled(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            StaticContext.sGetAndroidContext().getPackageManager().getApplicationInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public static boolean isCheckedImprovementProgram(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "upload_log_pref", 0) == 1;
    }
}
