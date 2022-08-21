package com.nexstreaming.app.common.nexasset.assetpackage.db;

import ch.qos.logback.core.CoreConstants;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemCategory;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemType;
import com.nexstreaming.app.common.nexasset.assetpackage.f;
import com.nexstreaming.app.common.norm.b;
import java.util.Collections;
import java.util.Map;

/* loaded from: classes3.dex */
public class ItemRecord extends b implements f {
    public long _id;
    @b.d
    @b.c
    public AssetPackageRecord assetPackageRecord;
    public String filePath;
    public boolean hidden;
    public String iconPath;
    public ItemCategory itemCategory;
    @b.e
    @b.g
    public String itemId;
    public ItemType itemType;
    public Map<String, String> label;
    public String packageURI;
    public String sampleText;
    public String thumbPath;

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
    public String getId() {
        return this.itemId;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
    public String getPackageURI() {
        return this.packageURI;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
    public String getFilePath() {
        return this.filePath;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
    public String getIconPath() {
        return this.iconPath;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
    public String getThumbPath() {
        return this.thumbPath;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
    public Map<String, String> getLabel() {
        Map<String, String> map = this.label;
        if (map == null || map.isEmpty()) {
            return Collections.singletonMap("en", this.itemId);
        }
        return this.label;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
    public String getSampleText() {
        return this.sampleText;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
    public ItemType getType() {
        return this.itemType;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
    public ItemCategory getCategory() {
        return this.itemCategory;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
    public com.nexstreaming.app.common.nexasset.assetpackage.b getAssetPackage() {
        return this.assetPackageRecord;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
    public boolean isHidden() {
        return this.hidden;
    }

    public String toString() {
        return "ItemRecord{_id=" + this._id + ", itemId='" + this.itemId + CoreConstants.SINGLE_QUOTE_CHAR + ", packageURI='" + this.packageURI + CoreConstants.SINGLE_QUOTE_CHAR + ", filePath='" + this.filePath + CoreConstants.SINGLE_QUOTE_CHAR + ", iconPath='" + this.iconPath + CoreConstants.SINGLE_QUOTE_CHAR + ", thumbPath='" + this.thumbPath + CoreConstants.SINGLE_QUOTE_CHAR + ", label=" + this.label + ", itemType=" + this.itemType + ", itemCategory=" + this.itemCategory + ", assetPackageRecord=" + this.assetPackageRecord + '}';
    }
}
