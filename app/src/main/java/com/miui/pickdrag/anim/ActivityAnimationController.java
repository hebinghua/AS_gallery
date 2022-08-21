package com.miui.pickdrag.anim;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import com.miui.pickdrag.base.BasePickerDragActivity;
import com.miui.pickdrag.utils.Device;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.ViewProperty;

/* loaded from: classes3.dex */
public class ActivityAnimationController {
    public static final String TAG = "ActivityAnimationController";
    public static final List<WeakReference<PickerStackAnimListener>> mPickerStackAnimListeners = new ArrayList();
    public boolean isAnimating;
    public boolean isExitAnimStart = false;
    public float lastPageOutAnimValue;
    public WeakReference<BasePickerDragActivity> mActivityRef;
    public final float mContentInitialTop;
    public final PageAnimationListener mPageAnimationListener;
    public WeakReference<View> mTargetRef;

    public ActivityAnimationController(BasePickerDragActivity basePickerDragActivity, View view, PageAnimationListener pageAnimationListener) {
        this.mActivityRef = new WeakReference<>(basePickerDragActivity);
        this.mTargetRef = new WeakReference<>(view);
        this.mContentInitialTop = getPickerContentMarginTop(basePickerDragActivity);
        this.mPageAnimationListener = pageAnimationListener;
        mPickerStackAnimListeners.add(new WeakReference<>(basePickerDragActivity));
    }

    public void onUnBind() {
        WeakReference<BasePickerDragActivity> weakReference = this.mActivityRef;
        if (weakReference != null) {
            weakReference.clear();
            this.mActivityRef = null;
        }
        WeakReference<View> weakReference2 = this.mTargetRef;
        if (weakReference2 != null) {
            weakReference2.clear();
            this.mTargetRef = null;
        }
    }

    public final void startSlideInAnimation(boolean z) {
        View target;
        if (!this.isAnimating && (target = getTarget()) != null) {
            this.isAnimating = true;
            if (z) {
                PickerActivityTransitionAnimations.INSTANCE.startSlideFromBottomAnimation(target, createPageInListener(true));
            } else {
                PickerActivityTransitionAnimations.INSTANCE.startSlideFromRightAnimation(target, createPageInListener(false));
            }
        }
    }

    public final boolean startSlideOutAnimation(boolean z) {
        View target;
        if (!this.isAnimating && (target = getTarget()) != null) {
            this.isAnimating = true;
            if (z) {
                PickerActivityTransitionAnimations.INSTANCE.startSlideToBottomAnimation(target, createPageOutListener(true));
            } else {
                PickerActivityTransitionAnimations.INSTANCE.startSlideToRightAnimation(target, createPageOutListener(false));
            }
            return true;
        }
        return false;
    }

    public final TransitionListener createPageInListener(boolean z) {
        return new PageInListener(z, this);
    }

    /* loaded from: classes3.dex */
    public static class PageInListener extends TransitionListener {
        public boolean isFirstPickerActivity;
        public WeakReference<ActivityAnimationController> mControllerReference;

        public PageInListener(boolean z, ActivityAnimationController activityAnimationController) {
            this.isFirstPickerActivity = z;
            this.mControllerReference = new WeakReference<>(activityAnimationController);
        }

        public final ActivityAnimationController getController() {
            WeakReference<ActivityAnimationController> weakReference = this.mControllerReference;
            if (weakReference == null) {
                return null;
            }
            return weakReference.get();
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onBegin(Object obj) {
            super.onBegin(obj);
            ActivityAnimationController controller = getController();
            if (controller == null) {
                return;
            }
            controller.onPageInAnimationStart(this.isFirstPickerActivity);
            if (!this.isFirstPickerActivity) {
                return;
            }
            controller.updateVerticalAnimationState(1, true);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onUpdate(Object obj, Collection<UpdateInfo> collection) {
            super.onUpdate(obj, collection);
            ActivityAnimationController controller = getController();
            if (controller == null) {
                return;
            }
            controller.notifyActivityPageAnim(collection, true);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            ActivityAnimationController controller = getController();
            if (controller == null) {
                return;
            }
            controller.onPageInAnimationEnd(3, this.isFirstPickerActivity);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onCancel(Object obj) {
            ActivityAnimationController controller = getController();
            if (controller == null) {
                return;
            }
            controller.onPageInAnimationEnd(4, this.isFirstPickerActivity);
        }
    }

    public final TransitionListener createPageOutListener(boolean z) {
        return new PageOutListener(z, this);
    }

    /* loaded from: classes3.dex */
    public static class PageOutListener extends TransitionListener {
        public boolean isFirstPickerActivity;
        public WeakReference<ActivityAnimationController> mControllerReference;

        public PageOutListener(boolean z, ActivityAnimationController activityAnimationController) {
            this.isFirstPickerActivity = z;
            this.mControllerReference = new WeakReference<>(activityAnimationController);
        }

        public final ActivityAnimationController getController() {
            WeakReference<ActivityAnimationController> weakReference = this.mControllerReference;
            if (weakReference == null) {
                return null;
            }
            return weakReference.get();
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            ActivityAnimationController controller = getController();
            if (controller == null) {
                return;
            }
            controller.onPageOutAnimationEnd(3, this.isFirstPickerActivity);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onCancel(Object obj) {
            ActivityAnimationController controller = getController();
            if (controller == null) {
                return;
            }
            controller.onPageOutAnimationEnd(4, this.isFirstPickerActivity);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onBegin(Object obj) {
            ActivityAnimationController controller = getController();
            if (controller == null) {
                return;
            }
            controller.isExitAnimStart = true;
            controller.onPageOutAnimationStart(this.isFirstPickerActivity);
            if (!this.isFirstPickerActivity) {
                return;
            }
            controller.updateVerticalAnimationState(1, false);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onUpdate(Object obj, Collection<UpdateInfo> collection) {
            super.onUpdate(obj, collection);
            ActivityAnimationController controller = getController();
            if (controller == null) {
                return;
            }
            controller.notifyActivityPageAnim(collection, false);
        }
    }

    public final int getValueFromUpdateInfo(Collection<UpdateInfo> collection) {
        UpdateInfo next;
        if (collection == null || collection.isEmpty() || (next = collection.iterator().next()) == null) {
            return 0;
        }
        return next.getIntValue();
    }

    public final float getPageHorizontalAnimProgress(Collection<UpdateInfo> collection) {
        return getValueFromUpdateInfo(collection) / Device.getScreenWidth();
    }

    public final float getPageVerticalAnimProgress(Collection<UpdateInfo> collection, boolean z) {
        float f;
        float valueFromUpdateInfo = getValueFromUpdateInfo(collection);
        int screenHeight = Device.getScreenHeight();
        if (!z) {
            this.lastPageOutAnimValue = valueFromUpdateInfo;
        }
        if (z) {
            f = screenHeight;
            valueFromUpdateInfo = f - valueFromUpdateInfo;
        } else {
            f = screenHeight;
        }
        return Math.min(1.0f, Math.max(0.0f, valueFromUpdateInfo / f));
    }

    public final BasePickerDragActivity getActivity() {
        WeakReference<BasePickerDragActivity> weakReference = this.mActivityRef;
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    public final View getTarget() {
        WeakReference<View> weakReference = this.mTargetRef;
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    public final void onPageInAnimationStart(boolean z) {
        PageAnimationListener pageAnimationListener = this.mPageAnimationListener;
        if (pageAnimationListener != null) {
            pageAnimationListener.onPageInAnimationStart(z);
        }
    }

    public final void horizontalAnimUpdate(Collection<UpdateInfo> collection, boolean z) {
        BasePickerDragActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        int levelInPickerStack = activity.getLevelInPickerStack();
        float pageHorizontalAnimProgress = getPageHorizontalAnimProgress(collection);
        for (WeakReference<PickerStackAnimListener> weakReference : mPickerStackAnimListeners) {
            if (weakReference.get() != null) {
                weakReference.get().onPageHorizontalAnimUpdate(levelInPickerStack, pageHorizontalAnimProgress, z);
            }
        }
    }

    public final void verticalAnimUpdate(Collection<UpdateInfo> collection, boolean z) {
        float pageVerticalAnimProgress = getPageVerticalAnimProgress(collection, z);
        for (WeakReference<PickerStackAnimListener> weakReference : mPickerStackAnimListeners) {
            if (weakReference.get() != null) {
                weakReference.get().onPageVerticalAnimUpdate(2, pageVerticalAnimProgress, z);
            }
        }
    }

    public final void updateVerticalAnimationState(int i, boolean z) {
        int i2 = 1;
        if (i == 1 || i == 4) {
            i2 = 0;
        }
        for (WeakReference<PickerStackAnimListener> weakReference : mPickerStackAnimListeners) {
            if (weakReference.get() != null) {
                weakReference.get().onPageVerticalAnimUpdate(i, i2, z);
            }
        }
    }

    public final void notifyActivityPageAnim(Collection<UpdateInfo> collection, boolean z) {
        if (collection == null || collection.size() <= 0) {
            return;
        }
        String name = collection.iterator().next().property.getName();
        if (ViewProperty.TRANSLATION_X.getName().equals(name)) {
            horizontalAnimUpdate(collection, z);
        } else if (!ViewProperty.TRANSLATION_Y.getName().equals(name)) {
        } else {
            verticalAnimUpdate(collection, z);
        }
    }

    public final void onPageInAnimationEnd(int i, boolean z) {
        this.isAnimating = false;
        PageAnimationListener pageAnimationListener = this.mPageAnimationListener;
        if (pageAnimationListener != null) {
            pageAnimationListener.onPageInAnimationEnd(z);
        }
        if (z) {
            updateVerticalAnimationState(i, true);
        }
    }

    public final void onPageOutAnimationStart(boolean z) {
        PageAnimationListener pageAnimationListener = this.mPageAnimationListener;
        if (pageAnimationListener != null) {
            pageAnimationListener.onPageOutAnimationStart(z);
        }
    }

    public final void onPageOutAnimationEnd(int i, boolean z) {
        this.isAnimating = false;
        PageAnimationListener pageAnimationListener = this.mPageAnimationListener;
        if (pageAnimationListener != null) {
            pageAnimationListener.onPageOutAnimationEnd(z);
        }
        if (z) {
            String str = TAG;
            Log.i(str, "onPageOutAnimEnd # state: " + i + ", lastPageOutAnimValue: " + this.lastPageOutAnimValue);
            updateVerticalAnimationState(i, false);
        }
        BasePickerDragActivity activity = getActivity();
        if (activity != null) {
            activity.finishWithoutAnimation();
        }
    }

    public final float getPickerContentMarginTop(BasePickerDragActivity basePickerDragActivity) {
        return TypedValue.applyDimension(5, 166.0f, basePickerDragActivity.getResources().getDisplayMetrics());
    }

    public void onActivityCreated(boolean z) {
        startSlideInAnimation(z);
    }

    public boolean onActivityFinish(boolean z) {
        return startSlideOutAnimation(z);
    }

    public boolean isExitAnimStart() {
        return this.isExitAnimStart;
    }
}
