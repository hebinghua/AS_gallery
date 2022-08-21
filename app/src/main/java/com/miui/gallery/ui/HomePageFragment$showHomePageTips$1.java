package com.miui.gallery.ui;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: HomePageFragment.kt */
@DebugMetadata(c = "com.miui.gallery.ui.HomePageFragment$showHomePageTips$1", f = "HomePageFragment.kt", l = {1580}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class HomePageFragment$showHomePageTips$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ boolean $hasShowCta;
    public final /* synthetic */ IntroductionPage<HomePageFragment, ? extends Object> $head;
    public int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HomePageFragment$showHomePageTips$1(IntroductionPage<HomePageFragment, ? extends Object> introductionPage, boolean z, Continuation<? super HomePageFragment$showHomePageTips$1> continuation) {
        super(2, continuation);
        this.$head = introductionPage;
        this.$hasShowCta = z;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new HomePageFragment$showHomePageTips$1(this.$head, this.$hasShowCta, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((HomePageFragment$showHomePageTips$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            IntroductionPage<HomePageFragment, ? extends Object> introductionPage = this.$head;
            boolean z = this.$hasShowCta;
            this.label = 1;
            if (introductionPage.checkAndShow(z, this) == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            ResultKt.throwOnFailure(obj);
        }
        return Unit.INSTANCE;
    }
}
