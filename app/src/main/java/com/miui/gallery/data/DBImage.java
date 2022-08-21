package com.miui.gallery.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.cloud.CloudTableUtils;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.CryptoUtil;
import com.miui.gallery.util.CursorUtils;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.UpdateHelper;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class DBImage implements DBItem, ServerItem {
    public static final ArrayList<ExifDataConst> sExifDataConst;
    public static final Object sSecretKeyLock;
    public String mAppKey;
    public long mAttributes;
    public String mBabyInfoJson;
    public String mCreatorId;
    public long mDateModified;
    public long mDatetaken;
    public String mDescription;
    @Deprecated
    public String mDownloadFile;
    public long mDownloadFileAddTime;
    public int mDuration;
    public String mEditedColumns;
    public String mFileName;
    public String mFromLocalGroupId;
    public long mGroupId;
    public String mId;
    public boolean mIsFavorite;
    public int mLables;
    public String mLatitudeStr;
    public String mLatitudeStrRef;
    public String mLocalFile;
    public int mLocalFlag;
    public String mLocalGroupId;
    public String mLocalImageId;
    public String mLongitudeStr;
    public String mLongitudeStrRef;
    public String mMicroThumbFile;
    public String mMimeType;
    public long mMixedDateTime;
    public String mPeopleFaceId;
    public byte[] mSecretKey;
    public String mServerId;
    public String mServerStatus;
    public long mServerTag;
    public int mServerType;
    public String mSha1;
    public String mShareAlbumId;
    public long mSize;
    public String mSourcePkg;
    public long mSpecialTypeFlags;
    public String mThumbnailFile;
    public String mTitle;
    public int mUbiFocusIndex;
    public int mUbiSubImageCount;
    public int mUbiSubIndex;
    public JSONObject mExifInfo = new JSONObject();
    public JSONObject mGeoInfo = new JSONObject();

    /* loaded from: classes.dex */
    public interface SubUbiImage {
        String getUbiLocalId();
    }

    public abstract String getAlbumId();

    public abstract Uri getBaseUri();

    public String getOriginSha1() {
        return "";
    }

    public abstract String getRequestId();

    public abstract String getTagId();

    public String getUbiServerId() {
        return "";
    }

    public boolean isShareItem() {
        return false;
    }

    public boolean isSubUbiFocus() {
        return false;
    }

    public boolean isUbiFocus() {
        return false;
    }

    public void setOriginInfo(String str, String str2) {
    }

    public abstract void setRequestAlbumId(String str);

    public void setShareId(String str) {
    }

    /* loaded from: classes.dex */
    public static class ExifDataConst {
        public final String cloudTagName;
        public final int columnIndex;
        public final String exifTagName;
        public final int exifValueType;

        public ExifDataConst(int i, String str, String str2, int i2) {
            this.columnIndex = i;
            this.cloudTagName = str;
            this.exifTagName = str2;
            this.exifValueType = i2;
        }
    }

    static {
        ArrayList<ExifDataConst> arrayList = new ArrayList<>();
        sExifDataConst = arrayList;
        arrayList.add(new ExifDataConst(14, "imageWidth", "ImageWidth", 0));
        arrayList.add(new ExifDataConst(15, "imageLength", "ImageLength", 0));
        arrayList.add(new ExifDataConst(16, "orientation", "Orientation", 0));
        arrayList.add(new ExifDataConst(17, "GPSLatitude", "GPSLatitude", 1));
        arrayList.add(new ExifDataConst(18, "GPSLongitude", "GPSLongitude", 1));
        arrayList.add(new ExifDataConst(19, "make", "Make", 1));
        arrayList.add(new ExifDataConst(20, "model", "Model", 1));
        arrayList.add(new ExifDataConst(21, "flash", "Flash", 0));
        arrayList.add(new ExifDataConst(22, "GPSLatitudeRef", "GPSLatitudeRef", 1));
        arrayList.add(new ExifDataConst(23, "GPSLongitudeRef", "GPSLongitudeRef", 1));
        arrayList.add(new ExifDataConst(24, "exposureTime", "ExposureTime", 1));
        arrayList.add(new ExifDataConst(25, "FNumber", "FNumber", 1));
        arrayList.add(new ExifDataConst(26, "ISOSpeedRatings", "ISOSpeedRatings", 1));
        arrayList.add(new ExifDataConst(27, "GPSAltitude", "GPSAltitude", 1));
        arrayList.add(new ExifDataConst(28, "GPSAltitudeRef", "GPSAltitudeRef", 0));
        arrayList.add(new ExifDataConst(29, "GPSTimeStamp", "GPSTimeStamp", 1));
        arrayList.add(new ExifDataConst(30, "GPSDateStamp", "GPSDateStamp", 1));
        arrayList.add(new ExifDataConst(31, "whiteBalance", "WhiteBalance", 0));
        arrayList.add(new ExifDataConst(32, "focalLength", "FocalLength", 1));
        arrayList.add(new ExifDataConst(33, "GPSProcessingMethod", "GPSProcessingMethod", 1));
        arrayList.add(new ExifDataConst(34, "dateTime", "DateTime", 1));
        sSecretKeyLock = new Object();
    }

    public DBImage(Cursor cursor) {
        reloadData(cursor);
    }

    public boolean reloadData(Cursor cursor) {
        boolean z;
        UpdateHelper updateHelper = new UpdateHelper();
        this.mId = (String) updateHelper.update(this.mId, CursorUtils.getCursorString(cursor, 0));
        this.mGroupId = updateHelper.update(this.mGroupId, cursor.getLong(1));
        this.mSize = updateHelper.update(this.mSize, cursor.getLong(2));
        this.mDateModified = updateHelper.update(this.mDateModified, cursor.getLong(3));
        this.mMimeType = (String) updateHelper.update(this.mMimeType, CursorUtils.getCursorString(cursor, 4));
        this.mTitle = (String) updateHelper.update(this.mTitle, CursorUtils.getCursorString(cursor, 5));
        this.mDescription = (String) updateHelper.update(this.mDescription, CursorUtils.getCursorString(cursor, 6));
        this.mFileName = (String) updateHelper.update(this.mFileName, CursorUtils.getCursorString(cursor, 7));
        this.mDatetaken = updateHelper.update(this.mDatetaken, cursor.getLong(8));
        this.mDuration = updateHelper.update(this.mDuration, cursor.getInt(9));
        this.mServerId = (String) updateHelper.update(this.mServerId, CursorUtils.getCursorString(cursor, 10));
        this.mServerType = updateHelper.update(this.mServerType, cursor.getInt(11));
        this.mServerTag = updateHelper.update(this.mServerTag, cursor.getLong(13));
        this.mServerStatus = (String) updateHelper.update(this.mServerStatus, CursorUtils.getCursorString(cursor, 12));
        this.mLocalFlag = updateHelper.update(this.mLocalFlag, cursor.getInt(35));
        this.mThumbnailFile = (String) updateHelper.update(this.mThumbnailFile, CursorUtils.getCursorString(cursor, 36));
        this.mDownloadFile = (String) updateHelper.update(this.mDownloadFile, CursorUtils.getCursorString(cursor, 37));
        this.mMicroThumbFile = (String) updateHelper.update(this.mMicroThumbFile, CursorUtils.getCursorString(cursor, 41));
        this.mLocalFile = (String) updateHelper.update(this.mLocalFile, CursorUtils.getCursorString(cursor, 38));
        this.mSha1 = (String) updateHelper.update(this.mSha1, CursorUtils.getCursorString(cursor, 39));
        this.mLocalGroupId = (String) updateHelper.update(this.mLocalGroupId, CursorUtils.getCursorString(cursor, 42));
        this.mLocalImageId = (String) updateHelper.update(this.mLocalImageId, CursorUtils.getCursorString(cursor, 43));
        this.mShareAlbumId = (String) updateHelper.update(this.mShareAlbumId, CursorUtils.getCursorString(cursor, 44));
        this.mLongitudeStr = (String) updateHelper.update(this.mLongitudeStr, cursor.getString(18));
        this.mLatitudeStr = (String) updateHelper.update(this.mLatitudeStr, cursor.getString(17));
        this.mLongitudeStrRef = (String) updateHelper.update(this.mLongitudeStrRef, cursor.getString(23));
        this.mLatitudeStrRef = (String) updateHelper.update(this.mLatitudeStrRef, cursor.getString(22));
        try {
            Iterator<ExifDataConst> it = sExifDataConst.iterator();
            z = false;
            while (it.hasNext()) {
                try {
                    ExifDataConst next = it.next();
                    int i = next.exifValueType;
                    if (i != 0) {
                        if (i == 1) {
                            String string = cursor.getString(next.columnIndex);
                            if (string == null) {
                                if (this.mExifInfo.has(next.cloudTagName)) {
                                    try {
                                        this.mExifInfo.put(next.cloudTagName, (Object) null);
                                        z = true;
                                    } catch (JSONException unused) {
                                        z = true;
                                        DefaultLogger.e("DBCloud", "DBCloud(Cursor c): put JSON error");
                                        if (!z) {
                                        }
                                    }
                                } else {
                                    continue;
                                }
                            } else {
                                if (this.mExifInfo.has(next.cloudTagName)) {
                                    string = (String) updateHelper.update(this.mExifInfo.getString(next.cloudTagName), string);
                                } else {
                                    z = true;
                                }
                                this.mExifInfo.put(next.cloudTagName, string);
                            }
                        } else {
                            DefaultLogger.e("DBImage", "exifDataConst.exifValueType is wrong: " + next.exifValueType);
                        }
                    } else if (cursor.isNull(next.columnIndex)) {
                        if (this.mExifInfo.has(next.cloudTagName)) {
                            this.mExifInfo.put(next.cloudTagName, (Object) null);
                            z = true;
                        }
                    } else {
                        int i2 = cursor.getInt(next.columnIndex);
                        if (this.mExifInfo.has(next.cloudTagName)) {
                            i2 = updateHelper.update(this.mExifInfo.getInt(next.cloudTagName), i2);
                        } else {
                            z = true;
                        }
                        this.mExifInfo.put(next.cloudTagName, i2);
                    }
                } catch (JSONException unused2) {
                }
            }
        } catch (JSONException unused3) {
            z = false;
        }
        return !z || updateHelper.isUpdated();
    }

    public JSONObject toJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            getBasicValues(jSONObject);
            if (getGroupId() != 0) {
                jSONObject.put("groupId", getGroupId());
            }
            if (!TextUtils.isEmpty(getServerId())) {
                jSONObject.put("id", getServerId());
            }
            if (!TextUtils.isEmpty(getServerStatus())) {
                jSONObject.put("status", getServerStatus());
            }
            if (getServerTag() != 0) {
                jSONObject.put(nexExportFormat.TAG_FORMAT_TAG, getServerTag());
            }
            int i = this.mUbiSubImageCount;
            if (i > 0) {
                jSONObject.put("ubiSubImageCount", i);
                jSONObject.put("currentFocusIndex", this.mUbiFocusIndex);
                jSONObject.put("ubiDefaultIndex", this.mUbiSubIndex);
            }
            if (!TextUtils.isEmpty(getOriginSha1())) {
                jSONObject.put("originSha1", getOriginSha1());
            }
        } catch (JSONException unused) {
            DefaultLogger.e("DBCloud", "toJSONObject: get JSON error");
        }
        return jSONObject;
    }

    public void getBasicValues(JSONObject jSONObject) throws JSONException {
        String collectMediaDescription;
        jSONObject.put("fileName", getFileName());
        jSONObject.put("dateTaken", getDatetaken());
        jSONObject.put("dateModified", getDateModified());
        int serverType = getServerType();
        if (serverType == 1 || serverType == 2) {
            collectMediaDescription = CloudUtils.collectMediaDescription(this);
        } else {
            collectMediaDescription = getDescription();
        }
        if (!TextUtils.isEmpty(collectMediaDescription)) {
            jSONObject.put("description", collectMediaDescription);
        }
        int serverType2 = getServerType();
        if (serverType2 == 1) {
            jSONObject.put(nexExportFormat.TAG_FORMAT_TYPE, "image");
            jSONObject.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, getSize());
            jSONObject.put("mimeType", getMimeType());
            jSONObject.put("title", getTitle());
            jSONObject.put("sha1", getSha1());
        } else if (serverType2 == 2) {
            jSONObject.put(nexExportFormat.TAG_FORMAT_TYPE, "video");
            jSONObject.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, getSize());
            jSONObject.put("mimeType", getMimeType());
            jSONObject.put("title", getTitle());
            jSONObject.put("sha1", getSha1());
            jSONObject.put("duration", getDuration());
        } else {
            DefaultLogger.e("DBImage", "type is invalid:" + getServerType());
        }
        if (getExifInfo().length() > 0) {
            jSONObject.put("exifInfo", getExifInfo());
        }
    }

    public JSONObject toJsonObjectForSubUbi() {
        JSONObject jSONObject = new JSONObject();
        try {
            try {
                getBasicValues(jSONObject);
            } catch (JSONException unused) {
                DefaultLogger.e("DBCloud", "toJsonObjectForSubUbi: get JSON error");
            }
            try {
                jSONObject.put(nexExportFormat.TAG_FORMAT_TYPE, (Object) null);
            } catch (JSONException unused2) {
                DefaultLogger.e("DBCloud", "put null JSON_TAG_TYPE failed");
            }
            return jSONObject;
        } catch (Throwable th) {
            try {
                jSONObject.put(nexExportFormat.TAG_FORMAT_TYPE, (Object) null);
            } catch (JSONException unused3) {
                DefaultLogger.e("DBCloud", "put null JSON_TAG_TYPE failed");
            }
            throw th;
        }
    }

    @Override // com.miui.gallery.data.DBItem
    public String getId() {
        return this.mId;
    }

    public int getServerType() {
        return this.mServerType;
    }

    public long getGroupId() {
        return this.mGroupId;
    }

    public void setGroupId(long j) {
        this.mGroupId = j;
    }

    public String getFileName() {
        return this.mFileName;
    }

    public String getSha1FileName() {
        return this.mSha1 + "." + BaseFileUtils.getExtensionWithFileName(this.mFileName);
    }

    public String getSha1Thumbnail() {
        return this.mSha1 + ".jpg";
    }

    public String getSha1ThumbnailSA() {
        return CloudUtils.SecretAlbumUtils.getSha1ThumbnailSA(this.mSha1, getMD5Key(), isVideoType());
    }

    public String getEncodedFileName() {
        return CloudUtils.SecretAlbumUtils.getEncryptedFileName(this.mFileName, getMD5Key(), isVideoType());
    }

    public String getSha1() {
        return this.mSha1;
    }

    public void setSha1(String str) {
        this.mSha1 = str;
    }

    public int getLocalFlag() {
        return this.mLocalFlag;
    }

    public String getThumbnailFile() {
        return this.mThumbnailFile;
    }

    public void setThumbnailFile(String str) {
        this.mThumbnailFile = str;
    }

    public String getMicroThumbnailFile() {
        return this.mMicroThumbFile;
    }

    public void setMicroThumbFile(String str) {
        this.mMicroThumbFile = str;
    }

    public String getLocalFile() {
        return this.mLocalFile;
    }

    public void setLocalFile(String str) {
        this.mLocalFile = str;
    }

    public String getLocalGroupId() {
        return this.mLocalGroupId;
    }

    public String getLocalImageId() {
        return this.mLocalImageId;
    }

    public long getSize() {
        return this.mSize;
    }

    public long getDateModified() {
        return this.mDateModified;
    }

    public String getMimeType() {
        return this.mMimeType;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getSourcePkg() {
        return this.mSourcePkg;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public long getDatetaken() {
        return this.mDatetaken;
    }

    public int getDuration() {
        return this.mDuration;
    }

    @Override // com.miui.gallery.data.ServerItem
    public String getServerId() {
        return this.mServerId;
    }

    public void setServerId(String str) {
        this.mServerId = str;
    }

    public String getAppKey() {
        return this.mAppKey;
    }

    @Override // com.miui.gallery.data.ServerItem
    public long getServerTag() {
        return this.mServerTag;
    }

    public String getServerStatus() {
        return this.mServerStatus;
    }

    public JSONObject getExifInfo() {
        return this.mExifInfo;
    }

    public JSONObject getGeoInfo() {
        return this.mGeoInfo;
    }

    public Integer getJsonExifInteger(String str, Integer num) {
        try {
            if (getExifInfo().has(str)) {
                return Integer.valueOf(getExifInfo().getInt(str));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return num;
    }

    public String getJsonExifString(String str) {
        try {
            if (!getExifInfo().has(str)) {
                return null;
            }
            return getExifInfo().getString(str);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getShareAlbumId() {
        return this.mShareAlbumId;
    }

    public void setShareAlbumId(String str) {
        this.mShareAlbumId = str;
    }

    public String getCreatorId() {
        return this.mCreatorId;
    }

    public boolean isItemType() {
        return isImageType() || isVideoType();
    }

    public boolean isVideoType() {
        return getServerType() == 2;
    }

    public boolean isImageType() {
        return getServerType() == 1;
    }

    public long getMixedDateTime() {
        return this.mMixedDateTime;
    }

    public int getSubUbiImageCount() {
        return this.mUbiSubImageCount;
    }

    public void setSubUbiImageCount(int i) {
        this.mUbiSubImageCount = i;
    }

    public int getSubUbiIndex() {
        return this.mUbiSubIndex;
    }

    public String getEditedColumns() {
        return this.mEditedColumns;
    }

    public String getFromLocalGroupId() {
        return this.mFromLocalGroupId;
    }

    public boolean isDeleteItem() {
        return "deleted".equalsIgnoreCase(this.mServerStatus) || "purged".equalsIgnoreCase(this.mServerStatus);
    }

    public boolean isSecretItem() {
        if (!ApplicationHelper.isSecretAlbumFeatureOpen()) {
            return false;
        }
        return CloudTableUtils.isSecretAlbum(String.valueOf(getGroupId()), getLocalGroupId());
    }

    public byte[] getSecretKey() {
        if (CloudUtils.SecretAlbumUtils.isEmpty(this.mSecretKey)) {
            generateSecretKey();
        }
        return this.mSecretKey;
    }

    public byte[] getSecretKeyNoGenerate() {
        return this.mSecretKey;
    }

    public String getMD5Key() {
        return CloudUtils.SecretAlbumUtils.getMD5Key(getSecretKey());
    }

    public final void generateSecretKey() {
        synchronized (sSecretKeyLock) {
            Uri baseUri = getBaseUri();
            String str = "_id = '" + getId() + "'";
            byte[] bArr = (byte[]) GalleryUtils.safeQuery(baseUri, new String[]{"secretKey"}, str, (String[]) null, (String) null, new GalleryUtils.QueryHandler<byte[]>() { // from class: com.miui.gallery.data.DBImage.1
                @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
                /* renamed from: handle  reason: collision with other method in class */
                public byte[] mo1712handle(Cursor cursor) {
                    if (cursor == null || !cursor.moveToFirst()) {
                        return null;
                    }
                    return cursor.getBlob(0);
                }
            });
            if (bArr == null) {
                bArr = CryptoUtil.generateRandomKey();
                ContentValues contentValues = new ContentValues();
                contentValues.put("secretKey", bArr);
                GalleryUtils.safeUpdate(baseUri, contentValues, str, null);
            } else {
                DefaultLogger.d("DBImage", "generateSecretKey, secret key exists:" + Arrays.toString(bArr));
            }
            this.mSecretKey = bArr;
        }
    }

    public boolean isFavorite() {
        return this.mIsFavorite;
    }

    public long getSpecialTypeFlags() {
        return this.mSpecialTypeFlags;
    }

    public String toString() {
        return super.toString() + "[ _id = " + getId() + ", albumId = " + getAlbumId() + "]";
    }
}
