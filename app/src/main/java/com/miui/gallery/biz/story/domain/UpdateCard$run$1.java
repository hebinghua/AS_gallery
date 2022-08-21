package com.miui.gallery.biz.story.domain;

import com.miui.gallery.arch.function.Either;
import com.miui.gallery.arch.interactor.UseCase;
import com.miui.gallery.biz.story.domain.UpdateCard;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

/* compiled from: UpdateCard.kt */
@DebugMetadata(c = "com.miui.gallery.biz.story.domain.UpdateCard", f = "UpdateCard.kt", l = {51}, m = "run")
/* loaded from: classes.dex */
public final class UpdateCard$run$1 extends ContinuationImpl {
    public int label;
    public /* synthetic */ Object result;
    public final /* synthetic */ UpdateCard this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public UpdateCard$run$1(UpdateCard updateCard, Continuation<? super UpdateCard$run$1> continuation) {
        super(continuation);
        this.this$0 = updateCard;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return this.this$0.run2((UpdateCard.Params) null, (Continuation<? super Either<Object, UseCase.None>>) this);
    }
}
