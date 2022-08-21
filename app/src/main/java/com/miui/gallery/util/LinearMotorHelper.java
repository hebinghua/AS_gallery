package com.miui.gallery.util;

import android.content.Context;
import android.view.View;
import com.android.internal.SystemPropertiesCompat;
import com.miui.core.SdkHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import miuix.util.HapticFeedbackCompat;
import miuix.view.HapticFeedbackConstants;

/* loaded from: classes2.dex */
public class LinearMotorHelper {
    public static int HAPTIC_MESH_NORMAL = HapticFeedbackConstants.MIUI_MESH_NORMAL;
    public static int HAPTIC_MESH_LIGHT = HapticFeedbackConstants.MIUI_MESH_LIGHT;
    public static int HAPTIC_SWITCH = HapticFeedbackConstants.MIUI_SWITCH;
    public static int HAPTIC_TAP_NORMAL = HapticFeedbackConstants.MIUI_TAP_NORMAL;
    public static int HAPTIC_TAP_LIGHT = HapticFeedbackConstants.MIUI_TAP_LIGHT;
    public static int HAPTIC_PICK_UP = HapticFeedbackConstants.MIUI_PICK_UP;
    public static int HAPTIC_POPUP_NORMAL = HapticFeedbackConstants.MIUI_POPUP_NORMAL;
    public static int HAPTIC_MESH_HEAVY = HapticFeedbackConstants.MIUI_MESH_HEAVY;
    public static final LazyValue<Void, Boolean> LINEAR_MOTOR_SUPPORTED = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.util.LinearMotorHelper.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Void r2) {
            return Boolean.valueOf("linear".equals(SystemPropertiesCompat.get("sys.haptic.motor", "")));
        }
    };

    public static boolean performHapticFeedback(View view, int i) {
        try {
            if (view == null) {
                DefaultLogger.d("LinearMotorHelper", "the view is null.");
                return false;
            } else if (!view.isAttachedToWindow()) {
                DefaultLogger.e("LinearMotorHelper", "the view is not attach to window.");
                return false;
            } else {
                HapticFeedbackCompat hapticFeedbackCompat = new HapticFeedbackCompat(view.getContext(), true);
                if (SdkHelper.IS_MIUI && hapticFeedbackCompat.supportLinearMotor()) {
                    return hapticFeedbackCompat.performHapticFeedback(i);
                }
                DefaultLogger.d("LinearMotorHelper", "the device is not support LinearMotorVibrate.");
                return false;
            }
        } catch (Exception e) {
            DefaultLogger.e("LinearMotorHelper", "view performHapticFeedback  exception: ", e);
            return false;
        }
    }

    public static boolean performHapticFeedback(Context context, int i) {
        if (context == null) {
            return false;
        }
        try {
            HapticFeedbackCompat hapticFeedbackCompat = new HapticFeedbackCompat(context, true);
            if (SdkHelper.IS_MIUI && hapticFeedbackCompat.supportLinearMotor()) {
                return hapticFeedbackCompat.performHapticFeedback(i);
            }
            DefaultLogger.d("LinearMotorHelper", "the device is not support LinearMotorVibrate.");
            return false;
        } catch (Exception e) {
            DefaultLogger.e("LinearMotorHelper", "view performHapticFeedback  exception: ", e);
            return false;
        }
    }
}
