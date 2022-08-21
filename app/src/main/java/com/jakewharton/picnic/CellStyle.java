package com.jakewharton.picnic;

import ch.qos.logback.core.CoreConstants;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: model.kt */
/* loaded from: classes.dex */
public final class CellStyle {
    public final TextAlignment alignment;
    public final Boolean borderBottom;
    public final Boolean borderLeft;
    public final Boolean borderRight;
    public final Boolean borderTop;
    public final Integer paddingBottom;
    public final Integer paddingLeft;
    public final Integer paddingRight;
    public final Integer paddingTop;

    public CellStyle(Integer num, Integer num2, Integer num3, Integer num4, Boolean bool, Boolean bool2, Boolean bool3, Boolean bool4, TextAlignment textAlignment) {
        this.paddingLeft = num;
        this.paddingRight = num2;
        this.paddingTop = num3;
        this.paddingBottom = num4;
        this.borderLeft = bool;
        this.borderRight = bool2;
        this.borderTop = bool3;
        this.borderBottom = bool4;
        this.alignment = textAlignment;
    }

    public /* synthetic */ CellStyle(Integer num, Integer num2, Integer num3, Integer num4, Boolean bool, Boolean bool2, Boolean bool3, Boolean bool4, TextAlignment textAlignment, DefaultConstructorMarker defaultConstructorMarker) {
        this(num, num2, num3, num4, bool, bool2, bool3, bool4, textAlignment);
    }

    public final Integer getPaddingLeft() {
        return this.paddingLeft;
    }

    public final Integer getPaddingRight() {
        return this.paddingRight;
    }

    public final Integer getPaddingTop() {
        return this.paddingTop;
    }

    public final Integer getPaddingBottom() {
        return this.paddingBottom;
    }

    public final Boolean getBorderLeft() {
        return this.borderLeft;
    }

    public final Boolean getBorderRight() {
        return this.borderRight;
    }

    public final Boolean getBorderTop() {
        return this.borderTop;
    }

    public final Boolean getBorderBottom() {
        return this.borderBottom;
    }

    public final TextAlignment getAlignment() {
        return this.alignment;
    }

    public String toString() {
        return "CellStyle(padding(l=" + this.paddingLeft + ",r=" + this.paddingRight + ",t=" + this.paddingTop + ",b=" + this.paddingBottom + "), border(l=" + this.borderLeft + ",r=" + this.borderRight + ",t=" + this.borderTop + ",b=" + this.borderBottom + "), alignment=" + this.alignment + CoreConstants.RIGHT_PARENTHESIS_CHAR;
    }

    public int hashCode() {
        return Objects.hash(this.paddingLeft, this.paddingRight, this.paddingTop, this.paddingBottom, this.borderLeft, this.borderRight, this.borderTop, this.borderBottom, this.alignment);
    }

    public boolean equals(Object obj) {
        if (obj instanceof CellStyle) {
            CellStyle cellStyle = (CellStyle) obj;
            if (Intrinsics.areEqual(this.paddingLeft, cellStyle.paddingLeft) && Intrinsics.areEqual(this.paddingRight, cellStyle.paddingRight) && Intrinsics.areEqual(this.paddingTop, cellStyle.paddingTop) && Intrinsics.areEqual(this.paddingBottom, cellStyle.paddingBottom) && Intrinsics.areEqual(this.borderLeft, cellStyle.borderLeft) && Intrinsics.areEqual(this.borderRight, cellStyle.borderRight) && Intrinsics.areEqual(this.borderTop, cellStyle.borderTop) && Intrinsics.areEqual(this.borderBottom, cellStyle.borderBottom) && this.alignment == cellStyle.alignment) {
                return true;
            }
        }
        return false;
    }

    /* compiled from: model.kt */
    /* loaded from: classes.dex */
    public static final class Builder {
        public TextAlignment alignment;
        public Boolean borderBottom;
        public Boolean borderLeft;
        public Boolean borderRight;
        public Boolean borderTop;
        public Integer paddingBottom;
        public Integer paddingLeft;
        public Integer paddingRight;
        public Integer paddingTop;

        public final /* synthetic */ void setPaddingLeft(Integer num) {
            this.paddingLeft = num;
        }

        public final /* synthetic */ void setPaddingRight(Integer num) {
            this.paddingRight = num;
        }

        public final /* synthetic */ void setPaddingTop(Integer num) {
            this.paddingTop = num;
        }

        public final /* synthetic */ void setPaddingBottom(Integer num) {
            this.paddingBottom = num;
        }

        public final /* synthetic */ void setBorderLeft(Boolean bool) {
            this.borderLeft = bool;
        }

        public final /* synthetic */ void setBorderRight(Boolean bool) {
            this.borderRight = bool;
        }

        public final /* synthetic */ void setBorderTop(Boolean bool) {
            this.borderTop = bool;
        }

        public final /* synthetic */ void setBorderBottom(Boolean bool) {
            this.borderBottom = bool;
        }

        public final /* synthetic */ void setAlignment(TextAlignment textAlignment) {
            this.alignment = textAlignment;
        }

        public final CellStyle build() {
            return new CellStyle(this.paddingLeft, this.paddingRight, this.paddingTop, this.paddingBottom, this.borderLeft, this.borderRight, this.borderTop, this.borderBottom, this.alignment, null);
        }
    }
}
