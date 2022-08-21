package com.miui.gallery.provider.cache;

import android.database.Cursor;
import android.os.Bundle;
import com.miui.gallery.provider.cache.CacheItem;
import java.util.List;

/* compiled from: IMediaProcessor.kt */
/* loaded from: classes2.dex */
public interface IMediaProcessor<T extends CacheItem, R> {
    List<R> processCache(List<? extends T> list, Bundle bundle);

    List<R> processCursor(Cursor cursor);
}
