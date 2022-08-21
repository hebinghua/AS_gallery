package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.operation.EditShareAlbum;
import com.miui.gallery.data.DBItem;
import com.miui.gallery.data.DBShareAlbum;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class SyncShareAlbumFromLocal extends SyncFromLocalBase {
    public List<RequestSharerAlbumItem> mEditedItems;

    public SyncShareAlbumFromLocal(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        super(context, account, galleryExtendedAuthToken);
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public Uri getBaseUri() {
        return GalleryCloudUtils.SHARE_ALBUM_URI;
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public String getSelectionClause() {
        return String.format(" (%s) ", CloudUtils.SELECTION_NOT_SYNCED_OR_EDITED);
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    /* renamed from: generateDBImage */
    public DBItem mo689generateDBImage(Cursor cursor) {
        return new DBShareAlbum(cursor);
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public void initRequestCloudItemList() {
        this.mEditedItems = new ArrayList();
    }

    public final boolean shouldSyncEdit(DBShareAlbum dBShareAlbum) {
        if (dBShareAlbum.getLocalFlag() == 0) {
            String editedColumns = dBShareAlbum.getEditedColumns();
            return !TextUtils.isEmpty(editedColumns) && editedColumns.contains(GalleryCloudUtils.transformToEditedColumnsElement(25));
        }
        return false;
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public void putToRequestCloudItemList(DBItem dBItem) {
        DBShareAlbum dBShareAlbum = (DBShareAlbum) dBItem;
        if (shouldSyncEdit(dBShareAlbum)) {
            this.mEditedItems.add(new RequestSharerAlbumItem(0, dBShareAlbum));
        }
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public void handleRequestCloudItemList() throws Exception {
        if (this.mEditedItems.size() > 0) {
            RetryOperation.doOperation(this.mContext, this.mAccount, this.mExtendedAuthToken, this.mEditedItems, new EditShareAlbum(this.mContext));
        }
    }
}
