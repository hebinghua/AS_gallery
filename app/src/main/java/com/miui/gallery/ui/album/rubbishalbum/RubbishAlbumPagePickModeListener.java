package com.miui.gallery.ui.album.rubbishalbum;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumContract$P;
import com.miui.gallery.widget.editwrapper.MultiChoiceModeListener;
import java.util.List;

/* loaded from: classes2.dex */
public class RubbishAlbumPagePickModeListener<P extends RubbishAlbumContract$P> implements MultiChoiceModeListener {
    public RubbishAlbumContract$V<P> mView;

    @Override // android.view.ActionMode.Callback
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }

    @Override // android.view.ActionMode.Callback
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    public RubbishAlbumPagePickModeListener(RubbishAlbumContract$V<P> rubbishAlbumContract$V) {
        this.mView = rubbishAlbumContract$V;
    }

    @Override // android.view.ActionMode.Callback
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        startChoiceMode();
        return true;
    }

    @Override // android.view.ActionMode.Callback
    public void onDestroyActionMode(ActionMode actionMode) {
        stopChoiceMode();
    }

    public final void startChoiceMode() {
        RubbishAlbumContract$V<P> rubbishAlbumContract$V = this.mView;
        if (rubbishAlbumContract$V != null) {
            rubbishAlbumContract$V.startChoiceMode();
        }
    }

    public final void stopChoiceMode() {
        RubbishAlbumContract$V<P> rubbishAlbumContract$V = this.mView;
        if (rubbishAlbumContract$V != null) {
            rubbishAlbumContract$V.stopChoiceMode();
        }
    }

    @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
    public void onConfirmMultiChoiceResult(List<Integer> list) {
        RubbishAlbumContract$V<P> rubbishAlbumContract$V = this.mView;
        if (rubbishAlbumContract$V != null) {
            rubbishAlbumContract$V.doAddRemoveNoMediaForRubbishAlbum(list);
        }
    }
}
