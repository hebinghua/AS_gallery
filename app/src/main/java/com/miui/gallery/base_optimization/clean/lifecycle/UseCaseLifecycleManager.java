package com.miui.gallery.base_optimization.clean.lifecycle;

import android.app.Activity;
import android.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import com.miui.gallery.base_optimization.fragment.utils.FragmentUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class UseCaseLifecycleManager {
    public Map<Lifecycle, LifecycleObserver> mLifecycles;
    public onUserDefindLifecycleCallback mUserDefindLifecycleCallback;

    /* loaded from: classes.dex */
    public interface onLifecycleEventCallback {
        void onEvent(Lifecycle.Event event);
    }

    /* loaded from: classes.dex */
    public interface onUserDefindLifecycleCallback {
        void onCall(Object obj, onLifecycleEventCallback... onlifecycleeventcallbackArr);
    }

    public static UseCaseLifecycleManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes.dex */
    public static class SingletonHolder {
        public static final UseCaseLifecycleManager INSTANCE = new UseCaseLifecycleManager();
    }

    public UseCaseLifecycleManager() {
    }

    public void registerLifecycle(Activity activity, onLifecycleEventCallback... onlifecycleeventcallbackArr) {
        if (activity == null || onlifecycleeventcallbackArr == null) {
            return;
        }
        Fragment findFragmentByTag = activity.getFragmentManager().findFragmentByTag("listener_activity_lifecycle");
        if (findFragmentByTag != null) {
            ((UseCaseLifecycleFragment) findFragmentByTag).add(onlifecycleeventcallbackArr);
            return;
        }
        UseCaseLifecycleFragment newInstance = UseCaseLifecycleFragment.newInstance();
        newInstance.add(onlifecycleeventcallbackArr);
        FragmentUtils.addFragmentToActivityIfNeed(activity, newInstance, "listener_activity_lifecycle");
    }

    public void registerLifecycle(FragmentActivity fragmentActivity, onLifecycleEventCallback... onlifecycleeventcallbackArr) {
        if (fragmentActivity == null || onlifecycleeventcallbackArr == null) {
            return;
        }
        androidx.fragment.app.Fragment findFragmentByTag = fragmentActivity.getSupportFragmentManager().findFragmentByTag("listener_activity_lifecycle");
        if (findFragmentByTag != null) {
            ((com.miui.gallery.base_optimization.clean.lifecycle.androidx.UseCaseLifecycleFragment) findFragmentByTag).add(onlifecycleeventcallbackArr);
            return;
        }
        com.miui.gallery.base_optimization.clean.lifecycle.androidx.UseCaseLifecycleFragment newInstance = com.miui.gallery.base_optimization.clean.lifecycle.androidx.UseCaseLifecycleFragment.newInstance();
        newInstance.add(onlifecycleeventcallbackArr);
        FragmentUtils.addFragmentToActivityIfNeed(fragmentActivity, newInstance, "listener_activity_lifecycle");
    }

    public void registerLifecycle(androidx.fragment.app.Fragment fragment, onLifecycleEventCallback... onlifecycleeventcallbackArr) {
        if (fragment == null || onlifecycleeventcallbackArr == null) {
            return;
        }
        registerLifecycle(fragment.getLifecycle(), onlifecycleeventcallbackArr);
    }

    public void registerLifecycle(Lifecycle lifecycle, onLifecycleEventCallback... onlifecycleeventcallbackArr) {
        if (lifecycle == null || onlifecycleeventcallbackArr == null) {
            return;
        }
        if (this.mLifecycles == null) {
            this.mLifecycles = new HashMap(2);
        }
        LifecycleObserver lifecycleObserver = this.mLifecycles.get(lifecycle);
        if (lifecycleObserver != null) {
            if (!(lifecycleObserver instanceof UseCaseLifecycleObserver)) {
                return;
            }
            ((UseCaseLifecycleObserver) lifecycleObserver).add(onlifecycleeventcallbackArr);
            return;
        }
        UseCaseLifecycleObserver useCaseLifecycleObserver = new UseCaseLifecycleObserver(onlifecycleeventcallbackArr) { // from class: com.miui.gallery.base_optimization.clean.lifecycle.UseCaseLifecycleManager.1
            @Override // com.miui.gallery.base_optimization.clean.lifecycle.UseCaseLifecycleObserver, androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
            public void onDestroy(LifecycleOwner lifecycleOwner) {
                super.onDestroy(lifecycleOwner);
                if (((UseCaseLifecycleObserver) UseCaseLifecycleManager.this.mLifecycles.get(lifecycleOwner.getLifecycle())) != null) {
                    UseCaseLifecycleManager.this.mLifecycles.remove(lifecycleOwner.getLifecycle());
                }
            }
        };
        lifecycle.addObserver(useCaseLifecycleObserver);
        this.mLifecycles.put(lifecycle, useCaseLifecycleObserver);
    }

    public void registerLifecycle(Object obj, onLifecycleEventCallback... onlifecycleeventcallbackArr) {
        if (obj == null || onlifecycleeventcallbackArr == null) {
            return;
        }
        onUserDefindLifecycleCallback onuserdefindlifecyclecallback = this.mUserDefindLifecycleCallback;
        if (onuserdefindlifecyclecallback != null) {
            onuserdefindlifecyclecallback.onCall(obj, onlifecycleeventcallbackArr);
        } else if (obj instanceof Lifecycle) {
            registerLifecycle((Lifecycle) obj, onlifecycleeventcallbackArr);
        } else if (obj instanceof androidx.fragment.app.Fragment) {
            registerLifecycle((androidx.fragment.app.Fragment) obj, onlifecycleeventcallbackArr);
        } else if (obj instanceof FragmentActivity) {
            registerLifecycle((FragmentActivity) obj, onlifecycleeventcallbackArr);
        } else if (obj instanceof Activity) {
            registerLifecycle((Activity) obj, onlifecycleeventcallbackArr);
        } else {
            throw new IllegalArgumentException("owner can only be Lifecycle/androidx.fragment.app.Fragment/android.app.Activity");
        }
    }

    public final void sendEvent(Lifecycle.Event event, Collection<onLifecycleEventCallback> collection) {
        if (collection == null || collection.isEmpty()) {
            return;
        }
        for (onLifecycleEventCallback onlifecycleeventcallback : collection) {
            onlifecycleeventcallback.onEvent(event);
        }
    }
}
