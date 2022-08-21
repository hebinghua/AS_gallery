package com.miui.gallery.cloud;

import android.text.TextUtils;
import com.miui.gallery.util.Utils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ThumbnailInfo {
    public String mBgImageId;
    public long mBgImageLocalId = -1;
    public String mBgUrl;
    public String mCoverImageId;
    public String mCoverUrl;
    public String mFaceId;
    public FaceInfo mFaceInfo;
    public String mFaceUrl;
    public final int mGroupLocalId;
    public final boolean mIsSharerAlbum;
    public long mLastTimeRequest;

    /* loaded from: classes.dex */
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

    public ThumbnailInfo(int i, boolean z, String str) {
        this.mGroupLocalId = i;
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
            DefaultLogger.e("ThumbnailInfo", "fail to parse ThumbnailInfo from " + str, e);
        }
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

    public String getFaceId() {
        return this.mFaceId;
    }

    public long getBgImageLocalId() {
        return this.mBgImageLocalId;
    }
}
