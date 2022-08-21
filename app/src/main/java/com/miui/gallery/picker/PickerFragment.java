package com.miui.gallery.picker;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.picker.helper.CursorUtils;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.util.StringUtils;
import java.util.Locale;

/* loaded from: classes2.dex */
public abstract class PickerFragment extends PickerCompatFragment implements PickerImpl {
    public int mFastScrollerMarginTop;
    public Picker mPicker;

    public String getPreviewOrder() {
        return null;
    }

    public String[] getPreviewSelectionArgs(Cursor cursor) {
        return null;
    }

    public abstract Uri getUri();

    public boolean isPreviewFace() {
        return false;
    }

    public abstract boolean supportFoldBurstItems();

    @Override // com.miui.gallery.picker.PickerCompatFragment, com.miui.gallery.picker.PickerBaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mFastScrollerMarginTop = getResources().getDimensionPixelOffset(R.dimen.time_line_header_height) + getResources().getDimensionPixelSize(R.dimen.fast_scroller_margin_top_to_time_line);
    }

    @Override // com.miui.gallery.picker.PickerImpl
    public final void attach(Picker picker) {
        this.mPicker = picker;
    }

    public final Picker getPicker() {
        return this.mPicker;
    }

    public Uri getPreviewUri() {
        return getUri();
    }

    public String getPreviewSelection(Cursor cursor) {
        return String.format(Locale.US, "_id = %d", Long.valueOf(CursorUtils.getId(cursor)));
    }

    public String getFilterSelectionWithMimeType(String[] strArr) {
        return !StringUtils.isValid(strArr) ? "" : String.format("%s NOT IN ('%s')", "mimeType", TextUtils.join("','", strArr));
    }

    public long getKey(Cursor cursor) {
        return CursorUtils.getId(cursor);
    }

    public String getLocalPath(Cursor cursor) {
        return CursorUtils.getMicroPath(cursor);
    }

    public long getFileLength(Cursor cursor) {
        return CursorUtils.getFileLength(cursor);
    }
}
