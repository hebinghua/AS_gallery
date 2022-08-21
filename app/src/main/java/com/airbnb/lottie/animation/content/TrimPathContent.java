package com.airbnb.lottie.animation.content;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class TrimPathContent implements Content, BaseKeyframeAnimation.AnimationListener {
    public final BaseKeyframeAnimation<?, Float> endAnimation;
    public final boolean hidden;
    public final List<BaseKeyframeAnimation.AnimationListener> listeners = new ArrayList();
    public final String name;
    public final BaseKeyframeAnimation<?, Float> offsetAnimation;
    public final BaseKeyframeAnimation<?, Float> startAnimation;
    public final ShapeTrimPath.Type type;

    @Override // com.airbnb.lottie.animation.content.Content
    public void setContents(List<Content> list, List<Content> list2) {
    }

    public TrimPathContent(BaseLayer baseLayer, ShapeTrimPath shapeTrimPath) {
        this.name = shapeTrimPath.getName();
        this.hidden = shapeTrimPath.isHidden();
        this.type = shapeTrimPath.getType();
        BaseKeyframeAnimation<Float, Float> mo193createAnimation = shapeTrimPath.getStart().mo193createAnimation();
        this.startAnimation = mo193createAnimation;
        BaseKeyframeAnimation<Float, Float> mo193createAnimation2 = shapeTrimPath.getEnd().mo193createAnimation();
        this.endAnimation = mo193createAnimation2;
        BaseKeyframeAnimation<Float, Float> mo193createAnimation3 = shapeTrimPath.getOffset().mo193createAnimation();
        this.offsetAnimation = mo193createAnimation3;
        baseLayer.addAnimation(mo193createAnimation);
        baseLayer.addAnimation(mo193createAnimation2);
        baseLayer.addAnimation(mo193createAnimation3);
        mo193createAnimation.addUpdateListener(this);
        mo193createAnimation2.addUpdateListener(this);
        mo193createAnimation3.addUpdateListener(this);
    }

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener
    public void onValueChanged() {
        for (int i = 0; i < this.listeners.size(); i++) {
            this.listeners.get(i).onValueChanged();
        }
    }

    public void addListener(BaseKeyframeAnimation.AnimationListener animationListener) {
        this.listeners.add(animationListener);
    }

    public ShapeTrimPath.Type getType() {
        return this.type;
    }

    public BaseKeyframeAnimation<?, Float> getStart() {
        return this.startAnimation;
    }

    public BaseKeyframeAnimation<?, Float> getEnd() {
        return this.endAnimation;
    }

    public BaseKeyframeAnimation<?, Float> getOffset() {
        return this.offsetAnimation;
    }

    public boolean isHidden() {
        return this.hidden;
    }
}
