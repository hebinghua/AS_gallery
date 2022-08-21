package com.miui.gallery.ui;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: IntroductionPage.kt */
@DebugMetadata(c = "com.miui.gallery.ui.IntroductionPage$checkAndShow$2$param$1", f = "IntroductionPage.kt", l = {42}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class IntroductionPage$checkAndShow$2$param$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super PARAM>, Object> {
    public int label;
    public final /* synthetic */ IntroductionPage<HOST, PARAM> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public IntroductionPage$checkAndShow$2$param$1(IntroductionPage<HOST, PARAM> introductionPage, Continuation<? super IntroductionPage$checkAndShow$2$param$1> continuation) {
        super(2, continuation);
        this.this$0 = introductionPage;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new IntroductionPage$checkAndShow$2$param$1(this.this$0, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super PARAM> continuation) {
        return ((IntroductionPage$checkAndShow$2$param$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            IntroductionPage<HOST, PARAM> introductionPage = this.this$0;
            this.label = 1;
            obj = introductionPage.prepareInBackground(this);
            if (obj == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            ResultKt.throwOnFailure(obj);
        }
        return obj;
    }
}
