package com.miui.gallery.base_optimization.clean.lifecycle;

import android.app.Fragment;
import android.os.Bundle;
import androidx.collection.ArraySet;
import androidx.lifecycle.Lifecycle;
import com.miui.gallery.base_optimization.clean.lifecycle.UseCaseLifecycleManager;
import java.util.Arrays;
import java.util.Set;

@Deprecated
/* loaded from: classes.dex */
public class UseCaseLifecycleFragment extends Fragment {
    public final Set<UseCaseLifecycleManager.onLifecycleEventCallback> mLifecycleEventCallbacks = new ArraySet(2);

    public static UseCaseLifecycleFragment newInstance() {
        Bundle bundle = new Bundle();
        UseCaseLifecycleFragment useCaseLifecycleFragment = new UseCaseLifecycleFragment();
        useCaseLifecycleFragment.setArguments(bundle);
        return useCaseLifecycleFragment;
    }

    public void add(UseCaseLifecycleManager.onLifecycleEventCallback... onlifecycleeventcallbackArr) {
        this.mLifecycleEventCallbacks.addAll(Arrays.asList(onlifecycleeventcallbackArr));
    }

    @Override // android.app.Fragment
    public void onStart() {
        super.onStart();
        UseCaseLifecycleManager.getInstance().sendEvent(Lifecycle.Event.ON_START, this.mLifecycleEventCallbacks);
    }

    @Override // android.app.Fragment
    public void onStop() {
        super.onStop();
        UseCaseLifecycleManager.getInstance().sendEvent(Lifecycle.Event.ON_STOP, this.mLifecycleEventCallbacks);
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        UseCaseLifecycleManager.getInstance().sendEvent(Lifecycle.Event.ON_DESTROY, this.mLifecycleEventCallbacks);
        this.mLifecycleEventCallbacks.clear();
    }
}
