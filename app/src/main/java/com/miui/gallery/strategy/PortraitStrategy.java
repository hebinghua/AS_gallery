package com.miui.gallery.strategy;

import android.app.Activity;
import com.android.internal.WindowCompat;
import com.miui.gallery.util.SystemUiUtil;

/* loaded from: classes2.dex */
public class PortraitStrategy implements IStrategy$IDirectionStrategy {
    @Override // com.miui.gallery.strategy.IStrategy$IDirectionStrategy
    public void setStateBarVisibility(Activity activity) {
        SystemUiUtil.clearWindowFullScreenFlag(activity);
    }

    @Override // com.miui.gallery.strategy.IStrategy$IDirectionStrategy
    public void setCutoutModeShortEdges(IStrategyFollower iStrategyFollower) {
        WindowCompat.setCutoutModeShortEdges(iStrategyFollower.mo546getActivity().getWindow());
    }
}
