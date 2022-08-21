package com.jakewharton.picnic;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: dsl.kt */
/* loaded from: classes.dex */
public interface TableSectionDsl {
    void cellStyle(Function1<? super CellStyleDsl, Unit> function1);

    void row(Function1<? super RowDsl, Unit> function1);

    default void row(Object... cells) {
        Intrinsics.checkNotNullParameter(cells, "cells");
        row(new TableSectionDsl$row$1(cells));
    }
}
