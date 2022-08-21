package com.miui.gallery.widget.recyclerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.ViewConfiguration;
import androidx.annotation.Keep;
import com.miui.gallery.R;
import com.miui.gallery.R$styleable;
import com.miui.gallery.widget.recyclerview.transition.PhysicBasedInterpolator;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes3.dex */
public class ProportionTagListWrapper<T> {
    public final ProportionTagBaseAdapter<T> mAdapter;
    public AnimatorSet mHideAnimator;
    public ObjectAnimator mHideByAlphaAnimator;
    public ObjectAnimator mHideByTranslateAnimator;
    public int mListBackgroundColor;
    public int mListBackgroundColorAlpha;
    public int mListBackgroundMarginEnd;
    public int mListBackgroundRadius;
    public OnAnimatorListener mOnAnimatorListener;
    public List<ProportionTagView<T>> mProportionTagViews;
    public ObjectAnimator mScaleLargeAnimator;
    public ObjectAnimator mScaleSmallAnimator;
    public RectF mScrollBarBackgroundRectF;
    public int mScrollBarLeft;
    public int mScrollBarWidth;
    public int mScrollSlop;
    public int mScrollerThumbHeight;
    public AnimatorSet mShowAnimator;
    public ObjectAnimator mShowByAlphaAnimator;
    public ObjectAnimator mShowByTranslateAnimator;
    public int mTagViewLeft;
    public ProportionTagView<T> mTargetTagView;
    public Drawable mTimeLabelBackground;
    public int mTimeLabelDiameter;
    public int mAlpha = 255;
    public int mTranslationX = 0;
    public float mTagViewScale = 1.0f;
    public final Paint mPaint = new Paint();
    public int mLastScrollPosY = -1;
    public float mLastScrollTagPosY = -1.0f;
    public int mLastScrollPosYDistance = 0;
    public boolean mIsLayoutRTL = false;

    /* loaded from: classes3.dex */
    public interface OnAnimatorListener {
        void onProportionTagAnimatorFadeInEnd();

        void onProportionTagAnimatorFadeInStart();

        void onProportionTagAnimatorFadeOutEnd();

        void onProportionTagAnimatorFadeOutStart();

        void onProportionTagAnimatorUpdate();

        void onProportionTagViewAnimatorLargeEnd();

        void onProportionTagViewAnimatorSmallEnd();
    }

    public static /* synthetic */ void $r8$lambda$0DryAely6jpav0iBWIHachCrqdc(ProportionTagListWrapper proportionTagListWrapper, ValueAnimator valueAnimator) {
        proportionTagListWrapper.lambda$scaleSmallAnimator$3(valueAnimator);
    }

    /* renamed from: $r8$lambda$377raUnJTidoSrz-2fvOHJjBBG4 */
    public static /* synthetic */ void m1828$r8$lambda$377raUnJTidoSrz2fvOHJjBBG4(ProportionTagListWrapper proportionTagListWrapper, ValueAnimator valueAnimator) {
        proportionTagListWrapper.lambda$showTagByAnimator$0(valueAnimator);
    }

    public static /* synthetic */ void $r8$lambda$GOHOSelVc7mvJlCtJwcHtTfTkT8(ProportionTagListWrapper proportionTagListWrapper, ValueAnimator valueAnimator) {
        proportionTagListWrapper.lambda$hideTagByAnimator$1(valueAnimator);
    }

    /* renamed from: $r8$lambda$_6bFCD55PunKiDVYWvWoOYv-32A */
    public static /* synthetic */ void m1829$r8$lambda$_6bFCD55PunKiDVYWvWoOYv32A(ProportionTagListWrapper proportionTagListWrapper, ValueAnimator valueAnimator) {
        proportionTagListWrapper.lambda$scaleLargeAnimator$2(valueAnimator);
    }

    public ProportionTagListWrapper(ProportionTagBaseAdapter<T> proportionTagBaseAdapter) {
        this.mAdapter = proportionTagBaseAdapter;
        initStyle(proportionTagBaseAdapter.mContext);
        this.mScrollSlop = ViewConfiguration.get(proportionTagBaseAdapter.mContext).getScaledTouchSlop();
    }

    public final void initStyle(Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(null, R$styleable.CustomProportionTagList, R.attr.customProportionTagListStyle, R.style.TagListWrapProportion);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = obtainStyledAttributes.getIndex(i);
            switch (index) {
                case 0:
                    this.mScrollerThumbHeight = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 1:
                    this.mListBackgroundColor = obtainStyledAttributes.getInteger(index, 0);
                    break;
                case 2:
                    this.mListBackgroundRadius = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 3:
                    this.mScrollBarWidth = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 4:
                    this.mListBackgroundMarginEnd = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 5:
                    this.mTimeLabelDiameter = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 6:
                    this.mTimeLabelBackground = obtainStyledAttributes.getDrawable(index);
                    break;
            }
        }
        obtainStyledAttributes.recycle();
        Drawable drawable = this.mTimeLabelBackground;
        if (drawable != null) {
            int i2 = this.mTimeLabelDiameter;
            drawable.setBounds(0, 0, i2, i2);
        }
        this.mListBackgroundColorAlpha = Color.alpha(this.mListBackgroundColor);
        this.mPaint.setAntiAlias(true);
        this.mScrollBarBackgroundRectF = new RectF(0.0f, 0.0f, this.mScrollBarWidth, this.mScrollerThumbHeight);
    }

    public void setIsLayoutRTL(boolean z) {
        this.mIsLayoutRTL = z;
    }

    public void refreshViews(List<ProportionTagModel<T>> list, int i, int i2) {
        this.mProportionTagViews = this.mAdapter.setDataAndRefreshView(list, i, i2);
        this.mScrollBarBackgroundRectF.bottom = i2 + this.mScrollerThumbHeight;
    }

    public void showTagByAnimator() {
        cancelShowTagByAnimator();
        if (this.mShowByAlphaAnimator == null) {
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "alpha", 0, 255);
            this.mShowByAlphaAnimator = ofInt;
            ofInt.setDuration(250L);
            this.mShowByAlphaAnimator.setInterpolator(new PhysicBasedInterpolator(0.9f, 0.8f));
            this.mShowByAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.recyclerview.ProportionTagListWrapper$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ProportionTagListWrapper.m1828$r8$lambda$377raUnJTidoSrz2fvOHJjBBG4(ProportionTagListWrapper.this, valueAnimator);
                }
            });
            this.mShowByAlphaAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.widget.recyclerview.ProportionTagListWrapper.1
                {
                    ProportionTagListWrapper.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    if (ProportionTagListWrapper.this.mOnAnimatorListener != null) {
                        ProportionTagListWrapper.this.mOnAnimatorListener.onProportionTagAnimatorFadeInStart();
                    }
                    super.onAnimationStart(animator);
                }
            });
        }
        if (this.mShowByTranslateAnimator == null) {
            int[] iArr = new int[2];
            iArr[0] = this.mIsLayoutRTL ? -20 : 20;
            iArr[1] = 0;
            ObjectAnimator ofInt2 = ObjectAnimator.ofInt(this, "translationX", iArr);
            this.mShowByTranslateAnimator = ofInt2;
            ofInt2.setDuration(250L);
            this.mShowByTranslateAnimator.setInterpolator(new PhysicBasedInterpolator(0.9f, 0.8f));
        }
        if (this.mShowAnimator == null) {
            AnimatorSet animatorSet = new AnimatorSet();
            this.mShowAnimator = animatorSet;
            animatorSet.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.widget.recyclerview.ProportionTagListWrapper.2
                public boolean mCanceled = false;

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                }

                {
                    ProportionTagListWrapper.this = this;
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (!this.mCanceled) {
                        if (ProportionTagListWrapper.this.mOnAnimatorListener == null) {
                            return;
                        }
                        ProportionTagListWrapper.this.mOnAnimatorListener.onProportionTagAnimatorFadeInEnd();
                        return;
                    }
                    this.mCanceled = false;
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    this.mCanceled = true;
                }
            });
            this.mShowAnimator.play(this.mShowByAlphaAnimator).with(this.mShowByTranslateAnimator);
        }
        this.mShowAnimator.start();
    }

    public /* synthetic */ void lambda$showTagByAnimator$0(ValueAnimator valueAnimator) {
        OnAnimatorListener onAnimatorListener = this.mOnAnimatorListener;
        if (onAnimatorListener != null) {
            onAnimatorListener.onProportionTagAnimatorUpdate();
        }
    }

    public void cancelShowTagByAnimator() {
        AnimatorSet animatorSet = this.mShowAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }

    public void hideTagByAnimator(long j) {
        cancelHideTagByAnimator();
        if (this.mHideByAlphaAnimator == null) {
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "alpha", 255, 0);
            this.mHideByAlphaAnimator = ofInt;
            ofInt.setInterpolator(new PhysicBasedInterpolator(0.9f, 0.8f));
            this.mHideByAlphaAnimator.setDuration(250L);
            this.mHideByAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.recyclerview.ProportionTagListWrapper$$ExternalSyntheticLambda2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ProportionTagListWrapper.$r8$lambda$GOHOSelVc7mvJlCtJwcHtTfTkT8(ProportionTagListWrapper.this, valueAnimator);
                }
            });
            this.mHideByAlphaAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.widget.recyclerview.ProportionTagListWrapper.3
                {
                    ProportionTagListWrapper.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    if (ProportionTagListWrapper.this.mOnAnimatorListener != null) {
                        ProportionTagListWrapper.this.mOnAnimatorListener.onProportionTagAnimatorFadeOutStart();
                    }
                    super.onAnimationStart(animator);
                }
            });
        }
        if (this.mHideByTranslateAnimator == null) {
            int[] iArr = new int[2];
            iArr[0] = 0;
            iArr[1] = this.mIsLayoutRTL ? -20 : 20;
            ObjectAnimator ofInt2 = ObjectAnimator.ofInt(this, "translationX", iArr);
            this.mHideByTranslateAnimator = ofInt2;
            ofInt2.setInterpolator(new PhysicBasedInterpolator(0.9f, 0.8f));
            this.mHideByTranslateAnimator.setDuration(250L);
        }
        if (this.mHideAnimator == null) {
            AnimatorSet animatorSet = new AnimatorSet();
            this.mHideAnimator = animatorSet;
            animatorSet.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.widget.recyclerview.ProportionTagListWrapper.4
                public boolean mCanceled = false;

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                }

                {
                    ProportionTagListWrapper.this = this;
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (!this.mCanceled) {
                        if (ProportionTagListWrapper.this.mOnAnimatorListener == null) {
                            return;
                        }
                        ProportionTagListWrapper.this.mOnAnimatorListener.onProportionTagAnimatorFadeOutEnd();
                        return;
                    }
                    this.mCanceled = false;
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    this.mCanceled = true;
                }
            });
            this.mHideAnimator.play(this.mHideByAlphaAnimator).with(this.mHideByTranslateAnimator);
        }
        this.mHideAnimator.setStartDelay(j);
        this.mHideAnimator.start();
    }

    public /* synthetic */ void lambda$hideTagByAnimator$1(ValueAnimator valueAnimator) {
        OnAnimatorListener onAnimatorListener = this.mOnAnimatorListener;
        if (onAnimatorListener != null) {
            onAnimatorListener.onProportionTagAnimatorUpdate();
        }
    }

    public void cancelHideTagByAnimator() {
        AnimatorSet animatorSet = this.mHideAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }

    public void scaleLargeAnimator(ProportionTagView<T> proportionTagView) {
        cancelScaleLargeAnimator();
        if (this.mScaleLargeAnimator == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "tagViewScale", 1.0f, 1.1f);
            this.mScaleLargeAnimator = ofFloat;
            ofFloat.setInterpolator(new PhysicBasedInterpolator(0.95f, 0.15f));
            this.mScaleLargeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.recyclerview.ProportionTagListWrapper$$ExternalSyntheticLambda3
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ProportionTagListWrapper.m1829$r8$lambda$_6bFCD55PunKiDVYWvWoOYv32A(ProportionTagListWrapper.this, valueAnimator);
                }
            });
            this.mScaleLargeAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.widget.recyclerview.ProportionTagListWrapper.5
                {
                    ProportionTagListWrapper.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (ProportionTagListWrapper.this.mOnAnimatorListener != null) {
                        ProportionTagListWrapper.this.mOnAnimatorListener.onProportionTagViewAnimatorLargeEnd();
                    }
                }
            });
        }
        this.mTargetTagView = proportionTagView;
        this.mScaleLargeAnimator.start();
    }

    public /* synthetic */ void lambda$scaleLargeAnimator$2(ValueAnimator valueAnimator) {
        OnAnimatorListener onAnimatorListener = this.mOnAnimatorListener;
        if (onAnimatorListener != null) {
            onAnimatorListener.onProportionTagAnimatorUpdate();
        }
    }

    public void cancelScaleLargeAnimator() {
        ObjectAnimator objectAnimator = this.mScaleLargeAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    public void scaleSmallAnimator(ProportionTagView<T> proportionTagView) {
        cancelScaleSmallAnimator();
        if (this.mScaleSmallAnimator == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "tagViewScale", proportionTagView.getScaleX(), 1.0f);
            this.mScaleSmallAnimator = ofFloat;
            ofFloat.setInterpolator(new PhysicBasedInterpolator(0.95f, 0.15f));
            this.mScaleSmallAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.recyclerview.ProportionTagListWrapper$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ProportionTagListWrapper.$r8$lambda$0DryAely6jpav0iBWIHachCrqdc(ProportionTagListWrapper.this, valueAnimator);
                }
            });
            this.mScaleSmallAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.widget.recyclerview.ProportionTagListWrapper.6
                {
                    ProportionTagListWrapper.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (ProportionTagListWrapper.this.mOnAnimatorListener != null) {
                        ProportionTagListWrapper.this.mOnAnimatorListener.onProportionTagViewAnimatorSmallEnd();
                    }
                }
            });
        }
        this.mTargetTagView = proportionTagView;
        this.mScaleSmallAnimator.start();
    }

    public /* synthetic */ void lambda$scaleSmallAnimator$3(ValueAnimator valueAnimator) {
        OnAnimatorListener onAnimatorListener = this.mOnAnimatorListener;
        if (onAnimatorListener != null) {
            onAnimatorListener.onProportionTagAnimatorUpdate();
        }
    }

    public void cancelScaleSmallAnimator() {
        ObjectAnimator objectAnimator = this.mScaleSmallAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    public void setOnAnimatorListener(OnAnimatorListener onAnimatorListener) {
        this.mOnAnimatorListener = onAnimatorListener;
    }

    public void setVisible() {
        this.mAlpha = 255;
        resetDefaultValues();
    }

    public void setInvisible() {
        this.mAlpha = 0;
        resetDefaultValues();
    }

    public final void resetDefaultValues() {
        this.mTranslationX = 0;
        this.mLastScrollPosY = -1;
        this.mLastScrollPosYDistance = 0;
        this.mLastScrollTagPosY = -1.0f;
        this.mTagViewScale = 1.0f;
        this.mTargetTagView = null;
    }

    @Keep
    public int getAlpha() {
        return this.mAlpha;
    }

    @Keep
    public void setAlpha(int i) {
        this.mAlpha = i;
    }

    @Keep
    public void setTranslationX(int i) {
        this.mTranslationX = i;
    }

    @Keep
    public int getTranslationX() {
        return this.mTranslationX;
    }

    @Keep
    public void setTagViewScale(float f) {
        this.mTagViewScale = f;
    }

    @Keep
    public float getTagViewScale() {
        return this.mTagViewScale;
    }

    public void draw(int i, boolean z, Canvas canvas) {
        List<ProportionTagView<T>> list = this.mProportionTagViews;
        if (list == null || list.size() <= 1) {
            return;
        }
        ProportionTagView<T> proportionTagView = this.mProportionTagViews.get(0);
        if (z) {
            int i2 = this.mListBackgroundMarginEnd;
            this.mScrollBarLeft = i2;
            this.mTagViewLeft = i2 + this.mScrollBarWidth + proportionTagView.getTagMarginStart() + this.mTranslationX;
        } else {
            int i3 = (i - this.mListBackgroundMarginEnd) - this.mScrollBarWidth;
            this.mScrollBarLeft = i3;
            this.mTagViewLeft = ((i3 - proportionTagView.getTagMarginStart()) - proportionTagView.getViewWidth()) + this.mTranslationX;
        }
        int i4 = this.mScrollBarLeft + ((this.mScrollBarWidth - this.mTimeLabelDiameter) >> 1);
        canvas.save();
        canvas.translate(this.mScrollBarLeft, proportionTagView.getPositionY());
        this.mPaint.setColor(this.mListBackgroundColor);
        Paint paint = this.mPaint;
        int i5 = this.mAlpha;
        int i6 = this.mListBackgroundColorAlpha;
        if (i5 > i6) {
            i5 = i6;
        }
        paint.setAlpha(i5);
        RectF rectF = this.mScrollBarBackgroundRectF;
        int i7 = this.mListBackgroundRadius;
        canvas.drawRoundRect(rectF, i7, i7, this.mPaint);
        canvas.restore();
        for (int i8 = 0; i8 < this.mProportionTagViews.size(); i8++) {
            ProportionTagView<T> proportionTagView2 = this.mProportionTagViews.get(i8);
            proportionTagView2.setAlpha(this.mAlpha);
            float f = proportionTagView2 == this.mTargetTagView ? this.mTagViewScale : 1.0f;
            proportionTagView2.setScaleX(f);
            proportionTagView2.setScaleY(f);
            proportionTagView2.setIsLayoutRTL(z);
            float positionY = proportionTagView2.getPositionY();
            canvas.translate(this.mTagViewLeft, positionY);
            proportionTagView2.draw(canvas);
            canvas.translate(-this.mTagViewLeft, -positionY);
            float viewHeight = positionY + (proportionTagView2.getViewHeight() >> 1);
            canvas.translate(i4, viewHeight);
            this.mTimeLabelBackground.setAlpha(this.mAlpha);
            this.mTimeLabelBackground.draw(canvas);
            canvas.translate(-i4, -viewHeight);
        }
    }

    public boolean isPerformHapticFeedback(int i) {
        List<ProportionTagView<T>> list = this.mProportionTagViews;
        boolean z = false;
        if (list != null && list.size() > 1) {
            int i2 = i - this.mLastScrollPosY;
            if (Math.abs(i2) > this.mScrollSlop) {
                if ((this.mLastScrollPosYDistance ^ i2) < 0) {
                    this.mLastScrollTagPosY = -1.0f;
                }
                Iterator<ProportionTagView<T>> it = this.mProportionTagViews.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    ProportionTagView<T> next = it.next();
                    boolean z2 = this.mLastScrollPosY < next.getPositionY() && i >= next.getPositionY();
                    boolean z3 = this.mLastScrollPosY > next.getPositionY() && i <= next.getPositionY();
                    if (z2 || z3) {
                        if (this.mLastScrollTagPosY != next.getPositionY()) {
                            this.mLastScrollTagPosY = next.getPositionY();
                            z = true;
                            break;
                        }
                    }
                }
                this.mLastScrollPosY = i;
                this.mLastScrollPosYDistance = i2;
            }
        }
        return z;
    }

    public ProportionTagView<T> getProportionTagViewByPoint(int i, int i2) {
        List<ProportionTagView<T>> list = this.mProportionTagViews;
        if (list == null || list.size() <= 1) {
            return null;
        }
        for (ProportionTagView<T> proportionTagView : this.mProportionTagViews) {
            int i3 = this.mScrollBarLeft;
            int i4 = this.mListBackgroundMarginEnd;
            boolean z = false;
            boolean z2 = i >= i3 - i4 && i <= (i3 + this.mScrollBarWidth) + i4;
            boolean z3 = i2 >= proportionTagView.getPositionY() && i2 <= proportionTagView.getPositionY() + proportionTagView.getViewHeight();
            int i5 = this.mTagViewLeft;
            if (i >= i5 && i <= i5 + proportionTagView.getViewWidth()) {
                z = true;
            }
            if (z3 && (z || z2)) {
                return proportionTagView;
            }
        }
        return null;
    }
}
