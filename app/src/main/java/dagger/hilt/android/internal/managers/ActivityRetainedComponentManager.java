package dagger.hilt.android.internal.managers;

import android.content.Context;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import dagger.hilt.EntryPoints;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.internal.ThreadUtil;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.internal.GeneratedComponentManager;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes3.dex */
public final class ActivityRetainedComponentManager implements GeneratedComponentManager<ActivityRetainedComponent> {
    public volatile ActivityRetainedComponent component;
    public final Object componentLock = new Object();
    public final ViewModelProvider viewModelProvider;

    /* loaded from: classes3.dex */
    public interface ActivityRetainedComponentBuilderEntryPoint {
        ActivityRetainedComponentBuilder retainedComponentBuilder();
    }

    /* loaded from: classes3.dex */
    public interface ActivityRetainedLifecycleEntryPoint {
        ActivityRetainedLifecycle getActivityRetainedLifecycle();
    }

    /* loaded from: classes3.dex */
    public static final class ActivityRetainedComponentViewModel extends ViewModel {
        public final ActivityRetainedComponent component;

        public ActivityRetainedComponentViewModel(ActivityRetainedComponent component) {
            this.component = component;
        }

        public ActivityRetainedComponent getComponent() {
            return this.component;
        }

        @Override // androidx.lifecycle.ViewModel
        public void onCleared() {
            super.onCleared();
            ((Lifecycle) ((ActivityRetainedLifecycleEntryPoint) EntryPoints.get(this.component, ActivityRetainedLifecycleEntryPoint.class)).getActivityRetainedLifecycle()).dispatchOnCleared();
        }
    }

    public ActivityRetainedComponentManager(ComponentActivity activity) {
        this.viewModelProvider = getViewModelProvider(activity, activity);
    }

    public final ViewModelProvider getViewModelProvider(ViewModelStoreOwner owner, final Context context) {
        return new ViewModelProvider(owner, new ViewModelProvider.Factory() { // from class: dagger.hilt.android.internal.managers.ActivityRetainedComponentManager.1
            @Override // androidx.lifecycle.ViewModelProvider.Factory
            public <T extends ViewModel> T create(Class<T> aClass) {
                return new ActivityRetainedComponentViewModel(((ActivityRetainedComponentBuilderEntryPoint) EntryPointAccessors.fromApplication(context, ActivityRetainedComponentBuilderEntryPoint.class)).retainedComponentBuilder().mo450build());
            }
        });
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.hilt.internal.GeneratedComponentManager
    /* renamed from: generatedComponent */
    public ActivityRetainedComponent mo2560generatedComponent() {
        if (this.component == null) {
            synchronized (this.componentLock) {
                if (this.component == null) {
                    this.component = createComponent();
                }
            }
        }
        return this.component;
    }

    public final ActivityRetainedComponent createComponent() {
        return ((ActivityRetainedComponentViewModel) this.viewModelProvider.get(ActivityRetainedComponentViewModel.class)).getComponent();
    }

    /* loaded from: classes3.dex */
    public static final class Lifecycle implements ActivityRetainedLifecycle {
        public final Set<ActivityRetainedLifecycle.OnClearedListener> listeners = new HashSet();
        public boolean onClearedDispatched = false;

        public void dispatchOnCleared() {
            ThreadUtil.ensureMainThread();
            this.onClearedDispatched = true;
            for (ActivityRetainedLifecycle.OnClearedListener onClearedListener : this.listeners) {
                onClearedListener.onCleared();
            }
        }
    }
}
