package com.jakewharton.picnic;

/* compiled from: dsl.kt */
/* loaded from: classes.dex */
public final class TableStyleDslImpl implements TableStyleDsl {
    public Boolean border;
    public BorderStyle borderStyle;

    public Boolean getBorder() {
        return this.border;
    }

    public BorderStyle getBorderStyle() {
        return this.borderStyle;
    }

    @Override // com.jakewharton.picnic.TableStyleDsl
    public void setBorderStyle(BorderStyle borderStyle) {
        this.borderStyle = borderStyle;
    }

    public final TableStyle createOrNull() {
        if (getBorder() == null && getBorderStyle() == null) {
            return null;
        }
        return ModelKt.TableStyle(new TableStyleDslImpl$createOrNull$1(this));
    }
}
