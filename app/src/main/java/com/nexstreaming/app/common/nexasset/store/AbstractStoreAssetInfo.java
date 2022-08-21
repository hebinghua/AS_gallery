package com.nexstreaming.app.common.nexasset.store;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/* loaded from: classes3.dex */
public abstract class AbstractStoreAssetInfo implements StoreAssetInfo {
    private int minVersion;
    private int packageVersion;

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public String getAssetDescription() {
        return null;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public int getAssetFilesize() {
        return 0;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public String getAssetId() {
        return null;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public int getAssetIndex() {
        return 0;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public String getAssetPackageDownloadURL() {
        return null;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public String getAssetThumbnailURL() {
        return null;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public String getAssetThumbnailURL_L() {
        return null;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public String getAssetThumbnailURL_S() {
        return null;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public String getAssetTitle() {
        return null;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public String getAssetVideoURL() {
        return null;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public String getCategoryAliasName() {
        return null;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public String getCategoryIconURL() {
        return null;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public int getCategoryIndex() {
        return 0;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public long getExpireTime() {
        return 0L;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public String getPriceType() {
        return null;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public int getSubCategoryIndex() {
        return 0;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public List<String> getThumbnailPaths() {
        return null;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public int getUpdateTime() {
        return 0;
    }

    public AbstractStoreAssetInfo(int i, int i2) {
        this.minVersion = i;
        this.packageVersion = i2;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public Map<String, String> getSubCategoryNameMap() {
        return Collections.emptyMap();
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public Map<String, String> getAssetNameMap() {
        return Collections.emptyMap();
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public int getAssetVersion() {
        return this.packageVersion;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public int getAssetScopeVersion() {
        return this.minVersion;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
    public Map<String, String> getAssetDescriptionMap() {
        return Collections.emptyMap();
    }
}
