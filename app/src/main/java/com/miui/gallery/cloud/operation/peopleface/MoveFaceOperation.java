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
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class MoveFaceOperation extends FaceRequestOperationBase {
    @Override // com.miui.gallery.cloud.operation.peopleface.FaceRequestOperationBase
    public int getLimitCountForOperation() {
        return 10;
    }

    public MoveFaceOperation(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        ArrayList<RequestItemBase> items = requestItemBase.getItems();
        if (items == null || items.isEmpty()) {
            SyncLogger.e("MoveFaceOperation", "multiRequest items is null.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        } else if (!(items.get(0) instanceof RequestFaceItem)) {
            SyncLogger.e(getTag(), "item is not instanceof RequestFaceItem.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        } else {
            return super.onPreRequest(requestItemBase);
        }
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
        ArrayList<RequestItemBase> items = requestItemBase.getItems();
        StringBuilder sb = new StringBuilder();
        Iterator<RequestItemBase> it = items.iterator();
        while (it.hasNext()) {
            RequestFaceItem requestFaceItem = (RequestFaceItem) it.next();
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(requestFaceItem.face.serverId);
        }
        String peopleId = getPeopleId((RequestFaceItem) items.get(0));
        if (TextUtils.isEmpty(peopleId)) {
            SyncLogger.e(getTag(), "people id is empty");
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("destPeopleId", peopleId));
        arrayList.add(new BasicNameValuePair("faceIds", sb.toString()));
        return new RequestOperationBase.Request.Builder().setMethod(2).setUrl(HostManager.PeopleFace.getFaceMoveUrl()).setParams(arrayList).setRetryTimes(requestItemBase.createRetryTimes).setNeedReRequest(requestItemBase.needReRequest).build();
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
        JSONArray optJSONArray = jSONObject.optJSONArray("successResults");
        if (optJSONArray == null) {
            SyncLogger.e(getTag(), "success result null");
            return;
        }
        for (int i = 0; i < optJSONArray.length(); i++) {
            ContentValues contentValuesForPeopleFace = FaceAlbumUtil.getContentValuesForPeopleFace(optJSONArray.getJSONObject(i).getJSONObject("record"));
            CloudUtils.updateToPeopleFaceDBForSync(FaceDataManager.PEOPLE_FACE_URI, contentValuesForPeopleFace, contentValuesForPeopleFace.getAsString("serverId"));
        }
    }

    public String getPeopleId(RequestFaceItem requestFaceItem) {
        return CloudUtils.getStringColumnValue(this.mContext, FaceDataManager.PEOPLE_FACE_URI, "serverId", j.c, requestFaceItem.face.localGroupId);
    }
}
