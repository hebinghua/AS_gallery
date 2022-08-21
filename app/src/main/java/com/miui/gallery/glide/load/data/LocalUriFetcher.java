package com.miui.gallery.glide.load.data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import java.io.FileNotFoundException;
import java.io.IOException;

/* loaded from: classes2.dex */
public abstract class LocalUriFetcher<T> implements DataFetcher<T> {
    public final Context mContext;
    public T mData;
    public final Uri mUri;

    @Override // com.bumptech.glide.load.data.DataFetcher
    public void cancel() {
    }

    public abstract void close(T t) throws IOException;

    /* renamed from: loadResource */
    public abstract T mo994loadResource(Uri uri, Context context) throws FileNotFoundException;

    public LocalUriFetcher(Context context, Uri uri) {
        this.mContext = context;
        this.mUri = uri;
    }

    @Override // com.bumptech.glide.load.data.DataFetcher
    public final void loadData(Priority priority, DataFetcher.DataCallback<? super T> dataCallback) {
        try {
            T mo994loadResource = mo994loadResource(this.mUri, this.mContext);
            this.mData = mo994loadResource;
            dataCallback.onDataReady(mo994loadResource);
        } catch (FileNotFoundException e) {
            if (Log.isLoggable("LocalUriFetcher", 3)) {
                Log.d("LocalUriFetcher", "Failed to open Uri", e);
            }
            dataCallback.onLoadFailed(e);
        }
    }

    @Override // com.bumptech.glide.load.data.DataFetcher
    public void cleanup() {
        T t = this.mData;
        if (t != null) {
            try {
                close(t);
            } catch (IOException unused) {
            }
        }
    }

    @Override // com.bumptech.glide.load.data.DataFetcher
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }
}
