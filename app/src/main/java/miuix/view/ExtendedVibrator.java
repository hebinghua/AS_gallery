package miuix.view;

import android.util.Log;
import android.view.View;
import androidx.annotation.Keep;

@Keep
/* loaded from: classes3.dex */
class ExtendedVibrator implements HapticFeedbackProvider {
    private static final String TAG = "ExtendedVibrator";

    private ExtendedVibrator() {
    }

    @Override // miuix.view.HapticFeedbackProvider
    public boolean performHapticFeedback(View view, int i) {
        if (i == HapticFeedbackConstants.MIUI_VIRTUAL_RELEASE) {
            return view.performHapticFeedback(2);
        }
        return false;
    }

    static {
        initialize();
    }

    private static void initialize() {
        if (PlatformConstants.VERSION < 0) {
            Log.w(TAG, "MiuiHapticFeedbackConstants not found.");
            return;
        }
        HapticCompat.registerProvider(new ExtendedVibrator());
        Log.i(TAG, "setup ExtendedVibrator success.");
    }
}
