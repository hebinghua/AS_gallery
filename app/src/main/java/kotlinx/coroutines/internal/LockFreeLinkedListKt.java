package kotlinx.coroutines.internal;

/* compiled from: LockFreeLinkedList.kt */
/* loaded from: classes3.dex */
public final class LockFreeLinkedListKt {
    public static final Object CONDITION_FALSE = new Symbol("CONDITION_FALSE");
    public static final Object LIST_EMPTY = new Symbol("LIST_EMPTY");

    public static final Object getCONDITION_FALSE() {
        return CONDITION_FALSE;
    }

    public static final LockFreeLinkedListNode unwrap(Object obj) {
        LockFreeLinkedListNode lockFreeLinkedListNode = null;
        Removed removed = obj instanceof Removed ? (Removed) obj : null;
        if (removed != null) {
            lockFreeLinkedListNode = removed.ref;
        }
        return lockFreeLinkedListNode == null ? (LockFreeLinkedListNode) obj : lockFreeLinkedListNode;
    }
}
