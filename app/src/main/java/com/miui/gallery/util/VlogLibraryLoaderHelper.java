package com.miui.gallery.util;

import android.os.AsyncTask;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/* loaded from: classes2.dex */
public class VlogLibraryLoaderHelper extends LibraryLoaderHelper {
    public static VlogLibraryLoaderHelper sInstance;

    public static /* synthetic */ void $r8$lambda$j64b5hQdXogiD69H2uOu_J5PlpE(VlogLibraryLoaderHelper vlogLibraryLoaderHelper, LibraryLoaderHelper.DownloadStateListener downloadStateListener, boolean z, OptionalResult optionalResult) {
        vlogLibraryLoaderHelper.lambda$startDownloadVlogWithCheck$2(downloadStateListener, z, optionalResult);
    }

    public static /* synthetic */ void $r8$lambda$ujqZWYdttSGFXx_seM7fFa2INPc(VlogLibraryLoaderHelper vlogLibraryLoaderHelper, ObservableEmitter observableEmitter) {
        vlogLibraryLoaderHelper.lambda$startDownloadVlogWithCheck$1(observableEmitter);
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public long getLibraryId() {
        return 101953L;
    }

    public static VlogLibraryLoaderHelper getInstance() {
        if (sInstance == null) {
            sInstance = new VlogLibraryLoaderHelper();
        }
        return sInstance;
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showNoNetworkToast() {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.vlog_download_failed_for_notwork);
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showConfirmDownloadDialog(FragmentActivity fragmentActivity, final Library library, final LibraryLoaderHelper.DownloadStartListener downloadStartListener) {
        NetworkConsider.consider(fragmentActivity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.util.VlogLibraryLoaderHelper$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
            public final void onConfirmed(boolean z, boolean z2) {
                VlogLibraryLoaderHelper.this.lambda$showConfirmDownloadDialog$0(library, downloadStartListener, z, z2);
            }
        });
    }

    public /* synthetic */ void lambda$showConfirmDownloadDialog$0(Library library, LibraryLoaderHelper.DownloadStartListener downloadStartListener, boolean z, boolean z2) {
        if (z) {
            startDownloadWithCheckLibrary(library, true, downloadStartListener);
        } else {
            refreshDownloadResult(false, -1);
        }
    }

    @Override // com.miui.gallery.net.library.LibraryLoaderHelper
    public void showDownloadResultToast(boolean z) {
        ToastUtils.makeText(StaticContext.sGetAndroidContext(), z ? R.string.photo_editor_common_download_finish : R.string.photo_editor_common_download_failed_msg);
    }

    public void startDownloadVlogWithCheck(final boolean z, final LibraryLoaderHelper.DownloadStateListener downloadStateListener) {
        Library library = LibraryManager.getInstance().getLibrary(getLibraryId());
        if (downloadStateListener != null) {
            downloadStateListener.onDownloading();
        }
        if (library == null) {
            Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.util.VlogLibraryLoaderHelper$$ExternalSyntheticLambda1
                @Override // io.reactivex.ObservableOnSubscribe
                public final void subscribe(ObservableEmitter observableEmitter) {
                    VlogLibraryLoaderHelper.$r8$lambda$ujqZWYdttSGFXx_seM7fFa2INPc(VlogLibraryLoaderHelper.this, observableEmitter);
                }
            }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.util.VlogLibraryLoaderHelper$$ExternalSyntheticLambda2
                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    VlogLibraryLoaderHelper.$r8$lambda$j64b5hQdXogiD69H2uOu_J5PlpE(VlogLibraryLoaderHelper.this, downloadStateListener, z, (OptionalResult) obj);
                }
            });
        } else {
            doDownloadVlog(library, z, downloadStateListener);
        }
    }

    public /* synthetic */ void lambda$startDownloadVlogWithCheck$1(ObservableEmitter observableEmitter) throws Exception {
        observableEmitter.onNext(new OptionalResult(LibraryManager.getInstance().getLibrarySync(getLibraryId())));
    }

    public /* synthetic */ void lambda$startDownloadVlogWithCheck$2(LibraryLoaderHelper.DownloadStateListener downloadStateListener, boolean z, OptionalResult optionalResult) throws Exception {
        Library library = (Library) optionalResult.getIncludeNull();
        if (library == null) {
            DefaultLogger.w("VlogLibraryLoaderHelper_", "getLibrarySync failed");
            downloadStateListener.onFinish(false, -2);
            return;
        }
        doDownloadVlog(library, z, downloadStateListener);
    }

    public final void doDownloadVlog(Library library, boolean z, final LibraryLoaderHelper.DownloadStateListener downloadStateListener) {
        LibraryManager.getInstance().downloadLibrary(library, z, new LibraryManager.DownloadListener() { // from class: com.miui.gallery.util.VlogLibraryLoaderHelper.1
            @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
            public void onDownloadProgress(long j, int i) {
            }

            {
                VlogLibraryLoaderHelper.this = this;
            }

            @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
            public void onDownloadResult(long j, int i) {
                DefaultLogger.d("VlogLibraryLoaderHelper_", "download result %d", Integer.valueOf(i));
                downloadStateListener.onFinish(i == 0, i);
            }
        });
    }
}
