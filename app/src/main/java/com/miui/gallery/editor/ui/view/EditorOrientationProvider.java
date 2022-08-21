package com.miui.gallery.editor.ui.view;

import android.content.Context;
import androidx.annotation.Keep;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.widget.OrientationProvider;

@Keep
/* loaded from: classes2.dex */
public class EditorOrientationProvider implements OrientationProvider {
    @Override // com.miui.gallery.widget.OrientationProvider
    public boolean isPortrait(Context context) {
        return EditorOrientationHelper.isLayoutPortrait(context);
    }
}
