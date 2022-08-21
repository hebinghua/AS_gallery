package com.jakewharton.picnic;

import com.jakewharton.picnic.Table;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: textRender.kt */
/* loaded from: classes.dex */
public final /* synthetic */ class TextRendering$renderText$1 extends FunctionReferenceImpl implements Function1<Table.PositionedCell, SimpleLayout> {
    public static final TextRendering$renderText$1 INSTANCE = new TextRendering$renderText$1();

    public TextRendering$renderText$1() {
        super(1, SimpleLayout.class, "<init>", "<init>(Lcom/jakewharton/picnic/Table$PositionedCell;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final SimpleLayout mo2577invoke(Table.PositionedCell p1) {
        Intrinsics.checkNotNullParameter(p1, "p1");
        return new SimpleLayout(p1);
    }
}
