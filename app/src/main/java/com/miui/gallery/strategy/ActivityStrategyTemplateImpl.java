package com.miui.gallery.strategy;

import android.content.Context;
import android.content.res.Configuration;
import android.view.ActionMode;
import androidx.lifecycle.Lifecycle;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.util.BaseScreenUtil;

/* loaded from: classes2.dex */
public class ActivityStrategyTemplateImpl {
    public boolean isInActionMode;
    public final Context mContext;
    public final StrategyContext mStrategyContext;

    public ActivityStrategyTemplateImpl(IStrategyFollower iStrategyFollower) {
        this.mContext = iStrategyFollower.mo546getActivity();
        Lifecycle lifecycle = iStrategyFollower.mo546getActivity().getLifecycle();
        StrategyContext strategyContext = new StrategyContext(iStrategyFollower);
        this.mStrategyContext = strategyContext;
        lifecycle.addObserver(strategyContext);
    }

    public void onWindowFocusChanged(boolean z) {
        this.mStrategyContext.onNavigationBarSwapped(BaseScreenUtil.isFullScreenGestureNav(this.mContext), this.isInActionMode);
    }

    public void onActionModeStarted(ActionMode actionMode) {
        this.isInActionMode = true;
        this.mStrategyContext.onActionModeStarted(BaseScreenUtil.isFullScreenGestureNav(this.mContext));
    }

    public void onActionModeFinished(ActionMode actionMode) {
        this.isInActionMode = false;
        this.mStrategyContext.onActionModeFinished(BaseScreenUtil.isFullScreenGestureNav(this.mContext));
    }

    public void onConfigurationChanged(Configuration configuration) {
        StrategyContext strategyContext = this.mStrategyContext;
        if (strategyContext != null) {
            strategyContext.onConfigurationChanged(configuration);
        }
    }

    public void onMultiWindowModeChanged(boolean z, Configuration configuration) {
        StrategyContext strategyContext = this.mStrategyContext;
        if (strategyContext != null) {
            strategyContext.onMultiWindowModeChanged(z, configuration);
        }
    }

    public void requestDisableStrategy(StrategyContext.DisableStrategyType disableStrategyType) {
        StrategyContext strategyContext = this.mStrategyContext;
        if (strategyContext != null) {
            strategyContext.requestDisableStrategy(disableStrategyType);
        }
    }
}
