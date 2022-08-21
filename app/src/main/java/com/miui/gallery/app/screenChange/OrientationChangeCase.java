package com.miui.gallery.app.screenChange;

import android.content.res.Configuration;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OrientationChangeCase extends BaseWidgetCase {
    public List<IScreenChange.OnOrientationChangeListener> mListeners;

    public void addOnOrientationListener(IScreenChange.OnOrientationChangeListener onOrientationChangeListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(onOrientationChangeListener);
    }

    @Override // com.miui.gallery.app.screenChange.WidgetCase
    public boolean needHandle(ScreenConfig screenConfig, ScreenConfig screenConfig2) {
        return screenConfig.getOrientation() != screenConfig2.getOrientation();
    }

    @Override // com.miui.gallery.app.screenChange.WidgetCase
    public void onScreenChange(ScreenSize screenSize, Configuration configuration) {
        onOrientationChange(configuration);
    }

    public void onOrientationChange(Configuration configuration) {
        DefaultLogger.d("OrientationChangeCase", "onOrientationChange");
        List<IScreenChange.OnOrientationChangeListener> list = this.mListeners;
        if (list == null) {
            return;
        }
        for (IScreenChange.OnOrientationChangeListener onOrientationChangeListener : list) {
            onOrientationChangeListener.onOrientationChange(configuration);
        }
    }

    @Override // com.miui.gallery.app.screenChange.WidgetCase
    public int getCaseType() {
        return WidgetCase.CASE_EVENT_TYPE_ORIENTATION_CHANGE;
    }
}
