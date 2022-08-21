package com.miui.gallery.cloud.operation;

import android.accounts.Account;
import android.content.Context;
import com.miui.gallery.cloud.CloudUrlProvider;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.util.UbiFocusUtils;
import com.xiaomi.stat.MiStat;
import java.util.List;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class EditCloudItem extends EditCloudBase {
    @Override // com.miui.gallery.cloud.operation.EditCloudBase
    public int getColumnIndex() {
        return 57;
    }

    public EditCloudItem(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.operation.EditCloudBase
    public RequestOperationBase.Request buildRequest(Account account, RequestCloudItem requestCloudItem) throws Exception {
        List<DBImage> subUbiImages;
        DBImage dBImage = requestCloudItem.dbCloud;
        String editUrl = CloudUrlProvider.getUrlProvider(dBImage.isShareItem(), dBImage.isVideoType()).getEditUrl(account.name, dBImage.getRequestId());
        new JSONObject();
        JSONObject jSONObject = dBImage.toJSONObject();
        if (dBImage.isUbiFocus() && (subUbiImages = UbiFocusUtils.getSubUbiImages(dBImage.isShareItem(), dBImage.getRequestId(), dBImage.getId())) != null) {
            JSONObject jSONObject2 = new JSONObject();
            for (DBImage dBImage2 : subUbiImages) {
                jSONObject2.putOpt(String.valueOf(dBImage2.getSubUbiIndex()), dBImage2.toJsonObjectForSubUbi());
            }
            jSONObject2.putOpt(String.valueOf(dBImage.getSubUbiIndex()), dBImage.toJsonObjectForSubUbi());
            jSONObject.putOpt("ubiSubImageContentMap", jSONObject2);
        }
        return new RequestOperationBase.Request.Builder().setUrl(editUrl).setMethod(2).setPostData(new JSONObject().put(MiStat.Param.CONTENT, jSONObject)).setRetryTimes(requestCloudItem.otherRetryTimes).setNeedReRequest(false).build();
    }
}
