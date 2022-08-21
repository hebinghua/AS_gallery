package com.miui.gallery.biz.story;

import com.miui.gallery.biz.story.domain.DeleteCard;
import com.miui.gallery.card.Card;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: StoryAlbumViewModel.kt */
@DebugMetadata(c = "com.miui.gallery.biz.story.StoryAlbumViewModel$deleteCard$1$1", f = "StoryAlbumViewModel.kt", l = {183}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class StoryAlbumViewModel$deleteCard$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Card $it;
    public int label;
    public final /* synthetic */ StoryAlbumViewModel this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StoryAlbumViewModel$deleteCard$1$1(StoryAlbumViewModel storyAlbumViewModel, Card card, Continuation<? super StoryAlbumViewModel$deleteCard$1$1> continuation) {
        super(2, continuation);
        this.this$0 = storyAlbumViewModel;
        this.$it = card;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new StoryAlbumViewModel$deleteCard$1$1(this.this$0, this.$it, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((StoryAlbumViewModel$deleteCard$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        DeleteCard deleteCard;
        Object coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            deleteCard = this.this$0.deleteCard;
            Card it = this.$it;
            Intrinsics.checkNotNullExpressionValue(it, "it");
            this.label = 1;
            if (deleteCard.invoke(it, this) == coroutine_suspended) {
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
