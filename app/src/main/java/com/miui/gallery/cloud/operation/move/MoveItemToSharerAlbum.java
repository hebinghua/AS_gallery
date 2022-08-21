package com.miui.gallery.cloud.operation.move;

import android.content.ContentValues;
import android.content.Context;
import com.miui.gallery.cloud.CloudUrlProvider;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.SpaceFullHandler;
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
public class MoveItemToSharerAlbum extends MoveItemBase {
    @Override // com.miui.gallery.cloud.operation.move.MoveItemBase
    public boolean isToShare() {
        return true;
    }

    public MoveItemToSharerAlbum(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.operation.move.MoveItemBase
    public String getAlbumId(RequestCloudItem requestCloudItem) {
        return CloudUtils.getShareAlbumIdByLocalId(this.mContext, requestCloudItem);
    }

    @Override // com.miui.gallery.cloud.operation.move.MoveItemBase
    public void appendAlbumIdParameter(ArrayList<NameValuePair> arrayList) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        arrayList.add(new BasicNameValuePair("toSharedAlbumId", this.mAlbumId));
    }

    @Override // com.miui.gallery.cloud.operation.move.MoveItemBase
    public void appendValues(ContentValues contentValues) {
        contentValues.put("albumId", this.mAlbumId);
        contentValues.putNull("fromLocalGroupId");
    }

    @Override // com.miui.gallery.cloud.operation.move.MoveItemBase
    public void handleSubUbiSchema(JSONObject jSONObject, String str) throws JSONException {
        UbiFocusUtils.handleSubUbiImage(jSONObject, true, str);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public SpaceFullHandler.SpaceFullListener getSpaceFullListener() {
        return SpaceFullHandler.getSharerSpaceFullListener();
    }

    @Override // com.miui.gallery.cloud.operation.move.MoveItemBase
    public String getUrl(CloudUrlProvider cloudUrlProvider, String str, String str2, RequestCloudItem requestCloudItem) {
        return cloudUrlProvider.getMoveUrl(str, str2);
    }
}
