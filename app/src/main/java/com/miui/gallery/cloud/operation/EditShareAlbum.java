package com.miui.gallery.cloud.operation;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.RequestSharerAlbumItem;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.data.DBShareAlbum;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SyncLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class EditShareAlbum extends RequestOperationBase {
    public EditShareAlbum(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        if (!(requestItemBase instanceof RequestSharerAlbumItem)) {
            SyncLogger.e("EditShareAlbum", "requestItem not instanceof RequestSharerAlbumItem!");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        String editedColumns = ((RequestSharerAlbumItem) requestItemBase).mDBItem.getEditedColumns();
        if (TextUtils.isEmpty(editedColumns) || !editedColumns.contains(GalleryCloudUtils.transformToEditedColumnsElement(25))) {
            SyncLogger.e("EditShareAlbum", "editedColumns %s not supported!", editedColumns);
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        return GallerySyncCode.OK;
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
        DBShareAlbum dBShareAlbum = ((RequestSharerAlbumItem) requestItemBase).mDBItem;
        String editAlbumUrl = HostManager.ShareAlbum.getEditAlbumUrl();
        JSONObject jSONObject = new JSONObject(dBShareAlbum.getBabyInfoJson());
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("sharedAlbumId", dBShareAlbum.getAlbumId()));
        arrayList.add(new BasicNameValuePair("autoUpdate", String.valueOf(jSONObject.getBoolean("autoUpdate"))));
        arrayList.add(new BasicNameValuePair("peopleId", dBShareAlbum.getPeopleId()));
        return new RequestOperationBase.Request.Builder().setMethod(2).setUrl(editAlbumUrl).setParams(arrayList).setRetryTimes(requestItemBase.otherRetryTimes).setNeedReRequest(requestItemBase.needReRequest).build();
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestError(GallerySyncCode gallerySyncCode, RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        if (gallerySyncCode != GallerySyncCode.OK) {
            requestItemBase.otherRetryTimes++;
        }
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        DBShareAlbum dBShareAlbum = ((RequestSharerAlbumItem) requestItemBase).mDBItem;
        ContentValues contentValues = new ContentValues();
        contentValues.putNull("editedColumns");
        GalleryUtils.safeUpdate(GalleryCloudUtils.SHARE_ALBUM_URI, contentValues, String.format(Locale.US, "%s=%s", j.c, dBShareAlbum.getId()), null);
        SyncLogger.d("EditShareAlbum", "edit share album successfully. album name: %s", dBShareAlbum.getFileName());
    }
}
