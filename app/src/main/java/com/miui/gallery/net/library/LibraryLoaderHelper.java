package com.miui.gallery.net.library;

import android.os.AsyncTask;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.assistant.library.LibraryUtils;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.OptionalResult;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.base.stat.VlogStatUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes2.dex */
public abstract class LibraryLoaderHelper {
    public boolean mIsDownloading;
    public volatile boolean mIsLoading;
    public LoadLibraryTask mLoadLibraryTask;
    public final CopyOnWriteArrayList<DownloadStateListener> mDownloadStateListeners = new CopyOnWriteArrayList<>();
    public final CopyOnWriteArrayList<OnLibraryLoadListener> mLoadLibraryListeners = new CopyOnWriteArrayList<>();

    /* loaded from: classes2.dex */
    public interface DownloadStartListener {
        void onDownloadStart();
    }

    /* loaded from: classes2.dex */
    public interface DownloadStateListener {
        void onDownloading();

        void onFinish(boolean z, int i);
    }

    /* loaded from: classes2.dex */
    public enum Error {
        NO_NETWORK,
        NO_WLAN,
        DOWNLOAD_LIBRARY_FAIL,
        LOAD_LIBRARY_FAIL,
        INITIALIZE_LIBRARY_FAIL,
        CANNOT_FOUND_LIBRARY
    }

    /* loaded from: classes2.dex */
    public interface OnLibraryLoadListener {
        void onLoadFinish(boolean z);

        void onLoading();
    }

    public static /* synthetic */ void $r8$lambda$6h5HWf9UMe8BEslx9NoPzQ2z2v8(LibraryLoaderHelper libraryLoaderHelper, ObservableEmitter observableEmitter) {
        libraryLoaderHelper.lambda$startLoadWithCheckLibrary$1(observableEmitter);
    }

    /* renamed from: $r8$lambda$DKq_KYZTYaaA8qn-86fYwurqdpI */
    public static /* synthetic */ void m1162$r8$lambda$DKq_KYZTYaaA8qn86fYwurqdpI(LibraryLoaderHelper libraryLoaderHelper, boolean z, OptionalResult optionalResult) {
        libraryLoaderHelper.lambda$startDownloadWithCheckLibrary$4(z, optionalResult);
    }

    /* renamed from: $r8$lambda$LnyTOJ8be1-lzQL2PG1eqqll4q4 */
    public static /* synthetic */ void m1163$r8$lambda$LnyTOJ8be1lzQL2PG1eqqll4q4(LibraryLoaderHelper libraryLoaderHelper, ObservableEmitter observableEmitter) {
        libraryLoaderHelper.lambda$startDownloadWithCheckLibrary$3(observableEmitter);
    }

    public static /* synthetic */ void $r8$lambda$atQ85cOuRe4XU_IQRGbTfd21GAI(LibraryLoaderHelper libraryLoaderHelper, boolean z, Optional optional) {
        libraryLoaderHelper.lambda$startLoadWithCheckLibrary$2(z, optional);
    }

    public abstract long getLibraryId();

    public boolean initLibrary(boolean z) {
        return z;
    }

    public abstract void showConfirmDownloadDialog(FragmentActivity fragmentActivity, Library library, DownloadStartListener downloadStartListener);

    public void showConfirmLoadDialog(FragmentActivity fragmentActivity, Library library) {
    }

    public abstract void showDownloadResultToast(boolean z);

    public void showLoadResultToast(boolean z, Error error) {
    }

    public abstract void showNoNetworkToast();

    public int getLoaderState() {
        return getLoaderState(LibraryManager.getInstance().getLibrary(getLibraryId()));
    }

    public int getLoaderState(Library library) {
        if (library == null) {
            return this.mIsDownloading ? 1 : 2;
        } else if (library.getLibraryStatus() == Library.LibraryStatus.STATE_AVAILABLE) {
            return 0;
        } else {
            return library.getLibraryStatus() == Library.LibraryStatus.STATE_LOADED ? 3 : 2;
        }
    }

    public int getLoaderStateConsistent() {
        return getLoaderStateConsistent(LibraryManager.getInstance().getLibrary(getLibraryId()));
    }

    public int getLoaderStateConsistent(Library library) {
        if (library == null || !(library.getLibraryStatus() == Library.LibraryStatus.STATE_AVAILABLE || library.getLibraryStatus() == Library.LibraryStatus.STATE_LOADED)) {
            return this.mIsDownloading ? 18 : 19;
        }
        return 17;
    }

    public boolean checkHasDownload() {
        Library library = LibraryManager.getInstance().getLibrary(getLibraryId());
        return library != null && (getLoaderState(library) == 0 || getLoaderState(library) == 3);
    }

    public boolean checkAbleOrDownload(FragmentActivity fragmentActivity) {
        return checkAbleOrDownload(fragmentActivity, null);
    }

    public boolean checkAbleOrDownload(FragmentActivity fragmentActivity, DownloadStartListener downloadStartListener) {
        Library library = LibraryManager.getInstance().getLibrary(getLibraryId());
        if (library == null || !(getLoaderState(library) == 0 || getLoaderState(library) == 3)) {
            startDownloadWithCheck(fragmentActivity, library, downloadStartListener);
            return false;
        }
        return true;
    }

    public void startDownloadWithCheck(final FragmentActivity fragmentActivity, final Library library, final DownloadStartListener downloadStartListener) {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            AgreementsUtils.showUserAgreements(fragmentActivity, new OnAgreementInvokedListener() { // from class: com.miui.gallery.net.library.LibraryLoaderHelper.1
                {
                    LibraryLoaderHelper.this = this;
                }

                @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                public void onAgreementInvoked(boolean z) {
                    if (z) {
                        LibraryLoaderHelper.this.startDownloadWithCheck(fragmentActivity, library, downloadStartListener);
                    }
                }
            });
        } else if (!BaseNetworkUtils.isNetworkConnected()) {
            showNoNetworkToast();
            DefaultLogger.d("LibraryLoaderHelper", "download sdk no network");
            refreshDownloadResult(false, -1);
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            showConfirmDownloadDialog(fragmentActivity, library, downloadStartListener);
        } else {
            startDownloadWithCheckLibrary(library, false, downloadStartListener);
        }
    }

    public boolean checkAbleOrLoaded(FragmentActivity fragmentActivity) {
        return checkAbleOrLoaded(fragmentActivity, true);
    }

    public boolean checkAbleOrLoaded(FragmentActivity fragmentActivity, boolean z) {
        Library library = LibraryManager.getInstance().getLibrary(getLibraryId());
        if (library != null && getLoaderState(library) == 3) {
            return initLibrary(true);
        }
        if (library != null && getLoaderState(library) == 0) {
            startLoadLibrary();
            return false;
        }
        startDownloadAndLoadLibrary(fragmentActivity, library, z);
        return false;
    }

    public void startDownloadAndLoadLibrary(final FragmentActivity fragmentActivity, final Library library, boolean z) {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            AgreementsUtils.showUserAgreements(fragmentActivity, new OnAgreementInvokedListener() { // from class: com.miui.gallery.net.library.LibraryLoaderHelper$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                public final void onAgreementInvoked(boolean z2) {
                    LibraryLoaderHelper.this.lambda$startDownloadAndLoadLibrary$0(fragmentActivity, library, z2);
                }
            });
        } else if (!BaseNetworkUtils.isNetworkConnected()) {
            showNoNetworkToast();
            DefaultLogger.d("LibraryLoaderHelper", "download sdk no network");
            refreshLoadLibraryResult(false, Error.NO_NETWORK);
        } else if (!BaseNetworkUtils.isActiveNetworkMetered()) {
            startLoadWithCheckLibrary(library, false);
        } else if (z) {
            showConfirmLoadDialog(fragmentActivity, library);
        } else {
            refreshLoadLibraryResult(false, Error.NO_WLAN);
        }
    }

    public /* synthetic */ void lambda$startDownloadAndLoadLibrary$0(FragmentActivity fragmentActivity, Library library, boolean z) {
        if (z) {
            startDownloadAndLoadLibrary(fragmentActivity, library, true);
        }
    }

    public void startLoadWithCheckLibrary(Library library, final boolean z) {
        onLoading();
        if (library == null) {
            Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.net.library.LibraryLoaderHelper$$ExternalSyntheticLambda1
                @Override // io.reactivex.ObservableOnSubscribe
                public final void subscribe(ObservableEmitter observableEmitter) {
                    LibraryLoaderHelper.$r8$lambda$6h5HWf9UMe8BEslx9NoPzQ2z2v8(LibraryLoaderHelper.this, observableEmitter);
                }
            }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.net.library.LibraryLoaderHelper$$ExternalSyntheticLambda4
                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    LibraryLoaderHelper.$r8$lambda$atQ85cOuRe4XU_IQRGbTfd21GAI(LibraryLoaderHelper.this, z, (Optional) obj);
                }
            });
        } else {
            doDownloadAndLoadLibrary(library, z);
        }
    }

    public /* synthetic */ void lambda$startLoadWithCheckLibrary$1(ObservableEmitter observableEmitter) throws Exception {
        observableEmitter.onNext(Optional.ofNullable(LibraryManager.getInstance().getLibrarySync(getLibraryId())));
    }

    public /* synthetic */ void lambda$startLoadWithCheckLibrary$2(boolean z, Optional optional) throws Exception {
        if (!optional.isPresent()) {
            DefaultLogger.w("LibraryLoaderHelper", "getLibrarySync failed");
            refreshLoadLibraryResult(false, Error.CANNOT_FOUND_LIBRARY);
            return;
        }
        doDownloadAndLoadLibrary((Library) optional.get(), z);
    }

    public final void doDownloadAndLoadLibrary(Library library, boolean z) {
        LibraryManager.getInstance().downloadLibrary(library, z, new LibraryManager.DownloadListener() { // from class: com.miui.gallery.net.library.LibraryLoaderHelper.2
            @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
            public void onDownloadProgress(long j, int i) {
            }

            {
                LibraryLoaderHelper.this = this;
            }

            @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
            public void onDownloadResult(long j, int i) {
                DefaultLogger.w("LibraryLoaderHelper", "download result %d", Integer.valueOf(i));
                if (i == 0) {
                    LibraryLoaderHelper.this.startLoadLibrary();
                }
                LibraryLoaderHelper.this.refreshDownloadResult(i == 0, i);
            }
        });
    }

    public void startDownloadWithCheckLibrary(Library library, final boolean z, DownloadStartListener downloadStartListener) {
        onDownloading();
        if (downloadStartListener != null) {
            downloadStartListener.onDownloadStart();
        }
        if (library == null) {
            Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.net.library.LibraryLoaderHelper$$ExternalSyntheticLambda2
                @Override // io.reactivex.ObservableOnSubscribe
                public final void subscribe(ObservableEmitter observableEmitter) {
                    LibraryLoaderHelper.m1163$r8$lambda$LnyTOJ8be1lzQL2PG1eqqll4q4(LibraryLoaderHelper.this, observableEmitter);
                }
            }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.net.library.LibraryLoaderHelper$$ExternalSyntheticLambda3
                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    LibraryLoaderHelper.m1162$r8$lambda$DKq_KYZTYaaA8qn86fYwurqdpI(LibraryLoaderHelper.this, z, (OptionalResult) obj);
                }
            });
        } else {
            doDownloadLibrary(library, z);
        }
    }

    public /* synthetic */ void lambda$startDownloadWithCheckLibrary$3(ObservableEmitter observableEmitter) throws Exception {
        observableEmitter.onNext(new OptionalResult(LibraryManager.getInstance().getLibrarySync(getLibraryId())));
    }

    public /* synthetic */ void lambda$startDownloadWithCheckLibrary$4(boolean z, OptionalResult optionalResult) throws Exception {
        Library library = (Library) optionalResult.getIncludeNull();
        if (library == null) {
            DefaultLogger.w("LibraryLoaderHelper", "getLibrarySync failed");
            refreshDownloadResult(false, -2);
            return;
        }
        doDownloadLibrary(library, z);
    }

    public final void doDownloadLibrary(Library library, boolean z) {
        LibraryManager.getInstance().downloadLibrary(library, z, new LibraryManager.DownloadListener() { // from class: com.miui.gallery.net.library.LibraryLoaderHelper.3
            @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
            public void onDownloadProgress(long j, int i) {
            }

            {
                LibraryLoaderHelper.this = this;
            }

            @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
            public void onDownloadResult(long j, int i) {
                DefaultLogger.d("LibraryLoaderHelper", "download result %d", Integer.valueOf(i));
                LibraryLoaderHelper.this.refreshDownloadResult(i == 0, i);
            }
        });
    }

    public void startLoadLibrary() {
        if (getLoaderState() == 3) {
            refreshLoadLibraryResult(true, null);
        }
        onLoading();
        LoadLibraryTask loadLibraryTask = new LoadLibraryTask(getLibraryId(), new OnLibraryLoadListener() { // from class: com.miui.gallery.net.library.LibraryLoaderHelper.4
            @Override // com.miui.gallery.net.library.LibraryLoaderHelper.OnLibraryLoadListener
            public void onLoading() {
            }

            {
                LibraryLoaderHelper.this = this;
            }

            @Override // com.miui.gallery.net.library.LibraryLoaderHelper.OnLibraryLoadListener
            public void onLoadFinish(boolean z) {
                LibraryLoaderHelper.this.refreshLoadLibraryResult(z, Error.LOAD_LIBRARY_FAIL);
            }
        });
        this.mLoadLibraryTask = loadLibraryTask;
        loadLibraryTask.execute(new Void[0]);
    }

    public String getLibraryDirPath() {
        return LibraryUtils.getLibraryDirPath(GalleryApp.sGetAndroidContext(), getLibraryId());
    }

    public final void onDownloading() {
        this.mIsDownloading = true;
        Iterator<DownloadStateListener> it = this.mDownloadStateListeners.iterator();
        while (it.hasNext()) {
            it.next().onDownloading();
        }
    }

    public void refreshDownloadResult(boolean z, int i) {
        this.mIsDownloading = false;
        Iterator<DownloadStateListener> it = this.mDownloadStateListeners.iterator();
        while (it.hasNext()) {
            it.next().onFinish(z, i);
        }
        VlogStatUtils.statLibraryDownloadResult(z);
        showDownloadResultToast(z);
    }

    public final void onLoading() {
        this.mIsLoading = true;
        Iterator<OnLibraryLoadListener> it = this.mLoadLibraryListeners.iterator();
        while (it.hasNext()) {
            it.next().onLoading();
        }
    }

    public void refreshLoadLibraryResult(boolean z, Error error) {
        this.mIsLoading = false;
        Iterator<OnLibraryLoadListener> it = this.mLoadLibraryListeners.iterator();
        while (it.hasNext()) {
            it.next().onLoadFinish(z);
        }
        initLibrary(z);
        showLoadResultToast(z, error);
    }

    public void addDownloadStateListener(DownloadStateListener downloadStateListener) {
        if (downloadStateListener != null) {
            this.mDownloadStateListeners.add(downloadStateListener);
        }
    }

    public void addLoadLibraryListener(OnLibraryLoadListener onLibraryLoadListener) {
        if (onLibraryLoadListener != null) {
            this.mLoadLibraryListeners.add(onLibraryLoadListener);
        }
    }

    public void removeLoadLibraryListener(OnLibraryLoadListener onLibraryLoadListener) {
        if (onLibraryLoadListener != null) {
            this.mLoadLibraryListeners.remove(onLibraryLoadListener);
        }
        LoadLibraryTask loadLibraryTask = this.mLoadLibraryTask;
        if (loadLibraryTask != null) {
            loadLibraryTask.removeListener();
            this.mLoadLibraryTask.cancel(true);
            this.mIsLoading = false;
        }
    }

    public void removeDownloadStateListener(DownloadStateListener downloadStateListener) {
        if (downloadStateListener != null) {
            this.mDownloadStateListeners.remove(downloadStateListener);
        }
    }

    /* loaded from: classes2.dex */
    public static class LoadLibraryTask extends AsyncTask<Void, Void, Boolean> {
        public final long mLibraryId;
        public OnLibraryLoadListener mListener;

        public LoadLibraryTask(long j, OnLibraryLoadListener onLibraryLoadListener) {
            this.mLibraryId = j;
            this.mListener = onLibraryLoadListener;
        }

        @Override // android.os.AsyncTask
        public Boolean doInBackground(Void... voidArr) {
            return Boolean.valueOf(LibraryManager.getInstance().loadLibrary(this.mLibraryId));
        }

        public void removeListener() {
            this.mListener = null;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean bool) {
            super.onPostExecute((LoadLibraryTask) bool);
            OnLibraryLoadListener onLibraryLoadListener = this.mListener;
            if (onLibraryLoadListener != null) {
                onLibraryLoadListener.onLoadFinish(bool.booleanValue());
            }
        }
    }
}
