package miuix.animation;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public class ViewTarget extends IAnimTarget<View> {
    public static final ITargetCreator<View> sCreator = new ITargetCreator<View>() { // from class: miuix.animation.ViewTarget.1
        @Override // miuix.animation.ITargetCreator
        public IAnimTarget createTarget(View view) {
            return new ViewTarget(view);
        }
    };
    public WeakReference<Context> mContextRef;
    public LifecycleCallbacks mLifecycleCallbacks;
    public ViewLifecyclerObserver mViewLifecyclerObserver;
    public WeakReference<View> mViewRef;

    public ViewTarget(View view) {
        this.mViewRef = new WeakReference<>(view);
        registerLifecycle(view.getContext());
    }

    public final boolean registerLifecycle(Context context) {
        while (context != null) {
            if (context instanceof LifecycleOwner) {
                this.mContextRef = new WeakReference<>(context);
                if (this.mViewLifecyclerObserver == null) {
                    this.mViewLifecyclerObserver = new ViewLifecyclerObserver();
                }
                ((LifecycleOwner) context).getLifecycle().addObserver(this.mViewLifecyclerObserver);
                return true;
            } else if (context instanceof Activity) {
                if (Build.VERSION.SDK_INT < 29) {
                    return false;
                }
                this.mContextRef = new WeakReference<>(context);
                if (this.mLifecycleCallbacks == null) {
                    this.mLifecycleCallbacks = new LifecycleCallbacks();
                }
                ((Activity) context).registerActivityLifecycleCallbacks(this.mLifecycleCallbacks);
                return true;
            } else {
                context = context instanceof ContextWrapper ? ((ContextWrapper) context).getBaseContext() : null;
            }
        }
        return false;
    }

    public final boolean unRegisterLifecycle(Context context) {
        LifecycleCallbacks lifecycleCallbacks;
        if (context == null) {
            return false;
        }
        if (context instanceof LifecycleOwner) {
            if (this.mViewLifecyclerObserver != null) {
                ((LifecycleOwner) context).getLifecycle().removeObserver(this.mViewLifecyclerObserver);
            }
            this.mViewLifecyclerObserver = null;
            return true;
        } else if (Build.VERSION.SDK_INT < 29 || !(context instanceof Activity) || (lifecycleCallbacks = this.mLifecycleCallbacks) == null) {
            return false;
        } else {
            ((Activity) context).unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
            this.mLifecycleCallbacks = null;
            return true;
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // miuix.animation.IAnimTarget
    /* renamed from: getTargetObject */
    public View mo2588getTargetObject() {
        return this.mViewRef.get();
    }

    @Override // miuix.animation.IAnimTarget
    public void clean() {
        WeakReference<Context> weakReference = this.mContextRef;
        if (weakReference != null) {
            unRegisterLifecycle(weakReference.get());
        }
    }

    @Override // miuix.animation.IAnimTarget
    public boolean isValid() {
        return this.mViewRef.get() != null;
    }

    @Override // miuix.animation.IAnimTarget
    public void executeOnInitialized(final Runnable runnable) {
        final View view = this.mViewRef.get();
        if (view != null) {
            if (view.getVisibility() == 8 && !view.isLaidOut() && (view.getWidth() == 0 || view.getHeight() == 0)) {
                post(new Runnable() { // from class: miuix.animation.ViewTarget.2
                    @Override // java.lang.Runnable
                    public void run() {
                        ViewTarget.this.initLayout(view, runnable);
                    }
                });
            } else {
                post(runnable);
            }
        }
    }

    public final void initLayout(View view, Runnable runnable) {
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            int i = R$id.miuix_animation_tag_init_layout;
            view.setTag(i, Boolean.TRUE);
            ViewGroup viewGroup = (ViewGroup) parent;
            int left = viewGroup.getLeft();
            int top = viewGroup.getTop();
            int visibility = view.getVisibility();
            if (visibility == 8) {
                view.setVisibility(4);
            }
            viewGroup.measure(viewGroup.getWidth(), viewGroup.getHeight());
            viewGroup.layout(left, top, viewGroup.getWidth() + left, viewGroup.getHeight() + top);
            view.setVisibility(visibility);
            runnable.run();
            view.setTag(i, null);
        }
    }

    @Override // miuix.animation.IAnimTarget
    public void post(Runnable runnable) {
        View mo2588getTargetObject = mo2588getTargetObject();
        if (mo2588getTargetObject == null) {
            return;
        }
        if (!this.handler.isInTargetThread() && mo2588getTargetObject.isAttachedToWindow()) {
            mo2588getTargetObject.post(runnable);
        } else {
            executeTask(runnable);
        }
    }

    public final void executeTask(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            Log.w("miuix_anim", "ViewTarget.executeTask failed, " + mo2588getTargetObject(), e);
        }
    }

    /* loaded from: classes3.dex */
    public class ViewLifecyclerObserver implements LifecycleObserver {
        public ViewLifecyclerObserver() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void onDestroy() {
            ViewTarget.this.cleanViewTarget();
        }
    }

    /* loaded from: classes3.dex */
    public class LifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityResumed(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStarted(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity activity) {
        }

        public LifecycleCallbacks() {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityDestroyed(Activity activity) {
            ViewTarget.this.cleanViewTarget();
        }
    }

    public final void cleanViewTarget() {
        WeakReference<Context> weakReference = this.mContextRef;
        if (weakReference != null) {
            unRegisterLifecycle(weakReference.get());
        }
        setCorner(0.0f);
        Folme.clean(this);
    }

    public final void setCorner(float f) {
        View view = this.mViewRef.get();
        if (view != null) {
            view.setTag(R$id.miuix_animation_tag_view_corner, Float.valueOf(f));
        }
    }
}
