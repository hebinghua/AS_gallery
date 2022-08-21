package com.miui.gallery.ui.album.otheralbum;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.MultiChoiceModeDataProvider;
import com.miui.gallery.ui.album.otheralbum.OtherAlbumContract$P;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.widget.editwrapper.MultiChoiceModeListener;
import java.util.Collection;

/* loaded from: classes2.dex */
public class OtherAlbumPageMultiChoiceModeListener<P extends OtherAlbumContract$P> implements MultiChoiceModeListener {
    public int mCurrentValue;
    public MultiChoiceModeDataProvider mDataProvider;
    public MenuItem mDeleteAlbum;
    public MenuItem mMoveAlbum;
    public MenuItem mRenameAlbum;
    public OtherAlbumContract$V<P> mView;
    public final int MASK_CAN_RENAME = 2;
    public final int MASK_CAN_DELETE = 8;
    public final int MASK_CAN_REMOVE_FROM_OTHER = 16;
    public final int MASK_CAN_MOVE_TO_RUBBISH = 32;

    @Override // android.view.ActionMode.Callback
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    public OtherAlbumPageMultiChoiceModeListener(OtherAlbumContract$V<P> otherAlbumContract$V, MultiChoiceModeDataProvider multiChoiceModeDataProvider) {
        this.mView = otherAlbumContract$V;
        this.mDataProvider = multiChoiceModeDataProvider;
    }

    @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
    public void onAllItemsCheckedStateChanged(ActionMode actionMode, boolean z) {
        TrackController.trackClick("403.40.2.1.11126", "403.40.2.1.11129");
        if (!z) {
            onItemCheckedStateChange(null);
        } else {
            onItemCheckedStateChange(this.mDataProvider.providerCurrentOperationAlbums());
        }
    }

    @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long j, boolean z) {
        onItemCheckedStateChange(this.mDataProvider.providerCurrentOperationAlbums());
    }

    public final void onItemCheckedStateChange(Collection<Album> collection) {
        if (collection == null) {
            resetCurrentValue();
        } else if (collection.size() > 1) {
            commandMultiSelection(collection);
        } else if (collection.size() == 1) {
            commandSingleItem(collection.iterator().next());
        }
        updateMenuState();
    }

    public final void commandMultiSelection(Collection<Album> collection) {
        int i = this.mCurrentValue & (-3);
        this.mCurrentValue = i;
        int i2 = i | 8;
        this.mCurrentValue = i2;
        int i3 = i2 | 16;
        this.mCurrentValue = i3;
        this.mCurrentValue = i3 | 32;
        for (Album album : collection) {
            if (album != null && (album.isUserCreateAlbum() || album.isMustVisibleAlbum())) {
                if (isContains(32)) {
                    this.mCurrentValue &= -33;
                    return;
                }
            }
        }
    }

    public final void commandSingleItem(Album album) {
        resetCurrentValue();
        if (album.isBabyAlbum()) {
            int i = this.mCurrentValue | 8;
            this.mCurrentValue = i;
            this.mCurrentValue = i | 16;
            return;
        }
        int i2 = this.mCurrentValue | 8;
        this.mCurrentValue = i2;
        int i3 = i2 | 2;
        this.mCurrentValue = i3;
        int i4 = i3 | 32;
        this.mCurrentValue = i4;
        this.mCurrentValue = i4 | 16;
        if (album.isUserCreateAlbum() || album.isMustVisibleAlbum()) {
            this.mCurrentValue &= -33;
        }
        if (!album.isSystemAlbum() && !album.isOtherShareAlbum() && !album.isBabyAlbum() && !album.albumUnwriteable() && !album.isManualRenameRestricted()) {
            return;
        }
        this.mCurrentValue &= -3;
    }

    public final void resetCurrentValue() {
        this.mCurrentValue = 0;
    }

    public final boolean isContains(int i) {
        return (i & this.mCurrentValue) != 0;
    }

    @Override // android.view.ActionMode.Callback
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.other_album_page_menu, menu);
        this.mRenameAlbum = menu.findItem(R.id.menu_rename);
        this.mDeleteAlbum = menu.findItem(R.id.menu_delete);
        this.mMoveAlbum = menu.findItem(R.id.menu_move);
        startChoiceMode();
        return true;
    }

    @Override // android.view.ActionMode.Callback
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if (menuItem.getItemId() != 16908313 && menuItem.getItemId() != 16908314) {
            LinearMotorHelper.performHapticFeedback(this.mView.getContext(), LinearMotorHelper.HAPTIC_TAP_LIGHT);
        }
        Collection<Album> providerCurrentOperationAlbums = this.mDataProvider.providerCurrentOperationAlbums();
        boolean z = false;
        if (providerCurrentOperationAlbums == null || providerCurrentOperationAlbums.size() == 0) {
            return false;
        }
        int itemId = menuItem.getItemId();
        if (itemId == R.id.menu_delete) {
            this.mView.doDeleteAlbums(providerCurrentOperationAlbums);
            TrackController.trackClick("403.40.2.1.11125", "403.40.2.1.11129");
        } else if (itemId == R.id.menu_move) {
            OtherAlbumContract$V<P> otherAlbumContract$V = this.mView;
            if ((this.mCurrentValue & 32) != 0) {
                z = true;
            }
            otherAlbumContract$V.showMoveOperations(providerCurrentOperationAlbums, z);
        } else if (itemId != R.id.menu_rename) {
            return false;
        } else {
            Album next = providerCurrentOperationAlbums.iterator().next();
            if (next != null) {
                this.mView.doRenameAlbum(next);
            }
        }
        return true;
    }

    @Override // android.view.ActionMode.Callback
    public void onDestroyActionMode(ActionMode actionMode) {
        stopChoiceMode();
    }

    public final void updateMenuState() {
        if (this.mDataProvider.providerCheckedCount() < 1) {
            onNoCheckItem();
            return;
        }
        this.mRenameAlbum.setEnabled(isContains(2));
        this.mMoveAlbum.setEnabled(true);
        this.mDeleteAlbum.setEnabled(isContains(8));
    }

    public final void onNoCheckItem() {
        enableMenuItem(false, this.mRenameAlbum, this.mMoveAlbum, this.mDeleteAlbum);
    }

    public final void enableMenuItem(boolean z, MenuItem... menuItemArr) {
        if (menuItemArr == null) {
            return;
        }
        for (MenuItem menuItem : menuItemArr) {
            menuItem.setEnabled(z);
        }
    }

    public final void startChoiceMode() {
        OtherAlbumContract$V<P> otherAlbumContract$V = this.mView;
        if (otherAlbumContract$V != null) {
            otherAlbumContract$V.startChoiceMode();
        }
    }

    public final void stopChoiceMode() {
        OtherAlbumContract$V<P> otherAlbumContract$V = this.mView;
        if (otherAlbumContract$V != null) {
            otherAlbumContract$V.stopChoiceMode();
        }
    }
}
