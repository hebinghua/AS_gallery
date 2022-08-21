package com.miui.gallery.app.screenChange;

import android.content.res.Configuration;
import android.os.Bundle;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class HandleInstanceCase extends BaseWidgetCase {
    public List<IScreenChange.OnRestoreInstanceListener> mListeners;

    @Override // com.miui.gallery.app.screenChange.WidgetCase
    public boolean needHandle(ScreenConfig screenConfig, ScreenConfig screenConfig2) {
        return false;
    }

    @Override // com.miui.gallery.app.screenChange.BaseWidgetCase, com.miui.gallery.app.screenChange.WidgetCase
    public boolean needHandleInstance() {
        return true;
    }

    @Override // com.miui.gallery.app.screenChange.WidgetCase
    public void onScreenChange(ScreenSize screenSize, Configuration configuration) {
    }

    public void addOnRestoreInstanceListener(IScreenChange.OnRestoreInstanceListener onRestoreInstanceListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(onRestoreInstanceListener);
    }

    public int getScreenLayoutSize(Configuration configuration) {
        return configuration.screenLayout & 15;
    }

    @Override // com.miui.gallery.app.screenChange.BaseWidgetCase, com.miui.gallery.app.screenChange.WidgetCase
    public void handleWhenSaveInstance(Bundle bundle, Configuration configuration) {
        super.handleWhenSaveInstance(bundle, configuration);
        bundle.putInt("screenSize", getScreenLayoutSize(configuration));
    }

    @Override // com.miui.gallery.app.screenChange.BaseWidgetCase, com.miui.gallery.app.screenChange.WidgetCase
    public void handleRestoreInstance(Bundle bundle, Configuration configuration) {
        super.handleRestoreInstance(bundle, configuration);
        if (getScreenLayoutSize(configuration) != bundle.getInt("screenSize")) {
            onRestoreInstance(bundle, configuration);
        }
    }

    public void onRestoreInstance(Bundle bundle, Configuration configuration) {
        DefaultLogger.d("SaveInstanceCase", "RestoreInstance");
        List<IScreenChange.OnRestoreInstanceListener> list = this.mListeners;
        if (list == null) {
            return;
        }
        for (IScreenChange.OnRestoreInstanceListener onRestoreInstanceListener : list) {
            onRestoreInstanceListener.onRestoreInstance(bundle, configuration);
        }
    }

    @Override // com.miui.gallery.app.screenChange.WidgetCase
    public int getCaseType() {
        return WidgetCase.CASE_EVENT_TYPE_HANDLE_INSTANCE_CHANGE;
    }
}
