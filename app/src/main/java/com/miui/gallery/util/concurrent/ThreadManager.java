package com.miui.gallery.util.concurrent;

import android.os.Looper;
import com.android.internal.CompatHandler;
import com.miui.gallery.concurrent.ThreadPool;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ThreadManager.kt */
/* loaded from: classes2.dex */
public class ThreadManager {
    public static final Companion Companion = new Companion(null);
    public static final Lazy<ThreadPool> regionDecodePool$delegate = LazyKt__LazyJVMKt.lazy(ThreadManager$Companion$regionDecodePool$2.INSTANCE);
    public static final Lazy<ThreadPool> decodePoolForHeif$delegate = LazyKt__LazyJVMKt.lazy(ThreadManager$Companion$decodePoolForHeif$2.INSTANCE);
    public static final Lazy<ThreadPool> tileProviderPool$delegate = LazyKt__LazyJVMKt.lazy(ThreadManager$Companion$tileProviderPool$2.INSTANCE);
    public static final Lazy<ThreadPool> miscPool$delegate = LazyKt__LazyJVMKt.lazy(ThreadManager$Companion$miscPool$2.INSTANCE);
    public static final Lazy<ThreadPool> previewPool$delegate = LazyKt__LazyJVMKt.lazy(ThreadManager$Companion$previewPool$2.INSTANCE);
    public static final Lazy<ThreadPool> requestPool$delegate = LazyKt__LazyJVMKt.lazy(ThreadManager$Companion$requestPool$2.INSTANCE);
    public static final Lazy<CompatHandler> mainHandler$delegate = LazyKt__LazyJVMKt.lazy(ThreadManager$Companion$mainHandler$2.INSTANCE);
    public static final Lazy<CompatHandler> workHandler$delegate = LazyKt__LazyJVMKt.lazy(ThreadManager$Companion$workHandler$2.INSTANCE);
    public static final Lazy<CompatHandler> networkRequestHandler$delegate = LazyKt__LazyJVMKt.lazy(ThreadManager$Companion$networkRequestHandler$2.INSTANCE);
    public static final Lazy<Looper> drawLooper$delegate = LazyKt__LazyJVMKt.lazy(ThreadManager$Companion$drawLooper$2.INSTANCE);

    public static final ThreadPool getDecodePoolForHeif() {
        return Companion.getDecodePoolForHeif();
    }

    public static final CompatHandler getMainHandler() {
        return Companion.getMainHandler();
    }

    public static final ThreadPool getMiscPool() {
        return Companion.getMiscPool();
    }

    public static final CompatHandler getNetworkRequestHandler() {
        return Companion.getNetworkRequestHandler();
    }

    public static final Looper getNetworkRequestLooper() {
        return Companion.getNetworkRequestLooper();
    }

    public static final ThreadPool getPreviewPool() {
        return Companion.getPreviewPool();
    }

    public static final ThreadPool getRegionDecodePool() {
        return Companion.getRegionDecodePool();
    }

    public static final ThreadPool getRequestPool() {
        return Companion.getRequestPool();
    }

    public static final ThreadPool getTileProviderPool() {
        return Companion.getTileProviderPool();
    }

    public static final CompatHandler getWorkHandler() {
        return Companion.getWorkHandler();
    }

    public static final Looper getWorkThreadLooper() {
        return Companion.getWorkThreadLooper();
    }

    public static final boolean isMainThread() {
        return Companion.isMainThread();
    }

    public static final void runOnMainThread(Runnable runnable) {
        Companion.runOnMainThread(runnable);
    }

    public static final void sleepThread(long j) {
        Companion.sleepThread(j);
    }

    /* compiled from: ThreadManager.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final ThreadPool getRegionDecodePool() {
            return (ThreadPool) ThreadManager.regionDecodePool$delegate.mo119getValue();
        }

        public final ThreadPool getDecodePoolForHeif() {
            return (ThreadPool) ThreadManager.decodePoolForHeif$delegate.mo119getValue();
        }

        public final ThreadPool getTileProviderPool() {
            return (ThreadPool) ThreadManager.tileProviderPool$delegate.mo119getValue();
        }

        public final ThreadPool getMiscPool() {
            return (ThreadPool) ThreadManager.miscPool$delegate.mo119getValue();
        }

        public final ThreadPool getPreviewPool() {
            return (ThreadPool) ThreadManager.previewPool$delegate.mo119getValue();
        }

        public final ThreadPool getRequestPool() {
            return (ThreadPool) ThreadManager.requestPool$delegate.mo119getValue();
        }

        public final CompatHandler getMainHandler() {
            return (CompatHandler) ThreadManager.mainHandler$delegate.mo119getValue();
        }

        public final CompatHandler getWorkHandler() {
            return (CompatHandler) ThreadManager.workHandler$delegate.mo119getValue();
        }

        public final Looper getWorkThreadLooper() {
            Looper looper = getWorkHandler().getLooper();
            Intrinsics.checkNotNullExpressionValue(looper, "workHandler.looper");
            return looper;
        }

        public final void runOnMainThread(Runnable runnable) {
            if (runnable == null) {
                return;
            }
            if (Intrinsics.areEqual(Looper.getMainLooper(), Looper.myLooper())) {
                runnable.run();
            } else {
                getMainHandler().post(runnable);
            }
        }

        public final CompatHandler getNetworkRequestHandler() {
            return (CompatHandler) ThreadManager.networkRequestHandler$delegate.mo119getValue();
        }

        public final Looper getNetworkRequestLooper() {
            Looper looper = getNetworkRequestHandler().getLooper();
            Intrinsics.checkNotNullExpressionValue(looper, "networkRequestHandler.looper");
            return looper;
        }

        public final void sleepThread(long j) {
            try {
                Thread.sleep(j);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public final boolean isMainThread() {
            return Intrinsics.areEqual(Looper.myLooper(), Looper.getMainLooper());
        }
    }
}
