package com.miui.gallery.app.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import com.miui.gallery.strategy.FragmentStrategyTemplateImpl;
import com.miui.gallery.strategy.IStrategyFollower;
import miuix.preference.PreferenceFragment;

/* loaded from: classes.dex */
public abstract class MiuiPreferenceFragment extends PreferenceFragment implements IStrategyFollower {
    public FragmentStrategyTemplateImpl mStrategyTemplate;

    @Override // miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mStrategyTemplate = new FragmentStrategyTemplateImpl(this);
    }

    @Override // miuix.preference.PreferenceFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mStrategyTemplate.onConfigurationChanged(configuration);
    }

    @Override // androidx.fragment.app.Fragment
    public void onMultiWindowModeChanged(boolean z) {
        super.onMultiWindowModeChanged(z);
        this.mStrategyTemplate.onMultiWindowModeChanged(z);
    }
}
