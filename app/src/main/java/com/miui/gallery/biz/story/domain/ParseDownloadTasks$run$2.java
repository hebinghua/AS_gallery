package com.miui.gallery.biz.story.domain;

import android.net.Uri;
import com.miui.gallery.arch.function.Either;
import com.miui.gallery.biz.story.data.MediaInfo;
import com.miui.gallery.biz.story.domain.ParseDownloadTasks;
import com.miui.gallery.picker.uri.Downloader;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: ParseDownloadTasks.kt */
@DebugMetadata(c = "com.miui.gallery.biz.story.domain.ParseDownloadTasks$run$2", f = "ParseDownloadTasks.kt", l = {}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class ParseDownloadTasks$run$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Either.Right<? extends List<Downloader.DownloadTask>>>, Object> {
    public final /* synthetic */ ParseDownloadTasks.Params $params;
    public int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ParseDownloadTasks$run$2(ParseDownloadTasks.Params params, Continuation<? super ParseDownloadTasks$run$2> continuation) {
        super(2, continuation);
        this.$params = params;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new ParseDownloadTasks$run$2(this.$params, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Either.Right<? extends List<Downloader.DownloadTask>>> continuation) {
        return ((ParseDownloadTasks$run$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label == 0) {
            ResultKt.throwOnFailure(obj);
            ArrayList arrayList = new ArrayList();
            int i = 0;
            for (MediaInfo mediaInfo : this.$params.getMedias()) {
                String filePath = mediaInfo.getFilePath();
                boolean z = true;
                if ((filePath == null || filePath.length() == 0) || !new File(filePath).exists()) {
                    if (this.$params.isShowVideo()) {
                        if (mediaInfo.isVideo()) {
                            arrayList.add(new Downloader.DownloadTask(CloudUriAdapter.getDownloadUri(mediaInfo.getId()), DownloadType.ORIGIN_FORCE, 0, i));
                            i++;
                        }
                    } else if (!mediaInfo.isVideo()) {
                        String thumbPath = mediaInfo.getThumbPath();
                        if (thumbPath != null && thumbPath.length() != 0) {
                            z = false;
                        }
                        if (z || !new File(thumbPath).exists()) {
                            Uri downloadUri = CloudUriAdapter.getDownloadUri(mediaInfo.getId());
                            arrayList.add(new Downloader.DownloadTask(downloadUri, DownloadType.THUMBNAIL, 0, i));
                            DefaultLogger.d("ParseDownloadTasks", "Add " + downloadUri + " to download list");
                            i++;
                        }
                    }
                }
            }
            DefaultLogger.d("ParseDownloadTasks", "Check end, " + arrayList.size() + " files need download.");
            return new Either.Right(arrayList);
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
}
