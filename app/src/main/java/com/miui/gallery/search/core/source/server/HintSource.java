package com.miui.gallery.search.core.source.server;

import android.content.Context;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.BaseSourceResult;
import com.miui.gallery.search.core.result.SourceResult;
import com.miui.gallery.search.core.source.Source;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.utils.SearchLog;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class HintSource extends ServerSource {
    public static final SearchConstants.SearchType[] SUPPORT_SEARCH_TYPE = {SearchConstants.SearchType.SEARCH_TYPE_HINT};

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public boolean canCarryLog() {
        return true;
    }

    @Override // com.miui.gallery.search.core.source.SuggestionResultProvider
    public String getName() {
        return "server_controlled_hints";
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public String getQueryAppendPath(QueryInfo queryInfo) {
        return "hint";
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource, com.miui.gallery.search.core.source.InterceptableSource
    public boolean isFatalCondition(QueryInfo queryInfo, int i) {
        return false;
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public boolean useCache(QueryInfo queryInfo) {
        return true;
    }

    public HintSource(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.search.core.source.AbstractSource
    public SearchConstants.SearchType[] getSupportSearchTypes() {
        return SUPPORT_SEARCH_TYPE;
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public String getQueryPathPrefix(QueryInfo queryInfo, String str) {
        if (str == null) {
            return HostManager.Search.getSearchAnonymousUrlHost();
        }
        return super.getQueryPathPrefix(queryInfo, str);
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public String getQueryUserPath(String str) {
        return str == null ? "anonymous" : super.getQueryUserPath(str);
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public int getMethod(QueryInfo queryInfo, String str) {
        if (str == null) {
            return 0;
        }
        return super.getMethod(queryInfo, str);
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public Type getResponseDataType(QueryInfo queryInfo) {
        return HintResponseData.class;
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public SourceResult onResponse(QueryInfo queryInfo, ServerSearchRequest serverSearchRequest, Object obj) {
        if (!(obj instanceof HintResponseData)) {
            SearchLog.e("HintSource", "Not supported response data type");
            return null;
        }
        return new BaseSourceResult(queryInfo, this, createSuggestionCursor(((HintResponseData) obj).hintSuggestions, this, queryInfo));
    }

    public static SuggestionCursor createSuggestionCursor(List<HintSuggestion> list, Source source, QueryInfo queryInfo) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (HintSuggestion hintSuggestion : list) {
            HashMap hashMap = new HashMap(3);
            hashMap.put("hint_text", hintSuggestion.hintText);
            hashMap.put("display_duration", String.valueOf(hintSuggestion.displayDurationMs));
            String str = hintSuggestion.queryText;
            if (str != null) {
                hashMap.put("query_text", str);
            }
            arrayList.add(ConvertUtil.createSuggestion(hashMap, source));
        }
        return ConvertUtil.createSuggestionCursor(arrayList, queryInfo);
    }

    /* loaded from: classes2.dex */
    public static class HintResponseData implements Cacheable {
        @SerializedName("expireMs")
        public long expireMills;
        @SerializedName("hints")
        public List<HintSuggestion> hintSuggestions;

        @Override // com.miui.gallery.search.core.source.server.Cacheable
        public long getExpireMills() {
            return this.expireMills;
        }
    }
}
