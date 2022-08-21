package com.miui.gallery.app;

import android.content.res.Configuration;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import com.miui.gallery.strategy.IStrategyFollower;
import com.miui.gallery.strategy.StrategyFactory;
import com.miui.gallery.strategy.StrategyHandler;

/* loaded from: classes.dex */
public final class StrategyContext implements DefaultLifecycleObserver {
    public final StrategyFactory mStrategyFactory = new StrategyFactory();
    public final IStrategyFollower mStrategyFollower;

    /* loaded from: classes.dex */
    public enum DisableStrategyType {
        ALL,
        WINDOW_MODE,
        IN_MULTI_WINDOW,
        IN_SINGLE_WINDOW,
        DIRECTION,
        PORTRAIT_DIRECTION,
        LANDSCAPE_DIRECTION,
        SCREEN_SIZE,
        SMALL_SCREEN,
        LARGE_SCREEN,
        NAVIGATION_BAR,
        FULL_SCREEN,
        TRADITION,
        NULL
    }

    public StrategyContext(IStrategyFollower iStrategyFollower) {
        this.mStrategyFollower = iStrategyFollower;
    }

    public void requestDisableStrategy(DisableStrategyType disableStrategyType) {
        this.mStrategyFactory.setDisableStrategy(disableStrategyType);
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onCreate(LifecycleOwner lifecycleOwner) {
        StrategyHandler strategyHandler = this.mStrategyFactory.getStrategyHandler(this.mStrategyFollower);
        if (strategyHandler == null) {
            return;
        }
        strategyHandler.handleOnCreate(this.mStrategyFollower);
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onStart(LifecycleOwner lifecycleOwner) {
        StrategyHandler strategyHandler = this.mStrategyFactory.getStrategyHandler(this.mStrategyFollower);
        if (strategyHandler == null) {
            return;
        }
        strategyHandler.handleOnStart(this.mStrategyFollower);
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onResume(LifecycleOwner lifecycleOwner) {
        StrategyHandler strategyHandler = this.mStrategyFactory.getStrategyHandler(this.mStrategyFollower);
        if (strategyHandler == null) {
            return;
        }
        strategyHandler.handleOnResume(this.mStrategyFollower);
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onPause(LifecycleOwner lifecycleOwner) {
        StrategyHandler strategyHandler = this.mStrategyFactory.getStrategyHandler(this.mStrategyFollower);
        if (strategyHandler == null) {
            return;
        }
        strategyHandler.handleOnPause(this.mStrategyFollower);
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onStop(LifecycleOwner lifecycleOwner) {
        StrategyHandler strategyHandler = this.mStrategyFactory.getStrategyHandler(this.mStrategyFollower);
        if (strategyHandler == null) {
            return;
        }
        strategyHandler.handleOnStop(this.mStrategyFollower);
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onDestroy(LifecycleOwner lifecycleOwner) {
        StrategyHandler strategyHandler = this.mStrategyFactory.getStrategyHandler(this.mStrategyFollower);
        if (strategyHandler == null) {
            return;
        }
        strategyHandler.handleOnDestroy(this.mStrategyFollower);
        lifecycleOwner.getLifecycle().removeObserver(this);
    }

    public void onConfigurationChanged(Configuration configuration) {
        StrategyHandler strategyHandler = this.mStrategyFactory.getStrategyHandler(this.mStrategyFollower, configuration);
        if (strategyHandler == null) {
            return;
        }
        strategyHandler.handleOnConfigurationChanged(this.mStrategyFollower, configuration);
    }

    public void onMultiWindowModeChanged(boolean z) {
        StrategyHandler strategyHandler = this.mStrategyFactory.getStrategyHandler(this.mStrategyFollower, z);
        if (strategyHandler == null) {
            return;
        }
        strategyHandler.handleOnMultiWindowModeChanged(this.mStrategyFollower, z);
    }

    public void onMultiWindowModeChanged(boolean z, Configuration configuration) {
        StrategyHandler strategyHandler = this.mStrategyFactory.getStrategyHandler(z, configuration);
        if (strategyHandler == null) {
            return;
        }
        strategyHandler.handleOnMultiWindowModeChanged(this.mStrategyFollower, z, configuration);
    }

    public void onNavigationBarSwapped(boolean z, boolean z2) {
        StrategyHandler strategyHandler = this.mStrategyFactory.getStrategyHandler(z);
        if (strategyHandler == null) {
            return;
        }
        strategyHandler.handleOnNavigationBarSwapped(this.mStrategyFollower, z2);
    }

    public void onActionModeStarted(boolean z) {
        StrategyHandler strategyHandler = this.mStrategyFactory.getStrategyHandler(z);
        if (strategyHandler == null) {
            return;
        }
        strategyHandler.handleOnActionModeStarted(this.mStrategyFollower);
    }

    public void onActionModeFinished(boolean z) {
        StrategyHandler strategyHandler = this.mStrategyFactory.getStrategyHandler(z);
        if (strategyHandler == null) {
            return;
        }
        strategyHandler.handleOnActionModeFinished(this.mStrategyFollower);
    }
}
