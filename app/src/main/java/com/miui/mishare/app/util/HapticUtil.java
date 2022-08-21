package com.miui.mishare.app.util;

import android.view.View;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* loaded from: classes3.dex */
public class HapticUtil {
    public static void performMeshNormal(View view) {
        try {
            HapticCompat.performHapticFeedback(view, HapticFeedbackConstants.MIUI_MESH_NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
