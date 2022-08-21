package com.airbnb.lottie.animation.content;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.RectangleShape;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

/* loaded from: classes.dex */
public class RectangleContent implements BaseKeyframeAnimation.AnimationListener, KeyPathElementContent, PathContent {
    public final BaseKeyframeAnimation<?, Float> cornerRadiusAnimation;
    public final boolean hidden;
    public boolean isPathValid;
    public final LottieDrawable lottieDrawable;
    public final String name;
    public final BaseKeyframeAnimation<?, PointF> positionAnimation;
    public final BaseKeyframeAnimation<?, PointF> sizeAnimation;
    public final Path path = new Path();
    public final RectF rect = new RectF();
    public CompoundTrimPathContent trimPaths = new CompoundTrimPathContent();

    public RectangleContent(LottieDrawable lottieDrawable, BaseLayer baseLayer, RectangleShape rectangleShape) {
        this.name = rectangleShape.getName();
        this.hidden = rectangleShape.isHidden();
        this.lottieDrawable = lottieDrawable;
        BaseKeyframeAnimation<PointF, PointF> mo193createAnimation = rectangleShape.getPosition().mo193createAnimation();
        this.positionAnimation = mo193createAnimation;
        BaseKeyframeAnimation<PointF, PointF> mo193createAnimation2 = rectangleShape.getSize().mo193createAnimation();
        this.sizeAnimation = mo193createAnimation2;
        BaseKeyframeAnimation<Float, Float> mo193createAnimation3 = rectangleShape.getCornerRadius().mo193createAnimation();
        this.cornerRadiusAnimation = mo193createAnimation3;
        baseLayer.addAnimation(mo193createAnimation);
        baseLayer.addAnimation(mo193createAnimation2);
        baseLayer.addAnimation(mo193createAnimation3);
        mo193createAnimation.addUpdateListener(this);
        mo193createAnimation2.addUpdateListener(this);
        mo193createAnimation3.addUpdateListener(this);
    }

    @Override // com.airbnb.lottie.animation.content.Content
    public String getName() {
        return this.name;
    }

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener
    public void onValueChanged() {
        invalidate();
    }

    public final void invalidate() {
        this.isPathValid = false;
        this.lottieDrawable.invalidateSelf();
    }

    @Override // com.airbnb.lottie.animation.content.Content
    public void setContents(List<Content> list, List<Content> list2) {
        for (int i = 0; i < list.size(); i++) {
            Content content = list.get(i);
            if (content instanceof TrimPathContent) {
                TrimPathContent trimPathContent = (TrimPathContent) content;
                if (trimPathContent.getType() == ShapeTrimPath.Type.SIMULTANEOUSLY) {
                    this.trimPaths.addTrimPath(trimPathContent);
                    trimPathContent.addListener(this);
                }
            }
        }
    }

    @Override // com.airbnb.lottie.animation.content.PathContent
    public Path getPath() {
        if (this.isPathValid) {
            return this.path;
        }
        this.path.reset();
        if (this.hidden) {
            this.isPathValid = true;
            return this.path;
        }
        PointF mo190getValue = this.sizeAnimation.mo190getValue();
        float f = mo190getValue.x / 2.0f;
        float f2 = mo190getValue.y / 2.0f;
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation = this.cornerRadiusAnimation;
        float floatValue = baseKeyframeAnimation == null ? 0.0f : ((FloatKeyframeAnimation) baseKeyframeAnimation).getFloatValue();
        float min = Math.min(f, f2);
        if (floatValue > min) {
            floatValue = min;
        }
        PointF mo190getValue2 = this.positionAnimation.mo190getValue();
        this.path.moveTo(mo190getValue2.x + f, (mo190getValue2.y - f2) + floatValue);
        this.path.lineTo(mo190getValue2.x + f, (mo190getValue2.y + f2) - floatValue);
        int i = (floatValue > 0.0f ? 1 : (floatValue == 0.0f ? 0 : -1));
        if (i > 0) {
            RectF rectF = this.rect;
            float f3 = mo190getValue2.x;
            float f4 = floatValue * 2.0f;
            float f5 = mo190getValue2.y;
            rectF.set((f3 + f) - f4, (f5 + f2) - f4, f3 + f, f5 + f2);
            this.path.arcTo(this.rect, 0.0f, 90.0f, false);
        }
        this.path.lineTo((mo190getValue2.x - f) + floatValue, mo190getValue2.y + f2);
        if (i > 0) {
            RectF rectF2 = this.rect;
            float f6 = mo190getValue2.x;
            float f7 = mo190getValue2.y;
            float f8 = floatValue * 2.0f;
            rectF2.set(f6 - f, (f7 + f2) - f8, (f6 - f) + f8, f7 + f2);
            this.path.arcTo(this.rect, 90.0f, 90.0f, false);
        }
        this.path.lineTo(mo190getValue2.x - f, (mo190getValue2.y - f2) + floatValue);
        if (i > 0) {
            RectF rectF3 = this.rect;
            float f9 = mo190getValue2.x;
            float f10 = mo190getValue2.y;
            float f11 = floatValue * 2.0f;
            rectF3.set(f9 - f, f10 - f2, (f9 - f) + f11, (f10 - f2) + f11);
            this.path.arcTo(this.rect, 180.0f, 90.0f, false);
        }
        this.path.lineTo((mo190getValue2.x + f) - floatValue, mo190getValue2.y - f2);
        if (i > 0) {
            RectF rectF4 = this.rect;
            float f12 = mo190getValue2.x;
            float f13 = floatValue * 2.0f;
            float f14 = mo190getValue2.y;
            rectF4.set((f12 + f) - f13, f14 - f2, f12 + f, (f14 - f2) + f13);
            this.path.arcTo(this.rect, 270.0f, 90.0f, false);
        }
        this.path.close();
        this.trimPaths.apply(this.path);
        this.isPathValid = true;
        return this.path;
    }

    @Override // com.airbnb.lottie.model.KeyPathElement
    public void resolveKeyPath(KeyPath keyPath, int i, List<KeyPath> list, KeyPath keyPath2) {
        MiscUtils.resolveKeyPath(keyPath, i, list, keyPath2, this);
    }

    @Override // com.airbnb.lottie.model.KeyPathElement
    public <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        if (t == LottieProperty.RECTANGLE_SIZE) {
            this.sizeAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POSITION) {
            this.positionAnimation.setValueCallback(lottieValueCallback);
        } else if (t != LottieProperty.CORNER_RADIUS) {
        } else {
            this.cornerRadiusAnimation.setValueCallback(lottieValueCallback);
        }
    }
}
