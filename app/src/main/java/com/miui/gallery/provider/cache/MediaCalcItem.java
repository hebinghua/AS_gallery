package com.miui.gallery.provider.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public class MediaCalcItem {
    public final Long albumId;
    public final long id;
    public final int type;

    public MediaCalcItem(long j, Long l, int i) {
        this.id = j;
        this.albumId = l;
        this.type = i;
    }

    public static <T extends MediaCacheItem> MediaCalcItem from(T t) {
        return new MediaCalcItem(t.getId(), t.getAlbumId(), t.getType());
    }

    public static <T extends MediaCacheItem> List<MediaCalcItem> from(List<T> list) {
        if (list != null && !list.isEmpty()) {
            ArrayList arrayList = new ArrayList(list.size());
            for (T t : list) {
                arrayList.add(from(t));
            }
            return arrayList;
        }
        return Collections.emptyList();
    }
}
