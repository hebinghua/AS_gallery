package com.miui.gallery.ui.album.main.usecase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.base_optimization.clean.LifecycleUseCase;
import com.miui.gallery.provider.deprecated.GalleryCloudProvider;
import com.miui.gallery.search.SearchStatusLoader;
import com.miui.gallery.trackers.ConstraintListener;
import com.miui.gallery.trackers.NetworkState;
import com.miui.gallery.trackers.Trackers;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.RxUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

/* loaded from: classes2.dex */
public class SearchStatusListenerUsecase extends LifecycleUseCase<Integer, Void> {
    public boolean isFirst;
    public BroadcastReceiver mBroadCastReceiver;
    public ConnectivityListener mConnectivityListener;
    public final WeakReference<Context> mContext;
    public final Handler mHandler;
    public int mLastStatus;
    public final WeakReference<SearchStatusLoader.StatusReportDelegate> mStatusReportDelegateRef;
    public ContentObserver mUploadingObserver;

    public static /* synthetic */ void $r8$lambda$X9u3mPFaTREdTZXUP06PVUV7wZg(SearchStatusListenerUsecase searchStatusListenerUsecase, Integer num) {
        searchStatusListenerUsecase.lambda$onContentChanged$0(num);
    }

    public SearchStatusListenerUsecase(Context context, SearchStatusLoader.StatusReportDelegate statusReportDelegate) {
        super(RxGalleryExecutors.getInstance().getUserThreadExecutor(), RxGalleryExecutors.getInstance().getUiThreadExecutor());
        this.mLastStatus = -1;
        this.mHandler = new Handler();
        this.isFirst = true;
        this.mContext = new WeakReference<>(context);
        this.mStatusReportDelegateRef = new WeakReference<>(statusReportDelegate);
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<Integer> buildUseCaseFlowable(Void r1) {
        if (this.isFirst) {
            initSelf();
        }
        return Flowable.fromCallable(new Callable<Integer>() { // from class: com.miui.gallery.ui.album.main.usecase.SearchStatusListenerUsecase.1
            {
                SearchStatusListenerUsecase.this = this;
            }

            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public Integer mo1598call() throws Exception {
                int i = 2;
                if (!SearchStatusListenerUsecase.this.shouldReportStatus(2) || !BaseNetworkUtils.isActiveNetworkMetered()) {
                    if (SearchStatusListenerUsecase.this.shouldReportStatus(1) && !BaseNetworkUtils.isNetworkConnected()) {
                        i = 1;
                    } else if (!SearchStatusListenerUsecase.this.shouldReportStatus(3) || SyncUtil.existXiaomiAccount(SearchStatusListenerUsecase.this.getContext())) {
                        if (!SearchStatusListenerUsecase.this.shouldReportStatus(4) || SyncUtil.isGalleryCloudSyncable(SearchStatusListenerUsecase.this.getContext())) {
                            if (!SearchStatusListenerUsecase.this.shouldReportStatus(10) || Preference.sIsFirstSynced()) {
                                i = 0;
                            } else if (SyncUtil.isGalleryCloudSyncable(SearchStatusListenerUsecase.this.getContext())) {
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
        });
    }

    public void deliverResult(Integer num) {
        this.mLastStatus = num.intValue();
        if (!this.mLastSubscribe.isDisposed()) {
            this.mLastSubscribe.onNext(num);
        }
    }

    public boolean shouldReportStatus(int i) {
        WeakReference<SearchStatusLoader.StatusReportDelegate> weakReference = this.mStatusReportDelegateRef;
        SearchStatusLoader.StatusReportDelegate statusReportDelegate = weakReference != null ? weakReference.get() : null;
        return statusReportDelegate == null || statusReportDelegate.shouldReportStatus(i);
    }

    public final void initSelf() {
        int i = this.mLastStatus;
        if (i != -1) {
            deliverResult(Integer.valueOf(i));
        }
        if (this.mConnectivityListener == null) {
            this.mConnectivityListener = new ConnectivityListener();
            Trackers.getNetworkStateTracker().registerListener(this.mConnectivityListener);
        }
        if (this.mUploadingObserver == null) {
            this.mUploadingObserver = new ContentObserver(this.mHandler) { // from class: com.miui.gallery.ui.album.main.usecase.SearchStatusListenerUsecase.2
                {
                    SearchStatusListenerUsecase.this = this;
                }

                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    SearchStatusListenerUsecase.this.onContentChanged();
                }
            };
            getContext().getContentResolver().registerContentObserver(GalleryCloudProvider.UPLOAD_STATE_URI, true, this.mUploadingObserver);
        }
        if (!Preference.sIsFirstSynced() && this.mBroadCastReceiver == null) {
            this.mBroadCastReceiver = new BroadcastReceiver() { // from class: com.miui.gallery.ui.album.main.usecase.SearchStatusListenerUsecase.3
                {
                    SearchStatusListenerUsecase.this = this;
                }

                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if ("com.miui.gallery.action.FIRST_SYNC_FINISHED".equals(intent.getAction()) || "com.miui.gallery.action.AI_ALBUM_SWITCH_CHANGE".equals(intent.getAction())) {
                        SearchStatusListenerUsecase.this.onContentChanged();
                    }
                }
            };
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
            localBroadcastManager.registerReceiver(this.mBroadCastReceiver, new IntentFilter("com.miui.gallery.action.FIRST_SYNC_FINISHED"));
            localBroadcastManager.registerReceiver(this.mBroadCastReceiver, new IntentFilter("com.miui.gallery.action.AI_ALBUM_SWITCH_CHANGE"));
        }
        this.isFirst = false;
    }

    public final void onContentChanged() {
        try {
            buildUseCaseFlowable((Void) null).compose(RxUtils.ioAndMainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.ui.album.main.usecase.SearchStatusListenerUsecase$$ExternalSyntheticLambda0
                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    SearchStatusListenerUsecase.$r8$lambda$X9u3mPFaTREdTZXUP06PVUV7wZg(SearchStatusListenerUsecase.this, (Integer) obj);
                }
            }, SearchStatusListenerUsecase$$ExternalSyntheticLambda1.INSTANCE);
        } catch (Exception e) {
            DefaultLogger.e("SearchStatusListenerUsecase", e.getMessage());
        }
    }

    public /* synthetic */ void lambda$onContentChanged$0(Integer num) throws Exception {
        this.mLastSubscribe.onNext(num);
    }

    public final Context getContext() {
        WeakReference<Context> weakReference = this.mContext;
        return weakReference != null ? weakReference.get() : GalleryApp.sGetAndroidContext();
    }

    @Override // com.miui.gallery.base_optimization.clean.LifecycleUseCase
    public void onDestroy() {
        super.onDestroy();
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

    /* loaded from: classes2.dex */
    public class ConnectivityListener implements ConstraintListener<NetworkState> {
        public Boolean mLastConnected;

        public ConnectivityListener() {
            SearchStatusListenerUsecase.this = r1;
        }

        @Override // com.miui.gallery.trackers.ConstraintListener
        public void onConstraintChanged(NetworkState networkState) {
            if (networkState == null) {
                return;
            }
            Boolean bool = this.mLastConnected;
            if (bool != null && bool.booleanValue() == networkState.isConnected()) {
                return;
            }
            this.mLastConnected = Boolean.valueOf(networkState.isConnected());
            SearchStatusListenerUsecase.this.onContentChanged();
        }
    }
}
