package com.miui.gallery.widget.recyclerview;

import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;

/* compiled from: DiffableItem.kt */
/* loaded from: classes3.dex */
public interface DiffableItem<T> {
    default boolean itemSameAs(T t) {
        return Intrinsics.areEqual(this, t);
    }

    default boolean contentSameAs(T t) {
        Comparable comparable = this instanceof Comparable ? (Comparable) this : null;
        return comparable != null && comparable.compareTo(t) == 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    default boolean genericItemSameAs(Object other) {
        Intrinsics.checkNotNullParameter(other, "other");
        return Intrinsics.areEqual(Reflection.getOrCreateKotlinClass(other.getClass()), Reflection.getOrCreateKotlinClass(getClass())) && itemSameAs(other);
    }

    /* JADX WARN: Multi-variable type inference failed */
    default boolean genericContentSameAs(Object other) {
        Intrinsics.checkNotNullParameter(other, "other");
        return Intrinsics.areEqual(Reflection.getOrCreateKotlinClass(other.getClass()), Reflection.getOrCreateKotlinClass(getClass())) && contentSameAs(other);
    }
}
