package com.miui.gallery.search.suggestionpage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.search.SearchConfig;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.SearchFragmentBase;
import com.miui.gallery.search.StatusHandleHelper;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.context.SearchContext;
import com.miui.gallery.search.core.display.SectionedSuggestionAdapter;
import com.miui.gallery.search.core.display.SectionedSuggestionItemDecoration;
import com.miui.gallery.search.core.display.SuggestionViewFactory;
import com.miui.gallery.search.core.query.QueryLoader;
import com.miui.gallery.search.core.result.SuggestionResult;
import com.miui.gallery.search.core.resultprocessor.SectionedResultProcessor;
import com.miui.gallery.search.core.suggestion.GroupedSuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.search.utils.SearchUtils;
import com.miui.gallery.util.concurrent.ThreadManager;

/* loaded from: classes2.dex */
public class SuggestionFragment extends SearchFragmentBase {
    public SectionedSuggestionAdapter mAdapter;
    public RecyclerView mDataView;
    public ErrorViewAdapter mErrorViewAdapter;
    public boolean mIsLoading;
    public StatusHandleHelper mStatusHandleHelper = new StatusHandleHelper();
    public String mQueryText = null;
    public final Handler mQueryHandler = new Handler() { // from class: com.miui.gallery.search.suggestionpage.SuggestionFragment.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1) {
                SuggestionFragment.this.restartDataLoader(SearchConstants.SearchType.SEARCH_TYPE_SUGGESTION, (String) message.obj, false);
            }
        }
    };
    public SectionedResultProcessor mSectionProcessor = new SuggestionResultProcessor();
    public LoaderManager.LoaderCallbacks<SuggestionResult<GroupedSuggestionCursor<SuggestionSection>>> mDataLoaderCallback = new LoaderManager.LoaderCallbacks<SuggestionResult<GroupedSuggestionCursor<SuggestionSection>>>() { // from class: com.miui.gallery.search.suggestionpage.SuggestionFragment.3
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<SuggestionResult<GroupedSuggestionCursor<SuggestionSection>>> onCreateLoader(int i, Bundle bundle) {
            SearchConstants.SearchType searchType = SearchConstants.SearchType.SEARCH_TYPE_SEARCH;
            String string = bundle.getString("data_loader_extra_text");
            if (TextUtils.isEmpty(string)) {
                SearchLog.e("SuggestionFragment", "Invalid query text for loader!");
                return null;
            }
            SearchStatUtils.onCompleteSerial("from_suggestion");
            SearchStatUtils.createNewSerial("from_suggestion");
            if (bundle.getSerializable("data_loader_extra_type") != null) {
                searchType = (SearchConstants.SearchType) bundle.getSerializable("data_loader_extra_type");
            }
            return new QueryLoader(SuggestionFragment.this.mActivity, new QueryInfo.Builder(searchType).addParam("query", string).addParam("enableShortcut", String.valueOf(bundle.getBoolean("data_loader_extra_enable_shortcut", false))).setAppendSerialInfo(true).build(), SuggestionFragment.this.mSectionProcessor, false, false, true);
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<SuggestionResult<GroupedSuggestionCursor<SuggestionSection>>> loader, SuggestionResult<GroupedSuggestionCursor<SuggestionSection>> suggestionResult) {
            int i;
            QueryInfo queryInfo = ((QueryLoader) loader).getQueryInfo();
            boolean isDone = isDone(suggestionResult);
            if (suggestionResult != null) {
                TrackController.trackExpose("403.20.0.1.11266", AutoTracking.getRef());
                if (suggestionResult.isEmpty()) {
                    SearchLog.d("SuggestionFragment", "Loader %s load finished, got empty result", Integer.valueOf(loader.getId()));
                } else {
                    SearchLog.d("SuggestionFragment", "Loader %s load finished, got %s results", Integer.valueOf(loader.getId()), Integer.valueOf(suggestionResult.getData().getCount()));
                }
                SuggestionFragment.this.mAdapter.changeSuggestions(queryInfo, suggestionResult.getData());
                i = SearchUtils.getErrorStatus(suggestionResult);
                final String shortCutUri = getShortCutUri(suggestionResult);
                if (isDone && !TextUtils.isEmpty(shortCutUri) && suggestionResult.getData().getCount() == 1) {
                    ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.search.suggestionpage.SuggestionFragment.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            SuggestionFragment.this.mAdapter.getActionClickListener().onClick(null, 1, shortCutUri, SearchStatUtils.buildSearchEventExtras(null, new String[]{"from"}, new String[]{"from_suggestion"}));
                        }
                    });
                }
            } else {
                SearchLog.w("SuggestionFragment", "Loader %s load finished, got no data available", Integer.valueOf(loader.getId()));
                i = 0;
            }
            SuggestionFragment.this.mIsLoading = !isDone;
            StatusHandleHelper statusHandleHelper = SuggestionFragment.this.mStatusHandleHelper;
            if (SuggestionFragment.this.mIsLoading) {
                i = -1;
            }
            statusHandleHelper.updateResultStatus(i);
            if (isDone) {
                SearchStatUtils.reportEvent(queryInfo.getSearchType() == SearchConstants.SearchType.SEARCH_TYPE_SEARCH ? "from_search" : "from_suggestion", "suggestion_recall", "ItemCount", String.valueOf(getResultItemCount(suggestionResult)));
            }
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<SuggestionResult<GroupedSuggestionCursor<SuggestionSection>>> loader) {
            if (SuggestionFragment.this.mAdapter != null) {
                SuggestionFragment.this.mAdapter.changeSuggestions(null, null);
                SuggestionFragment.this.mStatusHandleHelper.updateResultStatus(-1);
            }
            SuggestionFragment.this.mIsLoading = false;
        }

        public final boolean isDone(SuggestionResult suggestionResult) {
            return (suggestionResult == null || suggestionResult.getResultExtras() == null || !suggestionResult.getResultExtras().getBoolean("is_done", true)) ? false : true;
        }

        public final String getShortCutUri(SuggestionResult suggestionResult) {
            if (suggestionResult == null || suggestionResult.getData() == null || suggestionResult.getData().getExtras() == null) {
                return null;
            }
            return suggestionResult.getData().getExtras().getString("short_cut_uri");
        }

        public final int getResultItemCount(SuggestionResult suggestionResult) {
            if (suggestionResult == null || suggestionResult.getData() == null || suggestionResult.getData().getExtras() == null) {
                return 0;
            }
            return suggestionResult.getData().getExtras().getInt("itemCount");
        }
    };

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "search_suggestions";
    }

    @Override // com.miui.gallery.search.SearchFragmentBase
    public void setQueryText(String str, boolean z, boolean z2) {
        SearchConstants.SearchType searchType;
        if (!TextUtils.isEmpty(str)) {
            this.mQueryText = str;
            if (this.mAdapter != null) {
                this.mStatusHandleHelper.updateResultStatus(-1);
                this.mAdapter.changeSuggestions(null, null);
            }
            if (z) {
                searchType = SearchConstants.SearchType.SEARCH_TYPE_SEARCH;
            } else {
                searchType = SearchConstants.SearchType.SEARCH_TYPE_SUGGESTION;
            }
            requery(searchType, str, z2);
        }
    }

    @Override // com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        doRetryIfNeeded();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        SearchStatUtils.onCompleteSerial("from_suggestion");
    }

    public void doRetryIfNeeded() {
        if (!SearchConstants.isErrorStatus(this.mStatusHandleHelper.getResultStatus()) || TextUtils.isEmpty(this.mQueryText)) {
            return;
        }
        requery(SearchConstants.SearchType.SEARCH_TYPE_SEARCH, this.mQueryText, false);
    }

    public final void requery(SearchConstants.SearchType searchType, String str, boolean z) {
        if (this.mQueryHandler.hasMessages(1)) {
            this.mQueryHandler.removeMessages(1);
        }
        if (searchType == SearchConstants.SearchType.SEARCH_TYPE_SEARCH) {
            restartDataLoader(searchType, str, z);
            return;
        }
        this.mQueryHandler.sendMessageDelayed(this.mQueryHandler.obtainMessage(1, str), 300L);
    }

    public final void restartDataLoader(SearchConstants.SearchType searchType, String str, boolean z) {
        if (!isAdded() || getLoaderManager() == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("data_loader_extra_type", searchType);
        bundle.putString("data_loader_extra_text", str);
        bundle.putBoolean("data_loader_extra_enable_shortcut", z);
        getLoaderManager().restartLoader(1, bundle, this.mDataLoaderCallback);
        this.mIsLoading = true;
        this.mStatusHandleHelper.refreshInfoViews();
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.navigation_fragment, viewGroup, false);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.data_view);
        this.mDataView = recyclerView;
        recyclerView.setItemAnimator(null);
        SearchContext searchContext = SearchContext.getInstance();
        this.mDataView.setLayoutManager(new LinearLayoutManager(this.mActivity, 1, false));
        SuggestionAdapter suggestionAdapter = new SuggestionAdapter(this.mActivity, searchContext.getSuggestionViewFactory(), "from_suggestion");
        this.mAdapter = suggestionAdapter;
        this.mDataView.setAdapter(suggestionAdapter);
        this.mDataView.addItemDecoration(new SectionedSuggestionItemDecoration(this.mActivity, this.mAdapter, true, getResources().getDimensionPixelSize(R.dimen.default_suggestion_start_margin), true));
        this.mDataView.setOnTouchListener(new View.OnTouchListener() { // from class: com.miui.gallery.search.suggestionpage.SuggestionFragment.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (SuggestionFragment.this.getCallback() != null) {
                    SuggestionFragment.this.getCallback().requestIME(false);
                }
                return false;
            }
        });
        this.mErrorViewAdapter = new ErrorViewAdapter(this.mActivity);
        this.mStatusHandleHelper.init(this.mDataView, inflate.findViewById(R.id.info_view), inflate.findViewById(R.id.loading_view), inflate.findViewById(R.id.empty_view), this.mErrorViewAdapter);
        return inflate;
    }

    /* loaded from: classes2.dex */
    public static class SuggestionAdapter extends SectionedSuggestionAdapter {
        public SuggestionAdapter(Activity activity, SuggestionViewFactory suggestionViewFactory, String str) {
            super(activity, suggestionViewFactory, str);
        }

        @Override // com.miui.gallery.search.core.display.SectionedSuggestionAdapter
        public int getHeaderCount(int i) {
            return SearchConfig.get().getSuggestionConfig().shouldDrawSectionHeader(getSection(i).getSectionType()) ? 1 : 0;
        }
    }

    /* loaded from: classes2.dex */
    public class ErrorViewAdapter extends StatusHandleHelper.AbstractErrorViewAdapter {
        public ErrorViewAdapter(Context context) {
            super(context);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void requestRetry() {
            SuggestionFragment.this.doRetryIfNeeded();
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public boolean isEmptyDataView() {
            return SuggestionFragment.this.mAdapter.isEmpty();
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public boolean isLoading() {
            return SuggestionFragment.this.mIsLoading && isEmptyDataView();
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void addHeaderView(View view) {
            SuggestionFragment.this.mAdapter.addHeaderView(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void removeHeaderView(View view) {
            SuggestionFragment.this.mAdapter.removeHeaderView(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void addFooterView(View view) {
            SuggestionFragment.this.mAdapter.addFooterView(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void removeFooterView(View view) {
            SuggestionFragment.this.mAdapter.removeFooterView(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.AbstractErrorViewAdapter, com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public View getInfoView(View view, int i, StatusHandleHelper.InfoViewPosition infoViewPosition) {
            if (!SearchConstants.isErrorStatus(i) || infoViewPosition != StatusHandleHelper.InfoViewPosition.FOOTER) {
                return super.getInfoView(view, i, infoViewPosition);
            }
            return null;
        }
    }
}
