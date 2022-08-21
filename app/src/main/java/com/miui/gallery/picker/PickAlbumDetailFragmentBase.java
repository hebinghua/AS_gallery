package com.miui.gallery.picker;

import android.app.Activity;
import android.content.res.Configuration;
import android.database.Cursor;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.common.collect.Lists;
import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.activity.InternalPhotoPageActivity;
import com.miui.gallery.adapter.BaseRecyclerAdapter;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.picker.albumdetail.IPickAlbumDetail;
import com.miui.gallery.picker.albumdetail.ISelectAllDecor;
import com.miui.gallery.picker.albumdetail.ItemStateListener;
import com.miui.gallery.picker.albumdetail.SelectionHolder;
import com.miui.gallery.picker.helper.AdapterHolder;
import com.miui.gallery.picker.helper.CursorUtils;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.Checkable;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.util.PhotoPageIntent;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class PickAlbumDetailFragmentBase extends PickerFragment implements IPickAlbumDetail, ISelectAllDecor {
    public int mColumns;
    public String mPageName;
    public GalleryRecyclerView mRecyclerView;
    public SelectionHolder mSelections;
    public int mSpacing;

    public static /* synthetic */ BaseRecyclerAdapter $r8$lambda$nid6Qchjw9sqIgyX5rHvEDYgFGk(PickAlbumDetailFragmentBase pickAlbumDetailFragmentBase) {
        return pickAlbumDetailFragmentBase.lambda$initialSelections$0();
    }

    public PickAlbumDetailFragmentBase(String str) {
        this.mPageName = str;
    }

    public /* synthetic */ BaseRecyclerAdapter lambda$initialSelections$0() {
        return findDataAdapter(this.mRecyclerView);
    }

    public void initialSelections() {
        this.mSelections = new SelectionHolder(this, new AdapterHolder() { // from class: com.miui.gallery.picker.PickAlbumDetailFragmentBase$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.picker.helper.AdapterHolder
            public final BaseRecyclerAdapter get() {
                return PickAlbumDetailFragmentBase.$r8$lambda$nid6Qchjw9sqIgyX5rHvEDYgFGk(PickAlbumDetailFragmentBase.this);
            }
        });
    }

    public void stopAndHideScroller() {
        GalleryRecyclerView galleryRecyclerView = this.mRecyclerView;
        if (galleryRecyclerView != null) {
            galleryRecyclerView.stopScroll();
            this.mRecyclerView.hideScrollerBar();
        }
    }

    public void copy2Pick() {
        this.mSelections.copyFrom(getPicker());
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

    @Override // com.miui.gallery.picker.albumdetail.ISelectAllDecor
    public void selectAll() {
        BaseRecyclerAdapter findDataAdapter = findDataAdapter(this.mRecyclerView);
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
        BaseRecyclerAdapter findDataAdapter = findDataAdapter(this.mRecyclerView);
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
        RecyclerView.Adapter adapter = this.mRecyclerView.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
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

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateConfiguration(configuration);
    }

    public void updateConfiguration(Configuration configuration) {
        int findFirstVisibleItemPosition = this.mRecyclerView.findFirstVisibleItemPosition();
        if (configuration.orientation == 2) {
            this.mColumns = Config$ThumbConfig.get().sMicroThumbColumnsLand;
        } else {
            this.mColumns = Config$ThumbConfig.get().sMicroThumbColumnsPortrait;
        }
        ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(this.mColumns);
        this.mRecyclerView.scrollToPosition(findFirstVisibleItemPosition);
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

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public Activity getPickerActivity() {
        return getActivity();
    }

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public void gotoPhotoPageFromPicker(RecyclerView recyclerView, View view, int i) {
        BaseRecyclerAdapter findDataAdapter = findDataAdapter(recyclerView);
        if (findDataAdapter == null) {
            return;
        }
        FolmeUtil.setFakeTouchAnim(view, 0.9f, 200L);
        Cursor cursor = (Cursor) findDataAdapter.mo1558getItem(i);
        new PhotoPageIntent.Builder(getActivity(), InternalPhotoPageActivity.class).setAdapterView(this.mRecyclerView).setUri(getPreviewUri()).setSelection(getPreviewSelection(cursor)).setOrderBy(getPreviewOrder()).setImageLoadParams(new ImageLoadParams.Builder().setKey(getKey(cursor)).setFilePath(getLocalPath(cursor)).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize).setInitPosition(0).setMimeType(CursorUtils.getMimeType(cursor)).fromFace(isPreviewFace()).setFileLength(getFileLength(cursor)).setImageWidth(CursorUtils.getWidth(cursor)).setImageHeight(CursorUtils.getHeight(cursor)).setCreateTime(CursorUtils.getCreateTime(cursor)).setLocation(CursorUtils.getLocation(cursor)).build()).setIdForPicker(getKey(cursor)).setUnfoldBurst(!supportFoldBurstItems()).setPreview(true).build().gotoPhotoPage();
    }

    public final BaseRecyclerAdapter findDataAdapter(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        while (adapter instanceof AbstractHeaderFooterWrapperAdapter) {
            adapter = ((AbstractHeaderFooterWrapperAdapter) adapter).getWrappedAdapter();
        }
        return (BaseRecyclerAdapter) WrapperAdapterUtils.findWrappedAdapter(adapter, BaseRecyclerAdapter.class);
    }
}
