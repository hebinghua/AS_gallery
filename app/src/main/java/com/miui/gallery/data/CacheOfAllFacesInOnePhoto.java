package com.miui.gallery.data;

import android.util.LruCache;
import com.miui.gallery.cloud.peopleface.PeopleFace;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CacheOfAllFacesInOnePhoto {
    public static CacheOfAllFacesInOnePhoto mInstance;
    public final LruCache<String, ArrayList<PeopleFace>> mFacesCache = new LruCache<>(5);

    /* loaded from: classes.dex */
    public interface PhotoViewProvider {
        void onInvalidated();
    }

    public static CacheOfAllFacesInOnePhoto getInstance() {
        if (mInstance == null) {
            mInstance = new CacheOfAllFacesInOnePhoto();
        }
        return mInstance;
    }

    public ArrayList<PeopleFace> requestFacesOfThePhoto(final PhotoViewProvider photoViewProvider, final String str) {
        ArrayList<PeopleFace> arrayList;
        synchronized (this.mFacesCache) {
            arrayList = this.mFacesCache.get(str);
        }
        if (arrayList == null) {
            ThreadManager.getMiscPool().submit(new ThreadPool.Job<ArrayList<PeopleFace>>() { // from class: com.miui.gallery.data.CacheOfAllFacesInOnePhoto.1
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run  reason: collision with other method in class */
                public ArrayList<PeopleFace> mo1807run(ThreadPool.JobContext jobContext) {
                    return NormalPeopleFaceMediaSet.getBrothers(str);
                }
            }, new FutureListener<ArrayList<PeopleFace>>() { // from class: com.miui.gallery.data.CacheOfAllFacesInOnePhoto.2
                @Override // com.miui.gallery.concurrent.FutureListener
                public void onFutureDone(Future<ArrayList<PeopleFace>> future) {
                    if (future.get() != null) {
                        CacheOfAllFacesInOnePhoto.this.saveFacesOfThePhoto(future.get(), str);
                        photoViewProvider.onInvalidated();
                    }
                }
            });
        }
        return arrayList;
    }

    public void saveFacesOfThePhoto(ArrayList<PeopleFace> arrayList, String str) {
        synchronized (this.mFacesCache) {
            this.mFacesCache.put(str, arrayList);
        }
    }

    public void clearCache() {
        synchronized (this.mFacesCache) {
            this.mFacesCache.trimToSize(-1);
        }
    }
}
