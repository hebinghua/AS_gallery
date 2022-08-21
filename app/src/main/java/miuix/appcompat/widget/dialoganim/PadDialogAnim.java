package miuix.appcompat.widget.dialoganim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import java.lang.ref.WeakReference;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.EaseManager;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.widget.DialogAnimHelper;
import miuix.internal.util.AnimHelper;

/* loaded from: classes3.dex */
public class PadDialogAnim implements IDialogAnim {
    @Override // miuix.appcompat.widget.dialoganim.IDialogAnim
    public void cancelAnimator() {
    }

    @Override // miuix.appcompat.widget.dialoganim.IDialogAnim
    public void executeDismissAnim(View view, View view2, DialogAnimHelper.OnDismiss onDismiss) {
        if ("hide".equals(view.getTag())) {
            return;
        }
        dismissPanel(view, new WeakRefDismissListener(onDismiss, view));
        DimAnimator.dismiss(view2);
    }

    public final void dismissPanel(View view, WeakRefDismissListener weakRefDismissListener) {
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat(ViewProperty.ALPHA, 1.0f, 0.0f);
        float scale = getScale(view);
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, ofFloat, PropertyValuesHolder.ofFloat(ViewProperty.SCALE_X, 1.0f, scale), PropertyValuesHolder.ofFloat(ViewProperty.SCALE_Y, 1.0f, scale));
        ofPropertyValuesHolder.setInterpolator(new DecelerateInterpolator(1.5f));
        ofPropertyValuesHolder.addListener(weakRefDismissListener);
        ofPropertyValuesHolder.setDuration(200L);
        ofPropertyValuesHolder.start();
    }

    @Override // miuix.appcompat.widget.dialoganim.IDialogAnim
    public void executeShowAnim(View view, View view2, boolean z, AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener) {
        if (view.getScaleX() != 1.0f) {
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
        }
        if (AnimHelper.isDialogDebugInAndroidUIThreadEnabled()) {
            executeShowAnimAndroidUIThread(view, onDialogShowAnimListener);
        } else {
            AnimConfig animConfig = new AnimConfig();
            animConfig.setEase(EaseManager.getStyle(-2, 0.8f, 0.3f));
            animConfig.addListeners(new WeakRefShowListener(onDialogShowAnimListener, view));
            Folme.useAt(view).state().setFlags(1L).fromTo(getState(true, true, view), getState(true, false, view), animConfig);
        }
        DimAnimator.show(view2);
    }

    public final AnimState getState(boolean z, boolean z2, View view) {
        AnimState animState = new AnimState();
        float f = 1.0f;
        if (z) {
            if (z2) {
                f = getScale(view);
            }
        } else if (!z2) {
            f = getScale(view);
        }
        if (z) {
            z2 = !z2 ? 1 : 0;
        }
        double d = f;
        animState.add(ViewProperty.SCALE_X, d);
        animState.add(ViewProperty.SCALE_Y, d);
        animState.add(ViewProperty.ALPHA, z2 ? 1.0d : 0.0d);
        return animState;
    }

    public final float getScale(View view) {
        return Math.max(0.8f, 1.0f - (60.0f / Math.max(view.getWidth(), view.getHeight())));
    }

    public final void executeShowAnimAndroidUIThread(View view, AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener) {
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat(ViewProperty.ALPHA, 0.0f, 1.0f);
        float scale = getScale(view);
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, ofFloat, PropertyValuesHolder.ofFloat(ViewProperty.SCALE_X, scale, 1.0f), PropertyValuesHolder.ofFloat(ViewProperty.SCALE_Y, scale, 1.0f));
        ofPropertyValuesHolder.setInterpolator(new DecelerateInterpolator(1.5f));
        ofPropertyValuesHolder.addListener(new WeakRefShowOnAndroidUIListener(onDialogShowAnimListener, view));
        ofPropertyValuesHolder.setDuration(300L);
        ofPropertyValuesHolder.start();
    }

    /* loaded from: classes3.dex */
    public class WeakRefShowOnAndroidUIListener extends AnimatorListenerAdapter {
        public WeakReference<AlertDialog.OnDialogShowAnimListener> mOnDismiss;
        public WeakReference<View> mView;

        public WeakRefShowOnAndroidUIListener(AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener, View view) {
            this.mOnDismiss = new WeakReference<>(onDialogShowAnimListener);
            this.mView = new WeakReference<>(view);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            View view = this.mView.get();
            if (view != null) {
                view.setTag("show");
            }
            AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener = this.mOnDismiss.get();
            if (onDialogShowAnimListener != null) {
                onDialogShowAnimListener.onShowAnimStart();
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener = this.mOnDismiss.get();
            if (onDialogShowAnimListener != null) {
                onDialogShowAnimListener.onShowAnimComplete();
            }
        }
    }

    /* loaded from: classes3.dex */
    public class WeakRefDismissListener implements Animator.AnimatorListener {
        public WeakReference<DialogAnimHelper.OnDismiss> mOnDismiss;
        public WeakReference<View> mView;

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        public WeakRefDismissListener(DialogAnimHelper.OnDismiss onDismiss, View view) {
            this.mOnDismiss = new WeakReference<>(onDismiss);
            this.mView = new WeakReference<>(view);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            View view = this.mView.get();
            if (view != null) {
                view.setTag("hide");
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            DialogAnimHelper.OnDismiss onDismiss = this.mOnDismiss.get();
            if (onDismiss != null) {
                onDismiss.end();
            } else {
                Log.d("PhoneDialogAnim", "weak dismiss onComplete mOnDismiss get null");
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            DialogAnimHelper.OnDismiss onDismiss = this.mOnDismiss.get();
            if (onDismiss != null) {
                onDismiss.end();
            } else {
                Log.d("PhoneDialogAnim", "weak dismiss onCancel mOnDismiss get null");
            }
        }
    }

    /* loaded from: classes3.dex */
    public class WeakRefShowListener extends TransitionListener {
        public WeakReference<AlertDialog.OnDialogShowAnimListener> mShowDismiss;
        public WeakReference<View> mView;

        public WeakRefShowListener(AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener, View view) {
            this.mShowDismiss = new WeakReference<>(onDialogShowAnimListener);
            this.mView = new WeakReference<>(view);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            super.onComplete(obj);
            AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener = this.mShowDismiss.get();
            if (onDialogShowAnimListener != null) {
                onDialogShowAnimListener.onShowAnimComplete();
            } else {
                Log.d("PhoneDialogAnim", "weak show onComplete mOnDismiss get null");
            }
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onBegin(Object obj) {
            super.onBegin(obj);
            View view = this.mView.get();
            if (view != null) {
                view.setTag("show");
            }
            AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener = this.mShowDismiss.get();
            if (onDialogShowAnimListener != null) {
                onDialogShowAnimListener.onShowAnimStart();
            } else {
                Log.d("PhoneDialogAnim", "weak show onCancel mOnDismiss get null");
            }
        }
    }
}
