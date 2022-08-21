package com.miui.gallery.widget.imageview;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Matrix;
import com.miui.gallery.app.MatrixEvaluator;

/* loaded from: classes2.dex */
public class MatrixTransition {
    public Matrix[] mCurrentMatrix;
    public MatrixUpdateListener mMatrixUpdateListener;
    public ValueAnimator mValueAnimator;
    public ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.imageview.MatrixTransition.1
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int length = valueAnimator.getValues().length;
            for (int i = 0; i < length; i++) {
                Matrix matrix = MatrixTransition.this.mCurrentMatrix[i];
                matrix.set((Matrix) valueAnimator.getAnimatedValue("property_matrix" + i));
            }
            if (MatrixTransition.this.mMatrixUpdateListener != null) {
                MatrixTransition.this.mMatrixUpdateListener.onMatrixUpdate();
            }
        }
    };
    public Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() { // from class: com.miui.gallery.widget.imageview.MatrixTransition.2
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (MatrixTransition.this.mMatrixUpdateListener != null) {
                MatrixTransition.this.mMatrixUpdateListener.onAnimationStart();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            MatrixTransition.this.mCurrentMatrix = null;
            if (MatrixTransition.this.mMatrixUpdateListener != null) {
                MatrixTransition.this.mMatrixUpdateListener.onAnimationEnd();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            MatrixTransition.this.mCurrentMatrix = null;
            if (MatrixTransition.this.mMatrixUpdateListener != null) {
                MatrixTransition.this.mMatrixUpdateListener.onAnimationEnd();
            }
        }
    };

    /* loaded from: classes2.dex */
    public interface MatrixUpdateListener {
        void onAnimationEnd();

        void onAnimationStart();

        void onMatrixUpdate();
    }

    public MatrixTransition() {
        ValueAnimator valueAnimator = new ValueAnimator();
        this.mValueAnimator = valueAnimator;
        valueAnimator.setDuration(300L);
        this.mValueAnimator.addUpdateListener(this.mAnimatorUpdateListener);
        this.mValueAnimator.addListener(this.mAnimatorListener);
    }

    public void animMatrix(Matrix... matrixArr) {
        if (matrixArr.length % 2 != 0) {
            throw new RuntimeException("values length should be a even number !");
        }
        this.mValueAnimator.cancel();
        int length = matrixArr.length / 2;
        this.mCurrentMatrix = new Matrix[length];
        PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[length];
        for (int i = 0; i < matrixArr.length; i += 2) {
            Matrix matrix = matrixArr[i];
            Matrix matrix2 = matrixArr[i + 1];
            int i2 = i / 2;
            this.mCurrentMatrix[i2] = matrix;
            propertyValuesHolderArr[i2] = PropertyValuesHolder.ofObject("property_matrix" + i2, new MatrixEvaluator(), new Matrix(matrix), matrix2);
        }
        this.mValueAnimator.setValues(propertyValuesHolderArr);
        this.mValueAnimator.start();
    }

    public void setMatrixUpdateListener(MatrixUpdateListener matrixUpdateListener) {
        this.mMatrixUpdateListener = matrixUpdateListener;
    }
}
