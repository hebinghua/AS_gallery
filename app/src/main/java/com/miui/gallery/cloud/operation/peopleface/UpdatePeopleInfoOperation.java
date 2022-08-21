package com.miui.gallery.cloud.operation.peopleface;

import android.accounts.Account;
import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.peopleface.FaceAlbumUtil;
import com.miui.gallery.cloud.peopleface.FaceDataManager;
import com.miui.gallery.cloud.peopleface.PeopleFace;
import com.miui.gallery.cloud.peopleface.RequestFaceItem;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.util.SyncLogger;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class UpdatePeopleInfoOperation extends FaceRequestOperationBase {
    public UpdatePeopleInfoOperation(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        if (!(requestItemBase instanceof RequestFaceItem)) {
            SyncLogger.e("UpdatePeopleInfoOperation", "item is not instanceof RequestFaceItem.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        return super.onPreRequest(requestItemBase);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
        RequestFaceItem requestFaceItem = (RequestFaceItem) requestItemBase;
        String peopleUpdateInfoUrl = HostManager.PeopleFace.getPeopleUpdateInfoUrl(requestFaceItem.face.serverId);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("peopleName", requestFaceItem.face.peopleName));
        if (!TextUtils.isEmpty(requestFaceItem.face.peopleContactJson)) {
            arrayList.add(new BasicNameValuePair("peopleContact", requestFaceItem.face.peopleContactJson));
        }
        if (!TextUtils.isEmpty(requestFaceItem.face.selectCoverId) && !TextUtils.isEmpty(requestFaceItem.face.serverId)) {
            PeopleFace peopleFace = requestFaceItem.face;
            if (peopleFace.serverId.equals(FaceManager.queryGroupId(peopleFace.selectCoverId))) {
                arrayList.add(new BasicNameValuePair("faceId", requestFaceItem.face.selectCoverId));
            }
        }
        return new RequestOperationBase.Request.Builder().setMethod(2).setUrl(peopleUpdateInfoUrl).setParams(arrayList).setRetryTimes(requestFaceItem.otherRetryTimes).setNeedReRequest(requestFaceItem.needReRequest).build();
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        JSONObject optJSONObject = jSONObject.optJSONObject("record");
        if (optJSONObject == null) {
            SyncLogger.e(getTag(), "response record error");
            return;
        }
        CloudUtils.updateToLocalDBForSync(FaceDataManager.PEOPLE_FACE_URI, FaceAlbumUtil.getContentValuesForPeopleFace(optJSONObject), ((RequestFaceItem) requestItemBase).face._id);
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
