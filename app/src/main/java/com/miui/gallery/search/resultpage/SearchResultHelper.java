package com.miui.gallery.search.resultpage;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.search.SearchConfig;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.BaseSuggestionResult;
import com.miui.gallery.search.core.result.ErrorInfo;
import com.miui.gallery.search.core.result.SourceResult;
import com.miui.gallery.search.core.result.SuggestionResult;
import com.miui.gallery.search.core.resultprocessor.LinearResultProcessor;
import com.miui.gallery.search.core.resultprocessor.ResultProcessor;
import com.miui.gallery.search.core.suggestion.GroupedSuggestionCursor;
import com.miui.gallery.search.core.suggestion.ListSuggestionCursor;
import com.miui.gallery.search.core.suggestion.RankInfo;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.search.utils.SearchUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class SearchResultHelper {
    public WeakReference<SuggestionResult> mCachedResult;
    public ResultProcessor<SuggestionResult> mDataListResultProcessor;
    public ResultProcessor<SuggestionResult> mFilterResultProcessor;
    public QueryInfo mQueryInfo;
    public Map<String, String> mNextLoadParams = null;
    public Long mDataListIndexHash = null;
    public List<Suggestion> mDataListSuggestions = new ArrayList();
    public boolean mLoadCompleted = false;
    public boolean mIsInvalid = false;
    public List<RankInfo> mDataListRankInfos = null;
    public RankInfo mCurrentDataListRankInfo = null;
    public final Object mProcessLock = new Object();
    public final Object mCachedResultLock = new Object();
    public Map<String, String> mFilterQueryParams = null;

    public SearchResultHelper(QueryInfo queryInfo, SuggestionSection suggestionSection) {
        this.mQueryInfo = queryInfo;
        handleSection(suggestionSection);
    }

    public SearchResultHelper(QueryInfo queryInfo, GroupedSuggestionCursor<SuggestionSection> groupedSuggestionCursor) {
        this.mQueryInfo = queryInfo;
        for (int i = 0; i < groupedSuggestionCursor.getGroupCount(); i++) {
            handleSection(groupedSuggestionCursor.mo1334getGroup(i));
        }
    }

    /* renamed from: com.miui.gallery.search.resultpage.SearchResultHelper$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType;

        static {
            int[] iArr = new int[SearchConstants.SectionType.values().length];
            $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType = iArr;
            try {
                iArr[SearchConstants.SectionType.SECTION_TYPE_FILTER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public final void handleSection(SuggestionSection suggestionSection) {
        if (AnonymousClass1.$SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[suggestionSection.getSectionType().ordinal()] == 1) {
            if (this.mFilterQueryParams != null || TextUtils.isEmpty(suggestionSection.getDataURI())) {
                return;
            }
            this.mFilterQueryParams = new HashMap();
            Uri parse = Uri.parse(suggestionSection.getDataURI());
            for (String str : parse.getQueryParameterNames()) {
                this.mFilterQueryParams.put(str, parse.getQueryParameter(str));
            }
            this.mFilterResultProcessor = createFilterProcessor();
        } else if (this.mNextLoadParams == null && !TextUtils.isEmpty(suggestionSection.getDataURI())) {
            this.mDataListRankInfos = suggestionSection.getRankInfos();
            HashMap hashMap = new HashMap();
            this.mNextLoadParams = hashMap;
            hashMap.put("pos", "0");
            this.mNextLoadParams.put("num", String.valueOf(getDataLoadCount(this.mQueryInfo)));
            Uri parse2 = Uri.parse(suggestionSection.getDataURI());
            for (String str2 : parse2.getQueryParameterNames()) {
                this.mNextLoadParams.put(str2, parse2.getQueryParameter(str2));
            }
            this.mDataListResultProcessor = mo1342createDataListResultProcessor(getDataListRankInfo());
        }
    }

    public ResultProcessor<SuggestionResult> getDataListResultProcessor() {
        return this.mDataListResultProcessor;
    }

    public ResultProcessor<SuggestionResult> getFilterResultProcessor() {
        return this.mFilterResultProcessor;
    }

    public SuggestionResult getResult() {
        SuggestionResult suggestionResult;
        synchronized (this.mCachedResultLock) {
            WeakReference<SuggestionResult> weakReference = this.mCachedResult;
            suggestionResult = (weakReference == null || weakReference.get() == null || this.mCachedResult.get().isClosed()) ? null : this.mCachedResult.get();
        }
        return suggestionResult;
    }

    public QueryInfo.Builder buildDataListQueryInfo() {
        synchronized (this.mCachedResultLock) {
            if (canLoadNextPage()) {
                QueryInfo.Builder addParams = new QueryInfo.Builder(SearchConstants.SearchType.SEARCH_TYPE_RESULT_LIST).addParams(this.mNextLoadParams);
                appendRankInfoToQuery(addParams, getDataListRankInfo());
                return addParams;
            }
            return null;
        }
    }

    public boolean canLoadNextPage() {
        boolean z;
        Map<String, String> map;
        synchronized (this.mCachedResultLock) {
            z = !isLoadCompleted() && (map = this.mNextLoadParams) != null && !map.isEmpty();
        }
        return z;
    }

    public QueryInfo.Builder buildFilterListQueryInfoBuilder() {
        if (needLoadFilterList()) {
            return new QueryInfo.Builder(SearchConstants.SearchType.SEARCH_TYPE_FILTER).addParams(this.mFilterQueryParams);
        }
        return null;
    }

    public boolean needLoadFilterList() {
        Map<String, String> map = this.mFilterQueryParams;
        return map != null && !map.isEmpty();
    }

    public boolean isInvalid() {
        return this.mIsInvalid;
    }

    public boolean isLoadCompleted() {
        return this.mLoadCompleted;
    }

    public RankInfo getDataListRankInfo() {
        RankInfo rankInfo = this.mCurrentDataListRankInfo;
        if (rankInfo != null) {
            return rankInfo;
        }
        List<RankInfo> list = this.mDataListRankInfos;
        if (list != null && !list.isEmpty()) {
            return this.mDataListRankInfos.get(0);
        }
        return null;
    }

    public void resetCacheInfo() {
        synchronized (this.mProcessLock) {
            synchronized (this.mCachedResultLock) {
                this.mNextLoadParams.put("pos", "0");
                this.mNextLoadParams.put("num", String.valueOf(Math.max(this.mDataListSuggestions.size(), getDataLoadCount(this.mQueryInfo))));
                this.mIsInvalid = true;
                this.mLoadCompleted = false;
            }
            this.mDataListSuggestions.clear();
        }
        this.mDataListIndexHash = null;
        SearchLog.w("SearchResultHelper", "On reset cache info called!");
    }

    public final void appendRankInfoToQuery(QueryInfo.Builder builder, RankInfo rankInfo) {
        if (rankInfo != null) {
            builder.addParam("rankName", rankInfo.getName());
            builder.addParam("rankOrder", rankInfo.getOrder());
        }
    }

    /* renamed from: createDataListResultProcessor */
    public ResultProcessor<SuggestionResult> mo1342createDataListResultProcessor(RankInfo rankInfo) {
        return new DataListResultProcessor(rankInfo);
    }

    public ResultProcessor<SuggestionResult> createFilterProcessor() {
        return new LinearResultProcessor();
    }

    public int getDataLoadCount(QueryInfo queryInfo) {
        return SearchConfig.get().getResultConfig().getTagLocationLoadCount();
    }

    /* loaded from: classes2.dex */
    public class DataListResultProcessor extends ResultProcessor<SuggestionResult> {
        public final RankInfo mRankInfo;

        public DataListResultProcessor(RankInfo rankInfo) {
            this.mRankInfo = rankInfo;
        }

        public final void updateCacheResult(QueryInfo queryInfo, List<Suggestion> list, ErrorInfo errorInfo) {
            SuggestionResult createSuggestionResult = createSuggestionResult(list, queryInfo, this.mRankInfo, errorInfo);
            synchronized (SearchResultHelper.this.mCachedResultLock) {
                SearchResultHelper.this.mCachedResult = new WeakReference(createSuggestionResult);
            }
        }

        /* JADX WARN: Type inference failed for: r1v10, types: [com.miui.gallery.search.core.suggestion.SuggestionCursor, android.database.Cursor] */
        @Override // com.miui.gallery.search.core.resultprocessor.ResultProcessor
        public SuggestionResult getMergedResult(List<SourceResult> list) {
            SearchResultHelper searchResultHelper;
            DataListSourceResult dataListResult = getDataListResult(list);
            if (dataListResult == null || !checkRankInfo(dataListResult)) {
                return null;
            }
            if (SearchUtils.isErrorResult(dataListResult) && dataListResult.isEmpty()) {
                return null;
            }
            synchronized (SearchResultHelper.this.mProcessLock) {
                if (SearchResultHelper.this.mDataListSuggestions.size() <= 0 && !"0".equals(dataListResult.getQueryInfo().getParam("pos"))) {
                    SearchLog.w("SearchResultHelper", "Later pages arrived before first page!");
                    return SearchResultHelper.this.getResult();
                } else if (!checkIndexHash(dataListResult)) {
                    SearchResultHelper searchResultHelper2 = SearchResultHelper.this;
                    updateCacheResult(searchResultHelper2.mQueryInfo, searchResultHelper2.mDataListSuggestions, new ErrorInfo(9));
                    SearchResultHelper.this.mDataListSuggestions.clear();
                    return SearchResultHelper.this.getResult();
                } else {
                    if (!dataListResult.isEmpty() && dataListResult.getNextPosition() > SearchResultHelper.this.mDataListSuggestions.size()) {
                        ?? data = dataListResult.getData();
                        for (int i = 0; i < data.getCount(); i++) {
                            if (data.moveToPosition(i) && data.getCurrent() != null) {
                                SearchResultHelper.this.mDataListSuggestions.add(toSuggestion(data.getCurrent()));
                            }
                        }
                    }
                    SearchResultHelper searchResultHelper3 = SearchResultHelper.this;
                    updateCacheResult(searchResultHelper3.mQueryInfo, searchResultHelper3.mDataListSuggestions, dataListResult.getErrorInfo());
                    synchronized (SearchResultHelper.this.mCachedResultLock) {
                        if (!dataListResult.isLastPage()) {
                            SearchResultHelper.this.mNextLoadParams.put("pos", String.valueOf(dataListResult.getNextPosition()));
                            SearchResultHelper searchResultHelper4 = SearchResultHelper.this;
                            searchResultHelper4.mNextLoadParams.put("num", String.valueOf(searchResultHelper4.getDataLoadCount(searchResultHelper4.mQueryInfo)));
                        } else {
                            SearchResultHelper.this.mLoadCompleted = true;
                        }
                        searchResultHelper = SearchResultHelper.this;
                        searchResultHelper.mIsInvalid = false;
                    }
                    return searchResultHelper.getResult();
                }
            }
        }

        public boolean checkRankInfo(DataListSourceResult dataListSourceResult) {
            if (this.mRankInfo != null || !TextUtils.isEmpty(dataListSourceResult.getQueryInfo().getParam("rankName"))) {
                String param = dataListSourceResult.getQueryInfo().getParam("rankName");
                RankInfo rankInfo = this.mRankInfo;
                String str = null;
                if (TextUtils.equals(param, rankInfo == null ? null : rankInfo.getName())) {
                    String param2 = dataListSourceResult.getQueryInfo().getParam("rankOrder");
                    RankInfo rankInfo2 = this.mRankInfo;
                    if (rankInfo2 != null) {
                        str = rankInfo2.getOrder();
                    }
                    if (TextUtils.equals(param2, str)) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }

        public boolean checkIndexHash(DataListSourceResult dataListSourceResult) {
            SearchResultHelper searchResultHelper;
            if (SearchResultHelper.this.mDataListIndexHash != null && dataListSourceResult.getIndexHash() != SearchResultHelper.this.mDataListIndexHash.longValue()) {
                synchronized (SearchResultHelper.this.mCachedResultLock) {
                    SearchResultHelper.this.mNextLoadParams.put("pos", "0");
                    Map<String, String> map = SearchResultHelper.this.mNextLoadParams;
                    int nextPosition = dataListSourceResult.getNextPosition();
                    SearchResultHelper searchResultHelper2 = SearchResultHelper.this;
                    map.put("num", String.valueOf(Math.max(nextPosition, searchResultHelper2.getDataLoadCount(searchResultHelper2.mQueryInfo))));
                    searchResultHelper = SearchResultHelper.this;
                    searchResultHelper.mIsInvalid = true;
                    searchResultHelper.mLoadCompleted = false;
                }
                searchResultHelper.mDataListIndexHash = null;
                SearchLog.d("SearchResultHelper", "On check index hash failed, old %s, new %s, next pos %d", String.valueOf((Object) null), String.valueOf(dataListSourceResult.getIndexHash()), Integer.valueOf(dataListSourceResult.getNextPosition()));
                return false;
            }
            SearchResultHelper searchResultHelper3 = SearchResultHelper.this;
            if (searchResultHelper3.mDataListIndexHash == null) {
                searchResultHelper3.mDataListIndexHash = Long.valueOf(dataListSourceResult.getIndexHash());
            }
            return true;
        }

        public SuggestionResult createSuggestionResult(List<Suggestion> list, QueryInfo queryInfo, RankInfo rankInfo, ErrorInfo errorInfo) {
            return new BaseSuggestionResult(queryInfo, new ListSuggestionCursor(queryInfo, new ArrayList(list), rankInfo != null ? createRankInfoBundle(rankInfo) : null), errorInfo);
        }

        public DataListSourceResult getDataListResult(List<SourceResult> list) {
            for (SourceResult sourceResult : list) {
                if (sourceResult instanceof DataListSourceResult) {
                    return (DataListSourceResult) sourceResult;
                }
            }
            return null;
        }

        public Bundle createRankInfoBundle(RankInfo rankInfo) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("rankInfo", rankInfo);
            return bundle;
        }
    }
}
