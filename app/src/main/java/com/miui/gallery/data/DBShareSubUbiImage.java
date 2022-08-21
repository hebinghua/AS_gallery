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
public class DBShareSubUbiImage extends DBImage implements DBImage.SubUbiImage {
    public boolean mHasQuery;
    public String mShareId;
    public String mUbiLocalId;
    public String mUbiServerId;

    @Override // com.miui.gallery.data.DBImage
    public boolean isShareItem() {
        return true;
    }

    @Override // com.miui.gallery.data.DBImage
    public boolean isSubUbiFocus() {
        return true;
    }

    public DBShareSubUbiImage(Cursor cursor) {
        super(cursor);
    }

    @Override // com.miui.gallery.data.DBImage
    public Uri getBaseUri() {
        return GalleryCloudUtils.SHARE_SUB_UBIFOCUS_URI;
    }

    @Override // com.miui.gallery.data.DBImage
    public String getTagId() {
        return "DBShareSubUbiImage" + getId();
    }

    @Override // com.miui.gallery.data.DBImage
    public boolean reloadData(Cursor cursor) {
        boolean reloadData = super.reloadData(cursor);
        UpdateHelper updateHelper = new UpdateHelper();
        this.mCreatorId = (String) updateHelper.update(this.mCreatorId, CursorUtils.getCursorString(cursor, 45));
        this.mShareId = (String) updateHelper.update(this.mShareId, CursorUtils.getCursorString(cursor, 46));
        this.mDownloadFileAddTime = updateHelper.update(this.mDownloadFileAddTime, cursor.getLong(48));
        this.mMixedDateTime = updateHelper.update(this.mMixedDateTime, cursor.getLong(49));
        this.mUbiServerId = (String) updateHelper.update(this.mUbiServerId, CursorUtils.getCursorString(cursor, 50));
        this.mUbiLocalId = (String) updateHelper.update(this.mUbiLocalId, CursorUtils.getCursorString(cursor, 51));
        this.mUbiSubIndex = updateHelper.update(this.mUbiSubIndex, cursor.getInt(52));
        byte[] update = updateHelper.update(this.mSecretKey, cursor.getBlob(53));
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
    public String getUbiServerId() {
        if (TextUtils.isEmpty(this.mUbiServerId)) {
            this.mUbiServerId = UbiFocusUtils.getUbiServerIdByUbiLocalId(this.mUbiLocalId, true);
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
            setSubUbiImageCount(UbiFocusUtils.getSubUbiCount(true, getRequestId(), getUbiLocalId()));
        }
        return super.getSubUbiImageCount();
    }
}
