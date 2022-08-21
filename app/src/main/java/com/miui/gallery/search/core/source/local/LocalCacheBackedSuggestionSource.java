package com.miui.gallery.search.core.source.local;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.suggestion.Suggestion;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class LocalCacheBackedSuggestionSource<T> extends LocalSingleSectionSuggestionSource implements ContentCacheProvider<T> {
    public ContentCache<T> mCache;

    public abstract List<Suggestion> handleQuery(T t, String str, QueryInfo queryInfo);

    public LocalCacheBackedSuggestionSource(Context context) {
        super(context);
        this.mCache = new ContentCache<>(this);
    }

    @Override // com.miui.gallery.search.core.source.local.ContentCacheProvider
    public void registerContentObserver(Uri uri, boolean z, ContentObserver contentObserver) {
        this.mContext.getContentResolver().registerContentObserver(uri, z, contentObserver);
    }

    @Override // com.miui.gallery.search.core.source.local.ContentCacheProvider
    public void unregisterContentObserver(ContentObserver contentObserver) {
        this.mContext.getContentResolver().unregisterContentObserver(contentObserver);
    }

    @Override // com.miui.gallery.search.core.source.local.LocalSingleSectionSuggestionSource
    public List<Suggestion> querySuggestion(String str, QueryInfo queryInfo) {
        return handleQuery(this.mCache.getCache(), str, queryInfo);
    }
}
