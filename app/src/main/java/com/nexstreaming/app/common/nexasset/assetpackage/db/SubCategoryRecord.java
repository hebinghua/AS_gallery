package com.nexstreaming.app.common.nexasset.assetpackage.db;

import com.nexstreaming.app.common.nexasset.assetpackage.d;
import com.nexstreaming.app.common.norm.b;
import java.util.Map;

/* loaded from: classes3.dex */
public class SubCategoryRecord extends b implements d {
    public long _id;
    public String subCategoryAlias;
    @b.e
    @b.g
    public long subCategoryId;
    public Map<String, String> subCategoryName;

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.d
    public long getSubCategoryId() {
        return this.subCategoryId;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.d
    public Map<String, String> getSubCategoryName() {
        return this.subCategoryName;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.d
    public String getSubCategoryAlias() {
        return this.subCategoryAlias;
    }
}
