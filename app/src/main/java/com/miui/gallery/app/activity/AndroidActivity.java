package com.miui.gallery.app.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ActionMode;
import androidx.fragment.app.FragmentActivity;
import com.miui.display.DisplayFeatureHelper;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.strategy.ActivityStrategyTemplateImpl;
import com.miui.gallery.strategy.IStrategyFollower;
import com.miui.gallery.util.ScreenUtils;

/* loaded from: classes.dex */
public class AndroidActivity extends FragmentActivity implements IStrategyFollower {
    public ActivityStrategyTemplateImpl mStrategyTemplate;

    @Override // com.miui.gallery.strategy.IStrategyFollower
    /* renamed from: getActivity */
    public FragmentActivity mo546getActivity() {
        return this;
    }

    public boolean useDefaultScreenSceneMode() {
        return true;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mStrategyTemplate = new ActivityStrategyTemplateImpl(this);
        requestDisableStrategy(StrategyContext.DisableStrategyType.NAVIGATION_BAR);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        this.mStrategyTemplate.onWindowFocusChanged(z);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onActionModeStarted(ActionMode actionMode) {
        super.onActionModeStarted(actionMode);
        this.mStrategyTemplate.onActionModeStarted(actionMode);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onActionModeFinished(ActionMode actionMode) {
        super.onActionModeFinished(actionMode);
        this.mStrategyTemplate.onActionModeFinished(actionMode);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mStrategyTemplate.onConfigurationChanged(configuration);
    }

    @Override // android.app.Activity
    public void onMultiWindowModeChanged(boolean z, Configuration configuration) {
        super.onMultiWindowModeChanged(z);
        this.mStrategyTemplate.onMultiWindowModeChanged(z, configuration);
    }

    public void requestDisableStrategy(StrategyContext.DisableStrategyType disableStrategyType) {
        this.mStrategyTemplate.requestDisableStrategy(disableStrategyType);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (!useDefaultScreenSceneMode() || !ScreenUtils.isUseScreenSceneMode()) {
            return;
        }
        DisplayFeatureHelper.setScreenSceneClassification(0);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        StorageSolutionProvider.get().onHandleRequestPermissionResult(this, i, i2, intent);
    }
}
