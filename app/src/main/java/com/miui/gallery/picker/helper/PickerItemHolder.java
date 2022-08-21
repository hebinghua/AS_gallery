package com.miui.gallery.picker.helper;

import android.database.Cursor;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.adapter.IBaseRecyclerAdapter;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.ScalableTouchDelegate;

/* loaded from: classes2.dex */
public class PickerItemHolder implements View.OnClickListener {
    public IBaseRecyclerAdapter<Cursor> mAdapter;
    public PickerItemCheckedListener mCheckListener;
    public int mPosition;

    public PickerItemHolder(View view, IBaseRecyclerAdapter<Cursor> iBaseRecyclerAdapter, PickerItemCheckedListener pickerItemCheckedListener) {
        View checkableView;
        this.mAdapter = iBaseRecyclerAdapter;
        this.mCheckListener = pickerItemCheckedListener;
        if (!(iBaseRecyclerAdapter instanceof CheckableAdapter) || (checkableView = ((CheckableAdapter) iBaseRecyclerAdapter).getCheckableView(view)) == null) {
            return;
        }
        checkableView.setOnClickListener(this);
        view.setTouchDelegate(new ScalableTouchDelegate(1.8f, view, checkableView));
    }

    public void setPosition(int i) {
        this.mPosition = i;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.mCheckListener != null) {
            LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_TAP_LIGHT);
            this.mCheckListener.onItemChecked(this.mAdapter.mo1558getItem(this.mPosition), view);
        }
    }

    public static void bindView(int i, View view, IBaseRecyclerAdapter<Cursor> iBaseRecyclerAdapter, PickerItemCheckedListener pickerItemCheckedListener) {
        PickerItemHolder pickerItemHolder = (PickerItemHolder) view.getTag(R.id.tag_picker_item_holder);
        if (pickerItemHolder == null) {
            pickerItemHolder = new PickerItemHolder(view, iBaseRecyclerAdapter, pickerItemCheckedListener);
            view.setTag(R.id.tag_picker_item_holder, pickerItemHolder);
        }
        pickerItemHolder.setPosition(i);
    }
}
