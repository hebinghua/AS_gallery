package com.miui.gallery.compat.app;

import androidx.appcompat.app.ActionBar;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.view.ActionBarWrapper;

/* loaded from: classes.dex */
public class ActionBarCompat {
    public static void setShowHideAnimationEnabled(ActionBar actionBar, boolean z) {
        if (actionBar != null) {
            if (actionBar instanceof ActionBarWrapper) {
                ((ActionBarWrapper) actionBar).setShowHideAnimationEnabled(z);
            } else {
                BaseMiscUtil.invokeSafely(actionBar, "setShowHideAnimationEnabled", new Class[]{Boolean.TYPE}, Boolean.valueOf(z));
            }
        }
    }
}
