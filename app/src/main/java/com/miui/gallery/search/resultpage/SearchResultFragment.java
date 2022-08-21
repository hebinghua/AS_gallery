package com.miui.gallery.search.resultpage;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.search.StatusHandleHelper;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.context.SearchContext;
import com.miui.gallery.search.core.display.BaseSuggestionAdapter;
import com.miui.gallery.search.core.display.DefaultActionClickListener;
import com.miui.gallery.search.core.display.FullSpanDelegate;
import com.miui.gallery.search.core.display.OnActionClickListener;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class SearchResultFragment extends SearchResultFragmentBase {
    public boolean isInMultiWindow;
    public boolean isRtl;
    public int mColumn;
    public String mDefaultTitle;
    public ErrorViewAdapter mErrorViewAdapter = null;
    public String mFrom;
    public GridLayoutManager mGridLayoutManager;
    public int mHorizontalPadding;
    public RecyclerViewDecoration mItemDecoration;
    public String mPageName;
    public BaseSuggestionAdapter<SuggestionCursor> mResultAdapter;
    public GalleryRecyclerView mResultView;
    public int mVerticalPadding;

    public static /* synthetic */ void $r8$lambda$VJ7ntQ0zMi3ZGIeufmBvE8eG0l4(SearchResultFragment searchResultFragment) {
        searchResultFragment.lambda$onLoadComplete$0();
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void changeFilterData(QueryInfo queryInfo, SuggestionCursor suggestionCursor) {
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public int getLayoutResource() {
        return R.layout.search_result_fragment;
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void restartFilterLoader(Bundle bundle) {
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public boolean usePersistentResponse() {
        return true;
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void onInflateView(View view, Bundle bundle, Uri uri) {
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) view.findViewById(R.id.result_view);
        this.mResultView = galleryRecyclerView;
        galleryRecyclerView.setItemAnimator(null);
        this.isRtl = ScreenUtils.isRtl(getContext());
        if ("locationList".equals(uri.getQueryParameter(nexExportFormat.TAG_FORMAT_TYPE))) {
            this.mFrom = "from_location_list";
            this.mDefaultTitle = getString(R.string.search_title_location);
            this.mPageName = "search_location_list";
        } else if ("tagList".equals(uri.getQueryParameter(nexExportFormat.TAG_FORMAT_TYPE))) {
            this.mFrom = "from_tag_list";
            this.mDefaultTitle = getString(R.string.search_title_tag);
            this.mPageName = "search_tag_list";
        } else {
            this.mFrom = "from_image_result";
            this.mDefaultTitle = super.getDefaultTitle();
            this.mPageName = "search_result";
        }
        configAdapter();
        if (this.mInFeedbackTaskMode) {
            this.mResultAdapter.setActionClickListener(genFeedbackTaskModeActionClickListener());
        }
        this.isInMultiWindow = ActivityCompat.isInMultiWindowMode(this.mActivity) && !BaseBuildUtil.isLargeHorizontalWindow();
        if (getResources().getConfiguration().orientation == 1 || this.isInMultiWindow) {
            this.mColumn = getResources().getInteger(R.integer.search_result_page_column_count);
        } else {
            this.mColumn = getResources().getInteger(R.integer.search_result_page_column_count_land);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.mActivity, this.mColumn);
        this.mGridLayoutManager = gridLayoutManager;
        this.mGridLayoutManager.setSpanSizeLookup(new GridSpanLookUp(gridLayoutManager, this.mResultAdapter));
        this.mResultView.setLayoutManager(this.mGridLayoutManager);
        this.mHorizontalPadding = getResources().getDimensionPixelSize(R.dimen.result_page_horizontal_padding);
        this.mVerticalPadding = getResources().getDimensionPixelSize(R.dimen.result_page_top_padding);
        this.mResultView.setClipToPadding(false);
        GalleryRecyclerView galleryRecyclerView2 = this.mResultView;
        int i = this.mHorizontalPadding;
        galleryRecyclerView2.setPadding(i, this.mVerticalPadding, i, 0);
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(getResources().getDimensionPixelOffset(R.dimen.result_item_grid_horizontal_space));
        this.mItemDecoration = recyclerViewDecoration;
        this.mResultView.addItemDecoration(recyclerViewDecoration);
    }

    public void configAdapter() {
        BaseSuggestionAdapter<SuggestionCursor> baseSuggestionAdapter = new BaseSuggestionAdapter<>(this.mActivity, SearchContext.getInstance().getSuggestionViewFactory(), this.mFrom, getOnActionClickListener(getContext()));
        this.mResultAdapter = baseSuggestionAdapter;
        baseSuggestionAdapter.setOnLoadMoreListener(this);
        this.mResultView.setAdapter(this.mResultAdapter);
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (!this.isRtl && configuration.getLayoutDirection() == 1) {
            this.isRtl = true;
            this.mResultView.invalidateItemDecorations();
        } else if (this.isRtl && configuration.getLayoutDirection() != 1) {
            this.isRtl = false;
            this.mResultView.invalidateItemDecorations();
        }
        updateConfiguration();
    }

    public final void updateConfiguration() {
        this.isInMultiWindow = ActivityCompat.isInMultiWindowMode(this.mActivity) && !BaseBuildUtil.isLargeHorizontalWindow();
        if (getResources().getConfiguration().orientation == 1 || this.isInMultiWindow) {
            this.mColumn = getResources().getInteger(R.integer.search_result_page_column_count);
        } else {
            this.mColumn = getResources().getInteger(R.integer.search_result_page_column_count_land);
        }
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.result_page_horizontal_padding);
        this.mHorizontalPadding = dimensionPixelSize;
        this.mResultView.setPadding(dimensionPixelSize, this.mVerticalPadding, dimensionPixelSize, 0);
        this.mGridLayoutManager.setSpanCount(this.mColumn);
        this.mGridLayoutManager.setSpanSizeLookup(new GridSpanLookUp(this.mGridLayoutManager, this.mResultAdapter));
        this.mItemDecoration.setSpaceWidth(getResources().getDimensionPixelOffset(R.dimen.result_item_grid_horizontal_space));
        this.mResultView.invalidateItemDecorations();
    }

    public final OnActionClickListener genFeedbackTaskModeActionClickListener() {
        return new DefaultActionClickListener(this.mActivity) { // from class: com.miui.gallery.search.resultpage.SearchResultFragment.1
            {
                SearchResultFragment.this = this;
            }

            @Override // com.miui.gallery.search.core.display.DefaultActionClickListener
            public void handleUri(FragmentActivity fragmentActivity, Uri uri, Bundle bundle) {
                super.handleUri(fragmentActivity, uri.buildUpon().appendQueryParameter("inFeedbackTaskMode", String.valueOf(SearchResultFragment.this.mInFeedbackTaskMode)).build(), bundle);
            }
        };
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public StatusHandleHelper.AbstractErrorViewAdapter getErrorViewAdapter() {
        if (this.mErrorViewAdapter == null) {
            this.mErrorViewAdapter = new ErrorViewAdapter(this.mActivity);
        }
        return this.mErrorViewAdapter;
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public View getResultView() {
        return this.mResultView;
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void openLoadMore() {
        this.mResultAdapter.openLoadMore(false);
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void closeLoadMore() {
        this.mResultAdapter.closeLoadMore();
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void onLoadComplete() {
        this.mResultAdapter.closeLoadMore();
        this.mStatusHandleHelper.refreshInfoViews();
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.search.resultpage.SearchResultFragment$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SearchResultFragment.$r8$lambda$VJ7ntQ0zMi3ZGIeufmBvE8eG0l4(SearchResultFragment.this);
            }
        });
    }

    public /* synthetic */ void lambda$onLoadComplete$0() {
        if (moreThanOnePage()) {
            this.mResultAdapter.showLoadCompleteView();
            this.mStatusHandleHelper.refreshInfoViews();
        }
        trackLoadComplete();
    }

    public void trackLoadComplete() {
        BaseSuggestionAdapter<SuggestionCursor> baseSuggestionAdapter = this.mResultAdapter;
        TimeMonitor.trackTimeMonitor("403.50.0.1.14020", baseSuggestionAdapter == null ? 0L : baseSuggestionAdapter.getItemCount());
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void restartSectionLoader(Bundle bundle) {
        if (getActivity() == null || getLoaderManager() == null) {
            return;
        }
        getLoaderManager().restartLoader(1, bundle, this.mSectionsLoaderCallback);
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void restartSectionDataLoader(Bundle bundle) {
        if (getActivity() == null || getLoaderManager() == null) {
            return;
        }
        getLoaderManager().restartLoader(2, bundle, this.mSectionDataLoaderCallback);
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void changeSuggestions(QueryInfo queryInfo, SuggestionCursor suggestionCursor) {
        BaseSuggestionAdapter<SuggestionCursor> baseSuggestionAdapter = this.mResultAdapter;
        if (baseSuggestionAdapter != null) {
            baseSuggestionAdapter.changeSuggestions(queryInfo, suggestionCursor);
        }
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment
    public List<Loader<?>> getLoaders() {
        ArrayList arrayList = new ArrayList();
        Loader loader = getLoaderManager().getLoader(1);
        if (loader != null) {
            arrayList.add(loader);
        }
        Loader loader2 = getLoaderManager().getLoader(2);
        if (loader2 != null) {
            arrayList.add(loader2);
        }
        return arrayList;
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return this.mPageName;
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public String getFrom() {
        return this.mFrom;
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public String getDefaultTitle() {
        return this.mDefaultTitle;
    }

    public OnActionClickListener getOnActionClickListener(Context context) {
        return new DefaultActionClickListener(context);
    }

    public boolean moreThanOnePage() {
        return this.mResultView.getLayoutManager().findViewByPosition(0) == null || this.mResultView.getLayoutManager().findViewByPosition(this.mResultAdapter.getItemCount() - 1) == null;
    }

    /* loaded from: classes2.dex */
    public class ErrorViewAdapter extends StatusHandleHelper.AbstractErrorViewAdapter {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ErrorViewAdapter(Context context) {
            super(context);
            SearchResultFragment.this = r1;
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void requestRetry() {
            SearchResultFragment.this.doRetry();
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public boolean isEmptyDataView() {
            return SearchResultFragment.this.mResultAdapter.isEmpty();
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public boolean isLoading() {
            return SearchResultFragment.this.mResultAdapter.isLoading();
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void addHeaderView(View view) {
            SearchResultFragment.this.mResultAdapter.addHeaderView(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void removeHeaderView(View view) {
            SearchResultFragment.this.mResultAdapter.removeHeaderView(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void addFooterView(View view) {
            SearchResultFragment.this.mResultAdapter.addFooterView(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void removeFooterView(View view) {
            SearchResultFragment.this.mResultAdapter.removeFooterView(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.AbstractErrorViewAdapter
        public View getInfoItemView(View view, int i, StatusHandleHelper.InfoViewPosition infoViewPosition) {
            if (infoViewPosition == StatusHandleHelper.InfoViewPosition.FOOTER) {
                return this.mInflater.inflate(R.layout.search_item_error_footer_layout, (ViewGroup) null);
            }
            return super.getInfoItemView(view, i, infoViewPosition);
        }
    }

    /* loaded from: classes2.dex */
    public static class GridSpanLookUp extends GridLayoutManager.SpanSizeLookup {
        public final FullSpanDelegate mFullSpanDelegate;
        public final GridLayoutManager mLayoutManager;

        public GridSpanLookUp(GridLayoutManager gridLayoutManager, FullSpanDelegate fullSpanDelegate) {
            this.mLayoutManager = gridLayoutManager;
            this.mFullSpanDelegate = fullSpanDelegate;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
        public int getSpanSize(int i) {
            if (this.mFullSpanDelegate.isFullSpan(i)) {
                return this.mLayoutManager.getSpanCount();
            }
            return 1;
        }
    }

    /* loaded from: classes2.dex */
    public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {
        public int mSpaceWidth;

        public RecyclerViewDecoration(int i) {
            SearchResultFragment.this = r1;
            this.mSpaceWidth = i;
        }

        public void setSpaceWidth(int i) {
            this.mSpaceWidth = i;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            int spanCount = gridLayoutManager.getSpanCount();
            int spanIndex = gridLayoutManager.getSpanSizeLookup().getSpanIndex(recyclerView.getChildAdapterPosition(view), spanCount);
            int i = this.mSpaceWidth;
            int i2 = ((spanCount - 1) * i) / spanCount;
            int i3 = (spanIndex % spanCount) * (i - i2);
            int i4 = i2 - i3;
            if (SearchResultFragment.this.isRtl) {
                rect.set(i4, 0, i3, 0);
            } else {
                rect.set(i3, 0, i4, 0);
            }
        }
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        TimeMonitor.cancelTimeMonitor("403.50.0.1.14020");
        super.onDestroy();
    }
}
