package com.jakewharton.picnic;

import com.jakewharton.picnic.TableSection;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: dsl.kt */
/* loaded from: classes.dex */
public final class TableSectionDslImpl implements TableSectionDsl {
    public final TableSection.Builder builder = new TableSection.Builder();
    public final CellStyleDslImpl cellStyleImpl = new CellStyleDslImpl();

    @Override // com.jakewharton.picnic.TableSectionDsl
    public void row(Function1<? super RowDsl, Unit> content) {
        Intrinsics.checkNotNullParameter(content, "content");
        TableSection.Builder builder = this.builder;
        RowDslImpl rowDslImpl = new RowDslImpl();
        content.mo2577invoke(rowDslImpl);
        builder.addRow(rowDslImpl.create());
    }

    @Override // com.jakewharton.picnic.TableSectionDsl
    public void cellStyle(Function1<? super CellStyleDsl, Unit> content) {
        Intrinsics.checkNotNullParameter(content, "content");
        content.mo2577invoke(this.cellStyleImpl);
    }

    public final TableSection createOrNull() {
        if (this.builder.getRows().isEmpty()) {
            return null;
        }
        return create();
    }

    public final TableSection create() {
        return this.builder.setCellStyle(this.cellStyleImpl.createOrNull()).build();
    }
}
