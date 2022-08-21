package com.miui.gallery.scanner.extra.genthumbnail;

import android.text.TextUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.glide.util.GlideLoadingUtils;
import com.miui.gallery.util.ProcessingMediaHelper;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.Locale;

/* loaded from: classes2.dex */
public class GenMicroThumbnailTask extends AbsGenThumbnailTask {
    public GenMicroThumbnailTask(String str, long j) {
        super(str, j);
    }

    @Override // com.miui.gallery.scanner.extra.genthumbnail.AbsGenThumbnailTask
    public void request(ThreadPool.JobContext jobContext) {
        boolean isMediaInProcessing = ProcessingMediaHelper.getInstance().isMediaInProcessing(Scheme.FILE.wrap(this.mPath));
        DefaultLogger.d("GenMicroThumbnailTask", "genMicroThumbnail path: %s, isTemp: %s", this.mPath, Boolean.valueOf(isMediaInProcessing));
        if (isMediaInProcessing) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        GlideLoadingUtils.blockingLoad(Glide.with(GalleryApp.sGetAndroidContext()), GalleryModel.of(this.mPath), GlideOptions.microThumbOf(new File(this.mPath).length()).mo978skipMemoryCache(true).mo950diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).mo974priority(Priority.LOW));
        DefaultLogger.d("GenMicroThumbnailTask", "genMicroThumbnail: decode %s, cost %d", this.mPath, Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
    }

    public boolean equals(Object obj) {
        if (obj instanceof GenMicroThumbnailTask) {
            return TextUtils.equals(generateKey(), ((GenMicroThumbnailTask) obj).generateKey());
        }
        return false;
    }

    public int hashCode() {
        return TextUtils.isEmpty(this.mPath) ? super.hashCode() : generateKey().hashCode();
    }

    public final String generateKey() {
        if (TextUtils.isEmpty(this.mPath)) {
            return "";
        }
        return this.mPath + "_L" + new File(this.mPath).length();
    }

    public String toString() {
        return String.format(Locale.US, "GenMicroThumbnailJob: %s", this.mPath);
    }
}
