package com.jakewharton.picnic;

import com.jakewharton.picnic.CellStyle;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: dsl.kt */
/* loaded from: classes.dex */
public final class CellStyleDslImpl$createOrNull$1 extends Lambda implements Function1<CellStyle.Builder, Unit> {
    public final /* synthetic */ CellStyleDslImpl this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CellStyleDslImpl$createOrNull$1(CellStyleDslImpl cellStyleDslImpl) {
        super(1);
        this.this$0 = cellStyleDslImpl;
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
        receiver.setPaddingLeft(this.this$0.getPaddingLeft());
        receiver.setPaddingRight(this.this$0.getPaddingRight());
        receiver.setPaddingTop(this.this$0.getPaddingTop());
        receiver.setPaddingBottom(this.this$0.getPaddingBottom());
        receiver.setBorderLeft(this.this$0.getBorderLeft());
        receiver.setBorderRight(this.this$0.getBorderRight());
        receiver.setBorderTop(this.this$0.getBorderTop());
        receiver.setBorderBottom(this.this$0.getBorderBottom());
        receiver.setAlignment(this.this$0.getAlignment());
    }
}
