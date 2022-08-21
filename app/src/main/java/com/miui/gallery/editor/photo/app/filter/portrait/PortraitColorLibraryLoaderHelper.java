package com.miui.gallery.editor.photo.app.filter.portrait;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.util.OptionalResult;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/* loaded from: classes2.dex */
public class PortraitColorLibraryLoaderHelper {
    public static PortraitColorLibraryLoaderHelper sInstance = new PortraitColorLibraryLoaderHelper();
    public PortraitColorDownloadStateListener mDownloadStateListener;
    public boolean mIsDownloading;

    public static /* synthetic */ void $r8$lambda$EMYSj2qQJeVtThrBsF3Pnb4pFIc(PortraitColorLibraryLoaderHelper portraitColorLibraryLoaderHelper, boolean z, OptionalResult optionalResult) {
        portraitColorLibraryLoaderHelper.lambda$startDownload$1(z, optionalResult);
    }

    public static PortraitColorLibraryLoaderHelper getInstance() {
        return sInstance;
    }

    public final int getLoaderState() {
        Library library = LibraryManager.getInstance().getLibrary(1031L);
        if (library == null || library.getLibraryStatus() != Library.LibraryStatus.STATE_AVAILABLE) {
            return this.mIsDownloading ? 1 : 2;
        }
        return 0;
    }

    @SuppressLint({"CheckResult"})
    public void startDownload(final boolean z) {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            DefaultLogger.d("PortraitLibraryLoader", "download sdk failed ,cta not allowed");
            refreshDownloadResult(false, 101);
            return;
        }
        refreshDownloadStart();
        Library library = LibraryManager.getInstance().getLibrary(1031L);
        if (library == null) {
            Observable.create(PortraitColorLibraryLoaderHelper$$ExternalSyntheticLambda0.INSTANCE).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.editor.photo.app.filter.portrait.PortraitColorLibraryLoaderHelper$$ExternalSyntheticLambda1
                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    PortraitColorLibraryLoaderHelper.$r8$lambda$EMYSj2qQJeVtThrBsF3Pnb4pFIc(PortraitColorLibraryLoaderHelper.this, z, (OptionalResult) obj);
                }
            });
        } else {
            doDownloadLibrary(library, z);
        }
    }

    public static /* synthetic */ void lambda$startDownload$0(ObservableEmitter observableEmitter) throws Exception {
        observableEmitter.onNext(new OptionalResult(LibraryManager.getInstance().getLibrarySync(1031L)));
    }

    public /* synthetic */ void lambda$startDownload$1(boolean z, OptionalResult optionalResult) throws Exception {
        Library library = (Library) optionalResult.getIncludeNull();
        if (library == null) {
            DefaultLogger.w("PortraitLibraryLoader", "getLibrarySync failed");
            refreshDownloadResult(false, -2);
            return;
        }
        doDownloadLibrary(library, z);
    }

    public final void doDownloadLibrary(Library library, boolean z) {
        LibraryManager.getInstance().downloadLibrary(library, z, new LibraryManager.DownloadListener() { // from class: com.miui.gallery.editor.photo.app.filter.portrait.PortraitColorLibraryLoaderHelper.1
            {
                PortraitColorLibraryLoaderHelper.this = this;
            }

            @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
            public void onDownloadProgress(long j, int i) {
                DefaultLogger.d("PortraitLibraryLoader", "onDownloadProgress: %d", Integer.valueOf(i));
                PortraitColorLibraryLoaderHelper.this.refreshDownloading(i);
            }

            @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
            public void onDownloadResult(long j, int i) {
                DefaultLogger.d("PortraitLibraryLoader", "download result %d", Integer.valueOf(i));
                PortraitColorLibraryLoaderHelper.this.refreshDownloadResult(i == 0, i);
            }
        });
    }

    public final void refreshDownloadStart() {
        DefaultLogger.d("PortraitLibraryLoader", "refreshDownloadStart");
        this.mIsDownloading = true;
        PortraitColorDownloadStateListener portraitColorDownloadStateListener = this.mDownloadStateListener;
        if (portraitColorDownloadStateListener != null) {
            portraitColorDownloadStateListener.onDownloadStart();
        }
    }

    public final void refreshDownloading(int i) {
        this.mIsDownloading = true;
        PortraitColorDownloadStateListener portraitColorDownloadStateListener = this.mDownloadStateListener;
        if (portraitColorDownloadStateListener != null) {
            portraitColorDownloadStateListener.onDownloading(i);
        }
    }

    public final void refreshDownloadResult(boolean z, int i) {
        this.mIsDownloading = false;
        PortraitColorDownloadStateListener portraitColorDownloadStateListener = this.mDownloadStateListener;
        if (portraitColorDownloadStateListener != null) {
            portraitColorDownloadStateListener.onFinish(z, i);
        }
    }

    public void setDownloadStateListener(PortraitColorDownloadStateListener portraitColorDownloadStateListener) {
        this.mDownloadStateListener = portraitColorDownloadStateListener;
    }

    public boolean isDownloading() {
        return this.mIsDownloading;
    }

    public boolean isDownloaded() {
        return getLoaderState() == 0;
    }

    public boolean isNeedDownload() {
        return 2 == getLoaderState();
    }
}
