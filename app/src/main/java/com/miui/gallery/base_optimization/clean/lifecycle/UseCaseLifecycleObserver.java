package com.miui.gallery.base_optimization.clean.lifecycle;

import androidx.collection.ArraySet;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.miui.gallery.base_optimization.clean.lifecycle.UseCaseLifecycleManager;
import java.util.Arrays;
import java.util.Set;

/* loaded from: classes.dex */
public class UseCaseLifecycleObserver implements DefaultLifecycleObserver {
    public final Set<UseCaseLifecycleManager.onLifecycleEventCallback> mLifecycleEventCallbacks;

    public UseCaseLifecycleObserver(UseCaseLifecycleManager.onLifecycleEventCallback... onlifecycleeventcallbackArr) {
        ArraySet arraySet = new ArraySet(onlifecycleeventcallbackArr.length);
        this.mLifecycleEventCallbacks = arraySet;
        arraySet.addAll(Arrays.asList(onlifecycleeventcallbackArr));
    }

    public void add(UseCaseLifecycleManager.onLifecycleEventCallback... onlifecycleeventcallbackArr) {
        this.mLifecycleEventCallbacks.addAll(Arrays.asList(onlifecycleeventcallbackArr));
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onStart(LifecycleOwner lifecycleOwner) {
        UseCaseLifecycleManager.getInstance().sendEvent(Lifecycle.Event.ON_START, this.mLifecycleEventCallbacks);
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onStop(LifecycleOwner lifecycleOwner) {
        UseCaseLifecycleManager.getInstance().sendEvent(Lifecycle.Event.ON_STOP, this.mLifecycleEventCallbacks);
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onDestroy(LifecycleOwner lifecycleOwner) {
        UseCaseLifecycleManager.getInstance().sendEvent(Lifecycle.Event.ON_DESTROY, this.mLifecycleEventCallbacks);
        this.mLifecycleEventCallbacks.clear();
    }
}
