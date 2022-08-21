package com.miui.pickdrag.anim;

import android.view.View;
import com.miui.pickdrag.utils.Device;
import kotlin.jvm.internal.Intrinsics;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.EaseManager;

/* compiled from: PickerActivityTransitionAnimations.kt */
/* loaded from: classes3.dex */
public final class PickerActivityTransitionAnimations {
    public static final PickerActivityTransitionAnimations INSTANCE = new PickerActivityTransitionAnimations();

    public final void startSlideFromBottomAnimation(View target, TransitionListener listener) {
        Intrinsics.checkNotNullParameter(target, "target");
        Intrinsics.checkNotNullParameter(listener, "listener");
        Folme.clean(target);
        AnimState animState = new AnimState();
        ViewProperty viewProperty = ViewProperty.TRANSLATION_Y;
        Folme.useAt(target).state().fromTo(animState.add(viewProperty, Device.getRealDisplayHeight(target.getContext()), new long[0]), new AnimState().add(viewProperty, 0, new long[0]), new AnimConfig().setEase(EaseManager.getStyle(-2, 0.9f, 0.4f)).addListeners(listener));
    }

    public final void startSlideFromRightAnimation(View target, TransitionListener listener) {
        Intrinsics.checkNotNullParameter(target, "target");
        Intrinsics.checkNotNullParameter(listener, "listener");
        Folme.clean(target);
        AnimState animState = new AnimState();
        ViewProperty viewProperty = ViewProperty.TRANSLATION_X;
        Folme.useAt(target).state().fromTo(animState.add(viewProperty, Device.getScreenWidth(), new long[0]), new AnimState().add(viewProperty, 0, new long[0]), new AnimConfig().setEase(EaseManager.getStyle(-2, 0.9f, 0.4f)).addListeners(listener));
    }

    public final void startSlideToBottomAnimation(View target, TransitionListener listener) {
        Intrinsics.checkNotNullParameter(target, "target");
        Intrinsics.checkNotNullParameter(listener, "listener");
        Folme.clean(target);
        Folme.useAt(target).state().to(new AnimState().add(ViewProperty.TRANSLATION_Y, Device.getRealDisplayHeight(target.getContext()), new long[0]), new AnimConfig().setEase(EaseManager.getStyle(-2, 0.9f, 0.4f)).addListeners(listener));
    }

    public final void startSlideToRightAnimation(View target, TransitionListener listener) {
        Intrinsics.checkNotNullParameter(target, "target");
        Intrinsics.checkNotNullParameter(listener, "listener");
        Folme.clean(target);
        Folme.useAt(target).state().to(new AnimState().add(ViewProperty.TRANSLATION_X, Device.getScreenWidth(), new long[0]), new AnimConfig().setEase(EaseManager.getStyle(-2, 0.9f, 0.4f)).addListeners(listener));
    }
}
