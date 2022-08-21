package com.miui.gallery.search.core.source.local;

import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.search.SearchConfig;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.BaseSourceResult;
import com.miui.gallery.search.core.result.SourceResult;
import com.miui.gallery.search.core.source.InterceptableSource;
import com.miui.gallery.search.core.suggestion.BaseSuggestionSection;
import com.miui.gallery.search.core.suggestion.ListSuggestionCursor;
import com.miui.gallery.search.core.suggestion.Suggestion;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class LocalSingleSectionSuggestionSource extends InterceptableSource {
    public static final SearchConstants.SearchType[] SUPPORT_SEARCH_TYPE = {SearchConstants.SearchType.SEARCH_TYPE_SUGGESTION, SearchConstants.SearchType.SEARCH_TYPE_SEARCH};

    @Override // com.miui.gallery.search.core.source.Source
    public int getPriority(QueryInfo queryInfo) {
        return 1;
    }

    public abstract SearchConstants.SectionType getSectionType();

    public boolean isIgnoreQueryContent() {
        return false;
    }

    public abstract List<Suggestion> querySuggestion(String str, QueryInfo queryInfo);

    public LocalSingleSectionSuggestionSource(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.search.core.source.AbstractSource
    public SearchConstants.SearchType[] getSupportSearchTypes() {
        return SUPPORT_SEARCH_TYPE;
    }

    @Override // com.miui.gallery.search.core.source.InterceptableSource
    public SourceResult doGetSuggestions(QueryInfo queryInfo) {
        BaseSuggestionSection generateSection;
        if (isIgnoreQueryContent() || !TextUtils.isEmpty(queryInfo.getParam("query"))) {
            String param = queryInfo.getParam("query");
            boolean isEmpty = TextUtils.isEmpty(param);
            if (!isEmpty) {
                param = param.toLowerCase();
            }
            List<Suggestion> querySuggestion = querySuggestion(param, queryInfo);
            if (!isEmpty) {
                querySuggestion = sortSuggestions(querySuggestion, param, queryInfo);
            }
            generateSection = generateSection(queryInfo, querySuggestion);
        } else {
            generateSection = null;
        }
        return new BaseSourceResult(queryInfo, this, generateSection);
    }

    public List<Suggestion> sortSuggestions(List<Suggestion> list, final String str, final QueryInfo queryInfo) {
        Collections.sort(list, new Comparator<Suggestion>() { // from class: com.miui.gallery.search.core.source.local.LocalSingleSectionSuggestionSource.1
            @Override // java.util.Comparator
            public int compare(Suggestion suggestion, Suggestion suggestion2) {
                return LocalSingleSectionSuggestionSource.this.sort(suggestion, suggestion2, str, queryInfo);
            }
        });
        return list;
    }

    public int sort(Suggestion suggestion, Suggestion suggestion2, String str, QueryInfo queryInfo) {
        int indexOf = suggestion.getSuggestionTitle().toLowerCase().indexOf(str);
        int indexOf2 = suggestion2.getSuggestionTitle().toLowerCase().indexOf(str);
        return indexOf == indexOf2 ? suggestion.getSuggestionTitle().length() - suggestion2.getSuggestionTitle().length() : indexOf - indexOf2;
    }

    public String getSectionTitle(SearchConstants.SectionType sectionType) {
        return SearchConfig.get().getTitleForSection(sectionType);
    }

    public BaseSuggestionSection generateSection(QueryInfo queryInfo, List<Suggestion> list) {
        if (list != null) {
            SearchConstants.SectionType sectionType = getSectionType();
            return new BaseSuggestionSection(queryInfo, sectionType, new ListSuggestionCursor(queryInfo, list), null, getSectionTitle(sectionType), null, null);
        }
        return null;
    }
}
