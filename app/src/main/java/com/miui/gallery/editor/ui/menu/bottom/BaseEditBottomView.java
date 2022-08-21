package com.miui.gallery.editor.ui.menu.bottom;

import android.content.Context;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.miui.gallery.editor.R$layout;

/* loaded from: classes2.dex */
public class BaseEditBottomView extends ConstraintLayout {
    public BaseEditBottomView(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        ViewGroup.inflate(context, R$layout.common_editor_apply_layout, this);
    }
}
