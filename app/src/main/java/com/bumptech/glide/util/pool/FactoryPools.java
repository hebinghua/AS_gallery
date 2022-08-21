package com.bumptech.glide.util.pool;

import android.util.Log;
import androidx.core.util.Pools$Pool;
import androidx.core.util.Pools$SynchronizedPool;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class FactoryPools {
    public static final Resetter<Object> EMPTY_RESETTER = new Resetter<Object>() { // from class: com.bumptech.glide.util.pool.FactoryPools.1
        @Override // com.bumptech.glide.util.pool.FactoryPools.Resetter
        public void reset(Object obj) {
        }
    };

    /* loaded from: classes.dex */
    public interface Factory<T> {
        /* renamed from: create */
        T mo238create();
    }

    /* loaded from: classes.dex */
    public interface Poolable {
        StateVerifier getVerifier();
    }

    /* loaded from: classes.dex */
    public interface Resetter<T> {
        void reset(T t);
    }

    public static <T extends Poolable> Pools$Pool<T> threadSafe(int i, Factory<T> factory) {
        return build(new Pools$SynchronizedPool(i), factory);
    }

    public static <T> Pools$Pool<List<T>> threadSafeList() {
        return threadSafeList(20);
    }

    public static <T> Pools$Pool<List<T>> threadSafeList(int i) {
        return build(new Pools$SynchronizedPool(i), new Factory<List<T>>() { // from class: com.bumptech.glide.util.pool.FactoryPools.2
            @Override // com.bumptech.glide.util.pool.FactoryPools.Factory
            /* renamed from: create  reason: collision with other method in class */
            public List<T> mo238create() {
                return new ArrayList();
            }
        }, new Resetter<List<T>>() { // from class: com.bumptech.glide.util.pool.FactoryPools.3
            @Override // com.bumptech.glide.util.pool.FactoryPools.Resetter
            public void reset(List<T> list) {
                list.clear();
            }
        });
    }

    public static <T extends Poolable> Pools$Pool<T> build(Pools$Pool<T> pools$Pool, Factory<T> factory) {
        return build(pools$Pool, factory, emptyResetter());
    }

    public static <T> Pools$Pool<T> build(Pools$Pool<T> pools$Pool, Factory<T> factory, Resetter<T> resetter) {
        return new FactoryPool(pools$Pool, factory, resetter);
    }

    public static <T> Resetter<T> emptyResetter() {
        return (Resetter<T>) EMPTY_RESETTER;
    }

    /* loaded from: classes.dex */
    public static final class FactoryPool<T> implements Pools$Pool<T> {
        public final Factory<T> factory;
        public final Pools$Pool<T> pool;
        public final Resetter<T> resetter;

        public FactoryPool(Pools$Pool<T> pools$Pool, Factory<T> factory, Resetter<T> resetter) {
            this.pool = pools$Pool;
            this.factory = factory;
            this.resetter = resetter;
        }

        @Override // androidx.core.util.Pools$Pool
        public T acquire() {
            T acquire = this.pool.acquire();
            if (acquire == null) {
                acquire = this.factory.mo238create();
                if (Log.isLoggable("FactoryPools", 2)) {
                    Log.v("FactoryPools", "Created new " + acquire.getClass());
                }
            }
            if (acquire instanceof Poolable) {
                ((Poolable) acquire).getVerifier().setRecycled(false);
            }
            return acquire;
        }

        @Override // androidx.core.util.Pools$Pool
        public boolean release(T t) {
            if (t instanceof Poolable) {
                ((Poolable) t).getVerifier().setRecycled(true);
            }
            this.resetter.reset(t);
            return this.pool.release(t);
        }
    }
}
