package dagger.hilt.android.internal.managers;

import dagger.hilt.internal.GeneratedComponentManager;

/* loaded from: classes3.dex */
public final class ApplicationComponentManager implements GeneratedComponentManager<Object> {
    public volatile Object component;
    public final ComponentSupplier componentCreator;
    public final Object componentLock = new Object();

    public ApplicationComponentManager(ComponentSupplier componentCreator) {
        this.componentCreator = componentCreator;
    }

    @Override // dagger.hilt.internal.GeneratedComponentManager
    /* renamed from: generatedComponent */
    public Object mo2560generatedComponent() {
        if (this.component == null) {
            synchronized (this.componentLock) {
                if (this.component == null) {
                    this.component = this.componentCreator.get();
                }
            }
        }
        return this.component;
    }
}
