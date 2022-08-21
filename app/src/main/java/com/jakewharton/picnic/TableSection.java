package com.jakewharton.picnic;

import ch.qos.logback.core.CoreConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: model.kt */
/* loaded from: classes.dex */
public final class TableSection {
    public final CellStyle cellStyle;
    public final List<Row> rows;

    public TableSection(List<Row> list, CellStyle cellStyle) {
        this.rows = list;
        this.cellStyle = cellStyle;
    }

    public /* synthetic */ TableSection(List list, CellStyle cellStyle, DefaultConstructorMarker defaultConstructorMarker) {
        this(list, cellStyle);
    }

    public final List<Row> getRows() {
        return this.rows;
    }

    public final CellStyle getCellStyle() {
        return this.cellStyle;
    }

    public String toString() {
        return "TableSection(rows=" + this.rows + ", cellStyle=" + this.cellStyle + CoreConstants.RIGHT_PARENTHESIS_CHAR;
    }

    public int hashCode() {
        return Objects.hash(this.rows, this.cellStyle);
    }

    public boolean equals(Object obj) {
        if (obj instanceof TableSection) {
            TableSection tableSection = (TableSection) obj;
            if (Intrinsics.areEqual(this.rows, tableSection.rows) && Intrinsics.areEqual(this.cellStyle, tableSection.cellStyle)) {
                return true;
            }
        }
        return false;
    }

    /* compiled from: model.kt */
    /* loaded from: classes.dex */
    public static final class Builder {
        public CellStyle cellStyle;
        public List<Row> rows = new ArrayList();

        public final List<Row> getRows() {
            return this.rows;
        }

        public final Builder addRow(Row row) {
            Intrinsics.checkNotNullParameter(row, "row");
            this.rows.add(row);
            return this;
        }

        public final Builder setCellStyle(CellStyle cellStyle) {
            this.cellStyle = cellStyle;
            return this;
        }

        public final TableSection build() {
            return new TableSection(CollectionsKt___CollectionsKt.toList(this.rows), this.cellStyle, null);
        }
    }
}
