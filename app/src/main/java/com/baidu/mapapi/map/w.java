package com.baidu.mapapi.map;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

/* loaded from: classes.dex */
class w extends AnimatorListenerAdapter {
    public final /* synthetic */ SwipeDismissTouchListener a;

    public w(SwipeDismissTouchListener swipeDismissTouchListener) {
        this.a = swipeDismissTouchListener;
    }

    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        this.a.a();
    }
}
