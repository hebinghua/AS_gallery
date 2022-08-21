package com.miui.gallery.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.util.CursorUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DBShareUser {
    public String albumId;
    public long createTime;
    public String id;
    public String localAlbumId;
    public int localFlag;
    public String mRelation;
    public String mRelationText;
    public int requestType;
    public String requestValue;
    public String serverStatus;
    public long serverTag;
    public String shareText;
    public String shareUrl;
    public String userId;
    public String userName;

    public DBShareUser(Cursor cursor) {
        this.id = CursorUtils.getCursorString(cursor, 0);
        this.userId = CursorUtils.getCursorString(cursor, 1);
        String cursorString = CursorUtils.getCursorString(cursor, 2);
        this.userName = cursorString;
        if (TextUtils.isEmpty(cursorString)) {
            this.userName = this.userId;
        }
        this.createTime = cursor.getLong(3);
        this.requestType = cursor.getInt(4);
        this.requestValue = CursorUtils.getCursorString(cursor, 5);
        this.serverStatus = cursor.getString(6);
        this.serverTag = cursor.getInt(7);
        this.albumId = CursorUtils.getCursorString(cursor, 8);
        this.localFlag = cursor.getInt(9);
        this.shareUrl = CursorUtils.getCursorString(cursor, 10);
        this.shareText = CursorUtils.getCursorString(cursor, 11);
        this.localAlbumId = CursorUtils.getCursorString(cursor, 12);
        this.mRelation = CursorUtils.getCursorString(cursor, 13);
        this.mRelationText = CursorUtils.getCursorString(cursor, 14);
    }

    public static ContentValues getContentValues(JSONObject jSONObject) throws JSONException {
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", jSONObject.getString("userId"));
        contentValues.put("serverStatus", jSONObject.getString("status"));
        contentValues.put("serverTag", Long.valueOf(CloudUtils.getLongAttributeFromJson(jSONObject, nexExportFormat.TAG_FORMAT_TAG)));
        contentValues.put("createTime", jSONObject.getString("createTime"));
        contentValues.put("requestType", jSONObject.getString("requestType"));
        contentValues.put("requestValue", jSONObject.getString("requestValue"));
        if (jSONObject.has("relation")) {
            contentValues.put("relation", jSONObject.getString("relation"));
        }
        if (jSONObject.has("relationText")) {
            contentValues.put("relationText", jSONObject.getString("relationText"));
        }
        return contentValues;
    }

    public String getId() {
        return this.id;
    }

    public long getServerTag() {
        return this.serverTag;
    }
}
