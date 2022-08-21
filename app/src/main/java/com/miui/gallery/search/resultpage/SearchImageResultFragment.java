package com.miui.gallery.search.resultpage;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.request.PicToPdfHelper;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.query.QueryLoader;
import com.miui.gallery.search.core.suggestion.GroupedSuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import com.miui.gallery.search.feedback.SearchFeedbackHelper;
import com.miui.gallery.search.resultpage.ImageResultAdapter;
import com.miui.gallery.search.resultpage.SearchImageResultFragment;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.search.utils.SearchUtils;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.DeletionTask;
import com.miui.gallery.ui.KeyboardShortcutGroupManager;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.util.ActionURIHandler;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.SoundUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.editwrapper.HeaderFooterWrapper;
import com.miui.gallery.widget.editwrapper.MultiChoiceModeListener;
import com.miui.gallery.widget.recyclerview.EditableListSpanSizeProvider;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import com.miui.gallery.widget.recyclerview.SimpleHeaderFooterWrapperAdapter;
import com.miui.gallery.widget.tsd.INestedTwoStageDrawer;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class SearchImageResultFragment extends SearchImageResultFragmentBase {
    public EditableListViewWrapper mEditableWrapper;
    public GridLayoutManager mGridLayoutManager;
    public ChoiceModeListener mChoiceModeListener = new ChoiceModeListener();
    public int mFilterVisibleImageThreshold = 0;
    public int mFilterVisibleFilterThreshold = 0;
    public FilterBarController mFilterBarController = null;
    public SearchKeyboardShortcutCallback mShortcutCallback = new SearchKeyboardShortcutCallback();
    public SearchFeedbackLikelyController mFeedbackLikelyController = null;

    public static /* synthetic */ void $r8$lambda$152h6El0ncuXz6mp3nBmePDQw5k(SearchImageResultFragment searchImageResultFragment, int i) {
        searchImageResultFragment.lambda$onInflateView$0(i);
    }

    public static /* synthetic */ AbstractHeaderFooterWrapperAdapter $r8$lambda$t7MUxXPWmvX6SmcRgZDsqMzlPBo(SearchImageResultFragment searchImageResultFragment, RecyclerView.Adapter adapter) {
        return searchImageResultFragment.lambda$onInflateView$1(adapter);
    }

    /* renamed from: $r8$lambda$xzwRHP6lKKQ0Fd-jE2tjfbAHOqI */
    public static /* synthetic */ void m1339$r8$lambda$xzwRHP6lKKQ0FdjE2tjfbAHOqI(SearchImageResultFragment searchImageResultFragment, Configuration configuration) {
        searchImageResultFragment.lambda$onInflateView$3(configuration);
    }

    /* renamed from: $r8$lambda$z8zzqbbXqjGEQksBp5nsrhvi-a8 */
    public static /* synthetic */ int m1340$r8$lambda$z8zzqbbXqjGEQksBp5nsrhvia8(SearchImageResultFragment searchImageResultFragment, int i) {
        return searchImageResultFragment.lambda$onInflateView$2(i);
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public String getFrom() {
        return "from_image_result";
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public int getLayoutResource() {
        return R.layout.search_image_result_fragment;
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "search_image_result";
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public boolean receiveResultUpdates() {
        return true;
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void onInflateView(View view, Bundle bundle, Uri uri) {
        ImageResultAdapter imageResultAdapter = new ImageResultAdapter(this.mActivity);
        bind((GalleryRecyclerView) view.findViewById(R.id.grid), imageResultAdapter);
        imageResultAdapter.setHeaderClickListener(new ImageResultAdapter.OnHeaderItemClickedListener() { // from class: com.miui.gallery.search.resultpage.SearchImageResultFragment$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.search.resultpage.ImageResultAdapter.OnHeaderItemClickedListener
            public final void onHeaderItemClicked(int i) {
                SearchImageResultFragment.$r8$lambda$152h6El0ncuXz6mp3nBmePDQw5k(SearchImageResultFragment.this, i);
            }
        });
        this.mEditableWrapper = new EditableListViewWrapper(this.mRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.mActivity, this.mColumnCount);
        this.mGridLayoutManager = gridLayoutManager;
        gridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new EditableListSpanSizeProvider(this.mEditableWrapper, gridLayoutManager)));
        this.mEditableWrapper.setLayoutManager(this.mGridLayoutManager);
        this.mEditableWrapper.setAdapter(this.mAdapter, new HeaderFooterWrapper() { // from class: com.miui.gallery.search.resultpage.SearchImageResultFragment$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.widget.editwrapper.HeaderFooterWrapper
            public final AbstractHeaderFooterWrapperAdapter wrap(RecyclerView.Adapter adapter) {
                return SearchImageResultFragment.$r8$lambda$t7MUxXPWmvX6SmcRgZDsqMzlPBo(SearchImageResultFragment.this, adapter);
            }
        });
        this.mEditableWrapper.setHandleTouchAnimItemType(MicroThumbGridItem.class.getSimpleName(), SearchDocumentGridItem.class.getSimpleName());
        this.mEditableWrapper.enableChoiceMode(true);
        this.mEditableWrapper.enterChoiceModeWithLongClick(true);
        this.mEditableWrapper.setMultiChoiceModeListener(this.mChoiceModeListener);
        this.mEditableWrapper.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.search.resultpage.SearchImageResultFragment.1
            {
                SearchImageResultFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public boolean onItemClick(RecyclerView recyclerView, View view2, int i, long j, float f, float f2) {
                SearchImageResultFragment.this.gotoPhotoPage(recyclerView, i, "from_image_result");
                HashMap hashMap = new HashMap();
                hashMap.put("position", String.valueOf(i));
                hashMap.put("from", SearchImageResultFragment.this.getPageName());
                SamplingStatHelper.recordCountEvent("photo", "click_micro_thumb", hashMap);
                return true;
            }
        });
        this.mRecyclerView.setAdapterPos2ViewPosConverter(new GalleryRecyclerView.AdapterPos2ViewPosConverter() { // from class: com.miui.gallery.search.resultpage.SearchImageResultFragment$$ExternalSyntheticLambda3
            @Override // com.miui.gallery.widget.recyclerview.GalleryRecyclerView.AdapterPos2ViewPosConverter
            public final int convert(int i) {
                return SearchImageResultFragment.m1340$r8$lambda$z8zzqbbXqjGEQksBp5nsrhvia8(SearchImageResultFragment.this, i);
            }
        });
        this.mFilterVisibleImageThreshold = getResources().getInteger(R.integer.search_filter_visible_image_threshold);
        this.mFilterVisibleFilterThreshold = getResources().getInteger(R.integer.search_filter_visible_filter_threshold);
        if (this.mInFeedbackTaskMode && SearchFeedbackHelper.needToQueryLikelyImagesForFeedbackTask(this.mQueryText)) {
            this.mFeedbackLikelyController = new SearchFeedbackLikelyController((ViewStub) view.findViewById(R.id.likely_results));
        }
        addScreenChangeListener(new IScreenChange.OnScreenLayoutSizeChangeListener() { // from class: com.miui.gallery.search.resultpage.SearchImageResultFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnScreenLayoutSizeChangeListener
            public final void onScreenLayoutSizeChange(Configuration configuration) {
                SearchImageResultFragment.m1339$r8$lambda$xzwRHP6lKKQ0FdjE2tjfbAHOqI(SearchImageResultFragment.this, configuration);
            }
        });
    }

    public /* synthetic */ void lambda$onInflateView$0(int i) {
        IntentUtil.gotoDailyAlbumDetailPage(this.mActivity, this.mAdapter.getGroupCreateDate(i));
        SearchStatUtils.reportEvent(getFrom(), "client_image_operation_jump_to_daily", "queryText", this.mQueryText);
    }

    public /* synthetic */ AbstractHeaderFooterWrapperAdapter lambda$onInflateView$1(RecyclerView.Adapter adapter) {
        SimpleHeaderFooterWrapperAdapter simpleHeaderFooterWrapperAdapter = new SimpleHeaderFooterWrapperAdapter(adapter);
        this.mHeaderFooterWrapper = simpleHeaderFooterWrapperAdapter;
        return simpleHeaderFooterWrapperAdapter;
    }

    public /* synthetic */ int lambda$onInflateView$2(int i) {
        return this.mEditableWrapper.getRawPosition(i);
    }

    public /* synthetic */ void lambda$onInflateView$3(Configuration configuration) {
        this.mGridLayoutManager.setSpanCount(Config$ThumbConfig.get().sMicroThumbColumnsPortrait);
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void updateTitle(String str) {
        if (this.mInFeedbackTaskMode && !TextUtils.isEmpty(str)) {
            str = String.format(getString(R.string.search_feedback_false_positive_title), str);
        }
        super.updateTitle(str);
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public View getResultView() {
        return this.mRecyclerView;
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public SearchResultHelper createSearchResultHelper(QueryInfo queryInfo, GroupedSuggestionCursor<SuggestionSection> groupedSuggestionCursor) {
        return new SearchImageResultHelper((Context) getActivity(), queryInfo, false, groupedSuggestionCursor);
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
    public void restartFilterLoader(Bundle bundle) {
        if (this.mInFeedbackTaskMode || getActivity() == null || getLoaderManager() == null) {
            return;
        }
        getLoaderManager().restartLoader(3, bundle, this.mFilterLoaderCallback);
    }

    @Override // com.miui.gallery.search.resultpage.SearchImageResultFragmentBase, com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void changeSuggestions(QueryInfo queryInfo, SuggestionCursor suggestionCursor) {
        super.changeSuggestions(queryInfo, suggestionCursor);
        showFilterBar();
        if (SearchConstants.isHorizontalDocumentStyle(SearchUtils.getRankInfo(suggestionCursor))) {
            this.mEditableWrapper.disableScaleImageViewAniWhenInActionMode();
        } else {
            this.mEditableWrapper.enableScaleImageViewAniWhenInActionMode();
        }
        if (this.mFeedbackLikelyController == null || this.mChoiceModeListener.inChoiceMode()) {
            return;
        }
        this.mFeedbackLikelyController.showLikelyBar();
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void changeFilterData(QueryInfo queryInfo, SuggestionCursor suggestionCursor) {
        if (this.mFilterBarController == null) {
            this.mFilterBarController = new FilterBarController(getActivity(), (INestedTwoStageDrawer) getView(), "from_image_result");
        }
        this.mFilterBarController.swapCursor(queryInfo, suggestionCursor);
        showFilterBar();
    }

    public final void showFilterBar() {
        if (this.mFilterBarController != null) {
            ImageResultAdapter imageResultAdapter = this.mAdapter;
            boolean z = true;
            boolean z2 = imageResultAdapter != null && imageResultAdapter.getItemCount() >= this.mFilterVisibleImageThreshold;
            boolean z3 = this.mFilterBarController.getFilterDataCount() >= this.mFilterVisibleFilterThreshold;
            FilterBarController filterBarController = this.mFilterBarController;
            if (!z2 || !z3) {
                z = false;
            }
            filterBarController.showFilterBar(z);
        }
    }

    /* loaded from: classes2.dex */
    public class SearchFeedbackLikelyController implements LoaderManager.LoaderCallbacks<DataListSourceResult> {
        public ViewStub mLikelyGuideStub;
        public View mLikelyGuide = null;
        public Boolean mHasLikelyItems = null;

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<DataListSourceResult> loader) {
        }

        public SearchFeedbackLikelyController(ViewStub viewStub) {
            SearchImageResultFragment.this = r1;
            this.mLikelyGuideStub = null;
            this.mLikelyGuideStub = viewStub;
        }

        public void showLikelyBar() {
            ViewStub viewStub;
            Boolean bool = this.mHasLikelyItems;
            if (bool == null) {
                this.mHasLikelyItems = Boolean.FALSE;
                SearchImageResultFragment.this.getLoaderManager().restartLoader(4, null, this);
            } else if (!bool.booleanValue()) {
            } else {
                if (this.mLikelyGuide == null && (viewStub = this.mLikelyGuideStub) != null) {
                    View inflate = viewStub.inflate();
                    this.mLikelyGuide = inflate;
                    inflate.findViewById(R.id.click_view).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.search.resultpage.SearchImageResultFragment.SearchFeedbackLikelyController.1
                        {
                            SearchFeedbackLikelyController.this = this;
                        }

                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            Uri.Builder appendQueryParameter = GalleryContract.Search.URI_LIKELY_RESULT_PAGE.buildUpon().appendQueryParameter("title", SearchImageResultFragment.this.mQueryText).appendQueryParameter("tagName", SearchImageResultFragment.this.mQueryText);
                            Bundle bundle = new Bundle(3);
                            bundle.putBoolean("start_activity_for_result", true);
                            bundle.putInt("request_code", 1);
                            bundle.putString("from", "from_image_result");
                            ActionURIHandler.handleUri(SearchImageResultFragment.this.mActivity, appendQueryParameter.build(), bundle);
                        }
                    });
                }
                View view = this.mLikelyGuide;
                if (view == null) {
                    return;
                }
                view.setVisibility(0);
                ((TextView) this.mLikelyGuide.findViewById(R.id.title)).setText(String.format(SearchImageResultFragment.this.getString(R.string.search_feedback_likely_title), SearchImageResultFragment.this.mQueryText));
            }
        }

        public void hideLikelyBar() {
            View view = this.mLikelyGuide;
            if (view != null) {
                view.setVisibility(8);
            }
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<DataListSourceResult> onCreateLoader(int i, Bundle bundle) {
            return new QueryLoader(SearchImageResultFragment.this.mActivity, new QueryInfo.Builder(SearchConstants.SearchType.SEARCH_TYPE_FEEDBACK_LIKELY_RESULT).addParam("pos", "0").addParam("num", "1").addParam("tagName", SearchImageResultFragment.this.mQueryText).addParam("inFeedbackTaskMode", String.valueOf(SearchImageResultFragment.this.mInFeedbackTaskMode)).build(), new DataListResultProcessor(), false, SearchImageResultFragment.this.receiveResultUpdates(), false);
        }

        /* JADX WARN: Type inference failed for: r2v7, types: [com.miui.gallery.search.core.suggestion.SuggestionCursor, android.database.Cursor] */
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<DataListSourceResult> loader, DataListSourceResult dataListSourceResult) {
            Boolean valueOf = Boolean.valueOf((dataListSourceResult == null || dataListSourceResult.getData() == 0 || dataListSourceResult.getData().getCount() <= 0) ? false : true);
            this.mHasLikelyItems = valueOf;
            SearchLog.d("SearchImageResultFragment", "On query likely item finished [%s]", valueOf);
            showLikelyBar();
            SearchImageResultFragment.this.getLoaderManager().destroyLoader(4);
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
        Loader loader3 = getLoaderManager().getLoader(3);
        if (loader3 != null) {
            arrayList.add(loader3);
        }
        return arrayList;
    }

    public final void restartSectionLoader(boolean z) {
        Bundle bundle;
        if (z) {
            bundle = new Bundle(1);
            bundle.putBoolean("force_requery", true);
        } else {
            bundle = null;
        }
        restartSectionLoader(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1 && i2 == -1) {
            SearchFeedbackLikelyController searchFeedbackLikelyController = this.mFeedbackLikelyController;
            if (searchFeedbackLikelyController != null) {
                searchFeedbackLikelyController.hideLikelyBar();
                this.mFeedbackLikelyController = null;
            }
            restartSectionLoader(true);
        }
    }

    @Override // com.miui.gallery.search.resultpage.SearchImageResultFragmentBase
    public void trackLoadComplete() {
        TimeMonitor.trackTimeMonitor("403.18.0.1.13787");
        TimeMonitor.trackTimeMonitor("403.19.0.1.13788");
    }

    /* loaded from: classes2.dex */
    public class ChoiceModeListener implements MultiChoiceModeListener {
        public MenuItem mAddToAlbum;
        public MenuItem mCreativity;
        public MenuItem mDelete;
        public MenuItem mFeedback;
        public ActionMode mMode;
        public MenuItem mSend;

        public static /* synthetic */ void $r8$lambda$QKSaLZjihDSGnh290ziDYoaLXLI(ChoiceModeListener choiceModeListener) {
            choiceModeListener.lambda$onActionItemClicked$1();
        }

        public static /* synthetic */ void $r8$lambda$wApRMsaGFyYDRuVU9PJB6rvmU7E(ChoiceModeListener choiceModeListener, String str, long[] jArr, boolean z) {
            choiceModeListener.lambda$onActionItemClicked$0(str, jArr, z);
        }

        public ChoiceModeListener() {
            SearchImageResultFragment.this = r1;
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void onAllItemsCheckedStateChanged(ActionMode actionMode, boolean z) {
            updateMenuState();
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long j, boolean z) {
            updateMenuState();
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            this.mMode = actionMode;
            if (SearchImageResultFragment.this.supportFeedback()) {
                actionMode.getMenuInflater().inflate(R.menu.image_result_image_menu, menu);
            } else {
                actionMode.getMenuInflater().inflate(R.menu.home_page_menu, menu);
            }
            this.mFeedback = menu.findItem(R.id.feedback);
            this.mAddToAlbum = menu.findItem(R.id.add_to_album);
            this.mDelete = menu.findItem(R.id.delete);
            this.mCreativity = menu.findItem(R.id.action_produce);
            this.mSend = menu.findItem(R.id.action_send);
            this.mCreativity.setVisible(GalleryPreferences.Assistant.isCreativityFunctionOn());
            if (SearchImageResultFragment.this.mInFeedbackTaskMode) {
                this.mAddToAlbum.setVisible(false);
                this.mDelete.setVisible(false);
                this.mCreativity.setVisible(false);
                this.mSend.setVisible(false);
            }
            TrackController.trackExpose("403.50.0.1.13125", AutoTracking.getRef());
            return true;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            if (SearchImageResultFragment.this.mFeedbackLikelyController != null) {
                SearchImageResultFragment.this.mFeedbackLikelyController.hideLikelyBar();
                return false;
            }
            return false;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(final ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() != 16908313 && menuItem.getItemId() != 16908314) {
                LinearMotorHelper.performHapticFeedback(SearchImageResultFragment.this.mActivity, LinearMotorHelper.HAPTIC_TAP_LIGHT);
            }
            switch (menuItem.getItemId()) {
                case R.id.action_produce /* 2131361895 */:
                    MediaAndAlbumOperations.doProduceCreation(SearchImageResultFragment.this.mActivity, new MediaAndAlbumOperations.OnCompleteListener() { // from class: com.miui.gallery.search.resultpage.SearchImageResultFragment.ChoiceModeListener.2
                        {
                            ChoiceModeListener.this = this;
                        }

                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnCompleteListener
                        public void onComplete(long[] jArr) {
                            actionMode.finish();
                        }
                    }, SearchImageResultFragment.this.mEditableWrapper.getCheckedItems());
                    SamplingStatHelper.recordCountEvent(MiStat.Event.SEARCH, "produce");
                    return true;
                case R.id.action_send /* 2131361903 */:
                    SearchImageResultFragment.this.onSend(null, null);
                    return true;
                case R.id.add_to_album /* 2131361918 */:
                    SearchImageResultFragment searchImageResultFragment = SearchImageResultFragment.this;
                    final String checkedServerIds = searchImageResultFragment.getCheckedServerIds(searchImageResultFragment.mEditableWrapper.getCheckedPositions());
                    MediaAndAlbumOperations.addToAlbum(SearchImageResultFragment.this.mActivity, new MediaAndAlbumOperations.OnAddAlbumListener() { // from class: com.miui.gallery.search.resultpage.SearchImageResultFragment$ChoiceModeListener$$ExternalSyntheticLambda0
                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnAddAlbumListener
                        public final void onComplete(long[] jArr, boolean z) {
                            SearchImageResultFragment.ChoiceModeListener.$r8$lambda$wApRMsaGFyYDRuVU9PJB6rvmU7E(SearchImageResultFragment.ChoiceModeListener.this, checkedServerIds, jArr, z);
                        }
                    }, new MediaAndAlbumOperations.OnPicToPdfClickListener() { // from class: com.miui.gallery.search.resultpage.SearchImageResultFragment$ChoiceModeListener$$ExternalSyntheticLambda1
                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnPicToPdfClickListener
                        public final void onPicToPdfClick() {
                            SearchImageResultFragment.ChoiceModeListener.$r8$lambda$QKSaLZjihDSGnh290ziDYoaLXLI(SearchImageResultFragment.ChoiceModeListener.this);
                        }
                    }, false, true, true, SearchImageResultFragment.this.mEditableWrapper.isCheckedItemContainVideo(), SearchImageResultFragment.this.mEditableWrapper.getCheckedItemIds());
                    TrackController.trackExpose("403.26.0.1.11240", "403.50.0.1.13125");
                    return true;
                case R.id.delete /* 2131362250 */:
                    SearchImageResultFragment.this.doDelete(actionMode);
                    return true;
                case R.id.feedback /* 2131362394 */:
                    SearchImageResultFragment searchImageResultFragment2 = SearchImageResultFragment.this;
                    ArrayList<String> checkedServerIdList = searchImageResultFragment2.getCheckedServerIdList(searchImageResultFragment2.mEditableWrapper.getCheckedPositions());
                    AppCompatActivity appCompatActivity = SearchImageResultFragment.this.mActivity;
                    SearchImageResultFragment searchImageResultFragment3 = SearchImageResultFragment.this;
                    SearchFeedbackHelper.reportFalsePositiveImages(appCompatActivity, searchImageResultFragment3.mQueryText, searchImageResultFragment3.mInFeedbackTaskMode, checkedServerIdList, new SearchFeedbackHelper.OnFeedbackCompleteListener() { // from class: com.miui.gallery.search.resultpage.SearchImageResultFragment.ChoiceModeListener.1
                        {
                            ChoiceModeListener.this = this;
                        }

                        @Override // com.miui.gallery.search.feedback.SearchFeedbackHelper.OnFeedbackCompleteListener
                        public void onComplete(int i) {
                            actionMode.finish();
                            if (i > 0) {
                                SearchImageResultFragment.this.mSectionDataResultHelper.resetCacheInfo();
                                Bundle bundle = new Bundle(1);
                                bundle.putBoolean("force_requery", true);
                                SearchImageResultFragment.this.restartSectionDataLoader(bundle);
                            }
                        }
                    });
                    return true;
                default:
                    return false;
            }
        }

        public /* synthetic */ void lambda$onActionItemClicked$0(String str, long[] jArr, boolean z) {
            this.mMode.finish();
            if (jArr != null) {
                HashMap hashMap = new HashMap();
                hashMap.put("queryText", SearchImageResultFragment.this.mQueryText);
                hashMap.put(MiStat.Param.COUNT, String.valueOf(jArr.length));
                hashMap.put("serverIds", str);
                SearchStatUtils.reportEvent("from_image_result", "client_image_operation_add_to_album", hashMap);
            }
        }

        public /* synthetic */ void lambda$onActionItemClicked$1() {
            if (PicToPdfHelper.prepareGotoPicToPdfPreviewPage(SearchImageResultFragment.this.mActivity, SearchImageResultFragment.this.mEditableWrapper.getCheckedItems())) {
                TrackController.trackClick("403.26.0.1.11243", "403.26.0.1.11240", SearchImageResultFragment.this.mEditableWrapper.getCheckedItems().size());
                SearchImageResultFragment.this.mEditableWrapper.stopActionMode();
            }
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            this.mMode = null;
            if (SearchImageResultFragment.this.mFeedbackLikelyController != null) {
                SearchImageResultFragment.this.mFeedbackLikelyController.showLikelyBar();
            }
        }

        public boolean inChoiceMode() {
            return this.mMode != null;
        }

        public final void updateMenuState() {
            if (SearchImageResultFragment.this.mEditableWrapper.getCheckedItemCount() < 1) {
                this.mAddToAlbum.setEnabled(false);
                this.mDelete.setEnabled(false);
                this.mCreativity.setEnabled(false);
                this.mSend.setEnabled(false);
                MenuItem menuItem = this.mFeedback;
                if (menuItem == null) {
                    return;
                }
                menuItem.setEnabled(false);
                return;
            }
            this.mAddToAlbum.setEnabled(true);
            this.mDelete.setEnabled(true);
            this.mCreativity.setEnabled(true);
            this.mSend.setEnabled(true);
            MenuItem menuItem2 = this.mFeedback;
            if (menuItem2 == null) {
                return;
            }
            menuItem2.setEnabled(true);
        }

        public ActionMode getMode() {
            return this.mMode;
        }
    }

    public final void doDelete(final ActionMode actionMode) {
        final String checkedServerIds = getCheckedServerIds(this.mEditableWrapper.getCheckedPositions());
        MediaAndAlbumOperations.delete(this.mActivity, "SearchImageResultFragmentDeleteMediaDialogFragment", new DeletionTask.OnDeletionCompleteListener() { // from class: com.miui.gallery.search.resultpage.SearchImageResultFragment.2
            {
                SearchImageResultFragment.this = this;
            }

            @Override // com.miui.gallery.ui.DeletionTask.OnDeletionCompleteListener
            public void onDeleted(int i, long[] jArr) {
                if (SearchImageResultFragment.this.mActivity == null) {
                    return;
                }
                ToastUtils.makeText(SearchImageResultFragment.this.mActivity, SearchImageResultFragment.this.getResources().getQuantityString(R.plurals.delete_finish_format, i, Integer.valueOf(i)));
                if (i > 0) {
                    SoundUtils.playSoundForOperation(SearchImageResultFragment.this.mActivity, 0);
                }
                actionMode.finish();
                HashMap hashMap = new HashMap();
                hashMap.put("queryText", SearchImageResultFragment.this.mQueryText);
                hashMap.put(MiStat.Param.COUNT, String.valueOf(i));
                hashMap.put("serverIds", checkedServerIds);
                SearchStatUtils.reportEvent("from_image_result", "client_image_operation_delete", hashMap);
            }
        }, null, -1L, "", 27, this.mEditableWrapper.getCheckedItemIds());
    }

    public final void onSend(String str, String str2) {
        List<Integer> checkedPositions = this.mEditableWrapper.getCheckedPositions();
        ArrayList arrayList = new ArrayList(checkedPositions.size());
        int i = 0;
        int i2 = Integer.MAX_VALUE;
        for (int i3 = 0; i3 < checkedPositions.size(); i3++) {
            int intValue = checkedPositions.get(i3).intValue();
            arrayList.add(Long.valueOf(this.mAdapter.getItemKey(intValue)));
            if (intValue < i2) {
                i2 = intValue;
            }
        }
        if (checkedPositions.size() == 0) {
            return;
        }
        int[] iArr = new int[checkedPositions.size()];
        long[] jArr = new long[arrayList.size()];
        for (int i4 = 0; i4 < checkedPositions.size(); i4++) {
            iArr[i4] = checkedPositions.get(i4).intValue();
            jArr[i4] = ((Long) arrayList.get(i4)).longValue();
        }
        if (i2 != Integer.MAX_VALUE) {
            i = i2;
        }
        IntentUtil.gotoPreviewSelectPage(this, getPhotoPageDataLoaderUri(), i, this.mAdapter.getItemCount(), getSelection(), getSelectionArguments(), getOrder(), new ImageLoadParams.Builder().setKey(this.mAdapter.getItemKey(i)).setFilePath(this.mAdapter.getBindImagePath(i)).setTargetSize(getMicroThumbnailSize()).setRegionRect(this.mAdapter.getItemDecodeRectF(i)).setInitPosition(i).setMimeType(this.mAdapter.getMimeType(i)).setFileLength(this.mAdapter.getFileLength(i)).setCreateTime(this.mAdapter.getCreateTime(i)).setLocation(this.mAdapter.getLocation(i)).build(), jArr, iArr, str, str2);
        this.mEditableWrapper.stopActionMode();
        HashMap hashMap = new HashMap();
        hashMap.put("queryText", this.mQueryText);
        hashMap.put(MiStat.Param.COUNT, String.valueOf(checkedPositions.size()));
        hashMap.put("serverIds", getCheckedServerIds(checkedPositions));
        SearchStatUtils.reportEvent("from_image_result", "client_image_operation_share", hashMap);
    }

    @Override // com.miui.gallery.search.resultpage.SearchImageResultFragmentBase, com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mGridLayoutManager.setSpanCount(this.mColumnCount);
    }

    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        ArrayList arrayList = new ArrayList();
        if (this.mEditableWrapper.isInActionMode()) {
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getSelectAllShortcutInfo());
            arrayList.addAll(KeyboardShortcutGroupManager.getInstance().getDeleteShortcutInfo());
        }
        list.add(new KeyboardShortcutGroup(getPageName(), arrayList));
    }

    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        return KeyboardShortcutGroupManager.getInstance().onKeyShortcut(i, keyEvent, this.mShortcutCallback);
    }

    /* loaded from: classes2.dex */
    public class SearchKeyboardShortcutCallback implements KeyboardShortcutGroupManager.OnKeyShortcutCallback {
        public SearchKeyboardShortcutCallback() {
            SearchImageResultFragment.this = r1;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onSelectAllPressed() {
            if (SearchImageResultFragment.this.mEditableWrapper.isInActionMode()) {
                SearchImageResultFragment.this.mEditableWrapper.setAllItemsCheckState(true);
            }
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onDeletePressed() {
            if (SearchImageResultFragment.this.mEditableWrapper.isInActionMode()) {
                SearchImageResultFragment searchImageResultFragment = SearchImageResultFragment.this;
                searchImageResultFragment.doDelete(searchImageResultFragment.mChoiceModeListener.getMode());
                return true;
            }
            return true;
        }
    }
}
