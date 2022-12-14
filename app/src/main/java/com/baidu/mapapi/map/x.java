package com.baidu.mapapi.map;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.baidu.mapapi.map.SwipeDismissTouchListener;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class x extends AnimatorListenerAdapter {
    public final /* synthetic */ ViewGroup.LayoutParams a;
    public final /* synthetic */ int b;
    public final /* synthetic */ SwipeDismissTouchListener c;

    public x(SwipeDismissTouchListener swipeDismissTouchListener, ViewGroup.LayoutParams layoutParams, int i) {
        this.c = swipeDismissTouchListener;
        this.a = layoutParams;
        this.b = i;
    }

    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        SwipeDismissTouchListener.DismissCallbacks dismissCallbacks;
        View view;
        Object obj;
        View view2;
        View view3;
        dismissCallbacks = this.c.f;
        view = this.c.e;
        obj = this.c.l;
        dismissCallbacks.onDismiss(view, obj);
        view2 = this.c.e;
        view2.setTranslationX(0.0f);
        this.a.height = this.b;
        view3 = this.c.e;
        view3.setLayoutParams(this.a);
    }
}
