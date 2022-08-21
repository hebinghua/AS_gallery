package com.miui.gallery.editor.photo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.miui.gallery.editor.photo.utils.EditorMiscHelper;

/* loaded from: classes2.dex */
public class ProtectiveImageView extends ImageView {
    public ProtectiveImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ProtectiveImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
        EditorMiscHelper.configProtectiveArea(this);
    }
}
