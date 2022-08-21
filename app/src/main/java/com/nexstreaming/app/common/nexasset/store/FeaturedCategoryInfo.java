package com.nexstreaming.app.common.nexasset.store;

import java.util.List;
import java.util.Map;

/* loaded from: classes3.dex */
public class FeaturedCategoryInfo implements StoreCategoryInfo {
    @Override // com.nexstreaming.app.common.nexasset.store.StoreCategoryInfo
    public String getCategoryAliasName() {
        return "Featured";
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreCategoryInfo
    public int getCategoryIdx() {
        return -1;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreCategoryInfo
    public Map<String, String> getCategoryName() {
        return null;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreCategoryInfo
    public String getIconURL() {
        return null;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreCategoryInfo
    public String getSelectedIconURL() {
        return null;
    }

    @Override // com.nexstreaming.app.common.nexasset.store.StoreCategoryInfo
    public List<StoreSubcategoryInfo> getSubCategories() {
        return null;
    }
}
