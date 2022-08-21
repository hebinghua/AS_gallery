package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.data.DBItem;
import com.miui.gallery.data.DBOwnerSubUbiImage;

/* loaded from: classes.dex */
public class SyncOwnerSubUbiFromLocal extends SyncSubUbifocusFromLocalBase {
    public SyncOwnerSubUbiFromLocal(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, boolean z) {
        super(context, account, galleryExtendedAuthToken, z);
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public Uri getBaseUri() {
        return GalleryCloudUtils.OWNER_SUB_UBIFOCUS_URI;
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    /* renamed from: generateDBImage */
    public DBItem mo689generateDBImage(Cursor cursor) {
        return new DBOwnerSubUbiImage(cursor);
    }
}
