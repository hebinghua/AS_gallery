package com.miui.gallery.strategy;

import android.content.res.Configuration;
import androidx.lifecycle.Lifecycle;
import com.miui.gallery.app.StrategyContext;

/* loaded from: classes2.dex */
public class FragmentStrategyTemplateImpl {
    public final StrategyContext mStrategyContext;

    public FragmentStrategyTemplateImpl(IStrategyFollower iStrategyFollower) {
        Lifecycle lifecycle = iStrategyFollower.getLifecycle();
        StrategyContext strategyContext = new StrategyContext(iStrategyFollower);
        this.mStrategyContext = strategyContext;
        lifecycle.addObserver(strategyContext);
    }

    public void onConfigurationChanged(Configuration configuration) {
        StrategyContext strategyContext = this.mStrategyContext;
        if (strategyContext != null) {
            strategyContext.onConfigurationChanged(configuration);
        }
    }

    public void onMultiWindowModeChanged(boolean z) {
        StrategyContext strategyContext = this.mStrategyContext;
        if (strategyContext != null) {
            strategyContext.onMultiWindowModeChanged(z);
        }
    }

    public void requestDisableStrategy(StrategyContext.DisableStrategyType disableStrategyType) {
        StrategyContext strategyContext = this.mStrategyContext;
        if (strategyContext != null) {
            strategyContext.requestDisableStrategy(disableStrategyType);
        }
    }
}
