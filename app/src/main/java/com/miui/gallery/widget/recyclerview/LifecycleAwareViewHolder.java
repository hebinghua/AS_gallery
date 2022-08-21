package com.miui.gallery.widget.recyclerview;

import android.view.View;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public class LifecycleAwareViewHolder extends BaseViewHolder implements DefaultLifecycleObserver {
    public boolean mIsObserved;
    public final WeakReference<Lifecycle> mLifecycleRef;

    public LifecycleAwareViewHolder(View view, Lifecycle lifecycle) {
        super(view);
        this.mLifecycleRef = new WeakReference<>(lifecycle);
    }

    @Override // androidx.recyclerview.widget.BindAwareViewHolder
    public void onBind() {
        super.onBind();
        observeLifecycle();
    }

    @Override // androidx.recyclerview.widget.BindAwareViewHolder
    public void onUnbind() {
        super.onUnbind();
        unObserveLifecycle();
    }

    public void onRecycle() {
        unObserveLifecycle();
    }

    public final void observeLifecycle() {
        Lifecycle lifecycle;
        if (!this.mIsObserved && (lifecycle = this.mLifecycleRef.get()) != null) {
            lifecycle.addObserver(this);
            this.mIsObserved = true;
        }
    }

    public final void unObserveLifecycle() {
        Lifecycle lifecycle;
        if (this.mIsObserved && (lifecycle = this.mLifecycleRef.get()) != null) {
            lifecycle.removeObserver(this);
            this.mIsObserved = false;
        }
    }
}
