package com.miui.gallery.ui;

import com.miui.gallery.util.logger.DefaultLogger;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: IntroductionPage.kt */
@DebugMetadata(c = "com.miui.gallery.ui.IntroductionPage$scheduleNext$1$1", f = "IntroductionPage.kt", l = {74}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class IntroductionPage$scheduleNext$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ boolean $hasShowAnyIntro;
    public int label;
    public final /* synthetic */ IntroductionPage<HOST, PARAM> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public IntroductionPage$scheduleNext$1$1(IntroductionPage<HOST, PARAM> introductionPage, boolean z, Continuation<? super IntroductionPage$scheduleNext$1$1> continuation) {
        super(2, continuation);
        this.this$0 = introductionPage;
        this.$hasShowAnyIntro = z;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new IntroductionPage$scheduleNext$1$1(this.this$0, this.$hasShowAnyIntro, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((IntroductionPage$scheduleNext$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        IntroductionPage introductionPage;
        IntroductionPage introductionPage2;
        Object coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            if (IntroductionPage.Companion.getDEBUG()) {
                StringBuilder sb = new StringBuilder();
                sb.append("scheduleNext for ");
                introductionPage2 = this.this$0.next;
                sb.append((Object) (introductionPage2 == null ? null : introductionPage2.getClass().getName()));
                sb.append(", hasShowAnyIntro: ");
                sb.append(this.$hasShowAnyIntro);
                DefaultLogger.d("IntroductionPage", sb.toString());
            }
            introductionPage = this.this$0.next;
            if (introductionPage != null) {
                boolean z = this.$hasShowAnyIntro;
                this.label = 1;
                if (introductionPage.checkAndShow(z, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            }
        } else if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            ResultKt.throwOnFailure(obj);
        }
        return Unit.INSTANCE;
    }
}
