package com.miui.gallery.widget.editwrapper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.EaseManager;

/* loaded from: classes2.dex */
public class EditableListViewItemAnimHelper {
    public boolean isHandleTouch;
    public boolean isOnlyUpAlphaAnimEnable;
    public ConfigParams mConfigParams;
    public ValueAnimator mDownAnimator;
    public ValueAnimator mEnlargeAnimator;
    public EnlargeRunnable mEnlargeRunnable;
    public HashSet<String> mHandleViewTypes;
    public HideRunnable mHideRunnable;
    public ShowRunnable mShowRunnable;
    public View mTouchView;
    public ValueAnimator mUpAnimator;

    public EditableListViewItemAnimHelper(ConfigParams configParams) {
        this.isHandleTouch = true;
        this.mConfigParams = configParams;
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int i) {
        this.isHandleTouch = i == 0;
        if (i == 1) {
            fastUpItemView(this.mTouchView, null);
        }
    }

    public <V extends View> void saveHandleTouchAnimItemTyp(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return;
        }
        this.mHandleViewTypes = new HashSet<>(Arrays.asList(strArr));
    }

    public boolean isHandleType(String str) {
        HashSet<String> hashSet;
        return !TextUtils.isEmpty(str) && (hashSet = this.mHandleViewTypes) != null && hashSet.contains(str);
    }

    public void release() {
        ValueAnimator valueAnimator = this.mDownAnimator;
        if (valueAnimator != null) {
            valueAnimator.removeAllListeners();
            this.mDownAnimator.removeAllUpdateListeners();
            if (Build.VERSION.SDK_INT >= 19) {
                this.mDownAnimator.pause();
            }
            this.mDownAnimator.cancel();
            this.mDownAnimator = null;
        }
        ValueAnimator valueAnimator2 = this.mUpAnimator;
        if (valueAnimator2 != null) {
            valueAnimator2.removeAllListeners();
            this.mUpAnimator.removeAllUpdateListeners();
            if (Build.VERSION.SDK_INT >= 19) {
                this.mUpAnimator.pause();
            }
            this.mUpAnimator.cancel();
            this.mUpAnimator = null;
        }
        ValueAnimator valueAnimator3 = this.mEnlargeAnimator;
        if (valueAnimator3 != null) {
            valueAnimator3.removeAllListeners();
            this.mEnlargeAnimator.removeAllUpdateListeners();
            if (Build.VERSION.SDK_INT >= 19) {
                this.mEnlargeAnimator.pause();
            }
            this.mEnlargeAnimator.cancel();
            this.mEnlargeAnimator = null;
        }
        EnlargeRunnable enlargeRunnable = this.mEnlargeRunnable;
        if (enlargeRunnable != null) {
            enlargeRunnable.cancel();
            View view = this.mTouchView;
            if (view != null) {
                view.removeCallbacks(this.mEnlargeRunnable);
            }
            this.mEnlargeRunnable = null;
        }
        HideRunnable hideRunnable = this.mHideRunnable;
        if (hideRunnable != null) {
            hideRunnable.cancel();
            View view2 = this.mTouchView;
            if (view2 != null) {
                view2.removeCallbacks(this.mHideRunnable);
            }
            this.mHideRunnable = null;
        }
        ShowRunnable showRunnable = this.mShowRunnable;
        if (showRunnable != null) {
            showRunnable.cancel();
            View view3 = this.mTouchView;
            if (view3 != null) {
                view3.removeCallbacks(this.mShowRunnable);
            }
            this.mShowRunnable = null;
        }
    }

    public boolean isNeedFastUp() {
        ConfigParams configParams = this.mConfigParams;
        return configParams != null && !configParams.isWithScale;
    }

    public void setOnlyUpAlphaAnimEnable(boolean z) {
        this.isOnlyUpAlphaAnimEnable = z;
    }

    /* loaded from: classes2.dex */
    public static class ConfigParams {
        public final long DEFAULT_DURATION = 250;
        public final TimeInterpolator DEFAULT_INTERPOLATOR = new DecelerateInterpolator();
        public boolean isWithAlpha;
        public boolean isWithScale;
        public AnimatorListenerAdapter mDownAnimatorListenerAdapter;
        public ValueAnimator.AnimatorUpdateListener mDownAnimatorUpdateListener;
        public long mDownDuration;
        public TimeInterpolator mDownTimeInterpolator;
        public float mDownTo;
        public AnimatorListenerAdapter mUpAnimatorListenerAdapter;
        public ValueAnimator.AnimatorUpdateListener mUpAnimatorUpdateListener;
        public long mUpDuration;
        public TimeInterpolator mUpTimeInterpolator;
        public float mUpTo;

        public void setDefaultDown() {
            this.mDownTo = 0.9f;
            this.mDownDuration = 250L;
            this.mDownTimeInterpolator = this.DEFAULT_INTERPOLATOR;
        }

        public void setWithTitleAlpha() {
            this.isWithAlpha = true;
        }

        public void setWithScale() {
            this.isWithScale = true;
        }

        public void setDefaultUp() {
            this.mUpTo = 1.0f;
            this.mUpDuration = 250L;
            this.mUpTimeInterpolator = this.DEFAULT_INTERPOLATOR;
        }
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public ConfigParams mConfigParams = new ConfigParams();

        public Builder withDownAnim() {
            this.mConfigParams.setDefaultDown();
            return this;
        }

        public Builder withDownAlphaEnlargeAnim() {
            this.mConfigParams.setDefaultDown();
            this.mConfigParams.setWithTitleAlpha();
            this.mConfigParams.setWithScale();
            return this;
        }

        public Builder withUpAnim() {
            this.mConfigParams.setDefaultUp();
            return this;
        }

        public Builder withUpAlphaNarrowAnim() {
            this.mConfigParams.setDefaultUp();
            this.mConfigParams.setWithTitleAlpha();
            this.mConfigParams.setWithScale();
            return this;
        }

        public EditableListViewItemAnimHelper build() {
            return new EditableListViewItemAnimHelper(this.mConfigParams);
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class BaseAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        public ConfigParams mAnimConfig;
        public WeakReference<View> mWeakTouchView;

        public abstract ValueAnimator.AnimatorUpdateListener getUpdateListener();

        public BaseAnimatorUpdateListener(View view, ConfigParams configParams) {
            this.mWeakTouchView = new WeakReference<>(view);
            this.mAnimConfig = configParams;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            WeakReference<View> weakReference = this.mWeakTouchView;
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            View view = this.mWeakTouchView.get();
            view.setScaleX(((Float) valueAnimator.getAnimatedValue()).floatValue());
            view.setScaleY(((Float) valueAnimator.getAnimatedValue()).floatValue());
            if (this.mAnimConfig == null || getUpdateListener() == null) {
                return;
            }
            getUpdateListener().onAnimationUpdate(valueAnimator);
        }
    }

    /* loaded from: classes2.dex */
    public static class DownAnimatorUpdateListener extends BaseAnimatorUpdateListener {
        public DownAnimatorUpdateListener(View view, ConfigParams configParams) {
            super(view, configParams);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewItemAnimHelper.BaseAnimatorUpdateListener
        public ValueAnimator.AnimatorUpdateListener getUpdateListener() {
            return this.mAnimConfig.mDownAnimatorUpdateListener;
        }
    }

    /* loaded from: classes2.dex */
    public static class UpAnimatorUpdateListener extends BaseAnimatorUpdateListener {
        public UpAnimatorUpdateListener(View view, ConfigParams configParams) {
            super(view, configParams);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewItemAnimHelper.BaseAnimatorUpdateListener
        public ValueAnimator.AnimatorUpdateListener getUpdateListener() {
            return this.mAnimConfig.mUpAnimatorUpdateListener;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class BaseAnimatorListenerAdapter extends AnimatorListenerAdapter {
        public WeakReference<EditableListViewItemAnimHelper> mWeakHelper;
        public WeakReference<View> mWeakTouchView;

        public abstract AnimatorListenerAdapter getAnimatorListenerAdapter();

        public BaseAnimatorListenerAdapter(View view, EditableListViewItemAnimHelper editableListViewItemAnimHelper) {
            this.mWeakTouchView = new WeakReference<>(view);
            this.mWeakHelper = new WeakReference<>(editableListViewItemAnimHelper);
        }

        public boolean isValid() {
            WeakReference<View> weakReference;
            WeakReference<EditableListViewItemAnimHelper> weakReference2 = this.mWeakHelper;
            return (weakReference2 == null || weakReference2.get() == null || this.mWeakHelper.get().mConfigParams == null || (weakReference = this.mWeakTouchView) == null || weakReference.get() == null) ? false : true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            if (!isValid() || getAnimatorListenerAdapter() == null) {
                return;
            }
            getAnimatorListenerAdapter().onAnimationStart(animator);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener
        public void onAnimationResume(Animator animator) {
            super.onAnimationResume(animator);
            if (!isValid() || getAnimatorListenerAdapter() == null) {
                return;
            }
            getAnimatorListenerAdapter().onAnimationResume(animator);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener
        public void onAnimationPause(Animator animator) {
            super.onAnimationPause(animator);
            if (!isValid() || getAnimatorListenerAdapter() == null) {
                return;
            }
            getAnimatorListenerAdapter().onAnimationPause(animator);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            if (!isValid() || getAnimatorListenerAdapter() == null) {
                return;
            }
            getAnimatorListenerAdapter().onAnimationEnd(animator);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            super.onAnimationCancel(animator);
            if (!isValid() || getAnimatorListenerAdapter() == null) {
                return;
            }
            getAnimatorListenerAdapter().onAnimationCancel(animator);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            super.onAnimationRepeat(animator);
            if (!isValid() || getAnimatorListenerAdapter() == null) {
                return;
            }
            getAnimatorListenerAdapter().onAnimationRepeat(animator);
        }

        public void release() {
            if (!isValid()) {
                return;
            }
            EditableListViewItemAnimHelper editableListViewItemAnimHelper = this.mWeakHelper.get();
            View view = this.mWeakTouchView.get();
            if (editableListViewItemAnimHelper.mEnlargeAnimator != null && editableListViewItemAnimHelper.mEnlargeAnimator.isRunning()) {
                editableListViewItemAnimHelper.mEnlargeAnimator.cancel();
            }
            if (editableListViewItemAnimHelper.mEnlargeRunnable != null) {
                view.removeCallbacks(editableListViewItemAnimHelper.mEnlargeRunnable);
                editableListViewItemAnimHelper.mEnlargeRunnable = null;
            }
            if (editableListViewItemAnimHelper.mHideRunnable != null) {
                view.removeCallbacks(editableListViewItemAnimHelper.mHideRunnable);
                editableListViewItemAnimHelper.mHideRunnable = null;
            }
            if (editableListViewItemAnimHelper.mShowRunnable == null) {
                return;
            }
            view.removeCallbacks(editableListViewItemAnimHelper.mShowRunnable);
            editableListViewItemAnimHelper.mShowRunnable = null;
        }
    }

    /* loaded from: classes2.dex */
    public static class DownAnimatorListenerAdapter extends BaseAnimatorListenerAdapter {
        public DownAnimatorListenerAdapter(View view, EditableListViewItemAnimHelper editableListViewItemAnimHelper) {
            super(view, editableListViewItemAnimHelper);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewItemAnimHelper.BaseAnimatorListenerAdapter, android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            release();
            super.onAnimationStart(animator);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewItemAnimHelper.BaseAnimatorListenerAdapter, android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (!isValid()) {
                return;
            }
            EditableListViewItemAnimHelper editableListViewItemAnimHelper = this.mWeakHelper.get();
            if (editableListViewItemAnimHelper.mConfigParams.isWithScale) {
                this.mWeakTouchView.get().postDelayed(editableListViewItemAnimHelper.createEnlargeTask(this.mWeakTouchView.get()), 50L);
            }
            super.onAnimationEnd(animator);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewItemAnimHelper.BaseAnimatorListenerAdapter
        public AnimatorListenerAdapter getAnimatorListenerAdapter() {
            return this.mWeakHelper.get().mConfigParams.mDownAnimatorListenerAdapter;
        }
    }

    /* loaded from: classes2.dex */
    public static class UpAnimatorListenerAdapter extends BaseAnimatorListenerAdapter {
        public UpAnimatorListenerAdapter(View view, EditableListViewItemAnimHelper editableListViewItemAnimHelper) {
            super(view, editableListViewItemAnimHelper);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewItemAnimHelper.BaseAnimatorListenerAdapter, android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (!isValid()) {
                return;
            }
            EditableListViewItemAnimHelper editableListViewItemAnimHelper = this.mWeakHelper.get();
            View view = this.mWeakTouchView.get();
            release();
            if (editableListViewItemAnimHelper.mConfigParams.isWithAlpha) {
                view.post(editableListViewItemAnimHelper.createShowTask(view));
            }
            super.onAnimationStart(animator);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewItemAnimHelper.BaseAnimatorListenerAdapter
        public AnimatorListenerAdapter getAnimatorListenerAdapter() {
            return this.mWeakHelper.get().mConfigParams.mUpAnimatorListenerAdapter;
        }
    }

    public final Runnable createHideTask(View view) {
        HideRunnable hideRunnable = new HideRunnable(view);
        this.mHideRunnable = hideRunnable;
        return hideRunnable;
    }

    public final Runnable createShowTask(View view) {
        ShowRunnable showRunnable = new ShowRunnable(view);
        this.mShowRunnable = showRunnable;
        return showRunnable;
    }

    public final Runnable createEnlargeTask(View view) {
        EnlargeRunnable enlargeRunnable = new EnlargeRunnable(view);
        this.mEnlargeRunnable = enlargeRunnable;
        return enlargeRunnable;
    }

    /* loaded from: classes2.dex */
    public static abstract class CancelRunnable implements Runnable {
        public boolean isCanceled;
        public WeakReference<View> mWeakTouchView;

        public CancelRunnable(View view) {
            this.mWeakTouchView = new WeakReference<>(view);
        }

        public void cancel() {
            this.isCanceled = true;
        }
    }

    /* loaded from: classes2.dex */
    public static class ShowRunnable extends CancelRunnable {
        public ShowRunnable(View view) {
            super(view);
        }

        @Override // java.lang.Runnable
        public void run() {
            WeakReference<View> weakReference = this.mWeakTouchView;
            if (weakReference == null || weakReference.get() == null || this.isCanceled) {
                return;
            }
            View view = this.mWeakTouchView.get();
            View findViewById = view.findViewById(R.id.album_common_title);
            View findViewById2 = view.findViewById(R.id.album_common_sub_title);
            if (findViewById == null || findViewById2 == null) {
                return;
            }
            AnimConfig animConfig = new AnimConfig();
            animConfig.setEase(EaseManager.getStyle(2, 250.0f));
            AnimState add = new AnimState("show").add(ViewProperty.ALPHA, 1.0d);
            Folme.useAt(findViewById).state().to(add, animConfig);
            Folme.useAt(findViewById2).state().to(add, animConfig);
        }
    }

    /* loaded from: classes2.dex */
    public static class HideRunnable extends CancelRunnable {
        public HideRunnable(View view) {
            super(view);
        }

        @Override // java.lang.Runnable
        public void run() {
            WeakReference<View> weakReference = this.mWeakTouchView;
            if (weakReference == null || weakReference.get() == null || this.isCanceled) {
                return;
            }
            View view = this.mWeakTouchView.get();
            View findViewById = view.findViewById(R.id.album_common_title);
            View findViewById2 = view.findViewById(R.id.album_common_sub_title);
            if (findViewById == null || findViewById2 == null) {
                return;
            }
            AnimConfig animConfig = new AnimConfig();
            animConfig.setEase(EaseManager.getStyle(20, 150.0f));
            AnimState add = new AnimState("hide").add(ViewProperty.ALPHA, SearchStatUtils.POW);
            Folme.useAt(findViewById).state().to(add, animConfig);
            Folme.useAt(findViewById2).state().to(add, animConfig);
        }
    }

    /* loaded from: classes2.dex */
    public class EnlargeRunnable extends CancelRunnable {
        public EnlargeRunnable(View view) {
            super(view);
        }

        @Override // java.lang.Runnable
        public void run() {
            WeakReference<View> weakReference = this.mWeakTouchView;
            if (weakReference == null || weakReference.get() == null || this.isCanceled) {
                return;
            }
            final View view = this.mWeakTouchView.get();
            if (EditableListViewItemAnimHelper.this.mEnlargeAnimator != null && EditableListViewItemAnimHelper.this.mEnlargeAnimator.isRunning()) {
                EditableListViewItemAnimHelper.this.mEnlargeAnimator.cancel();
            }
            EditableListViewItemAnimHelper.this.mEnlargeAnimator = ValueAnimator.ofFloat(view.getScaleX(), 1.1f);
            EditableListViewItemAnimHelper.this.mEnlargeAnimator.setDuration(150L);
            EditableListViewItemAnimHelper.this.mEnlargeAnimator.setInterpolator(new DecelerateInterpolator());
            EditableListViewItemAnimHelper.this.mEnlargeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewItemAnimHelper.EnlargeRunnable.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    view.setScaleX(((Float) valueAnimator.getAnimatedValue()).floatValue());
                    view.setScaleY(((Float) valueAnimator.getAnimatedValue()).floatValue());
                }
            });
            EditableListViewItemAnimHelper.this.mEnlargeAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewItemAnimHelper.EnlargeRunnable.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    EditableListViewItemAnimHelper.this.mEnlargeRunnable = null;
                }
            });
            EditableListViewItemAnimHelper.this.mEnlargeAnimator.start();
            if (!EditableListViewItemAnimHelper.this.mConfigParams.isWithAlpha) {
                return;
            }
            view.post(EditableListViewItemAnimHelper.this.createHideTask(view));
        }
    }

    public void onTouchItemView(View view, int i) {
        if (view == null || this.mConfigParams == null || !this.isHandleTouch) {
            return;
        }
        if (view != this.mTouchView) {
            reductionTouchView();
        }
        this.mTouchView = view;
        boolean z = i == 0;
        float scaleX = view.getScaleX();
        ValueAnimator valueAnimator = this.mDownAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mDownAnimator.cancel();
        }
        ValueAnimator valueAnimator2 = this.mUpAnimator;
        if (valueAnimator2 != null && valueAnimator2.isRunning()) {
            this.mUpAnimator.cancel();
        }
        if (z) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(scaleX, this.mConfigParams.mDownTo);
            this.mDownAnimator = ofFloat;
            ofFloat.setDuration(this.mConfigParams.mDownDuration);
            this.mDownAnimator.setInterpolator(this.mConfigParams.mDownTimeInterpolator);
            this.mDownAnimator.addUpdateListener(new DownAnimatorUpdateListener(view, this.mConfigParams));
            this.mDownAnimator.addListener(new DownAnimatorListenerAdapter(view, this));
            this.mDownAnimator.start();
        } else if (this.isOnlyUpAlphaAnimEnable) {
            view.post(createShowTask(view));
        } else {
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(scaleX, this.mConfigParams.mUpTo);
            this.mUpAnimator = ofFloat2;
            ofFloat2.setDuration(this.mConfigParams.mUpDuration);
            this.mUpAnimator.setInterpolator(this.mConfigParams.mUpTimeInterpolator);
            this.mUpAnimator.addUpdateListener(new UpAnimatorUpdateListener(view, this.mConfigParams));
            this.mUpAnimator.addListener(new UpAnimatorListenerAdapter(view, this));
            this.mUpAnimator.start();
        }
    }

    public void fastUpItemView(View view, AnimatorListenerAdapter animatorListenerAdapter) {
        ConfigParams configParams;
        if (view == null || (configParams = this.mConfigParams) == null) {
            return;
        }
        if (view != this.mTouchView || configParams.isWithAlpha) {
            reductionTouchView();
        }
        this.mTouchView = view;
        float scaleX = view.getScaleX();
        float f = this.mConfigParams.mUpTo;
        ValueAnimator valueAnimator = this.mDownAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mDownAnimator.cancel();
        }
        ValueAnimator valueAnimator2 = this.mUpAnimator;
        if (valueAnimator2 != null && valueAnimator2.isRunning()) {
            this.mUpAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(scaleX, f);
        this.mUpAnimator = ofFloat;
        ofFloat.setDuration(150L);
        this.mUpAnimator.setInterpolator(this.mConfigParams.mUpTimeInterpolator);
        this.mUpAnimator.addUpdateListener(new UpAnimatorUpdateListener(view, this.mConfigParams));
        if (animatorListenerAdapter != null) {
            this.mUpAnimator.addListener(animatorListenerAdapter);
        }
        this.mUpAnimator.start();
    }

    public void reductionTouchView() {
        release();
        if (this.mTouchView == null) {
            return;
        }
        AnimState animState = new AnimState("current");
        ViewProperty viewProperty = ViewProperty.SCALE_X;
        AnimState add = animState.add(viewProperty, this.mTouchView.getScaleX());
        ViewProperty viewProperty2 = ViewProperty.SCALE_Y;
        Folme.useAt(this.mTouchView).state().fromTo(add.add(viewProperty2, this.mTouchView.getScaleY()), new AnimState("reduction").add(viewProperty, 1.0d).add(viewProperty2, 1.0d), new AnimConfig[0]);
        View findViewById = this.mTouchView.findViewById(R.id.album_common_title);
        View findViewById2 = this.mTouchView.findViewById(R.id.album_common_sub_title);
        if (findViewById == null || findViewById2 == null) {
            return;
        }
        AnimState add2 = new AnimState("show").add(ViewProperty.ALPHA, 1.0d);
        Folme.useAt(findViewById).state().setTo(add2);
        Folme.useAt(findViewById2).state().setTo(add2);
    }
}
