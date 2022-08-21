package com.xiaomi.micloudsdk.stat;

@Deprecated
/* loaded from: classes3.dex */
public class MiCloudStatManager {
    public IMiCloudStatCallback mCldStatCallback;
    public boolean mEnable;
    public boolean mIsInitialized;

    public MiCloudStatManager() {
        this.mEnable = false;
        this.mIsInitialized = false;
    }

    public static MiCloudStatManager getInstance() {
        return Holder._instance;
    }

    public void init(IMiCloudStatCallback iMiCloudStatCallback) {
        this.mCldStatCallback = iMiCloudStatCallback;
        this.mEnable = true;
        iMiCloudStatCallback.onInitialize();
        this.mIsInitialized = true;
        this.mCldStatCallback.onSetUploadPolicy();
        this.mCldStatCallback.onSetEventFilter();
        this.mCldStatCallback.onEnableAutoRecord();
    }

    public void addHttpEvent(String str, long j, long j2, int i, String str2) {
        IMiCloudStatCallback iMiCloudStatCallback;
        if (!this.mEnable || !this.mIsInitialized || (iMiCloudStatCallback = this.mCldStatCallback) == null) {
            return;
        }
        iMiCloudStatCallback.onAddHttpEvent(str, j, j2, i, str2);
    }

    /* loaded from: classes3.dex */
    public static class Holder {
        public static final MiCloudStatManager _instance = new MiCloudStatManager();
    }
}
