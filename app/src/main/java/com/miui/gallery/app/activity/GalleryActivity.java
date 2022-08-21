package com.miui.gallery.app.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import com.miui.display.DisplayFeatureHelper;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.app.screenChange.ScreenSize;
import com.miui.gallery.app.screenChange.ScreenStrategyContext;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.ScreenUtils;

/* loaded from: classes.dex */
public class GalleryActivity extends MiuiActivity {
    public boolean isLargeScreenAndWindow = false;
    public ScreenStrategyContext mScreenStrategyContext;

    @Override // com.miui.gallery.app.activity.MiuiActivity, com.miui.gallery.strategy.IStrategyFollower
    /* renamed from: getActivity  reason: collision with other method in class */
    public GalleryActivity mo546getActivity() {
        return this;
    }

    public boolean useDefaultScreenSceneMode() {
        return true;
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Configuration configuration = getResources().getConfiguration();
        this.isLargeScreenAndWindow = BaseBuildUtil.isLargeScreenDevice() && BaseBuildUtil.isLargeHorizontalWindow();
        int curScreenWidthInDip = BaseBuildUtil.getCurScreenWidthInDip();
        int curScreenHeightInDip = BaseBuildUtil.getCurScreenHeightInDip();
        ScreenStrategyContext screenStrategyContext = new ScreenStrategyContext(mo546getActivity());
        this.mScreenStrategyContext = screenStrategyContext;
        screenStrategyContext.handleWhenCreate(new ScreenSize(curScreenWidthInDip, curScreenHeightInDip), configuration);
    }

    public void addScreenChangeListener(IScreenChange iScreenChange) {
        this.mScreenStrategyContext.addOnScreenChangeListener(iScreenChange);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (!useDefaultScreenSceneMode() || !ScreenUtils.isUseScreenSceneMode()) {
            return;
        }
        DisplayFeatureHelper.setScreenSceneClassification(0);
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.isLargeScreenAndWindow = BaseBuildUtil.isLargeScreenDevice() && BaseBuildUtil.isLargeHorizontalWindow();
        this.mScreenStrategyContext.dispatchScreenSizeChange(new ScreenSize(BaseBuildUtil.getCurScreenWidthInDip(), BaseBuildUtil.getCurScreenHeightInDip()), configuration);
    }
}
