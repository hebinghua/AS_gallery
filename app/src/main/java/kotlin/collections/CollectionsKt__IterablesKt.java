package kotlin.collections;

import java.util.Collection;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Iterables.kt */
/* loaded from: classes3.dex */
public class CollectionsKt__IterablesKt extends CollectionsKt__CollectionsKt {
    public static final <T> int collectionSizeOrDefault(Iterable<? extends T> iterable, int i) {
        Intrinsics.checkNotNullParameter(iterable, "<this>");
        return iterable instanceof Collection ? ((Collection) iterable).size() : i;
    }
}
