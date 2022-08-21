package com.miui.gallery.data;

import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.util.CursorUtils;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.UpdateHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DBCloud extends DBImage {
    public String mAddress;
    public boolean mCanModified;
    public String mExtraGps;
    public boolean mIsPublic;
    public String mOriginFileName;
    public String mOriginSha1;
    public String mPublicUrl;
    public String mShareUrl;
    public String mThumbnailInfo;

    public DBCloud(Cursor cursor) {
        super(cursor);
    }

    @Override // com.miui.gallery.data.DBImage
    public Uri getBaseUri() {
        return GalleryCloudUtils.CLOUD_URI;
    }

    @Override // com.miui.gallery.data.DBImage
    public String getTagId() {
        return "DBCloud" + getId();
    }

    @Override // com.miui.gallery.data.DBImage
    public boolean reloadData(Cursor cursor) {
        boolean reloadData = super.reloadData(cursor);
        UpdateHelper updateHelper = new UpdateHelper();
        this.mCanModified = updateHelper.update(this.mCanModified ? 1 : 0, cursor.getInt(45)) == 1;
        this.mShareUrl = (String) updateHelper.update(this.mShareUrl, CursorUtils.getCursorString(cursor, 46));
        this.mCreatorId = (String) updateHelper.update(this.mCreatorId, CursorUtils.getCursorString(cursor, 50));
        this.mIsPublic = updateHelper.update(this.mIsPublic ? 1 : 0, cursor.getInt(51)) == 1;
        this.mPublicUrl = (String) updateHelper.update(this.mPublicUrl, CursorUtils.getCursorString(cursor, 52));
        this.mDownloadFileAddTime = updateHelper.update(this.mDownloadFileAddTime, cursor.getLong(54));
        this.mMixedDateTime = updateHelper.update(this.mMixedDateTime, cursor.getLong(55));
        this.mUbiSubImageCount = updateHelper.update(this.mUbiSubImageCount, cursor.getInt(56));
        this.mUbiFocusIndex = updateHelper.update(this.mUbiFocusIndex, cursor.getInt(57));
        this.mUbiSubIndex = updateHelper.update(this.mUbiSubIndex, cursor.getInt(58));
        this.mLables = updateHelper.update(this.mLables, cursor.getInt(65));
        this.mEditedColumns = (String) updateHelper.update(this.mEditedColumns, cursor.getString(59));
        this.mFromLocalGroupId = (String) updateHelper.update(this.mFromLocalGroupId, cursor.getString(60));
        byte[] update = updateHelper.update(this.mSecretKey, cursor.getBlob(61));
        if (update != null) {
            this.mSecretKey = update;
        }
        this.mAppKey = (String) updateHelper.update(this.mAppKey, cursor.getString(62));
        this.mBabyInfoJson = (String) updateHelper.update(this.mBabyInfoJson, CursorUtils.getCursorString(cursor, 63));
        this.mPeopleFaceId = (String) updateHelper.update(this.mPeopleFaceId, CursorUtils.getCursorString(cursor, 64));
        this.mThumbnailInfo = (String) updateHelper.update(this.mThumbnailInfo, CursorUtils.getCursorString(cursor, 66));
        this.mAttributes = updateHelper.update(this.mAttributes, cursor.getLong(68));
        this.mAddress = (String) updateHelper.update(this.mAddress, cursor.getString(70));
        this.mExtraGps = (String) updateHelper.update(this.mExtraGps, cursor.getString(69));
        this.mSpecialTypeFlags = updateHelper.update(this.mSpecialTypeFlags, cursor.getLong(71));
        try {
            if (!TextUtils.isEmpty(this.mAddress)) {
                boolean isEmpty = TextUtils.isEmpty(this.mExtraGps);
                String str = this.mExtraGps;
                if (isEmpty) {
                    str = LocationManager.formatExifGpsString(this.mLatitudeStr, this.mLongitudeStr, this.mLatitudeStrRef, this.mLongitudeStrRef);
                }
                this.mGeoInfo.put("isAccurate", isEmpty);
                this.mGeoInfo.put("gps", str);
                String str2 = null;
                try {
                    JSONArray jSONArray = new JSONArray(this.mAddress);
                    this.mGeoInfo.put("addressList", jSONArray);
                    if (jSONArray.length() > 0) {
                        str2 = jSONArray.optString(0);
                    }
                } catch (Exception unused) {
                    str2 = this.mAddress;
                }
                if (!TextUtils.isEmpty(str2)) {
                    try {
                        this.mGeoInfo.put("address", new JSONObject(str2));
                    } catch (Exception e) {
                        DefaultLogger.w("DBCloud", "Failed to convert Address info, %s", e);
                    }
                }
            }
        } catch (JSONException unused2) {
            DefaultLogger.e("DBCloud", "DBCloud(Cursor c): put JSON error");
        }
        if (!TextUtils.isEmpty(getId())) {
            Boolean bool = (Boolean) GalleryUtils.safeQuery("favorites", new String[]{"isFavorite"}, "cloud_id = ?", new String[]{getId()}, (String) null, new GalleryUtils.QueryHandler<Boolean>() { // from class: com.miui.gallery.data.DBCloud.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
                /* renamed from: handle */
                public Boolean mo1712handle(Cursor cursor2) {
                    boolean z = false;
                    if (cursor2 != null && cursor2.moveToFirst() && cursor2.getInt(0) > 0) {
                        z = true;
                    }
                    return Boolean.valueOf(z);
                }
            });
            this.mIsFavorite = ((Boolean) updateHelper.update(Boolean.valueOf(this.mIsFavorite), Boolean.valueOf(bool != null && bool.booleanValue()))).booleanValue();
        }
        this.mSourcePkg = (String) updateHelper.update(this.mSourcePkg, cursor.getString(75));
        return reloadData || updateHelper.isUpdated();
    }

    @Override // com.miui.gallery.data.DBImage
    public JSONObject toJSONObject() {
        JSONObject jSONObject = super.toJSONObject();
        return jSONObject == null ? new JSONObject() : jSONObject;
    }

    @Override // com.miui.gallery.data.DBImage
    public void getBasicValues(JSONObject jSONObject) throws JSONException {
        super.getBasicValues(jSONObject);
        if (getServerType() == 0) {
            DefaultLogger.e("DBCloud", "error call:%s", TextUtils.join("\n\t", Thread.currentThread().getStackTrace()));
        }
        if (getServerType() == 1 || getServerType() == 2) {
            jSONObject.put("isFavorite", isFavorite());
        }
        if (getServerType() == 1) {
            jSONObject.put("sourcePackage", getSourcePkg());
        }
    }

    @Override // com.miui.gallery.data.DBImage
    public void setRequestAlbumId(String str) {
        setGroupId(Long.valueOf(str).longValue());
    }

    @Override // com.miui.gallery.data.DBImage
    public String getRequestId() {
        return getServerId();
    }

    @Override // com.miui.gallery.data.DBImage
    public String getAlbumId() {
        return String.valueOf(getGroupId());
    }

    @Override // com.miui.gallery.data.DBImage
    public boolean isUbiFocus() {
        return this.mUbiSubImageCount > 0;
    }

    @Override // com.miui.gallery.data.DBImage
    public void setOriginInfo(String str, String str2) {
        this.mOriginSha1 = str;
        this.mOriginFileName = str2;
    }

    @Override // com.miui.gallery.data.DBImage
    public String getOriginSha1() {
        return this.mOriginSha1;
    }
}
