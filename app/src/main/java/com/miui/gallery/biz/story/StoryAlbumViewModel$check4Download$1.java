package com.miui.gallery.biz.story;

import androidx.lifecycle.MutableLiveData;
import com.miui.gallery.arch.function.Either;
import com.miui.gallery.arch.function.EitherKt;
import com.miui.gallery.biz.story.data.DownloadCommand;
import com.miui.gallery.biz.story.data.MediaInfo;
import com.miui.gallery.biz.story.domain.ParseDownloadTasks;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: StoryAlbumViewModel.kt */
@DebugMetadata(c = "com.miui.gallery.biz.story.StoryAlbumViewModel$check4Download$1", f = "StoryAlbumViewModel.kt", l = {220}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class StoryAlbumViewModel$check4Download$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ boolean $isShowVideo;
    private /* synthetic */ Object L$0;
    public Object L$1;
    public int label;
    public final /* synthetic */ StoryAlbumViewModel this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StoryAlbumViewModel$check4Download$1(StoryAlbumViewModel storyAlbumViewModel, boolean z, Continuation<? super StoryAlbumViewModel$check4Download$1> continuation) {
        super(2, continuation);
        this.this$0 = storyAlbumViewModel;
        this.$isShowVideo = z;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        StoryAlbumViewModel$check4Download$1 storyAlbumViewModel$check4Download$1 = new StoryAlbumViewModel$check4Download$1(this.this$0, this.$isShowVideo, continuation);
        storyAlbumViewModel$check4Download$1.L$0 = obj;
        return storyAlbumViewModel$check4Download$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((StoryAlbumViewModel$check4Download$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        ParseDownloadTasks parseDownloadTasks;
        StoryAlbumViewModel storyAlbumViewModel;
        MutableLiveData mutableLiveData;
        Object coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            CoroutineScope coroutineScope = (CoroutineScope) this.L$0;
            List<MediaInfo> value = this.this$0.getMedias().getValue();
            if (value != null) {
                StoryAlbumViewModel storyAlbumViewModel2 = this.this$0;
                boolean z = this.$isShowVideo;
                parseDownloadTasks = storyAlbumViewModel2.parseDownloadTasks;
                ParseDownloadTasks.Params params = new ParseDownloadTasks.Params(value, z);
                this.L$0 = coroutineScope;
                this.L$1 = storyAlbumViewModel2;
                this.label = 1;
                obj = parseDownloadTasks.invoke(params, this);
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
                storyAlbumViewModel = storyAlbumViewModel2;
            }
            return Unit.INSTANCE;
        } else if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            storyAlbumViewModel = (StoryAlbumViewModel) this.L$1;
            CoroutineScope coroutineScope2 = (CoroutineScope) this.L$0;
            ResultKt.throwOnFailure(obj);
        }
        Unit unit = null;
        List list = (List) EitherKt.getOrElse((Either) obj, null);
        if (list != null) {
            mutableLiveData = storyAlbumViewModel._downloadCommands;
            mutableLiveData.setValue(new DownloadCommand(false, list));
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            DefaultLogger.w("StoryAlbumViewModel", "Failed to check download");
        }
        return Unit.INSTANCE;
    }
}
