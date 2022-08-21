package dagger.hilt.android.internal.managers;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import dagger.hilt.internal.Preconditions;

/* loaded from: classes3.dex */
public final class ViewComponentManager$FragmentContextWrapper extends ContextWrapper {
    public LayoutInflater baseInflater;
    public Fragment fragment;
    public final LifecycleEventObserver fragmentLifecycleObserver;
    public LayoutInflater inflater;

    public ViewComponentManager$FragmentContextWrapper(Context base, Fragment fragment) {
        super((Context) Preconditions.checkNotNull(base));
        LifecycleEventObserver lifecycleEventObserver = new LifecycleEventObserver() { // from class: dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper.1
            @Override // androidx.lifecycle.LifecycleEventObserver
            public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    ViewComponentManager$FragmentContextWrapper.this.fragment = null;
                    ViewComponentManager$FragmentContextWrapper.this.baseInflater = null;
                    ViewComponentManager$FragmentContextWrapper.this.inflater = null;
                }
            }
        };
        this.fragmentLifecycleObserver = lifecycleEventObserver;
        this.baseInflater = null;
        Fragment fragment2 = (Fragment) Preconditions.checkNotNull(fragment);
        this.fragment = fragment2;
        fragment2.getLifecycle().addObserver(lifecycleEventObserver);
    }

    public ViewComponentManager$FragmentContextWrapper(LayoutInflater baseInflater, Fragment fragment) {
        super((Context) Preconditions.checkNotNull(((LayoutInflater) Preconditions.checkNotNull(baseInflater)).getContext()));
        LifecycleEventObserver lifecycleEventObserver = new LifecycleEventObserver() { // from class: dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper.1
            @Override // androidx.lifecycle.LifecycleEventObserver
            public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    ViewComponentManager$FragmentContextWrapper.this.fragment = null;
                    ViewComponentManager$FragmentContextWrapper.this.baseInflater = null;
                    ViewComponentManager$FragmentContextWrapper.this.inflater = null;
                }
            }
        };
        this.fragmentLifecycleObserver = lifecycleEventObserver;
        this.baseInflater = baseInflater;
        Fragment fragment2 = (Fragment) Preconditions.checkNotNull(fragment);
        this.fragment = fragment2;
        fragment2.getLifecycle().addObserver(lifecycleEventObserver);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Object getSystemService(String name) {
        if (!"layout_inflater".equals(name)) {
            return getBaseContext().getSystemService(name);
        }
        if (this.inflater == null) {
            if (this.baseInflater == null) {
                this.baseInflater = (LayoutInflater) getBaseContext().getSystemService("layout_inflater");
            }
            this.inflater = this.baseInflater.cloneInContext(this);
        }
        return this.inflater;
    }
}
