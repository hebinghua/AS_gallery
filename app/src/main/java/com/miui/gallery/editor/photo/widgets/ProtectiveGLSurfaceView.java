package com.miui.gallery.editor.photo.widgets;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import com.miui.gallery.editor.photo.utils.EditorMiscHelper;

/* loaded from: classes2.dex */
public class ProtectiveGLSurfaceView extends GLSurfaceView {
    public ProtectiveGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
        EditorMiscHelper.configProtectiveArea(this);
    }
}
