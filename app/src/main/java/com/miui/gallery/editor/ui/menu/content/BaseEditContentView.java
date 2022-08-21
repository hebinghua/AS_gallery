package com.miui.gallery.editor.ui.menu.content;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;

/* loaded from: classes2.dex */
public abstract class BaseEditContentView extends ConstraintLayout {
    public abstract void inflateContentView(Context context);

    public BaseEditContentView(Context context) {
        super(context);
        init(context);
    }

    public final void init(Context context) {
        inflateContentView(context);
    }
}
