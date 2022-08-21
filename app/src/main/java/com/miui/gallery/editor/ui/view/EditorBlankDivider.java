package com.miui.gallery.editor.ui.view;

import android.content.Context;
import android.content.res.Resources;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.widget.OrientationProvider;
import com.miui.gallery.widget.recyclerview.BlankDivider;

/* loaded from: classes2.dex */
public class EditorBlankDivider extends BlankDivider {
    public EditorBlankDivider(Resources resources, int i) {
        this(i == 0 ? 0 : resources.getDimensionPixelSize(i));
    }

    public EditorBlankDivider(int i) {
        this(i, i, i, 0, 0);
    }

    public EditorBlankDivider(int i, int i2, int i3, int i4, int i5) {
        super(i, i2, i3, i4, i5);
        setOrientationProvider(new OrientationProvider() { // from class: com.miui.gallery.editor.ui.view.EditorBlankDivider.1
            @Override // com.miui.gallery.widget.OrientationProvider
            public boolean isPortrait(Context context) {
                return EditorOrientationHelper.isLayoutPortrait(context);
            }
        });
    }
}
