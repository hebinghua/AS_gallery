package com.miui.gallery.picker.albumdetail;

import android.app.Activity;
import android.database.Cursor;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.miui.gallery.adapter.BaseRecyclerAdapter;
import com.miui.gallery.picker.helper.CursorUtils;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import com.miui.security.CrossUserCompatActivity;
import java.util.List;

/* loaded from: classes2.dex */
public interface IPickAlbumDetail {
    default boolean bindCheckState(View view, Cursor cursor) {
        return false;
    }

    default Activity getPickerActivity() {
        return null;
    }

    default void gotoPhotoPageFromPicker(RecyclerView recyclerView, View view, int i) {
    }

    default boolean onPhotoItemClick(Cursor cursor, View view) {
        return false;
    }

    default void pick(Picker picker, String str) {
    }

    default void remove(Picker picker, String str) {
    }

    int unwrapPosition(int i);

    default String genKeyFromCursor(Cursor cursor) {
        return String.valueOf(CursorUtils.getId(cursor));
    }

    default ItemClickSupport.OnItemClickListener getGridViewOnItemClickListener() {
        return new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.picker.albumdetail.IPickAlbumDetail.1
            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                int unwrapPosition;
                Activity pickerActivity = IPickAlbumDetail.this.getPickerActivity();
                if (pickerActivity == null) {
                    return false;
                }
                if (((pickerActivity instanceof CrossUserCompatActivity) && ((CrossUserCompatActivity) pickerActivity).isCrossUserPick()) || (unwrapPosition = IPickAlbumDetail.this.unwrapPosition(i)) == -1) {
                    return false;
                }
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                while (adapter instanceof AbstractHeaderFooterWrapperAdapter) {
                    adapter = ((AbstractHeaderFooterWrapperAdapter) adapter).getWrappedAdapter();
                }
                BaseRecyclerAdapter baseRecyclerAdapter = (BaseRecyclerAdapter) WrapperAdapterUtils.findWrappedAdapter(adapter, BaseRecyclerAdapter.class);
                if (baseRecyclerAdapter == null) {
                    DefaultLogger.w(IPickAlbumDetail.class.getSimpleName(), "adapter shouldn't be null");
                    return false;
                }
                Cursor cursor = (Cursor) baseRecyclerAdapter.mo1558getItem(unwrapPosition);
                if (cursor == null || IPickAlbumDetail.this.onPhotoItemClick(cursor, view)) {
                    return false;
                }
                IPickAlbumDetail.this.gotoPhotoPageFromPicker(recyclerView, view, unwrapPosition);
                return true;
            }
        };
    }

    default List<String> getBurstGroupIds(Cursor cursor) {
        return CursorUtils.getBurstGroupIds(cursor);
    }
}
