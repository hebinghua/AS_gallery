package miuix.appcompat.widget.dialoganim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import java.lang.ref.WeakReference;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.EaseManager;
import miuix.appcompat.R$id;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.widget.DialogAnimHelper;

/* loaded from: classes3.dex */
public class PhoneDialogAnim implements IDialogAnim {
    public static WeakReference<ValueAnimator> sValueAnimatorWeakRef;

    @Override // miuix.appcompat.widget.dialoganim.IDialogAnim
    public void cancelAnimator() {
        ValueAnimator valueAnimator;
        WeakReference<ValueAnimator> weakReference = sValueAnimatorWeakRef;
        if (weakReference == null || (valueAnimator = weakReference.get()) == null) {
            return;
        }
        valueAnimator.cancel();
    }

    @Override // miuix.appcompat.widget.dialoganim.IDialogAnim
    public void executeDismissAnim(View view, View view2, DialogAnimHelper.OnDismiss onDismiss) {
        if ("hide".equals(view.getTag())) {
            return;
        }
        dismissPanel(view, new WeakRefDismissListener(view, onDismiss));
        DimAnimator.dismiss(view2);
    }

    public final void dismissPanel(View view, WeakRefDismissListener weakRefDismissListener) {
        if (view == null) {
            return;
        }
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(ViewProperty.ALPHA, 1.0f, 0.0f), PropertyValuesHolder.ofFloat(ViewProperty.TRANSLATION_Y, view.getTranslationY(), 100.0f));
        ofPropertyValuesHolder.setInterpolator(new DecelerateInterpolator(1.5f));
        ofPropertyValuesHolder.addListener(weakRefDismissListener);
        ofPropertyValuesHolder.setDuration(200L);
        ofPropertyValuesHolder.start();
    }

    @Override // miuix.appcompat.widget.dialoganim.IDialogAnim
    public void executeShowAnim(final View view, View view2, final boolean z, final AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener) {
        if (view.getScaleX() != 1.0f) {
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
        }
        if (view.getHeight() > 0) {
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: miuix.appcompat.widget.dialoganim.PhoneDialogAnim.1
                @Override // android.view.View.OnLayoutChangeListener
                public void onLayoutChange(View view3, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    if (z || i4 == view3.getRootView().getHeight()) {
                        view3.removeOnLayoutChangeListener(this);
                        View view4 = view;
                        PhoneDialogAnim.relayoutView(view4, view4.getHeight());
                        View view5 = view;
                        int height = view5.getHeight();
                        boolean z2 = z;
                        WeakRefShowListener weakRefShowListener = new WeakRefShowListener(onDialogShowAnimListener, view);
                        PhoneDialogAnim phoneDialogAnim = PhoneDialogAnim.this;
                        View view6 = view;
                        PhoneDialogAnim.executeAnim(view5, height, 0, z2, weakRefShowListener, new WeakRefUpdateListener(view6, PhoneDialogAnim.getHolderAnimView(view6), z));
                        view3.setVisibility(0);
                    }
                }
            });
            view.setVisibility(4);
            view.setAlpha(1.0f);
        } else {
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: miuix.appcompat.widget.dialoganim.PhoneDialogAnim.2
                @Override // android.view.View.OnLayoutChangeListener
                public void onLayoutChange(View view3, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    int i9 = (i4 - i2) + 0;
                    PhoneDialogAnim.relayoutView(view3, i9);
                    view3.removeOnLayoutChangeListener(this);
                    boolean z2 = z;
                    WeakRefShowListener weakRefShowListener = new WeakRefShowListener(onDialogShowAnimListener, view);
                    PhoneDialogAnim phoneDialogAnim = PhoneDialogAnim.this;
                    View view4 = view;
                    PhoneDialogAnim.executeAnim(view3, i9, 0, z2, weakRefShowListener, new WeakRefUpdateListener(view4, PhoneDialogAnim.getHolderAnimView(view4), z));
                }
            });
        }
        DimAnimator.show(view2);
    }

    public static void executeAnim(View view, int i, int i2, boolean z, WeakRefShowListener weakRefShowListener, WeakRefUpdateListener weakRefUpdateListener) {
        View holderAnimView = getHolderAnimView(view);
        if (!z) {
            changeHeight(getHolderAnimView(holderAnimView), 15);
        }
        ValueAnimator ofInt = ValueAnimator.ofInt(i, i2);
        ofInt.setDuration(300L);
        ofInt.setInterpolator(EaseManager.getInterpolator(0, 0.85f, 0.66f));
        ofInt.addUpdateListener(weakRefUpdateListener);
        ofInt.addListener(weakRefShowListener);
        ofInt.start();
        sValueAnimatorWeakRef = new WeakReference<>(ofInt);
    }

    public static void changeHeight(View view, int i) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = i;
        layoutParams.gravity = 80;
        view.setLayoutParams(layoutParams);
    }

    public static void relayoutView(View view, int i) {
        view.setTranslationY(i);
    }

    public static View getHolderAnimView(View view) {
        return view.getRootView().findViewById(R$id.dialog_anim_holder);
    }

    /* loaded from: classes3.dex */
    public class WeakRefDismissListener implements Animator.AnimatorListener {
        public WeakReference<DialogAnimHelper.OnDismiss> mOnDismiss;
        public WeakReference<View> mView;

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        public WeakRefDismissListener(View view, DialogAnimHelper.OnDismiss onDismiss) {
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
                Log.d("PhoneDialogAnim", "onComplete mOnDismiss get null");
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            DialogAnimHelper.OnDismiss onDismiss = this.mOnDismiss.get();
            if (onDismiss != null) {
                onDismiss.end();
            } else {
                Log.d("PhoneDialogAnim", "onCancel mOnDismiss get null");
            }
        }
    }

    /* loaded from: classes3.dex */
    public class WeakRefShowListener extends AnimatorListenerAdapter {
        public WeakReference<AlertDialog.OnDialogShowAnimListener> mOnShow;
        public WeakReference<View> mView;

        public WeakRefShowListener(AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener, View view) {
            this.mOnShow = new WeakReference<>(onDialogShowAnimListener);
            this.mView = new WeakReference<>(view);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator, boolean z) {
            View view = this.mView.get();
            if (view != null) {
                view.setTag("show");
            }
            AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener = this.mOnShow.get();
            if (onDialogShowAnimListener != null) {
                onDialogShowAnimListener.onShowAnimStart();
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            done();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            super.onAnimationCancel(animator);
            done();
            View view = this.mView.get();
            if (view != null) {
                PhoneDialogAnim.relayoutView(view, 0);
            }
        }

        public final void done() {
            AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener = this.mOnShow.get();
            if (onDialogShowAnimListener != null) {
                onDialogShowAnimListener.onShowAnimComplete();
            }
            if (PhoneDialogAnim.sValueAnimatorWeakRef != null) {
                PhoneDialogAnim.sValueAnimatorWeakRef.clear();
                WeakReference unused = PhoneDialogAnim.sValueAnimatorWeakRef = null;
            }
        }
    }

    /* loaded from: classes3.dex */
    public class WeakRefUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        public WeakReference<View> mHolderView;
        public boolean mIsLandscape;
        public WeakReference<View> mView;

        public WeakRefUpdateListener(View view, View view2, boolean z) {
            this.mView = new WeakReference<>(view);
            this.mHolderView = new WeakReference<>(view2);
            this.mIsLandscape = z;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            View view = this.mView.get();
            View view2 = this.mHolderView.get();
            if (view == null || view2 == null) {
                return;
            }
            if ("hide".equals(view.getTag())) {
                valueAnimator.cancel();
                view2.setVisibility(8);
                return;
            }
            int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            if (this.mIsLandscape) {
                view2.setVisibility(8);
            } else if (intValue == 0) {
                view2.setVisibility(8);
            } else if (Math.abs(intValue) < 15) {
                view2.setVisibility(0);
            }
            PhoneDialogAnim.relayoutView(view, intValue);
        }
    }
}
