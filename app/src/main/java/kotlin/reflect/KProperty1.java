package kotlin.reflect;

import kotlin.jvm.functions.Function1;

/* compiled from: KProperty.kt */
/* loaded from: classes3.dex */
public interface KProperty1<T, V> extends KProperty<V>, Function1<T, V> {

    /* compiled from: KProperty.kt */
    /* loaded from: classes3.dex */
    public interface Getter<T, V> extends KFunction, Function1<T, V> {
    }

    V get(T t);

    Getter<T, V> getGetter();
}
