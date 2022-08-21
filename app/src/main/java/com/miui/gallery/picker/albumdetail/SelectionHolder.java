package com.miui.gallery.picker.albumdetail;

import android.database.Cursor;
import com.miui.gallery.adapter.BaseRecyclerAdapter;
import com.miui.gallery.picker.helper.AdapterHolder;
import com.miui.gallery.picker.helper.Picker;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/* loaded from: classes2.dex */
public class SelectionHolder implements Iterable<String> {
    public AdapterHolder mAdapter;
    public IPickAlbumDetail mAlbumDetail;
    public ItemStateListener mItemStateListener;
    public LinkedHashSet<String> mSelection = new LinkedHashSet<>();

    public SelectionHolder(IPickAlbumDetail iPickAlbumDetail, AdapterHolder adapterHolder) {
        this.mAlbumDetail = iPickAlbumDetail;
        this.mAdapter = adapterHolder;
    }

    public boolean add(String str) {
        boolean add = this.mSelection.add(str);
        updateState();
        return add;
    }

    public boolean addAll(List<String> list) {
        boolean addAll = this.mSelection.addAll(list);
        updateState();
        return addAll;
    }

    public boolean remove(String str) {
        boolean remove = this.mSelection.remove(str);
        updateState();
        return remove;
    }

    public boolean removeAll(List<String> list) {
        boolean removeAll = this.mSelection.removeAll(list);
        updateState();
        return removeAll;
    }

    public void copyFrom(Picker picker) {
        this.mSelection.clear();
        BaseRecyclerAdapter baseRecyclerAdapter = this.mAdapter.get();
        for (int i = 0; i < baseRecyclerAdapter.getItemCount(); i++) {
            String genKeyFromCursor = this.mAlbumDetail.genKeyFromCursor((Cursor) baseRecyclerAdapter.mo1558getItem(i));
            if (picker.contains(genKeyFromCursor)) {
                this.mSelection.add(genKeyFromCursor);
            }
        }
        updateState();
    }

    public void setItemStateListener(ItemStateListener itemStateListener) {
        this.mItemStateListener = itemStateListener;
    }

    @Override // java.lang.Iterable
    public Iterator<String> iterator() {
        return this.mSelection.iterator();
    }

    public final void updateState() {
        ItemStateListener itemStateListener = this.mItemStateListener;
        if (itemStateListener == null) {
            return;
        }
        itemStateListener.onStateChanged();
    }

    public boolean isAllSelected() {
        BaseRecyclerAdapter baseRecyclerAdapter = this.mAdapter.get();
        int size = this.mSelection.size();
        return size > 0 && baseRecyclerAdapter != null && size == baseRecyclerAdapter.getItemCount();
    }

    public boolean isNoneSelected() {
        return this.mSelection.size() == 0;
    }
}
