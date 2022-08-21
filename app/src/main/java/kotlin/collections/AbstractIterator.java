package kotlin.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.markers.KMappedMarker;

/* compiled from: AbstractIterator.kt */
/* loaded from: classes3.dex */
public abstract class AbstractIterator<T> implements Iterator<T>, KMappedMarker {
    public T nextValue;
    public State state = State.NotReady;

    /* compiled from: AbstractIterator.kt */
    /* loaded from: classes3.dex */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[State.values().length];
            iArr[State.Done.ordinal()] = 1;
            iArr[State.Ready.ordinal()] = 2;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public abstract void computeNext();

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        State state = this.state;
        if (!(state != State.Failed)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        int i = WhenMappings.$EnumSwitchMapping$0[state.ordinal()];
        if (i == 1) {
            return false;
        }
        if (i == 2) {
            return true;
        }
        return tryToComputeNext();
    }

    @Override // java.util.Iterator
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        this.state = State.NotReady;
        return this.nextValue;
    }

    public final boolean tryToComputeNext() {
        this.state = State.Failed;
        computeNext();
        return this.state == State.Ready;
    }

    public final void setNext(T t) {
        this.nextValue = t;
        this.state = State.Ready;
    }

    public final void done() {
        this.state = State.Done;
    }
}
