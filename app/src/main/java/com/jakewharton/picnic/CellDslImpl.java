package com.jakewharton.picnic;

/* compiled from: dsl.kt */
/* loaded from: classes.dex */
public final class CellDslImpl implements CellDsl, CellStyleDsl {
    public final CellStyleDslImpl cellStyleImpl;
    public int columnSpan;
    public final Object content;
    public int rowSpan;

    @Override // com.jakewharton.picnic.CellStyleDsl
    public void setAlignment(TextAlignment textAlignment) {
        this.cellStyleImpl.setAlignment(textAlignment);
    }

    @Override // com.jakewharton.picnic.CellStyleDsl
    public void setBorderBottom(Boolean bool) {
        this.cellStyleImpl.setBorderBottom(bool);
    }

    @Override // com.jakewharton.picnic.CellStyleDsl
    public void setBorderLeft(Boolean bool) {
        this.cellStyleImpl.setBorderLeft(bool);
    }

    @Override // com.jakewharton.picnic.CellStyleDsl
    public void setBorderRight(Boolean bool) {
        this.cellStyleImpl.setBorderRight(bool);
    }

    @Override // com.jakewharton.picnic.CellStyleDsl
    public void setBorderTop(Boolean bool) {
        this.cellStyleImpl.setBorderTop(bool);
    }

    @Override // com.jakewharton.picnic.CellStyleDsl
    public void setPaddingLeft(Integer num) {
        this.cellStyleImpl.setPaddingLeft(num);
    }

    @Override // com.jakewharton.picnic.CellStyleDsl
    public void setPaddingRight(Integer num) {
        this.cellStyleImpl.setPaddingRight(num);
    }

    public CellDslImpl(Object obj, CellStyleDslImpl cellStyleDslImpl) {
        this.content = obj;
        this.cellStyleImpl = cellStyleDslImpl;
        this.columnSpan = 1;
        this.rowSpan = 1;
    }

    public CellDslImpl(Object obj) {
        this(obj, new CellStyleDslImpl());
    }

    public int getColumnSpan() {
        return this.columnSpan;
    }

    @Override // com.jakewharton.picnic.CellDsl
    public void setColumnSpan(int i) {
        this.columnSpan = i;
    }

    public int getRowSpan() {
        return this.rowSpan;
    }

    public final Cell create() {
        String str;
        Object obj = this.content;
        if (obj == null || (str = obj.toString()) == null) {
            str = "";
        }
        return ModelKt.Cell(str, new CellDslImpl$create$1(this));
    }
}
