package com.miui.gallery.strategy;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

/* loaded from: classes2.dex */
public interface IStrategyFollower extends LifecycleOwner {
    /* renamed from: getActivity */
    FragmentActivity mo546getActivity();

    default boolean isNeedCheckCutoutBlacklist() {
        return true;
    }

    default boolean isSupportCutoutModeShortEdges() {
        return true;
    }
}
