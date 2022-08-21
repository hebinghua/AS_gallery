package com.miui.gallery.search.core.source.server;

import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.search.SearchConfig;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.MiStat;
import java.util.Map;

/* loaded from: classes2.dex */
public class SearchSource extends SectionedServerSource {
    public static final SearchConstants.SearchType[] SUPPORT_SEARCH_TYPE = {SearchConstants.SearchType.SEARCH_TYPE_SEARCH, SearchConstants.SearchType.SEARCH_TYPE_SUGGESTION};

    @Override // com.miui.gallery.search.core.source.SuggestionResultProvider
    public String getName() {
        return "server_controlled_search_suggestions";
    }

    public SearchSource(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.search.core.source.AbstractSource
    public SearchConstants.SearchType[] getSupportSearchTypes() {
        return SUPPORT_SEARCH_TYPE;
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public boolean useCache(QueryInfo queryInfo) {
        return TextUtils.isEmpty(queryInfo.getParam("extraInfo"));
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public Map<String, String> getQueryParams(QueryInfo queryInfo) {
        Map<String, String> queryParams = super.getQueryParams(queryInfo);
        String str = queryParams.get("query");
        if (!TextUtils.isEmpty(str)) {
            String queryExtras = SearchConfig.get().getSuggestionConfig().getQueryExtras(str);
            if (!TextUtils.isEmpty(queryExtras)) {
                queryParams.put("extraInfo", queryExtras);
                DefaultLogger.d("SearchSource", "On append extra [%s] to query [%s]", queryExtras, str);
            }
        }
        queryParams.remove("enableShortcut");
        return queryParams;
    }

    /* renamed from: com.miui.gallery.search.core.source.server.SearchSource$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$search$SearchConstants$SearchType;

        static {
            int[] iArr = new int[SearchConstants.SearchType.values().length];
            $SwitchMap$com$miui$gallery$search$SearchConstants$SearchType = iArr;
            try {
                iArr[SearchConstants.SearchType.SEARCH_TYPE_SUGGESTION.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SearchType[SearchConstants.SearchType.SEARCH_TYPE_SEARCH.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public String getQueryAppendPath(QueryInfo queryInfo) {
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$search$SearchConstants$SearchType[queryInfo.getSearchType().ordinal()];
        if (i != 1) {
            if (i == 2) {
                return MiStat.Event.SEARCH;
            }
            return null;
        }
        return "suggestion";
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource, com.miui.gallery.search.core.source.InterceptableSource
    public boolean isFatalCondition(QueryInfo queryInfo, int i) {
        return super.isFatalCondition(queryInfo, i) || i == 14;
    }
}
