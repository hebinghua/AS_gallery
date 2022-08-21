package com.miui.gallery.cloud.baby;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.AlbumSyncHelper;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.RequestAlbumItem;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.RetryOperation;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SyncLogger;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SyncBabyInfoFromLocal {
    public Account mAccount;
    public Context mContext;
    public GalleryExtendedAuthToken mExtendedAuthToken;
    public ArrayList<RequestAlbumItem> mUpdateBabyInfoItems = new ArrayList<>();

    public SyncBabyInfoFromLocal(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        this.mContext = context;
        this.mAccount = account;
        this.mExtendedAuthToken = galleryExtendedAuthToken;
    }

    public void sync() throws Exception {
        Uri uri = GalleryCloudUtils.ALBUM_URI;
        String[] projectionAll = CloudUtils.getProjectionAll();
        GalleryUtils.safeQuery(uri, projectionAll, "not ( json_extract(extra,'$.peopleId') is null or  json_extract(extra,'$.peopleId') = '')  AND  " + CloudUtils.PHOTO_LOCAL_FLAG_SYNCED, (String[]) null, (String) null, new GalleryUtils.QueryHandler<Void>() { // from class: com.miui.gallery.cloud.baby.SyncBabyInfoFromLocal.1
            @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public Void mo1712handle(Cursor cursor) {
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        DBAlbum dBAlbum = new DBAlbum(cursor);
                        String babyInfo = dBAlbum.getBabyInfo();
                        if (!TextUtils.isEmpty(babyInfo)) {
                            try {
                                JSONObject jSONObject = new JSONObject(babyInfo);
                                if (jSONObject.has("localFlag") && jSONObject.getInt("localFlag") != 0) {
                                    SyncBabyInfoFromLocal.this.mUpdateBabyInfoItems.add(new RequestAlbumItem(0, dBAlbum));
                                }
                            } catch (JSONException unused) {
                            }
                        }
                    }
                    return null;
                }
                return null;
            }
        });
        if (this.mUpdateBabyInfoItems.size() > 0) {
            try {
                RetryOperation.doOperation(this.mContext, this.mAccount, this.mExtendedAuthToken, this.mUpdateBabyInfoItems, new UpdateBabyInfo(this.mContext));
            } catch (Exception unused) {
            }
        }
    }

    /* loaded from: classes.dex */
    public static class UpdateBabyInfo extends RequestOperationBase {
        public UpdateBabyInfo(Context context) {
            super(context);
        }

        @Override // com.miui.gallery.cloud.RequestOperationBase
        public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
            if (!(requestItemBase instanceof RequestAlbumItem)) {
                return GallerySyncCode.NOT_RETRY_ERROR;
            }
            return super.onPreRequest(requestItemBase);
        }

        @Override // com.miui.gallery.cloud.RequestOperationBase
        public RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
            RequestAlbumItem requestAlbumItem = (RequestAlbumItem) requestItemBase;
            String serverId = requestAlbumItem.dbAlbum.getServerId();
            if (TextUtils.isEmpty(serverId)) {
                return null;
            }
            String updateBabyInfoUrl = HostManager.Baby.getUpdateBabyInfoUrl(serverId);
            return new RequestOperationBase.Request.Builder().setMethod(2).setUrl(updateBabyInfoUrl).setPostData(new JSONObject().put(MiStat.Param.CONTENT, requestAlbumItem.dbAlbum.toJSONObject())).setRetryTimes(requestAlbumItem.createRetryTimes).setNeedReRequest(requestItemBase.needReRequest).build();
        }

        @Override // com.miui.gallery.cloud.RequestOperationBase
        public void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
            JSONObject optJSONObject = jSONObject.optJSONObject(MiStat.Param.CONTENT);
            if (optJSONObject == null) {
                SyncLogger.e(getTag(), "response content null %s", jSONObject);
                return;
            }
            RequestAlbumItem requestAlbumItem = (RequestAlbumItem) requestItemBase;
            ContentValues contentValuesForResponse = AlbumSyncHelper.getContentValuesForResponse(optJSONObject);
            Uri uri = GalleryCloudUtils.ALBUM_URI;
            synchronized (uri) {
                AlbumDataHelper.updateAlbumAndSetLocalFlagToSynced(uri, contentValuesForResponse, requestAlbumItem.dbAlbum.getId());
            }
        }

        @Override // com.miui.gallery.cloud.RequestOperationBase
        public void onRequestError(GallerySyncCode gallerySyncCode, RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
            if (gallerySyncCode != GallerySyncCode.OK) {
                requestItemBase.createRetryTimes++;
            }
        }
    }
}
