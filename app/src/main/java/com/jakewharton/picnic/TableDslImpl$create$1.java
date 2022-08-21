package com.jakewharton.picnic;

import com.jakewharton.picnic.Table;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: dsl.kt */
/* loaded from: classes.dex */
public final class TableDslImpl$create$1 extends Lambda implements Function1<Table.Builder, Unit> {
    public final /* synthetic */ TableDslImpl this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TableDslImpl$create$1(TableDslImpl tableDslImpl) {
        super(1);
        this.this$0 = tableDslImpl;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo2577invoke(Table.Builder builder) {
        invoke2(builder);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(Table.Builder receiver) {
        TableSectionDslImpl tableSectionDslImpl;
        TableSectionDslImpl tableSectionDslImpl2;
        TableSectionDslImpl tableSectionDslImpl3;
        CellStyleDslImpl cellStyleDslImpl;
        TableStyleDslImpl tableStyleDslImpl;
        Intrinsics.checkNotNullParameter(receiver, "$receiver");
        tableSectionDslImpl = this.this$0.headerImpl;
        receiver.setHeader(tableSectionDslImpl.createOrNull());
        tableSectionDslImpl2 = this.this$0.bodyImpl;
        receiver.setBody(tableSectionDslImpl2.create());
        tableSectionDslImpl3 = this.this$0.footerImpl;
        receiver.setFooter(tableSectionDslImpl3.createOrNull());
        cellStyleDslImpl = this.this$0.cellStyleImpl;
        receiver.setCellStyle(cellStyleDslImpl.createOrNull());
        tableStyleDslImpl = this.this$0.tableStyleImpl;
        receiver.setTableStyle(tableStyleDslImpl.createOrNull());
    }
}
