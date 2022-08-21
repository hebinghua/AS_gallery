package com.nexstreaming.nexeditorsdk.service;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes3.dex */
public class NexInstalledAssetItem {
    public String assetName;
    public long assetVersion;
    public String categoryName;
    public long expireTime;
    public String id;
    public int index;
    public long installedTime;
    public long sdkLevel;
    public String thumbUrl;

    public NexInstalledAssetItem(int i, String str, String str2, String str3, String str4) {
        this.index = i;
        this.id = str;
        this.assetName = str2;
        this.categoryName = str3;
        this.thumbUrl = str4;
        this.installedTime = 0L;
        this.expireTime = 0L;
        this.sdkLevel = 2L;
        this.assetVersion = 1L;
    }

    public NexInstalledAssetItem(int i, String str, String str2, String str3, String str4, long j, long j2, int i2, int i3) {
        this.index = i;
        this.id = str;
        this.assetName = str2;
        this.categoryName = str3;
        this.thumbUrl = str4;
        this.installedTime = j;
        this.expireTime = j2;
        this.sdkLevel = i2;
        this.assetVersion = i3;
    }

    public String toString() {
        return "nexAssetTransferData{index=" + this.index + ", id='" + this.id + CoreConstants.SINGLE_QUOTE_CHAR + ", assetName='" + this.assetName + CoreConstants.SINGLE_QUOTE_CHAR + ", categoryName='" + this.categoryName + CoreConstants.SINGLE_QUOTE_CHAR + ", thumbUrl='" + this.thumbUrl + CoreConstants.SINGLE_QUOTE_CHAR + '}';
    }
}
