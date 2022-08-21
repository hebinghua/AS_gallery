package com.miui.gallery.app.screenChange;

import android.content.res.Configuration;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ScreenLayoutSizeCase extends BaseWidgetCase {
    public List<IScreenChange.OnScreenLayoutSizeChangeListener> mListeners;

    public void addOnScreenLayoutSizeChangeListener(IScreenChange.OnScreenLayoutSizeChangeListener onScreenLayoutSizeChangeListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(onScreenLayoutSizeChangeListener);
    }

    public int getScreenLayoutSize(ScreenConfig screenConfig) {
        return screenConfig.getScreenLayout() & 15;
    }

    @Override // com.miui.gallery.app.screenChange.WidgetCase
    public boolean needHandle(ScreenConfig screenConfig, ScreenConfig screenConfig2) {
        return getScreenLayoutSize(screenConfig) != getScreenLayoutSize(screenConfig2);
    }

    @Override // com.miui.gallery.app.screenChange.WidgetCase
    public void onScreenChange(ScreenSize screenSize, Configuration configuration) {
        onScreenLayoutSizeChange(configuration);
    }

    public void onScreenLayoutSizeChange(Configuration configuration) {
        DefaultLogger.d("ScreenLayoutSizeCase", "onScreenLayoutSizeChange");
        List<IScreenChange.OnScreenLayoutSizeChangeListener> list = this.mListeners;
        if (list == null) {
            return;
        }
        for (IScreenChange.OnScreenLayoutSizeChangeListener onScreenLayoutSizeChangeListener : list) {
            onScreenLayoutSizeChangeListener.onScreenLayoutSizeChange(configuration);
        }
    }

    @Override // com.miui.gallery.app.screenChange.WidgetCase
    public int getCaseType() {
        return WidgetCase.CASE_EVENT_TYPE_SCREEN_LAYOUT_SIZE;
    }
}
