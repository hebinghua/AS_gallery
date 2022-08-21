package com.miui.gallery.picker.albumdetail;

import android.database.Cursor;
import android.view.View;
import android.widget.Checkable;
import com.miui.gallery.R;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.picker.helper.PickerItemCheckedListener;
import com.miui.gallery.util.ToastUtils;

/* loaded from: classes2.dex */
public class AlbumItemCheckListener extends PickerItemCheckedListener {
    public Picker picker;

    public AlbumItemCheckListener(IPickAlbumDetail iPickAlbumDetail, Picker picker) {
        super(iPickAlbumDetail, picker);
        this.picker = picker;
    }

    @Override // com.miui.gallery.picker.helper.PickerItemCheckedListener
    public void onItemChecked(Cursor cursor, View view) {
        String genKeyFromCursor = this.mAlbumDetail.genKeyFromCursor(cursor);
        if (this.picker.contains(genKeyFromCursor)) {
            this.mAlbumDetail.remove(this.picker, genKeyFromCursor);
        } else if (!this.picker.isFull()) {
            this.mAlbumDetail.pick(this.picker, genKeyFromCursor);
        } else {
            ToastUtils.makeText(view.getContext(), view.getContext().getString(R.string.picker_full_format, Integer.valueOf(this.picker.capacity())));
        }
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(this.picker.contains(genKeyFromCursor));
        }
    }
}
