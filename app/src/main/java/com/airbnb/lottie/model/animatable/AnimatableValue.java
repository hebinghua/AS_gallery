package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

/* loaded from: classes.dex */
public interface AnimatableValue<K, A> {
    /* renamed from: createAnimation */
    BaseKeyframeAnimation<K, A> mo193createAnimation();

    List<Keyframe<K>> getKeyframes();

    boolean isStatic();
}
