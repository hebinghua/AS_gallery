package com.miui.gallery.data;

import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.ThumbnailInfo;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.util.CursorUtils;
import com.miui.gallery.util.UpdateHelper;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DBAlbum implements DBItem, ServerItem {
    public long mAlbumSize;
    public long mAttributes;
    public long mCoverId;
    public String mCoverPath;
    public String mCoverSha1;
    public long mCoverSize;
    public String mCoverSyncState;
    public long mDateModified;
    public long mDateTaken;
    public String mEditedColumns;
    public String mExtraInfo;
    public Album.ExtraInfo mExtraInfoBean;
    public String mId;
    public long mLocalFlag;
    public String mLocalPath;
    public String mName;
    public int mPhotoCount;
    public long mRealDateModified;
    public String mServerId;
    public String mServerStatus;
    public long mServerTag;
    public long mSortBy;
    public String mSortInfo;

    public DBAlbum(Cursor cursor) {
        reloadData(cursor);
    }

    public boolean reloadData(Cursor cursor) {
        if (cursor == null) {
            return false;
        }
        UpdateHelper updateHelper = new UpdateHelper();
        this.mId = (String) updateHelper.update(this.mId, CursorUtils.getCursorString(cursor, 0));
        this.mName = (String) updateHelper.update(this.mName, CursorUtils.getCursorString(cursor, 1));
        this.mAttributes = updateHelper.update(this.mAttributes, CursorUtils.getCursorLong(cursor, 2));
        this.mCoverId = updateHelper.update(this.mCoverId, CursorUtils.getCursorLong(cursor, 3));
        this.mDateTaken = updateHelper.update(this.mDateTaken, CursorUtils.getCursorLong(cursor, 4));
        this.mDateModified = updateHelper.update(this.mDateModified, CursorUtils.getCursorLong(cursor, 5));
        this.mRealDateModified = updateHelper.update(this.mRealDateModified, CursorUtils.getCursorLong(cursor, 6));
        String str = (String) updateHelper.update(this.mEditedColumns, CursorUtils.getCursorString(cursor, 9));
        this.mEditedColumns = str;
        this.mEditedColumns = (String) updateHelper.update(str, CursorUtils.getCursorString(cursor, 9));
        this.mSortInfo = (String) updateHelper.update(this.mSortInfo, CursorUtils.getCursorString(cursor, 15));
        this.mExtraInfo = (String) updateHelper.update(this.mExtraInfo, CursorUtils.getCursorString(cursor, 14));
        this.mLocalFlag = updateHelper.update(this.mLocalFlag, CursorUtils.getCursorLong(cursor, 8));
        this.mServerId = (String) updateHelper.update(this.mServerId, CursorUtils.getCursorString(cursor, 10));
        this.mServerStatus = (String) updateHelper.update(this.mServerStatus, CursorUtils.getCursorString(cursor, 12));
        this.mServerTag = updateHelper.update(this.mServerTag, CursorUtils.getCursorLong(cursor, 11));
        this.mLocalPath = (String) updateHelper.update(this.mLocalPath, CursorUtils.getCursorString(cursor, 13));
        this.mPhotoCount = updateHelper.update(this.mPhotoCount, CursorUtils.getCursorInt(cursor, 15));
        this.mCoverSyncState = (String) updateHelper.update(this.mCoverSyncState, CursorUtils.getCursorString(cursor, 16));
        this.mCoverSize = updateHelper.update(this.mCoverSize, CursorUtils.getCursorLong(cursor, 17));
        this.mCoverPath = (String) updateHelper.update(this.mCoverPath, CursorUtils.getCursorString(cursor, 18));
        this.mCoverSha1 = (String) updateHelper.update(this.mCoverSha1, CursorUtils.getCursorString(cursor, 19));
        this.mAlbumSize = updateHelper.update(this.mAlbumSize, CursorUtils.getCursorInt(cursor, 20));
        this.mSortBy = updateHelper.update(this.mSortBy, CursorUtils.getCursorInt(cursor, 21));
        parseExtraInfo();
        return updateHelper.isUpdated();
    }

    public final void parseExtraInfo() {
        if (TextUtils.isEmpty(this.mExtraInfo)) {
            return;
        }
        this.mExtraInfoBean = Album.ExtraInfo.newInstance(this.mExtraInfo);
    }

    @Override // com.miui.gallery.data.DBItem
    public String getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }

    public long getAttributes() {
        return this.mAttributes;
    }

    public long getDateTaken() {
        return this.mDateTaken;
    }

    public long getDateModified() {
        return this.mDateModified;
    }

    public long getLocalFlag() {
        return this.mLocalFlag;
    }

    @Override // com.miui.gallery.data.ServerItem
    public String getServerId() {
        return this.mServerId;
    }

    public String getServerStatus() {
        return this.mServerStatus;
    }

    @Override // com.miui.gallery.data.ServerItem
    public long getServerTag() {
        return this.mServerTag;
    }

    public String getLocalPath() {
        return this.mLocalPath;
    }

    public long getCoverId() {
        return this.mCoverId;
    }

    public String getEditedColumns() {
        return this.mEditedColumns;
    }

    public ThumbnailInfo getThumbnailInfo() {
        Album.ExtraInfo extraInfo = this.mExtraInfoBean;
        if (extraInfo == null || TextUtils.isEmpty(extraInfo.getThumbnailInfo())) {
            return null;
        }
        return new ThumbnailInfo(Integer.parseInt(this.mId), Album.isShareAlbum(Long.parseLong(this.mId)), this.mExtraInfoBean.getThumbnailInfo());
    }

    public String getRequestId() {
        return this.mServerId;
    }

    public boolean isShareAlbum() {
        return Album.isShareAlbum(Long.parseLong(this.mId));
    }

    public String getAppKey() {
        Album.ExtraInfo extraInfo = this.mExtraInfoBean;
        if (extraInfo != null) {
            return extraInfo.getAppKey();
        }
        return null;
    }

    public Album.ExtraInfo.DescriptionBean getDescription() {
        Album.ExtraInfo extraInfo = this.mExtraInfoBean;
        if (extraInfo != null) {
            return extraInfo.getDescriptionBean();
        }
        return null;
    }

    public String getBabyInfo() {
        Album.ExtraInfo extraInfo = this.mExtraInfoBean;
        if (extraInfo != null) {
            return extraInfo.getBabyInfo();
        }
        return null;
    }

    public String getPeopleId() {
        Album.ExtraInfo extraInfo = this.mExtraInfoBean;
        if (extraInfo != null) {
            return extraInfo.getPeopleId();
        }
        return null;
    }

    public boolean isPublic() {
        Album.ExtraInfo extraInfo = this.mExtraInfoBean;
        return extraInfo != null && extraInfo.isPublic();
    }

    public String getPublicUrl() {
        Album.ExtraInfo extraInfo = this.mExtraInfoBean;
        if (extraInfo != null) {
            return extraInfo.getPublicUrl();
        }
        return null;
    }

    public void setCoverServerId(String str) {
        this.mCoverId = TextUtils.isEmpty(str) ? 0L : Long.parseLong(str);
    }

    public JSONObject toJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            getBasicValues(jSONObject);
            String collectAlbumDescription = collectAlbumDescription(this);
            if (!TextUtils.isEmpty(collectAlbumDescription)) {
                jSONObject.put("description", collectAlbumDescription);
            }
            ThumbnailInfo thumbnailInfo = getThumbnailInfo();
            if (thumbnailInfo != null && !TextUtils.isEmpty(thumbnailInfo.getFaceId())) {
                jSONObject.put("faceId", thumbnailInfo.getFaceId());
            }
            if (ApplicationHelper.isAutoUploadFeatureOpen() && !TextUtils.isEmpty(getAppKey())) {
                jSONObject.put("appKey", getAppKey());
            }
            if (ApplicationHelper.isBabyAlbumFeatureOpen() && !TextUtils.isEmpty(getBabyInfo())) {
                JSONObject jSONObject2 = new JSONObject(getBabyInfo());
                if (jSONObject2.has("localFlag")) {
                    jSONObject2.remove("localFlag");
                }
                JSONArray jSONArray = new JSONArray();
                jSONArray.put(0, jSONObject2);
                jSONObject.put("renderInfos", jSONArray);
            }
        } catch (JSONException unused) {
            DefaultLogger.e("DBCloud", "toJSONObject: get JSON error");
        }
        return jSONObject;
    }

    public void getBasicValues(JSONObject jSONObject) throws JSONException {
        ThumbnailInfo thumbnailInfo = getThumbnailInfo();
        if (thumbnailInfo != null && !TextUtils.isEmpty(thumbnailInfo.getFaceId())) {
            jSONObject.put("faceId", thumbnailInfo.getFaceId());
        }
        jSONObject.put("fileName", getName());
        jSONObject.put("dateTaken", getDateTaken());
        jSONObject.put("dateModified", getDateModified());
        if (!TextUtils.isEmpty(getServerId())) {
            jSONObject.put("id", getServerId());
        }
        if (!TextUtils.isEmpty(getServerStatus())) {
            jSONObject.put("status", getServerStatus());
        }
        if (getServerTag() != 0) {
            jSONObject.put(nexExportFormat.TAG_FORMAT_TAG, getServerTag());
        }
        jSONObject.put(nexExportFormat.TAG_FORMAT_TYPE, "group");
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x004b A[Catch: JSONException -> 0x0138, TryCatch #0 {JSONException -> 0x0138, blocks: (B:5:0x0014, B:7:0x001a, B:16:0x004b, B:17:0x0050, B:21:0x0076, B:25:0x0084, B:29:0x0092, B:33:0x00a1, B:37:0x00b0, B:41:0x00bf, B:45:0x00d0, B:49:0x00e7, B:53:0x00fe, B:57:0x0114, B:59:0x0126, B:60:0x0133, B:13:0x002f, B:8:0x001e, B:10:0x0024), top: B:65:0x0014, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0075  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0083  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0091  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00ad  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00e4  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00fd  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0113  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0126 A[Catch: JSONException -> 0x0138, TryCatch #0 {JSONException -> 0x0138, blocks: (B:5:0x0014, B:7:0x001a, B:16:0x004b, B:17:0x0050, B:21:0x0076, B:25:0x0084, B:29:0x0092, B:33:0x00a1, B:37:0x00b0, B:41:0x00bf, B:45:0x00d0, B:49:0x00e7, B:53:0x00fe, B:57:0x0114, B:59:0x0126, B:60:0x0133, B:13:0x002f, B:8:0x001e, B:10:0x0024), top: B:65:0x0014, inners: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String collectAlbumDescription(com.miui.gallery.data.DBAlbum r22) {
        /*
            Method dump skipped, instructions count: 318
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.data.DBAlbum.collectAlbumDescription(com.miui.gallery.data.DBAlbum):java.lang.String");
    }

    public Uri getBaseUri() {
        return GalleryCloudUtils.ALBUM_URI;
    }
}
