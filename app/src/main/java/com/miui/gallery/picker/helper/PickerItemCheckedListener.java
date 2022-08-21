package com.miui.gallery.picker.helper;

import android.database.Cursor;
import android.view.View;
import android.widget.Checkable;
import com.miui.gallery.R;
import com.miui.gallery.picker.albumdetail.IPickAlbumDetail;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ToastUtils;
import java.util.List;

/* loaded from: classes2.dex */
public class PickerItemCheckedListener {
    public IPickAlbumDetail mAlbumDetail;
    public Picker mPicker;

    public PickerItemCheckedListener(IPickAlbumDetail iPickAlbumDetail, Picker picker) {
        this.mPicker = picker;
        this.mAlbumDetail = iPickAlbumDetail;
    }

    public void onItemChecked(Cursor cursor, View view) {
        String genKeyFromCursor;
        List<String> burstGroupIds = this.mAlbumDetail.getBurstGroupIds(cursor);
        if (BaseMiscUtil.isValid(burstGroupIds)) {
            genKeyFromCursor = burstGroupIds.get(0);
            if (this.mPicker.contains(genKeyFromCursor)) {
                this.mPicker.removeAll(burstGroupIds);
            } else if (!this.mPicker.isFull()) {
                this.mPicker.pickAll(burstGroupIds);
            } else {
                ToastUtils.makeText(view.getContext(), view.getContext().getString(R.string.picker_full_format, Integer.valueOf(this.mPicker.capacity())));
            }
        } else {
            genKeyFromCursor = this.mAlbumDetail.genKeyFromCursor(cursor);
            if (this.mPicker.contains(genKeyFromCursor)) {
                this.mPicker.remove(genKeyFromCursor);
            } else if (!this.mPicker.isFull()) {
                this.mPicker.pick(genKeyFromCursor);
            } else {
                ToastUtils.makeText(view.getContext(), view.getContext().getString(R.string.picker_full_format, Integer.valueOf(this.mPicker.capacity())));
            }
        }
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(this.mPicker.contains(genKeyFromCursor));
        }
    }
}
