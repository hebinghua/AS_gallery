package com.miui.gallery.adapter;

import com.miui.gallery.ui.pictures.PictureViewMode;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: AlbumDetailAdapter2.kt */
@DebugMetadata(c = "com.miui.gallery.adapter.AlbumDetailAdapter2$setViewMode$1", f = "AlbumDetailAdapter2.kt", l = {412}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class AlbumDetailAdapter2$setViewMode$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ PictureViewMode $viewMode;
    public int label;
    public final /* synthetic */ AlbumDetailAdapter2 this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AlbumDetailAdapter2$setViewMode$1(AlbumDetailAdapter2 albumDetailAdapter2, PictureViewMode pictureViewMode, Continuation<? super AlbumDetailAdapter2$setViewMode$1> continuation) {
        super(2, continuation);
        this.this$0 = albumDetailAdapter2;
        this.$viewMode = pictureViewMode;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AlbumDetailAdapter2$setViewMode$1(this.this$0, this.$viewMode, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AlbumDetailAdapter2$setViewMode$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            AlbumDetailAdapter2 albumDetailAdapter2 = this.this$0;
            this.label = 1;
            if (ListMultiViewMediaAdapter.reselectViewMode$default(albumDetailAdapter2, this.$viewMode, !albumDetailAdapter2.isShowTimeLine(), null, this, 4, null) == coroutine_suspended) {
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
