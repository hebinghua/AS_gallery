package com.miui.gallery.search.core.source.server;

import android.content.Context;
import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.BaseSourceResult;
import com.miui.gallery.search.core.result.SourceResult;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.resultpage.DataListSourceResult;
import com.miui.gallery.search.utils.SearchLog;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class DataListSource extends ServerSource {
    public static final SearchConstants.SearchType[] SUPPORT_SEARCH_TYPE = {SearchConstants.SearchType.SEARCH_TYPE_RESULT_LIST, SearchConstants.SearchType.SEARCH_TYPE_FILTER};

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public boolean canCarryLog() {
        return true;
    }

    @Override // com.miui.gallery.search.core.source.SuggestionResultProvider
    public String getName() {
        return "server_controlled_data_list";
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public String getQueryAppendPath(QueryInfo queryInfo) {
        return "list";
    }

    public DataListSource(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.search.core.source.AbstractSource
    public SearchConstants.SearchType[] getSupportSearchTypes() {
        return SUPPORT_SEARCH_TYPE;
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public Type getResponseDataType(QueryInfo queryInfo) {
        return DataListResponseData.class;
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public boolean isPersistable(QueryInfo queryInfo) {
        return queryInfo != null && "0".equals(queryInfo.getParam("pos"));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0106, code lost:
        throw new com.miui.gallery.net.base.RequestError(com.miui.gallery.net.base.ErrorCode.BODY_EMPTY, "Execute result should not be null", null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x011c, code lost:
        return onResponse(r19, null, r10);
     */
    @Override // com.miui.gallery.search.core.source.InterceptableSource, com.miui.gallery.search.core.source.Source, com.miui.gallery.search.core.source.SuggestionResultProvider
    /* renamed from: getSuggestions */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.miui.gallery.search.core.result.SourceResult mo1333getSuggestions(com.miui.gallery.search.core.QueryInfo r19) {
        /*
            Method dump skipped, instructions count: 290
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.search.core.source.server.DataListSource.mo1333getSuggestions(com.miui.gallery.search.core.QueryInfo):com.miui.gallery.search.core.result.SourceResult");
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public SourceResult onResponse(QueryInfo queryInfo, ServerSearchRequest serverSearchRequest, Object obj) {
        if (!(obj instanceof DataListResponseData)) {
            SearchLog.e("DataListSource", "Not supported response data type");
            return null;
        }
        DataListResponseData dataListResponseData = (DataListResponseData) obj;
        return new DataListSourceResult(queryInfo, this, ConvertUtil.createSuggestionCursor(dataListResponseData.items, this, queryInfo, null, null), dataListResponseData.nextPosition, dataListResponseData.isLastPage, dataListResponseData.indexHash);
    }

    @Override // com.miui.gallery.search.core.source.InterceptableSource
    public BaseSourceResult generateDefaultResult(QueryInfo queryInfo, SuggestionCursor suggestionCursor) {
        return new DataListSourceResult(queryInfo, this, suggestionCursor, 0, true, -1L);
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource, com.miui.gallery.search.core.source.Source
    public int getPriority(QueryInfo queryInfo) {
        if (queryInfo.getSearchType() == SearchConstants.SearchType.SEARCH_TYPE_FILTER) {
            return 3;
        }
        return super.getPriority(queryInfo);
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public Map<String, String> getQueryParams(QueryInfo queryInfo) {
        Map<String, String> queryParams = super.getQueryParams(queryInfo);
        if (queryParams != null && !queryParams.isEmpty()) {
            String str = queryParams.get("rankName");
            queryParams.remove("rankName");
            String str2 = queryParams.get("rankOrder");
            queryParams.remove("rankOrder");
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                ItemRankInfo itemRankInfo = new ItemRankInfo();
                itemRankInfo.name = str;
                itemRankInfo.order = str2;
                queryParams.put("rankInfo", HttpUtils.createGson().toJson(itemRankInfo));
            }
        }
        return queryParams;
    }

    /* loaded from: classes2.dex */
    public static class DataListResponseData {
        @SerializedName("indexHash")
        public long indexHash;
        @SerializedName("isLastPage")
        public boolean isLastPage;
        @SerializedName("items")
        public List<ItemSuggestion> items;
        @SerializedName("nextPos")
        public int nextPosition;

        public DataListResponseData() {
            this.nextPosition = 0;
            this.isLastPage = false;
            this.indexHash = -1L;
        }
    }
}
