package com.miui.gallery.data;

import android.content.ContentValues;
import android.database.Cursor;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.baby.BabyInfo;
import com.miui.gallery.util.AlbumSortHelper;
import com.miui.gallery.util.CursorUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DBShareAlbum implements DBItem, ServerItem {
    public String albumId;
    public String albumStatus;
    public String babyInfoJson;
    public int canModified;
    public String creatorId;
    public long dateModified;
    public long datetaken;
    public String description;
    public String editedColumns;
    public String fileName;
    public long groupId;
    public String id;
    public boolean isPublic;
    public int localFlag;
    public String peopleId;
    public String publicUrl;
    public String serverId;
    public String serverStatus;
    public long serverTag;
    public int serverType;
    public String shareUrl;
    public String shareUrlLong;
    public String sharerInfo;
    public int sortBy;

    public DBShareAlbum(Cursor cursor) {
        boolean z = false;
        this.id = CursorUtils.getCursorString(cursor, 0);
        this.groupId = cursor.getLong(1);
        this.dateModified = cursor.getLong(2);
        this.description = CursorUtils.getCursorString(cursor, 3);
        this.fileName = CursorUtils.getCursorString(cursor, 4);
        this.datetaken = cursor.getLong(5);
        this.serverId = CursorUtils.getCursorString(cursor, 6);
        this.serverType = cursor.getInt(7);
        this.serverStatus = CursorUtils.getCursorString(cursor, 8);
        this.serverTag = cursor.getLong(9);
        this.localFlag = cursor.getInt(10);
        this.sortBy = cursor.getInt(11);
        this.canModified = cursor.getInt(12);
        this.albumId = CursorUtils.getCursorString(cursor, 13);
        this.creatorId = CursorUtils.getCursorString(cursor, 14);
        this.shareUrl = CursorUtils.getCursorString(cursor, 15);
        this.isPublic = cursor.getInt(22) == 1 ? true : z;
        this.publicUrl = CursorUtils.getCursorString(cursor, 23);
        this.peopleId = CursorUtils.getCursorString(cursor, 26);
        this.babyInfoJson = CursorUtils.getCursorString(cursor, 25);
        this.editedColumns = CursorUtils.getCursorString(cursor, 28);
        this.sharerInfo = CursorUtils.getCursorString(cursor, 27);
        this.albumStatus = CursorUtils.getCursorString(cursor, 16);
        this.shareUrlLong = CursorUtils.getCursorString(cursor, 20);
    }

    public static ContentValues getContentValue(JSONObject jSONObject, DBShareAlbum dBShareAlbum) throws JSONException {
        String str;
        JSONArray jSONArray;
        String str2;
        ContentValues contentValues = new ContentValues();
        contentValues.put("albumId", jSONObject.getString("sharedId"));
        if (jSONObject.has("status")) {
            contentValues.put("albumStatus", jSONObject.getString("status"));
        }
        contentValues.put("albumTag", jSONObject.getString(nexExportFormat.TAG_FORMAT_TAG));
        contentValues.put("creatorId", jSONObject.getString("creatorId"));
        JSONObject jSONObject2 = jSONObject.getJSONObject(MiStat.Param.CONTENT);
        contentValues.put("serverId", Long.valueOf(CloudUtils.getLongAttributeFromJson(jSONObject2, "id")));
        contentValues.put("serverStatus", jSONObject2.getString("status"));
        contentValues.put("serverTag", jSONObject2.getString(nexExportFormat.TAG_FORMAT_TAG));
        contentValues.put("serverType", jSONObject2.getString(nexExportFormat.TAG_FORMAT_TYPE));
        contentValues.put("fileName", jSONObject2.getString("fileName"));
        if (jSONObject2.has("coverImageId")) {
            contentValues.put("coverId", jSONObject2.getString("coverImageId"));
        }
        String str3 = null;
        if (jSONObject2.has("dateModified")) {
            str = jSONObject2.getString("dateModified");
            contentValues.put("dateModified", str);
        } else {
            str = null;
        }
        int i = 0;
        if (jSONObject2.has("isPublic")) {
            if (jSONObject2.getBoolean("isPublic")) {
                contentValues.put("isPublic", (Integer) 1);
                if (jSONObject2.has("publicUrl")) {
                    contentValues.put("publicUrl", jSONObject2.getString("publicUrl"));
                }
            } else {
                contentValues.put("isPublic", (Integer) 0);
                contentValues.putNull("publicUrl");
            }
        }
        if (jSONObject2.has("renderInfos") && (jSONArray = jSONObject2.getJSONArray("renderInfos")) != null) {
            while (true) {
                if (i >= jSONArray.length()) {
                    break;
                }
                JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                if (!jSONObject3.has(nexExportFormat.TAG_FORMAT_TYPE) || !jSONObject3.getString(nexExportFormat.TAG_FORMAT_TYPE).equalsIgnoreCase("baby")) {
                    i++;
                } else {
                    String jSONObject4 = jSONObject3.toString();
                    if (dBShareAlbum != null) {
                        BabyInfo fromJSON = BabyInfo.fromJSON(jSONObject4);
                        BabyInfo fromJSON2 = BabyInfo.fromJSON(dBShareAlbum.babyInfoJson);
                        if (fromJSON != null && fromJSON2 != null) {
                            str2 = dBShareAlbum.peopleId;
                            fromJSON.mPeopleId = str2;
                            fromJSON.mAutoupdate = fromJSON2.mAutoupdate;
                            jSONObject4 = fromJSON.toJSON();
                            contentValues.put("babyInfoJson", jSONObject4);
                            contentValues.put("peopleId", str2);
                        }
                    }
                    str2 = null;
                    contentValues.put("babyInfoJson", jSONObject4);
                    contentValues.put("peopleId", str2);
                }
            }
        }
        if (jSONObject2.has("dateTaken")) {
            String string = jSONObject2.getString("dateTaken");
            contentValues.put("dateTaken", string);
            if (contentValues.containsKey("babyInfoJson")) {
                contentValues.put("sortInfo", AlbumSortHelper.calculateSortPositionByBabyAlbum());
            } else {
                str3 = string;
            }
        } else {
            str3 = str;
        }
        if (str3 != null && !str3.isEmpty()) {
            contentValues.put("sortInfo", AlbumSortHelper.calculateSortPositionByNormalAlbum(Long.parseLong(str3)));
        }
        return contentValues;
    }

    @Override // com.miui.gallery.data.DBItem
    public String getId() {
        return this.id;
    }

    public String getFileName() {
        return this.fileName;
    }

    @Override // com.miui.gallery.data.ServerItem
    public String getServerId() {
        return this.serverId;
    }

    @Override // com.miui.gallery.data.ServerItem
    public long getServerTag() {
        return this.serverTag;
    }

    public int getLocalFlag() {
        return this.localFlag;
    }

    public String getAlbumId() {
        return this.albumId;
    }

    public String getAlbumStatus() {
        return this.albumStatus;
    }

    public String getCreatorId() {
        return this.creatorId;
    }

    public String getShareUrl() {
        return this.shareUrl;
    }

    public String getShareUrlLong() {
        return this.shareUrlLong;
    }

    public boolean getIsPublic() {
        return this.isPublic;
    }

    public String getPublicUri() {
        return this.publicUrl;
    }

    public String getSharerInfo() {
        return this.sharerInfo;
    }

    public String getPeopleId() {
        return this.peopleId;
    }

    public String getBabyInfoJson() {
        return this.babyInfoJson;
    }

    public String getEditedColumns() {
        return this.editedColumns;
    }

    public static boolean isNormalStatus(String str, String str2) {
        return "normal".equals(str2) && "custom".equals(str);
    }
}
