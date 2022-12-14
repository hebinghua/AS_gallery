package com.nexstreaming.app.common.nexasset.store;

import java.util.List;
import java.util.Map;

/* loaded from: classes3.dex */
public interface StoreCategoryInfo {
    String getCategoryAliasName();

    int getCategoryIdx();

    Map<String, String> getCategoryName();

    String getIconURL();

    String getSelectedIconURL();

    List<StoreSubcategoryInfo> getSubCategories();
}
