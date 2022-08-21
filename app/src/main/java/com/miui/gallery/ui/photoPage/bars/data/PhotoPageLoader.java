package com.miui.gallery.ui.photoPage.bars.data;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.tracing.Trace;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.loader.BaseLoader;
import com.miui.gallery.loader.PhotoLoaderManager;
import com.miui.gallery.loader.ProcessingMediaLoader;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.provider.ProcessingMedia;
import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.ui.photoPage.bars.data.DataProvider;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.ProcessingMediaHelper;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes2.dex */
public class PhotoPageLoader {
    public final GalleryFragment mFragment;
    public boolean mPendingLoadPhotos;
    public PhotoPageLoaderCallBack mPhotosLoaderCallBack;
    public ProcessingMediaLoaderCallback mProcessingMediaLoaderCallback;
    public Map<String, ProcessingMedia> mProcessingMediaMap;
    public Runnable mProcessingMediaPollingRunnable;
    public final DataProvider.ViewModelData mViewModel;

    public PhotoPageLoader(GalleryFragment galleryFragment, DataProvider.ViewModelData viewModelData) {
        this.mFragment = galleryFragment;
        this.mViewModel = viewModelData;
    }

    public Loader getCurrentPhotoLoader() {
        return this.mFragment.getLoaderManager().getLoader(2);
    }

    public void startToLoad() {
        Trace.beginSection("startToLoadProcessingMedias");
        if (this.mProcessingMediaLoaderCallback == null) {
            this.mProcessingMediaLoaderCallback = new ProcessingMediaLoaderCallback();
        }
        if (this.mProcessingMediaPollingRunnable == null) {
            this.mProcessingMediaPollingRunnable = new ProcessingMediaPollingRunnable();
        }
        LoaderManager.getInstance(this.mFragment).initLoader(1, null, this.mProcessingMediaLoaderCallback);
        this.mPendingLoadPhotos = true;
        Trace.endSection();
    }

    public final void startToLoadPhotos() {
        Trace.beginSection("startToLoadPhotos");
        if (this.mPhotosLoaderCallBack == null) {
            this.mPhotosLoaderCallBack = new PhotoPageLoaderCallBack();
        }
        LoaderManager.getInstance(this.mFragment).initLoader(2, this.mFragment.getArguments(), this.mPhotosLoaderCallBack);
        Trace.endSection();
    }

    public void release() {
        ThreadManager.getMainHandler().removeCallbacks(this.mProcessingMediaPollingRunnable);
        this.mFragment.getLoaderManager().destroyLoader(1);
        this.mFragment.getLoaderManager().destroyLoader(2);
    }

    /* loaded from: classes2.dex */
    public class ProcessingMediaLoaderCallback implements LoaderManager.LoaderCallbacks<List<ProcessingMedia>> {
        public boolean isFirstLoad;
        public long start;

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<List<ProcessingMedia>> loader) {
        }

        public ProcessingMediaLoaderCallback() {
            this.isFirstLoad = true;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<List<ProcessingMedia>> onCreateLoader(int i, Bundle bundle) {
            this.start = System.currentTimeMillis();
            return new ProcessingMediaLoader(GalleryApp.sGetAndroidContext());
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<List<ProcessingMedia>> loader, List<ProcessingMedia> list) {
            HashMap hashMap;
            DefaultLogger.e("PhotoPageModel", "onLoadProcessingMediasFinished isFirstLoad:%b,", Boolean.valueOf(this.isFirstLoad));
            Trace.beginSection("onLoadProcessingMediasFinished");
            if (!BaseMiscUtil.isValid(list)) {
                ThreadManager.getMainHandler().removeCallbacks(PhotoPageLoader.this.mProcessingMediaPollingRunnable);
                hashMap = null;
            } else {
                DefaultLogger.d("PhotoPageModel", "processingMediaList.size:" + list.size());
                hashMap = new HashMap();
                for (ProcessingMedia processingMedia : list) {
                    hashMap.put(processingMedia.getPath(), processingMedia);
                }
                ThreadManager.getMainHandler().removeCallbacks(PhotoPageLoader.this.mProcessingMediaPollingRunnable);
                ThreadManager.getMainHandler().postDelayed(PhotoPageLoader.this.mProcessingMediaPollingRunnable, AbstractComponentTracker.LINGERING_TIMEOUT);
            }
            ProcessingMediaHelper.getInstance().calibrateCache(list);
            PhotoPageLoader.this.mProcessingMediaMap = hashMap;
            PhotoPageLoader.this.mViewModel.setProcessingMediaMap(hashMap);
            if (!this.isFirstLoad) {
                PhotoPageLoader.this.mViewModel.setIsFirstLoadProcessingMedia(this.isFirstLoad);
            } else {
                DefaultLogger.d("PhotoPageModel", "ProcessingMediaLoader first load costs [%d] ms", Long.valueOf(System.currentTimeMillis() - this.start));
            }
            Trace.endSection();
            if (PhotoPageLoader.this.mPendingLoadPhotos) {
                PhotoPageLoader.this.mPendingLoadPhotos = false;
                PhotoPageLoader.this.startToLoadPhotos();
            }
            this.isFirstLoad = false;
        }
    }

    /* loaded from: classes2.dex */
    public class ProcessingMediaPollingRunnable implements Runnable {
        public ProcessingMediaPollingRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            DefaultLogger.d("PhotoPageModel", "Query processing medias by polling, interval: %dms", (Object) 10000);
            LoaderManager.getInstance(PhotoPageLoader.this.mFragment).restartLoader(1, null, PhotoPageLoader.this.mProcessingMediaLoaderCallback);
        }
    }

    /* loaded from: classes2.dex */
    public class PhotoPageLoaderCallBack implements LoaderManager.LoaderCallbacks<Object> {
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<Object> loader) {
        }

        public PhotoPageLoaderCallBack() {
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<Object> onCreateLoader(int i, Bundle bundle) {
            Trace.beginSection("getPhotoDataSetLoader");
            Uri parse = Uri.parse(bundle.getString("photo_uri"));
            BaseLoader photoDataSet = PhotoLoaderManager.getInstance().getPhotoDataSet(PhotoPageLoader.this.mFragment.getContext(), parse, bundle);
            Trace.endSection();
            if (photoDataSet == null) {
                HashMap hashMap = new HashMap();
                String callingPackage = IntentUtil.getCallingPackage(PhotoPageLoader.this.mFragment.getActivity());
                if (TextUtils.isEmpty(callingPackage)) {
                    callingPackage = "Unknown";
                }
                hashMap.put("calling_package", callingPackage);
                hashMap.put(nexExportFormat.TAG_FORMAT_PATH, parse.toString());
                StatHelper.recordCountEvent("photo", "illegal_photo_uri", hashMap);
                throw new IllegalArgumentException(String.format(Locale.US, "Illegal photo uri: %s, callingPkg: %s", parse.toString(), callingPackage));
            }
            return photoDataSet;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<Object> loader, Object obj) {
            DefaultLogger.d("PhotoPageModel", "PhotoPageLoaderCallBack onLoadFinished");
            Trace.beginSection("startToLoadPhotos");
            PhotoPageLoader.this.mViewModel.setCurrentDataSet((BaseDataSet) obj);
            Trace.endSection();
        }
    }

    public void onContentChanged() {
        GalleryFragment galleryFragment = this.mFragment;
        if (galleryFragment == null || galleryFragment.isDetached() || !this.mFragment.isAdded() || this.mFragment.getLoaderManager().getLoader(2) == null) {
            return;
        }
        this.mFragment.getLoaderManager().getLoader(2).onContentChanged();
    }

    public void loadInBackground() {
        Loader loader;
        GalleryFragment galleryFragment = this.mFragment;
        if (galleryFragment == null || (loader = galleryFragment.getLoaderManager().getLoader(2)) == null) {
            return;
        }
        if (loader.isStarted()) {
            loader.onContentChanged();
        } else if (!(loader instanceof BaseLoader)) {
        } else {
            BaseLoader baseLoader = (BaseLoader) loader;
            baseLoader.setBackgroundLoadListener(new BackgroundLoadListener(this.mPhotosLoaderCallBack));
            baseLoader.forceLoad();
        }
    }

    public void cancelBackgroundLoad() {
        GalleryFragment galleryFragment = this.mFragment;
        if (galleryFragment == null) {
            return;
        }
        Loader loader = galleryFragment.getLoaderManager().getLoader(2);
        if (!(loader instanceof BaseLoader)) {
            return;
        }
        ((BaseLoader) loader).setBackgroundLoadListener(null);
    }

    public boolean isProcessingMedia(BaseDataItem baseDataItem) {
        Map<String, ProcessingMedia> map = this.mProcessingMediaMap;
        return (map == null || baseDataItem == null || !map.containsKey(baseDataItem.getOriginalPath())) ? false : true;
    }

    /* loaded from: classes2.dex */
    public static class BackgroundLoadListener implements BaseLoader.OnLoadCompleteListener {
        public WeakReference<PhotoPageLoaderCallBack> mCallbackRef;

        public BackgroundLoadListener(PhotoPageLoaderCallBack photoPageLoaderCallBack) {
            this.mCallbackRef = new WeakReference<>(photoPageLoaderCallBack);
        }

        @Override // com.miui.gallery.loader.BaseLoader.OnLoadCompleteListener
        public void onLoadComplete(BaseLoader baseLoader, BaseDataSet baseDataSet) {
            PhotoPageLoaderCallBack photoPageLoaderCallBack;
            WeakReference<PhotoPageLoaderCallBack> weakReference = this.mCallbackRef;
            if (weakReference != null && baseDataSet != null && (photoPageLoaderCallBack = weakReference.get()) != null) {
                photoPageLoaderCallBack.onLoadFinished(baseLoader, baseDataSet);
            }
            baseLoader.setBackgroundLoadListener(null);
        }
    }
}
