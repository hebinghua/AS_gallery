package com.miui.gallery.data;

import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.util.CursorUtils;
import com.miui.gallery.util.UpdateHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DBShareImage extends DBImage {
    public String mShareId;

    @Override // com.miui.gallery.data.DBImage
    public boolean isShareItem() {
        return true;
    }

    public DBShareImage(Cursor cursor) {
        super(cursor);
    }

    @Override // com.miui.gallery.data.DBImage
    public Uri getBaseUri() {
        return GalleryCloudUtils.SHARE_IMAGE_URI;
    }

    @Override // com.miui.gallery.data.DBImage
    public String getTagId() {
        return "DBShareImage" + getId();
    }

    @Override // com.miui.gallery.data.DBImage
    public boolean reloadData(Cursor cursor) {
        boolean reloadData = super.reloadData(cursor);
        UpdateHelper updateHelper = new UpdateHelper();
        this.mCreatorId = (String) updateHelper.update(this.mCreatorId, CursorUtils.getCursorString(cursor, 45));
        this.mShareId = (String) updateHelper.update(this.mShareId, CursorUtils.getCursorString(cursor, 46));
        this.mDownloadFileAddTime = updateHelper.update(this.mDownloadFileAddTime, cursor.getLong(48));
        this.mMixedDateTime = updateHelper.update(this.mMixedDateTime, cursor.getLong(49));
        this.mUbiSubImageCount = updateHelper.update(this.mUbiSubImageCount, cursor.getInt(50));
        this.mUbiFocusIndex = updateHelper.update(this.mUbiFocusIndex, cursor.getInt(51));
        this.mUbiSubIndex = updateHelper.update(this.mUbiSubIndex, cursor.getInt(52));
        this.mLables = updateHelper.update(this.mLables, cursor.getInt(56));
        this.mEditedColumns = (String) updateHelper.update(this.mEditedColumns, cursor.getString(53));
        this.mFromLocalGroupId = (String) updateHelper.update(this.mFromLocalGroupId, cursor.getString(54));
        byte[] update = updateHelper.update(this.mSecretKey, cursor.getBlob(55));
        if (update != null) {
            this.mSecretKey = update;
        }
        this.mSpecialTypeFlags = updateHelper.update(this.mSpecialTypeFlags, cursor.getLong(60));
        return reloadData || updateHelper.isUpdated();
    }

    @Override // com.miui.gallery.data.DBImage
    public JSONObject toJSONObject() {
        JSONObject jSONObject = super.toJSONObject();
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        try {
            if (!TextUtils.isEmpty(getCreatorId())) {
                jSONObject.put("creatorId", getCreatorId());
            }
            if (!TextUtils.isEmpty(getShareId())) {
                jSONObject.put("shareId", getShareId());
            }
        } catch (JSONException unused) {
            DefaultLogger.e("DBCloud", "toJSONObject: get JSON error");
        }
        return jSONObject;
    }

    public String getShareId() {
        return this.mShareId;
    }

    @Override // com.miui.gallery.data.DBImage
    public void setShareId(String str) {
        this.mShareId = str;
    }

    @Override // com.miui.gallery.data.DBImage
    public void setRequestAlbumId(String str) {
        setShareAlbumId(str);
    }

    @Override // com.miui.gallery.data.DBImage
    public String getRequestId() {
        return getShareId();
    }

    @Override // com.miui.gallery.data.DBImage
    public String getAlbumId() {
        return getShareAlbumId();
    }

    @Override // com.miui.gallery.data.DBImage
    public boolean isUbiFocus() {
        return this.mUbiSubImageCount > 0;
    }
}
