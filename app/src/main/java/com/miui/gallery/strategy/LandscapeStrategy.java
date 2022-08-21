package com.miui.gallery.strategy;

import android.app.Activity;
import com.android.internal.WindowCompat;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.MiuiSdkCompat;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.SystemUiUtil;

/* loaded from: classes2.dex */
public class LandscapeStrategy implements IStrategy$IDirectionStrategy {
    @Override // com.miui.gallery.strategy.IStrategy$IDirectionStrategy
    public void setStateBarVisibility(Activity activity) {
        if (!BaseBuildUtil.isLargeScreen(activity) && !activity.isInMultiWindowMode()) {
            SystemUiUtil.setWindowFullScreenFlag(activity);
        } else {
            SystemUiUtil.clearWindowFullScreenFlag(activity);
        }
    }

    @Override // com.miui.gallery.strategy.IStrategy$IDirectionStrategy
    public void setCutoutModeShortEdges(IStrategyFollower iStrategyFollower) {
        boolean isSupportCutoutModeShortEdges = MiuiSdkCompat.isSupportCutoutModeShortEdges(iStrategyFollower.mo546getActivity());
        boolean isSupportCutoutModeShortEdges2 = iStrategyFollower.isSupportCutoutModeShortEdges();
        boolean z = true;
        boolean z2 = !ScreenUtils.isForceBlack(iStrategyFollower.mo546getActivity());
        if (!iStrategyFollower.isNeedCheckCutoutBlacklist() || !ScreenUtils.isOnTheCutoutBlacklist(iStrategyFollower.mo546getActivity())) {
            z = false;
        }
        if (isSupportCutoutModeShortEdges && z2 && !z && isSupportCutoutModeShortEdges2) {
            WindowCompat.setCutoutModeShortEdges(iStrategyFollower.mo546getActivity().getWindow());
        } else {
            WindowCompat.setCutoutDefaultMode(iStrategyFollower.mo546getActivity().getWindow());
        }
    }
}
