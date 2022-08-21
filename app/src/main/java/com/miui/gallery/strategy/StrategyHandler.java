package com.miui.gallery.strategy;

import android.content.res.Configuration;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public final class StrategyHandler {
    public IStrategy$IDirectionStrategy mDirectionStrategy;
    public IStrategy$INavigationBarStrategy mNavigationBarStrategy;
    public IStrategy$IWindowModeStrategy mWindowModeStrategy;
    public IStrategy$IWindowSizeStrategy mWindowSizeStrategy;

    public void setDirectionStrategy(IStrategy$IDirectionStrategy iStrategy$IDirectionStrategy) {
        this.mDirectionStrategy = iStrategy$IDirectionStrategy;
    }

    public void setWindowModeStrategy(IStrategy$IWindowModeStrategy iStrategy$IWindowModeStrategy) {
        this.mWindowModeStrategy = iStrategy$IWindowModeStrategy;
    }

    public void setScreenSizeStrategy(IStrategy$IWindowSizeStrategy iStrategy$IWindowSizeStrategy) {
        this.mWindowSizeStrategy = iStrategy$IWindowSizeStrategy;
    }

    public StrategyHandler setNavigationBarStrategy(IStrategy$INavigationBarStrategy iStrategy$INavigationBarStrategy) {
        this.mNavigationBarStrategy = iStrategy$INavigationBarStrategy;
        return this;
    }

    public void handleOnConfigurationChanged(IStrategyFollower iStrategyFollower, Configuration configuration) {
        DefaultLogger.d("StrategyHandler", iStrategyFollower.toString() + " OnConfigurationChanged => ");
        if (this.mDirectionStrategy == null || iStrategyFollower.mo546getActivity() == null) {
            return;
        }
        this.mDirectionStrategy.setStateBarVisibility(iStrategyFollower.mo546getActivity());
        this.mDirectionStrategy.setCutoutModeShortEdges(iStrategyFollower);
    }

    public void handleOnMultiWindowModeChanged(IStrategyFollower iStrategyFollower, boolean z) {
        DefaultLogger.d("StrategyHandler", iStrategyFollower.toString() + " OnMultiWindowModeChanged => ");
    }

    public void handleOnMultiWindowModeChanged(IStrategyFollower iStrategyFollower, boolean z, Configuration configuration) {
        DefaultLogger.d("StrategyHandler", iStrategyFollower.toString() + " OnMultiWindowModeChanged => ");
    }

    public void handleOnCreate(IStrategyFollower iStrategyFollower) {
        DefaultLogger.d("StrategyHandler", iStrategyFollower.toString() + " OnCreate => ");
        if (this.mDirectionStrategy != null && iStrategyFollower.mo546getActivity() != null) {
            this.mDirectionStrategy.setStateBarVisibility(iStrategyFollower.mo546getActivity());
            this.mDirectionStrategy.setCutoutModeShortEdges(iStrategyFollower);
        }
        if (this.mWindowSizeStrategy == null || iStrategyFollower.mo546getActivity() == null) {
            return;
        }
        this.mWindowSizeStrategy.setActionBarExpandState(iStrategyFollower.mo546getActivity());
    }

    public void handleOnNavigationBarSwapped(IStrategyFollower iStrategyFollower, boolean z) {
        DefaultLogger.d("StrategyHandler", iStrategyFollower.toString() + " OnNavigationBarSwapped => ");
        IStrategy$INavigationBarStrategy iStrategy$INavigationBarStrategy = this.mNavigationBarStrategy;
        if (iStrategy$INavigationBarStrategy != null) {
            iStrategy$INavigationBarStrategy.setNavigationBarColor(iStrategyFollower.mo546getActivity(), z);
        }
    }

    public void handleOnActionModeStarted(IStrategyFollower iStrategyFollower) {
        DefaultLogger.d("StrategyHandler", iStrategyFollower.toString() + " OnActionModeStarted => ");
        IStrategy$INavigationBarStrategy iStrategy$INavigationBarStrategy = this.mNavigationBarStrategy;
        if (iStrategy$INavigationBarStrategy != null) {
            iStrategy$INavigationBarStrategy.setNavigationBarColor(iStrategyFollower.mo546getActivity(), true);
        }
    }

    public void handleOnActionModeFinished(IStrategyFollower iStrategyFollower) {
        DefaultLogger.d("StrategyHandler", iStrategyFollower.toString() + " OnActionModeFinished => ");
        IStrategy$INavigationBarStrategy iStrategy$INavigationBarStrategy = this.mNavigationBarStrategy;
        if (iStrategy$INavigationBarStrategy != null) {
            iStrategy$INavigationBarStrategy.setNavigationBarColor(iStrategyFollower.mo546getActivity(), false);
        }
    }

    public void handleOnStart(IStrategyFollower iStrategyFollower) {
        DefaultLogger.d("StrategyHandler", iStrategyFollower.toString() + " OnStart => ");
    }

    public void handleOnResume(IStrategyFollower iStrategyFollower) {
        DefaultLogger.d("StrategyHandler", iStrategyFollower.toString() + " OnResume => ");
    }

    public void handleOnPause(IStrategyFollower iStrategyFollower) {
        DefaultLogger.d("StrategyHandler", iStrategyFollower.toString() + " OnPause => ");
    }

    public void handleOnStop(IStrategyFollower iStrategyFollower) {
        DefaultLogger.d("StrategyHandler", iStrategyFollower.toString() + " OnStop => ");
    }

    public void handleOnDestroy(IStrategyFollower iStrategyFollower) {
        DefaultLogger.d("StrategyHandler", iStrategyFollower.toString() + " OnDestroy => ");
    }
}
