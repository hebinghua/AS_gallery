package com.miui.gallery.search.core.context;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.display.SuggestionViewFactory;
import com.miui.gallery.search.core.query.QueryTask;
import com.miui.gallery.search.core.source.Source;
import com.miui.gallery.search.core.source.Sources;
import com.miui.gallery.search.utils.SearchLog;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class SearchContext {
    public static volatile SearchContext sInstance;
    public Context mContext;
    public TaskExecutor mIconLoaderCacheExecutor;
    public TaskExecutor mIconLoaderExecutor;
    public Handler mPublishHandler;
    public HandlerThread mPublishThread;
    public TaskExecutor<QueryTask> mQueryTaskExecutor;
    public Sources mSources;
    public SuggestionViewFactory mSuggestionViewFactory;
    public final Object mPublishLock = new Object();
    public int mHandlerRef = 0;

    public SearchContext() {
        init(SearchContextConfigurationImp.createDefault(GalleryApp.sGetAndroidContext()));
    }

    public Handler acquirePublishHandler() {
        Handler handler;
        synchronized (this.mPublishLock) {
            int i = this.mHandlerRef + 1;
            this.mHandlerRef = i;
            if (i > 0 && this.mPublishHandler == null) {
                SearchLog.d("SearchContext", "On create public thread");
                HandlerThread handlerThread = new HandlerThread("search_publish_thread");
                this.mPublishThread = handlerThread;
                handlerThread.start();
                this.mPublishHandler = new Handler(this.mPublishThread.getLooper());
            }
            handler = this.mPublishHandler;
        }
        return handler;
    }

    public void releasePublishHandler() {
        synchronized (this.mPublishLock) {
            int i = this.mHandlerRef - 1;
            this.mHandlerRef = i;
            if (i == 0) {
                SearchLog.d("SearchContext", "On quit public thread");
                this.mPublishThread.quit();
                this.mPublishThread = null;
                this.mPublishHandler = null;
            }
            if (this.mHandlerRef < 0) {
                throw new RuntimeException("Invalid publish handler reference");
            }
        }
    }

    public static SearchContext getInstance() {
        if (sInstance == null) {
            synchronized (SearchContext.class) {
                if (sInstance == null) {
                    sInstance = new SearchContext();
                }
            }
        }
        return sInstance;
    }

    public void init(SearchContextConfiguration searchContextConfiguration) {
        this.mContext = searchContextConfiguration.getContext();
        Sources sources = searchContextConfiguration.getSources();
        this.mSources = sources;
        sources.update();
        this.mQueryTaskExecutor = searchContextConfiguration.getQueryTaskExecutor();
        this.mIconLoaderExecutor = searchContextConfiguration.getIconLoaderExecutor();
        this.mIconLoaderCacheExecutor = searchContextConfiguration.getIconLoaderCacheExecutor();
        this.mSuggestionViewFactory = searchContextConfiguration.getSuggestionViewFactory();
    }

    public Sources getSources() {
        return this.mSources;
    }

    public List<Source> getSourceList() {
        if (this.mSources != null) {
            return new ArrayList(getSources().getSources());
        }
        return new ArrayList();
    }

    public TaskExecutor getIconLoaderExecutor() {
        return this.mIconLoaderExecutor;
    }

    public TaskExecutor getIconLoaderCacheExecutor() {
        return this.mIconLoaderCacheExecutor;
    }

    public TaskExecutor<QueryTask> getQueryTaskExecutor() {
        return this.mQueryTaskExecutor;
    }

    public SuggestionViewFactory getSuggestionViewFactory() {
        return this.mSuggestionViewFactory;
    }

    public List<Source> getMatchedSources(QueryInfo queryInfo) {
        return getMatchedSources(queryInfo, getSourceList());
    }

    public List<Source> getMatchedSources(QueryInfo queryInfo, List<Source> list) {
        ArrayList arrayList = new ArrayList();
        for (Source source : list) {
            if (source.match(queryInfo)) {
                arrayList.add(source);
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return arrayList;
    }
}
