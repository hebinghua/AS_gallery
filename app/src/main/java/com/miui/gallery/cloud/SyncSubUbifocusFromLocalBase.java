package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.Context;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.data.DBItem;
import com.miui.gallery.util.SyncLogger;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class SyncSubUbifocusFromLocalBase extends SyncFromLocalBase {
    public ArrayList<RequestCloudItem> mAutoCreateImageItems;
    public ArrayList<RequestCloudItem> mManualCreateImageItems;
    public boolean mNoDelay;

    public SyncSubUbifocusFromLocalBase(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, boolean z) {
        super(context, account, galleryExtendedAuthToken);
        this.mNoDelay = z;
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public void initRequestCloudItemList() {
        this.mManualCreateImageItems = new ArrayList<>();
        this.mAutoCreateImageItems = new ArrayList<>();
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public void putToRequestCloudItemList(DBItem dBItem) {
        DBImage dBImage = (DBImage) dBItem;
        int localFlag = dBImage.getLocalFlag();
        if (localFlag == 7) {
            if (dBImage.getServerType() == 1) {
                this.mAutoCreateImageItems.add(new RequestCloudItem(4, dBImage, this.mNoDelay));
                return;
            }
            SyncLogger.e("SyncUbifocusFromLocal", "unsupport local flag=%d serverType=%d", Integer.valueOf(dBImage.getLocalFlag()), Integer.valueOf(dBImage.getServerType()));
        } else if (localFlag == 8) {
            if (dBImage.getServerType() == 1) {
                this.mManualCreateImageItems.add(new RequestCloudItem(5, dBImage, this.mNoDelay));
                return;
            }
            SyncLogger.e("SyncUbifocusFromLocal", "unsupport local flag= %d serverType= %d", Integer.valueOf(dBImage.getLocalFlag()), Integer.valueOf(dBImage.getServerType()));
        } else {
            SyncLogger.e("SyncUbifocusFromLocal", "unsupport local flag %d", Integer.valueOf(dBImage.getLocalFlag()));
        }
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public void handleRequestCloudItemList() throws Exception {
        if (this.mManualCreateImageItems.size() > 0) {
            SyncLogger.v("SyncUbifocusFromLocal", "start upload manual create images");
            UpDownloadManager.dispatchList(this.mManualCreateImageItems);
        }
        if (this.mAutoCreateImageItems.size() > 0) {
            SyncLogger.v("SyncUbifocusFromLocal", "start upload auto create images");
            UpDownloadManager.dispatchList(this.mAutoCreateImageItems);
        }
    }
}
