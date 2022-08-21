package com.miui.gallery.widget.recyclerview;

import com.miui.gallery.util.concurrent.GalleryDispatchers;
import com.miui.gallery.widget.recyclerview.DiffUtil;
import java.util.concurrent.ExecutorService;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineDispatcher;

/* compiled from: AsyncDifferConfig.kt */
/* loaded from: classes3.dex */
public final class AsyncDifferConfig<T> {
    public final CoroutineDispatcher backgroundDispatcher;
    public final DiffUtil.ItemCallback<T> diffCallback;
    public final ExecutorService mainThreadExecutor;

    public AsyncDifferConfig(ExecutorService executorService, CoroutineDispatcher backgroundDispatcher, DiffUtil.ItemCallback<T> diffCallback) {
        Intrinsics.checkNotNullParameter(backgroundDispatcher, "backgroundDispatcher");
        Intrinsics.checkNotNullParameter(diffCallback, "diffCallback");
        this.mainThreadExecutor = executorService;
        this.backgroundDispatcher = backgroundDispatcher;
        this.diffCallback = diffCallback;
    }

    public final CoroutineDispatcher getBackgroundDispatcher() {
        return this.backgroundDispatcher;
    }

    public final DiffUtil.ItemCallback<T> getDiffCallback() {
        return this.diffCallback;
    }

    /* compiled from: AsyncDifferConfig.kt */
    /* loaded from: classes3.dex */
    public static final class Builder<T> {
        public static CoroutineDispatcher sDiffDispatcher;
        public CoroutineDispatcher backgroundDispatcher;
        public final DiffUtil.ItemCallback<T> diffCallback;
        public ExecutorService mainThreadExecutor;
        public static final Companion Companion = new Companion(null);
        public static final Object sExecutorLock = new Object();

        public Builder(DiffUtil.ItemCallback<T> diffCallback) {
            Intrinsics.checkNotNullParameter(diffCallback, "diffCallback");
            this.diffCallback = diffCallback;
        }

        public final AsyncDifferConfig<T> build() {
            if (this.backgroundDispatcher == null) {
                synchronized (sExecutorLock) {
                    if (sDiffDispatcher == null) {
                        sDiffDispatcher = GalleryDispatchers.INSTANCE.getASYNC_DIFFER();
                    }
                    Unit unit = Unit.INSTANCE;
                }
                this.backgroundDispatcher = sDiffDispatcher;
            }
            ExecutorService executorService = this.mainThreadExecutor;
            CoroutineDispatcher coroutineDispatcher = this.backgroundDispatcher;
            Intrinsics.checkNotNull(coroutineDispatcher);
            return new AsyncDifferConfig<>(executorService, coroutineDispatcher, this.diffCallback);
        }

        /* compiled from: AsyncDifferConfig.kt */
        /* loaded from: classes3.dex */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public Companion() {
            }
        }
    }
}
