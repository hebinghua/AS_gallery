package com.miui.gallery.movie.utils;

import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.movie.R$string;
import com.miui.gallery.movie.entity.MovieResource;
import com.miui.gallery.movie.ui.listener.MovieDownloadListener;
import com.miui.gallery.net.download.Request;
import com.miui.gallery.net.resource.ResourceDownloadManager;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.UnzipUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/* loaded from: classes2.dex */
public class MovieDownloadManager {
    public static MovieDownloadManager sInstance = new MovieDownloadManager();
    public Disposable mDisposible;
    public ResourceDownloadManager mResourceDownloadManager;

    public static /* synthetic */ void $r8$lambda$7zqDK9RWeaDEcxH76G6ZaglIwD4(MovieDownloadManager movieDownloadManager, MovieResource movieResource, boolean z, ObservableEmitter observableEmitter) {
        movieDownloadManager.lambda$downloadResource$1(movieResource, z, observableEmitter);
    }

    public static /* synthetic */ boolean $r8$lambda$nAXLjM9emsJUHvlzhD6jNSkxgYw(MovieDownloadListener movieDownloadListener, Integer num) {
        return lambda$downloadResource$2(movieDownloadListener, num);
    }

    public static /* synthetic */ void $r8$lambda$tzi2A5ppQuPYq7Dgbq1NCT_n9bg(MovieResource movieResource, MovieDownloadListener movieDownloadListener, Integer num) {
        lambda$downloadResource$3(movieResource, movieDownloadListener, num);
    }

    public static /* synthetic */ void $r8$lambda$ucIT1yrEOH7rFQqy1iAo5BEqWO8(MovieDownloadManager movieDownloadManager, MovieResource movieResource, MovieDownloadListener movieDownloadListener, boolean z, boolean z2) {
        movieDownloadManager.lambda$downloadResourceWithCheck$0(movieResource, movieDownloadListener, z, z2);
    }

    public static MovieDownloadManager getInstance() {
        return sInstance;
    }

    public void downloadResourceWithCheck(FragmentActivity fragmentActivity, final MovieResource movieResource, final MovieDownloadListener movieDownloadListener) {
        if (!BaseNetworkUtils.isNetworkConnected()) {
            ToastUtils.makeText(fragmentActivity, R$string.movie_download_failed_for_notwork);
            DefaultLogger.d("MovieDownloadManager", "download resource no network");
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            NetworkConsider.consider(fragmentActivity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.movie.utils.MovieDownloadManager$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                public final void onConfirmed(boolean z, boolean z2) {
                    MovieDownloadManager.$r8$lambda$ucIT1yrEOH7rFQqy1iAo5BEqWO8(MovieDownloadManager.this, movieResource, movieDownloadListener, z, z2);
                }
            });
        } else {
            downloadResource(movieResource, movieDownloadListener, false);
        }
    }

    public /* synthetic */ void lambda$downloadResourceWithCheck$0(MovieResource movieResource, MovieDownloadListener movieDownloadListener, boolean z, boolean z2) {
        if (z) {
            downloadResource(movieResource, movieDownloadListener, true);
        }
    }

    public void downloadResource(final MovieResource movieResource, final MovieDownloadListener movieDownloadListener, final boolean z) {
        if (this.mResourceDownloadManager == null) {
            this.mResourceDownloadManager = new ResourceDownloadManager();
        }
        if (movieDownloadListener != null) {
            movieDownloadListener.onStart();
        }
        this.mDisposible = Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.movie.utils.MovieDownloadManager$$ExternalSyntheticLambda1
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                MovieDownloadManager.$r8$lambda$7zqDK9RWeaDEcxH76G6ZaglIwD4(MovieDownloadManager.this, movieResource, z, observableEmitter);
            }
        }).filter(new Predicate() { // from class: com.miui.gallery.movie.utils.MovieDownloadManager$$ExternalSyntheticLambda3
            @Override // io.reactivex.functions.Predicate
            public final boolean test(Object obj) {
                return MovieDownloadManager.$r8$lambda$nAXLjM9emsJUHvlzhD6jNSkxgYw(MovieDownloadListener.this, (Integer) obj);
            }
        }).observeOn(Schedulers.io()).subscribe(new Consumer() { // from class: com.miui.gallery.movie.utils.MovieDownloadManager$$ExternalSyntheticLambda2
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                MovieDownloadManager.$r8$lambda$tzi2A5ppQuPYq7Dgbq1NCT_n9bg(MovieResource.this, movieDownloadListener, (Integer) obj);
            }
        });
    }

    public /* synthetic */ void lambda$downloadResource$1(final MovieResource movieResource, boolean z, final ObservableEmitter observableEmitter) throws Exception {
        this.mResourceDownloadManager.download(movieResource, movieResource.getDownloadFilePath(), new Request.Listener() { // from class: com.miui.gallery.movie.utils.MovieDownloadManager.1
            @Override // com.miui.gallery.net.download.Request.Listener
            public void onStart() {
            }

            {
                MovieDownloadManager.this = this;
            }

            @Override // com.miui.gallery.net.download.Request.Listener
            public void onProgressUpdate(int i) {
                DefaultLogger.d("MovieDownloadManager", "download progress %s :%d", movieResource.getDownloadFilePath(), Integer.valueOf(i));
            }

            @Override // com.miui.gallery.net.download.Request.Listener
            public void onComplete(int i) {
                observableEmitter.onNext(Integer.valueOf(i));
            }
        }, z);
    }

    public static /* synthetic */ boolean lambda$downloadResource$2(MovieDownloadListener movieDownloadListener, Integer num) throws Exception {
        if (num.intValue() != 0 && movieDownloadListener != null) {
            movieDownloadListener.onCompleted(false);
        }
        return num.intValue() == 0;
    }

    public static /* synthetic */ void lambda$downloadResource$3(MovieResource movieResource, MovieDownloadListener movieDownloadListener, Integer num) throws Exception {
        DefaultLogger.d("MovieDownloadManager", "download %s :%d", movieResource.getDownloadFilePath(), num);
        boolean unZipFile = UnzipUtils.unZipFile(movieResource.getDownloadFilePath());
        if (movieDownloadListener != null) {
            movieDownloadListener.onCompleted(unZipFile);
        }
    }

    public void cancelAll() {
        Disposable disposable = this.mDisposible;
        if (disposable != null && !disposable.isDisposed()) {
            this.mDisposible.dispose();
        }
        ResourceDownloadManager resourceDownloadManager = this.mResourceDownloadManager;
        if (resourceDownloadManager != null) {
            resourceDownloadManager.cancelAll();
        }
    }
}
