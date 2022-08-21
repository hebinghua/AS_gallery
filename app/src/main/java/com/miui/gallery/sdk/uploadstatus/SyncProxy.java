package com.miui.gallery.sdk.uploadstatus;

import android.content.Context;

/* loaded from: classes2.dex */
public class SyncProxy {
    public static SyncProxy sInstance;
    public Context mContext;
    public UploadStatusProxy mUploadStatusProxy;
    public UriAdapter mUriAdapter;

    public static synchronized SyncProxy getInstance() {
        SyncProxy syncProxy;
        synchronized (SyncProxy.class) {
            if (sInstance == null) {
                sInstance = new SyncProxy();
            }
            syncProxy = sInstance;
        }
        return syncProxy;
    }

    public synchronized UploadStatusProxy getUploadStatusProxy() {
        if (this.mUploadStatusProxy == null) {
            this.mUploadStatusProxy = new UploadStatusProxy();
        }
        return this.mUploadStatusProxy;
    }

    public void init(Context context, UriAdapter uriAdapter) {
        this.mContext = context;
        this.mUriAdapter = uriAdapter;
    }

    public UriAdapter getUriAdapter() {
        return this.mUriAdapter;
    }
}
