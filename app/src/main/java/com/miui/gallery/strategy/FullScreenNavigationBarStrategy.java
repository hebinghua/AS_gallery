package com.miui.gallery.strategy;

import android.app.Activity;
import com.miui.gallery.compat.view.WindowCompat;
import org.jcodec.containers.mp4.boxes.Box;

/* loaded from: classes2.dex */
public class FullScreenNavigationBarStrategy implements IStrategy$INavigationBarStrategy {
    @Override // com.miui.gallery.strategy.IStrategy$INavigationBarStrategy
    public void setNavigationBarColor(Activity activity, boolean z) {
        if (z) {
            boolean z2 = (activity.getResources().getConfiguration().uiMode & 48) == 32;
            activity.findViewById(16908290).setFitsSystemWindows(false);
            activity.getWindow().clearFlags(Box.MAX_BOX_SIZE);
            WindowCompat.setNavigationBarColor(activity.getWindow(), z2 ? -16777216 : -1);
            return;
        }
        activity.findViewById(16908290).setFitsSystemWindows(false);
        activity.getWindow().addFlags(Box.MAX_BOX_SIZE);
        WindowCompat.setNavigationBarColor(activity.getWindow(), 0);
    }
}
