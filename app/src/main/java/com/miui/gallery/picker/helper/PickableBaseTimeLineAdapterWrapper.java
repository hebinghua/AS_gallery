package com.miui.gallery.picker.helper;

import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.miui.gallery.adapter.IBaseRecyclerAdapter;
import com.miui.gallery.adapter.ICursorAdapter;
import com.miui.gallery.picker.albumdetail.IPickAlbumDetail;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.ui.Checkable;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.miui.gallery.widget.recyclerview.GroupedItemAdapter;
import java.util.List;

/* loaded from: classes2.dex */
public class PickableBaseTimeLineAdapterWrapper<T extends RecyclerView.Adapter<BaseViewHolder> & ICursorAdapter & IBaseRecyclerAdapter<Cursor>> extends PickableSimpleAdapterWrapper<BaseViewHolder, T> implements GroupedItemAdapter<BaseViewHolder, BaseViewHolder> {
    public GroupedItemAdapter<BaseViewHolder, BaseViewHolder> mBaseAdapter;

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public /* bridge */ /* synthetic */ void onBindChildViewHolder(RecyclerView.ViewHolder viewHolder, int i, int i2, int i3, List list) {
        onBindChildViewHolder((BaseViewHolder) viewHolder, i, i2, i3, (List<Object>) list);
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public /* bridge */ /* synthetic */ void onBindGroupViewHolder(RecyclerView.ViewHolder viewHolder, int i, int i2, List list) {
        onBindGroupViewHolder((BaseViewHolder) viewHolder, i, i2, (List<Object>) list);
    }

    /* JADX WARN: Incorrect types in method signature: (Lcom/miui/gallery/picker/albumdetail/IPickAlbumDetail;Lcom/miui/gallery/picker/helper/Picker;TT;)V */
    public PickableBaseTimeLineAdapterWrapper(IPickAlbumDetail iPickAlbumDetail, Picker picker, RecyclerView.Adapter adapter) {
        super(iPickAlbumDetail, picker, adapter);
        if (adapter instanceof GroupedItemAdapter) {
            this.mBaseAdapter = (GroupedItemAdapter) adapter;
            return;
        }
        throw new IllegalArgumentException("Adapter must be a GroupedItemAdapter");
    }

    public GroupedItemAdapter<BaseViewHolder, BaseViewHolder> getBaseAdapter() {
        return this.mBaseAdapter;
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public boolean isFlatten() {
        return this.mBaseAdapter.isFlatten();
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public int getGroupCount() {
        return this.mBaseAdapter.getGroupCount();
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public int getChildCount(int i) {
        return this.mBaseAdapter.getChildCount(i);
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public long getGroupId(int i) {
        return this.mBaseAdapter.getGroupId(i);
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public long getChildId(int i, int i2) {
        return this.mBaseAdapter.getChildId(i, i2);
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public int getGroupItemViewType(int i) {
        return this.mBaseAdapter.getGroupItemViewType(i);
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public int getChildItemViewType(int i, int i2) {
        return this.mBaseAdapter.getChildItemViewType(i, i2);
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    /* renamed from: onCreateGroupViewHolder  reason: collision with other method in class */
    public BaseViewHolder mo1338onCreateGroupViewHolder(ViewGroup viewGroup, int i) {
        return this.mBaseAdapter.mo1338onCreateGroupViewHolder(viewGroup, i);
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    /* renamed from: onCreateChildViewHolder  reason: collision with other method in class */
    public BaseViewHolder mo1337onCreateChildViewHolder(ViewGroup viewGroup, int i) {
        return this.mBaseAdapter.mo1337onCreateChildViewHolder(viewGroup, i);
    }

    public void onBindGroupViewHolder(BaseViewHolder baseViewHolder, int i, int i2, List<Object> list) {
        this.mBaseAdapter.onBindGroupViewHolder(baseViewHolder, i, i2, list);
    }

    public void onBindChildViewHolder(BaseViewHolder baseViewHolder, int i, int i2, int i3, List<Object> list) {
        this.mBaseAdapter.onBindChildViewHolder(baseViewHolder, i, i2, i3, list);
        bindCheckable(baseViewHolder, i, i2);
    }

    public final void bindCheckable(BaseViewHolder baseViewHolder, int i, int i2) {
        View view = baseViewHolder.itemView;
        int packDataPosition = this.mBaseAdapter.packDataPosition(i, i2);
        if (this.mPicker.getMode() == Picker.Mode.SINGLE) {
            if (view instanceof MicroThumbGridItem) {
                ((MicroThumbGridItem) view).setSimilarMarkEnable(true);
            }
        } else if (view instanceof Checkable) {
            Cursor cursor = ((ICursorAdapter) this.mWrapped).getCursor();
            cursor.moveToPosition(packDataPosition);
            if (!this.mAlbumDetail.bindCheckState(view, cursor)) {
                Checkable checkable = (Checkable) view;
                checkable.setCheckable(true);
                checkable.setChecked(this.mPicker.contains(this.mAlbumDetail.genKeyFromCursor(cursor)));
            }
        }
        PickerItemHolder.bindView(packDataPosition, view, (IBaseRecyclerAdapter) this.mWrapped, this.mPickerItemCheckedListener);
        FolmeUtil.setDefaultTouchAnim(view, null, false);
    }
}
