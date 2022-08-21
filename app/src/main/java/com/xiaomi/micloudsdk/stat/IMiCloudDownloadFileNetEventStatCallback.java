package com.xiaomi.micloudsdk.stat;

/* loaded from: classes3.dex */
public interface IMiCloudDownloadFileNetEventStatCallback {
    boolean isGetDownloadFileRequestUrl(String str, String str2);

    void onAddDownloadFileFailedEvent(DownloadFileFailedStatParam downloadFileFailedStatParam);

    void onAddGetDownloadFileUrlsFailedEvent(GetDownloadFileUrlFailedStatParam getDownloadFileUrlFailedStatParam);
}
