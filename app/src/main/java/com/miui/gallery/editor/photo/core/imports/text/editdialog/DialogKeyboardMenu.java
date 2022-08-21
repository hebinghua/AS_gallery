package com.miui.gallery.editor.photo.core.imports.text.editdialog;

import android.content.Context;
import android.view.ViewGroup;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class DialogKeyboardMenu extends DialogSubMenu {
    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenu
    public ViewGroup initSubMenuView(Context context, ViewGroup viewGroup) {
        return null;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenu
    public void initializeData(Object obj) {
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenu
    public /* bridge */ /* synthetic */ void OnConfigurationChanged() {
        super.OnConfigurationChanged();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenu
    public /* bridge */ /* synthetic */ void release() {
        super.release();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenu
    public /* bridge */ /* synthetic */ void setChecked(boolean z) {
        super.setChecked(z);
    }

    public DialogKeyboardMenu(Context context) {
        super(context, null, R.string.text_edit_dialog_keyboard, R.drawable.text_edit_dialog_tab_icon_keyboard);
    }
}
