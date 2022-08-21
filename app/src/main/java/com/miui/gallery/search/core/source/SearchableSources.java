package com.miui.gallery.search.core.source;

import android.content.Context;
import com.miui.gallery.search.core.source.local.AlbumSource;
import com.miui.gallery.search.core.source.local.AppScreenshotSource;
import com.miui.gallery.search.core.source.local.PhotoNameSource;
import com.miui.gallery.search.core.source.local.SecretAlbumSource;
import com.miui.gallery.search.core.source.server.DataListSource;
import com.miui.gallery.search.core.source.server.FeedbackLikelyListSource;
import com.miui.gallery.search.core.source.server.HintSource;
import com.miui.gallery.search.core.source.server.NavigationSource;
import com.miui.gallery.search.core.source.server.ResultSource;
import com.miui.gallery.search.core.source.server.SearchSource;
import com.miui.gallery.search.history.HistorySource;
import com.miui.gallery.search.utils.SearchLog;
import java.util.Collection;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class SearchableSources implements Sources {
    public final Context mContext;
    public HashMap<String, Source> mSources;

    public SearchableSources(Context context) {
        this.mContext = context;
    }

    @Override // com.miui.gallery.search.core.source.Sources
    public Collection<Source> getSources() {
        return this.mSources.values();
    }

    @Override // com.miui.gallery.search.core.source.Sources
    public void update() {
        SearchLog.d("SearchableSources", "update()");
        this.mSources = new HashMap<>();
        addHintSources();
        addNavigationSources();
        addResultSources();
        addSuggestionSources();
        addHistorySources();
        addLocalSources();
    }

    public void addHintSources() {
        addSource(new HintSource(this.mContext));
    }

    public void addNavigationSources() {
        addSource(new NavigationSource(this.mContext));
    }

    public void addResultSources() {
        addSource(new ResultSource(this.mContext));
        addSource(new DataListSource(this.mContext));
        addSource(new FeedbackLikelyListSource(this.mContext));
    }

    public void addSuggestionSources() {
        addSource(new SearchSource(this.mContext));
    }

    public void addHistorySources() {
        addSource(new HistorySource(this.mContext));
    }

    public void addLocalSources() {
        addSource(new AlbumSource(this.mContext));
        addSource(new AppScreenshotSource(this.mContext));
        addSource(new PhotoNameSource(this.mContext));
        addSource(new SecretAlbumSource(this.mContext));
    }

    public void addSource(Source source) {
        this.mSources.put(source.getName(), source);
    }
}
