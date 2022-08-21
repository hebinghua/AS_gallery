package com.miui.gallery.search.core.source.server;

import android.content.Context;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;

/* loaded from: classes2.dex */
public class ResultSource extends SectionedServerSource {
    public static final SearchConstants.SearchType[] SUPPORT_SEARCH_TYPE = {SearchConstants.SearchType.SEARCH_TYPE_RESULT};

    @Override // com.miui.gallery.search.core.source.SuggestionResultProvider
    public String getName() {
        return "server_controlled_results";
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public String getQueryAppendPath(QueryInfo queryInfo) {
        return "result";
    }

    public ResultSource(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.search.core.source.AbstractSource
    public SearchConstants.SearchType[] getSupportSearchTypes() {
        return SUPPORT_SEARCH_TYPE;
    }
}
