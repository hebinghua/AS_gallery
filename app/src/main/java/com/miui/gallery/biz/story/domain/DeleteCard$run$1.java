package com.miui.gallery.biz.story.domain;

import com.miui.gallery.arch.function.Either;
import com.miui.gallery.arch.interactor.UseCase;
import com.miui.gallery.card.Card;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

/* compiled from: DeleteCard.kt */
@DebugMetadata(c = "com.miui.gallery.biz.story.domain.DeleteCard", f = "DeleteCard.kt", l = {17}, m = "run")
/* loaded from: classes.dex */
public final class DeleteCard$run$1 extends ContinuationImpl {
    public Object L$0;
    public int label;
    public /* synthetic */ Object result;
    public final /* synthetic */ DeleteCard this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DeleteCard$run$1(DeleteCard deleteCard, Continuation<? super DeleteCard$run$1> continuation) {
        super(continuation);
        this.this$0 = deleteCard;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return this.this$0.run2((Card) null, (Continuation<? super Either<Object, UseCase.None>>) this);
    }
}
