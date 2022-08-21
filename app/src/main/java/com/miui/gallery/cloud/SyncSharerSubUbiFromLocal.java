package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.data.DBItem;
import com.miui.gallery.data.DBShareSubUbiImage;

/* loaded from: classes.dex */
public class SyncSharerSubUbiFromLocal extends SyncSubUbifocusFromLocalBase {
    public SyncSharerSubUbiFromLocal(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        super(context, account, galleryExtendedAuthToken, false);
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public Uri getBaseUri() {
        return GalleryCloudUtils.SHARE_SUB_UBIFOCUS_URI;
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    /* renamed from: generateDBImage */
    public DBItem mo689generateDBImage(Cursor cursor) {
        return new DBShareSubUbiImage(cursor);
    }
}
