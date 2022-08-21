package com.miui.gallery.arch.livedata;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Looper;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.miui.gallery.arch.internal.TaskExecutor;
import java.io.Closeable;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ComputableLiveData.kt */
/* loaded from: classes.dex */
public abstract class ComputableLiveData<T> extends LiveData<T> implements Closeable {
    public static final Companion Companion = new Companion(null);
    public final AtomicBoolean computing;
    public final Executor executor;
    public final AtomicBoolean invalid;
    public final Runnable invalidationRunnable;
    public final Runnable refreshRunnable;

    /* renamed from: $r8$lambda$5TfaQAU-4HbEdNqCkpA41Jkqfx8 */
    public static /* synthetic */ void m551$r8$lambda$5TfaQAU4HbEdNqCkpA41Jkqfx8(ComputableLiveData computableLiveData) {
        m552invalidationRunnable$lambda1(computableLiveData);
    }

    public static /* synthetic */ void $r8$lambda$8Gm8eg3VT5pawXzSe6z8YCpjd7U(ComputableLiveData computableLiveData) {
        m553refreshRunnable$lambda0(computableLiveData);
    }

    public ComputableLiveData() {
        this(null, 1, null);
    }

    /* renamed from: compute */
    public abstract T mo1205compute();

    @Override // androidx.lifecycle.LiveData
    public void onInactive() {
    }

    public void preSetValue(T t, T t2) {
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public /* synthetic */ ComputableLiveData(java.util.concurrent.Executor r1, int r2, kotlin.jvm.internal.DefaultConstructorMarker r3) {
        /*
            r0 = this;
            r2 = r2 & 1
            if (r2 == 0) goto Lb
            java.util.concurrent.Executor r1 = android.os.AsyncTask.THREAD_POOL_EXECUTOR
            java.lang.String r2 = "THREAD_POOL_EXECUTOR"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r1, r2)
        Lb:
            r0.<init>(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.arch.livedata.ComputableLiveData.<init>(java.util.concurrent.Executor, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public ComputableLiveData(Executor executor) {
        Intrinsics.checkNotNullParameter(executor, "executor");
        this.executor = executor;
        this.invalid = new AtomicBoolean(true);
        this.computing = new AtomicBoolean(false);
        this.refreshRunnable = new Runnable() { // from class: com.miui.gallery.arch.livedata.ComputableLiveData$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ComputableLiveData.$r8$lambda$8Gm8eg3VT5pawXzSe6z8YCpjd7U(ComputableLiveData.this);
            }
        };
        this.invalidationRunnable = new Runnable() { // from class: com.miui.gallery.arch.livedata.ComputableLiveData$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ComputableLiveData.m551$r8$lambda$5TfaQAU4HbEdNqCkpA41Jkqfx8(ComputableLiveData.this);
            }
        };
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* renamed from: refreshRunnable$lambda-0 */
    public static final void m553refreshRunnable$lambda0(ComputableLiveData this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        do {
            boolean z = false;
            if (this$0.computing.compareAndSet(false, true)) {
                Object obj = null;
                boolean z2 = false;
                while (this$0.invalid.compareAndSet(true, false)) {
                    try {
                        obj = this$0.mo1205compute();
                        z2 = true;
                    } catch (Throwable th) {
                        this$0.computing.set(false);
                        throw th;
                    }
                }
                if (z2) {
                    this$0.postValue(obj);
                }
                this$0.computing.set(false);
                z = z2;
            }
            if (!z) {
                return;
            }
        } while (this$0.invalid.get());
    }

    /* renamed from: invalidationRunnable$lambda-1 */
    public static final void m552invalidationRunnable$lambda1(ComputableLiveData this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        boolean isActiveForRefresh = this$0.isActiveForRefresh();
        if (!this$0.invalid.compareAndSet(false, true) || !isActiveForRefresh) {
            return;
        }
        this$0.executor.execute(this$0.refreshRunnable);
    }

    public boolean isActiveForRefresh() {
        return hasActiveObservers();
    }

    public final boolean isInvalid() {
        return this.invalid.get();
    }

    @Override // androidx.lifecycle.LiveData
    public void onActive() {
        this.executor.execute(this.refreshRunnable);
    }

    @Override // androidx.lifecycle.LiveData
    public final void setValue(T t) {
        preSetValue(getValue(), t);
        super.setValue(t);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (Looper.getMainLooper().isCurrentThread()) {
            setValue(null);
        } else {
            postValue(null);
        }
    }

    public final void invalidate() {
        TaskExecutor.INSTANCE.postToMainThread(this.invalidationRunnable);
    }

    /* compiled from: ComputableLiveData.kt */
    /* loaded from: classes.dex */
    public static final class SelfContentObserver extends ContentObserver {
        public ContentResolver contentResolver;
        public WeakReference<ComputableLiveData<?>> liveDataRef;

        @Override // android.database.ContentObserver
        public boolean deliverSelfNotifications() {
            return false;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SelfContentObserver(ComputableLiveData<?> liveData, ContentResolver contentResolver) {
            super(null);
            Intrinsics.checkNotNullParameter(liveData, "liveData");
            Intrinsics.checkNotNullParameter(contentResolver, "contentResolver");
            this.contentResolver = contentResolver;
            this.liveDataRef = new WeakReference<>(liveData);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            ComputableLiveData<?> computableLiveData = this.liveDataRef.get();
            if (computableLiveData == null) {
                Log.d("ComputableLiveData", "Unregister observer, trigger by GC");
                this.contentResolver.unregisterContentObserver(this);
                return;
            }
            computableLiveData.invalidate();
        }
    }

    /* compiled from: ComputableLiveData.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
