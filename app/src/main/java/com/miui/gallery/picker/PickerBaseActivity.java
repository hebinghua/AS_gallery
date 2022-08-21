package com.miui.gallery.picker;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ActionMode;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.strategy.ActivityStrategyTemplateImpl;
import com.miui.gallery.strategy.IStrategyFollower;
import com.miui.security.CrossUserCompatActivity;

/* loaded from: classes2.dex */
public class PickerBaseActivity extends CrossUserCompatActivity implements IStrategyFollower {
    public ActivityStrategyTemplateImpl mStrategyTemplate;

    @Override // com.miui.gallery.strategy.IStrategyFollower
    /* renamed from: getActivity */
    public FragmentActivity mo546getActivity() {
        return this;
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mStrategyTemplate = new ActivityStrategyTemplateImpl(this);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        this.mStrategyTemplate.onWindowFocusChanged(z);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity, android.view.Window.Callback
    public void onActionModeStarted(ActionMode actionMode) {
        super.onActionModeStarted(actionMode);
        this.mStrategyTemplate.onActionModeStarted(actionMode);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity, android.view.Window.Callback
    public void onActionModeFinished(ActionMode actionMode) {
        super.onActionModeFinished(actionMode);
        this.mStrategyTemplate.onActionModeFinished(actionMode);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mStrategyTemplate.onConfigurationChanged(configuration);
    }

    @Override // android.app.Activity
    public void onMultiWindowModeChanged(boolean z, Configuration configuration) {
        super.onMultiWindowModeChanged(z);
        this.mStrategyTemplate.onMultiWindowModeChanged(z, configuration);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        StorageSolutionProvider.get().onHandleRequestPermissionResult(this, i, i2, intent);
        super.onActivityResult(i, i2, intent);
    }
}
