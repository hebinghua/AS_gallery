package miuix.animation.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/* loaded from: classes3.dex */
public class ObjectPool {
    public static final Handler sMainHandler = new Handler(Looper.getMainLooper());
    public static final ConcurrentHashMap<Class<?>, Cache> sCacheMap = new ConcurrentHashMap<>();

    /* loaded from: classes3.dex */
    public interface IPoolObject {
        void clear();
    }

    /* loaded from: classes3.dex */
    public static class Cache {
        public final ConcurrentHashMap<Object, Boolean> mCacheRecord;
        public final ConcurrentLinkedQueue<Object> pool;
        public final Runnable shrinkTask;

        public Cache() {
            this.pool = new ConcurrentLinkedQueue<>();
            this.mCacheRecord = new ConcurrentHashMap<>();
            this.shrinkTask = new Runnable() { // from class: miuix.animation.utils.ObjectPool.Cache.1
                @Override // java.lang.Runnable
                public void run() {
                    Cache.this.shrink();
                }
            };
        }

        public <T> T acquireObject(Class<T> cls, Object... objArr) {
            T t = (T) this.pool.poll();
            if (t == null) {
                return cls != null ? (T) ObjectPool.createObject(cls, objArr) : t;
            }
            this.mCacheRecord.remove(t);
            return t;
        }

        public void releaseObject(Object obj) {
            if (this.mCacheRecord.putIfAbsent(obj, Boolean.TRUE) != null) {
                return;
            }
            this.pool.add(obj);
            ObjectPool.sMainHandler.removeCallbacks(this.shrinkTask);
            if (this.pool.size() <= 10) {
                return;
            }
            ObjectPool.sMainHandler.postDelayed(this.shrinkTask, 5000L);
        }

        public void shrink() {
            Object poll;
            while (this.pool.size() > 10 && (poll = this.pool.poll()) != null) {
                this.mCacheRecord.remove(poll);
            }
        }
    }

    public static <T> T acquire(Class<T> cls, Object... objArr) {
        return (T) getObjectCache(cls, true).acquireObject(cls, objArr);
    }

    public static void release(Object obj) {
        if (obj == null) {
            return;
        }
        Class<?> cls = obj.getClass();
        if (obj instanceof IPoolObject) {
            ((IPoolObject) obj).clear();
        } else if (obj instanceof Collection) {
            ((Collection) obj).clear();
        } else if (obj instanceof Map) {
            ((Map) obj).clear();
        }
        Cache objectCache = getObjectCache(cls, false);
        if (objectCache == null) {
            return;
        }
        objectCache.releaseObject(obj);
    }

    public static Cache getObjectCache(Class<?> cls, boolean z) {
        ConcurrentHashMap<Class<?>, Cache> concurrentHashMap = sCacheMap;
        Cache cache = concurrentHashMap.get(cls);
        if (cache != null || !z) {
            return cache;
        }
        Cache cache2 = new Cache();
        Cache putIfAbsent = concurrentHashMap.putIfAbsent(cls, cache2);
        return putIfAbsent != null ? putIfAbsent : cache2;
    }

    public static Object createObject(Class<?> cls, Object... objArr) {
        Constructor<?>[] declaredConstructors;
        try {
            for (Constructor<?> constructor : cls.getDeclaredConstructors()) {
                if (constructor.getParameterTypes().length == objArr.length) {
                    constructor.setAccessible(true);
                    return constructor.newInstance(objArr);
                }
            }
            return null;
        } catch (Exception e) {
            Log.w("miuix_anim", "ObjectPool.createObject failed, clz = " + cls, e);
            return null;
        }
    }
}
