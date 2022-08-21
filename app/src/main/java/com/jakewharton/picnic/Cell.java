package com.jakewharton.picnic;

import ch.qos.logback.core.CoreConstants;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: model.kt */
/* loaded from: classes.dex */
public final class Cell {
    public final int columnSpan;
    public final String content;
    public final int rowSpan;
    public final CellStyle style;

    public Cell(String str, int i, int i2, CellStyle cellStyle) {
        this.content = str;
        this.columnSpan = i;
        this.rowSpan = i2;
        this.style = cellStyle;
    }

    public /* synthetic */ Cell(String str, int i, int i2, CellStyle cellStyle, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, i, i2, cellStyle);
    }

    public final String getContent() {
        return this.content;
    }

    public final int getColumnSpan() {
        return this.columnSpan;
    }

    public final int getRowSpan() {
        return this.rowSpan;
    }

    public final CellStyle getStyle() {
        return this.style;
    }

    public String toString() {
        return "Cell(content=" + this.content + ", columnSpan=" + this.columnSpan + ", rowSpan=" + this.rowSpan + ", style=" + this.style + CoreConstants.RIGHT_PARENTHESIS_CHAR;
    }

    public int hashCode() {
        return Objects.hash(this.content, Integer.valueOf(this.columnSpan), Integer.valueOf(this.rowSpan), this.style);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Cell) {
            Cell cell = (Cell) obj;
            if (Intrinsics.areEqual(this.content, cell.content) && this.columnSpan == cell.columnSpan && this.rowSpan == cell.rowSpan && Intrinsics.areEqual(this.style, cell.style)) {
                return true;
            }
        }
        return false;
    }

    /* compiled from: model.kt */
    /* loaded from: classes.dex */
    public static final class Builder {
        public final Object content;
        public CellStyle style;
        public int columnSpan = 1;
        public int rowSpan = 1;

        public Builder(Object obj) {
            this.content = obj;
        }

        public final /* synthetic */ void setColumnSpan(int i) {
            this.columnSpan = i;
        }

        public final /* synthetic */ void setRowSpan(int i) {
            this.rowSpan = i;
        }

        public final /* synthetic */ void setStyle(CellStyle cellStyle) {
            this.style = cellStyle;
        }

        public final Cell build() {
            return new Cell(String.valueOf(this.content), this.columnSpan, this.rowSpan, this.style, null);
        }
    }
}
