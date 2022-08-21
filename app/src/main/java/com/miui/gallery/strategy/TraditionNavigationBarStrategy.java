package com.miui.gallery.strategy;

import android.app.Activity;
import com.miui.gallery.compat.view.WindowCompat;
import org.jcodec.containers.mp4.boxes.Box;

/* loaded from: classes2.dex */
public class TraditionNavigationBarStrategy implements IStrategy$INavigationBarStrategy {
    @Override // com.miui.gallery.strategy.IStrategy$INavigationBarStrategy
    public void setNavigationBarColor(Activity activity, boolean z) {
        activity.getWindow().clearFlags(Box.MAX_BOX_SIZE);
        WindowCompat.setNavigationBarColor(activity.getWindow(), (activity.getResources().getConfiguration().uiMode & 48) == 32 ? -16777216 : -1);
    }
}
