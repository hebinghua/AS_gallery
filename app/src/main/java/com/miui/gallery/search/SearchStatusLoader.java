package com.miui.gallery.search;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.miui.gallery.content.ExtendedAsyncTaskLoader;
import com.miui.gallery.provider.deprecated.GalleryCloudProvider;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.trackers.ConstraintListener;
import com.miui.gallery.trackers.NetworkState;
import com.miui.gallery.trackers.Trackers;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.deprecated.Preference;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class SearchStatusLoader extends ExtendedAsyncTaskLoader<Integer> {
    public BroadcastReceiver mBroadCastReceiver;
    public ConnectivityListener mConnectivityListener;
    public final Handler mHandler;
    public int mLastStatus;
    public final WeakReference<StatusReportDelegate> mStatusReportDelegateRef;
    public ContentObserver mUploadingObserver;

    /* loaded from: classes2.dex */
    public interface StatusReportDelegate {
        boolean shouldReportStatus(int i);
    }

    public SearchStatusLoader(Context context, StatusReportDelegate statusReportDelegate) {
        super(context);
        this.mLastStatus = -1;
        this.mHandler = new Handler();
        this.mStatusReportDelegateRef = new WeakReference<>(statusReportDelegate);
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    /* renamed from: loadInBackground */
    public Integer mo1444loadInBackground() {
        int i = 2;
        if (!shouldReportStatus(2) || !BaseNetworkUtils.isActiveNetworkMetered()) {
            if (shouldReportStatus(1) && !BaseNetworkUtils.isNetworkConnected()) {
                i = 1;
            } else if (!shouldReportStatus(3) || SyncUtil.existXiaomiAccount(getContext())) {
                if (!shouldReportStatus(4) || SyncUtil.isGalleryCloudSyncable(getContext())) {
                    if (!shouldReportStatus(10) || Preference.sIsFirstSynced()) {
                        i = 0;
                    } else if (SyncUtil.isGalleryCloudSyncable(getContext())) {
                        i = 10;
                    }
                }
                i = 4;
            } else {
                i = 3;
            }
        }
        return Integer.valueOf(i);
    }

    public boolean shouldReportStatus(int i) {
        WeakReference<StatusReportDelegate> weakReference = this.mStatusReportDelegateRef;
        StatusReportDelegate statusReportDelegate = weakReference != null ? weakReference.get() : null;
        return statusReportDelegate == null || statusReportDelegate.shouldReportStatus(i);
    }

    @Override // androidx.loader.content.Loader
    public void deliverResult(Integer num) {
        this.mLastStatus = num.intValue();
        if (isStarted()) {
            super.deliverResult((SearchStatusLoader) num);
        }
    }

    @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
    public void onStartLoading() {
        super.onStartLoading();
        int i = this.mLastStatus;
        if (i != -1) {
            deliverResult(Integer.valueOf(i));
        }
        if (this.mConnectivityListener == null) {
            this.mConnectivityListener = new ConnectivityListener();
            Trackers.getNetworkStateTracker().registerListener(this.mConnectivityListener);
        }
        if (this.mUploadingObserver == null) {
            this.mUploadingObserver = new ContentObserver(this.mHandler) { // from class: com.miui.gallery.search.SearchStatusLoader.1
                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    SearchStatusLoader.this.onContentChanged();
                }
            };
            getContext().getContentResolver().registerContentObserver(GalleryCloudProvider.UPLOAD_STATE_URI, true, this.mUploadingObserver);
        }
        if (!Preference.sIsFirstSynced() && this.mBroadCastReceiver == null) {
            this.mBroadCastReceiver = new BroadcastReceiver() { // from class: com.miui.gallery.search.SearchStatusLoader.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if ("com.miui.gallery.action.FIRST_SYNC_FINISHED".equals(intent.getAction()) || "com.miui.gallery.action.AI_ALBUM_SWITCH_CHANGE".equals(intent.getAction())) {
                        SearchStatusLoader.this.onContentChanged();
                    }
                }
            };
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
            localBroadcastManager.registerReceiver(this.mBroadCastReceiver, new IntentFilter("com.miui.gallery.action.FIRST_SYNC_FINISHED"));
            localBroadcastManager.registerReceiver(this.mBroadCastReceiver, new IntentFilter("com.miui.gallery.action.AI_ALBUM_SWITCH_CHANGE"));
        }
        if (takeContentChanged() || this.mLastStatus == -1) {
            forceLoad();
        }
    }

    @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
    public void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
        if (this.mConnectivityListener != null) {
            Trackers.getNetworkStateTracker().unregisterListener(this.mConnectivityListener);
            this.mConnectivityListener = null;
        }
        if (this.mUploadingObserver != null) {
            getContext().getContentResolver().unregisterContentObserver(this.mUploadingObserver);
        }
        if (this.mBroadCastReceiver != null) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(this.mBroadCastReceiver);
            this.mBroadCastReceiver = null;
        }
    }

    @Override // androidx.loader.content.Loader
    public void onReset() {
        super.onReset();
        onStopLoading();
    }

    /* loaded from: classes2.dex */
    public class ConnectivityListener implements ConstraintListener<NetworkState> {
        public Boolean mLastConnected;

        public ConnectivityListener() {
        }

        @Override // com.miui.gallery.trackers.ConstraintListener
        public void onConstraintChanged(NetworkState networkState) {
            if (networkState == null) {
                return;
            }
            boolean isConnected = networkState.isConnected();
            SearchLog.d("SearchStatusLoader", "lastConnected: %b, currConnected: %b", this.mLastConnected, Boolean.valueOf(isConnected));
            Boolean bool = this.mLastConnected;
            if (bool != null && bool.booleanValue() == BaseNetworkUtils.isNetworkConnected()) {
                return;
            }
            this.mLastConnected = Boolean.valueOf(isConnected);
            SearchStatusLoader.this.onContentChanged();
        }
    }
}
