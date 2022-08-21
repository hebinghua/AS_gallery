package com.miui.gallery.cloud.operation.peopleface;

import android.accounts.Account;
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
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class MergePeopleOperation extends FaceRequestOperationBase {
    public MergePeopleOperation(Context context) {
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
        String peopleMergeUrl = HostManager.PeopleFace.getPeopleMergeUrl();
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("srcPeopleId", requestFaceItem.face.serverId));
        arrayList.add(new BasicNameValuePair("destPeopleId", requestFaceItem.face.groupId));
        return new RequestOperationBase.Request.Builder().setMethod(2).setUrl(peopleMergeUrl).setParams(arrayList).setRetryTimes(requestFaceItem.otherRetryTimes).setNeedReRequest(false).build();
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        RequestFaceItem requestFaceItem = (RequestFaceItem) requestItemBase;
        JSONObject optJSONObject = jSONObject.optJSONObject("record");
        if (optJSONObject == null) {
            SyncLogger.e(getTag(), "response record error");
            return;
        }
        CloudUtils.updateToLocalDBForSync(FaceDataManager.PEOPLE_FACE_URI, FaceAlbumUtil.getContentValuesForPeopleFace(optJSONObject), requestFaceItem.face._id);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestError(GallerySyncCode gallerySyncCode, RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        if (gallerySyncCode != GallerySyncCode.OK) {
            String tag = getTag();
            SyncLogger.e(tag, "request error: " + gallerySyncCode);
            requestItemBase.otherRetryTimes = requestItemBase.otherRetryTimes + 1;
        }
    }
}
