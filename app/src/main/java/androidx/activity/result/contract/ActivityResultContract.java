package androidx.activity.result.contract;

import android.content.Context;
import android.content.Intent;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ActivityResultContract.kt */
/* loaded from: classes.dex */
public abstract class ActivityResultContract<I, O> {
    public abstract Intent createIntent(Context context, I i);

    public SynchronousResult<O> getSynchronousResult(Context context, I i) {
        Intrinsics.checkNotNullParameter(context, "context");
        return null;
    }

    /* renamed from: parseResult */
    public abstract O mo1398parseResult(int i, Intent intent);

    /* compiled from: ActivityResultContract.kt */
    /* loaded from: classes.dex */
    public static final class SynchronousResult<T> {
        public final T value;

        public SynchronousResult(T t) {
            this.value = t;
        }

        public final T getValue() {
            return this.value;
        }
    }
}
