package com.miui.gallery.biz.story.data.source;

import com.miui.gallery.card.Card;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: CardRepositoryImpl.kt */
@DebugMetadata(c = "com.miui.gallery.biz.story.data.source.CardRepositoryImpl$updateCard$2", f = "CardRepositoryImpl.kt", l = {14}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class CardRepositoryImpl$updateCard$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Card $card;
    public int label;
    public final /* synthetic */ CardRepositoryImpl this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CardRepositoryImpl$updateCard$2(CardRepositoryImpl cardRepositoryImpl, Card card, Continuation<? super CardRepositoryImpl$updateCard$2> continuation) {
        super(2, continuation);
        this.this$0 = cardRepositoryImpl;
        this.$card = card;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new CardRepositoryImpl$updateCard$2(this.this$0, this.$card, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((CardRepositoryImpl$updateCard$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        CardDataSource cardDataSource;
        Object coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            cardDataSource = this.this$0.dataSource;
            Card card = this.$card;
            this.label = 1;
            if (cardDataSource.updateCard(card, this) == coroutine_suspended) {
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
