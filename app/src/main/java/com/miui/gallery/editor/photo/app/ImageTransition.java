package com.miui.gallery.editor.photo.app;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.transition.ChangeBounds;
import android.transition.TransitionValues;
import android.util.Property;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.miui.gallery.app.MatrixEvaluator;
import com.miui.gallery.util.BaseMiscUtil;

/* loaded from: classes2.dex */
public class ImageTransition extends ChangeBounds {
    public static Property<ImageView, Matrix> PROPERTY_MATRIX = new Property<ImageView, Matrix>(Matrix.class, "matrix") { // from class: com.miui.gallery.editor.photo.app.ImageTransition.1
        @Override // android.util.Property
        public Matrix get(ImageView imageView) {
            return imageView.getImageMatrix();
        }

        @Override // android.util.Property
        public void set(ImageView imageView, Matrix matrix) {
            imageView.setImageMatrix(matrix);
        }
    };
    public boolean mEnter;
    public int mImageHeight;
    public int mImageWidth;
    public Matrix mMatrix;

    public ImageTransition(boolean z, Matrix matrix, int i, int i2) {
        this.mMatrix = new Matrix();
        this.mEnter = z;
        this.mMatrix = matrix;
        this.mImageWidth = i;
        this.mImageHeight = i2;
    }

    @Override // android.transition.ChangeBounds, android.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        if (this.mEnter) {
            captureInitialMatrix(transitionValues);
        } else {
            captureMatrix(transitionValues);
        }
    }

    @Override // android.transition.ChangeBounds, android.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        if (!this.mEnter) {
            captureInitialMatrix(transitionValues);
        } else {
            captureMatrix(transitionValues);
        }
    }

    public final void captureInitialMatrix(TransitionValues transitionValues) {
        RectF rectF = new RectF();
        rectF.set(0.0f, 0.0f, this.mImageWidth, this.mImageHeight);
        RectF rectF2 = new RectF();
        Matrix matrix = new Matrix();
        Drawable drawable = ((ImageView) transitionValues.view).getDrawable();
        if (drawable != null) {
            rectF2.set(0.0f, 0.0f, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            matrix.setRectToRect(rectF2, rectF, Matrix.ScaleToFit.FILL);
        }
        matrix.postConcat(this.mMatrix);
        transitionValues.values.put("image-matrix", matrix);
    }

    public final void captureMatrix(TransitionValues transitionValues) {
        ImageView imageView = (ImageView) transitionValues.view;
        RectF rectF = new RectF();
        rectF.set(0.0f, 0.0f, imageView.getWidth(), imageView.getHeight());
        RectF rectF2 = new RectF();
        Matrix matrix = new Matrix();
        Drawable drawable = imageView.getDrawable();
        if (drawable != null) {
            rectF2.set(0.0f, 0.0f, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            matrix.setRectToRect(rectF2, rectF, Matrix.ScaleToFit.CENTER);
        }
        transitionValues.values.put("image-matrix", matrix);
    }

    @Override // android.transition.ChangeBounds, android.transition.Transition
    public String[] getTransitionProperties() {
        String[] transitionProperties = super.getTransitionProperties();
        String[] strArr = new String[transitionProperties.length + 1];
        System.arraycopy(transitionProperties, 0, strArr, 0, transitionProperties.length);
        strArr[transitionProperties.length] = "image-matrix";
        return strArr;
    }

    @Override // android.transition.ChangeBounds, android.transition.Transition
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        ObjectAnimator objectAnimator;
        Drawable drawable;
        if (transitionValues == null || transitionValues2 == null) {
            return null;
        }
        Animator createAnimator = super.createAnimator(viewGroup, transitionValues, transitionValues2);
        if ((this.mEnter || (drawable = ((ImageView) transitionValues.view).getDrawable()) == null) ? false : !BaseMiscUtil.floatNear((this.mImageWidth * 1.0f) / this.mImageHeight, (drawable.getIntrinsicWidth() * 1.0f) / drawable.getIntrinsicHeight(), 0.01f)) {
            objectAnimator = ObjectAnimator.ofFloat(transitionValues.view, "transitionAlpha", 0.0f);
        } else {
            ObjectAnimator objectAnimator2 = new ObjectAnimator();
            objectAnimator2.setValues(PropertyValuesHolder.ofObject(PROPERTY_MATRIX, new MatrixEvaluator(), (Matrix) transitionValues.values.get("image-matrix"), (Matrix) transitionValues2.values.get("image-matrix")));
            objectAnimator2.setTarget(transitionValues2.view);
            objectAnimator2.addListener(new ConfigCropType());
            objectAnimator = objectAnimator2;
        }
        if (createAnimator == null) {
            return objectAnimator;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createAnimator, objectAnimator);
        return animatorSet;
    }

    /* loaded from: classes2.dex */
    public static class ConfigCropType implements Animator.AnimatorListener {
        public ImageView.ScaleType mScaleType;

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        public ConfigCropType() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            ImageView imageView = (ImageView) ((ObjectAnimator) animator).getTarget();
            this.mScaleType = imageView.getScaleType();
            imageView.setScaleType(ImageView.ScaleType.MATRIX);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ((ImageView) ((ObjectAnimator) animator).getTarget()).setScaleType(this.mScaleType);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            ((ImageView) ((ObjectAnimator) animator).getTarget()).setScaleType(this.mScaleType);
        }
    }
}
