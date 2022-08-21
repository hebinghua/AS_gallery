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
public class EditCloudFavoriteInfo extends EditCloudBase {
    @Override // com.miui.gallery.cloud.operation.EditCloudBase
    public int getColumnIndex() {
        return -1;
    }

    public EditCloudFavoriteInfo(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.operation.EditCloudBase
    public RequestOperationBase.Request buildRequest(Account account, RequestCloudItem requestCloudItem) throws Exception {
        DBImage dBImage = requestCloudItem.dbCloud;
        String updateUrl = CloudUrlProvider.getUrlProvider(dBImage.isShareItem(), dBImage.isVideoType()).getUpdateUrl(account.name, dBImage.getServerId());
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(MiStat.Param.CONTENT, dBImage.toJSONObject());
        return new RequestOperationBase.Request.Builder().setUrl(updateUrl).setMethod(2).setPostData(jSONObject).setRetryTimes(requestCloudItem.otherRetryTimes).setNeedReRequest(false).build();
    }
}
