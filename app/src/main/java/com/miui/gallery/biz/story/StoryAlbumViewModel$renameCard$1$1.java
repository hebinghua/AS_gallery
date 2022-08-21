package com.miui.gallery.biz.story;

import ch.qos.logback.core.net.SyslogConstants;
import com.miui.gallery.biz.story.domain.RenameCard;
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
@DebugMetadata(c = "com.miui.gallery.biz.story.StoryAlbumViewModel$renameCard$1$1", f = "StoryAlbumViewModel.kt", l = {SyslogConstants.LOG_LOCAL6}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class StoryAlbumViewModel$renameCard$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Card $it;
    public final /* synthetic */ String $newName;
    public int label;
    public final /* synthetic */ StoryAlbumViewModel this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StoryAlbumViewModel$renameCard$1$1(Card card, String str, StoryAlbumViewModel storyAlbumViewModel, Continuation<? super StoryAlbumViewModel$renameCard$1$1> continuation) {
        super(2, continuation);
        this.$it = card;
        this.$newName = str;
        this.this$0 = storyAlbumViewModel;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new StoryAlbumViewModel$renameCard$1$1(this.$it, this.$newName, this.this$0, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((StoryAlbumViewModel$renameCard$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        RenameCard renameCard;
        Object coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            this.$it.setTitle(this.$newName);
            renameCard = this.this$0.renameCard;
            Card it = this.$it;
            Intrinsics.checkNotNullExpressionValue(it, "it");
            this.label = 1;
            if (renameCard.invoke(it, this) == coroutine_suspended) {
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
