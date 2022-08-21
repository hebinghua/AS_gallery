package com.miui.gallery.model;

import android.content.DialogInterface;
import android.database.Cursor;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.ui.DeletionTask;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.MiscUtil;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public abstract class BaseCloudDataSet extends CursorDataSet {
    public long mAlbumId;
    public String mAlbumName;

    @Override // com.miui.gallery.model.BaseDataSet
    public int doDelete(int i, BaseDataItem baseDataItem, boolean z) {
        return 0;
    }

    public BaseCloudDataSet(Cursor cursor, int i, long j, String str) {
        super(cursor, i);
        this.mAlbumId = -1L;
        this.mAlbumId = j;
        this.mAlbumName = str;
    }

    @Override // com.miui.gallery.model.BaseDataSet
    public AlertDialog delete(FragmentActivity fragmentActivity, int i, DeletionTask.OnDeletionCompleteListener onDeletionCompleteListener, DialogInterface.OnDismissListener onDismissListener) {
        BaseDataItem item = getItem(null, i);
        if (item != null) {
            if (item.isBurstItem() && item.getBurstGroup().size() > 0) {
                MediaAndAlbumOperations.delete(fragmentActivity, "DeleteMediaDialogFragment", onDeletionCompleteListener, onDismissListener, this.mAlbumId, this.mAlbumName, 25, true, 0, MiscUtil.ListToArray(item.getBurstKeys()));
            } else {
                MediaAndAlbumOperations.delete(fragmentActivity, "DeleteMediaDialogFragment", onDeletionCompleteListener, onDismissListener, this.mAlbumId, this.mAlbumName, 25, ((CloudItem) item).getId());
            }
        }
        return null;
    }

    @Override // com.miui.gallery.model.BaseDataSet
    public boolean addToAlbum(FragmentActivity fragmentActivity, int i, boolean z, boolean z2, MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener) {
        BaseDataItem item = getItem(null, i);
        if (item != null) {
            if (item.isBurstItem() && item.getBurstGroup().size() > 0) {
                MediaAndAlbumOperations.addToAlbum(fragmentActivity, onAddAlbumListener, ShareAlbumHelper.isOtherShareAlbumId(this.mAlbumId), z, z2, item.isVideo(), MiscUtil.ListToArray(item.getBurstKeys()));
            } else {
                MediaAndAlbumOperations.addToAlbum(fragmentActivity, onAddAlbumListener, ShareAlbumHelper.isOtherShareAlbumId(this.mAlbumId), z, z2, item.isVideo(), ((CloudItem) item).getId());
            }
        }
        return true;
    }

    @Override // com.miui.gallery.model.BaseDataSet
    public boolean removeFromSecret(FragmentActivity fragmentActivity, int i, MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
        BaseDataItem item = getItem(null, i);
        if (item != null) {
            if (!item.isBurstItem() || item.getBurstGroup().size() <= 0) {
                MediaAndAlbumOperations.removeFromSecretAlbum(fragmentActivity, onCompleteListener, ((CloudItem) item).getId());
            } else {
                MediaAndAlbumOperations.removeFromSecretAlbum(fragmentActivity, onCompleteListener, MiscUtil.ListToArray(item.getBurstKeys()));
            }
        }
        return true;
    }

    @Override // com.miui.gallery.model.BaseDataSet
    public boolean addToFavorites(FragmentActivity fragmentActivity, int i, MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
        CloudItem cloudItem = (CloudItem) getItem(null, i);
        if (cloudItem != null) {
            if (cloudItem.isBurstItem() && cloudItem.getBurstGroup() != null) {
                MediaAndAlbumOperations.addToFavoritesById(fragmentActivity, cloudItem.wrapAddToFavoritesListener(onCompleteListener), MiscUtil.ListToArray(cloudItem.getBurstKeys()));
            } else {
                MediaAndAlbumOperations.addToFavoritesById(fragmentActivity, cloudItem.wrapAddToFavoritesListener(onCompleteListener), cloudItem.getId());
            }
        }
        return true;
    }

    @Override // com.miui.gallery.model.BaseDataSet
    public boolean removeFromFavorites(FragmentActivity fragmentActivity, int i, MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
        CloudItem cloudItem = (CloudItem) getItem(null, i);
        if (cloudItem != null) {
            if (cloudItem.isBurstItem() && cloudItem.getBurstGroup() != null) {
                MediaAndAlbumOperations.removeFromFavoritesById(fragmentActivity, cloudItem.wrapRemoveFromFavoritesListener(onCompleteListener), MiscUtil.ListToArray(cloudItem.getBurstKeys()));
            } else {
                MediaAndAlbumOperations.removeFromFavoritesById(fragmentActivity, cloudItem.wrapRemoveFromFavoritesListener(onCompleteListener), cloudItem.getId());
            }
        }
        return true;
    }
}
