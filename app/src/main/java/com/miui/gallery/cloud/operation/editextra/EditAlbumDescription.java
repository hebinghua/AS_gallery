package com.miui.gallery.cloud.operation.editextra;

import android.accounts.Account;
import android.content.Context;
import com.miui.gallery.cloud.CloudGroupUrlProvider;
import com.miui.gallery.cloud.RequestAlbumItem;
import com.miui.gallery.cloud.RequestOperationBase;
import com.xiaomi.stat.MiStat;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class EditAlbumDescription extends EditAlbumBase {
    @Override // com.miui.gallery.cloud.operation.editextra.EditAlbumBase
    public int getColumnIndex() {
        return 22;
    }

    public EditAlbumDescription(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.operation.editextra.EditAlbumBase
    public RequestOperationBase.Request buildRequest(Account account, RequestAlbumItem requestAlbumItem) throws Exception {
        String editGroupUrl = CloudGroupUrlProvider.getUrlProvider(false).getEditGroupUrl(account.name, requestAlbumItem.dbAlbum.getServerId());
        return new RequestOperationBase.Request.Builder().setUrl(editGroupUrl).setMethod(2).setPostData(new JSONObject().put(MiStat.Param.CONTENT, requestAlbumItem.dbAlbum.toJSONObject())).setRetryTimes(requestAlbumItem.otherRetryTimes).setNeedReRequest(false).build();
    }
}
