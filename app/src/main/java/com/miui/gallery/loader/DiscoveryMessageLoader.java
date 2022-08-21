package com.miui.gallery.loader;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import com.miui.gallery.content.ExtendedAsyncTaskLoader;
import com.miui.gallery.discovery.DiscoveryMessageManager;
import com.miui.gallery.model.DiscoveryMessage;
import com.miui.gallery.provider.GalleryContract;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class DiscoveryMessageLoader extends ExtendedAsyncTaskLoader<ArrayList<DiscoveryMessage>> {
    public ForceLoadContentObserver mContentObserver;
    public Context mContext;
    public ArrayList<DiscoveryMessage> mDataSet;
    public int mMessageTypeMask;

    public DiscoveryMessageLoader(Context context, int i) {
        super(context);
        this.mContext = context.getApplicationContext();
        this.mContentObserver = new ForceLoadContentObserver(this, i);
        this.mMessageTypeMask = i;
        this.mContext.getContentResolver().registerContentObserver(GalleryContract.DiscoveryMessage.URI, true, this.mContentObserver);
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    /* renamed from: loadInBackground  reason: collision with other method in class */
    public ArrayList<DiscoveryMessage> mo1444loadInBackground() {
        return DiscoveryMessageManager.getInstance().loadMessage(this.mContext, this.mMessageTypeMask);
    }

    @Override // androidx.loader.content.Loader
    public final void deliverResult(ArrayList<DiscoveryMessage> arrayList) {
        if (isReset()) {
            if (arrayList == null) {
                return;
            }
            arrayList.clear();
            return;
        }
        ArrayList<DiscoveryMessage> arrayList2 = this.mDataSet;
        this.mDataSet = arrayList;
        if (isStarted()) {
            super.deliverResult((DiscoveryMessageLoader) arrayList);
        }
        if (arrayList2 == null || arrayList2 == arrayList) {
            return;
        }
        arrayList2.clear();
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    public final void onCanceled(ArrayList<DiscoveryMessage> arrayList) {
        if (arrayList != null) {
            arrayList.clear();
        }
    }

    @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
    public final void onStartLoading() {
        ArrayList<DiscoveryMessage> arrayList = this.mDataSet;
        if (arrayList != null) {
            deliverResult(arrayList);
        }
        if (takeContentChanged() || this.mDataSet == null) {
            forceLoad();
        }
    }

    @Override // androidx.loader.content.Loader
    public final void onReset() {
        super.onReset();
        onStopLoading();
        this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
        ArrayList<DiscoveryMessage> arrayList = this.mDataSet;
        if (arrayList != null) {
            arrayList.clear();
            this.mDataSet = null;
        }
    }

    @Override // androidx.loader.content.Loader
    public void onAbandon() {
        super.onAbandon();
        this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
    }

    /* loaded from: classes2.dex */
    public static final class ForceLoadContentObserver extends ContentObserver {
        public WeakReference<DiscoveryMessageLoader> mLoaderRef;
        public int mMessageTypeMask;

        public ForceLoadContentObserver(DiscoveryMessageLoader discoveryMessageLoader, int i) {
            super(new Handler(Looper.myLooper()));
            this.mMessageTypeMask = i;
            this.mLoaderRef = new WeakReference<>(discoveryMessageLoader);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            if (this.mLoaderRef.get() != null) {
                this.mLoaderRef.get().onContentChanged();
            }
        }
    }
}
