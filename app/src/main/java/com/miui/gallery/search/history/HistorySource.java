package com.miui.gallery.search.history;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.search.SearchConfig;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.SearchContract;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.BaseSourceResult;
import com.miui.gallery.search.core.result.SourceResult;
import com.miui.gallery.search.core.source.InterceptableSource;
import com.miui.gallery.search.core.suggestion.BaseSuggestion;
import com.miui.gallery.search.core.suggestion.BaseSuggestionSection;
import com.miui.gallery.search.core.suggestion.CursorBackedSuggestionCursor;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class HistorySource extends InterceptableSource {
    public static final SearchConstants.SearchType[] SUPPORT_SEARCH_TYPE = {SearchConstants.SearchType.SEARCH_TYPE_HISTORY, SearchConstants.SearchType.SEARCH_TYPE_NAVIGATION};
    public static final String[] PROJECTION = {"title", "subTitle", "actionUri"};

    @Override // com.miui.gallery.search.core.source.SuggestionResultProvider
    public String getName() {
        return "local_history_source";
    }

    public HistorySource(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.search.core.source.AbstractSource
    public SearchConstants.SearchType[] getSupportSearchTypes() {
        return SUPPORT_SEARCH_TYPE;
    }

    @Override // com.miui.gallery.search.core.source.InterceptableSource
    public boolean isFatalCondition(QueryInfo queryInfo, int i) {
        if (queryInfo.getSearchType() == SearchConstants.SearchType.SEARCH_TYPE_NAVIGATION) {
            return SearchConfig.get().getNavigationConfig().isFatalCondition(i);
        }
        return super.isFatalCondition(queryInfo, i);
    }

    @Override // com.miui.gallery.search.core.source.InterceptableSource
    public SourceResult doGetSuggestions(QueryInfo queryInfo) {
        Uri.Builder appendQueryParameter = SearchContract.History.URI.buildUpon().appendQueryParameter("query_limit", String.valueOf(SearchConfig.get().getHistoryConfig().getNavigationReturnCount()));
        if (!TextUtils.isEmpty(queryInfo.getParam("query_text"))) {
            appendQueryParameter.appendQueryParameter("query_text", queryInfo.getParam("query_text"));
        }
        Cursor query = this.mContext.getContentResolver().query(appendQueryParameter.build(), PROJECTION, null, null, "timestamp DESC");
        SearchConfig.HistoryConfig historyConfig = SearchConfig.get().getHistoryConfig();
        BaseSuggestionSection baseSuggestionSection = null;
        if (query != null) {
            baseSuggestionSection = new BaseSuggestionSection(queryInfo, historyConfig.getSectionType(), new CursorBackedSuggestionCursor(queryInfo, query), null, historyConfig.getTitle(this.mContext), historyConfig.getSubTitle(this.mContext), query.getCount() <= 0 ? null : new BaseSuggestion(this.mContext.getResources().getString(R.string.search_clear_histories), null, null, null, null, null));
        }
        DefaultLogger.d("HistorySource", "On load %s search histories", Integer.valueOf(baseSuggestionSection == null ? 0 : baseSuggestionSection.getCount()));
        return new BaseSourceResult(queryInfo, this, baseSuggestionSection);
    }

    @Override // com.miui.gallery.search.core.source.Source
    public int getPriority(QueryInfo queryInfo) {
        return (queryInfo == null || queryInfo.getSearchType() != SearchConstants.SearchType.SEARCH_TYPE_HISTORY) ? 3 : 0;
    }
}
