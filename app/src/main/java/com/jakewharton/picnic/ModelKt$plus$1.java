package com.jakewharton.picnic;

import com.jakewharton.picnic.CellStyle;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: model.kt */
/* loaded from: classes.dex */
public final class ModelKt$plus$1 extends Lambda implements Function1<CellStyle.Builder, Unit> {
    public final /* synthetic */ CellStyle $override;
    public final /* synthetic */ CellStyle $this_plus;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ModelKt$plus$1(CellStyle cellStyle, CellStyle cellStyle2) {
        super(1);
        this.$this_plus = cellStyle;
        this.$override = cellStyle2;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo2577invoke(CellStyle.Builder builder) {
        invoke2(builder);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(CellStyle.Builder receiver) {
        Intrinsics.checkNotNullParameter(receiver, "$receiver");
        Integer paddingLeft = this.$override.getPaddingLeft();
        if (paddingLeft == null) {
            paddingLeft = this.$this_plus.getPaddingLeft();
        }
        receiver.setPaddingLeft(paddingLeft);
        Integer paddingRight = this.$override.getPaddingRight();
        if (paddingRight == null) {
            paddingRight = this.$this_plus.getPaddingRight();
        }
        receiver.setPaddingRight(paddingRight);
        Integer paddingTop = this.$override.getPaddingTop();
        if (paddingTop == null) {
            paddingTop = this.$this_plus.getPaddingTop();
        }
        receiver.setPaddingTop(paddingTop);
        Integer paddingBottom = this.$override.getPaddingBottom();
        if (paddingBottom == null) {
            paddingBottom = this.$this_plus.getPaddingBottom();
        }
        receiver.setPaddingBottom(paddingBottom);
        Boolean borderLeft = this.$override.getBorderLeft();
        if (borderLeft == null) {
            borderLeft = this.$this_plus.getBorderLeft();
        }
        receiver.setBorderLeft(borderLeft);
        Boolean borderRight = this.$override.getBorderRight();
        if (borderRight == null) {
            borderRight = this.$this_plus.getBorderRight();
        }
        receiver.setBorderRight(borderRight);
        Boolean borderTop = this.$override.getBorderTop();
        if (borderTop == null) {
            borderTop = this.$this_plus.getBorderTop();
        }
        receiver.setBorderTop(borderTop);
        Boolean borderBottom = this.$override.getBorderBottom();
        if (borderBottom == null) {
            borderBottom = this.$this_plus.getBorderBottom();
        }
        receiver.setBorderBottom(borderBottom);
        TextAlignment alignment = this.$override.getAlignment();
        if (alignment == null) {
            alignment = this.$this_plus.getAlignment();
        }
        receiver.setAlignment(alignment);
    }
}
