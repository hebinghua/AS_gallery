package com.miui.gallery.editor.photo.widgets;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import com.miui.gallery.editor.photo.utils.EditorMiscHelper;
import com.miui.gallery.editor.photo.widgets.glview.BitmapGestureGLView;

/* loaded from: classes2.dex */
public class ProtectiveBitmapGestureGLView extends BitmapGestureGLView {
    public ProtectiveBitmapGestureGLView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        EditorMiscHelper.configProtectiveArea(context, this.mBitmapGestureParamsHolder);
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        EditorMiscHelper.configProtectiveArea(getContext(), this.mBitmapGestureParamsHolder);
    }
}
