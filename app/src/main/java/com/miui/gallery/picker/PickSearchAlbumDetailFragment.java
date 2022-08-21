package com.miui.gallery.picker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.common.collect.Lists;
import com.h6ah4i.android.widget.advrecyclerview.composedadapter.ComposedAdapter;
import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.activity.InternalPhotoPageActivity;
import com.miui.gallery.adapter.BaseRecyclerAdapter;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.picker.albumdetail.AlbumItemCheckListener;
import com.miui.gallery.picker.albumdetail.IPickAlbumDetail;
import com.miui.gallery.picker.albumdetail.ISelectAllDecor;
import com.miui.gallery.picker.albumdetail.ItemStateListener;
import com.miui.gallery.picker.albumdetail.SelectionHolder;
import com.miui.gallery.picker.helper.AdapterHolder;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.picker.helper.PickerItemHolder;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.suggestion.GroupedSuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import com.miui.gallery.search.resultpage.ImageResultAdapter;
import com.miui.gallery.search.resultpage.SearchImageResultFragmentBase;
import com.miui.gallery.search.resultpage.SearchImageResultHelper;
import com.miui.gallery.search.resultpage.SearchResultHelper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.Checkable;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.util.PhotoPageIntent;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.miui.gallery.widget.recyclerview.SimpleHeaderFooterWrapperAdapter;
import com.miui.gallery.widget.recyclerview.SpanSizeProvider;
import com.miui.gallery.widget.recyclerview.grouped.GroupedItemManager;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class PickSearchAlbumDetailFragment extends SearchImageResultFragmentBase implements PickerImpl, IPickAlbumDetail, ISelectAllDecor {
    public AlbumItemCheckListener mAlbumItemCheckListener;
    public GroupedItemManager mGroupedItemManager;
    public String mPageName = MiStat.Event.SEARCH;
    public Picker mPicker;
    public SelectionHolder mSelections;

    public static /* synthetic */ BaseRecyclerAdapter $r8$lambda$Y6ziEhlX_Xp_WRdfy4erR8s3rqo(PickSearchAlbumDetailFragment pickSearchAlbumDetailFragment) {
        return pickSearchAlbumDetailFragment.findDataAdapter();
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void changeFilterData(QueryInfo queryInfo, SuggestionCursor suggestionCursor) {
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
    public void restartFilterLoader(Bundle bundle) {
    }

    @Override // com.miui.gallery.picker.PickerImpl
    public void attach(Picker picker) {
        this.mPicker = picker;
    }

    public void initialSelections() {
        this.mSelections = new SelectionHolder(this, new AdapterHolder() { // from class: com.miui.gallery.picker.PickSearchAlbumDetailFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.picker.helper.AdapterHolder
            public final BaseRecyclerAdapter get() {
                return PickSearchAlbumDetailFragment.$r8$lambda$Y6ziEhlX_Xp_WRdfy4erR8s3rqo(PickSearchAlbumDetailFragment.this);
            }
        });
    }

    public void copy2Pick() {
        this.mSelections.copyFrom(this.mPicker);
    }

    public final BaseRecyclerAdapter findDataAdapter() {
        RecyclerView.Adapter adapter = this.mRecyclerView.getAdapter();
        while (adapter instanceof AbstractHeaderFooterWrapperAdapter) {
            adapter = ((AbstractHeaderFooterWrapperAdapter) adapter).getWrappedAdapter();
        }
        return (BaseRecyclerAdapter) WrapperAdapterUtils.findWrappedAdapter(adapter, BaseRecyclerAdapter.class);
    }

    @Override // com.miui.gallery.search.resultpage.SearchResultFragmentBase
    public void onInflateView(View view, Bundle bundle, Uri uri) {
        bind((GalleryRecyclerView) view.findViewById(R.id.grid), new PickImageResultAdapter(this.mActivity));
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this.mActivity, Config$ThumbConfig.get().sMicroThumbColumnsPortrait);
        gridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new SpanSizeProvider() { // from class: com.miui.gallery.picker.PickSearchAlbumDetailFragment.1
            {
                PickSearchAlbumDetailFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanSize(int i) {
                if (gridLayoutManager.getSpanCount() > 1) {
                    long segmentedPosition = PickSearchAlbumDetailFragment.this.mHeaderFooterWrapper.getSegmentedPosition(i);
                    if (ComposedAdapter.extractSegmentPart(segmentedPosition) != 1) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (GroupedItemManager.getPackedPositionChild(PickSearchAlbumDetailFragment.this.mGroupedItemManager.getExpandablePosition(ComposedAdapter.extractSegmentOffsetPart(segmentedPosition))) != -1) {
                        return 1;
                    }
                    return gridLayoutManager.getSpanCount();
                }
                return gridLayoutManager.getSpanCount();
            }

            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanIndex(int i, int i2) {
                int packedPositionChild;
                if (gridLayoutManager.getSpanCount() <= 1) {
                    return 0;
                }
                long segmentedPosition = PickSearchAlbumDetailFragment.this.mHeaderFooterWrapper.getSegmentedPosition(i);
                if (ComposedAdapter.extractSegmentPart(segmentedPosition) != 1 || (packedPositionChild = GroupedItemManager.getPackedPositionChild(PickSearchAlbumDetailFragment.this.mGroupedItemManager.getExpandablePosition(ComposedAdapter.extractSegmentOffsetPart(segmentedPosition)))) == -1) {
                    return 0;
                }
                return packedPositionChild % i2;
            }
        }));
        this.mGroupedItemManager = new GroupedItemManager();
        this.mRecyclerView.setLayoutManager(gridLayoutManager);
        SimpleHeaderFooterWrapperAdapter simpleHeaderFooterWrapperAdapter = new SimpleHeaderFooterWrapperAdapter(this.mGroupedItemManager.createWrappedAdapter(this.mAdapter));
        this.mHeaderFooterWrapper = simpleHeaderFooterWrapperAdapter;
        this.mRecyclerView.setAdapter(simpleHeaderFooterWrapperAdapter);
        this.mRecyclerView.setOnItemClickListener(getGridViewOnItemClickListener());
        this.mAlbumItemCheckListener = new AlbumItemCheckListener(this, this.mPicker);
        initialSelections();
        copy2Pick();
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
            restartSectionLoader(true);
        }
    }

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public void pick(Picker picker, String str) {
        if (picker.pick(str)) {
            this.mSelections.add(str);
        }
        statPickEvent();
    }

    public void pick(Picker picker, List<String> list) {
        if (picker.pickAll(list)) {
            this.mSelections.addAll(list);
        }
        statPickEvent();
    }

    public final void statPickEvent() {
        HashMap hashMap = new HashMap();
        hashMap.put("page", this.mPageName);
        SamplingStatHelper.recordCountEvent("picker", "pick_event_page", hashMap);
    }

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public void remove(Picker picker, String str) {
        if (picker.remove(str)) {
            this.mSelections.remove(str);
        }
    }

    public void remove(Picker picker, List<String> list) {
        if (picker.removeAll(list)) {
            this.mSelections.removeAll(list);
        }
    }

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public boolean onPhotoItemClick(Cursor cursor, View view) {
        if (this.mPicker.getMode() == Picker.Mode.SINGLE) {
            this.mPicker.pick(genKeyFromCursor(cursor));
            this.mPicker.done(-1);
            return true;
        }
        return false;
    }

    @Override // com.miui.gallery.picker.albumdetail.ISelectAllDecor
    public void selectAll() {
        BaseRecyclerAdapter findDataAdapter = findDataAdapter();
        if (findDataAdapter != null) {
            int itemCount = findDataAdapter.getItemCount();
            LinkedList newLinkedList = Lists.newLinkedList();
            int i = 0;
            int i2 = 0;
            while (!this.mPicker.isFull() && i < itemCount) {
                i2 = Math.min(Math.max(0, this.mPicker.capacity() - this.mPicker.count()) + i, itemCount);
                while (i < i2) {
                    newLinkedList.add(genKeyFromCursor((Cursor) findDataAdapter.mo1558getItem(i)));
                    i++;
                }
                pick(this.mPicker, newLinkedList);
                newLinkedList.clear();
            }
            if (this.mPicker.isFull() && i2 < itemCount) {
                ToastUtils.makeText(getActivity(), getActivity().getString(R.string.picker_full_format, new Object[]{Integer.valueOf(this.mPicker.capacity())}));
            }
            refreshPickState();
        }
    }

    @Override // com.miui.gallery.picker.albumdetail.ISelectAllDecor
    public void deselectAll() {
        BaseRecyclerAdapter findDataAdapter = findDataAdapter();
        if (findDataAdapter != null) {
            LinkedList newLinkedList = Lists.newLinkedList();
            for (int i = 0; i < findDataAdapter.getItemCount(); i++) {
                newLinkedList.add(genKeyFromCursor((Cursor) findDataAdapter.mo1558getItem(i)));
                if (newLinkedList.size() >= 1000 || i == findDataAdapter.getItemCount() - 1) {
                    remove(this.mPicker, newLinkedList);
                    newLinkedList.clear();
                }
                if (this.mPicker.count() <= 0) {
                    break;
                }
            }
            refreshPickState();
        }
    }

    public final void refreshPickState() {
        int findFirstVisibleItemPosition = this.mRecyclerView.findFirstVisibleItemPosition();
        BaseRecyclerAdapter findDataAdapter = findDataAdapter();
        if (findDataAdapter == null) {
            DefaultLogger.w("SearchImageResultFragment", "adapter shouldn't be null");
            return;
        }
        for (int i = 0; i < this.mRecyclerView.getChildCount(); i++) {
            int unwrapPosition = unwrapPosition(findFirstVisibleItemPosition + i);
            if (unwrapPosition != -1) {
                View childAt = this.mRecyclerView.getChildAt(i);
                if (childAt instanceof Checkable) {
                    ((Checkable) childAt).setChecked(this.mPicker.contains(genKeyFromCursor((Cursor) findDataAdapter.mo1558getItem(unwrapPosition))));
                }
            }
        }
    }

    @Override // com.miui.gallery.picker.albumdetail.ISelectAllDecor
    public void setItemStateListener(ItemStateListener itemStateListener) {
        this.mSelections.setItemStateListener(itemStateListener);
    }

    @Override // com.miui.gallery.picker.albumdetail.ISelectAllDecor
    public boolean isAllSelected() {
        SelectionHolder selectionHolder = this.mSelections;
        return selectionHolder != null && selectionHolder.isAllSelected();
    }

    @Override // com.miui.gallery.picker.albumdetail.ISelectAllDecor
    public boolean isNoneSelected() {
        SelectionHolder selectionHolder = this.mSelections;
        return selectionHolder == null || selectionHolder.isNoneSelected();
    }

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public boolean bindCheckState(View view, Cursor cursor) {
        if (this.mPicker.getMode() == Picker.Mode.SINGLE) {
            if (view instanceof MicroThumbGridItem) {
                ((MicroThumbGridItem) view).setSimilarMarkEnable(true);
            }
            return true;
        }
        Checkable checkable = (Checkable) view;
        checkable.setCheckable(true);
        checkable.setChecked(this.mPicker.contains(genKeyFromCursor(cursor)));
        return true;
    }

    @Override // com.miui.gallery.search.resultpage.SearchImageResultFragmentBase, com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public void gotoPhotoPageFromPicker(RecyclerView recyclerView, View view, int i) {
        super.gotoPhotoPageFromPicker(recyclerView, view, i);
        new PhotoPageIntent.Builder(this, InternalPhotoPageActivity.class).setAdapterView(recyclerView).setUri(getPhotoPageDataLoaderUri()).setInitPosition(i).setCount(this.mAdapter.getItemCount()).setSelection(getSelection()).setSelectionArgs(getSelectionArguments()).setOrderBy(getOrder()).setImageLoadParams(new ImageLoadParams.Builder().setKey(this.mAdapter.getItemKey(i)).setFilePath(this.mAdapter.getBindImagePath(i)).setTargetSize(getMicroThumbnailSize()).setRegionRect(this.mAdapter.getItemDecodeRectF(i)).setInitPosition(i).setMimeType(this.mAdapter.getMimeType(i)).setFileLength(this.mAdapter.getFileLength(i)).setImageWidth(this.mAdapter.getImageWidth(i)).setImageHeight(this.mAdapter.getImageHeight(i)).setCreateTime(this.mAdapter.getCreateTime(i)).setLocation(this.mAdapter.getLocation(i)).build()).setUnfoldBurst(true).setPreview(true).build().gotoPhotoPage();
    }

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public Activity getPickerActivity() {
        return getActivity();
    }

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public int unwrapPosition(int i) {
        long segmentedPosition = this.mHeaderFooterWrapper.getSegmentedPosition(i);
        if (ComposedAdapter.extractSegmentPart(segmentedPosition) != 1) {
            return -1;
        }
        long expandablePosition = this.mGroupedItemManager.getExpandablePosition(ComposedAdapter.extractSegmentOffsetPart(segmentedPosition));
        int packedPositionGroup = GroupedItemManager.getPackedPositionGroup(expandablePosition);
        int packedPositionChild = GroupedItemManager.getPackedPositionChild(expandablePosition);
        if (packedPositionChild != -1) {
            return this.mAdapter.packDataPosition(packedPositionGroup, packedPositionChild);
        }
        return -1;
    }

    /* loaded from: classes2.dex */
    public class PickImageResultAdapter extends ImageResultAdapter {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public PickImageResultAdapter(Context context) {
            super(context);
            PickSearchAlbumDetailFragment.this = r1;
        }

        @Override // com.miui.gallery.search.resultpage.ImageResultAdapter, com.miui.gallery.adapter.BaseGroupedMediaAdapter
        public void doBindChildViewHolder(BaseViewHolder baseViewHolder, int i, int i2, List<Object> list) {
            super.doBindChildViewHolder(baseViewHolder, i, i2, list);
            MicroThumbGridItem microThumbGridItem = (MicroThumbGridItem) baseViewHolder.itemView;
            PickSearchAlbumDetailFragment.this.bindCheckState(microThumbGridItem, mo1558getItem(i));
            PickerItemHolder.bindView(i, microThumbGridItem, this, PickSearchAlbumDetailFragment.this.mAlbumItemCheckListener);
        }
    }
}
