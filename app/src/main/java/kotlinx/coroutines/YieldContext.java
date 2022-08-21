package kotlinx.coroutines;

import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* compiled from: Unconfined.kt */
/* loaded from: classes3.dex */
public final class YieldContext extends AbstractCoroutineContextElement {
    public static final Key Key = new Key(null);
    public boolean dispatcherWasUnconfined;

    /* compiled from: Unconfined.kt */
    /* loaded from: classes3.dex */
    public static final class Key implements CoroutineContext.Key<YieldContext> {
        public /* synthetic */ Key(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Key() {
        }
    }

    public YieldContext() {
        super(Key);
    }
}
