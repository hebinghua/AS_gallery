package com.nexstreaming.app.common.nexasset.store;

import java.util.List;

/* loaded from: classes3.dex */
public interface StoreFeaturedAssetInfo {
    String getFeatuedAssetLayoutType();

    List<StoreAssetInfo> getFeaturedAssetList();

    long getServerTimestamp();
}
