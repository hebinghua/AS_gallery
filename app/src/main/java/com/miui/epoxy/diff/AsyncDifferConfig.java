package com.miui.epoxy.diff;

import androidx.recyclerview.widget.DiffUtil;
import com.miui.epoxy.diff.AsyncListDiffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
public final class AsyncDifferConfig<T> {
    public final Executor mBackgroundThreadExecutor;
    public final DiffUtil.ItemCallback<T> mDiffCallback;
    public AsyncListDiffer.ListGenerator<T> mListGenerator;
    public final Executor mMainThreadExecutor;

    public AsyncDifferConfig(Executor executor, Executor executor2, DiffUtil.ItemCallback<T> itemCallback, AsyncListDiffer.ListGenerator<T> listGenerator) {
        this.mMainThreadExecutor = executor;
        this.mBackgroundThreadExecutor = executor2;
        this.mDiffCallback = itemCallback;
        this.mListGenerator = listGenerator;
    }

    public Executor getMainThreadExecutor() {
        return this.mMainThreadExecutor;
    }

    public Executor getBackgroundThreadExecutor() {
        return this.mBackgroundThreadExecutor;
    }

    public DiffUtil.ItemCallback<T> getDiffCallback() {
        return this.mDiffCallback;
    }

    public AsyncListDiffer.ListGenerator<T> getListGenerator() {
        return this.mListGenerator;
    }

    /* loaded from: classes.dex */
    public static final class Builder<T> {
        public static Executor sDiffExecutor;
        public static final Object sExecutorLock = new Object();
        public Executor mBackgroundThreadExecutor;
        public final DiffUtil.ItemCallback<T> mDiffCallback;
        public AsyncListDiffer.ListGenerator<T> mListGenerator;
        public Executor mMainThreadExecutor;

        public Builder(DiffUtil.ItemCallback<T> itemCallback) {
            this.mDiffCallback = itemCallback;
        }

        public Builder<T> setBackgroundThreadExecutor(Executor executor) {
            this.mBackgroundThreadExecutor = executor;
            return this;
        }

        public Builder<T> setListGenerator(AsyncListDiffer.ListGenerator<T> listGenerator) {
            this.mListGenerator = listGenerator;
            return this;
        }

        public AsyncDifferConfig<T> build() {
            if (this.mBackgroundThreadExecutor == null) {
                synchronized (sExecutorLock) {
                    if (sDiffExecutor == null) {
                        sDiffExecutor = Executors.newFixedThreadPool(2);
                    }
                }
                this.mBackgroundThreadExecutor = sDiffExecutor;
            }
            return new AsyncDifferConfig<>(this.mMainThreadExecutor, this.mBackgroundThreadExecutor, this.mDiffCallback, this.mListGenerator);
        }
    }
}
