package com.jakewharton.picnic;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: dsl.kt */
/* loaded from: classes.dex */
public final class TableDslImpl implements TableDsl {
    public final TableSectionDslImpl headerImpl = new TableSectionDslImpl();
    public final TableSectionDslImpl bodyImpl = new TableSectionDslImpl();
    public final TableSectionDslImpl footerImpl = new TableSectionDslImpl();
    public final CellStyleDslImpl cellStyleImpl = new CellStyleDslImpl();
    public final TableStyleDslImpl tableStyleImpl = new TableStyleDslImpl();

    @Override // com.jakewharton.picnic.TableDsl
    public void header(Function1<? super TableSectionDsl, Unit> content) {
        Intrinsics.checkNotNullParameter(content, "content");
        content.mo2577invoke(this.headerImpl);
    }

    @Override // com.jakewharton.picnic.TableDsl
    public void body(Function1<? super TableSectionDsl, Unit> content) {
        Intrinsics.checkNotNullParameter(content, "content");
        content.mo2577invoke(this.bodyImpl);
    }

    @Override // com.jakewharton.picnic.TableDsl
    public void footer(Function1<? super TableSectionDsl, Unit> content) {
        Intrinsics.checkNotNullParameter(content, "content");
        content.mo2577invoke(this.footerImpl);
    }

    @Override // com.jakewharton.picnic.TableSectionDsl
    public void row(Function1<? super RowDsl, Unit> content) {
        Intrinsics.checkNotNullParameter(content, "content");
        this.bodyImpl.row(content);
    }

    @Override // com.jakewharton.picnic.TableSectionDsl
    public void cellStyle(Function1<? super CellStyleDsl, Unit> content) {
        Intrinsics.checkNotNullParameter(content, "content");
        content.mo2577invoke(this.cellStyleImpl);
    }

    @Override // com.jakewharton.picnic.TableDsl
    public void style(Function1<? super TableStyleDsl, Unit> content) {
        Intrinsics.checkNotNullParameter(content, "content");
        content.mo2577invoke(this.tableStyleImpl);
    }

    public final Table create() {
        return ModelKt.Table(new TableDslImpl$create$1(this));
    }
}
