package com.miui.gallery.video.compress;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.util.OptionalResult;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/* loaded from: classes2.dex */
public class VideoCompressLibraryLoaderHelper {
    public static VideoCompressLibraryLoaderHelper sInstance = new VideoCompressLibraryLoaderHelper();
    public VideoCompressDownloadStateListener mDownloadStateListener;
    public boolean mIsDownloading;

    /* renamed from: $r8$lambda$vL_LqB3PJchOzCCnmRSwRmHLU-4 */
    public static /* synthetic */ void m1748$r8$lambda$vL_LqB3PJchOzCCnmRSwRmHLU4(VideoCompressLibraryLoaderHelper videoCompressLibraryLoaderHelper, boolean z, OptionalResult optionalResult) {
        videoCompressLibraryLoaderHelper.lambda$startDownload$1(z, optionalResult);
    }

    public static VideoCompressLibraryLoaderHelper getInstance() {
        return sInstance;
    }

    public final int getLoaderState() {
        Library library = LibraryManager.getInstance().getLibrary(1043L);
        if (library == null || library.getLibraryStatus() != Library.LibraryStatus.STATE_AVAILABLE) {
            return this.mIsDownloading ? 1 : 2;
        }
        return 0;
    }

    @SuppressLint({"CheckResult"})
    public void startDownload(final boolean z) {
        refreshDownloadStart();
        Library library = LibraryManager.getInstance().getLibrary(1043L);
        if (library == null) {
            Observable.create(VideoCompressLibraryLoaderHelper$$ExternalSyntheticLambda0.INSTANCE).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.video.compress.VideoCompressLibraryLoaderHelper$$ExternalSyntheticLambda1
                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    VideoCompressLibraryLoaderHelper.m1748$r8$lambda$vL_LqB3PJchOzCCnmRSwRmHLU4(VideoCompressLibraryLoaderHelper.this, z, (OptionalResult) obj);
                }
            });
        } else {
            doDownloadLibrary(library, z);
        }
    }

    public static /* synthetic */ void lambda$startDownload$0(ObservableEmitter observableEmitter) throws Exception {
        observableEmitter.onNext(new OptionalResult(LibraryManager.getInstance().getLibrarySync(1043L)));
    }

    public /* synthetic */ void lambda$startDownload$1(boolean z, OptionalResult optionalResult) throws Exception {
        Library library = (Library) optionalResult.getIncludeNull();
        if (library == null) {
            DefaultLogger.w("VideoCompressLibraryLoaderHelper", "getLibrarySync failed");
            refreshDownloadResult(false, -2);
            return;
        }
        doDownloadLibrary(library, z);
    }

    public final void doDownloadLibrary(Library library, boolean z) {
        LibraryManager.getInstance().downloadLibrary(library, z, new LibraryManager.DownloadListener() { // from class: com.miui.gallery.video.compress.VideoCompressLibraryLoaderHelper.1
            {
                VideoCompressLibraryLoaderHelper.this = this;
            }

            @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
            public void onDownloadProgress(long j, int i) {
                DefaultLogger.d("VideoCompressLibraryLoaderHelper", "onDownloadProgress: %d", Integer.valueOf(i));
                VideoCompressLibraryLoaderHelper.this.refreshDownloading(i);
            }

            @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
            public void onDownloadResult(long j, int i) {
                DefaultLogger.d("VideoCompressLibraryLoaderHelper", "download result %d", Integer.valueOf(i));
                VideoCompressLibraryLoaderHelper.this.refreshDownloadResult(i == 0, i);
            }
        });
    }

    public final void refreshDownloadStart() {
        DefaultLogger.d("VideoCompressLibraryLoaderHelper", "refreshDownloadStart");
        this.mIsDownloading = true;
        VideoCompressDownloadStateListener videoCompressDownloadStateListener = this.mDownloadStateListener;
        if (videoCompressDownloadStateListener != null) {
            videoCompressDownloadStateListener.onDownloadStart();
        }
    }

    public final void refreshDownloading(int i) {
        this.mIsDownloading = true;
        VideoCompressDownloadStateListener videoCompressDownloadStateListener = this.mDownloadStateListener;
        if (videoCompressDownloadStateListener != null) {
            videoCompressDownloadStateListener.onDownloading(i);
        }
    }

    public final void refreshDownloadResult(boolean z, int i) {
        this.mIsDownloading = false;
        VideoCompressDownloadStateListener videoCompressDownloadStateListener = this.mDownloadStateListener;
        if (videoCompressDownloadStateListener != null) {
            videoCompressDownloadStateListener.onFinish(z, i);
        }
    }

    public void setDownloadStateListener(VideoCompressDownloadStateListener videoCompressDownloadStateListener) {
        this.mDownloadStateListener = videoCompressDownloadStateListener;
    }

    public boolean isDownloading() {
        return this.mIsDownloading;
    }

    public boolean isDownloaded() {
        return getLoaderState() == 0;
    }
}
