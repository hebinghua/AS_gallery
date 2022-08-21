package com.miui.gallery.picker.helper;

import android.database.Cursor;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.miui.gallery.adapter.CursorRecyclerAdapterWrapper;
import com.miui.gallery.adapter.IBaseRecyclerAdapter;
import com.miui.gallery.adapter.ICursorAdapter;
import com.miui.gallery.picker.albumdetail.IPickAlbumDetail;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.ui.Checkable;
import com.miui.gallery.ui.MicroThumbGridItem;
import java.util.List;

/* loaded from: classes2.dex */
public class PickableSimpleAdapterWrapper<VH extends RecyclerView.ViewHolder, T extends RecyclerView.Adapter<VH> & ICursorAdapter & IBaseRecyclerAdapter<Cursor>> extends CursorRecyclerAdapterWrapper<VH, T> {
    public IPickAlbumDetail mAlbumDetail;
    public Picker mPicker;
    public PickerItemCheckedListener mPickerItemCheckedListener;

    /* JADX WARN: Incorrect types in method signature: (Lcom/miui/gallery/picker/albumdetail/IPickAlbumDetail;Lcom/miui/gallery/picker/helper/Picker;TT;)V */
    public PickableSimpleAdapterWrapper(IPickAlbumDetail iPickAlbumDetail, Picker picker, RecyclerView.Adapter adapter) {
        super(adapter);
        this.mAlbumDetail = iPickAlbumDetail;
        this.mPicker = picker;
        this.mPickerItemCheckedListener = new PickerItemCheckedListener(iPickAlbumDetail, picker);
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(VH vh, int i) {
        super.onBindViewHolder(vh, i);
        bindCheckable(vh, i);
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(VH vh, int i, List<Object> list) {
        super.onBindViewHolder(vh, i, list);
        bindCheckable(vh, i);
    }

    public final void bindCheckable(RecyclerView.ViewHolder viewHolder, int i) {
        View view = viewHolder.itemView;
        if (this.mPicker.getMode() == Picker.Mode.SINGLE) {
            if (view instanceof MicroThumbGridItem) {
                ((MicroThumbGridItem) view).setSimilarMarkEnable(true);
            }
        } else if (view instanceof Checkable) {
            Checkable checkable = (Checkable) view;
            checkable.setCheckable(true);
            Cursor cursor = ((ICursorAdapter) this.mWrapped).getCursor();
            cursor.moveToPosition(i);
            checkable.setChecked(this.mPicker.contains(this.mAlbumDetail.genKeyFromCursor(cursor)));
        }
        PickerItemHolder.bindView(i, view, (IBaseRecyclerAdapter) this.mWrapped, this.mPickerItemCheckedListener);
    }
}
