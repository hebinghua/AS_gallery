package com.jakewharton.picnic;

import ch.qos.logback.core.CoreConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: model.kt */
/* loaded from: classes.dex */
public final class Table {
    public final TableSection body;
    public final CellStyle cellStyle;
    public final List<List<PositionedCell>> cellTable;
    public final int columnCount;
    public final TableSection footer;
    public final TableSection header;
    public final List<PositionedCell> positionedCells;
    public final int rowCount;
    public final TableStyle tableStyle;

    public Table(TableSection tableSection, TableSection tableSection2, TableSection tableSection3, CellStyle cellStyle, TableStyle tableStyle) {
        List<Row> rows;
        List<Row> rows2;
        this.header = tableSection;
        this.body = tableSection2;
        this.footer = tableSection3;
        this.cellStyle = cellStyle;
        this.tableStyle = tableStyle;
        int i = 0;
        this.rowCount = ((tableSection == null || (rows2 = tableSection.getRows()) == null) ? 0 : rows2.size()) + tableSection2.getRows().size() + ((tableSection3 == null || (rows = tableSection3.getRows()) == null) ? 0 : rows.size());
        IntCounts intCounts = new IntCounts(0, 1, null);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        Iterator it = CollectionsKt__CollectionsKt.listOfNotNull(tableSection, tableSection2, tableSection3).iterator();
        int i2 = 0;
        while (it.hasNext()) {
            TableSection tableSection4 = (TableSection) it.next();
            CellStyle access$plus = ModelKt.access$plus(this.cellStyle, tableSection4.getCellStyle());
            Iterator it2 = tableSection4.getRows().iterator();
            while (it2.hasNext()) {
                Row row = (Row) it2.next();
                CellStyle access$plus2 = ModelKt.access$plus(access$plus, row.getCellStyle());
                ArrayList arrayList3 = new ArrayList();
                arrayList2.add(arrayList3);
                int i3 = i;
                int i4 = i3;
                for (Object obj : row.getCells()) {
                    int i5 = i3 + 1;
                    if (i3 < 0) {
                        CollectionsKt__CollectionsKt.throwIndexOverflow();
                    }
                    Cell cell = (Cell) obj;
                    while (i4 < intCounts.getSize() && intCounts.get(i4) > 0) {
                        arrayList3.add(((List) arrayList2.get(i2 - 1)).get(i4));
                        intCounts.set(i4, intCounts.get(i4) - 1);
                        i4++;
                    }
                    PositionedCell positionedCell = new PositionedCell(i2, i4, cell, ModelKt.access$plus(access$plus2, cell.getStyle()));
                    arrayList.add(positionedCell);
                    int rowSpan = cell.getRowSpan();
                    Iterator it3 = it;
                    Iterator it4 = it2;
                    if (!(i2 + rowSpan <= this.rowCount)) {
                        throw new IllegalArgumentException(("Cell " + i3 + " in row " + i2 + " has rowSpan=" + rowSpan + " but table rowCount=" + this.rowCount).toString());
                    }
                    int i6 = rowSpan - 1;
                    int columnSpan = cell.getColumnSpan();
                    for (int i7 = 0; i7 < columnSpan; i7++) {
                        arrayList3.add(positionedCell);
                        intCounts.set(i4, i6);
                        i4++;
                    }
                    it = it3;
                    it2 = it4;
                    i3 = i5;
                }
                Iterator it5 = it;
                Iterator it6 = it2;
                while (i4 < intCounts.getSize()) {
                    if (intCounts.get(i4) > 0) {
                        arrayList3.add(((List) arrayList2.get(i2 - 1)).get(i4));
                        intCounts.set(i4, intCounts.get(i4) - 1);
                    } else {
                        arrayList3.add(null);
                    }
                    i4++;
                }
                i2++;
                it2 = it6;
                i = 0;
                it = it5;
            }
        }
        this.columnCount = intCounts.getSize();
        this.positionedCells = arrayList;
        this.cellTable = arrayList2;
    }

    public /* synthetic */ Table(TableSection tableSection, TableSection tableSection2, TableSection tableSection3, CellStyle cellStyle, TableStyle tableStyle, DefaultConstructorMarker defaultConstructorMarker) {
        this(tableSection, tableSection2, tableSection3, cellStyle, tableStyle);
    }

    public final TableStyle getTableStyle() {
        return this.tableStyle;
    }

    public String toString() {
        return TextRendering.render$default(this, null, null, 3, null);
    }

    public int hashCode() {
        return Objects.hash(this.header, this.body, this.footer, this.cellStyle, this.tableStyle);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Table) {
            Table table = (Table) obj;
            if (Intrinsics.areEqual(this.header, table.header) && Intrinsics.areEqual(this.body, table.body) && Intrinsics.areEqual(this.footer, table.footer) && Intrinsics.areEqual(this.cellStyle, table.cellStyle) && Intrinsics.areEqual(this.tableStyle, table.tableStyle)) {
                return true;
            }
        }
        return false;
    }

    public final int getRowCount() {
        return this.rowCount;
    }

    public final int getColumnCount() {
        return this.columnCount;
    }

    public final List<PositionedCell> getPositionedCells() {
        return this.positionedCells;
    }

    public final PositionedCell getOrNull(int i, int i2) {
        List list = (List) CollectionsKt___CollectionsKt.getOrNull(this.cellTable, i);
        if (list != null) {
            return (PositionedCell) CollectionsKt___CollectionsKt.getOrNull(list, i2);
        }
        return null;
    }

    /* compiled from: model.kt */
    /* loaded from: classes.dex */
    public static final class PositionedCell {
        public final CellStyle canonicalStyle;
        public final Cell cell;
        public final int columnIndex;
        public final int rowIndex;

        public PositionedCell(int i, int i2, Cell cell, CellStyle cellStyle) {
            Intrinsics.checkNotNullParameter(cell, "cell");
            this.rowIndex = i;
            this.columnIndex = i2;
            this.cell = cell;
            this.canonicalStyle = cellStyle;
        }

        public final int getRowIndex() {
            return this.rowIndex;
        }

        public final int getColumnIndex() {
            return this.columnIndex;
        }

        public final Cell getCell() {
            return this.cell;
        }

        public final CellStyle getCanonicalStyle() {
            return this.canonicalStyle;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.rowIndex), Integer.valueOf(this.columnIndex), this.cell, this.canonicalStyle);
        }

        public boolean equals(Object obj) {
            if (obj instanceof PositionedCell) {
                PositionedCell positionedCell = (PositionedCell) obj;
                if (this.rowIndex == positionedCell.rowIndex && this.columnIndex == positionedCell.columnIndex && Intrinsics.areEqual(this.cell, positionedCell.cell) && Intrinsics.areEqual(this.canonicalStyle, positionedCell.canonicalStyle)) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return "PositionedCell(rowIndex=" + this.rowIndex + ", colIndex=" + this.columnIndex + ", cell=" + this.cell + ", canonicalStyle=" + this.canonicalStyle + CoreConstants.RIGHT_PARENTHESIS_CHAR;
        }
    }

    /* compiled from: model.kt */
    /* loaded from: classes.dex */
    public static final class Builder {
        public TableSection body;
        public CellStyle cellStyle;
        public TableSection footer;
        public TableSection header;
        public TableStyle tableStyle;

        public final /* synthetic */ void setHeader(TableSection tableSection) {
            this.header = tableSection;
        }

        public final /* synthetic */ void setBody(TableSection tableSection) {
            this.body = tableSection;
        }

        public final /* synthetic */ void setFooter(TableSection tableSection) {
            this.footer = tableSection;
        }

        public final /* synthetic */ void setCellStyle(CellStyle cellStyle) {
            this.cellStyle = cellStyle;
        }

        public final /* synthetic */ void setTableStyle(TableStyle tableStyle) {
            this.tableStyle = tableStyle;
        }

        public final Table build() {
            TableSection tableSection = this.header;
            TableSection tableSection2 = this.body;
            if (tableSection2 != null) {
                return new Table(tableSection, tableSection2, this.footer, this.cellStyle, this.tableStyle, null);
            }
            throw new IllegalStateException("Body section is required".toString());
        }
    }
}
