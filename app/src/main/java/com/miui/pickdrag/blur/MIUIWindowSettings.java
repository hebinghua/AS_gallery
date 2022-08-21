package com.miui.pickdrag.blur;

import android.os.Build;
import android.view.Window;
import com.miui.pickdrag.blur.compat.BlurSettingsCompatBelowS;
import com.miui.pickdrag.blur.compat.BlurSettingsCompatS;

/* compiled from: MIUIWindowSettings.kt */
/* loaded from: classes3.dex */
public final class MIUIWindowSettings {
    public static final MIUIWindowSettings INSTANCE = new MIUIWindowSettings();

    public final boolean setBlurRatio(Window window, float f) {
        if (Build.VERSION.SDK_INT >= 31) {
            return BlurSettingsCompatS.setBlurRadius(window, f);
        }
        return BlurSettingsCompatBelowS.setBlurRatio(window, f);
    }
}
