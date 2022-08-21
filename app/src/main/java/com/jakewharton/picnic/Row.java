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
public final class Row {
    public final CellStyle cellStyle;
    public final List<Cell> cells;

    public Row(List<Cell> list, CellStyle cellStyle) {
        this.cells = list;
        this.cellStyle = cellStyle;
    }

    public /* synthetic */ Row(List list, CellStyle cellStyle, DefaultConstructorMarker defaultConstructorMarker) {
        this(list, cellStyle);
    }

    public final List<Cell> getCells() {
        return this.cells;
    }

    public final CellStyle getCellStyle() {
        return this.cellStyle;
    }

    public String toString() {
        return "Row(cells=" + this.cells + ", cellStyle=" + this.cellStyle + CoreConstants.RIGHT_PARENTHESIS_CHAR;
    }

    public int hashCode() {
        return Objects.hash(this.cells, this.cellStyle);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Row) {
            Row row = (Row) obj;
            if (Intrinsics.areEqual(this.cells, row.cells) && Intrinsics.areEqual(this.cellStyle, row.cellStyle)) {
                return true;
            }
        }
        return false;
    }

    /* compiled from: model.kt */
    /* loaded from: classes.dex */
    public static final class Builder {
        public CellStyle cellStyle;
        public List<Cell> cells = new ArrayList();

        public final Builder addCell(Cell cell) {
            Intrinsics.checkNotNullParameter(cell, "cell");
            this.cells.add(cell);
            return this;
        }

        public final Builder setCellStyle(CellStyle cellStyle) {
            this.cellStyle = cellStyle;
            return this;
        }

        public final Row build() {
            return new Row(CollectionsKt___CollectionsKt.toList(this.cells), this.cellStyle, null);
        }
    }
}
