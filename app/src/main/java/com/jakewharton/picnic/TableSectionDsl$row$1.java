package com.jakewharton.picnic;

import com.jakewharton.picnic.RowDsl;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: dsl.kt */
/* loaded from: classes.dex */
public final class TableSectionDsl$row$1 extends Lambda implements Function1<RowDsl, Unit> {
    public final /* synthetic */ Object[] $cells;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TableSectionDsl$row$1(Object[] objArr) {
        super(1);
        this.$cells = objArr;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo2577invoke(RowDsl rowDsl) {
        invoke2(rowDsl);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(RowDsl receiver) {
        Intrinsics.checkNotNullParameter(receiver, "$receiver");
        for (Object obj : this.$cells) {
            RowDsl.DefaultImpls.cell$default(receiver, obj, null, 2, null);
        }
    }
}
