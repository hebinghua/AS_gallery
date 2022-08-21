package com.miui.gallery.provider.cache;

import com.miui.gallery.provider.cache.CacheItem;
import com.miui.gallery.provider.cache.MediaCacheItem;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class MediaCacheItem$QueryFactory$$ExternalSyntheticLambda0 implements CacheItem.Merger {
    public static final /* synthetic */ MediaCacheItem$QueryFactory$$ExternalSyntheticLambda0 INSTANCE = new MediaCacheItem$QueryFactory$$ExternalSyntheticLambda0();

    @Override // com.miui.gallery.provider.cache.CacheItem.Merger
    public final CacheItem merge(CacheItem cacheItem, CacheItem cacheItem2, int i) {
        MediaCacheItem lambda$static$0;
        lambda$static$0 = MediaCacheItem.QueryFactory.lambda$static$0((MediaCacheItem) cacheItem, (MediaCacheItem) cacheItem2, i);
        return lambda$static$0;
    }
}
