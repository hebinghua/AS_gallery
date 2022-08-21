package com.miui.gallery.loader;

import android.content.Context;
import com.miui.gallery.content.ExtendedAsyncTaskLoader;
import com.miui.gallery.model.BaseDataSet;

/* loaded from: classes2.dex */
public abstract class BaseLoader extends ExtendedAsyncTaskLoader<BaseDataSet> {
    public OnLoadCompleteListener mBackgroundLoadListener;
    public BaseDataSet mDataSet;

    /* loaded from: classes2.dex */
    public interface OnLoadCompleteListener {
        void onLoadComplete(BaseLoader baseLoader, BaseDataSet baseDataSet);
    }

    public BaseLoader(Context context) {
        super(context);
    }

    @Override // androidx.loader.content.Loader
    public final void deliverResult(BaseDataSet baseDataSet) {
        if (isReset()) {
            if (baseDataSet == null) {
                return;
            }
            baseDataSet.release();
            return;
        }
        BaseDataSet baseDataSet2 = this.mDataSet;
        this.mDataSet = baseDataSet;
        if (isStarted()) {
            super.deliverResult((BaseLoader) baseDataSet);
        } else {
            OnLoadCompleteListener onLoadCompleteListener = this.mBackgroundLoadListener;
            if (onLoadCompleteListener != null) {
                onLoadCompleteListener.onLoadComplete(this, baseDataSet);
            }
        }
        if (baseDataSet2 == null || baseDataSet2 == baseDataSet) {
            return;
        }
        baseDataSet2.release();
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    public final void onCanceled(BaseDataSet baseDataSet) {
        OnLoadCompleteListener onLoadCompleteListener;
        if (baseDataSet != null) {
            baseDataSet.release();
        }
        if (isStarted() || (onLoadCompleteListener = this.mBackgroundLoadListener) == null) {
            return;
        }
        onLoadCompleteListener.onLoadComplete(this, null);
    }

    @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
    public final void onStartLoading() {
        BaseDataSet baseDataSet = this.mDataSet;
        if (baseDataSet != null) {
            deliverResult(baseDataSet);
        }
        if (takeContentChanged() || this.mDataSet == null) {
            forceLoad();
        }
    }

    @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
    public final void onStopLoading() {
        cancelLoad();
    }

    @Override // androidx.loader.content.Loader
    public final void onReset() {
        super.onReset();
        onStopLoading();
        BaseDataSet baseDataSet = this.mDataSet;
        if (baseDataSet != null) {
            baseDataSet.release();
            this.mDataSet = null;
        }
    }

    public void setBackgroundLoadListener(OnLoadCompleteListener onLoadCompleteListener) {
        this.mBackgroundLoadListener = onLoadCompleteListener;
    }
}
