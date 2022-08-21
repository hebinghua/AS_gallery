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
import com.miui.gallery.cloud.peopleface.RequestFaceItem;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.util.SyncLogger;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CreatePeopleOperation extends FaceRequestOperationBase {
    public CreatePeopleOperation(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        if (!(requestItemBase instanceof RequestFaceItem)) {
            SyncLogger.e("CreatePeopleOperation", "item is not instanceof RequestFaceItem.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        return super.onPreRequest(requestItemBase);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
        RequestFaceItem requestFaceItem = (RequestFaceItem) requestItemBase;
        String peopleCreateUrl = HostManager.PeopleFace.getPeopleCreateUrl();
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("peopleName", requestFaceItem.face.peopleName));
        if (!TextUtils.isEmpty(requestFaceItem.face.peopleContactJson)) {
            arrayList.add(new BasicNameValuePair("peopleContact", requestFaceItem.face.peopleContactJson));
        }
        if (!TextUtils.isEmpty(requestFaceItem.face.selectCoverId) && FaceManager.queryLocalGroupId(requestFaceItem.face.selectCoverId) == Long.parseLong(requestFaceItem.face._id)) {
            arrayList.add(new BasicNameValuePair("faceId", requestFaceItem.face.selectCoverId));
        }
        return new RequestOperationBase.Request.Builder().setMethod(2).setUrl(peopleCreateUrl).setParams(arrayList).setRetryTimes(requestFaceItem.createRetryTimes).setNeedReRequest(requestFaceItem.needReRequest).build();
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
        CloudUtils.updateToLocalDBForSync(FaceDataManager.PEOPLE_FACE_URI, FaceAlbumUtil.getContentValuesForPeopleFace(jSONObject.optJSONObject("record")), ((RequestFaceItem) requestItemBase).face._id);
    }
}
