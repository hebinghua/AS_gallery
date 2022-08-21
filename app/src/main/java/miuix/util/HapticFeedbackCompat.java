package miuix.util;

import android.content.Context;
import android.util.Log;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import miui.util.HapticFeedbackUtil;
import miuix.view.HapticCompat;
import miuix.view.PlatformConstants;

/* loaded from: classes3.dex */
public class HapticFeedbackCompat {
    public static boolean mAvailable;
    public static boolean mCanCheckExtHaptic;
    public static boolean mCanStop;
    public static boolean mExtHapticAlways;
    public static boolean mIsSupportExtHapticWithReason;
    public static boolean mIsSupportHapticWithReason;
    public static final Executor sSingleThread = Executors.newSingleThreadExecutor();
    public HapticFeedbackUtil hapticFeedbackUtil;

    static {
        if (PlatformConstants.VERSION >= 1) {
            try {
                mAvailable = HapticFeedbackUtil.isSupportLinearMotorVibrate();
            } catch (Throwable th) {
                Log.w("HapticFeedbackCompat", "MIUI Haptic Implementation is not available", th);
                mAvailable = false;
            }
            if (!mAvailable) {
                return;
            }
            try {
                HapticFeedbackUtil.class.getMethod("performHapticFeedback", Integer.TYPE, Double.TYPE, String.class);
                mIsSupportHapticWithReason = true;
            } catch (Throwable th2) {
                Log.w("HapticFeedbackCompat", "Not support haptic with reason", th2);
                mIsSupportHapticWithReason = false;
            }
            try {
                HapticFeedbackUtil.class.getMethod("isSupportExtHapticFeedback", Integer.TYPE);
                mCanCheckExtHaptic = true;
            } catch (Throwable unused) {
                mCanCheckExtHaptic = false;
            }
            try {
                HapticFeedbackUtil.class.getMethod("performExtHapticFeedback", Integer.TYPE, Boolean.TYPE);
                mExtHapticAlways = true;
            } catch (Throwable unused2) {
                mExtHapticAlways = false;
            }
            try {
                HapticFeedbackUtil.class.getMethod("stop", new Class[0]);
                mCanStop = true;
            } catch (Throwable unused3) {
                mCanStop = false;
            }
            try {
                HapticFeedbackUtil.class.getMethod("performExtHapticFeedback", Integer.TYPE, Double.TYPE, String.class);
                mIsSupportExtHapticWithReason = true;
            } catch (Throwable th3) {
                Log.w("HapticFeedbackCompat", "Not support ext haptic with reason", th3);
                mIsSupportExtHapticWithReason = false;
            }
        }
    }

    @Deprecated
    public HapticFeedbackCompat(Context context, boolean z) {
        if (PlatformConstants.VERSION < 1) {
            Log.w("HapticFeedbackCompat", "MiuiHapticFeedbackConstants not found or not compatible for LinearVibrator.");
        } else if (!mAvailable) {
            Log.w("HapticFeedbackCompat", "linear motor is not supported in this platform.");
        } else {
            this.hapticFeedbackUtil = new HapticFeedbackUtil(context, z);
        }
    }

    public HapticFeedbackCompat(Context context) {
        this(context, true);
    }

    public boolean supportLinearMotor() {
        return mAvailable;
    }

    public boolean performExtHapticFeedback(int i) {
        HapticFeedbackUtil hapticFeedbackUtil = this.hapticFeedbackUtil;
        if (hapticFeedbackUtil != null) {
            return hapticFeedbackUtil.performExtHapticFeedback(i);
        }
        return false;
    }

    public boolean performHapticFeedback(int i, boolean z) {
        int obtainFeedBack;
        if (this.hapticFeedbackUtil == null || (obtainFeedBack = HapticCompat.obtainFeedBack(i)) == -1) {
            return false;
        }
        return this.hapticFeedbackUtil.performHapticFeedback(obtainFeedBack, z);
    }

    public boolean performHapticFeedback(int i) {
        return performHapticFeedback(i, false);
    }
}
