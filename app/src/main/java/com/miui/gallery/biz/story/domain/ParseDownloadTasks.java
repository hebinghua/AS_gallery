package com.miui.gallery.biz.story.domain;

import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.arch.function.Either;
import com.miui.gallery.arch.interactor.UseCase;
import com.miui.gallery.biz.story.data.MediaInfo;
import com.miui.gallery.picker.uri.Downloader;
import com.miui.gallery.util.concurrent.CoroutineDispatcherProvider;
import java.util.List;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;

/* compiled from: ParseDownloadTasks.kt */
/* loaded from: classes.dex */
public final class ParseDownloadTasks extends UseCase<List<? extends Downloader.DownloadTask>, Params> {
    public static final Companion Companion = new Companion(null);
    public final CoroutineDispatcherProvider dispatchers;

    public ParseDownloadTasks(CoroutineDispatcherProvider dispatchers) {
        Intrinsics.checkNotNullParameter(dispatchers, "dispatchers");
        this.dispatchers = dispatchers;
    }

    @Override // com.miui.gallery.arch.interactor.UseCase
    public Object run(Params params, Continuation<? super Either<Object, ? extends List<? extends Downloader.DownloadTask>>> continuation) {
        return BuildersKt.withContext(this.dispatchers.getIo(), new ParseDownloadTasks$run$2(params, null), continuation);
    }

    /* compiled from: ParseDownloadTasks.kt */
    /* loaded from: classes.dex */
    public static final class Params {
        public final boolean isShowVideo;
        public final List<MediaInfo> medias;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Params)) {
                return false;
            }
            Params params = (Params) obj;
            return Intrinsics.areEqual(this.medias, params.medias) && this.isShowVideo == params.isShowVideo;
        }

        public int hashCode() {
            int hashCode = this.medias.hashCode() * 31;
            boolean z = this.isShowVideo;
            if (z) {
                z = true;
            }
            int i = z ? 1 : 0;
            int i2 = z ? 1 : 0;
            return hashCode + i;
        }

        public String toString() {
            return "Params(medias=" + this.medias + ", isShowVideo=" + this.isShowVideo + CoreConstants.RIGHT_PARENTHESIS_CHAR;
        }

        public Params(List<MediaInfo> medias, boolean z) {
            Intrinsics.checkNotNullParameter(medias, "medias");
            this.medias = medias;
            this.isShowVideo = z;
        }

        public final List<MediaInfo> getMedias() {
            return this.medias;
        }

        public final boolean isShowVideo() {
            return this.isShowVideo;
        }
    }

    /* compiled from: ParseDownloadTasks.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
