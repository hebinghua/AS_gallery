package com.jakewharton.picnic;

import ch.qos.logback.core.CoreConstants;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: model.kt */
/* loaded from: classes.dex */
public final class TableStyle {
    public final Boolean border;
    public final BorderStyle borderStyle;

    public TableStyle(Boolean bool, BorderStyle borderStyle) {
        this.border = bool;
        this.borderStyle = borderStyle;
    }

    public /* synthetic */ TableStyle(Boolean bool, BorderStyle borderStyle, DefaultConstructorMarker defaultConstructorMarker) {
        this(bool, borderStyle);
    }

    public final Boolean getBorder() {
        return this.border;
    }

    public final BorderStyle getBorderStyle() {
        return this.borderStyle;
    }

    public String toString() {
        return "TableStyle(border=" + this.border + ", borderStyle=" + this.borderStyle + CoreConstants.RIGHT_PARENTHESIS_CHAR;
    }

    public int hashCode() {
        Boolean bool = this.border;
        int i = 0;
        int hashCode = (bool != null ? bool.hashCode() : 0) * 37;
        BorderStyle borderStyle = this.borderStyle;
        if (borderStyle != null) {
            i = borderStyle.hashCode();
        }
        return hashCode + i;
    }

    public boolean equals(Object obj) {
        if (obj instanceof TableStyle) {
            TableStyle tableStyle = (TableStyle) obj;
            if (Intrinsics.areEqual(this.border, tableStyle.border) && this.borderStyle == tableStyle.borderStyle) {
                return true;
            }
        }
        return false;
    }

    /* compiled from: model.kt */
    /* loaded from: classes.dex */
    public static final class Builder {
        public Boolean border;
        public BorderStyle borderStyle;

        public final /* synthetic */ void setBorder(Boolean bool) {
            this.border = bool;
        }

        public final /* synthetic */ void setBorderStyle(BorderStyle borderStyle) {
            this.borderStyle = borderStyle;
        }

        public final TableStyle build() {
            return new TableStyle(this.border, this.borderStyle, null);
        }
    }
}
