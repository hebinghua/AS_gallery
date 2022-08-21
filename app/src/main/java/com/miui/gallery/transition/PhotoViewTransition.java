package com.miui.gallery.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Property;
import android.view.ViewGroup;
import com.github.chrisbanes.photoview.PhotoView;
import com.miui.gallery.app.MatrixEvaluator;

/* loaded from: classes2.dex */
public class PhotoViewTransition extends Transition {
    public static Property<PhotoView, Matrix> PROPERTY_MATRIX = new Property<PhotoView, Matrix>(Matrix.class, "matrix") { // from class: com.miui.gallery.transition.PhotoViewTransition.1
        @Override // android.util.Property
        public Matrix get(PhotoView photoView) {
            return photoView.getSuppMatrix();
        }

        @Override // android.util.Property
        public void set(PhotoView photoView, Matrix matrix) {
            photoView.setSuppMatrix(matrix);
        }
    };
    public boolean mIsEnter;
    public RectF mSrcDisplayRect;

    public PhotoViewTransition(boolean z, RectF rectF) {
        this.mIsEnter = z;
        this.mSrcDisplayRect = rectF;
    }

    @Override // android.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        if (this.mIsEnter) {
            captureMatrix(transitionValues);
        } else {
            transitionValues.values.put("image_matrix", new Matrix());
        }
    }

    @Override // android.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        if (!this.mIsEnter) {
            captureMatrix(transitionValues);
        } else {
            transitionValues.values.put("image_matrix", new Matrix());
        }
    }

    public final void captureMatrix(TransitionValues transitionValues) {
        PhotoView photoView = (PhotoView) transitionValues.view;
        Matrix matrix = new Matrix();
        RectF displayRect = photoView.getDisplayRect(photoView.getDisplayMatrix());
        if (displayRect == null) {
            displayRect = new RectF(0.0f, 0.0f, photoView.getWidth(), photoView.getHeight());
        }
        matrix.setRectToRect(displayRect, this.mSrcDisplayRect, Matrix.ScaleToFit.FILL);
        transitionValues.values.put("image_matrix", matrix);
    }

    @Override // android.transition.Transition
    public String[] getTransitionProperties() {
        return new String[]{"image_matrix"};
    }

    @Override // android.transition.Transition
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues == null || transitionValues2 == null) {
            return null;
        }
        return doCreateAnimator(transitionValues, transitionValues2);
    }

    public final Animator doCreateAnimator(TransitionValues transitionValues, final TransitionValues transitionValues2) {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(transitionValues2.view, PropertyValuesHolder.ofObject(PROPERTY_MATRIX, new MatrixEvaluator(), (Matrix) transitionValues.values.get("image_matrix"), (Matrix) transitionValues2.values.get("image_matrix")));
        ofPropertyValuesHolder.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.transition.PhotoViewTransition.2
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                ((PhotoView) transitionValues2.view).startSharedElementTransition();
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ((PhotoView) transitionValues2.view).endSharedElementTransition();
            }
        });
        return ofPropertyValuesHolder;
    }
}
