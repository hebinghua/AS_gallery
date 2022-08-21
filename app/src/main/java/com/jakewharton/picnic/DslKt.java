package com.jakewharton.picnic;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: dsl.kt */
/* renamed from: com.jakewharton.picnic.-DslKt  reason: invalid class name */
/* loaded from: classes.dex */
public final class DslKt {
    public static final Table table(Function1<? super TableDsl, Unit> content) {
        Intrinsics.checkNotNullParameter(content, "content");
        TableDslImpl tableDslImpl = new TableDslImpl();
        content.mo2577invoke(tableDslImpl);
        return tableDslImpl.create();
    }
}
