package com.miui.gallery.app.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.miui.gallery.strategy.FragmentStrategyTemplateImpl;
import com.miui.gallery.strategy.IStrategyFollower;

/* loaded from: classes.dex */
public class AndroidFragment extends Fragment implements IStrategyFollower {
    public FragmentStrategyTemplateImpl mStrategyTemplate;

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mStrategyTemplate = new FragmentStrategyTemplateImpl(this);
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
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
