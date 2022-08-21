package com.jakewharton.picnic;

import com.jakewharton.picnic.TableStyle;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: dsl.kt */
/* loaded from: classes.dex */
public final class TableStyleDslImpl$createOrNull$1 extends Lambda implements Function1<TableStyle.Builder, Unit> {
    public final /* synthetic */ TableStyleDslImpl this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TableStyleDslImpl$createOrNull$1(TableStyleDslImpl tableStyleDslImpl) {
        super(1);
        this.this$0 = tableStyleDslImpl;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo2577invoke(TableStyle.Builder builder) {
        invoke2(builder);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(TableStyle.Builder receiver) {
        Intrinsics.checkNotNullParameter(receiver, "$receiver");
        receiver.setBorder(this.this$0.getBorder());
        receiver.setBorderStyle(this.this$0.getBorderStyle());
    }
}
