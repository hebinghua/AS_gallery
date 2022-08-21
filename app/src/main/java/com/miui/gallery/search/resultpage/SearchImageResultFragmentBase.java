package com.miui.gallery.search.resultpage;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.activity.InternalPhotoPageActivity;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.StatusHandleHelper;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.suggestion.RankInfo;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.search.utils.SearchUtils;
import com.miui.gallery.util.PhotoPageIntent;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import com.miui.gallery.widget.recyclerview.SimpleHeaderFooterWrapperAdapter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class SearchImageResultFragmentBase extends SearchResultFragmentBase {
    public ImageResultAdapter mAdapter;
    public int mColumnCount = 0;
    public ErrorViewAdapter mErrorViewAdapter = null;
    public SimpleHeaderFooterWrapperAdapter mHeaderFooterWrapper;
    public View mLoadCompleteFooter;
    public GalleryRecyclerView mRecyclerView;
    public GridItemSpacingDecoration mSpacingDecoration;

    /* renamed from: $r8$lambda$Q8b0w7_-H8sGU2pXGu4wu3EMzKk */
    public static /* synthetic */ void m1341$r8$lambda$Q8b0w7_H8sGU2pXGu4wu3EMzKk(SearchImageResultFragmentBase searchImageResultFragmentBase) {
        searchImageResultFragmentBase.lambda$onLoadComplete$0();
    }

    public String getOrder() {
        return null;
    }

    public String getSelection() {
        return null;
    }

    public void trackLoadComplete() {
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public boolean usePersistentResponse() {
        return true;
    }

    public final void bind(GalleryRecyclerView galleryRecyclerView, ImageResultAdapter imageResultAdapter) {
        this.mRecyclerView = galleryRecyclerView;
        galleryRecyclerView.setItemAnimator(null);
        this.mAdapter = imageResultAdapter;
        this.mSpacingDecoration = new GridItemSpacingDecoration(galleryRecyclerView, false);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.micro_thumb_horizontal_spacing);
        this.mSpacingDecoration.setSpacing(dimensionPixelSize, dimensionPixelSize);
        this.mRecyclerView.setItemAnimator(null);
        this.mRecyclerView.addItemDecoration(this.mSpacingDecoration);
        updateConfig(getResources().getConfiguration());
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(this.mActivity, this.mColumnCount));
        SimpleHeaderFooterWrapperAdapter simpleHeaderFooterWrapperAdapter = new SimpleHeaderFooterWrapperAdapter(this.mAdapter);
        this.mHeaderFooterWrapper = simpleHeaderFooterWrapperAdapter;
        this.mRecyclerView.setAdapter(simpleHeaderFooterWrapperAdapter);
        this.mRecyclerView.setFastScrollEnabled(true);
        this.mRecyclerView.setFastScrollerTopMargin(this.mFastScrollerMarginTop);
        this.mAdapter.setRequestLoadMoreListener(this);
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public boolean supportFeedback() {
        return this.mInFeedbackTaskMode || super.supportFeedback();
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void changeSuggestions(QueryInfo queryInfo, SuggestionCursor suggestionCursor) {
        int i;
        int dimensionPixelSize;
        RankInfo rankInfo = SearchUtils.getRankInfo(suggestionCursor);
        boolean isHorizontalDocumentStyle = SearchConstants.isHorizontalDocumentStyle(rankInfo);
        if (isHorizontalDocumentStyle) {
            i = Config$ThumbConfig.get().sMicroThumbHorizontalDocumentColumns;
        } else {
            i = getResources().getConfiguration().orientation == 2 ? Config$ThumbConfig.get().sMicroThumbColumnsLand : Config$ThumbConfig.get().sMicroThumbColumnsPortrait;
        }
        if (isHorizontalDocumentStyle) {
            dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.micro_thumb_document_spacing);
        } else {
            dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.micro_thumb_horizontal_spacing);
        }
        if (this.mColumnCount != i) {
            this.mColumnCount = i;
            ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(this.mColumnCount);
            this.mSpacingDecoration.setSpacing(dimensionPixelSize, dimensionPixelSize);
        }
        this.mAdapter.changeSuggestions(queryInfo, rankInfo, suggestionCursor);
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public StatusHandleHelper.AbstractErrorViewAdapter getErrorViewAdapter() {
        if (this.mErrorViewAdapter == null) {
            this.mErrorViewAdapter = new ErrorViewAdapter(this.mActivity);
        }
        return this.mErrorViewAdapter;
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void openLoadMore() {
        this.mAdapter.openLoadMore();
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void closeLoadMore() {
        this.mAdapter.closeLoadMore();
        this.mStatusHandleHelper.refreshInfoViews();
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void onLoadComplete() {
        this.mAdapter.closeLoadMore();
        this.mStatusHandleHelper.refreshInfoViews();
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.search.resultpage.SearchImageResultFragmentBase$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SearchImageResultFragmentBase.m1341$r8$lambda$Q8b0w7_H8sGU2pXGu4wu3EMzKk(SearchImageResultFragmentBase.this);
            }
        });
    }

    public /* synthetic */ void lambda$onLoadComplete$0() {
        if (moreThanOnePage()) {
            if (this.mLoadCompleteFooter == null) {
                this.mLoadCompleteFooter = LayoutInflater.from(this.mActivity).inflate(R.layout.search_item_load_complete_layout, (ViewGroup) this.mRecyclerView, false);
                GalleryRecyclerView galleryRecyclerView = this.mRecyclerView;
                galleryRecyclerView.setPadding(galleryRecyclerView.getPaddingLeft(), this.mRecyclerView.getPaddingTop(), this.mRecyclerView.getPaddingRight(), 0);
                this.mHeaderFooterWrapper.addFooter(this.mLoadCompleteFooter);
            }
            this.mStatusHandleHelper.refreshInfoViews();
        } else {
            View view = this.mLoadCompleteFooter;
            if (view != null) {
                this.mHeaderFooterWrapper.removeFooter(view);
                this.mLoadCompleteFooter = null;
            }
        }
        trackLoadComplete();
    }

    public boolean moreThanOnePage() {
        return this.mRecyclerView.findFirstVisibleItemPosition() > 0 || this.mRecyclerView.findLastVisibleItemPosition() < this.mAdapter.getItemCount() - 1;
    }

    public void gotoPhotoPage(ViewGroup viewGroup, int i, String str) {
        new PhotoPageIntent.Builder(this, InternalPhotoPageActivity.class).setAdapterView(viewGroup).setUri(getPhotoPageDataLoaderUri()).setInitPosition(i).setCount(this.mAdapter.getItemCount()).setSelection(getSelection()).setSelectionArgs(getSelectionArguments()).setOrderBy(getOrder()).setImageLoadParams(new ImageLoadParams.Builder().setKey(this.mAdapter.getItemKey(i)).setFilePath(this.mAdapter.getBindImagePath(i)).setTargetSize(getMicroThumbnailSize()).setRegionRect(this.mAdapter.getItemDecodeRectF(i)).setInitPosition(i).setMimeType(this.mAdapter.getMimeType(i)).setFileLength(this.mAdapter.getFileLength(i)).setImageWidth(this.mAdapter.getImageWidth(i)).setImageHeight(this.mAdapter.getImageHeight(i)).setCreateTime(this.mAdapter.getCreateTime(i)).setLocation(this.mAdapter.getLocation(i)).build()).setUnfoldBurst(true).build().gotoPhotoPage();
        SearchStatUtils.reportEvent(str, "client_image_operation_open_photo_page", "serverIds", this.mAdapter.getServerId(i), "queryText", this.mQueryText);
    }

    public String getImageIds() {
        ImageResultAdapter imageResultAdapter = this.mAdapter;
        if (imageResultAdapter != null) {
            return imageResultAdapter.getImageIds();
        }
        return null;
    }

    public Uri getPhotoPageDataLoaderUri() {
        return GalleryContract.SearchResultPhoto.URI;
    }

    public String[] getSelectionArguments() {
        String imageIds = getImageIds();
        if (!TextUtils.isEmpty(imageIds)) {
            return imageIds.split(",");
        }
        return null;
    }

    public Size getMicroThumbnailSize() {
        ImageResultAdapter imageResultAdapter = this.mAdapter;
        return imageResultAdapter != null ? imageResultAdapter.getMicroThumbnailSize() : Config$ThumbConfig.get().sMicroTargetSize;
    }

    public void gotoPhotoPageFromPicker(RecyclerView recyclerView, View view, int i) {
        FolmeUtil.setFakeTouchAnim(view, 0.9f, 200L);
    }

    /* loaded from: classes2.dex */
    public class ErrorViewAdapter extends StatusHandleHelper.AbstractErrorViewAdapter {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ErrorViewAdapter(Context context) {
            super(context);
            SearchImageResultFragmentBase.this = r1;
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void requestRetry() {
            SearchImageResultFragmentBase.this.doRetry();
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public boolean isEmptyDataView() {
            return SearchImageResultFragmentBase.this.mAdapter.getItemCount() <= 0;
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public boolean isLoading() {
            return SearchImageResultFragmentBase.this.mAdapter.isLoading();
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void addHeaderView(View view) {
            SearchImageResultFragmentBase.this.mHeaderFooterWrapper.addHeader(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void removeHeaderView(View view) {
            SearchImageResultFragmentBase.this.mHeaderFooterWrapper.removeHeader(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void addFooterView(View view) {
            SearchImageResultFragmentBase.this.mHeaderFooterWrapper.addFooter(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.ErrorViewAdapter
        public void removeFooterView(View view) {
            SearchImageResultFragmentBase.this.mHeaderFooterWrapper.removeFooter(view);
        }

        @Override // com.miui.gallery.search.StatusHandleHelper.AbstractErrorViewAdapter
        public View getInfoItemView(View view, int i, StatusHandleHelper.InfoViewPosition infoViewPosition) {
            if (infoViewPosition == StatusHandleHelper.InfoViewPosition.FOOTER) {
                return this.mInflater.inflate(R.layout.search_item_error_footer_layout, (ViewGroup) null);
            }
            return super.getInfoItemView(view, i, infoViewPosition);
        }
    }

    public ArrayList<String> getCheckedServerIdList(List<Integer> list) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                arrayList.add(this.mAdapter.getServerId(list.get(i).intValue()));
            }
        }
        return arrayList;
    }

    public String getCheckedServerIds(List<Integer> list) {
        return TextUtils.join(",", getCheckedServerIdList(list));
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        ImageResultAdapter imageResultAdapter = this.mAdapter;
        if (imageResultAdapter != null) {
            imageResultAdapter.swapCursor(null);
        }
        super.onDestroy();
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateConfig(configuration);
    }

    public final void updateConfig(Configuration configuration) {
        Config$ThumbConfig.get().updateConfig();
        if (configuration.orientation == 1 || this.mIsMultiWindow) {
            this.mColumnCount = Config$ThumbConfig.get().sMicroThumbColumnsPortrait;
        } else {
            this.mColumnCount = Config$ThumbConfig.get().sMicroThumbColumnsLand;
        }
    }
}
