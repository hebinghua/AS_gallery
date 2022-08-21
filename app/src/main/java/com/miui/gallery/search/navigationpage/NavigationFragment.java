package com.miui.gallery.search.navigationpage;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.search.SearchConfig;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.SearchFragmentBase;
import com.miui.gallery.search.SearchStatusLoader;
import com.miui.gallery.search.StatusHandleHelper;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.context.SearchContext;
import com.miui.gallery.search.core.display.SectionedSuggestionItemDecoration;
import com.miui.gallery.search.core.query.QueryLoader;
import com.miui.gallery.search.core.result.SuggestionResult;
import com.miui.gallery.search.core.resultprocessor.SectionedResultProcessor;
import com.miui.gallery.search.core.suggestion.BaseSuggestionSection;
import com.miui.gallery.search.core.suggestion.GroupedSuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.search.utils.SearchUtils;
import java.util.HashSet;

/* loaded from: classes2.dex */
public class NavigationFragment extends SearchFragmentBase {
    public NavigationAdapter mAdapter;
    public RecyclerView mDataView;
    public boolean mIsLoading;
    public SectionedSuggestionItemDecoration mItemDecoration;
    public final StatusHandleHelper mStatusHandleHelper = new StatusHandleHelper();
    public final SearchStatusLoader.StatusReportDelegate mStatusReportDelegate = new StatusReportDelegate();
    public SectionedResultProcessor mSectionProcessor = new SectionedResultProcessor() { // from class: com.miui.gallery.search.navigationpage.NavigationFragment.2
        @Override // com.miui.gallery.search.core.resultprocessor.SectionedResultProcessor
        public BaseSuggestionSection getIndependentSection(HashSet<String> hashSet, SuggestionSection suggestionSection) {
            BaseSuggestionSection independentSection = super.getIndependentSection(hashSet, suggestionSection);
            int i = AnonymousClass5.$SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[independentSection.getSectionType().ordinal()];
            if ((i == 1 || i == 2) && independentSection.moveToMore() == null) {
                independentSection.setSectionMoreItem(SearchConfig.get().getDefaultMoreItem(GalleryApp.sGetAndroidContext(), independentSection.getSectionType(), false));
            }
            return independentSection;
        }
    };
    public LoaderManager.LoaderCallbacks<SuggestionResult<GroupedSuggestionCursor<SuggestionSection>>> mDataLoaderCallback = new LoaderManager.LoaderCallbacks<SuggestionResult<GroupedSuggestionCursor<SuggestionSection>>>() { // from class: com.miui.gallery.search.navigationpage.NavigationFragment.3
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<SuggestionResult<GroupedSuggestionCursor<SuggestionSection>>> onCreateLoader(int i, Bundle bundle) {
            return new QueryLoader(NavigationFragment.this.mActivity, new QueryInfo.Builder(SearchConstants.SearchType.SEARCH_TYPE_NAVIGATION).setAppendSerialInfo(true).addParam("use_persistent_response", String.valueOf(true)).build(), NavigationFragment.this.mSectionProcessor, true, false, true);
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<SuggestionResult<GroupedSuggestionCursor<SuggestionSection>>> loader, SuggestionResult<GroupedSuggestionCursor<SuggestionSection>> suggestionResult) {
            int i;
            if (suggestionResult != null) {
                if (suggestionResult.isEmpty()) {
                    SearchLog.d("NavigationFragment", "Loader %s load finished, got empty result", Integer.valueOf(loader.getId()));
                } else {
                    SearchLog.d("NavigationFragment", "Loader %s load finished, got %s results", Integer.valueOf(loader.getId()), Integer.valueOf(suggestionResult.getData().getCount()));
                }
                NavigationFragment.this.mAdapter.changeSuggestions(((QueryLoader) loader).getQueryInfo(), suggestionResult.getData(), true);
                NavigationFragment.this.mAdapter.notifyDataSetChanged();
                i = SearchUtils.getErrorStatus(suggestionResult);
            } else {
                SearchLog.w("NavigationFragment", "Loader %s load finished, got no data available", Integer.valueOf(loader.getId()));
                i = 0;
            }
            NavigationFragment.this.mIsLoading = !isDone(suggestionResult);
            NavigationFragment.this.mStatusHandleHelper.updateResultStatus(i);
            TimeMonitor.trackTimeMonitor("403.20.0.1.14019");
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<SuggestionResult<GroupedSuggestionCursor<SuggestionSection>>> loader) {
            if (NavigationFragment.this.mAdapter != null) {
                NavigationFragment.this.mAdapter.changeSuggestions(null, null, false);
                NavigationFragment.this.mStatusHandleHelper.updateResultStatus(-1);
            }
            NavigationFragment.this.mIsLoading = false;
        }

        public final boolean isDone(SuggestionResult suggestionResult) {
            return (suggestionResult == null || suggestionResult.getResultExtras() == null || !suggestionResult.getResultExtras().getBoolean("is_done", true)) ? false : true;
        }
    };
    public LoaderManager.LoaderCallbacks<Integer> mStatusLoaderCallback = new LoaderManager.LoaderCallbacks<Integer>() { // from class: com.miui.gallery.search.navigationpage.NavigationFragment.4
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<Integer> onCreateLoader(int i, Bundle bundle) {
            return new SearchStatusLoader(NavigationFragment.this.mActivity, NavigationFragment.this.mStatusReportDelegate);
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<Integer> loader, Integer num) {
            NavigationFragment.this.mStatusHandleHelper.updateBaseStatus(num.intValue());
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<Integer> loader) {
            NavigationFragment.this.mStatusHandleHelper.updateBaseStatus(-1);
        }
    };

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "search_navigation";
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.navigation_fragment, viewGroup, false);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.data_view);
        this.mDataView = recyclerView;
        recyclerView.setItemAnimator(null);
        this.mDataView.setLayoutManager(new LinearLayoutManager(this.mActivity, 1, false));
        NavigationAdapter navigationAdapter = new NavigationAdapter(this.mActivity, SearchContext.getInstance().getSuggestionViewFactory(), "from_navigation");
        this.mAdapter = navigationAdapter;
        this.mDataView.setAdapter(navigationAdapter);
        SectionedSuggestionItemDecoration sectionedSuggestionItemDecoration = new SectionedSuggestionItemDecoration(this.mActivity, this.mAdapter, false, 0, true);
        this.mItemDecoration = sectionedSuggestionItemDecoration;
        this.mDataView.addItemDecoration(sectionedSuggestionItemDecoration);
        this.mDataView.setOnTouchListener(new View.OnTouchListener() { // from class: com.miui.gallery.search.navigationpage.NavigationFragment.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (NavigationFragment.this.getCallback() != null) {
                    NavigationFragment.this.getCallback().requestIME(false);
                }
                return false;
            }
        });
        this.mDataView.setPadding(getResources().getDimensionPixelSize(R.dimen.navigation_page_margin_left_right), 0, getResources().getDimensionPixelSize(R.dimen.navigation_page_margin_left_right), 0);
        SearchConfig.get().getNavigationConfig().checkConfig(this.mActivity);
        this.mStatusHandleHelper.init(this.mDataView, inflate.findViewById(R.id.info_view), inflate.findViewById(R.id.loading_view), inflate.findViewById(R.id.empty_view), new ErrorViewAdapter(this.mActivity));
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getLoaderManager().initLoader(0, null, this.mStatusLoaderCallback);
        restartDataLoader();
    }

    public void doRetry() {
        if (SearchConstants.isErrorStatus(this.mStatusHandleHelper.getResultStatus())) {
            restartDataLoader();
        }
    }

    public final void restartDataLoader() {
        getLoaderManager().restartLoader(1, null, this.mDataLoaderCallback);
        this.mIsLoading = true;
        this.mStatusHandleHelper.refreshInfoViews();
    }

    /* renamed from: com.miui.gallery.search.navigationpage.NavigationFragment$5  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass5 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType;

        static {
            int[] iArr = new int[SearchConstants.SectionType.values().length];
            $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType = iArr;
            try {
                iArr[SearchConstants.SectionType.SECTION_TYPE_LOCATION.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_TAG.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    @Override // com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        getLoaderManager().getLoader(0).forceLoad();
        doRetry();
    }

    /* loaded from: classes2.dex */
    public class ErrorViewAdapter extends StatusHandleHelper.AbstractErrorViewAdapter {
        public ErrorViewAdapter(Context context) {
            super(context);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void requestRetry() {
            NavigationFragment.this.doRetry();
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public boolean isEmptyDataView() {
            return NavigationFragment.this.mAdapter.isEmpty();
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public boolean isLoading() {
            return NavigationFragment.this.mIsLoading && isEmptyDataView();
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void addHeaderView(View view) {
            NavigationFragment.this.mAdapter.addHeaderView(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void removeHeaderView(View view) {
            NavigationFragment.this.mAdapter.removeHeaderView(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void addFooterView(View view) {
            NavigationFragment.this.mAdapter.addFooterView(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void removeFooterView(View view) {
            NavigationFragment.this.mAdapter.removeFooterView(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.AbstractErrorViewAdapter, com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public View getInfoView(View view, int i, StatusHandleHelper.InfoViewPosition infoViewPosition) {
            if (!SearchConstants.isErrorStatus(i) || infoViewPosition != StatusHandleHelper.InfoViewPosition.FOOTER) {
                return super.getInfoView(view, i, infoViewPosition);
            }
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static class StatusReportDelegate implements SearchStatusLoader.StatusReportDelegate {
        @Override // com.miui.gallery.search.SearchStatusLoader.StatusReportDelegate
        public boolean shouldReportStatus(int i) {
            return i == 4;
        }

        public StatusReportDelegate() {
        }
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mItemDecoration.refreshConfig(this.mActivity);
        this.mDataView.invalidateItemDecorations();
        this.mDataView.setPadding(getResources().getDimensionPixelSize(R.dimen.navigation_page_margin_left_right), 0, getResources().getDimensionPixelSize(R.dimen.navigation_page_margin_left_right), 0);
        if (SearchConfig.get().getNavigationConfig().checkConfig(this.mActivity)) {
            restartDataLoader();
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        TimeMonitor.cancelTimeMonitor("403.20.0.1.14019");
        super.onDestroy();
    }
}
