package com.xiaomi.micloudsdk.stat;

import android.os.Looper;

/* loaded from: classes3.dex */
public class MiCloudNetEventStatInjector {
    public IMiCloudDownloadFileNetEventStatCallback mIMiCloudDownloadFileNetEventStatCallback;
    public IMiCloudNetEventStatCallback mIMiCloudStatCallback;
    public boolean mIsDownloadCallbackInitialized;
    public boolean mIsInitialized;

    public MiCloudNetEventStatInjector() {
        this.mIsInitialized = false;
        this.mIsDownloadCallbackInitialized = false;
    }

    public static MiCloudNetEventStatInjector getInstance() {
        return MiCloudNetEventStatInjectorHolder.instance;
    }

    public void initDownloadFile(IMiCloudDownloadFileNetEventStatCallback iMiCloudDownloadFileNetEventStatCallback) {
        if (!Looper.getMainLooper().getThread().equals(Thread.currentThread())) {
            throw new IllegalStateException("initDownloadFile() must be invoked in main thread");
        }
        if (this.mIsDownloadCallbackInitialized) {
            return;
        }
        this.mIMiCloudDownloadFileNetEventStatCallback = iMiCloudDownloadFileNetEventStatCallback;
        this.mIsDownloadCallbackInitialized = true;
    }

    public void addNetSuccessEvent(NetSuccessStatParam netSuccessStatParam) {
        IMiCloudNetEventStatCallback iMiCloudNetEventStatCallback = this.mIMiCloudStatCallback;
        if (iMiCloudNetEventStatCallback != null) {
            iMiCloudNetEventStatCallback.onAddNetSuccessEvent(netSuccessStatParam);
        }
    }

    public void addNetFailedEvent(NetFailedStatParam netFailedStatParam) {
        IMiCloudNetEventStatCallback iMiCloudNetEventStatCallback = this.mIMiCloudStatCallback;
        if (iMiCloudNetEventStatCallback != null) {
            iMiCloudNetEventStatCallback.onAddNetFailedEvent(netFailedStatParam);
        }
    }

    public boolean isGetDownloadFileRequestUrl(String str, String str2) {
        IMiCloudDownloadFileNetEventStatCallback iMiCloudDownloadFileNetEventStatCallback = this.mIMiCloudDownloadFileNetEventStatCallback;
        return iMiCloudDownloadFileNetEventStatCallback != null && iMiCloudDownloadFileNetEventStatCallback.isGetDownloadFileRequestUrl(str, str2);
    }

    public void addGetDownloadFileUrlsFailedEvent(GetDownloadFileUrlFailedStatParam getDownloadFileUrlFailedStatParam) {
        IMiCloudDownloadFileNetEventStatCallback iMiCloudDownloadFileNetEventStatCallback = this.mIMiCloudDownloadFileNetEventStatCallback;
        if (iMiCloudDownloadFileNetEventStatCallback != null) {
            iMiCloudDownloadFileNetEventStatCallback.onAddGetDownloadFileUrlsFailedEvent(getDownloadFileUrlFailedStatParam);
        }
    }

    public void addAddDownloadFileFailedEvent(DownloadFileFailedStatParam downloadFileFailedStatParam) {
        IMiCloudDownloadFileNetEventStatCallback iMiCloudDownloadFileNetEventStatCallback = this.mIMiCloudDownloadFileNetEventStatCallback;
        if (iMiCloudDownloadFileNetEventStatCallback != null) {
            iMiCloudDownloadFileNetEventStatCallback.onAddDownloadFileFailedEvent(downloadFileFailedStatParam);
        }
    }

    /* loaded from: classes3.dex */
    public static class MiCloudNetEventStatInjectorHolder {
        public static final MiCloudNetEventStatInjector instance = new MiCloudNetEventStatInjector();
    }
}
