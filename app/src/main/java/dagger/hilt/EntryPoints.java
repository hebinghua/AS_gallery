package dagger.hilt;

import dagger.hilt.internal.GeneratedComponent;
import dagger.hilt.internal.GeneratedComponentManager;

/* loaded from: classes3.dex */
public final class EntryPoints {
    public static <T> T get(Object component, Class<T> entryPoint) {
        if (component instanceof GeneratedComponent) {
            return entryPoint.cast(component);
        }
        if (component instanceof GeneratedComponentManager) {
            return (T) get(((GeneratedComponentManager) component).mo2560generatedComponent(), entryPoint);
        }
        throw new IllegalStateException(String.format("Given component holder %s does not implement %s or %s", component.getClass(), GeneratedComponent.class, GeneratedComponentManager.class));
    }
}
