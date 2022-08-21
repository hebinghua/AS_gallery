package com.miui.gallery.cloud.operation;

import android.accounts.Account;
import android.content.Context;
import com.miui.gallery.cloud.CloudUrlProvider;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.data.DBImage;
import com.xiaomi.stat.MiStat;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class EditCloudItemName extends EditCloudBase {
    @Override // com.miui.gallery.cloud.operation.EditCloudBase
    public int getColumnIndex() {
        return 7;
    }

    public EditCloudItemName(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.operation.EditCloudBase
    public RequestOperationBase.Request buildRequest(Account account, RequestCloudItem requestCloudItem) throws Exception {
        DBImage dBImage = requestCloudItem.dbCloud;
        String updateUrl = CloudUrlProvider.getUrlProvider(dBImage.isShareItem(), dBImage.isVideoType()).getUpdateUrl(account.name, dBImage.getServerId());
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = dBImage.toJSONObject();
        jSONObject2.put("fileName", dBImage.getFileName());
        jSONObject2.put("title", dBImage.getTitle());
        jSONObject.put(MiStat.Param.CONTENT, jSONObject2);
        return new RequestOperationBase.Request.Builder().setUrl(updateUrl).setMethod(2).setPostData(jSONObject).setRetryTimes(requestCloudItem.otherRetryTimes).setNeedReRequest(false).build();
    }
}
