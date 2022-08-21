package miuix.core.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import miuix.core.util.concurrent.ConcurrentRingQueue;

/* loaded from: classes3.dex */
public final class Pools {
    public static final HashMap<Class<?>, InstanceHolder<?>> mInstanceHolderMap = new HashMap<>();
    public static final HashMap<Class<?>, SoftReferenceInstanceHolder<?>> mSoftReferenceInstanceHolderMap = new HashMap<>();
    public static final Pool<StringBuilder> mStringBuilderPool = createSoftReferencePool(new Manager<StringBuilder>() { // from class: miuix.core.util.Pools.1
        @Override // miuix.core.util.Pools.Manager
        /* renamed from: createInstance  reason: collision with other method in class */
        public StringBuilder mo2624createInstance() {
            return new StringBuilder();
        }

        @Override // miuix.core.util.Pools.Manager
        public void onRelease(StringBuilder sb) {
            sb.setLength(0);
        }
    }, 4);

    /* loaded from: classes3.dex */
    public interface IInstanceHolder<T> {
        T get();

        boolean put(T t);
    }

    /* loaded from: classes3.dex */
    public static abstract class Manager<T> {
        /* renamed from: createInstance */
        public abstract T mo2624createInstance();

        public void onAcquire(T t) {
        }

        public void onDestroy(T t) {
        }

        public void onRelease(T t) {
        }
    }

    /* loaded from: classes3.dex */
    public interface Pool<T> {
        T acquire();

        void release(T t);
    }

    /* loaded from: classes3.dex */
    public static class InstanceHolder<T> implements IInstanceHolder<T> {
        public final Class<T> mClazz;
        public final ConcurrentRingQueue<T> mQueue;

        public InstanceHolder(Class<T> cls, int i) {
            this.mClazz = cls;
            this.mQueue = new ConcurrentRingQueue<>(i, false, true);
        }

        public Class<T> getElementClass() {
            return this.mClazz;
        }

        public synchronized void resize(int i) {
            int capacity = i + this.mQueue.getCapacity();
            if (capacity <= 0) {
                synchronized (Pools.mInstanceHolderMap) {
                    Pools.mInstanceHolderMap.remove(getElementClass());
                }
                return;
            }
            if (capacity > 0) {
                this.mQueue.increaseCapacity(capacity);
            } else {
                this.mQueue.decreaseCapacity(-capacity);
            }
        }

        @Override // miuix.core.util.Pools.IInstanceHolder
        public T get() {
            return this.mQueue.get();
        }

        @Override // miuix.core.util.Pools.IInstanceHolder
        public boolean put(T t) {
            return this.mQueue.put(t);
        }
    }

    /* loaded from: classes3.dex */
    public static class SoftReferenceInstanceHolder<T> implements IInstanceHolder<T> {
        public final Class<T> mClazz;
        public volatile SoftReference<T>[] mElements;
        public volatile int mIndex = 0;
        public volatile int mSize;

        public SoftReferenceInstanceHolder(Class<T> cls, int i) {
            this.mClazz = cls;
            this.mSize = i;
            this.mElements = new SoftReference[i];
        }

        public Class<T> getElementClass() {
            return this.mClazz;
        }

        public synchronized void resize(int i) {
            int i2 = i + this.mSize;
            if (i2 <= 0) {
                synchronized (Pools.mSoftReferenceInstanceHolderMap) {
                    Pools.mSoftReferenceInstanceHolderMap.remove(getElementClass());
                }
                return;
            }
            this.mSize = i2;
            SoftReference<T>[] softReferenceArr = this.mElements;
            int i3 = this.mIndex;
            if (i2 > softReferenceArr.length) {
                SoftReference<T>[] softReferenceArr2 = new SoftReference[i2];
                System.arraycopy(softReferenceArr, 0, softReferenceArr2, 0, i3);
                this.mElements = softReferenceArr2;
            }
        }

        @Override // miuix.core.util.Pools.IInstanceHolder
        public synchronized T get() {
            int i = this.mIndex;
            SoftReference<T>[] softReferenceArr = this.mElements;
            while (i != 0) {
                i--;
                if (softReferenceArr[i] != null) {
                    T t = softReferenceArr[i].get();
                    softReferenceArr[i] = null;
                    if (t != null) {
                        this.mIndex = i;
                        return t;
                    }
                }
            }
            return null;
        }

        @Override // miuix.core.util.Pools.IInstanceHolder
        public synchronized boolean put(T t) {
            int i = this.mIndex;
            SoftReference<T>[] softReferenceArr = this.mElements;
            if (i < this.mSize) {
                softReferenceArr[i] = new SoftReference<>(t);
                this.mIndex = i + 1;
                return true;
            }
            for (int i2 = 0; i2 < i; i2++) {
                if (softReferenceArr[i2] != null && softReferenceArr[i2].get() != null) {
                }
                softReferenceArr[i2] = new SoftReference<>(t);
                return true;
            }
            return false;
        }
    }

    public static Pool<StringBuilder> getStringBuilderPool() {
        return mStringBuilderPool;
    }

    public static <T> InstanceHolder<T> onPoolCreate(Class<T> cls, int i) {
        InstanceHolder<?> instanceHolder;
        HashMap<Class<?>, InstanceHolder<?>> hashMap = mInstanceHolderMap;
        synchronized (hashMap) {
            instanceHolder = hashMap.get(cls);
            if (instanceHolder == null) {
                instanceHolder = new InstanceHolder<>(cls, i);
                hashMap.put(cls, instanceHolder);
            } else {
                instanceHolder.resize(i);
            }
        }
        return instanceHolder;
    }

    public static <T> void onPoolClose(InstanceHolder<T> instanceHolder, int i) {
        synchronized (mInstanceHolderMap) {
            instanceHolder.resize(-i);
        }
    }

    public static <T> SoftReferenceInstanceHolder<T> onSoftReferencePoolCreate(Class<T> cls, int i) {
        SoftReferenceInstanceHolder<?> softReferenceInstanceHolder;
        HashMap<Class<?>, SoftReferenceInstanceHolder<?>> hashMap = mSoftReferenceInstanceHolderMap;
        synchronized (hashMap) {
            softReferenceInstanceHolder = hashMap.get(cls);
            if (softReferenceInstanceHolder == null) {
                softReferenceInstanceHolder = new SoftReferenceInstanceHolder<>(cls, i);
                hashMap.put(cls, softReferenceInstanceHolder);
            } else {
                softReferenceInstanceHolder.resize(i);
            }
        }
        return softReferenceInstanceHolder;
    }

    public static <T> void onSoftReferencePoolClose(SoftReferenceInstanceHolder<T> softReferenceInstanceHolder, int i) {
        synchronized (mSoftReferenceInstanceHolderMap) {
            softReferenceInstanceHolder.resize(-i);
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class BasePool<T> implements Pool<T> {
        public final Object mFinalizeGuardian;
        public IInstanceHolder<T> mInstanceHolder;
        public final Manager<T> mManager;
        public final int mSize;

        public abstract IInstanceHolder<T> createInstanceHolder(Class<T> cls, int i);

        public abstract void destroyInstanceHolder(IInstanceHolder<T> iInstanceHolder, int i);

        public BasePool(Manager<T> manager, int i) {
            Object obj = new Object() { // from class: miuix.core.util.Pools.BasePool.1
                public void finalize() throws Throwable {
                    try {
                        BasePool.this.close();
                    } finally {
                        super.finalize();
                    }
                }
            };
            this.mFinalizeGuardian = obj;
            if (manager == null || i < 1) {
                this.mSize = obj.hashCode();
                throw new IllegalArgumentException("manager cannot be null and size cannot less then 1");
            }
            this.mManager = manager;
            this.mSize = i;
            T mo2624createInstance = manager.mo2624createInstance();
            if (mo2624createInstance == null) {
                throw new IllegalStateException("manager create instance cannot return null");
            }
            this.mInstanceHolder = createInstanceHolder(mo2624createInstance.getClass(), i);
            doRelease(mo2624createInstance);
        }

        public final T doAcquire() {
            IInstanceHolder<T> iInstanceHolder = this.mInstanceHolder;
            if (iInstanceHolder == null) {
                throw new IllegalStateException("Cannot acquire object after close()");
            }
            T t = iInstanceHolder.get();
            if (t == null && (t = this.mManager.mo2624createInstance()) == null) {
                throw new IllegalStateException("manager create instance cannot return null");
            }
            this.mManager.onAcquire(t);
            return t;
        }

        public final void doRelease(T t) {
            if (this.mInstanceHolder != null) {
                if (t == null) {
                    return;
                }
                this.mManager.onRelease(t);
                if (this.mInstanceHolder.put(t)) {
                    return;
                }
                this.mManager.onDestroy(t);
                return;
            }
            throw new IllegalStateException("Cannot release object after close()");
        }

        @Override // miuix.core.util.Pools.Pool
        public T acquire() {
            return doAcquire();
        }

        @Override // miuix.core.util.Pools.Pool
        public void release(T t) {
            doRelease(t);
        }

        public void close() {
            IInstanceHolder<T> iInstanceHolder = this.mInstanceHolder;
            if (iInstanceHolder != null) {
                destroyInstanceHolder(iInstanceHolder, this.mSize);
                this.mInstanceHolder = null;
            }
        }
    }

    public static <T> SimplePool<T> createSimplePool(Manager<T> manager, int i) {
        return new SimplePool<>(manager, i);
    }

    public static <T> SoftReferencePool<T> createSoftReferencePool(Manager<T> manager, int i) {
        return new SoftReferencePool<>(manager, i);
    }

    /* loaded from: classes3.dex */
    public static class SimplePool<T> extends BasePool<T> {
        @Override // miuix.core.util.Pools.BasePool, miuix.core.util.Pools.Pool
        public /* bridge */ /* synthetic */ Object acquire() {
            return super.acquire();
        }

        @Override // miuix.core.util.Pools.BasePool
        public /* bridge */ /* synthetic */ void close() {
            super.close();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // miuix.core.util.Pools.BasePool, miuix.core.util.Pools.Pool
        public /* bridge */ /* synthetic */ void release(Object obj) {
            super.release(obj);
        }

        public SimplePool(Manager<T> manager, int i) {
            super(manager, i);
        }

        @Override // miuix.core.util.Pools.BasePool
        public final IInstanceHolder<T> createInstanceHolder(Class<T> cls, int i) {
            return Pools.onPoolCreate(cls, i);
        }

        @Override // miuix.core.util.Pools.BasePool
        public final void destroyInstanceHolder(IInstanceHolder<T> iInstanceHolder, int i) {
            Pools.onPoolClose((InstanceHolder) iInstanceHolder, i);
        }
    }

    /* loaded from: classes3.dex */
    public static class SoftReferencePool<T> extends BasePool<T> {
        @Override // miuix.core.util.Pools.BasePool, miuix.core.util.Pools.Pool
        public /* bridge */ /* synthetic */ Object acquire() {
            return super.acquire();
        }

        @Override // miuix.core.util.Pools.BasePool
        public /* bridge */ /* synthetic */ void close() {
            super.close();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // miuix.core.util.Pools.BasePool, miuix.core.util.Pools.Pool
        public /* bridge */ /* synthetic */ void release(Object obj) {
            super.release(obj);
        }

        public SoftReferencePool(Manager<T> manager, int i) {
            super(manager, i);
        }

        @Override // miuix.core.util.Pools.BasePool
        public final IInstanceHolder<T> createInstanceHolder(Class<T> cls, int i) {
            return Pools.onSoftReferencePoolCreate(cls, i);
        }

        @Override // miuix.core.util.Pools.BasePool
        public final void destroyInstanceHolder(IInstanceHolder<T> iInstanceHolder, int i) {
            Pools.onSoftReferencePoolClose((SoftReferenceInstanceHolder) iInstanceHolder, i);
        }
    }
}
