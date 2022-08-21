package miuix.animation;

import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.FloatProperty;

/* loaded from: classes3.dex */
public interface IStateStyle extends IStateContainer {
    IStateStyle add(String str, int i);

    IStateStyle add(FloatProperty floatProperty, float f);

    IStateStyle addListener(TransitionListener transitionListener);

    IStateStyle fromTo(Object obj, Object obj2, AnimConfig... animConfigArr);

    AnimState getCurrentState();

    long predictDuration(Object... objArr);

    IStateStyle set(Object obj);

    IStateStyle setFlags(long j);

    IStateStyle setTo(Object obj);

    IStateStyle setTo(Object obj, AnimConfig... animConfigArr);

    IStateStyle setTo(Object... objArr);

    IStateStyle setup(Object obj);

    IStateStyle then(Object obj, AnimConfig... animConfigArr);

    IStateStyle to(Object obj, AnimConfig... animConfigArr);

    IStateStyle to(Object... objArr);

    IStateStyle to(AnimConfig... animConfigArr);
}
