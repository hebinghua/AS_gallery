package com.miui.gallery.cloud.base;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import com.miui.gallery.cloud.AsyncUpDownloadService;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.Rom;

/* loaded from: classes.dex */
public abstract class AbstractSyncAdapter<T> {
    public final String TAG = getClass().getSimpleName();
    public final Context mContext;
    public AsyncUpDownloadService.SyncLock mSyncLock;

    public abstract long getBindingReason();

    public abstract boolean isAsynchronous();

    public abstract GallerySyncResult<T> onPerformSync(Account account, Bundle bundle, GalleryExtendedAuthToken galleryExtendedAuthToken) throws Exception;

    public AbstractSyncAdapter(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return this.mContext;
    }

    public final GallerySyncResult<T> startSync(Account account, Bundle bundle, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        GallerySyncResult<T> build;
        long currentTimeMillis = System.currentTimeMillis();
        SyncLogger.w("GallerySyncAdapterImpl", "%s sync executor start %d", this.TAG, Long.valueOf(currentTimeMillis));
        acquireLock();
        try {
            try {
                build = onPerformSync(account, bundle, galleryExtendedAuthToken);
            } catch (Exception e) {
                SyncLogger.e(this.TAG, e);
                build = new GallerySyncResult.Builder().setCode(GallerySyncCode.UNKNOWN).setException(e).build();
            }
            return build;
        } finally {
            SyncLogger.w("GallerySyncAdapterImpl", "%s sync executor finish,cost: %d", this.TAG, Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            releaseLock();
        }
    }

    public final void acquireLock() {
        synchronized (this) {
            if (this.mSyncLock == null) {
                this.mSyncLock = AsyncUpDownloadService.newSyncLock(this.TAG);
            }
            this.mSyncLock.acquire();
        }
    }

    public final void releaseLock() {
        synchronized (this) {
            AsyncUpDownloadService.SyncLock syncLock = this.mSyncLock;
            if (syncLock != null) {
                syncLock.release();
                this.mSyncLock = null;
            }
        }
    }

    public void finalize() throws Throwable {
        if (this.mSyncLock != null) {
            releaseLock();
            IllegalStateException illegalStateException = new IllegalStateException("not call #cleanup");
            if (Rom.IS_ALPHA) {
                throw illegalStateException;
            }
            DefaultLogger.e(this.TAG, illegalStateException);
        }
        super.finalize();
    }
}
