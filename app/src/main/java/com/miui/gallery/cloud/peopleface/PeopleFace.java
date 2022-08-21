package com.miui.gallery.cloud.peopleface;

import android.database.Cursor;
import com.miui.gallery.data.DBItem;
import com.miui.gallery.data.ServerItem;
import com.miui.gallery.util.CursorUtils;
import com.miui.gallery.util.UpdateHelper;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class PeopleFace implements DBItem, ServerItem {
    public String _id;
    public double faceHScale;
    public double faceWScale;
    public double faceXScale;
    public double faceYScale;
    public String groupId;
    public double leftEyeXScale;
    public double leftEyeYScale;
    public int localFlag;
    public String localGroupId;
    public String peopleContactJson;
    public String peopleName;
    public int relationType;
    public double rightEyeXScale;
    public double rightEyeYScale;
    public String selectCoverId;
    public String serverId;
    public String serverStatus;
    public long serverTag;
    public String type;
    public int visibilityType;

    public PeopleFace(Cursor cursor) {
        reloadData(cursor, 0);
    }

    public boolean reloadData(Cursor cursor, int i) {
        UpdateHelper updateHelper = new UpdateHelper();
        this._id = (String) updateHelper.update(this._id, CursorUtils.getCursorString(cursor, i + 0));
        this.serverId = (String) updateHelper.update(this.serverId, CursorUtils.getCursorString(cursor, i + 1));
        this.type = (String) updateHelper.update(this.type, CursorUtils.getCursorString(cursor, i + 2));
        this.groupId = (String) updateHelper.update(this.groupId, CursorUtils.getCursorString(cursor, i + 3));
        this.localGroupId = (String) updateHelper.update(this.localGroupId, CursorUtils.getCursorString(cursor, i + 4));
        this.localFlag = updateHelper.update(this.localFlag, cursor.getInt(i + 5));
        this.faceXScale = updateHelper.update(this.faceXScale, cursor.getDouble(i + 6));
        this.faceYScale = updateHelper.update(this.faceYScale, cursor.getDouble(i + 7));
        this.faceWScale = updateHelper.update(this.faceWScale, cursor.getDouble(i + 8));
        this.faceHScale = updateHelper.update(this.faceHScale, cursor.getDouble(i + 9));
        this.leftEyeXScale = updateHelper.update(this.leftEyeXScale, cursor.getDouble(i + 10));
        this.leftEyeYScale = updateHelper.update(this.leftEyeYScale, cursor.getDouble(i + 11));
        this.rightEyeXScale = updateHelper.update(this.rightEyeXScale, cursor.getDouble(i + 12));
        this.rightEyeYScale = updateHelper.update(this.rightEyeYScale, cursor.getDouble(i + 13));
        this.serverStatus = (String) updateHelper.update(this.serverStatus, CursorUtils.getCursorString(cursor, i + 14));
        this.peopleName = (String) updateHelper.update(this.peopleName, CursorUtils.getCursorString(cursor, i + 15));
        this.peopleContactJson = (String) updateHelper.update(this.peopleContactJson, CursorUtils.getCursorString(cursor, i + 18));
        this.serverTag = updateHelper.update(this.serverTag, cursor.getLong(i + 20));
        this.relationType = updateHelper.update(this.relationType, cursor.getInt(i + 19));
        this.visibilityType = updateHelper.update(this.visibilityType, cursor.getInt(i + 16));
        this.selectCoverId = (String) updateHelper.update(this.selectCoverId, CursorUtils.getCursorString(cursor, i + 23));
        DefaultLogger.d("peopleface", "reloadData for the face with server id:" + this.serverId);
        return updateHelper.isUpdated();
    }

    public PeopleFace(String str, String str2, String str3, long j) {
        this.serverId = str;
        this.type = str2;
        this.groupId = str3;
        this.serverTag = j;
    }

    @Override // com.miui.gallery.data.DBItem
    public String getId() {
        return this._id;
    }

    public boolean equalMainInfoWith(PeopleFace peopleFace) {
        return equalString(this._id, peopleFace._id) && equalString(this.serverId, peopleFace.serverId) && equalString(this.localGroupId, peopleFace.localGroupId) && equalString(this.peopleName, peopleFace.peopleName);
    }

    public final boolean equalString(String str, String str2) {
        if (str == null && str2 == null) {
            return true;
        }
        if (str == null && str2 != null) {
            return false;
        }
        if (str != null && str2 == null) {
            return false;
        }
        return str.equalsIgnoreCase(str2);
    }

    @Override // com.miui.gallery.data.ServerItem
    public long getServerTag() {
        return this.serverTag;
    }

    @Override // com.miui.gallery.data.ServerItem
    public String getServerId() {
        return this.serverId;
    }
}
