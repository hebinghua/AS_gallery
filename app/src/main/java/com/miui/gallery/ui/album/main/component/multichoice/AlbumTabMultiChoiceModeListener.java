package com.miui.gallery.ui.album.main.component.multichoice;

import android.util.ArrayMap;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import com.miui.gallery.R;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.ui.album.common.MultiChoiceModeDataProvider;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.AlbumTabContract$P;
import com.miui.gallery.ui.album.main.AlbumTabContract$V;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.widget.editwrapper.MultiChoiceModeListener;
import java.util.Collection;

/* loaded from: classes2.dex */
public class AlbumTabMultiChoiceModeListener<P extends AlbumTabContract$P> implements MultiChoiceModeListener {
    public int mCurrentValue;
    public MenuItem mDeleteAlbum;
    public MenuItem mDisableShowInPhotosTab;
    public MenuItem mHiddenAlbum;
    public MenuItem mMoveToOthersAlbum;
    public MultiChoiceModeDataProvider mMultiChoiceModeDataProvider;
    public MenuItem mRenameAlbum;
    public MenuItem mReplaceAlbumCover;
    public MenuItem mShowInPhotosTab;
    public AlbumTabContract$V<P> mView;
    public final int MASK_CAN_RENAME = 2;
    public final int MASK_CAN_HIDDEN = 4;
    public final int MASK_CAN_DELETE = 8;
    public final int MASK_CAN_SHOW_IN_PHOTOS_TAB = 16;
    public final int MASK_CAN_DISABLE_SHOW_IN_PHOTOS_TAB = 32;
    public final int MASK_CAN_MOVE_TO_OTHERS = 64;
    public final int MASK_CAN_REPLACE_ALBUM_COVER = 128;
    public ArrayMap<Long, Boolean> mSparseBooleanArray = new ArrayMap<>(4);
    public boolean isFirstChecked = true;

    @Override // android.view.ActionMode.Callback
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    public AlbumTabMultiChoiceModeListener(AlbumTabContract$V<P> albumTabContract$V) {
        this.mView = albumTabContract$V;
        this.mMultiChoiceModeDataProvider = new AlbumTabMultiChoiceModeDataProvider(albumTabContract$V);
    }

    @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
    public void onAllItemsCheckedStateChanged(ActionMode actionMode, boolean z) {
        if (!z) {
            onItemCheckedStateChange(null);
        } else {
            onItemCheckedStateChange(this.mMultiChoiceModeDataProvider.providerCurrentOperationAlbums());
        }
    }

    @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long j, boolean z) {
        if (z) {
            checkAlbumTypeAndToast(j);
        }
        onItemCheckedStateChange(this.mMultiChoiceModeDataProvider.providerCurrentOperationAlbums());
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
        removeMasks(2, 16, 32);
        addMasks(8, 64, 4, 128);
        for (Album album : collection) {
            if (album.isOtherShareAlbum()) {
                removeMasks(8, 64, 16, 32, 128);
            }
            if (album.isBabyAlbum() && !album.isOtherShareAlbum()) {
                removeMasks(4);
            }
            if (album.isSystemAlbum()) {
                removeMasks(4, 8, 64);
            }
        }
    }

    public final void commandSingleItem(Album album) {
        resetCurrentValue();
        if (album.isCameraAlbum() || album.isVirtualAlbum()) {
            addMasks(128);
            if (!album.isScreenshotsRecorders()) {
                return;
            }
            if (album.isShowedPhotosTabAlbum()) {
                addMasks(32);
            } else {
                addMasks(16);
            }
        } else if (album.isOtherShareAlbum()) {
            addMasks(4);
            if (!album.isBabyAlbum()) {
                return;
            }
            removeMasks(16, 32);
        } else {
            if (album.isShowedPhotosTabAlbum()) {
                addMasks(32);
            } else {
                addMasks(16);
            }
            if (album.isBabyAlbum()) {
                if (!album.isUserCreateAlbum()) {
                    addMasks(4);
                }
                addMasks(8);
                addMasks(128);
                addMasks(64);
            } else if (album.isScreenshotsAlbum()) {
                addMasks(128);
            } else {
                addMasks(8, 64, 2, 4, 128);
                if (album.isSystemAlbum() || album.isOtherShareAlbum() || album.isBabyAlbum() || album.albumUnwriteable() || album.isManualRenameRestricted()) {
                    removeMasks(2);
                }
                if (!album.isRawAlbum()) {
                    return;
                }
                removeMasks(4, 64);
            }
        }
    }

    public final void checkAlbumTypeAndToast(long j) {
        if (this.isFirstChecked) {
            this.mSparseBooleanArray.put(Long.valueOf(j), Boolean.TRUE);
            this.isFirstChecked = false;
            return;
        }
        BaseViewBean findDataById = this.mView.findDataById(j);
        if (!(findDataById instanceof CommonAlbumItemViewBean)) {
            return;
        }
        Album album = (Album) ((CommonAlbumItemViewBean) findDataById).getSource();
        if (this.mSparseBooleanArray.containsKey(Long.valueOf(j)) && this.mSparseBooleanArray.get(Long.valueOf(j)).booleanValue()) {
            return;
        }
        if (album.isScreenshotsAlbum() || album.isScreenshotsRecorders()) {
            ToastUtils.makeText(this.mView.getContext(), (int) R.string.drag_failed_because_screenshots_album_cant_sort);
            this.mSparseBooleanArray.put(Long.valueOf(j), Boolean.TRUE);
        } else if (album.isOtherShareAlbum()) {
            ToastUtils.makeText(this.mView.getContext(), (int) R.string.drag_failed_because_share_album_cant_sort);
            this.mSparseBooleanArray.put(Long.valueOf(j), Boolean.TRUE);
        } else if (!album.isBabyAlbum()) {
        } else {
            ToastUtils.makeText(this.mView.getContext(), (int) R.string.drag_failed_because_baby_album_cant_sort);
            this.mSparseBooleanArray.put(Long.valueOf(j), Boolean.TRUE);
        }
    }

    public final void removeMasks(int... iArr) {
        if (iArr == null) {
            return;
        }
        for (int i : iArr) {
            this.mCurrentValue = (~i) & this.mCurrentValue;
        }
    }

    public final void addMasks(int... iArr) {
        if (iArr == null) {
            return;
        }
        for (int i : iArr) {
            this.mCurrentValue = i | this.mCurrentValue;
        }
    }

    public final void resetCurrentValue() {
        this.mCurrentValue = 0;
    }

    public final boolean isContains(int i) {
        return (i & this.mCurrentValue) != 0;
    }

    @Override // android.view.ActionMode.Callback
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.album_page_menu, menu);
        this.mRenameAlbum = menu.findItem(R.id.menu_rename);
        this.mHiddenAlbum = menu.findItem(R.id.menu_hidden);
        this.mDeleteAlbum = menu.findItem(R.id.menu_delete);
        this.mShowInPhotosTab = menu.findItem(R.id.menu_show_in_photos_tab);
        this.mDisableShowInPhotosTab = menu.findItem(R.id.menu_disable_show_in_photos_tab);
        this.mMoveToOthersAlbum = menu.findItem(R.id.menu_move_to_other_albums);
        this.mReplaceAlbumCover = menu.findItem(R.id.menu_replace_album_cover);
        startChoiceMode();
        return true;
    }

    @Override // android.view.ActionMode.Callback
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if (this.mView.isInMoveMode()) {
            return true;
        }
        Collection<Album> providerCurrentOperationAlbums = this.mMultiChoiceModeDataProvider.providerCurrentOperationAlbums();
        if (providerCurrentOperationAlbums == null || providerCurrentOperationAlbums.size() == 0) {
            return false;
        }
        if (menuItem.getItemId() != 16908313 && menuItem.getItemId() != 16908314) {
            LinearMotorHelper.performHapticFeedback(this.mView.getContext(), LinearMotorHelper.HAPTIC_TAP_LIGHT);
        }
        switch (menuItem.getItemId()) {
            case R.id.menu_delete /* 2131362885 */:
                this.mView.doDeleteAlbums(providerCurrentOperationAlbums);
                break;
            case R.id.menu_disable_show_in_photos_tab /* 2131362891 */:
                this.mView.doChangeAlbumsShowInPhotosTab(false, providerCurrentOperationAlbums);
                break;
            case R.id.menu_hidden /* 2131362894 */:
                this.mView.doHideAlbums(providerCurrentOperationAlbums);
                break;
            case R.id.menu_move_to_other_albums /* 2131362901 */:
                this.mView.doAlbumMoveToOtherAlbums(providerCurrentOperationAlbums);
                break;
            case R.id.menu_rename /* 2131362911 */:
                Album next = providerCurrentOperationAlbums.iterator().next();
                if (next != null) {
                    this.mView.doRenameAlbum(next);
                    break;
                }
                break;
            case R.id.menu_replace_album_cover /* 2131362913 */:
                this.mView.startReplaceAlbumCover(providerCurrentOperationAlbums);
                break;
            case R.id.menu_show_in_photos_tab /* 2131362920 */:
                this.mView.doChangeAlbumsShowInPhotosTab(true, providerCurrentOperationAlbums);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override // android.view.ActionMode.Callback
    public void onDestroyActionMode(ActionMode actionMode) {
        stopChoiceMode();
        this.mSparseBooleanArray.clear();
        this.isFirstChecked = true;
    }

    public final void updateMenuState() {
        if (this.mMultiChoiceModeDataProvider.providerCheckedCount() < 1) {
            onNoCheckItem();
            return;
        }
        this.mRenameAlbum.setEnabled(isContains(2));
        this.mHiddenAlbum.setEnabled(isContains(4));
        this.mDeleteAlbum.setEnabled(isContains(8));
        this.mShowInPhotosTab.setEnabled(false);
        this.mDisableShowInPhotosTab.setEnabled(false);
        if (isContains(16)) {
            this.mShowInPhotosTab.setEnabled(true);
            this.mShowInPhotosTab.setVisible(true);
            this.mDisableShowInPhotosTab.setVisible(false);
        } else if (isContains(32)) {
            this.mDisableShowInPhotosTab.setVisible(true);
            this.mDisableShowInPhotosTab.setEnabled(true);
            this.mShowInPhotosTab.setVisible(false);
        }
        this.mMoveToOthersAlbum.setEnabled(isContains(64));
        this.mReplaceAlbumCover.setEnabled(isContains(128));
    }

    public final void onNoCheckItem() {
        disableMenuItem(this.mRenameAlbum, this.mHiddenAlbum, this.mDeleteAlbum, this.mShowInPhotosTab, this.mDisableShowInPhotosTab, this.mMoveToOthersAlbum, this.mReplaceAlbumCover);
    }

    public final void disableMenuItem(MenuItem... menuItemArr) {
        if (menuItemArr == null) {
            return;
        }
        for (MenuItem menuItem : menuItemArr) {
            menuItem.setEnabled(false);
        }
    }

    public final void startChoiceMode() {
        AlbumTabContract$V<P> albumTabContract$V = this.mView;
        if (albumTabContract$V != null) {
            albumTabContract$V.onStartChoiceMode();
        }
    }

    public final void stopChoiceMode() {
        AlbumTabContract$V<P> albumTabContract$V = this.mView;
        if (albumTabContract$V != null) {
            albumTabContract$V.onStopChoiceMode();
        }
    }

    public boolean isDeleteEnable() {
        MenuItem menuItem = this.mDeleteAlbum;
        return menuItem != null && menuItem.isEnabled();
    }

    public void doDelete() {
        this.mView.doDeleteAlbums(this.mMultiChoiceModeDataProvider.providerCurrentOperationAlbums());
    }
}
