package com.miui.gallery.search.core.source.server;

import android.content.Context;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;

/* loaded from: classes2.dex */
public class FeedbackLikelyListSource extends DataListSource {
    public static final SearchConstants.SearchType[] SUPPORT_SEARCH_TYPE = {SearchConstants.SearchType.SEARCH_TYPE_FEEDBACK_LIKELY_RESULT};

    @Override // com.miui.gallery.search.core.source.server.DataListSource, com.miui.gallery.search.core.source.SuggestionResultProvider
    public String getName() {
        return "server_controlled_feedback_likely_list";
    }

    @Override // com.miui.gallery.search.core.source.server.DataListSource, com.miui.gallery.search.core.source.server.ServerSource
    public String getQueryAppendPath(QueryInfo queryInfo) {
        return "tag/feedback/image/list";
    }

    @Override // com.miui.gallery.search.core.source.server.DataListSource, com.miui.gallery.search.core.source.server.ServerSource
    public boolean isPersistable(QueryInfo queryInfo) {
        return false;
    }

    public FeedbackLikelyListSource(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.search.core.source.server.DataListSource, com.miui.gallery.search.core.source.AbstractSource
    public SearchConstants.SearchType[] getSupportSearchTypes() {
        return SUPPORT_SEARCH_TYPE;
    }
}
