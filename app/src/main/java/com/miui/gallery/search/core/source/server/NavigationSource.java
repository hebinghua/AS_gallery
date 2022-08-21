package com.miui.gallery.search.core.source.server;

import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.search.SearchConfig;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.source.server.ConvertUtil;

/* loaded from: classes2.dex */
public class NavigationSource extends SectionedServerSource {
    public static final SearchConstants.SearchType[] SUPPORT_SEARCH_TYPE = {SearchConstants.SearchType.SEARCH_TYPE_NAVIGATION};
    public ConvertUtil.Filter mFilter;

    @Override // com.miui.gallery.search.core.source.SuggestionResultProvider
    public String getName() {
        return "server_controlled_navigations";
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public String getQueryAppendPath(QueryInfo queryInfo) {
        return "navigation";
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public boolean useCache(QueryInfo queryInfo) {
        return true;
    }

    public NavigationSource(Context context) {
        super(context);
        this.mFilter = new ConvertUtil.Filter() { // from class: com.miui.gallery.search.core.source.server.NavigationSource.1
            @Override // com.miui.gallery.search.core.source.server.ConvertUtil.Filter
            public boolean filter(ItemSuggestion itemSuggestion, String str) {
                return itemSuggestion == null || TextUtils.isEmpty(str) || !str.equals(SearchConstants.SectionType.SECTION_TYPE_PEOPLE.getName()) || itemSuggestion.resultCount >= FaceManager.LEAST_FACE_COUNT_OF_DISPLAY_PEOPLE;
            }
        };
    }

    @Override // com.miui.gallery.search.core.source.AbstractSource
    public SearchConstants.SearchType[] getSupportSearchTypes() {
        return SUPPORT_SEARCH_TYPE;
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource, com.miui.gallery.search.core.source.InterceptableSource
    public boolean isFatalCondition(QueryInfo queryInfo, int i) {
        return SearchConfig.get().getNavigationConfig().isFatalCondition(i);
    }

    @Override // com.miui.gallery.search.core.source.server.SectionedServerSource
    public ConvertUtil.Filter getFilter() {
        return this.mFilter;
    }
}
