package com.miui.gallery.search.core.context;

import android.content.Context;
import com.miui.gallery.search.core.display.SuggestionViewFactory;
import com.miui.gallery.search.core.display.SuggestionViewFactoryImpl;
import com.miui.gallery.search.core.source.SearchableSources;
import com.miui.gallery.search.core.source.Sources;

/* loaded from: classes2.dex */
public class SearchContextConfigurationImp implements SearchContextConfiguration {
    public Builder mBuilder;

    @Override // com.miui.gallery.search.core.context.SearchContextConfiguration
    public PriorityTaskExecutor getQueryTaskExecutor() {
        return this.mBuilder.queryTaskExecutor;
    }

    @Override // com.miui.gallery.search.core.context.SearchContextConfiguration
    public PriorityTaskExecutor getIconLoaderExecutor() {
        return this.mBuilder.iconLoaderExecutor;
    }

    @Override // com.miui.gallery.search.core.context.SearchContextConfiguration
    public TaskExecutor getIconLoaderCacheExecutor() {
        return this.mBuilder.iconLoaderCacheExecutor;
    }

    @Override // com.miui.gallery.search.core.context.SearchContextConfiguration
    public SuggestionViewFactory getSuggestionViewFactory() {
        return this.mBuilder.suggestionViewFactory;
    }

    @Override // com.miui.gallery.search.core.context.SearchContextConfiguration
    public Sources getSources() {
        return this.mBuilder.sources;
    }

    @Override // com.miui.gallery.search.core.context.SearchContextConfiguration
    public Context getContext() {
        return this.mBuilder.context;
    }

    public static SearchContextConfigurationImp createDefault(Context context) {
        return new Builder(context).build();
    }

    public SearchContextConfigurationImp(Builder builder) {
        this.mBuilder = builder;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public Context context;
        public TaskExecutor iconLoaderCacheExecutor;
        public PriorityTaskExecutor iconLoaderExecutor;
        public PriorityTaskExecutor queryTaskExecutor;
        public Sources sources;
        public SuggestionViewFactory suggestionViewFactory;
        public int queryTaskExecutorSize = 3;
        public int iconLoaderExecutorSize = 2;
        public int iconLoaderCacheExecutorSize = 1;

        public Builder(Context context) {
            this.context = context;
        }

        public SearchContextConfigurationImp build() {
            initEmptyFieldsWithDefaultValues();
            return new SearchContextConfigurationImp(this);
        }

        public final void initEmptyFieldsWithDefaultValues() {
            if (this.queryTaskExecutor == null) {
                this.queryTaskExecutor = new PriorityTaskExecutor(this.queryTaskExecutorSize, "SearchQueryTask");
            }
            if (this.suggestionViewFactory == null) {
                this.suggestionViewFactory = new SuggestionViewFactoryImpl(this.context);
            }
            if (this.sources == null) {
                this.sources = new SearchableSources(this.context);
            }
            if (this.iconLoaderExecutor == null) {
                this.iconLoaderExecutor = new PriorityTaskExecutor(this.iconLoaderExecutorSize, "SearchIconLoader");
            }
            if (this.iconLoaderCacheExecutor == null) {
                this.iconLoaderCacheExecutor = new SimpleTaskExecutor(this.iconLoaderCacheExecutorSize, "CacheIconLoader");
            }
        }
    }
}
