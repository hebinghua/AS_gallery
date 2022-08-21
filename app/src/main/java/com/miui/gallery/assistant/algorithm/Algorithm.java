package com.miui.gallery.assistant.algorithm;

import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;

/* loaded from: classes.dex */
public abstract class Algorithm {
    public static final int[] FLAG_FEATURE_ALL_ARRAY = {1, 4};
    public final long mAlgorithmId;
    public final String TAG = getClass().getSimpleName();
    public volatile boolean mIsNativeInitiated = false;

    public abstract void clearAlgorithm();

    public boolean isForegroundUsed() {
        return false;
    }

    public abstract void onDestroyAlgorithm();

    public abstract boolean onInitAlgorithm();

    public Algorithm(long j) {
        this.mAlgorithmId = j;
    }

    public long getAlgorithmId() {
        return this.mAlgorithmId;
    }

    public synchronized boolean init() {
        if (!this.mIsNativeInitiated) {
            try {
                if (onInitAlgorithm()) {
                    this.mIsNativeInitiated = true;
                }
            } catch (Error e) {
                reportAlgorithmError(e);
            }
        }
        return this.mIsNativeInitiated;
    }

    public final synchronized void destroy() {
        if (this.mIsNativeInitiated) {
            try {
                onDestroyAlgorithm();
            } catch (Error e) {
                reportAlgorithmError(e);
            }
            this.mIsNativeInitiated = false;
        }
    }

    public void finalize() throws Throwable {
        super.finalize();
        destroy();
    }

    public void reportAlgorithmError(Throwable th) {
        DefaultLogger.e(this.TAG, th);
        HashMap hashMap = new HashMap();
        hashMap.put("algorithm", getClass().getSimpleName());
        hashMap.put("error", th != null ? th.toString() : "");
        SamplingStatHelper.recordCountEvent("assistant", "assistant_algorithm_error", hashMap);
    }
}
