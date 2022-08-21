package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.markers.KMappedMarker;

/* compiled from: Sequences.kt */
/* loaded from: classes3.dex */
public final class TransformingSequence$iterator$1 implements Iterator<R>, KMappedMarker {
    public final Iterator<T> iterator;
    public final /* synthetic */ TransformingSequence<T, R> this$0;

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public TransformingSequence$iterator$1(TransformingSequence<T, R> transformingSequence) {
        Sequence sequence;
        this.this$0 = transformingSequence;
        sequence = transformingSequence.sequence;
        this.iterator = sequence.iterator();
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [R, java.lang.Object] */
    @Override // java.util.Iterator
    public R next() {
        Function1 function1;
        function1 = this.this$0.transformer;
        return function1.mo2577invoke(this.iterator.next());
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.iterator.hasNext();
    }
}
