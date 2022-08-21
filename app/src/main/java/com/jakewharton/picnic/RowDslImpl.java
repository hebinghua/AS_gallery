package com.jakewharton.picnic;

import com.jakewharton.picnic.Row;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: dsl.kt */
/* loaded from: classes.dex */
public final class RowDslImpl implements RowDsl {
    public final Row.Builder builder = new Row.Builder();
    public final CellStyleDslImpl cellStyleImpl = new CellStyleDslImpl();

    @Override // com.jakewharton.picnic.RowDsl
    public void cell(Object obj, Function1<? super CellDsl, Unit> style) {
        Intrinsics.checkNotNullParameter(style, "style");
        Row.Builder builder = this.builder;
        CellDslImpl cellDslImpl = new CellDslImpl(obj);
        style.mo2577invoke(cellDslImpl);
        builder.addCell(cellDslImpl.create());
    }

    public final Row create() {
        return this.builder.setCellStyle(this.cellStyleImpl.createOrNull()).build();
    }
}
