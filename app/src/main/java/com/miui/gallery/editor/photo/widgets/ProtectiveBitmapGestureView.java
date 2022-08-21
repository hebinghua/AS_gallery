package com.miui.gallery.editor.photo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import com.miui.gallery.editor.photo.utils.EditorMiscHelper;
import com.miui.gallery.widget.imageview.BitmapGestureView;

/* loaded from: classes2.dex */
public class ProtectiveBitmapGestureView extends BitmapGestureView {
    public ProtectiveBitmapGestureView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ProtectiveBitmapGestureView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
        EditorMiscHelper.configProtectiveArea(this);
    }
}
