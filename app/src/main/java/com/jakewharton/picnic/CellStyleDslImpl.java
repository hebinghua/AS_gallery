package com.jakewharton.picnic;

/* compiled from: dsl.kt */
/* loaded from: classes.dex */
public final class CellStyleDslImpl implements CellStyleDsl {
    public TextAlignment alignment;
    public Boolean borderBottom;
    public Boolean borderLeft;
    public Boolean borderRight;
    public Boolean borderTop;
    public Integer paddingBottom;
    public Integer paddingLeft;
    public Integer paddingRight;
    public Integer paddingTop;

    public Integer getPaddingLeft() {
        return this.paddingLeft;
    }

    @Override // com.jakewharton.picnic.CellStyleDsl
    public void setPaddingLeft(Integer num) {
        this.paddingLeft = num;
    }

    public Integer getPaddingRight() {
        return this.paddingRight;
    }

    @Override // com.jakewharton.picnic.CellStyleDsl
    public void setPaddingRight(Integer num) {
        this.paddingRight = num;
    }

    public Integer getPaddingTop() {
        return this.paddingTop;
    }

    public Integer getPaddingBottom() {
        return this.paddingBottom;
    }

    public Boolean getBorderLeft() {
        return this.borderLeft;
    }

    @Override // com.jakewharton.picnic.CellStyleDsl
    public void setBorderLeft(Boolean bool) {
        this.borderLeft = bool;
    }

    public Boolean getBorderRight() {
        return this.borderRight;
    }

    @Override // com.jakewharton.picnic.CellStyleDsl
    public void setBorderRight(Boolean bool) {
        this.borderRight = bool;
    }

    public Boolean getBorderTop() {
        return this.borderTop;
    }

    @Override // com.jakewharton.picnic.CellStyleDsl
    public void setBorderTop(Boolean bool) {
        this.borderTop = bool;
    }

    public Boolean getBorderBottom() {
        return this.borderBottom;
    }

    @Override // com.jakewharton.picnic.CellStyleDsl
    public void setBorderBottom(Boolean bool) {
        this.borderBottom = bool;
    }

    public TextAlignment getAlignment() {
        return this.alignment;
    }

    @Override // com.jakewharton.picnic.CellStyleDsl
    public void setAlignment(TextAlignment textAlignment) {
        this.alignment = textAlignment;
    }

    public final CellStyle createOrNull() {
        if (getPaddingLeft() == null && getPaddingRight() == null && getPaddingTop() == null && getPaddingBottom() == null && getBorderLeft() == null && getBorderRight() == null && getBorderTop() == null && getBorderBottom() == null && getAlignment() == null) {
            return null;
        }
        return ModelKt.CellStyle(new CellStyleDslImpl$createOrNull$1(this));
    }
}
