package com.miui.gallery.collage.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.collage.render.PosterElementRender;
import com.miui.gallery.collage.utils.TextEditDialog;

/* loaded from: classes.dex */
public class PosterLayout extends ViewGroup {
    public static final float[] DEFAULT_LAYOUT_PARAMS = {0.0f, 0.0f, 1.0f, 1.0f};
    public PosterElementRender.TextEditorHolder mActiveTextEditorHolder;
    public Context mContext;
    public GesListener mGesListener;
    public GestureDetector mGestureDetector;
    public Paint mPaint;
    public PosterElementRender mPosterElementRender;
    public Rect mRectTemp;
    public ValueAnimator mTextAnimator;
    public TextEditDialog mTextEditDialog;
    public CustomTextWatch mTextWatch;

    public PosterLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRectTemp = new Rect();
        this.mGesListener = new GesListener();
        this.mTextEditDialog = new TextEditDialog();
        this.mTextWatch = new CustomTextWatch();
        init(context);
    }

    public final void init(Context context) {
        this.mContext = context;
        setWillNotDraw(false);
        Paint paint = new Paint(1);
        this.mPaint = paint;
        paint.setStyle(Paint.Style.FILL);
        this.mGestureDetector = new GestureDetector(this.mContext, this.mGesListener);
        this.mTextEditDialog.setTextWatch(this.mTextWatch);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        PosterElementRender posterElementRender = this.mPosterElementRender;
        if (posterElementRender == null) {
            return;
        }
        canvas.drawColor(posterElementRender.getBackground());
        drawElement(canvas);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    public final void drawElement(Canvas canvas) {
        PosterElementRender posterElementRender = this.mPosterElementRender;
        if (posterElementRender == null) {
            return;
        }
        posterElementRender.draw(canvas);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int width = getWidth();
        int height = getHeight();
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                float f = width;
                float f2 = height;
                childAt.layout((int) (layoutParams.mLeft * f), (int) (layoutParams.mTop * f2), (int) (f * layoutParams.mRight), (int) (f2 * layoutParams.mBottom));
            }
        }
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        measureChildren(i, i2);
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        public float mBottom;
        public float mLeft;
        public float mRight;
        public float mTop;

        public LayoutParams(float... fArr) {
            super(0, 0);
            this.mLeft = fArr[0];
            this.mTop = fArr[1];
            this.mRight = fArr[2];
            this.mBottom = fArr[3];
        }
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(DEFAULT_LAYOUT_PARAMS);
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(DEFAULT_LAYOUT_PARAMS);
    }

    @Override // android.view.ViewGroup
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public void setRenderData(PosterElementRender posterElementRender) {
        ValueAnimator valueAnimator = this.mTextAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.mPosterElementRender = posterElementRender;
        if (posterElementRender != null) {
            notifyTextEdit();
        }
        invalidate();
    }

    public final void notifyTextEdit() {
        ValueAnimator valueAnimator = this.mTextAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        } else {
            AnimListener animListener = new AnimListener();
            ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f, 1.0f, 0.0f);
            this.mTextAnimator = ofFloat;
            ofFloat.setDuration(3000L);
            this.mTextAnimator.setRepeatCount(0);
            this.mTextAnimator.setInterpolator(new LinearInterpolator());
            this.mTextAnimator.addUpdateListener(animListener);
            this.mTextAnimator.addListener(animListener);
        }
        PosterElementRender.TextEditorHolder[] textEditorHolders = this.mPosterElementRender.getTextEditorHolders();
        if (textEditorHolders == null || textEditorHolders.length <= 0) {
            return;
        }
        this.mTextAnimator.start();
    }

    /* loaded from: classes.dex */
    public class AnimListener implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        public AnimListener() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            PosterLayout.this.setTextAnimProgress(((Float) valueAnimator.getAnimatedValue()).floatValue());
            PosterLayout.this.invalidate();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            PosterLayout.this.setTextAnimProgress(0.0f);
            PosterLayout.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTextAnimProgress(float f) {
        PosterElementRender.TextEditorHolder[] textEditorHolders;
        PosterElementRender posterElementRender = this.mPosterElementRender;
        if (posterElementRender == null || (textEditorHolders = posterElementRender.getTextEditorHolders()) == null || textEditorHolders.length <= 0) {
            return;
        }
        for (PosterElementRender.TextEditorHolder textEditorHolder : textEditorHolders) {
            textEditorHolder.setCurrentTextProgress(f);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mGestureDetector.onTouchEvent(motionEvent);
    }

    /* loaded from: classes.dex */
    public class GesListener implements GestureDetector.OnGestureListener {
        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent motionEvent) {
        }

        public GesListener() {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            PosterLayout posterLayout = PosterLayout.this;
            posterLayout.mActiveTextEditorHolder = posterLayout.findTextHolderByTouchEvent(motionEvent.getX(), motionEvent.getY());
            if (PosterLayout.this.mActiveTextEditorHolder != null) {
                PosterLayout posterLayout2 = PosterLayout.this;
                posterLayout2.showDialog(posterLayout2.mActiveTextEditorHolder.getCurrentText(), !PosterLayout.this.mActiveTextEditorHolder.hasModify(), PosterLayout.this.mActiveTextEditorHolder.getMaxSize());
            }
            return true;
        }
    }

    /* loaded from: classes.dex */
    public class CustomTextWatch implements TextWatcher {
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public CustomTextWatch() {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (PosterLayout.this.mActiveTextEditorHolder != null) {
                PosterLayout.this.mActiveTextEditorHolder.setTextAndCount(charSequence.toString(), PosterLayout.this.mRectTemp);
                PosterLayout.this.invalidate();
            }
        }
    }

    public final void showDialog(String str, boolean z, int i) {
        if (!(this.mContext instanceof Activity) || this.mTextEditDialog.isShowing()) {
            return;
        }
        this.mTextEditDialog.setWillEditText(str, z);
        this.mTextEditDialog.setMaxEditLength(i);
        this.mTextEditDialog.showAllowingStateLoss(((FragmentActivity) this.mContext).getSupportFragmentManager(), "PosterLayout");
    }

    public PosterElementRender.TextEditorHolder findTextHolderByTouchEvent(float f, float f2) {
        PosterElementRender.TextEditorHolder[] textEditorHolders;
        PosterElementRender posterElementRender = this.mPosterElementRender;
        if (posterElementRender != null && (textEditorHolders = posterElementRender.getTextEditorHolders()) != null && textEditorHolders.length > 0) {
            for (PosterElementRender.TextEditorHolder textEditorHolder : textEditorHolders) {
                if (textEditorHolder.contains(f, f2)) {
                    return textEditorHolder;
                }
            }
        }
        return null;
    }
}
