package com.miui.gallery.ui;

import androidx.fragment.app.Fragment;
import com.miui.gallery.util.logger.DefaultLogger;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* JADX WARN: Incorrect field signature: THOST; */
/* compiled from: IntroductionPage.kt */
@DebugMetadata(c = "com.miui.gallery.ui.IntroductionPage$checkAndShow$2$1$1", f = "IntroductionPage.kt", l = {}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class IntroductionPage$checkAndShow$2$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ boolean $hasShowAnyIntro;
    public final /* synthetic */ Fragment $it;
    public final /* synthetic */ PARAM $param;
    public int label;
    public final /* synthetic */ IntroductionPage<HOST, PARAM> this$0;

    /* compiled from: IntroductionPage.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[ShowResult.values().length];
            iArr[ShowResult.SHOWN_N_CONTINUE.ordinal()] = 1;
            iArr[ShowResult.SKIPPED.ordinal()] = 2;
            iArr[ShowResult.SHOWN_N_WAITING.ordinal()] = 3;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Incorrect types in method signature: (Lcom/miui/gallery/ui/IntroductionPage<THOST;TPARAM;>;THOST;TPARAM;ZLkotlin/coroutines/Continuation<-Lcom/miui/gallery/ui/IntroductionPage$checkAndShow$2$1$1;>;)V */
    /* JADX WARN: Multi-variable type inference failed */
    public IntroductionPage$checkAndShow$2$1$1(IntroductionPage introductionPage, Fragment fragment, Object obj, boolean z, Continuation continuation) {
        super(2, continuation);
        this.this$0 = introductionPage;
        this.$it = fragment;
        this.$param = obj;
        this.$hasShowAnyIntro = z;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new IntroductionPage$checkAndShow$2$1$1(this.this$0, this.$it, this.$param, this.$hasShowAnyIntro, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((IntroductionPage$checkAndShow$2$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label == 0) {
            ResultKt.throwOnFailure(obj);
            ShowResult show = this.this$0.show(this.$it, this.$param);
            int i = WhenMappings.$EnumSwitchMapping$0[show.ordinal()];
            if (i == 1) {
                this.this$0.scheduleNext(true);
            } else if (i == 2) {
                this.this$0.scheduleNext(this.$hasShowAnyIntro);
            }
            if (IntroductionPage.Companion.getDEBUG()) {
                DefaultLogger.d("IntroductionPage", "show for " + ((Object) this.this$0.getClass().getName()) + ": " + show.name());
            }
            return Unit.INSTANCE;
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
}
