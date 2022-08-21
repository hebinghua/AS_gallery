package com.miui.gallery.search.resultpage;

import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import com.miui.gallery.R;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.StatusHandleHelper;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.display.RequestLoadMoreListener;
import com.miui.gallery.search.core.query.QueryLoader;
import com.miui.gallery.search.core.result.SuggestionResult;
import com.miui.gallery.search.core.resultprocessor.SectionedResultProcessor;
import com.miui.gallery.search.core.suggestion.GroupedSuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.search.utils.SearchUtils;
import com.miui.gallery.ui.AIAlbumStatusHelper;
import com.miui.gallery.ui.BaseMediaFragment;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.widget.EmptyPage;

/* loaded from: classes2.dex */
public abstract class SearchResultFragmentBase extends BaseMediaFragment implements RequestLoadMoreListener {
    public View mEmptyView;
    public boolean mIsMultiWindow;
    public long mLastInvalidTime;
    public String mQueryText;
    public QueryInfo mResultSectionQueryInfo;
    public SearchResultHelper mSectionDataResultHelper;
    public StatusHandleHelper mStatusHandleHelper = new StatusHandleHelper();
    public boolean mSupportFeedback = false;
    public boolean mInFeedbackTaskMode = false;
    public SectionedResultProcessor mSectionProcessor = new SectionedResultProcessor();
    public LoaderManager.LoaderCallbacks<SuggestionResult<GroupedSuggestionCursor<SuggestionSection>>> mSectionsLoaderCallback = new LoaderManager.LoaderCallbacks<SuggestionResult<GroupedSuggestionCursor<SuggestionSection>>>() { // from class: com.miui.gallery.search.resultpage.SearchResultFragmentBase.1
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<SuggestionResult<GroupedSuggestionCursor<SuggestionSection>>> onCreateLoader(int i, Bundle bundle) {
            if (SearchResultFragmentBase.this.mResultSectionQueryInfo != null) {
                QueryInfo.Builder appendSerialInfo = new QueryInfo.Builder().cloneFrom(SearchResultFragmentBase.this.mResultSectionQueryInfo).setAppendSerialInfo(true);
                if (SearchResultFragmentBase.this.shouldUserPersistentResponse(bundle)) {
                    appendSerialInfo.addParam("use_persistent_response", String.valueOf(true));
                }
                return new QueryLoader(SearchResultFragmentBase.this.mActivity, appendSerialInfo.build(), SearchResultFragmentBase.this.mSectionProcessor, false, SearchResultFragmentBase.this.receiveResultUpdates(), false);
            }
            return null;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<SuggestionResult<GroupedSuggestionCursor<SuggestionSection>>> loader, SuggestionResult<GroupedSuggestionCursor<SuggestionSection>> suggestionResult) {
            int i;
            if (suggestionResult != null) {
                if (suggestionResult.getData() == null || suggestionResult.getData().getGroupCount() <= 0) {
                    SearchResultFragmentBase.this.closeLoadMore();
                    SearchResultFragmentBase.this.mSectionDataResultHelper = null;
                } else {
                    SearchResultFragmentBase searchResultFragmentBase = SearchResultFragmentBase.this;
                    searchResultFragmentBase.mSectionDataResultHelper = searchResultFragmentBase.createSearchResultHelper(searchResultFragmentBase.mResultSectionQueryInfo, suggestionResult.getData());
                    SearchResultFragmentBase.this.openLoadMore();
                    SearchResultFragmentBase.this.onLoadMoreRequested();
                    if (SearchResultFragmentBase.this.mSectionDataResultHelper.needLoadFilterList()) {
                        SearchResultFragmentBase.this.restartFilterLoader(null);
                    }
                }
                i = SearchUtils.getErrorStatus(suggestionResult);
            } else {
                SearchLog.w("SearchResultFragmentBase", "Loader %s load finished, got no data available", Integer.valueOf(loader.getId()));
                i = 0;
            }
            SearchResultFragmentBase.this.mStatusHandleHelper.updateResultStatus(i);
            if (i == 13) {
                AIAlbumStatusHelper.requestOpenCloudControlSearch(SearchResultFragmentBase.this.getFrom());
            }
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<SuggestionResult<GroupedSuggestionCursor<SuggestionSection>>> loader) {
            SearchResultFragmentBase.this.changeSuggestions(null, null);
            SearchResultFragmentBase.this.closeLoadMore();
            SearchResultFragmentBase.this.mStatusHandleHelper.updateResultStatus(-1);
            SearchResultFragmentBase.this.mSectionDataResultHelper = null;
        }
    };
    public LoaderManager.LoaderCallbacks<SuggestionResult> mSectionDataLoaderCallback = new LoaderManager.LoaderCallbacks<SuggestionResult>() { // from class: com.miui.gallery.search.resultpage.SearchResultFragmentBase.2
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<SuggestionResult> loader) {
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<SuggestionResult> onCreateLoader(int i, Bundle bundle) {
            QueryInfo.Builder sectionDataQueryInfoBuilder;
            SearchResultFragmentBase searchResultFragmentBase = SearchResultFragmentBase.this;
            if (searchResultFragmentBase.mSectionDataResultHelper != null && (sectionDataQueryInfoBuilder = searchResultFragmentBase.getSectionDataQueryInfoBuilder()) != null) {
                sectionDataQueryInfoBuilder.setAppendSerialInfo(true);
                if (SearchResultFragmentBase.this.shouldUserPersistentResponse(bundle)) {
                    sectionDataQueryInfoBuilder.addParam("use_persistent_response", String.valueOf(true));
                }
                return new QueryLoader(SearchResultFragmentBase.this.mActivity, sectionDataQueryInfoBuilder.build(), SearchResultFragmentBase.this.mSectionDataResultHelper.getDataListResultProcessor(), false, SearchResultFragmentBase.this.receiveResultUpdates(), false);
            }
            SearchResultFragmentBase.this.closeLoadMore();
            return null;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<SuggestionResult> loader, SuggestionResult suggestionResult) {
            int i;
            int i2 = 0;
            if (SearchResultFragmentBase.this.mSectionDataResultHelper.isInvalid()) {
                if (Math.abs(SystemClock.elapsedRealtime() - SearchResultFragmentBase.this.mLastInvalidTime) < 3000) {
                    if (suggestionResult != null) {
                        i = SearchUtils.getErrorStatus(suggestionResult);
                        SearchResultFragmentBase searchResultFragmentBase = SearchResultFragmentBase.this;
                        searchResultFragmentBase.changeSuggestions(searchResultFragmentBase.mResultSectionQueryInfo, suggestionResult.getData());
                    } else {
                        SearchResultFragmentBase.this.changeSuggestions(null, null);
                        i = 11;
                    }
                    i2 = i;
                    SearchResultFragmentBase.this.closeLoadMore();
                    SearchStatUtils.reportEvent(SearchResultFragmentBase.this.getFrom(), "result_invalid_hash", "Query", SearchResultFragmentBase.this.mResultSectionQueryInfo.toString(), "Time", String.valueOf(System.currentTimeMillis()));
                } else {
                    if (suggestionResult != null) {
                        SearchResultFragmentBase searchResultFragmentBase2 = SearchResultFragmentBase.this;
                        searchResultFragmentBase2.changeSuggestions(searchResultFragmentBase2.mResultSectionQueryInfo, suggestionResult.getData());
                    } else {
                        SearchLog.w("SearchResultFragmentBase", "Loader %s load finished, invalid hash code got no data available", Integer.valueOf(loader.getId()));
                    }
                    Bundle bundle = new Bundle(1);
                    bundle.putBoolean("force_requery", true);
                    SearchResultFragmentBase.this.restartSectionDataLoader(bundle);
                }
                SearchResultFragmentBase.this.mLastInvalidTime = SystemClock.elapsedRealtime();
                SearchResultFragmentBase.this.mStatusHandleHelper.updateResultStatus(i2);
                return;
            }
            if (suggestionResult != null) {
                SearchResultFragmentBase searchResultFragmentBase3 = SearchResultFragmentBase.this;
                searchResultFragmentBase3.changeSuggestions(searchResultFragmentBase3.mResultSectionQueryInfo, suggestionResult.getData());
                i2 = SearchUtils.getErrorStatus(suggestionResult);
            } else {
                SearchLog.w("SearchResultFragmentBase", "Loader %s load finished, got no data available", Integer.valueOf(loader.getId()));
            }
            if (SearchResultFragmentBase.this.mSectionDataResultHelper.isLoadCompleted()) {
                SearchResultFragmentBase.this.onLoadComplete();
            } else if (!SearchConstants.isErrorStatus(i2)) {
                SearchResultFragmentBase.this.openLoadMore();
                if (SearchResultFragmentBase.this.getErrorViewAdapter().isEmptyDataView() && !SearchResultFragmentBase.this.mSectionDataResultHelper.isInvalid()) {
                    SearchResultFragmentBase.this.onLoadMoreRequested();
                }
            } else {
                SearchResultFragmentBase.this.closeLoadMore();
            }
            SearchResultFragmentBase.this.mStatusHandleHelper.updateResultStatus(i2);
        }
    };
    public LoaderManager.LoaderCallbacks<SuggestionResult> mFilterLoaderCallback = new LoaderManager.LoaderCallbacks<SuggestionResult>() { // from class: com.miui.gallery.search.resultpage.SearchResultFragmentBase.3
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<SuggestionResult> loader) {
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<SuggestionResult> onCreateLoader(int i, Bundle bundle) {
            QueryInfo.Builder buildFilterListQueryInfoBuilder;
            SearchResultHelper searchResultHelper = SearchResultFragmentBase.this.mSectionDataResultHelper;
            if (searchResultHelper == null || (buildFilterListQueryInfoBuilder = searchResultHelper.buildFilterListQueryInfoBuilder()) == null) {
                return null;
            }
            buildFilterListQueryInfoBuilder.setAppendSerialInfo(true);
            if (SearchResultFragmentBase.this.shouldUserPersistentResponse(bundle)) {
                buildFilterListQueryInfoBuilder.addParam("use_persistent_response", String.valueOf(true));
            }
            return new QueryLoader(SearchResultFragmentBase.this.mActivity, buildFilterListQueryInfoBuilder.build(), SearchResultFragmentBase.this.mSectionDataResultHelper.getFilterResultProcessor());
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<SuggestionResult> loader, SuggestionResult suggestionResult) {
            if (suggestionResult != null) {
                SearchResultFragmentBase.this.changeFilterData(suggestionResult.getQueryInfo(), suggestionResult.getData());
            } else {
                SearchLog.w("SearchResultFragmentBase", "Loader %s load finished, got no data available", Integer.valueOf(loader.getId()));
            }
        }
    };

    public abstract void changeFilterData(QueryInfo queryInfo, SuggestionCursor suggestionCursor);

    public abstract void changeSuggestions(QueryInfo queryInfo, SuggestionCursor suggestionCursor);

    public abstract void closeLoadMore();

    public abstract StatusHandleHelper.AbstractErrorViewAdapter getErrorViewAdapter();

    public abstract String getFrom();

    public abstract int getLayoutResource();

    public abstract View getResultView();

    public abstract void onInflateView(View view, Bundle bundle, Uri uri);

    public abstract void onLoadComplete();

    public abstract void openLoadMore();

    public boolean receiveResultUpdates() {
        return false;
    }

    public abstract void restartFilterLoader(Bundle bundle);

    public abstract void restartSectionDataLoader(Bundle bundle);

    public abstract void restartSectionLoader(Bundle bundle);

    public abstract boolean usePersistentResponse();

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(getLayoutResource(), viewGroup, false);
        Uri data = this.mActivity.getIntent().getData();
        this.mResultSectionQueryInfo = initResultQueryInfoBuilder(data).build();
        onInflateView(inflate, bundle, data);
        updateTitle(data.getQueryParameter("title"));
        initStatusHandlerHelper(inflate);
        return inflate;
    }

    public boolean supportFeedback() {
        return this.mSupportFeedback;
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        SearchStatUtils.createNewSerial(getFrom());
        restartSectionLoader(null);
        openLoadMore();
        this.mStatusHandleHelper.refreshInfoViews();
        this.mIsMultiWindow = !BaseBuildUtil.isLargeScreenDevice() && ActivityCompat.isInMultiWindowMode(this.mActivity);
    }

    public void onLoadMoreRequested() {
        SearchResultHelper searchResultHelper = this.mSectionDataResultHelper;
        if (searchResultHelper != null && searchResultHelper.canLoadNextPage()) {
            restartSectionDataLoader(null);
        } else {
            closeLoadMore();
        }
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        View view = this.mEmptyView;
        if (view != null && (view instanceof EmptyPage)) {
            ((EmptyPage) view).resumeMaml();
        }
        doRetry();
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        View view = this.mEmptyView;
        if (view == null || !(view instanceof EmptyPage)) {
            return;
        }
        ((EmptyPage) view).pauseMaml();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        View view = this.mEmptyView;
        if (view != null && (view instanceof EmptyPage)) {
            ((EmptyPage) view).destroyMaml();
        }
        super.onDestroyView();
    }

    public void doRetry() {
        if (SearchConstants.isErrorStatus(this.mStatusHandleHelper.getResultStatus())) {
            openLoadMore();
            this.mStatusHandleHelper.refreshInfoViews();
            SearchResultHelper searchResultHelper = this.mSectionDataResultHelper;
            if (searchResultHelper == null) {
                restartSectionLoader(null);
                return;
            }
            if (searchResultHelper.canLoadNextPage()) {
                restartSectionDataLoader(null);
            } else {
                closeLoadMore();
            }
            if (!this.mSectionDataResultHelper.needLoadFilterList()) {
                return;
            }
            restartFilterLoader(null);
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        SearchStatUtils.onCompleteSerial(getFrom());
    }

    public QueryInfo.Builder initResultQueryInfoBuilder(Uri uri) {
        QueryInfo.Builder builder = new QueryInfo.Builder(SearchConstants.SearchType.SEARCH_TYPE_RESULT);
        for (String str : uri.getQueryParameterNames()) {
            if (str.equals("title")) {
                this.mQueryText = uri.getQueryParameter(str);
            } else if (str.equals("inFeedbackTaskMode")) {
                this.mInFeedbackTaskMode = uri.getBooleanQueryParameter(str, false);
            } else if (str.equals("supportFeedback")) {
                this.mSupportFeedback = uri.getBooleanQueryParameter(str, false);
            } else {
                builder.addParam(str, uri.getQueryParameter(str));
            }
        }
        return builder;
    }

    public SearchResultHelper createSearchResultHelper(QueryInfo queryInfo, GroupedSuggestionCursor<SuggestionSection> groupedSuggestionCursor) {
        return new SearchResultHelper(queryInfo, groupedSuggestionCursor);
    }

    public void initStatusHandlerHelper(View view) {
        this.mEmptyView = view.findViewById(R.id.empty_view);
        this.mStatusHandleHelper.init(getResultView(), view.findViewById(R.id.info_view), view.findViewById(R.id.loading_view), view.findViewById(R.id.empty_view), getErrorViewAdapter());
    }

    public void updateTitle(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mActivity.getAppCompatActionBar().setTitle(str);
        } else {
            this.mActivity.getAppCompatActionBar().setTitle(getDefaultTitle());
        }
    }

    public String getDefaultTitle() {
        return getString(R.string.search_image_result_title);
    }

    public final boolean shouldUserPersistentResponse(Bundle bundle) {
        if (usePersistentResponse()) {
            return bundle == null || !bundle.getBoolean("force_requery", false);
        }
        return false;
    }

    public QueryInfo.Builder getSectionDataQueryInfoBuilder() {
        return this.mSectionDataResultHelper.buildDataListQueryInfo();
    }

    @Override // com.miui.gallery.app.fragment.MiuiFragment, androidx.fragment.app.Fragment
    public void onMultiWindowModeChanged(boolean z) {
        super.onMultiWindowModeChanged(z);
        this.mIsMultiWindow = !BaseBuildUtil.isLargeScreenDevice() && z;
    }
}
