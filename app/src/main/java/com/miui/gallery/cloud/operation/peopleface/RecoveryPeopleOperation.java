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
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.util.SyncLogger;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class RecoveryPeopleOperation extends FaceRequestOperationBase {
    public RecoveryPeopleOperation(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        if (!(requestItemBase instanceof RequestFaceItem)) {
            SyncLogger.e(getTag(), "item is not instanceof RequestFaceItem.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        return super.onPreRequest(requestItemBase);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
        RequestFaceItem requestFaceItem = (RequestFaceItem) requestItemBase;
        String peopleRecoveryUrl = HostManager.PeopleFace.getPeopleRecoveryUrl(requestFaceItem.face.serverId);
        ArrayList arrayList = new ArrayList();
        if (!TextUtils.isEmpty(requestFaceItem.face.peopleName)) {
            arrayList.add(new BasicNameValuePair("peopleName", requestFaceItem.face.peopleName));
        }
        return new RequestOperationBase.Request.Builder().setMethod(2).setUrl(peopleRecoveryUrl).setParams(arrayList).setRetryTimes(requestFaceItem.createRetryTimes).setNeedReRequest(requestFaceItem.needReRequest).build();
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestError(GallerySyncCode gallerySyncCode, RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        if (gallerySyncCode != GallerySyncCode.OK) {
            String tag = getTag();
            SyncLogger.e(tag, "request error: " + gallerySyncCode);
            requestItemBase.createRetryTimes = requestItemBase.createRetryTimes + 1;
        }
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
        if (contentValuesForPeopleFace == null) {
            contentValuesForPeopleFace = new ContentValues();
        }
        boolean z = true;
        contentValuesForPeopleFace.put("visibilityType", (Integer) 1);
        if (FaceManager.getPeopleLocalFlagByLocalID(requestFaceItem.face._id) == 14) {
            z = false;
        }
        CloudUtils.updateToLocalDBForSync(FaceDataManager.PEOPLE_FACE_URI, contentValuesForPeopleFace, requestFaceItem.face._id, z);
    }
}
