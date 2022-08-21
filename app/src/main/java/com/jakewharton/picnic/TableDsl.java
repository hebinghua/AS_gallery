package com.jakewharton.picnic;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/* compiled from: dsl.kt */
/* loaded from: classes.dex */
public interface TableDsl extends TableSectionDsl {
    void body(Function1<? super TableSectionDsl, Unit> function1);

    void footer(Function1<? super TableSectionDsl, Unit> function1);

    void header(Function1<? super TableSectionDsl, Unit> function1);

    void style(Function1<? super TableStyleDsl, Unit> function1);
}
