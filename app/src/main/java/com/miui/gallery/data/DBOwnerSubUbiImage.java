package com.miui.gallery.data;

import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.util.CursorUtils;
import com.miui.gallery.util.UbiFocusUtils;
import com.miui.gallery.util.UpdateHelper;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DBOwnerSubUbiImage extends DBImage implements DBImage.SubUbiImage {
    public boolean mHasQuery;
    public String mUbiLocalId;
    public String mUbiServerId;

    @Override // com.miui.gallery.data.DBImage
    public boolean isShareItem() {
        return false;
    }

    @Override // com.miui.gallery.data.DBImage
    public boolean isSubUbiFocus() {
        return true;
    }

    public DBOwnerSubUbiImage(Cursor cursor) {
        super(cursor);
    }

    @Override // com.miui.gallery.data.DBImage
    public Uri getBaseUri() {
        return GalleryCloudUtils.OWNER_SUB_UBIFOCUS_URI;
    }

    @Override // com.miui.gallery.data.DBImage
    public String getTagId() {
        return "DBOwnerSubUbiImage" + getId();
    }

    @Override // com.miui.gallery.data.DBImage
    public boolean reloadData(Cursor cursor) {
        boolean reloadData = super.reloadData(cursor);
        UpdateHelper updateHelper = new UpdateHelper();
        this.mDownloadFileAddTime = updateHelper.update(this.mDownloadFileAddTime, cursor.getLong(46));
        this.mMixedDateTime = updateHelper.update(this.mMixedDateTime, cursor.getLong(47));
        this.mUbiServerId = (String) updateHelper.update(this.mUbiServerId, CursorUtils.getCursorString(cursor, 48));
        this.mUbiLocalId = (String) updateHelper.update(this.mUbiLocalId, CursorUtils.getCursorString(cursor, 49));
        this.mUbiSubIndex = updateHelper.update(this.mUbiSubIndex, cursor.getInt(50));
        byte[] update = updateHelper.update(this.mSecretKey, cursor.getBlob(51));
        if (update != null) {
            this.mSecretKey = update;
        }
        return reloadData || updateHelper.isUpdated();
    }

    @Override // com.miui.gallery.data.DBImage
    public JSONObject toJSONObject() {
        JSONObject jSONObject = super.toJSONObject();
        return jSONObject == null ? new JSONObject() : jSONObject;
    }

    @Override // com.miui.gallery.data.DBImage
    public String getRequestId() {
        return getUbiServerId();
    }

    @Override // com.miui.gallery.data.DBImage
    public String getAlbumId() {
        return String.valueOf(getGroupId());
    }

    @Override // com.miui.gallery.data.DBImage
    public void setRequestAlbumId(String str) {
        setGroupId(Long.valueOf(str).longValue());
    }

    @Override // com.miui.gallery.data.DBImage
    public String getUbiServerId() {
        if (TextUtils.isEmpty(this.mUbiServerId)) {
            this.mUbiServerId = UbiFocusUtils.getUbiServerIdByUbiLocalId(this.mUbiLocalId, false);
        }
        return this.mUbiServerId;
    }

    @Override // com.miui.gallery.data.DBImage.SubUbiImage
    public String getUbiLocalId() {
        return this.mUbiLocalId;
    }

    @Override // com.miui.gallery.data.DBImage
    public int getSubUbiImageCount() {
        if (super.getSubUbiImageCount() <= 0 && !this.mHasQuery) {
            this.mHasQuery = true;
            setSubUbiImageCount(UbiFocusUtils.getSubUbiCount(false, getRequestId(), getUbiLocalId()));
        }
        return super.getSubUbiImageCount();
    }
}
