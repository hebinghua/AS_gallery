package kotlin.reflect;

import kotlin.jvm.functions.Function0;

/* compiled from: KProperty.kt */
/* loaded from: classes3.dex */
public interface KProperty0<V> extends KProperty<V>, Function0<V> {

    /* compiled from: KProperty.kt */
    /* loaded from: classes3.dex */
    public interface Getter<V> extends KFunction, Function0<V> {
    }

    V get();

    Getter<V> getGetter();
}
