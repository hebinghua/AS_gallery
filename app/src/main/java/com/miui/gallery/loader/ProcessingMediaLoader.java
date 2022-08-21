package com.miui.gallery.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Trace;
import androidx.loader.content.Loader;
import com.miui.gallery.content.ExtendedAsyncTaskLoader;
import com.miui.gallery.photosapi.PhotosOemApi;
import com.miui.gallery.provider.ProcessingMedia;
import com.miui.gallery.provider.ProcessingMediaManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;

/* loaded from: classes2.dex */
public class ProcessingMediaLoader extends ExtendedAsyncTaskLoader<List<ProcessingMedia>> {
    public static boolean sHasSpecialTypesProvider = true;
    public List<ProcessingMedia> mDataSet;
    public final ContentObserver mForceLoadContentObserver;
    public boolean mIsObserversRegistered;

    public ProcessingMediaLoader(Context context) {
        super(context);
        this.mForceLoadContentObserver = new Loader.ForceLoadContentObserver();
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    /* renamed from: loadInBackground  reason: collision with other method in class */
    public List<ProcessingMedia> mo1444loadInBackground() {
        try {
            Trace.beginSection("QueryProcessingMedias");
            long currentTimeMillis = System.currentTimeMillis();
            DefaultLogger.d("ProcessingMediaLoader", "start load\u3000processing medias");
            List<ProcessingMedia> queryProcessingMedias = ProcessingMediaManager.queryProcessingMedias();
            DefaultLogger.d("ProcessingMediaLoader", "load cost %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            return queryProcessingMedias;
        } finally {
            Trace.endSection();
        }
    }

    @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
    public void onContentChanged() {
        super.onContentChanged();
        DefaultLogger.d("ProcessingMediaLoader", "processing medias changed");
    }

    @Override // androidx.loader.content.Loader
    public final void deliverResult(List<ProcessingMedia> list) {
        if (isReset()) {
            if (list == null) {
                return;
            }
            list.clear();
            return;
        }
        List<ProcessingMedia> list2 = this.mDataSet;
        this.mDataSet = list;
        if (isStarted()) {
            super.deliverResult((ProcessingMediaLoader) list);
        }
        if (list2 == null || list2 == list) {
            return;
        }
        list2.clear();
    }

    @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
    public final void onStartLoading() {
        List<ProcessingMedia> list = this.mDataSet;
        if (list != null) {
            deliverResult(list);
        }
        if (takeContentChanged() || this.mDataSet == null) {
            forceLoad();
        }
        registerContentObservers();
    }

    @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
    public void onStopLoading() {
        cancelLoad();
    }

    @Override // androidx.loader.content.Loader
    public final void onReset() {
        super.onReset();
        onStopLoading();
        unregisterContentObservers();
        List<ProcessingMedia> list = this.mDataSet;
        if (list != null) {
            list.clear();
            this.mDataSet = null;
        }
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    public final void onCanceled(List<ProcessingMedia> list) {
        if (list != null) {
            list.clear();
        }
    }

    @Override // androidx.loader.content.Loader
    public void onAbandon() {
        super.onAbandon();
        unregisterContentObservers();
    }

    public final void registerContentObservers() {
        if (this.mIsObserversRegistered) {
            return;
        }
        this.mIsObserversRegistered = true;
        ContentResolver contentResolver = getContext().getContentResolver();
        if (!sHasSpecialTypesProvider) {
            return;
        }
        try {
            contentResolver.registerContentObserver(PhotosOemApi.getQueryProcessingUri(getContext()), true, this.mForceLoadContentObserver);
        } catch (Exception unused) {
            sHasSpecialTypesProvider = false;
        }
    }

    public final void unregisterContentObservers() {
        if (!this.mIsObserversRegistered) {
            return;
        }
        this.mIsObserversRegistered = false;
        getContext().getContentResolver().unregisterContentObserver(this.mForceLoadContentObserver);
    }
}
