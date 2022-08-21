package com.miui.gallery.app.screenChange;

import android.content.Context;
import android.content.res.Configuration;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class LargeScreenChangeCase extends BaseWidgetCase {
    public static String TAG = "LargeScreenWidgetCase";
    public Context mContext;
    public List<IScreenChange.OnLargeScreenChangeListener> mListeners;

    public LargeScreenChangeCase(Context context) {
        this.mContext = context;
    }

    public void addOnLargeScreenChangeListener(IScreenChange.OnLargeScreenChangeListener onLargeScreenChangeListener, ScreenSize screenSize) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(onLargeScreenChangeListener);
        if (BaseBuildUtil.isLargeScreenDevice()) {
            DefaultLogger.d(TAG, "onCreatedWhileLargeDevice");
            onLargeScreenChangeListener.onCreatedWhileLargeDevice(screenSize);
            return;
        }
        DefaultLogger.d(TAG, "onCreatedWhileNormalDevice");
        onLargeScreenChangeListener.onCreatedWhileNormalDevice(screenSize);
    }

    @Override // com.miui.gallery.app.screenChange.WidgetCase
    public boolean needHandle(ScreenConfig screenConfig, ScreenConfig screenConfig2) {
        if (BaseBuildUtil.isLargeScreenDevice()) {
            return (screenConfig.getScreenWidth() == screenConfig2.getScreenWidth() && screenConfig.getScreenHeight() == screenConfig2.getScreenHeight()) ? false : true;
        }
        return false;
    }

    @Override // com.miui.gallery.app.screenChange.WidgetCase
    public void onScreenChange(ScreenSize screenSize, Configuration configuration) {
        if (this.mListeners == null) {
            return;
        }
        DefaultLogger.d(TAG, "onScreenSizeToLargeOrNormal");
        for (IScreenChange.OnLargeScreenChangeListener onLargeScreenChangeListener : this.mListeners) {
            onLargeScreenChangeListener.onScreenSizeToLargeOrNormal(screenSize);
        }
    }

    @Override // com.miui.gallery.app.screenChange.WidgetCase
    public int getCaseType() {
        return WidgetCase.CASE_EVENT_TYPE_LARGE_SCREEN_CHANGE;
    }
}
