package com.miui.gallery.editor.photo.app.menu;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class Remover2View extends RemoverView {
    public Remover2View(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.photo.widgets.PhotoEditorMenuView, com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initBottomView(FrameLayout frameLayout) {
        return FrameLayout.inflate(getContext(), R.layout.photo_editor_menu_title_bottom_layout, null);
    }
}
