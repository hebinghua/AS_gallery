package com.jakewharton.picnic;

import com.jakewharton.picnic.Cell;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: dsl.kt */
/* loaded from: classes.dex */
public final class CellDslImpl$create$1 extends Lambda implements Function1<Cell.Builder, Unit> {
    public final /* synthetic */ CellDslImpl this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CellDslImpl$create$1(CellDslImpl cellDslImpl) {
        super(1);
        this.this$0 = cellDslImpl;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo2577invoke(Cell.Builder builder) {
        invoke2(builder);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(Cell.Builder receiver) {
        CellStyleDslImpl cellStyleDslImpl;
        Intrinsics.checkNotNullParameter(receiver, "$receiver");
        receiver.setColumnSpan(this.this$0.getColumnSpan());
        receiver.setRowSpan(this.this$0.getRowSpan());
        cellStyleDslImpl = this.this$0.cellStyleImpl;
        receiver.setStyle(cellStyleDslImpl.createOrNull());
    }
}
