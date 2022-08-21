package com.miui.gallery.assistant.algorithm;

import android.util.SparseArray;
import com.miui.gallery.util.assistant.FlagUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class AlgorithmFactroy {
    public static final ConcurrentHashMap<Long, AtomicInteger> mAlgorithmMap;
    public static final SparseArray<SoftReference<Algorithm>> sAlgorithmCache = new SparseArray<>();
    public static final SparseArray<Algorithm> sForegroundUsedAlgorithmCache = new SparseArray<>();
    public static Runnable sRunnable;

    public static long getAlgorithmIdByFlag(int i) {
        if (i == 32) {
            return 3414L;
        }
        if (i == 1) {
            return 1002002L;
        }
        return i == 2 ? 1003L : 1004003L;
    }

    public static int getCacheKey(int i) {
        if (i == 4 || i == 8) {
            return 12;
        }
        return i;
    }

    static {
        ConcurrentHashMap<Long, AtomicInteger> concurrentHashMap = new ConcurrentHashMap<>();
        mAlgorithmMap = concurrentHashMap;
        concurrentHashMap.put(3414L, new AtomicInteger(0));
        concurrentHashMap.put(1002002L, new AtomicInteger(0));
        concurrentHashMap.put(1003L, new AtomicInteger(0));
        concurrentHashMap.put(1004003L, new AtomicInteger(0));
        sRunnable = new Runnable() { // from class: com.miui.gallery.assistant.algorithm.AlgorithmFactroy.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (AlgorithmFactroy.class) {
                    for (int i = 0; i < AlgorithmFactroy.sAlgorithmCache.size(); i++) {
                        SoftReference softReference = (SoftReference) AlgorithmFactroy.sAlgorithmCache.valueAt(i);
                        if (softReference != null && softReference.get() != null && ((AtomicInteger) AlgorithmFactroy.mAlgorithmMap.get(Long.valueOf(((Algorithm) softReference.get()).getAlgorithmId()))).get() <= 0) {
                            DefaultLogger.d("AlgorithmFactroy", "release algorithm flag:%d", Integer.valueOf(AlgorithmFactroy.sAlgorithmCache.keyAt(i)));
                            softReference.clear();
                        }
                    }
                    for (int i2 = 0; i2 < AlgorithmFactroy.sForegroundUsedAlgorithmCache.size(); i2++) {
                        Algorithm algorithm = (Algorithm) AlgorithmFactroy.sForegroundUsedAlgorithmCache.valueAt(i2);
                        if (algorithm != null && ((AtomicInteger) AlgorithmFactroy.mAlgorithmMap.get(Long.valueOf(algorithm.getAlgorithmId()))).get() <= 0) {
                            DefaultLogger.d("AlgorithmFactroy", "release foreground algorithm flag:%d", Integer.valueOf(AlgorithmFactroy.sForegroundUsedAlgorithmCache.keyAt(i2)));
                            algorithm.clearAlgorithm();
                            AlgorithmFactroy.sForegroundUsedAlgorithmCache.remove(i2);
                        }
                    }
                }
            }
        };
    }

    public static synchronized <T extends Algorithm> T getAlgorithmByFlag(int i) {
        T t;
        synchronized (AlgorithmFactroy.class) {
            int ensureFlag = ensureFlag(i);
            t = (T) getAlgorithmFromCache(ensureFlag);
            if (t == null) {
                t = (T) createAlgorithmByFlag(ensureFlag);
                DefaultLogger.d("AlgorithmFactroy", "Create new algorithm %d", Integer.valueOf(ensureFlag));
            } else {
                DefaultLogger.d("AlgorithmFactroy", "Get algorithm %d from Cache", Integer.valueOf(ensureFlag));
            }
            if (t != null) {
                addAlgorithmToCache(ensureFlag, t);
            }
            mAlgorithmMap.get(Long.valueOf(getAlgorithmIdByFlag(ensureFlag))).incrementAndGet();
        }
        return t;
    }

    public static synchronized void releaseAlgorithmByFlag(int i) {
        synchronized (AlgorithmFactroy.class) {
            DefaultLogger.d("AlgorithmFactroy", "releaseAlgorithmByFlag %d", Integer.valueOf(i));
            mAlgorithmMap.get(Long.valueOf(getAlgorithmIdByFlag(i))).decrementAndGet();
            scheduleAlgorithmRelease();
        }
    }

    public static int ensureFlag(int i) {
        if (FlagUtil.hasFlag(i, 32)) {
            return 32;
        }
        if (FlagUtil.hasFlag(i, 1)) {
            return 1;
        }
        if (FlagUtil.hasFlag(i, 2)) {
            return 2;
        }
        return FlagUtil.hasFlag(i, 4) ? 4 : 8;
    }

    public static void scheduleAlgorithmRelease() {
        ThreadManager.getWorkHandler().removeCallbacks(sRunnable);
        ThreadManager.getWorkHandler().postDelayed(sRunnable, 5000L);
    }

    public static Algorithm createAlgorithmByFlag(int i) {
        Algorithm clusterAlgorithm;
        if (FlagUtil.hasFlag(i, 32)) {
            clusterAlgorithm = new AnalyticFaceAndSceneAlgorithm();
        } else if (FlagUtil.hasFlag(i, 1)) {
            clusterAlgorithm = new QualityScoreAlgorithm();
        } else if (FlagUtil.hasFlag(i, 2)) {
            clusterAlgorithm = new SceneFilterAlgorithm();
        } else {
            clusterAlgorithm = (FlagUtil.hasFlag(i, 4) || FlagUtil.hasFlag(i, 8)) ? new ClusterAlgorithm() : null;
        }
        if (clusterAlgorithm != null) {
            clusterAlgorithm.init();
        }
        return clusterAlgorithm;
    }

    public static synchronized void addAlgorithmToCache(int i, Algorithm algorithm) {
        synchronized (AlgorithmFactroy.class) {
            if (algorithm == null) {
                return;
            }
            int cacheKey = getCacheKey(i);
            if (algorithm.isForegroundUsed()) {
                sForegroundUsedAlgorithmCache.put(cacheKey, algorithm);
            } else {
                sAlgorithmCache.put(cacheKey, new SoftReference<>(algorithm));
            }
        }
    }

    public static synchronized Algorithm getAlgorithmFromCache(int i) {
        Algorithm algorithm;
        SparseArray<Algorithm> sparseArray;
        synchronized (AlgorithmFactroy.class) {
            int cacheKey = getCacheKey(i);
            SparseArray<SoftReference<Algorithm>> sparseArray2 = sAlgorithmCache;
            SoftReference<Algorithm> softReference = sparseArray2.get(cacheKey);
            algorithm = null;
            if (softReference != null) {
                algorithm = softReference.get();
                softReference.clear();
                sparseArray2.remove(cacheKey);
            }
            if (algorithm == null && (algorithm = (sparseArray = sForegroundUsedAlgorithmCache).get(cacheKey)) != null) {
                sparseArray.remove(cacheKey);
            }
        }
        return algorithm;
    }
}
