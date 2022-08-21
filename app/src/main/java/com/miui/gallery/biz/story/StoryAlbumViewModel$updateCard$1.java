package com.miui.gallery.biz.story;

import ch.qos.logback.core.net.SyslogConstants;
import com.miui.gallery.biz.story.domain.UpdateCard;
import com.miui.gallery.card.Card;
import java.util.Set;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: StoryAlbumViewModel.kt */
@DebugMetadata(c = "com.miui.gallery.biz.story.StoryAlbumViewModel$updateCard$1", f = "StoryAlbumViewModel.kt", l = {SyslogConstants.LOG_LOCAL5}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class StoryAlbumViewModel$updateCard$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Card $card;
    public final /* synthetic */ boolean $forceUpdate;
    public int label;
    public final /* synthetic */ StoryAlbumViewModel this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StoryAlbumViewModel$updateCard$1(StoryAlbumViewModel storyAlbumViewModel, Card card, boolean z, Continuation<? super StoryAlbumViewModel$updateCard$1> continuation) {
        super(2, continuation);
        this.this$0 = storyAlbumViewModel;
        this.$card = card;
        this.$forceUpdate = z;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new StoryAlbumViewModel$updateCard$1(this.this$0, this.$card, this.$forceUpdate, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((StoryAlbumViewModel$updateCard$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        UpdateCard updateCard;
        Set set;
        Object coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            updateCard = this.this$0.updateCard;
            Card card = this.$card;
            set = this.this$0.sha1OfSelectedMedias;
            UpdateCard.Params params = new UpdateCard.Params(card, set, this.$forceUpdate);
            this.label = 1;
            if (updateCard.invoke(params, this) == coroutine_suspended) {
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
