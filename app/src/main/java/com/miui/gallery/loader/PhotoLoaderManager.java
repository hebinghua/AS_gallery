package com.miui.gallery.loader;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import com.miui.gallery.model.ContentProxySource;
import com.miui.gallery.model.FaceSource;
import com.miui.gallery.model.LocalSource;
import com.miui.gallery.model.MediaSource;
import com.miui.gallery.model.PhotoLoaderSource;
import com.miui.gallery.model.SearchResultSource;
import com.miui.gallery.model.StorageSource;
import com.miui.gallery.model.TrashSource;
import com.miui.gallery.model.UriSource;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class PhotoLoaderManager {
    public static PhotoLoaderManager sInstance;
    public ArrayList<PhotoLoaderSource> mSources;

    public PhotoLoaderManager() {
        initSources();
    }

    public final void initSources() {
        ArrayList<PhotoLoaderSource> arrayList = new ArrayList<>();
        this.mSources = arrayList;
        arrayList.add(new LocalSource());
        this.mSources.add(new FaceSource());
        this.mSources.add(new TrashSource());
        this.mSources.add(new SearchResultSource());
        this.mSources.add(new MediaSource());
        this.mSources.add(new StorageSource());
        this.mSources.add(new ContentProxySource());
        this.mSources.add(new UriSource());
    }

    public BaseLoader getPhotoDataSet(Context context, Uri uri, Bundle bundle) {
        if (uri != null) {
            Iterator<PhotoLoaderSource> it = this.mSources.iterator();
            while (it.hasNext()) {
                PhotoLoaderSource next = it.next();
                if (next.match(uri, bundle)) {
                    return next.createDataLoader(context, uri, bundle);
                }
            }
            return null;
        }
        return null;
    }

    public static synchronized PhotoLoaderManager getInstance() {
        PhotoLoaderManager photoLoaderManager;
        synchronized (PhotoLoaderManager.class) {
            if (sInstance == null) {
                sInstance = new PhotoLoaderManager();
            }
            photoLoaderManager = sInstance;
        }
        return photoLoaderManager;
    }
}
