package com.miui.gallery.app.screenChange;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.app.screenChange.ScreenConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes.dex */
public class ScreenStrategyContext {
    public Context mContext;
    public ScreenConfig mCurrentScreenConfig;
    public ScreenSize mScreenSize;
    public ScreenStrategyProvider mScreenStrategyProvider;
    public List<WidgetCase> mWidgetCaseList;

    public ScreenStrategyContext(Context context) {
        this.mContext = context;
    }

    public void addWidgetCase(WidgetCase widgetCase) {
        if (this.mWidgetCaseList == null) {
            this.mWidgetCaseList = new ArrayList();
        }
        this.mWidgetCaseList.add(widgetCase);
    }

    public void addOnScreenChangeListener(IScreenChange iScreenChange) {
        if (iScreenChange instanceof IScreenChange.OnLargeScreenChangeListener) {
            ((LargeScreenChangeCase) getScreenStrategy(ScreenStrategyType.LARGE_SCREEN_CHANGE)).addOnLargeScreenChangeListener((IScreenChange.OnLargeScreenChangeListener) iScreenChange, this.mScreenSize);
        } else if (iScreenChange instanceof IScreenChange.OnRestoreInstanceListener) {
            ((HandleInstanceCase) getScreenStrategy(ScreenStrategyType.HANDLE_INSTANCE)).addOnRestoreInstanceListener((IScreenChange.OnRestoreInstanceListener) iScreenChange);
        } else if (iScreenChange instanceof IScreenChange.OnOrientationChangeListener) {
            ((OrientationChangeCase) getScreenStrategy(ScreenStrategyType.ORIENTATION_CHANGE)).addOnOrientationListener((IScreenChange.OnOrientationChangeListener) iScreenChange);
        } else if (!(iScreenChange instanceof IScreenChange.OnScreenLayoutSizeChangeListener)) {
        } else {
            ((ScreenLayoutSizeCase) getScreenStrategy(ScreenStrategyType.SCREEN_LAYOUT_SIZE_CHANGE)).addOnScreenLayoutSizeChangeListener((IScreenChange.OnScreenLayoutSizeChangeListener) iScreenChange);
        }
    }

    public <T> T getScreenStrategy(ScreenStrategyType screenStrategyType) {
        if (this.mScreenStrategyProvider == null) {
            this.mScreenStrategyProvider = new ScreenStrategyProvider(this.mContext, this);
        }
        return (T) this.mScreenStrategyProvider.getScreenChangeCase(screenStrategyType);
    }

    public void handleWhenCreate(ScreenSize screenSize, Configuration configuration) {
        this.mScreenSize = screenSize;
        this.mCurrentScreenConfig = new ScreenConfig.Builder().setScreenWidth(screenSize.getScreenWidth()).setScreenHeight(screenSize.getScreenHeight()).setOrientation(configuration.orientation).setScreenLayout(configuration.screenLayout).build();
    }

    public void dispatchScreenSizeChange(ScreenSize screenSize, Configuration configuration) {
        if (this.mWidgetCaseList != null) {
            ScreenConfig build = new ScreenConfig.Builder().setScreenWidth(screenSize.getScreenWidth()).setScreenHeight(screenSize.getScreenHeight()).setOrientation(configuration.orientation).setScreenLayout(configuration.screenLayout).build();
            for (WidgetCase widgetCase : this.mWidgetCaseList) {
                if (widgetCase.needHandle(build, this.mCurrentScreenConfig)) {
                    widgetCase.dispatchScreenSizeChange(screenSize, configuration);
                }
            }
            this.mScreenSize = screenSize;
            this.mCurrentScreenConfig = build;
        }
    }

    public List<Integer> calConfigurationCaseType(ScreenSize screenSize, Configuration configuration) {
        if (this.mWidgetCaseList != null) {
            ScreenConfig build = new ScreenConfig.Builder().setScreenWidth(screenSize.getScreenWidth()).setScreenHeight(screenSize.getScreenHeight()).setOrientation(configuration.orientation).setScreenLayout(configuration.screenLayout).build();
            LinkedList linkedList = new LinkedList();
            for (WidgetCase widgetCase : this.mWidgetCaseList) {
                if (widgetCase.needHandle(build, this.mCurrentScreenConfig)) {
                    linkedList.add(Integer.valueOf(widgetCase.getCaseType()));
                }
            }
            return linkedList;
        }
        return Collections.emptyList();
    }

    public void handleWhenSaveInstance(Bundle bundle, Configuration configuration) {
        List<WidgetCase> list = this.mWidgetCaseList;
        if (list != null) {
            for (WidgetCase widgetCase : list) {
                if (widgetCase.needHandleInstance()) {
                    widgetCase.handleWhenSaveInstance(bundle, configuration);
                }
            }
        }
    }

    public void handleRestoreInstance(Bundle bundle, Configuration configuration) {
        List<WidgetCase> list = this.mWidgetCaseList;
        if (list != null) {
            for (WidgetCase widgetCase : list) {
                if (widgetCase.needHandleInstance()) {
                    widgetCase.handleRestoreInstance(bundle, configuration);
                }
            }
        }
    }
}
