package kotlin.comparisons;

/* compiled from: Comparisons.kt */
/* loaded from: classes3.dex */
public class ComparisonsKt__ComparisonsKt {
    public static final <T extends Comparable<?>> int compareValues(T t, T t2) {
        if (t == t2) {
            return 0;
        }
        if (t == null) {
            return -1;
        }
        if (t2 != null) {
            return t.compareTo(t2);
        }
        return 1;
    }
}
