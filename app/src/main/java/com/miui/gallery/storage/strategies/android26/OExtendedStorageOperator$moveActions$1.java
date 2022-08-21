package com.miui.gallery.storage.strategies.android26;

import kotlin.Triple;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: OExtendedStorageOperator.kt */
/* loaded from: classes2.dex */
public final class OExtendedStorageOperator$moveActions$1 extends Lambda implements Function1<Triple<? extends String, ? extends String, ? extends String>, Boolean> {
    public final /* synthetic */ OExtendedStorageOperator this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public OExtendedStorageOperator$moveActions$1(OExtendedStorageOperator oExtendedStorageOperator) {
        super(1);
        this.this$0 = oExtendedStorageOperator;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Boolean mo2577invoke(Triple<? extends String, ? extends String, ? extends String> triple) {
        return invoke2((Triple<String, String, String>) triple);
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Boolean invoke2(Triple<String, String, String> params) {
        boolean moveFileInner1;
        Intrinsics.checkNotNullParameter(params, "params");
        moveFileInner1 = this.this$0.moveFileInner1(params.getFirst(), params.getSecond(), params.getThird());
        return Boolean.valueOf(moveFileInner1);
    }
}
