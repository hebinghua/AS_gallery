package kotlin.properties;

import kotlin.reflect.KProperty;

/* compiled from: Interfaces.kt */
/* loaded from: classes3.dex */
public interface ReadWriteProperty<T, V> {
    V getValue(T t, KProperty<?> kProperty);

    void setValue(T t, KProperty<?> kProperty, V v);
}
