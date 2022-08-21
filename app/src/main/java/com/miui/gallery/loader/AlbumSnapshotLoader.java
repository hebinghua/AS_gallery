package com.miui.gallery.loader;

import android.content.Context;
import com.miui.gallery.content.ExtendedAsyncTaskLoader;
import com.miui.gallery.dao.GalleryLiteEntityManager;
import com.miui.gallery.model.dto.Album;
import java.util.List;

/* loaded from: classes2.dex */
public class AlbumSnapshotLoader extends ExtendedAsyncTaskLoader<List<Album>> {
    public List<Album> mDataSet;
    public String mSelection;
    public String[] mSelectionArgs;

    public AlbumSnapshotLoader(Context context) {
        super(context);
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    /* renamed from: loadInBackground  reason: collision with other method in class */
    public List<Album> mo1444loadInBackground() {
        return GalleryLiteEntityManager.getInstance().query(Album.class, getSelection(), getSelectionArgs());
    }

    @Override // androidx.loader.content.Loader
    public final void deliverResult(List<Album> list) {
        if (isReset()) {
            if (list == null) {
                return;
            }
            list.clear();
            return;
        }
        List<Album> list2 = this.mDataSet;
        this.mDataSet = list;
        if (isStarted()) {
            super.deliverResult((AlbumSnapshotLoader) list);
        }
        if (list2 == null || list2 == list) {
            return;
        }
        list2.clear();
    }

    @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
    public final void onStartLoading() {
        List<Album> list = this.mDataSet;
        if (list != null) {
            deliverResult(list);
        }
        if (takeContentChanged() || this.mDataSet == null) {
            forceLoad();
        }
    }

    @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
    public void onStopLoading() {
        cancelLoad();
    }

    @Override // androidx.loader.content.Loader
    public final void onReset() {
        super.onReset();
        onStopLoading();
        List<Album> list = this.mDataSet;
        if (list != null) {
            list.clear();
            this.mDataSet = null;
        }
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    public final void onCanceled(List<Album> list) {
        if (list != null) {
            list.clear();
        }
    }

    public String getSelection() {
        return this.mSelection;
    }

    public void setSelection(String str) {
        this.mSelection = str;
    }

    public String[] getSelectionArgs() {
        return this.mSelectionArgs;
    }
}
