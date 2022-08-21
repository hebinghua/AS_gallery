package com.miui.gallery.cloud.operation.move;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.cloud.CloudTableUtils;
import com.miui.gallery.cloud.CloudUrlProvider;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.SpaceFullHandler;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.UbiFocusUtils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class MoveItemToOwnerAlbum extends MoveItemBase {
    @Override // com.miui.gallery.cloud.operation.move.MoveItemBase
    public boolean isToShare() {
        return false;
    }

    public MoveItemToOwnerAlbum(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.operation.move.MoveItemBase
    public String getAlbumId(RequestCloudItem requestCloudItem) {
        return AlbumDataHelper.getAlbumServerIdByLocalId(this.mContext, requestCloudItem.dbCloud.getLocalGroupId());
    }

    @Override // com.miui.gallery.cloud.operation.move.MoveItemBase
    public void appendAlbumIdParameter(ArrayList<NameValuePair> arrayList) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        arrayList.add(new BasicNameValuePair("toAlbumId", this.mAlbumId));
    }

    @Override // com.miui.gallery.cloud.operation.move.MoveItemBase
    public void handleSubUbiSchema(JSONObject jSONObject, String str) throws JSONException {
        UbiFocusUtils.handleSubUbiImage(jSONObject, false, str);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public SpaceFullHandler.SpaceFullListener getSpaceFullListener() {
        return SpaceFullHandler.getOwnerSpaceFullListener();
    }

    @Override // com.miui.gallery.cloud.operation.move.MoveItemBase
    public void appendValues(ContentValues contentValues) {
        contentValues.putNull("fromLocalGroupId");
    }

    @Override // com.miui.gallery.cloud.operation.move.MoveItemBase
    public String getUrl(CloudUrlProvider cloudUrlProvider, String str, String str2, RequestCloudItem requestCloudItem) {
        boolean isSecretAlbum = CloudTableUtils.isSecretAlbum(this.mAlbumId, null);
        String fromLocalGroupId = requestCloudItem.dbCloud.getFromLocalGroupId();
        if (TextUtils.isEmpty(fromLocalGroupId)) {
            SyncLogger.d("MoveItemToOwnerAlbum", "getUrl  fromLocalGroupId does not exist");
            return cloudUrlProvider.getMoveUrl(str, str2);
        }
        boolean isSecretAlbum2 = CloudTableUtils.isSecretAlbum(null, fromLocalGroupId);
        if (!isSecretAlbum && !isSecretAlbum2) {
            return cloudUrlProvider.getMoveUrl(str, str2);
        }
        if (!isSecretAlbum && isSecretAlbum2) {
            return cloudUrlProvider.getUnHideMoveUrl(str, str2);
        }
        return cloudUrlProvider.getHideMoveUrl(str, str2);
    }
}
