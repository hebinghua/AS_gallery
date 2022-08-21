package com.miui.gallery.cloud.adapter;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.base.AbstractSyncAdapter;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.cloud.peopleface.FaceDataManager;
import com.miui.gallery.cloud.peopleface.SyncPeopleFaceFromServer;
import com.miui.gallery.settingssync.GallerySettingsSyncHelper;
import com.miui.gallery.ui.AIAlbumStatusHelper;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class PullFaceDataAdapter extends AbstractSyncAdapter {
    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public long getBindingReason() {
        return 8L;
    }

    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public boolean isAsynchronous() {
        return true;
    }

    public PullFaceDataAdapter(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public GallerySyncResult onPerformSync(Account account, Bundle bundle, GalleryExtendedAuthToken galleryExtendedAuthToken) throws Exception {
        if (ApplicationHelper.isFaceAlbumFeatureOpen()) {
            GallerySettingsSyncHelper.doUpload(getContext());
        }
        if (!Preference.sHaveCheckBabyForNewService(getContext())) {
            if (needResyncFaceDataLaterForBaby()) {
                try2updatePeopleFace2getBaby(account, getContext());
            }
            Preference.sSetHaveCheckBabyForNewService(getContext());
        }
        if (!AIAlbumStatusHelper.isFaceAlbumEnabled()) {
            SyncLogger.w(this.TAG, "the feature of face isn't enabled");
            return new GallerySyncResult.Builder().setCode(GallerySyncCode.OK).build();
        }
        GallerySyncResult<JSONObject> sync = new SyncPeopleFaceFromServer(getContext(), account, galleryExtendedAuthToken).sync();
        SyncLogger.d(this.TAG, "pull result %s", sync);
        return sync;
    }

    public final boolean needResyncFaceDataLaterForBaby() {
        if (Preference.sHaveCheckBabyForNewService(getContext()) || !AIAlbumStatusHelper.isFaceAlbumEnabled()) {
            return false;
        }
        return !FaceDataManager.ifHaveBabyType(getContext());
    }

    public final void try2updatePeopleFace2getBaby(Account account, Context context) {
        GalleryCloudSyncTagUtils.setFaceSyncWatermark(account, 0L);
        GalleryCloudSyncTagUtils.setFaceSyncToken(account, "");
        SyncLogger.d(this.TAG, "reset people face wartemark to 0, zero");
    }
}
