package com.miui.pickdrag.blur;

import android.util.Log;
import com.miui.pickdrag.utils.Device;
import kotlin.jvm.internal.Intrinsics;
import miuix.appcompat.app.AppCompatActivity;

/* compiled from: BlurControllerFactory.kt */
/* loaded from: classes3.dex */
public final class BlurControllerFactory {
    public static final BlurControllerFactory INSTANCE;
    public static final String TAG;

    static {
        BlurControllerFactory blurControllerFactory = new BlurControllerFactory();
        INSTANCE = blurControllerFactory;
        TAG = blurControllerFactory.getClass().getSimpleName();
    }

    public final BlurController createBlurController(AppCompatActivity activity) {
        BlurController functionEnabled;
        Intrinsics.checkNotNullParameter(activity, "activity");
        boolean isHighDevice = Device.isHighDevice();
        Log.i(TAG, Intrinsics.stringPlus("createBlurController # isHighDevice: ", Boolean.valueOf(isHighDevice)));
        if (isHighDevice) {
            functionEnabled = new BlurWindowController();
        } else {
            functionEnabled = new TranslucentWindowController().setFunctionEnabled(false);
        }
        functionEnabled.setActivity(activity);
        return functionEnabled;
    }
}
