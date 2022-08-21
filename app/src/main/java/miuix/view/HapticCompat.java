package miuix.view;

import android.os.Looper;
import android.util.Log;
import android.view.View;
import androidx.annotation.Keep;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public class HapticCompat {
    public static List<HapticFeedbackProvider> sProviders = new ArrayList();
    public static final Executor sSingleThread = Executors.newSingleThreadExecutor();

    static {
        loadProviders("miuix.view.LinearVibrator", "miuix.view.ExtendedVibrator");
    }

    @Keep
    public static boolean performHapticFeedback(View view, int i) {
        if (i < 268435456) {
            Log.i("HapticCompat", String.format("perform haptic: 0x%08x", Integer.valueOf(i)));
            return view.performHapticFeedback(i);
        }
        int i2 = HapticFeedbackConstants.MIUI_HAPTIC_END;
        if (i > i2) {
            Log.w("HapticCompat", String.format("illegal feedback constant, should be in range [0x%08x..0x%08x]", 268435456, Integer.valueOf(i2)));
            return false;
        }
        for (HapticFeedbackProvider hapticFeedbackProvider : sProviders) {
            if (hapticFeedbackProvider.performHapticFeedback(view, i)) {
                return true;
            }
        }
        return false;
    }

    /* loaded from: classes3.dex */
    public static class WeakReferenceHandler implements Runnable {
        public final int mFeedbackConstant;
        public final WeakReference<View> mViewReference;

        public WeakReferenceHandler(View view, int i) {
            this.mViewReference = new WeakReference<>(view);
            this.mFeedbackConstant = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            View view = this.mViewReference.get();
            if (view != null) {
                HapticCompat.performHapticFeedback(view, this.mFeedbackConstant);
            }
        }
    }

    @Keep
    public static void performHapticFeedbackAsync(View view, int i) {
        if (!(Looper.getMainLooper() == Looper.myLooper())) {
            performHapticFeedback(view, i);
        } else {
            sSingleThread.execute(new WeakReferenceHandler(view, i));
        }
    }

    public static int obtainFeedBack(int i) {
        for (HapticFeedbackProvider hapticFeedbackProvider : sProviders) {
            if (hapticFeedbackProvider instanceof LinearVibrator) {
                return ((LinearVibrator) hapticFeedbackProvider).obtainFeedBack(i);
            }
        }
        return -1;
    }

    @Keep
    public static void registerProvider(HapticFeedbackProvider hapticFeedbackProvider) {
        sProviders.add(hapticFeedbackProvider);
    }

    public static void loadProviders(String... strArr) {
        for (String str : strArr) {
            Log.i("HapticCompat", "loading provider: " + str);
            try {
                Class.forName(str, true, HapticCompat.class.getClassLoader());
            } catch (ClassNotFoundException e) {
                Log.w("HapticCompat", String.format("load provider %s failed.", str), e);
            }
        }
    }
}
