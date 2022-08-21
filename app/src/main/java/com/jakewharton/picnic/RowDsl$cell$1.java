package com.jakewharton.picnic;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: dsl.kt */
/* loaded from: classes.dex */
public final class RowDsl$cell$1 extends Lambda implements Function1<CellDsl, Unit> {
    public static final RowDsl$cell$1 INSTANCE = new RowDsl$cell$1();

    public RowDsl$cell$1() {
        super(1);
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(CellDsl receiver) {
        Intrinsics.checkNotNullParameter(receiver, "$receiver");
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo2577invoke(CellDsl cellDsl) {
        invoke2(cellDsl);
        return Unit.INSTANCE;
    }
}
