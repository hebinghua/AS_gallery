package com.miui.gallery.widget.editwrapper;

import android.view.ActionMode;
import java.util.List;

/* loaded from: classes2.dex */
public interface MultiChoiceModeListener extends ActionMode.Callback {
    default void onAllItemsCheckedStateChanged(ActionMode actionMode, boolean z) {
    }

    default void onConfirmMultiChoiceResult(List<Integer> list) {
    }

    default void onItemCheckedStateChanged(ActionMode actionMode, int i, long j, boolean z) {
    }

    default void statGroupItemsCheckedStateChanged(boolean z) {
    }
}
