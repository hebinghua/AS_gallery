package com.miui.gallery.widget.editwrapper;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

/* loaded from: classes2.dex */
public class SimpleMultiChoiceModeListener implements MultiChoiceModeListener {
    @Override // android.view.ActionMode.Callback
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }

    @Override // android.view.ActionMode.Callback
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override // android.view.ActionMode.Callback
    public void onDestroyActionMode(ActionMode actionMode) {
    }

    @Override // android.view.ActionMode.Callback
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }
}
