package com.nexstreaming.app.common.nexasset.assetpackage.db;

import ch.qos.logback.core.CoreConstants;
import com.nexstreaming.app.common.nexasset.assetpackage.InstallSourceType;
import com.nexstreaming.app.common.nexasset.assetpackage.a;
import com.nexstreaming.app.common.nexasset.assetpackage.d;
import com.nexstreaming.app.common.norm.b;
import java.io.File;
import java.util.Map;

/* loaded from: classes3.dex */
public class AssetPackageRecord extends b implements com.nexstreaming.app.common.nexasset.assetpackage.b {
    public long _id;
    public Map<String, String> assetDesc;
    @b.e
    @b.g
    public String assetId;
    public int assetIdx;
    public Map<String, String> assetName;
    public String assetUrl;
    public CategoryRecord category;
    @b.a(a = 12)
    public long expireTime;
    @b.d
    @b.c
    public InstallSourceRecord installSource;
    @b.a(a = 12)
    public long installedTime;
    public String localPath;
    @b.a(a = 13)
    public int minVersion;
    public String packageURI;
    @b.a(a = 13)
    public int packageVersion;
    public String priceType;
    public SubCategoryRecord subCategory;
    public String thumbPath;
    public String thumbUrl;

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.b
    public int getAssetIdx() {
        return this.assetIdx;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.b
    public String getAssetId() {
        return this.assetId;
    }

    public String getAssetUrl() {
        return this.assetUrl;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.b
    public String getThumbUrl() {
        return this.thumbUrl;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.b
    public String getThumbPath() {
        return this.thumbPath;
    }

    public File getLocalPath() {
        if (this.localPath == null) {
            return null;
        }
        return new File(this.localPath);
    }

    public String getPackageURI() {
        return this.packageURI;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.b
    public String getPriceType() {
        return this.priceType;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.b
    public a getAssetCategory() {
        return this.category;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.b
    public d getAssetSubCategory() {
        return this.subCategory;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.b
    public Map<String, String> getAssetName() {
        return this.assetName;
    }

    public Map<String, String> getAssetDesc() {
        return this.assetDesc;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.b
    public InstallSourceType getInstallSourceType() {
        InstallSourceRecord installSourceRecord = this.installSource;
        if (installSourceRecord == null) {
            return null;
        }
        return installSourceRecord.installSourceType;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.b
    public long getInstalledTime() {
        return this.installedTime;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.b
    public long getExpireTime() {
        return this.expireTime;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.b
    public int getMinVersion() {
        return this.minVersion;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.b
    public int getPackageVersion() {
        return this.packageVersion;
    }

    public String toString() {
        return "AssetPackageRecord{_id=" + this._id + ", assetId='" + this.assetId + CoreConstants.SINGLE_QUOTE_CHAR + ", assetIdx=" + this.assetIdx + ", assetUrl='" + this.assetUrl + CoreConstants.SINGLE_QUOTE_CHAR + ", thumbUrl='" + this.thumbUrl + CoreConstants.SINGLE_QUOTE_CHAR + ", thumbPath='" + this.thumbPath + CoreConstants.SINGLE_QUOTE_CHAR + ", priceType='" + this.priceType + CoreConstants.SINGLE_QUOTE_CHAR + ", localPath='" + this.localPath + CoreConstants.SINGLE_QUOTE_CHAR + ", category=" + this.category + ", subCategory=" + this.subCategory + ", assetName=" + this.assetName + ", assetDesc=" + this.assetDesc + ", packageURI='" + this.packageURI + CoreConstants.SINGLE_QUOTE_CHAR + ", installSource=" + this.installSource + '}';
    }
}
