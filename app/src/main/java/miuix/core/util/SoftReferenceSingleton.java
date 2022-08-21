package miuix.core.util;

import java.lang.ref.SoftReference;

/* loaded from: classes3.dex */
public abstract class SoftReferenceSingleton<T> {
    public SoftReference<T> mInstance = null;

    /* renamed from: createInstance */
    public T mo2622createInstance() {
        return null;
    }

    public T createInstance(Object obj) {
        return null;
    }

    public void updateInstance(T t) {
    }

    public void updateInstance(T t, Object obj) {
    }

    public final T get() {
        T mo2622createInstance;
        synchronized (this) {
            SoftReference<T> softReference = this.mInstance;
            if (softReference != null && (mo2622createInstance = softReference.get()) != null) {
                updateInstance(mo2622createInstance);
            }
            mo2622createInstance = mo2622createInstance();
            this.mInstance = new SoftReference<>(mo2622createInstance);
        }
        return mo2622createInstance;
    }

    public final T get(Object obj) {
        T createInstance;
        synchronized (this) {
            SoftReference<T> softReference = this.mInstance;
            if (softReference != null && (createInstance = softReference.get()) != null) {
                updateInstance(createInstance, obj);
            }
            createInstance = createInstance(obj);
            this.mInstance = new SoftReference<>(createInstance);
        }
        return createInstance;
    }
}
