package com.miui.gallery.provider.deprecated;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.CheckResult;
import com.miui.gallery.cloud.CloudGroupUrlProvider;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.data.DBShareAlbum;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.Utils;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class ThumbnailInfo {
    public String mBgImageId;
    public long mBgImageLocalId = -1;
    public String mBgUrl;
    public String mCoverImageId;
    public String mCoverUrl;
    public String mFaceId;
    public FaceInfo mFaceInfo;
    public FaceRegionRectF mFaceRegion;
    public String mFaceUrl;
    public final long mGroupLocalId;
    public final boolean mIsSharerAlbum;
    public long mLastTimeRequest;

    /* loaded from: classes2.dex */
    public static class FaceInfo {
        public final double eyeLeftXScale;
        public final double eyeLeftYScale;
        public final double eyeRightXScale;
        public final double eyeRightYScale;
        public final double faceHScale;
        public final int faceRegionOrientation;
        public final double faceWScale;
        public final double faceXScale;
        public final double faceYScale;

        public FaceInfo(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, int i) {
            this.eyeLeftXScale = d;
            this.eyeLeftYScale = d2;
            this.eyeRightXScale = d3;
            this.eyeRightYScale = d4;
            this.faceXScale = d5;
            this.faceYScale = d6;
            this.faceWScale = d7;
            this.faceHScale = d8;
            this.faceRegionOrientation = i;
        }

        public String toJSON() throws JSONException {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("eyeLeftXScale", this.eyeLeftXScale);
            jSONObject.put("eyeLeftYScale", this.eyeLeftYScale);
            jSONObject.put("eyeRightXScale", this.eyeRightXScale);
            jSONObject.put("eyeRightYScale", this.eyeRightYScale);
            jSONObject.put("faceXScale", this.faceXScale);
            jSONObject.put("faceYScale", this.faceYScale);
            jSONObject.put("faceWScale", this.faceWScale);
            jSONObject.put("faceHScale", this.faceHScale);
            jSONObject.put("faceOrientation", this.faceRegionOrientation);
            return jSONObject.toString();
        }

        public FaceRegionRectF getFacePosition() {
            double d = this.faceXScale;
            double d2 = this.faceYScale;
            return new FaceRegionRectF((float) d, (float) d2, (float) (d + this.faceWScale), (float) (d2 + this.faceHScale), this.faceRegionOrientation);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof FaceInfo)) {
                return false;
            }
            FaceInfo faceInfo = (FaceInfo) obj;
            return Utils.doubleEquals(this.eyeLeftXScale, faceInfo.eyeLeftXScale) && Utils.doubleEquals(this.eyeLeftYScale, faceInfo.eyeLeftYScale) && Utils.doubleEquals(this.eyeRightXScale, faceInfo.eyeRightXScale) && Utils.doubleEquals(this.eyeRightYScale, faceInfo.eyeRightYScale) && Utils.doubleEquals(this.faceXScale, faceInfo.faceXScale) && Utils.doubleEquals(this.faceYScale, faceInfo.faceYScale) && Utils.doubleEquals(this.faceWScale, faceInfo.faceWScale) && Utils.doubleEquals(this.faceHScale, faceInfo.faceHScale) && this.faceRegionOrientation == faceInfo.faceRegionOrientation;
        }

        public int hashCode() {
            return Objects.hash(Double.valueOf(this.eyeLeftXScale), Double.valueOf(this.eyeLeftYScale), Double.valueOf(this.eyeRightXScale), Double.valueOf(this.eyeRightYScale), Double.valueOf(this.faceXScale), Double.valueOf(this.faceYScale), Double.valueOf(this.faceWScale), Double.valueOf(this.faceHScale), Integer.valueOf(this.faceRegionOrientation));
        }

        public static boolean equals(FaceInfo faceInfo, FaceInfo faceInfo2) {
            if (faceInfo == null) {
                return faceInfo2 == null;
            }
            return faceInfo.equals(faceInfo2);
        }

        public static FaceInfo from(String str) throws JSONException {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            JSONObject jSONObject = new JSONObject(str);
            return new FaceInfo(jSONObject.getDouble("eyeLeftXScale"), jSONObject.getDouble("eyeLeftYScale"), jSONObject.getDouble("eyeRightXScale"), jSONObject.getDouble("eyeRightYScale"), jSONObject.getDouble("faceXScale"), jSONObject.getDouble("faceYScale"), jSONObject.getDouble("faceWScale"), jSONObject.getDouble("faceHScale"), jSONObject.getInt("faceOrientation"));
        }
    }

    public ThumbnailInfo(long j, boolean z, String str) {
        this.mGroupLocalId = j;
        this.mIsSharerAlbum = z;
        initBy(str);
    }

    public final void initBy(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            this.mCoverImageId = jSONObject.optString("coverImageId");
            this.mBgImageId = jSONObject.optString("backgroundImageId");
            this.mBgImageLocalId = jSONObject.optLong("backgroundImageLocalId", -1L);
            this.mFaceId = jSONObject.optString("faceId");
            this.mCoverUrl = jSONObject.optString("coverUrl");
            this.mFaceUrl = jSONObject.optString("faceUrl");
            this.mBgUrl = jSONObject.optString("backgroundUrl");
            String optString = jSONObject.optString("faceInfo");
            if (!TextUtils.isEmpty(optString)) {
                JSONObject jSONObject2 = new JSONObject(optString);
                JSONObject optJSONObject = jSONObject.optJSONObject("faceExif");
                int i = -1;
                if (optJSONObject != null) {
                    i = optJSONObject.optInt("orientation", -1);
                }
                jSONObject2.put("faceOrientation", i);
                optString = jSONObject2.toString();
            }
            this.mFaceInfo = FaceInfo.from(optString);
            this.mLastTimeRequest = jSONObject.optLong("lastTimeRequest");
        } catch (JSONException e) {
            Log.e("ThumbnailInfo", "fail to parse ThumbnailInfo from " + str, e);
        }
    }

    public boolean setFaceId(String str) {
        if (!TextUtils.equals(this.mFaceId, str)) {
            this.mFaceId = str;
            this.mFaceUrl = null;
            this.mFaceInfo = null;
            return true;
        }
        return false;
    }

    public String getBgPath() {
        long j = this.mBgImageLocalId;
        if (j == -1 && !TextUtils.isEmpty(this.mBgImageId)) {
            j = CloudUtils.getItemId(GalleryCloudUtils.CLOUD_URI, "serverId", this.mBgImageId);
        }
        Cursor cursor = null;
        try {
            cursor = getItemCursor(GalleryCloudUtils.CLOUD_URI, j.c, String.valueOf(j));
            if (cursor == null || !cursor.moveToNext()) {
            }
            String itemPath = getItemPath(cursor);
            cursor.close();
            return itemPath;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String setBgImage(long j) {
        String valueOf = String.valueOf(-1001L);
        Cursor itemCursor = getItemCursor(GalleryCloudUtils.CLOUD_URI, j.c, String.valueOf(j));
        if (itemCursor == null || itemCursor.getCount() == 0) {
            if (itemCursor == null) {
                return "";
            }
            itemCursor.close();
            return "";
        }
        itemCursor.moveToNext();
        ContentValues contentValues = new ContentValues();
        int i = itemCursor.getInt(itemCursor.getColumnIndex("localFlag"));
        if (i != 0 && i != 5 && i != 6) {
            Log.d("ThumbnailInfo", "no server id, just insert as manual create");
            contentValues.put("localFlag", (Integer) (-2));
        } else {
            Log.d("ThumbnailInfo", "server id found, insert as copy from cloud");
            contentValues.put("localFlag", (Integer) 6);
        }
        contentValues.put("microthumbfile", itemCursor.getString(itemCursor.getColumnIndex("microthumbfile")));
        contentValues.put("thumbnailFile", itemCursor.getString(itemCursor.getColumnIndex("thumbnailFile")));
        contentValues.put("localFile", itemCursor.getString(itemCursor.getColumnIndex("localFile")));
        contentValues.put("localGroupId", valueOf);
        contentValues.put("localImageId", Integer.valueOf(itemCursor.getInt(itemCursor.getColumnIndex(j.c))));
        String itemPath = getItemPath(itemCursor);
        itemCursor.close();
        long insert = GalleryDBHelper.getInstance().getWritableDatabase().insert("cloud", 0, contentValues);
        Log.w("ThumbnailInfo", "mediaId1 : " + insert);
        if (insert > 0) {
            long j2 = this.mBgImageLocalId;
            if (j2 != -1 && !CloudUtils.deleteItemInHiddenAlbum(j2)) {
                Log.w("ThumbnailInfo", "failed to delete old bg, old local id: " + this.mBgImageLocalId);
            }
            this.mBgImageLocalId = insert;
            this.mBgImageId = null;
            this.mBgUrl = null;
            saveToDB();
        }
        return itemPath;
    }

    public String getItemPath(Cursor cursor) {
        String string = cursor.getString(cursor.getColumnIndex("localFile"));
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        String string2 = cursor.getString(cursor.getColumnIndex("thumbnailFile"));
        return !TextUtils.isEmpty(string2) ? string2 : cursor.getString(cursor.getColumnIndex("microthumbfile"));
    }

    public final boolean userChanged() {
        return !TextUtils.isEmpty(this.mCoverImageId) || !TextUtils.isEmpty(this.mFaceId) || !TextUtils.isEmpty(this.mBgImageId) || this.mBgImageLocalId != -1;
    }

    public final void saveToDB() {
        ContentValues contentValues;
        String genUpdateAlbumExtraInfoSql;
        String str = this.mIsSharerAlbum ? "thumbnailInfo" : CallMethod.ARG_EXTRA_STRING;
        if (userChanged()) {
            if (this.mIsSharerAlbum) {
                genUpdateAlbumExtraInfoSql = DatabaseUtils.sqlEscapeString(toString());
                contentValues = null;
            } else {
                contentValues = new ContentValues(1);
                contentValues.put("thumbnailInfo", toString());
                genUpdateAlbumExtraInfoSql = AlbumDataHelper.genUpdateAlbumExtraInfoSql(contentValues, false);
            }
            boolean z = this.mIsSharerAlbum;
            String str2 = z ? "shareAlbum" : "album";
            String transformToEditedColumnsElement = GalleryCloudUtils.transformToEditedColumnsElement(z ? 29 : 23);
            GalleryUtils.safeExec(String.format(Locale.US, "update %s set %s=%s, %s=coalesce(replace(%s, '%s', '') || '%s', '%s') where %s=%d", str2, str, genUpdateAlbumExtraInfoSql, "editedColumns", "editedColumns", transformToEditedColumnsElement, transformToEditedColumnsElement, transformToEditedColumnsElement, j.c, Long.valueOf(this.mGroupLocalId)));
            AlbumCacheManager.getInstance().update(this.mGroupLocalId, contentValues);
        } else {
            ContentValues contentValues2 = new ContentValues();
            if (this.mIsSharerAlbum) {
                contentValues2.put(str, toString());
            } else {
                contentValues2.put(str, DatabaseUtils.sqlEscapeString(toString()));
            }
            GalleryUtils.safeUpdate(this.mIsSharerAlbum ? GalleryCloudUtils.SHARE_ALBUM_URI : GalleryCloudUtils.ALBUM_URI, contentValues2, String.format(Locale.US, "%s=?", j.c), new String[]{String.valueOf(this.mGroupLocalId)});
        }
        GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.Album.URI_CACHE, (ContentObserver) null, true);
        GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.Album.URI, (ContentObserver) null, true);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ThumbnailInfo)) {
            return false;
        }
        ThumbnailInfo thumbnailInfo = (ThumbnailInfo) obj;
        return TextUtils.equals(this.mCoverImageId, thumbnailInfo.mCoverImageId) && TextUtils.equals(this.mBgImageId, thumbnailInfo.mBgImageId) && this.mBgImageLocalId == thumbnailInfo.mBgImageLocalId && TextUtils.equals(this.mFaceId, thumbnailInfo.mFaceId) && TextUtils.equals(this.mCoverUrl, thumbnailInfo.mCoverUrl) && TextUtils.equals(this.mFaceUrl, thumbnailInfo.mFaceUrl) && TextUtils.equals(this.mBgUrl, thumbnailInfo.mBgUrl) && FaceInfo.equals(this.mFaceInfo, thumbnailInfo.mFaceInfo);
    }

    public int hashCode() {
        return Objects.hash(this.mCoverImageId, this.mBgImageId, Long.valueOf(this.mBgImageLocalId), this.mFaceId, this.mCoverUrl, this.mFaceUrl, this.mFaceInfo, this.mBgUrl);
    }

    public static String getFaceByFaceId(String str, FaceRegionRectF[] faceRegionRectFArr) {
        return FaceManager.queryCoverImageOfOneFace(str, faceRegionRectFArr);
    }

    public String getFaceInfo(boolean z) {
        String str;
        boolean z2 = false;
        if (z || TextUtils.isEmpty(this.mFaceId)) {
            str = null;
        } else {
            FaceRegionRectF[] faceRegionRectFArr = new FaceRegionRectF[1];
            str = getFaceByFaceId(this.mFaceId, faceRegionRectFArr);
            this.mFaceRegion = faceRegionRectFArr[0];
        }
        if (str == null) {
            boolean isEmpty = TextUtils.isEmpty(this.mFaceUrl);
            if (!z ? !(!isEmpty || !thumbnailInfoTimeout()) : !(!isEmpty && !thumbnailInfoTimeout())) {
                z2 = true;
            }
            if (z2) {
                getThumbnailInfoFromServer();
            }
            if (TextUtils.isEmpty(this.mFaceUrl)) {
                return null;
            }
            return this.mFaceUrl;
        }
        return str;
    }

    public void setmFaceRegion(FaceRegionRectF faceRegionRectF) {
        this.mFaceRegion = faceRegionRectF;
    }

    public FaceRegionRectF getFaceRegion() {
        return this.mFaceRegion;
    }

    public FaceRegionRectF getFaceRegionFromFaceInfo() {
        FaceInfo faceInfo = this.mFaceInfo;
        if (faceInfo == null) {
            return null;
        }
        return faceInfo.getFacePosition();
    }

    public final boolean thumbnailInfoTimeout() {
        return System.currentTimeMillis() - this.mLastTimeRequest > 86400000;
    }

    public final void getThumbnailInfoFromServer() {
        String serverId;
        String valueOf = String.valueOf(this.mGroupLocalId);
        if (this.mIsSharerAlbum) {
            DBShareAlbum dBShareAlbumByLocalId = CloudUtils.getDBShareAlbumByLocalId(valueOf);
            if (dBShareAlbumByLocalId != null) {
                serverId = dBShareAlbumByLocalId.getAlbumId();
            }
            serverId = null;
        } else {
            DBImage item = CloudUtils.getItem(GalleryCloudUtils.CLOUD_URI, GalleryApp.sGetAndroidContext(), j.c, valueOf);
            if (item != null) {
                serverId = item.getServerId();
            }
            serverId = null;
        }
        if (TextUtils.isEmpty(serverId)) {
            return;
        }
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            Log.e("ThumbnailInfo", "cta not allowed");
            return;
        }
        AccountCache.AccountInfo accountInfo = AccountCache.getAccountInfo();
        if (accountInfo == null) {
            Log.e("ThumbnailInfo", "accountInfo is null");
            return;
        }
        String thumbnailInfoUrl = CloudGroupUrlProvider.getUrlProvider(this.mIsSharerAlbum).getThumbnailInfoUrl(GalleryCloudUtils.getAccountName(), serverId);
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.add(new BasicNameValuePair("sharedAlbumId", serverId));
            JSONObject fromXiaomi = CloudUtils.getFromXiaomi(thumbnailInfoUrl, arrayList, accountInfo.mAccount, accountInfo.mToken, 0, false);
            Log.d("ThumbnailInfo", "get thumbnail info from server: " + fromXiaomi);
            if (CheckResult.parseErrorCode(fromXiaomi) != 0) {
                return;
            }
            ThumbnailInfo thumbnailInfo = new ThumbnailInfo(this.mGroupLocalId, this.mIsSharerAlbum, fromXiaomi.getJSONObject("data").toString());
            if (TextUtils.isEmpty(this.mCoverImageId)) {
                this.mCoverUrl = thumbnailInfo.mCoverUrl;
            }
            if (TextUtils.isEmpty(this.mFaceId)) {
                this.mFaceUrl = thumbnailInfo.mFaceUrl;
                this.mFaceInfo = thumbnailInfo.mFaceInfo;
            }
            if (this.mBgImageLocalId == -1) {
                this.mBgUrl = thumbnailInfo.mBgUrl;
                this.mBgImageId = null;
            }
            this.mLastTimeRequest = System.currentTimeMillis();
            saveToDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        try {
            JSONObject jSONObject = new JSONObject();
            putString(jSONObject, "coverImageId", this.mCoverImageId);
            putString(jSONObject, "backgroundImageId", this.mBgImageId);
            putString(jSONObject, "faceId", this.mFaceId);
            putString(jSONObject, "coverUrl", this.mCoverUrl);
            putString(jSONObject, "faceUrl", this.mFaceUrl);
            putString(jSONObject, "backgroundUrl", this.mBgUrl);
            FaceInfo faceInfo = this.mFaceInfo;
            jSONObject.put("faceInfo", faceInfo == null ? null : faceInfo.toJSON());
            long j = this.mBgImageLocalId;
            if (j != -1) {
                jSONObject.put("backgroundImageLocalId", j);
            }
            long j2 = this.mLastTimeRequest;
            if (j2 > 0) {
                jSONObject.put("lastTimeRequest", j2);
            }
            return jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "null";
        }
    }

    public final void putString(JSONObject jSONObject, String str, String str2) throws JSONException {
        if (!TextUtils.isEmpty(str2)) {
            jSONObject.put(str, str2);
        }
    }

    public static Cursor getItemCursor(Uri uri, String str, String str2) {
        ContentResolver contentResolver = GalleryApp.sGetAndroidContext().getContentResolver();
        Uri limitUri = CloudUtils.getLimitUri(uri, 1);
        String[] strArr = {Marker.ANY_MARKER};
        return contentResolver.query(limitUri, strArr, str + "=?", new String[]{str2}, null);
    }
}
