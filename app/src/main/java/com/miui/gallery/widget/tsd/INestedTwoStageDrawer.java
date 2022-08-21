package com.miui.gallery.widget.tsd;

import android.view.View;

/* loaded from: classes3.dex */
public interface INestedTwoStageDrawer {
    void addScrollerStateListener(InestedScrollerStateListener inestedScrollerStateListener);

    void cancelDrawerAnim();

    boolean isAnimating();

    boolean isDrawerOpen();

    void removeHeaderView(View view);

    void removeSubHeaderView(View view);

    void setContentView(View view);

    void setDragEnabled(boolean z);

    void setDrawerState(DrawerState drawerState, boolean z, DrawerAnimEndListener drawerAnimEndListener);

    void setHeaderView(View view);

    void setMarginEnabled(boolean z);

    void setStickEnable(boolean z);

    void setSubHeaderView(View view);
}
