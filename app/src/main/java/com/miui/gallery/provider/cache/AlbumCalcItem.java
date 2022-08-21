package com.miui.gallery.provider.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public class AlbumCalcItem {
    public final Long id;
    public final String path;

    public AlbumCalcItem(Long l, String str) {
        this.id = l;
        this.path = str;
    }

    public static AlbumCalcItem from(AlbumCacheItem albumCacheItem) {
        return new AlbumCalcItem(Long.valueOf(albumCacheItem.getId()), albumCacheItem.getDirectoryPath());
    }

    public static List<AlbumCalcItem> from(List<AlbumCacheItem> list) {
        if (list != null && !list.isEmpty()) {
            ArrayList arrayList = new ArrayList(list.size());
            for (AlbumCacheItem albumCacheItem : list) {
                arrayList.add(from(albumCacheItem));
            }
            return arrayList;
        }
        return Collections.emptyList();
    }
}
