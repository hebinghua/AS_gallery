package com.miui.gallery.search.core.source;

import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;

/* loaded from: classes2.dex */
public abstract class AbstractSource implements Source {
    public abstract SearchConstants.SearchType[] getSupportSearchTypes();

    @Override // com.miui.gallery.search.core.source.SuggestionResultProvider
    public boolean match(QueryInfo queryInfo) {
        SearchConstants.SearchType[] supportSearchTypes;
        if (queryInfo != null && queryInfo.getSearchType() != null && (supportSearchTypes = getSupportSearchTypes()) != null && supportSearchTypes.length > 0) {
            for (SearchConstants.SearchType searchType : supportSearchTypes) {
                if (searchType == queryInfo.getSearchType()) {
                    return true;
                }
            }
        }
        return false;
    }
}
