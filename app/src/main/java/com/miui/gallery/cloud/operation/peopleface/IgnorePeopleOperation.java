package com.miui.gallery.cloud.operation.peopleface;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.peopleface.FaceAlbumUtil;
import com.miui.gallery.cloud.peopleface.FaceDataManager;
import com.miui.gallery.cloud.peopleface.RequestFaceItem;
import com.miui.gallery.util.SyncLogger;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class IgnorePeopleOperation extends FaceRequestOperationBase {
    public IgnorePeopleOperation(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        if (!(requestItemBase instanceof RequestFaceItem)) {
            SyncLogger.e("IgnorePeopleOperation", "item is not instanceof RequestFaceItem.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        RequestFaceItem requestFaceItem = (RequestFaceItem) requestItemBase;
        if (TextUtils.isEmpty(requestFaceItem.face.serverId)) {
            try {
                CloudUtils.updateToLocalDBForSync(FaceDataManager.PEOPLE_FACE_URI, (ContentValues) null, requestFaceItem.face._id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        return super.onPreRequest(requestItemBase);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
        return new RequestOperationBase.Request.Builder().setUrl(HostManager.PeopleFace.getPeopleIgnoreUrl(((RequestFaceItem) requestItemBase).face.serverId)).setMethod(2).setRetryTimes(requestItemBase.createRetryTimes).setNeedReRequest(requestItemBase.needReRequest).build();
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        RequestFaceItem requestFaceItem = (RequestFaceItem) requestItemBase;
        JSONObject optJSONObject = jSONObject.optJSONObject("record");
        if (optJSONObject == null) {
            SyncLogger.e(getTag(), "response record null");
            return;
        }
        CloudUtils.updateToLocalDBForSync(FaceDataManager.PEOPLE_FACE_URI, FaceAlbumUtil.getContentValuesForPeopleFace(optJSONObject), requestFaceItem.face._id);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestError(GallerySyncCode gallerySyncCode, RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        if (gallerySyncCode != GallerySyncCode.OK) {
            String tag = getTag();
            SyncLogger.e(tag, "request error: " + gallerySyncCode);
            requestItemBase.createRetryTimes = requestItemBase.createRetryTimes + 1;
        }
    }
}
