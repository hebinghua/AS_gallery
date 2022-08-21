package com.miui.gallery.adapter;

import android.content.Context;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public abstract class BaseMediaSyncStateAdapter<M, S> extends BaseMediaAdapter<M, S, BaseViewHolder> {
    public static volatile boolean sIsGalleryCloudSyncable;
    public static volatile boolean sIsLoginAccount;
    public static Runnable sUpdateRunnable;
    public SyncStateDisplay$DisplayScene mShowScene;
    public int mSyncStateDisplayOptions;

    public BaseMediaSyncStateAdapter(Context context, SyncStateDisplay$DisplayScene syncStateDisplay$DisplayScene, int i) {
        super(context);
        this.mShowScene = syncStateDisplay$DisplayScene;
        this.mSyncStateDisplayOptions = i;
        updateGalleryCloudSyncableState();
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public void updateGalleryCloudSyncableState() {
        if (sUpdateRunnable == null) {
            sUpdateRunnable = new SyncStateRunnable(this);
        }
        ThreadManager.getWorkHandler().removeCallbacks(sUpdateRunnable);
        ThreadManager.getWorkHandler().postDelayed(sUpdateRunnable, 100L);
    }

    public int getSyncStateInternal(int i) {
        return !sIsGalleryCloudSyncable ? (!sIsLoginAccount || i != 3) ? Integer.MAX_VALUE : 4 : i;
    }

    /* loaded from: classes.dex */
    public static class SyncStateRunnable implements Runnable {
        public final WeakReference<BaseMediaSyncStateAdapter> mAdapterRef;

        public SyncStateRunnable(BaseMediaSyncStateAdapter baseMediaSyncStateAdapter) {
            this.mAdapterRef = new WeakReference<>(baseMediaSyncStateAdapter);
        }

        @Override // java.lang.Runnable
        public void run() {
            BaseMediaSyncStateAdapter baseMediaSyncStateAdapter = this.mAdapterRef.get();
            if (baseMediaSyncStateAdapter != null) {
                Context applicationContext = baseMediaSyncStateAdapter.mContext.getApplicationContext();
                boolean unused = BaseMediaSyncStateAdapter.sIsLoginAccount = SyncUtil.existXiaomiAccount(applicationContext);
                boolean z = false;
                if (BaseMediaSyncStateAdapter.sIsLoginAccount) {
                    z = SyncUtil.isGalleryCloudSyncable(applicationContext);
                }
                if (BaseMediaSyncStateAdapter.sIsGalleryCloudSyncable == z) {
                    return;
                }
                boolean unused2 = BaseMediaSyncStateAdapter.sIsGalleryCloudSyncable = z;
                notifyStateChanged();
            }
        }

        public final void notifyStateChanged() {
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.adapter.BaseMediaSyncStateAdapter.SyncStateRunnable.1
                @Override // java.lang.Runnable
                public void run() {
                    BaseMediaSyncStateAdapter baseMediaSyncStateAdapter = (BaseMediaSyncStateAdapter) SyncStateRunnable.this.mAdapterRef.get();
                    if (baseMediaSyncStateAdapter != null) {
                        baseMediaSyncStateAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
