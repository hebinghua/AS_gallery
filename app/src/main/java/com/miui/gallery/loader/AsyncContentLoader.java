package com.miui.gallery.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.OperationCanceledException;
import androidx.loader.content.Loader;
import com.miui.gallery.content.ExtendedAsyncTaskLoader;
import com.miui.gallery.util.BaseMiscUtil;
import java.io.Closeable;

/* loaded from: classes2.dex */
public class AsyncContentLoader<T extends Closeable> extends ExtendedAsyncTaskLoader<T> {
    public CancellationSignal mCancellationSignal;
    public CursorConvertCallback<T> mCursorConvertCallback;
    public T mData;
    public boolean mIsObserversRegistered;
    public final Loader<T>.ForceLoadContentObserver mObserver;
    public String[] mProjection;
    public String mSelection;
    public String[] mSelectionArgs;
    public String mSortOrder;
    public Uri mUri;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.loader.content.Loader
    public /* bridge */ /* synthetic */ void deliverResult(Object obj) {
        deliverResult((AsyncContentLoader<T>) ((Closeable) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.loader.content.AsyncTaskLoader
    public /* bridge */ /* synthetic */ void onCanceled(Object obj) {
        onCanceled((AsyncContentLoader<T>) ((Closeable) obj));
    }

    public AsyncContentLoader(Context context, CursorConvertCallback<T> cursorConvertCallback) {
        super(context);
        this.mObserver = new Loader.ForceLoadContentObserver();
        this.mCursorConvertCallback = cursorConvertCallback;
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    /* renamed from: loadInBackground */
    public final T mo1444loadInBackground() {
        Cursor cursor;
        T mo1129convert;
        synchronized (this) {
            if (isLoadInBackgroundCanceled()) {
                throw new OperationCanceledException();
            }
            this.mCancellationSignal = new CancellationSignal();
        }
        try {
            cursor = getContext().getContentResolver().query(this.mUri, this.mProjection, this.mSelection, this.mSelectionArgs, this.mSortOrder, this.mCancellationSignal);
            if (cursor != null) {
                try {
                    mo1129convert = this.mCursorConvertCallback.mo1129convert(cursor);
                } catch (Throwable th) {
                    th = th;
                    BaseMiscUtil.closeSilently(cursor);
                    synchronized (this) {
                        this.mCancellationSignal = null;
                    }
                    throw th;
                }
            } else {
                mo1129convert = null;
            }
            BaseMiscUtil.closeSilently(cursor);
            synchronized (this) {
                this.mCancellationSignal = null;
            }
            return mo1129convert;
        } catch (Throwable th2) {
            th = th2;
            cursor = null;
        }
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    public void cancelLoadInBackground() {
        super.cancelLoadInBackground();
        synchronized (this) {
            CancellationSignal cancellationSignal = this.mCancellationSignal;
            if (cancellationSignal != null) {
                cancellationSignal.cancel();
            }
        }
    }

    public void deliverResult(T t) {
        if (isReset()) {
            if (t == null) {
                return;
            }
            BaseMiscUtil.closeSilently(t);
            return;
        }
        T t2 = this.mData;
        this.mData = t;
        if (isStarted()) {
            super.deliverResult((AsyncContentLoader<T>) t);
        }
        if (t2 == null || t2 == t) {
            return;
        }
        BaseMiscUtil.closeSilently(t2);
    }

    @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
    public void onStartLoading() {
        T t = this.mData;
        if (t != null) {
            deliverResult((AsyncContentLoader<T>) t);
        }
        if (takeContentChanged() || this.mData == null) {
            forceLoad();
        }
        registerContentObservers();
    }

    @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
    public void onStopLoading() {
        cancelLoad();
    }

    public void onCanceled(T t) {
        if (t != null) {
            BaseMiscUtil.closeSilently(t);
        }
    }

    @Override // androidx.loader.content.Loader
    public void onReset() {
        super.onReset();
        onStopLoading();
        unregisterContentObservers();
        T t = this.mData;
        if (t != null) {
            BaseMiscUtil.closeSilently(t);
        }
        this.mData = null;
    }

    @Override // androidx.loader.content.Loader
    public void onAbandon() {
        super.onAbandon();
        unregisterContentObservers();
    }

    public void setUri(Uri uri) {
        this.mUri = uri;
    }

    public void setProjection(String[] strArr) {
        this.mProjection = strArr;
    }

    public void setSelection(String str) {
        this.mSelection = str;
    }

    public final void registerContentObservers() {
        if (this.mIsObserversRegistered) {
            return;
        }
        this.mIsObserversRegistered = true;
        getContext().getContentResolver().registerContentObserver(this.mUri, true, this.mObserver);
    }

    public final void unregisterContentObservers() {
        if (!this.mIsObserversRegistered) {
            return;
        }
        this.mIsObserversRegistered = false;
        getContext().getContentResolver().unregisterContentObserver(this.mObserver);
    }
}
