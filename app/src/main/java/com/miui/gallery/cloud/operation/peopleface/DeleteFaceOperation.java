package com.miui.gallery.cloud.operation.peopleface;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.peopleface.FaceAlbumUtil;
import com.miui.gallery.cloud.peopleface.FaceDataManager;
import com.miui.gallery.cloud.peopleface.RequestFaceItem;
import com.miui.gallery.util.SyncLogger;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DeleteFaceOperation extends FaceRequestOperationBase {
    public DeleteFaceOperation(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        if (!(requestItemBase instanceof RequestFaceItem)) {
            SyncLogger.e("DeleteFaceOperation", "item is not instanceof RequestFaceItem.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        return super.onPreRequest(requestItemBase);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
        return new RequestOperationBase.Request.Builder().setUrl(HostManager.PeopleFace.getFaceDeleteUrl(((RequestFaceItem) requestItemBase).face.serverId)).setMethod(2).setRetryTimes(requestItemBase.otherRetryTimes).setNeedReRequest(false).build();
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestError(GallerySyncCode gallerySyncCode, RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        if (gallerySyncCode == GallerySyncCode.OK || gallerySyncCode == GallerySyncCode.ALBUM_NOT_EXIST) {
            return;
        }
        String tag = getTag();
        SyncLogger.e(tag, "request error: " + gallerySyncCode);
        requestItemBase.otherRetryTimes = requestItemBase.otherRetryTimes + 1;
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        JSONObject optJSONObject = jSONObject.optJSONObject("record");
        if (optJSONObject == null) {
            SyncLogger.e(getTag(), "response record null");
            return;
        }
        RequestFaceItem requestFaceItem = (RequestFaceItem) requestItemBase;
        ContentValues contentValuesForPeopleFace = FaceAlbumUtil.getContentValuesForPeopleFace(optJSONObject);
        CloudUtils.updateToPeopleFaceDBForDeleteItem(FaceDataManager.PEOPLE_FACE_URI, contentValuesForPeopleFace, contentValuesForPeopleFace.getAsString("serverId"));
    }
}
