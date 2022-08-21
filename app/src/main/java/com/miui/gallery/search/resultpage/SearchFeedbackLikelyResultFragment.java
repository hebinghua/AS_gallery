package com.miui.gallery.search.resultpage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;
import com.h6ah4i.android.widget.advrecyclerview.composedadapter.ComposedAdapter;
import com.miui.gallery.R;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.suggestion.BaseSuggestionSection;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import com.miui.gallery.search.feedback.SearchFeedbackHelper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import miui.gallery.support.MiuiSdkCompat;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class SearchFeedbackLikelyResultFragment extends SearchImageResultFragmentBase implements View.OnClickListener {
    public LongSparseArray<Integer> mCheckedIds = new LongSparseArray<>();
    public View mContentView;
    public TextView mReportButton;
    public Button mSelectAllBtn;

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void changeFilterData(QueryInfo queryInfo, SuggestionCursor suggestionCursor) {
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public String getFrom() {
        return "from_likely_image_result";
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public int getLayoutResource() {
        return R.layout.search_likely_result_fragment;
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "search_likely_image_result";
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void restartFilterLoader(Bundle bundle) {
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void updateTitle(String str) {
    }

    public final void initTopBar() {
        View inflate = LayoutInflater.from(this.mActivity).inflate(R.layout.search_feedback_likely_result_page_title, (ViewGroup) null);
        ((TextView) inflate.findViewById(R.id.home_text)).setText(String.format(getString(R.string.search_feedback_false_negative_title), this.mQueryText));
        inflate.findViewById(R.id.home_arrow).setOnClickListener(this);
        Button button = (Button) inflate.findViewById(R.id.select_mode_button);
        this.mSelectAllBtn = button;
        MiuiSdkCompat.setEditActionModeButton(this.mActivity, button, 0);
        this.mSelectAllBtn.setOnClickListener(this);
        this.mSelectAllBtn.setEnabled(false);
        this.mActivity.getAppCompatActionBar().setDisplayOptions(16);
        this.mActivity.getAppCompatActionBar().setCustomView(inflate, new ActionBar.LayoutParams(-1, -1, 19));
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void onInflateView(View view, Bundle bundle, Uri uri) {
        bind((GalleryRecyclerView) view.findViewById(R.id.grid), new LikelyResultAdapter(this.mActivity));
        this.mRecyclerView.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.search.resultpage.SearchFeedbackLikelyResultFragment.1
            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public boolean onItemClick(RecyclerView recyclerView, View view2, int i, long j, float f, float f2) {
                long segmentedPosition = SearchFeedbackLikelyResultFragment.this.mHeaderFooterWrapper.getSegmentedPosition(i);
                if (ComposedAdapter.extractSegmentPart(segmentedPosition) != 1) {
                    return false;
                }
                SearchFeedbackLikelyResultFragment.this.gotoPhotoPage(recyclerView, ComposedAdapter.extractSegmentOffsetPart(segmentedPosition), "from_likely_image_result");
                HashMap hashMap = new HashMap();
                hashMap.put("from", SearchFeedbackLikelyResultFragment.this.getPageName());
                hashMap.put("position", Integer.valueOf(i));
                SamplingStatHelper.recordCountEvent("photo", "click_micro_thumb", hashMap);
                return true;
            }
        });
        this.mContentView = view.findViewById(R.id.content_container);
        TextView textView = (TextView) view.findViewById(R.id.report_button);
        this.mReportButton = textView;
        textView.setOnClickListener(this);
        initTopBar();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == null) {
            return;
        }
        int id = view.getId();
        if (id == R.id.home_arrow) {
            finish();
        } else if (id == R.id.report_button) {
            report();
        } else if (id != R.id.select_mode_button) {
        } else {
            toggleSelectAll();
        }
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public View getResultView() {
        return this.mContentView;
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public QueryInfo.Builder initResultQueryInfoBuilder(Uri uri) {
        QueryInfo.Builder initResultQueryInfoBuilder = super.initResultQueryInfoBuilder(uri);
        initResultQueryInfoBuilder.setSearchType(SearchConstants.SearchType.SEARCH_TYPE_FEEDBACK_LIKELY_RESULT);
        return initResultQueryInfoBuilder;
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void restartSectionLoader(Bundle bundle) {
        AppCompatActivity appCompatActivity = this.mActivity;
        QueryInfo queryInfo = this.mResultSectionQueryInfo;
        this.mSectionDataResultHelper = new SearchImageResultHelper((Context) appCompatActivity, queryInfo, false, (SuggestionSection) new BaseSuggestionSection(queryInfo, SearchConstants.SectionType.SECTION_TYPE_IMAGE_LIST, null, appCompatActivity.getIntent().getData().toString(), null, null, null));
        openLoadMore();
        onLoadMoreRequested();
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void restartSectionDataLoader(Bundle bundle) {
        if (getActivity() == null || getLoaderManager() == null) {
            return;
        }
        getLoaderManager().restartLoader(1, bundle, this.mSectionDataLoaderCallback);
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public QueryInfo.Builder getSectionDataQueryInfoBuilder() {
        QueryInfo.Builder sectionDataQueryInfoBuilder = super.getSectionDataQueryInfoBuilder();
        sectionDataQueryInfoBuilder.setSearchType(SearchConstants.SearchType.SEARCH_TYPE_FEEDBACK_LIKELY_RESULT);
        return sectionDataQueryInfoBuilder;
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment
    public List<Loader<?>> getLoaders() {
        Loader loader = LoaderManager.getInstance(this).getLoader(1);
        if (loader != null) {
            return Collections.singletonList(loader);
        }
        return Collections.emptyList();
    }

    @Override // com.miui.gallery.search.resultpage.SearchImageResultFragmentBase, com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void changeSuggestions(QueryInfo queryInfo, SuggestionCursor suggestionCursor) {
        super.changeSuggestions(queryInfo, suggestionCursor);
        updateReportButtonState();
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase, com.miui.gallery.search.core.display.RequestLoadMoreListener
    public void onLoadMoreRequested() {
        if (this.mAdapter.getItemCount() != 0) {
            closeLoadMore();
        } else {
            super.onLoadMoreRequested();
        }
    }

    public final void toggleSelectAll() {
        if (isCheckedAll()) {
            this.mCheckedIds.clear();
        } else {
            for (int i = 0; i < this.mAdapter.getItemCount(); i++) {
                this.mCheckedIds.put(this.mAdapter.getItemId(i), Integer.valueOf(i));
            }
        }
        this.mAdapter.notifyDataSetChanged();
        updateReportButtonState();
    }

    public final void report() {
        ArrayList<String> checkedServerIdList = getCheckedServerIdList(getCheckedPositions());
        if (checkedServerIdList.size() <= 0) {
            return;
        }
        SearchFeedbackHelper.reportFalseNegativeImages(this.mActivity, this.mQueryText, this.mInFeedbackTaskMode, checkedServerIdList, new SearchFeedbackHelper.OnFeedbackCompleteListener() { // from class: com.miui.gallery.search.resultpage.SearchFeedbackLikelyResultFragment.2
            @Override // com.miui.gallery.search.feedback.SearchFeedbackHelper.OnFeedbackCompleteListener
            public void onComplete(int i) {
                if (i > 0) {
                    SearchFeedbackLikelyResultFragment.this.mActivity.setResult(-1);
                    SearchFeedbackLikelyResultFragment.this.finish();
                }
            }
        });
    }

    public final boolean isCheckedAll() {
        int checkedItemCount = getCheckedItemCount();
        return checkedItemCount > 0 && checkedItemCount == this.mAdapter.getItemCount();
    }

    public final boolean isChecked(int i) {
        return this.mCheckedIds.indexOfKey(this.mAdapter.getItemId(i)) >= 0;
    }

    public final int getCheckedItemCount() {
        return this.mCheckedIds.size();
    }

    public final List<Integer> getCheckedPositions() {
        ArrayList arrayList = new ArrayList(this.mAdapter.getItemCount());
        for (int i = 0; i < this.mAdapter.getItemCount(); i++) {
            if (isChecked(i)) {
                arrayList.add(Integer.valueOf(i));
            }
        }
        return arrayList;
    }

    public final void updateReportButtonState() {
        boolean z = true;
        this.mReportButton.setEnabled(getCheckedItemCount() > 0);
        Button button = this.mSelectAllBtn;
        if (this.mAdapter.getItemCount() <= 0) {
            z = false;
        }
        button.setEnabled(z);
        MiuiSdkCompat.setEditActionModeButton(this.mActivity, this.mSelectAllBtn, isCheckedAll() ? 1 : 0);
    }

    /* loaded from: classes2.dex */
    public class LikelyResultAdapter extends ImageResultAdapter {
        public LikelyResultAdapter(Context context) {
            super(context);
        }

        @Override // com.miui.gallery.search.resultpage.ImageResultAdapter, com.miui.gallery.adapter.BaseGroupedMediaAdapter
        public void doBindChildViewHolder(BaseViewHolder baseViewHolder, final int i, int i2, List<Object> list) {
            super.doBindChildViewHolder(baseViewHolder, i, i2, list);
            final MicroThumbGridItem microThumbGridItem = (MicroThumbGridItem) baseViewHolder.itemView;
            microThumbGridItem.setCheckable(true);
            microThumbGridItem.setChecked(SearchFeedbackLikelyResultFragment.this.isChecked(i));
            microThumbGridItem.setCheckableListener(new View.OnClickListener() { // from class: com.miui.gallery.search.resultpage.SearchFeedbackLikelyResultFragment.LikelyResultAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    boolean isChecked = SearchFeedbackLikelyResultFragment.this.isChecked(i);
                    if (isChecked) {
                        SearchFeedbackLikelyResultFragment.this.mCheckedIds.delete(SearchFeedbackLikelyResultFragment.this.mAdapter.getItemId(i));
                    } else {
                        SearchFeedbackLikelyResultFragment.this.mCheckedIds.put(SearchFeedbackLikelyResultFragment.this.mAdapter.getItemId(i), Integer.valueOf(i));
                    }
                    microThumbGridItem.setChecked(!isChecked);
                    SearchFeedbackLikelyResultFragment.this.updateReportButtonState();
                }
            });
        }
    }
}
