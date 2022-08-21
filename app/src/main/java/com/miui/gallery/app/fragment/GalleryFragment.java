package com.miui.gallery.app.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.app.screenChange.ScreenSize;
import com.miui.gallery.app.screenChange.ScreenStrategyContext;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;

/* loaded from: classes.dex */
public class GalleryFragment extends MiuiFragment {
    public boolean isLargeScreenAndWindow = false;
    public ScreenStrategyContext mScreenStrategyContext;

    @Override // com.miui.gallery.strategy.IStrategyFollower
    public boolean isNeedCheckCutoutBlacklist() {
        return false;
    }

    public boolean isSupportCutoutModeShortEdges() {
        return false;
    }

    public void onConfigurationChanged(List<Integer> list, Configuration configuration) {
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        ScreenStrategyContext screenStrategyContext = this.mScreenStrategyContext;
        if (screenStrategyContext != null) {
            screenStrategyContext.handleWhenSaveInstance(bundle, getResources().getConfiguration());
        }
    }

    @Override // com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Configuration configuration = getResources().getConfiguration();
        this.isLargeScreenAndWindow = BaseBuildUtil.isLargeScreenDevice() && BaseBuildUtil.isLargeHorizontalWindow();
        int curScreenWidthInDip = BaseBuildUtil.getCurScreenWidthInDip();
        int curScreenHeightInDip = BaseBuildUtil.getCurScreenHeightInDip();
        this.mScreenStrategyContext = new ScreenStrategyContext(getContext());
        this.mScreenStrategyContext.handleWhenCreate(new ScreenSize(curScreenWidthInDip, curScreenHeightInDip), configuration);
        if (bundle != null) {
            this.mScreenStrategyContext.handleRestoreInstance(bundle, configuration);
        }
    }

    public void addScreenChangeListener(IScreenChange iScreenChange) {
        this.mScreenStrategyContext.addOnScreenChangeListener(iScreenChange);
    }

    @Override // com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        DefaultLogger.d("Fragment", "onConfigurationChanged");
        super.onConfigurationChanged(configuration);
        this.isLargeScreenAndWindow = BaseBuildUtil.isLargeScreenDevice() && BaseBuildUtil.isLargeHorizontalWindow();
        ScreenSize screenSize = new ScreenSize(BaseBuildUtil.getCurScreenWidthInDip(), BaseBuildUtil.getCurScreenHeightInDip());
        this.mScreenStrategyContext.dispatchScreenSizeChange(screenSize, configuration);
        onConfigurationChanged(this.mScreenStrategyContext.calConfigurationCaseType(screenSize, configuration), configuration);
    }
}
