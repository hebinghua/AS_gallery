package com.miui.gallery.content;

import android.content.Context;
import androidx.loader.content.AsyncTaskLoader;

/* loaded from: classes.dex */
public abstract class ExtendedAsyncTaskLoader<D> extends AsyncTaskLoader<D> {
    public ExtendedCursorHelper mHelper;

    public ExtendedAsyncTaskLoader(Context context) {
        super(context);
        this.mHelper = new ExtendedCursorHelper();
    }

    @Override // androidx.loader.content.Loader
    public void onStopLoading() {
        super.onStopLoading();
        if (isReset()) {
            return;
        }
        this.mHelper.onStopLoading();
    }

    @Override // androidx.loader.content.Loader
    public void onStartLoading() {
        super.onStartLoading();
        this.mHelper.onStartLoading();
    }

    @Override // androidx.loader.content.Loader
    public void onContentChanged() {
        super.onContentChanged();
        if (!this.mHelper.shouldLoadInProcessStopped() || !takeContentChanged()) {
            return;
        }
        forceLoad();
    }
}
