package com.nexstreaming.app.common.nexasset.assetpackage;

import java.util.Map;

/* compiled from: AssetInfo.java */
/* loaded from: classes3.dex */
public interface b {
    a getAssetCategory();

    String getAssetId();

    int getAssetIdx();

    Map<String, String> getAssetName();

    d getAssetSubCategory();

    long getExpireTime();

    InstallSourceType getInstallSourceType();

    long getInstalledTime();

    int getMinVersion();

    int getPackageVersion();

    String getPriceType();

    String getThumbPath();

    String getThumbUrl();
}
