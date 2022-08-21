package com.miui.gallery.storage.strategies.android30;

import kotlin.Triple;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: RExtendedStorageOperator.kt */
/* loaded from: classes2.dex */
public final class RExtendedStorageOperator$moveActions$2 extends Lambda implements Function1<Triple<? extends String, ? extends String, ? extends String>, Boolean> {
    public final /* synthetic */ RExtendedStorageOperator this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public RExtendedStorageOperator$moveActions$2(RExtendedStorageOperator rExtendedStorageOperator) {
        super(1);
        this.this$0 = rExtendedStorageOperator;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Boolean mo2577invoke(Triple<? extends String, ? extends String, ? extends String> triple) {
        return invoke2((Triple<String, String, String>) triple);
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Boolean invoke2(Triple<String, String, String> params) {
        boolean moveFileInner2;
        Intrinsics.checkNotNullParameter(params, "params");
        moveFileInner2 = this.this$0.moveFileInner2(params.getFirst(), params.getSecond(), params.getThird());
        return Boolean.valueOf(moveFileInner2);
    }
}
