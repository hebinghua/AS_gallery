package com.miui.gallery.movie.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.movie.MovieConfig;
import com.miui.gallery.movie.entity.TemplateResource;
import com.miui.gallery.movie.net.TemplateResourceRequest;
import com.miui.gallery.movie.ui.factory.MovieFactory;
import com.miui.gallery.movie.ui.factory.TemplateFactory;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/* loaded from: classes2.dex */
public class MovieBackgroundDownloadManager {
    public static MovieBackgroundDownloadManager sInstance = new MovieBackgroundDownloadManager();
    public List<TemplateResource> mTemplateResources;

    public static /* synthetic */ void $r8$lambda$dRrjWOx6bY5PsnH3HZuNNBB0mL4(MovieBackgroundDownloadManager movieBackgroundDownloadManager, String str) {
        movieBackgroundDownloadManager.lambda$downloadTemplate$1(str);
    }

    public static MovieBackgroundDownloadManager getInstance() {
        return sInstance;
    }

    public void downloadTemplate(Context context, int i) {
        if (!BaseNetworkUtils.isNetworkConnected()) {
            DefaultLogger.d("MovieBackgroundDownloadManager", "download templateId %d no network", Integer.valueOf(i));
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            DefaultLogger.d("MovieBackgroundDownloadManager", "download templateId %d in network metered", Integer.valueOf(i));
        } else {
            MovieConfig.init(context);
            Observable.just(Integer.valueOf(i)).observeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).map(MovieBackgroundDownloadManager$$ExternalSyntheticLambda1.INSTANCE).subscribe(new Consumer() { // from class: com.miui.gallery.movie.utils.MovieBackgroundDownloadManager$$ExternalSyntheticLambda0
                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    MovieBackgroundDownloadManager.$r8$lambda$dRrjWOx6bY5PsnH3HZuNNBB0mL4(MovieBackgroundDownloadManager.this, (String) obj);
                }
            });
        }
    }

    public static /* synthetic */ String lambda$downloadTemplate$0(Integer num) throws Exception {
        return MovieFactory.getParentTemplateName(MovieFactory.getTemplateNameById(num.intValue()));
    }

    public /* synthetic */ void lambda$downloadTemplate$1(String str) throws Exception {
        boolean equals = TextUtils.equals(str, "movieAssetsNormal");
        if (!equals) {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(TemplateFactory.getTemplatePath(str), IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("MovieBackgroundDownloadManager", "downloadTemplate"));
            equals = documentFile != null && documentFile.exists();
        }
        if (equals) {
            DefaultLogger.d("MovieBackgroundDownloadManager", "template %s is already exist", str);
            return;
        }
        TemplateResource templateResourceFromNameSync = getTemplateResourceFromNameSync(str);
        if (templateResourceFromNameSync == null) {
            return;
        }
        DefaultLogger.d("MovieBackgroundDownloadManager", "start download %s in background", templateResourceFromNameSync.nameKey);
        MovieDownloadManager.getInstance().downloadResource(templateResourceFromNameSync, null, false);
    }

    public final TemplateResource getTemplateResourceFromNameSync(String str) {
        List<TemplateResource> list = this.mTemplateResources;
        if (list == null) {
            try {
                Object[] executeSync = new TemplateResourceRequest().executeSync();
                if (executeSync != null && executeSync.length > 0 && (executeSync[0] instanceof List)) {
                    list = (List) executeSync[0];
                }
                DefaultLogger.d("MovieBackgroundDownloadManager", "getTemplateList %d ", Integer.valueOf(list == null ? -1 : list.size()));
            } catch (RequestError unused) {
                DefaultLogger.e("MovieBackgroundDownloadManager", "RequestError: getTemplateList");
            }
        }
        TemplateResource templateResource = null;
        if (list == null) {
            DefaultLogger.d("MovieBackgroundDownloadManager", "template resource is null");
        } else {
            for (TemplateResource templateResource2 : list) {
                if (TextUtils.equals(templateResource2.nameKey, str)) {
                    templateResource = templateResource2;
                }
            }
        }
        this.mTemplateResources = list;
        return templateResource;
    }
}
