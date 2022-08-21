package com.miui.gallery.widget.recyclerview;

import androidx.recyclerview.widget.GridLayoutManager;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;

/* loaded from: classes3.dex */
public class EditableListSpanSizeProvider implements SpanSizeProvider {
    public final EditableListViewWrapper mEditableWrapper;
    public final GridLayoutManager mLayoutManager;

    public EditableListSpanSizeProvider(EditableListViewWrapper editableListViewWrapper, GridLayoutManager gridLayoutManager) {
        this.mEditableWrapper = editableListViewWrapper;
        this.mLayoutManager = gridLayoutManager;
    }

    @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
    public int getSpanSize(int i) {
        if (this.mLayoutManager.getSpanCount() == 1) {
            return 1;
        }
        return this.mEditableWrapper.getSpanSize(i, this.mLayoutManager.getSpanCount());
    }

    @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
    public int getSpanIndex(int i, int i2) {
        if (this.mLayoutManager.getSpanCount() == 1) {
            return 0;
        }
        return this.mEditableWrapper.getSpanIndex(i, this.mLayoutManager.getSpanCount());
    }
}
